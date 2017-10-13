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
package org.kuali.rice.ksb.api.registry;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.util.jaxb.QNameAsStringAdapter;
import org.kuali.rice.ksb.api.KsbApiConstants;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import java.util.List;

/**
 * Defines the interface for a remotely accessible service registry.  Applications
 * can query for information about available services through the apis provided
 * as well as publishing their own services.
 * 
 * <p>The {@code ServiceRegistry} deals primarily with the concept of a
 * {@link ServiceEndpoint} which holds a {@link ServiceInfo}
 * and a {@link ServiceDescriptor}.  These pieces include information about the
 * service and it's configuration which might be needed by applications wishing to
 * invoke those services.
 * 
 * <p>Many of the operations on the {@code ServiceRegistry} only return the 
 * {@code ServiceInfo}.  This is because retrieving the full {@code ServiceDescriptor}
 * is a more expensive operation (since it consists of a serialized XML
 * representation of the service's configuration which needs to be unmarshaled
 * and processed) and typically the descriptor is only needed when the client
 * application actually wants to connect to the service.
 * 
 * <p>The {@link ServiceInfo} provides two important pieces of information which
 * help the registry (and the applications which interact with it) understand
 * who the owner of a service is.  The first of these is the "application id"
 * which identifies the application which owns the service.  In terms of
 * Kuali Rice, an "application" is an abstract concept and consist of multiple
 * instances of an application which are essentially mirrors of each other and
 * publish the same set of services.  Each of these individuals instances of
 * an application is identified by the "instance id" which is also available
 * from the {@code ServiceInfo}.
 * 
 * @see ServiceEndpoint
 * @see ServiceInfo
 * @see ServiceDescriptor
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "serviceRegistry", targetNamespace = KsbApiConstants.Namespaces.KSB_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ServiceRegistry {
	
	/**
	 * Returns an unmodifiable list of {@link ServiceInfo} for all services that have a status
	 * of {@link ServiceEndpointStatus#ONLINE} with the given name.  If there
	 * are no services with the given name, this method should return an empty
	 * list.
	 * 
	 * <p>It is typical in clustered environments and other situations that
	 * more than one online service might be available for a given service name.
	 * It is intended that a client of the registry will use an available endpoint
	 * of their choosing to connect to and invoke the service.
	 * 
	 * @param serviceName the name of the service to locate
	 * 
	 * @return an unmodifiable list of {@code ServiceInfo} for online services with the given name.
	 * If no services were found, an empty list will be returned, but this method should never
	 * return null.
	 * 
	 * @throws RiceIllegalArgumentException if serviceName is null
	 */
	@WebMethod(operationName = "getOnlineServiceByName")
	@WebResult(name = "serviceInfos")
	@XmlElementWrapper(name = "serviceInfos", required = false)
	@XmlElement(name = "serviceInfo", required = false)
	List<ServiceInfo> getOnlineServicesByName(
			@XmlJavaTypeAdapter(QNameAsStringAdapter.class)
			@WebParam(name = "serviceName")
			QName serviceName) throws RiceIllegalArgumentException;
	
	/**
	 * Returns an unmodifiable list of {@link ServiceInfo} for all services in
	 * the registry that have a status of {@link ServiceEndpointStatus#ONLINE}.
	 * If there are no online services in the registry, this method will return
	 * an empty list.
	 * 
	 * @return an unmodifiable list of {@code ServiceInfo} for all online services
	 * in the registry. If no services were found, an empty list will be
	 * returned, but this method should never return null.
	 */
	@WebMethod(operationName = "getAllOnlineServices")
	@WebResult(name = "serviceInfo")
	@XmlElementWrapper(name = "serviceInfos", required = false)
	@XmlElement(name = "serviceInfo", required = false)
	List<ServiceInfo> getAllOnlineServices();
	
	/**
	 * Returns an unmodifiable list of {@link ServiceInfo} for all services in
	 * the registry.  If there are no services in the registry, this method will
	 * return an empty list.
	 * 
	 * @return an unmodifiable list of {@code ServiceInfo} for all services in the
	 * registry. If no services were found, an empty list will be returned, but
	 * this method should never return null.
	 */
	@WebMethod(operationName = "getAllServices")
	@WebResult(name = "serviceInfo")
	@XmlElementWrapper(name = "serviceInfos", required = false)
	@XmlElement(name = "serviceInfo", required = false)
	List<ServiceInfo> getAllServices();
	
	/**
	 * Returns an unmodifiable list of {@link ServiceInfo} for all services that
	 * have an instance id which matches the given instance id, regardless of
	 * their status.  If there are no services published for the given instance,
	 * this method should return an empty list.
	 * 
	 * @param instanceId the instance id of the services to locate
	 * 
	 * @return an unmodifiable listof {@code ServiceInfo} for all services in the
	 * registry for the given instance id
	 * 
	 * @throws RiceIllegalArgumentException if instanceId is a null or blank value
	 */
	@WebMethod(operationName = "getAllServicesForInstance")
	@WebResult(name = "serviceInfos")
	@XmlElementWrapper(name = "serviceInfos", required = false)
	@XmlElement(name = "serviceInfo", required = false)
	List<ServiceInfo> getAllServicesForInstance(@WebParam(name = "instanceId") String instanceId) throws RiceIllegalArgumentException;

    /**
     * Returns an unmodifiable list of {@link ServiceInfo} for all services that
     * have an application id which matches the given application id, regardless of
     * their status.  If there are no services published for the given application,
     * this method should return an empty list.
     *
     * @param applicationId the application id of the services to locate
     *
     * @return an unmodifiable listof {@code ServiceInfo} for all services in the
     * registry for the given application id
     *
     * @throws RiceIllegalArgumentException if applicationId is a null or blank value
     */
    @WebMethod(operationName = "getAllServicesForApplication")
    @WebResult(name = "serviceInfos")
    @XmlElementWrapper(name = "serviceInfos", required = false)
    @XmlElement(name = "serviceInfo", required = false)
    List<ServiceInfo> getAllServicesForApplication(@WebParam(name = "applicationId") String applicationId) throws RiceIllegalArgumentException;
	
	/**
	 * Returns the {@link ServiceDescriptor} which has the given id.  If there
	 * is no descriptor for the id, this method will return null.
	 * 
	 * @param serviceDescriptorId
	 * @return
	 * @throws RiceIllegalArgumentException
	 */
	@WebMethod(operationName = "getServiceDescriptor")
	@WebResult(name = "serviceDescriptor")
	@XmlElement(name = "serviceDescriptor", required = false)
	ServiceDescriptor getServiceDescriptor(@WebParam(name = "serviceDescriptorId") String serviceDescriptorId) throws RiceIllegalArgumentException;
	
	/**
	 * Returns an unmodifiable list of {@link ServiceDescriptor} which match the
	 * given list of service descriptor ids.  The list that is returned from this
	 * method may be smaller than the list of ids that were supplied.  This
	 * happens in cases where a service descriptor for a given id in the list
	 * could not be found.
	 * 
	 * @param serviceDescriptorIds the list of service descriptor ids for which to
	 * locate the corresponding service descriptor
	 * 
	 * @return an unmodifiable list of the service descriptors that could be
	 * located for the given list of ids.  This list may be smaller than the
	 * original list of ids that was supplied if the corresponding descriptor
	 * could not be located for a given id in the registry.  If no service
	 * descriptors could be located, this method will return an empty list. It
	 * should never return null. 
	 * 
	 * @throws RiceIllegalArgumentException if serviceDescriptorIds is null
	 */
	@WebMethod(operationName = "getServiceDescriptors")
	@WebResult(name = "serviceDescriptors")
	@XmlElementWrapper(name = "serviceDescriptors", required = false)
	@XmlElement(name = "serviceDescriptor", required = false)
	List<ServiceDescriptor> getServiceDescriptors(@WebParam(name = "serviceDescriptorId") List<String> serviceDescriptorIds) throws RiceIllegalArgumentException;
	
	/**
	 * Publishes the given {@link ServiceEndpoint} to the registry.  If there
	 * is no service id on the {@code ServiceInfo} then this constitutes a new
	 * registry endpoint, so it will be added to the registry.  If the given
	 * endpoint already has a {@code ServiceInfo} with a service id, then the
	 * corresponding entry in the registry will be updated instead.  
	 * 
	 * @param serviceEndpoint the service endpoint to publish
	 * 
	 * @return the result of publishing the endpoint, if this is a new registry
	 * entry, then the service endpoint that is returned will contain access to
	 * both the service id and service descriptor id that were generated for
	 * this entry in the registry.  This method will never return null.
	 * 
	 * @throws RiceIllegalArgumentException if serviceEndpoint is null
	 */
	@WebMethod(operationName = "publishService")
	@WebResult(name = "serviceEndpoint")
	@XmlElement(name = "serviceEndpoint", required = true)
	ServiceEndpoint publishService(@WebParam(name = "serviceEndpoint") ServiceEndpoint serviceEndpoint) throws RiceIllegalArgumentException;

	/**
	 * Publishes the list of {@link ServiceEndpoint}s to the registry.  This
	 * functions the same way as executing {@link #publishService(ServiceEndpoint)}
	 * on each individual {@code ServiceEndpoint}.  However, it performs this as
	 * an atomic operation, so if there is an error when publishing one service
	 * endpoint in the list, then none of the endpoints will be published.
	 * 
	 * @param serviceEndpoints the list of service endpoints to publish
	 * 
	 * @return the result of publishing the endpoints (see {@link #publishService(ServiceEndpoint)}
	 * for details).  This list will always be the same size and in the same
	 * order as the list of service endpoints that were supplied for publshing.
	 * 
	 * @throws RiceIllegalArgumentException if serviceEndpoints is null or if any
	 * {@code ServiceEndpoint} within the list is null
	 */
	@WebMethod(operationName = "publishServices")
	@WebResult(name = "serviceEndpoints")
	@XmlElementWrapper(name = "serviceEndpoints", required = false)
	@XmlElement(name = "serviceEndpoint", required = false)
	List<ServiceEndpoint> publishServices(@WebParam(name = "serviceEndpoint") List<ServiceEndpoint> serviceEndpoints) throws RiceIllegalArgumentException;
	
	/**
	 * Removes the service from the registry with the given service id if it
	 * exists.  If the service with the given id exists and was successfully
	 * removed, a copy of the removed {@link ServiceEndpoint} entry will be
	 * returned.  Otherwise, this method will return null.
	 * 
	 * @param serviceId the id of the service to remove
	 * 
	 * @return the removed {@link ServiceEndpoint} if a service with the given
	 * id exists in the registry, if no such service exists, this method will
	 * return null
	 * 
	 * @throws RiceIllegalArgumentException if serviceId is null or a blank value
	 */
	@WebMethod(operationName = "removeServiceEndpoint")
	@WebResult(name = "serviceEndpoint")
	@XmlElement(name = "serviceEndpoint", required = false)
	ServiceEndpoint removeServiceEndpoint(@WebParam(name = "serviceId") String serviceId) throws RiceIllegalArgumentException;
	
	/**
	 * As {@link #removeServiceEndpoint(String)} but removes all services that
	 * match the given list of service ids.  It could be the case that some of
	 * the given ids do not match a service in the registry, in this case that
	 * {@link ServiceEndpoint} would not be included in the resulting list of
	 * services that were removed.  Because of this, the list that is returned
	 * from this method may be smaller then the list of ids that were supplied.
	 * 
	 * @param serviceIds the list of service ids to remove from the registry
	 * 
	 * @return a list of all service endpoints that were successfully removed,
	 * if no such endpoints were removed, this list will be empty, but it will
	 * never be null
	 * 
	 * @throws RiceIllegalArgumentException if serviceIds is null or if one of
	 * the ids in the list is null or blank
	 */
	@WebMethod(operationName = "removeServiceEndpoints")
	@WebResult(name = "serviceEndpoints")
	@XmlElementWrapper(name = "serviceEndpoints", required = false)
	@XmlElement(name = "serviceEndpoint", required = false)
	List<ServiceEndpoint> removeServiceEndpoints(@WebParam(name = "serviceId") List<String> serviceIds) throws RiceIllegalArgumentException;
	
	/**
	 * Performs a single atomic operation of removing and publishing a set of
	 * services in the registry.  This operation is useful in situations where
	 * a client application contains apis to manage the services they are
	 * publishing on the bus and they want to ensure the registry is kept in
	 * a consistent state in terms of what they have published.
	 * 
	 * <p>Behaviorally, this operation is equivalent to performing a
	 * {@link #removeServiceEndpoints(List)} followed by a
	 * {@link #publishServices(List)}, except that a null list is valid for
	 * either {@code removeServiceIds} or {@code publishServiceEndpoints}.  In
	 * the case that a null or empty list is passed for either of these, that
	 * particular portion of the operation will not be performed.
	 * 
	 * <p>This method returns a {@link RemoveAndPublishResult} which contains
	 * a list of the services that were successfully removed as well as those
	 * that were published.
	 * 
	 * @param removeServiceIds the list of ids of the services to remove, if
	 * this parameter is null or an empty list, then no remove operation will
	 * be executed
	 * @param publishServiceEndpoints the list of service endpoints to publish,
	 * if this parameter is null or an empty list, then no publish operation
	 * will be executed
	 * 
	 * @return the result of the operation which contains information on which
	 * services were successfully removed as well as published, this method will
	 * never return null
	 */
	@WebMethod(operationName = "removeAndPublish")
	@WebResult(name = "removeAndPublishResult")
	@XmlElement(name = "removeAndPublishResult", required = true)
	RemoveAndPublishResult removeAndPublish(@WebParam(name = "removeServiceId") List<String> removeServiceIds,
			@WebParam(name = "publishServiceEndpoint") List<ServiceEndpoint> publishServiceEndpoints);
	
	/**
	 * Updates the status for the service with the given id to the given
	 * {@link ServiceEndpointStatus}.
	 * 
	 * @param serviceId the id of the service for which to update the status
	 * @param status the status to update this service to
	 * 
	 * @return true if the service with the given id exists in the registry and
	 * was updated, false otherwise
	 * 
	 * @throws RiceIllegalArgumentException if serviceId is null or a blank value
	 * @throws RiceIllegalArgumentException if status is null
	 */
	@WebMethod(operationName = "updateStatus")
	@WebResult(name = "statusUpdated")
	boolean updateStatus(@WebParam(name = "serviceId") String serviceId, @WebParam(name = "status") ServiceEndpointStatus status) throws RiceIllegalArgumentException;

	/**
	 * As per {@link #updateStatus(String, ServiceEndpointStatus)} but updates
	 * mutliple statuses as part of a single operation.
	 * 
	 * <p>This method returns a List of ids of the services that were updated.
	 * If a given service id does not exist in the registry, that id won't be
	 * included in the result.  So the resuling list of updated ids may be
	 * smaller than the given list of service ids (though it will never be
	 * null).
	 * 
	 * @param serviceIds the list of ids of the services for which to update the status
	 * @param status the status to update the services to
	 * 
	 * @return an unmodifiable list containing the ids of the services that
	 * were successfully updated, since it's possible some of the supplied ids
	 * might not exist in the registry this list could be smaller than the
	 * given serviceIds list
	 * 
	 * @throws RiceIllegalArgumentException if serviceIds is null or if any of
	 * the entries in the list is null or has a blank value
	 * @throws RiceIllegalArgumentException if status is null
	 */
	@WebMethod(operationName = "updateStatuses")
	@WebResult(name = "serviceIds")
	@XmlElementWrapper(name = "serviceIds", required = false)
	@XmlElement(name = "serviceId", required = false)
	List<String> updateStatuses(@WebParam(name = "serviceId") List<String> serviceIds, @WebParam(name = "status") ServiceEndpointStatus status) throws RiceIllegalArgumentException;
	
	/**
	 * Flips the status of all services that match the given instance id to the
	 * status of {@link ServiceEndpointStatus#OFFLINE.  It is intended that this
	 * operation will be used by a registry client who is going offline for
	 * maintenance or other reasons and wants to ensure that the state of the
	 * registry is consistent with the application's state.
	 * 
	 * @param instanceId the id of the instance for which to set all services to
	 * the offline status
	 * 
	 * @throws RiceIllegalArgumentException if instanceId is null or a blank value
	 */
	@WebMethod(operationName = "takeInstanceOffline")
	void takeInstanceOffline(@WebParam(name = "instanceId") String instanceId) throws RiceIllegalArgumentException;
	
}
