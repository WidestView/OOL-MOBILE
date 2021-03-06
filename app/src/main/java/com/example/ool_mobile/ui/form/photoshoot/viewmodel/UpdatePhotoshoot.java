package com.example.ool_mobile.ui.form.photoshoot.viewmodel;

import androidx.annotation.NonNull;

import com.example.ool_mobile.ui.form.photoshoot.PhotoshootInput;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormOperation;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class UpdatePhotoshoot implements FormOperation<PhotoshootInput> {

    private final PhotoshootViewModel viewModel;

    private final UUID resourceId;

    public UpdatePhotoshoot(
            @NonNull PhotoshootViewModel photoshootViewModel,
            @NonNull UUID resourceId) {

        this.viewModel = photoshootViewModel;

        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public FormMode getFormMode() {
        return FormMode.Update;
    }

    @NonNull
    @Override
    public Single<PhotoshootInput> getInput() {

        return viewModel.photoshootApi.
                getPhotoshootWithId(resourceId)
                .delay(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(PhotoshootInput::new);

    }

    @Override
    public Completable savePhotoshoot() {

        PhotoshootInput input = viewModel.getInput().getValue();

        if (input == null) {
            return Completable.complete();
        }

        return Completable.fromMaybe(
                viewModel.validation.validate(input)
                        .flatMapSingle(result ->
                                viewModel.photoshootApi.updatePhotoshoot(resourceId, result))
        );
    }
}
