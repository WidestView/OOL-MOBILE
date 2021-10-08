package com.example.ool_mobile.ui.util.adapter;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ool_mobile.ui.component.ContentRowEvents;

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

    @NonNull
    public ContentRowEvents asRowEvents(@NonNull Model model) {

        Objects.requireNonNull(model, "model is null");

        Consumer<Model> onEdit = onEdit();
        Consumer<Model> onDelete = onDelete();

        return new ContentRowEvents() {
            @Nullable
            @Override
            public View.OnClickListener getOnEdit() {
                if (onEdit == null) {
                    return null;
                } else {
                    return v -> onEdit.accept(model);
                }
            }

            @Nullable
            @Override
            public View.OnClickListener getOnDelete() {
                if (onDelete == null) {
                    return null;
                } else {
                    return v -> onDelete.accept(model);
                }
            }
        };
    }

    public static class Builder<Model> extends ImmutableAdapterParameters.Builder<Model> {

    }
}
