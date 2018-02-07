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
import org.kuali.rice.kew.engine.node.JoinNode;
import org.kuali.rice.kew.engine.node.ProcessResult;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;

/**
 * Handles transitions to and from {@link JoinNode} nodes in the route path.
 * 
 * @see JoinNode
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JoinTransitionEngine extends TransitionEngine {

    public RouteNodeInstance transitionTo(RouteNodeInstance nextNodeInstance, RouteContext context) {
        if (context.getNodeInstance().getBranch().getJoinNode() != null) {
            nextNodeInstance = context.getNodeInstance().getBranch().getJoinNode();
        } else{
            getRouteHelper().getJoinEngine().createExpectedJoinState(context, nextNodeInstance, context.getNodeInstance());
        }
        getRouteHelper().getJoinEngine().addActualJoiner(nextNodeInstance, context.getNodeInstance().getBranch());
        return nextNodeInstance;
    }
    
    public ProcessResult isComplete(RouteContext context) throws Exception {
        RouteNodeInstance nodeInstance = context.getNodeInstance();
		JoinNode node = (JoinNode)getNode(nodeInstance.getRouteNode(), JoinNode.class);
		return node.process(context, getRouteHelper());
    }

}
