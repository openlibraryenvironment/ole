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
package org.kuali.rice.kew.routemodule;

import java.util.List;

import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.util.ResponsibleParty;


/**
 * A RouteModule is responsible for generating Action Requests for a given Route Header document.
 * Implementations of this class are potentially remotable, so this interface uses value objects.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RouteModule {

    /**
     * Generate action requests for the given RouteContext.
     *
     * @return A List of the generated ActionRequestValue objects.
     */
    List<ActionRequestValue> findActionRequests(RouteContext context) throws Exception;

    /**
     * The route module will re-resolve the given responsibilityId and return an object that contains the key to either
     * a user, group, or role.  The original responsibility associated with the given id will then be swapped with the
     * new ResponsibleParty when routing is resolved.
     *
     * <p>This method is intended to allow for the calling code to be able to "switch-out" one responsibility for
     * another (in the case of a change to an assigned reponsibility).</p>
     *
     * <p>Returning null from this method will indicate that the original responsibile party should remain unchanged.
     * This is typically the recommended default for implementation of this method and covers most use cases.</p>
     *
     * @param responsibilityId the id of the responsibility to be resolved
     * @return the responsibility party with which to replace the original responsibility, or null if the responsibility
     * should remain unchanged.
     */
    ResponsibleParty resolveResponsibilityId(String responsibilityId) throws WorkflowException;

    /**
     * Returns true if this RouteModule has more requests available which it can generate.  This method will only be
     * called after the first set of action requests returned by the first invocation of findActionRequests have been
     * fully activated.  If this method returns true, findActionRequests will be invoked again and those requests will
     * all be activated before the cycle continues until this method returns false;
     *
     * @param context the route context
     * @return true if this route module has more requests it can generate, false otherwise
     */
    boolean isMoreRequestsAvailable(RouteContext context);

}
