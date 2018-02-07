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
package org.kuali.rice.kew.engine.node.hierarchyrouting;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.engine.node.DynamicNode;
import org.kuali.rice.kew.engine.node.DynamicResult;
import org.kuali.rice.kew.engine.node.NoOpNode;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RequestsNode;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.engine.node.SimpleJoinNode;
import org.kuali.rice.kew.engine.node.SimpleSplitNode;
import org.kuali.rice.kew.engine.node.hierarchyrouting.HierarchyProvider.Stop;
import org.kuali.rice.kew.engine.transition.SplitTransitionEngine;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.Utilities;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Generic hierarchy routing node
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class HierarchyRoutingNode implements DynamicNode {
    protected final Logger LOG = Logger.getLogger(getClass());

    /**
     * The RouteNode configuration parameter that specifies the hierarchy provider implementation
     */
    public static final String HIERARCHY_PROVIDER = "hierarchyProvider";
    /**
     * RouteNodeInstance NodeState key for id of stop
     */
    public static final String STOP_ID = "stop_id";

    protected static final String SPLIT_PROCESS_NAME = "Hierarchy Split";
    protected static final String JOIN_PROCESS_NAME = "Hierarchy Join";
    protected static final String REQUEST_PROCESS_NAME = "Hierarchy Request";
    protected static final String NO_STOP_NAME = "No stop";

    // constants for the process state in tracking stops we've traveled
    private static final String VISITED_STOPS = "visited_stops";
    private static final String V_STOPS_DEL = ",";
    
    private static final String INITIAL_SPLIT_NODE_MARKER = "InitialSplitNode";

    /**
     * Loads hierarchy provider class via the GlobalResourceLoader
     * @param nodeInstance the current RouteNodeInstance
     * @param context the current RouteContext
     * @return the HierarchyProvider implementation, as specified by the HIERARCHY_PROVIDER config parameter
     */
    protected HierarchyProvider getHierarchyProvider(RouteNodeInstance nodeInstance, RouteContext context) {
        Map<String, String> cfgMap = Utilities.getKeyValueCollectionAsMap(nodeInstance.getRouteNode().getConfigParams());
        String hierarchyProviderClass = cfgMap.get(HIERARCHY_PROVIDER); 
        if (StringUtils.isEmpty(hierarchyProviderClass)) {
            throw new WorkflowRuntimeException("hierarchyProvider configuration parameter not set for HierarchyRoutingNode: " + nodeInstance.getName());
        }
        QName qn = QName.valueOf(hierarchyProviderClass);
        ObjectDefinition od;
        if (XMLConstants.NULL_NS_URI.equals(qn.getNamespaceURI())) {
            od = new ObjectDefinition(qn.getLocalPart());
        } else {
            od = new ObjectDefinition(qn.getLocalPart(), qn.getNamespaceURI());
        }
        HierarchyProvider hp = (HierarchyProvider) GlobalResourceLoader.getObject(od);
        hp.init(nodeInstance, context);
        return hp;
    }

    public DynamicResult transitioningInto(RouteContext context, RouteNodeInstance dynamicNodeInstance, RouteHelper helper) throws Exception {

        HierarchyProvider provider = getHierarchyProvider(dynamicNodeInstance, context);
        DocumentType documentType = setUpDocumentType(provider, context.getDocument().getDocumentType(), dynamicNodeInstance);
        RouteNode splitNode = documentType.getNamedProcess(SPLIT_PROCESS_NAME).getInitialRouteNode();

        //set up initial SplitNodeInstance
        RouteNodeInstance splitNodeInstance = helper.getNodeFactory().createRouteNodeInstance(context.getDocument().getDocumentId(), splitNode);
        splitNodeInstance.setBranch(dynamicNodeInstance.getBranch());
        markAsInitialSplitNode(splitNodeInstance);
        
        int i = 0;
        List<Stop> stops = provider.getLeafStops(context);
        if (stops.isEmpty()) {
            // if we have no stops, then just return a no-op node with IU-UNIV attached, this will terminate the process
            RouteNode noStopNode = documentType.getNamedProcess(NO_STOP_NAME).getInitialRouteNode();
            RouteNodeInstance noChartOrgInstance = helper.getNodeFactory().createRouteNodeInstance(context.getDocument().getDocumentId(), noStopNode);
            noChartOrgInstance.setBranch(dynamicNodeInstance.getBranch());

            provider.setStop(noChartOrgInstance, null);

            return new DynamicResult(true, noChartOrgInstance);
        }
        for (Stop stop: stops) {
            RouteNode requestNode = getStopRequestNode(stop, documentType);
            createInitialRequestNodeInstance(provider, stop, splitNodeInstance, dynamicNodeInstance, requestNode);
        }

        return new DynamicResult(false, splitNodeInstance);
    }

    public DynamicResult transitioningOutOf(RouteContext context, RouteHelper helper) throws Exception {
        // process initial nodes govern transitioning within the process
        // the process node will be the hierarchy node, so that's what we need to grab
        HierarchyProvider provider = getHierarchyProvider(context.getNodeInstance().getProcess(), context);
        
        RouteNodeInstance processInstance = context.getNodeInstance().getProcess();
        RouteNodeInstance curStopNode = context.getNodeInstance();
        Map<String, RouteNodeInstance> stopRequestNodeMap = new HashMap<String, RouteNodeInstance>();
        findStopRequestNodes(provider, context, stopRequestNodeMap);//SpringServiceLocator.getRouteNodeService().findProcessNodeInstances(processInstance);
        
        Stop stop = provider.getStop(curStopNode);

        if (provider.isRoot(stop)) {
            return new DynamicResult(true, null);
        }        
        
        //create a join node for the next node and attach any sibling orgs to the join
        //if no join node is necessary i.e. no siblings create a requests node
        InnerTransitionResult transition = canTransitionFrom(provider, stop, stopRequestNodeMap.values(), helper);
        DynamicResult result = null;
        if (transition.isCanTransition()) {
            DocumentType documentType = context.getDocument().getDocumentType();
            // make a simple requests node
            RouteNodeInstance requestNode = createNextStopRequestNodeInstance(provider, context, stop, processInstance, helper);

            if (transition.getSiblings().isEmpty()) {
                result = new DynamicResult(false, requestNode);
            } else {
                /* join stuff not working

                //create a join to transition us to the next org
                RouteNode joinPrototype = documentType.getNamedProcess(JOIN_PROCESS_NAME).getInitialRouteNode();
                RouteNodeInstance joinNode = helper.getNodeFactory().createRouteNodeInstance(context.getDocument().getDocumentId(), joinPrototype);
                LOG.debug("Created join node: " + joinNode);
                String branchName = "Branch for join " + provider.getStopIdentifier(stop);
                Branch joinBranch = helper.getNodeFactory().createBranch(branchName, null, joinNode);
                LOG.debug("Created branch for join node: " + joinBranch);
                joinNode.setBranch(joinBranch);

                for (RouteNodeInstance sibling: transition.getSiblings()) {
                    LOG.debug("Adding expected joiner: " + sibling.getRouteNodeInstanceId() + " " + provider.getStop(sibling));
                    helper.getJoinEngine().addExpectedJoiner(joinNode, sibling.getBranch());
                }

                ///XXX: can't get stop from node that hasn't been saved yet maybe...need to follow up on this...comes back as 'root'
                LOG.debug("Adding as stop after join: " + requestNode.getRouteNodeInstanceId() + " " + provider.getStop(requestNode));
                //set the next org after the join
                joinNode.addNextNodeInstance(requestNode);

                result = new DynamicResult(false, joinNode);
                
                */
            }

        } else {
            result = new DynamicResult(false, null);
        }
        result.getNextNodeInstances().addAll(getNewlyAddedOrgRouteInstances(provider, context, helper));
        return result;
    }
    
    private void findStopRequestNodes(HierarchyProvider provider, RouteContext context, Map<String, RouteNodeInstance> stopRequestNodes) {
        List<RouteNodeInstance> nodeInstances = KEWServiceLocator.getRouteNodeService().getFlattenedNodeInstances(context.getDocument(), true);
        for (RouteNodeInstance nodeInstance: nodeInstances) {
            if (provider.hasStop(nodeInstance)) {
                LOG.debug("Stop node instance: " + nodeInstance);
                stopRequestNodes.put(nodeInstance.getRouteNodeInstanceId(), nodeInstance);
            }
        }
        
    }

    private RouteNodeInstance createNextStopRequestNodeInstance(HierarchyProvider provider, RouteContext context, Stop stop, RouteNodeInstance processInstance, RouteHelper helper) {
        Stop futureStop = provider.getParent(stop);
        LOG.debug("Creating next stop request node instance " + provider.getStopIdentifier(futureStop) + " as parent of " + provider.getStopIdentifier(stop));
        RouteNode requestsPrototype = getStopRequestNode(futureStop, context.getDocument().getDocumentType());
        RouteNodeInstance requestNode = helper.getNodeFactory().createRouteNodeInstance(context.getDocument().getDocumentId(), requestsPrototype);
        requestNode.setBranch(processInstance.getBranch());
        NodeState ns = new NodeState();
        ns.setKey(STOP_ID);
        ns.setValue(provider.getStopIdentifier(futureStop));
        requestNode.addNodeState(ns);
        provider.setStop(requestNode, futureStop);
        LOG.debug("Stop set on request node: " + provider.getStop(requestNode));
        addStopToProcessState(provider, processInstance, futureStop);
        return requestNode;
    }

    /**
     * i can only transition from this if all the nodes left are completed immediate siblings
     * 
     * @param org
     * @param requestNodes
     * @return List of Nodes that are siblings to the org passed in
     */
    private InnerTransitionResult canTransitionFrom(HierarchyProvider provider, Stop currentStop, Collection<RouteNodeInstance> requestNodes, RouteHelper helper) {
        LOG.debug("Testing whether we can transition from stop: " + currentStop);
        Stop parent = provider.getParent(currentStop);
        InnerTransitionResult result = new InnerTransitionResult();
        result.setCanTransition(false);

        for (RouteNodeInstance requestNode: requestNodes) {
            if (!provider.hasStop(requestNode)) {
                LOG.debug("request node " + requestNode.getName() + " does not have a stop associated with it");
                continue;
            }

            Stop requestNodeStop = provider.getStop(requestNode);
            if (requestNodeStop != null) {
                LOG.debug("Request node: " + requestNode.getRouteNodeInstanceId() + " has stop " + requestNodeStop.toString());
            }
            if (requestNodeStop != null && provider.equals(currentStop, requestNodeStop)) {
                LOG.debug("Skipping node " + requestNode.getName() + " because it is associated with the current stop");
                continue;
            }


            Stop stop = provider.getStop(requestNode);

            LOG.debug("Found an outstanding stop: " + stop);

            boolean isChildOfMyParent = isDescendent(provider, parent, stop);

            if (isChildOfMyParent) {
                LOG.debug("Found stop node whose parent is my parent:");
                LOG.debug("Stop: " + stop);
                LOG.debug("Node: " + requestNode);

                // if any sibling request node is active, then I can't transition
                if (requestNode.isActive()) {
                    // can't transition
                    result.getSiblings().clear();
                    return result;
                }

                /* join stuff not working
                // if it's a direct sibling
                if (provider.equals(parent, provider.getParent(stop))) {
                    LOG.debug("Adding stop " + provider.getStopIdentifier(stop) + " as sibling to " + provider.getStopIdentifier(currentStop));
                    result.getSiblings().add(requestNode);
                }
                */
            }
        }
        result.setCanTransition(true);
        return result;
    }

    protected boolean isDescendent(HierarchyProvider provider, Stop parent, Stop otherStop) {
        return provider.isRoot(parent) || hasAsParent(provider, parent, otherStop);
    }

    private static class InnerTransitionResult {
        private boolean canTransition;
        private List<RouteNodeInstance> siblings = new ArrayList<RouteNodeInstance>();

        public boolean isCanTransition() {
            return canTransition;
        }

        public void setCanTransition(boolean canTransition) {
            this.canTransition = canTransition;
        }

        public List<RouteNodeInstance> getSiblings() {
            return siblings;
        }

        public void setSiblings(List<RouteNodeInstance> siblings) {
            this.siblings = siblings;
        }
    }

    private static void markAsInitialSplitNode(RouteNodeInstance splitNode) {
        NodeState ns = new NodeState();
        ns.setKey(INITIAL_SPLIT_NODE_MARKER);
        ns.setValue(INITIAL_SPLIT_NODE_MARKER);
    	
    	splitNode.addNodeState(ns);
    }

    /**
     * @param routeNodeInstance
     * @return
     */
    private static boolean isInitialSplitNode(RouteNodeInstance routeNodeInstance) {
        return routeNodeInstance.getNodeState(INITIAL_SPLIT_NODE_MARKER) != null;
    }

    /**
     * Adds the org to the process state 
     * @param processInstance
     * @param org
     */
    private void addStopToProcessState(HierarchyProvider provider, RouteNodeInstance processInstance, Stop stop) {
        String stopStateName = provider.getStopIdentifier(stop);
        NodeState visitedStopsState = processInstance.getNodeState(VISITED_STOPS);
        if (visitedStopsState == null) {
            NodeState ns = new NodeState();
            ns.setKey(VISITED_STOPS);
            ns.setValue(stopStateName + V_STOPS_DEL);
        	
        	processInstance.addNodeState(ns);
        } else if (! getVisitedStopsList(processInstance).contains(stopStateName)) {
            visitedStopsState.setValue(visitedStopsState.getValue() + stopStateName + V_STOPS_DEL);
        }
    }
    
    /**
     * @param process
     * @return List of stop strings on the process state
     */
    private static List<String> getVisitedStopsList(RouteNodeInstance process) {
        return Arrays.asList(process.getNodeState(VISITED_STOPS).getValue().split(V_STOPS_DEL));
    }
    
    /**
     * Determines if the org has been routed to or will be.
     * @param stop
     * @param process
     * @return boolean if this is an org we would not hit in routing
     */
    private boolean isNewStop(HierarchyProvider provider, Stop stop, RouteNodeInstance process) {
        
        String orgStateName = provider.getStopIdentifier(stop);
        List<String> visitedOrgs = getVisitedStopsList(process);
        boolean isInVisitedList = visitedOrgs.contains(orgStateName);
        if (isInVisitedList) {
            return false;
        }
        boolean willEventualRouteThere = false;
        //determine if we will eventually route to this chart anyway
        for (Iterator<String> iter = visitedOrgs.iterator(); iter.hasNext() && willEventualRouteThere == false; ) {
            String visitedStopStateName = iter.next();
            Stop visitedStop = provider.getStopByIdentifier(visitedStopStateName);
            willEventualRouteThere = hasAsParent(provider, stop, visitedStop) || willEventualRouteThere;
        }
        return ! willEventualRouteThere;
    }

    /**
     * Creates a Org Request RouteNodeInstance that is a child of the passed in split.  This is used to create the initial 
     * request RouteNodeInstances off the begining split.
     * @param org
     * @param splitNodeInstance
     * @param processInstance
     * @param requestsNode
     * @return Request RouteNodeInstance bound to the passed in split as a 'nextNodeInstance'
     */
    private RouteNodeInstance createInitialRequestNodeInstance(HierarchyProvider provider, Stop stop, RouteNodeInstance splitNodeInstance, RouteNodeInstance processInstance, RouteNode requestsNode) {
        String branchName = "Branch " + provider.getStopIdentifier(stop);
        RouteNodeInstance orgRequestInstance = SplitTransitionEngine.createSplitChild(branchName, requestsNode, splitNodeInstance);
        splitNodeInstance.addNextNodeInstance(orgRequestInstance);
        NodeState ns = new NodeState();
        ns.setKey(STOP_ID);
        ns.setValue(provider.getStopIdentifier(stop));
        
        orgRequestInstance.addNodeState(ns);
        provider.setStop(orgRequestInstance, stop);
        addStopToProcessState(provider, processInstance, stop);
        return orgRequestInstance;
    }
    
    /**
     * Check the xml and determine there are any orgs declared that we will not travel through on our current trajectory.
     * @param context
     * @param helper
     * @return RouteNodeInstances for any orgs we would not have traveled through that are now in the xml.
     * @throws Exception
     */
    private List<RouteNodeInstance> getNewlyAddedOrgRouteInstances(HierarchyProvider provider, RouteContext context, RouteHelper helper) throws Exception {
        RouteNodeInstance processInstance = context.getNodeInstance().getProcess();
        RouteNodeInstance chartOrgNode = context.getNodeInstance();
        //check for new stops in the xml
        List<Stop> stops = provider.getLeafStops(context);
        List<RouteNodeInstance> newStopsRoutingTo = new ArrayList<RouteNodeInstance>();
        for (Stop stop: stops) {
            if (isNewStop(provider, stop, processInstance)) {
                //the idea here is to always use the object referenced by the engine so simulation can occur
                List<RouteNodeInstance> processNodes = chartOrgNode.getPreviousNodeInstances();
                for (RouteNodeInstance splitNodeInstance: processNodes) {
                    if (isInitialSplitNode(splitNodeInstance)) {                        
                        RouteNode requestsNode = getStopRequestNode(stop, context.getDocument().getDocumentType());
                        RouteNodeInstance newOrgRequestNode = createInitialRequestNodeInstance(provider, stop, splitNodeInstance, processInstance, requestsNode);
                        newStopsRoutingTo.add(newOrgRequestNode);
                    }
                }
            }
        }
        return newStopsRoutingTo;
    }    
    
    /**
     * @param parent
     * @param child
     * @return true - if child or one of it's eventual parents reports to parent false - if child or one of it's eventual parents does not report to parent
     */
    private boolean hasAsParent(HierarchyProvider provider, Stop parent, Stop child) {
        if (child == null || provider.isRoot(child)) {
            return false;
        } else if (provider.equals(parent, child)) {
            return true;
        } else {
            child = provider.getParent(child);
            return hasAsParent(provider, parent, child);
        }
    }


    /**
     * Make the 'floating' split, join and request RouteNodes that will be independent processes. These are the prototypes from which our RouteNodeInstance will belong
     * 
     * @param documentType
     * @param dynamicNodeInstance
     */
    private DocumentType setUpDocumentType(HierarchyProvider provider, DocumentType documentType, RouteNodeInstance dynamicNodeInstance) {
        boolean altered = false;
        if (documentType.getNamedProcess(SPLIT_PROCESS_NAME) == null) {
            RouteNode splitNode = getSplitNode(dynamicNodeInstance);
            documentType.addProcess(getPrototypeProcess(splitNode, documentType));
            altered = true;
        }
        if (documentType.getNamedProcess(JOIN_PROCESS_NAME) == null) {
            RouteNode joinNode = getJoinNode(dynamicNodeInstance);
            documentType.addProcess(getPrototypeProcess(joinNode, documentType));
            altered = true;
        }
        if (documentType.getNamedProcess(REQUEST_PROCESS_NAME) == null) {
            RouteNode requestsNode = getRequestNode(provider, dynamicNodeInstance);
            documentType.addProcess(getPrototypeProcess(requestsNode, documentType));
            altered = true;
        }
        if (documentType.getNamedProcess(NO_STOP_NAME) == null) {
            RouteNode noChartOrgNode = getNoChartOrgNode(dynamicNodeInstance);
            documentType.addProcess(getPrototypeProcess(noChartOrgNode, documentType));
            altered = true;
        }
        if (altered) {
                //side step normal version etc. because it's a pain.
            KEWServiceLocator.getDocumentTypeService().save(documentType);
        }
        return KEWServiceLocator.getDocumentTypeService().findByName(documentType.getName());
    }

    /**
     * Places a ProcessDefinition on the documentType wrapping the node and setting the node as the process's initalRouteNode
     * 
     * @param node
     * @param documentType
     * @return Process wrapping the node passed in
     */
    protected ProcessDefinitionBo getPrototypeProcess(RouteNode node, DocumentType documentType) {
        ProcessDefinitionBo process = new ProcessDefinitionBo();
        process.setDocumentType(documentType);
        process.setInitial(false);
        process.setInitialRouteNode(node);
        process.setName(node.getRouteNodeName());
        return process;
    }

    /**
     * @param process
     * @return Route Node of the JoinNode that will be prototype for the split RouteNodeInstances generated by this component
     */
    private static RouteNode getSplitNode(RouteNodeInstance process) {
        RouteNode dynamicNode = process.getRouteNode();
        RouteNode splitNode = new RouteNode();
        splitNode.setActivationType(dynamicNode.getActivationType());
        splitNode.setDocumentType(dynamicNode.getDocumentType());
        splitNode.setFinalApprovalInd(dynamicNode.getFinalApprovalInd());
        splitNode.setExceptionWorkgroupId(dynamicNode.getExceptionWorkgroupId());
        splitNode.setMandatoryRouteInd(dynamicNode.getMandatoryRouteInd());
        splitNode.setNodeType(SimpleSplitNode.class.getName());
        splitNode.setRouteMethodCode("FR");
        splitNode.setRouteMethodName(null);
        splitNode.setRouteNodeName(SPLIT_PROCESS_NAME);
        return splitNode;
        //SubRequests
    }

    /**
     * @param process
     * @return Route Node of the JoinNode that will be prototype for the join RouteNodeInstances generated by this component
     */
    private static RouteNode getJoinNode(RouteNodeInstance process) {
        RouteNode dynamicNode = process.getRouteNode();
        RouteNode joinNode = new RouteNode();
        joinNode.setActivationType(dynamicNode.getActivationType());
        joinNode.setDocumentType(dynamicNode.getDocumentType());
        joinNode.setFinalApprovalInd(dynamicNode.getFinalApprovalInd());
        joinNode.setExceptionWorkgroupId(dynamicNode.getExceptionWorkgroupId());
        joinNode.setMandatoryRouteInd(dynamicNode.getMandatoryRouteInd());
        joinNode.setNodeType(SimpleJoinNode.class.getName());
        joinNode.setRouteMethodCode("FR");
        joinNode.setRouteMethodName(null);
        joinNode.setRouteNodeName(JOIN_PROCESS_NAME);
        return joinNode;
    }

    /**
     * @param process
     * @return RouteNode of RequestsNode that will be prototype for RouteNodeInstances having requets that are generated by this component
     */
    private RouteNode getRequestNode(HierarchyProvider provider, RouteNodeInstance process) {
        RouteNode dynamicNode = process.getRouteNode();
        RouteNode requestsNode = new RouteNode();
        requestsNode.setActivationType(dynamicNode.getActivationType());
        requestsNode.setDocumentType(dynamicNode.getDocumentType());
        requestsNode.setFinalApprovalInd(dynamicNode.getFinalApprovalInd());
        requestsNode.setExceptionWorkgroupId(dynamicNode.getExceptionWorkgroupId());
        requestsNode.setMandatoryRouteInd(dynamicNode.getMandatoryRouteInd());
        requestsNode.setNodeType(RequestsNode.class.getName());
        requestsNode.setRouteMethodCode("FR");
        requestsNode.setRouteMethodName(process.getRouteNode().getRouteMethodName());
        requestsNode.setRouteNodeName(REQUEST_PROCESS_NAME);
        provider.configureRequestNode(process, requestsNode);
        return requestsNode;
    }

    /**
     * @param process
     * @return RouteNode of a no-op node which will be used if the user sends no Chart+Org XML to this routing component.
     */
    private static RouteNode getNoChartOrgNode(RouteNodeInstance process) {
        RouteNode dynamicNode = process.getRouteNode();
        RouteNode noChartOrgNOde = new RouteNode();
        noChartOrgNOde.setActivationType(dynamicNode.getActivationType());
        noChartOrgNOde.setDocumentType(dynamicNode.getDocumentType());
        noChartOrgNOde.setFinalApprovalInd(dynamicNode.getFinalApprovalInd());
        noChartOrgNOde.setExceptionWorkgroupId(dynamicNode.getExceptionWorkgroupId());
        noChartOrgNOde.setMandatoryRouteInd(dynamicNode.getMandatoryRouteInd());
        noChartOrgNOde.setNodeType(NoOpNode.class.getName());
        noChartOrgNOde.setRouteMethodCode("FR");
        noChartOrgNOde.setRouteMethodName(null);
        noChartOrgNOde.setRouteNodeName(NO_STOP_NAME);
        return noChartOrgNOde;
    }

 
    
    // methods which can be overridden to change the chart org routing node behavior
    
    protected RouteNode getStopRequestNode(Stop stop, DocumentType documentType) {
        return documentType.getNamedProcess(REQUEST_PROCESS_NAME).getInitialRouteNode();
    }
    
}
