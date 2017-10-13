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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Struts action for interacting with the queue of messages.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ThreadPoolAction extends KSBAction {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ThreadPoolAction.class);

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	return mapping.findForward("basic");
    }

    public ActionMessages establishRequiredState(HttpServletRequest request, ActionForm actionForm) throws Exception {
	ThreadPoolForm form = (ThreadPoolForm)actionForm;
	form.setThreadPool(KSBServiceLocator.getThreadPool());
	if (form.getCorePoolSize() == null) {
	    form.setCorePoolSize(form.getThreadPool().getCorePoolSize());
	}
	if (form.getMaximumPoolSize() == null) {
	    form.setMaximumPoolSize(form.getThreadPool().getMaximumPoolSize());
	}
	if (form.getTimeIncrement() == null) {
	    String timeIncrementValue = ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.ROUTE_QUEUE_TIME_INCREMENT_KEY);
	    if (!StringUtils.isEmpty(timeIncrementValue)) {
		form.setTimeIncrement(Long.parseLong(timeIncrementValue));
	    }
	}
	if (form.getMaxRetryAttempts() == null) {
	    String maxRetryAttemptsValue = ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.ROUTE_QUEUE_MAX_RETRY_ATTEMPTS_KEY);
	    if (!StringUtils.isEmpty(maxRetryAttemptsValue)) {
		form.setMaxRetryAttempts(Long.parseLong(maxRetryAttemptsValue));
	    }
	}
	return null;
    }

}
