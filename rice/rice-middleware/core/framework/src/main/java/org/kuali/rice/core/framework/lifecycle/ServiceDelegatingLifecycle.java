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
package org.kuali.rice.core.framework.lifecycle;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

/**
 * A lifecycle that wraps a service.  This fetches and calls a lifecycle available
 * in the GRL and calls lifecycle methods on that.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ServiceDelegatingLifecycle extends BaseLifecycle {
	
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BaseLifecycle.class);

	private QName serviceName;
    private Lifecycle service;

	public ServiceDelegatingLifecycle(QName serviceName) {
		this.serviceName = serviceName;
	}

	public ServiceDelegatingLifecycle(String serviceName) {
		this(new QName(serviceName));
	}

	public void start() throws Exception {
	    if (!isStarted()) {
	        this.service = loadService(this.serviceName);
            if (this.service == null) {
                throw new IllegalStateException("Failed to load the service with the given name: " + this.serviceName);
            }
            this.service.start();
	    }
		super.start();
	}

	public void stop() throws Exception {
	    if (isStarted()) {
	    	try {
	    		if (this.service == null) {
	    			LOG.warn("Couldn't stop service, failed to locate service with name " + serviceName);
	    		} else {
	    			this.service.stop();
	    		}
	    	} catch (Exception e) {
	    		LOG.warn("couldn't stop service " + this.serviceName, e);
	    	}
	    }
        this.service = null;
		super.stop();
	}

	protected Lifecycle loadService(QName serviceName) {
	    Object service = GlobalResourceLoader.getService(serviceName);
	    if (service == null) {
	        return null;
	    }
	    if (!(service instanceof Lifecycle)) {
	        throw new IllegalStateException("Service with name " + serviceName + " does not implement the Lifecycle interface!");
	    }
	    return (Lifecycle) service;
	}

}
