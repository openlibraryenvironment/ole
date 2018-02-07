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
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.bo.ui.KimDocumentRolePermission;
import org.kuali.rice.kim.document.IdentityManagementRoleDocument;
import org.kuali.rice.kim.rule.event.ui.AddPermissionEvent;
import org.kuali.rice.kim.rule.ui.AddPermissionRule;
import org.kuali.rice.kns.rules.DocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KimDocumentPermissionRule extends DocumentRuleBase implements AddPermissionRule {

	public static final String ERROR_PATH = "document.permission.permissionId";
	
	public boolean processAddPermission(AddPermissionEvent addPermissionEvent) {
		KimDocumentRolePermission newPermission = addPermissionEvent.getPermission();
		if(newPermission==null || StringUtils.isEmpty(newPermission.getPermissionId())){
			GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Permission"});
			return false;
		}

		Permission kimPermissionInfo = newPermission.getPermission();
		if(kimPermissionInfo==null){
			GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Permission"});
			return false;
		}
	    boolean rulePassed = true;
		IdentityManagementRoleDocument document = (IdentityManagementRoleDocument)addPermissionEvent.getDocument();
		if(!hasPermissionToGrantPermission(kimPermissionInfo, document)){
	        GlobalVariables.getMessageMap().putError(KimDocumentPermissionRule.ERROR_PATH, RiceKeyConstants.ERROR_ASSIGN_PERMISSION, 
	        		new String[] {kimPermissionInfo.getNamespaceCode(), kimPermissionInfo.getTemplate().getName()});
	        return false;
		}

		if (newPermission == null || StringUtils.isBlank(newPermission.getPermissionId())) {
            rulePassed = false;
            GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Permission"});
        } else {
		    int i = 0;
        	for (KimDocumentRolePermission permission: document.getPermissions()) {
		    	if (permission.getPermissionId().equals(newPermission.getPermissionId())) {
		            rulePassed = false;
		            GlobalVariables.getMessageMap().putError("document.permissions["+i+"].permissionId", RiceKeyConstants.ERROR_DUPLICATE_ENTRY, new String[] {"Permission"});
		    	}
		    	i++;
		    }
        }
		return rulePassed;
	} 

	public boolean hasPermissionToGrantPermission(Permission kimPermissionInfo , IdentityManagementRoleDocument document){
		Map<String,String> permissionDetails = new HashMap<String,String>();
		permissionDetails.put(KimConstants.AttributeConstants.NAMESPACE_CODE, kimPermissionInfo.getNamespaceCode());
		permissionDetails.put(KimConstants.AttributeConstants.PERMISSION_NAME, kimPermissionInfo.getTemplate().getName());
		if (!getDocumentDictionaryService().getDocumentAuthorizer(document).isAuthorizedByTemplate(
				document, 
				KimConstants.NAMESPACE_CODE, 
				KimConstants.PermissionTemplateNames.GRANT_PERMISSION,
				GlobalVariables.getUserSession().getPerson().getPrincipalId(), 
				permissionDetails, null)) {
	        return false;
		}
		return true;
	}
	
}
