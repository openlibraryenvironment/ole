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
package org.kuali.rice.kew.impl.support.krms;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.extension.ExtensionRepositoryService;
import org.kuali.rice.kew.api.extension.ExtensionUtils;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeUtils;
import org.kuali.rice.kew.engine.node.service.RouteNodeService;
import org.kuali.rice.kew.framework.support.krms.RulesEngineExecutor;
import org.kuali.rice.kew.impl.peopleflow.PeopleFlowRouteModule;
import org.kuali.rice.kew.routemodule.RouteModule;
import org.kuali.rice.kew.util.ResponsibleParty;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.engine.Engine;
import org.kuali.rice.krms.api.engine.EngineResults;
import org.w3c.dom.Element;

import java.util.List;

/**
 * An implementation of a {@link RouteModule} which executes the KRMS rules engine using the configured
 * {@link RulesEngineExecutor}.  It then interprets those results and processes them, which may include instantiating
 * and delegating to another RouteModule.  Currently, this implementation only supports PeopleFlow results returned from
 * KRMS and passes those off to the {@link org.kuali.rice.kew.impl.peopleflow.PeopleFlowRouteModule}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RulesEngineRouteModule implements RouteModule {

    private static final Logger LOG = Logger.getLogger(RulesEngineRouteModule.class);

    private static final String RULES_ENGINE_ELEMENT = "rulesEngine";
    private static final String EXECUTOR_NAME_ATTRIBUTE = "executorName";
    private static final String EXECUTOR_CLASS_ATTRIBUTE = "executorClass";
    private static final String PEOPLE_FLOWS_SELECTED_ATTRIBUTE = "peopleFlowsSelected";

    private volatile Engine rulesEngine;

    private PeopleFlowRouteModule peopleFlowRouteModule;
    private ExtensionRepositoryService extensionRepositoryService;
    private RouteNodeService routeNodeService;

    @Override
    public List<ActionRequestValue> findActionRequests(RouteContext context) throws Exception {
        EngineResults engineResults = executeRulesEngine(context, getRulesEngine());
        if (engineResults != null) {
            processEngineResults(context, engineResults);
        }
        return peopleFlowRouteModule.findActionRequests(context);
    }

    @Override
    public ResponsibleParty resolveResponsibilityId(String responsibilityId) throws WorkflowException {
        return null;
    }

    @Override
    public boolean isMoreRequestsAvailable(RouteContext context) {
        return peopleFlowRouteModule.isMoreRequestsAvailable(context);
    }

    protected EngineResults executeRulesEngine(RouteContext context, Engine rulesEngine) {
        RulesEngineExecutor executor = loadRulesEngineExecutor(context);
        return executor.execute(context, rulesEngine);
    }

    protected void processEngineResults(RouteContext context, EngineResults engineResults) {
        String peopleFlowsSelected = (String)engineResults.getAttribute(PEOPLE_FLOWS_SELECTED_ATTRIBUTE);
        if (StringUtils.isBlank(peopleFlowsSelected)) {
            LOG.info("No PeopleFlows returned from KRMS execution.");
        } else {
            LOG.info("PeopleFlows returned from KRMS execution: " + peopleFlowsSelected);
        }
        NodeState nodeState = context.getNodeInstance().getNodeState(PeopleFlowRouteModule.PEOPLE_FLOW_SEQUENCE);
        if (nodeState == null) {
            nodeState = new NodeState();
            nodeState.setNodeInstance(context.getNodeInstance());
            nodeState.setKey(PeopleFlowRouteModule.PEOPLE_FLOW_SEQUENCE);
            context.getNodeInstance().addNodeState(nodeState);
        }
        nodeState.setValue(peopleFlowsSelected);
        if (!context.isSimulation()) {
            routeNodeService.save(nodeState);
        }
    }

    protected RulesEngineExecutor loadRulesEngineExecutor(RouteContext context) {
        RouteNode routeNode = context.getNodeInstance().getRouteNode();
        Element rulesEngineElement = RouteNodeUtils.getCustomRouteNodeElement(
                context.getNodeInstance().getRouteNode(), RULES_ENGINE_ELEMENT);
        if (rulesEngineElement == null) {
            throw new ConfigurationException("Failed to located rules engine configuration for route node: " + routeNode.getName());
        }
        String executorName = rulesEngineElement.getAttribute(EXECUTOR_NAME_ATTRIBUTE);
        String executorClassName = rulesEngineElement.getAttribute(EXECUTOR_CLASS_ATTRIBUTE);
        if (StringUtils.isBlank(executorName) && StringUtils.isBlank(executorClassName)) {
            throw new ConfigurationException("Failed to resolve a valid executor name or class name from rules engine configuration, was null or blank.");
        }
        RulesEngineExecutor rulesEngineExecutor = null;
        if (StringUtils.isNotBlank(executorClassName)) {
            rulesEngineExecutor = GlobalResourceLoader.getObject(new ObjectDefinition(executorClassName));
        } else if (StringUtils.isNotBlank(executorName)) {
            ExtensionDefinition extensionDefinition = getExtensionRepositoryService().getExtensionByName(executorName);
            if (extensionDefinition != null) {
                rulesEngineExecutor = ExtensionUtils.loadExtension(extensionDefinition);
            }
        }
        if (rulesEngineExecutor == null) {
            throw new ConfigurationException("Failed to load RulesEngineExecutor for either executorName=" + executorName + " or executorClass=" + executorClassName);
        }
        return rulesEngineExecutor;
    }

    protected Engine getRulesEngine() {
        if (rulesEngine == null) {
            rulesEngine = KrmsApiServiceLocator.getEngine();
        }
        return rulesEngine;
    }

    public PeopleFlowRouteModule getPeopleFlowRouteModule() {
        return peopleFlowRouteModule;
    }

    public void setPeopleFlowRouteModule(PeopleFlowRouteModule peopleFlowRouteModule) {
        this.peopleFlowRouteModule = peopleFlowRouteModule;
    }

    public ExtensionRepositoryService getExtensionRepositoryService() {
        return extensionRepositoryService;
    }

    public void setExtensionRepositoryService(ExtensionRepositoryService extensionRepositoryService) {
        this.extensionRepositoryService = extensionRepositoryService;
    }

    public RouteNodeService getRouteNodeService() {
        return routeNodeService;
    }

    public void setRouteNodeService(RouteNodeService routeNodeService) {
        this.routeNodeService = routeNodeService;
    }
    
}
