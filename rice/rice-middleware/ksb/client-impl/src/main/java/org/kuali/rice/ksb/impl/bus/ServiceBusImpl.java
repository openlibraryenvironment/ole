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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceBus;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.api.bus.ServiceDefinition;
import org.kuali.rice.ksb.api.registry.RemoveAndPublishResult;
import org.kuali.rice.ksb.api.registry.ServiceEndpoint;
import org.kuali.rice.ksb.api.registry.ServiceInfo;
import org.kuali.rice.ksb.api.registry.ServiceRegistry;
import org.kuali.rice.ksb.impl.bus.diff.CompleteServiceDiff;
import org.kuali.rice.ksb.impl.bus.diff.LocalServicesDiff;
import org.kuali.rice.ksb.impl.bus.diff.RemoteServicesDiff;
import org.kuali.rice.ksb.impl.bus.diff.ServiceRegistryDiffCalculator;
import org.kuali.rice.ksb.messaging.serviceexporters.ServiceExportManager;
import org.kuali.rice.ksb.messaging.threadpool.KSBScheduledPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class ServiceBusImpl extends BaseLifecycle implements ServiceBus, InitializingBean, DisposableBean {
	
	private static final Logger LOG = Logger.getLogger(ServiceBusImpl.class);
	
	private final Object serviceLock = new Object();
	private final Object synchronizeLock = new Object();
	private final Random randomNumber = new Random();
	
	// injected values
	private String instanceId;
	private ServiceRegistry serviceRegistry;
	private ServiceRegistryDiffCalculator diffCalculator;
	private ServiceExportManager serviceExportManager;
	private KSBScheduledPool scheduledPool;
	
	private ScheduledFuture<?> registrySyncFuture;
	
	/**
	 * Contains endpoints for services which were published by this client application.
	 */
	private final Map<QName, LocalService> localServices;
	
	/**
	 * Contains endpoints for services which exist remotely.  This list may not be
	 * entirely complete as entries get lazily loaded into it as services are requested.
	 */
	private final Map<QName, Set<RemoteService>> clientRegistryCache;
		
	public ServiceBusImpl() {
		this.localServices = new HashMap<QName, LocalService>();
		this.clientRegistryCache = new HashMap<QName, Set<RemoteService>>();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (StringUtils.isBlank(instanceId)) {
			throw new IllegalStateException("a valid instanceId was not injected");
		}
		if (serviceRegistry == null) {
			throw new IllegalStateException("serviceRegistry was not injected");
		}
		if (diffCalculator == null) {
			throw new IllegalStateException("diffCalculator was not injected");
		}
		if (scheduledPool == null) {
			throw new IllegalStateException("scheduledPool was not injected");
		}
	}
	
	@Override
	public void start() throws Exception {
		startSynchronizationThread();
		super.start();
	}
		
	protected boolean isDevMode() {
		return ConfigContext.getCurrentContextConfig().getDevMode();
	}

	protected void startSynchronizationThread() {
		synchronized (synchronizeLock) {
			LOG.info("Starting Service Bus synchronization thread...");
			if (!isDevMode()) {
				int refreshRate = ConfigContext.getCurrentContextConfig().getRefreshRate();
				Runnable runnable = new Runnable() {
					public void run() {
						try {
							synchronize();
						} catch (Throwable t) {
							LOG.error("Failed to execute background service bus synchronization.", t);
						}
					}
				};
				this.registrySyncFuture = scheduledPool.scheduleWithFixedDelay(runnable, 30, refreshRate, TimeUnit.SECONDS);
			}
			LOG.info("...Service Bus synchronization thread successfully started.");
		}
	}
	
	@Override
	public void destroy() throws Exception {
		LOG.info("Stopping the Service Bus...");
		stopSynchronizationThread();
		serviceRegistry.takeInstanceOffline(getInstanceId());
		LOG.info("...Service Bus successfully stopped.");
	}
	
	protected void stopSynchronizationThread() {
		synchronized (synchronizeLock) {
			// remove services from the bus
			if (this.registrySyncFuture != null) {
				if (!this.registrySyncFuture.cancel(false)) {
					LOG.warn("Failed to cancel registry sychronization.");
				}
				this.registrySyncFuture = null;
			}
		}
	}

	@Override
	public String getInstanceId() {
		return this.instanceId;
	}
	
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
	@Override
	public List<Endpoint> getEndpoints(QName serviceName) {
		return getEndpoints(serviceName, null);
	}
	
	@Override
	public List<Endpoint> getEndpoints(QName serviceName, String applicationId) {
		if (serviceName == null) {
			throw new IllegalArgumentException("serviceName cannot be null");
		}
		List<Endpoint> endpoints = new ArrayList<Endpoint>();
		synchronized (serviceLock) {
			endpoints.addAll(getRemoteEndpoints(serviceName));
			Endpoint localEndpoint = getLocalEndpoint(serviceName);
			if (localEndpoint != null) {
				for (Iterator<Endpoint> iterator = endpoints.iterator(); iterator.hasNext();) {
					Endpoint endpoint = (Endpoint) iterator.next();
					if (localEndpoint.getServiceConfiguration().equals(endpoint.getServiceConfiguration())) {
						iterator.remove();
						break;
					}
				}
				if(StringUtils.isBlank(applicationId) || StringUtils.equals(localEndpoint.getServiceConfiguration().getApplicationId(), applicationId)) {
					// add at first position, just because we like the local endpoint the best, it's our friend ;)
					endpoints.add(0, localEndpoint);
				}
			}
			if(StringUtils.isNotBlank(applicationId)) {
				for (Iterator<Endpoint> iterator = endpoints.iterator(); iterator.hasNext();) {
					Endpoint endpoint = (Endpoint) iterator.next();
					if(!StringUtils.equals(endpoint.getServiceConfiguration().getApplicationId(), applicationId)) {
						iterator.remove();
					}
				}
			}
		}
		return Collections.unmodifiableList(endpoints);
	}
	
	@Override
	public List<Endpoint> getRemoteEndpoints(QName serviceName) {
		if (serviceName == null) {
			throw new IllegalArgumentException("serviceName cannot be null");
		}
		List<Endpoint> endpoints = new ArrayList<Endpoint>();
		synchronized (serviceLock) {
			Set<RemoteService> remoteServices = clientRegistryCache.get(serviceName);
			if (remoteServices != null) {
				for (RemoteService remoteService : remoteServices) {
					endpoints.add(remoteService.getEndpoint());
				}
			}
		}
		return Collections.unmodifiableList(endpoints);
	}

	@Override
	public Endpoint getLocalEndpoint(QName serviceName) {
		if (serviceName == null) {
			throw new IllegalArgumentException("serviceName cannot be null");
		}
		synchronized (serviceLock) {
			LocalService localService = localServices.get(serviceName);
			if (localService != null) {
				return localService.getEndpoint();
			}
			return null;
		}
	}

	@Override
	public Map<QName, Endpoint> getLocalEndpoints() {
		Map<QName, Endpoint> localEndpoints = new HashMap<QName, Endpoint>();
		synchronized (serviceLock) {
			for (QName localServiceName : localServices.keySet()) {
				LocalService localService = localServices.get(localServiceName);
				localEndpoints.put(localServiceName, localService.getEndpoint());
			}
		}
		return Collections.unmodifiableMap(localEndpoints);
	}

	@Override
	public List<Endpoint> getAllEndpoints() {
		List<Endpoint> allEndpoints = new ArrayList<Endpoint>();
		synchronized (serviceLock) {
			for (QName serviceName : this.localServices.keySet()) {
				allEndpoints.add(this.localServices.get(serviceName).getEndpoint());
			}
			for (QName serviceName : this.clientRegistryCache.keySet()) {
				Set<RemoteService> remoteServices = clientRegistryCache.get(serviceName);
				for (RemoteService remoteService : remoteServices) {
					allEndpoints.add(remoteService.getEndpoint());
				}
			}
		}
		return Collections.unmodifiableList(allEndpoints);
	}

	@Override
	public Endpoint getEndpoint(QName serviceName) {
		return getEndpoint(serviceName, null);
	}
	
	@Override
    public Endpoint getEndpoint(QName serviceName, String applicationId) {
        if (serviceName == null) {
            throw new IllegalArgumentException("serviceName cannot be null");
        }
        Endpoint availableEndpoint = null;
        synchronized (serviceLock) {
            // look at local services first
            availableEndpoint = getLocalEndpoint(serviceName);
            if (availableEndpoint == null || (!StringUtils.isBlank(applicationId) && !availableEndpoint.getServiceConfiguration().getApplicationId().equals(applicationId))) {
                 // TODO - would be better to return an Endpoint that contained an internal proxy to all the services so fail-over would be easier to implement!
                Set<RemoteService> remoteServices = clientRegistryCache.get(serviceName);
                remoteServices = filterByApplicationId(applicationId, remoteServices);
                if (remoteServices != null && !remoteServices.isEmpty()) {
                    // TODO - this should also probably check the current status of the service?
                    RemoteService[] remoteServiceArray = remoteServices.toArray(new RemoteService[0]);
                    RemoteService availableRemoteService = remoteServiceArray[this.randomNumber.nextInt(remoteServiceArray.length)];
                    availableEndpoint = availableRemoteService.getEndpoint();
                }
            }
        }
        return availableEndpoint;
    }
	
	protected Set<RemoteService> filterByApplicationId(String applicationId, Set<RemoteService> remoteServices) {
	    if (StringUtils.isBlank(applicationId) || remoteServices == null || remoteServices.isEmpty()) {
	        return remoteServices;
	    }
	    Set<RemoteService> filtered = new HashSet<RemoteService>();
	    for (RemoteService remoteService : remoteServices) {
	        if (remoteService.getServiceInfo().getApplicationId().equals(applicationId)) {
	            filtered.add(remoteService);
	        }
	    }
	    return filtered;
	}
	
	@Override
	public Endpoint getConfiguredEndpoint(ServiceConfiguration serviceConfiguration) {
		if (serviceConfiguration == null) {
			throw new IllegalArgumentException("serviceConfiguration cannot be null");
		}
		synchronized (serviceLock) {
			Endpoint localEndpoint = getLocalEndpoint(serviceConfiguration.getServiceName());
			if (localEndpoint != null && localEndpoint.getServiceConfiguration().equals(serviceConfiguration)) {
				return localEndpoint;
			}
			List<Endpoint> remoteEndpoints = getRemoteEndpoints(serviceConfiguration.getServiceName());
			for (Endpoint remoteEndpoint : remoteEndpoints) {
				if (remoteEndpoint.getServiceConfiguration().equals(serviceConfiguration)) {
					return remoteEndpoint;
				}
			}
		}
		return null;
	}

	@Override
    public Object getService(QName serviceName) {
        return getService(serviceName, null);
    }
	
	@Override
	public Object getService(QName serviceName, String applicationId) {
		Endpoint availableEndpoint = getEndpoint(serviceName, applicationId);
		if (availableEndpoint == null) {
			return null;
		}
		return availableEndpoint.getService();
	}

	@Override
	public ServiceConfiguration publishService(ServiceDefinition serviceDefinition, boolean synchronize) {
		if (serviceDefinition == null) {
			throw new IllegalArgumentException("serviceDefinition cannot be null");
		}
		LocalService localService = new LocalService(getInstanceId(), serviceDefinition);
		synchronized (serviceLock) {
			serviceExportManager.exportService(serviceDefinition);
			localServices.put(serviceDefinition.getServiceName(), localService);
		}
		if (synchronize) {
			synchronize();
		}
		return localService.getEndpoint().getServiceConfiguration();
	}

	@Override
	public List<ServiceConfiguration> publishServices(List<ServiceDefinition> serviceDefinitions, boolean synchronize) {
		if (serviceDefinitions == null) {
			throw new IllegalArgumentException("serviceDefinitions list cannot be null");
		}
		List<ServiceConfiguration> serviceConfigurations = new ArrayList<ServiceConfiguration>();
		synchronized (serviceLock) {
			for (ServiceDefinition serviceDefinition : serviceDefinitions) {
				ServiceConfiguration serviceConfiguration = publishService(serviceDefinition, false);
				serviceConfigurations.add(serviceConfiguration);
			}
		}
		if (synchronize) {
			synchronize();
		}
		return Collections.unmodifiableList(serviceConfigurations);
	}

	@Override
	public boolean removeService(QName serviceName, boolean synchronize) {
		if (serviceName == null) {
			throw new IllegalArgumentException("serviceName cannot be null");
		}
		boolean serviceRemoved = false;
		synchronized (serviceLock) {
			LocalService localService = localServices.remove(serviceName);
			serviceRemoved = localService != null;
			serviceExportManager.removeService(serviceName);
		}
		if (serviceRemoved && synchronize) {
			synchronize();
		}
		return serviceRemoved;
	}

	@Override
	public List<Boolean> removeServices(List<QName> serviceNames, boolean synchronize) {
		if (serviceNames == null) {
			throw new IllegalArgumentException("serviceNames cannot be null");
		}
		boolean serviceRemoved = false;
		List<Boolean> servicesRemoved = new ArrayList<Boolean>();
		synchronized (serviceLock) {
			for (QName serviceName : serviceNames) {
				serviceExportManager.removeService(serviceName);
				LocalService localService = localServices.remove(serviceName);
				if (localService != null) {
					servicesRemoved.add(Boolean.TRUE);
					serviceRemoved = true;
				} else {
					servicesRemoved.add(Boolean.FALSE);
				}
			}
		}
		if (serviceRemoved && synchronize) {
			synchronize();
		}
		return servicesRemoved;
	}

    protected void synchronizeAndProcess(SyncProcessor processor) {
        if (!isDevMode()) {
			synchronized (synchronizeLock) {
				List<LocalService> localServicesList;
				List<RemoteService> clientRegistryCacheList;
				synchronized (serviceLock) {
					// first, flatten the lists
					localServicesList = new ArrayList<LocalService>(this.localServices.values());
					clientRegistryCacheList = new ArrayList<RemoteService>();
					for (Set<RemoteService> remoteServices : this.clientRegistryCache.values()) {
						clientRegistryCacheList.addAll(remoteServices);
					}
				}
				CompleteServiceDiff serviceDiff = diffCalculator.diffServices(getInstanceId(), localServicesList, clientRegistryCacheList);
                logCompleteServiceDiff(serviceDiff);
                processor.sync(serviceDiff);
            }
        }
    }

	@Override
	public void synchronize() {
        synchronizeAndProcess(new SyncProcessor() {
            @Override
            public void sync(CompleteServiceDiff diff) {
                RemoteServicesDiff remoteServicesDiff = diff.getRemoteServicesDiff();
				processRemoteServiceDiff(remoteServicesDiff);
				LocalServicesDiff localServicesDiff = diff.getLocalServicesDiff();
				processLocalServiceDiff(localServicesDiff);
            }
        });
	}

    @Override
	public void synchronizeRemoteServices() {
        synchronizeAndProcess(new SyncProcessor() {
            @Override
            public void sync(CompleteServiceDiff diff) {
                RemoteServicesDiff remoteServicesDiff = diff.getRemoteServicesDiff();
				processRemoteServiceDiff(remoteServicesDiff);
            }
        });
	}

    @Override
    public void synchronizeLocalServices() {
        synchronizeAndProcess(new SyncProcessor() {
            @Override
            public void sync(CompleteServiceDiff diff) {
                LocalServicesDiff localServicesDiff = diff.getLocalServicesDiff();
                processLocalServiceDiff(localServicesDiff);
            }
        });
    }

    protected void logCompleteServiceDiff(CompleteServiceDiff serviceDiff) {
        RemoteServicesDiff remoteServicesDiff = serviceDiff.getRemoteServicesDiff();
        int newServices = remoteServicesDiff.getNewServices().size();
        int removedServices = remoteServicesDiff.getRemovedServices().size();

        LocalServicesDiff localServicesDiff = serviceDiff.getLocalServicesDiff();
        int servicesToPublish = localServicesDiff.getLocalServicesToPublish().size();
        int servicesToUpdate = localServicesDiff.getLocalServicesToUpdate().size();
        int servicesToRemove = localServicesDiff.getServicesToRemoveFromRegistry().size();

        if (newServices + removedServices + servicesToPublish + servicesToUpdate + servicesToRemove > 0) {
            LOG.info("Found service changes during synchronization: remoteNewServices=" + newServices +
                    ", remoteRemovedServices=" + removedServices +
                    ", localServicesToPublish=" + servicesToPublish +
                    ", localServicesToUpdate=" + servicesToUpdate +
                    ", localServicesToRemove=" + servicesToRemove);
        }
    }
		
	protected void processRemoteServiceDiff(RemoteServicesDiff remoteServicesDiff) {
		// note that since there is a gap between when the original services are acquired, the diff, and this subsequent critical section
		// the list of local and client registry services could have changed, so that needs to be considered in the remaining code
		synchronized (serviceLock) {
			// first, let's update what we know about the remote services
			List<RemoteService> removedServices = remoteServicesDiff.getRemovedServices();
			for (RemoteService removedRemoteService : removedServices) {
				Set<RemoteService> remoteServiceSet = this.clientRegistryCache.get(removedRemoteService.getServiceName());
				if (remoteServiceSet != null) {
					boolean wasRemoved = remoteServiceSet.remove(removedRemoteService);
					if (!wasRemoved) {
						LOG.warn("Failed to remove remoteService during synchronization: " + removedRemoteService);
					}
				}
			}
			List<ServiceInfo> newServices = remoteServicesDiff.getNewServices();
			for (ServiceInfo newService : newServices) {
				Set<RemoteService> remoteServiceSet = clientRegistryCache.get(newService.getServiceName());
				if (remoteServiceSet == null) {
					remoteServiceSet = new HashSet<RemoteService>();
					clientRegistryCache.put(newService.getServiceName(), remoteServiceSet);
				}
				remoteServiceSet.add(new RemoteService(newService, this.serviceRegistry));
			}
		}
	}
	
	protected void processLocalServiceDiff(LocalServicesDiff localServicesDiff) {
		List<String> removeServiceEndpointIds = new ArrayList<String>();
		List<ServiceEndpoint> publishServiceEndpoints = new ArrayList<ServiceEndpoint>();
		for (ServiceInfo serviceToRemove : localServicesDiff.getServicesToRemoveFromRegistry()) {
			removeServiceEndpointIds.add(serviceToRemove.getServiceId());
		}
		for (LocalService localService : localServicesDiff.getLocalServicesToPublish()) {
			publishServiceEndpoints.add(localService.getServiceEndpoint());
		}
		for (LocalService localService : localServicesDiff.getLocalServicesToUpdate().keySet()) {
			ServiceInfo registryServiceInfo = localServicesDiff.getLocalServicesToUpdate().get(localService);
			publishServiceEndpoints.add(rebuildServiceEndpointForUpdate(localService.getServiceEndpoint(), registryServiceInfo));
		}
		boolean batchMode = ConfigContext.getCurrentContextConfig().getBooleanProperty(Config.BATCH_MODE, false);
		if (!batchMode && (!removeServiceEndpointIds.isEmpty() || !publishServiceEndpoints.isEmpty())) {
			RemoveAndPublishResult result = this.serviceRegistry.removeAndPublish(removeServiceEndpointIds, publishServiceEndpoints);
			// now update the ServiceEndpoints for our local services so we can get the proper id for them
			if (!result.getServicesPublished().isEmpty()) {
				synchronized (serviceLock) {
					for (ServiceEndpoint publishedService : result.getServicesPublished()) {
						rebuildLocalServiceEndpointAfterPublishing(publishedService);
					}
				}
			}
		}
	}
	
	protected ServiceEndpoint rebuildServiceEndpointForUpdate(ServiceEndpoint originalEndpoint, ServiceInfo registryServiceInfo) {
		ServiceEndpoint.Builder builder = ServiceEndpoint.Builder.create(originalEndpoint);
		builder.getInfo().setServiceId(registryServiceInfo.getServiceId());
		builder.getInfo().setServiceDescriptorId(registryServiceInfo.getServiceDescriptorId());
		builder.getInfo().setVersionNumber(registryServiceInfo.getVersionNumber());
		builder.getDescriptor().setId(registryServiceInfo.getServiceDescriptorId());
		return builder.build();
	}
	
	protected void rebuildLocalServiceEndpointAfterPublishing(ServiceEndpoint publishedService) {
		// verify the service is still published
		QName serviceName = publishedService.getInfo().getServiceName();
		if (localServices.containsKey(serviceName)) {
			LocalService newLocalService = new LocalService(localServices.get(serviceName), publishedService);
			localServices.put(serviceName, newLocalService);
		}
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	
	public void setDiffCalculator(ServiceRegistryDiffCalculator diffCalculator) {
		this.diffCalculator = diffCalculator;
	}
	
	public void setServiceExportManager(ServiceExportManager serviceExportManager) {
		this.serviceExportManager = serviceExportManager;
	}
	
	public void setScheduledPool(KSBScheduledPool scheduledPool) {
		this.scheduledPool = scheduledPool;
	}

    private static interface SyncProcessor {
        void sync(CompleteServiceDiff diff);
    }
	
}
