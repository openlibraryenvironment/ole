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

import java.sql.Timestamp;
import java.util.List;

import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;


/**
 * RuleSelector is responsible for selecting the rules to be evaluated for a given rule-based requests node.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RuleSelector {
    /**
     * Returns a list of rules selected given the criteria specified in the arguments.
     * 
     * @param context the RouteContext
     * @param routeHeader the DocumentRouteHeaderValue of the current document
     * @param nodeInstance the current RouteNodeInstance being executed
     * @param selectionCriterion an implementation-specific criterion passed in from the calling context
     * @param effectiveDate an optional criterion that indicates that the rules selected should be active on the given date 
     * @return a list of applicable rules, if any (null or empty list otherwise)
     * @throws org.kuali.rice.kew.api.exception.WorkflowException if anything goes awry...
     */
    /* inputs taken from FlexRM getActionRequests/makeActionRequests */
    public List<Rule> selectRules(RouteContext context, DocumentRouteHeaderValue routeHeader, RouteNodeInstance nodeInstance, String selectionCriterion, Timestamp effectiveDate);
}
