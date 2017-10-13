/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.core.framework.config.property;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.Truth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This abstract class implements all the convenience config methods that
 * can be implemented independent of the config impl.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public abstract class AbstractBaseConfig implements org.kuali.rice.core.api.config.property.Config {

    private static final Logger LOG = Logger.getLogger(AbstractBaseConfig.class);

    public abstract Object getObject(String key);

    public abstract Map<String, Object> getObjects();

    public abstract Properties getProperties();

    public abstract String getProperty(String key);

    public abstract void parseConfig() throws IOException;

    public Map<String, String> getPropertiesWithPrefix(String prefix, boolean stripPrefix) {
        Map<String, String> props = new HashMap<String, String>();
        for (Map.Entry<Object, Object> entry : getProperties().entrySet()) {
            String key = (String) entry.getKey();
            if (StringUtils.isNotBlank(key) && key.trim().startsWith(prefix)) {
                props.put(stripPrefix ? key.substring(prefix.length()) : key, (String) entry.getValue());
            }
        }
        return props;
    }

    public String getAlternateOJBFile() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.ALT_OJB_FILE);
    }

    public String getAlternateSpringFile() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.ALT_SPRING_FILE);
    }

    public String getBaseWebServiceURL() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.BASE_WEB_SERVICE_URL_WORKFLOW_CLIENT_FILE);
    }

    public String getBaseWebServiceWsdlPath() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.BASE_WEB_SERVICE_WSDL_PATH);
    }

    public Boolean getBatchMode() {
        return new Boolean(getProperty(org.kuali.rice.core.api.config.property.Config.BATCH_MODE));
    }

    @Override
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        return Truth.strToBooleanIgnoreCase(getProperty(key), defaultValue).booleanValue();
    }

    @Override
    public Boolean getBooleanProperty(String key) {
        return Truth.strToBooleanIgnoreCase(getProperty(key));
    }

    @Override
    public long getNumericProperty(String key, long defaultValue) {
        Long propertyValue = getNumericProperty(key);
        if (propertyValue == null) {
            return defaultValue;
        } else {
            return propertyValue.longValue();
        }
    }

    @Override
    public Long getNumericProperty(String key) {
        String propertyValue = getProperty(key);
        if (StringUtils.isBlank(propertyValue)) {
            return null;
        }
        return new Long(propertyValue);
    }

    public String getClientWSDLFullPathAndFileName() {
        return getProperty(Config.WSDL_LOCATION_WORKFLOW_CLIENT_FILE);
    }

    public String getDailyEmailFirstDeliveryDate() {
        return getProperty(Config.FIRST_DAILY_EMAIL_DELIVERY_DATE);
    }

    public String getDefaultKewNoteClass() {
        return getProperty(Config.DEFAULT_KEW_NOTE_CLASS);
    }

    public Boolean getDevMode() {
        return Boolean.valueOf(getProperty(Config.DEV_MODE));
    }

    public String getDocumentLockTimeout() {
        return getProperty(Config.DOCUMENT_LOCK_TIMEOUT);
    }

    public String getEDLConfigLocation() {
        return getProperty(Config.EDL_CONFIG_LOCATION);
    }

    public String getEmailConfigurationPath() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.EMAIL_SECURITY_PATH);
    }

    public Boolean getEmailReminderLifecycleEnabled() {
        return Boolean.valueOf(getProperty(org.kuali.rice.core.api.config.property.Config.ENABLE_EMAIL_REMINDER_LIFECYCLE));
    }

    public String getEmbeddedPluginLocation() {
        return getProperty(Config.EMBEDDED_PLUGIN_LOCATIAON);
    }

    public String getEndPointUrl() {
        // TODO why was this using ConfigContext.getCurrentConfig in the rice BaseConfig
        return getProperty(org.kuali.rice.core.api.config.property.Config.SERVICE_SERVLET_URL);
    }

    public String getEnvironment() {
        return getProperty(Config.ENVIRONMENT);
    }

    public String getProductionEnvironmentCode() {
        return getProperty(Config.PROD_ENVIRONMENT_CODE);
    }

    public boolean isProductionEnvironment() {
        String env = getEnvironment();
        String prod = getProductionEnvironmentCode();
        // test whether the current env is production. assuming undefined env or prod code
        // is a configuration error ensure that it returns false
        return env != null && prod != null && StringUtils.equalsIgnoreCase(env, prod);
    }

    /**
     * @return the {@link RICE_VERSION} property
     */
    public String getRiceVersion() {
        return getProperty(Config.RICE_VERSION);
    }

    /**
     * @return {@link APPLICATION_NAME} or {@link MODULE_NAME} property
     */
    public String getApplicationName() {
        // first try APPLICATION_NAME
        String val = getProperty(APPLICATION_NAME);
        if (val == null) {
            val = getProperty(MODULE_NAME);
        }
        return val;
    }

    /**
     * @return {@link APPLICATION_VERSION} or {@link VERSION} property
     */
    public String getApplicationVersion() {
        // first try APPLICATION_VERSION
        String val = getProperty(APPLICATION_VERSION);
        if (val == null) {
            val = getProperty(VERSION);
        }
        return val;
    }

    public String getKENBaseURL() {
        return getProperty(Config.KEN_URL);
    }

    public String getKEWBaseURL() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.KEW_URL);
    }

    public String getKIMBaseURL() {
        return getProperty(Config.KIM_URL);
    }

    public String getKRBaseURL() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.KR_URL);
    }

    public String getKeystoreAlias() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.KEYSTORE_ALIAS);
    }

    public String getKeystoreFile() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.KEYSTORE_FILE);
    }

    public String getKeystorePassword() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.KEYSTORE_PASSWORD);
    }

    public String getLog4jFileLocation() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.LOG4J_SETTINGS_PATH);
    }

    public String getLog4jReloadInterval() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.LOG4J_SETTINGS_RELOADINTERVAL_MINS);
    }

    public Boolean getOutBoxOn() {
        // subtle difference with BaseConfig - BaseConfig returned true on missing property
        return Boolean.valueOf(getProperty(org.kuali.rice.core.api.config.property.Config.OUT_BOX_MODE));
    }

    public Integer getRefreshRate() {
        // TODO can this be moved to default config file
        // TODO why going to currentContextConfig
        Integer refreshRate;
        try {
            refreshRate = new Integer(ConfigContext.getCurrentContextConfig().getProperty(Config.REFRESH_RATE));
        } catch (NumberFormatException nfe) {
            LOG.error("Couldn't parse property " + org.kuali.rice.core.api.config.property.Config.REFRESH_RATE + " to set bus refresh rate. Defaulting to 60 seconds.");
            ConfigContext.getCurrentContextConfig().putProperty(org.kuali.rice.core.api.config.property.Config.REFRESH_RATE, "60");
            return 60;
        }
        return refreshRate;
    }

    public String getTransactionTimeout() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.TRANSACTION_TIMEOUT);
    }

    public String getWebServicesConnectRetry() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.WEB_SERVICE_CONNECT_RETRY);
    }

    public String getWeeklyEmailFirstDeliveryDate() {
        return getProperty(org.kuali.rice.core.api.config.property.Config.FIRST_WEEKLY_EMAIL_DELIVERY_DATE);
    }

    public Boolean getXmlPipelineLifeCycleEnabled() {
        return Boolean.valueOf(getProperty(Config.ENABLE_XML_PIPELINE_LIFECYCLE));
    }
}
