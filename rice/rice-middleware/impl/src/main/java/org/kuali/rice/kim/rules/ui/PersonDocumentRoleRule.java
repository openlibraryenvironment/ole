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
import org.kuali.rice.kim.bo.ui.PersonDocumentRole;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.rule.event.ui.AddRoleEvent;
import org.kuali.rice.kim.rule.ui.AddRoleRule;
import org.kuali.rice.kns.rules.DocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.util.CollectionUtils;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PersonDocumentRoleRule extends DocumentRuleBase implements AddRoleRule {
    public static final String ERROR_PATH = "newRole.roleId";

	public boolean processAddRole(AddRoleEvent addRoleEvent) {
		PersonDocumentRole newRole = addRoleEvent.getRole();
		IdentityManagementPersonDocument document = (IdentityManagementPersonDocument)addRoleEvent.getDocument();
		boolean rulePassed = true;
//    	List<String> roleIds = KimImplServiceLocator.getUiDocumentService().getAssignableRoleIds();

        if (newRole == null || StringUtils.isBlank(newRole.getRoleId())) {
            rulePassed = false;
            GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Role"});
        	
//        } else if (roleIds.isEmpty() || !roleIds.contains(newRole.getRoleId())) {
//            errorMap.putError(ERROR_PATH, RiceKeyConstants.ERROR_ASSIGN_ROLE, new String[] {newRole.getRoleId()});
//            rulePassed = false;
        } else {
		    for (PersonDocumentRole role : document.getRoles()) {
		    	if (role.getRoleId().equals(newRole.getRoleId())) {
		            rulePassed = false;
		            GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_DUPLICATE_ENTRY, new String[] {"Role"});
		    		
		    	}
		    }
        }

        // KULRICE-7930  Check for field validation errors
        if(!CollectionUtils.isEmpty(GlobalVariables.getMessageMap().getErrorMessagesForProperty("newRole.*", true))) {
            rulePassed = false;
        }

		return rulePassed;
	} 

}
