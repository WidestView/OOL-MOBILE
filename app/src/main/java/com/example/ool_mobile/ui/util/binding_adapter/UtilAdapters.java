package com.example.ool_mobile.ui.util.binding_adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;

import java.util.List;
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

    @BindingAdapter("app:items")
    public static void setItems(
            @NonNull AutoCompleteTextView textView,
            @Nullable List<String> items
    ) {
        Objects.requireNonNull(textView, "textView is null");

        if (items != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    textView.getContext(),
                    R.layout.list_item, items
            );

            textView.setAdapter(adapter);
        }
    }
}
