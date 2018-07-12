package org.appfresh.instance.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The configuration of references (instances/objects) for configuration class.
 * This is field annotation that indicates that field of config file should be initialized by app-instance framework.
 * The referenceName indicates the qualifier of object reference.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface ConfigurationReference {

    String referenceName();

}
