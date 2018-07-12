package org.appfresh.instance.content;

import android.os.Build;
import org.appfresh.cache.InstanceMapping;
import org.appfresh.exception.InvalidInstanceException;
import org.appfresh.instance.annotation.ConfigurationReference;
import org.appfresh.instance.annotation.InjectInstance;
import org.appfresh.instance.annotation.Instance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static java.lang.String.format;
import static org.appfresh.instance.enums.InstanceScope.SINGLETON;

/**
 * The context to manipulate with configured instances, get correct instance
 * and create cache based on configurations
 */
public class InstanceContext {

    private Object configClass;
    private int SDK_VERSION = Build.VERSION.SDK_INT;
    private final InstanceMapping mapping;

    protected InstanceContext(Class configHolder) throws IllegalAccessException, InstantiationException {
        this.configClass = configHolder.newInstance();
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
        return initiateInstance(sourceClass);
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
     * Set up values for config class
     * NOTE all fields should be static
     */
    public void initiateConfigClassReferences(Map<String, Object> references) {
        Field[] fields = configClass.getClass().getDeclaredFields();
        for (Field field : fields) {
            ConfigurationReference configReference = field.getAnnotation(ConfigurationReference.class);
            if (configReference != null) {
                String reference = configReference.referenceName();
                Object instance = references.get(reference);
                if (instance != null) {
                    boolean accessibility = field.isAccessible();
                    field.setAccessible(true);
                    try {
                        field.set(configClass, instance);
                    } catch (IllegalAccessException e) {
                        throw new InvalidInstanceException(format("Instance reference can not be applied for reference %s", reference));
                    }
                    field.setAccessible(accessibility);
                } else {
                    throw new InvalidInstanceException(format("Instance reference is not present for field with reference name %s", reference));
                }
            }
        }
    }

    /**
     * Initialize instance by target
     *
     * @param target used to get instance
     * @param <T> the required instance
     * @return the instance by given configs
     */
    private <R, T> R initiateInstance(T target) {
        try {
            String targetName = target instanceof String ?
                    (String) target : ((Class) target).getName();
            Object mappedCache = this.mapping.getInstanceMapping(targetName);
            if (mappedCache != null) {
                if (mappedCache instanceof Method) {
                    return (R) ((Method) mappedCache).invoke(configClass);
                } else {
                    return (R) mappedCache;
                }
            } else {
                int version = 0;
                Method instanceMethod = null;
                Method[] configMethods = configClass.getClass().getDeclaredMethods();
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
                    this.mapping.setInstanceMapping(targetName, instance);
                } else {
                    this.mapping.setInstanceMapping(targetName, instanceMethod);
                }
                return (R) instance;
            }
        } catch (NullPointerException e) {
            throw new InvalidInstanceException(format("Unable to initiate reference for class %s during the " +
                    "missing instance config from config source %s", target, configClass.getClass().getName()));
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
                        field.set(target, getInstance(field.getType()));
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
        if (source == null || config == null) return false;
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
