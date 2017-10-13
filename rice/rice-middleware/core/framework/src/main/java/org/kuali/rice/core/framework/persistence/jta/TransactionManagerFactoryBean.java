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
package org.kuali.rice.core.framework.persistence.jta;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.RiceConstants;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jndi.JndiTemplate;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;


/**
 * Factory bean that supplies a TransactionManager object from the the current context
 * (i.e. plugin, embedding webapp) Config object map if defined therein (under the Config.TRANSACTION_MANAGER_OBJ key),
 * from JNDI if {@link Config#TRANSACTION_MANAGER_JNDI} is defined,
 * or from a default declaratively assigned in containing bean factory.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TransactionManagerFactoryBean implements FactoryBean {

	private TransactionManager defaultTransactionManager;
	private JndiTemplate jndiTemplate;
	
	public Object getObject() throws Exception {
		
		if (ConfigContext.getCurrentContextConfig().getObject(RiceConstants.SPRING_TRANSACTION_MANAGER) != null) {
			return null;
		}
		
		TransactionManager transactionManager =  (TransactionManager)ConfigContext.getCurrentContextConfig().getObject(RiceConstants.TRANSACTION_MANAGER_OBJ);
		if (transactionManager == null) {
			String transactionManagerJndiName = ConfigContext.getCurrentContextConfig().getProperty(RiceConstants.TRANSACTION_MANAGER_JNDI);
			if (!StringUtils.isEmpty(transactionManagerJndiName)) {
				if (this.jndiTemplate == null) {
				    this.jndiTemplate = new JndiTemplate();
				}
				try {
					transactionManager = (TransactionManager)this.jndiTemplate.lookup(transactionManagerJndiName, TransactionManager.class);
				} catch (NamingException e) {
					throw new ConfigurationException("Could not locate the TransactionManager at the given JNDI location: '" + transactionManagerJndiName + "'", e);
				}
			}
			
		}
		if (transactionManager != null) {
			return transactionManager;
		}
		return this.defaultTransactionManager;
	}

	public Class getObjectType() {
		return TransactionManager.class;
	}

	public boolean isSingleton() {
		return true;
	}
	
	public void setDefaultTransactionManager(TransactionManager transactionManager) {
	    this.defaultTransactionManager = transactionManager;
	}

	public JndiTemplate getJndiTemplate() {
		return this.jndiTemplate;
	}

	public void setJndiTemplate(JndiTemplate jndiTemplate) {
		this.jndiTemplate = jndiTemplate;
	}

}
