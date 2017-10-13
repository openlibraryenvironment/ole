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
package org.kuali.rice.kim.rules.ui;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMember;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.document.rule.AttributeValidationHelper;
import org.kuali.rice.kim.rule.event.ui.AddPersonDelegationMemberEvent;
import org.kuali.rice.kim.rule.ui.AddPersonDelegationMemberRule;
import org.kuali.rice.kns.rules.DocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PersonDocumentDelegationMemberRule extends DocumentRuleBase implements AddPersonDelegationMemberRule {

	public static final String ERROR_PATH = "document.newDelegationMember";

	protected AttributeValidationHelper attributeValidationHelper = new AttributeValidationHelper();
	
	public boolean processAddPersonDelegationMember(AddPersonDelegationMemberEvent addPersonDelegationMemberEvent){
		RoleDocumentDelegationMember newDelegationMember = addPersonDelegationMemberEvent.getDelegationMember();
		IdentityManagementPersonDocument document = (IdentityManagementPersonDocument)addPersonDelegationMemberEvent.getDocument();
	    boolean rulePassed = true;
        if(newDelegationMember == null){
            GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Delegation Member"});
            return false;
        }
        if(StringUtils.isBlank(newDelegationMember.getRoleMemberId())){
            GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Role Member"});
            return false;
        }
		return rulePassed;
	} 

}
