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
package org.kuali.rice.ksb.messaging.web;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceBus;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.api.registry.ServiceInfo;
import org.kuali.rice.ksb.api.registry.ServiceRegistry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Struts action for interacting with the queue of messages.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ServiceRegistryAction extends KSBAction {

	private static final String REMOVED_APPLICATION_ID_PARAM = "removedApplicationId";
    private static final Logger LOG = Logger.getLogger(ServiceRegistryAction.class);

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	return mapping.findForward("basic");
    }

    public ActionForward refreshServiceRegistry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    	HttpServletResponse response) throws IOException, ServletException {
    	// TODO is this what really constitutes a "refresh" of the service registry?
    	KsbApiServiceLocator.getServiceBus().synchronize();
    	return mapping.findForward("basic");
    }
    
	/**
     * Enable deletion of localhost service registry entries.
     */
    public ActionForward deleteLocalhostEntries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        	HttpServletResponse response) throws IOException, ServletException {
    	ServiceRegistry serviceRegistry = KsbApiServiceLocator.getServiceRegistry();
    	List<ServiceInfo> serviceInfos = serviceRegistry.getAllOnlineServices();
    	List<String> serviceEndpointsToDelete = new ArrayList<String>();
    	for (ServiceInfo serviceInfo : serviceInfos) {
    		if (serviceInfo.getServerIpAddress().equals("localhost") ||
                serviceInfo.getEndpointUrl().contains("localhost")) {
    			serviceEndpointsToDelete.add(serviceInfo.getServiceId());
    		}
    	}
    	serviceRegistry.removeServiceEndpoints(serviceEndpointsToDelete);
    	KsbApiServiceLocator.getServiceBus().synchronize();
		return mapping.findForward("basic");
    }

    public ActionForward deleteApplicationIdEntries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        
        final String applicationId = request.getParameter(REMOVED_APPLICATION_ID_PARAM);
        if(StringUtils.isNotBlank(applicationId)) {
            ServiceRegistry serviceRegistry = KsbApiServiceLocator.getServiceRegistry();
            List<ServiceInfo> serviceInfos = serviceRegistry.getAllOnlineServices();
            List<String> serviceEndpointsToDelete = new ArrayList<String>();
            for (ServiceInfo serviceInfo : serviceInfos) {
                if (serviceInfo.getApplicationId().equals(applicationId)) {
                    serviceEndpointsToDelete.add(serviceInfo.getServiceId());
                }
            }
            serviceRegistry.removeServiceEndpoints(serviceEndpointsToDelete);
            KsbApiServiceLocator.getServiceBus().synchronize();
        } else {
            LOG.info("No rows were deleted from the KRSB_SVC_DEF_T table because the application ID was null or blank.");
        }

        return mapping.findForward("basic");
    }

    public ActionMessages establishRequiredState(HttpServletRequest request, ActionForm actionForm) throws Exception {
	ServiceRegistryForm form = (ServiceRegistryForm)actionForm;
	form.setMyIpAddress(RiceUtilities.getIpNumber());
	form.setMyApplicationId(CoreConfigHelper.getApplicationId());
	form.setDevMode(ConfigContext.getCurrentContextConfig().getDevMode());
	ServiceBus serviceBus = KsbApiServiceLocator.getServiceBus();
	form.setMyInstanceId(serviceBus.getInstanceId());
	form.setPublishedServices(getPublishedServices(serviceBus));
	ServiceRegistry serviceRegistry = KsbApiServiceLocator.getServiceRegistry();
	form.setGlobalRegistryServices(getGlobalRegistryServices(serviceRegistry));

	return null;
    }

    private List<ServiceConfiguration> getPublishedServices(ServiceBus serviceBus) {
    	Map<QName, Endpoint> localEndpoints = serviceBus.getLocalEndpoints();
    	List<ServiceConfiguration> publishedServices = new ArrayList<ServiceConfiguration>();
    	for (Endpoint endpoint : localEndpoints.values()) {
    		publishedServices.add(endpoint.getServiceConfiguration());
    	}
    	return publishedServices;
    }

    private List<ServiceInfo> getGlobalRegistryServices(ServiceRegistry serviceRegistry) {
    	return serviceRegistry.getAllServices();
    }

}
