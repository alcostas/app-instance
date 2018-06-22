package org.appfresh.instance.annotation;

import org.appfresh.utils.StringUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates the fields of object that should be instantiated
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface InjectInstance {

    String qualifier() default StringUtils.EMPTY;

}
