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
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.bo.ui.GroupDocumentMember;
import org.kuali.rice.kim.document.IdentityManagementGroupDocument;
import org.kuali.rice.kim.rule.event.ui.AddGroupMemberEvent;
import org.kuali.rice.kim.rule.ui.AddGroupMemberRule;
import org.kuali.rice.kns.rules.DocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.Map;

//import org.kuali.rice.kim.api.group.GroupServiceBase;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class GroupDocumentMemberRule extends DocumentRuleBase implements AddGroupMemberRule {

	private static final String ERROR_PATH = "document.member.memberId";

	public boolean processAddGroupMember(AddGroupMemberEvent addGroupMemberEvent){
		GroupDocumentMember newMember = addGroupMemberEvent.getMember();
		IdentityManagementGroupDocument document = (IdentityManagementGroupDocument)addGroupMemberEvent.getDocument();
	    boolean rulePassed = true;

        if (newMember == null || StringUtils.isBlank(newMember.getMemberId())){
            GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Member"});
            return false;
        }
    	if(!validAssignGroup(newMember, document))
    		return false;

	    int i = 0;
	    for (GroupDocumentMember member: document.getMembers()){
	    	if (member.getMemberId().equals(newMember.getMemberId()) && member.getMemberTypeCode().equals(newMember.getMemberTypeCode())){
	            rulePassed = false;
	            GlobalVariables.getMessageMap().putError("document.members["+i+"].memberId", RiceKeyConstants.ERROR_DUPLICATE_ENTRY, new String[] {"Member"});
	    	}
	    	i++;
	    }
	    
	    // check for circular reference
		GroupService groupService = KimApiServiceLocator.getGroupService();
		if (groupService.isGroupMemberOfGroup(document.getGroupId(),newMember.getMemberId())){
            GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_ASSIGN_GROUP_MEMBER_CIRCULAR, new String[] {newMember.getMemberId()});
			return false;
		}
	    
		return rulePassed;
	} 

	protected boolean validAssignGroup(GroupDocumentMember groupMember, IdentityManagementGroupDocument document){
        boolean rulePassed = true;
		if(StringUtils.isNotEmpty(document.getGroupNamespace())){
			Map<String,String> roleDetails = new HashMap<String,String>();
			roleDetails.put(KimConstants.AttributeConstants.NAMESPACE_CODE, document.getGroupNamespace());
			roleDetails.put(KimConstants.AttributeConstants.GROUP_NAME, document.getGroupName());
			if (!getDocumentDictionaryService().getDocumentAuthorizer(document).isAuthorizedByTemplate(
					document, 
					KimConstants.NAMESPACE_CODE, 
					KimConstants.PermissionTemplateNames.POPULATE_GROUP,
					GlobalVariables.getUserSession().getPerson().getPrincipalId(), 
					roleDetails, null)){
	            GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_ASSIGN_GROUP, 
	            		new String[] {document.getGroupNamespace(), document.getGroupName()});
	            rulePassed = false;
			}
		}
		return rulePassed;
	}

}
