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
package org.kuali.rice.krms.impl.authorization;

/**
 * This class contains the authorization for KRMS Agendas
 */
public interface AgendaAuthorizationService {
    /**
     * Checks if the current user is authorized to perform the action on an agenda in the specified context.
     *
     * When the contextId is unknown (i.e. at initiation of a new Agenda document) then null should be passed as
     * the contextId. On save the authorization needs to be checked again with the contextId.  The business rules
     * should have taken care of this.
     *
     * @param permissionName
     * @param contextId
     * @return true if current user is authorized, false otherwise
     */
    public boolean isAuthorized(String permissionName, String contextId);
}
