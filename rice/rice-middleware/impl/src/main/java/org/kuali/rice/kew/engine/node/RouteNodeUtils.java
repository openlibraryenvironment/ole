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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple class for performing operations on RouteNode.  In particular, this class provides some
 * convenience methods for processing custom RouteNode XML content fragments. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class RouteNodeUtils {
	
	private RouteNodeUtils() {
		throw new UnsupportedOperationException("do not call");
	}

	/**
	 * Searches a RouteNode's "contentFragment" (it's XML definition) for an XML element with
	 * the given name and returns it's value.
	 * 
	 * <p>For example, in a node with the following definition:
	 *
	 * <pre><routeNode name="...">
	 *   ...
	 *   <myCustomProperty>propertyValue</myCustomProperty>
	 * </routeNode></pre>
	 * 
	 * <p>An invocation of getValueOfCustomProperty(routeNode, "myCustomProperty") would return
	 * "propertyValue".
	 * 
	 * @param routeNode RouteNode to examine
	 * @param propertyName name of the XML element to search for
	 * 
	 * @return the value of the XML element, or null if it could not be located
	 */
	public static String getValueOfCustomProperty(RouteNode routeNode, String propertyName) {
		String contentFragment = routeNode.getContentFragment();
		String elementValue = null;
		if (!StringUtils.isBlank(contentFragment)) {
			try {
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document document = db.parse(new InputSource(new StringReader(contentFragment)));	
				elementValue = XPathHelper.newXPath().evaluate("//" + propertyName, document);
			} catch (Exception e) {
				throw new RiceRuntimeException("Error when attempting to parse Route Node content fragment for property name: " + propertyName, e);
			}
		}
		return elementValue;
	}

    public static List<Element> getCustomRouteNodeElements(RouteNode routeNode, String elementName) {
        String contentFragment = routeNode.getContentFragment();
        List<Element> elements = new ArrayList<Element>();
		NodeList nodeList = null;
		if (!StringUtils.isBlank(contentFragment)) {
			try {
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document document = db.parse(new InputSource(new StringReader(contentFragment)));
				nodeList = (NodeList)XPathHelper.newXPath().evaluate("//" + elementName, document, XPathConstants.NODESET);
			} catch (Exception e) {
				throw new RiceRuntimeException("Error when attempting to parse Route Node content fragment for element name: " + elementName, e);
			}
		}
        for (int index = 0; index < nodeList.getLength(); index++) {
            Element element = (Element)nodeList.item(index);
            elements.add(element);
        }
		return elements;
    }

    public static Element getCustomRouteNodeElement(RouteNode routeNode, String elementName) {
        List<Element> elements = getCustomRouteNodeElements(routeNode, elementName);
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        } else if (elements.size() > 1) {
            throw new RiceRuntimeException("More than one element found with the given name: " + elementName);
        }
        return elements.get(0);
    }
	
	public static List<RouteNodeInstance> getFlattenedNodeInstances(DocumentRouteHeaderValue document, boolean includeProcesses) {
        List<RouteNodeInstance> nodeInstances = new ArrayList<RouteNodeInstance>();
        Set<String> visitedNodeInstanceIds = new HashSet<String>();
        for (RouteNodeInstance initialNodeInstance : document.getInitialRouteNodeInstances())
        {
            flattenNodeInstanceGraph(nodeInstances, visitedNodeInstanceIds, initialNodeInstance, includeProcesses);
        }
        return nodeInstances;
    }
    
    private static void flattenNodeInstanceGraph(List<RouteNodeInstance> nodeInstances, Set<String> visitedNodeInstanceIds, RouteNodeInstance nodeInstance, boolean includeProcesses) {
        if (visitedNodeInstanceIds.contains(nodeInstance.getRouteNodeInstanceId())) {
            return;
        }
        if (includeProcesses && nodeInstance.getProcess() != null) {
            flattenNodeInstanceGraph(nodeInstances, visitedNodeInstanceIds, nodeInstance.getProcess(), includeProcesses);
        }
        visitedNodeInstanceIds.add(nodeInstance.getRouteNodeInstanceId());
        nodeInstances.add(nodeInstance);
        for (RouteNodeInstance nextNodeInstance : nodeInstance.getNextNodeInstances())
        {
            flattenNodeInstanceGraph(nodeInstances, visitedNodeInstanceIds, nextNodeInstance, includeProcesses);
        }
    }
    
    public static List<RouteNode> getFlattenedNodes(DocumentType documentType, boolean climbHierarchy) {
        List<RouteNode> nodes = new ArrayList<RouteNode>();
        if (!documentType.isRouteInherited() || climbHierarchy) {
            for (Object o : documentType.getProcesses())
            {
                ProcessDefinitionBo process = (ProcessDefinitionBo) o;
                nodes.addAll(getFlattenedNodes(process));
            }
        }
        Collections.sort(nodes, new RouteNodeSorter());
        return nodes;
    }
    
    public static List<RouteNode> getFlattenedNodes(ProcessDefinitionBo process) {
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
     * @param nodes map
     * @param node graph
     */
    private static void flattenNodeGraph(Map<String, RouteNode> nodes, RouteNode node) {
        if (node != null) {
            if (nodes.containsKey(node.getRouteNodeName())) {
                return;
            }
            nodes.put(node.getRouteNodeName(), node);
            for (RouteNode nextNode : node.getNextNodes())
            {
                flattenNodeGraph(nodes, nextNode);
            }
        } else {
            return;
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
    
    public static List<RouteNodeInstance> getActiveNodeInstances(DocumentRouteHeaderValue document) {
        List<RouteNodeInstance> flattenedNodeInstances = getFlattenedNodeInstances(document, true);
        List<RouteNodeInstance> activeNodeInstances = new ArrayList<RouteNodeInstance>();
        for (RouteNodeInstance nodeInstance : flattenedNodeInstances)
        {
            if (nodeInstance.isActive())
            {
                activeNodeInstances.add(nodeInstance);
            }
        }
        return activeNodeInstances;
    }
    
    public static RouteNodeInstance findRouteNodeInstanceById(String nodeInstanceId, DocumentRouteHeaderValue document) {
    	List<RouteNodeInstance> flattenedNodeInstances = getFlattenedNodeInstances(document, true);
    	RouteNodeInstance niRet = null;
        for (RouteNodeInstance nodeInstance : flattenedNodeInstances)
        {
            if (nodeInstanceId.equals(nodeInstance.getRouteNodeInstanceId()))
            {
                niRet = nodeInstance;
                break;
            }
        }
        return niRet;
    }
    
    
	
}
