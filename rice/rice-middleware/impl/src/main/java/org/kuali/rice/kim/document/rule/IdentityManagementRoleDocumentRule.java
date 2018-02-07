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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.VersionHelper;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.api.responsibility.ResponsibilityService;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimAttributeField;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.bo.ui.KimDocumentRolePermission;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleQualifier;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleResponsibility;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleResponsibilityAction;
import org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMember;
import org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMemberQualifier;
import org.kuali.rice.kim.document.IdentityManagementRoleDocument;
import org.kuali.rice.kim.framework.role.RoleTypeService;
import org.kuali.rice.kim.framework.services.KimFrameworkServiceLocator;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeBo;
import org.kuali.rice.kim.impl.responsibility.AddResponsibilityEvent;
import org.kuali.rice.kim.impl.responsibility.AddResponsibilityRule;
import org.kuali.rice.kim.impl.responsibility.KimDocumentResponsibilityRule;
import org.kuali.rice.kim.impl.responsibility.ResponsibilityBo;
import org.kuali.rice.kim.impl.responsibility.ResponsibilityInternalService;
import org.kuali.rice.kim.impl.services.KimImplServiceLocator;
import org.kuali.rice.kim.impl.type.KimTypeLookupableHelperServiceImpl;
import org.kuali.rice.kim.rule.event.ui.AddDelegationEvent;
import org.kuali.rice.kim.rule.event.ui.AddDelegationMemberEvent;
import org.kuali.rice.kim.rule.event.ui.AddMemberEvent;
import org.kuali.rice.kim.rule.event.ui.AddPermissionEvent;
import org.kuali.rice.kim.rule.ui.AddDelegationMemberRule;
import org.kuali.rice.kim.rule.ui.AddDelegationRule;
import org.kuali.rice.kim.rule.ui.AddMemberRule;
import org.kuali.rice.kim.rule.ui.AddPermissionRule;
import org.kuali.rice.kim.rules.ui.KimDocumentMemberRule;
import org.kuali.rice.kim.rules.ui.KimDocumentPermissionRule;
import org.kuali.rice.kim.rules.ui.RoleDocumentDelegationMemberRule;
import org.kuali.rice.kim.rules.ui.RoleDocumentDelegationRule;
import org.kuali.rice.kns.kim.type.DataDictionaryTypeServiceHelper;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.kns.rules.TransactionalDocumentRuleBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.MessageMap;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceBus;

import javax.xml.namespace.QName;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class IdentityManagementRoleDocumentRule extends TransactionalDocumentRuleBase implements AddPermissionRule, AddResponsibilityRule, AddMemberRule, AddDelegationRule, AddDelegationMemberRule {
//	protected static final Logger LOG = Logger.getLogger( IdentityManagementRoleDocumentRule.class );

    public static final int PRIORITY_NUMBER_MIN_VALUE = 1;
    public static final int PRIORITY_NUMBER_MAX_VALUE = 11;

	protected AddResponsibilityRule addResponsibilityRule;
	protected AddPermissionRule  addPermissionRule;
	protected AddMemberRule  addMemberRule;
	protected AddDelegationRule addDelegationRule;
	protected AddDelegationMemberRule addDelegationMemberRule;
	protected BusinessObjectService businessObjectService;
	protected ResponsibilityService responsibilityService;
	protected Class<? extends AddResponsibilityRule> addResponsibilityRuleClass = KimDocumentResponsibilityRule.class;
	protected Class<? extends AddPermissionRule> addPermissionRuleClass = KimDocumentPermissionRule.class;
	protected Class<? extends AddMemberRule> addMemberRuleClass = KimDocumentMemberRule.class;
	protected Class<? extends AddDelegationRule> addDelegationRuleClass = RoleDocumentDelegationRule.class;
	protected Class<? extends AddDelegationMemberRule> addDelegationMemberRuleClass = RoleDocumentDelegationMemberRule.class;

	protected IdentityService identityService;
	private static ResponsibilityInternalService responsibilityInternalService;

	protected AttributeValidationHelper attributeValidationHelper = new AttributeValidationHelper();

	// KULRICE-4153
	protected ActiveRoleMemberHelper activeRoleMemberHelper = new ActiveRoleMemberHelper();

    public IdentityService getIdentityService() {
        if ( identityService == null) {
            identityService = KimApiServiceLocator.getIdentityService();
        }
        return identityService;
    }

    @Override
    protected boolean processCustomSaveDocumentBusinessRules(Document document) {
        if (!(document instanceof IdentityManagementRoleDocument)) {
            return false;
        }

        IdentityManagementRoleDocument roleDoc = (IdentityManagementRoleDocument)document;

        boolean valid = true;
        boolean validateRoleAssigneesAndDelegations = !KimTypeLookupableHelperServiceImpl
                .hasDerivedRoleTypeService(roleDoc.getKimType());
        GlobalVariables.getMessageMap().addToErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);
        valid &= validDuplicateRoleName(roleDoc);
        valid &= validPermissions(roleDoc);
        valid &= validResponsibilities(roleDoc);
        //KULRICE-6858 Validate group members are in identity system
        valid &= validRoleMemberPrincipalIDs(roleDoc.getModifiedMembers());
        getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(document, getMaxDictionaryValidationDepth(), true, false);
        validateRoleAssigneesAndDelegations &= validAssignRole(roleDoc);
        if(validateRoleAssigneesAndDelegations){
	        //valid &= validAssignRole(roleDoc);
        	// KULRICE-4153 & KULRICE-4818
        	// Use the Active Role Member Helper to retrieve only those Role Members & Delegation member that are active
        	// If a member or delegation is active on a Role, and they have an inactive Role Qualifier, Role Qualifier validation will fail
        	// If a member or delegation is inactive on a Role, and they have an inactive Role Qualifier, Role Qualifier validation will pass
            List<KimDocumentRoleMember> activeRoleMembers = activeRoleMemberHelper.getActiveRoleMembers(roleDoc.getMembers());
            List<KimDocumentRoleMember> newActiveRoleMembers = activeRoleMemberHelper.getActiveRoleMembers(roleDoc.getModifiedMembers());
            List<RoleDocumentDelegationMember> activeRoleDelegationMembers = activeRoleMemberHelper.getActiveDelegationRoleMembers(roleDoc.getDelegationMembers());

            valid &= validateRoleQualifier(newActiveRoleMembers, roleDoc.getKimType());
	        valid &= validRoleMemberActiveDates(roleDoc.getModifiedMembers());
	        valid &= validateDelegationMemberRoleQualifier(newActiveRoleMembers, activeRoleDelegationMembers, roleDoc.getKimType(), activeRoleMembers);
	        valid &= validDelegationMemberActiveDates(roleDoc.getDelegationMembers());
	        valid &= validRoleMembersResponsibilityActions(roleDoc.getModifiedMembers());
        }
        valid &= validRoleResponsibilitiesActions(roleDoc.getResponsibilities());
        GlobalVariables.getMessageMap().removeFromErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);

        return valid;
    }

	protected boolean validAssignRole(IdentityManagementRoleDocument document){
        boolean rulePassed = true;
        Map<String,String> additionalPermissionDetails = new HashMap<String,String>();
        additionalPermissionDetails.put(KimConstants.AttributeConstants.NAMESPACE_CODE, document.getRoleNamespace());
        additionalPermissionDetails.put(KimConstants.AttributeConstants.ROLE_NAME, document.getRoleName());
		if((document.getMembers()!=null && document.getMembers().size()>0) ||
				(document.getDelegationMembers()!=null && document.getDelegationMembers().size()>0)){
			if(!getDocumentDictionaryService().getDocumentAuthorizer(document).isAuthorizedByTemplate(
					document, KimConstants.NAMESPACE_CODE, KimConstants.PermissionTemplateNames.ASSIGN_ROLE,
					GlobalVariables.getUserSession().getPrincipalId(), additionalPermissionDetails, null)){
	            rulePassed = false;
			}
		}
		return rulePassed;
	}

    protected boolean validRoleMemberPrincipalIDs(List<KimDocumentRoleMember> roleMembers) {
        boolean valid = true;
        List<String> principalIds = new ArrayList<String>();
        for(KimDocumentRoleMember roleMember: roleMembers) {
            if (StringUtils.equals(roleMember.getMemberTypeCode(), KimConstants.KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.getCode()) ) {
                principalIds.add(roleMember.getMemberId());
            }
        }
        if(!principalIds.isEmpty())       {
            List<Principal> validPrincipals = getIdentityService().getPrincipals(principalIds);
            for(KimDocumentRoleMember roleMember: roleMembers) {
                if (StringUtils.equals(roleMember.getMemberTypeCode(), MemberType.PRINCIPAL.getCode()) ) {
                    boolean validPrincipalId = false;
                    if(validPrincipals != null && !validPrincipals.isEmpty())       {
                        for(Principal validPrincipal: validPrincipals) {
                            if(roleMember.getMemberId().equals(validPrincipal.getPrincipalId()))     {
                             validPrincipalId = true;
                            }
                        }
                    }
                    if(!validPrincipalId) {
                        GlobalVariables.getMessageMap().putError("document.member.memberId", RiceKeyConstants.ERROR_MEMBERID_MEMBERTYPE_MISMATCH,
                                new String[] {roleMember.getMemberId()});
                        valid = false;
                    }
                }
            }
        }
        return valid;
    }

    @SuppressWarnings("unchecked")
	protected boolean validDuplicateRoleName(IdentityManagementRoleDocument roleDoc){
        Role role = KimApiServiceLocator.getRoleService().getRoleByNamespaceCodeAndName(roleDoc.getRoleNamespace(),
                roleDoc.getRoleName());
    	boolean rulePassed = true;
    	if(role!=null){
    		if(role.getId().equals(roleDoc.getRoleId())) {
                rulePassed = true;
            }
    		else{
	    		GlobalVariables.getMessageMap().putError("document.roleName",
	    				RiceKeyConstants.ERROR_DUPLICATE_ENTRY, new String[] {"Role Name"});
	    		rulePassed = false;
    		}
    	}
    	return rulePassed;
    }

    protected boolean validRoleMemberActiveDates(List<KimDocumentRoleMember> roleMembers) {
    	boolean valid = true;
		int i = 0;
    	for(KimDocumentRoleMember roleMember: roleMembers) {
   			valid &= validateActiveDate("document.members["+i+"].activeToDate", roleMember.getActiveFromDate(), roleMember.getActiveToDate());
    		i++;
    	}
    	return valid;
    }

    protected boolean validDelegationMemberActiveDates(List<RoleDocumentDelegationMember> delegationMembers) {
    	boolean valid = true;
		int i = 0;
    	for(RoleDocumentDelegationMember delegationMember: delegationMembers) {
   			valid &= validateActiveDate("document.delegationMembers[" + i + "].activeToDate",
                       delegationMember.getActiveFromDate(), delegationMember.getActiveToDate());
    		i++;
    	}
    	return valid;
    }

    protected boolean validPermissions(IdentityManagementRoleDocument document){
    	Permission kimPermissionInfo;
    	boolean valid = true;
    	int i = 0;
    	for(KimDocumentRolePermission permission: document.getPermissions()){
    		kimPermissionInfo = permission.getPermission();
    		if(!permission.isActive() && !hasPermissionToGrantPermission(permission.getPermission(), document)){
    	        GlobalVariables.getMessageMap().putError("permissions["+i+"].active", RiceKeyConstants.ERROR_ASSIGN_PERMISSION,
    	        		new String[] {kimPermissionInfo.getNamespaceCode(), kimPermissionInfo.getTemplate().getName()});
    	        valid = false;
    		}
    		i++;
    	}
    	return valid;
    }

    protected boolean validResponsibilities(IdentityManagementRoleDocument document){
    	ResponsibilityBo kimResponsibilityImpl;
    	boolean valid = true;
    	int i = 0;
    	for(KimDocumentRoleResponsibility responsibility: document.getResponsibilities()){
    		kimResponsibilityImpl = responsibility.getKimResponsibility();
    		if(!responsibility.isActive() && !hasPermissionToGrantResponsibility(ResponsibilityBo.to(responsibility.getKimResponsibility()), document)){
    	        GlobalVariables.getMessageMap().putError("responsibilities["+i+"].active", RiceKeyConstants.ERROR_ASSIGN_RESPONSIBILITY,
    	        		new String[] {kimResponsibilityImpl.getNamespaceCode(), kimResponsibilityImpl.getTemplate().getName()});
    	        valid = false;
    		}
    		i++;
    	}
    	return valid;
    }

    protected boolean validRoleResponsibilitiesActions(List<KimDocumentRoleResponsibility> roleResponsibilities){
        int i = 0;
        boolean rulePassed = true;
    	for(KimDocumentRoleResponsibility roleResponsibility: roleResponsibilities){
    		if(!getResponsibilityInternalService().areActionsAtAssignmentLevelById(roleResponsibility.getResponsibilityId())) {
    			validateRoleResponsibilityAction("document.responsibilities["+i+"].roleRspActions[0].priorityNumber", roleResponsibility.getRoleRspActions().get(0));
            }
        	i++;
    	}
    	return rulePassed;
    }

    protected boolean validRoleMembersResponsibilityActions(List<KimDocumentRoleMember> roleMembers){
        int i = 0;
        int j;
        boolean rulePassed = true;
    	for(KimDocumentRoleMember roleMember: roleMembers){
    		j = 0;
    		if(roleMember.getRoleRspActions()!=null && !roleMember.getRoleRspActions().isEmpty()){
	    		for(KimDocumentRoleResponsibilityAction roleRspAction: roleMember.getRoleRspActions()){
	    			validateRoleResponsibilityAction("document.members["+i+"].roleRspActions["+j+"].priorityNumber", roleRspAction);
		        	j++;
	    		}
    		}
    		i++;
    	}
    	return rulePassed;
    }

    protected boolean validateRoleResponsibilityAction(String errorPath, KimDocumentRoleResponsibilityAction roleRspAction){
    	boolean rulePassed = true;
    	/*if(StringUtils.isBlank(roleRspAction.getActionPolicyCode())){
    		GlobalVariables.getErrorMap().putError(errorPath,
    				RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Action Policy Code"});
    		rulePassed = false;
    	}
    	if(roleRspAction.getPriorityNumber()==null){
    		GlobalVariables.getErrorMap().putError(errorPath,
    				RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Priority Number"});
    		rulePassed = false;
    	}
    	if(StringUtils.isBlank(roleRspAction.getActionTypeCode())){
    		GlobalVariables.getErrorMap().putError(errorPath,
    				RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Action Type Code"});
    		rulePassed = false;
    	}*/
    	if(roleRspAction.getPriorityNumber()!=null &&
    			(roleRspAction.getPriorityNumber()<PRIORITY_NUMBER_MIN_VALUE
    					|| roleRspAction.getPriorityNumber()>PRIORITY_NUMBER_MAX_VALUE)){
    		GlobalVariables.getMessageMap().putError(errorPath,
   				RiceKeyConstants.ERROR_PRIORITY_NUMBER_RANGE, new String[] {PRIORITY_NUMBER_MIN_VALUE+"", PRIORITY_NUMBER_MAX_VALUE+""});
    		rulePassed = false;
    	}

    	return rulePassed;
    }

    protected boolean validateRoleQualifier(List<KimDocumentRoleMember> roleMembers, KimType kimType){
		List<RemotableAttributeError> validationErrors = new ArrayList<RemotableAttributeError>();

		int memberCounter = 0;
		int roleMemberCount = 0;
		List<RemotableAttributeError> errorsTemp;
		Map<String, String> mapToValidate;
        KimTypeService kimTypeService = KimFrameworkServiceLocator.getKimTypeService(kimType);
        GlobalVariables.getMessageMap().removeFromErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);
        final List<KimAttributeField> attributeDefinitions = kimTypeService.getAttributeDefinitions(kimType.getId());
        final Set<String> uniqueAttributeNames = figureOutUniqueQualificationSet(roleMembers, attributeDefinitions);

		for(KimDocumentRoleMember roleMember: roleMembers) {
			errorsTemp = Collections.emptyList();
			mapToValidate = attributeValidationHelper.convertQualifiersToMap(roleMember.getQualifiers());

            VersionedService<RoleTypeService> versionedRoleTypeService = getVersionedRoleTypeService(kimType);
            boolean shouldNotValidate = true;
            if (versionedRoleTypeService != null) {
                boolean versionOk = VersionHelper.compareVersion(versionedRoleTypeService.getVersion(),
                        CoreConstants.Versions.VERSION_2_1_2)!=-1? true:false;
                if(versionOk) {
                    shouldNotValidate = versionedRoleTypeService.getService().shouldValidateQualifiersForMemberType( MemberType.fromCode(roleMember.getMemberTypeCode()));
                } else {
                    shouldNotValidate = false;
                }
            }
            if(!shouldNotValidate){
				errorsTemp = kimTypeService.validateAttributes(kimType.getId(), mapToValidate);
				validationErrors.addAll(attributeValidationHelper.convertErrorsForMappedFields(
                        "members[" + memberCounter + "]", errorsTemp));
		        memberCounter++;
			}
			if (uniqueAttributeNames.size() > 0) {
				validateUniquePersonRoleQualifiersUniqueForRoleMembership(roleMember, roleMemberCount, roleMembers, uniqueAttributeNames, validationErrors);
			}

			roleMemberCount += 1;
    	}

		GlobalVariables.getMessageMap().addToErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);

    	if (validationErrors.isEmpty()) {
    		return true;
    	} 
    	attributeValidationHelper.moveValidationErrorsToErrorMap(validationErrors);
    	return false;
    }

    /**
     * Finds the names of the unique qualification attributes which this role should be checking against
     *
     * @param memberships the memberships (we take the qualification from the first)
     * @param attributeDefinitions information about the attributeDefinitions
     * @return a Set of unique attribute ids (with their indices, for error reporting)
     */
    protected Set<String> figureOutUniqueQualificationSet(List<KimDocumentRoleMember> memberships, List<KimAttributeField> attributeDefinitions) {
    	Set<String> uniqueAttributeIds = new HashSet<String>();

    	if (memberships != null && memberships.size() > 1) { // if there aren't two or more members, doing this whole check is kinda silly
    		KimDocumentRoleMember membership = memberships.get(0);

    		for (KimDocumentRoleQualifier qualifier : membership.getQualifiers()) {
        		if (qualifier != null && qualifier.getKimAttribute() != null && !StringUtils.isBlank(qualifier.getKimAttribute().getAttributeName())) {
    	    		final KimAttributeField relatedDefinition = DataDictionaryTypeServiceHelper.findAttributeField(
                            qualifier.getKimAttribute().getAttributeName(), attributeDefinitions);

    	    		if (relatedDefinition != null && relatedDefinition.isUnique()) {
    	    			uniqueAttributeIds.add(qualifier.getKimAttrDefnId()); // it's unique - add it to the Set
    	    		}
        		}
    		}
    	}

    	return uniqueAttributeIds;
    }

    /**
     * Checks all the qualifiers for the given membership, so that all qualifiers which should be unique are guaranteed to be unique
     *
     * @param membershipToCheck the membership to check
     * @param membershipToCheckIndex the index of the person's membership in the role (for error reporting purposes)
     * @param validationErrors Map<String, String> of errors to report
     * @return true if all unique values are indeed unique, false otherwise
     */
    protected boolean validateUniquePersonRoleQualifiersUniqueForRoleMembership(KimDocumentRoleMember membershipToCheck, int membershipToCheckIndex, List<KimDocumentRoleMember> memberships, Set<String> uniqueQualifierIds, List<RemotableAttributeError> validationErrors) {
    	boolean foundError = false;
    	int count = 0;

    	for (KimDocumentRoleMember membership : memberships) {
    		if (membershipToCheckIndex != count) {
    			if (sameMembership(membershipToCheck, membership)) {
    				if (sameUniqueMembershipQualifications(membershipToCheck, membership, uniqueQualifierIds)) {
    					foundError = true;
    					// add error to each qualifier which is supposed to be unique
    					int qualifierCount = 0;

    					for (KimDocumentRoleQualifier qualifier : membership.getQualifiers()) {
    						if (qualifier != null && uniqueQualifierIds.contains(qualifier.getKimAttrDefnId())) {
                                // for new member lines, KimAttribute is not preloaded
                                // make sure to load it here in order to obtain the name for use in error message
                                KimAttributeBo attr = qualifier.getKimAttribute();
                                String attrName = "<unknown>";
                                if (attr == null && qualifier.getKimAttrDefnId() != null) {
                                    attr = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(KimAttributeBo.class, qualifier.getKimAttrDefnId());
                                }
                                if (attr != null) {
                                    attrName = attr.getAttributeName();
                                }
                                validationErrors.add(RemotableAttributeError.Builder.create("document.members["+membershipToCheckIndex+"].qualifiers["+qualifierCount+"].attrVal", RiceKeyConstants.ERROR_DOCUMENT_IDENTITY_MANAGEMENT_PERSON_QUALIFIER_VALUE_NOT_UNIQUE+":"+membership.getMemberId()+";"+attrName+";"+qualifier.getAttrVal()).build());
    						}
    						qualifierCount += 1;
    					}
    				}
    			}
    		}
    		count += 1;
    	}

    	return foundError;
    }

    /**
     * Determines if two memberships represent the same member being added: that is, the two memberships have the same type code and id
     *
     * @param membershipA the first membership to check
     * @param membershipB the second membership to check
     * @return true if the two memberships represent the same member; false if they do not, or if it could not be profitably determined if the members were the same
     */
    protected boolean sameMembership(KimDocumentRoleMember membershipA, KimDocumentRoleMember membershipB) {
    	if (!StringUtils.isBlank(membershipA.getMemberTypeCode()) && !StringUtils.isBlank(membershipB.getMemberTypeCode()) && !StringUtils.isBlank(membershipA.getMemberId()) && !StringUtils.isBlank(membershipB.getMemberId())) {
    		return membershipA.getMemberTypeCode().equals(membershipB.getMemberTypeCode()) && membershipA.getMemberId().equals(membershipB.getMemberId());
    	}
    	return false;
    }

    /**
     * Given two memberships which represent the same member, do they share qualifications?
     *
     * @param membershipA the first membership to check
     * @param membershipB the second membership to check
     * @param uniqueAttributeIds the Set of attribute definition ids which should be unique
     * @return
     */
    protected boolean sameUniqueMembershipQualifications(KimDocumentRoleMember membershipA, KimDocumentRoleMember membershipB, Set<String> uniqueAttributeIds) {
    	boolean equalSoFar = true;
    	for (String kimAttributeDefinitionId : uniqueAttributeIds) {
    		final KimDocumentRoleQualifier qualifierA = membershipA.getQualifier(kimAttributeDefinitionId);
    		final KimDocumentRoleQualifier qualifierB = membershipB.getQualifier(kimAttributeDefinitionId);

    		if (qualifierA != null && qualifierB != null) {
    			equalSoFar &= (qualifierA.getAttrVal() == null && qualifierB.getAttrVal() == null) || (qualifierA.getAttrVal() == null || qualifierA.getAttrVal().equals(qualifierB.getAttrVal()));
    		}
    	}
    	return equalSoFar;
    }

    protected KimDocumentRoleMember getRoleMemberForDelegation(
    		List<KimDocumentRoleMember> roleMembers, RoleDocumentDelegationMember delegationMember, List<KimDocumentRoleMember> modifiedRoleMembers) {
    	if((roleMembers==null && modifiedRoleMembers==null) || delegationMember==null || delegationMember.getRoleMemberId()==null) { return null; }
        for(KimDocumentRoleMember roleMember: modifiedRoleMembers){
            if(delegationMember.getRoleMemberId().equals(roleMember.getRoleMemberId())) {
                return roleMember;
            }
        }
    	for(KimDocumentRoleMember roleMember: roleMembers){
    		if(delegationMember.getRoleMemberId().equals(roleMember.getRoleMemberId())) {
                return roleMember;
            }
    	}
    	return null;
    }

    protected boolean validateDelegationMemberRoleQualifier(List<KimDocumentRoleMember> modifiedRoleMembers,
    		List<RoleDocumentDelegationMember> delegationMembers, KimType kimType, List<KimDocumentRoleMember> nonModifiedRoleMembers){
		List<RemotableAttributeError> validationErrors = new ArrayList<RemotableAttributeError>();
		boolean valid;
		int memberCounter = 0;
		List<RemotableAttributeError> errorsTemp;
		Map<String, String> mapToValidate;
        KimTypeService kimTypeService = KimFrameworkServiceLocator.getKimTypeService(kimType);
        GlobalVariables.getMessageMap().removeFromErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);
        KimDocumentRoleMember roleMember;
        String errorPath;
        final List<KimAttributeField> attributeDefinitions = kimTypeService.getAttributeDefinitions(kimType.getId());
        final Set<String> uniqueQualifierAttributes = figureOutUniqueQualificationSetForDelegation(delegationMembers, attributeDefinitions);

		for(RoleDocumentDelegationMember delegationMember: delegationMembers) {
			errorPath = "delegationMembers["+memberCounter+"]";
			mapToValidate = attributeValidationHelper.convertQualifiersToMap(delegationMember.getQualifiers());
			if(!delegationMember.isRole()){
				errorsTemp = kimTypeService.validateAttributes(kimType.getId(), mapToValidate);
				validationErrors.addAll(
						attributeValidationHelper.convertErrorsForMappedFields(errorPath, errorsTemp));
			}
			roleMember = getRoleMemberForDelegation(nonModifiedRoleMembers, delegationMember, modifiedRoleMembers);
			if(roleMember==null){
				valid = false;
				GlobalVariables.getMessageMap().putError("document.delegationMembers["+memberCounter+"]", RiceKeyConstants.ERROR_DELEGATE_ROLE_MEMBER_ASSOCIATION, new String[]{});
			} else{
				errorsTemp = kimTypeService.validateUnmodifiableAttributes(
								kimType.getId(),
								attributeValidationHelper.convertQualifiersToMap(roleMember.getQualifiers()),
								mapToValidate);
				validationErrors.addAll(
						attributeValidationHelper.convertErrorsForMappedFields(errorPath, errorsTemp) );
			}
			if (uniqueQualifierAttributes.size() > 0) {
				validateUniquePersonRoleQualifiersUniqueForRoleDelegation(delegationMember, memberCounter, delegationMembers, uniqueQualifierAttributes, validationErrors);
			}
	        memberCounter++;
    	}
		GlobalVariables.getMessageMap().addToErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);
    	if (validationErrors.isEmpty()) {
    		valid = true;
    	} else {
    		attributeValidationHelper.moveValidationErrorsToErrorMap(validationErrors);
    		valid = false;
    	}
    	return valid;
    }

    /**
     * Finds the names of the unique qualification attributes which this role should be checking against
     *
     * @param memberships the memberships (we take the qualification from the first)
     * @param attributeDefinitions information about the attributeDefinitions
     * @return a Set of unique attribute ids (with their indices, for error reporting)
     */
    protected Set<String> figureOutUniqueQualificationSetForDelegation(List<RoleDocumentDelegationMember> memberships, List<KimAttributeField> attributeDefinitions) {
    	Set<String> uniqueAttributeIds = new HashSet<String>();

    	if (memberships != null && memberships.size() > 1) { // if there aren't two or more members, doing this whole check is kinda silly
    		RoleDocumentDelegationMember membership = memberships.get(0);

    		for (RoleDocumentDelegationMemberQualifier qualifier : membership.getQualifiers()) {
        		if (qualifier != null && qualifier.getKimAttribute() != null && !StringUtils.isBlank(qualifier.getKimAttribute().getAttributeName())) {
    	    		final KimAttributeField relatedDefinition = DataDictionaryTypeServiceHelper.findAttributeField(
                            qualifier.getKimAttribute().getAttributeName(), attributeDefinitions);

    	    		if (relatedDefinition.isUnique()) {
    	    			uniqueAttributeIds.add(qualifier.getKimAttrDefnId()); // it's unique - add it to the Set
    	    		}
        		}
    		}
    	}

    	return uniqueAttributeIds;
    }

    /**
     * Checks all the qualifiers for the given membership, so that all qualifiers which should be unique are guaranteed to be unique
     *
     * @param delegationMembershipToCheck the membership to check
     * @param membershipToCheckIndex the index of the person's membership in the role (for error reporting purposes)
     * @param validationErrors Map<String, String> of errors to report
     * @return true if all unique values are indeed unique, false otherwise
     */
    protected boolean validateUniquePersonRoleQualifiersUniqueForRoleDelegation(RoleDocumentDelegationMember delegationMembershipToCheck, int membershipToCheckIndex, List<RoleDocumentDelegationMember> delegationMemberships, Set<String> uniqueQualifierIds, List<RemotableAttributeError> validationErrors) {
    	boolean foundError = false;
    	int count = 0;

    	for (RoleDocumentDelegationMember delegationMembership : delegationMemberships) {
    		if (membershipToCheckIndex != count) {
    			if (sameDelegationMembership(delegationMembershipToCheck, delegationMembership)) {
    				if (sameUniqueDelegationMembershipQualifications(delegationMembershipToCheck, delegationMembership, uniqueQualifierIds)) {
    					foundError = true;
    					// add error to each qualifier which is supposed to be unique
    					int qualifierCount = 0;

    					for (RoleDocumentDelegationMemberQualifier qualifier : delegationMembership.getQualifiers()) {
    						if (qualifier != null && uniqueQualifierIds.contains(qualifier.getKimAttrDefnId())) {
    							validationErrors.add(RemotableAttributeError.Builder.create("document.delegationMembers["+membershipToCheckIndex+"].qualifiers["+qualifierCount+"].attrVal", RiceKeyConstants.ERROR_DOCUMENT_IDENTITY_MANAGEMENT_PERSON_QUALIFIER_VALUE_NOT_UNIQUE+":"+qualifier.getKimAttribute().getAttributeName()+";"+qualifier.getAttrVal()).build());
    						}
    						qualifierCount += 1;
    					}
    				}
    			}
    		}
    		count += 1;
    	}

    	return foundError;
    }

    /**
     * Determines if two memberships represent the same member being added: that is, the two memberships have the same type code and id
     *
     * @param membershipA the first membership to check
     * @param membershipB the second membership to check
     * @return true if the two memberships represent the same member; false if they do not, or if it could not be profitably determined if the members were the same
     */
    protected boolean sameDelegationMembership(RoleDocumentDelegationMember membershipA, RoleDocumentDelegationMember membershipB) {
    	if (!StringUtils.isBlank(membershipA.getMemberTypeCode()) && !StringUtils.isBlank(membershipB.getMemberTypeCode()) && !StringUtils.isBlank(membershipA.getMemberId()) && !StringUtils.isBlank(membershipB.getMemberId())) {
    		return membershipA.getMemberTypeCode().equals(membershipB.getMemberTypeCode()) && membershipA.getMemberId().equals(membershipB.getMemberId());
    	}
    	return false;
    }

    /**
     * Given two memberships which represent the same member, do they share qualifications?
     *
     * @param membershipA the first membership to check
     * @param membershipB the second membership to check
     * @param uniqueAttributeIds the Set of attribute definition ids which should be unique
     * @return
     */
    protected boolean sameUniqueDelegationMembershipQualifications(RoleDocumentDelegationMember membershipA, RoleDocumentDelegationMember membershipB, Set<String> uniqueAttributeIds) {
    	boolean equalSoFar = true;
    	for (String kimAttributeDefinitionId : uniqueAttributeIds) {
    		final RoleDocumentDelegationMemberQualifier qualifierA = membershipA.getQualifier(kimAttributeDefinitionId);
    		final RoleDocumentDelegationMemberQualifier qualifierB = membershipB.getQualifier(kimAttributeDefinitionId);

    		if (qualifierA != null && qualifierB != null) {
    			equalSoFar &= (qualifierA.getAttrVal() == null && qualifierB.getAttrVal() == null) || (qualifierA.getAttrVal() == null || qualifierA.getAttrVal().equals(qualifierB.getAttrVal()));
    		}
    	}
    	return equalSoFar;
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
	 *
	 * This method checks to see if adding a role to role membership
	 * creates a circular reference.
	 *
	 * @param addMemberEvent
	 * @return true  - ok to assign, no circular references
	 *         false - do not make assignment, will create circular reference.
	 */
	protected boolean checkForCircularRoleMembership(AddMemberEvent addMemberEvent)
	{
		KimDocumentRoleMember newMember = addMemberEvent.getMember();
		if (newMember == null || StringUtils.isBlank(newMember.getMemberId())){
			MessageMap errorMap = GlobalVariables.getMessageMap();
			errorMap.putError("member.memberId", RiceKeyConstants.ERROR_INVALID_ROLE, new String[] {""});
			return false;
		}
		Set<String> roleMemberIds = null;
		// if the role member is a role, check to make sure we won't be creating a circular reference.
		// Verify that the new role is not already related to the role either directly or indirectly
		if (newMember.isRole()){
			// get all nested role member ids that are of type role
			RoleService roleService = KimApiServiceLocator.getRoleService();
			roleMemberIds = roleService.getRoleTypeRoleMemberIds(newMember.getMemberId());

			// check to see if the document role is not a member of the new member role
			IdentityManagementRoleDocument document = (IdentityManagementRoleDocument)addMemberEvent.getDocument();
			if (roleMemberIds.contains(document.getRoleId())){
				MessageMap errorMap = GlobalVariables.getMessageMap();
				errorMap.putError("member.memberId", RiceKeyConstants.ERROR_ASSIGN_ROLE_MEMBER_CIRCULAR, new String[] {newMember.getMemberId()});
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the addResponsibilityRule
	 */
	public AddResponsibilityRule getAddResponsibilityRule() {
		if(addResponsibilityRule == null){
			try {
				addResponsibilityRule = addResponsibilityRuleClass.newInstance();
			} catch ( Exception ex ) {
				throw new RuntimeException( "Unable to create AddResponsibilityRule instance using class: " + addResponsibilityRuleClass, ex );
			}
		}
		return addResponsibilityRule;
	}

	/**
	 * @return the addPermissionRule
	 */
	public AddPermissionRule getAddPermissionRule() {
		if(addPermissionRule == null){
			try {
				addPermissionRule = addPermissionRuleClass.newInstance();
			} catch ( Exception ex ) {
				throw new RuntimeException( "Unable to create AddPermissionRule instance using class: " + addPermissionRuleClass, ex );
			}
		}
		return addPermissionRule;
	}

	/**
	 * @return the addMemberRule
	 */
	public AddMemberRule getAddMemberRule() {
		if(addMemberRule == null){
			try {
				addMemberRule = addMemberRuleClass.newInstance();
			} catch ( Exception ex ) {
				throw new RuntimeException( "Unable to create AddMemberRule instance using class: " + addMemberRuleClass, ex );
			}
		}
		return addMemberRule;
	}

	/**
	 * @return the addDelegationRule
	 */
	public AddDelegationRule getAddDelegationRule() {
		if(addDelegationRule == null){
			try {
				addDelegationRule = addDelegationRuleClass.newInstance();
			} catch ( Exception ex ) {
				throw new RuntimeException( "Unable to create AddDelegationRule instance using class: " + addDelegationRuleClass, ex );
			}
		}
		return addDelegationRule;
	}

	/**
	 * @return the addDelegationMemberRule
	 */
	public AddDelegationMemberRule getAddDelegationMemberRule() {
		if(addDelegationMemberRule == null){
			try {
				addDelegationMemberRule = addDelegationMemberRuleClass.newInstance();
			} catch ( Exception ex ) {
				throw new RuntimeException( "Unable to create AddDelegationMemberRule instance using class: " + addDelegationMemberRuleClass, ex );
			}
		}
		return addDelegationMemberRule;
	}

    public boolean processAddPermission(AddPermissionEvent addPermissionEvent) {
        return getAddPermissionRule().processAddPermission(addPermissionEvent);
    }

    public boolean hasPermissionToGrantPermission(Permission kimPermissionInfo , IdentityManagementRoleDocument document){
        return getAddPermissionRule().hasPermissionToGrantPermission(kimPermissionInfo, document);
    }

    public boolean processAddResponsibility(AddResponsibilityEvent addResponsibilityEvent) {
        return getAddResponsibilityRule().processAddResponsibility(addResponsibilityEvent);
    }

    public boolean hasPermissionToGrantResponsibility(Responsibility kimResponsibilityInfo, IdentityManagementRoleDocument document) {
        return getAddResponsibilityRule().hasPermissionToGrantResponsibility(kimResponsibilityInfo, document);
    }

    public boolean processAddMember(AddMemberEvent addMemberEvent) {
        boolean success = new KimDocumentMemberRule().processAddMember(addMemberEvent);
        success &= validateActiveDate("member.activeFromDate", addMemberEvent.getMember().getActiveFromDate(), addMemberEvent.getMember().getActiveToDate());
        success &= checkForCircularRoleMembership(addMemberEvent);
        return success;
    }

    public boolean processAddDelegation(AddDelegationEvent addDelegationEvent) {
        return getAddDelegationRule().processAddDelegation(addDelegationEvent);
    }

    public boolean processAddDelegationMember(AddDelegationMemberEvent addDelegationMemberEvent) {
        boolean success = new RoleDocumentDelegationMemberRule().processAddDelegationMember(addDelegationMemberEvent);
        RoleDocumentDelegationMember roleDocumentDelegationMember = addDelegationMemberEvent.getDelegationMember();
        success &= validateActiveDate("delegationMember.activeFromDate", roleDocumentDelegationMember.getActiveFromDate(), roleDocumentDelegationMember.getActiveToDate());
        return success;
    }

	public ResponsibilityService getResponsibilityService() {
		if(responsibilityService == null){
			responsibilityService = KimApiServiceLocator.getResponsibilityService();
		}
		return responsibilityService;
	}

    public ResponsibilityInternalService getResponsibilityInternalService() {
    	if ( responsibilityInternalService == null ) {
    		responsibilityInternalService = KimImplServiceLocator.getResponsibilityInternalService();
    	}
		return responsibilityInternalService;
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


    protected RoleTypeService getRoleTypeService(KimType typeInfo) {
        String serviceName = typeInfo.getServiceName();
        if (serviceName != null) {
            try {
                KimTypeService service = (KimTypeService) GlobalResourceLoader.getService(QName.valueOf(serviceName));
                if (service != null && service instanceof RoleTypeService) {
                    return (RoleTypeService) service;
                }
                return (RoleTypeService) KimImplServiceLocator.getService("kimNoMembersRoleTypeService");
            } catch (Exception ex) {
                return (RoleTypeService) KimImplServiceLocator.getService("kimNoMembersRoleTypeService");
            }
        }
        return null;
    }

    private static class VersionedService<T> {

        String version;
        T service;

        VersionedService(String version, T service) {
            this.version = version;
            this.service = service;
        }

        T getService() {
            return this.service;
        }

        String getVersion() {
            return this.version;
        }

    }

    protected VersionedService<RoleTypeService> getVersionedRoleTypeService(KimType typeInfo) {
        String serviceName = typeInfo.getServiceName();
        if (serviceName != null) {
            String version = "2.0.0"; // default version since the base services have been available since then
            RoleTypeService roleTypeService = null;

            try {

                ServiceBus serviceBus = KsbApiServiceLocator.getServiceBus();
                Endpoint endpoint = serviceBus.getEndpoint(QName.valueOf(serviceName));
                if (endpoint != null) {
                    version = endpoint.getServiceConfiguration().getServiceVersion();
                }
                KimTypeService service = (KimTypeService) GlobalResourceLoader.getService(QName.valueOf(serviceName));
                if (service != null && service instanceof RoleTypeService) {
                    roleTypeService = (RoleTypeService) service;
                } else {
                    roleTypeService = (RoleTypeService) KimImplServiceLocator.getService("kimNoMembersRoleTypeService");
                }
            } catch (Exception ex) {
                roleTypeService = (RoleTypeService) KimImplServiceLocator.getService("kimNoMembersRoleTypeService");
            }

            return new VersionedService<RoleTypeService>(version, roleTypeService);
        }

        return null;
    }

}
