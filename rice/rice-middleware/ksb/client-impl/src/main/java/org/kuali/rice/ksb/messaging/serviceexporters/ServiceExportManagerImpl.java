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
package org.kuali.rice.ksb.messaging.serviceexporters;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.ServerRegistry;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.api.bus.ServiceDefinition;

import javax.xml.namespace.QName;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServiceExportManagerImpl implements ServiceExportManager {

	private final ConcurrentMap<QName, ExportedServiceHolder> exportedServices;
	private final ServiceNameFinder serviceNameFinder;
	
	private Bus cxfBus;

	public ServiceExportManagerImpl() {
		this.exportedServices = new ConcurrentHashMap<QName, ExportedServiceHolder>();
		this.serviceNameFinder = new ServiceNameFinder();
	}
	
	@Override
	public QName getServiceName(String endpointUrl) {
		return getServiceNameFinder().lookup(endpointUrl);
	}
	
	protected ServiceNameFinder getServiceNameFinder() {
		return serviceNameFinder;
	}
	
	@Override
	public Object getService(QName serviceName) {
		ExportedServiceHolder holder = exportedServices.get(serviceName);
		if (holder == null) {
			return null;
		}
		return holder.getExportedService();
	}
	
	@Override
	public void exportService(ServiceDefinition serviceDefinition) {
		if (serviceDefinition == null) {
			throw new IllegalArgumentException("serviceDefinition was null");
		}
		ServiceExporter serviceExporter = ServiceExporterFactory.getServiceExporter(serviceDefinition, cxfBus);
		Object exportedService = serviceExporter.exportService(serviceDefinition);
		exportedServices.put(serviceDefinition.getServiceName(), new ExportedServiceHolder(exportedService, serviceDefinition));
		getServiceNameFinder().register(serviceDefinition);
	}

	@Override
	public void removeService(QName serviceName) {
		ExportedServiceHolder exportedServiceHolder = exportedServices.remove(serviceName);
		getServiceNameFinder().remove(exportedServiceHolder.getServiceDefinition().getEndpointUrl());
	}
		
	protected ConcurrentMap<QName, ExportedServiceHolder> getExportedServices() {
		return exportedServices;
	}
	
	public void setCxfBus(Bus cxfBus) {
		this.cxfBus = cxfBus;
	}

    /**
     * @deprecated setting ServerRegistry here has no effect, the ServerRegistry extension on the CXF Bus is used instead
     */
    @Deprecated
	public void setCxfServerRegistry(ServerRegistry cxfServerRegistry) {
        // no-op, see deprecation information
	}
	
	protected static class ExportedServiceHolder {
		
		private final Object exportedService;
		private final ServiceDefinition serviceDefinition;
		
		ExportedServiceHolder(Object exportedService, ServiceDefinition serviceDefinition) {
			this.exportedService = exportedService;
			this.serviceDefinition = serviceDefinition;
		}
		
		public Object getExportedService() {
			return exportedService;
		}
		
		public ServiceDefinition getServiceDefinition() {
			return serviceDefinition;
		}
		
	}
	
	/**
	 * Looks up service QNameS based on URL StringS.  API is Map-like, but non-service specific portions of the
	 * URL are trimmed prior to accessing its internal Map.
	 * 
	 * @author Kuali Rice Team (rice.collab@kuali.org)
	 *
	 */
	protected static class ServiceNameFinder {
	    
		/**
		 * A service path to service QName map
		 */
		private ConcurrentMap<String, QName> servicePathToQName = new ConcurrentHashMap<String, QName>();
		

		/**
		 * This method trims the endpoint url base ({@link Config#getEndPointUrl()}) base off of the full service URL, e.g.
		 * "http://kuali.edu/kr-dev/remoting/SomeService" -> "SomeService".  It makes an effort to do so even if the host
		 * and ip don't match what is in {@link Config#getEndPointUrl()} by stripping host/port info.
		 * 
		 * If the service URL contains the configured subpath for RESTful service, additional trimming is done to
		 * isolate the service name from the rest of the url.
		 * 
		 * @param url
		 * @return the service specific suffix.  If fullServiceUrl doesn't contain the endpoint url base,
		 * fullServiceUrl is returned unmodified.  
		 */
		private String trimServiceUrlBase(String url) {
			String trimmedUrl = StringUtils.removeStart(url, ConfigContext.getCurrentContextConfig().getEndPointUrl());
			
			if (trimmedUrl.length() == url.length()) { // it didn't contain the endpoint url base.
				// Perhaps the incoming url has a different host (or the ip) or a different port.
				// Trim off the host & port, then trim off the common base.
				URI serviceUri = URI.create(url);
				URI endpointUrlBase = URI.create(ConfigContext.getCurrentContextConfig().getEndPointUrl());
				
				String reqPath = serviceUri.getPath();
				String basePath = endpointUrlBase.getPath();
				
				trimmedUrl = StringUtils.removeStart(reqPath, basePath);
			}
			
			return trimmedUrl;
		}
		
		/**
		 * adds a mapping from the service specific portion of the service URL to the service name.
		 */
		public void register(ServiceDefinition serviceDefinition) {
			String serviceUrlBase = trimServiceUrlBase(serviceDefinition.getEndpointUrl().toExternalForm());
			if (serviceUrlBase.endsWith("/")) {
				serviceUrlBase = StringUtils.chop(serviceUrlBase);
			}
			servicePathToQName.put(serviceUrlBase, serviceDefinition.getServiceName());
		}
		
		/**
		 * removes the mapping (if one exists) for the service specific portion of this url.
		 */
		public void remove(URL endpointUrl) {
			servicePathToQName.remove(trimServiceUrlBase(endpointUrl.toExternalForm()));
		}
		
		/**
		 * gets the QName for the service
		 * 
		 * @param serviceUrl
		 * @return
		 */
		public QName lookup(String serviceUrl) {
			String serviceUrlBase = trimServiceUrlBase(serviceUrl);

			// First, make sure we don't have any query params
			if (serviceUrlBase.length() > 0 && serviceUrlBase.lastIndexOf('?') != -1) {
				serviceUrlBase = serviceUrlBase.substring(0, serviceUrlBase.lastIndexOf('?'));
			}

			QName qname = null;
			// Now, iterate backwards through the url, stripping off pieces until you match -- this should work for rest too
			while (qname == null) {
				qname = servicePathToQName.get(serviceUrlBase);

				int lastSeparatorIndex = serviceUrlBase.lastIndexOf('/');
				if (lastSeparatorIndex == -1)
					break;
				serviceUrlBase = serviceUrlBase.substring(0, lastSeparatorIndex);
			}

			return qname;
		}

	}

}
