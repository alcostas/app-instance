package org.appfresh.utils;

/**
 * Utils for strings
 */
public class StringUtils {

    public static final String EMPTY = "";

    public static boolean isEmptyString(String target) {
        return EMPTY.equals(target);
    }

    public static boolean isNullOrEmptyString(String target) {
        return target == null || EMPTY.equals(target);
    }

}
