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
package org.kuali.rice.ken.document.kew;

import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;

/**
 * This class extends the KEW WorkflowDocument object and becomes our gateway for get a handle on
 * KEW documents in workflow.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class NotificationWorkflowDocument {

    private static final long serialVersionUID = 6125662798733898964L;

    /**
     * Constructs a NotificationWorkflowDocument instance - this essentially creates a new routable
     * document in KEW for the given user.
     * @param principalId
     * @throws org.kuali.rice.kew.api.exception.WorkflowException
     */
    public static WorkflowDocument createNotificationDocument(String principalId) {
        return WorkflowDocumentFactory.createDocument(principalId,
                NotificationConstants.KEW_CONSTANTS.NOTIFICATION_DOC_TYPE, null);
    }

    /**
     * Constructs a NotificationWorkflowDocument instance - this essentially creates a new routable
     * document in KEW for the given user and document type name.
     * @param user
     * @param documentTypeName
     * @throws org.kuali.rice.kew.api.exception.WorkflowException
     */
    public static WorkflowDocument createNotificationDocument(String principalId, String documentTypeName) {
        return WorkflowDocumentFactory.createDocument(principalId, documentTypeName, null);
    }

    /**
     * Constructs a NotificationWorkflowDocument instance - this one is used to get a handle on a
     * workflow document that was already created in the system.
     * @param user
     * @param documentId
     * @throws org.kuali.rice.kew.api.exception.WorkflowException
     */
    public static WorkflowDocument loadNotificationDocument(String principalId, String documentId) {
        return WorkflowDocumentFactory.loadDocument(principalId, documentId);
    }

    private NotificationWorkflowDocument() {}

}
