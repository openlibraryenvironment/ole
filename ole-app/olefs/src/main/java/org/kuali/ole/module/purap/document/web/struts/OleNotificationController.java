package org.kuali.ole.module.purap.document.web.struts;

import org.apache.log4j.Logger;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.bo.NotificationRecipientBo;
import org.kuali.rice.ken.bo.NotificationSenderBo;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.ken.util.Util;
import org.kuali.rice.ken.web.spring.NotificationController;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleNotificationController extends NotificationController {
    /**
     * Logger for this class and subclasses
     */
    private static final Logger LOG = Logger.getLogger(NotificationController.class);

    /**
     * This method takes an action on the message delivery - dismisses it with the action/cause that comes from the
     * UI layer
     *
     * @param action   the action or cause of the dismissal
     * @param message  the message to display to the user
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @return an appropriate ModelAndView
     */

    private ModelAndView dismissMessage(String action, String message, HttpServletRequest request, HttpServletResponse response) {
        String view = "NotificationDetail";
        String userName = request.getRemoteUser();
        String user = null;
        if (GlobalVariables.getUserSession() != null) {
            user = GlobalVariables.getUserSession().getPrincipalId();
        }
        String messageDeliveryId = request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.MSG_DELIVERY_ID);
        String command = request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.COMMAND);
        String standaloneWindow = request.getParameter(NotificationConstants.NOTIFICATION_CONTROLLER_CONSTANTS.STANDALONE_WINDOW);
        if (messageDeliveryId == null) {
            throw new RuntimeException("A null messageDeliveryId was provided.");
        }

        LOG.debug("messageDeliveryId: " + messageDeliveryId);
        LOG.debug("command: " + command);

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

        notificationService.dismissNotificationMessageDelivery(delivery.getId(), user, action);


        List<NotificationSenderBo> senders = notification.getSenders();
        List<NotificationRecipientBo> recipients = notification.getRecipients();

        String contenthtml = Util.transformContent(notification);

        // first check to see if this is a standalone window, b/c if it is, we'll want to close
        if (standaloneWindow != null && standaloneWindow.equals("true")) {
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
