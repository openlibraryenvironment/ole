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
package org.kuali.rice.kim.document.rule;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.bo.ui.GroupDocumentMember;
import org.kuali.rice.kim.bo.ui.GroupDocumentQualifier;
import org.kuali.rice.kim.document.IdentityManagementGroupDocument;
import org.kuali.rice.kim.framework.services.KimFrameworkServiceLocator;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.rule.event.ui.AddGroupMemberEvent;
import org.kuali.rice.kim.rule.ui.AddGroupMemberRule;
import org.kuali.rice.kim.rules.ui.GroupDocumentMemberRule;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.kns.rules.TransactionalDocumentRuleBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.MessageMap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class IdentityManagementGroupDocumentRule extends TransactionalDocumentRuleBase implements AddGroupMemberRule {

	protected AddGroupMemberRule addGroupMemberRule;
	protected AttributeValidationHelper attributeValidationHelper = new AttributeValidationHelper();
	
	protected BusinessObjectService businessObjectService;
	protected Class<? extends GroupDocumentMemberRule> addGroupMemberRuleClass = GroupDocumentMemberRule.class;

	protected IdentityService identityService; 
	
    public IdentityService getIdentityService() {
        if ( identityService == null) {
            identityService = KimApiServiceLocator.getIdentityService();
        }
        return identityService;
    }

    @Override
    protected boolean processCustomSaveDocumentBusinessRules(Document document) {
        if (!(document instanceof IdentityManagementGroupDocument)) {
            return false;
        }

        IdentityManagementGroupDocument groupDoc = (IdentityManagementGroupDocument)document;

        boolean valid = true;
        GlobalVariables.getMessageMap().addToErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);
        valid &= validAssignGroup(groupDoc);
        valid &= validDuplicateGroupName(groupDoc);
        getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(document, getMaxDictionaryValidationDepth(), true, false);
        valid &= validateGroupQualifier(groupDoc.getQualifiers(), groupDoc.getKimType());
        valid &= validGroupMemberActiveDates(groupDoc.getMembers());
        //KULRICE-6858 Validate group members are in identity system
        valid &= validGroupMemberPrincipalIDs(groupDoc.getMembers());
        GlobalVariables.getMessageMap().removeFromErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);

        return valid;
    }
    
	protected boolean validAssignGroup(IdentityManagementGroupDocument document){
        boolean rulePassed = true;
        Map<String,String> additionalPermissionDetails = new HashMap<String,String>();
        additionalPermissionDetails.put(KimConstants.AttributeConstants.NAMESPACE_CODE, document.getGroupNamespace());
        additionalPermissionDetails.put(KimConstants.AttributeConstants.GROUP_NAME, document.getGroupName());
		if(document.getMembers()!=null && document.getMembers().size()>0){
			if(!getDocumentDictionaryService().getDocumentAuthorizer(document).isAuthorizedByTemplate(
					document, KimConstants.NAMESPACE_CODE, KimConstants.PermissionTemplateNames.POPULATE_GROUP,
					GlobalVariables.getUserSession().getPrincipalId(), additionalPermissionDetails, null)){
	    		GlobalVariables.getMessageMap().putError("document.groupName", 
	    				RiceKeyConstants.ERROR_ASSIGN_GROUP, 
	    				new String[] {document.getGroupNamespace(), document.getGroupName()});
	            rulePassed = false;
			}
		}
		return rulePassed;
	}

    @SuppressWarnings("unchecked")
	protected boolean validDuplicateGroupName(IdentityManagementGroupDocument groupDoc){
        Group group = null;
        if(null != groupDoc.getGroupNamespace() && null != groupDoc.getGroupName()){
            group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(
                groupDoc.getGroupNamespace(), groupDoc.getGroupName());
        }
        boolean rulePassed = true;
    	if(group!=null){
    		if(group.getId().equals(groupDoc.getGroupId())) {
    			rulePassed = true;
            }
    		else{
	    		GlobalVariables.getMessageMap().putError("document.groupName", 
	    				RiceKeyConstants.ERROR_DUPLICATE_ENTRY, new String[] {"Group Name"});
	    		rulePassed = false;
    		}
    	}
    	return rulePassed;
    }
    
    protected boolean validGroupMemberActiveDates(List<GroupDocumentMember> groupMembers) {
    	boolean valid = true;
		int i = 0;
    	for(GroupDocumentMember groupMember: groupMembers) {
   			valid &= validateActiveDate("document.members["+i+"].activeToDate", groupMember.getActiveFromDate(), groupMember.getActiveToDate());
    		i++;
    	}
    	return valid;
    }

    protected boolean validGroupMemberPrincipalIDs(List<GroupDocumentMember> groupMembers) {
        boolean valid = true;
        List<String> principalIds = new ArrayList<String>();
        for(GroupDocumentMember groupMember: groupMembers) {
            if (StringUtils.equals(groupMember.getMemberTypeCode(), KimConstants.KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.getCode()) ) {
                principalIds.add(groupMember.getMemberId());
            }
        }
        if(!principalIds.isEmpty())       {
            // retrieve valid principals/principal-ids from identity service
            List<Principal> validPrincipals = getIdentityService().getPrincipals(principalIds);
            List<String> validPrincipalIds = new ArrayList<String>();
            for (Principal principal : validPrincipals) {
                validPrincipalIds.add(principal.getPrincipalId());
            }
            // check that there are no invalid principals in the principal list, return false
            List<String> invalidPrincipalIds = new ArrayList<String>(CollectionUtils.subtract(principalIds, validPrincipalIds));
            // if list is not empty add error messages and return false
            if(CollectionUtils.isNotEmpty(invalidPrincipalIds)) {
                GlobalVariables.getMessageMap().putError("document.member.memberId", RiceKeyConstants.ERROR_MEMBERID_MEMBERTYPE_MISMATCH,
                        invalidPrincipalIds.toArray(new String[invalidPrincipalIds.size()]));
                valid = false;
            }
        }
        return valid;
    }

    protected boolean validateGroupQualifier(List<GroupDocumentQualifier> groupQualifiers, KimType kimType){
		List<RemotableAttributeError> validationErrors = new ArrayList<RemotableAttributeError>();

		List<RemotableAttributeError> errorsTemp;
		Map<String, String> mapToValidate;
        KimTypeService kimTypeService = KimFrameworkServiceLocator.getKimTypeService(kimType);
        GlobalVariables.getMessageMap().removeFromErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);
		mapToValidate = attributeValidationHelper.convertQualifiersToMap(groupQualifiers);
		errorsTemp = kimTypeService.validateAttributes(kimType.getId(), mapToValidate);
		validationErrors.addAll(attributeValidationHelper.convertErrors("",
                attributeValidationHelper.convertQualifiersToAttrIdxMap(groupQualifiers), errorsTemp));
		GlobalVariables.getMessageMap().addToErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);
		
    	if (validationErrors.isEmpty()) {
    		return true;
    	} 
    	attributeValidationHelper.moveValidationErrorsToErrorMap(validationErrors);
    	return false;
    }
    
	protected boolean validateActiveDate(String errorPath, Timestamp activeFromDate, Timestamp activeToDate) {
		// TODO : do not have detail bus rule yet, so just check this for now.
		boolean valid = true;
		if (activeFromDate != null && activeToDate !=null && activeToDate.before(activeFromDate)) {
	        MessageMap errorMap = GlobalVariables.getMessageMap();
            errorMap.putError(errorPath, RiceKeyConstants.ERROR_ACTIVE_TO_DATE_BEFORE_FROM_DATE);
            valid = false;
			
		}
		return valid;
	}
	
	/**
	 * @return the addGroupMemberRule
	 */
	public AddGroupMemberRule getAddGroupMemberRule() {
		if(addGroupMemberRule == null){
			try {
				addGroupMemberRule = addGroupMemberRuleClass.newInstance();
			} catch ( Exception ex ) {
				throw new RuntimeException( "Unable to create AddMemberRule instance using class: " + addGroupMemberRuleClass, ex );
			}
		}
		return addGroupMemberRule;
	}

    public boolean processAddGroupMember(AddGroupMemberEvent addGroupMemberEvent) {
        return new GroupDocumentMemberRule().processAddGroupMember(addGroupMemberEvent);    
    }

    /**
	 * @return the businessObjectService
	 */
	public BusinessObjectService getBusinessObjectService() {
		if(businessObjectService == null){
			businessObjectService = KRADServiceLocator.getBusinessObjectService();
		}
		return businessObjectService;
	}

}
