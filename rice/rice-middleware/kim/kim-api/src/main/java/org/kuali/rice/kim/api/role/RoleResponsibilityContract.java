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
package org.kuali.rice.kim.api.role;

import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * An lightweight association of a Responsibility and a Role represented by references to the identifiers of a
 * Role and a Responsibility that are related to each other.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RoleResponsibilityContract extends Versioned, Inactivatable {

    /**
     * Provides the String identifier for a given RoleResponsibility
     *
     * @return id of the Role/Responsibility contract
     */
    String getRoleResponsibilityId();

    /**
     * Returns the String identifier for the Role represented by this Role/Responsibility association.
     *
     * @return Role Id
     */
    String getRoleId();

    /**
     * Returns the String identifier for the Responsibility represented by this Role/Responsibility association.
     *
     * @return Responsibility Id
     */
    String getResponsibilityId();
}
