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
 * RoleAttribute that exposes an INITIATOR abstract role which resolves to the
 * initiator of the document.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class InitiatorRoleAttribute extends UnqualifiedRoleAttribute {
    private static final String INITIATOR_ROLE_KEY = "INITIATOR";
    private static final String INITIATOR_ROLE_LABEL = "Initiator";

    private static final RoleName ROLE = new RoleName(InitiatorRoleAttribute.class.getName(), INITIATOR_ROLE_KEY, INITIATOR_ROLE_LABEL);
    private static final List<RoleName> ROLES;
    static {
        ArrayList<RoleName> roles = new ArrayList<RoleName>(1);
        roles.add(ROLE);
        ROLES = Collections.unmodifiableList(roles);
    }

    public InitiatorRoleAttribute() {
        super(ROLES);
    }

    public ResolvedQualifiedRole resolveRole(RouteContext routeContext, String roleName) {
        List members = new ArrayList(1);
        members.add(new PrincipalId(routeContext.getDocument().getInitiatorWorkflowId()));
        return new ResolvedQualifiedRole(INITIATOR_ROLE_LABEL, members);
    }
}
