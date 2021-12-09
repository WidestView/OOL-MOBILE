package com.example.ool_mobile.ui.util.binding_adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Objects;

public class UtilAdapters {

    @BindingAdapter("fixedLinear")
    public static void setRecyclerViewFixedLinear(@NonNull RecyclerView view, boolean isFixedLinear) {

        if (isFixedLinear) {
            setRecyclerViewFixedSize(view, true);
            setRecyclerViewLinear(view, true);
        }
    }

    @BindingAdapter("hasFixedSize")
    public static void setRecyclerViewFixedSize(@NonNull RecyclerView view, boolean hasFixedSize) {

        Objects.requireNonNull(view, "view is null");

        view.setHasFixedSize(hasFixedSize);
    }

    @BindingAdapter("isLinear")
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


    @BindingAdapter(
            value = {
                    "url",
                    "fallback",
                    "cacheResult"
            },
            requireAll = false
    )
    public static void setImageViewUrl(
            @NonNull ImageView imageView,
            @Nullable Uri url,
            @Nullable Drawable fallback,
            @Nullable Boolean cacheResult
    ) {
        if (cacheResult == null) {
            cacheResult = true;
        }

        Objects.requireNonNull(imageView);

        RequestCreator request = Picasso.get()
                .load(url);

        cacheResult = false;

        if (!cacheResult) {
            request.memoryPolicy(MemoryPolicy.NO_CACHE);
        }

        if (fallback != null) {
            request.placeholder(fallback);
        }

        request.into(imageView);
    }

    @BindingAdapter("visible")
    public static void setVisible(@NonNull View view, boolean isVisible) {

        Objects.requireNonNull(view);

        view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter("gone")
    public static void setGone(@NonNull View view, boolean isGone) {

        Objects.requireNonNull(view);

        view.setVisibility(isGone ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("imageBitmap")
    public static void setBitmap(@NonNull ImageView view, @Nullable Bitmap bitmap) {

        Objects.requireNonNull(view);

        view.setImageBitmap(bitmap);
    }

    @BindingAdapter("error")
    public static void setEditTextError(@NonNull EditText editText, @Nullable String error) {
        editText.setError(error);
    }
}


