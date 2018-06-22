package org.appfresh.instance.annotation;

import org.appfresh.instance.enums.InstanceScope;
import org.appfresh.utils.StringUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates the configurations of instances
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Instance {

    Class instance() default Object.class;

    String qualifier() default StringUtils.EMPTY;

    int sdkVersion();

    InstanceScope scope() default InstanceScope.SINGLETON;

}
