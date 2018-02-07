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
import org.kuali.rice.kew.engine.node.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Handles transitions into and out of {@link SplitNode} nodes.
 * 
 * @see SplitNode
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SplitTransitionEngine extends TransitionEngine {
	
    public ProcessResult isComplete(RouteContext context) throws Exception {
        SplitNode node = (SplitNode)getNode(context.getNodeInstance().getRouteNode(), SplitNode.class);
        return node.process(context, getRouteHelper());
    }
    
	public Transition transitionFrom(RouteContext context, ProcessResult processResult)
			throws Exception {
		RouteNodeInstance splitInstance = context.getNodeInstance();
		List<RouteNodeInstance> nextNodeInstances = new ArrayList<RouteNodeInstance>();
        SplitResult result = (SplitResult)processResult;
        for (String branchName : result.getBranchNames())
        {
            for (RouteNode routeNode : splitInstance.getRouteNode().getNextNodes())
            {
                if (routeNode.getBranch() != null && routeNode.getBranch().getName().equals(branchName))
                {
                    nextNodeInstances.add(createSplitChild(branchName, routeNode, splitInstance));
                }
            }
        }
		return new Transition(nextNodeInstances);
	}
	
	public static RouteNodeInstance createSplitChild(String branchName, RouteNode routeNode, RouteNodeInstance splitInstance) {
	    RouteHelper routeHelper = new RouteHelper();
	    RouteNodeInstance nextNodeInstance = routeHelper.getNodeFactory().createRouteNodeInstance(splitInstance.getDocumentId(), routeNode);
		Branch branch = routeHelper.getNodeFactory().createBranch(branchName, splitInstance.getBranch(), nextNodeInstance);
		branch.setSplitNode(splitInstance);
		nextNodeInstance.setBranch(branch);
		nextNodeInstance.setProcess(splitInstance.getProcess());
		return nextNodeInstance;
	}
	
}
