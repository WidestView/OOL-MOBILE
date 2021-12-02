package com.example.ool_mobile.ui.home;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.misc.BuilderOnly;

import org.immutables.value.Value;

@Value.Immutable
@BuilderOnly
public interface OptionItem {

    @NonNull
    String getItemName();

    @NonNull
    Drawable getImageIcon();

    @NonNull
    String getContentDescription();

    @Nullable
    View.OnClickListener getClickListener();
}
