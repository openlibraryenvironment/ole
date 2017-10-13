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
package org.kuali.rice.kim.impl.responsibility;

import org.kuali.rice.kim.bo.ui.KimDocumentRoleResponsibility;
import org.kuali.rice.kim.document.IdentityManagementRoleDocument;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEventBase;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class AddResponsibilityEvent extends KualiDocumentEventBase {
	private KimDocumentRoleResponsibility permission;

	public AddResponsibilityEvent(String errorPathPrefix, IdentityManagementRoleDocument document) {
        super("adding Responsibility document " + getDocumentId(document), errorPathPrefix, document);
    }

    public AddResponsibilityEvent(String errorPathPrefix, Document document, KimDocumentRoleResponsibility permission) {
        this(errorPathPrefix, (IdentityManagementRoleDocument) document);
        this.permission = (KimDocumentRoleResponsibility) ObjectUtils.deepCopy(permission);
    }

    public Class<? extends BusinessRule> getRuleInterfaceClass() {
        return AddResponsibilityRule.class;
    }

    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((AddResponsibilityRule) rule).processAddResponsibility(this);
    }

	/**
	 * @return the permission
	 */
	public KimDocumentRoleResponsibility getResponsibility() {
		return this.permission;
	}

	/**
	 * @param permission the permission to set
	 */
	public void setResponsibility(KimDocumentRoleResponsibility permission) {
		this.permission = permission;
	}

}
