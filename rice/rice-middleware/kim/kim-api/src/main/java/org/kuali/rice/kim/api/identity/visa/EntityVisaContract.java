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
package org.kuali.rice.kim.api.identity.visa;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * visa information for a KIM identity
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public interface EntityVisaContract extends Versioned, GloballyUnique, Identifiable {

	/**
     * Gets this {@link EntityVisaContract}'s identity id.
     * @return the identity id for this {@link EntityVisaContract}, or null if none has been assigned.
     */
	String getEntityId();
	
	/**
     * Gets this {@link EntityVisaContract}'s visa type key.
     * @return the visa type key for this {@link EntityVisaContract}, or null if none has been assigned.
     */
	String getVisaTypeKey();
	
	/**
     * Gets this {@link EntityVisaContract}'s visa entry.
     * @return the visa entry for this {@link EntityVisaContract}, or null if none has been assigned.
     */
	String getVisaEntry();
	
	/**
     * Gets this {@link EntityVisaContract}'s visa id.
     * @return the visa id for this {@link EntityVisaContract}, or null if none has been assigned.
     */
	String getVisaId();
}
