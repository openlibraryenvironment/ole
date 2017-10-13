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
package org.kuali.rice.kew.rule.bo;

import java.util.List;
import java.util.Map;

import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.web.WebRuleUtils;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.kns.web.ui.Section;
import org.kuali.rice.krad.bo.BusinessObject;

/**
 * Inquirable implementation for KEW rule delegations.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RuleDelegationInquirableImpl extends KualiInquirableImpl {

	@Override
	public Object retrieveDataObject(Map fieldValues){
		RuleDelegationBo rule = (RuleDelegationBo)super.retrieveDataObject(fieldValues);
		WebRuleUtils.populateRuleMaintenanceFields(rule.getDelegationRule());
		return rule;
    }
	
	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.krad.inquiry.Inquirable#getBusinessObject(java.util.Map)
	 */
	public BusinessObject getBusinessObject(Map fieldValues) {
		RuleDelegationBo rule = (RuleDelegationBo)super.getBusinessObject(fieldValues);
		WebRuleUtils.populateRuleMaintenanceFields(rule.getDelegationRule());
		return rule;
	}

	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.krad.inquiry.Inquirable#getSections(org.kuali.rice.krad.bo.BusinessObject)
	 */
	public List getSections(BusinessObject bo) {
		List<Section> sections = super.getSections(bo);
		
		return WebRuleUtils.customizeSections(((RuleDelegationBo)bo).getDelegationRule(), sections, true);
		
	}

}
