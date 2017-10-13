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
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kew.api.doctype.DocumentTypeService;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.kuali.rice.kns.kim.permission.PermissionTypeServiceBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is a description of what this class does - mpham don't forget to fill
 * this in.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class DocumentTypePermissionTypeServiceImpl extends PermissionTypeServiceBase {
	protected transient DocumentTypeService documentTypeService;

    @Override
    protected List<String> getRequiredAttributes() {
        return Collections.singletonList(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME);
    }

    @Override
    protected boolean isCheckRequiredAttributes() {
        return true;
    }

	/**
	 * Loops over the given permissions and returns the most specific permission that matches.
	 * 
	 * That is, if a permission exists for the document type, then the permission for any 
	 * parent document will not be considered/returned.
	 *
	 */
	@Override
	protected List<Permission> performPermissionMatches(Map<String, String> requestedDetails,
            List<Permission> permissionsList) {
		// pull all the potential parent doc type names from the permission list
		Set<String> permissionDocTypeNames = new HashSet<String>( permissionsList.size() );
		for ( Permission permission : permissionsList ) {
            PermissionBo bo = PermissionBo.from(permission);
			String docTypeName = bo.getDetails().get( KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME );
			if ( StringUtils.isNotBlank( docTypeName ) ) {
				permissionDocTypeNames.add( docTypeName );
			}
		}
		// find the parent documents which match
		DocumentType docType = getDocumentTypeService().getDocumentTypeByName(requestedDetails.get(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME));
		String matchingDocTypeName = getClosestParentDocumentTypeName(docType, permissionDocTypeNames);
		// re-loop over the permissions and build a new list of the ones which have the
		// matching document type names in their details
		List<Permission> matchingPermissions = new ArrayList<Permission>();
		for ( Permission kpi : permissionsList ) {
            PermissionBo bo = PermissionBo.from(kpi);
			String docTypeName = bo.getDetails().get( KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME );
			// only allow a match on the "*" type if no matching document types were found
			if((StringUtils.isEmpty(matchingDocTypeName) && StringUtils.equals(docTypeName,"*")) 
				|| (StringUtils.isNotEmpty(matchingDocTypeName) && matchingDocTypeName.equals(docTypeName))) {
				matchingPermissions.add( kpi );
			}
		}

		return matchingPermissions;
	}
	
	protected DocumentTypeService getDocumentTypeService() {
		if ( documentTypeService == null ) {
			documentTypeService = KewApiServiceLocator.getDocumentTypeService();
		}
		return this.documentTypeService;
	}

}
