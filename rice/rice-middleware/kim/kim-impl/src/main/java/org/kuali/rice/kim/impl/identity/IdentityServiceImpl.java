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
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.IdentityService;
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
import org.kuali.rice.kim.api.identity.name.EntityNameQueryResults;
import org.kuali.rice.kim.api.identity.personal.EntityBioDemographics;
import org.kuali.rice.kim.api.identity.personal.EntityEthnicity;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalQueryResults;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences;
import org.kuali.rice.kim.api.identity.residency.EntityResidency;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;
import org.kuali.rice.kim.api.identity.visa.EntityVisa;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.address.EntityAddressTypeBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationTypeBo;
import org.kuali.rice.kim.impl.identity.citizenship.EntityCitizenshipBo;
import org.kuali.rice.kim.impl.identity.citizenship.EntityCitizenshipStatusBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailTypeBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentStatusBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentTypeBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.external.EntityExternalIdentifierBo;
import org.kuali.rice.kim.impl.identity.external.EntityExternalIdentifierTypeBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameTypeBo;
import org.kuali.rice.kim.impl.identity.personal.EntityBioDemographicsBo;
import org.kuali.rice.kim.impl.identity.personal.EntityEthnicityBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneTypeBo;
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.kim.impl.identity.privacy.EntityPrivacyPreferencesBo;
import org.kuali.rice.kim.impl.identity.residency.EntityResidencyBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.kim.impl.identity.visa.EntityVisaBo;
import org.kuali.rice.kim.impl.services.KimImplServiceLocator;
import org.kuali.rice.krad.service.BusinessObjectService;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base implementation of the identity (identity) service.  This version assumes the KimEntity
 * and related data is located within the KIM database. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class IdentityServiceImpl implements IdentityService {

    private CriteriaLookupService criteriaLookupService;
	private BusinessObjectService businessObjectService;

    private static final String UNAVAILABLE = "Unavailable";

    @Override
	public Entity getEntity(String entityId) throws RiceIllegalArgumentException {
        incomingParamCheck(entityId, "entityId");

		EntityBo entity = getEntityBo( entityId );
		if ( entity == null ) {
			return null;
		}
		return EntityBo.to( entity );
	}
	
    @Override
	public Entity getEntityByPrincipalId(String principalId) throws RiceIllegalArgumentException {
        incomingParamCheck(principalId, "principalId");

		EntityBo entity = getEntityBoByPrincipalId(principalId);
		if ( entity == null ) {
			return null;
		}
		return EntityBo.to(entity);
	}
	
    @Override
	public Entity getEntityByPrincipalName(String principalName) throws RiceIllegalArgumentException{
        incomingParamCheck(principalName, "principalName");

		EntityBo entity = getEntityBoByPrincipalName(principalName);
		if ( entity == null ) {
			return null;
		}
		return EntityBo.to(entity);
	}
    
    @Override
	public Entity getEntityByEmployeeId(String employeeId) throws RiceIllegalArgumentException{
        incomingParamCheck(employeeId, "employeeId");

		EntityBo entity = getEntityBoByEmployeeId(employeeId);
		if ( entity == null ) {
			return null;
		}
		return EntityBo.to(entity);
	}
	
    @Override
	public EntityDefault getEntityDefault(String entityId) throws RiceIllegalArgumentException {
        incomingParamCheck(entityId, "entityId");

		EntityBo entity = getEntityBo( entityId );
		if ( entity == null ) {
			return null;
		}
		return EntityBo.toDefault( entity );
	}
	
    @Override
	public EntityDefault getEntityDefaultByPrincipalId(String principalId) throws RiceIllegalArgumentException {
        incomingParamCheck(principalId, "principalId");

		EntityBo entity = getEntityBoByPrincipalId(principalId);
		if ( entity == null ) {
			return null;
		}
		return EntityBo.toDefault(entity);
	}
	
    @Override
	public EntityDefault getEntityDefaultByPrincipalName(String principalName) throws RiceIllegalArgumentException {
        incomingParamCheck(principalName, "principalName");

		EntityBo entity = getEntityBoByPrincipalName(principalName);
		if ( entity == null ) {
			return null;
		}
		return EntityBo.toDefault(entity);
	}
	
    @Override
    public EntityDefault getEntityDefaultByEmployeeId(String employeeId) throws RiceIllegalArgumentException {
        incomingParamCheck(employeeId, "employeeId");

        EntityBo entity = getEntityBoByEmployeeId(employeeId);
        if ( entity == null ) {
            return null;
        }
        return EntityBo.toDefault(entity);
    }
    
    @Override
	public Principal getPrincipalByPrincipalNameAndPassword(String principalName, String password) throws RiceIllegalArgumentException {
        incomingParamCheck(principalName, "principalName");
        incomingParamCheck(password, "password");

		Map<String,Object> criteria = new HashMap<String,Object>(3);
        criteria.put(KIMPropertyConstants.Principal.PRINCIPAL_NAME, principalName);
        criteria.put(KIMPropertyConstants.Principal.PASSWORD, password);
        criteria.put(KIMPropertyConstants.Principal.ACTIVE, Boolean.TRUE);
        Collection<PrincipalBo> principals = businessObjectService.findMatching(PrincipalBo.class, criteria);

        if (!principals.isEmpty()) {
            return PrincipalBo.to(principals.iterator().next());
        }
        return null;
	}

    @Override
    public Principal addPrincipalToEntity(Principal principal) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(principal, "principal");

        if (StringUtils.isEmpty(principal.getEntityId()) || StringUtils.isBlank(principal.getEntityId())
                || StringUtils.isEmpty(principal.getPrincipalName()) || StringUtils.isBlank(principal.getPrincipalName())) {
            throw new RiceIllegalStateException("Principal's entityId and PrincipalName must be populated before creation");
        }  else {
            if (getPrincipalByPrincipalName(principal.getPrincipalName()) != null) {
                throw new RiceIllegalStateException("the Principal to create already exists: " + principal);
            }
        }
        PrincipalBo bo = PrincipalBo.from(principal);
        return PrincipalBo.to(businessObjectService.save(bo));
    }

    @Override
    public Principal updatePrincipal(Principal principal) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(principal, "principal");
        PrincipalBo originalPrincipal = null;
        if (StringUtils.isEmpty(principal.getEntityId()) || StringUtils.isBlank(principal.getEntityId())
                || StringUtils.isEmpty(principal.getPrincipalName()) || StringUtils.isBlank(principal.getPrincipalName())) {
            throw new RiceIllegalStateException("Principal's entityId and PrincipalName must be populated before update");
        }  else {
             originalPrincipal = getPrincipalBoByPrincipalName(principal.getPrincipalName());
            if (StringUtils.isEmpty(principal.getPrincipalId()) || originalPrincipal == null) {
                throw new RiceIllegalStateException("the Principal to update does not exist: " + principal);
            }
        }
        
        PrincipalBo bo = PrincipalBo.from(principal);
        //Password is not set on the principal DTO, so we need to make sure the value is kept from existing principal
        bo.setPassword(originalPrincipal.getPassword());
        PrincipalBo updatedPrincipal = businessObjectService.save(bo);
        if (originalPrincipal.isActive()
                && !updatedPrincipal.isActive()) {
            KimImplServiceLocator.getRoleInternalService().principalInactivated(updatedPrincipal.getPrincipalId());
        }
        return PrincipalBo.to(updatedPrincipal);
    }

    @Override
    public Principal inactivatePrincipal(String principalId) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(principalId, "principalId");

        Principal principal = getPrincipal(principalId);
        if (principal == null) {
            throw new RiceIllegalStateException("Principal with principalId: " + principalId + " does not exist");
        }
        PrincipalBo bo = PrincipalBo.from(principal);
        bo.setActive(false);
        return PrincipalBo.to(businessObjectService.save(bo));
    }

    @Override
    public Principal inactivatePrincipalByName(String principalName) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(principalName, "principalName");

        Principal principal = getPrincipalByPrincipalName(principalName);
        if (principal == null) {
            throw new RiceIllegalStateException("Principal with principalName: " + principalName + " does not exist");
        }
        PrincipalBo bo = PrincipalBo.from(principal);
        bo.setActive(false);
        return PrincipalBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityTypeContactInfo addEntityTypeContactInfoToEntity(EntityTypeContactInfo entityTypeData) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(entityTypeData, "entityTypeData");

        if (StringUtils.isEmpty(entityTypeData.getEntityId()) || StringUtils.isBlank(entityTypeData.getEntityId())
                || StringUtils.isEmpty(entityTypeData.getEntityTypeCode()) || StringUtils.isBlank(entityTypeData.getEntityTypeCode())) {
            throw new RiceIllegalStateException("EntityTypeData's entityId and entityTypeCode must be populated before creation");
        }  else {
            if (getEntityTypeDataBo(entityTypeData.getEntityId(), entityTypeData.getEntityTypeCode()) != null) {
                throw new RiceIllegalStateException("the entityTypeData to create already exists: " + entityTypeData);
            }
        }
        EntityTypeContactInfoBo bo = EntityTypeContactInfoBo.from(entityTypeData);
        return EntityTypeContactInfoBo.to(businessObjectService.save(bo));
    }

    private EntityTypeContactInfoBo getEntityTypeDataBo(String entityId, String entityTypeCode) {
        Map<String,Object> criteria = new HashMap<String,Object>(3);
         criteria.put(KIMPropertyConstants.Entity.ENTITY_ID, entityId);
         criteria.put(KIMPropertyConstants.Entity.ENTITY_TYPE_CODE, entityTypeCode);
         criteria.put(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
         return businessObjectService.findByPrimaryKey(EntityTypeContactInfoBo.class, criteria);
    }

    @Override
    public EntityTypeContactInfo updateEntityTypeContactInfo(EntityTypeContactInfo entityTypeContactInfo) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(entityTypeContactInfo, "entityTypeContactInfo");

        if (StringUtils.isBlank(entityTypeContactInfo.getEntityId()) || StringUtils.isEmpty(entityTypeContactInfo.getEntityId())
                || StringUtils.isBlank(entityTypeContactInfo.getEntityTypeCode()) || StringUtils.isEmpty(entityTypeContactInfo.getEntityTypeCode())) {
            throw new RiceIllegalStateException("EntityTypeData's entityId and entityTypeCode must be populated before update");
        }  else {
            if (getEntityTypeDataBo(entityTypeContactInfo.getEntityId(), entityTypeContactInfo.getEntityTypeCode()) == null) {
                throw new RiceIllegalStateException("the entityTypeData to update does not exist: " + entityTypeContactInfo);
            }
        }
        EntityTypeContactInfoBo bo = EntityTypeContactInfoBo.from(entityTypeContactInfo);
        return EntityTypeContactInfoBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityTypeContactInfo inactivateEntityTypeContactInfo(String entityId, String entityTypeCode) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(entityId, "entityId");
        incomingParamCheck(entityTypeCode, "entityTypeCode");

        EntityTypeContactInfoBo bo = getEntityTypeDataBo(entityId, entityTypeCode);
        if (bo == null) {
            throw new RiceIllegalStateException("EntityTypeData with entityId: " + entityId + " entityTypeCode: " + entityTypeCode + " does not exist");
        }
        bo.setActive(false);
        return EntityTypeContactInfoBo.to(businessObjectService.save(bo));
    }

    private EntityAddressBo getEntityAddressBo(String entityId, String entityTypeCode, String addressTypeCode) {
        Map<String,Object> criteria = new HashMap<String,Object>(4);
        criteria.put(KIMPropertyConstants.Entity.ENTITY_ID, entityId);
        criteria.put(KIMPropertyConstants.Entity.ENTITY_TYPE_CODE, entityTypeCode);
        criteria.put("addressTypeCode", addressTypeCode);
        criteria.put(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        return businessObjectService.findByPrimaryKey(EntityAddressBo.class, criteria);
    }

    private EntityAddressBo getEntityAddressBo(String addressId) {
        Map<String,Object> criteria = new HashMap<String,Object>(4);
        criteria.put(KIMPropertyConstants.Entity.ID, addressId);
        return businessObjectService.findByPrimaryKey(EntityAddressBo.class, criteria);
    }

    @Override
    public EntityAddress addAddressToEntity(EntityAddress address) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(address, "address");

        if (StringUtils.isEmpty(address.getEntityId()) || StringUtils.isBlank(address.getEntityId())
                || StringUtils.isEmpty(address.getEntityTypeCode()) || StringUtils.isBlank(address.getEntityTypeCode())) {
            throw new RiceIllegalStateException("Address's entityId and entityTypeCode must be populated before creation");
        }  else {
            if (address.getAddressType() == null) {
                throw new RiceIllegalStateException("Address's type must be populated before creation");
            }
            if (getEntityAddressBo(address.getEntityId(), address.getEntityTypeCode(), address.getAddressType().getCode()) != null) {
                throw new RiceIllegalStateException("the address to create already exists: " + address);
            }
        }
        EntityAddressBo bo = EntityAddressBo.from(address);
        return EntityAddressBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityAddress updateAddress(EntityAddress address) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(address, "address");

        if (StringUtils.isEmpty(address.getEntityId()) || StringUtils.isBlank(address.getEntityId())
                || StringUtils.isEmpty(address.getEntityTypeCode()) || StringUtils.isBlank(address.getEntityTypeCode())) {
            throw new RiceIllegalStateException("Address's entityId and entityTypeCode must be populated before creation");
        }  else {
            if (address.getAddressType() == null) {
                throw new RiceIllegalStateException("Address's type must be populated before creation");
            }
            if (StringUtils.isEmpty(address.getId())
                  ||  getEntityAddressBo(address.getEntityId(), address.getEntityTypeCode(), address.getAddressType().getCode()) == null) {
                throw new RiceIllegalStateException("the address to update does not exists: " + address);
            }
        }
        EntityAddressBo bo = EntityAddressBo.from(address);
        return EntityAddressBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityAddress inactivateAddress(String addressId) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(addressId, "addressId");

        EntityAddressBo bo = getEntityAddressBo(addressId);
        if (bo == null) {
            throw new RiceIllegalStateException("Address with addressId: " + addressId + " does not exist");
        }
        bo.setActive(false);
        return EntityAddressBo.to(businessObjectService.save(bo));
    }

    private EntityEmailBo getEntityEmailBo(String entityId, String entityTypeCode, String emailTypeCode) {
        Map<String,Object> criteria = new HashMap<String,Object>(4);
        criteria.put(KIMPropertyConstants.Entity.ENTITY_ID, entityId);
        criteria.put(KIMPropertyConstants.Entity.ENTITY_TYPE_CODE, entityTypeCode);
        criteria.put("emailTypeCode", emailTypeCode);
        criteria.put(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        return businessObjectService.findByPrimaryKey(EntityEmailBo.class, criteria);
    }

    private EntityEmailBo getEntityEmailBo(String emailId) {
        Map<String,Object> criteria = new HashMap<String,Object>(4);
        criteria.put(KIMPropertyConstants.Entity.ID, emailId);
        return businessObjectService.findByPrimaryKey(EntityEmailBo.class, criteria);
    }
    @Override
    public EntityEmail addEmailToEntity(EntityEmail email) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(email, "email");

        if (StringUtils.isEmpty(email.getEntityId()) || StringUtils.isBlank(email.getEntityId())
                || StringUtils.isEmpty(email.getEntityTypeCode()) || StringUtils.isBlank(email.getEntityTypeCode())) {
            throw new RiceIllegalStateException("Email's entityId and entityTypeCode must be populated before creation");
        }  else {
            if (email.getEmailType() == null) {
                throw new RiceIllegalStateException("Email's type must be populated before creation");
            }
            if (getEntityEmailBo(email.getEntityId(), email.getEntityTypeCode(), email.getEmailType().getCode()) != null) {
                throw new RiceIllegalStateException("the email to create already exists: " + email);
            }
        }
        EntityEmailBo bo = EntityEmailBo.from(email);
        return EntityEmailBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityEmail updateEmail(EntityEmail email) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(email, "email");

        if (StringUtils.isEmpty(email.getEntityId()) || StringUtils.isBlank(email.getEntityId())
                || StringUtils.isEmpty(email.getEntityTypeCode()) || StringUtils.isBlank(email.getEntityTypeCode())) {
            throw new RiceIllegalStateException("Email's entityId and entityTypeCode must be populated before creation");
        }  else {
            if (email.getEmailType() == null) {
                throw new RiceIllegalStateException("Email's type must be populated before creation");
            }
            if (StringUtils.isEmpty(email.getId())
                  ||  getEntityEmailBo(email.getEntityId(), email.getEntityTypeCode(), email.getEmailType().getCode()) == null) {
                throw new RiceIllegalStateException("the email to update does not exists: " + email);
            }
        }
        EntityEmailBo bo = EntityEmailBo.from(email);
        return EntityEmailBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityEmail inactivateEmail(String emailId) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(emailId, "emailId");

        EntityEmailBo bo = getEntityEmailBo(emailId);
        if (bo == null) {
            throw new RiceIllegalStateException("Email with emailId: " + emailId + " does not exist");
        }
        bo.setActive(false);
        return EntityEmailBo.to(businessObjectService.save(bo));
    }

    private EntityPhoneBo getEntityPhoneBo(String entityId, String entityTypeCode, String phoneTypeCode) {
        Map<String,Object> criteria = new HashMap<String,Object>(4);
        criteria.put(KIMPropertyConstants.Entity.ENTITY_ID, entityId);
        criteria.put(KIMPropertyConstants.Entity.ENTITY_TYPE_CODE, entityTypeCode);
        criteria.put("phoneTypeCode", phoneTypeCode);
        criteria.put(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        return businessObjectService.findByPrimaryKey(EntityPhoneBo.class, criteria);
    }

    private EntityPhoneBo getEntityPhoneBo(String phoneId) {
        Map<String,Object> criteria = new HashMap<String,Object>(4);
        criteria.put(KIMPropertyConstants.Entity.ID, phoneId);
        return businessObjectService.findByPrimaryKey(EntityPhoneBo.class, criteria);
    }

    @Override
    public EntityPhone addPhoneToEntity(EntityPhone phone) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(phone, "phone");

        if (StringUtils.isEmpty(phone.getEntityId()) || StringUtils.isBlank(phone.getEntityId())
                || StringUtils.isEmpty(phone.getEntityTypeCode()) || StringUtils.isBlank(phone.getEntityTypeCode())) {
            throw new RiceIllegalStateException("Phone's entityId and entityTypeCode must be populated before creation");
        }  else {
            if (phone.getPhoneType() == null) {
                throw new RiceIllegalStateException("Phone's type must be populated before creation");
            }
            if (getEntityPhoneBo(phone.getEntityId(), phone.getEntityTypeCode(), phone.getPhoneType().getCode()) != null) {
                throw new RiceIllegalStateException("the phone to create already exists: " + phone);
            }
        }
        EntityPhoneBo bo = EntityPhoneBo.from(phone);
        return EntityPhoneBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityPhone updatePhone(EntityPhone phone) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(phone, "phone");

        if (StringUtils.isEmpty(phone.getEntityId()) || StringUtils.isBlank(phone.getEntityId())
                || StringUtils.isEmpty(phone.getEntityTypeCode()) || StringUtils.isBlank(phone.getEntityTypeCode())) {
            throw new RiceIllegalStateException("Phone's entityId and entityTypeCode must be populated before creation");
        }  else {
            if (phone.getPhoneType() == null) {
                throw new RiceIllegalStateException("Phone's type must be populated before creation");
            }
            if (StringUtils.isEmpty(phone.getId())
                  ||  getEntityPhoneBo(phone.getEntityId(), phone.getEntityTypeCode(), phone.getPhoneType().getCode()) == null) {
                throw new RiceIllegalStateException("the phone to update does not exists: " + phone);
            }
        }
        EntityPhoneBo bo = EntityPhoneBo.from(phone);
        return EntityPhoneBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityPhone inactivatePhone(String phoneId) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(phoneId, "phoneId");

        EntityPhoneBo bo = getEntityPhoneBo(phoneId);
        if (bo == null) {
            throw new RiceIllegalStateException("Phone with phoneId: " + phoneId + " does not exist");
        }
        bo.setActive(false);
        return EntityPhoneBo.to(businessObjectService.save(bo));
    }


    private EntityExternalIdentifierBo getEntityExternalIdentifierBo(String entityId, String externalIdentifierTypeCode) {
        Map<String,Object> criteria = new HashMap<String,Object>(4);
        criteria.put(KIMPropertyConstants.Entity.ENTITY_ID, entityId);
        criteria.put("externalIdentifierTypeCode", externalIdentifierTypeCode);
        return businessObjectService.findByPrimaryKey(EntityExternalIdentifierBo.class, criteria);
    }

    @Override
    public EntityExternalIdentifier addExternalIdentifierToEntity(EntityExternalIdentifier externalId) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(externalId, "externalId");

        if (StringUtils.isEmpty(externalId.getEntityId()) || StringUtils.isBlank(externalId.getEntityId())
                || StringUtils.isEmpty(externalId.getExternalIdentifierTypeCode()) || StringUtils.isBlank(externalId.getExternalIdentifierTypeCode())) {
            throw new RiceIllegalStateException("EntityExternalIdentifier's entityId and entityTypeCode must be populated before creation");
        }  else {
            if (getEntityExternalIdentifierBo(externalId.getEntityId(), externalId.getExternalIdentifierTypeCode()) != null) {
                throw new RiceIllegalStateException("the EntityExternalIdentifier to create already exists: " + externalId);
            }
        }
        EntityExternalIdentifierBo bo = EntityExternalIdentifierBo.from(externalId);
        return EntityExternalIdentifierBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityExternalIdentifier updateExternalIdentifier(EntityExternalIdentifier externalId) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(externalId, "externalId");

        if (StringUtils.isEmpty(externalId.getEntityId()) || StringUtils.isBlank(externalId.getEntityId())
                || StringUtils.isEmpty(externalId.getExternalIdentifierTypeCode()) || StringUtils.isBlank(externalId.getExternalIdentifierTypeCode())) {
            throw new RiceIllegalStateException("EntityExternalIdentifier's entityId and externalIdentifierTypeCode must be populated before creation");
        }  else {
            if (StringUtils.isEmpty(externalId.getId())
                  ||  getEntityExternalIdentifierBo(externalId.getEntityId(), externalId.getExternalIdentifierTypeCode()) == null) {
                throw new RiceIllegalStateException("the external identifier to update does not exist: " + externalId);
            }
        }
        EntityExternalIdentifierBo bo = EntityExternalIdentifierBo.from(externalId);
        return EntityExternalIdentifierBo.to(businessObjectService.save(bo));
    }


    private EntityAffiliationBo getEntityAffiliationBo(String id) {
        Map<String,Object> criteria = new HashMap<String,Object>();
        criteria.put(KIMPropertyConstants.Entity.ID, id);
        return businessObjectService.findByPrimaryKey(EntityAffiliationBo.class, criteria);
    }

    @Override
    public EntityAffiliation addAffiliationToEntity(EntityAffiliation affiliation) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(affiliation, "affiliation");

        if (StringUtils.isEmpty(affiliation.getEntityId()) || StringUtils.isBlank(affiliation.getEntityId())) {
            throw new RiceIllegalStateException("Affiliation's entityId must be populated before creation");
        }  else {
            if (affiliation.getAffiliationType() == null) {
                throw new RiceIllegalStateException("EntityAffiliation's type must be populated before creation");
            }
            if (getEntityAffiliationBo(affiliation.getId()) != null) {
                throw new RiceIllegalStateException("the EntityAffiliation to create already exists: " + affiliation);
            }
        }
        EntityAffiliationBo bo = EntityAffiliationBo.from(affiliation);
        return EntityAffiliationBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityAffiliation updateAffiliation(EntityAffiliation affiliation) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(affiliation, "affiliation");

        if (StringUtils.isEmpty(affiliation.getEntityId()) || StringUtils.isBlank(affiliation.getEntityId())) {
            throw new RiceIllegalStateException("Affiliation's entityId must be populated before creation");
        }  else {
            if (affiliation.getAffiliationType() == null) {
                throw new RiceIllegalStateException("EntityAffiliation's type must be populated before creation");
            }
            if (StringUtils.isEmpty(affiliation.getId())
                  ||  getEntityAffiliationBo(affiliation.getId()) == null) {
                throw new RiceIllegalStateException("the EntityAffiliation to update already exists: " + affiliation);
            }
        }
        EntityAffiliationBo bo = EntityAffiliationBo.from(affiliation);
        return EntityAffiliationBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityAffiliation inactivateAffiliation(String id) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(id, "id");

        EntityAffiliationBo bo = getEntityAffiliationBo(id);
        if (bo == null) {
            throw new RiceIllegalStateException("EntityAffiliation with id: " + id + " does not exist");
        }
        bo.setActive(false);
        return EntityAffiliationBo.to(businessObjectService.save(bo));
    }

    @Override
	public EntityQueryResults findEntities(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
		incomingParamCheck(queryByCriteria, "queryByCriteria");

        GenericQueryResults<EntityBo> results = criteriaLookupService.lookup(EntityBo.class, queryByCriteria);

        EntityQueryResults.Builder builder = EntityQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<Entity.Builder> ims = new ArrayList<Entity.Builder>();
        for (EntityBo bo : results.getResults()) {
            ims.add(Entity.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
	}

    @Override
	public EntityDefaultQueryResults findEntityDefaults(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
		incomingParamCheck(queryByCriteria, "queryByCriteria");

        GenericQueryResults<EntityBo> results = criteriaLookupService.lookup(EntityBo.class, queryByCriteria);

        EntityDefaultQueryResults.Builder builder = EntityDefaultQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<EntityDefault.Builder> ims = new ArrayList<EntityDefault.Builder>();
        for (EntityBo bo : results.getResults()) {
            ims.add(EntityDefault.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
	}

	protected EntityNameQueryResults findNames(QueryByCriteria queryByCriteria) {
		incomingParamCheck(queryByCriteria, "queryByCriteria");

        GenericQueryResults<EntityNameBo> results = criteriaLookupService.lookup(EntityNameBo.class, queryByCriteria);

        EntityNameQueryResults.Builder builder = EntityNameQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<EntityName.Builder> ims = new ArrayList<EntityName.Builder>();
        for (EntityNameBo bo : results.getResults()) {
            ims.add(EntityName.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
	}

    @Override
	public EntityPrivacyPreferences getEntityPrivacyPreferences(String entityId) throws RiceIllegalArgumentException {
        incomingParamCheck(entityId, "entityId");
		Map<String,String> criteria = new HashMap<String,String>(1);
        criteria.put(KIMPropertyConstants.Entity.ENTITY_ID, entityId);
		return EntityPrivacyPreferencesBo.to(businessObjectService.findByPrimaryKey(EntityPrivacyPreferencesBo.class, criteria));
	}

    @Override
	public Principal getPrincipal(String principalId) throws RiceIllegalArgumentException {
		incomingParamCheck(principalId, "principalId");

        PrincipalBo principal = getPrincipalBo(principalId);
		if ( principal == null ) {
			return null;
		}
        if (StringUtils.isBlank(principal.getPrincipalName())) {
            principal.setPrincipalName(UNAVAILABLE);
        }
		return PrincipalBo.to(principal);
	}

    @Override
    public List<Principal> getPrincipals (List<String> principalIds) {
        List<Principal>  ret = new ArrayList<Principal>();
        for(String p: principalIds) {
            Principal principalInfo = getPrincipal(p);

            if (principalInfo != null) {
                ret.add(principalInfo) ;
            }
        }
        return ret;
    }
	
	private PrincipalBo getPrincipalBo(String principalId) {
		Map<String,String> criteria = new HashMap<String,String>(1);
        criteria.put(KIMPropertyConstants.Principal.PRINCIPAL_ID, principalId);
		return businessObjectService.findByPrimaryKey(PrincipalBo.class, criteria);
	}

	private EntityBo getEntityBo(String entityId) {
		return businessObjectService.findByPrimaryKey(EntityBo.class, Collections.singletonMap("id", entityId));
	}

	@Override
	public Principal getPrincipalByPrincipalName(String principalName) throws RiceIllegalArgumentException {
		incomingParamCheck(principalName, "principalName");

		return PrincipalBo.to(getPrincipalBoByPrincipalName(principalName));
    }

    private PrincipalBo getPrincipalBoByPrincipalName(String principalName) throws RiceIllegalArgumentException {

        Map<String,Object> criteria = new HashMap<String,Object>(1);
        criteria.put(KIMPropertyConstants.Principal.PRINCIPAL_NAME, principalName.toLowerCase());
        Collection<PrincipalBo> principals = businessObjectService.findMatching(PrincipalBo.class, criteria);
        if (!principals.isEmpty() && principals.size() == 1) {
            return principals.iterator().next();
        }
        return null;
    }

    @Override
    public List<Principal> getPrincipalsByEntityId(String entityId) throws RiceIllegalArgumentException {
        incomingParamCheck(entityId, "entityId");

        List<Principal>  principals = new ArrayList<Principal>();
        Map<String,Object> criteria = new HashMap<String,Object>(2);
        criteria.put(KIMPropertyConstants.Person.ENTITY_ID, entityId);
        Collection<PrincipalBo> principalBos = businessObjectService.findMatching(PrincipalBo.class, criteria);

        if (principalBos != null && !principalBos.isEmpty()) {

            for(PrincipalBo principalBo: principalBos) {
                Principal principal = PrincipalBo.to(principalBo);
                principals.add(principal);
            }
            return principals;
        }
        return null;
    }

    @Override
    public List<Principal> getPrincipalsByEmployeeId(String employeeId) throws RiceIllegalArgumentException {
        incomingParamCheck(employeeId, "employeeId");

        List<Principal>  principals = new ArrayList<Principal>();
        Map<String,Object> criteria = new HashMap<String,Object>(2);
        criteria.put(KIMPropertyConstants.Person.EMPLOYEE_ID, employeeId);
        Collection<EntityEmploymentBo> entityEmploymentBos = businessObjectService.findMatching(EntityEmploymentBo.class, criteria);

        if (entityEmploymentBos != null && !entityEmploymentBos.isEmpty()) {
            List<String>  entityIds = new ArrayList<String>();
            for(EntityEmploymentBo entityEmploymentBo: entityEmploymentBos) {
                String entityId =  entityEmploymentBo.getEntityId();
                if (StringUtils.isNotBlank(entityId) && !entityIds.contains(entityId)) {
                    entityIds.add(entityId);
                }
            }

            for(String entityId: entityIds) {
                List<Principal> principalsForEntity = getPrincipalsByEntityId(entityId);
                if (principalsForEntity != null && !principalsForEntity.isEmpty()) {
                    principals.addAll(principalsForEntity);
                }
            }
            if (!principals.isEmpty()) {
                return principals;
            }
        }
        return null;
    }

	/**
	 * @see org.kuali.rice.kim.api.identity.IdentityService#getEntityByPrincipalName(java.lang.String)
	 */
	protected EntityBo getEntityBoByPrincipalName(String principalName) {
		if ( StringUtils.isBlank( principalName ) ) {
			return null;
		}
        return getEntityByKeyValue("principals." + KIMPropertyConstants.Principal.PRINCIPAL_NAME, principalName.toLowerCase());
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.IdentityService#getEntityByPrincipalId(java.lang.String)
	 */
	protected EntityBo getEntityBoByPrincipalId(String principalId) {
		if ( StringUtils.isBlank( principalId ) ) {
			return null;
		}
        return getEntityByKeyValue("principals." + KIMPropertyConstants.Principal.PRINCIPAL_ID, principalId);
	}

	/**
     * @see org.kuali.rice.kim.api.identity.IdentityService#getEntityByEmployeeId(java.lang.String)
     */
    protected EntityBo getEntityBoByEmployeeId(String employeeId) {
        if ( StringUtils.isBlank( employeeId ) ) {
            return null;
        }
        return getEntityByKeyValue("employmentInformation." + KIMPropertyConstants.Person.EMPLOYEE_ID, employeeId);
    }
    
	/**
	 * Generic helper method for performing a lookup through the business object service.
	 */
	protected EntityBo getEntityByKeyValue(String key, String value) {
		Map<String,String> criteria = new HashMap<String,String>(1);
        criteria.put(key, value);
        Collection<EntityBo> entities = businessObjectService.findMatching(EntityBo.class, criteria);
        if (entities != null && entities.size() >= 1) {
        	return entities.iterator().next();
        }
		return null;
	}

    @Override
	public CodedAttribute getAddressType( String code ) throws RiceIllegalArgumentException {
        incomingParamCheck(code, "code");
		EntityAddressTypeBo impl = businessObjectService.findBySinglePrimaryKey(EntityAddressTypeBo.class, code);
		if ( impl == null ) {
			return null;
		}
		return EntityAddressTypeBo.to(impl);
	}

    @Override
    public List<CodedAttribute> findAllAddressTypes() {
        List<EntityAddressTypeBo> bos = (List<EntityAddressTypeBo>)businessObjectService
                .findMatching(EntityAddressTypeBo.class, Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE));

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityAddressTypeBo bo : bos) {
            codedAttributes.add(EntityAddressTypeBo.to(bo));
        }
        return Collections.unmodifiableList(codedAttributes);
    }

    @Override
    public EntityAffiliationType getAffiliationType( String code ) throws RiceIllegalArgumentException {
        incomingParamCheck(code, "code");

        EntityAffiliationTypeBo impl = businessObjectService.findBySinglePrimaryKey(EntityAffiliationTypeBo.class, code);
		if ( impl == null ) {
			return null;
		}
		return EntityAffiliationTypeBo.to(impl);
	}

    @Override
    public List<EntityAffiliationType> findAllAffiliationTypes() {
        List<EntityAffiliationTypeBo> bos = (List<EntityAffiliationTypeBo>)businessObjectService
                .findMatching(EntityAffiliationTypeBo.class, Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE));

        List<EntityAffiliationType> codedAttributes = new ArrayList<EntityAffiliationType>();
        for (EntityAffiliationTypeBo bo : bos) {
            codedAttributes.add(EntityAffiliationTypeBo.to(bo));
        }
        return Collections.unmodifiableList(codedAttributes);
    }

    @Override
    public CodedAttribute getCitizenshipStatus( String code ) throws RiceIllegalArgumentException {
		incomingParamCheck(code, "code");
        EntityCitizenshipStatusBo impl = businessObjectService.findBySinglePrimaryKey(EntityCitizenshipStatusBo.class, code);
		if ( impl == null ) {
			return null;
		}
		return EntityCitizenshipStatusBo.to(impl);
	}

    @Override
    public List<CodedAttribute> findAllCitizenshipStatuses() {
        List<EntityCitizenshipStatusBo> bos = (List<EntityCitizenshipStatusBo>)businessObjectService
                .findMatching(EntityCitizenshipStatusBo.class, Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE));

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityCitizenshipStatusBo bo : bos) {
            codedAttributes.add(EntityCitizenshipStatusBo.to(bo));
        }
        return Collections.unmodifiableList(codedAttributes);
    }

    @Override
    public CodedAttribute getEmailType( String code ) throws RiceIllegalArgumentException {
		incomingParamCheck(code, "code");
        EntityEmailTypeBo impl = businessObjectService.findBySinglePrimaryKey(EntityEmailTypeBo.class, code);
		if ( impl == null ) {
			return null;
		}
		return EntityEmailTypeBo.to(impl);
	}

    @Override
    public List<CodedAttribute> findAllEmailTypes() {
        List<EntityEmailTypeBo> bos = (List<EntityEmailTypeBo>)businessObjectService
                .findMatching(EntityEmailTypeBo.class, Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE));

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityEmailTypeBo bo : bos) {
            codedAttributes.add(EntityEmailTypeBo.to(bo));
        }
        return Collections.unmodifiableList(codedAttributes);
    }

    @Override
    public PrincipalQueryResults findPrincipals(
            @WebParam(name = "query") QueryByCriteria query) throws RiceIllegalArgumentException {
        incomingParamCheck(query, "query");

        GenericQueryResults<PrincipalBo> results = criteriaLookupService.lookup(PrincipalBo.class, query);

        PrincipalQueryResults.Builder builder = PrincipalQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<Principal.Builder> ims = new ArrayList<Principal.Builder>();
        for (PrincipalBo bo : results.getResults()) {
            ims.add(Principal.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
    }

    @Override
    public CodedAttribute getEmploymentStatus( String code ) throws RiceIllegalArgumentException {
		incomingParamCheck(code, "code");
        EntityEmploymentStatusBo impl = businessObjectService.findBySinglePrimaryKey(EntityEmploymentStatusBo.class, code);
		if ( impl == null ) {
			return null;
		}
		return EntityEmploymentStatusBo.to(impl);
	}

    @Override
    public List<CodedAttribute> findAllEmploymentStatuses() {
        List<EntityEmploymentStatusBo> bos = (List<EntityEmploymentStatusBo>)businessObjectService
                .findMatching(EntityEmploymentStatusBo.class, Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE));

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityEmploymentStatusBo bo : bos) {
            codedAttributes.add(EntityEmploymentStatusBo.to(bo));
        }
        return Collections.unmodifiableList(codedAttributes);
    }

    @Override
    public CodedAttribute getEmploymentType( String code ) throws RiceIllegalArgumentException {
		incomingParamCheck(code, "code");
        EntityEmploymentTypeBo impl = businessObjectService.findBySinglePrimaryKey(EntityEmploymentTypeBo.class, code);
		if ( impl == null ) {
			return null;
		}
		return EntityEmploymentTypeBo.to(impl);
	}

    @Override
    public List<CodedAttribute> findAllEmploymentTypes() {
        List<EntityEmploymentTypeBo> bos = (List<EntityEmploymentTypeBo>)businessObjectService
                .findMatching(EntityEmploymentTypeBo.class, Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE));

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityEmploymentTypeBo bo : bos) {
            codedAttributes.add(EntityEmploymentTypeBo.to(bo));
        }
        return Collections.unmodifiableList(codedAttributes);
    }

    @Override
    public CodedAttribute getNameType(String code) throws RiceIllegalArgumentException {
		incomingParamCheck(code, "code");
        EntityNameTypeBo impl = businessObjectService.findBySinglePrimaryKey(EntityNameTypeBo.class, code);
		if ( impl == null ) {
			return null;
		}
		return EntityNameTypeBo.to(impl);
	}

    @Override
    public List<CodedAttribute> findAllNameTypes() {
        List<EntityNameTypeBo> bos = (List<EntityNameTypeBo>)businessObjectService
                .findMatching(EntityNameTypeBo.class, Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE));

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityNameTypeBo bo : bos) {
            codedAttributes.add(EntityNameTypeBo.to(bo));
        }
        return Collections.unmodifiableList(codedAttributes);
    }

    @Override
    public CodedAttribute getEntityType( String code ) throws RiceIllegalArgumentException {
		incomingParamCheck(code, "code");
        EntityTypeBo impl = businessObjectService.findBySinglePrimaryKey(EntityTypeBo.class, code);
		if ( impl == null ) {
			return null;
		}
		return EntityTypeBo.to(impl);
	}

    @Override
    public List<CodedAttribute> findAllEntityTypes() {
        List<EntityTypeBo> bos = (List<EntityTypeBo>)businessObjectService
                .findMatching(EntityTypeBo.class, Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE));
        
        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityTypeBo bo : bos) {
            codedAttributes.add(EntityTypeBo.to(bo));    
        }
        return Collections.unmodifiableList(codedAttributes);
    }

    @Override
    public EntityExternalIdentifierType getExternalIdentifierType( String code ) throws RiceIllegalArgumentException {
		incomingParamCheck(code, "code");

        EntityExternalIdentifierTypeBo impl = businessObjectService.findBySinglePrimaryKey(EntityExternalIdentifierTypeBo.class, code);
		if ( impl == null ) {
			return null;
		}
		return EntityExternalIdentifierTypeBo.to(impl);
	}

    @Override
    public List<EntityExternalIdentifierType> findAllExternalIdendtifierTypes() {
        List<EntityExternalIdentifierTypeBo> bos = (List<EntityExternalIdentifierTypeBo>)businessObjectService
                .findMatching(EntityExternalIdentifierTypeBo.class, Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE));

        List<EntityExternalIdentifierType> codedAttributes = new ArrayList<EntityExternalIdentifierType>();
        for (EntityExternalIdentifierTypeBo bo : bos) {
            codedAttributes.add(EntityExternalIdentifierTypeBo.to(bo));
        }
        return Collections.unmodifiableList(codedAttributes);
    }

    @Override
    public CodedAttribute getPhoneType( String code ) throws RiceIllegalArgumentException {
		incomingParamCheck(code, "code");
        EntityPhoneTypeBo impl = businessObjectService.findBySinglePrimaryKey(EntityPhoneTypeBo.class, code);
		if ( impl == null ) {
			return null;
		}
		return EntityPhoneTypeBo.to(impl);
	}

    @Override
    public List<CodedAttribute> findAllPhoneTypes() {
        List<EntityPhoneTypeBo> bos = (List<EntityPhoneTypeBo>)businessObjectService
                .findMatching(EntityPhoneTypeBo.class, Collections.singletonMap(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE));

        List<CodedAttribute> codedAttributes = new ArrayList<CodedAttribute>();
        for (EntityPhoneTypeBo bo : bos) {
            codedAttributes.add(EntityPhoneTypeBo.to(bo));
        }
        return Collections.unmodifiableList(codedAttributes);
    }

    @Override
    public Entity createEntity(Entity entity) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(entity, "entity");

        if (StringUtils.isNotBlank(entity.getId()) && getEntity(entity.getId()) != null) {
            throw new RiceIllegalStateException("the Entity to create already exists: " + entity);
        }

        EntityBo bo = EntityBo.from(entity);
        return EntityBo.to(businessObjectService.save(bo));
    }

    @Override
    public Entity updateEntity(Entity entity) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(entity, "entity");
        EntityBo oldBo = null;
        Map<String, String> passwdMap = new HashMap<String, String>();
        if (StringUtils.isBlank(entity.getId())) {
            throw new RiceIllegalStateException("the Entity does not exist: " + entity);
        } else {
            oldBo = getEntityBo(entity.getId());
            if (oldBo == null) {
                throw new RiceIllegalStateException("the Entity does not exist: " + entity);    
            }
        }

        for (PrincipalBo principalBo : oldBo.getPrincipals()) {
            passwdMap.put(principalBo.getPrincipalId(), principalBo.getPassword());
        }
        EntityBo bo = EntityBo.from(entity);
        for (PrincipalBo principal : bo.getPrincipals()) {
            principal.setPassword(passwdMap.get(principal.getPrincipalId()));
        }
        return EntityBo.to(businessObjectService.save(bo));
    }



    @Override
    public Entity inactivateEntity(String entityId) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(entityId, "entityId");

        EntityBo entity = getEntityBo(entityId);
        if (entity == null) {
            throw new RiceIllegalStateException("an Entity does not exist for entityId: " + entityId);
        }

        entity.setActive(false);
        return EntityBo.to(businessObjectService.save(entity));
    }

    @Override
    public EntityPrivacyPreferences addPrivacyPreferencesToEntity(EntityPrivacyPreferences privacyPreferences) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(privacyPreferences, "privacyPreferences");

        if (StringUtils.isEmpty(privacyPreferences.getEntityId()) || StringUtils.isBlank(privacyPreferences.getEntityId())) {
            throw new RiceIllegalStateException("PrivacyPreferences' entityId must be populated before creation");
        }  else {
            if (getEntityPrivacyPreferences(privacyPreferences.getEntityId()) != null) {
                throw new RiceIllegalStateException("the PrivacyPreferences to create already exists: " + privacyPreferences);
            }
        }
        EntityPrivacyPreferencesBo bo = EntityPrivacyPreferencesBo.from(privacyPreferences);
        return EntityPrivacyPreferencesBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityPrivacyPreferences updatePrivacyPreferences(EntityPrivacyPreferences privacyPreferences) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(privacyPreferences, "privacyPreferences");

        if (StringUtils.isEmpty(privacyPreferences.getEntityId()) || StringUtils.isBlank(privacyPreferences.getEntityId())) {
            throw new RiceIllegalStateException("PrivacyPreferences' entityId must be populated before update");
        }  else {
            if (getEntityPrivacyPreferences(privacyPreferences.getEntityId()) == null) {
                throw new RiceIllegalStateException("the PrivacyPreferences to update does not exist: " + privacyPreferences);
            }
        }
        EntityPrivacyPreferencesBo bo = EntityPrivacyPreferencesBo.from(privacyPreferences);
        return EntityPrivacyPreferencesBo.to(businessObjectService.save(bo));
    }

    private EntityCitizenshipBo getEntityCitizenshipBo(String entityId, String citizenshipStatusCode) {
        if (StringUtils.isEmpty(entityId) || StringUtils.isEmpty(citizenshipStatusCode)) {
            return null;
        }
        Map<String,Object> criteria = new HashMap<String,Object>(4);
        criteria.put(KIMPropertyConstants.Entity.ENTITY_ID, entityId);
        criteria.put("statusCode", citizenshipStatusCode);
        criteria.put(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        return businessObjectService.findByPrimaryKey(EntityCitizenshipBo.class, criteria);
    }

    private EntityCitizenshipBo getEntityCitizenshipBo(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Map<String,Object> criteria = new HashMap<String,Object>();
        criteria.put(KIMPropertyConstants.Entity.ID, id);
        criteria.put(KIMPropertyConstants.Entity.ACTIVE, Boolean.TRUE);
        return businessObjectService.findByPrimaryKey(EntityCitizenshipBo.class, criteria);
    }

    @Override
    public EntityCitizenship addCitizenshipToEntity(EntityCitizenship citizenship) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(citizenship, "citizenship");

        if (StringUtils.isEmpty(citizenship.getEntityId()) || StringUtils.isBlank(citizenship.getEntityId())) {
            throw new RiceIllegalStateException("Citizenship's entityId must be populated before creation");
        }  else {
            if (citizenship.getStatus() == null) {
                throw new RiceIllegalStateException("Citizenship's status must be populated before creation");
            }
            if (getEntityCitizenshipBo(citizenship.getEntityId(), citizenship.getStatus().getCode()) != null) {
                throw new RiceIllegalStateException("the EntityCitizenship to create already exists: " + citizenship);
            }
        }
        EntityCitizenshipBo bo = EntityCitizenshipBo.from(citizenship);
        return EntityCitizenshipBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityCitizenship updateCitizenship(EntityCitizenship citizenship) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(citizenship, "citizenship");

        if (StringUtils.isEmpty(citizenship.getEntityId()) || StringUtils.isBlank(citizenship.getEntityId())) {
            throw new RiceIllegalStateException("Email's entityId must be populated before creation");
        }  else {
            if (citizenship.getStatus() == null) {
                throw new RiceIllegalStateException("Citizenship's status must be populated before creation");
            }
            if (getEntityCitizenshipBo(citizenship.getEntityId(), citizenship.getStatus().getCode()) == null) {
                throw new RiceIllegalStateException("the EntityCitizenship to update does not exist: " + citizenship);
            }
        }
        EntityCitizenshipBo bo = EntityCitizenshipBo.from(citizenship);
        return EntityCitizenshipBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityCitizenship inactivateCitizenship(String id) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(id, "id");

        EntityCitizenshipBo bo = getEntityCitizenshipBo(id);
        if (bo == null) {
            throw new RiceIllegalStateException("the EntityCitizenship with id: " + id + " does not exist");
        }
        bo.setActive(false);
        return EntityCitizenshipBo.to(businessObjectService.save(bo));
    }

    private EntityEthnicityBo getEntityEthnicityBo(String ethnicityId) {
        if (StringUtils.isEmpty(ethnicityId)) {
            return null;
        }
        Map<String,Object> criteria = new HashMap<String,Object>();
        criteria.put(KIMPropertyConstants.Entity.ID, ethnicityId);
        return businessObjectService.findByPrimaryKey(EntityEthnicityBo.class, criteria);
    }
    @Override
    public EntityEthnicity addEthnicityToEntity(EntityEthnicity ethnicity) throws RiceIllegalArgumentException {
        incomingParamCheck(ethnicity, "ethnicity");

        if (StringUtils.isEmpty(ethnicity.getEntityId()) || StringUtils.isBlank(ethnicity.getEntityId())) {
            throw new RiceIllegalStateException("Ethnicity's entityId must be populated before creation");
        }  else {
            if (StringUtils.isNotEmpty(ethnicity.getId()) && getEntityEthnicityBo(ethnicity.getId()) != null) {
                throw new RiceIllegalStateException("the EntityEthnicity to create already exists: " + ethnicity);
            }
        }
        EntityEthnicityBo bo = EntityEthnicityBo.from(ethnicity);
        return EntityEthnicityBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityEthnicity updateEthnicity(EntityEthnicity ethnicity) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(ethnicity, "ethnicity");

        if (StringUtils.isEmpty(ethnicity.getEntityId()) || StringUtils.isBlank(ethnicity.getEntityId())) {
            throw new RiceIllegalStateException("Ethnicity's entityId must be populated before creation");
        }  else {
            if (StringUtils.isEmpty(ethnicity.getId()) || getEntityEthnicityBo(ethnicity.getId()) == null) {
                throw new RiceIllegalStateException("the EntityEthnicity to update does not exist: " + ethnicity);
            }
        }
        EntityEthnicityBo bo = EntityEthnicityBo.from(ethnicity);
        return EntityEthnicityBo.to(businessObjectService.save(bo));
    }

    private EntityResidencyBo getEntityResidencyBo(String residencyId) {
        if (StringUtils.isEmpty(residencyId)) {
            return null;
        }
        Map<String,Object> criteria = new HashMap<String,Object>();
        criteria.put(KIMPropertyConstants.Entity.ID, residencyId);
        return businessObjectService.findByPrimaryKey(EntityResidencyBo.class, criteria);
    }

    @Override
    public EntityResidency addResidencyToEntity(EntityResidency residency) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(residency, "residency");

        if (StringUtils.isEmpty(residency.getEntityId()) || StringUtils.isBlank(residency.getEntityId())) {
            throw new RiceIllegalStateException("Residency's entityId must be populated before creation");
        }  else {
            if (StringUtils.isNotEmpty(residency.getId()) && getEntityResidencyBo(residency.getId()) != null) {
                throw new RiceIllegalStateException("the EntityResidency to create already exists: " + residency);
            }
        }
        EntityResidencyBo bo = EntityResidencyBo.from(residency);
        return EntityResidencyBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityResidency updateResidency(EntityResidency residency) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(residency, "residency");

        if (StringUtils.isEmpty(residency.getEntityId()) || StringUtils.isBlank(residency.getEntityId())) {
            throw new RiceIllegalStateException("Residency's entityId must be populated before creation");
        }  else {
            if (StringUtils.isEmpty(residency.getId()) || getEntityResidencyBo(residency.getId()) == null) {
                throw new RiceIllegalStateException("the EntityResidency to update does not exist: " + residency);
            }
        }
        EntityResidencyBo bo = EntityResidencyBo.from(residency);
        return EntityResidencyBo.to(businessObjectService.save(bo));
    }

    private EntityVisaBo getEntityVisaBo(String visaId) {
        if (StringUtils.isEmpty(visaId)) {
            return null;
        }
        Map<String,Object> criteria = new HashMap<String,Object>();
        criteria.put(KIMPropertyConstants.Entity.ID, visaId);
        return businessObjectService.findByPrimaryKey(EntityVisaBo.class, criteria);
    }

    @Override
    public EntityVisa addVisaToEntity(EntityVisa visa) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(visa, "visa");

        if (StringUtils.isEmpty(visa.getEntityId()) || StringUtils.isBlank(visa.getEntityId())) {
            throw new RiceIllegalStateException("Visa's entityId must be populated before creation");
        }  else {
            if (StringUtils.isNotEmpty(visa.getId()) && getEntityVisaBo(visa.getId()) != null) {
                throw new RiceIllegalStateException("the EntityVisa to create already exists: " + visa);
            }
        }
        EntityVisaBo bo = EntityVisaBo.from(visa);
        return EntityVisaBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityVisa updateVisa(EntityVisa visa) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(visa, "visa");

        if (StringUtils.isEmpty(visa.getEntityId()) || StringUtils.isBlank(visa.getEntityId())) {
            throw new RiceIllegalStateException("Visa's entityId must be populated before creation");
        }  else {
            if (StringUtils.isEmpty(visa.getId()) || getEntityVisaBo(visa.getId()) == null) {
                throw new RiceIllegalStateException("the EntityVisa to update does not exist: " + visa);
            }
        }
        EntityVisaBo bo = EntityVisaBo.from(visa);
        return EntityVisaBo.to(businessObjectService.save(bo));
    }

    private EntityNameBo getEntityNameBo(String entityId, String nameTypeCode) {
        if (StringUtils.isEmpty(entityId) || StringUtils.isEmpty(nameTypeCode)) {
            return null;
        }
        Map<String,Object> criteria = new HashMap<String,Object>();
        criteria.put(KIMPropertyConstants.Entity.ENTITY_ID, entityId);
        criteria.put("nameCode", nameTypeCode);
        criteria.put(KIMPropertyConstants.Entity.ACTIVE, "Y");
        return businessObjectService.findByPrimaryKey(EntityNameBo.class, criteria);
    }

    private EntityNameBo getEntityNameBo(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Map<String,Object> criteria = new HashMap<String,Object>();
        criteria.put(KIMPropertyConstants.Entity.ID, id);
        criteria.put(KIMPropertyConstants.Entity.ACTIVE, "Y");
        return businessObjectService.findByPrimaryKey(EntityNameBo.class, criteria);
    }
    
    @Override
    public EntityNamePrincipalName getDefaultNamesForPrincipalId(String principalId) {
    	EntityNamePrincipalName.Builder nameBuilder = EntityNamePrincipalName.Builder.create();
    	Map<String,String> criteria = new HashMap<String,String>();
    	criteria.put(KIMPropertyConstants.Principal.PRINCIPAL_ID, principalId);
    	PrincipalBo principal = (PrincipalBo) businessObjectService.findByPrimaryKey(PrincipalBo.class, criteria);

    	if (null != principal) {
    		nameBuilder.setPrincipalName(principal.getPrincipalName());

    		criteria.clear();
    		criteria.put(KIMPropertyConstants.Entity.ENTITY_ID, principal.getEntityId());
    		criteria.put("DFLT_IND", "Y");
    		criteria.put("ACTV_IND", "Y");
    		EntityNameBo name = (EntityNameBo) businessObjectService.findByPrimaryKey(EntityNameBo.class, criteria);

    		if (name == null) {
    			// to make this simple for now, assume if there is no default name that this is a system entity we are dealing with here
    			EntityName.Builder defaultNameBuilder = EntityName.Builder.create();
    			defaultNameBuilder.setLastName(principal.getPrincipalName().toUpperCase());
    			nameBuilder.setDefaultName(defaultNameBuilder);
    		} else {
    			nameBuilder.setDefaultName( EntityName.Builder.create(name) );
    		}
    		EntityNamePrincipalName entityNamePrincipalName = nameBuilder.build(); 
    		return entityNamePrincipalName;
    	}
    	return null;
    }

    @Override
    public EntityName addNameToEntity(EntityName name) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(name, "name");

        if (StringUtils.isEmpty(name.getEntityId()) || StringUtils.isBlank(name.getEntityId())) {
            throw new RiceIllegalStateException("Name's entityId must be populated before creation");
        }  else {
            if (name.getNameType() == null) {
                throw new RiceIllegalStateException("EntityName's type must be populated before creation");
            }
            if (getEntityNameBo(name.getEntityId(), name.getNameType().getCode()) != null) {
                throw new RiceIllegalStateException("the EntityName to create already exists: " + name);
            }
        }
        EntityNameBo bo = EntityNameBo.from(name);
        return EntityNameBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityName updateName(EntityName name) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(name, "name");

        if (StringUtils.isEmpty(name.getEntityId()) || StringUtils.isBlank(name.getEntityId())) {
            throw new RiceIllegalStateException("Name's entityId must be populated before update");
        }  else {
            if (name.getNameType() == null) {
                throw new RiceIllegalStateException("EntityName's type must be populated before update");
            }
            if (StringUtils.isEmpty(name.getId()) || getEntityNameBo(name.getId()) == null) {
                throw new RiceIllegalStateException("the EntityName to update does not exist: " + name);
            }
        }
        EntityNameBo bo = EntityNameBo.from(name);
        return EntityNameBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityName inactivateName(String id) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(id, "id");

        EntityNameBo bo = getEntityNameBo(id);
        if (bo == null) {
            throw new RiceIllegalStateException("the EntityName to inactivate does not exist");
        }

        bo.setActive(false);
        return EntityNameBo.to(businessObjectService.save(bo));
    }

    private EntityEmploymentBo getEntityEmploymentBo(String entityId, String employmentTypeCode,
                        String employmentStatusCode, String employmentAffiliationId) {
        if (StringUtils.isEmpty(entityId) || StringUtils.isEmpty(employmentTypeCode)
                || StringUtils.isEmpty(employmentStatusCode) || StringUtils.isEmpty(employmentAffiliationId)) {
            return null;
        }
        Map<String,Object> criteria = new HashMap<String,Object>();
        criteria.put(KIMPropertyConstants.Entity.ENTITY_ID, entityId);
        criteria.put("employeeTypeCode", employmentTypeCode);
        criteria.put("employeeStatusCode", employmentStatusCode);
        criteria.put("entityAffiliationId", employmentAffiliationId);
        criteria.put(KIMPropertyConstants.Entity.ACTIVE, "Y");
        return businessObjectService.findByPrimaryKey(EntityEmploymentBo.class, criteria);
    }

    private EntityEmploymentBo getEntityEmploymentBo(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Map<String,Object> criteria = new HashMap<String,Object>();
        criteria.put(KIMPropertyConstants.Entity.ID, id);
        criteria.put(KIMPropertyConstants.Entity.ACTIVE, "Y");
        return businessObjectService.findByPrimaryKey(EntityEmploymentBo.class, criteria);
    }
    @Override
    public EntityEmployment addEmploymentToEntity(EntityEmployment employment) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(employment, "employment");

        if (StringUtils.isEmpty(employment.getEntityId()) || StringUtils.isBlank(employment.getEntityId())) {
            throw new RiceIllegalStateException("EntityEmployment's entityId must be populated before creation");
        }  else {
            if (employment.getEmployeeType() == null
                    || employment.getEmployeeStatus() == null
                    || employment.getEntityAffiliation() == null) {
                throw new RiceIllegalStateException("EntityEmployment's status, type, and entity affiliation must be populated before creation");
            }
            if (getEntityEmploymentBo(employment.getEntityId(), employment.getEmployeeType().getCode(), employment.getEmployeeStatus().getCode(), employment.getEntityAffiliation().getId()) != null) {
                throw new RiceIllegalStateException("the EntityEmployment to create already exists: " + employment);
            }
        }
        EntityEmploymentBo bo = EntityEmploymentBo.from(employment);
        return EntityEmploymentBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityEmployment updateEmployment(EntityEmployment employment) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(employment, "employment");

        if (StringUtils.isEmpty(employment.getEntityId()) || StringUtils.isBlank(employment.getEntityId())) {
            throw new RiceIllegalStateException("EntityEmployment's entityId must be populated before update");
        }  else {
            if (employment.getEmployeeType() == null
                    || employment.getEmployeeStatus() == null
                    || employment.getEntityAffiliation() == null) {
                throw new RiceIllegalStateException("EntityEmployment's status, type, and entity affiliation must be populated before update");
            }
            if (getEntityEmploymentBo(employment.getEntityId(), employment.getEmployeeType().getCode(), employment.getEmployeeStatus().getCode(), employment.getEntityAffiliation().getId()) == null) {
                throw new RiceIllegalStateException("the EntityEmployment to udpate does not exist: " + employment);
            }
        }
        EntityEmploymentBo bo = EntityEmploymentBo.from(employment);
        return EntityEmploymentBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityEmployment inactivateEmployment(String id) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(id, "id");

        EntityEmploymentBo bo = getEntityEmploymentBo(id);
        if (bo == null) {
            throw new RiceIllegalStateException("the EntityEmployment to inactivate does not exist");
        }
        bo.setActive(false);
        return EntityEmploymentBo.to(businessObjectService.save(bo));
    }

	private EntityBioDemographicsBo getEntityBioDemographicsBo(String entityId) {
        if (StringUtils.isEmpty(entityId)) {
            return null;
        }
		Map<String,String> criteria = new HashMap<String,String>(1);
        criteria.put(KIMPropertyConstants.Entity.ENTITY_ID, entityId);
		return businessObjectService.findByPrimaryKey(EntityBioDemographicsBo.class, criteria);
	}

    @Override
    public EntityBioDemographics addBioDemographicsToEntity(EntityBioDemographics bioDemographics) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(bioDemographics, "bioDemographics");

        if (StringUtils.isEmpty(bioDemographics.getEntityId()) || StringUtils.isBlank(bioDemographics.getEntityId())) {
            throw new RiceIllegalStateException("BioDemographics' entityId must be populated before creation");
        }  else {
            if (getEntityBioDemographicsBo(bioDemographics.getEntityId()) != null) {
                throw new RiceIllegalStateException("the EntityBioDemographics to create already exists: " + bioDemographics);
            }
        }
        EntityBioDemographicsBo bo = EntityBioDemographicsBo.from(bioDemographics);
        return EntityBioDemographicsBo.to(businessObjectService.save(bo));
    }

    @Override
    public EntityBioDemographics updateBioDemographics(EntityBioDemographics bioDemographics) throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(bioDemographics, "bioDemographics");

        if (getEntityBioDemographicsBo(bioDemographics.getEntityId()) == null) {
            throw new RiceIllegalStateException("the EntityBioDemographics to update does not exist: " + bioDemographics);
        }
        EntityBioDemographicsBo bo = EntityBioDemographicsBo.from(bioDemographics);
        return EntityBioDemographicsBo.to(businessObjectService.save(bo));
    }


    public void setCriteriaLookupService(final CriteriaLookupService criteriaLookupService) {
        this.criteriaLookupService = criteriaLookupService;
    }

    public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    private void incomingParamCheck(Object object, String name) {
        if (object == null) {
            throw new RiceIllegalArgumentException(name + " was null");
        } else if (object instanceof String
                && StringUtils.isBlank((String) object)) {
            throw new RiceIllegalArgumentException(name + " was blank");
        }
    }
}
