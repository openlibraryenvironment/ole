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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.framework.util.ApplicationThreadLocal;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import javax.xml.namespace.QName;

/**
 * Exports services in the {@link org.kuali.rice.core.api.resourceloader.GlobalResourceLoader} as beans available to Spring.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class GlobalResourceLoaderServiceFactoryBean implements FactoryBean<Object>, InitializingBean {

    private String serviceNamespace;
	private String serviceName;
	private boolean singleton;
	private boolean mustExist;

	// used to prevent a stack overflow when trying to get the service
	private ThreadLocal<Boolean> isFetchingService = new ApplicationThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
	};
	
	public GlobalResourceLoaderServiceFactoryBean() {
		this.mustExist = true;
	}
	
	public Object getObject() throws Exception {
		if (isFetchingService.get()) return null; // we already have been invoked, don't recurse, just return null.
		isFetchingService.set(true);
		try {
            Object service = null;
            if (StringUtils.isBlank(getServiceNamespace())) {
			    service = GlobalResourceLoader.getService(this.getServiceName());
            } else {
                service = GlobalResourceLoader.getService(new QName(getServiceNamespace(), getServiceName()));
            }
			if (mustExist && service == null) {
				throw new IllegalStateException("Service must exist and no service could be located with serviceNamespace='" + getServiceNamespace() + "' and name='" + this.getServiceName() + "'");
			}
			return service;
		} finally {
			isFetchingService.remove();
		}
	}

	public Class<?> getObjectType() {
		return Object.class;
	}

	public boolean isSingleton() {
		return singleton;
	}

    public String getServiceNamespace() {
        return serviceNamespace;
    }

    public void setServiceNamespace(String serviceNamespace) {
        this.serviceNamespace = serviceNamespace;
    }

    public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}
	
	public boolean isMustExist() {
		return mustExist;
	}
	
	public void setMustExist(boolean mustExist) {
		this.mustExist = mustExist;
	}
	

	public void afterPropertiesSet() throws Exception {
		if (StringUtils.isBlank(this.getServiceName())) {
			throw new ConfigurationException("No serviceName given.");
		}
	}

}
