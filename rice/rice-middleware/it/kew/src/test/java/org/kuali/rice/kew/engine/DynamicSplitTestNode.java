/**
 * Copyright 2005-2013 The Kuali Foundation
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

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.engine.node.DynamicNode;
import org.kuali.rice.kew.engine.node.DynamicResult;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.api.exception.WorkflowException;


public class DynamicSplitTestNode implements DynamicNode {

    private static final String NEXT_NODE_NAME = "SubRequests";
    private static final String[] ROLES = new String[] { "Sub1", "Sub2", "Sub3" };
    
    public DynamicResult transitioningInto(RouteContext context, RouteNodeInstance process, RouteHelper helper) throws Exception {
        List<RouteNodeInstance> nextNodeInstances = new ArrayList<RouteNodeInstance>();
        for (int index = 0; index < ROLES.length; index++) {
            String roleName = ROLES[index];
            RouteNode node = helper.getNodeFactory().getRouteNode(context, NEXT_NODE_NAME);
            if (node == null) {
                throw new WorkflowException("Couldn't locate node for name: " + NEXT_NODE_NAME);
            }
            RouteNodeInstance nextNodeInstance = helper.getNodeFactory().createRouteNodeInstance(context.getDocument().getDocumentId(), node);
            Branch branch = helper.getNodeFactory().createBranch(roleName, context.getNodeInstance().getBranch(), nextNodeInstance);
            branch.addBranchState(new BranchState("role", roleName));
            branch.setSplitNode(context.getNodeInstance());
            nextNodeInstances.add(nextNodeInstance);
        }
        //return new DynamicResult(true, nextNodeInstances);
        throw new UnsupportedOperationException("No!!!!");
    }
    
    

    public DynamicResult transitioningOutOf(RouteContext context, RouteHelper helper) throws Exception {
        throw new UnsupportedOperationException("never written");
    }
}
