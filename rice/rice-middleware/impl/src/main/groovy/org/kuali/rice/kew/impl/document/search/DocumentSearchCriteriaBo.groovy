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
package org.kuali.rice.kew.impl.document.search

import org.kuali.rice.krad.bo.BusinessObject
import org.kuali.rice.kew.doctype.bo.DocumentType
import org.kuali.rice.kew.service.KEWServiceLocator

import org.kuali.rice.kim.api.identity.name.EntityName
import org.kuali.rice.kim.api.services.KimApiServiceLocator
import org.kuali.rice.kim.api.identity.Person
import org.kuali.rice.kew.api.document.search.DocumentSearchResult
import org.kuali.rice.kew.api.document.Document

import org.kuali.rice.kew.api.document.DocumentStatus
import java.sql.Timestamp
import org.kuali.rice.kim.api.group.Group
import org.kuali.rice.kim.impl.group.GroupBo
import org.kuali.rice.kim.api.identity.principal.Principal
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName

/**
 * Defines the business object that specifies the criteria used on document searches.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class DocumentSearchCriteriaBo implements BusinessObject {

    String documentTypeName
    String documentId
    String statusCode
    String applicationDocumentId
    String applicationDocumentStatus
    String title
    String initiatorPrincipalName
    String initiatorPrincipalId
    String viewerPrincipalName
    String viewerPrincipalId
    String groupViewerName
    String groupViewerId
    String approverPrincipalName
    String approverPrincipalId
    String routeNodeName
    String routeNodeLogic
    Timestamp dateCreated
    Timestamp dateLastModified
    Timestamp dateApproved
    Timestamp dateFinalized
    Timestamp dateApplicationDocumentStatusChanged
    String saveName

    void refresh() {
        // nothing to refresh
    }

    DocumentType getDocumentType() {
        if (documentTypeName == null) {
            return null
        }
    	return KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName)
    }

    Person getInitiatorPerson() {
        if (initiatorPrincipalId == null) {
            return null
        }
        return KimApiServiceLocator.getPersonService().getPerson(initiatorPrincipalId)
    }

    String getInitiatorDisplayName() {
        if(initiatorPrincipalId != null) {
            EntityNamePrincipalName entityNamePrincipalName = KimApiServiceLocator.getIdentityService().getDefaultNamesForPrincipalId(initiatorPrincipalId);
            if(entityNamePrincipalName != null){
                EntityName entityName = entityNamePrincipalName.getDefaultName();
                return entityName == null ? null : entityName.getCompositeName();
            }
        }
        return null;
    }

    Person getApproverPerson() {
        if (approverPrincipalName == null) {
            return null
        }
        return KimApiServiceLocator.getPersonService().getPersonByPrincipalName(approverPrincipalName)
    }

    Person getViewerPerson() {
        if (viewerPrincipalName == null) {
            return null
        }
        return KimApiServiceLocator.getPersonService().getPersonByPrincipalName(viewerPrincipalName)
    }

    GroupBo getGroupViewer() {
        if (groupViewerId == null) {
            return null
        }
        Group grp = KimApiServiceLocator.getGroupService().getGroup(groupViewerId)
        if(null!= grp){
            return GroupBo.from(grp);
        }  else{
            return null;
        }
    }
    String getStatusLabel() {
        if (statusCode == null) {
            return ""
        }
        return DocumentStatus.fromCode(statusCode).getLabel()
    }

    String getDocumentTypeLabel() {
        DocumentType documentType = getDocumentType()
        if (documentType != null) {
            return documentType.getLabel()
        }
        return ""
    }

    /**
     * Returns the route image which can be used to construct the route log link in custom lookup helper code.
     */
    String getRouteLog() {
        return "<img alt=\"Route Log for Document\" src=\"images/my_route_log.gif\"/>";
    }

    void populateFromDocumentSearchResult(DocumentSearchResult result) {
        Document document = result.document
        documentTypeName = document.documentTypeName
        documentId = document.documentId
        statusCode = document.status.code
        applicationDocumentId = document.applicationDocumentId
        applicationDocumentStatus = document.applicationDocumentStatus
        title = document.title
        initiatorPrincipalName = principalIdToName(document.initiatorPrincipalId)
        initiatorPrincipalId = document.initiatorPrincipalId
        dateCreated = new Timestamp(document.dateCreated.getMillis())
    }

    private String principalIdToName(String principalId) {
        if (principalId != null && principalId.trim() ) {
            Principal principal =  KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
            if(principal != null){
                return principal.getPrincipalName();
            }
        }
        return null
    }

}
