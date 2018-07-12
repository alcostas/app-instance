package org.appfresh.instance.content;

import java.util.Map;

/**
 * Content to initiate instance by configuration
 */
public final class InstanceContentHolder {

    private final InstanceContext context;

    public InstanceContentHolder(InstanceContext instanceContext) {
        context = instanceContext;
    }

    public <T> T getInstance(String sourceName) {
        return context.getInstance(sourceName);
    }

    public <T> T getInstance(Class sourceClass) {
        return context.getInstance(sourceClass);
    }

    public <T> void injectInstances(T target) {
        context.injectInstance(target);
    }

    public void initiateConfigClassReferences(Map<String, Object> references) {
        context.initiateConfigClassReferences(references);
    }

}
