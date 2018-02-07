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
package org.kuali.rice.kim.api.identity.external;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * an external identifier for a KIM identity
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface EntityExternalIdentifierContract extends Versioned, GloballyUnique, Identifiable {

    /**
     * Gets this id of the parent identity object.
     * @return the identity id for this {@link EntityEmailContract}
     */
    String getEntityId();

    /**
     * Gets this {@link EntityExternalIdentifierContract}'s type code.
     * @return the type code for this {@link EntityExternalIdentifierContract}, or null if none has been assigned.
     */
	String getExternalIdentifierTypeCode();

	/**
     * Gets this {@link EntityExternalIdentifierContract}'s type.
     * @return the type for this {@link EntityExternalIdentifierContract}, or null if none has been assigned.
     */
	EntityExternalIdentifierTypeContract getExternalIdentifierType();
	
	/**
     * Gets this {@link EntityExternalIdentifierContract}'s external id.
     * @return the external id for this {@link EntityExternalIdentifierContract}, or null if none has been assigned.
     */
	String getExternalId();
}
