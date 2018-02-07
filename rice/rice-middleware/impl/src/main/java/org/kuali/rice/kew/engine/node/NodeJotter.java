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

import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;

import java.util.Iterator;
import java.util.List;


/**
 * Logs {@link RouteNodeInstance} graphs in a format which is indented and easy to read. 
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NodeJotter {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(NodeJotter.class);
    private static final String INDENT = "   ";

    public static void jotNodeInstance(DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance) {
        try {
            if (LOG.isDebugEnabled()) {
                List initialNodeInstances = document.getInitialRouteNodeInstances();
                for (Iterator iterator = initialNodeInstances.iterator(); iterator.hasNext();) {
                    RouteNodeInstance initialNodeInstance = (RouteNodeInstance) iterator.next();
                    NodeType nodeType = NodeType.fromNodeInstance(initialNodeInstance);
                    LOG.debug(orchestrateOutput(initialNodeInstance, nodeType, null, 0));
                }
            } else if (LOG.isInfoEnabled()) {
                NodeType nodeType = NodeType.fromNodeInstance(nodeInstance);
                LOG.info(outputNodeInstanceToLog(nodeInstance, nodeType, 0));
            }
        } catch (Throwable t) {
            LOG.warn("Caught error attempting to jot node instance" + nodeInstance);
        }
    }

    private static String orchestrateOutput(RouteNodeInstance nodeInstance, NodeType nodeType, SplitJoinContext sjCtx, int indentDepth) throws Exception {
        String output = "";
        boolean isSplit = NodeType.SPLIT.equals(nodeType);
        boolean isJoin = NodeType.JOIN.equals(nodeType);
        if (isJoin && sjCtx != null) {
            sjCtx.joinNodeInstance = nodeInstance;
            return output;
        }
        SplitJoinContext newSplitJoinContext = null;
        if (isSplit) {
            newSplitJoinContext = new SplitJoinContext(nodeInstance);
        }
        output += outputNodeInstanceToLog(nodeInstance, nodeType, indentDepth);
        for (Iterator iterator = nodeInstance.getNextNodeInstances().iterator(); iterator.hasNext();) {
            RouteNodeInstance nextNodeInstance = (RouteNodeInstance) iterator.next();
            nodeType = NodeType.fromNodeInstance(nextNodeInstance);
            if (newSplitJoinContext != null) {
                output += orchestrateOutput(nextNodeInstance, nodeType, newSplitJoinContext, indentDepth + 1);
            } else {
                output += orchestrateOutput(nextNodeInstance, nodeType, sjCtx, indentDepth + 1);
            }
        }
        if (isSplit) {
            if (newSplitJoinContext != null && newSplitJoinContext.joinNodeInstance != null) {
                nodeType = NodeType.fromNodeInstance(newSplitJoinContext.joinNodeInstance);
                output += orchestrateOutput(newSplitJoinContext.joinNodeInstance, nodeType, sjCtx, indentDepth);
            }
        }
        return output;
    }

    private static String outputNodeInstanceToLog(RouteNodeInstance nodeInstance, NodeType nodeType, int indentDepth) throws Exception {
        String memAddress = nodeInstance.toString().split("@")[1];
        String dataIndent = getIndent(indentDepth + 1);
        String output = getIndent(indentDepth) + "[NODE type=" + nodeType.getName() + "]\n" + dataIndent + "Name: " + nodeInstance.getName() + "(" + memAddress + ")\n";
        output += dataIndent + "State: ";
        for (Iterator iterator = nodeInstance.getState().iterator(); iterator.hasNext();) {
            NodeState nodeState = (NodeState) iterator.next();
            output += nodeState.getKey() + "=" + nodeState.getValue();
            if (iterator.hasNext()) {
                output += ",";
            }
        }
        output += "\n" + dataIndent + "Status Flags: initial=" + nodeInstance.isInitial() + ", active=" + nodeInstance.isActive() + ", complete=" + nodeInstance.isComplete();
        output += (nodeInstance.getProcess() == null ? "" : "\n" + dataIndent + "ProcessDefinition Name: " + nodeInstance.getProcess().getName());
        output += "\n";
        return output;
    }

    private static String getIndent(int indentDepth) {
        StringBuffer buffer = new StringBuffer();
        for (int depth = 0; depth < indentDepth; depth++) {
            buffer.append(INDENT);
        }
        return buffer.toString();
    }

    private static class SplitJoinContext {
        public RouteNodeInstance splitNodeInstance;
        public RouteNodeInstance joinNodeInstance;

        public SplitJoinContext(RouteNodeInstance splitNodeInstance) {
            this.splitNodeInstance = splitNodeInstance;
        }
    }

}
