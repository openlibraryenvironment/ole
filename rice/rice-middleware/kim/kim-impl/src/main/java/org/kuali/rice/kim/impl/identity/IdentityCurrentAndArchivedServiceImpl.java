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
package org.kuali.rice.kim.impl.identity;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationType;
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenship;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.entity.EntityDefaultQueryResults;
import org.kuali.rice.kim.api.identity.entity.EntityQueryResults;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierType;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.personal.EntityBioDemographics;
import org.kuali.rice.kim.api.identity.personal.EntityEthnicity;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalQueryResults;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences;
import org.kuali.rice.kim.api.identity.residency.EntityResidency;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;
import org.kuali.rice.kim.api.identity.visa.EntityVisa;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This IdentityService implementation is largely just a knee-jerk delegator, except for
 * getters returning {@link EntityDefault} in which case the IdentityArchiveService
 * will be invoked if the inner IndentityService impl returns null.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class IdentityCurrentAndArchivedServiceImpl implements IdentityService {
	private final IdentityArchiveService identityArchiveService;
	private final IdentityService innerIdentityService;
	
	/**
	 * This constructs a IdentityCurrentAndArchivedServiceImpl, injecting the
	 * needed services.
	 */
	public IdentityCurrentAndArchivedServiceImpl(IdentityService innerIdentityService, 
			IdentityArchiveService identityArchiveService) {
		this.innerIdentityService = innerIdentityService;
		this.identityArchiveService = identityArchiveService;
	}
	
	/**
	 * @see org.kuali.rice.kim.api.identity.IdentityService#getAddressType(java.lang.String)
	 */
    @Override
	public CodedAttribute getAddressType(String code) {
		return getInnerIdentityService().getAddressType(code);
	}

    @Override
    public List<CodedAttribute> findAllAddressTypes() {
        return getInnerIdentityService().findAllAddressTypes();
    }

    @Override
	public EntityAffiliationType getAffiliationType(String code) {
		return getInnerIdentityService().getAffiliationType(code);
	}

    @Override
    public List<EntityAffiliationType> findAllAffiliationTypes() {
        return getInnerIdentityService().findAllAffiliationTypes();
    }

    /**
	 * @see org.kuali.rice.kim.api.identity.IdentityService#getCitizenshipStatus(java.lang.String)
	 */       
    @Override
	public CodedAttribute getCitizenshipStatus(String code) {
		return CodedAttribute.Builder.create(getInnerIdentityService().getCitizenshipStatus(code)).build();
	}

    @Override
    public List<CodedAttribute> findAllCitizenshipStatuses() {
        return getInnerIdentityService().findAllCitizenshipStatuses();
    }

    @Override
	public EntityNamePrincipalName getDefaultNamesForPrincipalId(String principalId) {
    	EntityNamePrincipalName name = getInnerIdentityService().getDefaultNamesForPrincipalId(principalId);
    	if(name == null || ObjectUtils.isNull(name.getDefaultName()) || StringUtils.isBlank(name.getPrincipalName()) || StringUtils.isBlank(name.getDefaultName().getCompositeName())) {
    		EntityDefault defaultEntity = this.getEntityDefaultByPrincipalId(principalId);
            if (defaultEntity != null) {
                EntityNamePrincipalName.Builder nameBuilder = EntityNamePrincipalName.Builder.create();
                for(Principal principal : defaultEntity.getPrincipals()) {
                    nameBuilder.setPrincipalName(principal.getPrincipalName());
                }
                nameBuilder.setDefaultName(EntityName.Builder.create(defaultEntity.getName()));
                if (StringUtils.isBlank(defaultEntity.getName().getCompositeName())) {
                    String formattedName = (defaultEntity.getName().getLastName() + ", " + defaultEntity.getName().getFirstName() + (defaultEntity.getName().getMiddleName()==null?"":" " + defaultEntity.getName().getMiddleName())).trim();
                    nameBuilder.getDefaultName().setCompositeName(formattedName);
                }
                return nameBuilder.build();
            }
        }
		return name;
	}

    @Override
    public EntityName addNameToEntity(EntityName name) {
        return getInnerIdentityService().addNameToEntity(name);
    }

    @Override
    public EntityName updateName(EntityName name) {
        return getInnerIdentityService().updateName(name);
    }

    @Override
    public EntityName inactivateName(String id) {
        return getInnerIdentityService().inactivateName(id);
    }

    @Override
    public EntityEmployment addEmploymentToEntity(EntityEmployment employment) {
        return getInnerIdentityService().addEmploymentToEntity(employment);
    }

    @Override
    public EntityEmployment updateEmployment(EntityEmployment employment) {
        return getInnerIdentityService().updateEmployment(employment);
    }

    @Override
    public EntityEmployment inactivateEmployment(String id) {
        return getInnerIdentityService().inactivateEmployment(id);
    }

    @Override
    public EntityBioDemographics addBioDemographicsToEntity(EntityBioDemographics bioDemographics) {
        return getInnerIdentityService().addBioDemographicsToEntity(bioDemographics);
    }

    @Override
    public EntityBioDemographics updateBioDemographics(EntityBioDemographics bioDemographics) {
        return getInnerIdentityService().updateBioDemographics(bioDemographics);
    }

	/**
	 * @see org.kuali.rice.kim.api.identity.IdentityService#getEmailType(java.lang.String)
	 */
    @Override
	public CodedAttribute getEmailType(String code) {
		return getInnerIdentityService().getEmailType(code);
	}

    @Override
    public List<CodedAttribute> findAllEmailTypes() {
        return getInnerIdentityService().findAllEmailTypes();
    }

    @Override
    public PrincipalQueryResults findPrincipals(QueryByCriteria query) throws RiceIllegalArgumentException {
        return getInnerIdentityService().findPrincipals(query);
    }

    /**
	 * @see org.kuali.rice.kim.api.identity.IdentityService#getEmploymentStatus(java.lang.String)
	 */
    @Override
	public CodedAttribute getEmploymentStatus(String code) {
		return getInnerIdentityService().getEmploymentStatus(code);
	}

    @Override
    public List<CodedAttribute> findAllEmploymentStatuses() {
        return getInnerIdentityService().findAllEmploymentStatuses();
    }

    /**
	 * @see org.kuali.rice.kim.api.identity.IdentityService#getEmploymentType(java.lang.String)
	 */
    @Override
	public CodedAttribute getEmploymentType(String code) {
		return getInnerIdentityService().getEmploymentType(code);
	}

    @Override
    public List<CodedAttribute> findAllEmploymentTypes() {
        return getInnerIdentityService().findAllEmploymentTypes();
    }

    /**
	 * This method first tries the inner IdentityService impl, and resorts to
	 * the IdentityArchiveService if need be.
	 */
    @Override
	public EntityDefault getEntityDefault(String entityId) {
		EntityDefault entity = getInnerIdentityService().getEntityDefault(entityId);
    	if ( entity == null ) {
    		entity = getIdentityArchiveService().getEntityDefaultFromArchive(entityId);
    	} else {
			getIdentityArchiveService().saveEntityDefaultToArchive(entity);
    	}
		return entity;
	}

	/**
	 * This method first tries the inner IdentityService impl, and resorts to
	 * the IdentityArchiveService if need be.
	 */
    @Override
	public EntityDefault getEntityDefaultByPrincipalId(String principalId) {
		EntityDefault entity = getInnerIdentityService().getEntityDefaultByPrincipalId(principalId);
    	if ( entity == null ) {
    		entity = getIdentityArchiveService().getEntityDefaultFromArchiveByPrincipalId(principalId);
    	} else {
			getIdentityArchiveService().saveEntityDefaultToArchive(entity);
    	}
    	return entity;
	}

	/**
	 * This method first tries the inner IdentityService impl, and resorts to
	 * the IdentityArchiveService if need be.
	 */
    @Override
	public EntityDefault getEntityDefaultByPrincipalName(String principalName) {
		EntityDefault entity = getInnerIdentityService().getEntityDefaultByPrincipalName(principalName);
    	if ( entity == null ) {
    		entity = getIdentityArchiveService().getEntityDefaultFromArchiveByPrincipalName(principalName);
    	} else {
			getIdentityArchiveService().saveEntityDefaultToArchive(entity);
    	}
    	return entity;
	}

    /**
     * This method first tries the inner IdentityService impl, and resorts to
     * the IdentityArchiveService if need be.
     */
    @Override
    public EntityDefault getEntityDefaultByEmployeeId(String employeeId) {
        EntityDefault entity = getInnerIdentityService().getEntityDefaultByEmployeeId(employeeId);
        if ( entity == null ) {
            entity = getIdentityArchiveService().getEntityDefaultFromArchiveByEmployeeId( employeeId );
        } else {
            getIdentityArchiveService().saveEntityDefaultToArchive(entity);
        }
        return entity;
    }

    @Override
    public EntityDefaultQueryResults findEntityDefaults( QueryByCriteria queryByCriteria) {
        return getInnerIdentityService().findEntityDefaults(queryByCriteria);
    }

    @Override
    public EntityQueryResults findEntities(QueryByCriteria queryByCriteria) {
        return getInnerIdentityService().findEntities(queryByCriteria);
    }

    @Override
	public Entity getEntity(String entityId) {
		return getInnerIdentityService().getEntity(entityId);
	}

    @Override
	public Entity getEntityByPrincipalId(String principalId) {
		return getInnerIdentityService().getEntityByPrincipalId(principalId);
	}

    @Override
	public Entity getEntityByPrincipalName(String principalName) {
		return getInnerIdentityService().getEntityByPrincipalName(principalName);
	}
    
    @Override
	public Entity getEntityByEmployeeId(String employeeId) {
		return getInnerIdentityService().getEntityByEmployeeId(employeeId);
	}

    @Override
    public Entity createEntity(Entity entity) {
        return getInnerIdentityService().createEntity(entity);
    }

    @Override
    public Entity updateEntity(Entity entity) {
        return getInnerIdentityService().updateEntity(entity);
    }

    @Override
    public Entity inactivateEntity(String entityId) {
        return getInnerIdentityService().inactivateEntity(entityId);
    }

    /**
	 * @see org.kuali.rice.kim.api.identity.IdentityService#getNameType(java.lang.String)
	 */
    @Override
	public CodedAttribute getNameType(String code) {
		return getInnerIdentityService().getNameType(code);
	}

    @Override
    public List<CodedAttribute> findAllNameTypes() {
        return getInnerIdentityService().findAllNameTypes();
    }

    @Override
	public EntityPrivacyPreferences getEntityPrivacyPreferences(
			String entityId) {
		return getInnerIdentityService().getEntityPrivacyPreferences(entityId);
	}

    @Override
    public EntityPrivacyPreferences addPrivacyPreferencesToEntity(EntityPrivacyPreferences privacyPreferences) {
        return getInnerIdentityService().addPrivacyPreferencesToEntity(privacyPreferences);
    }

    @Override
    public EntityPrivacyPreferences updatePrivacyPreferences(EntityPrivacyPreferences privacyPreferences) {
        return getInnerIdentityService().updatePrivacyPreferences(privacyPreferences);
    }

    @Override
    public EntityCitizenship addCitizenshipToEntity(EntityCitizenship citizenship) {
        return getInnerIdentityService().addCitizenshipToEntity(citizenship);
    }

    @Override
    public EntityCitizenship updateCitizenship(EntityCitizenship citizenship) {
        return getInnerIdentityService().updateCitizenship(citizenship);
    }

    @Override
    public EntityCitizenship inactivateCitizenship(String id) {
        return getInnerIdentityService().inactivateCitizenship(id);
    }

    @Override
    public EntityEthnicity addEthnicityToEntity(EntityEthnicity ethnicity) {
        return getInnerIdentityService().addEthnicityToEntity(ethnicity);
    }

    @Override
    public EntityEthnicity updateEthnicity(EntityEthnicity ethnicity) {
        return getInnerIdentityService().updateEthnicity(ethnicity);
    }

    @Override
    public EntityResidency addResidencyToEntity(EntityResidency residency) {
        return getInnerIdentityService().addResidencyToEntity(residency);
    }

    @Override
    public EntityResidency updateResidency(EntityResidency residency) {
        return getInnerIdentityService().updateResidency(residency);
    }

    @Override
    public EntityVisa addVisaToEntity(EntityVisa visa) {
        return getInnerIdentityService().addVisaToEntity(visa);
    }

    @Override
    public EntityVisa updateVisa(EntityVisa visa) {
        return getInnerIdentityService().updateVisa(visa);
    }

    /**
	 * @see org.kuali.rice.kim.api.identity.IdentityService#getEntityType(java.lang.String)
	 */
    @Override
	public CodedAttribute getEntityType(String code) {
		return getInnerIdentityService().getEntityType(code);
	}

    @Override
    public List<CodedAttribute> findAllEntityTypes() {
        return getInnerIdentityService().findAllEntityTypes();
    }

    @Override
	public EntityExternalIdentifierType getExternalIdentifierType(String code) {
		return getInnerIdentityService().getExternalIdentifierType(code);
	}

    @Override
    public List<EntityExternalIdentifierType> findAllExternalIdendtifierTypes() {
        return getInnerIdentityService().findAllExternalIdendtifierTypes();
    }

    /**
	 * @see org.kuali.rice.kim.api.identity.IdentityService#getPhoneType(java.lang.String)
	 */
    @Override
	public CodedAttribute getPhoneType(String code) {
		return getInnerIdentityService().getPhoneType(code);
	}

    @Override
    public List<CodedAttribute> findAllPhoneTypes() {
        return getInnerIdentityService().findAllPhoneTypes();
    }

    @Override
	public Principal getPrincipal(String principalId) {
        Principal principal = getInnerIdentityService().getPrincipal(principalId);
        if ( principal == null ) {
            EntityDefault entity = getEntityDefaultByPrincipalId(principalId);
            if ( entity != null ) {
                List<Principal> principals = entity.getPrincipals();
                if ( principals != null && !principals.isEmpty() ) {
                    principal = principals.get(0);
                }
            }
        }
        return principal;
    }

    /**
     * Gets a list of {@link org.kuali.rice.kim.api.identity.principal.Principal} from a string list of principalId.
     *
     * <p>
     * This method will only return principals that exist.  It will return null if the none of the principals exist.
     * </p>
     *
     * @param principalIds the unique id to retrieve the principal by. cannot be null.
     * @return a list of {@link org.kuali.rice.kim.api.identity.principal.Principal} or null
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if the principalId is blank
     */
    @Override
    public List<Principal> getPrincipals(@WebParam(name = "principalIds") List<String> principalIds) {
        List<Principal> ret = new ArrayList<Principal>();
        for(String p: principalIds) {
            Principal principalInfo = getPrincipal(p);

            if (principalInfo != null) {
                ret.add(principalInfo) ;
            }
        }
        if (!ret.isEmpty()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
	public Principal getPrincipalByPrincipalName(String principalName) {
        Principal principal = getInnerIdentityService().getPrincipalByPrincipalName(principalName);
        if ( principal == null ) {
            EntityDefault entity = getEntityDefaultByPrincipalName(principalName);
            if ( entity != null ) {
                List<Principal> principals = entity.getPrincipals();
                if ( principals != null && !principals.isEmpty() ) {
                    principal = principals.get(0);
                }
            }
        }
        return principal;
	}

    @Override
    public List<Principal> getPrincipalsByEntityId(String entityId) {
        List<Principal> principals = getInnerIdentityService().getPrincipalsByEntityId(entityId);
        if ( principals == null ) {
            EntityDefault entity = getIdentityArchiveService().getEntityDefaultFromArchive(entityId);
            if (entity != null && entity.getPrincipals() != null && !entity.getPrincipals().isEmpty() ) {
                principals = entity.getPrincipals();
            }
        }
        return principals;
    }

    @Override
    public List<Principal> getPrincipalsByEmployeeId(String employeeId) {
        List<Principal> principals = getInnerIdentityService().getPrincipalsByEmployeeId(employeeId);
        if ( principals == null ) {
            EntityDefault entity = getIdentityArchiveService().getEntityDefaultFromArchive(employeeId);
            if (entity != null && entity.getPrincipals() != null && !entity.getPrincipals().isEmpty() ) {
                principals = entity.getPrincipals();
            }
        }
        return principals;
    }

    @Override
	public Principal getPrincipalByPrincipalNameAndPassword(
			String principalName, String password) {
		return getInnerIdentityService().getPrincipalByPrincipalNameAndPassword(
				principalName, password);
	}

    @Override
    public Principal addPrincipalToEntity(Principal principal) {
        return getInnerIdentityService().addPrincipalToEntity(principal);
    }

    @Override
    public Principal updatePrincipal(Principal principal) {
        return getInnerIdentityService().updatePrincipal(principal);
    }

    @Override
    public Principal inactivatePrincipal(String principalId) {
        return getInnerIdentityService().inactivatePrincipal(principalId);
    }

    @Override
    public Principal inactivatePrincipalByName(String principalName) {
        return getInnerIdentityService().inactivatePrincipalByName(principalName);
    }

    @Override
    public EntityTypeContactInfo addEntityTypeContactInfoToEntity(EntityTypeContactInfo entityTypeContactInfo) {
        return getInnerIdentityService().addEntityTypeContactInfoToEntity(entityTypeContactInfo);
    }

    @Override
    public EntityTypeContactInfo updateEntityTypeContactInfo(EntityTypeContactInfo entityTypeContactInfo) {
        return getInnerIdentityService().updateEntityTypeContactInfo(entityTypeContactInfo);
    }

    @Override
    public EntityTypeContactInfo inactivateEntityTypeContactInfo(String entityId, String entityTypeCode) {
        return getInnerIdentityService().inactivateEntityTypeContactInfo(entityId, entityTypeCode);
    }

    @Override
    public EntityAddress addAddressToEntity(EntityAddress address) {
        return getInnerIdentityService().addAddressToEntity(address);
    }

    @Override
    public EntityAddress updateAddress(EntityAddress address) {
        return getInnerIdentityService().updateAddress(address);
    }

    @Override
    public EntityAddress inactivateAddress(String addressId) {
        return getInnerIdentityService().inactivateAddress(addressId);
    }

    @Override
    public EntityEmail addEmailToEntity(EntityEmail email) {
        return getInnerIdentityService().addEmailToEntity(email);
    }

    @Override
    public EntityEmail updateEmail(EntityEmail email) {
        return getInnerIdentityService().updateEmail(email);
    }

    @Override
    public EntityEmail inactivateEmail(String emailId) {
        return getInnerIdentityService().inactivateEmail(emailId);
    }

    @Override
    public EntityPhone addPhoneToEntity(EntityPhone phone) {
        return getInnerIdentityService().addPhoneToEntity(phone);
    }

    @Override
    public EntityPhone updatePhone(EntityPhone phone) {
        return getInnerIdentityService().updatePhone(phone);
    }

    @Override
    public EntityPhone inactivatePhone(String phoneId) {
        return getInnerIdentityService().inactivatePhone(phoneId);
    }

    @Override
    public EntityExternalIdentifier addExternalIdentifierToEntity(EntityExternalIdentifier externalId) {
        return getInnerIdentityService().addExternalIdentifierToEntity(externalId);
    }

    @Override
    public EntityExternalIdentifier updateExternalIdentifier(EntityExternalIdentifier externalId) {
        return getInnerIdentityService().updateExternalIdentifier(externalId);
    }

    @Override
    public EntityAffiliation addAffiliationToEntity(EntityAffiliation affiliation) {
        return getInnerIdentityService().addAffiliationToEntity(affiliation);
    }

    @Override
    public EntityAffiliation updateAffiliation(EntityAffiliation affiliation) {
        return getInnerIdentityService().updateAffiliation(affiliation);
    }

    @Override
    public EntityAffiliation inactivateAffiliation(String id) {
        return getInnerIdentityService().inactivateAffiliation(id);
    }


	private IdentityService getInnerIdentityService() {
		return innerIdentityService;
	}
	
	private IdentityArchiveService getIdentityArchiveService() {
		return identityArchiveService;
	}
}
