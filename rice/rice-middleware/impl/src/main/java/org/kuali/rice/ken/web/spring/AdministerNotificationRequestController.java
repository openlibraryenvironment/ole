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
package org.kuali.rice.ken.web.spring;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationChannelReviewerBo;
import org.kuali.rice.ken.document.kew.NotificationWorkflowDocument;
import org.kuali.rice.ken.service.NotificationMessageContentService;
import org.kuali.rice.ken.service.NotificationRecipientService;
import org.kuali.rice.ken.service.NotificationWorkflowDocumentService;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.ken.util.Util;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kim.api.KimConstants.KimGroupMemberTypes;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Implements reviewer Approve/Disapprove and initiator Acknowledge of a Notification requests
 * sent to channels configured with reviewers
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AdministerNotificationRequestController extends MultiActionController {
    private static final Logger LOG = Logger.getLogger(AdministerNotificationRequestController.class);

    /**
     * Command object for this controller
     */
    public static class AdministerNotificationRequestCommand {
        // incoming
        private String docId;

        // outgoing
        private WorkflowDocument document;
        private NotificationBo notification;
        private String renderedContent;
        private boolean valid = true;
        private String message;

        public String getDocId() {
            return docId;
        }
        public void setDocId(String docId) {
            this.docId = docId;
        }
        public WorkflowDocument getDocument() {
            return document;
        }
        public void setDocument(WorkflowDocument document) {
            this.document = document;
        }
        public NotificationBo getNotification() {
            return notification;
        }
        public void setNotification(NotificationBo notification) {
            this.notification = notification;
        }
        public String getRenderedContent() {
            return renderedContent;
        }
        public void setRenderedContent(String renderedContent) {
            this.renderedContent = renderedContent;
        }
        public boolean isValid() {
            return valid;
        }
        public void setValid(boolean valid) {
            this.valid = valid;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
    }

    protected NotificationMessageContentService messageContentService;
    protected NotificationWorkflowDocumentService workflowDocumentService;
    protected NotificationRecipientService recipientService;

    /**
     * Sets the messageContentService attribute value.
     * @param messageContentService the NotificationMessageContentService impl
     */
    public void setMessageContentService(
            NotificationMessageContentService notificationMessageContentService) {
        this.messageContentService = notificationMessageContentService;
    }

    /**
     * Sets the workflowDocumentService attribute value.
     * @param workflowDocumentService the NotificationWorkflowDocumentService impl
     */
    public void setWorkflowDocumentService(
            NotificationWorkflowDocumentService notificationWorkflowDocumentService) {
        this.workflowDocumentService = notificationWorkflowDocumentService;
    }

    /**
     * Sets the recipientService attribute value.
     * @param recipientService the NotificationRecipientService impl
     */
    public void setRecipientService(
            NotificationRecipientService notificationRecipientService) {
        this.recipientService = notificationRecipientService;
    }

    /**
     * Parses the serialized Notification xml from the workflow document application content into a reconstituted
     * Notification BO
     * @param document the WorkflowDocument
     * @return a Notification BO reconstituted from the serialized XML form in the workflow document
     * @throws Exception if parsing fails
     */
    private NotificationBo retrieveNotificationForWorkflowDocument(WorkflowDocument document) throws Exception {
        String notificationAsXml = document.getApplicationContent();

        //parse out the application content into a Notification BO
        NotificationBo notification = messageContentService.parseSerializedNotificationXml(notificationAsXml.getBytes());

        return notification;
    }

    /**
     * View action that displays an approve/disapprove/acknowledge view
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param command the command object bound for this MultiActionController
     * @return a view ModelAndView
     */
    public ModelAndView view(HttpServletRequest request, HttpServletResponse response, AdministerNotificationRequestCommand command) {
        // obtain a workflow user object first
        String initiatorId = request.getRemoteUser();

        // now construct the workflow document, which will interact with workflow
        if (command.getDocId() == null) {
            throw new RuntimeException("An invalid document ID was recieved from KEW's action list.");
        }

        //check to see which view is being passed to us from the notification list - pop up or inline
        String view = request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.COMMAND);
        String standaloneWindow = "true";
        if(view != null && view.equals(NotificationConstants.NOTIFICATION_DETAIL_VIEWS.INLINE)) {
            standaloneWindow = "false";
        }

        WorkflowDocument document;
        Map<String, Object> model = new HashMap<String, Object>();
        // set into model whether we are dealing with a pop up or an inline window
        model.put(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW, standaloneWindow);
        try {
            document = NotificationWorkflowDocument.loadNotificationDocument(initiatorId, command.getDocId());

            NotificationBo notification = retrieveNotificationForWorkflowDocument(document);

            // set up model
            command.setDocument(document);
            command.setNotification(notification);
            // render the event content according to registered XSLT stylesheet
            command.setRenderedContent(Util.transformContent(notification));

            LOG.info("notification auto remove date time: " + notification.getAutoRemoveDateTime());
            if (document.isApproved()) {
                command.setValid(false);
                command.setMessage("This notification request has been approved.");
            } else if (document.isDisapproved()) {
                command.setMessage("This notification request has been disapproved.");
            } else if (notification.getAutoRemoveDateTime() != null && notification.getAutoRemoveDateTimeValue().before(new Date(System.currentTimeMillis()))) {
                /*if (!document.stateIsCanceled()) {
                workflowDocumentService.terminateWorkflowDocument(new WorkflowDocument(new NetworkIdVO("notsys"), new Long(command.getDocId())));
                }*/
                // the autoremove date time has already passed...this notification request is null and void at this time
                boolean disapproved = document.isDisapproved();
                if (!document.isDisapproved()) {
                    List<NotificationChannelReviewerBo> reviewers = notification.getChannel().getReviewers();
                    String user = null;
                    for (NotificationChannelReviewerBo reviewer: reviewers) {
                        if (KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.equals(reviewer.getReviewerType())) {
                            if (reviewer.getReviewerId().equals(request.getRemoteUser())) {
                                user = request.getRemoteUser();
                            }
                        } else if (KimGroupMemberTypes.GROUP_MEMBER_TYPE.equals(reviewer.getReviewerType())) {
                            // if it's a group
                            String[] members = recipientService.getGroupMembers(reviewer.getReviewerId());
                            for (String member: members) {
                                if (StringUtils.equals(member, request.getRemoteUser())) {
                                    user = request.getRemoteUser();
                                    break;
                                }
                            }
                        }
                    }
                    // if the current user is a reviewer, then disapprove as that user
                    if (user != null) {
	                    WorkflowDocumentFactory.loadDocument(user, command.getDocId()).disapprove("Disapproving notification request.  Auto-remove datetime has already passed.");
                        disapproved = true;
                    }
                }
                command.setValid(false);
                if (disapproved) {
                    command.setMessage("This notification request is no longer valid because the Auto-Remove date has already passed.  It has been disapproved.  Please refresh your action list.");
                } else {
                    command.setMessage("This notification request is no longer valid because the Auto-Remove date has already passed.");
                }
            }

            model.put(getCommandName(command), command);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ModelAndView("ViewNotificationRequestDetails", model);
    }

    /**
     * Approve action that approves a notification request
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param command the command object bound for this MultiActionController
     * @return a view ModelAndView
     * @throws ServletException if an error occurs during approval
     */
    public ModelAndView approve(HttpServletRequest request, HttpServletResponse response, AdministerNotificationRequestCommand command) throws ServletException {
        administerEventNotificationMessage(request, response, command, "approve");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("workflowActionTaken", "Approved");
        model.put(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW, request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW));
        return new ModelAndView("SendNotificationRequestActionTakenWindow", model);
    }

    /**
     * Disapprove action that disapproves a notification request
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param command the command object bound for this MultiActionController
     * @return a view ModelAndView
     * @throws ServletException if an error occurs during disapproval
     */
    public ModelAndView disapprove(HttpServletRequest request, HttpServletResponse response, AdministerNotificationRequestCommand command) throws ServletException {
        administerEventNotificationMessage(request, response, command, "disapprove");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("workflowActionTaken", "Disapproved");
        model.put(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW, request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW));
        return new ModelAndView("SendNotificationRequestActionTakenWindow", model);
    }

    /**
     * Acknowledge action that acknowledges a notification request disapproval
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param command the command object bound for this MultiActionController
     * @return a view ModelAndView
     * @throws ServletException if an error occurs during acknowledgement
     */
    public ModelAndView acknowledge(HttpServletRequest request, HttpServletResponse response, AdministerNotificationRequestCommand command) throws ServletException {
        administerEventNotificationMessage(request, response, command, "acknowledge");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW, request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW));
        model.put("workflowActionTaken", "Acknowledged");
        return new ModelAndView("SendNotificationRequestActionTakenWindow", model);
    }

    /**
     * This method handles approval/disapproval/acknowledgement of the notification request
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param command the command object bound for this MultiActionController
     * @throws ServletException
     */
    private void administerEventNotificationMessage(HttpServletRequest request, HttpServletResponse response, AdministerNotificationRequestCommand command, String action) throws ServletException {
        LOG.debug("remoteUser: " + request.getRemoteUser());

        BindException bindException = new BindException(command, "command");
        ValidationUtils.rejectIfEmpty(bindException, "docId", "Document id must be specified");
        if (bindException.hasErrors()) {
            throw new ServletRequestBindingException("Document id must be specified", bindException);
        }

        // obtain a workflow user object first
        //WorkflowIdDTO user = new WorkflowIdDTO(request.getRemoteUser());
        String userId = request.getRemoteUser();

        try {
            // now construct the workflow document, which will interact with workflow
            WorkflowDocument document = NotificationWorkflowDocument.loadNotificationDocument(userId, command.getDocId());

            NotificationBo notification = retrieveNotificationForWorkflowDocument(document);

            String initiatorPrincipalId = document.getInitiatorPrincipalId();
            Person initiator = KimApiServiceLocator.getPersonService().getPerson(initiatorPrincipalId);
            String notificationBlurb =  notification.getContentType().getName() + " notification submitted by " + initiator.getName() + " for channel " + notification.getChannel().getName();
            if ("disapprove".equals(action)) {
                document.disapprove("User " + userId + " disapproving " + notificationBlurb);
            } else if ("approve".equals(action)) {
                document.approve("User " + userId + " approving " + notificationBlurb);
            } else if ("acknowledge".equals(action)) {
                document.acknowledge("User " + userId + " acknowledging " + notificationBlurb);
            }
        } catch (Exception e) {
            LOG.error("Exception occurred taking action on notification request", e);
            throw new ServletException("Exception occurred taking action on notification request", e);
        }
    }
}
