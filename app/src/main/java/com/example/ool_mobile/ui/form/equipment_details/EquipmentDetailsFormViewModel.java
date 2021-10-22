package com.example.ool_mobile.ui.form.equipment_details;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.EquipmentKind;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.SubscriptionViewModel;
import com.example.ool_mobile.ui.util.ViewModelFactory;
import com.example.ool_mobile.ui.util.form.FormMode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import static io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread;

public class EquipmentDetailsFormViewModel extends SubscriptionViewModel {

    @NonNull
    private final EquipmentApi api;

    @NonNull
    private final FormMode formMode;

    public EquipmentDetailsFormViewModel(
            @NonNull EquipmentApi api,
            @NonNull FormMode formMode) {
        this.api = api;
        this.formMode = formMode;
    }

    @NonNull
    public LiveData<FormMode> getFormMode() {
        return new MutableLiveData<>(formMode);
    }

    private final Subject<Event> events = PublishSubject.create();

    @NonNull
    public Observable<Event> getEvents() {
        return events;
    }


    private MutableLiveData<List<EquipmentKind>> kinds;

    @NonNull
    public LiveData<List<String>> getKindNames() {

        if (kinds == null) {
            kinds = new MutableLiveData<>();


            subscriptions.add(
                    api.listKinds()
                            .observeOn(mainThread())
                            .subscribe(kinds::setValue, this::handleError)
            );
        }

        return Transformations
                .map(kinds, kinds -> kinds.stream().map(EquipmentKind::getName)
                        .collect(Collectors.toList()));
    }

    @Nullable
    private MutableLiveData<EquipmentDetailsInput> input;

    @NonNull
    public LiveData<EquipmentDetailsInput> getInput() {

        if (input == null) {
            input = new MutableLiveData<>();

            subscriptions.add(
                    api.getDetailsById(1)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(details -> {

                                input.setValue(new EquipmentDetailsInput(details));
                            })
            );
        }

        return input;
    }

    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();

    @NonNull
    public LiveData<Bitmap> getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(@NonNull Bitmap bitmap) {
        Objects.requireNonNull(bitmap, "bitmap is null");

        imageBitmap.setValue(bitmap);
    }


    public void onSave() {

        System.out.println(getInput().getValue());
    }

    private void handleError(Throwable error) {

        error.printStackTrace();

        events.onNext(Event.Error);
    }

    @NonNull
    public static ViewModelProvider.Factory create(
            @NonNull FormMode formMode,
            @NonNull EquipmentApi api,
            @Nullable Integer initialId
    ) {
        Objects.requireNonNull(api, "api is null");

        return ViewModelFactory.create(
                EquipmentDetailsFormViewModel.class,
                () -> new EquipmentDetailsFormViewModel(api, formMode)
        );
    }


    public interface Event {

        Event Error = Visitor::visitError;

        void accept(@NonNull Visitor visitor);

        interface Visitor {
            void visitError();
        }
    }

}
