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
import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.core.GlobalNotificationServiceLocator;
import org.kuali.rice.ken.deliverer.impl.KEWActionListMessageDeliverer;
import org.kuali.rice.ken.service.NotificationMessageDeliveryService;
import org.kuali.rice.ken.service.NotificationService;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.ken.util.Util;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


/**
 * This class is the post processor that gets run when workflow state changes occur for the 
 * underlying core NotificationDocumentType that all notifications go into KEW as.  This class is responsible for changing 
 * the state of the associated notification message delivery record after someone FYIs or ACKs their notification 
 * in the KEW Action List.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationPostProcessor implements PostProcessor {
    private static final Logger LOG = Logger.getLogger(NotificationPostProcessor.class);

    NotificationService notificationService;
    NotificationMessageDeliveryService msgDeliverySvc;

    /**
     * Constructs a NotificationPostProcessor instance.
     */
    public NotificationPostProcessor() {
        this.msgDeliverySvc = GlobalNotificationServiceLocator.getInstance().getNotificationMessageDeliveryService();
        this.notificationService = GlobalNotificationServiceLocator.getInstance().getNotificationService();
    }

    /**
     * Need to intercept ACKNOWLEDGE or FYI actions taken on notification workflow documents and set the local state of the 
     * Notification to REMOVED as well.
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doActionTaken(org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent)
     */
    public ProcessDocReport doActionTaken(ActionTakenEvent event) throws Exception {
        LOG.debug("ENTERING NotificationPostProcessor.doActionTaken() for Notification action item with document ID: " + event.getDocumentId());

        // NOTE: this action could be happening because the user initiated it via KEW, OR because a dismiss or autoremove action
        // has been invoked programmatically and the KEWActionListMessageDeliverer is taking an action...so there is a risk of being
        // invoked recursively (which will lead to locking issues and other problems).  We therefore mark the document in the KEWActionList
        // MessageDeliverer before performing an action, so that we can detect this scenario here, and avoid invoking KEN again.

        LOG.debug("ACTION TAKEN=" + event.getActionTaken().getActionTaken());

        String actionTakenCode = event.getActionTaken().getActionTaken().getCode();

        Properties p = new Properties();
        WorkflowDocument doc = WorkflowDocumentFactory.loadDocument(event.getActionTaken().getPrincipalId(), event.getDocumentId());
        try {
            p.load(new ByteArrayInputStream(doc.getAttributeContent().getBytes()));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        String internalCommand = p.getProperty(KEWActionListMessageDeliverer.INTERNAL_COMMAND_FLAG);

        if (Boolean.valueOf(internalCommand).booleanValue()) {
            LOG.info("Internal command detected by NotificationPostProcessor - will not invoke KEN");
            return new ProcessDocReport(true, "");
        }
        
        LOG.info("NotificationPostProcessor detected end-user action " + event.getActionTaken().getActionTaken() + " on document " + event.getActionTaken().getDocumentId());

        if(actionTakenCode.equals(KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD) || actionTakenCode.equals(KewApiConstants.ACTION_TAKEN_FYI_CD)) {
            LOG.debug("User has taken either acknowledge or fy action (action code=" + actionTakenCode + 
                    ") for Notification action item with document ID: " + event.getDocumentId() + 
            ".  We are now changing the status of the associated NotificationMessageDelivery to REMOVED.");

            try {
                NotificationMessageDelivery nmd = msgDeliverySvc.getNotificationMessageDeliveryByDelivererId(event.getDocumentId());

                if (nmd == null) {
                    throw new RuntimeException("Could not find message delivery from workflow document " + event.getDocumentId() + " to dismiss");
                }

                //get the id of the associated notification message delivery record
                String cause;
                if (KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD.equals(actionTakenCode)) {
                    cause = NotificationConstants.ACK_CAUSE;
                } else if (KewApiConstants.ACTION_TAKEN_FYI_CD.equals(actionTakenCode)) {
                    cause = NotificationConstants.FYI_CAUSE;
                } else {
                    cause = "unknown";
                }

                LOG.info("Dismissing message id " + nmd.getId() + " due to cause: " + cause);
                notificationService.dismissNotificationMessageDelivery(nmd.getId(),
                        Util.getNotificationSystemUser(),
                        cause);
            } catch(Exception e) {
                throw new RuntimeException("Error dismissing message", e);
            }
        }

        LOG.debug("LEAVING NotificationPostProcessor.doActionTaken() for Notification action item with document ID: " + event.getDocumentId());
        return new ProcessDocReport(true);
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
    public ProcessDocReport doDeleteRouteHeader(DeleteEvent arg0) throws Exception {
        return new ProcessDocReport(true, "");
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doRouteLevelChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange)
     */
    public ProcessDocReport doRouteLevelChange(DocumentRouteLevelChange arg0) throws Exception {
        return new ProcessDocReport(true, "");
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doRouteStatusChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange)
     */
    public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange arg0) throws Exception {
        return new ProcessDocReport(true, "");
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#beforeProcess(org.kuali.rice.kew.framework.postprocessor.BeforeProcessEvent)
     */
    public ProcessDocReport beforeProcess(BeforeProcessEvent beforeProcessEvent) throws Exception {
        return new ProcessDocReport(true, "");
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#afterProcess(org.kuali.rice.kew.framework.postprocessor.AfterProcessEvent)
     */
    public ProcessDocReport afterProcess(AfterProcessEvent afterProcessEvent) throws Exception {
        return new ProcessDocReport(true, "");
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#getDocumentIdsToLock(org.kuali.rice.kew.framework.postprocessor.DocumentLockingEvent)
     */
	public List<String> getDocumentIdsToLock(DocumentLockingEvent documentLockingEvent) throws Exception {
		return null;
	}
    
    
    
    
}
