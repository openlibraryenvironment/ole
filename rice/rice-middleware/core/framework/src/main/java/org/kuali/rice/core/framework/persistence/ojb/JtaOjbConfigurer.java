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
package org.kuali.rice.core.framework.persistence.ojb;

import javax.transaction.TransactionManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.orm.ojb.support.LocalOjbConfigurer;

/**
 * Utility bean that sets the JTA TransactionManager on the WorkflowTransactionManagerFactory, the
 * OJB TransactionManagerFactory implementation that makes this available from Workflow core.
 * @see TransactionManagerFactory
 * @see org.apache.ojb.broker.transaction.tm.TransactionManagerFactory
 */
public class JtaOjbConfigurer extends LocalOjbConfigurer implements InitializingBean, DisposableBean {
    private static final Logger LOG = Logger.getLogger(JtaOjbConfigurer.class);

	private TransactionManager transactionManager;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		super.setBeanFactory(beanFactory);
		RiceDataSourceConnectionFactory.addBeanFactory(beanFactory);
	}

	public void afterPropertiesSet() {
        LOG.debug("Setting OJB WorkflowTransactionManagerFactory transaction manager to: " + this.transactionManager);
        TransactionManagerFactory.setTransactionManager(this.transactionManager);
	}

	public void destroy() {
	    this.transactionManager = null;
	}

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}
