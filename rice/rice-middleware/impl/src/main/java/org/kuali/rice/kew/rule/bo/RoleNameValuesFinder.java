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

import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.kns.web.struts.form.KualiMaintenanceForm;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * A values finder for generating a list of Role names that can be selected for a given RuleTemplate.
 * 
 * This is dependant on the template selected on the maintenance document so it needs to use
 * GlobalVariables to get a reference to the KualiForm so it can examine the business object
 * and extract the role names from the RuleTemplate.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RoleNameValuesFinder extends KeyValuesBase {
	
	@Override
	public List<KeyValue> getKeyValues() {
		List<KeyValue> roleNames = new ArrayList<KeyValue>();
		if (KNSGlobalVariables.getKualiForm() != null && KNSGlobalVariables.getKualiForm() instanceof KualiMaintenanceForm) {
			KualiMaintenanceForm form = (KualiMaintenanceForm)KNSGlobalVariables.getKualiForm();
			MaintenanceDocument document = (MaintenanceDocument)form.getDocument();
			PersistableBusinessObject businessObject = document.getNewMaintainableObject().getBusinessObject();
			RuleBaseValues rule = null;
			if (businessObject instanceof RuleBaseValues) {
				rule = (RuleBaseValues)businessObject;
			} else if (businessObject instanceof RuleDelegationBo) {
				rule = ((RuleDelegationBo)businessObject).getDelegationRule();
			} else {
				throw new RiceRuntimeException("Cannot locate RuleBaseValues business object on maintenance document.  Business Object was " + businessObject);
			}
			RuleTemplateBo ruleTemplate = rule.getRuleTemplate();
			List<RoleName> roles = ruleTemplate.getRoles();
			for (RoleName role : roles) {
				roleNames.add(new ConcreteKeyValue(role.getName(), role.getLabel()));
			}
		}
		return roleNames;
	}

}
