package com.example.ool_mobile.ui.form.photoshoot.viewmodel;

import androidx.annotation.NonNull;

import com.example.ool_mobile.ui.form.photoshoot.PhotoshootInput;
import com.example.ool_mobile.ui.util.form.FormMode;

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

        return viewModel.photoshootApi.getPhotoshootWithId(resourceId)
                .map(PhotoshootInput::new);

    }

    @Override
    public Completable savePhotoshoot() {

        return Completable
                .timer(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .doOnComplete(() -> {

                    // todo: implement this

                    if (Math.random() < 0.5) {
                        viewModel.events.onNext(PhotoshootViewModel.Event.Error);
                    } else {
                        viewModel.events.onNext(PhotoshootViewModel.Event.Success);
                    }
                });
    }
}
