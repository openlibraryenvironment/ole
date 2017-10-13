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

import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.ksb.api.KsbApiConstants;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;

/**
 * Service definition for RESTful services.  A JAX-WS service has a resource
 * class, which is the class or interface marked by the JAX-WS annotations
 * (e.g. @Path, @GET, etc).  This may or may not be the implementation class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class RestServiceDefinition extends AbstractServiceDefinition {

    private static final long serialVersionUID = 5892163789061959602L;

	private String resourceClass;
	transient private List<Object> resources;
	private BidiMap resourceToClassNameMap;
	transient private List<Object> providers;
	transient private Map<Object, Object> extensionMappings;
	transient private Map<Object, Object> languageMappings;

	/**
	 * Default constructor.  Sets bus security to FALSE.
	 */
	public RestServiceDefinition() {
		setBusSecurity(false);
	}
	
	@Override
	public String getType() {
		return KsbApiConstants.ServiceTypes.REST;
	}

	/**
	 * To ensure transparency that RESTful services are not digitally signed, throw an exception
	 * if someone tries to enable bus security.
	 *
	 * @see org.kuali.rice.ksb.AbstractServiceDefinition.ServiceDefinition#setBusSecurity(java.lang.Boolean)
	 */
	@Override
	public void setBusSecurity(Boolean busSecurity) {
	    if (busSecurity == true) {
	        throw new RiceRuntimeException("Rice does not support bus security (digital request/response signing) " +
	        		"for RESTful services");
	    }
	    super.setBusSecurity(busSecurity);
	}

	/**
	 * Set the resourceClass, the class or interface marked by the JAX-WS annotations
	 * which specify the RESTful URL interface.
	 * @param resourceClass the resourceClass to set
	 */
	public void setResourceClass(String resourceClass) {
		this.resourceClass = resourceClass;
	}

	/**
	 * @see #setResourceClass(String)
	 * @return the resourceClass
	 */
	public String getResourceClass() {
		return this.resourceClass;
	}

	/**
	 * does some simple validation of this RESTServiceDefinition
	 *
	 * @see org.kuali.rice.ksb.AbstractServiceDefinition.ServiceDefinition#validate()
	 */
	@Override
	public void validate() {

		List<Object> resources = getResources();

		if (resources != null && !resources.isEmpty()) {
			resourceToClassNameMap = new DualHashBidiMap();
			for (Object resource : resources) {
				// If there is no service set then we have to assume that it's the first resource
				if (getService() == null) {
					setService(resource);
				}

				Class<?> resourceClass = resource.getClass();
				if (resourceClass != null) {
					Class<?>[] interfaces = null;

					if (resourceClass.isInterface()) {
						interfaces = new Class[1];
						interfaces[0] = resourceClass;
					} else {
						interfaces = resourceClass.getInterfaces();
					}

					if (interfaces != null) {
						for (Class<?> iface : interfaces) {
							Path pathAnnotation = (Path)iface.getAnnotation(Path.class);
							if (pathAnnotation != null) {
								String pathAnnotationValue = pathAnnotation.value();
								String resourceId = pathAnnotationValue == null || pathAnnotationValue.equals("/") ? iface.getSimpleName() : pathAnnotationValue;
								resourceToClassNameMap.put(resourceId, iface.getName());
							} else {
								// If no path annotation exists, use the simple class name
								resourceToClassNameMap.put(iface.getSimpleName(), iface.getName());
							}
						}
					}
				}
			}

		}
		
		super.validate();

		// if interface is null, set it to the service class
		if (getResourceClass() == null) {
			Class<?>[] interfaces = getService().getClass().getInterfaces();
			if (interfaces != null && interfaces.length > 0) {
				setResourceClass(interfaces[0].getName());
			} else {
			    throw new ConfigurationException("resource class must be set to export a REST service");
			}
		}

		// Validate that the JAX-WS annotated class / interface is available to the classloader.
		try {
		    Class.forName(getResourceClass());
		} catch (ClassNotFoundException e) {
		    throw new ConfigurationException(
		            "resource class '" + getResourceClass() + "' could not be found in the classpath");
		}

	}
	
	@Override
	protected ServiceConfiguration configure() {
		return RestServiceConfiguration.fromServiceDefinition(this);
	}

	/**
	 * @return the resources
	 */
	public List<Object> getResources() {
		return this.resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(List<Object> resources) {
		this.resources = resources;
	}

	/**
	 * @return the resourceToClassNameMap
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getResourceToClassNameMap() {
		return this.resourceToClassNameMap;
	}

	/**
	 * @param className
	 * @return true if this service contains a resource for the given class name
	 */
	public boolean hasClass(String className) {
		if (resourceToClassNameMap == null) return false;
		return resourceToClassNameMap.containsValue(className);
	}

	/**
	 * @return the providers
	 */
	public List<Object> getProviders() {
		return this.providers;
	}

	/**
	 * @param providers the providers to set
	 */
	public void setProviders(List<Object> providers) {
		this.providers = providers;
	}

	/**
	 * @return the extensionMappings
	 */
	public Map<Object, Object> getExtensionMappings() {
		return this.extensionMappings;
	}

	/**
	 * @param extensionMappings the extensionMappings to set
	 */
	public void setExtensionMappings(Map<Object, Object> extensionMappings) {
		this.extensionMappings = extensionMappings;
	}

	/**
	 * @return the languageMappings
	 */
	public Map<Object, Object> getLanguageMappings() {
		return this.languageMappings;
	}

	/**
	 * @param languageMappings the languageMappings to set
	 */
	public void setLanguageMappings(Map<Object, Object> languageMappings) {
		this.languageMappings = languageMappings;
	}



}
