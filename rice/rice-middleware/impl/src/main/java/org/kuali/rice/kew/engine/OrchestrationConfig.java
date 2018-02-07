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

import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiConstants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Specifies configuration for orchestration through the engine.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class OrchestrationConfig {

    public enum EngineCapability { STANDARD, BLANKET_APPROVAL, SIMULATION }
    
    private final EngineCapability capability;
    private final boolean sendNotifications;
    private final String notificationType = KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ;
    private final Set<String> destinationNodeNames;
    private final ActionTakenValue cause;
    private final boolean runPostProcessorLogic;
    
    public OrchestrationConfig(EngineCapability capability) {
        this(capability, Collections.<String>emptySet(), null, true, true);
    }
    
    public OrchestrationConfig(EngineCapability capability, boolean isRunPostProcessorLogic) {
        this(capability, Collections.<String>emptySet(), null, true, isRunPostProcessorLogic);
    }
    
    public OrchestrationConfig(EngineCapability capability, Set<String> destinationNodeNames, ActionTakenValue cause) {
        this(capability, destinationNodeNames, cause, true, true);
    }
    
    public OrchestrationConfig(EngineCapability capability, Set<String> destinationNodeNames, ActionTakenValue cause, boolean sendNotifications, boolean doRunPostProcessorLogic) {
        this.capability = capability;
        this.destinationNodeNames = Collections.unmodifiableSet(new HashSet<String>(destinationNodeNames));
        this.cause = cause;
        this.sendNotifications = sendNotifications;
        this.runPostProcessorLogic = doRunPostProcessorLogic;
    }
    
    public Set<String> getDestinationNodeNames() {
        return destinationNodeNames;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public boolean isSendNotifications() {
        return sendNotifications;
    }

    public ActionTakenValue getCause() {
        return cause;
    }

	public boolean isRunPostProcessorLogic() {
		return this.runPostProcessorLogic;
	}

    /**
     * @return the capability
     */
    public EngineCapability getCapability() {
        return this.capability;
    }    
    
}
