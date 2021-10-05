package com.example.ool_mobile.ui.util.form;

import org.immutables.value.Value;

@Value.Immutable
public interface FormTime {

    @Value.Parameter
    long getHour();

    @Value.Parameter
    long getMinute();
}
