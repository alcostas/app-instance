package org.appfresh.instance.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotation that indicates that is target for auto inject of filed
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface InstanceTarget {
}
