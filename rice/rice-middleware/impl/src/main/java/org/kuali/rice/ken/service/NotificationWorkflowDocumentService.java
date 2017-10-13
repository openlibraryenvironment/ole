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
package org.kuali.rice.ken.service;

import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.kew.api.WorkflowDocument;


/**
 * The NotificationWorkflowDocumentService class is responsible for housing service methods for interacting with KEW.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationWorkflowDocumentService {
    /**
     * This service method is responsible for creating a NotificationWorkflowDocument for the given user, which in turn, 
     * creates a workflow document in KEW.  It then ad-hoc routes the document to the passed in recipient.  This 
     * method will be used by the NotificationService.deliverNotification() service method.
     * @param messageDelivery - the specific NotificationMessageDelivery that is associated with this ad hoc route
     * @param initiatorUserId - the person/workflow user who is responsible for "initiating" this workflow document
     * @param recipientUserId - the person/workflow user who will recieve this document via an ad hoc route
     * @param annotation - a description of the workflow ad hoc route transaction
     * @return String - the id of the workflow document
     */
    public String createAndAdHocRouteNotificationWorkflowDocument(NotificationMessageDelivery messageDelivery, String initiatorUserId, 
	    String recipientUserId, String annotation);
    
    /**
     * This method is responsible for canceling a workflow document; which in turn simulates the "checking-off" 
     * of a notification in the notification list by the system through an auto-removal.
     * @param initiatorUserId
     * @param workflowDocument
     * @param annotation
     */
    public void clearAllFyisAndAcknowledgeNotificationWorkflowDocument(String initiatorUserId, WorkflowDocument workflowDocument, String annotation);

    /**
     * This method is responsible for unconditionally terminating a workflow document, after which there should be no
     * pending action requests against this document.
     * @param document workflow document to terminate
     */
    public void terminateWorkflowDocument(WorkflowDocument document);

    /**
     * This service method is responsible for retrieving a NotificationWorkflowDocument from KEW.
     * @param initiatorUserId
     * @param workflowDocumentId
     * @return NotificationWorkflowDocument
     */
    public WorkflowDocument getNotificationWorkflowDocumentByDocumentId(String initiatorUserId, String workflowDocumentId);
}
