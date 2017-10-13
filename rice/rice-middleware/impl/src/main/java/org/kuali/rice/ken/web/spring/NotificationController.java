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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.bo.NotificationRecipientBo;
import org.kuali.rice.ken.bo.NotificationSenderBo;
import org.kuali.rice.ken.service.NotificationMessageDeliveryService;
import org.kuali.rice.ken.service.NotificationService;
import org.kuali.rice.ken.service.NotificationWorkflowDocumentService;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.ken.util.Util;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


/**
 * This class is the controller for the basic notification related actions - viewing, etc.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationController extends MultiActionController {
    /** Logger for this class and subclasses */
    private static final Logger LOG = Logger.getLogger(NotificationController.class);
    
    protected NotificationService notificationService;
    protected NotificationWorkflowDocumentService notificationWorkflowDocService;
    protected NotificationMessageDeliveryService messageDeliveryService;
   
    /**
     * Set the NotificationService
     * @param notificationService
     */   
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * This method sets the NotificationWorkflowDocumentService
     * @param s
     */
    public void setNotificationWorkflowDocumentService(NotificationWorkflowDocumentService s) {
        this.notificationWorkflowDocService = s;
    }

    /**
     * Sets the messageDeliveryService attribute value.
     * @param messageDeliveryService The messageDeliveryService to set.
     */
    public void setMessageDeliveryService(NotificationMessageDeliveryService messageDeliveryService) {
        this.messageDeliveryService = messageDeliveryService;
    }

    /**
     * Handles the display of the main home page in the system.
     * @param request : a servlet request
     * @param response : a servlet response
     * @throws ServletException : an exception
     * @throws IOException : an exception
     * @return a ModelAndView object
     */   
    public ModelAndView displayHome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "HomePage";
        LOG.debug("remoteUser: "+request.getRemoteUser());
        Map<String, Object> model = new HashMap<String, Object>(); 
        return new ModelAndView(view, model);
    }
   
    /**
     * This method handles displaying the notifications that an individual sent.
     * @param request
     * @param response
     * @return ModelAndView
     * @throws ServletException
     * @throws IOException
     */
    public ModelAndView displayNotificationsSent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "NotificationsSent";
        LOG.debug("remoteUser: "+request.getRemoteUser());
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("userId", request.getRemoteUser());
        return new ModelAndView(view, model);
    }

    /**
     * This method handles displaying the search screen.
     * @param request
     * @param response
     * @return ModelAndView
     * @throws ServletException
     * @throws IOException
     */
    public ModelAndView displaySearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "Search";
        LOG.debug("remoteUser: "+request.getRemoteUser());
        Map<String, Object> model = new HashMap<String, Object>(); 
        return new ModelAndView(view, model);
    }

    /**
     * This method displays the user lookup screen.
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public ModelAndView displayLookupUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "LookupUsers";
        LOG.debug("remoteUser: "+request.getRemoteUser());
        Map<String, Object> model = new HashMap<String, Object>(); 
        return new ModelAndView(view, model);
    }

    /**
     * This method displays the workgroup lookup screen.
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public ModelAndView displayLookupWorkgroups(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "LookupWorkgroups";
        LOG.debug("remoteUser: "+request.getRemoteUser());
        Map<String, Object> model = new HashMap<String, Object>(); 
        return new ModelAndView(view, model);
    }


    /**
     * This method retrieves the NotificationMessageDelivery given an HttpServletRequest which
     * may contain EITHER a message delivery id or a workflow doc id.  Therefore, this is a
     * "special case" for handling the workflow deliverer.
     * @param request the incoming {@link HttpServletRequest}
     * @return the {@link NotificationMessageDelivery} or null if not found
     */
    protected NotificationMessageDelivery determineMessageFromRequest(HttpServletRequest request) {
        /**
         * We can get the NotificationMessageDelivery object given a workflow ID or a NotificationMessageDelivery
         * Id.  This method might be called either from a workflow action list or
         * as a link from a message deliverer endpoint such as an email message.
         */
        String messageDeliveryId = request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.MSG_DELIVERY_ID);
        String delivererId = request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.DELIVERER_ID);
        if (delivererId == null) {
            delivererId = request.getParameter(KewApiConstants.DOCUMENT_ID_PARAMETER);
        }

        NotificationMessageDelivery messageDelivery;
        if (messageDeliveryId != null) { // this means that the request came in not from the action list, but rather from a delivery end point
            LOG.debug("Looking up notification with messageDeliveryId: "+messageDeliveryId);
            try {
                messageDelivery = messageDeliveryService.getNotificationMessageDelivery(new Long(messageDeliveryId));
            } catch (Exception e) {
                throw new RuntimeException("Error getting message with id: " + messageDeliveryId, e);
            }
        } else if (delivererId != null) {  // this means that the request was triggered via the action list
            LOG.debug("Looking up notification with workflowId: "+delivererId);
            try {
                messageDelivery = messageDeliveryService.getNotificationMessageDeliveryByDelivererId(delivererId);
            } catch (Exception e) {
                LOG.error("Error getting message with from deliverer id: " + delivererId, e);
                throw new RuntimeException("Error getting message with deliverer id: " + delivererId, e);
            }
        } else {
            throw new RuntimeException("Neither message ('" + NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.MSG_DELIVERY_ID
                                       + "') nor deliverer id ('" + NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.DELIVERER_ID + "') were specified in the request");
        }
        
        return messageDelivery;
    }

    /**
     * @param req the {@link HttpServletRequest}
     * @return whether the incoming request was from the action list
     */
    protected boolean requestIsFromKEW(HttpServletRequest req) {
        return req.getParameter(KewApiConstants.DOCUMENT_ID_PARAMETER) != null;
    }

    /**
     * This controller handles displaying the appropriate notification details for a specific record.
     * @param request : a servlet request
     * @param response : a servlet response
     * @throws ServletException : an exception
     * @throws IOException : an exception
     * @return a ModelAndView object
     */   
    public ModelAndView displayNotificationDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "NotificationDetail"; // default to full view

        String principalNm = request.getRemoteUser();
        String command = request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.COMMAND);
        String standaloneWindow = request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW);

        NotificationMessageDelivery messageDelivery = determineMessageFromRequest(request);
        // now get the notification from the message delivery object
        NotificationBo notification = messageDelivery.getNotification();
        boolean actionable = false;

        if (requestIsFromKEW(request)) {
            // check to see if this was a standalone window by examining the command from KEW before setting it to INLINE to force an inline view
            if(command != null && 
                    (command.equals(NotificationConstants.NOTIFICATION_DETAIL_VIEWS.NORMAL_VIEW) || 
                            command.equals(NotificationConstants.NOTIFICATION_DETAIL_VIEWS.DOC_SEARCH_VIEW))) {
                standaloneWindow = "true";
            }

            // we want all messages from the action list in line
            command = NotificationConstants.NOTIFICATION_DETAIL_VIEWS.INLINE;
        }
        
        Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalNm);
        if (principal != null) {
        	actionable = (principal.getPrincipalId()).equals(messageDelivery.getUserRecipientId()) && NotificationConstants.MESSAGE_DELIVERY_STATUS.DELIVERED.equals(messageDelivery.getMessageDeliveryStatus());
        } else {
            throw new RuntimeException("There is no principal for principalNm " + principalNm);
        }
        
        List<NotificationSenderBo> senders = notification.getSenders();
        List<NotificationRecipientBo> recipients = notification.getRecipients();

        String contenthtml = Util.transformContent(notification);

        // check to see if the details need to be rendered in line (no stuff around them)
        if (command != null && command.equals(NotificationConstants.NOTIFICATION_DETAIL_VIEWS.INLINE)) {
            view = "NotificationDetailInline";   
        } 

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("notification", notification);
        model.put("senders", senders);
        model.put("recipients", recipients);
        model.put("contenthtml", contenthtml);
        model.put("messageDeliveryId", messageDelivery.getId());
        model.put("command", command);
        model.put("actionable", actionable);
        model.put(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW, standaloneWindow);
        return new ModelAndView(view, model);
    }

    /**
     * This method handles user dismissal of a message
     * @param request : a servlet request
     * @param response : a servlet response
     * @return a ModelAndView object
     */   
    public ModelAndView dismissMessage(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter("action");
        if (command == null) throw new RuntimeException("Dismissal command not specified");

        if (NotificationConstants.ACK_CAUSE.equals(command)) {
            return dismissMessage(command, "Notificaton acknowledged.  Please refresh your action list.", request, response);
        } else if (NotificationConstants.FYI_CAUSE.equals(command)) {
            return dismissMessage(command, "Action Taken.  Please refresh your action list.", request, response);
        } else {
            throw new RuntimeException("Unknown dismissal command: " + command);
        }
    }

    /**
     * This method takes an action on the message delivery - dismisses it with the action/cause that comes from the
     * UI layer
     * @param action the action or cause of the dismissal
     * @param message the message to display to the user
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @return an appropriate ModelAndView
     */
    private ModelAndView dismissMessage(String action, String message, HttpServletRequest request, HttpServletResponse response) {
        String view = "NotificationDetail";

        String principalNm = request.getRemoteUser();
        String messageDeliveryId = request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.MSG_DELIVERY_ID);
        String command = request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.COMMAND);
        String standaloneWindow = request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW);

        if (messageDeliveryId == null) {
            throw new RuntimeException("A null messageDeliveryId was provided.");
        }

        LOG.debug("messageDeliveryId: "+messageDeliveryId);
        LOG.debug("command: "+command);

        /**
         * We can get the notification object given a workflow ID or a notification
         * Id.  This method might be called either from a workflow action list or
         * as a link from a message deliverer endpoint such as an email message.  
         */        
        NotificationMessageDelivery delivery = messageDeliveryService.getNotificationMessageDelivery(Long.decode(messageDeliveryId));
        if (delivery == null) {
            throw new RuntimeException("Could not find message delivery with id " + messageDeliveryId);
        }
        NotificationBo notification = delivery.getNotification();

        /*
         * dismiss the message delivery
         */

        Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalNm);
        notificationService.dismissNotificationMessageDelivery(delivery.getId(), principal.getPrincipalId(), action);

        List<NotificationSenderBo> senders = notification.getSenders();
        List<NotificationRecipientBo> recipients = notification.getRecipients();

        String contenthtml = Util.transformContent(notification);       

        // first check to see if this is a standalone window, b/c if it is, we'll want to close
        if(standaloneWindow != null && standaloneWindow.equals("true")) {
            view = "NotificationActionTakenCloseWindow";
        } else { // otherwise check to see if the details need to be rendered in line (no stuff around them)
            if (command != null && command.equals(NotificationConstants.NOTIFICATION_DETAIL_VIEWS.INLINE)) { 
                view = "NotificationDetailInline";   
            }
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("notification", notification);
        model.put("message", message);
        model.put("senders", senders);
        model.put("recipients", recipients);
        model.put("contenthtml", contenthtml);
        model.put("messageDeliveryId", messageDeliveryId);
        model.put("command", command);
        model.put(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW, standaloneWindow);
        return new ModelAndView(view, model);
    }
}
