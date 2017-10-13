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
package org.kuali.rice.ksb.messaging.quartz;

import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

/**
 * An implementation of the Quartz SchedulerFactoryBean which uses a database-backed quartz if the useQuartzDatabase property
 * is set.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KSBSchedulerFactoryBean extends SchedulerFactoryBean {

    private PlatformTransactionManager jtaTransactionManager;
    private TransactionManager transactionManager;
    private DataSource dataSource;
    private DataSource nonTransactionalDataSource;
    private boolean transactionManagerSet = false;
    private boolean nonTransactionalDataSourceSet = false;
    private boolean nonTransactionalDataSourceNull = true;
    private boolean dataSourceSet = false;
    private boolean schedulerInjected = false;
    
    @Override
    protected Scheduler createScheduler(SchedulerFactory schedulerFactory, String schedulerName) throws SchedulerException {
        if (ConfigContext.getCurrentContextConfig().getObject(KSBConstants.Config.INJECTED_EXCEPTION_MESSAGE_SCHEDULER_KEY) != null) {
            try {
	            schedulerInjected = true;
                Scheduler scheduler = (Scheduler) ConfigContext.getCurrentContextConfig().getObject(KSBConstants.Config.INJECTED_EXCEPTION_MESSAGE_SCHEDULER_KEY);
                scheduler.addJobListener(new MessageServiceExecutorJobListener());
                return scheduler;
            } catch (Exception e) {
                throw new ConfigurationException(e);
            }
        }
        return super.createScheduler(schedulerFactory, schedulerName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        boolean useQuartzDatabase = new Boolean(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.USE_QUARTZ_DATABASE));
        if (useQuartzDatabase && !schedulerInjected) {
            // require a transaction manager
            if (jtaTransactionManager == null) {
                throw new ConfigurationException("No jta transaction manager was configured for the KSB Quartz Scheduler");
            }
            setTransactionManager(jtaTransactionManager);
            if (!nonTransactionalDataSourceSet) {
            	// since transaction manager is required... require a non transactional datasource
            	nonTransactionalDataSource = KSBServiceLocator.getMessageNonTransactionalDataSource();
            	if (nonTransactionalDataSource == null) {
            		throw new ConfigurationException("No non-transactional data source was found but is required for the KSB Quartz Scheduler");
            	}
            	super.setNonTransactionalDataSource(nonTransactionalDataSource);
            }
            if (!dataSourceSet) {
            	dataSource = KSBServiceLocator.getMessageDataSource();
            }
            super.setDataSource(dataSource);
            if (transactionManagerSet && nonTransactionalDataSourceNull) {
                throw new ConfigurationException("A valid transaction manager was set but no non-transactional data source was found");
            }
        }
        super.afterPropertiesSet();
    }
    
    /**
     * This overridden method is used simply to keep track of whether the transactionManager property has been set
     * 
     * @see org.springframework.scheduling.quartz.SchedulerFactoryBean#setTransactionManager(org.springframework.transaction.PlatformTransactionManager)
     */
    @Override
    public void setTransactionManager(PlatformTransactionManager jtaTransactionManager) {
        transactionManagerSet = jtaTransactionManager != null;
        this.jtaTransactionManager = jtaTransactionManager;
    }
    
    /**
     * This overridden method is used to keep track of whether the non transactional data source is null
     * 
     * @see org.springframework.scheduling.quartz.SchedulerFactoryBean#setNonTransactionalDataSource(javax.sql.DataSource)
     */
    @Override
    public void setNonTransactionalDataSource(DataSource nonTransactionalDataSource) {
        nonTransactionalDataSourceSet = true;
        nonTransactionalDataSourceNull = (nonTransactionalDataSource == null);
        this.nonTransactionalDataSource = nonTransactionalDataSource;
    }
    
    @Override
    public void setDataSource(DataSource dataSource) {
    	dataSourceSet = true;
    	this.dataSource = dataSource;
    }
}
