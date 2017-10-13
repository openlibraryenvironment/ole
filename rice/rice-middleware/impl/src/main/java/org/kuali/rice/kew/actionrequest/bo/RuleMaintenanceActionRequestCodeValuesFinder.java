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
package org.kuali.rice.kew.actionrequest.bo;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleTemplateOptionBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.kns.web.struts.form.KualiMaintenanceForm;

import java.util.ArrayList;
import java.util.List;

/**
 * A values finder for returning KEW Action Request codes related to Kuali maintenance forms.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RuleMaintenanceActionRequestCodeValuesFinder extends ActionRequestCodeValuesFinder {

	/**
	 * @see org.kuali.rice.krad.keyvalues.KeyValuesFinder#getKeyValues()
	 */
	@Override
	public List<KeyValue> getKeyValues() {
		final List<KeyValue> actionRequestCodes = new ArrayList<KeyValue>();
		// Acquire the Kuali form, and return the super class' result if the form is not a Kuali maintenance form.
		final KualiForm kForm = KNSGlobalVariables.getKualiForm();
		if (!(kForm instanceof KualiMaintenanceForm)) {
			return super.getKeyValues();
		}
		// Acquire the Kuali maintenance form's document and its rule template.
		final MaintenanceDocument maintDoc = (MaintenanceDocument) ((KualiMaintenanceForm) kForm).getDocument();
		final RuleTemplateBo ruleTemplate = ((RuleBaseValues) maintDoc.getNewMaintainableObject().getBusinessObject()).getRuleTemplate();
		// Ensure that the rule template is defined.
		if (ruleTemplate == null) {
			throw new RuntimeException("Rule template cannot be null for document ID " + maintDoc.getDocumentNumber());
		}
		// get the options to check for, as well as their related KEW constants.
		final RuleTemplateOptionBo[] ruleOpts = {ruleTemplate.getAcknowledge(), ruleTemplate.getComplete(),
				ruleTemplate.getApprove(), ruleTemplate.getFyi()};
		final String[] ruleConsts = {KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ,
				KewApiConstants.ACTION_REQUEST_APPROVE_REQ, KewApiConstants.ACTION_REQUEST_FYI_REQ};
		// Add the rule options to the list if they are not defined (true by default) or if they are explicitly set to true.
		for (int i = 0; i < ruleOpts.length; i++) {
			if (ruleOpts[i] == null || ruleOpts[i].getValue() == null || "true".equals(ruleOpts[i].getValue())) {
				actionRequestCodes.add(new ConcreteKeyValue(ruleConsts[i], KewApiConstants.ACTION_REQUEST_CODES.get(ruleConsts[i])));
			}
		}
		return actionRequestCodes;
	}
	
}
