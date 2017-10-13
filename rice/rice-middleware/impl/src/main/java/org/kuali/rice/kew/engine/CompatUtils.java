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

import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.api.KewApiConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * Provides utility methods for handling backwards compatibility between KEW releases.
 * Currently, it's primary function is to handle backward compatability between the
 * deprecated "route level" concept and the "node" concept which was introduced in
 * KEW 2.1.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class CompatUtils {
	
    private static RouteHelper helper = new RouteHelper();
    
	private CompatUtils() {
		throw new UnsupportedOperationException("do not call");
	}
    
    public static Integer getLevelForNode(DocumentType documentType, String nodeName) {
        if (isRouteLevelCompatible(documentType)) {
            return getLevelForNode(documentType.getPrimaryProcess().getInitialRouteNode(), nodeName, 0);
        }
        return new Integer(KewApiConstants.INVALID_ROUTE_LEVEL);
    }
    
    private static Integer getLevelForNode(RouteNode node, String nodeName, Integer currentLevel) {
        if (node == null) {
            throw new WorkflowRuntimeException("Could not locate node with name '"+nodeName+"'");
        }
        // TODO potential for infinite recursion here if their document type has loops in it.  Should this be a concern?
        // If their routing version is really "route level" then there should be no cycles.
        if (node.getRouteNodeName().equals(nodeName)) {
            return currentLevel;
        }
        List<RouteNode> nextNodes = node.getNextNodes();
        if (nextNodes.isEmpty()) {
            throw new WorkflowRuntimeException("Could not locate node with name '"+nodeName+"'");
        }
        if (nextNodes.size() > 1) {
            throw new WorkflowRuntimeException("Can only determine route level for document types with no splitting");
        }
        RouteNode nextNode = (RouteNode)nextNodes.get(0);
        return getLevelForNode(nextNode, nodeName, new Integer(currentLevel.intValue()+1));
    }
    
    /**
     * Returns the RouteNode at the given numerical route level for the given document type.
     * This currently throws a WorkflowException if the document has parallel routing structures
     * because the route level as a number becomes arbitrary in that case. 
     */
    public static RouteNode getNodeForLevel(DocumentType documentType, Integer routeLevel) {
        RouteNode result = null;
        
        RouteNode initialRouteNode = documentType.getPrimaryProcess().getInitialRouteNode();
        if (initialRouteNode != null) {
            Object[] node = getNodeForLevel(initialRouteNode, routeLevel, new Integer(0));
            result = (RouteNode)node[0];
        }
        return result;
    }
    
    private static Object[] getNodeForLevel(RouteNode node, Integer routeLevel, Integer currentLevel) {
        if (helper.isSubProcessNode(node)) {
            Object[] result = getNodeForLevel(node.getDocumentType().getNamedProcess(node.getRouteNodeName()).getInitialRouteNode(), routeLevel, currentLevel);
            if (result[0] != null) {
                node = (RouteNode)result[0];
            }
            currentLevel = (Integer)result[1];
        }
        if (currentLevel.equals(routeLevel)) {
            return new Object[] { node, currentLevel };
        }
        List<RouteNode> nextNodes = node.getNextNodes();
        if (nextNodes.isEmpty()) {
            return new Object[] { null, currentLevel };
        }
        if (nextNodes.size() > 1) {
            throw new WorkflowRuntimeException("Cannot determine a route level number for documents with splitting.");
        }
        currentLevel = new Integer(currentLevel.intValue()+1);
        return getNodeForLevel((RouteNode)nextNodes.get(0), routeLevel, currentLevel);
    }

    public static boolean isRouteLevelCompatible(DocumentType documentType) {
        return KewApiConstants.ROUTING_VERSION_ROUTE_LEVEL.equals(documentType.getRoutingVersion());
    }
    
    public static boolean isRouteLevelCompatible(DocumentRouteHeaderValue document) {
        return isRouteLevelCompatible(document.getDocumentType());
    }
    
    public static boolean isNodalDocument(DocumentRouteHeaderValue document) {
        return KewApiConstants.DocumentContentVersions.NODAL == document.getDocVersion().intValue();
    }
    
    public static boolean isNodalRequest(ActionRequestValue request) {
        return KewApiConstants.DocumentContentVersions.NODAL == request.getDocVersion().intValue();
    }
    
    public static boolean isRouteLevelDocument(DocumentRouteHeaderValue document) {
        return KewApiConstants.DocumentContentVersions.ROUTE_LEVEL == document.getDocVersion().intValue();
    }
    
    public static boolean isRouteLevelRequest(ActionRequestValue request) {
        return KewApiConstants.DocumentContentVersions.ROUTE_LEVEL == request.getDocVersion().intValue();
    }
    
    /**
     * Returns a list of RouteNodes in a flat list which is equivalent to the route level concept of
     * Workflow <= version 2.0.  If the document type is not route level compatible, then this method will throw an error.
     */
    public static List<RouteNode> getRouteLevelCompatibleNodeList(DocumentType documentType) {
        if (!isRouteLevelCompatible(documentType)) {
            throw new WorkflowRuntimeException("Attempting to invoke a 'route level' operation on a document which is not route level compatible.");
        }
        ProcessDefinitionBo primaryProcess = documentType.getPrimaryProcess();
        RouteNode routeNode = primaryProcess.getInitialRouteNode();
        List<RouteNode> nodes = new ArrayList<RouteNode>();
        int count = 0;
        int maxCount = 100;
        if (routeNode != null) {
            while (true) {
                nodes.add(routeNode);
                List<RouteNode> nextNodes = routeNode.getNextNodes();
                if (nextNodes.size() == 0) {
                    break;
                }
                if (nextNodes.size() > 1) {
                    throw new RuntimeException("Node has more than one next node!  It is not route level compatible!" + routeNode.getRouteNodeName());
                }
                if (count >= maxCount) {
                    throw new RuntimeException("A runaway loop was detected when attempting to create route level compatible node graph.  documentType=" + documentType.getDocumentTypeId()+","+documentType.getName());
                }
                routeNode = nextNodes.iterator().next();
            }
        }
        return nodes;
    }
    
    public static int getMaxRouteLevel(DocumentType documentType) {
        return getRouteLevelCompatibleNodeList(documentType).size();
    }
}
