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
package org.kuali.rice.kew.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kuali.rice.kew.api.identity.PrincipalId;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.engine.RouteContext;

/**
 * RoleAttribute that exposes a document's user who routed the document
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RoutedByUserRoleAttribute extends UnqualifiedRoleAttribute {
    private static final long serialVersionUID = -7099014184598851664L;
	private static final String ROUTED_BY_USER_ROLE_KEY = "ROUTED_BY_USER";
    private static final String ROUTED_BY_USER_ROLE_LABEL = "Routed By User";

    private static final RoleName ROLE = new RoleName(RoutedByUserRoleAttribute.class.getName(), ROUTED_BY_USER_ROLE_KEY, ROUTED_BY_USER_ROLE_LABEL);
    private static final List<RoleName> ROLES;
    static {
        ArrayList<RoleName> roles = new ArrayList<RoleName>(1);
        roles.add(ROLE);
        ROLES = Collections.unmodifiableList(roles);
    }

    public RoutedByUserRoleAttribute() {
        super(ROLES);
    }

    public ResolvedQualifiedRole resolveRole(RouteContext routeContext, String roleName) {
        // sounds like the role label should be specified as the first parameter here,
        // but I'll follow AccountAttribute's lead and specify the role key
        List members = new ArrayList(1);
        //members.add(routeContext.getDocument().getRoutedByPrincipal().getPrincipalId());
        members.add(new PrincipalId(routeContext.getDocument().getRoutedByUserWorkflowId()));
        return new ResolvedQualifiedRole(ROUTED_BY_USER_ROLE_LABEL, members);
    }
}
