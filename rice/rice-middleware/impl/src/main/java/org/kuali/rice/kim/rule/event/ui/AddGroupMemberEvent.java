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

import org.kuali.rice.kim.bo.ui.GroupDocumentMember;
import org.kuali.rice.kim.document.IdentityManagementGroupDocument;
import org.kuali.rice.kim.rule.ui.AddGroupMemberRule;
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
public class AddGroupMemberEvent extends KualiDocumentEventBase {
	private GroupDocumentMember member;

	public AddGroupMemberEvent(String errorPathPrefix, IdentityManagementGroupDocument document) {
        super("Adding Group Member Document " + getDocumentId(document), errorPathPrefix, document);
    }

    public AddGroupMemberEvent(String errorPathPrefix, Document document, GroupDocumentMember member) {
        this(errorPathPrefix, (IdentityManagementGroupDocument) document);
        this.member = (GroupDocumentMember) ObjectUtils.deepCopy(member);
    }

    public Class<? extends BusinessRule> getRuleInterfaceClass() {
        return AddGroupMemberRule.class;
    }

    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((AddGroupMemberRule) rule).processAddGroupMember(this);
    }

	/**
	 * @return the member
	 */
	public GroupDocumentMember getMember() {
		return this.member;
	}

	/**
	 * @param member the member to set
	 */
	public void setMember(GroupDocumentMember member) {
		this.member = member;
	}

}
