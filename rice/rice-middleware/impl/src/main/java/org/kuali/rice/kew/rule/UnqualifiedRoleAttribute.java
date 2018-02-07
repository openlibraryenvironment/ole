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

import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentContent;


/**
 * A simple base RoleAttribute implementation for roles that do not need to be qualified
 * prior to resolution.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class UnqualifiedRoleAttribute extends AbstractRoleAttribute {

    private static final long serialVersionUID = -356582375961050905L;
	protected List<RoleName> roles;

    /**
     * No-arg constructor for subclasses that will override {@link #getRoleNames()} to provide their own roles list
     */
    public UnqualifiedRoleAttribute() {
        roles = Collections.emptyList();
    }

    /**
     * Constructor for subclasses that can provide a role list at construction time
     */
    public UnqualifiedRoleAttribute(List<RoleName> roles) {
        this.roles = roles;
    }

    public List<RoleName> getRoleNames() {
        return roles;
    }

    /**
     * Returns a List<String> containing only the roleName parameter; i.e. no qualification occurs
     */
    public List<String> getQualifiedRoleNames(String roleName, DocumentContent documentContent) {
        List<String> qualifiedRoleName = new ArrayList<String>(1);
        qualifiedRoleName.add(roleName);
        return qualifiedRoleName;
    }

    /**
     * Helper method for parsing the actual role name out from the class/rolename combination
     * as Role class combines the two and does expose the original role name
     * @param classAndRole the class and role string (e.g. org.blah.MyRoleAttribute!SOME_ROLE_NAME)
     * @return the role name portion of the class and role string (e.g. SOME_ROLE_NAME);
     */
    protected String parseRoleNameFromClassAndRole(String classAndRole) {
        return classAndRole.substring(classAndRole.indexOf("!") + 1);
    }

    /**
     * @param roleName roleName to test
     * @return whether the roleName specifies a role that this attribute can resolve
     */
    protected boolean isValidRoleName(String roleName) {
        // this attribute should never be called to resolve any roles other than those it advertised as supporting!
        boolean valid = false;
        for (RoleName role: getRoleNames()) {
            if (parseRoleNameFromClassAndRole(role.getName()).equals(roleName)) {
                valid = true;
                break;
            }
        }
        return valid;
    }

    public ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, String roleName, String qualifiedRole) {
        // some sanity checking
        if (!roleName.equals(qualifiedRole)) {
            throw new IllegalArgumentException("UnqualifiedRoleAttribute resolveQualifiedRole invoked with differing role and qualified role (they should be the same)");
        }
        if (!isValidRoleName(roleName)) {
            throw new IllegalArgumentException("This attribute does not support the role: '" + roleName + "'");
        }
        return resolveRole(routeContext, roleName);
    }

    /**
     * Template method for subclasses to implement
     * @param routeContext the RouteContext
     * @param roleName the role name
     * @return a ResolvedQualifiedRole
     */
    protected abstract ResolvedQualifiedRole resolveRole(RouteContext routeContext, String roleName);
}
