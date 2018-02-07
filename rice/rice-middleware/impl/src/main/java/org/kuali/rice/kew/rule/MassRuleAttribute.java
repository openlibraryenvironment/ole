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

import java.util.List;

import org.kuali.rice.kew.engine.RouteContext;


/**
 * An attribute which can be used to filter a List of rules prior to evaluation
 * by the rules engine.
 * 
 * @see FlexRM
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface MassRuleAttribute {
    
    public List<org.kuali.rice.kew.api.rule.Rule> filterNonMatchingRules(RouteContext routeContext, List<org.kuali.rice.kew.api.rule.Rule> rules);

}
