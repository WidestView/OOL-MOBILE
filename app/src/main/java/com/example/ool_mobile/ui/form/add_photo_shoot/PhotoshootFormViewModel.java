package com.example.ool_mobile.ui.form.add_photo_shoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

@Value.Immutable(copy = false)
interface PhotoshootInput {

    @NonNull
    String orderId();

    @NonNull
    String address();

    @Nullable
    FormTime startTime();

    @Nullable
    FormTime endTime();

    @Nullable
    Long date();
}

public class PhotoshootFormViewModel extends SubscriptionViewModel {

    @NonNull
    private final Subject<Event> events = PublishSubject.create();
    private final FormMode formMode;
    @NonNull
    private final PhotoshootApi photoshootApi;

    private PhotoshootFormViewModel(@NonNull FormMode formMode, @NonNull PhotoshootApi photoshootApi) {
        this.formMode = formMode;
        this.photoshootApi = photoshootApi;
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull FormMode formMode, @NonNull PhotoshootApi api) {
        return ViewModelFactory.create(
                PhotoshootFormViewModel.class,
                () -> new PhotoshootFormViewModel(formMode, api)
        );
    }

    @NonNull
    public Observable<Event> getEvents() {
        return events;
    }

    public void savePhotoshoot(@NonNull PhotoshootInput input) {

        Objects.requireNonNull((Object) input, "input is null");

        Photoshoot photoshoot = normalize(input);

        if (photoshoot == null) {
            return;
        }

        formMode.accept(new FormMode.Visitor() {
            @Override
            public void visitAdd() {
                addPhotoshoot(photoshoot);
            }

            public void visitUpdate() {
                updatePhotoshoot(photoshoot);
            }
        });

    }

    private void addPhotoshoot(@NonNull Photoshoot photoshoot) {

        subscriptions.add(
                photoshootApi.addPhotoshoot(photoshoot)
                        .subscribe(
                                success -> events.onNext(Event.Success),
                                error -> {
                                    error.printStackTrace();
                                    events.onNext(Event.Error);
                                }
                        )
        );
    }

    private void updatePhotoshoot(@NonNull Photoshoot photoshoot) {

        // todo: implement update in the api
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

        List<FormCheck<Event>> checks = getChecks(data);

        boolean error = false;

        for (FormCheck<Event> check : checks) {

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

    @NotNull
    private List<FormCheck<Event>> getChecks(PhotoshootInput data) {
        return Arrays.asList(
                FormCheck.failIf(
                        () -> data.address().isEmpty(),
                        Event.EmptyAddress
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
                        Event.InvalidOrder
                ),
                FormCheck.failIf(
                        () -> data.orderId().isEmpty(),
                        Event.EmptyOrder
                ),
                FormCheck.failIf(
                        () -> data.startTime() == null,
                        Event.EmptyStartTime
                ),
                FormCheck.failIf(
                        () -> data.endTime() == null,
                        Event.EmptyEndTime
                ),
                FormCheck.failIf(
                        () -> data.date() == null,
                        Event.EmptyDate
                ),
                FormCheck.failIf(
                        () -> {
                            FormTime end = data.endTime();
                            FormTime start = data.startTime();

                            return end != null &&
                                    start != null &&
                                    totalMinutes(end) < totalMinutes(start);
                        },
                        Event.InvalidTimeRange
                )

        );
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
}
