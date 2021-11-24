package com.example.ool_mobile.ui.component.date_dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.util.Date;
import java.util.Objects;

public class DateAdapters {

    @InverseBindingAdapter(attribute = "dialogDate")
    @Nullable
    public static Date getDialogDate(@NonNull DateDialogView view) {

        Objects.requireNonNull(view, "view is null");

        return view.getContent().getSelection();
    }

    @BindingAdapter("dialogDate")
    public static void setDialogDate(@NonNull DateDialogView view, @Nullable Date date) {

        Objects.requireNonNull(view, "view is null");

        if (!Objects.equals(date, view.getContent().getSelection())) {
            view.getContent().setSelection(date);
        }
    }

    @BindingAdapter("dialogDateAttrChanged")
    public static void setDateListeners(
            @NonNull DateDialogView view,
            @NonNull InverseBindingListener changeListener
    ) {
        Objects.requireNonNull(view, "view is null");
        Objects.requireNonNull(changeListener, "changeListener is null");

        view.getContent().setSelectionListener(l -> changeListener.onChange());
    }


}
