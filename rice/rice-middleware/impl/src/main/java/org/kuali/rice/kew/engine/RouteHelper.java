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

import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.kew.engine.node.BasicJoinEngine;
import org.kuali.rice.kew.engine.node.DynamicNode;
import org.kuali.rice.kew.engine.node.JoinEngine;
import org.kuali.rice.kew.engine.node.JoinNode;
import org.kuali.rice.kew.engine.node.Node;
import org.kuali.rice.kew.engine.node.RequestActivationNode;
import org.kuali.rice.kew.engine.node.RequestsNode;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.SimpleNode;
import org.kuali.rice.kew.engine.node.SplitNode;
import org.kuali.rice.kew.engine.node.SubProcessNode;


/**
 * A helper class which provides some useful utilities for examining and generating nodes.
 * Provides access to the {@link JoinEngine} and the {@link RoutingNodeFactory}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RouteHelper {

    private JoinEngine joinEngine = new BasicJoinEngine();
    private RoutingNodeFactory nodeFactory = new RoutingNodeFactory();

    public JoinEngine getJoinEngine() {
        return joinEngine;
    }

    public RoutingNodeFactory getNodeFactory() {
        return nodeFactory;
    }

    public boolean isSimpleNode(RouteNode routeNode) {
        return ClassLoaderUtils.isInstanceOf(getNode(routeNode), SimpleNode.class);
    }

    public boolean isJoinNode(RouteNode routeNode) {
        return ClassLoaderUtils.isInstanceOf(getNode(routeNode), JoinNode.class);
    }

    public boolean isSplitNode(RouteNode routeNode) {
        return ClassLoaderUtils.isInstanceOf(getNode(routeNode), SplitNode.class);
    }

    public boolean isDynamicNode(RouteNode routeNode) {
        return ClassLoaderUtils.isInstanceOf(getNode(routeNode), DynamicNode.class);
    }

    public boolean isSubProcessNode(RouteNode routeNode) {
        return ClassLoaderUtils.isInstanceOf(getNode(routeNode), SubProcessNode.class);
    }

    public boolean isRequestActivationNode(RouteNode routeNode) {
        return ClassLoaderUtils.isInstanceOf(getNode(routeNode), RequestActivationNode.class);
    }

    public boolean isRequestsNode(RouteNode routeNode) {
        return getNode(routeNode) instanceof RequestsNode;
    }

    public Node getNode(RouteNode routeNode) {
    	return (Node) GlobalResourceLoader.getObject(new ObjectDefinition(routeNode.getNodeType(), routeNode.getDocumentType().getApplicationId()));
    }
}
