package com.example.ool_mobile.ui.component.content_row;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import java.util.Objects;

public class ContentRowAdapters {

    @BindingAdapter("app:titleText")
    public static void contentRowTitleText(@NonNull ContentRow contentRow, @NonNull String text) {

        Objects.requireNonNull(contentRow, "contentRow is null");
        Objects.requireNonNull(text, "text is null");

        contentRow.getTitleTextView().setText(text);
    }

    @BindingAdapter("app:statusText")
    public static void contentRowStatusText(@NonNull ContentRow contentRow, @NonNull String text) {

        Objects.requireNonNull(contentRow, "contentRow is null");
        Objects.requireNonNull(text, "text is null");

        contentRow.getStatusTextView().setText(text);
    }

    @BindingAdapter("app:value")
    public static void contentRowFieldValue(
            @NonNull ContentRowField view,
            @NonNull String text
    ) {

        Objects.requireNonNull(view, "view is null");
        Objects.requireNonNull(text, "text is null");

        view.getValueTextView().setText(text);
    }

    @BindingAdapter("app:events")
    public static void bindEvents(
            @NonNull ContentRow contentRow,
            @NonNull ContentRowEvents events
    ) {
        Objects.requireNonNull(contentRow, "contentRow is null");
        Objects.requireNonNull(events, "events is null");

        bindOnEdit(contentRow, events.getOnEdit());
        bindOnDelete(contentRow, events.getOnDelete());
    }

    @BindingAdapter("app:on_edit")
    public static void bindOnEdit(
            @NonNull ContentRow contentRow,
            @Nullable View.OnClickListener listener
    ) {

        Objects.requireNonNull(contentRow, "contentRow is null");

        if (listener == null) {
            contentRow.disableEditButtonView();
        } else {
            contentRow.getEditView().setOnClickListener(listener);
        }

    }

    @BindingAdapter("app:on_delete")
    public static void bindOnDelete(
            @NonNull ContentRow contentRow,
            @Nullable View.OnClickListener listener
    ) {
        Objects.requireNonNull(contentRow, "contentRow is null");

        if (listener == null) {
            contentRow.disableDeleteButtonView();
        } else {
            contentRow.getDeleteView().setOnClickListener(listener);
        }
    }
}
