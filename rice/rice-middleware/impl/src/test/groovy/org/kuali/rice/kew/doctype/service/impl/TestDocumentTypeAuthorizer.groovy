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
package org.kuali.rice.kew.doctype.service.impl

import org.kuali.rice.kew.doctype.bo.DocumentType
import org.kuali.rice.kew.engine.node.RouteNodeInstance
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue

/**
 * A custom document type permission service implementation that supplies additional permission details and role qualifiers
 */
class TestDocumentTypeAuthorizer extends KimDocumentTypeAuthorizer {
    static final ADDITIONAL_ENTRIES= [ ADDITIONAL_ENTRY: "ADDITIONAL_ENTRY" ];
    static final ADDITIONAL_DETAILS = ADDITIONAL_ENTRIES;
    static final ADDITIONAL_QUALIFIERS = ADDITIONAL_DETAILS;

    @Override
    def boolean useKimPermission(String namespace, String permissionTemplateName, Map<String, String> permissionDetails, boolean checkKimPriorityInd) { true }

    @Override
    protected Map<String, String> buildDocumentTypePermissionDetails(DocumentType documentType, String documentStatus, String actionRequestCode, String routeNodeName) {
        return super.buildDocumentTypePermissionDetails(documentType, documentStatus, actionRequestCode, routeNodeName) + ADDITIONAL_DETAILS
    }

    @Override
    protected Map<String, String> buildDocumentRoleQualifiers(DocumentRouteHeaderValue document, String routeNodeName) {
        return super.buildDocumentRoleQualifiers(document, routeNodeName) + ADDITIONAL_QUALIFIERS
    }
}