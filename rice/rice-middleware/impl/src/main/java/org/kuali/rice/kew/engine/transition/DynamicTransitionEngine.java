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
package org.kuali.rice.kew.engine.transition;

import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.*;
import org.kuali.rice.kew.exception.RouteManagerException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * The DynamicTransitionEngine operates on a {@link DynamicNode} and takes the next node instances returned 
 * by the node and runs returns them in a TransitionResult after doing some processing and validation on them.
 * 
 * @see DynamicNode
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DynamicTransitionEngine extends TransitionEngine {

    // TODO interate the all the nodes and attach the dynamic node as the 'process'
    // don't include the dynamic node instance in the routing structure - require a correctly built graph
    // change dynamic node signiture to next node because of above
    // reconcile branching if necessary
    public RouteNodeInstance transitionTo(RouteNodeInstance dynamicNodeInstance, RouteContext context) throws Exception {
        dynamicNodeInstance.setInitial(false);
        dynamicNodeInstance.setActive(false);
        DynamicNode dynamicNode = (DynamicNode) getNode(dynamicNodeInstance.getRouteNode(), DynamicNode.class);
        DynamicResult result = dynamicNode.transitioningInto(context, dynamicNodeInstance, getRouteHelper());
        RouteNodeInstance nextNodeInstance = result.getNextNodeInstance();
        RouteNodeInstance finalNodeInstance = null;
        if (result.isComplete()) {
            dynamicNodeInstance.setComplete(true);
            finalNodeInstance = getFinalNodeInstance(dynamicNodeInstance, context); 
            if (nextNodeInstance == null) {
                nextNodeInstance = finalNodeInstance;
            }
        }
      
        if (nextNodeInstance !=null) {
            initializeNodeGraph(context, dynamicNodeInstance, nextNodeInstance, new HashSet<RouteNodeInstance>(), finalNodeInstance);
        }
        return nextNodeInstance;   
    }
    
    public ProcessResult isComplete(RouteContext context) throws Exception {
        throw new UnsupportedOperationException("isComplete() should not be invoked on a Dynamic node!");
    }
    
    public Transition transitionFrom(RouteContext context, ProcessResult processResult) throws Exception {
        
        Transition transition = new Transition();
        RouteNodeInstance dynamicNodeInstance = context.getNodeInstance().getProcess();
        DynamicNode dynamicNode = (DynamicNode) getNode(dynamicNodeInstance.getRouteNode(), DynamicNode.class);
        DynamicResult result = dynamicNode.transitioningOutOf(context, getRouteHelper());
        if (result.getNextNodeInstance() == null && result.getNextNodeInstances().isEmpty() && result.isComplete()) {
            dynamicNodeInstance.setComplete(true);
            RouteNodeInstance finalNodeInstance = getFinalNodeInstance(dynamicNodeInstance, context);
            if (finalNodeInstance != null) {
                transition.getNextNodeInstances().add(finalNodeInstance);    
            }
        } else {
            if (result.getNextNodeInstance() != null) {
                result.getNextNodeInstance().setProcess(dynamicNodeInstance);
                transition.getNextNodeInstances().add(result.getNextNodeInstance());    
            }
            for (Iterator iter = result.getNextNodeInstances().iterator(); iter.hasNext();) {
                RouteNodeInstance nextNodeInstance = (RouteNodeInstance) iter.next();
                nextNodeInstance.setProcess(dynamicNodeInstance);
            }
            transition.getNextNodeInstances().addAll(result.getNextNodeInstances());
        }
        return transition;
    }

    /**
     * This method checks the next node returned by the user and walks the resulting node graph, filling in required data where possible.
     * Will throw errors if there is a problem with what the implementor has returned to us. This allows them to do things like return next
     * nodes with no attached branches, and we will go ahead and generate the branches for them, etc.
     */
    private void initializeNodeGraph(RouteContext context, RouteNodeInstance dynamicNodeInstance, RouteNodeInstance nodeInstance, Set<RouteNodeInstance> nodeInstances, RouteNodeInstance finalNodeInstance) throws Exception {
        if (nodeInstances.contains(nodeInstance)) {
            throw new RouteManagerException("A cycle was detected in the node graph returned from the dynamic node.", context);
        }
        nodeInstances.add(nodeInstance);
        nodeInstance.setProcess(dynamicNodeInstance);
        List<RouteNodeInstance> nextNodeInstances = nodeInstance.getNextNodeInstances();
        
        if (nextNodeInstances.size() > 1) {
            // TODO implement this feature
//            throw new UnsupportedOperationException("Need to implement support for branch generation!");
        }
        for (RouteNodeInstance nextNodeInstance : nextNodeInstances)
        {
            initializeNodeGraph(context, dynamicNodeInstance, nextNodeInstance, nodeInstances, finalNodeInstance);
        }
        if (nextNodeInstances.isEmpty() && finalNodeInstance != null) {
            nodeInstance.addNextNodeInstance(finalNodeInstance);
        }
    }

    private RouteNodeInstance getFinalNodeInstance(RouteNodeInstance dynamicNodeInstance, RouteContext context) throws Exception {
        List<RouteNode> nextNodes = dynamicNodeInstance.getRouteNode().getNextNodes();
        if (nextNodes.size() > 1) {
            throw new RouteManagerException("There should only be 1 next node following a dynamic node, there were " + nextNodes.size(), context);
        }
        RouteNodeInstance finalNodeInstance = null;
        if (!nextNodes.isEmpty()) {
            finalNodeInstance = getRouteHelper().getNodeFactory().createRouteNodeInstance(context.getDocument().getDocumentId(), (RouteNode) nextNodes.get(0));
            finalNodeInstance.setBranch(dynamicNodeInstance.getBranch());
            finalNodeInstance.setProcess(dynamicNodeInstance.getProcess());
        }
        return finalNodeInstance;
    }
}
