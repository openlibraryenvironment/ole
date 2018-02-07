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

import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;

/**
 * A simple implementation of a {@link JoinNode} which indicates it is complete once all branches
 * have joined.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SimpleJoinNode implements JoinNode {
        
    protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(getClass());
    
    public JoinResult process(RouteContext context, RouteHelper helper) throws Exception {
        RouteNodeInstance nodeInstance = context.getNodeInstance();
        LOG.debug("Processing join node " + nodeInstance.getRouteNodeInstanceId());
        if (helper.getJoinEngine().isJoined(nodeInstance)) {
            LOG.debug("Join node is completely joined "+nodeInstance.getRouteNodeInstanceId());
            return new JoinResult(true);
        }
        return new JoinResult(false);
    }
    
    
	
}
