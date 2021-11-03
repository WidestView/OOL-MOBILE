package com.example.ool_mobile.ui.component.content_row;


import android.view.View;

import androidx.annotation.Nullable;

public interface ContentRowEvents {

    @Nullable
    View.OnClickListener getOnEdit();

    @Nullable
    View.OnClickListener getOnDelete();
}
