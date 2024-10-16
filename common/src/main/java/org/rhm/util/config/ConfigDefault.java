package org.rhm.util.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigDefault { // I'll never use any of these as config values anyway
    String stringValue() default "N/A";

    float floatValue() default Float.MIN_VALUE;

    int intValue() default Integer.MIN_VALUE;

    BoolState boolValue() default BoolState.UNDEFINED;

    enum BoolState { //Need to have a workaround for bools
        TRUE, FALSE, UNDEFINED
    }
}
