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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.ksb.api.KsbApiConstants;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceBus;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.api.bus.ServiceBusAdminService;


/**
 * Struts action for interacting with the queue of messages.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ServiceBusAction extends KSBAction {

    private static final QName SERVICE_BUS_ADMIN_SERVICE_QUEUE = new QName(
            KsbApiConstants.Namespaces.KSB_NAMESPACE_2_0, "serviceBusAdminService");
    private static final Logger LOG = Logger.getLogger(ServiceBusAction.class);

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("basic");
	}

	public ActionForward refreshServiceBus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

        String applicationId = ConfigContext.getCurrentContextConfig().getProperty(
                CoreConstants.Config.APPLICATION_ID);

        List<Endpoint> endpoints = KsbApiServiceLocator.getServiceBus().getEndpoints(SERVICE_BUS_ADMIN_SERVICE_QUEUE, applicationId);
        if (endpoints.isEmpty()) {
            KsbApiServiceLocator.getServiceBus().synchronize();
        } else {
            for (Endpoint endpoint : endpoints) {
                ServiceBusAdminService serviceBusAdminService = (ServiceBusAdminService) endpoint.getService();
                LOG.info("Calling " + endpoint.getServiceConfiguration().getEndpointUrl() + " on " +
                    endpoint.getServiceConfiguration().getInstanceId());

                serviceBusAdminService.clearServiceBusCache();
            }
        }
		return mapping.findForward("basic");
	}

	public ActionMessages establishRequiredState(HttpServletRequest request, ActionForm actionForm) throws Exception {
		ServiceBusForm form = (ServiceBusForm)actionForm;
		form.setMyIpAddress(RiceUtilities.getIpNumber());
		form.setMyApplicationId(CoreConfigHelper.getApplicationId());
		form.setDevMode(ConfigContext.getCurrentContextConfig().getDevMode());
		ServiceBus serviceBus = KsbApiServiceLocator.getServiceBus();
		form.setMyInstanceId(serviceBus.getInstanceId());
		form.setPublishedServices(getPublishedServices(serviceBus));
		form.setGlobalServices(getGlobalServices(serviceBus));

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

	private List<ServiceConfiguration> getGlobalServices(ServiceBus serviceBus) {
		List<ServiceConfiguration> allServices = new ArrayList<ServiceConfiguration>();
		for(Endpoint endpoint : serviceBus.getAllEndpoints()) {
			allServices.add(endpoint.getServiceConfiguration());
		}
		return allServices;
	}

}
