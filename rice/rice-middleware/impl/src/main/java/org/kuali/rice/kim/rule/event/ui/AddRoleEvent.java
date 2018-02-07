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

import org.kuali.rice.kim.bo.ui.PersonDocumentRole;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.rule.ui.AddRoleRule;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEventBase;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class AddRoleEvent  extends KualiDocumentEventBase {
	private PersonDocumentRole role;

	public AddRoleEvent(String errorPathPrefix, IdentityManagementPersonDocument document) {
        super("adding role document " + getDocumentId(document), errorPathPrefix, document);
    }

    public AddRoleEvent(String errorPathPrefix, Document document, PersonDocumentRole role) {
        this(errorPathPrefix, (IdentityManagementPersonDocument) document);
        this.role = role;
    }

    public Class<? extends BusinessRule> getRuleInterfaceClass() {
        return AddRoleRule.class;
    }

    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((AddRoleRule) rule).processAddRole(this);
    }

	public PersonDocumentRole getRole() {
		return this.role;
	}

	public void setRole(PersonDocumentRole role) {
		this.role = role;
	}


}
