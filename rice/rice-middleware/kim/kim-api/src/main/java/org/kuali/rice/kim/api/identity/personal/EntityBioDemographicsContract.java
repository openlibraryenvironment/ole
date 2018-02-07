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
package org.kuali.rice.kim.api.identity.personal;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Versioned;

import java.util.Date;
/**
 * This is a contract for EntityBioDemographics and represents the
 * demographic information of an Entity.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */


public interface EntityBioDemographicsContract extends Versioned, GloballyUnique {
    public static final String BIRTH_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DECEASED_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Gets this {@link EntityBioDemographicsContract}'s identity id.
     * @return the identity id for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getEntityId();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s deceased date.
     * @return the deceased date for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getDeceasedDate();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s birth date.
     * @return the birth date for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getBirthDate();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s gender code.
     * @return the gender code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getGenderCode();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s marital status code.
     * @return the marital status code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getMaritalStatusCode();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s primary language code.
     * @return the primary language code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getPrimaryLanguageCode();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s secondary language code.
     * @return the secondary language code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getSecondaryLanguageCode();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s country of birth code.
     * Corresponds to PESC BirthCountry.
     * @return the country of birth code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getBirthCountry();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s birth state or extra-state jurisdiction code.  Corresponds to PESC BirthStateProvinceCode.
     * @return the birth state code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getBirthStateProvinceCode();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s city of birth. Corresponds to PESC BirthCity.
     * @return the city of birth for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getBirthCity();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s geographic origin.
     * @return the geographic origin for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getGeographicOrigin();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s unmasked birth date.
     * @return the unmasked birth date for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getBirthDateUnmasked();

    /**
     * Gets this {@link EntityBioDemographicsContract}'s current age based on birth date if present.  Age calculation uses
     * deceased date if present.
     * @return the calculated age for this {@link EntityBioDemographicsContract}, or null if {@link org.kuali.rice.kim.api.identity.personal.EntityBioDemographicsContract#getBirthDate()} is unpresent, suppressed, or there is a calculation error.
     */
    Integer getAge();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s unmasked gender code.
     * @return the unmasked gender code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getGenderCodeUnmasked();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s unmasked martial status code.
     * @return the unmasked martial status code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getMaritalStatusCodeUnmasked();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s unmasked primary language code.
     * @return the unmasked primary language code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getPrimaryLanguageCodeUnmasked();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s unmasked secondary language code.
     * @return the unmasked secondary language code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getSecondaryLanguageCodeUnmasked();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s unmasked country of birth code.
     * @return the unmasked country of birth code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getBirthCountryUnmasked();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s unmasked birth state or extra-state jurisdiction code. Corresponds to PESC BirthStateProvinceCode.
     * @return the unmaksed birth state code for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getBirthStateProvinceCodeUnmasked();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s unmasked city of birth. Corresponds to PESC BirthCity.
     * @return the unmasked city of birth for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getBirthCityUnmasked();

	/**
     * Gets this {@link EntityBioDemographicsContract}'s unmasked geographic origin.
     * @return the unmasked geographic origin for this {@link EntityBioDemographicsContract}, or null if none has been assigned.
     */
	String getGeographicOriginUnmasked();

    /**
     * Get the note associated with this {@link EntityBioDemographicsContract}
     * Corresponds to PESC NoteMessage
     * @return the message associated with this EntityName
     */
    String getNoteMessage();

    /**
     * Get the {@link EntityBioDemographicsContract}'s gender change code
     * Corresponds to PESC GenderChangeCode
     * @return the gender change code or null if no change
     */
    String getGenderChangeCode();

    /**
     * Get the {@link EntityBioDemographicsContract}'s gender change code
     * Corresponds to PESC GenderChangeCode
     * @return the gender change code or null if no change
     */
    String getGenderChangeCodeUnmasked();

    /**
     * Returns a boolean value that determines if personal fields should be suppressed.
     * @return boolean value that determines if personal fields should be suppressed.
     */
	boolean isSuppressPersonal();
}
