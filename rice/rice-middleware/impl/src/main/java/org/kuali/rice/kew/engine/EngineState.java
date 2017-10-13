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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kew.engine.node.RouteNodeInstance;


/**
 * Represents the current state of the workflow engine.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EngineState implements Serializable {
    
	private static final long serialVersionUID = 2405363802483005090L;

	private static int currentSimulationId = -10;
    
    private RouteNodeInstance transitioningFrom;
    private RouteNodeInstance transitioningTo;
    private List completeNodeInstances = new ArrayList();
    private List generatedRequests = new ArrayList();
    
    public List getCompleteNodeInstances() {
        return completeNodeInstances;
    }
    public void setCompleteNodeInstances(List completeNodeInstances) {
        this.completeNodeInstances = completeNodeInstances;
    }
    public RouteNodeInstance getTransitioningFrom() {
        return transitioningFrom;
    }
    public void setTransitioningFrom(RouteNodeInstance transitioningFrom) {
        this.transitioningFrom = transitioningFrom;
    }
    public RouteNodeInstance getTransitioningTo() {
        return transitioningTo;
    }
    public void setTransitioningTo(RouteNodeInstance transitioningTo) {
        this.transitioningTo = transitioningTo;
    }
    public List getGeneratedRequests() {
        return generatedRequests;
    }
    public void setGeneratedRequests(List generatedRequests) {
        this.generatedRequests = generatedRequests;
    }
    
    /**
     * Gets the next id to be used for simulation purposes.  Since, during simulation, we cannot save to the database and get primary keys
     * assigned to our data beans, this method will be used to get a new simulation id which is guaranteed to be a negative number
     * which will be unique for at least the lifetime of the simulation.
     */
    public String getNextSimulationId() {
        synchronized (EngineState.class) {
            return String.valueOf(currentSimulationId--);
        }
    }
    
}
