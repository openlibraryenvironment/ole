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
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.namespace.Namespace;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.kuali.rice.krad.kim.NamespacePermissionTypeServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NamespaceWildcardAllowedAndOrStringExactMatchPermissionTypeServiceImpl
		extends NamespacePermissionTypeServiceImpl {
	protected static final String NAMESPACE_CODE = KimConstants.UniqueKeyConstants.NAMESPACE_CODE;
	
	protected String exactMatchStringAttributeName;
	protected boolean namespaceRequiredOnStoredMap;
    private List<String> requiredAttributes = new ArrayList<String>();

    @Override
    protected List<String> getRequiredAttributes() {
        return Collections.unmodifiableList(requiredAttributes);
    }

	@Override
	protected List<Permission> performPermissionMatches(Map<String, String> requestedDetails, List<Permission> permissionsList) {
	    List<Permission> matchingPermissions = new ArrayList<Permission>();
        List<Permission> matchingBlankPermissions = new ArrayList<Permission>();
	    String requestedAttributeValue = requestedDetails.get(exactMatchStringAttributeName);
	    for ( Permission kpi : permissionsList ) {
            PermissionBo bo = PermissionBo.from(kpi);
	        String permissionAttributeValue = bo.getDetails().get(exactMatchStringAttributeName);
	        if ( StringUtils.equals(requestedAttributeValue, permissionAttributeValue) ) {
	            matchingPermissions.add(kpi);
	        } else if ( StringUtils.isBlank(permissionAttributeValue) ) {
	            matchingBlankPermissions.add(kpi);
	        }
	    }
	    // if the exact match worked, use those when checking the namespace
	    // otherwise, use those with a blank additional property value
	    if ( !matchingPermissions.isEmpty() ) {
            List<Permission> matchingWithNamespace = super.performPermissionMatches(requestedDetails, matchingPermissions);
	        if ( !namespaceRequiredOnStoredMap ) {
	            // if the namespace is not required and the namespace match would have excluded
	            // the results, return the original set of matches
	            if ( matchingWithNamespace.isEmpty() ) {
	                return matchingPermissions;
	            }
	        }
            return matchingWithNamespace;
	    } else if ( !matchingBlankPermissions.isEmpty() ) {
            List<Permission> matchingWithNamespace = super.performPermissionMatches(requestedDetails, matchingBlankPermissions);
            if ( !namespaceRequiredOnStoredMap ) {
                // if the namespace is not required and the namespace match would have excluded
                // the results, return the original set of matches
                if ( matchingWithNamespace.isEmpty() ) {
                    return matchingBlankPermissions;
                }
            }
            return matchingWithNamespace;
	    }
	    return matchingPermissions; // will be empty if drops to here
	}
	
	public void setExactMatchStringAttributeName(
			String exactMatchStringAttributeName) {
		this.exactMatchStringAttributeName = exactMatchStringAttributeName;
		requiredAttributes.add(exactMatchStringAttributeName);
	}

	public void setNamespaceRequiredOnStoredMap(
			boolean namespaceRequiredOnStoredMap) {
		this.namespaceRequiredOnStoredMap = namespaceRequiredOnStoredMap;
	}

	/**
	 * Overrides the superclass's version of this method in order to account for "namespaceCode" permission detail values containing wildcards.
	 */
	@Override
	protected List<RemotableAttributeError> validateReferencesExistAndActive(KimType kimType, Map<String, String> attributes, List<RemotableAttributeError> previousValidationErrors) {
		List<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>();
		Map<String, String> nonNamespaceCodeAttributes = new HashMap<String, String>(attributes);
		// Check if "namespaceCode" is one of the permission detail values.
		if (attributes.containsKey(NAMESPACE_CODE)) {
			nonNamespaceCodeAttributes.remove(NAMESPACE_CODE);

            final Namespace namespace =
                    StringUtils.isBlank(attributes.get(NAMESPACE_CODE)) ?
                            null : CoreServiceApiServiceLocator.getNamespaceService().getNamespace(attributes.get(NAMESPACE_CODE));

            if (namespace != null) {
			    errors.addAll(super.validateReferencesExistAndActive(kimType, Collections.singletonMap(NAMESPACE_CODE,
                        namespace.getCode()), previousValidationErrors));
			} else {
				// If no namespaces were found, let the superclass generate an appropriate error.
				errors.addAll(super.validateReferencesExistAndActive(kimType, Collections.singletonMap(NAMESPACE_CODE,
                        attributes.get(NAMESPACE_CODE)), previousValidationErrors));
			}
		}
		// Validate all non-namespaceCode attributes.
		errors.addAll(super.validateReferencesExistAndActive(kimType, nonNamespaceCodeAttributes, previousValidationErrors));
		return errors;
	}
}
