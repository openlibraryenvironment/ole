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
import org.kuali.rice.krad.kim.DocumentTypePermissionTypeServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentTypeAndNodeOrStatePermissionTypeServiceImpl extends DocumentTypePermissionTypeServiceImpl {

	/**
     *  Permission type service which can check the route node and status as well as the document hierarchy.
     *  
     *  Permission should be able to (in addition to taking the routingStatus, routingNote, and documentTypeName attributes) 
     *  should take a documentNumber and retrieve those values from workflow before performing the comparison.
     *
     *  consider the document type hierarchy - check for a permission that just specifies the document type first at each level 
     *  - then if you don't find that, check for the doc type and the node, then the doc type and the state. 
     *
	 */
    @Override
    protected List<Permission> performPermissionMatches(Map<String, String> requestedDetails,
            List<Permission> permissionsList) {
        List<Permission> matchingPermissions = new ArrayList<Permission>();
        // loop over the permissions, checking the non-document-related ones
        for ( Permission kpi : permissionsList ) {
            PermissionBo bo = PermissionBo.from(kpi);
            if ( routeNodeMatches(requestedDetails, bo.getDetails()) &&
                 routeStatusMatches(requestedDetails, bo.getDetails()) &&
                 appDocStatusMatches(requestedDetails, bo.getDetails()) ) {
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
		return StringUtils.equals( requestedDetails.get(KimConstants.AttributeConstants.ROUTE_NODE_NAME), permissionDetails.get(KimConstants.AttributeConstants.ROUTE_NODE_NAME));
	}
	
	protected boolean routeStatusMatches(Map<String, String> requestedDetails, Map<String, String> permissionDetails) {
		if ( StringUtils.isBlank( permissionDetails.get(KimConstants.AttributeConstants.ROUTE_STATUS_CODE) ) ) {
			return true;
		}
		return StringUtils.equals( requestedDetails.get(KimConstants.AttributeConstants.ROUTE_STATUS_CODE), permissionDetails.get(
                KimConstants.AttributeConstants.ROUTE_STATUS_CODE));
	}

    protected boolean appDocStatusMatches(Map<String, String> requestedDetails, Map<String, String> permissionDetails) {
        if ( StringUtils.isBlank( permissionDetails.get(KimConstants.AttributeConstants.APP_DOC_STATUS) ) ) {
            return true;
        }
        return StringUtils.equals( requestedDetails.get(KimConstants.AttributeConstants.APP_DOC_STATUS), permissionDetails.get(
                KimConstants.AttributeConstants.APP_DOC_STATUS));
    }

}
