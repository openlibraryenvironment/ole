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
package org.kuali.rice.kns.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
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
public class ComponentFieldPermissionTypeServiceImpl extends PermissionTypeServiceBase {
	
	/**
	 * Compare the component and property names between the request and matching permissions.
	 * Make entries with a matching property name take precedence over those with blank property 
	 * names on the stored permissions.  Only match entries with blank property names if
	 * no entries match on the exact property name. 
	 */
	@Override
	protected List<Permission> performPermissionMatches(Map<String, String> requestedDetails,
			List<Permission> permissionsList) {

        List<Permission> propertyMatches = new ArrayList<Permission>();
		List<Permission> prefixPropertyMatches = new ArrayList<Permission>();
		List<Permission> blankPropertyMatches = new ArrayList<Permission>();
		String propertyName = requestedDetails.get(KimConstants.AttributeConstants.PROPERTY_NAME);
		String componentName = requestedDetails.get(KimConstants.AttributeConstants.COMPONENT_NAME);
		for ( Permission kpi : permissionsList ) {
            PermissionBo bo = PermissionBo.from(kpi);
			if ( StringUtils.equals( componentName, bo.getDetails().get( KimConstants.AttributeConstants.COMPONENT_NAME ) ) ) {
				String permPropertyName = bo.getDetails().get(KimConstants.AttributeConstants.PROPERTY_NAME);
				if ( StringUtils.isBlank( permPropertyName ) ) {
					blankPropertyMatches.add( kpi );
				} else if ( StringUtils.equals( propertyName, permPropertyName ) ) {
					propertyMatches.add( kpi );
				} else if ( doesPropertyNameMatch(propertyName, permPropertyName) ) {
					prefixPropertyMatches.add( kpi );
				}
			}
		}
		if ( !propertyMatches.isEmpty() ) {
			return propertyMatches;
		} else if ( !prefixPropertyMatches.isEmpty() ) {
			return prefixPropertyMatches;
		} else {
			return blankPropertyMatches;
		}
	}

}
