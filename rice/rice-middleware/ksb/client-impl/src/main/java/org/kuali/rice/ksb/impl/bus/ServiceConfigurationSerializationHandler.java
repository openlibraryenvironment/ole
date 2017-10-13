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
package org.kuali.rice.ksb.impl.bus;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.api.bus.support.JavaServiceConfiguration;
import org.kuali.rice.ksb.api.bus.support.RestServiceConfiguration;
import org.kuali.rice.ksb.api.bus.support.SoapServiceConfiguration;

/**
 * This class is meant to be a temporary implementation of serialization operations
 * for {@link ServiceConfiguration} classes.  Coming into Rice 2.0, there is not a
 * "pluggable" spi for dealing with custom service configurations, definitions,
 * connectors, and exporters.  So for now everything is hardcoded and this
 * class creates a hardcoded JAXBContext to perform marshalling/unmarshalling
 * of the standard {@link ServiceConfiguration} classes.
 * 
 * <p>NOTE: JAXBContext is thread-safe, but marshaller/unmarshaller are *NOT* thread-safe.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ServiceConfigurationSerializationHandler {

	private static final Set<Class<?>> CONFIG_CLASSES_SET = new HashSet<Class<?>>();
	static {
		CONFIG_CLASSES_SET.add(JavaServiceConfiguration.class);
		CONFIG_CLASSES_SET.add(SoapServiceConfiguration.class);
		CONFIG_CLASSES_SET.add(RestServiceConfiguration.class);
	}
	
	public static String marshallToXml(ServiceConfiguration serviceConfiguration) {
		if (serviceConfiguration == null) {
			throw new IllegalArgumentException("serviceConfiguration was null");
		}
		if (!CONFIG_CLASSES_SET.contains(serviceConfiguration.getClass())) {
			throw new IllegalArgumentException("Illegal ServiceConfiguration class: " + serviceConfiguration.getClass());
		}
		StringWriter writer = new StringWriter();
		try {
			getContext().createMarshaller().marshal(serviceConfiguration, writer);
		} catch (JAXBException e) {
			throw new RiceRuntimeException("Failed to marshall ServiceConfiguration to XML: " + serviceConfiguration, e);
		}
		return writer.toString();
	}
	
	public static ServiceConfiguration unmarshallFromXml(String xml) {
		if (StringUtils.isBlank(xml)) {
			throw new IllegalArgumentException("xml was null or blank");
		}
		try {
			Object unmarshalled = getContext().createUnmarshaller().unmarshal(new StringReader(xml));
			if (!(unmarshalled instanceof ServiceConfiguration)) {
				throw new RiceRuntimeException("Unmarshalled value was not a valid ServiceConfiguration: " + unmarshalled.getClass());
			}
			return (ServiceConfiguration)unmarshalled;
		} catch (JAXBException e) {
			throw new RiceRuntimeException("Failed to unmarhsal ServiceConfiguration from XML: " + xml, e);
		}
	}
	
	private static JAXBContext getContext() {
		return ContextHolder.context;
	}
	
	/**
	 * Implements the lazy initialization holder class idiom as per Effective Java item 71.
	 */
	private static class ContextHolder {
		private static final Class<?>[] CONFIG_CLASSES_ARRAY = CONFIG_CLASSES_SET.toArray(new Class<?>[0]);
		static final JAXBContext context;
		static {
			try {
				context = JAXBContext.newInstance(CONFIG_CLASSES_ARRAY);
			} catch (JAXBException e) {
				throw new RiceRuntimeException(e);
			}
		}
	}
	
}
