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

import org.kuali.rice.kew.engine.node.InitialNode;
import org.kuali.rice.kew.engine.node.SimpleResult;

/**
 * This node will wait until a sibling branch has joined and it has no more pending requests
 * before it signals that it is complete.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AdHocApproversNode extends InitialNode {

    public SimpleResult process(RouteContext context, RouteHelper helper) throws Exception {
        SimpleResult requestsResult = super.process(context, helper);
        return new SimpleResult(requestsResult.isComplete() && isSiblingBranchJoined(context));
    }

    private boolean isSiblingBranchJoined(RouteContext context) {
        return context.getNodeInstance().getBranch().getJoinNode() != null;
    }

}
