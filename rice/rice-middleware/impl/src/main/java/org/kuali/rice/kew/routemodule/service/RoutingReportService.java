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
package org.kuali.rice.kew.routemodule.service;

import org.kuali.rice.kew.engine.simulation.SimulationCriteria;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;


/**
 * A service for executing routing reports from {@link SimulationCriteria}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RoutingReportService {

	//public DocumentRouteHeaderValue report(String documentId, Integer startRouteLevel, Integer endRouteLevel, List usersToFilterIn) throws WorkflowException;
	
	//public DocumentRouteHeaderValue report(String documentTypeName, DocumentContent documentContent, Integer startRouteLevel, Integer endRouteLevel, List usersToFilterIn) throws WorkflowException;
	
	//public DocumentRouteHeaderValue simulationReport(DocumentRouteHeaderValue document, List actionsToTake) throws WorkflowException;
    
    public DocumentRouteHeaderValue report(SimulationCriteria criteria);
	
}
