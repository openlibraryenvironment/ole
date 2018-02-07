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
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.ProcessResult;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.engine.node.SubProcessNode;
import org.kuali.rice.kew.engine.node.SubProcessResult;
import org.kuali.rice.kew.api.exception.WorkflowException;

import java.util.ArrayList;
import java.util.List;


/**
 * Handles transitions into and out of {@link SubProcessNode} nodes.
 * 
 * @see SubProcessNode
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SubProcessTransitionEngine extends TransitionEngine {
    
    public RouteNodeInstance transitionTo(RouteNodeInstance nextNodeInstance, RouteContext context) throws Exception {
        String processName = nextNodeInstance.getRouteNode().getRouteNodeName();
        ProcessDefinitionBo process = context.getDocument().getDocumentType().getNamedProcess(processName);
        if (process == null) {
            throw new WorkflowException("Could not locate named sub process: " + processName);
        }
        RouteNodeInstance subProcessNodeInstance = nextNodeInstance;
        subProcessNodeInstance.setInitial(false);
        subProcessNodeInstance.setActive(false);
        nextNodeInstance = getRouteHelper().getNodeFactory().createRouteNodeInstance(subProcessNodeInstance.getDocumentId(), process.getInitialRouteNode());
        nextNodeInstance.setBranch(subProcessNodeInstance.getBranch());
        nextNodeInstance.setProcess(subProcessNodeInstance);
	    return nextNodeInstance;
	}

	public ProcessResult isComplete(RouteContext context) throws Exception {
        throw new UnsupportedOperationException("isComplete() should not be invoked on a SubProcess!");
    }

    public Transition transitionFrom(RouteContext context, ProcessResult processResult) throws Exception {
		RouteNodeInstance processInstance = context.getNodeInstance().getProcess();
        processInstance.setComplete(true);
		SubProcessNode node = (SubProcessNode)getNode(processInstance.getRouteNode(), SubProcessNode.class);
		SubProcessResult result = node.process(context);
		List<RouteNodeInstance> nextNodeInstances = new ArrayList<RouteNodeInstance>();
		if (result.isComplete()) {
			List<RouteNode> nextNodes = processInstance.getRouteNode().getNextNodes();
            for (RouteNode nextNode : nextNodes)
            {
                RouteNodeInstance nextNodeInstance = getRouteHelper().getNodeFactory().createRouteNodeInstance(processInstance.getDocumentId(), nextNode);
                nextNodeInstance.setBranch(processInstance.getBranch());
                nextNodeInstance.setProcess(processInstance.getProcess());
                nextNodeInstances.add(nextNodeInstance);
            }
		}
		return new Transition(nextNodeInstances);
	}

}
