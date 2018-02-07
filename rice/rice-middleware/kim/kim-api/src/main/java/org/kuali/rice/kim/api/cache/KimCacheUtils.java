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
package org.kuali.rice.kim.api.cache;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.api.responsibility.ResponsibilityService;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class KimCacheUtils {

    private KimCacheUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Used for a caching condition to determine if a role passed to a method is derived or not.
     *
     * @param roleIds list of role id values
     *
     * @return true if list contains a derived role.
     */
    public static boolean isDynamicRoleMembership(List<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        RoleService roleService = KimApiServiceLocator.getRoleService();
        for (String roleId : roleIds) {
             if (roleService.isDynamicRoleMembership(roleId)) {
                 return true;
             }
        }
        return false;
    }

    /**
     * Used for a caching condition to determine if a role passed to a method is derived or not.
     *
     * @param namespaceCode namespaceCode of role
     * @param roleName name of role
     *
     * @return true if list contains role.
     */
    public static boolean isDynamicMembshipRoleByNamespaceAndName(String namespaceCode, String roleName) {
        List<String> roleIds = Collections.singletonList(
                KimApiServiceLocator.getRoleService().getRoleIdByNamespaceCodeAndName(namespaceCode, roleName));
        return isDynamicRoleMembership(roleIds);
    }

    /**
     * Used for a caching condition to determine if a responsibility is assigned to a derived role.
     *
     * @param responsibilityId id of responsibility
     *
     * @return true if assigned to a derived role.
     */
    public static boolean isResponsibilityIdAssignedToDynamicRole(String responsibilityId) {
        if ( StringUtils.isBlank(responsibilityId)) {
            return false;
        }

        List<String> roleIds = KimApiServiceLocator.getResponsibilityService().getRoleIdsForResponsibility(responsibilityId);
        return isDynamicRoleMembership(roleIds);
    }

    /**
     * Used for a caching condition to determine if a responsibility is assigned to a derived role.
     *
     * @param namespaceCode namespaceCode of responsibility
     * @param responsibilityName name of responsibility
     *
     * @return true if assigned to a derived role.
     */
    public static boolean isResponsibilityAssignedToDynamicRole(String namespaceCode, String responsibilityName) {
        if (StringUtils.isBlank(namespaceCode) || StringUtils.isBlank(responsibilityName)) {
            return false;
        }
        Responsibility responsibility = KimApiServiceLocator.getResponsibilityService().findRespByNamespaceCodeAndName(namespaceCode, responsibilityName);

        if (responsibility != null) {
            return isResponsibilityIdAssignedToDynamicRole(responsibility.getId());
        }
        return false;
    }

    /**
     * Used for a caching condition to determine if a responsibility template is assigned to a derived role.
     *
     * @param namespaceCode namespaceCode of permission
     * @param responsibilityTemplateName name of responsibility template
     *
     * @return true if assigned to a derived role.
     */
    public static boolean isResponsibilityTemplateAssignedToDynamicRole(String namespaceCode,
            String responsibilityTemplateName) {
        if (StringUtils.isBlank(namespaceCode) || StringUtils.isBlank(responsibilityTemplateName)) {
            return false;
        }
        ResponsibilityService respService = KimApiServiceLocator.getResponsibilityService();
        List<Responsibility> responsibilities = KimApiServiceLocator.getResponsibilityService().findResponsibilitiesByTemplate(namespaceCode, responsibilityTemplateName);
        List<String> roleIds = new ArrayList<String>();
        for (Responsibility resp : responsibilities) {
            roleIds.addAll(respService.getRoleIdsForResponsibility(resp.getId()));
        }

        return isDynamicRoleMembership(roleIds);
    }

}
