package com.example.ool_mobile.model;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Value.Style(
        visibility = Value.Style.ImplementationVisibility.PUBLIC,
        defaults = @Value.Immutable(copy = false)
)
@Target(ElementType.TYPE)
public @interface Model {
}
