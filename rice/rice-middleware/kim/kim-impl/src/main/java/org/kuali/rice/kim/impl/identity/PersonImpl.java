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
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationContract;
import org.kuali.rice.kim.api.identity.email.EntityEmailContract;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.phone.EntityPhoneContract;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoDefault;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentStatusBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentTypeBo;
import org.kuali.rice.krad.bo.TransientBusinessObjectBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonImpl extends TransientBusinessObjectBase implements Person {

	private static final long serialVersionUID = 1L;
	
	protected static PersonService personService;
	protected static IdentityService identityService;

	private String lookupRoleNamespaceCode;
	private String lookupRoleName;
	
	// principal data
	protected String principalId;
	protected String principalName;
	protected String entityId;
	protected String entityTypeCode;
	// name data
	protected String firstName = "";
	protected String middleName = "";
	protected String lastName = "";

	protected String name = "";
	// address data
    protected EntityAddress address;
	/*protected String addressLine1 = "";
	protected String addressLine2 = "";
	protected String addressLine3 = "";
	protected String addressCity = "";
	protected String addressStateProvinceCode = "";
	protected String addressPostalCode = "";
	protected String addressCountryCode = "";*/
	// email data
	protected String emailAddress = "";
	// phone data
	protected String phoneNumber = "";
	// privacy preferences data
	protected boolean suppressName = false;
	protected boolean suppressAddress = false;
	protected boolean suppressPhone = false;
	protected boolean suppressPersonal = false;
	protected boolean suppressEmail = false;
	// affiliation data
	protected List<? extends EntityAffiliationContract> affiliations;
	
	protected String campusCode = "";
	//protected Campus campus;
	// external identifier data
	protected Map<String,String> externalIdentifiers = null;
	// employment data
	protected String employeeStatusCode = "";
	protected EntityEmploymentStatusBo employeeStatus;
	protected String employeeTypeCode = "";
	protected EntityEmploymentTypeBo employeeType;
	protected String primaryDepartmentCode = "";
	protected String employeeId = "";
	
	protected KualiDecimal baseSalaryAmount = KualiDecimal.ZERO;
	protected boolean active = true;
	
	public PersonImpl() {}
	
	public PersonImpl( Principal principal, String personEntityTypeCode ) {
		this( principal, null, personEntityTypeCode );
	}

	public PersonImpl( Principal principal, EntityDefault entity, String personEntityTypeCode ) {
		setPrincipal( principal, entity, personEntityTypeCode );
	}
	
	public PersonImpl( String principalId, String personEntityTypeCode ) {
		this( getIdentityService().getPrincipal(principalId), personEntityTypeCode );
	}
	
	public PersonImpl( EntityDefaultInfoCacheBo p ) {
		entityId = p.getEntityId();
		principalId = p.getPrincipalId();
		principalName = p.getPrincipalName();
		entityTypeCode = p.getEntityTypeCode();
		firstName = p.getFirstName();
		middleName = p.getMiddleName();
		lastName = p.getLastName();
		name = p.getName();
		campusCode = p.getCampusCode();
		primaryDepartmentCode = p.getPrimaryDepartmentCode();
		employeeId = p.getEmployeeId();
		affiliations = new ArrayList<EntityAffiliation>( 0 );
		externalIdentifiers = new HashMap<String,String>( 0 );
	}

	/**
	 * Sets the principal object and populates the person object from that. 
	 */
	public void setPrincipal(Principal principal, EntityDefault entity, String personEntityTypeCode) {
		populatePrincipalInfo( principal );
		if ( entity == null ) {
			entity = getIdentityService().getEntityDefault( principal.getEntityId() );
		}
		populateEntityInfo( entity, principal, personEntityTypeCode );
	}

	
	protected void populatePrincipalInfo( Principal principal ) {
		entityId = principal.getEntityId();
		principalId = principal.getPrincipalId();
		principalName = principal.getPrincipalName();
		active = principal.isActive();
	}
	
	protected void populateEntityInfo( EntityDefault entity, Principal principal, String personEntityTypeCode ) {
		if(entity!=null){
		    populatePrivacyInfo (entity );
			EntityTypeContactInfoDefault entityTypeContactInfoDefault = entity.getEntityType( personEntityTypeCode );
			entityTypeCode = personEntityTypeCode;
			populateNameInfo( personEntityTypeCode, entity, principal );
			populateAddressInfo( entityTypeContactInfoDefault );
			populateEmailInfo( entityTypeContactInfoDefault );
			populatePhoneInfo( entityTypeContactInfoDefault );
			populateAffiliationInfo( entity );
			populateEmploymentInfo( entity );
			populateExternalIdentifiers( entity );
		}
	}
	
	protected void populateNameInfo( String entityTypeCode, EntityDefault entity, Principal principal ) {
		if(entity!=null){
			EntityName entityName = entity.getName();
			if ( entityName != null ) {
				firstName = unNullify( entityName.getFirstName());
				middleName = unNullify( entityName.getMiddleName() );
				lastName = unNullify( entityName.getLastName() );
				if ( entityTypeCode.equals( KimConstants.EntityTypes.SYSTEM ) ) {
					name = principal.getPrincipalName().toUpperCase();
				} else {
					name = unNullify( entityName.getCompositeName() );
					if(name.equals("") || name == null){
						name = lastName + ", " + firstName;					
					}
				}
			} else {
				firstName = "";
				middleName = "";
				if ( entityTypeCode.equals( KimConstants.EntityTypes.SYSTEM ) ) {
					name = principal.getPrincipalName().toUpperCase();
					lastName = principal.getPrincipalName().toUpperCase();
				} else {
					name = "";
					lastName = "";
				}
			}
		}
	}
	
	protected void populatePrivacyInfo (EntityDefault entity) {
	    if(entity!=null) {
    	    if (entity.getPrivacyPreferences() != null) {
        	    suppressName = entity.getPrivacyPreferences().isSuppressName();
        	    suppressAddress = entity.getPrivacyPreferences().isSuppressAddress();
        	    suppressPhone = entity.getPrivacyPreferences().isSuppressPhone();
        	    suppressPersonal = entity.getPrivacyPreferences().isSuppressPersonal();
        	    suppressEmail = entity.getPrivacyPreferences().isSuppressEmail();
    	    }
	    }
	}
	
	protected void populateAddressInfo( EntityTypeContactInfoDefault contactInfoDefault ) {
		if(contactInfoDefault!=null){
			EntityAddress defaultAddress = contactInfoDefault.getDefaultAddress();
			if ( defaultAddress != null ) {
                address = defaultAddress;
			} else {
                EntityAddress.Builder builder = EntityAddress.Builder.create();
                builder.setCity("");
                builder.setCountryCode("");
                builder.setLine1("");
                builder.setLine2("");
                builder.setLine3("");
                builder.setCity("");
                builder.setPostalCode("");
                builder.setStateProvinceCode("");
                builder.setActive(true);
				address = builder.build();
			}
		}
	}
	
	protected void populateEmailInfo( EntityTypeContactInfoDefault contactInfoDefault ) {
		if(contactInfoDefault!=null){
			EntityEmailContract entityEmail = contactInfoDefault.getDefaultEmailAddress();
			if ( entityEmail != null ) {
				emailAddress = unNullify( entityEmail.getEmailAddressUnmasked() );
			} else {
				emailAddress = "";
			}
		}
	}
	
	protected void populatePhoneInfo( EntityTypeContactInfoDefault contactInfoDefault ) {
		if(contactInfoDefault!=null){
			EntityPhoneContract entityPhone = contactInfoDefault.getDefaultPhoneNumber();
			if ( entityPhone != null ) {
				phoneNumber = unNullify( entityPhone.getFormattedPhoneNumberUnmasked() );
			} else {
				phoneNumber = "";
			}
		}
	}
	
	protected void populateAffiliationInfo(EntityDefault entity ) {
		if(entity!=null){
			affiliations = entity.getAffiliations();
			EntityAffiliation defaultAffiliation = entity.getDefaultAffiliation();
			if ( defaultAffiliation != null  ) {
				campusCode = unNullify( defaultAffiliation.getCampusCode() );
			} else {
				campusCode = "";
			}
		}
	}
	
	protected void populateEmploymentInfo( EntityDefault entity ) {
		if(entity!=null){
			EntityEmployment employmentInformation = entity.getEmployment();
			if ( employmentInformation != null ) {
				employeeStatusCode = unNullify( employmentInformation.getEmployeeStatus() != null ? employmentInformation.getEmployeeStatus().getCode() : null);
				employeeTypeCode = unNullify( employmentInformation.getEmployeeType() != null ? employmentInformation.getEmployeeType().getCode() : null);
				primaryDepartmentCode = unNullify( employmentInformation.getPrimaryDepartmentCode() );
				employeeId = unNullify( employmentInformation.getEmployeeId() );
				if ( employmentInformation.getBaseSalaryAmount() != null ) {
					baseSalaryAmount = employmentInformation.getBaseSalaryAmount();
				} else {
					baseSalaryAmount = KualiDecimal.ZERO;
				}
			} else {
				employeeStatusCode = "";
				employeeTypeCode = "";
				primaryDepartmentCode = "";
				employeeId = "";
				baseSalaryAmount = KualiDecimal.ZERO;
			}
		}
	}
	
	protected void populateExternalIdentifiers( EntityDefault entity ) {
		if(entity!=null){
			List<? extends EntityExternalIdentifier> externalIds = entity.getExternalIdentifiers();
			externalIdentifiers = new HashMap<String,String>( externalIds.size() );
			for ( EntityExternalIdentifier eei : externalIds ) {
				externalIdentifiers.put( eei.getExternalIdentifierTypeCode(), eei.getExternalId() );
			}
		}
	}
	
	/** So users of this class don't need to program around nulls. */
	private String unNullify( String str ) {
		if ( str == null ) {
			return "";
		}
		return str;
	}
	
	/**
	 * @see org.kuali.rice.kim.api.identity.Person#getEntityId()
	 */
	public String getEntityId() {
		return entityId;
	}
	
	/**
	 * @see org.kuali.rice.kim.api.identity.Person#getPrincipalId()
	 */
	public String getPrincipalId() {
		return principalId;
	}
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kim.api.identity.Person#getPrincipalName()
	 */
	public String getPrincipalName() {
		return principalName;
	}
	
	/**
	 * @see org.kuali.rice.kim.api.identity.Person#getFirstName()
	 */
	public String getFirstName() {
	    if (KimInternalSuppressUtils.isSuppressName(getEntityId())){
	        return KimConstants.RESTRICTED_DATA_MASK;
	    }
		return firstName;
	}
	
	/**
     * @see org.kuali.rice.kim.api.identity.Person#getFirstNameUnmasked()
     */
    public String getFirstNameUnmasked() {
        return firstName;
    }

	/**
	 * @see org.kuali.rice.kim.api.identity.Person#getMiddleName()
	 */
	public String getMiddleName() {
	    if (KimInternalSuppressUtils.isSuppressName(getEntityId())){
            return KimConstants.RESTRICTED_DATA_MASK;
        }
		return middleName;
	}
	
	/**
     * @see org.kuali.rice.kim.api.identity.Person#getMiddleNameUnmasked()
     */
	public String getMiddleNameUnmasked() {
	    return middleName;
	}
	
	/**
	 * @see org.kuali.rice.kim.api.identity.Person#getLastName()
	 */
	public String getLastName() {
	    if (KimInternalSuppressUtils.isSuppressName(getEntityId())){
            return KimConstants.RESTRICTED_DATA_MASK;
        }
		return lastName;
	}
	
	/**
     * @see org.kuali.rice.kim.api.identity.Person#getLastNameUnmasked()
     */
    public String getLastNameUnmasked() {
        return lastName;
    }
	
	/**
	 * @see org.kuali.rice.kim.api.identity.Person#getName()
	 */
	public String getName() {
        if (StringUtils.isNotBlank(getEntityId()) && KimInternalSuppressUtils.isSuppressName(getEntityId())) {
            return KimConstants.RESTRICTED_DATA_MASK;
        }
        return name;
    }
	
	public String getNameUnmasked() {
	    return this.name;
	}
	
	/**
	 * @see org.kuali.rice.kim.api.identity.Person#getPhoneNumber()
	 */
	public String getPhoneNumber() {
	    if (KimInternalSuppressUtils.isSuppressPhone(getEntityId())){
            return KimConstants.RESTRICTED_DATA_MASK;
        }
		return phoneNumber;
	}
	
	   /**
     * @see org.kuali.rice.kim.api.identity.Person#getPhoneNumberUnmasked()
     */
    public String getPhoneNumberUnmasked() {
        return phoneNumber;
    }

	/**
	 * @see org.kuali.rice.kim.api.identity.Person#getEmailAddress()
	 */
	public String getEmailAddress() {
	    if (KimInternalSuppressUtils.isSuppressEmail(getEntityId())){
            return KimConstants.RESTRICTED_DATA_MASK;
        }
		return emailAddress;
	}
	
	public String getEmailAddressUnmasked() {
	    return emailAddress;
	}
	
	public List<? extends EntityAffiliationContract> getAffiliations() {
		return affiliations;
	}
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kim.api.identity.Person#hasAffiliationOfType(java.lang.String)
	 */
	public boolean hasAffiliationOfType(String affiliationTypeCode) {
		return getCampusCodesForAffiliationOfType(affiliationTypeCode).size() > 0;
	}
	
	
	public List<String> getCampusCodesForAffiliationOfType(String affiliationTypeCode) {
		ArrayList<String> campusCodes = new ArrayList<String>( 3 );
		if ( affiliationTypeCode == null ) {
			return campusCodes;
		}
		for ( EntityAffiliationContract a : getAffiliations() ) {
			if ( a.getAffiliationType().getCode().equals(affiliationTypeCode)  ) {
				campusCodes.add( a.getCampusCode() );
			}
		}
		return campusCodes;
	}
	
	/**
	 * @see org.kuali.rice.kim.api.identity.Person#getExternalId(java.lang.String)
	 */
	public String getExternalId(String externalIdentifierTypeCode) {
		return externalIdentifiers.get( externalIdentifierTypeCode );
	}
	
	/**
	 * Pulls the campus code from the default affiliation for the identity.
	 * Returns null if no default affiliation is set.
	 * @see org.kuali.rice.kim.api.identity.Person#getCampusCode()
	 */
	public String getCampusCode() {
		return campusCode;
	}

	/**
	 * @return the personService
	 */
	@SuppressWarnings("unchecked")
	public static PersonService getPersonService() {
		if ( personService == null ) {
			personService = KimApiServiceLocator.getPersonService();
		}
		return personService;
	}

	/**
	 * @return the identityService
	 */
	public static IdentityService getIdentityService() {
		if ( identityService == null ) {
			identityService = KimApiServiceLocator.getIdentityService();
		}
		return identityService;
	}

	/**
	 * @see org.kuali.rice.kim.api.identity.Person#getExternalIdentifiers()
	 */
	public Map<String,String> getExternalIdentifiers() {
		return externalIdentifiers;
	}

	public String getAddressLine1() {
	    return address.getLine1();
	}
	
	public String getAddressLine1Unmasked() {
	    return address.getLine1Unmasked();
	}

	public String getAddressLine2() {
	    return address.getLine2();
	}
	
	public String getAddressLine2Unmasked() {
        return address.getLine2Unmasked();
    }

	public String getAddressLine3() {
	    return address.getLine3();
	}
	
	public String getAddressLine3Unmasked() {
        return address.getLine3Unmasked();
    }

	public String getAddressCity() {
	    return address.getCity();
	}
	
	public String getAddressCityUnmasked() {
        return address.getCityUnmasked();
    }

	public String getAddressStateProvinceCode() {
	    return address.getStateProvinceCode();
	}
	
	public String getAddressStateProvinceCodeUnmasked() {
        return address.getStateProvinceCodeUnmasked();
    }

	public String getAddressPostalCode() {
	    return address.getPostalCode();
	}
	
	public String getAddressPostalCodeUnmasked() {
        return address.getPostalCodeUnmasked();
    }

	public String getAddressCountryCode() {
	    return address.getCountryCode();
	}
	
	public String getAddressCountryCodeUnmasked() {
        return address.getCountryCodeUnmasked();
    }

	public String getEmployeeStatusCode() {
		return this.employeeStatusCode;
	}

	public String getEmployeeTypeCode() {
		return this.employeeTypeCode;
	}

	public KualiDecimal getBaseSalaryAmount() {
		return this.baseSalaryAmount;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public String getPrimaryDepartmentCode() {
		return this.primaryDepartmentCode;
	}

	public String getEntityTypeCode() {
		return this.entityTypeCode;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the lookupRoleNamespaceCode
	 */
	public String getLookupRoleNamespaceCode() {
		return this.lookupRoleNamespaceCode;
	}

	/**
	 * @param lookupRoleNamespaceCode the lookupRoleNamespaceCode to set
	 */
	public void setLookupRoleNamespaceCode(String lookupRoleNamespaceCode) {
		this.lookupRoleNamespaceCode = lookupRoleNamespaceCode;
	}

	/**
	 * @return the lookupRoleName
	 */
	public String getLookupRoleName() {
		return this.lookupRoleName;
	}

	/**
	 * @param lookupRoleName the lookupRoleName to set
	 */
	public void setLookupRoleName(String lookupRoleName) {
		this.lookupRoleName = lookupRoleName;
	}

	/**
	 * @param principalName the principalName to set
	 */
	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	//public Campus getCampus() {
	//	return this.campus;
	//}

	public EntityEmploymentStatusBo getEmployeeStatus() {
		return this.employeeStatus;
	}

	public EntityEmploymentTypeBo getEmployeeType() {
		return this.employeeType;
	}
}
