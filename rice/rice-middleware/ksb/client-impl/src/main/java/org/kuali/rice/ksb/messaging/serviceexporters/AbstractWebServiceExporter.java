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

import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.endpoint.ServerRegistry;
import org.apache.log4j.Logger;
import org.kuali.rice.ksb.api.bus.ServiceDefinition;
import org.kuali.rice.ksb.messaging.bam.BAMServerProxy;
import org.kuali.rice.ksb.messaging.servlet.CXFServletControllerAdapter;

/**
 * Abstract ServiceExporter for web services 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public abstract class AbstractWebServiceExporter implements ServiceExporter {

    static final Logger LOG = Logger.getLogger(AbstractWebServiceExporter.class);
    
    private final ServiceDefinition serviceDefinition;
    private final Bus cxfBus;

    protected abstract void publishService(ServiceDefinition serviceDefinition, Object serviceImpl, String address) throws Exception;

    public AbstractWebServiceExporter(ServiceDefinition serviceDefinition, Bus cxfBus) {
        this.serviceDefinition = serviceDefinition;
        this.cxfBus = cxfBus;
    }
    
    @Override
    public Object exportService(ServiceDefinition serviceDefinition) {
    	try {			
    		String serviceAddress = getServiceAddress(serviceDefinition);
    		
    		//Publish the CXF service if it hasn't already been published
    		if (!(isServicePublished(serviceAddress))){
    			publishService(serviceDefinition, serviceDefinition.getService(), serviceAddress);
    		}
    		
    		//Create a CXF mvc controller for this service
    		CXFServletControllerAdapter cxfController = new CXFServletControllerAdapter();
    		
    		return BAMServerProxy.wrap(cxfController, serviceDefinition);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    	
    }

    /**
     * @return the address where the service is (or will be) published
     */
    protected String getServiceAddress(ServiceDefinition serviceDefinition) {
        //Determine endpoint address to publish service on
        String serviceAddress = serviceDefinition.getServicePath();
        if (("/").equals(serviceAddress)){
        	serviceAddress = serviceAddress + serviceDefinition.getServiceName().getLocalPart();
        } else {
        	serviceAddress = serviceAddress + "/" + serviceDefinition.getServiceName().getLocalPart();
        }
        return serviceAddress;
    }

    /** 
     * This determines if the service has already been published on the CXF bus.
     * 
     * @return true if cxf server exists for this service.
     */
    protected boolean isServicePublished(String serviceAddress) {
    	
    	ServerRegistry serverRegistry = getCXFServerRegistry();
    	List<Server> servers = serverRegistry.getServers();
    	
    	for (Server server:servers){		
    		String endpointAddress = server.getEndpoint().getEndpointInfo().getAddress();
    		if (endpointAddress.contains(serviceAddress)){
    			LOG.info("Service already published on CXF, not republishing: " + serviceAddress);
    			return true;
    		}
    	}
    	
    	return false;
    }

    protected ServiceDefinition getServiceDefinition() {
    	return this.serviceDefinition;
    }

    protected Bus getCXFBus() {
    	return this.cxfBus;
    }

    protected ServerRegistry getCXFServerRegistry() {
    	return getCXFBus().getExtension(ServerRegistry.class);
    }

}
