package com.example.ool_mobile.ui.home;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class OptionItem {

    @NonNull
    private final String itemName;

    @NonNull
    private final Drawable imageIcon;

    public OptionItem(@NonNull String nameOfItem, @NonNull Drawable imageIcon) {
        this.itemName = nameOfItem;
        this.imageIcon = imageIcon;
    }

    @NonNull
    public String getItemName() {
        return itemName;
    }

    @NonNull
    public Drawable getImageIcon() {
        return imageIcon;
    }
}
