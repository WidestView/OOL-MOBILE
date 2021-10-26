package com.example.ool_mobile.ui.form.equipment_details;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ool_mobile.model.EquipmentDetails;
import com.example.ool_mobile.service.api.EquipmentApi;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.image.ImageInputHandler;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

class AddDetailsViewModel extends CommonDetailsViewModel {

    private final MutableLiveData<EquipmentDetailsInput> input = new MutableLiveData<>(
            new EquipmentDetailsInput()
    );

    public AddDetailsViewModel(@NonNull EquipmentApi api) {
        super(api);
    }

    @NonNull
    public FormMode getFormMode() {
        return FormMode.Add;
    }

    @NonNull
    public LiveData<EquipmentDetailsInput> getInput() {
        return input;
    }

    @Override
    public void onSave() {

        fetchKinds()
                .flatMapMaybe(
                        kinds -> validation.validate(input.getValue(), kinds)
                )
                .flatMapSingle(api::addDetails)
                .flatMapCompletable(stuff -> {

                    Bitmap bitmap = getSelectedBitmap().getValue();

                    if (bitmap == null) {
                        return Completable.complete();
                    } else {
                        return postImage(stuff, bitmap);
                    }

                })
                .observeOn(AndroidSchedulers.mainThread())
                .to(disposedWhenCleared())
                .subscribe(() -> {
                    events.onNext(Event.Success);
                });
    }

    @NotNull
    private Completable postImage(EquipmentDetails stuff, Bitmap bitmap) {

        Single<byte[]> image = ImageInputHandler.formatBitmap(bitmap);

        return image.flatMapCompletable(bytes -> {

            RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), bytes);

            MultipartBody.Part part = MultipartBody.Part.createFormData(
                    "file",
                    "image.jpg",
                    body
            );

            return api.postEquipmentImage(stuff.getId(), part);
        });
    }
}
