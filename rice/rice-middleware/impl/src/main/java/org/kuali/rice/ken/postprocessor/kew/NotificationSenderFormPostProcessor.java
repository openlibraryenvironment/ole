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
package org.kuali.rice.ken.postprocessor.kew;

import org.apache.log4j.Logger;
import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.core.GlobalNotificationServiceLocator;
import org.kuali.rice.ken.document.kew.NotificationWorkflowDocument;
import org.kuali.rice.ken.service.NotificationMessageContentService;
import org.kuali.rice.ken.service.NotificationService;
import org.kuali.rice.ken.util.Util;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent;
import org.kuali.rice.kew.framework.postprocessor.AfterProcessEvent;
import org.kuali.rice.kew.framework.postprocessor.BeforeProcessEvent;
import org.kuali.rice.kew.framework.postprocessor.DeleteEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentLockingEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.PostProcessor;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;

import java.util.List;


/**
 * This class is the post processor that gets run when the general notification 
 * message sending form is approved by its reviewers.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationSenderFormPostProcessor implements PostProcessor {
    private static final Logger LOG = Logger.getLogger(NotificationSenderFormPostProcessor.class);
    
    NotificationService notificationService;
    GenericDao businessObjectDao;
    NotificationMessageContentService messageContentService;
    
    /**
     * Constructs a NotificationSenderFormPostProcessor instance.
     */
    public NotificationSenderFormPostProcessor() {
        this.notificationService = GlobalNotificationServiceLocator.getInstance().getNotificationService();
        this.businessObjectDao = GlobalNotificationServiceLocator.getInstance().getGenericDao();
        this.messageContentService = GlobalNotificationServiceLocator.getInstance().getNotificationMessageContentService();
    }

    /**
     * Constructs a NotificationSenderFormPostProcessor instance.
     * @param notificationService
     * @param businessObjectDao
     */
    public NotificationSenderFormPostProcessor(NotificationService notificationService, GenericDao businessObjectDao) {
        this.notificationService = notificationService;
        this.businessObjectDao = businessObjectDao;
    }
    
    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doActionTaken(org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent)
     */
    @Override
    public ProcessDocReport doActionTaken(ActionTakenEvent arg0) throws Exception {
	    return new ProcessDocReport(true, "");
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#afterActionTaken(org.kuali.rice.kew.api.action.ActionType, org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent)
     */
    @Override
    public ProcessDocReport afterActionTaken(ActionType performed, ActionTakenEvent event) throws Exception {
        return new ProcessDocReport(true, "");
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doDeleteRouteHeader(org.kuali.rice.kew.framework.postprocessor.DeleteEvent)
     */
    @Override
    public ProcessDocReport doDeleteRouteHeader(DeleteEvent arg0) throws Exception {
	    return new ProcessDocReport(true, "");
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doRouteLevelChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange)
     */
    @Override
    public ProcessDocReport doRouteLevelChange(DocumentRouteLevelChange arg0) throws Exception {
	    return new ProcessDocReport(true, "");
    }

    /**
     * When the EDL simple message sending form is submitted, it is routed straight to FINAL and at that time (when RESOLVED), we 
     * actually send the notification.
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doRouteStatusChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange)
     */
    @Override
    public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange arg0) throws Exception {
        LOG.debug("ENTERING NotificationSenderFormPostProcessor.doRouteStatusChange() for Notification Sender Form with document ID: " + arg0.getDocumentId());

        if(arg0.getNewRouteStatus().equals(KewApiConstants.ROUTE_HEADER_PROCESSED_CD)) {
            LOG.debug("Workflow status has changed to RESOLVED for Notification Sender Form with document ID: " + arg0.getDocumentId() +
                ".  We are now calling the NotificationService.sendNotification() service.");

            // obtain a workflow user object first
            //NetworkIdDTO proxyUser = new NetworkIdDTO(Util.getNotificationSystemUser());
            String proxyUserId = Util.getNotificationSystemUser();

            // now construct the workflow document, which will interact with workflow
            WorkflowDocument document;
            try {
            document = NotificationWorkflowDocument.loadNotificationDocument(proxyUserId, arg0.getDocumentId());

            LOG.debug("XML:" + document.getApplicationContent());

            //parse out the application content into a Notification BO
                    NotificationBo notification = messageContentService.parseSerializedNotificationXml(document.getApplicationContent().getBytes());

                    LOG.debug("Notification Content: " + notification.getContent());

                    // send the notification
                    notificationService.sendNotification(notification);

                    LOG.debug("NotificationService.sendNotification() was successfully called for Notification Sender Form with document ID: " + arg0.getDocumentId());
            } catch(Exception e) {
            throw new RuntimeException(e);
            }
        }

        LOG.debug("LEAVING NotificationSenderFormPostProcessor.doRouteStatusChange() for Notification Sender Form with document ID: " + arg0.getDocumentId());
        return new ProcessDocReport(true, "");
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#beforeProcess(org.kuali.rice.kew.framework.postprocessor.BeforeProcessEvent)
     */
    @Override
    public ProcessDocReport beforeProcess(BeforeProcessEvent beforeProcessEvent) throws Exception {
        return new ProcessDocReport(true, "");
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#afterProcess(org.kuali.rice.kew.framework.postprocessor.AfterProcessEvent)
     */
    @Override
    public ProcessDocReport afterProcess(AfterProcessEvent afterProcessEvent) throws Exception {
        return new ProcessDocReport(true, "");
    }
    
    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#getDocumentIdsToLock(org.kuali.rice.kew.framework.postprocessor.DocumentLockingEvent)
     */
    @Override
	public List<String> getDocumentIdsToLock(DocumentLockingEvent documentLockingEvent) throws Exception {
		return null;
	}
	
}
