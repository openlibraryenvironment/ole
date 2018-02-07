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
package org.kuali.rice.kim.api.identity.name;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.mo.common.Defaultable;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.kim.api.identity.CodedAttributeContract;

import java.util.Date;
 /**
  * This contract represents the name information  associated with an Entity.
  *
  * @author Kuali Rice Team (rice.collab@kuali.org)
  */
public interface EntityNameContract extends Versioned, GloballyUnique, Defaultable, Inactivatable, Identifiable {
    public static final String NAME_CHANGED_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Gets this {@link Entity}'s id.
     * @return the id for this {@link EntityNameContract}, or null if none has been assigned.
     */
	String getEntityId();

	/**
     * Gets this {@link EntityNameContract}'s TypeContract.
     * @return the type for this {@link EntityNameContract}, or null if none has been assigned.
     */
	CodedAttributeContract getNameType();

	/**
     * Gets this {@link EntityNameContract}'s first name.
     * @return the first name for this {@link EntityNameContract}, or null if none has been assigned.
     */
	String getFirstName();

	/**
     * Gets this {@link EntityNameContract}'s unmasked first name.
     * @return the unmasked first name for this {@link EntityNameContract}, or null if none has been assigned.
     */
	String getFirstNameUnmasked();

	/**
     * Gets this {@link EntityNameContract}'s middle name.
     * @return the middle name for this {@link EntityNameContract}, or null if none has been assigned.
     */
	String getMiddleName();

	/**
     * Gets this {@link EntityNameContract}'s unmasked middle name.
     * @return the unmasked middle name for this {@link EntityNameContract}, or null if none has been assigned.
     */
	String getMiddleNameUnmasked();

	/**
     * Gets this {@link EntityNameContract}'s last name.
     * @return the last name for this {@link EntityNameContract}, or null if none has been assigned.
     */
	String getLastName();

	/**
     * Gets this {@link EntityNameContract}'s unmasked last name.
     * @return the unmasked last name for this {@link EntityNameContract}, or null if none has been assigned.
     */
	String getLastNameUnmasked();

	/**
     * Gets this {@link EntityNameContract}'s name prefix.
     * Corresponds to PESC NamePrefix.
     * @return the name prefix for this {@link EntityNameContract}, or null if none has been assigned.
     */
	String getNamePrefix();

	/**
     * Gets this {@link EntityNameContract}'s unmasked name prefix.
     * Corresponds to PESC NamePrefix.
     * @return the unmasked name prefix for this {@link EntityNameContract}, or null if none has been assigned.
     */
	String getNamePrefixUnmasked();

    /**
     * Gets this {@link EntityNameContract}'s name title.
     * Corresponds to PESC NameTitle.
     * @return the name title for this {@link EntityNameContract}, or null if none has been assigned.
     */
    String getNameTitle();

    /**
     * Gets this {@link EntityNameContract}'s unmasked name title.
     * Corresponds to PESC NameTitle.
     * @return the unmasked name title for this {@link EntityNameContract}, or null if none has been assigned.
     */
    String getNameTitleUnmasked();


	/**
     * Gets this {@link EntityNameContract}'s suffix.
     * Corresponds to PESC NameSuffix.
     * @return the suffix for this {@link EntityNameContract}, or null if none has been assigned.
     */
	String getNameSuffix();

	/**
     * Gets this {@link EntityNameContract}'s unmasked suffix.
     * Corresponds to PESC NameSuffix.
     * @return the unmasked suffix for this {@link EntityNameContract}, or null if none has been assigned.
     */
	String getNameSuffixUnmasked();

	/**
	 * Return the entire name as the person or system wants it displayed.
     * Corresponds to PESC CompositeName.
     * @return the complete name in the format of "lastName, firstName middleName"
	 */
	String getCompositeName();

	/**
     * Gets this {@link EntityNameContract}'s unmasked formatted name.
     * Corresponds to PESC CompositeName.
     * @return the complete name in the format of "lastName, firstName middleName"
     */
	String getCompositeNameUnmasked();

    /**
     * Get the note associated with this {@link EntityNameContract}
     * Corresponds to PESC NoteMessage
     * @return the message associated with this EntityName
     */
    String getNoteMessage();

    /**
     * Get the name change date {@link EntityNameContract}
     * Corresponds to PESC name changed date
     * @return the name change date of this EntityName
     */
    DateTime getNameChangedDate();

    /**
     * Returns a boolean value that determines if email fields should be suppressed.
     * @return boolean value that determines if email should be suppressed.
     */
	boolean isSuppressName();
}
