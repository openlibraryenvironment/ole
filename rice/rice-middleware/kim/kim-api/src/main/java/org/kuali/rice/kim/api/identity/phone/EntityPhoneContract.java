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
package org.kuali.rice.kim.api.identity.phone;

import org.kuali.rice.core.api.mo.common.Defaultable;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.kim.api.identity.CodedAttributeContract;
/**
 * This is a contract for EntityPhone and represents the
 * phone information of an Entity.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public interface EntityPhoneContract extends Versioned, GloballyUnique, Defaultable, Inactivatable, Identifiable {

    /**
     * Gets this id of the parent identity object.
     * @return the identity id for this {@link EntityPhoneContract}
     */
    String getEntityId();

    /**
     * Gets this entityTypeCode of the {@link EntityPhoneContract}'s object.
     * @return the identity type code for this {@link EntityPhoneContract}
     */
    String getEntityTypeCode();

	/**
     * Gets this {@link EntityPhone}'s type code.
     * @return the type code for this {@link EntityPhone}, or null if none has been assigned.
     */
	CodedAttributeContract getPhoneType();

	/**
     * Gets this {@link EntityPhone}'s phone number.
     * @return the phone number for this {@link EntityPhone}, or null if none has been assigned.
     */
	String getPhoneNumber();

	/**
     * Gets this {@link EntityPhone}'s extension number.
     * @return the extension number for this {@link EntityPhone}, or null if none has been assigned.
     */
	String getExtensionNumber();

	/**
     * Gets this {@link EntityPhone}'s country code.
     * @return the country code for this {@link EntityPhone}, or null if none has been assigned.
     */
	String getCountryCode();

	/**
     * Gets this {@link EntityPhone}'s unmasked phone number.
     * @return the unmasked phone number for this {@link EntityPhone}, or null if none has been assigned.
     */
	String getPhoneNumberUnmasked();

	/**
     * Gets this {@link EntityPhone}'s unmasked extension number.
     * @return the unmasked extension number for this {@link EntityPhone}, or null if none has been assigned.
     */
    String getExtensionNumberUnmasked();

    /**
     * Gets this {@link EntityPhone}'s unmasked country code.
     * @return the unmasked country code for this {@link EntityPhone}, or null if none has been assigned.
     */
    String getCountryCodeUnmasked();

    /**
     * Gets this {@link EntityPhone}'s formatted phone number.
     * @return the formatted phone number for this {@link EntityPhone}, or null if none has been assigned.
     */
    String getFormattedPhoneNumber();

    /**
     * Gets this {@link EntityPhone}'s unmasked formatted phone number.
     * @return the unmasked formatted phone number for this {@link EntityPhone}, or null if none has been assigned.
     */
	String getFormattedPhoneNumberUnmasked();

    /**
     * Returns a boolean value that determines if phone fields should be suppressed.
     * @return boolean value that determines if phone should be suppressed.
     */
	boolean isSuppressPhone();
}
