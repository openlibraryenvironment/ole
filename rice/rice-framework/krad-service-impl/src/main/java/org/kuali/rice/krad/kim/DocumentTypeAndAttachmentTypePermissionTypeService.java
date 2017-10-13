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
public class DocumentTypeAndAttachmentTypePermissionTypeService extends DocumentTypePermissionTypeServiceImpl {

	@Override
	protected List<Permission> performPermissionMatches(
			Map<String, String> requestedDetails,
			List<Permission> permissionsList) {

		List<Permission> matchingPermissions = new ArrayList<Permission>();
		if (requestedDetails == null) {
			return matchingPermissions; // empty list
		}
		// loop over the permissions, checking the non-document-related ones
		for (Permission kimPermissionInfo : permissionsList) {
            PermissionBo bo = PermissionBo.from(kimPermissionInfo);
			if (!bo.getDetails().containsKey(
						KimConstants.AttributeConstants.ATTACHMENT_TYPE_CODE)
			  || bo.getDetails().get(KimConstants.AttributeConstants.ATTACHMENT_TYPE_CODE)
				 .equals(requestedDetails.get(KimConstants.AttributeConstants.ATTACHMENT_TYPE_CODE)))
			{
				matchingPermissions.add(kimPermissionInfo);
			}

		}
		// now, filter the list to just those for the current document
		matchingPermissions = super.performPermissionMatches(requestedDetails,
				matchingPermissions);
		return matchingPermissions;
	}
}
