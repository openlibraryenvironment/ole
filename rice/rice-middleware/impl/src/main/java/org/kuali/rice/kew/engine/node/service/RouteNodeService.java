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
package org.kuali.rice.kew.engine.node.service;

import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.NodeGraphSearchCriteria;
import org.kuali.rice.kew.engine.node.NodeGraphSearchResult;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;

import java.util.List;


/**
 * A service which provides data access for {@link RouteNode}, {@link RouteNodeInstance}, 
 * {@link NodeState}, and {@link Branch} objects.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RouteNodeService {

    public void save(RouteNode node);
    public void save(RouteNodeInstance nodeInstance);
    public void save(NodeState nodeState);
    public void save(Branch branch);
    public RouteNode findRouteNodeById(String nodeId);
    public RouteNodeInstance findRouteNodeInstanceById(String nodeInstanceId);
    
    /**
     * 
     * This method looks though the passed in DocumentRouteHeaderValue and retrieves a nodeInstance that
     * matches the ID passed in. 
     * 
     * @param nodeInstanceId
     * @param document
     * @return
     */
    public RouteNodeInstance findRouteNodeInstanceById(String nodeInstanceId, DocumentRouteHeaderValue document);

    /**
     * Retrieves the initial node instances of the given document.  The initial node instances are 
     * those node instances which are at the very beginning of the route.  Usually, this will
     * just be a single node instance.
     */
    public List getInitialNodeInstances(String documentId);

    /**
     * Retrieves the active node instances of the given Document.  The active node instances
     * represent where in the route path the document is currently located.
     * @param documentId of the document
     * @return list of route node instances
     */
    public List<RouteNodeInstance> getActiveNodeInstances(String documentId);

    /**
     * Retrieves the names of the route node instances where the document is currently located
     * for the document with the given id. This could be active nodes in the document if it is
     * in the middle of the routing process or it could be the names of the terminal nodes if
     * the document has completed routing.
     *
     * @param documentId of the document
     * @return list of names of the current route node instances
     * @since 2.1
     */
    public List<String> getCurrentRouteNodeNames(String documentId);

    /**
     * Retrieves the names of active node instances for the document with the
     * given id.  The active node instances represent where in the route path
     * the document is currently located.
     * @param documentId of the document
     * @return list of names of route node instances
     * @since 2.1
     */
    public List<String> getActiveRouteNodeNames(String documentId);
    
    public List<RouteNodeInstance> getActiveNodeInstances(DocumentRouteHeaderValue document);
    
    /**
     * Retrieves the names of terminal node instances for the document with the
     * given id. The terminal node instances are nodes in the route path which
     * are both inactive and complete and have no next nodes in their path.
     * Terminal node instances will typically only exist on documents which are
     * no longer enroute.
     * @param documentId for the given Document
     * @return list of terminal node instances
     * @since 2.1
     */
    public List<String> getTerminalRouteNodeNames(String documentId);
    
    /**
     * Retrieves the terminal node instances of the given Document.  The terminal node instances
     * are nodes in the route path which are both inactive and complete and have no next nodes
     * in their path.  Terminal node instances will typically only exist on documents which are no
     * longer Enroute.
     * @param documentId for the given Document
     * @return list of terminal node instances
     */
    public List<RouteNodeInstance> getTerminalNodeInstances(String documentId);
    
    /**
     * Returns the node instances representing the most recent node instances in the document.
     * The algorithm for locating the current nodes is as follows: If the document has
     * active node instances, return those, otherwise return it's terminal node instances.
     */
    public List<RouteNodeInstance> getCurrentNodeInstances(String documentId);

    public NodeState findNodeState(Long nodeInstanceId, String key);
    public RouteNode findRouteNodeByName(String documentTypeId, String name);
    public List<RouteNode> findFinalApprovalRouteNodes(String documentTypeId);
    public List findNextRouteNodesInPath(RouteNodeInstance nodeInstance, String nodeName);
    public boolean isNodeInPath(DocumentRouteHeaderValue document, String nodeName);
    public List findRouteNodeInstances(String documentId);
    public List findProcessNodeInstances(RouteNodeInstance process);
    public List<String> findPreviousNodeNames(String documentId);
    
    /**
     * Returns a List of the distinct node names through which this document might pass in it's future 
     * routing.  In certain cases this will be an approximation based on what the system knows at the
     * time of execution. 
     */
    public List<String> findFutureNodeNames(String documentId);
    
    /**
     * Flatten all the document types route nodes into a single List.  This includes all processes 
     * on the DocumentType.
     * 
     * @param documentType DocumentType who's nodes will be flattened.
     * @param climbHierarchy whether to include the parents nodes if the passed in DocumentType contains no nodes
     * @return List or empty List
     */
    public List<RouteNode> getFlattenedNodes(DocumentType documentType, boolean climbHierarchy);
    public List<RouteNode> getFlattenedNodes(ProcessDefinitionBo process);
    
    /**
     * Returns a flattened list of RouteNodeInstances on the given document.  If the includeProcesses flag is
     * true than this method includes process RouteNodeInstances, otherwise they are excluded.
     * which are processes.
     * @param document route header value
     * @param includeProcesses flag
     * @return list of routeNodeInstances
     */
    public List<RouteNodeInstance> getFlattenedNodeInstances(DocumentRouteHeaderValue document, boolean includeProcesses);
    
    public NodeGraphSearchResult searchNodeGraph(NodeGraphSearchCriteria criteria);
    
    /**
     * Returns a list of active node instances associated with the document that are active
     * @param document
     * @param nodeName
     * @return
     */
    public List<RouteNodeInstance> getActiveNodeInstances(DocumentRouteHeaderValue document, String nodeName);
        public void deleteByRouteNodeInstance(RouteNodeInstance routeNodeInstance);
    public void deleteNodeStateById(Long nodeStateId);
    public void deleteNodeStates(List statesToBeDeleted);
    
    /**
	 * Record that the given RouteNodeInstance on the Document was revoked.  This will happen when an 
	 * action such as Return to Previous or Move Document bypasses the given RouteNodeInstance on it's
	 * path back to a previous point in the history of the document's route path. 
	 */
    public void revokeNodeInstance(DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance);
    
    /**
     * Returns a List of the revoked RouteNodeInstances on the given Document.
     * 
     * @see revokeNodeInstance
     */
    public List getRevokedNodeInstances(DocumentRouteHeaderValue document);
    
}
