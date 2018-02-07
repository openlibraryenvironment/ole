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
package org.kuali.rice.ken.deliverer.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.core.GlobalNotificationServiceLocator;
import org.kuali.rice.ken.deliverer.NotificationMessageDeliverer;
import org.kuali.rice.ken.exception.NotificationAutoRemoveException;
import org.kuali.rice.ken.exception.NotificationMessageDeliveryException;
import org.kuali.rice.ken.service.NotificationWorkflowDocumentService;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.ken.util.Util;
import org.kuali.rice.kew.api.WorkflowDocument;

/**
 * This class is responsible for describing the default delivery mechanism for the system - the KEW
 * Action List.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KEWActionListMessageDeliverer implements NotificationMessageDeliverer {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(KEWActionListMessageDeliverer.class);

    /**
     * Property set in the attribute content that indicates the action received by workflow was
     * initiated by the Notification System itself (and not an end user)
     */
    public static final String INTERNAL_COMMAND_FLAG = "internal_command";

    private NotificationWorkflowDocumentService notificationWorkflowDocumentService;

    /**
     * Constructs a KEWActionListMessageDeliverer.java.
     */
    public KEWActionListMessageDeliverer() {
        this.notificationWorkflowDocumentService = GlobalNotificationServiceLocator.getInstance()
                .getNotificationWorkflowDocumentService();
    }

    /**
     * This implementation leverages the workflow integration services to push this notification
     * into the KEW action list.
     * @see org.kuali.rice.ken.deliverer.NotificationMessageDeliverer#deliverMessage(org.kuali.rice.ken.bo.NotificationMessageDelivery)
     */
    public void deliverMessage(NotificationMessageDelivery messageDelivery) throws NotificationMessageDeliveryException {
        // make the call to actually generate and ad-hoc route a workflow document
        String documentId = notificationWorkflowDocumentService.createAndAdHocRouteNotificationWorkflowDocument(
                messageDelivery,
                Util.getNotificationSystemUser(),
                messageDelivery.getUserRecipientId(),
                NotificationConstants.KEW_CONSTANTS.GENERIC_DELIVERY_ANNOTATION);

        // now set the workflow doc id into the message delivery's delivery system id
        messageDelivery.setDeliverySystemId(documentId);
        LOG.debug("Message Delivery: " + messageDelivery.toString());
    }

    /**
     * This implementation does an auto-remove by "canceling" the workflow document associated with
     * the message delivery record. This prevents the user from seeing the item in their list
     * anymore.
     * @see org.kuali.rice.ken.deliverer.NotificationMessageDeliverer#autoRemoveMessageDelivery(org.kuali.rice.ken.bo.NotificationMessageDelivery)
     */
    public void autoRemoveMessageDelivery(NotificationMessageDelivery messageDelivery)
            throws NotificationAutoRemoveException {
        // first retrieve the appropriate notification workflow document to "auto-remove" and proxy as the recipient
        WorkflowDocument workflowDoc = null;
        String sysId = messageDelivery.getDeliverySystemId();
        if (sysId == null) {
            LOG.error("NotificationMessageDelivery " + messageDelivery.getId()
                    + " is missing delivery system id (workflow document id");
            // there is no possibility for recovery, so since there is no id, we'll just log an error and return successfully instead
            // of throwing an exception
            return;
        }

        workflowDoc = notificationWorkflowDocumentService.getNotificationWorkflowDocumentByDocumentId(
                messageDelivery.getUserRecipientId(), sysId);

        flagWorkflowDocument(workflowDoc);

        notificationWorkflowDocumentService.clearAllFyisAndAcknowledgeNotificationWorkflowDocument(
                messageDelivery.getUserRecipientId(), workflowDoc,
                NotificationConstants.KEW_CONSTANTS.GENERIC_AUTO_REMOVE_ANNOTATION);
    }

    /**
     * @see org.kuali.rice.ken.deliverer.NotificationMessageDeliverer#dismissMessageDelivery(org.kuali.rice.ken.bo.NotificationMessageDelivery,
     *      java.lang.String, java.lang.String)
     */
    public void dismissMessageDelivery(NotificationMessageDelivery messageDelivery, String user, String cause) {
        // TODO: move hardcoded web controller actions here...
        LOG.info("Dismissing as user '" + user + "' workflow document '" + messageDelivery.getDeliverySystemId()
                + "' corresponding to message delivery #" + messageDelivery.getId() + " due to cause: " + cause);
        if (NotificationConstants.AUTO_REMOVE_CAUSE.equals(cause)) {
            // perform an auto-remove
            // XXX: currently auto-removes are going through autoremove method
        } else {
            WorkflowDocument nwd;
            nwd = notificationWorkflowDocumentService.getNotificationWorkflowDocumentByDocumentId(user,
                    messageDelivery.getDeliverySystemId());

            flagWorkflowDocument(nwd);

            if (NotificationConstants.ACK_CAUSE.equals(cause)) {
                // moved from NotificationController, ack command
                /*
                 * acknowledge using workflow docId
                 */
                if (nwd.isAcknowledgeRequested()) {
                    nwd.acknowledge("This notification has been acknowledged.");
                    LOG.debug("acknowledged " + nwd.getTitle());
                    LOG.debug("status display value: " + nwd.getStatus().getLabel());
                } else {
                    LOG.debug("Acknowledgement was not needed for document " + nwd.getDocumentId());
                }
            } else if (NotificationConstants.FYI_CAUSE.equals(cause)) {
                // moved from NotificationController, fyi command
                /*
                 * FYI using workflow docId
                 */
                if (nwd.isFYIRequested()) {
                    nwd.fyi();
                    LOG.debug("fyi " + nwd.getTitle());
                    LOG.debug("status display value: " + nwd.getStatus().getLabel());
                } else {
                    LOG.debug("FYI was not needed for document " + nwd.getDocumentId());
                }
            }
        }
    }

    /**
     * Marks the workflow document as originating from the Notification System, so that the
     * Notification post-processor does not route the action back through the Notification System.
     * @param doc the doc to monogram
     */
    protected void flagWorkflowDocument(WorkflowDocument doc) {
        Properties p = new Properties();
        p.setProperty(INTERNAL_COMMAND_FLAG, "true");
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        try {
            p.store(baos, null);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not store properties", ioe);
        }
        doc.setAttributeContent("<whatever>" + new String(baos.toByteArray()) + "</whatever>");
    }
}
