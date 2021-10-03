package com.example.ool_mobile.ui.util.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.ui.component.ContentRow;

import org.immutables.value.Value;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
public abstract class AdapterParameters<Model> {

    @NonNull
    public abstract List<Model> items();

    @Nullable
    public abstract Consumer<Model> onEdit();

    @Nullable
    public abstract Consumer<Model> onDelete();

    public void bindRowEvents(@NonNull ContentRow contentRow, @NonNull Model model) {

        Objects.requireNonNull(contentRow, "contentRow is null");

        Objects.requireNonNull(model, "model is null");

        Consumer<Model> onEdit = onEdit();

        if (onEdit == null) {
            contentRow.disableEditButtonView();
        } else {
            contentRow.getEditView().setOnClickListener(v ->
                    onEdit.accept(model)
            );
        }

        Consumer<Model> onDelete = onDelete();

        if (onDelete == null) {
            contentRow.disableDeleteButtonView();
        } else {
            contentRow.getDeleteView().setOnClickListener(v ->
                    onDelete.accept(model)
            );
        }
    }

    public static class Builder<Model> extends ImmutableAdapterParameters.Builder<Model> {

    }
}
