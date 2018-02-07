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
package org.kuali.rice.kim.api.identity;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;

import java.util.List;
import java.util.Map;

/**
 * Person object for use by the KNS and KNS-based applications.  This provides an abstraction layer
 * between application code and the KIM objects to simplify use. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public interface Person extends ExternalizableBusinessObject {
	
	String getPrincipalId();
	String getPrincipalName();
	String getEntityId();
	String getEntityTypeCode();

	/**
	 * The first name from the default name record for the entity.
	 */
	String getFirstName();
	String getFirstNameUnmasked();
	String getMiddleName();
	String getMiddleNameUnmasked();
	String getLastName();
	String getLastNameUnmasked();

	/*
	 * Method which composites the first, middle and last names.
	 */
	String getName();
	String getNameUnmasked();

	String getEmailAddress();
	String getEmailAddressUnmasked();

	/**
	 * Returns line1 of the default address for the Person.  Will lazy-load the information from the
	 * IdentityManagementService if requested.
	 */
	String getAddressLine1();
	String getAddressLine1Unmasked();
	/**
	 * Returns line2 of the default address for the Person.  Will lazy-load the information from the
	 * IdentityManagementService if requested.
	 */
	String getAddressLine2();
	String getAddressLine2Unmasked();

	/**
	 * Returns line3 of the default address for the Person.  Will lazy-load the information from the
	 * IdentityManagementService if requested.
	 */
	String getAddressLine3();
	String getAddressLine3Unmasked();
	/**
	 * Returns the city name from the default address for the Person.  Will lazy-load the information from the
	 * IdentityManagementService if requested.
	 */
	String getAddressCity();
	String getAddressCityUnmasked();
	/**
	 * Returns the state code from the default address for the Person.  Will lazy-load the information from the
	 * IdentityManagementService if requested.
	 */
	String getAddressStateProvinceCode();
	String getAddressStateProvinceCodeUnmasked();
	/**
	 * Returns the postal code from the default address for the Person.  Will lazy-load the information from the
	 * IdentityManagementService if requested.
	 */
	String getAddressPostalCode();
	String getAddressPostalCodeUnmasked();
	/**
	 * Returns the country code from the default address for the Person.  Will lazy-load the information from the
	 * IdentityManagementService if requested.
	 */
	String getAddressCountryCode();
	String getAddressCountryCodeUnmasked();

	/** Returns the default phone number for the entity.
	 */
	String getPhoneNumber();
	String getPhoneNumberUnmasked();

	String getCampusCode();

	Map<String,String> getExternalIdentifiers();

	/** Checks whether the person has an affiliation of a particular type: staff/faculty/student/etc... */
	boolean hasAffiliationOfType( String affiliationTypeCode );

	List<String> getCampusCodesForAffiliationOfType(String affiliationTypeCode);

	String getEmployeeStatusCode();
	String getEmployeeTypeCode();
	KualiDecimal getBaseSalaryAmount();

	String getExternalId( String externalIdentifierTypeCode );

	String getPrimaryDepartmentCode();

	String getEmployeeId();
	boolean isActive();
}
