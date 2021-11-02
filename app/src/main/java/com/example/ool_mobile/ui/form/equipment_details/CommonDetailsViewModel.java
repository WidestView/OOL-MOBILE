package com.example.ool_mobile.ui.form.equipment_details;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.model.EquipmentKind;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.image.ImageUtil;
import com.example.ool_mobile.ui.util.view_model.SubscriptionViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread;

abstract class CommonDetailsViewModel extends SubscriptionViewModel implements DetailsViewModel {

    protected final Subject<Event> events = PublishSubject.create();

    protected final EquipmentDetailsValidation validation = new EquipmentDetailsValidation(events);

    private Single<List<EquipmentKind>> kindsRequest;

    protected final EquipmentApi api;

    protected CommonDetailsViewModel(EquipmentApi api) {
        this.api = api;
    }

    private final MutableLiveData<Bitmap> selectedBitmap = new MutableLiveData<>();

    private MutableLiveData<List<String>> kindNames;

    protected final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    @Override
    public LiveData<Boolean> isLoading() {
        return loading;
    }

    @NonNull
    @Override
    public Observable<Event> getEvents() {
        return events;
    }

    @Override
    public void setSelectedBitmap(@NonNull Bitmap bitmap) {
        selectedBitmap.setValue(bitmap);
    }

    @NonNull
    @Override
    public LiveData<Bitmap> getSelectedBitmap() {
        return selectedBitmap;
    }

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

    @NonNull
    protected Single<List<EquipmentKind>> fetchKinds() {

        return Single.defer(() -> {

            if (kindsRequest == null) {
                kindsRequest = api.listKinds().cache();
            }

            return kindsRequest;
        });
    }

    protected void handleError(Throwable throwable) {
        throwable.printStackTrace();
        events.onNext(Event.Error);
    }

    @NonNull
    protected Completable uploadBitmap(int id) {

        Bitmap bitmap = getSelectedBitmap().getValue();

        if (bitmap == null) {
            return Completable.complete();
        } else {
            return postImage(id, bitmap);
        }
    }


    @NotNull
    private Completable postImage(int id, Bitmap bitmap) {

        Single<byte[]> image = ImageUtil.decodeBitmap(bitmap);

        return image.flatMapCompletable(bytes -> {

            RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), bytes);

            MultipartBody.Part part = MultipartBody.Part.createFormData(
                    "file",
                    "image.jpg",
                    body
            );

            return api.postEquipmentImage(id, part);
        });
    }

}
