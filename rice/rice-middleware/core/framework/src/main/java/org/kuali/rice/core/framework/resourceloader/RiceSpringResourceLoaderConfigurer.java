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
package org.kuali.rice.core.framework.resourceloader;

import java.util.Set;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * Wraps {@link BeanFactory} in {@link BeanFactoryResourceLoader} and places the {@link ResourceLoader} 
 * at the top of the {@link GlobalResourceLoader} stack.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RiceSpringResourceLoaderConfigurer implements BeanFactoryAware, InitializingBean {
	
	private QName name;
	private String localServiceName;
	private String serviceNameSpaceURI;
	private Set<String> beanNames;

	private BeanFactory beanFactory;

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void afterPropertiesSet() throws Exception {
		if (this.name == null) {
			if (this.getServiceNameSpaceURI() == null) {
				this.setServiceNameSpaceURI(CoreConfigHelper.getApplicationId());
			}
			if (this.getLocalServiceName() == null) {
				throw new ConfigurationException("Need to give " + this.getClass().getName() + " a LocalServiceName");
			}
		}
		
		ResourceLoader beanFactoryRL = new BeanFactoryResourceLoader(getName(), this.beanFactory, this.beanNames);
		GlobalResourceLoader.addResourceLoaderFirst(beanFactoryRL);
	}

	public QName getName() {
		if (this.name == null) {
			this.setName(new QName(this.getServiceNameSpaceURI(), this.getLocalServiceName()));
		}
		return name;
	}

	public void setName(QName name) {
		this.name = name;
	}
	
	public String getLocalServiceName() {
		return localServiceName;
	}

	public void setLocalServiceName(String localServiceName) {
		this.localServiceName = localServiceName;
	}

	public String getServiceNameSpaceURI() {
		return serviceNameSpaceURI;
	}

	public void setServiceNameSpaceURI(String serviceNameSpaceURI) {
		this.serviceNameSpaceURI = serviceNameSpaceURI;
	}
	
	public void setBeanNames(Set<String> beanNames) {
		this.beanNames = beanNames;
	}

}
