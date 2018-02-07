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
package org.kuali.rice.kim.api.identity.principal;


import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
/**
 * This is a contract for Principal.
 *
 * A principal represents an entity that can authenticate. A principal has an ID that is
 * used to uniquely identify it. It also has a name that represents the principal's username.
 * All principals are associated with one and only one entity.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface PrincipalContract extends Inactivatable, Versioned, GloballyUnique {
   /**
     * Gets this {@link PrincipalContract}'s id.
     * @return the id for this {@link PrincipalContract}, or null if none has been assigned.
     */
	String getPrincipalId();
	
	/**
     * Gets this {@link PrincipalContract}'s name.
     * @return the name for this {@link PrincipalContract}, this value cannot be null.
     */
	String getPrincipalName();
	
	/**
     * Gets this {@link PrincipalContract}'s identity id.
     * @return the identity id for this {@link PrincipalContract}, or null if none has been assigned.
     */
	String getEntityId();
	
}
