package org.appfresh.instance.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mandatory configuration android application that indicates class with
 * configuration of application instances, see {@link Configuration}
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ConfigurationScanner {

    Class target();

}
