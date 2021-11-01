package com.example.ool_mobile.ui.form.photoshoot.viewmodel;

import androidx.annotation.NonNull;

import com.example.ool_mobile.ui.form.photoshoot.PhotoshootInput;
import com.example.ool_mobile.ui.util.form.FormMode;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

class AddPhotoshoot implements FormOperation<PhotoshootInput> {

    private final PhotoshootViewModel viewModel;

    public AddPhotoshoot(PhotoshootViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public FormMode getFormMode() {
        return FormMode.Add;
    }

    @NonNull
    @Override
    public Single<PhotoshootInput> getInput() {
        return Single.just(new PhotoshootInput());
    }

    @Override
    public Completable savePhotoshoot() {

        return Completable.fromMaybe(

                viewModel.validation.validate(viewModel.getInput().getValue())
                        .flatMapSingle(viewModel.photoshootApi::addPhotoshoot)
                        .doOnSuccess(result -> {
                            viewModel.events.onNext(PhotoshootViewModel.Event.Success);
                        })
        );

    }
}
