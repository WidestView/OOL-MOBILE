package com.example.ool_mobile.ui.list.package_list;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class RowPackageUtil {

    @Nullable
    public String formatQuantities(@Nullable List<Integer> quantities) {
        if (quantities == null) {
            return null;
        }

        return quantities.stream().map(String::valueOf)
                .collect(Collectors.joining(", "));
    }


}
