package org.rhm.util.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Purely visual in cloth config
/**
 * This only works for boolean fields, which means if the field you depend on is true, it'll show, else, not
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigDepends {
    String[] fields();
}
