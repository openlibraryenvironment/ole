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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Type service for the 'View Line Action' KIM type which matches on the id for a UIF view, group id or collection
 * property name, field id or action event
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewLineActionPermissionTypeServiceImpl extends ViewPermissionTypeServiceImpl {

    @Override
    protected List<String> getRequiredAttributes() {
        List<String> attributes = new ArrayList<String>(super.getRequiredAttributes());
        attributes.add(KimConstants.AttributeConstants.VIEW_ID);

        return Collections.unmodifiableList(attributes);
    }

    /**
     * Filters the given permission list to return those that match on group id or collection property name, field
     * id or action event, then calls super to filter based on view id
     *
     * @param requestedDetails - map of details requested with permission (used for matching)
     * @param permissionsList - list of permissions to process for matches
     * @return List<Permission> list of permissions that match the requested details
     */
    @Override
    protected List<Permission> performPermissionMatches(Map<String, String> requestedDetails,
            List<Permission> permissionsList) {

        String requestedGroupId = null;
        if (requestedDetails.containsKey(KimConstants.AttributeConstants.GROUP_ID)) {
            requestedGroupId = requestedDetails.get(KimConstants.AttributeConstants.GROUP_ID);
        }

        String requestedCollectionPropertyName = null;
        if (requestedDetails.containsKey(KimConstants.AttributeConstants.COLLECTION_PROPERTY_NAME)) {
            requestedCollectionPropertyName = requestedDetails.get(
                    KimConstants.AttributeConstants.COLLECTION_PROPERTY_NAME);
        }

        String requestedFieldId = null;
        if (requestedDetails.containsKey(KimConstants.AttributeConstants.FIELD_ID)) {
            requestedFieldId = requestedDetails.get(KimConstants.AttributeConstants.FIELD_ID);
        }

        String requestedActionEvent = null;
        if (requestedDetails.containsKey(KimConstants.AttributeConstants.ACTION_EVENT)) {
            requestedActionEvent = requestedDetails.get(KimConstants.AttributeConstants.ACTION_EVENT);
        }

        List<Permission> matchingPermissions = new ArrayList<Permission>();
        for (Permission permission : permissionsList) {
            PermissionBo bo = PermissionBo.from(permission);

            String permissionGroupId = null;
            if (bo.getDetails().containsKey(KimConstants.AttributeConstants.GROUP_ID)) {
                permissionGroupId = bo.getDetails().get(KimConstants.AttributeConstants.GROUP_ID);
            }

            String permissionCollectionPropertyName = null;
            if (bo.getDetails().containsKey(KimConstants.AttributeConstants.COLLECTION_PROPERTY_NAME)) {
                permissionCollectionPropertyName = bo.getDetails().get(
                        KimConstants.AttributeConstants.COLLECTION_PROPERTY_NAME);
            }

            String permissionFieldId = null;
            if (bo.getDetails().containsKey(KimConstants.AttributeConstants.FIELD_ID)) {
                permissionFieldId = bo.getDetails().get(KimConstants.AttributeConstants.FIELD_ID);
            }

            String permissionActionEvent = null;
            if (bo.getDetails().containsKey(KimConstants.AttributeConstants.ACTION_EVENT)) {
                permissionActionEvent = bo.getDetails().get(KimConstants.AttributeConstants.ACTION_EVENT);
            }

            boolean groupMatch = false;
            if ((requestedGroupId != null) && (permissionGroupId != null) && StringUtils.equals(requestedGroupId,
                    permissionGroupId)) {
                groupMatch = true;
            } else if ((requestedCollectionPropertyName != null)
                    && (permissionCollectionPropertyName != null)
                    && StringUtils.equals(requestedCollectionPropertyName, permissionCollectionPropertyName)) {
                groupMatch = true;
            }

            if (groupMatch) {
                if ((requestedFieldId != null) && (permissionFieldId != null) && StringUtils.equals(requestedFieldId,
                        permissionFieldId)) {
                    matchingPermissions.add(permission);
                } else if ((requestedActionEvent != null) && (permissionActionEvent != null) && StringUtils.equals(
                        requestedActionEvent, permissionActionEvent)) {
                    matchingPermissions.add(permission);
                }
            }
        }

        return super.performPermissionMatches(requestedDetails, matchingPermissions);
    }

}
