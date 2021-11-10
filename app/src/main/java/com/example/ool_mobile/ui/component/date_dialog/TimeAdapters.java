package com.example.ool_mobile.ui.component.date_dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.example.ool_mobile.ui.util.form.FormTime;

import java.util.Objects;

public class TimeAdapters {

    @BindingAdapter("dialogTime")
    public static void setDialogTime(@NonNull TimeDialogView view, @NonNull FormTime formTime) {

        Objects.requireNonNull(view, "view is null");

        if (!Objects.equals(view.getContent().getSelection(), formTime)) {
            view.getContent().setSelection(formTime);
        }
    }

    @InverseBindingAdapter(attribute = "dialogTime")
    @Nullable
    public static FormTime getDialogTime(@NonNull TimeDialogView view) {
        return view.getContent().getSelection();
    }


    @BindingAdapter("dialogTimeAttrChanged")
    public static void setDialogTimeEvents(
            @NonNull TimeDialogView view,
            @NonNull InverseBindingListener listener) {
        Objects.requireNonNull(view, "view is null");
        Objects.requireNonNull(listener, "listener is null");

        view.getContent().setSelectionListener(l -> listener.onChange());
    }
}
