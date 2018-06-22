package org.appfresh.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Utils object to manage mapping of configured instances
 */
public class InstanceMapping {

    private final Map<String, Object> mapping;

    public InstanceMapping() {
        mapping = new HashMap<>();
    }

    public <T> T getInstanceMapping(String instance) {
        return (T) mapping.get(instance);
    }

    public <T> void setInstanceMapping(String instance, T target) {
        mapping.put(instance, target);
    }

}
