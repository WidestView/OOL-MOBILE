package com.example.ool_mobile.ui.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.ool_mobile.R;

public class Collapse {

    private boolean expanded;

    private final ViewGroup targetView;

    private ImageView toggle = null;

    private Collapse(@NonNull ViewGroup targetView) {
        this.targetView = targetView;

        startCollapsed();
    }

    @NonNull
    public static Collapse on(@NonNull ViewGroup targetView) {
        return new Collapse(targetView);
    }


    @NonNull
    public Collapse startCollapsed() {
        expanded = false;

        targetView.setVisibility(View.GONE);

        return this;
    }

    @NonNull
    public Collapse startExpanded() {
        targetView.setVisibility(View.VISIBLE);

        expanded = true;

        return this;
    }

    public void withToggle(@NonNull ImageView toggle) {

        this.toggle = toggle;

        toggle.setOnClickListener(v -> {

            expanded = !expanded;

            if (expanded) {
                expand();
            } else {
                collapse();
            }
        });

    }

    private void expand() {
        TransitionManager.beginDelayedTransition(targetView, new AutoTransition());

        targetView.setVisibility(View.VISIBLE);

        if (toggle != null) {
            toggle.setImageResource(R.drawable.ic_arrow_up);
        }
    }

    private void collapse() {
        TransitionManager.beginDelayedTransition(targetView, new AutoTransition());

        targetView.setVisibility(View.GONE);

        if (toggle != null) {
            toggle.setImageResource(R.drawable.ic_arrow_down);
        }
    }

}
