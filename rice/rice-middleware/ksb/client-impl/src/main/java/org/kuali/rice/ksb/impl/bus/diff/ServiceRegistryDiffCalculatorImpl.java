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
package org.kuali.rice.ksb.impl.bus.diff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.ksb.api.registry.ServiceInfo;
import org.kuali.rice.ksb.api.registry.ServiceRegistry;
import org.kuali.rice.ksb.impl.bus.LocalService;
import org.kuali.rice.ksb.impl.bus.RemoteService;

/**
 * Default implementation of the {@link ServiceRegistryDiffCalculator} which calculates
 * differences between client service bus state and service registry state.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ServiceRegistryDiffCalculatorImpl implements ServiceRegistryDiffCalculator {

	private static final Logger LOG = Logger.getLogger(ServiceRegistryDiffCalculatorImpl.class);
	
	private ServiceRegistry serviceRegistry;
	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	
	@Override
	public CompleteServiceDiff diffServices(String instanceId, List<LocalService> localServices, List<RemoteService> clientRegistryCache) {
        List<ServiceInfo> allRegistryServices = serviceRegistry.getAllOnlineServices();
        List<ServiceInfo> allRegistryServicesForInstance = serviceRegistry.getAllServicesForInstance(instanceId);
		LocalServicesDiff localServicesDiff = calculateLocalServicesDiff(allRegistryServicesForInstance, instanceId, localServices);
		RemoteServicesDiff remoteServicesDiff = calculateRemoteServicesDiff(allRegistryServices, clientRegistryCache);
		return new CompleteServiceDiff(localServicesDiff, remoteServicesDiff);
	}

	protected LocalServicesDiff calculateLocalServicesDiff(List<ServiceInfo> allRegistryServicesForInstance, String instanceId, List<LocalService> localServices) {
		
		List<ServiceInfo> servicesToRemoveFromRegistry = new ArrayList<ServiceInfo>();
		List<LocalService> localServicesToPublish = new ArrayList<LocalService>();
		Map<LocalService, ServiceInfo> localServicesToUpdate = new HashMap<LocalService, ServiceInfo>();
		
		Map<QName, LocalService> localServiceIndex = indexLocalServices(instanceId, localServices);
		for (ServiceInfo serviceInfo : allRegistryServicesForInstance) {
			// first validate that the service has a valid instance id
			if (!instanceId.equals(serviceInfo.getInstanceId())) {
                StringBuffer errorMessage = new StringBuffer("ServiceInfo given for local service diff does not have a valid instance id.  Should have been '" + instanceId + "' but was '" + serviceInfo.getInstanceId() + "'");
                if (serviceInfo.getInstanceId() == null) {
                    errorMessage.append(" Null instanceIds can be the result of multiple asm.jars or none in the classpath.");
                }
				throw new IllegalArgumentException(errorMessage.toString());
			}
			LocalService localService = localServiceIndex.get(serviceInfo.getServiceName());
			if (localService == null) {
				// this means the registry has the service but there is no local service, it has been unregistered
				servicesToRemoveFromRegistry.add(serviceInfo);
			} else {
				// if the LocalService is not null, that means that it exists but it may have changed, or this may be the first time the service
				// is being published upon startup in which case it's service id will be null
				if (!localService.getServiceEndpoint().getInfo().equals(serviceInfo)) {
					// if the service infos don't match, that means we need to re-publish our current copy of the local service
					localServicesToUpdate.put(localService, serviceInfo);
				}
				// whether or not it matches, remove it from the index
				localServiceIndex.remove(serviceInfo.getServiceName());
			}
		}
		// what's left in the localServiceIndex will be services that weren't in the registry at all, they need to be published
		localServicesToPublish.addAll(localServiceIndex.values());
		
		if (LOG.isDebugEnabled()) {
			LOG.info("For instance '" + instanceId + "', found " + servicesToRemoveFromRegistry.size() + " services to remove from registry, "+
				localServicesToPublish.size() + " local services to publish");
		}
		
		return new LocalServicesDiff(servicesToRemoveFromRegistry, localServicesToPublish, localServicesToUpdate);
				
	}
	
	private Map<QName, LocalService> indexLocalServices(String instanceId, List<LocalService> localServices) {
		Map<QName, LocalService> localServiceIndex = new HashMap<QName, LocalService>(localServices.size());
		for (LocalService localService : localServices) {
			String localServiceInstanceId = localService.getServiceEndpoint().getInfo().getInstanceId(); 
			if (!instanceId.equals(localServiceInstanceId)) {
				throw new IllegalStateException("Instance id of local service (" + localServiceInstanceId + ") does not match instance id given to the diff calculator (" + instanceId + ")");
			}
			localServiceIndex.put(localService.getServiceName(), localService);
		}
		return localServiceIndex;
	}
		
	protected RemoteServicesDiff calculateRemoteServicesDiff(List<ServiceInfo> allRegistryServices, List<RemoteService> clientRegistryCache) {

        Map<String, ServiceInfo> indexedRegistryServices = indexRegistryServices(allRegistryServices);
		Map<String, ServiceInfo> servicesToAddToClientRegistryCache = new HashMap<String, ServiceInfo>(indexedRegistryServices);
		List<RemoteService> servicesToRemoveFromClientRegistryCache = new ArrayList<RemoteService>();

		for (RemoteService remoteService : clientRegistryCache) {
			ServiceInfo indexedRegistryService = indexedRegistryServices.get(remoteService.getServiceInfo().getServiceId());
			if (indexedRegistryService == null) {
				servicesToRemoveFromClientRegistryCache.add(remoteService);
			} else {
				if (!remoteService.getServiceInfo().getChecksum().equals(indexedRegistryService.getChecksum())) {
					servicesToRemoveFromClientRegistryCache.add(remoteService);
				} else {
					servicesToAddToClientRegistryCache.remove(remoteService.getServiceInfo().getServiceId());
				}
			}
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("For instance found " + servicesToRemoveFromClientRegistryCache.size() + " services to remove from client registry cache, "+
				servicesToAddToClientRegistryCache.size() + " services to add to client registry cache");
		}
		
		return new RemoteServicesDiff(new ArrayList<ServiceInfo>(servicesToAddToClientRegistryCache.values()),
                servicesToRemoveFromClientRegistryCache);
	}
	
	private Map<String, ServiceInfo> indexRegistryServices(List<ServiceInfo> allRegistryServices) {
		Map<String, ServiceInfo> indexedRegistryServices = new HashMap<String, ServiceInfo>(allRegistryServices.size());
		for (ServiceInfo serviceInfo : allRegistryServices) {
			indexedRegistryServices.put(serviceInfo.getServiceId(), serviceInfo);
		}
		return indexedRegistryServices;
	}

}
