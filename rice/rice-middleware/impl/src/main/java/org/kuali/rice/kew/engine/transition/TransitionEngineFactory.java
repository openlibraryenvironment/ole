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

import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;

/**
 * Factory which creates a {@link TransitionEngine} for the given {@link RouteNodeInstance}.  The 
 * transition engine is determined based on the type of the node instance.
 * 
 * @see TransitionEngine
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TransitionEngineFactory {

	public static TransitionEngine createTransitionEngine(RouteNodeInstance nodeInstance) throws Exception {
		RouteHelper helper = new RouteHelper();
		RouteNode routeNode = nodeInstance.getRouteNode();
		TransitionEngine engine = null;
		if (helper.isSimpleNode(routeNode)) {
			engine = new SimpleTransitionEngine();
		} else if (helper.isSplitNode(routeNode)) {
			engine = new SplitTransitionEngine();
		} else if (helper.isJoinNode(routeNode)) {
			engine = new JoinTransitionEngine();
		} else if (helper.isDynamicNode(routeNode)) {
			engine = new DynamicTransitionEngine();
		} else if (helper.isSubProcessNode(routeNode)) {
			engine = new SubProcessTransitionEngine();
		} else {
			throw new WorkflowException("Could not locate transition engine for node " + routeNode.getNodeType());
		}
		engine.setRouteHelper(helper);
		return engine;
	}
	
}
