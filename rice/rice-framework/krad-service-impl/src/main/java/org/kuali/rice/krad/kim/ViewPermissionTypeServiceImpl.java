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
package org.kuali.rice.krad.kim;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.kuali.rice.kns.kim.permission.PermissionTypeServiceBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Type service for the 'View' KIM type which matches on the id for a UIF view
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewPermissionTypeServiceImpl extends PermissionTypeServiceBase {
    private boolean exactMatchPriority = true;

    @Override
    protected List<String> getRequiredAttributes() {
        List<String> attributes = new ArrayList<String>(super.getRequiredAttributes());
        attributes.add(KimConstants.AttributeConstants.VIEW_ID);

        return Collections.unmodifiableList(attributes);
    }

    /**
     * Filters the given permission list to return those that match the view id qualifier
     *
     * <p>
     * By default, this method will return all exact matches if any exist, and it will only return partial matches
     * if there are no exact matches. i.e. KR-DocumentView will have priority over KR-*. If ExactMatchPriority is
     * false, then this method will return all exact AND partial matching permissions.  By default, ExactMatchPriority
     * will be set to true.
     * </p>
     *
     * @param requestedDetails - map of details requested with permission (used for matching)
     * @param permissionsList - list of permissions to process for matches
     * @return List<Permission> list of permissions that match the requested details
     */
    @Override
    protected List<Permission> performPermissionMatches(Map<String, String> requestedDetails,
            List<Permission> permissionsList) {
        List<Permission> matchingPermissions = new ArrayList<Permission>();

        String requestedViewId = requestedDetails.get(KimConstants.AttributeConstants.VIEW_ID);

        // add all exact matches to the list
        for (Permission permission : permissionsList) {
            PermissionBo bo = PermissionBo.from(permission);

            String permissionViewId = bo.getDetails().get(KimConstants.AttributeConstants.VIEW_ID);
            if (StringUtils.equals(requestedViewId, permissionViewId)) {
                matchingPermissions.add(permission);
            }
        }

        // add partial matches to the list if there are no exact matches or if exactMatchPriority is false
        if ((exactMatchPriority && matchingPermissions.isEmpty()) || (!(exactMatchPriority))) {
            for (Permission kpi : permissionsList) {
                PermissionBo bo = PermissionBo.from(kpi);

                String permissionViewId = bo.getDetails().get(KimConstants.AttributeConstants.VIEW_ID);
                if (requestedViewId != null && permissionViewId != null && (!(StringUtils.equals(requestedViewId,
                        permissionViewId))) && requestedViewId.matches(permissionViewId.replaceAll("\\*", ".*"))) {
                    matchingPermissions.add(kpi);
                }
            }
        }

        return matchingPermissions;
    }

    /**
     * Indicates whether permissions with details that exactly match the requested details have priority over
     * permissions with details that partially match (based on wildcard match). Default is set to true
     *
     * @return boolean true if exact matches should be given priority, false if not
     */
    public boolean getExactMatchPriority() {
        return this.exactMatchPriority;
    }

    /**
     * Setter for the exact match priority indicator
     *
     * @param exactMatchPriority
     */
    public void setExactMatchPriority(Boolean exactMatchPriority) {
        this.exactMatchPriority = exactMatchPriority;
    }
}
