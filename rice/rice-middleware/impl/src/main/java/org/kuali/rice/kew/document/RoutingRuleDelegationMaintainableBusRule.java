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
package org.kuali.rice.kew.document;

import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kns.document.MaintenanceDocument;

/**
 * This is a description of what this class does - Garey don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RoutingRuleDelegationMaintainableBusRule extends RoutingRuleMaintainableBusRule
{

	// Name of the section in the xml file.
    protected static final String PERSON_RESP_SECTION = "delegationRule.personResponsibilities";
    // Name of the section in the xml file.
    protected static final String GROUP_RESP_SECTION = "delegationRule.groupResponsibilities";

    /**
	 * Returns the new RuleBaseValues business object. Overrides the base
	 * class method.  This business object has a different location
	 * of the RuleBaseValues.
	 */
	protected RuleBaseValues getRuleBaseValues(MaintenanceDocument document) {
		return this.getBusObject(document).getDelegationRule();
	}

	protected RuleDelegationBo getBusObject(MaintenanceDocument document){
		return (RuleDelegationBo)document.getNewMaintainableObject().getBusinessObject();
	}
	
	/**
	 * Returns the old RuleBaseValues business object. Overrides the base
	 * class method.  This business object has a different location
	 * of the RuleBaseValues.
	 */
	protected RuleBaseValues getOldRuleBaseValues(MaintenanceDocument document) {
		return this.getOldBusObject(document).getDelegationRule();
	}

	protected RuleDelegationBo getOldBusObject(MaintenanceDocument document){
		return (RuleDelegationBo)document.getOldMaintainableObject().getBusinessObject();
	}

	/**
	 * This overridden method returns the person section name for the delegation doc
	 *
	 * @see org.kuali.rice.kew.document.RoutingRuleMaintainableBusRule#getPersonSectionName()
	 */
	@Override
	protected String getPersonSectionName() {
		return PERSON_RESP_SECTION;
	}

	/**
	 * This overridden method returns the group section name for the delegation doc
	 *
	 * @see org.kuali.rice.kew.document.RoutingRuleMaintainableBusRule#getGroupSectionName()
	 */
	@Override
	protected String getGroupSectionName() {
		return GROUP_RESP_SECTION;
	}

}
