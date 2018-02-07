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
package org.kuali.rice.core.impl.config.module;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.security.credentials.CredentialsSourceFactory;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.config.module.ModuleConfigurer;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a place to put some of the common configuration logic that used to be done by the RiceConfigurer.
 */
public class CoreConfigurer extends ModuleConfigurer {

	private DataSource dataSource;
	private DataSource nonTransactionalDataSource;
	private DataSource serverDataSource;
	private String platform;
	private UserTransaction userTransaction;
	private TransactionManager transactionManager;
	private CredentialsSourceFactory credentialsSourceFactory;

    public CoreConfigurer() {
		super(CoreConstants.Namespaces.MODULE_NAME);
		setValidRunModes(Arrays.asList(RunMode.LOCAL));
	}

	@Override
	public List<Lifecycle> loadLifecycles() throws Exception {
		final List<Lifecycle> lifecycles = new LinkedList<Lifecycle>();
		if (isConfigureLogging()) {
			lifecycles.add(new Log4jLifeCycle());
		}
		 
		return lifecycles;
	}
	
	@Override
	public void addAdditonalToConfig() {
		configureJta();
		configureDataSource();
		configureCredentialsSourceFactory();
	}

    @Override
    public boolean hasWebInterface() {
        // Core has the XML Ingester user interface
        return true;
    }

    protected boolean isConfigureLogging() {
		return ConfigContext.getCurrentContextConfig().getBooleanProperty(RiceConstants.RICE_LOGGING_CONFIGURE, false);
	}
	
	protected void configureCredentialsSourceFactory() {
		if (credentialsSourceFactory != null) {
			ConfigContext.getCurrentContextConfig().putObject(Config.CREDENTIALS_SOURCE_FACTORY, this.credentialsSourceFactory);
		}
	}
 
	protected void configureDataSource() {
		if (this.dataSource != null) {
			ConfigContext.getCurrentContextConfig().putObject(RiceConstants.DATASOURCE_OBJ, this.dataSource);
		}
		
        if (this.nonTransactionalDataSource != null) {
        	ConfigContext.getCurrentContextConfig().putObject(RiceConstants.NON_TRANSACTIONAL_DATASOURCE_OBJ, this.nonTransactionalDataSource);
        }
        
        if (this.serverDataSource != null) {
        	ConfigContext.getCurrentContextConfig().putObject(RiceConstants.SERVER_DATASOURCE_OBJ, this.serverDataSource);
        }
	}

    @Override
	public List<String> getPrimarySpringFiles() {
		final List<String> springFileLocations = new ArrayList<String>();
		springFileLocations.add( "classpath:org/kuali/rice/core/config/CORESpringBeans.xml" );
		return springFileLocations;
	}
	
	/**
	 * If the user injected JTA classes into this configurer, verify that both the
	 * UserTransaction and TransactionManager are set and then attach them to
	 * the configuration.
	 */
	protected void configureJta() {
		if (this.userTransaction != null) {
			ConfigContext.getCurrentContextConfig().putObject(RiceConstants.USER_TRANSACTION_OBJ, this.userTransaction);
		}
		if (this.transactionManager != null) {
			ConfigContext.getCurrentContextConfig().putObject(RiceConstants.TRANSACTION_MANAGER_OBJ, this.transactionManager);
		}
		boolean userTransactionConfigured = this.userTransaction != null || !StringUtils.isEmpty(ConfigContext.getCurrentContextConfig().getProperty(RiceConstants.USER_TRANSACTION_JNDI));
		boolean transactionManagerConfigured = this.transactionManager != null || !StringUtils.isEmpty(ConfigContext.getCurrentContextConfig().getProperty(RiceConstants.TRANSACTION_MANAGER_JNDI));
		if (userTransactionConfigured && !transactionManagerConfigured) {
			throw new ConfigurationException("When configuring JTA, both a UserTransaction and a TransactionManager are required.  Only the UserTransaction was configured.");
		}
		if (transactionManagerConfigured && !userTransactionConfigured) {
			throw new ConfigurationException("When configuring JTA, both a UserTransaction and a TransactionManager are required.  Only the TransactionManager was configured.");
		}
	}

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

    public DataSource getNonTransactionalDataSource() {
        return this.nonTransactionalDataSource;
    }

    public void setNonTransactionalDataSource(DataSource nonTransactionalDataSource) {
        this.nonTransactionalDataSource = nonTransactionalDataSource;
    }

    public DataSource getServerDataSource() {
		return this.serverDataSource;
	}

	public void setServerDataSource(DataSource serverDataSource) {
		this.serverDataSource = serverDataSource;
	}

	public String getPlatform() {
		return this.platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public TransactionManager getTransactionManager() {
		return this.transactionManager;
	}

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public UserTransaction getUserTransaction() {
		return this.userTransaction;
	}

	public void setUserTransaction(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}

	public CredentialsSourceFactory getCredentialsSourceFactory() {
		return credentialsSourceFactory;
	}

	public void setCredentialsSourceFactory(
			final CredentialsSourceFactory credentialsSourceFactory) {
		this.credentialsSourceFactory = credentialsSourceFactory;
	}
}
