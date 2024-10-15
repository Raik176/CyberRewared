package org.rhm.util.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// I know im lazy
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(ConfigTranslations.class)
public @interface ConfigTranslation {
    String locale() default "en_us";

    String name();

    String[] description() default {"No description available."};
}
