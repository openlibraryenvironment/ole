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
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.engine.node.Node;
import org.kuali.rice.kew.engine.node.ProcessResult;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;

import java.util.ArrayList;
import java.util.List;


/**
 * Common superclass for all Transition Engines.  A TransitionEngine handles transitioning into and out of
 * a {@link RouteNodeInstance}.  The TransitionEngine is also responsible for determining if a Node has completed.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class TransitionEngine {
    
	private RouteHelper helper;
	
	public RouteNodeInstance transitionTo(RouteNodeInstance nextNodeInstance, RouteContext context) throws Exception {
		return nextNodeInstance;
	}
    
    /**
     * Tell the WorkflowEngine processing the activeNodeInstance if the node is complete and transitionFrom can 
     * be called.
     *
     * @return boolean
     * @param context for routing
     * @throws Exception
     */
    public abstract ProcessResult isComplete(RouteContext context) throws Exception;
	
    public Transition transitionFrom(RouteContext context, ProcessResult processResult) throws Exception {
        return new Transition(resolveNextNodeInstances(context.getNodeInstance()));
    }
    
    protected void setRouteHelper(RouteHelper helper) {
    	this.helper = helper;
    }
    
    protected RouteHelper getRouteHelper() {
    	return helper;
    }
    
    protected Node getNode(RouteNode routeNode, Class nodeClass) throws Exception {
		return helper.getNode(routeNode);
    }
    
    /**
     * Determines the next nodes instances for the transition.  If the node instance already
     * has next nodes instances (i.e. a dynamic node), then those will be returned.  Otherwise
     * it will resolve the next nodes from the RouteNode prototype.
     * @param nodeInstance for the transition
     * @param nextRouteNodes list of route notes
     * @return list of route note instances
     */
    protected List<RouteNodeInstance> resolveNextNodeInstances(RouteNodeInstance nodeInstance, List<RouteNode> nextRouteNodes) {
        List<RouteNodeInstance> nextNodeInstances = new ArrayList<RouteNodeInstance>();
        for (RouteNode nextRouteNode : nextRouteNodes)
        {
            RouteNode nextNode = (RouteNode) nextRouteNode;
            RouteNodeInstance nextNodeInstance = getRouteHelper().getNodeFactory().createRouteNodeInstance(nodeInstance.getDocumentId(), nextNode);
            nextNodeInstance.setBranch(nodeInstance.getBranch());
            nextNodeInstance.setProcess(nodeInstance.getProcess());
            nextNodeInstances.add(nextNodeInstance);
        }
        return nextNodeInstances;
    }
    
    protected List<RouteNodeInstance> resolveNextNodeInstances(RouteNodeInstance nodeInstance) {
        return resolveNextNodeInstances(nodeInstance, nodeInstance.getRouteNode().getNextNodes());
    }
    
}
