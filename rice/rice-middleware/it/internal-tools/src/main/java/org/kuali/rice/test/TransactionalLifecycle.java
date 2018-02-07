/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.test;

import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * A lifecycle for testing with database transactional rollback.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TransactionalLifecycle implements Lifecycle {
	
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(TransactionalLifecycle.class);
	
    /**
     * Name of the transaction manager to pull from the GlobalResourceLoader.
     * This will most likely be a Spring bean name.
     */
    public static final String DEFAULT_TRANSACTION_MANAGER_NAME = "transactionManager";

    private String transactionManagerName;
    private PlatformTransactionManager transactionManager;

    public TransactionalLifecycle(String transactionManagerName) {
    	this.transactionManagerName = transactionManagerName;
    }
    
    public TransactionalLifecycle() {
    	this(DEFAULT_TRANSACTION_MANAGER_NAME);
    }
    
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
    	this.transactionManager = transactionManager;
    }
    
    /**
     * Returns the PlatformTransactionManager configured on this lifecycle.
     * If none defined, pulls the transaction manager out of the GlobalResourceLoader
     * @return the transaction manager in the GlobalResourceLoader
     */
    private PlatformTransactionManager getTransactionManager() {
    	if (transactionManager == null) {
    		transactionManager = (PlatformTransactionManager) GlobalResourceLoader.getService(transactionManagerName);
    	}
    	return transactionManager;
    }

    private boolean started;
    private TransactionStatus TRANSACTION_STATUS;

    public boolean isStarted() {
        return started;
    }

    public void start() throws Exception {
    	LOG.info("Starting a transaction from TransactionalLifecycle...");
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setTimeout(3600);
        TRANSACTION_STATUS = getTransactionManager().getTransaction(defaultTransactionDefinition);
        started = true;
    }

    public void stop() throws Exception {
    	LOG.info("...rolling back transaction from TransactionalLifecycle.");
        getTransactionManager().rollback(TRANSACTION_STATUS);
        started = false;
    }

}
