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
package org.kuali.rice.ksb.impl.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.ksb.api.registry.RemoveAndPublishResult;
import org.kuali.rice.ksb.api.registry.ServiceDescriptor;
import org.kuali.rice.ksb.api.registry.ServiceEndpoint;
import org.kuali.rice.ksb.api.registry.ServiceEndpointStatus;
import org.kuali.rice.ksb.api.registry.ServiceInfo;
import org.kuali.rice.ksb.api.registry.ServiceRegistry;

/**
 * Reference implementation of the {@link ServiceRegistry} which is backed by a
 * data access object that handles reading and writing data related to registry
 * entries from a backend datastore.
 * 
 * <p>In order for this class to function properly, a valid {@link ServiceRegistryDao}
 * must be injected into it via the {@link #setServiceRegistryDao(ServiceRegistryDao)}
 * method.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ServiceRegistryImpl implements ServiceRegistry {

	private ServiceRegistryDao serviceRegistryDao;

	@Override
	public List<ServiceInfo> getOnlineServicesByName(QName serviceName)
			throws RiceIllegalArgumentException {
		if (serviceName == null) {
			throw new RiceIllegalArgumentException("serviceName cannot be null");
		}
		List<ServiceInfoBo> serviceInfoBos = serviceRegistryDao.getOnlineServiceInfosByName(serviceName);
		return convertServiceInfoBoList(serviceInfoBos);
	}

	@Override
	public List<ServiceInfo> getAllOnlineServices() {
		List<ServiceInfoBo> serviceInfoBos = serviceRegistryDao.getAllOnlineServiceInfos();
		return convertServiceInfoBoList(serviceInfoBos);
	}
	
	@Override
	public List<ServiceInfo> getAllServices() {
		List<ServiceInfoBo> serviceInfoBos = serviceRegistryDao.getAllServiceInfos();
		return convertServiceInfoBoList(serviceInfoBos);
	}
	
	@Override
	public List<ServiceInfo> getAllServicesForInstance(String instanceId) throws RiceIllegalArgumentException {
		if (StringUtils.isBlank(instanceId)) {
			throw new RiceIllegalArgumentException("instanceId cannot be blank");
		}
		List<ServiceInfoBo> serviceInfoBos = serviceRegistryDao.getAllServiceInfosForInstance(instanceId);
		return convertServiceInfoBoList(serviceInfoBos);
	}

    @Override
    public List<ServiceInfo> getAllServicesForApplication(String applicationId) throws RiceIllegalArgumentException {
        if (StringUtils.isBlank(applicationId)) {
            throw new RiceIllegalArgumentException("applicationId cannot be blank");
        }
        List<ServiceInfoBo> serviceInfoBos = serviceRegistryDao.getAllServiceInfosForApplication(applicationId);
        return convertServiceInfoBoList(serviceInfoBos);
    }

	@Override
	public ServiceDescriptor getServiceDescriptor(String serviceDescriptorId)
			throws RiceIllegalArgumentException {
		if (StringUtils.isBlank(serviceDescriptorId)) {
			throw new RiceIllegalArgumentException("serviceDescriptorId cannot be blank");
		}
		ServiceDescriptorBo serviceDescriptorBo = serviceRegistryDao.getServiceDescriptor(serviceDescriptorId);
		return ServiceDescriptorBo.to(serviceDescriptorBo);
	}

	@Override
	public List<ServiceDescriptor> getServiceDescriptors(List<String> serviceDescriptorIds)
			throws RiceIllegalArgumentException {
		if (serviceDescriptorIds == null) {
			throw new RiceIllegalArgumentException("serviceDescriptorIds cannot be null");
		}
		List<ServiceDescriptor> serviceDescriptors = new ArrayList<ServiceDescriptor>();
		for (String serviceDescriptorId : serviceDescriptorIds) {
			ServiceDescriptor serviceDescriptor = getServiceDescriptor(serviceDescriptorId);
			if (serviceDescriptor != null) {
				serviceDescriptors.add(serviceDescriptor);
			}
		}
		return Collections.unmodifiableList(serviceDescriptors);
	}

	@Override
	public ServiceEndpoint publishService(ServiceEndpoint serviceEndpoint)
			throws RiceIllegalArgumentException {
		if (serviceEndpoint == null) {
			throw new RiceIllegalArgumentException("serviceEndpoint cannot be null");
		}
		ServiceDescriptor serviceDescriptor = serviceEndpoint.getDescriptor();
		ServiceDescriptorBo serviceDescriptorBo = ServiceDescriptorBo.from(serviceDescriptor);
		ServiceInfo serviceInfo = serviceEndpoint.getInfo();
		ServiceInfoBo serviceInfoBo = ServiceInfoBo.from(serviceInfo);
		serviceDescriptorBo = serviceRegistryDao.saveServiceDescriptor(serviceDescriptorBo);
		serviceInfoBo.setServiceDescriptorId(serviceDescriptorBo.getId());
		serviceRegistryDao.saveServiceInfo(serviceInfoBo);
		
		
		return ServiceEndpoint.Builder.create(ServiceInfo.Builder.create(serviceInfoBo),
				ServiceDescriptor.Builder.create(serviceDescriptorBo)).build();
	}

	@Override
	public List<ServiceEndpoint> publishServices(List<ServiceEndpoint> serviceEndpoints)
			throws RiceIllegalArgumentException {
		if (serviceEndpoints == null) {
			throw new RiceIllegalArgumentException("serviceEndpoints cannot be null");
		}
		List<ServiceEndpoint> publishedEndpoints = new ArrayList<ServiceEndpoint>();
		for (ServiceEndpoint serviceEndpoint : serviceEndpoints) {
			publishedEndpoints.add(publishService(serviceEndpoint));
		}
		return publishedEndpoints;
	}

	@Override
	public ServiceEndpoint removeServiceEndpoint(String serviceId)
			throws RiceIllegalArgumentException {
		if (StringUtils.isBlank(serviceId)) {
			throw new RiceIllegalArgumentException("serviceId cannot be blank");
		}
		ServiceInfoBo serviceInfoBo = serviceRegistryDao.getServiceInfo(serviceId);
		if (serviceInfoBo != null) {
			ServiceDescriptorBo serviceDescriptorBo = serviceRegistryDao.getServiceDescriptor(serviceInfoBo.getServiceDescriptorId());
			ServiceEndpoint endpointPriorRemoval = ServiceEndpoint.Builder.create(ServiceInfo.Builder.create(serviceInfoBo),
					ServiceDescriptor.Builder.create(serviceDescriptorBo)).build();
			serviceRegistryDao.removeServiceInfo(serviceInfoBo.getServiceId());
			serviceRegistryDao.removeServiceDescriptor(serviceInfoBo.getServiceDescriptorId());
			return endpointPriorRemoval;
		}
		return null;
	}

	@Override
	public List<ServiceEndpoint> removeServiceEndpoints(List<String> serviceIds)
			throws RiceIllegalArgumentException {
		if (serviceIds == null) {
			throw new RiceIllegalArgumentException("serviceIds canot be null");
		}
		List<ServiceEndpoint> servicesRemoved = new ArrayList<ServiceEndpoint>();
		for (String serviceId : serviceIds) {
			servicesRemoved.add(removeServiceEndpoint(serviceId));
		}
		return servicesRemoved;
	}

	@Override
	public RemoveAndPublishResult removeAndPublish(List<String> removeServiceIds,
			List<ServiceEndpoint> publishServiceEndpoints) {
		List<ServiceEndpoint> servicesRemoved = new ArrayList<ServiceEndpoint>();
		List<ServiceEndpoint> servicesPublished = new ArrayList<ServiceEndpoint>();
		if (removeServiceIds != null && !removeServiceIds.isEmpty()) {
			servicesRemoved = removeServiceEndpoints(removeServiceIds);
		}
		if (publishServiceEndpoints != null && !publishServiceEndpoints.isEmpty()) {
			servicesPublished = publishServices(publishServiceEndpoints);
		}
		return RemoveAndPublishResult.create(servicesRemoved, servicesPublished);
	}

	@Override
	public boolean updateStatus(String serviceId, ServiceEndpointStatus status) throws RiceIllegalArgumentException {
		if (StringUtils.isBlank(serviceId)) {
			throw new RiceIllegalArgumentException("serviceId cannot be blank");
		}
		if (status == null) {
			throw new RiceIllegalArgumentException("status cannot be null");
		}
		return serviceRegistryDao.updateStatus(serviceId, status.getCode());
	}

	@Override
	public List<String> updateStatuses(List<String> serviceIds, ServiceEndpointStatus status) throws RiceIllegalArgumentException {
		if (serviceIds == null) {
			throw new RiceIllegalArgumentException("serviceIds canot be null");
		}
		if (status == null) {
			throw new RiceIllegalArgumentException("status cannot be null");
		}
		List<String> updatedServiceIds = new ArrayList<String>();
		for (String serviceId : serviceIds) {
			if (updateStatus(serviceId, status)) {
				updatedServiceIds.add(serviceId);
			}
		}
		return Collections.unmodifiableList(updatedServiceIds);
	}

	@Override
	public void takeInstanceOffline(String instanceId)
			throws RiceIllegalArgumentException {
		if (StringUtils.isBlank(instanceId)) {
			throw new RiceIllegalArgumentException("instanceId cannot be blank");
		}
		serviceRegistryDao.updateStatusForInstanceId(instanceId, ServiceEndpointStatus.OFFLINE.getCode());
	}

	private List<ServiceInfo> convertServiceInfoBoList(List<ServiceInfoBo> serviceInfoBos) {
		List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
		if (serviceInfoBos != null) {
			for (ServiceInfoBo serviceInfoBo : serviceInfoBos) {
				serviceInfos.add(ServiceInfoBo.to(serviceInfoBo));
			}
		} else {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(serviceInfos);
	}
	
	public void setServiceRegistryDao(ServiceRegistryDao serviceRegistryDao) {
		this.serviceRegistryDao = serviceRegistryDao;
	}
	
}
