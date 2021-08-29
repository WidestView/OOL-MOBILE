package com.example.ool_mobile.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ool_mobile.R;
import com.example.ool_mobile.ui.util.Collapse;

import static java.util.Objects.requireNonNull;

public class ContentRow extends ConstraintLayout {

    @NonNull
    private final TextView titleTextView;

    @NonNull
    private final TextView statusTextView;

    @NonNull
    private final LinearLayout collapseLayout;

    @NonNull
    private final View editView;

    @NonNull
    private final View deleteView;

    @NonNull
    private final ImageView arrowView;


    public ContentRow(@NonNull Context context) {
        this(context, null);
    }

    public ContentRow(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentRow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        inflate(context, R.layout.component_row, this);

        collapseLayout = requireNonNull(findViewById(R.id.contentRow_collapseLayout));
        titleTextView = requireNonNull(findViewById(R.id.contentRow_titleTextView));
        statusTextView = requireNonNull(findViewById(R.id.contentRow_statusTextView));
        editView = requireNonNull(findViewById(R.id.contentRow_editImageView));
        deleteView = requireNonNull(findViewById(R.id.contentRow_deleteImageView));
        arrowView = requireNonNull(findViewById(R.id.contentRow_expandImageView));

        setupArguments(context, attrs);

        if (!isInEditMode()) {
            Collapse.on(collapseLayout)
                    .startCollapsed()
                    .withToggle(arrowView);
        }
    }


    private void setupArguments(@NonNull Context context, @Nullable AttributeSet attrs) {

        int[] sets = {R.attr.titleText, R.attr.statusText};

        TypedArray array = context.obtainStyledAttributes(attrs, sets);

        try {
            setupText(array, this.titleTextView, 0);
            setupText(array, this.statusTextView, 1);
        } finally {
            array.recycle();
        }
    }

    public static void setupText(@NonNull TypedArray array, @NonNull TextView textView, int index) {
        CharSequence text = array.getText(index);

        if (text != null) {

            textView.setText(text);
        }
    }

    @Override
    public void addView(@NonNull View child, int index, @Nullable ViewGroup.LayoutParams params) {

        if (collapseLayout != null) {
            collapseLayout.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    @NonNull
    public TextView getTitleTextView() {
        return titleTextView;
    }

    @NonNull
    public TextView getStatusTextView() {
        return statusTextView;
    }

    @NonNull
    public LinearLayout getCollapseLayout() {
        return collapseLayout;
    }

    @NonNull
    public View getEditView() {
        return editView;
    }

    @NonNull
    public View getDeleteView() {
        return deleteView;
    }

    @NonNull
    public View getArrowView() {
        return arrowView;
    }
}
