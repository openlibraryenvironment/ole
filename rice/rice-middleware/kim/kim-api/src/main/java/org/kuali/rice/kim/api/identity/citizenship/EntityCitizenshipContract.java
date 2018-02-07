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
package org.kuali.rice.kim.api.identity.citizenship;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.kim.api.identity.CodedAttributeContract;
/**
 * This contract represents the citizenship information  associated with an Entity.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface EntityCitizenshipContract extends Versioned, GloballyUnique, Inactivatable, Identifiable {
	/**
     * Gets this id of the parent identity object.
     * @return the identity id for this {@link EntityCitizenshipContract}
     */
    String getEntityId();

    /**
     * Gets this {@link EntityCitizenshipContract}'s citizenship status object.
     * @return the Type object of citizenship status for this {@link EntityCitizenshipContract}, or null if none has been assigned.
     */
	CodedAttributeContract getStatus();

	/**
     * Gets this {@link EntityCitizenshipContract}'s country code.
     * @return the country code for this {@link EntityCitizenshipContract}, or null if none has been assigned.
     */
	String getCountryCode();

	/**
     * Gets this {@link EntityCitizenshipContract}'s start date.
     * @return the start date for this {@link EntityCitizenshipContract}, or null if none has been assigned.
     */
	DateTime getStartDate();

	/**
     * Gets this {@link EntityCitizenshipContract}'s end date.
     * @return the end date for this {@link EntityCitizenshipContract}, or null if none has been assigned.
     */
	DateTime getEndDate();
}
