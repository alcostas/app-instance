package org.appfresh.application;

import android.app.Application;
import org.appfresh.exception.MissingScannerException;
import org.appfresh.instance.annotation.ConfigurationScanner;
import org.appfresh.instance.content.InstanceContentHolder;
import org.appfresh.instance.content.InstanceContext;

import static java.lang.String.format;

/**
 * Initializer for application to initiate {@link InstanceContext}
 */
public class ApplicationInitializer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ConfigurationScanner scanner = this.getClass().getAnnotation(ConfigurationScanner.class);
        if (scanner == null) {
            throw new MissingScannerException(format("Unable to identify config scanner %s, " +
                    "for android application, setup correct one", ConfigurationScanner.class.getSimpleName()));
        }

        InstanceContentHolder.initInstanceContext(new InstanceContextImpl(scanner.target()));
    }

    private class InstanceContextImpl extends InstanceContext {

        private InstanceContextImpl(Class configHolder) {
            super(configHolder);
        }

    }

}
