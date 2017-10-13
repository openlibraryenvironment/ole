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
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationChannelBo;
import org.kuali.rice.ken.bo.NotificationChannelReviewerBo;
import org.kuali.rice.ken.bo.NotificationContentTypeBo;
import org.kuali.rice.ken.bo.NotificationPriorityBo;
import org.kuali.rice.ken.bo.NotificationProducerBo;
import org.kuali.rice.ken.bo.NotificationRecipientBo;
import org.kuali.rice.ken.bo.NotificationSenderBo;
import org.kuali.rice.ken.document.kew.NotificationWorkflowDocument;
import org.kuali.rice.ken.exception.ErrorList;
import org.kuali.rice.ken.service.NotificationChannelService;
import org.kuali.rice.ken.service.NotificationMessageContentService;
import org.kuali.rice.ken.service.NotificationRecipientService;
import org.kuali.rice.ken.service.NotificationService;
import org.kuali.rice.ken.service.NotificationWorkflowDocumentService;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.ken.util.Util;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.rule.GenericAttributeContent;
import org.kuali.rice.kim.api.KimConstants.KimGroupMemberTypes;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class is the controller for sending Event notification messages via an end user interface.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SendEventNotificationMessageController extends BaseSendNotificationController {
    /** Logger for this class and subclasses */
    private static final Logger LOG = Logger
	    .getLogger(SendEventNotificationMessageController.class);

    private static final String NONE_CHANNEL = "___NONE___";
    private static final long REASONABLE_IMMEDIATE_TIME_THRESHOLD = 1000 * 60 * 5; // <= 5 minutes is "immediate"

    /**
     * Returns whether the specified time is considered "in the future", based on some reasonable threshold
     * @param time the time to test
     * @return whether the specified time is considered "in the future", based on some reasonable threshold
     */
    private boolean timeIsInTheFuture(long time) {
        boolean future = (time - System.currentTimeMillis()) > REASONABLE_IMMEDIATE_TIME_THRESHOLD;
        LOG.info("Time: " + new Date(time) + " is in the future? " + future);
        return future;
    }

    /**
     * Returns whether the specified Notification can be reasonably expected to have recipients.
     * This is determined on whether the channel has default recipients, is subscribably, and whether
     * the send date time is far enough in the future to expect that if there are no subscribers, there
     * may actually be some by the time the notification is sent.
     * @param notification the notification to test
     * @return whether the specified Notification can be reasonably expected to have recipients
     */
    private boolean hasPotentialRecipients(NotificationBo notification) {
        LOG.info("notification channel " + notification.getChannel() + " is subscribable: " + notification.getChannel().isSubscribable());
        return notification.getChannel().getRecipientLists().size() > 0 ||
               notification.getChannel().getSubscriptions().size() > 0 ||
               (notification.getChannel().isSubscribable() && timeIsInTheFuture(notification.getSendDateTimeValue().getTime()));
    }

    protected NotificationService notificationService;

    protected NotificationWorkflowDocumentService notificationWorkflowDocService;

    protected NotificationChannelService notificationChannelService;

    protected NotificationRecipientService notificationRecipientService;

    protected NotificationMessageContentService messageContentService;

    protected GenericDao businessObjectDao;

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
    public void setNotificationWorkflowDocumentService(
	    NotificationWorkflowDocumentService s) {
	this.notificationWorkflowDocService = s;
    }

    /**
     * Sets the notificationChannelService attribute value.
     * @param notificationChannelService The notificationChannelService to set.
     */
    public void setNotificationChannelService(
	    NotificationChannelService notificationChannelService) {
	this.notificationChannelService = notificationChannelService;
    }

    /**
     * Sets the notificationRecipientService attribute value.
     * @param notificationRecipientService
     */
    public void setNotificationRecipientService(
	    NotificationRecipientService notificationRecipientService) {
	this.notificationRecipientService = notificationRecipientService;
    }

    /**
     * Sets the messageContentService attribute value.
     * @param messageContentService
     */
    public void setMessageContentService(
	    NotificationMessageContentService notificationMessageContentService) {
	this.messageContentService = notificationMessageContentService;
    }

    /**
     * Sets the businessObjectDao attribute value.
     * @param businessObjectDao The businessObjectDao to set.
     */
    public void setBusinessObjectDao(GenericDao businessObjectDao) {
	this.businessObjectDao = businessObjectDao;
    }

    /**
     * Handles the display of the form for sending an event notification message
     * @param request : a servlet request
     * @param response : a servlet response
     * @throws ServletException : an exception
     * @throws IOException : an exception
     * @return a ModelAndView object
     */
    public ModelAndView sendEventNotificationMessage(
	    HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	String view = "SendEventNotificationMessage";
	LOG.debug("remoteUser: " + request.getRemoteUser());

	Map<String, Object> model = setupModelForSendEventNotification(request);
	model.put("errors", new ErrorList()); // need an empty one so we don't have an NPE

	return new ModelAndView(view, model);
    }

    /**
     * This method prepares the model used for the send event notification message form.
     * @param request
     * @return Map<String, Object>
     */
    private Map<String, Object> setupModelForSendEventNotification(
	    HttpServletRequest request) {
	Map<String, Object> model = new HashMap<String, Object>();
	model.put("defaultSender", request.getRemoteUser());
	model.put("channels", notificationChannelService
		.getAllNotificationChannels());
	model.put("priorities", businessObjectDao
		.findAll(NotificationPriorityBo.class));
        // set sendDateTime to current datetime if not provided
	String sendDateTime = request.getParameter("sendDateTime");
	String currentDateTime = Util.getCurrentDateTime();
	if (StringUtils.isEmpty(sendDateTime)) {
	    sendDateTime = currentDateTime;
	}
	model.put("sendDateTime", sendDateTime);

	// retain the original date time or set to current if
	// it was not in the request
	if (request.getParameter("originalDateTime") == null) {
	   model.put("originalDateTime", currentDateTime);
	} else {
	   model.put("originalDateTime", request.getParameter("originalDateTime"));
	}
        model.put("summary", request.getParameter("summary"));
        model.put("description", request.getParameter("description"));
        model.put("location", request.getParameter("location"));
        model.put("startDateTime", request.getParameter("startDateTime"));
        model.put("stopDateTime", request.getParameter("stopDateTime"));

        model.put("userRecipients", request.getParameter("userRecipients"));
        model.put("workgroupRecipients", request.getParameter("workgroupRecipients"));
        model.put("workgroupNamespaceCodes", request.getParameter("workgroupNamespaceCodes"));

	return model;
    }

    /**
     * This method handles submitting the actual event notification message.
     * @param request
     * @param response
     * @return ModelAndView
     * @throws ServletException
     * @throws IOException
     */
    public ModelAndView submitEventNotificationMessage(
	    HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	LOG.debug("remoteUser: " + request.getRemoteUser());

	// obtain a workflow user object first
	//WorkflowIdDTO initiator = new WorkflowIdDTO(request.getRemoteUser());
    String initiatorId = getPrincipalIdFromIdOrName( request.getRemoteUser());
    LOG.debug("initiatorId="+initiatorId);

	// now construct the workflow document, which will interact with workflow
	WorkflowDocument document;
	Map<String, Object> model = new HashMap<String, Object>();
        String view;
	try {
	    document = NotificationWorkflowDocument.createNotificationDocument(
	    		initiatorId,
			    NotificationConstants.KEW_CONSTANTS.SEND_NOTIFICATION_REQ_DOC_TYPE);

	    //parse out the application content into a Notification BO
	    NotificationBo notification = populateNotificationInstance(request, model);

	    // now get that content in an understandable XML format and pass into document
	    String notificationAsXml = messageContentService
		    .generateNotificationMessage(notification);

            Map<String, String> attrFields = new HashMap<String,String>();
            List<NotificationChannelReviewerBo> reviewers = notification.getChannel().getReviewers();
            int ui = 0;
            int gi = 0;
            for (NotificationChannelReviewerBo reviewer: reviewers) {
                String prefix;
                int index;
                if (KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.equals(reviewer.getReviewerType())) {
                    prefix = "user";
                    index = ui;
                    ui++;
                } else if (KimGroupMemberTypes.GROUP_MEMBER_TYPE.equals(reviewer.getReviewerType())) {
                    prefix = "group";
                    index = gi;
                    gi++;
                } else {
                    LOG.error("Invalid type for reviewer " + reviewer.getReviewerId() + ": " + reviewer.getReviewerType());
                    continue;
                }
                attrFields.put(prefix + index, reviewer.getReviewerId());
            }
            GenericAttributeContent gac = new GenericAttributeContent("channelReviewers");
            document.setApplicationContent(notificationAsXml);
            document.setAttributeContent("<attributeContent>" + gac.generateContent(attrFields) + "</attributeContent>");

            document.setTitle(notification.getTitle());

	    document.route("This message was submitted via the event notification message submission form by user "
			    + initiatorId);

	    view = "SendEventNotificationMessage";
	    
	    // This ain't pretty, but it gets the job done for now.
	    ErrorList el = new ErrorList();
	    el.addError("Notification(s) sent.");
	    model.put("errors", el);
	    
	} catch (ErrorList el) {
	    // route back to the send form again
	    Map<String, Object> model2 = setupModelForSendEventNotification(request);
	    model.putAll(model2);
	    model.put("errors", el);

	    view = "SendEventNotificationMessage";
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}

	return new ModelAndView(view, model);
    }

    /**
     * This method creates a new Notification instance from the event form values.
     * @param request
     * @param model
     * @return Notification
     * @throws IllegalArgumentException
     */
    private NotificationBo populateNotificationInstance(
	    HttpServletRequest request, Map<String, Object> model)
	    throws IllegalArgumentException, ErrorList {
	ErrorList errors = new ErrorList();

	NotificationBo notification = new NotificationBo();

	// grab data from form
	// channel name
	String channelName = request.getParameter("channelName");
        if (StringUtils.isEmpty(channelName) || StringUtils.equals(channelName, NONE_CHANNEL)) {
	    errors.addError("You must choose a channel.");
	} else {
	    model.put("channelName", channelName);
	}

	// priority name
	String priorityName = request.getParameter("priorityName");
	if (StringUtils.isEmpty(priorityName)) {
	    errors.addError("You must choose a priority.");
	} else {
	    model.put("priorityName", priorityName);
	}

	// sender names
	String senderNames = request.getParameter("senderNames");
	String[] senders = null;
	if (StringUtils.isEmpty(senderNames)) {
	    errors.addError("You must enter at least one sender.");
	} else {
	    senders = StringUtils.split(senderNames, ",");

	    model.put("senderNames", senderNames);
	}

	// delivery type
	String deliveryType = request.getParameter("deliveryType");
	if (StringUtils.isEmpty(deliveryType)) {
	    errors.addError("You must choose a type.");
	} else {
	    if (deliveryType
		    .equalsIgnoreCase(NotificationConstants.DELIVERY_TYPES.FYI)) {
		deliveryType = NotificationConstants.DELIVERY_TYPES.FYI;
	    } else {
		deliveryType = NotificationConstants.DELIVERY_TYPES.ACK;
	    }
	    model.put("deliveryType", deliveryType);
	}

	//get datetime when form was initially rendered
	String originalDateTime = request.getParameter("originalDateTime");
	Date origdate = null;
	Date senddate = null;
	Date removedate = null;
	try {
            origdate = Util.parseUIDateTime(originalDateTime);
        } catch (ParseException pe) {
            errors.addError("Original date is invalid.");
        }
	// send date time
	String sendDateTime = request.getParameter("sendDateTime");
	if (StringUtils.isBlank(sendDateTime)) {
	    sendDateTime = Util.getCurrentDateTime();
	}

	try {
            senddate = Util.parseUIDateTime(sendDateTime);
        } catch (ParseException pe) {
            errors.addError("You specified an invalid Send Date/Time.  Please use the calendar picker.");
        }

        if(senddate != null && senddate.before(origdate)) {
            errors.addError("Send Date/Time cannot be in the past.");
        }

        model.put("sendDateTime", sendDateTime);

	// auto remove date time
	String autoRemoveDateTime = request.getParameter("autoRemoveDateTime");
	if (StringUtils.isNotBlank(autoRemoveDateTime)) {
	    try {
                removedate = Util.parseUIDateTime(autoRemoveDateTime);
            } catch (ParseException pe) {
                errors.addError("You specified an invalid Auto-Remove Date/Time.  Please use the calendar picker.");
            }

            if(removedate != null) {
        	if(removedate.before(origdate)) {
        	    errors.addError("Auto-Remove Date/Time cannot be in the past.");
        	} else if (senddate != null && removedate.before(senddate)){
        	    errors.addError("Auto-Remove Date/Time cannot be before the Send Date/Time.");
        	}
            }
	}

	model.put("autoRemoveDateTime", autoRemoveDateTime);

	// user recipient names
	String[] userRecipients = parseUserRecipients(request);

	// workgroup recipient names
	String[] workgroupRecipients = parseWorkgroupRecipients(request);

	// workgroup namespace names
	String[] workgroupNamespaceCodes = parseWorkgroupNamespaceCodes(request);
	
	// title
        String title = request.getParameter("title");
        if (!StringUtils.isEmpty(title)) {
            model.put("title", title);
        } else {
            errors.addError("You must fill in a title");
        }

	// message
	String message = request.getParameter("message");
	if (StringUtils.isEmpty(message)) {
	    errors.addError("You must fill in a message.");
	} else {
	    model.put("message", message);
	}

        // all event fields are mandatory for event type

	// start date time
        String startDateTime = request.getParameter("startDateTime");
        if (StringUtils.isEmpty(startDateTime)) {
            errors.addError("You must fill in a start date/time.");
        } else {
            model.put("startDateTime", startDateTime);
        }

        // stop date time
        String stopDateTime = request.getParameter("stopDateTime");
        if (StringUtils.isEmpty(stopDateTime)) {
            errors.addError("You must fill in a stop date/time.");
        } else {
            model.put("stopDateTime", stopDateTime);
        }

        // summary
        String summary = request.getParameter("summary");
        if (StringUtils.isEmpty(summary)) {
            errors.addError("You must fill in a summary.");
        } else {
            model.put("summary", summary);
        }

        // description
        String description = request.getParameter("description");
        if (StringUtils.isEmpty(description)) {
            errors.addError("You must fill in a description.");
        } else {
            model.put("description", description);
        }

        // location
        String location = request.getParameter("location");
        if (StringUtils.isEmpty(location)) {
            errors.addError("You must fill in a location.");
        } else {
            model.put("location", location);
        }

	// stop processing if there are errors
	if (errors.getErrors().size() > 0) {
	    throw errors;
	}

	// now populate the notification BO instance
	NotificationChannelBo channel = Util.retrieveFieldReference("channel",
		"name", channelName, NotificationChannelBo.class,
		businessObjectDao);
	notification.setChannel(channel);

	NotificationPriorityBo priority = Util.retrieveFieldReference("priority",
		"name", priorityName, NotificationPriorityBo.class,
		businessObjectDao);
	notification.setPriority(priority);

	NotificationContentTypeBo contentType = Util.retrieveFieldReference(
		"contentType", "name",
		NotificationConstants.CONTENT_TYPES.EVENT_CONTENT_TYPE,
		NotificationContentTypeBo.class, businessObjectDao);
	notification.setContentType(contentType);

	NotificationProducerBo producer = Util
		.retrieveFieldReference(
			"producer",
			"name",
			NotificationConstants.KEW_CONSTANTS.NOTIFICATION_SYSTEM_USER_NAME,
			NotificationProducerBo.class, businessObjectDao);
	notification.setProducer(producer);

	for (String senderName : senders) {
	    if (StringUtils.isEmpty(senderName)) {
		errors.addError("A sender's name cannot be blank.");
	    } else {
		NotificationSenderBo ns = new NotificationSenderBo();
		ns.setSenderName(senderName.trim());
		notification.addSender(ns);
	    }
	}

	boolean recipientsExist = false;

	if (userRecipients != null && userRecipients.length > 0) {
	    recipientsExist = true;
	    for (String userRecipientId : userRecipients) {
	        if (isUserRecipientValid(userRecipientId, errors)) {
        		NotificationRecipientBo recipient = new NotificationRecipientBo();
        		recipient.setRecipientType(KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.getCode());
        		recipient.setRecipientId(userRecipientId);
        		notification.addRecipient(recipient);
	        }
	    }
	}

	if (workgroupRecipients != null && workgroupRecipients.length > 0) {
	    recipientsExist = true;
	    if (workgroupNamespaceCodes != null && workgroupNamespaceCodes.length > 0) {
	    	if (workgroupNamespaceCodes.length == workgroupRecipients.length) {
	    		for (int i = 0; i < workgroupRecipients.length; i++) {
	    			if (isWorkgroupRecipientValid(workgroupRecipients[i], workgroupNamespaceCodes[i], errors)) {
	    				NotificationRecipientBo recipient = new NotificationRecipientBo();
	    				recipient.setRecipientType(KimGroupMemberTypes.GROUP_MEMBER_TYPE.getCode());
	    				recipient.setRecipientId(
	    						getGroupService().getGroupByNamespaceCodeAndName(workgroupNamespaceCodes[i],
                                        workgroupRecipients[i]).getId());
	    				notification.addRecipient(recipient);
	    			}
	    		}
	    	} else {
	    		errors.addError("The number of groups must match the number of namespace codes");
	    	}
	    } else {
			errors.addError("You must specify a namespace code for every group name");
		}
	} else if (workgroupNamespaceCodes != null && workgroupNamespaceCodes.length > 0) {
		errors.addError("You must specify a group name for every namespace code");
	}

	// check to see if there were any errors
	if (errors.getErrors().size() > 0) {
	    throw errors;
	}

        notification.setTitle(title);

	notification.setDeliveryType(deliveryType);

        Date startDate = null;
        Date stopDate = null;
	// simpledateformat is not threadsafe, have to sync and validate
        Date d = null;
        if (StringUtils.isNotBlank(sendDateTime)) {
            try {
                d = Util.parseUIDateTime(sendDateTime);
            } catch (ParseException pe) {
                errors.addError("You specified an invalid send date and time.  Please use the calendar picker.");
            }
            notification.setSendDateTimeValue(new Timestamp(d.getTime()));
        }

        Date d2 = null;
        if (StringUtils.isNotBlank(autoRemoveDateTime)) {
            try {
                d2 = Util.parseUIDateTime(autoRemoveDateTime);
                if (d2.before(d)) {
                    errors.addError("Auto Remove Date/Time cannot be before Send Date/Time.");
                }
            } catch (ParseException pe) {
                errors.addError("You specified an invalid auto-remove date and time.  Please use the calendar picker.");
            }
            notification.setAutoRemoveDateTimeValue(new Timestamp(d2.getTime()));
        }

        if (StringUtils.isNotBlank(startDateTime)) {
            try {
                startDate = Util.parseUIDateTime(startDateTime);
            } catch (ParseException pe) {
                errors.addError("You specified an invalid start date and time.  Please use the calendar picker.");
            }
        }

        if (StringUtils.isNotBlank(stopDateTime)) {
            try {
                stopDate = Util.parseUIDateTime(stopDateTime);
            } catch (ParseException pe) {
                errors.addError("You specified an invalid stop date and time.  Please use the calendar picker.");
            }
        }

        if(stopDate != null && startDate != null) {
            if (stopDate.before(startDate)) {
                errors.addError("Event Stop Date/Time cannot be before Event Start Date/Time.");
            }
        }

        if (!recipientsExist && !hasPotentialRecipients(notification)) {
            errors.addError("You must specify at least one user or group recipient.");
        }

	// check to see if there were any errors
	if (errors.getErrors().size() > 0) {
	    throw errors;
	}

	notification
		.setContent(NotificationConstants.XML_MESSAGE_CONSTANTS.CONTENT_EVENT_OPEN
			+ NotificationConstants.XML_MESSAGE_CONSTANTS.MESSAGE_OPEN
			+ message
			+ NotificationConstants.XML_MESSAGE_CONSTANTS.MESSAGE_CLOSE
                        + "<event>\n"
                        + "  <summary>" + summary + "</summary>\n"
                        + "  <description>" + description + "</description>\n"
                        + "  <location>" + location + "</location>\n"
                        + "  <startDateTime>" + Util.toUIDateTimeString(startDate) + "</startDateTime>\n"
                        + "  <stopDateTime>" + Util.toUIDateTimeString(stopDate) + "</stopDateTime>\n"
                        + "</event>"
			+ NotificationConstants.XML_MESSAGE_CONSTANTS.CONTENT_CLOSE);

	return notification;
    }
}
