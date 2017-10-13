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
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 * The order of precedence for this permission type service is as follows:
 *
 * 1 - Group Name
 * 2 - Kim Type Name
 * 3 - Group Namespace
 *
 * If there is a permission that is an exact match for any of these, less granular permissions will not be considered.
 *
 * For example, if there is a populate group permission for KFS-VND groups, a populate group permission for KFS* groups
 * will not be considered.   Likewise, if there is a populate group permission for the group ContractManagers (which has
 * group namespace of KFS-VND), both the populate group permisson for KFS-VND and KFS* will NOT be considered.
 *
 *  ALSO NOTE - At a minimum, a group namespace attribute must be specifed on any populate group permission, even if
 *              it is only a partial namespace.
 */
public class PopulateGroupPermissionTypeServiceImpl extends NamespaceWildcardAllowedAndOrStringExactMatchPermissionTypeServiceImpl {

    @Override
    protected List<Permission> performPermissionMatches(Map<String, String> requestedDetails, List<Permission> permissionsList) {
        String requestedGroupName = requestedDetails.get(KimConstants.AttributeConstants.GROUP_NAME);
        String requestedKimTypeName = requestedDetails.get(KimConstants.AttributeConstants.KIM_TYPE_NAME);
        String requestedNamespaceCode = requestedDetails.get(KimConstants.AttributeConstants.NAMESPACE_CODE);

        List<Permission> exactMatchingPermissions = new ArrayList<Permission>();
        List<Permission> nonKimTypeMatchingPermissions = new ArrayList<Permission>();

        for (Permission kpi : permissionsList ) {
            PermissionBo bo = PermissionBo.from(kpi);
            String groupName = bo.getDetails().get(KimConstants.AttributeConstants.GROUP_NAME);
            if (StringUtils.equals(requestedGroupName, groupName)) {
                exactMatchingPermissions.add(kpi);
            }
        }

        if  (exactMatchingPermissions.isEmpty()) {
            for (Permission kpi : permissionsList ) {
                PermissionBo bo = PermissionBo.from(kpi);
                String kimTypeName = bo.getDetails().get(KimConstants.AttributeConstants.KIM_TYPE_NAME);
                String namespaceCode = bo.getDetails().get(KimConstants.AttributeConstants.NAMESPACE_CODE);
                if (StringUtils.equals(requestedKimTypeName, kimTypeName) &&
                    requestedNamespaceCode.matches(namespaceCode.replaceAll("\\*", ".*"))) {
                        exactMatchingPermissions.add(kpi);
                } else if (StringUtils.isEmpty(kimTypeName)) {
                    nonKimTypeMatchingPermissions.add(kpi);
                }
            }
        }

        if  (exactMatchingPermissions.isEmpty()) {
            for (Permission kpi : permissionsList ) {
                PermissionBo bo = PermissionBo.from(kpi);
                String namespaceCode = bo.getDetails().get(KimConstants.AttributeConstants.NAMESPACE_CODE);
                if (StringUtils.equals(requestedNamespaceCode, namespaceCode)) {
                    exactMatchingPermissions.add(kpi);
                }
            }
        }

        if  (!exactMatchingPermissions.isEmpty()) {
            return super.performPermissionMatches(requestedDetails, exactMatchingPermissions);
        } else {
            return super.performPermissionMatches(requestedDetails, nonKimTypeMatchingPermissions);
        }
    }
}
