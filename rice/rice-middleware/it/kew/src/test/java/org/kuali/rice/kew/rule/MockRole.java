/**
 * Copyright 2005-2013 The Kuali Foundation
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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.identity.PrincipalName;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.workgroup.GroupNameId;



/**
 * A RoleAttribute implementation that can be used in tests to easily provide canned/pre-configured
 * recipients.  The role name should be a comma-delimited list of user authentication ids:
 * <pre>
 * <responsibilities>
 *   <responsibility>
 *     <role>org.kuali.rice.kew.rule.MockRole!user1,user2,user3</role>
 *   </responsibility>
 * </responsibilities>
 * </pre>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MockRole extends UnqualifiedRoleAttribute {
    private static final RoleName ROLE = new RoleName(MockRole.class.getName(), "List of authentication ids", "List of authentication ids");
    private static final List<RoleName> ROLES;
    static {
        ArrayList<RoleName> roles = new ArrayList<RoleName>(1);
        roles.add(ROLE);
        ROLES = Collections.unmodifiableList(roles);
    }

    public MockRole() {
        super(ROLES);
    }

    /**
     * Overridden to accept any role name
     * @see org.kuali.rice.kew.rule.UnqualifiedRoleAttribute#isValidRoleName(java.lang.String)
     */
    @Override
    protected boolean isValidRoleName(String roleName) {
        return true;
    }

    @Override
    protected ResolvedQualifiedRole resolveRole(RouteContext routeContext, String roleName) {
        String[] ids = roleName.split("[,\\s+]");
        ResolvedQualifiedRole rqr = new ResolvedQualifiedRole();
        for (String id: ids) {
            String type = "user";
            String[] components = id.split(":", 2);
            if (components.length > 1 && !StringUtils.isEmpty(components[0])) {
                type = components[0].trim();
            }
            Id recipientId;
            if ("user".equals(type)) {
                recipientId = new PrincipalName(id);
            } else if ("group".equals(type)) {
                recipientId = new GroupNameId(id);
            } else {
                throw new RuntimeException("Unknown role recipient type: '" + type + "'. Must be 'user' or 'group'.");
            }
            rqr.getRecipients().add(recipientId);
        }
        rqr.setQualifiedRoleLabel("Recipients from parsing mock role: " + roleName);
        rqr.setAnnotation("Recipients from parsing mock role: " + roleName);
        return rqr;
    }
}
