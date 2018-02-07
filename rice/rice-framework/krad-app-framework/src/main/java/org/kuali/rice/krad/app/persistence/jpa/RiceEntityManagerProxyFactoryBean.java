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
package org.kuali.rice.krad.app.persistence.jpa;

import org.kuali.rice.core.framework.persistence.jpa.NullEntityManagerFactory;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RiceEntityManagerProxyFactoryBean implements FactoryBean, InitializingBean {

	private RiceLocalContainerEntityManagerFactoryBean factoryBean;
	private String prefix;
	private DataSource datasource;
	private String moduleJpaEnabledPropertyPrefix;
	
	public RiceEntityManagerProxyFactoryBean(String prefix, DataSource datasource) {
		this.prefix = prefix;
		this.datasource = datasource;
		this.moduleJpaEnabledPropertyPrefix = prefix;
	}
		
	public RiceEntityManagerProxyFactoryBean(String prefix, DataSource datasource, String moduleJpaEnabledPropertyPrefix) {
		this.prefix = prefix;
		this.datasource = datasource;
		this.moduleJpaEnabledPropertyPrefix = moduleJpaEnabledPropertyPrefix;
	}	

	public void afterPropertiesSet() throws Exception {
		if (OrmUtils.isJpaEnabled(moduleJpaEnabledPropertyPrefix)) {
			factoryBean = new RiceLocalContainerEntityManagerFactoryBean(prefix, datasource);
			factoryBean.afterPropertiesSet();
		}
	}
	
	public Class getObjectType() {
		return (factoryBean != null ? factoryBean.getObjectType() : EntityManagerFactory.class);
	}

	public Object getObject() throws Exception {
		return (factoryBean != null ? factoryBean.getObject() : new NullEntityManagerFactory());
	}

	public boolean isSingleton() {
		return true;
	}

}
