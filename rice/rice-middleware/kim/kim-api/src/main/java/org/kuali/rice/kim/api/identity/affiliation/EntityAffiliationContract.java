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
package org.kuali.rice.kim.api.identity.affiliation;

import org.kuali.rice.core.api.mo.common.Defaultable;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
/**
 * This contract represents an affiliation for an Entity.
 * Each person must have at least one affiliation associated with it.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface EntityAffiliationContract extends Versioned, GloballyUnique, Defaultable, Inactivatable, Identifiable {

    /**
     * Gets this id of the parent identity object.
     * @return the identity id for this {@link EntityAddressContract}
     */
    String getEntityId();

	/**
     * Gets this {@link KimEntityAffiliation}'s type.
     * @return the type for this {@link KimEntityAffiliation}, or null if none has been assigned.
     */
	EntityAffiliationTypeContract getAffiliationType();

	/**
     * Gets this {@link KimEntityAffiliation}'s campus code.
     * @return the campus code for this {@link KimEntityAffiliation}, or null if none has been assigned.
     */
	String getCampusCode();
}
