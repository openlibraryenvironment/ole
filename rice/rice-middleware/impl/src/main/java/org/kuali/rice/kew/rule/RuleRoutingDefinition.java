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

import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;

/**
 * A {@link WorkflowAttributeDefinition} for the {@link RuleRoutingAttribute}.
 * 
 * @see RuleRoutingAttribute
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class RuleRoutingDefinition {
        
	private static final long serialVersionUID = -5633697385117416044L;
	private static final String RULE_ROUTING_ATTRIBUTE_CLASS = "org.kuali.rice.kew.rule.RuleRoutingAttribute";
        
	public static WorkflowAttributeDefinition createAttributeDefinition(String docTypeName) {
	    WorkflowAttributeDefinition.Builder builder = WorkflowAttributeDefinition.Builder.create(RULE_ROUTING_ATTRIBUTE_CLASS);
	    builder.addParameter(docTypeName);
	    return builder.build();
	}
	
	private RuleRoutingDefinition() {}

}
