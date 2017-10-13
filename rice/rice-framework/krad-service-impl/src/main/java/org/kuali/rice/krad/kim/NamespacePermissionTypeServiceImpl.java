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
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NamespacePermissionTypeServiceImpl extends PermissionTypeServiceBase {

	private Boolean exactMatchPriority = true;
	{
//		requiredAttributes.add(KimAttributes.NAMESPACE_CODE);
	}
	
	/**
	 * Check for entries that match the namespace.
	 * 
	 * By default, this method will return all exact matches if any exist, and it will only return partial matches if there are no exact matches.
	 * i.e. KR-NS will have priority over KR-*
	 * 
     * If ExactMatchPriority is false, then this method will return all exact AND partial matching permissions.  By default, ExactMatchPriority will be set to true.
	 */
	@Override
	protected List<Permission> performPermissionMatches(Map<String, String> requestedDetails, List<Permission> permissionsList) {
		List<Permission> matchingPermissions = new ArrayList<Permission>();
		
		String requestedNamespaceCode = requestedDetails.get(KimConstants.AttributeConstants.NAMESPACE_CODE);
		
		// Add all exact matches to the list 
		for ( Permission permission : permissionsList ) {
            PermissionBo bo = PermissionBo.from(permission);
			String permissionNamespaceCode = bo.getDetails().get(KimConstants.AttributeConstants.NAMESPACE_CODE);
			if ( StringUtils.equals(requestedNamespaceCode, permissionNamespaceCode ) ) {
				matchingPermissions.add(permission);
			} 
		}
		
		// Add partial matches to the list if there are no exact matches or if exactMatchPriority is false
		if ((exactMatchPriority && matchingPermissions.isEmpty()) || (!(exactMatchPriority))) {
			for ( Permission kpi : permissionsList ) {
                PermissionBo bo = PermissionBo.from(kpi);
				String permissionNamespaceCode = bo.getDetails().get(KimConstants.AttributeConstants.NAMESPACE_CODE);
				if ( requestedNamespaceCode != null
					 && permissionNamespaceCode != null
					 && (!( StringUtils.equals(requestedNamespaceCode, permissionNamespaceCode ))) 
					 && requestedNamespaceCode.matches(permissionNamespaceCode.replaceAll("\\*", ".*") ) ) {
					 	matchingPermissions.add(kpi);
				}
			}
		}
        return matchingPermissions;
	}
		
	public Boolean getExactMatchPriority() {
		return this.exactMatchPriority;
	}

	public void setExactMatchPriority(Boolean exactMatchPriority) {
		this.exactMatchPriority = exactMatchPriority;
	}
}
