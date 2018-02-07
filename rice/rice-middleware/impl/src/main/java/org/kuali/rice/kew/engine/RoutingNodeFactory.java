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
package org.kuali.rice.kew.engine;

import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.service.KEWServiceLocator;


/**
 * Provides factory methods for creating {@link Branch} objects and {@link RouteNodeInstance} object.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RoutingNodeFactory {

    public Branch createBranch(String name, Branch parentBranch, RouteNodeInstance initialNodeInstance) {
        Branch branch = new Branch();
        branch.setName(name);
        branch.setParentBranch(parentBranch);
        branch.setInitialNode(initialNodeInstance);
        initialNodeInstance.setBranch(branch);
        return branch;
    }
    
    public RouteNodeInstance createRouteNodeInstance(String documentId, RouteNode node) {
        RouteNodeInstance nodeInstance = new RouteNodeInstance();
        nodeInstance.setActive(false);
        nodeInstance.setComplete(false);
        nodeInstance.setRouteNode(node);
        nodeInstance.setDocumentId(documentId);
        return nodeInstance;
    }
    
    public RouteNode getRouteNode(RouteContext context, String name) {
        return KEWServiceLocator.getRouteNodeService().findRouteNodeByName(context.getDocument().getDocumentType().getDocumentTypeId(), name);
    }
    
}
