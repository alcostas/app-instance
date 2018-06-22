package org.appfresh.instance.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * On configuration class indicates the next configuration classes to apply
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ImportConfiguration {

    Class[] target();

}
