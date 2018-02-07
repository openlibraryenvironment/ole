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
package org.kuali.rice.kew.engine.node.service.impl;

import org.apache.commons.collections.ComparatorUtils;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.engine.node.NodeGraphContext;
import org.kuali.rice.kew.engine.node.NodeGraphSearchCriteria;
import org.kuali.rice.kew.engine.node.NodeGraphSearchResult;
import org.kuali.rice.kew.engine.node.NodeMatcher;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.engine.node.RouteNodeUtils;
import org.kuali.rice.kew.engine.node.dao.RouteNodeDAO;
import org.kuali.rice.kew.engine.node.service.RouteNodeService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class RouteNodeServiceImpl implements RouteNodeService {

	protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(getClass());
	
	public static final String REVOKED_NODE_INSTANCES_STATE_KEY = "NodeInstances.Revoked";

	private static final Comparator NODE_INSTANCE_FORWARD_SORT = new NodeInstanceIdSorter();
	private static final Comparator NODE_INSTANCE_BACKWARD_SORT = 
		ComparatorUtils.reversedComparator(NODE_INSTANCE_FORWARD_SORT);
    private RouteHelper helper = new RouteHelper();
	private RouteNodeDAO routeNodeDAO;
	
    public void save(RouteNode node) {
    	routeNodeDAO.save(node);
    }
    
    public void save(RouteNodeInstance nodeInstance) {
    	routeNodeDAO.save(nodeInstance);
    }
    
    public void save(NodeState nodeState) {
        routeNodeDAO.save(nodeState);
    }
    
    public void save(Branch branch) {
        routeNodeDAO.save(branch);
    }

    public RouteNode findRouteNodeById(String nodeId) {
    	return routeNodeDAO.findRouteNodeById(nodeId);
    }
    
    public RouteNodeInstance findRouteNodeInstanceById(String nodeInstanceId) {
    	return routeNodeDAO.findRouteNodeInstanceById(nodeInstanceId);
    }

    public RouteNodeInstance findRouteNodeInstanceById(String nodeInstanceId, DocumentRouteHeaderValue document) {
    	return RouteNodeUtils.findRouteNodeInstanceById(nodeInstanceId, document);
    }
    
    public List<RouteNodeInstance> getCurrentNodeInstances(String documentId) {
        List<RouteNodeInstance> currentNodeInstances = getActiveNodeInstances(documentId);
        if (currentNodeInstances.isEmpty()) {
            currentNodeInstances = getTerminalNodeInstances(documentId);
        }
        return currentNodeInstances;
    }
    
    public List<RouteNodeInstance> getActiveNodeInstances(String documentId) {
    	return routeNodeDAO.getActiveNodeInstances(documentId);
    }
    
    public List<RouteNodeInstance> getActiveNodeInstances(DocumentRouteHeaderValue document) {
       List<RouteNodeInstance> flattenedNodeInstances = getFlattenedNodeInstances(document, true);
        List<RouteNodeInstance> activeNodeInstances = new ArrayList<RouteNodeInstance>();
        for (RouteNodeInstance nodeInstance : flattenedNodeInstances) {
            if (nodeInstance.isActive()) {
                activeNodeInstances.add(nodeInstance);
            }
        }
        return activeNodeInstances;
    }

    @Override
    public List<String> getCurrentRouteNodeNames(String documentId) {
       	return routeNodeDAO.getCurrentRouteNodeNames(documentId);
    }

    @Override
	public List<String> getActiveRouteNodeNames(String documentId) {
    	return routeNodeDAO.getActiveRouteNodeNames(documentId);
    }
    
    public List<RouteNodeInstance> getTerminalNodeInstances(String documentId) {
        return routeNodeDAO.getTerminalNodeInstances(documentId);
    }
    
    @Override
	public List<String> getTerminalRouteNodeNames(String documentId) {
    	return routeNodeDAO.getTerminalRouteNodeNames(documentId);
    }

    public List getInitialNodeInstances(String documentId) {
    	return routeNodeDAO.getInitialNodeInstances(documentId);
    }
    
    public NodeState findNodeState(Long nodeInstanceId, String key) {
        return routeNodeDAO.findNodeState(nodeInstanceId, key);
    }
    
    public RouteNode findRouteNodeByName(String documentTypeId, String name) {
        return routeNodeDAO.findRouteNodeByName(documentTypeId, name);
    }
    
    public List<RouteNode> findFinalApprovalRouteNodes(String documentTypeId) {
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findById(documentTypeId);
        documentType = documentType.getRouteDefiningDocumentType();
        return routeNodeDAO.findFinalApprovalRouteNodes(documentType.getDocumentTypeId());
    }
    
    public List findNextRouteNodesInPath(RouteNodeInstance nodeInstance, String nodeName) {
        List<RouteNode> nodesInPath = new ArrayList<RouteNode>();
        for (Iterator<RouteNode> iterator = nodeInstance.getRouteNode().getNextNodes().iterator(); iterator.hasNext();) {
            RouteNode nextNode = iterator.next();
            nodesInPath.addAll(findNextRouteNodesInPath(nodeName, nextNode, new HashSet<String>()));
        }
        return nodesInPath;
    }
    
    private List<RouteNode> findNextRouteNodesInPath(String nodeName, RouteNode node, Set<String> inspected) {
        List<RouteNode> nextNodesInPath = new ArrayList<RouteNode>();
        if (inspected.contains(node.getRouteNodeId())) {
            return nextNodesInPath;
        }
        inspected.add(node.getRouteNodeId());
        if (node.getRouteNodeName().equals(nodeName)) {
            nextNodesInPath.add(node);
        } else {
            if (helper.isSubProcessNode(node)) {
                ProcessDefinitionBo subProcess = node.getDocumentType().getNamedProcess(node.getRouteNodeName());
                RouteNode subNode = subProcess.getInitialRouteNode();
                if (subNode != null) {
                    nextNodesInPath.addAll(findNextRouteNodesInPath(nodeName, subNode, inspected));
                }
            }
            for (Iterator<RouteNode> iterator = node.getNextNodes().iterator(); iterator.hasNext();) {
                RouteNode nextNode = iterator.next();
                nextNodesInPath.addAll(findNextRouteNodesInPath(nodeName, nextNode, inspected));
            }
        }
        return nextNodesInPath;
    }
    
    public boolean isNodeInPath(DocumentRouteHeaderValue document, String nodeName) {
        boolean isInPath = false;
        Collection<RouteNodeInstance> activeNodes = getActiveNodeInstances(document.getDocumentId());
        for (Iterator<RouteNodeInstance> iterator = activeNodes.iterator(); iterator.hasNext();) {
            RouteNodeInstance nodeInstance = iterator.next();
            List nextNodesInPath = findNextRouteNodesInPath(nodeInstance, nodeName);
            isInPath = isInPath || !nextNodesInPath.isEmpty();
        }
        return isInPath;
    }
    
    public List findRouteNodeInstances(String documentId) {
        return this.routeNodeDAO.findRouteNodeInstances(documentId);
    }
    
	public void setRouteNodeDAO(RouteNodeDAO dao) {
		this.routeNodeDAO = dao;
	}
    
    public List findProcessNodeInstances(RouteNodeInstance process) {
       return this.routeNodeDAO.findProcessNodeInstances(process);
    }
    
    public List<String> findPreviousNodeNames(String documentId) {
        DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
        List<String> revokedIds = Collections.emptyList();

        String revoked = document.getRootBranch().getBranchState(REVOKED_NODE_INSTANCES_STATE_KEY) == null ? null : document.getRootBranch().getBranchState(REVOKED_NODE_INSTANCES_STATE_KEY).getValue();
        if (revoked != null) {
            revokedIds = Arrays.asList(revoked.split(","));
        }
        List <RouteNodeInstance> currentNodeInstances = KEWServiceLocator.getRouteNodeService().getCurrentNodeInstances(documentId);
        List<RouteNodeInstance> nodeInstances = new ArrayList<RouteNodeInstance>();
        for (RouteNodeInstance nodeInstance : currentNodeInstances) {
            nodeInstances.addAll(nodeInstance.getPreviousNodeInstances());
        }
        List<String> nodeNames = new ArrayList<String>();
        while (!nodeInstances.isEmpty()) {
            RouteNodeInstance nodeInstance = nodeInstances.remove(0);
            if (!revokedIds.contains(nodeInstance.getRouteNodeInstanceId())) {
                nodeNames.add(nodeInstance.getName());
            }
            nodeInstances.addAll(nodeInstance.getPreviousNodeInstances());
        }

        //reverse the order, because it was built last to first
        Collections.reverse(nodeNames);

        return nodeNames;
    }
    
    public List<String> findFutureNodeNames(String documentId) {
        List currentNodeInstances = KEWServiceLocator.getRouteNodeService().getCurrentNodeInstances(documentId);
        List<RouteNode> nodes = new ArrayList<RouteNode>();
        for (Iterator iterator = currentNodeInstances.iterator(); iterator.hasNext();) {
            RouteNodeInstance nodeInstance = (RouteNodeInstance) iterator.next();
            nodes.addAll(nodeInstance.getRouteNode().getNextNodes());
        }
        List<String> nodeNames = new ArrayList<String>();
        while (!nodes.isEmpty()) {
            RouteNode node = nodes.remove(0);
            if (!nodeNames.contains(node.getRouteNodeName())) {
        	nodeNames.add(node.getRouteNodeName());
            }
            nodes.addAll(node.getNextNodes());
        }
        return nodeNames;
    }
    
    public List<RouteNode> getFlattenedNodes(DocumentType documentType, boolean climbHierarchy) {
        List<RouteNode> nodes = new ArrayList<RouteNode>();
        if (!documentType.isRouteInherited() || climbHierarchy) {
            for (Iterator iterator = documentType.getProcesses().iterator(); iterator.hasNext();) {
                ProcessDefinitionBo process = (ProcessDefinitionBo) iterator.next();
                nodes.addAll(getFlattenedNodes(process));
            }
        }
        Collections.sort(nodes, new RouteNodeSorter());
        return nodes;
    }
    
    public List<RouteNode> getFlattenedNodes(ProcessDefinitionBo process) {
        Map<String, RouteNode> nodesMap = new HashMap<String, RouteNode>();
        if (process.getInitialRouteNode() != null) {
            flattenNodeGraph(nodesMap, process.getInitialRouteNode());
            List<RouteNode> nodes = new ArrayList<RouteNode>(nodesMap.values());
            Collections.sort(nodes, new RouteNodeSorter());
            return nodes;
        } else {
            List<RouteNode> nodes = new ArrayList<RouteNode>();
            nodes.add(new RouteNode());
            return nodes;
        }

    }
    
    /**
     * Recursively walks the node graph and builds up the map.  Uses a map because we will
     * end up walking through duplicates, as is the case with Join nodes.
     */
    private void flattenNodeGraph(Map<String, RouteNode> nodes, RouteNode node) {
        if (node != null) {
            if (nodes.containsKey(node.getRouteNodeName())) {
                return;
            }
            nodes.put(node.getRouteNodeName(), node);
            for (Iterator<RouteNode> iterator = node.getNextNodes().iterator(); iterator.hasNext();) {
                RouteNode nextNode = iterator.next();
                flattenNodeGraph(nodes, nextNode);
            }
        } else {
            return;
        }
    }        
    
    public List<RouteNodeInstance> getFlattenedNodeInstances(DocumentRouteHeaderValue document, boolean includeProcesses) {
        List<RouteNodeInstance> nodeInstances = new ArrayList<RouteNodeInstance>();
        Set<String> visitedNodeInstanceIds = new HashSet<String>();
        for (Iterator<RouteNodeInstance> iterator = document.getInitialRouteNodeInstances().iterator(); iterator.hasNext();) {
            RouteNodeInstance initialNodeInstance = iterator.next();
            flattenNodeInstanceGraph(nodeInstances, visitedNodeInstanceIds, initialNodeInstance, includeProcesses);    
        }
        return nodeInstances;
    }
    
	private void flattenNodeInstanceGraph(List<RouteNodeInstance> nodeInstances, Set<String> visitedNodeInstanceIds, RouteNodeInstance nodeInstance, boolean includeProcesses) {

		if (nodeInstance != null) {
			if (visitedNodeInstanceIds.contains(nodeInstance.getRouteNodeInstanceId())) {
				return;
			}
			if (includeProcesses && nodeInstance.getProcess() != null) {
				flattenNodeInstanceGraph(nodeInstances, visitedNodeInstanceIds, nodeInstance.getProcess(), includeProcesses);
			}
			visitedNodeInstanceIds.add(nodeInstance.getRouteNodeInstanceId());
			nodeInstances.add(nodeInstance);
			for (Iterator<RouteNodeInstance> iterator = nodeInstance.getNextNodeInstances().iterator(); iterator.hasNext();) {
				RouteNodeInstance nextNodeInstance = iterator.next();
				flattenNodeInstanceGraph(nodeInstances, visitedNodeInstanceIds, nextNodeInstance, includeProcesses);
			}

		}

    }      
    
    public NodeGraphSearchResult searchNodeGraph(NodeGraphSearchCriteria criteria) {
    	NodeGraphContext context = new NodeGraphContext();
    	if (criteria.getSearchDirection() == NodeGraphSearchCriteria.SEARCH_DIRECTION_BACKWARD) {
        	searchNodeGraphBackward(context, criteria.getMatcher(), null, criteria.getStartingNodeInstances());
    	} else {
    		throw new UnsupportedOperationException("Search feature can only search backward currently.");
    	}
    	List exactPath = determineExactPath(context, criteria.getSearchDirection(), criteria.getStartingNodeInstances());
        return new NodeGraphSearchResult(context.getCurrentNodeInstance(), exactPath);
    }
    
    private void searchNodeGraphBackward(NodeGraphContext context, NodeMatcher matcher, RouteNodeInstance previousNodeInstance, Collection nodeInstances) {
        if (nodeInstances == null) {
            return;
        }
    	for (Iterator iterator = nodeInstances.iterator(); iterator.hasNext();) {
            RouteNodeInstance nodeInstance = (RouteNodeInstance) iterator.next();
            context.setPreviousNodeInstance(previousNodeInstance);
            context.setCurrentNodeInstance(nodeInstance);
            searchNodeGraphBackward(context, matcher);
            if (context.getResultNodeInstance() != null) {
            	// we've located the node instance we're searching for, we're done
            	break;
            }
        }
    }
    
    private void searchNodeGraphBackward(NodeGraphContext context, NodeMatcher matcher) {
        RouteNodeInstance current = context.getCurrentNodeInstance();
        int numBranches = current.getNextNodeInstances().size();
        // if this is a split node, we want to wait here, until all branches join back to us
        if (numBranches > 1) {
        	// determine the number of branches that have joined back to the split thus far
            Integer joinCount = (Integer)context.getSplitState().get(current.getRouteNodeInstanceId());
            if (joinCount == null) {
                joinCount = new Integer(0);
            }
            // if this split is not a leaf node we increment the count
            if (context.getPreviousNodeInstance() != null) {
                joinCount = new Integer(joinCount.intValue()+1);
            }
            context.getSplitState().put(current.getRouteNodeInstanceId(), joinCount);
            // if not all branches have joined, stop and wait for other branches to join
            if (joinCount.intValue() != numBranches) {
                return;
            }
        }
        if (matcher.isMatch(context)) {
            context.setResultNodeInstance(current);
        } else {
            context.getVisited().put(current.getRouteNodeInstanceId(), current);
            searchNodeGraphBackward(context, matcher, current, current.getPreviousNodeInstances());
        }
    }
    
    public List<RouteNodeInstance> getActiveNodeInstances(DocumentRouteHeaderValue document, String nodeName) {
		Collection<RouteNodeInstance> activeNodes = getActiveNodeInstances(document.getDocumentId());
		List<RouteNodeInstance> foundNodes = new ArrayList<RouteNodeInstance>();
        for (Iterator<RouteNodeInstance> iterator = activeNodes.iterator(); iterator.hasNext();) {
            RouteNodeInstance nodeInstance = iterator.next();
            if (nodeInstance.getName().equals(nodeName)) {
            	foundNodes.add(nodeInstance);
            }
        }
        return foundNodes;
    }
    
    private List determineExactPath(NodeGraphContext context, int searchDirection, Collection<RouteNodeInstance> startingNodeInstances) {
    	List<RouteNodeInstance> exactPath = new ArrayList<RouteNodeInstance>();
    	if (context.getResultNodeInstance() == null) {
    		exactPath.addAll(context.getVisited().values());
    	} else {
    		determineExactPath(exactPath, new HashMap<String, RouteNodeInstance>(), startingNodeInstances, context.getResultNodeInstance());
    	}
    	if (NodeGraphSearchCriteria.SEARCH_DIRECTION_FORWARD == searchDirection) {
    		Collections.sort(exactPath, NODE_INSTANCE_BACKWARD_SORT);
    	} else {
    		Collections.sort(exactPath, NODE_INSTANCE_FORWARD_SORT);
    	}
    	return exactPath;
    }
    
    private void determineExactPath(List<RouteNodeInstance> exactPath, Map<String, RouteNodeInstance> visited, Collection<RouteNodeInstance> startingNodeInstances, RouteNodeInstance nodeInstance) {
    	if (nodeInstance == null) {
    		return;
    	}
    	if (visited.containsKey(nodeInstance.getRouteNodeInstanceId())) {
    		return;
    	}
    	visited.put(nodeInstance.getRouteNodeInstanceId(), nodeInstance);
    	exactPath.add(nodeInstance);
    	for (RouteNodeInstance startingNode : startingNodeInstances) {
			if (startingNode.getRouteNodeInstanceId().equals(nodeInstance.getRouteNodeInstanceId())) {
				return;
			}
		}
    	for (Iterator<RouteNodeInstance> iterator = nodeInstance.getNextNodeInstances().iterator(); iterator.hasNext(); ) {
			RouteNodeInstance nextNodeInstance = iterator.next();
			determineExactPath(exactPath, visited, startingNodeInstances, nextNodeInstance);
		}
    }
    
       
    /**
     * Sorts by RouteNodeId or the order the nodes will be evaluated in *roughly*.  This is 
     * for display purposes when rendering a flattened list of nodes.
     * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    private static class RouteNodeSorter implements Comparator {
        public int compare(Object arg0, Object arg1) {
            RouteNode rn1 = (RouteNode)arg0;
            RouteNode rn2 = (RouteNode)arg1;
            return rn1.getRouteNodeId().compareTo(rn2.getRouteNodeId());
        }
    }
    
    private static class NodeInstanceIdSorter implements Comparator {
        public int compare(Object arg0, Object arg1) {
            RouteNodeInstance nodeInstance1 = (RouteNodeInstance)arg0;
            RouteNodeInstance nodeInstance2 = (RouteNodeInstance)arg1;
            return nodeInstance1.getRouteNodeInstanceId().compareTo(nodeInstance2.getRouteNodeInstanceId());
        }
    }
    
    
    public void deleteByRouteNodeInstance(RouteNodeInstance routeNodeInstance){
    	//update the route node instance link table to cancel the relationship between the to-be-deleted instance and the previous node instances
    	routeNodeDAO.deleteLinksToPreNodeInstances(routeNodeInstance);
    	//delete the routeNodeInstance and its next node instances
    	routeNodeDAO.deleteRouteNodeInstancesHereAfter(routeNodeInstance);
    }
    
    public void deleteNodeStateById(Long nodeStateId){
    	routeNodeDAO.deleteNodeStateById(nodeStateId);
    }
    
    public void deleteNodeStates(List statesToBeDeleted){
    	routeNodeDAO.deleteNodeStates(statesToBeDeleted);
    }
    
    /**
     * Records the revocation in the root BranchState of the document.
     */
    public void revokeNodeInstance(DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance) {
    	if (document == null) {
    		throw new IllegalArgumentException("Document must not be null.");
    	}
		if (nodeInstance == null || nodeInstance.getRouteNodeInstanceId() == null) {
			throw new IllegalArgumentException("In order to revoke a final approval node the node instance must be persisent and have an id.");
		}
		// get the initial node instance, the root branch is where we will store the state
    	Branch rootBranch = document.getRootBranch();
    	BranchState state = null;
    	if (rootBranch != null) {
    	    state = rootBranch.getBranchState(REVOKED_NODE_INSTANCES_STATE_KEY);
    	}
    	if (state == null) {
    		state = new BranchState();
    		state.setKey(REVOKED_NODE_INSTANCES_STATE_KEY);
    		state.setValue("");
    		rootBranch.addBranchState(state);
    	}
    	if (state.getValue() == null) {
    		state.setValue("");
    	}
    	state.setValue(state.getValue() + nodeInstance.getRouteNodeInstanceId() + ",");
    	save(rootBranch);
	}

    /**
     * Queries the list of revoked node instances from the root BranchState of the Document
     * and returns a List of revoked RouteNodeInstances.
     */
	public List getRevokedNodeInstances(DocumentRouteHeaderValue document) {
		if (document == null) {
    		throw new IllegalArgumentException("Document must not be null.");
    	}
		List<RouteNodeInstance> revokedNodeInstances = new ArrayList<RouteNodeInstance>();
    	
    	Branch rootBranch = document.getRootBranch();
    	BranchState state = null;
    	if (rootBranch != null) {
    	    state = rootBranch.getBranchState(REVOKED_NODE_INSTANCES_STATE_KEY);
    	}
    	if (state == null || org.apache.commons.lang.StringUtils.isEmpty(state.getValue())) {
    		return revokedNodeInstances;
    	}
    	String[] revokedNodes = state.getValue().split(",");
    	for (int index = 0; index < revokedNodes.length; index++) {
			String revokedNodeInstanceId = revokedNodes[index];
			RouteNodeInstance revokedNodeInstance = findRouteNodeInstanceById(revokedNodeInstanceId);
			if (revokedNodeInstance == null) {
				LOG.warn("Could not locate revoked RouteNodeInstance with the given id: " + revokedNodeInstanceId);
			} else {
				revokedNodeInstances.add(revokedNodeInstance);
			}
		}
    	return revokedNodeInstances;
	}
    
    
}
