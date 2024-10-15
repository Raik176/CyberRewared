package org.rhm.util.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigRange {
    float minFloat() default Float.MIN_VALUE;

    float maxFloat() default Float.MAX_VALUE;

    int minInt() default Integer.MIN_VALUE;

    int maxInt() default Integer.MAX_VALUE;
}
