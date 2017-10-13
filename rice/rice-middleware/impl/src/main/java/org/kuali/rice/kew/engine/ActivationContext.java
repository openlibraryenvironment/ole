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

import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the current Activation context of the workflow engine
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActivationContext implements Serializable {
    private static final long serialVersionUID = 5063689034941122774L;

    public static final boolean CONTEXT_IS_SIMULATION = true;

    boolean simulation = false;
    boolean activateRequests = false;
    List<ActionTakenValue> simulatedActionsTaken = new ArrayList<ActionTakenValue>();
    List<ActionItem> generatedActionItems = new ArrayList<ActionItem>();


	public ActivationContext(boolean simulation) {
		super();
		this.simulation = simulation;
	}

	public ActivationContext(boolean simulation, List<ActionTakenValue> simulatedActionsTaken) {
		super();
		this.simulation = simulation;
		this.simulatedActionsTaken = simulatedActionsTaken;
	}

    public boolean isActivateRequests() {
        return this.activateRequests;
    }

    public void setActivateRequests(boolean activateRequests) {
        this.activateRequests = activateRequests;
    }

    public List<ActionItem> getGeneratedActionItems() {
        return generatedActionItems;
    }

    public void setGeneratedActionItems(List<ActionItem> generatedActionItems) {
        this.generatedActionItems = generatedActionItems;
    }

    public List<ActionTakenValue> getSimulatedActionsTaken() {
        return simulatedActionsTaken;
    }

    public void setSimulatedActionsTaken(List<ActionTakenValue> simulatedActionsTaken) {
        this.simulatedActionsTaken = simulatedActionsTaken;
    }

    public boolean isSimulation() {
        return simulation;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

}
