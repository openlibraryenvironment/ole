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
package org.kuali.rice.kew.engine.node;

import java.util.HashMap;
import java.util.Map;

/**
 * The current context of a search process within a node graph.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NodeGraphContext {

	private RouteNodeInstance previousNodeInstance;
    private RouteNodeInstance currentNodeInstance;
    private RouteNodeInstance resultNodeInstance;
    private Map<String, RouteNodeInstance> visited = new HashMap<String, RouteNodeInstance>();
    private Map<String, Integer> splitState = new HashMap<String, Integer>();
    
    public RouteNodeInstance getCurrentNodeInstance() {
        return currentNodeInstance;
    }
    public void setCurrentNodeInstance(RouteNodeInstance currentNodeInstance) {
        this.currentNodeInstance = currentNodeInstance;
    }
    public RouteNodeInstance getPreviousNodeInstance() {
        return previousNodeInstance;
    }
    public void setPreviousNodeInstance(RouteNodeInstance previousNodeInstance) {
        this.previousNodeInstance = previousNodeInstance;
    }
    public RouteNodeInstance getResultNodeInstance() {
        return resultNodeInstance;
    }
    public void setResultNodeInstance(RouteNodeInstance resultNodeInstance) {
        this.resultNodeInstance = resultNodeInstance;
    }
    public Map<String, Integer> getSplitState() {
        return splitState;
    }
    public void setSplitState(Map<String, Integer> splitState) {
        this.splitState = splitState;
    }
    public Map<String, RouteNodeInstance> getVisited() {
        return visited;
    }
    public void setVisited(Map<String, RouteNodeInstance> visited) {
        this.visited = visited;
    }

}
