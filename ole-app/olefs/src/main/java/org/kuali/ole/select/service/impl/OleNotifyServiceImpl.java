/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.service.impl;

import org.kuali.ole.select.OleSelectNotificationConstant;
import org.kuali.ole.select.service.OleGenericService;
import org.kuali.ole.select.service.OleNotifyService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.impl.persistence.dao.GenericDaoOjb;
import org.kuali.rice.ken.bo.*;
import org.kuali.rice.ken.document.kew.NotificationWorkflowDocument;
import org.kuali.rice.ken.service.NotificationChannelService;
import org.kuali.rice.ken.service.impl.NotificationContentTypeServiceImpl;
import org.kuali.rice.ken.service.impl.NotificationMessageContentServiceImpl;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.ken.util.Util;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.document.DocumentContent;
import org.kuali.rice.kew.api.document.DocumentContent.Builder;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.identity.PrincipalId;
import org.kuali.rice.kew.identity.service.IdentityHelperService;
import org.kuali.rice.kew.rule.GenericAttributeContent;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.lang.reflect.Proxy;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OleNotifyServiceImpl implements OleNotifyService {

    private OleInvocationHandler oleInvocationHandler;

    //private GenericDao genericDaoOjb;


    /**
     * Gets the genericDaoOjb attribute. 
     * @return Returns the genericDaoOjb.
     */
    /*public GenericDao getGenericDaoOjb() {
        return genericDaoOjb;
    }*/


    /**
     * Sets the genericDaoOjb attribute value.
     * @param genericDaoOjb The genericDaoOjb to set.
     */
    /*public void setGenericDaoOjb(GenericDao genericDaoOjb) {
        this.genericDaoOjb = genericDaoOjb;
    }*/


    /**
     * Gets the oleInvocationHandler attribute.
     *
     * @return Returns the oleInvocationHandler.
     */
    public OleInvocationHandler getOleInvocationHandler() {
        return oleInvocationHandler;
    }


    /**
     * Sets the oleInvocationHandler attribute value.
     *
     * @param oleInvocationHandler The oleInvocationHandler to set.
     */
    public void setOleInvocationHandler(OleInvocationHandler oleInvocationHandler) {
        this.oleInvocationHandler = oleInvocationHandler;
    }


    /**
     * @see org.kuali.ole.select.service.OleNotifyService#notify(java.util.List, java.lang.String)
     */
    public void notify(List<String> userRecipients, String message) throws WorkflowException {
        String currentUser = null;
        GenericDaoOjb genericDaoOjb = new GenericDaoOjb();
        if (GlobalVariables.getUserSession().getPrincipalName() != null) {
            Person principalPerson1 = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
            currentUser = principalPerson1.getPrincipalId();

        }
        PrincipalId initiator = new PrincipalId(currentUser);
        WorkflowDocument docs = NotificationWorkflowDocument.createNotificationDocument(initiator.toString(), NotificationConstants.KEW_CONSTANTS.SEND_NOTIFICATION_REQ_DOC_TYPE);
        // // new NotificationWorkflowDocument();
        ///  (initiator,NotificationConstants.KEW_CONSTANTS.SEND_NOTIFICATION_REQ_DOC_TYPE);
        NotificationBo notification = populateNotificationInstance(currentUser, userRecipients, message);
        NotificationContentTypeServiceImpl content = new NotificationContentTypeServiceImpl(genericDaoOjb);
        NotificationMessageContentServiceImpl messageContentService = new NotificationMessageContentServiceImpl(genericDaoOjb, content);
        String notificationAsXml = messageContentService.generateNotificationMessage(notification);
        Map<String, String> attrFields = new HashMap<String, String>();
        List<NotificationChannelReviewerBo> reviewers = notification.getChannel().getReviewers();
        int ui = 0;
        int gi = 0;
        for (NotificationChannelReviewerBo reviewer : reviewers) {
            String prefix;
            int index;
            if (MemberType.PRINCIPAL.equals(reviewer.getReviewerType())) {
                prefix = "user";
                index = ui;
                ui++;
            } else if (MemberType.GROUP.equals(reviewer.getReviewerType())) {
                prefix = "group";
                index = gi;
                gi++;
            } else {
                continue;
            }
            attrFields.put(prefix + index, reviewer.getReviewerId());
        }
        GenericAttributeContent gac = new GenericAttributeContent("channelReviewers");
        DocumentContent documentContent = docs.getDocumentContent();
        DocumentContent.Builder builder = Builder.create(docs.getDocumentId());
        builder.setApplicationContent(notificationAsXml);
        builder.setAttributeContent("<attributeContent>" + gac.generateContent(attrFields) + "</attributeContent>");
        docs.setTitle(notification.getTitle());
        docs.route("This message was submitted via the simple notification message submission form by user "
                + initiator.getPrincipalId());
    }


    private NotificationBo populateNotificationInstance(String currentUser, List<String> userRecipients, String message) {
        OleGenericService generic = (OleGenericService) Proxy.newProxyInstance(OleGenericService.class.getClassLoader(),
                new Class[]{OleGenericService.class},
                oleInvocationHandler);
        NotificationBo notification = new NotificationBo();
        String channelName = OleSelectNotificationConstant.CHANNEL_NAME;
        String priorityName = OleSelectNotificationConstant.PRIORITY_NAME;
        String senderNames = currentUser;
        String deliveryType = NotificationConstants.DELIVERY_TYPES.FYI;
        String sendDateTime = Util.getCurrentDateTime();
        String title = OleSelectNotificationConstant.NOTIFICATION_TITLE;
        NotificationChannelService notify = (NotificationChannelService) SpringContext.getBean(NotificationChannelService.class);
        NotificationChannelBo channel = notify.getNotificationChannel(new String(OleSelectNotificationConstant.NOTIFICATION_CHANNEL_ID));
        NotificationPriorityBo priority = (NotificationPriorityBo) generic.getObject(OleSelectNotificationConstant.NOTIFICATION_NAME, priorityName, NotificationPriorityBo.class);
        notification.setPriority(priority);
        NotificationContentTypeBo contentType = (NotificationContentTypeBo) generic.getObject(OleSelectNotificationConstant.NOTIFICATION_NAME, NotificationConstants.CONTENT_TYPES.SIMPLE_CONTENT_TYPE, NotificationContentTypeBo.class);
        notification.setContentType(contentType);
        Map producerMap = new HashMap();
        producerMap.put(OleSelectNotificationConstant.NOTIFICATION_NAME, OleSelectNotificationConstant.NOTIFICATION_SYSTEM);
        NotificationProducerBo producer = (NotificationProducerBo) generic.getObject(OleSelectNotificationConstant.NOTIFICATION_NAME, OleSelectNotificationConstant.NOTIFICATION_SYSTEM, NotificationProducerBo.class);
        notification.setProducer(producer);
        NotificationSenderBo ns = new NotificationSenderBo();
        ns.setSenderName(senderNames.trim());
        notification.addSender(ns);
        notification.setChannel(channel);
        for (String userRecipient : userRecipients) {
            NotificationRecipientBo recipient = new NotificationRecipientBo();
            recipient.setRecipientType(MemberType.PRINCIPAL.getCode());
            String userRecipientId = SpringContext.getBean(IdentityHelperService.class).getPrincipalByPrincipalName(userRecipient).getPrincipalId();
            StringBuffer buffer = new StringBuffer();
            recipient.setRecipientId(userRecipient);
            notification.addRecipient(recipient);
        }
        notification.setTitle(title);
        notification.setDeliveryType(deliveryType);
        try {
            notification.setSendDateTimeValue(new Timestamp(Util.parseUIDateTime(sendDateTime).getTime()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        notification
                .setContent(NotificationConstants.XML_MESSAGE_CONSTANTS.CONTENT_SIMPLE_OPEN
                        + NotificationConstants.XML_MESSAGE_CONSTANTS.MESSAGE_OPEN
                        + message
                        + NotificationConstants.XML_MESSAGE_CONSTANTS.MESSAGE_CLOSE
                        + NotificationConstants.XML_MESSAGE_CONSTANTS.CONTENT_CLOSE);

        return notification;
    }


}
