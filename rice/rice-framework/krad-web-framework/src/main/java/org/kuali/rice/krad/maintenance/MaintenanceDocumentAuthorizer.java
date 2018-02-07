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
package org.kuali.rice.krad.maintenance;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.document.DocumentAuthorizer;

/**
 * Authorizer class for {@link MaintenanceDocument} instances
 *
 * <p>
 * The <code>MaintenanceDocumentAuthorizer</code> extends the available actions of <code>DocumentAuthorizer</code>
 * for <code>MaintenanceDocument</code>s.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see org.kuali.rice.krad.document.DocumentAuthorizer
 */
public interface MaintenanceDocumentAuthorizer extends DocumentAuthorizer {

    /**
     * Determines whether the user has the permission to create a new <code>MaintenanceDocument</code>
     *
     * @param boClass <code>Class</code> of the <code>MaintenanceDocument</code>
     * @param user <code>Person</code> instance of the current user
     * @return <code>true</code> if the user is allowed to create a new <code>MaintenanceDocument</code> for
     *         the boClass, <code>false</code> otherwise
     */
    public boolean canCreate(Class boClass, Person user);

    /**
     * Determines whether the user has the permission to maintain a <code>MaintenanceDocument</code>
     *
     * @param dataObject <code>Object</code> of the <code>MaintenanceDocument</code>
     * @param user <code>Person</code> instance of the current user
     * @return <code>true</code> if the user is allowed to maintain the dataObject, <code>false</code> otherwise
     */
    public boolean canMaintain(Object dataObject, Person user);

    /**
     * Determines whether the user has the permission to create or maintain a <code>MaintenanceDocument</code>
     *
     * @param maintenanceDocument the <code>MaintenanceDocument</code>
     * @param user <code>Person</code> instance of the current user
     * @return <code>true</code> if the user is allowed to create or maintain the <code>MaintenanceDocument</code>,
     *         <code>false</code> otherwise
     */
    public boolean canCreateOrMaintain(MaintenanceDocument maintenanceDocument, Person user);

}
