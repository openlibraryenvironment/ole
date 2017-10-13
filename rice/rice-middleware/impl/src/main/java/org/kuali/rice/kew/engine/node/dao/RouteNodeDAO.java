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
package org.kuali.rice.kew.engine.node.dao;

import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;

import java.util.List;



public interface RouteNodeDAO {

    public void save(RouteNode node);
    public void save(RouteNodeInstance nodeInstance);
    public void save(NodeState nodeState);
    public void save(Branch branch);
    public RouteNode findRouteNodeById(String nodeId);
    public RouteNodeInstance findRouteNodeInstanceById(String nodeInstanceId);
    public List<RouteNodeInstance> getActiveNodeInstances(String documentId);
    public List<String> getActiveRouteNodeNames(String documentId);
    public List<RouteNodeInstance> getTerminalNodeInstances(String documentId);
    public List<String> getTerminalRouteNodeNames(String documentId);
    public List<String> getCurrentRouteNodeNames(String documentId);
    public List getInitialNodeInstances(String documentId);
    public NodeState findNodeState(Long nodeInstanceId, String key);
    public RouteNode findRouteNodeByName(String documentTypeId, String name);
    public List<RouteNode> findFinalApprovalRouteNodes(String documentTypeId);
    public List findProcessNodeInstances(RouteNodeInstance process);
    public List findRouteNodeInstances(String documentId);
    public void deleteLinksToPreNodeInstances(RouteNodeInstance routeNodeInstance);
    public void deleteRouteNodeInstancesHereAfter(RouteNodeInstance routeNodeInstance);
    public void deleteNodeStateById(Long nodeStateId);
    public void deleteNodeStates(List statesToBeDeleted);
	
}
