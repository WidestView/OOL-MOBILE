package com.example.ool_mobile.ui.form.photoshoot.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.form.photoshoot.PhotoshootInput;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormOperation;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.view_model.ViewModelFactory;

import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class PhotoshootViewModel extends SubscriptionViewModel {

    private FormOperation<PhotoshootInput> operation;

    private MutableLiveData<PhotoshootInput> input;

    final PhotoshootApi photoshootApi;

    final Subject<Event> events = PublishSubject.create();

    final PhotoshootValidation validation = new PhotoshootValidation(events);

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);


    private PhotoshootViewModel(PhotoshootApi api, FormMode formMode, UUID initialId) {

        photoshootApi = api;

        formMode.accept(
                new FormMode.Visitor() {
                    @Override
                    public void visitAdd() {
                        operation = new AddPhotoshoot(PhotoshootViewModel.this);
                    }

                    @Override
                    public void visitUpdate() {

                        Objects.requireNonNull(initialId, "initialId is null");

                        operation = new UpdatePhotoshoot(
                                PhotoshootViewModel.this,
                                initialId
                        );
                    }
                }
        );

        if (operation == null) {
            throw new UnsupportedOperationException();
        }

    }


    @NonNull
    public Observable<Event> getEvents() {
        return events;
    }


    @NonNull
    public LiveData<PhotoshootInput> getInput() {

        if (input == null) {
            input = new MutableLiveData<>();

            operation.getInput()
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(disposedWhenCleared())
                    .subscribe(value -> {
                        this.input.setValue(value);
                        this.isLoading.setValue(false);
                    });
        }

        return input;
    }

    @NonNull
    public FormMode getFormMode() {
        return operation.getFormMode();
    }

    @NonNull
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public void savePhotoshoot() {

        isLoading.setValue(true);

        operation.savePhotoshoot()
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe(() -> {
                    isLoading.setValue(false);
                }, this::handleError);
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        events.onNext(Event.Error);
    }

    public interface Event {

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

    @NonNull
    public static ViewModelProvider.Factory create(
            @NonNull FormMode formMode,
            @NonNull PhotoshootApi photoshootApi,
            @Nullable UUID resourceId
    ) {

        return ViewModelFactory.create(PhotoshootViewModel.class, () -> {

            return new PhotoshootViewModel(photoshootApi, formMode, resourceId);
        });

    }

}
