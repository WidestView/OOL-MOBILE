package com.example.ool_mobile.ui.util.binding_adapter;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.example.ool_mobile.R;

import java.util.List;
import java.util.Objects;

public class AutoCompleteTextViewAdapters {

    private static final int SELECTION_TAG = R.id.autoCompleteTextView_selectionTag;

    @BindingAdapter("itemSelection")
    public static void setItemSelection(
            @NonNull AutoCompleteTextView textView,
            @Nullable Integer index
    ) {

        Objects.requireNonNull(textView, "textView is null");

        if (index == null) {
            index = -1;
        }

        textView.setListSelection(index);

        textView.setTag(SELECTION_TAG, index);

        ListAdapter adapter = textView.getAdapter();

        if (adapter != null) {
            Object object = textView.getAdapter().getItem(index);

            if (object instanceof String) {
                textView.setText((String) object);
            }
        }
    }

    @InverseBindingAdapter(attribute = "itemSelection")
    public static int getItemSelection(@NonNull AutoCompleteTextView textView) {

        Integer index = (Integer) textView.getTag(SELECTION_TAG);

        if (index == null) {
            index = textView.getListSelection();
        }

        return index;
    }

    @BindingAdapter("itemSelectionAttrChanged")
    public static void setItemSelectionListener(
            @NonNull AutoCompleteTextView textView,
            @NonNull InverseBindingListener onChange
    ) {
        Objects.requireNonNull(textView, "textView is null");
        Objects.requireNonNull(onChange);

        textView.setOnItemClickListener((parent, view, position, id) -> {

            textView.setTag(SELECTION_TAG, position);

            onChange.onChange();
        });
    }

    @BindingAdapter("items")
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
