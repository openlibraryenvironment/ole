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
package org.kuali.rice.kim.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.bo.ui.PersonDocumentAddress;
import org.kuali.rice.kim.bo.ui.PersonDocumentAffiliation;
import org.kuali.rice.kim.bo.ui.PersonDocumentEmail;
import org.kuali.rice.kim.bo.ui.PersonDocumentEmploymentInfo;
import org.kuali.rice.kim.bo.ui.PersonDocumentName;
import org.kuali.rice.kim.bo.ui.PersonDocumentPhone;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.document.IdentityManagementRoleDocument;
import org.kuali.rice.kim.framework.services.KimFrameworkServiceLocator;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.impl.common.delegate.DelegateTypeBo;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.group.GroupMemberBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.role.RoleMemberAttributeDataBo;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.kim.impl.role.RoleResponsibilityActionBo;
import org.kuali.rice.kim.impl.services.KimImplServiceLocator;
import org.kuali.rice.kim.util.KimCommonUtilsInternal;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * Customized version of the UiDocumentServiceImpl to support LDAP communcation
 *
 * @author Leo Przybylski (przybyls@arizona.edu)
 */
public class LdapUiDocumentServiceImpl extends org.kuali.rice.kim.service.impl.UiDocumentServiceImpl {

	/**
	 *
	 * @see org.kuali.rice.kim.service.UiDocumentService#loadEntityToPersonDoc(IdentityManagementPersonDocument, String)
	 */
	public void loadEntityToPersonDoc(IdentityManagementPersonDocument identityManagementPersonDocument, String principalId) {
		Principal principal = this.getIdentityService().getPrincipal(principalId);
        if(principal==null) {
        	throw new RuntimeException("Principal does not exist for principal id:"+principalId);
        }

        identityManagementPersonDocument.setPrincipalId(principal.getPrincipalId());
        identityManagementPersonDocument.setPrincipalName(principal.getPrincipalName());
        //identityManagementPersonDocument.setPassword(principal.getPassword());
        identityManagementPersonDocument.setActive(principal.isActive());
        Entity kimEntity = this.getIdentityService().getEntity(principal.getEntityId());
		identityManagementPersonDocument.setEntityId(kimEntity.getId());
		if ( ObjectUtils.isNotNull( kimEntity.getPrivacyPreferences() ) ) {
			identityManagementPersonDocument.setPrivacy(loadPrivacyReferences(kimEntity.getPrivacyPreferences()));
		}
		//identityManagementPersonDocument.setActive(kimEntity.isActive());
		identityManagementPersonDocument.setAffiliations(loadAffiliations(kimEntity.getAffiliations(),kimEntity.getEmploymentInformation()));
		identityManagementPersonDocument.setNames(loadNames( identityManagementPersonDocument, principalId, kimEntity.getNames(), identityManagementPersonDocument.getPrivacy().isSuppressName() ));
		EntityTypeContactInfo entityType = null;
		for (EntityTypeContactInfo type : kimEntity.getEntityTypeContactInfos()) {
			if (KimConstants.EntityTypes.PERSON.equals(type.getEntityTypeCode())) {
				entityType = EntityTypeContactInfo.Builder.create(type).build();
			}
		}

		if(entityType!=null){
			identityManagementPersonDocument.setEmails(loadEmails(identityManagementPersonDocument, principalId, entityType.getEmailAddresses(), identityManagementPersonDocument.getPrivacy().isSuppressEmail()));
			identityManagementPersonDocument.setPhones(loadPhones(identityManagementPersonDocument, principalId, entityType.getPhoneNumbers(), identityManagementPersonDocument.getPrivacy().isSuppressPhone()));
			identityManagementPersonDocument.setAddrs(loadAddresses(identityManagementPersonDocument, principalId, entityType.getAddresses(), identityManagementPersonDocument.getPrivacy().isSuppressAddress()));
		}

		List<Group> groups = getGroupService().getGroups(getGroupService().getDirectGroupIdsByPrincipalId(
                identityManagementPersonDocument.getPrincipalId()));
		loadGroupToPersonDoc(identityManagementPersonDocument, groups);
		loadRoleToPersonDoc(identityManagementPersonDocument);
		loadDelegationsToPersonDoc(identityManagementPersonDocument);
	}

	protected String getInitiatorPrincipalId(Document document){
		try{
			return document.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId();
		} catch(Exception ex){
			return null;
		}
	}

	/**
	 * @see org.kuali.rice.kim.service.UiDocumentService#saveEntityPerson(IdentityManagementPersonDocument)
	 */
    public void saveEntityPerson(IdentityManagementPersonDocument identityManagementPersonDocument) {
        final Entity kimEntity = getIdentityService().getEntity(identityManagementPersonDocument.getEntityId());
		boolean creatingNew = false;

		String initiatorPrincipalId = getInitiatorPrincipalId(identityManagementPersonDocument);
		boolean inactivatingPrincipal = false;

		List <GroupMemberBo>  groupPrincipals = populateGroupMembers(identityManagementPersonDocument);
		List <RoleMemberBo>  rolePrincipals = populateRoleMembers(identityManagementPersonDocument);
		List <DelegateTypeBo> personDelegations = populateDelegations(identityManagementPersonDocument);
		List <PersistableBusinessObject> bos = new ArrayList<PersistableBusinessObject>();
		List <RoleResponsibilityActionBo> roleRspActions = populateRoleRspActions(identityManagementPersonDocument);
		List <RoleMemberAttributeDataBo> blankRoleMemberAttrs = getBlankRoleMemberAttrs(rolePrincipals);
		//if(ObjectUtils.isNotNull(kimEntity.getPrivacyPreferences()))
		//	bos.add(kimEntity.getPrivacyPreferences());
		bos.addAll(groupPrincipals);
		bos.addAll(rolePrincipals);
		bos.addAll(roleRspActions);
		bos.addAll(personDelegations);
		// boservice.save(bos) does not handle deleteawarelist
		getBusinessObjectService().save(bos);

		if (!blankRoleMemberAttrs.isEmpty()) {
			getBusinessObjectService().delete(blankRoleMemberAttrs);
		}
		if ( inactivatingPrincipal ) {
			//when a person is inactivated, inactivate their group, role, and delegation memberships
			KimImplServiceLocator.getRoleInternalService().principalInactivated(identityManagementPersonDocument.getPrincipalId());
		}
	}

    protected boolean setupPrincipal(IdentityManagementPersonDocument identityManagementPersonDocument,EntityBo kimEntity, List<PrincipalBo> origPrincipals) {
    	boolean inactivatingPrincipal = false;
		List<PrincipalBo> principals = new ArrayList<PrincipalBo>();
		Principal.Builder principal = Principal.Builder.create(identityManagementPersonDocument.getPrincipalName());
		principal.setPrincipalId(identityManagementPersonDocument.getPrincipalId());
		//principal.setPassword(identityManagementPersonDocument.getPassword());
		principal.setActive(identityManagementPersonDocument.isActive());
		principal.setEntityId(identityManagementPersonDocument.getEntityId());
		if(ObjectUtils.isNotNull(origPrincipals)){
			for (PrincipalBo prncpl : origPrincipals) {
				if (prncpl.getPrincipalId()!=null && StringUtils.equals(prncpl.getPrincipalId(), principal.getPrincipalId())) {
					principal.setVersionNumber(prncpl.getVersionNumber());
                    principal.setObjectId(prncpl.getObjectId());
					// check if inactivating the principal
					if ( prncpl.isActive() && !principal.isActive() ) {
						inactivatingPrincipal = true;
					}
				}
			}
		}
		principals.add(PrincipalBo.from(principal.build()));

		kimEntity.setPrincipals(principals);
		return inactivatingPrincipal;
	}

	protected List<PersonDocumentAffiliation> loadAffiliations(List <EntityAffiliation> affiliations, List<EntityEmployment> empInfos) {
		List<PersonDocumentAffiliation> docAffiliations = new ArrayList<PersonDocumentAffiliation>();
		if(ObjectUtils.isNotNull(affiliations)){
			for (EntityAffiliation affiliation: affiliations) {
				if(affiliation.isActive()){
					PersonDocumentAffiliation docAffiliation = new PersonDocumentAffiliation();
					docAffiliation.setAffiliationTypeCode(affiliation.getAffiliationType().getCode());
					docAffiliation.setCampusCode(affiliation.getCampusCode());
					docAffiliation.setActive(affiliation.isActive());
					docAffiliation.setDflt(affiliation.isDefaultValue());
					docAffiliation.setEntityAffiliationId(affiliation.getId());
					docAffiliation.refreshReferenceObject("affiliationType");
					// EntityAffiliationImpl does not define empinfos as collection
					docAffiliations.add(docAffiliation);
					docAffiliation.setEdit(true);
					// employment informations
					List<PersonDocumentEmploymentInfo> docEmploymentInformations = new ArrayList<PersonDocumentEmploymentInfo>();
					if(ObjectUtils.isNotNull(empInfos)){
						for (EntityEmployment empInfo: empInfos) {
							if (empInfo.isActive()
                                    && StringUtils.equals(docAffiliation.getEntityAffiliationId(),
                                                          (empInfo.getEntityAffiliation() != null ? empInfo.getEntityAffiliation().getId() : null))) {
								PersonDocumentEmploymentInfo docEmpInfo = new PersonDocumentEmploymentInfo();
								docEmpInfo.setEntityEmploymentId(empInfo.getEmployeeId());
								docEmpInfo.setEmployeeId(empInfo.getEmployeeId());
								docEmpInfo.setEmploymentRecordId(empInfo.getEmploymentRecordId());
								docEmpInfo.setBaseSalaryAmount(empInfo.getBaseSalaryAmount());
								docEmpInfo.setPrimaryDepartmentCode(empInfo.getPrimaryDepartmentCode());
								docEmpInfo.setEmploymentStatusCode(empInfo.getEmployeeStatus() != null ? empInfo.getEmployeeStatus().getCode() : null);
								docEmpInfo.setEmploymentTypeCode(empInfo.getEmployeeType() != null ? empInfo.getEmployeeType().getCode() : null);
								docEmpInfo.setActive(empInfo.isActive());
								docEmpInfo.setPrimary(empInfo.isPrimary());
								docEmpInfo.setEntityAffiliationId(empInfo.getEntityAffiliation() != null ? empInfo.getEntityAffiliation().getId() : null);
								// there is no version number on KimEntityEmploymentInformationInfo
								//docEmpInfo.setVersionNumber(empInfo.getVersionNumber());
								docEmpInfo.setEdit(true);
								docEmpInfo.refreshReferenceObject("employmentType");
								docEmploymentInformations.add(docEmpInfo);
							}
						}
					}
					docAffiliation.setEmpInfos(docEmploymentInformations);
				}
			}
		}
		return docAffiliations;

	}

    
    protected List<PersonDocumentName> loadNames( IdentityManagementPersonDocument personDoc, String principalId, List <EntityName> names, boolean suppressDisplay ) {
		List<PersonDocumentName> docNames = new ArrayList<PersonDocumentName>();
		if(ObjectUtils.isNotNull(names)){
			for (EntityName name: names) {
				if(name.isActive()){
					PersonDocumentName docName = new PersonDocumentName();
                    if (name.getNameType() != null) {
					    docName.setNameCode(name.getNameType().getCode());
                    }

					//We do not need to check the privacy setting here - The UI should care of it
					docName.setFirstName(name.getFirstNameUnmasked());
					docName.setLastName(name.getLastNameUnmasked());
					docName.setMiddleName(name.getMiddleNameUnmasked());
					docName.setNamePrefix(name.getNamePrefixUnmasked());
					docName.setNameSuffix(name.getNameSuffixUnmasked());

					docName.setActive(name.isActive());
					docName.setDflt(name.isDefaultValue());
					docName.setEdit(true);
					docName.setEntityNameId(name.getId());
					docNames.add(docName);
				}
			}
		}
		return docNames;
	}

    protected List<PersonDocumentAddress> loadAddresses(IdentityManagementPersonDocument identityManagementPersonDocument, String principalId, List<EntityAddress> entityAddresses, boolean suppressDisplay ) {
		List<PersonDocumentAddress> docAddresses = new ArrayList<PersonDocumentAddress>();
		if(ObjectUtils.isNotNull(entityAddresses)){
			for (EntityAddress address: entityAddresses) {
				if(address.isActive()){
					PersonDocumentAddress docAddress = new PersonDocumentAddress();
					docAddress.setEntityTypeCode(address.getEntityTypeCode());
					docAddress.setAddressTypeCode(address.getAddressType().getCode());

					//We do not need to check the privacy setting here - The UI should care of it
					docAddress.setLine1(address.getLine1Unmasked());
					docAddress.setLine2(address.getLine2Unmasked());
					docAddress.setLine3(address.getLine3Unmasked());
					docAddress.setStateProvinceCode(address.getStateProvinceCodeUnmasked());
					docAddress.setPostalCode(address.getPostalCodeUnmasked());
					docAddress.setCountryCode(address.getCountryCodeUnmasked());
					docAddress.setCity(address.getCityUnmasked());

					docAddress.setActive(address.isActive());
					docAddress.setDflt(address.isDefaultValue());
					docAddress.setEntityAddressId(address.getId());
					docAddress.setEdit(true);
					docAddresses.add(docAddress);
				}
			}
		}
		return docAddresses;
	}

    protected List<PersonDocumentEmail> loadEmails(IdentityManagementPersonDocument identityManagementPersonDocument, String principalId, List<EntityEmail> entityEmails, boolean suppressDisplay ) {
		List<PersonDocumentEmail> emails = new ArrayList<PersonDocumentEmail>();
		if(ObjectUtils.isNotNull(entityEmails)){
			for (EntityEmail email: entityEmails) {
				if(email.isActive()){
					PersonDocumentEmail docEmail = new PersonDocumentEmail();
					//docEmail.setEntityId(email.getEntityId());
					docEmail.setEntityTypeCode(email.getEntityTypeCode());
                    if (email.getEmailType() != null) {
					    docEmail.setEmailTypeCode(email.getEmailType().getCode());
                    }
					// EmailType not on info object.
					//docEmail.setEmailType(((KimEntityEmailImpl)email).getEmailType());
					//We do not need to check the privacy setting here - The UI should care of it
					docEmail.setEmailAddress(email.getEmailAddressUnmasked());

					docEmail.setActive(email.isActive());
					docEmail.setDflt(email.isDefaultValue());
					docEmail.setEntityEmailId(email.getId());
					docEmail.setEdit(true);
					emails.add(docEmail);
				}
			}
		}
		return emails;
	}

    protected List<PersonDocumentPhone> loadPhones(IdentityManagementPersonDocument identityManagementPersonDocument, String principalId, List<EntityPhone> entityPhones, boolean suppressDisplay ) {
		List<PersonDocumentPhone> docPhones = new ArrayList<PersonDocumentPhone>();
		if(ObjectUtils.isNotNull(entityPhones)){
			for (EntityPhone phone: entityPhones) {
				if(phone.isActive()){
					PersonDocumentPhone docPhone = new PersonDocumentPhone();
                    if (phone.getPhoneType() != null) {
					    docPhone.setPhoneTypeCode(phone.getPhoneType().getCode());
                    }
					//docPhone.setPhoneType(((KimEntityPhoneImpl)phone).getPhoneType());
					docPhone.setEntityTypeCode(phone.getEntityTypeCode());
					//We do not need to check the privacy setting here - The UI should care of it
					docPhone.setPhoneNumber(phone.getPhoneNumberUnmasked());
					docPhone.setCountryCode(phone.getCountryCodeUnmasked());
					docPhone.setExtensionNumber(phone.getExtensionNumberUnmasked());

					docPhone.setActive(phone.isActive());
					docPhone.setDflt(phone.isDefaultValue());
					docPhone.setEntityPhoneId(phone.getId());
					docPhone.setEdit(true);
					docPhones.add(docPhone);
				}
			}
		}
		return docPhones;

	}

    public BusinessObject getMember(String memberTypeCode, String memberId){
        Class<? extends BusinessObject> roleMemberTypeClass = null;
        String roleMemberIdName = "";
    	if(MemberType.PRINCIPAL.getCode().equals(memberTypeCode)){
        	roleMemberTypeClass = PrincipalBo.class;
        	roleMemberIdName = KimConstants.PrimaryKeyConstants.PRINCIPAL_ID;
	 	 	Principal principalInfo = getIdentityService().getPrincipal(memberId);
	 	 	if (principalInfo != null) {
	 	 		
	 	 	}
        } else if(MemberType.GROUP.getCode().equals(memberTypeCode)){
        	roleMemberTypeClass = GroupBo.class;
        	roleMemberIdName = KimConstants.PrimaryKeyConstants.GROUP_ID;
        	Group groupInfo = null;
	 	 	groupInfo = getGroupService().getGroup(memberId);
	 	 	if (groupInfo != null) {
	 	 		
	 	 	}
        } else if(MemberType.ROLE.getCode().equals(memberTypeCode)){
        	roleMemberTypeClass = RoleBo.class;
        	roleMemberIdName = KimConstants.PrimaryKeyConstants.ROLE_ID;
	 	 	Role role = getRoleService().getRole(memberId);
	 	 	if (role != null) {
	 	 		
	 	 	}
        }
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put(roleMemberIdName, memberId);
        return getBusinessObjectService().findByPrimaryKey(roleMemberTypeClass, criteria);
    }

    /**
     * Overridden to only check permission - users should not be able to edit themselves.
     * 
     * @see org.kuali.rice.kim.service.impl.UiDocumentServiceImpl#canModifyEntity(java.lang.String, java.lang.String)
     */
    @Override
	public boolean canModifyEntity( String currentUserPrincipalId, String toModifyPrincipalId ){
		return (StringUtils.isNotBlank(currentUserPrincipalId) && StringUtils.isNotBlank(toModifyPrincipalId) &&
				currentUserPrincipalId.equals(toModifyPrincipalId)) ||
				getPermissionService().isAuthorized(
						currentUserPrincipalId,
						KimConstants.NAMESPACE_CODE,
						KimConstants.PermissionNames.MODIFY_ENTITY,
						Collections.singletonMap(KimConstants.AttributeConstants.PRINCIPAL_ID, currentUserPrincipalId));
	}
    
    protected List<RoleMemberBo> getRoleMembers(IdentityManagementRoleDocument identityManagementRoleDocument, List<RoleMemberBo> origRoleMembers){
        List<RoleMemberBo> roleMembers = new ArrayList<RoleMemberBo>();
        RoleMemberBo newRoleMember;
        RoleMemberBo origRoleMemberImplTemp;
        List<RoleMemberAttributeDataBo> origAttributes;
        boolean activatingInactive = false;
        String newRoleMemberIdAssigned = "";

        identityManagementRoleDocument.setKimType(KimApiServiceLocator.getKimTypeInfoService().getKimType(identityManagementRoleDocument.getRoleTypeId()));
        KimTypeService kimTypeService = KimFrameworkServiceLocator.getKimTypeService(identityManagementRoleDocument.getKimType());

        if(CollectionUtils.isNotEmpty(identityManagementRoleDocument.getMembers())){
            for(KimDocumentRoleMember documentRoleMember: identityManagementRoleDocument.getMembers()){
                origRoleMemberImplTemp = null;

                newRoleMember = new RoleMemberBo();
                KimCommonUtilsInternal.copyProperties(newRoleMember, documentRoleMember);
                newRoleMember.setRoleId(identityManagementRoleDocument.getRoleId());
                if(ObjectUtils.isNotNull(origRoleMembers)){
                    for(RoleMemberBo origRoleMemberImpl: origRoleMembers){
                        if((origRoleMemberImpl.getRoleId()!=null && StringUtils.equals(origRoleMemberImpl.getRoleId(), newRoleMember.getRoleId())) &&
                            (origRoleMemberImpl.getMemberId()!=null && StringUtils.equals(origRoleMemberImpl.getMemberId(), newRoleMember.getMemberId())) &&
                            (origRoleMemberImpl.getType()!=null && org.apache.commons.lang.ObjectUtils.equals(origRoleMemberImpl.getType(), newRoleMember.getType())) &&
                            !origRoleMemberImpl.isActive(new Timestamp(System.currentTimeMillis())) &&
                            !kimTypeService.validateUniqueAttributes(identityManagementRoleDocument.getKimType().getId(),
                                    documentRoleMember.getQualifierAsMap(), origRoleMemberImpl.getAttributes()).isEmpty()) {

                            //TODO: verify if you want to add  && newRoleMember.isActive() condition to if...

                            newRoleMemberIdAssigned = newRoleMember.getId();
                            newRoleMember.setId(origRoleMemberImpl.getId());
                            activatingInactive = true;
                        }
                        if(origRoleMemberImpl.getId()!=null && StringUtils.equals(origRoleMemberImpl.getId(), newRoleMember.getId())){
                            newRoleMember.setVersionNumber(origRoleMemberImpl.getVersionNumber());
                            origRoleMemberImplTemp = origRoleMemberImpl;
                        }
                    }
                }
                origAttributes = (origRoleMemberImplTemp==null || origRoleMemberImplTemp.getAttributes()==null)?
                                    new ArrayList<RoleMemberAttributeDataBo>():origRoleMemberImplTemp.getAttributeDetails();
                newRoleMember.setActiveFromDateValue(documentRoleMember.getActiveFromDate());
                newRoleMember.setActiveToDateValue(documentRoleMember.getActiveToDate());
                newRoleMember.setAttributeDetails(getRoleMemberAttributeData(documentRoleMember.getQualifiers(), origAttributes, activatingInactive, newRoleMemberIdAssigned));
                newRoleMember.setRoleRspActions(getRoleMemberResponsibilityActions(documentRoleMember, origRoleMemberImplTemp, activatingInactive, newRoleMemberIdAssigned));
                roleMembers.add(newRoleMember);
                activatingInactive = false;
            }
        }
        return roleMembers;
    }
}
