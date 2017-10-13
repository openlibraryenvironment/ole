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
package org.kuali.rice.kew.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kns.kim.permission.PermissionTypeServiceBase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Gives people with a Backdoor Restriction permission the ability to backdoor as anyone
 *
 * @author IU Kuali Rice Team (rice.collab@kuali.org)
 */

public class BackdoorRestrictionPermissionTypeServiceImpl extends PermissionTypeServiceBase {
    @Override
    protected List<Permission> performPermissionMatches(Map<String, String> requestedDetails,
            List<Permission> permissionsList) {
        // pull all the potential app codes from the permission list
        Set<String> permissionAppCodes = new HashSet<String>(permissionsList.size());
        for (Permission permission : permissionsList) {
            String appCode = permission.getAttributes().get(KimConstants.AttributeConstants.APP_CODE);
            if (StringUtils.isNotBlank(appCode)) {
                permissionAppCodes.add(appCode);
            }
        }
        String requestedAppCode = requestedDetails.get(KimConstants.AttributeConstants.APP_CODE);
        // re-loop over the permissions and build a new list of the ones which have the
        // matching app codes in their details
        List<Permission> matchingPermissions = new ArrayList<Permission>();
        for (Permission permission : permissionsList) {
            String currentAppCode = permission.getAttributes().get(KimConstants.AttributeConstants.APP_CODE);
            if (StringUtils.isNotEmpty(requestedAppCode) && requestedAppCode.equals(currentAppCode)) {
                matchingPermissions.add(permission);
            }
        }

        return matchingPermissions;
    }
}

