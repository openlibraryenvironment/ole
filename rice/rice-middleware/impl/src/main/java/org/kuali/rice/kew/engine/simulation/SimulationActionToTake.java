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

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.api.action.RoutingReportActionToTake;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

/**
 * An object represnting an action to take in the simulation engine
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SimulationActionToTake implements Serializable {
	private static final long serialVersionUID = 5212455086079117671L;

	private String actionToPerform;
    private Person user;
    private String nodeName;

    public SimulationActionToTake() {
    }

	public String getActionToPerform() {
		return actionToPerform;
	}

	public void setActionToPerform(String actionToPerform) {
		this.actionToPerform = actionToPerform;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Person getUser() {
		return user;
	}

	public void setUser(Person user) {
		this.user = user;
	}

    public static SimulationActionToTake from(RoutingReportActionToTake actionToTake) {
        if (actionToTake == null) {
            return null;
        }
        SimulationActionToTake simActionToTake = new SimulationActionToTake();
        simActionToTake.setNodeName(actionToTake.getNodeName());
        if (StringUtils.isBlank(actionToTake.getActionToPerform())) {
            throw new IllegalArgumentException("ReportActionToTakeVO must contain an action taken code and does not");
        }
        simActionToTake.setActionToPerform(actionToTake.getActionToPerform());
        if (actionToTake.getPrincipalId() == null) {
            throw new IllegalArgumentException("ReportActionToTakeVO must contain a principalId and does not");
        }
        Principal kPrinc = KEWServiceLocator.getIdentityHelperService().getPrincipal(actionToTake.getPrincipalId());
        Person user = KimApiServiceLocator.getPersonService().getPerson(kPrinc.getPrincipalId());
        if (user == null) {
            throw new IllegalStateException("Could not locate Person for the given id: " + actionToTake.getPrincipalId());
        }
        simActionToTake.setUser(user);
        return simActionToTake;
    }

}
