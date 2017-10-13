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

import org.kuali.rice.kew.engine.node.RouteNodeInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains context of the main processing loop of the workflow engine.  Essentially 
 * contains a List of node instances which need to be processed.  After a node instance
 * is processed from the context, the engine will check the status of the complete
 * flag to determine whether processing should continue or halt.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ProcessContext {

    private boolean complete = true;
    private final List<RouteNodeInstance> nextNodeInstances;
    
    public ProcessContext() {
        this(true, new ArrayList<RouteNodeInstance>());
    }
    
    public ProcessContext(boolean complete, List<RouteNodeInstance> nextNodeInstances) {
        this.complete = complete;
        this.nextNodeInstances = nextNodeInstances;
    }

    public List<RouteNodeInstance> getNextNodeInstances() {
        return nextNodeInstances;
    }
    
    public boolean isComplete() {
        return complete;
    }
    
    public void setComplete(boolean complete) {
        this.complete = complete;
    }
    
}
