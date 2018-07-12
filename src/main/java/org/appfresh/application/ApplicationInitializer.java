package org.appfresh.application;

import android.app.Application;
import org.appfresh.exception.MissingScannerException;
import org.appfresh.instance.annotation.ConfigurationScanner;
import org.appfresh.instance.content.InstanceContentHolder;
import org.appfresh.instance.content.InstanceContext;

import java.util.Map;

import static java.lang.String.format;

/**
 * Initializer for application to initiate {@link InstanceContext}
 */
public class ApplicationInitializer extends Application {

    private InstanceContentHolder contentHolder;

    @Override
    public void onCreate() {
        super.onCreate();

        ConfigurationScanner scanner = this.getClass().getAnnotation(ConfigurationScanner.class);
        if (scanner == null) {
            throw new MissingScannerException(format("Unable to identify config scanner %s, " +
                    "for android application, setup correct one", ConfigurationScanner.class.getSimpleName()));
        }

        try {
            contentHolder = new InstanceContentHolder(new InstanceContextImpl(scanner.target()));
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setConfigClassReferences(Map<String,Object> references) {
        contentHolder.initiateConfigClassReferences(references);
    }

    public InstanceContentHolder getInstanceContentHolder() {
        return contentHolder;
    }

    private class InstanceContextImpl extends InstanceContext {

        private InstanceContextImpl(Class configHolder) throws InstantiationException, IllegalAccessException {
            super(configHolder);
        }

    }

}
