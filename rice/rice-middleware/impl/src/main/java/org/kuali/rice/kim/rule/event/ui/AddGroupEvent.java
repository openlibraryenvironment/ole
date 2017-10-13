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

import org.kuali.rice.kim.bo.ui.PersonDocumentGroup;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.rule.ui.AddGroupRule;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEventBase;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class AddGroupEvent extends KualiDocumentEventBase {
	private PersonDocumentGroup group;

	public AddGroupEvent(String errorPathPrefix, IdentityManagementPersonDocument document) {
        super("adding group document " + getDocumentId(document), errorPathPrefix, document);
    }

    public AddGroupEvent(String errorPathPrefix, Document document, PersonDocumentGroup group) {
        this(errorPathPrefix, (IdentityManagementPersonDocument) document);
        this.group = group;
    }

    public Class<? extends BusinessRule> getRuleInterfaceClass() {
        return AddGroupRule.class;
    }

    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((AddGroupRule) rule).processAddGroup(this);
    }

	public PersonDocumentGroup getGroup() {
		return this.group;
	}

	public void setGroup(PersonDocumentGroup group) {
		this.group = group;
	}

}
