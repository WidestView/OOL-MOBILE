package com.example.ool_mobile.ui.util.binding_adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Objects;

public class UtilAdapters {

    @BindingAdapter("app:fixedLinear")
    public static void setRecyclerViewFixedLinear(@NonNull RecyclerView view, boolean isFixedLinear) {

        if (isFixedLinear) {
            setRecyclerViewFixedSize(view, true);
            setRecyclerViewLinear(view, true);
        }
    }

    @BindingAdapter("app:hasFixedSize")
    public static void setRecyclerViewFixedSize(@NonNull RecyclerView view, boolean hasFixedSize) {

        Objects.requireNonNull(view, "view is null");

        view.setHasFixedSize(hasFixedSize);
    }

    @BindingAdapter("app:isLinear")
    public static void setRecyclerViewLinear(@NonNull RecyclerView view, boolean isLinear) {

        Objects.requireNonNull(view, "view is null");

        if (isLinear) {

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


    @BindingAdapter(value = {"app:url", "app:fallback"}, requireAll = false)
    public static void setImageViewUrl(
            @NonNull ImageView imageView,
            @Nullable Uri url,
            @Nullable Drawable fallback
    ) {

        Objects.requireNonNull(imageView);

        RequestCreator request = Picasso.get().load(url);

        if (fallback != null) {
            request = request.placeholder(fallback);
        }

        request.into(imageView);
    }
}


