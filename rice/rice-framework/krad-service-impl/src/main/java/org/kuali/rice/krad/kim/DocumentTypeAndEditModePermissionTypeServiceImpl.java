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

import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.kuali.rice.krad.kim.DocumentTypePermissionTypeServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentTypeAndEditModePermissionTypeServiceImpl extends DocumentTypePermissionTypeServiceImpl {

    @Override
    protected List<String> getRequiredAttributes() {
        List<String> attrs = new ArrayList<String>(super.getRequiredAttributes());
        attrs.add(KimConstants.AttributeConstants.EDIT_MODE);
        return attrs;
    }
	
	@Override
	protected List<Permission> performPermissionMatches(
			Map<String, String> requestedDetails,
			List<Permission> permissionsList) {

        List<Permission> matchingPermissions = new ArrayList<Permission>();
		for (Permission permission : permissionsList) {
            PermissionBo bo = PermissionBo.from(permission);
			if (requestedDetails.get(KimConstants.AttributeConstants.EDIT_MODE).equals(bo.getDetails().get(KimConstants.AttributeConstants.EDIT_MODE))) {
				matchingPermissions.add(permission);
			}
		}
		return super.performPermissionMatches(requestedDetails, matchingPermissions);
	}
}
