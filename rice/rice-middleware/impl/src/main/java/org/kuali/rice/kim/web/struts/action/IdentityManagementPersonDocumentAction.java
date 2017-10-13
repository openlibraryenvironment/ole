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
package org.kuali.rice.kim.web.struts.action;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimAttributeField;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleQualifier;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleResponsibilityAction;
import org.kuali.rice.kim.bo.ui.PersonDocumentAddress;
import org.kuali.rice.kim.bo.ui.PersonDocumentAffiliation;
import org.kuali.rice.kim.bo.ui.PersonDocumentCitizenship;
import org.kuali.rice.kim.bo.ui.PersonDocumentEmail;
import org.kuali.rice.kim.bo.ui.PersonDocumentEmploymentInfo;
import org.kuali.rice.kim.bo.ui.PersonDocumentGroup;
import org.kuali.rice.kim.bo.ui.PersonDocumentName;
import org.kuali.rice.kim.bo.ui.PersonDocumentPhone;
import org.kuali.rice.kim.bo.ui.PersonDocumentRole;
import org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMember;
import org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMemberQualifier;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.document.rule.AttributeValidationHelper;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.responsibility.ResponsibilityInternalService;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.kim.impl.role.RoleResponsibilityBo;
import org.kuali.rice.kim.impl.services.KimImplServiceLocator;
import org.kuali.rice.kim.impl.type.KimTypeAttributesHelper;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.kim.rule.event.ui.AddGroupEvent;
import org.kuali.rice.kim.rule.event.ui.AddPersonDelegationMemberEvent;
import org.kuali.rice.kim.rule.event.ui.AddPersonDocumentRoleQualifierEvent;
import org.kuali.rice.kim.rule.event.ui.AddRoleEvent;
import org.kuali.rice.kim.rules.ui.PersonDocumentRoleRule;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.web.struts.form.IdentityManagementPersonDocumentForm;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class IdentityManagementPersonDocumentAction extends IdentityManagementDocumentActionBase {

    protected ResponsibilityInternalService responsibilityInternalService;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward;
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        // accept either the principal name or principal ID, looking up the latter if necessary
        // this allows inquiry links to work even when only the principal name is present
        String principalId = request.getParameter(KIMPropertyConstants.Person.PRINCIPAL_ID);
        String principalName = request.getParameter(KIMPropertyConstants.Person.PRINCIPAL_NAME);
        if ( StringUtils.isBlank(principalId) && StringUtils.isNotBlank(principalName) ) {
        	Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalName);
        	if ( principal != null ) {
        		principalId = principal.getPrincipalId();
        	}
        }
        if ( principalId != null ) {
        	personDocumentForm.setPrincipalId(principalId);
        }
        forward = super.execute(mapping, form, request, response);
        
        personDocumentForm.setCanModifyEntity(getUiDocumentService().canModifyEntity(GlobalVariables.getUserSession().getPrincipalId(), personDocumentForm.getPrincipalId()));
        EntityDefault origEntity = null;
        if(personDocumentForm.getPersonDocument()!=null) {
			origEntity = getIdentityService().getEntityDefault(personDocumentForm.getPersonDocument().getEntityId());
		}
        boolean isCreatingNew = (personDocumentForm.getPersonDocument()==null || origEntity==null)?true:false;
        personDocumentForm.setCanOverrideEntityPrivacyPreferences(isCreatingNew || getUiDocumentService().canOverrideEntityPrivacyPreferences(GlobalVariables.getUserSession().getPrincipalId(), personDocumentForm.getPrincipalId()));
		return forward;
    }

	@Override
	protected void loadDocument(KualiDocumentFormBase form)
			throws WorkflowException {
		super.loadDocument(form);
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
		IdentityManagementPersonDocument personDoc = personDocumentForm.getPersonDocument();
		populateRoleInformation(personDoc);
	}

	protected void populateRoleInformation( IdentityManagementPersonDocument personDoc ) {
		for (PersonDocumentRole role : personDoc.getRoles()) {
//			try {
            KimType type = KimApiServiceLocator.getKimTypeInfoService().getKimType(role.getKimTypeId());
            KimTypeService kimTypeService = null;
            if (StringUtils.isNotBlank(type.getServiceName()))  {
                kimTypeService = (KimTypeService) KimImplServiceLocator.getBean(type.getServiceName());
            } else {
                kimTypeService = getKimTypeService(KimTypeBo.to(role.getKimRoleType()));
            }
	        if ( kimTypeService != null ) {
                role.setDefinitions(kimTypeService.getAttributeDefinitions(role.getKimTypeId()));
	        }
        	// when post again, it will need this during populate
            role.setNewRolePrncpl(new KimDocumentRoleMember());
            for (KimAttributeField key : role.getDefinitions()) {
            	KimDocumentRoleQualifier qualifier = new KimDocumentRoleQualifier();
            	//qualifier.setQualifierKey(key);
	        	setAttrDefnIdForQualifier(qualifier,key);
            	role.getNewRolePrncpl().getQualifiers().add(qualifier);
            }
	        role.setAttributeEntry( getUiDocumentService().getAttributeEntries( role.getDefinitions() ) );
		}
    }

	@Override
	protected void createDocument(KualiDocumentFormBase form)
			throws WorkflowException {
		super.createDocument(form);
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        if(StringUtils.isBlank(personDocumentForm.getPrincipalId())){
    		personDocumentForm.getPersonDocument().initializeDocumentForNewPerson();
    		personDocumentForm.setPrincipalId(personDocumentForm.getPersonDocument().getPrincipalId());
        } else {
        	getUiDocumentService().loadEntityToPersonDoc(personDocumentForm.getPersonDocument(), personDocumentForm.getPrincipalId() );
        	populateRoleInformation( personDocumentForm.getPersonDocument() );
        	if(personDocumentForm.getPersonDocument()!=null) {
				personDocumentForm.getPersonDocument().setIfRolesEditable();
			}
        }
	}
	
	/***
	 * @see org.kuali.rice.kim.web.struts.action.IdentityManagementDocumentActionBase#getActionName()
	 */
	@Override
	public String getActionName(){
		return KimConstants.KimUIConstants.KIM_PERSON_DOCUMENT_ACTION;
	}

	public ActionForward addAffln(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentAffiliation newAffln = personDocumentForm.getNewAffln();
        newAffln.setDocumentNumber(personDocumentForm.getPersonDocument().getDocumentNumber());
        newAffln.refreshReferenceObject("affiliationType");
        personDocumentForm.getPersonDocument().getAffiliations().add(newAffln);
        personDocumentForm.setNewAffln(new PersonDocumentAffiliation());        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
	
    public ActionForward deleteAffln(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getAffiliations().remove(getLineToDelete(request));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    public ActionForward addCitizenship(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentCitizenship newCitizenship = personDocumentForm.getNewCitizenship();
        personDocumentForm.getPersonDocument().getCitizenships().add(newCitizenship);
        personDocumentForm.setNewCitizenship(new PersonDocumentCitizenship());        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    public ActionForward deleteCitizenship(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getCitizenships().remove(getLineToDelete(request));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addEmpInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        IdentityManagementPersonDocument personDOc = personDocumentForm.getPersonDocument();
        PersonDocumentAffiliation affiliation = personDOc.getAffiliations().get(getSelectedLine(request));        
        PersonDocumentEmploymentInfo newempInfo = affiliation.getNewEmpInfo();
        newempInfo.setDocumentNumber(personDOc.getDocumentNumber());
        newempInfo.setVersionNumber(new Long(1));
        affiliation.getEmpInfos().add(newempInfo);
        affiliation.setNewEmpInfo(new PersonDocumentEmploymentInfo());        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    public ActionForward deleteEmpInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        String selectedIndexes = getSelectedParentChildIdx(request);
        if (selectedIndexes != null) {
	        String [] indexes = StringUtils.split(selectedIndexes,":");
	        PersonDocumentAffiliation affiliation = personDocumentForm.getPersonDocument().getAffiliations().get(Integer.parseInt(indexes[0]));
	        affiliation.getEmpInfos().remove(Integer.parseInt(indexes[1]));
        }
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    protected String getSelectedParentChildIdx(HttpServletRequest request) {
    	String lineNumber = null;
        String parameterName = (String) request.getAttribute(KRADConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            lineNumber = StringUtils.substringBetween(parameterName, ".line", ".");
        }
        return lineNumber;
    }

    public ActionForward addName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentName newName = personDocumentForm.getNewName();
        newName.setDocumentNumber(personDocumentForm.getDocument().getDocumentNumber());
        personDocumentForm.getPersonDocument().getNames().add(newName);
        personDocumentForm.setNewName(new PersonDocumentName());        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    public ActionForward deleteName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getNames().remove(getLineToDelete(request));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addAddress(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentAddress newAddress = personDocumentForm.getNewAddress();
        newAddress.setDocumentNumber(personDocumentForm.getDocument().getDocumentNumber());
        personDocumentForm.getPersonDocument().getAddrs().add(newAddress);
        personDocumentForm.setNewAddress(new PersonDocumentAddress());        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    public ActionForward deleteAddress(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getAddrs().remove(getLineToDelete(request));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addPhone(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentPhone newPhone = personDocumentForm.getNewPhone();
        newPhone.setDocumentNumber(personDocumentForm.getDocument().getDocumentNumber());
        personDocumentForm.getPersonDocument().getPhones().add(newPhone);
        personDocumentForm.setNewPhone(new PersonDocumentPhone());        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    public ActionForward deletePhone(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getPhones().remove(getLineToDelete(request));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentEmail newEmail = personDocumentForm.getNewEmail();
        newEmail.setDocumentNumber(personDocumentForm.getDocument().getDocumentNumber());
        personDocumentForm.getPersonDocument().getEmails().add(newEmail);
        personDocumentForm.setNewEmail(new PersonDocumentEmail());        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    public ActionForward deleteEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getEmails().remove(getLineToDelete(request));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentGroup newGroup = personDocumentForm.getNewGroup();
        if (newGroup.getGroupName() == null 
        		&& newGroup.getNamespaceCode() == null 
        		&& newGroup.getGroupId() != null) {
        	Group tempGroup = KimApiServiceLocator.getGroupService().getGroup(newGroup.getGroupId());
            if (tempGroup == null) {
                GlobalVariables.getMessageMap().putError("newGroup.groupId",
                    RiceKeyConstants.ERROR_ASSIGN_GROUP_INVALID,
                    new String[] { newGroup.getGroupId(),""});
                return mapping.findForward(RiceConstants.MAPPING_BASIC);
            }
        	newGroup.setGroupName(tempGroup.getName());
	        newGroup.setNamespaceCode(tempGroup.getNamespaceCode());
	        newGroup.setKimTypeId(tempGroup.getKimTypeId());
        } else if (StringUtils.isBlank(newGroup.getGroupName())
                 || StringUtils.isBlank(newGroup.getNamespaceCode())) {
                 GlobalVariables.getMessageMap().putError("newGroup.groupName",
                      RiceKeyConstants.ERROR_ASSIGN_GROUP_INVALID,
                      new String[] { newGroup.getNamespaceCode(), newGroup.getGroupName()});
                 return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
        Group tempGroup = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(
            newGroup.getNamespaceCode(), newGroup.getGroupName());
        if (tempGroup == null) {
            GlobalVariables.getMessageMap().putError("newGroup.groupName",
                    RiceKeyConstants.ERROR_ASSIGN_GROUP_INVALID,
                    new String[] { newGroup.getNamespaceCode(), newGroup.getGroupName()});
            return mapping.findForward(RiceConstants.MAPPING_BASIC);
        }
        newGroup.setGroupId(tempGroup.getId());
	    newGroup.setKimTypeId(tempGroup.getKimTypeId());
        if (getKualiRuleService().applyRules(new AddGroupEvent("",personDocumentForm.getPersonDocument(), newGroup))) {
	        Group group = getGroupService().getGroup(newGroup.getGroupId());
	        newGroup.setGroupName(group.getName());
	        newGroup.setNamespaceCode(group.getNamespaceCode());
	        newGroup.setKimTypeId(group.getKimTypeId());
        	personDocumentForm.getPersonDocument().getGroups().add(newGroup);
	        personDocumentForm.setNewGroup(new PersonDocumentGroup());
        }
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    public ActionForward deleteGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentGroup inactivedGroupMembership = personDocumentForm.getPersonDocument().getGroups().get(getLineToDelete(request));
        Calendar cal = Calendar.getInstance();
        inactivedGroupMembership.setActiveToDate(new Timestamp(cal.getTimeInMillis()));        
        personDocumentForm.getPersonDocument().getGroups().set(getLineToDelete(request), inactivedGroupMembership);
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

       public ActionForward addRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentRole newRole = personDocumentForm.getNewRole();

        if (getKualiRuleService().applyRules(new AddRoleEvent("",personDocumentForm.getPersonDocument(), newRole))) {
         Role role = KimApiServiceLocator.getRoleService().getRole(newRole.getRoleId());
         if(!validateRole(newRole.getRoleId(), role, PersonDocumentRoleRule.ERROR_PATH, "Person")){
          return mapping.findForward(RiceConstants.MAPPING_BASIC);
         }
         newRole.setRoleName(role.getName());
         newRole.setNamespaceCode(role.getNamespaceCode());
         newRole.setKimTypeId(role.getKimTypeId());
            KimDocumentRoleMember roleMember = new KimDocumentRoleMember();
            roleMember.setMemberId(personDocumentForm.getPrincipalId());
            roleMember.setMemberTypeCode(MemberType.PRINCIPAL.getCode());
            roleMember.setRoleId(newRole.getRoleId());
            newRole.setNewRolePrncpl(roleMember);
         if(!validateRoleAssignment(personDocumentForm.getPersonDocument(), newRole)){
          return mapping.findForward(RiceConstants.MAPPING_BASIC);
         }
         KimTypeService kimTypeService = getKimTypeService(KimTypeBo.to(newRole.getKimRoleType()));
         //AttributeDefinitionMap definitions = kimTypeService.getAttributeDefinitions();
         // role type populated from form is not a complete record
         if ( kimTypeService != null ) {
          newRole.setDefinitions(kimTypeService.getAttributeDefinitions(newRole.getKimTypeId()));
         }
         KimDocumentRoleMember newRolePrncpl = newRole.getNewRolePrncpl();
         newRole.refreshReferenceObject("assignedResponsibilities");

         for (KimAttributeField key : newRole.getDefinitions()) {
          KimDocumentRoleQualifier qualifier = new KimDocumentRoleQualifier();
          //qualifier.setQualifierKey(key);
          setAttrDefnIdForQualifier(qualifier,key);
          newRolePrncpl.getQualifiers().add(qualifier);
         }
         if (newRole.getDefinitions().isEmpty()) {
          List<KimDocumentRoleMember> rolePrncpls = new ArrayList<KimDocumentRoleMember>();
          setupRoleRspActions(newRole, newRolePrncpl);
             rolePrncpls.add(newRolePrncpl);
          newRole.setRolePrncpls(rolePrncpls);
         }
         //newRole.setNewRolePrncpl(newRolePrncpl);
         newRole.setAttributeEntry( getUiDocumentService().getAttributeEntries( newRole.getDefinitions() ) );
         personDocumentForm.getPersonDocument().getRoles().add(newRole);
         personDocumentForm.setNewRole(new PersonDocumentRole());
        }
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
	protected boolean validateRoleAssignment(IdentityManagementPersonDocument document, PersonDocumentRole newRole){
        boolean rulePassed = true;
        if(!document.validAssignRole(newRole)){
			GlobalVariables.getMessageMap().putError("newRole.roleId", 
					RiceKeyConstants.ERROR_ASSIGN_ROLE, 
					new String[] {newRole.getNamespaceCode(), newRole.getRoleName()});
	        rulePassed = false;
        }
		return rulePassed;
	}

    protected void setupRoleRspActions(PersonDocumentRole role, KimDocumentRoleMember rolePrncpl) {
        for (RoleResponsibilityBo roleResp : role.getAssignedResponsibilities()) {
        	if (getResponsibilityInternalService().areActionsAtAssignmentLevelById(roleResp.getResponsibilityId())) {
        		KimDocumentRoleResponsibilityAction roleRspAction = new KimDocumentRoleResponsibilityAction();
        		roleRspAction.setRoleResponsibilityId("*");        		
        		roleRspAction.refreshReferenceObject("roleResponsibility");
        		if(rolePrncpl.getRoleRspActions()==null || rolePrncpl.getRoleRspActions().size()<1){
        			if(rolePrncpl.getRoleRspActions()==null) {
						rolePrncpl.setRoleRspActions(new ArrayList<KimDocumentRoleResponsibilityAction>());
					}
        			 rolePrncpl.getRoleRspActions().add(roleRspAction);
        		}
        	}        	
        }
    }
    
//	protected boolean isUniqueRoleRspAction(List<KimDocumentRoleResponsibilityAction> roleRspActions, KimDocumentRoleResponsibilityAction roleRspAction){
//    	if(roleRspActions==null || roleRspAction==null) return false;
//    	for(KimDocumentRoleResponsibilityAction roleRspActionTemp: roleRspActions){
//    		if((StringUtils.isNotEmpty(roleRspActionTemp.getRoleMemberId()) && roleRspActionTemp.getRoleMemberId().equals(roleRspAction.getRoleMemberId())) && 
//    			(StringUtils.isNotEmpty(roleRspActionTemp.getRoleResponsibilityId())	&& roleRspActionTemp.getRoleResponsibilityId().equals(roleRspAction.getRoleResponsibilityId())))
//    			return false;
//    	}
//    	return true;
//    }
	    

    protected void setAttrDefnIdForQualifier(KimDocumentRoleQualifier qualifier,KimAttributeField definition) {
   		qualifier.setKimAttrDefnId(definition.getId());
   		qualifier.refreshReferenceObject("kimAttribute");
    }

    public ActionForward deleteRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentRole personDocumentRole = personDocumentForm.getPersonDocument().getRoles().get(getLineToDelete(request));
        Calendar cal = Calendar.getInstance();
        personDocumentRole.getRolePrncpls().get(0).setActiveToDate(new Timestamp(cal.getTimeInMillis()));
        personDocumentForm.getPersonDocument().getRoles().set(getLineToDelete(request), personDocumentRole);
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addRoleQualifier(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        IdentityManagementPersonDocument personDOc = personDocumentForm.getPersonDocument();
        int selectedRoleIdx = getSelectedLine(request);
        PersonDocumentRole role = personDOc.getRoles().get(selectedRoleIdx);
        KimDocumentRoleMember newRolePrncpl = role.getNewRolePrncpl();
        newRolePrncpl.setMemberTypeCode(MemberType.PRINCIPAL.getCode());
        newRolePrncpl.setMemberId(personDOc.getPrincipalId());
    	
    	if (getKualiRuleService().applyRules(new AddPersonDocumentRoleQualifierEvent("",
    			personDOc, newRolePrncpl, role, selectedRoleIdx))) {
        	setupRoleRspActions(role, newRolePrncpl);
    		role.getRolePrncpls().add(newRolePrncpl);
            KimDocumentRoleMember roleMember = new KimDocumentRoleMember();
            roleMember.setMemberTypeCode(MemberType.PRINCIPAL.getCode());
            roleMember.setMemberId(personDocumentForm.getPrincipalId());
	        role.setNewRolePrncpl(roleMember);
	        for (KimAttributeField key : role.getDefinitions()) {
	        	KimDocumentRoleQualifier qualifier = new KimDocumentRoleQualifier();
	        	//qualifier.setQualifierKey(key);
	        	setAttrDefnIdForQualifier(qualifier,key);
	        	role.getNewRolePrncpl().getQualifiers().add(qualifier);
	        }
    	}

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward deleteRoleQualifier(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        String selectedIndexes = getSelectedParentChildIdx(request);
        if (selectedIndexes != null) {
	        String [] indexes = StringUtils.split(selectedIndexes,":");
	        PersonDocumentRole role = personDocumentForm.getPersonDocument().getRoles().get(Integer.parseInt(indexes[0]));
            KimDocumentRoleMember member = role.getRolePrncpls().get(Integer.parseInt(indexes[1]));
            Calendar cal = Calendar.getInstance();
            member.setActiveToDate(new Timestamp(cal.getTimeInMillis()));
            // role.getRolePrncpls().remove(Integer.parseInt(indexes[1]));
        }
        return mapping.findForward(RiceConstants.MAPPING_BASIC);

    }
    
    public ActionForward addDelegationMember(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        IdentityManagementPersonDocument personDocument = personDocumentForm.getPersonDocument();
        RoleDocumentDelegationMember newDelegationMember = personDocumentForm.getNewDelegationMember();
        KimTypeAttributesHelper attrHelper = newDelegationMember.getAttributesHelper();
        if (getKualiRuleService().applyRules(new AddPersonDelegationMemberEvent("", personDocumentForm.getPersonDocument(), newDelegationMember))) {
	        //RoleImpl roleBo = (RoleImpl)getUiDocumentService().getMember(KimApiConstants.KimUIConstants.MEMBER_TYPE_ROLE_CODE, newDelegationMember.getRoleDao().getRoleId());
	        Role role;
        	role = KimApiServiceLocator.getRoleService().getRole(newDelegationMember.getRoleBo().getId());
	        if (role != null) {
		        if(!validateRole(newDelegationMember.getRoleBo().getId(),role, PersonDocumentRoleRule.ERROR_PATH, "Person")){
		        	return mapping.findForward(RiceConstants.MAPPING_BASIC);
		        }
		        newDelegationMember.setRoleBo(RoleBo.from(role));
	        }
	        KimAttributeField attrDefinition;
	        RoleMemberBo roleMember = KIMServiceLocatorInternal.getUiDocumentService().getRoleMember(newDelegationMember.getRoleMemberId());
	        Map<String, String>
                    roleMemberAttributes = (new AttributeValidationHelper()).convertAttributesToMap(roleMember.getAttributeDetails());
	        for (KimAttributeField key: attrHelper.getDefinitions()) {
	        	RoleDocumentDelegationMemberQualifier qualifier = new RoleDocumentDelegationMemberQualifier();
	        	attrDefinition = key;
	        	qualifier.setKimAttrDefnId(attrHelper.getKimAttributeDefnId(attrDefinition));
	        	qualifier.setAttrVal(attrHelper.getAttributeValue(roleMemberAttributes, attrDefinition.getAttributeField().getName()));
	        	newDelegationMember.setMemberId(personDocument.getPrincipalId());
	        	newDelegationMember.setMemberTypeCode(MemberType.PRINCIPAL.getCode());
	        	newDelegationMember.getQualifiers().add(qualifier);
	        }
	        //newDelegationMember.setAttributeEntry(getUiDocumentService().getAttributeEntries(newDelegationMember.getAttributesHelper().getDefinitions())));
	        personDocument.getDelegationMembers().add(newDelegationMember);
	        personDocumentForm.setNewDelegationMember(new RoleDocumentDelegationMember());
        }
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    public ActionForward deleteDelegationMember(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getDelegationMembers().remove(getLineToDelete(request));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

	@Override
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return super.save(mapping, form, request, response);
	}
	
    @Override
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm impdForm = (IdentityManagementPersonDocumentForm) form;

        ActionForward forward = this.refreshAfterDelegationMemberRoleSelection(mapping, impdForm, request);
        if (forward != null) {
            return forward;
        }

        return super.refresh(mapping, form, request, response);
	}
    
    protected ActionForward refreshAfterDelegationMemberRoleSelection(ActionMapping mapping, IdentityManagementPersonDocumentForm impdForm, HttpServletRequest request) {
        String refreshCaller = impdForm.getRefreshCaller();

        boolean isRoleLookupable = KimConstants.KimUIConstants.ROLE_LOOKUPABLE_IMPL.equals(refreshCaller);
        boolean isRoleMemberLookupable = KimConstants.KimUIConstants.KIM_DOCUMENT_ROLE_MEMBER_LOOKUPABLE_IMPL.equals(refreshCaller);

        // do not execute the further refreshing logic if the refresh caller is not a lookupable
        if (!isRoleLookupable && !isRoleMemberLookupable) {
            return null;
        }

        //In case of delegation member lookup impdForm.getNewDelegationMemberRoleId() will be populated.
        if(impdForm.getNewDelegationMemberRoleId() == null){
            return null;
        }
        if(isRoleLookupable){
            return renderRoleMemberSelection(mapping, request, impdForm);
        }

        String roleMemberId = request.getParameter(KimConstants.PrimaryKeyConstants.ROLE_MEMBER_ID);
        if(StringUtils.isNotBlank(roleMemberId)){
            impdForm.getNewDelegationMember().setRoleMemberId(roleMemberId);
            RoleMemberBo roleMember = getUiDocumentService().getRoleMember(roleMemberId);
            impdForm.getNewDelegationMember().setRoleMemberMemberId(roleMember.getMemberId());
            impdForm.getNewDelegationMember().setRoleMemberMemberTypeCode(roleMember.getType().getCode());
            impdForm.getNewDelegationMember().setRoleMemberName(getUiDocumentService().getMemberName(MemberType.fromCode(impdForm.getNewDelegationMember().getRoleMemberMemberTypeCode()), impdForm.getNewDelegationMember().getRoleMemberMemberId()));
            impdForm.getNewDelegationMember().setRoleMemberNamespaceCode(getUiDocumentService().getMemberNamespaceCode(MemberType.fromCode(impdForm.getNewDelegationMember().getRoleMemberMemberTypeCode()), impdForm.getNewDelegationMember().getRoleMemberMemberId()));

            Role role;
        	role = KimApiServiceLocator.getRoleService().getRole(impdForm.getNewDelegationMember().getRoleBo().getId());
	        if (role != null) {
		        if(!validateRole(impdForm.getNewDelegationMember().getRoleBo().getId(), role, PersonDocumentRoleRule.ERROR_PATH, "Person")){
		        	return mapping.findForward(RiceConstants.MAPPING_BASIC);
		        }
		        impdForm.getNewDelegationMember().setRoleBo(RoleBo.from(role));
	        }
        }
        impdForm.setNewDelegationMemberRoleId(null);
        return null;
    }
    
    protected ActionForward renderRoleMemberSelection(ActionMapping mapping, HttpServletRequest request, IdentityManagementPersonDocumentForm impdForm) {
        Properties props = new Properties();

        props.put(KRADConstants.SUPPRESS_ACTIONS, Boolean.toString(true));
        props.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, KimDocumentRoleMember.class.getName());
        props.put(KRADConstants.LOOKUP_ANCHOR, KRADConstants.ANCHOR_TOP_OF_FORM);
        props.put(KRADConstants.LOOKED_UP_COLLECTION_NAME, KimConstants.KimUIConstants.ROLE_MEMBERS_COLLECTION_NAME);

        String conversionPatttern = "{0}" + KRADConstants.FIELD_CONVERSION_PAIR_SEPARATOR + "{0}";
        StringBuilder fieldConversion = new StringBuilder();
        fieldConversion.append(MessageFormat.format(conversionPatttern, 
       		KimConstants.PrimaryKeyConstants.SUB_ROLE_ID)).append(KRADConstants.FIELD_CONVERSIONS_SEPARATOR);
        fieldConversion.append(MessageFormat.format(conversionPatttern, 
           		KimConstants.PrimaryKeyConstants.ROLE_MEMBER_ID)).append(KRADConstants.FIELD_CONVERSIONS_SEPARATOR);

        props.put(KRADConstants.CONVERSION_FIELDS_PARAMETER, fieldConversion);

        props.put(KimConstants.PrimaryKeyConstants.SUB_ROLE_ID, impdForm.getNewDelegationMember().getRoleBo().getId());

        props.put(KRADConstants.RETURN_LOCATION_PARAMETER, this.getReturnLocation(request, mapping));
     //   props.put(KRADConstants.BACK_LOCATION, this.getReturnLocation(request, mapping));

        props.put(KRADConstants.LOOKUP_AUTO_SEARCH, "Yes");
        props.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.SEARCH_METHOD);

        props.put(KRADConstants.DOC_FORM_KEY, GlobalVariables.getUserSession().addObjectWithGeneratedKey(impdForm));
     //   props.put(KRADConstants.DOC_NUM, impdForm.getDocument().getDocumentNumber());

        // TODO: how should this forward be handled
        String url = UrlFactory.parameterizeUrl(getApplicationBaseUrl() + "/kr/" + KRADConstants.LOOKUP_ACTION, props);

        impdForm.registerEditableProperty("methodToCall");

        return new ActionForward(url, true);
    }


    public ResponsibilityInternalService getResponsibilityInternalService() {
    	if ( responsibilityInternalService == null ) {
    		responsibilityInternalService = KimImplServiceLocator.getResponsibilityInternalService();
    	}
		return responsibilityInternalService;
	}
}
