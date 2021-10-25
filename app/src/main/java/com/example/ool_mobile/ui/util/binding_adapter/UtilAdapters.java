package com.example.ool_mobile.ui.util.binding_adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class UtilAdapters {

    @BindingAdapter("app:fixedLinear")
    public static void setRecyclerViewFixedLinear(@NonNull RecyclerView view, boolean isFixedLinear) {

        Objects.requireNonNull(view, "view is null");

        if (isFixedLinear) {

            view.setHasFixedSize(true);

            Context context = view.getContext();

            Objects.requireNonNull(context, "recyclerview context is null");

            view.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        }
    }

    @BindingAdapter("adapter")
    public static void setRecyclerViewAdapter(
            @NonNull RecyclerView recyclerView,
            @Nullable RecyclerView.Adapter<?> adapter) {

        recyclerView.setAdapter(adapter);
    }

}
