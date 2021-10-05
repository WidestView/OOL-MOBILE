package com.example.ool_mobile.ui.form.add_photo_shoot;

import androidx.annotation.NonNull;

import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.api.PhotoshootApi;

public class UpdatePhotoshootViewModel extends PhotoshootFormViewModel {

    @NonNull
    private PhotoshootApi photoshootApi;

    protected UpdatePhotoshootViewModel(@NonNull PhotoshootApi photoshootApi) {
        this.photoshootApi = photoshootApi;
    }

    @Override
    protected void savePhotoshoot(@NonNull Photoshoot photoshoot) {

        // todo: implement this
    }
}
