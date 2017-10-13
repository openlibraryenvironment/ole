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
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.impl.permission.PermissionBo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentTypeAndNodeAndRouteStatusPermissionTypeServiceImpl extends DocumentTypePermissionTypeServiceImpl {

    @Override
    protected boolean isCheckRequiredAttributes() {
        return true;
    }

	/**
	 * 
	 *	consider the document type hierarchy - check for a permission that just specifies the document type first at each level 
	 *	- then if you don't find that, check for the doc type and the node, then the doc type and the Action Type.
	 *
	 */
	@Override
	protected List<Permission> performPermissionMatches(Map<String, String> requestedDetails,
			List<Permission> permissionsList) {

        List<Permission> matchingPermissions = new ArrayList<Permission>();
		// loop over the permissions, checking the non-document-related ones
		for ( Permission kpi : permissionsList ) {
            PermissionBo bo = PermissionBo.from(kpi);
			if ( (routeNodeMatches(requestedDetails, bo.getDetails())) &&
				 (routeStatusMatches(requestedDetails, bo.getDetails())) ) {
				matchingPermissions.add( kpi );
			}			
		}
		// now, filter the list to just those for the current document
		matchingPermissions = super.performPermissionMatches( requestedDetails, matchingPermissions );
		return matchingPermissions;
	}
		
	protected boolean routeNodeMatches(Map<String, String> requestedDetails, Map<String, String> permissionDetails) {
        if ( StringUtils.isBlank( permissionDetails.get(KimConstants.AttributeConstants.ROUTE_NODE_NAME) ) ) {
            return true;
        }
        return StringUtils.equals(requestedDetails.get(KimConstants.AttributeConstants.ROUTE_NODE_NAME), permissionDetails.get(
                KimConstants.AttributeConstants.ROUTE_NODE_NAME));
    }

    protected boolean routeStatusMatches(Map<String, String> requestedDetails, Map<String, String> permissionDetails) {
        if ( (StringUtils.isBlank(permissionDetails.get(KimConstants.AttributeConstants.ROUTE_STATUS_CODE))) &&
             (!(StringUtils.equals(KewApiConstants.ROUTE_HEADER_INITIATED_CD, requestedDetails.get(KimConstants.AttributeConstants.ROUTE_STATUS_CODE))))) {
            return true;
        }
        return StringUtils.equals( requestedDetails.get(KimConstants.AttributeConstants.ROUTE_STATUS_CODE), permissionDetails.get(
                KimConstants.AttributeConstants.ROUTE_STATUS_CODE));
    }
}
