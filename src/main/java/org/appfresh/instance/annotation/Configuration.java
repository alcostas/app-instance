package org.appfresh.instance.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates the object that contains the configurations for application instances
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Configuration {
}
