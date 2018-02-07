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
package org.kuali.rice.kew.engine;

import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.engine.node.service.RouteNodeService;
import org.kuali.rice.kew.engine.simulation.SimulationEngine;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.springframework.beans.factory.InitializingBean;

/**
 * An implementation of the {@link WorkflowEngineFactory}. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WorkflowEngineFactoryImpl implements WorkflowEngineFactory, InitializingBean {

    private RouteNodeService routeNodeService;
    private RouteHeaderService routeHeaderService;
    private ParameterService parameterService;
    
    
    /**
     * Ensures that all dependencies were injected into this factory.
     * 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     * @throws IllegalStateException if any of the required services are null
     */
    @Override
    public void afterPropertiesSet() {
        if (routeNodeService == null) {
            throw new IllegalStateException("routeNodeService not properly injected, was null.");
        }
        if (routeHeaderService == null) {
            throw new IllegalStateException("routeHeaderService not properly injected, was null.");
        }
        //if (parameterService == null) {
        //    throw new IllegalStateException("parameterService not properly injected, was null.");
        //}
    }
    
    /**
     * @see org.kuali.rice.kew.engine.WorkflowEngineFactory#newEngine(org.kuali.rice.kew.engine.OrchestrationConfig)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <E extends WorkflowEngine> E newEngine(OrchestrationConfig config) {
        switch (config.getCapability()) {
            case STANDARD:
                return (E) new StandardWorkflowEngine(routeNodeService, routeHeaderService, parameterService, config);
            case BLANKET_APPROVAL:
                return (E) new BlanketApproveEngine(routeNodeService, routeHeaderService, parameterService, config);
            case SIMULATION:
                return (E) new SimulationEngine(routeNodeService, routeHeaderService, parameterService, config);
        }
        
        return null;
    }
    
    /**
     * @return the routeNodeService
     */
    public RouteNodeService getRouteNodeService() {
        return this.routeNodeService;
    }


    /**
     * @param routeNodeService the routeNodeService to set
     */
    public void setRouteNodeService(RouteNodeService routeNodeService) {
        this.routeNodeService = routeNodeService;
    }


    /**
     * @return the routeHeaderService
     */
    public RouteHeaderService getRouteHeaderService() {
        return this.routeHeaderService;
    }


    /**
     * @param routeHeaderService the routeHeaderService to set
     */
    public void setRouteHeaderService(RouteHeaderService routeHeaderService) {
        this.routeHeaderService = routeHeaderService;
    }


    /**
     * @return the parameterService
     */
    public ParameterService getParameterService() {
        if (this.parameterService == null) {
            this.parameterService = CoreFrameworkServiceLocator.getParameterService();
        }
        return this.parameterService;
    }


    /**
     * @param parameterService the parameterService to set
     */
    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

}
