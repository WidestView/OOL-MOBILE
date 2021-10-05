package com.example.ool_mobile.ui.form.add_photo_shoot;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.api.PhotoshootApi;
import com.example.ool_mobile.ui.util.ViewModelFactory;

public class AddPhotoshootViewModel extends PhotoshootFormViewModel {

    private final PhotoshootApi photoshootApi;

    protected AddPhotoshootViewModel(@NonNull PhotoshootApi photoshootApi) {
        this.photoshootApi = photoshootApi;
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull PhotoshootApi api) {
        return ViewModelFactory.create(
                AddPhotoshootViewModel.class,
                () -> new AddPhotoshootViewModel(api)
        );
    }

    @Override
    protected void savePhotoshoot(@NonNull Photoshoot photoshoot) {

        subscriptions.add(
                photoshootApi.addPhotoshoot(photoshoot)
                        .subscribe(
                                success -> eventObserver().onNext(Event.Success),
                                error -> {
                                    error.printStackTrace();
                                    eventObserver().onNext(Event.Error);
                                }
                        )
        );
    }
}
