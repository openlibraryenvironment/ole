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
package org.kuali.rice.core.framework.persistence.jdbc.datasource;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.util.RiceConstants;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.jndi.JndiTemplate;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that can be used to load the primary datasource for a Rice module from an object in the Config system or from a
 * JNDI url specified by the Configuration system. By default, it loads these values from the following properties if the
 * <code>useNonTransactionalDataSource</code> param is not set or is set to false:
 * <ul>
 * <li>{@link RiceConstants#DATASOURCE_OBJ}</li>
 * <li>{@link RiceConstants#DATASOURCE_JNDI}</li>
 * </ul>
 * If the <code>useNonTransactionalDataSource</code> param is set to true the following properties will be used:<br>
 * <br>
 * <ul>
 * <li>{@link RiceConstants#NON_TRANSACTIONAL_DATASOURCE_OBJ}</li>
 * <li>{@link RiceConstants#NON_TRANSACTIONAL_DATASOURCE_JNDI}</li>
 * </ul>
 * If the <code>server</code> param is set to true, the following properties will be added to the end of the preferred lists:<br>
 * <br>
 * <ul>
 * <li>{@link RiceConstants#SERVER_DATASOURCE_OBJ}</li>
 * <li>{@link RiceConstants#SERVER_DATASOURCE_JNDI}</li> 
 * <p>
 * The config properties checked can be overridden by setting values into the list parameters
 * <code>preferredDataSourceParams</code> and <code>preferredDataSourceJndiParams</code>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PrimaryDataSourceFactoryBean extends AbstractFactoryBean {

    private static final String DEFAULT_DATASOURCE_PARAM = RiceConstants.DATASOURCE_OBJ;
    private static final String DEFAULT_SERVER_DATASOURCE_PARAM = RiceConstants.SERVER_DATASOURCE_OBJ;
    private static final String DEFAULT_NONTRANSACTIONAL_DATASOURCE_PARAM = RiceConstants.NON_TRANSACTIONAL_DATASOURCE_OBJ;
    private static final String DEFAULT_DATASOURCE_JNDI_PARAM = RiceConstants.DATASOURCE_JNDI;
    private static final String DEFAULT_SERVER_DATASOURCE_JNDI_PARAM = RiceConstants.SERVER_DATASOURCE_JNDI;
    private static final String DEFAULT_NONTRANSACTIONAL_DATASOURCE_JNDI_PARAM = RiceConstants.NON_TRANSACTIONAL_DATASOURCE_JNDI;

    private JndiTemplate jndiTemplate;
    private boolean nonTransactionalDataSource = false;
    private String defaultDataSourceParam = DEFAULT_DATASOURCE_PARAM;
    private String defaultNonTransactionalDataSourceParam = DEFAULT_NONTRANSACTIONAL_DATASOURCE_PARAM;
    private String defaultDataSourceJndiParam = DEFAULT_DATASOURCE_JNDI_PARAM;
    private String defaultNonTransactionalDataSourceJndiParam = DEFAULT_NONTRANSACTIONAL_DATASOURCE_JNDI_PARAM;
    private List<String> preferredDataSourceParams = new ArrayList<String>();
    private List<String> preferredDataSourceJndiParams = new ArrayList<String>();
    private boolean serverDataSource = false;
    private boolean nullAllowed = false;
    private boolean forceLazy = false;
    
    public PrimaryDataSourceFactoryBean() {
        setSingleton(true);
    }

    public Class<DataSource> getObjectType() {
        return DataSource.class;
    }
    
    @Override
	public void afterPropertiesSet() throws Exception {
    	if (serverDataSource) {
    		getPreferredDataSourceParams().add(DEFAULT_SERVER_DATASOURCE_PARAM);
    		getPreferredDataSourceJndiParams().add(DEFAULT_SERVER_DATASOURCE_JNDI_PARAM);
    	}
    	super.afterPropertiesSet();
	}

    /**
     * <p>returns the new DataSource instance, or a lazy proxy to it (see {@link #setForceLazy(boolean)}.</p>
     *
     * @return the new DataSource
     * @throws Exception see superclass method
     * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
     */
	@Override
    protected Object createInstance() throws Exception {
        DataSource dataSource = null;
        if (!isForceLazy()) {
            dataSource = createDataSourceInstance();
        } else {
            dataSource = (DataSource)Proxy.newProxyInstance(getClass().getClassLoader(),
                    new Class[] { DataSource.class },
                    new LazyInvocationHandler());
        }
        
        return dataSource;
    }

    private DataSource createDataSourceInstance() throws Exception {
        final DataSource dataSource;Config config = ConfigContext.getCurrentContextConfig();
        dataSource = createDataSource(config);
        if (dataSource == null && !isNullAllowed()) {
            throw new ConfigurationException("Failed to configure the Primary Data Source.");
        }
        return dataSource;
    }

    protected String getDefaultDataSourceParamByType() {
        return (nonTransactionalDataSource) ? getDefaultNonTransactionalDataSourceParam() : getDefaultDataSourceParam();
    }

    protected String getDefaultDataSourceJndiParamByType() {
        return (nonTransactionalDataSource) ? getDefaultNonTransactionalDataSourceJndiParam() : getDefaultDataSourceJndiParam();
    }

    protected DataSource createDataSource(Config config) throws Exception {
        DataSource dataSource = loadPreferredDataSourceFromConfig(config);
        if (dataSource == null) {

            Object dataSourceObject = config.getObject(getDefaultDataSourceParamByType());
            if (dataSourceObject != null) {
                validateDataSource(getDefaultDataSourceParamByType(), dataSourceObject);
                dataSource = (DataSource) dataSourceObject;
            } else {
                dataSource = getDataSourceFromJndi(config, getDefaultDataSourceJndiParamByType());
            }
        }
        return dataSource;
    }

    protected DataSource loadPreferredDataSourceFromConfig(Config config) {
        for (String dataSourceParam : getPreferredDataSourceParams()) {
            Object dataSource = config.getObject(dataSourceParam);
            if (dataSource != null) {
                validateDataSource(dataSourceParam, dataSource);
                return (DataSource) dataSource;
            }
        }
        if (this.jndiTemplate == null) {
            this.jndiTemplate = new JndiTemplate();
        }
        for (String dataSourceJndiParam : getPreferredDataSourceJndiParams()) {
            DataSource dataSource = getDataSourceFromJndi(config, dataSourceJndiParam);
            if (dataSource != null) {
                return dataSource;
            }
        }
        return null;
    }

    protected void validateDataSource(String paramName, Object dataSourceObject) {
        if (!(dataSourceObject instanceof DataSource)) {
            throw new ConfigurationException("DataSource configured for parameter '" + paramName + "' was not an instance of DataSource.  Was instead " + dataSourceObject.getClass().getName());
        }
    }

    protected DataSource getDataSourceFromJndi(Config config, String dataSourceJndiParam) {
        String jndiName = config.getProperty(dataSourceJndiParam);
        if (!StringUtils.isBlank(jndiName)) {
            try {
                Object dataSource = getJndiTemplate().lookup(jndiName, DataSource.class);
                if (dataSource != null) {
                    validateDataSource(dataSourceJndiParam, dataSource);
                    return (DataSource) dataSource;
                }
            } catch (NamingException e) {
                throw new ConfigurationException("Could not locate the DataSource at the given JNDI location: '" + jndiName + "'", e);
            }
        }
        return null;
    }

    protected void destroyInstance(Object instance) throws Exception {
        if (instance instanceof Lifecycle) {
            ((Lifecycle) instance).stop();
        }
    }

    protected String getStringProperty(Config config, String propertyName) {
        String data = config.getProperty(propertyName);
        if (StringUtils.isEmpty(data)) {
            throw new ConfigurationException("Could not locate a value for the given property '" + propertyName + "'.");
        }
        return data;
    }

    protected int getIntProperty(Config config, String propertyName) {
        String data = getStringProperty(config, propertyName);
        try {
            int intData = Integer.parseInt(data);
            return intData;
        } catch (NumberFormatException e) {
            throw new ConfigurationException("The given property '" + propertyName + "' was not a valid integer.  Value was '" + data + "'");
        }
    }

    public JndiTemplate getJndiTemplate() {
        return this.jndiTemplate;
    }

    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate;
    }

    public String getDefaultDataSourceJndiParam() {
        return defaultDataSourceJndiParam;
    }

    public void setDefaultDataSourceJndiParam(String defaultDataSourceJndiParam) {
        this.defaultDataSourceJndiParam = defaultDataSourceJndiParam;
    }

    public String getDefaultNonTransactionalDataSourceJndiParam() {
        return defaultNonTransactionalDataSourceJndiParam;
    }

    public void setDefaultNonTransactionalDataSourceJndiParam(String defaultNonTransactionalDataSourceJndiParam) {
        this.defaultNonTransactionalDataSourceJndiParam = defaultNonTransactionalDataSourceJndiParam;
    }

    public String getDefaultDataSourceParam() {
        return defaultDataSourceParam;
    }

    public void setDefaultDataSourceParam(String defaultDataSourceParam) {
        this.defaultDataSourceParam = defaultDataSourceParam;
    }

    public String getDefaultNonTransactionalDataSourceParam() {
        return defaultNonTransactionalDataSourceParam;
    }

    public void setDefaultNonTransactionalDataSourceParam(String defaultNonTransactionalDataSourceParam) {
        this.defaultNonTransactionalDataSourceParam = defaultNonTransactionalDataSourceParam;
    }

    public List<String> getPreferredDataSourceJndiParams() {
        return preferredDataSourceJndiParams;
    }

    public void setPreferredDataSourceJndiParams(List<String> preferredDataSourceJndiParams) {
        this.preferredDataSourceJndiParams = preferredDataSourceJndiParams;
    }

    public List<String> getPreferredDataSourceParams() {
        return preferredDataSourceParams;
    }

    public void setPreferredDataSourceParams(List<String> preferredDataSourceParams) {
        this.preferredDataSourceParams = preferredDataSourceParams;
    }

    public void setNonTransactionalDataSource(boolean nonTransactionalDataSource) {
        this.nonTransactionalDataSource = nonTransactionalDataSource;
    }

	public boolean isServerDataSource() {
		return this.serverDataSource;
	}

	public void setServerDataSource(boolean serverDataSource) {
		this.serverDataSource = serverDataSource;
	}

	public boolean isNullAllowed() {
		return this.nullAllowed;
	}

	public void setNullAllowed(boolean nullAllowed) {
		this.nullAllowed = nullAllowed;
	}

    /**
     * @see #setForceLazy(boolean)
     */
    public boolean isForceLazy() {
        return forceLazy;
    }

    /**
     * setting to true will cause the {@link #createInstance()} method to return a lazy proxy to the DataSource
     * @param forceLazy
     * @see #createInstance()
     */
    public void setForceLazy(boolean forceLazy) {
        this.forceLazy = forceLazy;
    }

    /**
     * used for constructing a lazy proxy to a DataSource in {@link
     * org.kuali.rice.core.framework.persistence.jdbc.datasource.PrimaryDataSourceFactoryBean#createInstance()}
     */
    private class LazyInvocationHandler implements InvocationHandler {
        private volatile DataSource dataSource = null;
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                if (dataSource == null) {
                    dataSource = createDataSourceInstance();
                }
                return method.invoke(dataSource, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
    
}
