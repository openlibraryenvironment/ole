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
package org.kuali.rice.kim.api.identity.residency;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * residency info for a KIM identity
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public interface EntityResidencyContract extends Versioned, GloballyUnique, Identifiable {

	
	/**
     * Gets this {@link EntityResidencyContract}'s identity id.
     * @return the identity id for this {@link EntityResidencyContract}, or null if none has been assigned.
     */
	String getEntityId();
	
	/**
     * Gets this {@link EntityResidencyContract}'s determination method.
     * @return the determination method for this {@link EntityResidencyContract}, or null if none has been assigned.
     */
	String getDeterminationMethod();
	
	/**
     * Gets the state this {@link EntityResidencyContract} is in.
     * @return the state this {@link EntityResidencyContract} is in, or null if none has been assigned.
     */
	String getInState();
}
