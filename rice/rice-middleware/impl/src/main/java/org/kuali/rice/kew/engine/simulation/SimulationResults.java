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
package org.kuali.rice.kew.engine.simulation;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;


/**
 * A set of results from the {@link SimulationEngine}.
 *
 * @see SimulationEngine
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SimulationResults {

	private DocumentRouteHeaderValue document;
    private List<ActionRequestValue> simulatedActionRequests = new ArrayList<ActionRequestValue>();
    private List simulatedActionsTaken = new ArrayList();

    public DocumentRouteHeaderValue getDocument() {
		return document;
	}
	public void setDocument(DocumentRouteHeaderValue document) {
		this.document = document;
	}
	public List<ActionRequestValue> getSimulatedActionRequests() {
		return simulatedActionRequests;
	}
	public void setSimulatedActionRequests(List simulatedActionRequests) {
		this.simulatedActionRequests = simulatedActionRequests;
	}
	public List getSimulatedActionsTaken() {
		return simulatedActionsTaken;
	}
	public void setSimulatedActionsTaken(List simulatedActionsTaken) {
		this.simulatedActionsTaken = simulatedActionsTaken;
	}
    
}
