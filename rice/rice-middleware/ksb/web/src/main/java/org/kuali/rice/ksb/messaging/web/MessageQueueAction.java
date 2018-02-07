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
package org.kuali.rice.ksb.messaging.web;

import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.core.api.config.CoreConfigHelper;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.core.api.util.io.SerializationUtils;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.messaging.AsynchronousCall;
import org.kuali.rice.ksb.api.registry.ServiceInfo;
import org.kuali.rice.ksb.messaging.MessageFetcher;
import org.kuali.rice.ksb.messaging.MessageServiceInvoker;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.messaging.service.MessageQueueService;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Struts action for interacting with the queue of messages.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MessageQueueAction extends KSBAction {

    @Override
	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	return mapping.findForward("report");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	MessageQueueForm routeQueueForm = (MessageQueueForm) form;
	save(routeQueueForm);

	routeQueueForm.getMessageQueueFromForm().getRouteQueueId();
	ActionMessages messages = new ActionMessages();
	messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("routequeue.RouteQueueService.saved"));
	saveMessages(request, messages);

//	routeQueueForm.setMessageId(null);
////	routeQueueForm.setMessageQueueFromDatabase(null);
////	routeQueueForm.setMessageQueueFromForm(null);
//	routeQueueForm.setShowEdit("yes");
//	routeQueueForm.setMethodToCall("");
//	establishRequiredState(request, form);
//	routeQueueForm.setMessageId(routeQueueId);
////	routeQueueForm.setMessageQueueFromForm(routeQueueForm.getMessageQueueFromDatabase());
//	routeQueueForm.setNewQueueDate(routeQueueForm.getExistingQueueDate());
//	routeQueueForm.getMessageQueueFromForm().setMethodCall(unwrapPayload(routeQueueForm.getMessageQueueFromForm()));
	return mapping.findForward("report");
    }

    public ActionForward saveAndResubmit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	MessageQueueForm routeQueueForm = (MessageQueueForm) form;
	PersistedMessageBO message = save(routeQueueForm);
	KSBServiceLocator.getThreadPool().execute(new MessageServiceInvoker(message));

	ActionMessages messages = new ActionMessages();
	messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("routequeue.RouteQueueService.queued"));
	saveMessages(request, messages);

	routeQueueForm.setMessageId(null);
	routeQueueForm.setMessageQueueFromDatabase(null);
	routeQueueForm.setMessageQueueFromForm(null);
	routeQueueForm.setShowEdit("yes");
	routeQueueForm.setMethodToCall("");
	establishRequiredState(request, form);
	routeQueueForm.setMessageId(message.getRouteQueueId());
	routeQueueForm.setMessageQueueFromForm(message);
	routeQueueForm.setNewQueueDate(routeQueueForm.getExistingQueueDate());
	routeQueueForm.getMessageQueueFromForm().setMethodCall(unwrapPayload(message));
	return mapping.findForward("report");
    }

//    public ActionForward saveAndForward(ActionMapping mapping, ActionForm form, HttpServletRequest request,
//	    HttpServletResponse response) throws Exception {
//	MessageQueueForm routeQueueForm = (MessageQueueForm) form;
//	PersistedMessageBO message = save(routeQueueForm);
//	ForwardedCallHandler adminService = getAdminServiceToForwardTo(message, routeQueueForm);
//	AsynchronousCall methodCall = message.getPayload().getMethodCall();
//	message.setMethodCall(methodCall);
//	adminService.handleCall(message);
//	KSBServiceLocator.getRouteQueueService().delete(message);
//
//	ActionMessages messages = new ActionMessages();
//	messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("routequeue.RouteQueueService.queued"));
//	saveMessages(request, messages);
//
//	routeQueueForm.setMessageId(null);
//	routeQueueForm.setMessageQueueFromDatabase(null);
//	routeQueueForm.setMessageQueueFromForm(null);
//	routeQueueForm.setShowEdit("yes");
//	routeQueueForm.setMethodToCall("");
//	establishRequiredState(request, form);
//	routeQueueForm.setMessageId(message.getRouteQueueId());
//	routeQueueForm.setMessageQueueFromForm(message);
//	routeQueueForm.setNewQueueDate(routeQueueForm.getExistingQueueDate());
//	routeQueueForm.getMessageQueueFromForm().setMethodCall(unwrapPayload(message));
//	return mapping.findForward("report");
//    }

    private PersistedMessageBO save(MessageQueueForm routeQueueForm) {
	Long routeQueueId = routeQueueForm.getMessageQueueFromForm().getRouteQueueId();
	if ((routeQueueId == null) || (routeQueueId.longValue() <= 0)) {
	    throw new IllegalArgumentException("Invalid routeQueueId passed in.  Cannot save");
	}
	// save the message
	PersistedMessageBO existingMessage = KSBServiceLocator.getMessageQueueService().findByRouteQueueId(routeQueueId);
	PersistedMessageBO message = routeQueueForm.getMessageQueueFromForm();
	// copy the new values over
	if (existingMessage == null) {
	    throw new RuntimeException("Could locate the existing message, it may have already been processed.");
	}

	existingMessage.setQueuePriority(message.getQueuePriority());
	existingMessage.setIpNumber(message.getIpNumber());
	existingMessage.setLockVerNbr(message.getLockVerNbr());
	existingMessage.setApplicationId(message.getApplicationId());
	existingMessage.setMethodName(message.getMethodName());
	existingMessage.setQueueStatus(message.getQueueStatus());
	existingMessage.setRetryCount(message.getRetryCount());
	existingMessage.setServiceName(message.getServiceName());
	existingMessage.setValue1(message.getValue1());
	existingMessage.setValue2(message.getValue2());
	KSBServiceLocator.getMessageQueueService().save(existingMessage);
	return existingMessage;
    }

    /**
         * Performs a quick ReQueue of the indicated persisted message.
         *
         * The net effect of this requeue is to set the Date to now, and to reset the RetryCount to zero. The payload is not
         * modified.
         *
         * @param message
         *                The populated message to be requeued.
         */
    protected void quickRequeueMessage(PersistedMessageBO message) {
	message.setQueueStatus(KSBConstants.ROUTE_QUEUE_ROUTING);
	message.setQueueDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
	message.setRetryCount(new Integer(0));
	getRouteQueueService().save(message);
    }

    public ActionForward quickRequeueMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	MessageQueueForm routeQueueForm = (MessageQueueForm) form;
	if (routeQueueForm.getMessageQueueFromDatabase() == null) {
	    throw new IllegalArgumentException("No messageId passed in with the Request.");
	}

	PersistedMessageBO message = routeQueueForm.getMessageQueueFromDatabase();
	quickRequeueMessage(message);
	KSBServiceLocator.getThreadPool().execute(new MessageServiceInvoker(message));

	ActionMessages messages = new ActionMessages();
	messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("routequeue.RouteQueueService.requeued"));
	saveMessages(request, messages);

	routeQueueForm.setMessageQueueFromDatabase(null);
	routeQueueForm.setMessageQueueFromForm(null);
	routeQueueForm.setMessageId(null);
	routeQueueForm.setMethodToCall("");

	// re-run the state method to load the full set of rows
	establishRequiredState(request, form);
	return mapping.findForward("report");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	MessageQueueForm routeQueueForm = (MessageQueueForm) form;
	routeQueueForm.setShowEdit("yes");
	routeQueueForm.setMessageQueueFromForm(routeQueueForm.getMessageQueueFromDatabase());
	routeQueueForm.setNewQueueDate(routeQueueForm.getExistingQueueDate());
	routeQueueForm.getMessageQueueFromForm().setMethodCall(unwrapPayload(routeQueueForm.getMessageQueueFromForm()));
	return mapping.findForward("basic");
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	MessageQueueForm routeQueueForm = (MessageQueueForm) form;
	routeQueueForm.setShowEdit("no");
	routeQueueForm.setMessageQueueFromForm(routeQueueForm.getMessageQueueFromDatabase());
	routeQueueForm.setNewQueueDate(routeQueueForm.getExistingQueueDate());
	AsynchronousCall messagePayload = unwrapPayload(routeQueueForm.getMessageQueueFromDatabase());
	routeQueueForm.getMessageQueueFromForm().setMethodCall(messagePayload);
	return mapping.findForward("payload");
    }

    public ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	MessageQueueForm routeQueueForm = (MessageQueueForm) form;
	if (routeQueueForm.getShowEdit().equals("yes")) {
	    routeQueueForm.setMessageQueueFromForm(routeQueueForm.getMessageQueueFromDatabase());
	}
	return mapping.findForward("basic");
    }

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	MessageQueueForm routeQueueForm = (MessageQueueForm) form;
	routeQueueForm.getMessageQueueFromForm().setQueuePriority(null);
	routeQueueForm.getMessageQueueFromForm().setQueueStatus(null);
	routeQueueForm.getMessageQueueFromForm().setQueueDate(null);
	routeQueueForm.getMessageQueueFromForm().setExpirationDate(null);
	routeQueueForm.getMessageQueueFromForm().setRetryCount(null);
	routeQueueForm.getMessageQueueFromForm().setIpNumber(null);
	routeQueueForm.getMessageQueueFromForm().setServiceName(null);
	routeQueueForm.getMessageQueueFromForm().setApplicationId(null);
	routeQueueForm.getMessageQueueFromForm().setMethodName(null);
	routeQueueForm.getMessageQueueFromForm().setPayload(null);
	routeQueueForm.getMessageQueueFromForm().setMethodCall(null);
	routeQueueForm.setExistingQueueDate(null);
	routeQueueForm.setNewQueueDate(null);
	return mapping.findForward("basic");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	MessageQueueForm routeQueueForm = (MessageQueueForm) form;
	routeQueueForm.setMessageQueueFromForm(routeQueueForm.getMessageQueueFromDatabase());
	routeQueueForm.setMessageQueueFromDatabase(null);
	getRouteQueueService().delete(routeQueueForm.getMessageQueueFromForm());
	ActionMessages messages = new ActionMessages();
	messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("routequeue.RouteQueueService.deleted", routeQueueForm
		.getMessageQueueFromForm().getRouteQueueId().toString()));
	saveMessages(request, messages);
	routeQueueForm.setMessageId(null);
	establishRequiredState(request, form);
	return mapping.findForward("report");
    }

    public ActionForward executeMessageFetcher(ActionMapping mapping,  ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	MessageQueueForm routeQueueForm = (MessageQueueForm) form;
	ActionMessages messages = new ActionMessages();
	if (routeQueueForm.getMaxMessageFetcherMessages() == null || routeQueueForm.getMaxMessageFetcherMessages() <= 0) {
	    messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("routequeue.RouteQueueService.invalidMessages", routeQueueForm.getMaxMessageFetcherMessages()));
	}
	if (!messages.isEmpty()) {
	    saveMessages(request, messages);
	    return mapping.findForward("report");
	}
	new MessageFetcher(routeQueueForm.getMaxMessageFetcherMessages()).run();
	return mapping.findForward("report");
    }

    /**
         * Sets up the expected state by retrieving the selected RouteQueue by RouteQueueId, and placing it in the
         * ExistingRouteQueue member.
         *
         * Called by the super's Execute method on every request.
         */
    @Override
	public ActionMessages establishRequiredState(HttpServletRequest request, ActionForm form) throws Exception {
	request.setAttribute("rice_constant", getServlet().getServletContext().getAttribute("RiceConstants"));
	request.setAttribute("ksb_constant", getServlet().getServletContext().getAttribute("KSBConstants"));
	MessageQueueForm routeQueueForm = (MessageQueueForm) form;
	routeQueueForm.setMyIpAddress(RiceUtilities.getIpNumber());
	routeQueueForm.setMyApplicationId(CoreConfigHelper.getApplicationId());
	routeQueueForm.setMessagePersistence(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.MESSAGE_PERSISTENCE));
	routeQueueForm.setMessageDelivery(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.MESSAGE_DELIVERY));
	routeQueueForm.setMessageOff(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.MESSAGING_OFF));
	List<ServiceInfo> services = KsbApiServiceLocator.getServiceRegistry().getAllOnlineServices();
	if (routeQueueForm.getMessageId() != null) {
	    PersistedMessageBO rq = getRouteQueueService().findByRouteQueueId(routeQueueForm.getMessageId());
	    if (rq != null) {
		routeQueueForm.setExistingQueueDate(RiceConstants.getDefaultDateFormat().format(new Date()));
		routeQueueForm.setMessageQueueFromDatabase(rq);
		// establish IP addresses where this message could safely be forwarded to
		String serviceName = rq.getServiceName();
		for (ServiceInfo serviceInfo : services) {
		    if (serviceInfo.getServiceName().equals(serviceName)) {
			routeQueueForm.getIpAddresses().add(
				new ConcreteKeyValue(serviceInfo.getServerIpAddress(), serviceInfo.getServerIpAddress()));
		    }
		}
	    } else {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			"messagequeue.RouteQueueService.queuedDocumentNotFound", routeQueueForm.getMessageId().toString()));
		return messages;
	    }
	    routeQueueForm.setMessageId(null);
	} else if (!"clear".equalsIgnoreCase(request.getParameter("methodToCall"))) {
	    List<PersistedMessageBO> queueEntries = findRouteQueues(request, routeQueueForm, routeQueueForm.getMaxRows() + 1);
	    if (queueEntries.size() > 0) {
		Collections.sort(queueEntries, new Comparator() {
		    private Comparator comp = new ComparableComparator();

		    @Override
			public int compare(Object object1, Object object2) {
			if (object1 == null && object2 == null) {
			    return 0;
			} else if (object1 == null) {
			    return 1;
			} else if (object2 == null) {
			    return -1;
			}
			Long id1 = ((PersistedMessageBO) object1).getRouteQueueId();
			Long id2 = ((PersistedMessageBO) object2).getRouteQueueId();

			try {
			    return this.comp.compare(id1, id2);
			} catch (Exception e) {
			    return 0;
			}
		    }
		});
	    }
	    routeQueueForm.setMessageQueueRows(queueEntries);
	}
	return null;
    }

    protected List<PersistedMessageBO> findRouteQueues(HttpServletRequest request, MessageQueueForm routeQueueForm, int maxRows) {
	List<PersistedMessageBO> routeQueues = new ArrayList<PersistedMessageBO>();

	// no filter applied
	if (StringUtils.isBlank(routeQueueForm.getFilterApplied())) {
	    routeQueues.addAll(getRouteQueueService().findAll(maxRows));
	}

	// one or more filters applied
	else {
	    if (!StringUtils.isBlank(routeQueueForm.getRouteQueueIdFilter())) {
		if (!NumberUtils.isNumber(routeQueueForm.getRouteQueueIdFilter())) {
		    // TODO better error handling here
		    throw new RuntimeException("Message Id must be a number.");
		}
	    }

	    Map<String, String> criteriaValues = new HashMap<String, String>();
	    String key = null;
	    String value = null;
	    String trimmedKey = null;
	    for (Iterator iter = request.getParameterMap().keySet().iterator(); iter.hasNext();) {
		key = (String) iter.next();
		if (key.endsWith(KSBConstants.ROUTE_QUEUE_FILTER_SUFFIX)) {
		    value = request.getParameter(key);
		    if (StringUtils.isNotBlank(value)) {
			trimmedKey = key.substring(0, key.indexOf(KSBConstants.ROUTE_QUEUE_FILTER_SUFFIX));
			criteriaValues.put(trimmedKey, value);
		    }
		}
	    }
	    routeQueues.addAll(getRouteQueueService().findByValues(criteriaValues, maxRows));
	}
	return routeQueues;
    }

    private MessageQueueService getRouteQueueService() {
	return KSBServiceLocator.getMessageQueueService();
    }

    /**
         * Extracts the payload from a PersistedMessageBO, attempts to convert it to the expected AsynchronousCall type, and
         * returns it.
         *
         * Throws an IllegalArgumentException if the decoded payload isnt of the expected type.
         *
         * @param message
         *                The populated PersistedMessageBO object to extract the payload from.
         * @return Returns the payload if one is present and it can be deserialized, otherwise returns null.
         */
    protected AsynchronousCall unwrapPayload(PersistedMessageBO message) {
	if (message == null || message.getPayload() == null) {
	    return null;
	}
	String encodedPayload = message.getPayload().getPayload();
	if (StringUtils.isBlank(encodedPayload)) {
	    return null;
	}
	Object decodedPayload = null;
	if (encodedPayload != null) {
	    decodedPayload = SerializationUtils.deserializeFromBase64(encodedPayload);
	}
	// fail fast if its not the expected type of AsynchronousCall
	if ((decodedPayload != null) && !(decodedPayload instanceof AsynchronousCall)) {
	    throw new IllegalArgumentException("PersistedMessageBO payload was not of the expected class. " + "Expected was ["
		    + AsynchronousCall.class.getName() + "], actual was: [" + decodedPayload.getClass().getName() + "]");
	}
	return (AsynchronousCall) decodedPayload;
    }

}
