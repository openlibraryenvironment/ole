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
package org.kuali.rice.kew.rule;

import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.NodeState;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.Utilities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Rule selector that select a rule based on configured rule name 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NamedRuleSelector implements RuleSelector {
    /**
     * The route node config param consulted to determine the rule name to select
     */
    public static final String RULE_NAME_CFG_KEY = "ruleName";

    /**
     * @return the name of the rule that should be selected
     */
    protected String getName(RouteContext context, DocumentRouteHeaderValue routeHeader,
            RouteNodeInstance nodeInstance, String selectionCriterion, Timestamp effectiveDate) {
        String ruleName = null;
        RouteNode routeNodeDef = nodeInstance.getRouteNode();
        // first check to see if there is a rule name configured on the node instance
        NodeState ns = nodeInstance.getNodeState(KewApiConstants.RULE_NAME_NODE_STATE_KEY);
        if (ns != null) {
            ruleName = ns.getValue();
        } else {
            // otherwise check the node def
            Map<String, String> routeNodeConfig = Utilities.getKeyValueCollectionAsMap(routeNodeDef.getConfigParams());
            ruleName = routeNodeConfig.get(RULE_NAME_CFG_KEY);
        }
        return ruleName;
    }

    public List<Rule> selectRules(RouteContext context, DocumentRouteHeaderValue routeHeader,
            RouteNodeInstance nodeInstance, String selectionCriterion, Timestamp effectiveDate) {

        String ruleName = getName(context, routeHeader, nodeInstance, selectionCriterion, effectiveDate);

        if (ruleName == null) {
            throw new RiceIllegalStateException("No 'ruleName' configuration parameter present on route node definition: " + nodeInstance.getRouteNode());
        }

        org.kuali.rice.kew.api.rule.Rule ruleDef = KewApiServiceLocator.getRuleService().getRuleByName(ruleName);
        if (ruleDef == null) {
            throw new RiceIllegalStateException("No rule found with name '" + ruleName + "'");
        }

        List<Rule> rules = new ArrayList<Rule>(1);
        rules.add(new RuleImpl(ruleDef));
        return rules;
    }
}
