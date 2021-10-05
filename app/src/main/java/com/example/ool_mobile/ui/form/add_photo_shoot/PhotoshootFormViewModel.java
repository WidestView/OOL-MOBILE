package com.example.ool_mobile.ui.form.add_photo_shoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.ImmutablePhotoshoot;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.util.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.ViewModelFactory;
import com.example.ool_mobile.ui.util.form.CheckResult;
import com.example.ool_mobile.ui.util.form.FormCheck;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormTime;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public abstract class PhotoshootFormViewModel extends SubscriptionViewModel {

    @NonNull
    private final Subject<Event> events = PublishSubject.create();

    private final MutableLiveData<FormMode> formMode = new MutableLiveData<>();

    @NonNull
    public LiveData<FormMode> getFormMode() {
        return formMode;
    }

    @NonNull
    public static ViewModelProvider.Factory create(
            @NonNull PhotoshootApi photoshootApi,
            @NonNull FormMode formMode,
            @Nullable UUID resourceId
    ) {

        return ViewModelFactory.create(
                PhotoshootFormViewModel.class,
                () -> {

                    AtomicReference<PhotoshootFormViewModel> result = new AtomicReference<>();

                    formMode.accept(new FormMode.Visitor() {
                        @Override
                        public void visitAdd() {
                            result.set(new AddPhotoshootViewModel(photoshootApi));
                        }

                        @Override
                        public void visitUpdate() {
                            result.set(new UpdatePhotoshootViewModel(photoshootApi, resourceId));
                        }
                    });

                    if (result.get() == null) {
                        throw new UnsupportedOperationException();
                    }

                    result.get().formMode.setValue(formMode);

                    return result.get();
                }
        );
    }


    @NonNull
    public Observable<Event> getEvents() {
        return events;
    }

    @NonNull
    protected Observer<Event> eventObserver() {
        return events;
    }

    public void savePhotoshoot(@NonNull PhotoshootInput input) {

        Objects.requireNonNull((Object) input, "input is null");

        Photoshoot photoshoot = normalize(input);

        if (photoshoot == null) {
            return;
        }

        savePhotoshoot(photoshoot);
    }

    protected abstract void savePhotoshoot(@NonNull Photoshoot photoshoot);

    private int getDuration(PhotoshootInput data) {
        return (int) (totalMinutes(data.endTime()) - totalMinutes(data.startTime()));
    }

    @NotNull
    private Date getDate(PhotoshootInput data) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date(data.date()));

        calendar.add(Calendar.HOUR, (int) data.startTime().getHour());
        calendar.add(Calendar.MINUTE, (int) data.startTime().getMinute());

        return calendar.getTime();
    }

    @Nullable
    private Photoshoot normalize(@NonNull final PhotoshootInput input) {

        final PhotoshootInput data = ImmutablePhotoshootInput.builder()
                .orderId(input.orderId().trim())
                .address(input.address().trim())
                .startTime(input.startTime())
                .endTime(input.endTime())
                .date(input.date())
                .build();

        List<FormCheck<PhotoshootFormViewModel.Event>> checks = getChecks(data);

        boolean error = false;

        for (FormCheck<PhotoshootFormViewModel.Event> check : checks) {

            if (check.performCheck() == CheckResult.Failure) {
                events.onNext(check.getError());
                error = true;
            }
        }

        if (error) {
            return null;
        }

        return ImmutablePhotoshoot.builder()
                .resourceId(UUID.randomUUID())
                .address(data.address())
                .orderId(Integer.parseInt(data.orderId()))
                .startTime(getDate(data))
                .durationMinutes(getDuration(data))
                .build();
    }

    long totalMinutes(@NonNull FormTime formTime) {
        return formTime.getHour() * 60 + formTime.getMinute();
    }

    interface Event {

        Event Success = Visitor::visitSuccess;
        Event Error = Visitor::visitError;
        Event EmptyAddress = Visitor::visitEmptyAddress;
        Event InvalidTimeRange = Visitor::visitInvalidTimeRange;
        Event EmptyOrder = Visitor::visitEmptyOrder;
        Event EmptyStartTime = Visitor::visitEmptyStartTime;
        Event EmptyEndTime = Visitor::visitEmptyEndTime;
        Event InvalidOrder = Visitor::visitInvalidOrder;
        Event EmptyDate = Visitor::visitEmptyDate;

        void accept(@NonNull Visitor visitor);

        interface Visitor {
            void visitSuccess();

            void visitError();

            void visitEmptyAddress();

            void visitInvalidTimeRange();

            void visitEmptyOrder();

            void visitEmptyStartTime();

            void visitEmptyEndTime();

            void visitInvalidOrder();

            void visitEmptyDate();
        }
    }

    @NotNull
    private List<FormCheck<PhotoshootFormViewModel.Event>> getChecks(PhotoshootInput data) {
        return Arrays.asList(
                FormCheck.failIf(
                        () -> data.address().isEmpty(),
                        PhotoshootFormViewModel.Event.EmptyAddress
                ),
                FormCheck.succeedIf(
                        () -> {
                            try {
                                Integer.parseInt(data.orderId());
                                return true;
                            } catch (NumberFormatException ex) {
                                return false;
                            }
                        },
                        PhotoshootFormViewModel.Event.InvalidOrder
                ),
                FormCheck.failIf(
                        () -> data.orderId().isEmpty(),
                        PhotoshootFormViewModel.Event.EmptyOrder
                ),
                FormCheck.failIf(
                        () -> data.startTime() == null,
                        PhotoshootFormViewModel.Event.EmptyStartTime
                ),
                FormCheck.failIf(
                        () -> data.endTime() == null,
                        PhotoshootFormViewModel.Event.EmptyEndTime
                ),
                FormCheck.failIf(
                        () -> data.date() == null,
                        PhotoshootFormViewModel.Event.EmptyDate
                ),
                FormCheck.failIf(
                        () -> {
                            FormTime end = data.endTime();
                            FormTime start = data.startTime();

                            return end != null &&
                                    start != null &&
                                    totalMinutes(end) < totalMinutes(start);
                        },
                        PhotoshootFormViewModel.Event.InvalidTimeRange
                )

        );
    }
}
