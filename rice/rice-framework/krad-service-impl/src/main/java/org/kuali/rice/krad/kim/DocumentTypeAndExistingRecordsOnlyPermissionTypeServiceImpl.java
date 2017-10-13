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
import org.kuali.rice.kns.kim.permission.PermissionTypeServiceBase;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.Map;

public class DocumentTypeAndExistingRecordsOnlyPermissionTypeServiceImpl extends PermissionTypeServiceBase {

    @Override
    protected boolean performMatch(Map<String, String> inputMap,
			Map<String, String> storedMap) {

		String requestedDocumentType = inputMap.get(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME);
		String permissionDocumentType = storedMap.get(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME);
		if ( requestedDocumentType == null || permissionDocumentType == null ) {
			return false; // again, don't match if missing document types
		}
		// exit if document types don't match
		if ( !requestedDocumentType.equals(permissionDocumentType) ) {
			return false;
		}
		// check the existing attributes only flag
		if ( !Boolean.parseBoolean(storedMap.get(KimConstants.AttributeConstants.EXISTING_RECORDS_ONLY)) ) {
			// if not set, then any document action allowed
			return true;
		}
		// otherwise, only edit actions are allowed (no New/Copy)
		return StringUtils.equals(inputMap.get(KRADConstants.MAINTENANCE_ACTN), KRADConstants.MAINTENANCE_EDIT_ACTION);
	}
}
