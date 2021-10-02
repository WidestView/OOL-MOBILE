package com.example.ool_mobile.ui.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.function.Supplier;

public final class ViewModelFactory {

    private ViewModelFactory() {

    }

    @NonNull
    public static <VM extends ViewModel> ViewModelProvider.Factory create(
            @NonNull Class<VM> viewClass,
            @NonNull Supplier<VM> supplier
    ) {
        return new ViewModelProvider.Factory() {

            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

                if (modelClass != viewClass) {
                    throw new UnsupportedOperationException(
                            "Expected" + viewClass.getSimpleName() + ", found "
                                    + modelClass.getSimpleName()
                    );
                }

                //noinspection unchecked
                return (T) supplier.get();
            }
        };
    }
}
