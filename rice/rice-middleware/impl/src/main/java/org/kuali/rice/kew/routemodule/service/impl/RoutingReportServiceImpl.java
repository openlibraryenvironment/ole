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
package org.kuali.rice.kew.routemodule.service.impl;

import org.kuali.rice.kew.engine.simulation.SimulationCriteria;
import org.kuali.rice.kew.engine.simulation.SimulationResults;
import org.kuali.rice.kew.engine.simulation.SimulationWorkflowEngine;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routemodule.service.RoutingReportService;
import org.kuali.rice.kew.service.KEWServiceLocator;


public class RoutingReportServiceImpl implements RoutingReportService {

    public DocumentRouteHeaderValue report(SimulationCriteria criteria) {
    	try {
    		SimulationWorkflowEngine simulationEngine = KEWServiceLocator.getSimulationEngine();
    		SimulationResults results = simulationEngine.runSimulation(criteria);
    		return materializeDocument(results);
        } catch (Exception e) {
        	if (e instanceof RuntimeException) {
        		throw (RuntimeException)e;
        	}
            throw new IllegalStateException("Problem running report: " + e.getMessage(), e);
        }
    }

    /**
     * The document returned does not have any of the simulated action requests set on it, we'll want to set them.
     */
    private DocumentRouteHeaderValue materializeDocument(SimulationResults results) {
    	DocumentRouteHeaderValue document = results.getDocument();
		//document.getActionRequests().addAll(results.getSimulatedActionRequests());
    	document.getSimulatedActionRequests().addAll(results.getSimulatedActionRequests());
        return document;

    }
}
