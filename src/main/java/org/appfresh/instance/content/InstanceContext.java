package org.appfresh.instance.content;

import android.os.Build;
import org.appfresh.cache.InstanceMapping;
import org.appfresh.exception.InvalidInstanceException;
import org.appfresh.instance.annotation.InjectInstance;
import org.appfresh.instance.annotation.Instance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.String.format;
import static org.appfresh.instance.enums.InstanceScope.SINGLETON;

/**
 * The context to manipulate with configured instances, get correct instance
 * and create cache based on configurations
 */
public class InstanceContext {

    private Class configClass;
    private int SDK_VERSION = Build.VERSION.SDK_INT;
    private final InstanceMapping mapping;

    protected InstanceContext(Class configHolder) {
        this.configClass = configHolder;
        this.mapping = new InstanceMapping();
    }

    /**
     * Initiate instance by class {@code sourceClass} from
     * {@link org.appfresh.instance.annotation.Configuration}
     *
     * @param sourceClass used to get instance
     * @param <T> the required instance
     * @return the instance by given configs
     */
    public <T> T getInstance(Class sourceClass) {
        return initiateInstance(sourceClass.getName());
    }

    /**
     * Initiate instance by qualifier {@code sourceName} from
     * {@link org.appfresh.instance.annotation.Configuration}
     *
     * @param sourceName used to get instance
     * @param <T> the required instance
     * @return the instance by given configs
     */
    public <T> T getInstance(String sourceName) {
        return initiateInstance(sourceName);
    }

    /**
     * Initialize instance by target
     *
     * @param target used to get instance
     * @param <T> the required instance
     * @return the instance by given configs
     */
    private <T> T initiateInstance(String target) {
        try {
            Object mapping = this.mapping.getInstanceMapping(target);
            if (mapping != null) {
                if (mapping instanceof Method) {
                    return (T) ((Method) mapping).invoke(configClass);
                } else {
                    return (T) mapping;
                }
            } else {
                int version = 0;
                Method instanceMethod = null;
                Method[] configMethods = configClass.getDeclaredMethods();
                for (Method method : configMethods) {
                    Instance config = method.getAnnotation(Instance.class);
                    if (isValidConfigInstance(config, target, version)) {
                        instanceMethod = method;
                        version = config.sdkVersion();
                    }
                }
                Instance config = instanceMethod.getAnnotation(Instance.class);
                Object instance = instanceMethod.invoke(configClass);
                if (config.scope().equals(SINGLETON)) {
                    this.mapping.setInstanceMapping(target, instance);
                } else {
                    this.mapping.setInstanceMapping(target, instanceMethod);
                }
                return (T) instance;
            }
        } catch (NullPointerException e) {
            throw new InvalidInstanceException(format("Unable to initiate reference for class %s during the " +
                    "missing instance config from config source %s", target, configClass.getName()));
        } catch (Exception e) {
            throw new InvalidInstanceException(format("Unable to initiate reference " +
                    "for class %s with cause %s", target, e.getMessage()), e);
        }
    }

    /**
     * Initiate annotated field with correct objects for given object
     *
     * @param target object to initiate annotated fields
     * @param <T> the class of object that should ne completed
     */
    public <T> void injectInstance(T target) {
        try {
            Field[] fields = target.getClass().getDeclaredFields();
            for (Field field : fields) {
                InjectInstance inject = field.getAnnotation(InjectInstance.class);
                if (inject != null) {
                    String qualifier = inject.qualifier();
                    boolean accessibility = field.isAccessible();
                    field.setAccessible(true);
                    if (!qualifier.equals("")) {
                        field.set(target, getInstance(qualifier));
                    } else {
                        field.set(target, getInstance(target.getClass()));
                    }
                    field.setAccessible(accessibility);
                }
            }
        } catch (Exception e) {
            throw new InvalidInstanceException(format("Unable to initiate instance or inject it on object %s", target.getClass()), e);
        }
    }

    /**
     * Verify if {@link org.appfresh.instance.annotation.Configuration}
     * satisfied given configs
     *
     * @param config contains instance qualifier, instance class and SDK version
     * @param source the qualifier of instance, class or string
     * @param version the bigger founded version of SDK
     * @return if config is satisfied for required instance
     */
    private boolean isValidConfigInstance(Instance config, Object source, int version) {
        if (source == null) return false;
        Object value = source instanceof String ? config.qualifier() : config.instance();
        return source.equals(value) && satisfiedSdkVersion(config, version);
    }

    /**
     * Verify if instance config is satisfied by mobile sdk and instance config if latest version
     *
     * @param config contains information about instance config sdk version
     * @param version the latest version
     * @return if config sdk version is satisfied for required instance
     */
    private boolean satisfiedSdkVersion(Instance config, int version) {
        return config.sdkVersion() <= SDK_VERSION && config.sdkVersion() >= version;
    }

}
