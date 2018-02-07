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
package org.kuali.rice.ksb.api.bus.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.security.credentials.CredentialsType;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.api.bus.ServiceDefinition;

import javax.xml.namespace.QName;
import java.net.URL;


/**
 * The definition of a service on the service bus.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class AbstractServiceDefinition implements ServiceDefinition {

	private static final Logger LOG = Logger.getLogger(AbstractServiceDefinition.class);
		
	// used internally to construct the service name
	private String localServiceName;
	private String serviceNameSpaceURI;
	
	private Object service;
	private QName serviceName;
	private boolean queue;
	private Integer priority;
	private Integer retryAttempts;
	private Long millisToLive;
	private String messageExceptionHandler;
	private String servicePath;
	private URL endpointUrl;
	private Boolean busSecurity;
	private CredentialsType credentialsType;
	private String serviceVersion;
	private String applicationId;
    private String instanceId;

	// if the service is exported from a plugin, we need to ensure it's invoked within the proper classloading context!
	private ClassLoader serviceClassLoader;

	protected AbstractServiceDefinition() {
		this.busSecurity = Boolean.TRUE;
		this.queue = true;
		this.serviceClassLoader = ClassLoaderUtils.getDefaultClassLoader();
	}

	private boolean basicAuthentication = false;

	/**
	 * @return the basicAuthentication
	 */
	public boolean isBasicAuthentication() {
		return this.basicAuthentication;
	}

	/**
	 * @param basicAuthentication the basicAuthentication to set
	 */
	public void setBasicAuthentication(boolean basicAuthentication) {
		this.basicAuthentication = basicAuthentication;
	}

	public Object getService() {
		return this.service;
	}
	public void setService(Object service) {
		this.service = service;
	}
	
	public String getLocalServiceName() {
		return this.localServiceName;
	}
	
	public void setLocalServiceName(String serviceName) {
		this.localServiceName = serviceName;
	}
	
	public String getMessageExceptionHandler() {
		return this.messageExceptionHandler;
	}
	
	public void setMessageExceptionHandler(String messageExceptionHandler) {
		this.messageExceptionHandler = messageExceptionHandler;
	}
	
	public Integer getPriority() {
		return this.priority;
	}
	
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public boolean isQueue() {
		return this.queue;
	}
	
	public void setQueue(boolean queue) {
		this.queue = queue;
	}
	
	public Integer getRetryAttempts() {
		return this.retryAttempts;
	}
	
	public void setRetryAttempts(Integer retryAttempts) {
		this.retryAttempts = retryAttempts;
	}

	public QName getServiceName() {
		if (this.serviceName == null) {
			if (this.serviceNameSpaceURI == null) {
			    return new QName(this.applicationId, this.localServiceName);	
			} else {
			    return new QName(this.serviceNameSpaceURI, this.localServiceName);
			}
			
		}
		return this.serviceName;
	}
	public void setServiceName(QName serviceName) {
		this.serviceName = serviceName;
	}
	
	@Override
	public URL getEndpointUrl() {
		return this.endpointUrl;
	}
	public void setEndpointUrl(URL endpointUrl) {
		this.endpointUrl = endpointUrl;
	}
	
	public void setCredentialsType(CredentialsType credentialsType) {
	    this.credentialsType = credentialsType;
	}
	
	public CredentialsType getCredentialsType() {
	    return this.credentialsType;
	}
	
	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public String getApplicationId() {
		return applicationId;
	}
	
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

    @Override
    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
	public ClassLoader getServiceClassLoader() {
		return this.serviceClassLoader;
	}
	
	public void setServiceClassLoader(ClassLoader serviceClassLoader) {
		this.serviceClassLoader = serviceClassLoader;
	}

	@Override
	public void validate() {
		
		if (this.serviceName == null && this.localServiceName == null) {
			throw new ConfigurationException("Must give a serviceName or localServiceName");
		}

		if (this.applicationId == null) {
			String applicationId = CoreConfigHelper.getApplicationId();
			if (applicationId == null) {
				throw new ConfigurationException("Must have an applicationId");
			}	
			this.applicationId = applicationId;
		}

        if (this.instanceId == null) {
			String instanceId = CoreConfigHelper.getInstanceId();
			if (instanceId == null) {
				throw new ConfigurationException("Must have an instanceId");
			}
			this.instanceId = instanceId;
		}
		
		if (this.serviceName != null && this.localServiceName == null) {
			this.localServiceName = this.getServiceName().getLocalPart();
		}
			
		if (this.servicePath != null){
			if (this.servicePath.endsWith("/")){
				this.servicePath = StringUtils.chop(servicePath);
			}
			if (!this.servicePath.startsWith("/")){
				this.servicePath = "/" + this.servicePath;
			}
		} else {
            // default the serivce path to namespace
			this.servicePath = "/";
		}
		
		// default to 'unspecified' service version
		if (StringUtils.isBlank(serviceVersion)) {
			setServiceVersion(CoreConstants.Versions.UNSPECIFIED);
		}

		LOG.debug("Validating service " + this.serviceName);
		
		
		if (this.endpointUrl == null) {
			String endPointURL = ConfigContext.getCurrentContextConfig().getEndPointUrl();
			if (endPointURL == null) {
				throw new ConfigurationException("Must provide a serviceEndPoint or serviceServletURL");
			}
			if (! endPointURL.endsWith("/")) {
				endPointURL += servicePath;
			} else {
				endPointURL = StringUtils.chop(endPointURL) + servicePath;
			}
			try {
				if (servicePath.equals("/")){
					this.endpointUrl = new URL(endPointURL + this.getServiceName().getLocalPart());
				} else {
					this.endpointUrl = new URL(endPointURL + "/" + this.getServiceName().getLocalPart());
				}
            } catch (Exception e) {
				throw new ConfigurationException("Service Endpoint URL creation failed.", e);
			}
			
		}
				
		if (this.priority == null) {
			setPriority(5);
		}
		
		if (this.retryAttempts == null) {
			setRetryAttempts(0);
		}
		
		if (this.millisToLive == null) {
			setMillisToLive(new Long(-1));
		}

	}
	
	@Override
	public Endpoint establishEndpoint() {
		return BasicEndpoint.newEndpoint(configure(), getService());
	}
	
	protected abstract ServiceConfiguration configure();
	
	public String getServiceNameSpaceURI() {
		return this.serviceNameSpaceURI;
	}
	public void setServiceNameSpaceURI(String serviceNameSpaceURI) {
		this.serviceNameSpaceURI = serviceNameSpaceURI;
	}
	public Long getMillisToLive() {
		return this.millisToLive;
	}
	public void setMillisToLive(Long millisToLive) {
		this.millisToLive = millisToLive;
	}
	public Boolean getBusSecurity() {
		return this.busSecurity;
	}
	public void setBusSecurity(Boolean busSecurity) {
		this.busSecurity = busSecurity;
	}
	
	/**
	 * @return the servicePath
	 */
	public String getServicePath() {
		return this.servicePath;
	}

	/**
	 * @param servicePath the servicePath to set
	 */
	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}
	
	public String toString() {
	    return ReflectionToStringBuilder.toString(this);
	}
	
	@Override
    public boolean equals(Object object) {
		return EqualsBuilder.reflectionEquals(object, this);
    }

	@Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

	
}
