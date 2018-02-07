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
import org.kuali.rice.kim.bo.ui.PersonDocumentGroup;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.rule.event.ui.AddGroupEvent;
import org.kuali.rice.kim.rule.ui.AddGroupRule;
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
public class PersonDocumentGroupRule extends DocumentRuleBase implements AddGroupRule {
    protected static final String NEW_GROUP = "newGroup";
    protected static final String GROUP_ID_ERROR_PATH = NEW_GROUP+".groupId";

	public boolean processAddGroup(AddGroupEvent addGroupEvent) {
		IdentityManagementPersonDocument document = (IdentityManagementPersonDocument)addGroupEvent.getDocument();
		PersonDocumentGroup newGroup = addGroupEvent.getGroup();
	    boolean rulePassed = true;
	    rulePassed = validAssignGroup(document, newGroup);
//    	List<String> groupIds = KimImplServiceLocator.getUiDocumentService().getPopulatableGroupIds();

        if (newGroup == null || StringUtils.isBlank(newGroup.getGroupId())) {
            rulePassed = false;
            GlobalVariables.getMessageMap().putError(GROUP_ID_ERROR_PATH, RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Group"});
        	
        } else {
		    for (PersonDocumentGroup group : document.getGroups()) {
		    	if (group.getGroupId().equals(newGroup.getGroupId())) {
		            rulePassed = false;
		            GlobalVariables.getMessageMap().putError(GROUP_ID_ERROR_PATH, RiceKeyConstants.ERROR_DUPLICATE_ENTRY, new String[] {"Group"});
		    		
		    	}
		    }
        }
        
//        if (rulePassed) {
//        	if (groupIds.isEmpty() || !groupIds.contains(newGroup.getGroupId())) {
//                errorMap.putError(errorPath+".groupId", RiceKeyConstants.ERROR_POPULATE_GROUP, new String[] {newGroup.getGroupId()});
//                rulePassed = false;
//        	}   
//        }
        // check it before save ??
        //rulePassed &= validateActiveDate(newGroup.getActiveFromDate(), newGroup.getActiveToDate());
		return rulePassed;
	} 

	protected boolean validAssignGroup(IdentityManagementPersonDocument document, PersonDocumentGroup newGroup){
        boolean rulePassed = true;
        Map<String,String> additionalPermissionDetails = new HashMap<String,String>();
        additionalPermissionDetails.put(KimConstants.AttributeConstants.NAMESPACE_CODE, newGroup.getNamespaceCode());
        additionalPermissionDetails.put(KimConstants.AttributeConstants.GROUP_NAME, newGroup.getGroupName());
		if(!getDocumentDictionaryService().getDocumentAuthorizer(document).isAuthorizedByTemplate(
				document, KimConstants.NAMESPACE_CODE, KimConstants.PermissionTemplateNames.POPULATE_GROUP,
				GlobalVariables.getUserSession().getPrincipalId(), additionalPermissionDetails, null)){
    		GlobalVariables.getMessageMap().putError(GROUP_ID_ERROR_PATH, 
    				RiceKeyConstants.ERROR_ASSIGN_GROUP, 
    				new String[] {newGroup.getNamespaceCode(), newGroup.getGroupName()});
            rulePassed = false;
		}
		return rulePassed;
	}
	
}
