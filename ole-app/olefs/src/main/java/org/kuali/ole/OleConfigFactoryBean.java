package org.kuali.ole;

import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 6/5/13
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleConfigFactoryBean implements FactoryBean<Config> {
    private List<String> configLocations;
    private boolean initialize = false;
    private boolean systemPropertiesAlwaysWin = true;
    public static String CONFIG_OVERRIDE_LOCATION;


    @Override
    public Config getObject() throws Exception {

        Config oldConfig = null;

        if (getConfigLocations() == null) {
            throw new ConfigurationException("No config locations declared, at least one is required");
        }

        if (ConfigContext.getCurrentContextConfig() != null) {
            oldConfig = ConfigContext.getCurrentContextConfig();
        }
        //SimpleConfig config = null;
        JAXBConfigImpl config = null;
        if (CONFIG_OVERRIDE_LOCATION != null) {
            config = new JAXBConfigImpl(CONFIG_OVERRIDE_LOCATION, oldConfig);
        } else {
            config = new JAXBConfigImpl(getConfigLocations(), oldConfig);
        }

        config.setSystemOverride(systemPropertiesAlwaysWin);
        config.parseConfig();

        if (initialize) {
            ConfigContext.init(config);
        }

        return config;
    }

    @Override
    public Class<Config> getObjectType() {
        return Config.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public List<String> getConfigLocations() {
        return this.configLocations;
    }

    public void setConfigLocations(List<String> configLocations) {
        this.configLocations = configLocations;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public boolean isSystemPropertiesAlwaysWin() {
        return systemPropertiesAlwaysWin;
    }

    public void setSystemPropertiesAlwaysWin(boolean systemPropertiesAlwaysWin) {
        this.systemPropertiesAlwaysWin = systemPropertiesAlwaysWin;
    }
}
