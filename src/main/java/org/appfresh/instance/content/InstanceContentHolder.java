package org.appfresh.instance.content;

/**
 * Content to initiate instance by configuration
 */
public final class InstanceContentHolder {

    private static InstanceContext context;

    public static void initInstanceContext(InstanceContext instanceContext) {
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

}
