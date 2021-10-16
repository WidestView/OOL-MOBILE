package com.example.ool_mobile.ui.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class UtilAdapters {


    @BindingAdapter("app:fixedLinear")
    public static void recyclerViewFixedLinear(@NonNull RecyclerView view, boolean isFixed) {

        Objects.requireNonNull(view, "view is null");

        if (isFixed) {

            view.setHasFixedSize(true);

            Context context = view.getContext();

            Objects.requireNonNull(context, "recyclerview context is null");

            view.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        }
    }

    @BindingAdapter("adapter")
    public static void adapter(
            @NonNull RecyclerView recyclerView,
            @Nullable RecyclerView.Adapter<?> adapter) {

        recyclerView.setAdapter(adapter);
    }


}
