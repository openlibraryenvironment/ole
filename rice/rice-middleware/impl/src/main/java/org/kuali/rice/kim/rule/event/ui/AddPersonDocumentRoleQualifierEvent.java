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
package org.kuali.rice.kim.rule.event.ui;

import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.bo.ui.PersonDocumentRole;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.rule.ui.AddPersonDocumentRoleQualifierRule;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEventBase;

/**
 * This is a description of what this class does - wliang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class AddPersonDocumentRoleQualifierEvent extends KualiDocumentEventBase {

	private IdentityManagementPersonDocument document;
	private KimDocumentRoleMember kimDocumentRoleMember;
	private PersonDocumentRole role;
	private int selectedRoleIdx;
		
	public AddPersonDocumentRoleQualifierEvent(String errorPathPrefix, IdentityManagementPersonDocument document, 
			KimDocumentRoleMember kimDocumentRoleMember, PersonDocumentRole role, int selectedRoleIdx) {
        super("adding role qualifiers to person document " + getDocumentId(document), errorPathPrefix, document);
        this.document = document;
        this.kimDocumentRoleMember = kimDocumentRoleMember;
        this.role = role;
        this.selectedRoleIdx = selectedRoleIdx;
    }

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
	 */
	public Class<? extends BusinessRule> getRuleInterfaceClass() {
		return AddPersonDocumentRoleQualifierRule.class;
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.rice.krad.rules.rule.BusinessRule)
	 */
	public boolean invokeRuleMethod(BusinessRule rule) {
		return ((AddPersonDocumentRoleQualifierRule) rule).processAddPersonDocumentRoleQualifier(document, role, kimDocumentRoleMember, selectedRoleIdx);
	}

}
