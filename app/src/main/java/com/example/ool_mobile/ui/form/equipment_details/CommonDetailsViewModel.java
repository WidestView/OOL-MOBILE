package com.example.ool_mobile.ui.form.equipment_details;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.model.EquipmentKind;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

import static io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread;

abstract class CommonDetailsViewModel extends SubscriptionViewModel implements DetailsViewModel {

    protected final Subject<Event> events = PublishSubject.create();

    protected final EquipmentDetailsValidation validation = new EquipmentDetailsValidation(events);

    private Single<List<EquipmentKind>> kindsRequest;

    protected final EquipmentApi api;

    protected CommonDetailsViewModel(EquipmentApi api) {
        this.api = api;
    }

    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();

    @NonNull
    @Override
    public Observable<Event> getEvents() {
        return events;
    }

    @NonNull
    protected Single<List<EquipmentKind>> fetchKinds() {

        return Single.defer(() -> {

            if (kindsRequest == null) {
                kindsRequest = api.listKinds().cache();
            }

            return kindsRequest;
        });
    }


    @Override
    public void setImageBitmap(@NonNull Bitmap bitmap) {
        imageBitmap.setValue(bitmap);
    }

    @NonNull
    @Override
    public LiveData<Bitmap> getImageBitmap() {
        return imageBitmap;
    }

    protected void handleError(Throwable throwable) {
        throwable.printStackTrace();
        events.onNext(Event.Error);
    }

    private MutableLiveData<List<String>> kindNames;

    @NonNull
    public LiveData<List<String>> getKindNames() {

        if (kindNames == null) {
            kindNames = new MutableLiveData<>();

            subscriptions.add(
                    fetchKinds()
                            .flatMapObservable(Observable::fromIterable)
                            .map(EquipmentKind::getName)
                            .toList()
                            .observeOn(mainThread())
                            .subscribe(
                                    kindNames::setValue,
                                    this::handleError
                            )
            );
        }

        return kindNames;
    }

}
