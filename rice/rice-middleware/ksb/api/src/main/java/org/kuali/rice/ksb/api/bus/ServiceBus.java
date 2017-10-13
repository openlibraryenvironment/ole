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
package org.kuali.rice.ksb.api.bus;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * The {@code ServiceBus} is the primary api that client applications use to interact with the Kuali
 * Service Bus. It provides capabilities to retrieve services endpoints for use when needing to
 * invoke a service. It also provides a mechanism by which a client application can publish it's own
 * services to the bus.
 * 
 * <p>
 * The service bus may be backed by a service registry which can be used to locate services which
 * other applications have published to the service registry. The service bus will synchronize it's
 * known state with the of the registry, either through explicit invocations of the
 * {@link #synchronize()} method or on a periodic basis (the details of which are up to the
 * implementation).
 * 
 * <p>
 * Note that the {@code ServiceBus} manages two primary collections of {@link Endpoint} classes.
 * Those that have been published by this application (referred to as "local" endpoints) and those
 * which have been published by other applications (referred to as "remote" endpoints).
 * 
 * @see Endpoint
 * @see ServiceConfiguration
 * @see ServiceDefinition
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public interface ServiceBus {

    /**
     * Returns the instance ID which identifies this client application to the service bus. The
     * instance ID should be unique for each client of the service bus and will never be blank or
     * null.
     * 
     * @return the instance ID for the application in which this service bus client is resident
     */
    String getInstanceId();

    /**
     * Returns an unmodifiable list of accessible endpoints that are available to the service bus
     * with the given service name. This method will only return endpoints that the service bus
     * believes are online, endpoints which are offline will not be included. In certain cases a
     * specific service endpoint will be available both as a local and remote service. In these
     * cases, the list returned from this method will preferentially include the local service. It
     * will *not* include both the local and remote endpoint to the same service. This is important
     * as this method may be used to get endpoints for broadcasting service calls to all endpoints
     * with a given name. In these cases, it is not desirable to invoke the same endpoint twice.
     * 
     * @return a list of the remote endpoints that are available for the given service name, this
     *         list will never be null but may be empty if there are no remote endpoints for the
     *         given service name
     * @throws IllegalArgumentException if serviceName is null
     */
    List<Endpoint> getEndpoints(QName serviceName);
    
    /**
     * Returns an unmodifiable list of accessible endpoints that are available to the service bus
     * with the given service name for the given application id. This method will only return
     * endpoints that the service bus believes are online, endpoints which are offline will not be
     * included. In certain cases a specific service endpoint will be available both as a local and
     * remote service. In these cases, the list returned from this method will preferentially
     * include the local service. It will *not* include both the local and remote endpoint to the
     * same service. This is important as this method may be used to get endpoints for broadcasting
     * service calls to all endpoints with a given name. In these cases, it is not desirable to
     * invoke the same endpoint twice.
     * @param serviceName the name of the service for which to locate an available endpoint
     * @param applicationId the id of the application for which to locate an available endpoint for
     *        the given service name
     * @return a list of the remote endpoints that are available for the given service name, this
     *         list will never be null but may be empty if there are no remote endpoints for the
     *         given service name
     * @throws IllegalArgumentException if serviceName is null
     * @since 2.1.1
     */
    List<Endpoint> getEndpoints(QName serviceName, String applicationId);

    /**
     * Returns an unmodifiable list of remotely accessible endpoints that are available in the
     * service registry with the given service name. This method will only return endpoints that the
     * service bus believes are online, endpoints which are offline will not be included.
     * 
     * <p>
     * If the service bus client has also deployed a service with this name, a remoted endpoint to
     * this service will likely also be included in this list assuming that endpoint has already
     * been synchronized with the registry.
     * 
     * <p>
     * In most cases, it is preferable to use {@link #getEndpoints(QName)} instead of this method.
     * Because it will preferably return a local endpoint reference for a given service endpoint if
     * one is available.
     * 
     * @return a list of all endpoints that are available for the given service name, this list will
     *         never be null but may be empty if there are no endpoints for the given service name
     * @throws IllegalArgumentException if serviceName is null
     */
    List<Endpoint> getRemoteEndpoints(QName serviceName);

    /**
     * Returns the endpoint for the service with the given name that was published by this service
     * bus client. If this client has not published any such service, then this method will return
     * null.
     * 
     * @param serviceName the name of the service represented by the local endpoint
     * @return the local endpoint for the given service name which was deployed by this client, or
     *         null if no such service has been published
     * @throws IllegalArgumentException if serviceName is null
     */
    Endpoint getLocalEndpoint(QName serviceName);

    /**
     * Returns an unmodifiable list of all services that have been published by this service bus
     * client.
     * 
     * @return a map with the local service name as the key and the local endpoint as the value
     *         which contains all local services published by this service bus client. This map may
     *         be empty if this client has published no services, but it should never be null.
     */
    Map<QName, Endpoint> getLocalEndpoints();

    /**
     * Returns an unmodifiable list of all available and online endpoints of which the service bus
     * is aware, including both local and remote endpoints.
     * 
     * @return all available endpoints, this list may be empty if no endpoints exist but will never
     *         be null
     */
    List<Endpoint> getAllEndpoints();

    /**
     * Returns an available endpoint for the service with the given name. If the service with the
     * given name is published locally, preference will be given to the locally deployed version of
     * the service.
     * 
     * <p>
     * Based on the nature of this method, if there is more than one endpoint available for the
     * given name, multiple invocations of this method with the same service name may return
     * different {@link Endpoint} instances each time.
     * 
     * @param serviceName the name of the service for which to locate an available endpoint
     * @return an available endpoint for the service with the given name, or null if none is
     *         available
     * @throws IllegalArgumentException if serviceName is null
     */
    Endpoint getEndpoint(QName serviceName);

    /**
     * Returns an available endpoint for the service with the given name which is hosted by the
     * application with the given application id. This operation functions the same as
     * {@link #getEndpoint(QName)} with the exception that it will only consider endpoints with the
     * given application id.
     * 
     * <p>
     * Invoking this method with a null or blank value for {@code applicationId} is equivalent to
     * invoking {@link #getEndpoint(QName)}.
     * 
     * @param serviceName the name of the service for which to locate an available endpoint
     * @param applicationId the id of the application for which to locate an available endpoint for
     *        the given service name
     * @return an available endpoint for the service with the given name and application id, or null
     *         if none is available
     * @throws IllegalArgumentException if serviceName is null
     */
    Endpoint getEndpoint(QName serviceName, String applicationId);

    /**
     * Returns the endpoint matching the given service configuration, if one exists. In the case of
     * services published by this service bus client, this method will preferably return the local
     * endpoints in place of a remote endpoint to the same service.
     * 
     * @param serviceConfiguration the service configuration by which to lookup up the endpoint
     * @return the endpoint who's service configuration matches the given configuration, or null if
     *         no such match could be determined
     * @throws IllegalArgumentException if the given serviceConfiguration is null
     */
    Endpoint getConfiguredEndpoint(ServiceConfiguration serviceConfiguration);

    /**
     * Returns a proxy to the service with the given name. This proxy should have built-in support
     * for fail-over and load balancing capabilities. This means it is safe for client applications
     * to cache a reference to this service proxy.
     * 
     * <p>
     * This proxy should additionally be thread-safe.
     * 
     * <p>
     * This method is equivalent to invoking {@link #getEndpoint(QName).getService()}.
     * 
     * @param serviceName the name of the service for which to locate an available proxy
     * @return an available proxy for the service with the given name, or null if none is available
     * @throws IllegalArgumentException if serviceName is null
     */
    Object getService(QName serviceName);

    /**
     * Returns a proxy to the service with the given name which is hosted by the application with
     * the given application id. This operation functions the same as {@link #getService(QName)}
     * with the exception that it will only consider endpoints with the given application id.
     * 
     * <p>
     * Invoking this method with a null or blank value for {@code applicationId} is equivalent to
     * invoking {@link #getService(QName)}. This method is also equivalent to invoking
     * {@link #getEndpoint(QName, String).getService()}.
     * 
     * @param serviceName the name of the service for which to locate an available proxy
     * @param applicationId the id of the application for which to locate an available proxy for the
     *        given service name
     * @return an available proxy for the service with the given name, or null if none is available
     * @throws IllegalArgumentException if serviceName is null
     */
    Object getService(QName serviceName, String applicationId);

    /**
     * Publish a service with the given ServiceDefinition to the service bus. This effectively
     * updates the service registry and provides an endpoint for other applications to invoke. If
     * this application has already published a service under this name, it will be updated instead
     * to reflect the new ServiceDefinition.
     * 
     * <p>
     * The method also provides the ability for the service bus to immediately synchronize with the
     * service registry after registering the service if {@code synchronize} is set to {@code true}.
     * 
     * @see #synchronize()
     * 
     * @param serviceDefinition the definition of the service to publish, must not be null
     * @param synchronize indicates whether or not this service bus client should immediately
     *        synchronize it's changes with the registry after registering the service.
     * @return the service configuration for the published service
     * @throws IllegalArgumentException if serviceDefinition is null
     */
    ServiceConfiguration publishService(ServiceDefinition serviceDefinition, boolean synchronize);

    /**
     * Functions as per {@link #publishService(ServiceDefinition, boolean)} but allows for multiple
     * services to be published to the bus in a single operation. If the given list of service
     * definitions is empty then this method will do nothing (including skipping synchronization
     * with the registry if that was requested).
     * 
     * @see #publishService(ServiceDefinition, boolean)
     * @see #synchronize()
     * 
     * @param serviceDefinitions the list of definition for the services to publish, must not be
     *        null
     * @param synchronize indicates whether or not this service bus client should immediately
     *        synchronize it's changes with the registry after registering the services.
     * @return the list of service configurations for the published services in the same order as
     *         the list of service definitions
     * @throws IllegalArgumentException if serviceDefinitions is null
     */
    List<ServiceConfiguration> publishServices(List<ServiceDefinition> serviceDefinitions, boolean synchronize);

    /**
     * Removes the service from the service bus and the service registry with the given service
     * name. Client applications should only be able to remove services that they themselves have
     * published.
     * 
     * <p>
     * This method also provides the ability for the service bus to immediately synchronize with the
     * service registry after removing the service if {@code synchronize} is set to {@code true}. If
     * the service is not located and successfully removed, however, the sychronization will not
     * run.
     * 
     * @see #synchronize()
     * 
     * @param serviceName the name of the service to remove
     * @param synchronize indicates whether or not this service bus client should immediately
     *        synchronize after removing the service
     * @return true if the service was removed, false otherwise
     * @throws IllegalArgumentException if the given serviceName is null
     */
    boolean removeService(QName serviceName, boolean synchronize);

    /**
     * Functions as per {@link #removeService(QName, boolean)} but allows for multiple services to
     * be removed from the bus in a single operation. If the given list of service names is empty
     * then this method will do nothing (including skipping synchronization with the registry if
     * that was requested).
     * 
     * <p>
     * If the list returned from the method contains only false responses (meaning that no services
     * were removed) this method will skip synchronization even if it is requested.
     * 
     * @see #removeService(QName, boolean)
     * @see #synchronize()
     * 
     * @param serviceNames the list of names for the services to remove, must not be null
     * @param synchronize indicates whether or not this service bus client should immediately
     *        synchronize it's changes with the registry after removing the services.
     * @return a list of Booleans indicating which of the service removals were successful. This
     *         list will be in the same order as the list of service configurations that were passed
     *         in.
     * @throws IllegalArgumentException if serviceNames is null
     */
    List<Boolean> removeServices(List<QName> serviceNames, boolean synchronize);

    /**
     * Synchronizes the current client's service bus with the central service registry. This may be
     * done automatically on a periodic basic depending on the implementation of this service, but
     * can be invoked manually through this method. This method should both register any outstanding
     * service publications to the registry, as well as detect any changes in remote services that
     * have been published/removed by other applications in the registry and update local service
     * bus state accordingly.
     *
     * <p>Invoking this method is equivalent to invoking {@link #synchronizeLocalServices()} and
     * {@link #synchronizeRemoteServices()} in either order.  However, the semantics vary slightly
     * as this method should attempt to invoke them as an atomic operation.</p>
     */
    void synchronize();

    /**
     * Fetches information about the current state of the remote services available in the registry
     * and the synchronizes the current client's service bus state.
     */
    void synchronizeRemoteServices();

    /**
     * Synchronizes information about the services this client is publishing with the central
     * service registry.
     */
    void synchronizeLocalServices();

}
