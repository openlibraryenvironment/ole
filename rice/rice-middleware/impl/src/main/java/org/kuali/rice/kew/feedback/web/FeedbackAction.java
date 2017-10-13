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
package org.kuali.rice.kew.feedback.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailContent;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.mail.service.EmailContentService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;


/**
 * Struts action which handles the Feedback screen.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FeedbackAction extends KewKualiAction {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FeedbackAction.class);

    @Override
	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // load fixed properties in Form elements
        FeedbackForm feedbackForm = (FeedbackForm) form;
        feedbackForm.setTimeDate(new Date().toString());

        // load User properties in Form elements
        feedbackForm.setComments("");

        // load application properties from Request in Form elements
        String documentType = request.getParameter("docType");
        if (documentType == null) {
            documentType = "";
        }
        feedbackForm.setDocumentType(documentType);

        String pageUrl = request.getParameter("pageUrl");
        if (pageUrl == null) {
            pageUrl = "";
        }
        feedbackForm.setPageUrl(pageUrl);

        String documentId = request.getParameter("documentId");
        if (documentId == null) {
            documentId = "";
        }
        feedbackForm.setDocumentId(documentId);

        String exception = request.getParameter("exception");
        if (exception == null) {
            feedbackForm.setException("");
            feedbackForm.setCategory("");
        } else {
            feedbackForm.setCategory("problem");
            feedbackForm.setException(exception);
        }

        UserSession uSession = getUserSession();

        Person workflowUser = uSession.getPerson();
        if (workflowUser != null) {
            feedbackForm.setNetworkId(workflowUser.getPrincipalName());
            feedbackForm.setUserEmail(workflowUser.getEmailAddress());
            String name = workflowUser.getName().trim();
            feedbackForm.setUserName(name);
            String firstName = name.substring(0, name.indexOf(" "));
            String lastName = name.substring(name.lastIndexOf(" ") + 1, name.length());
            feedbackForm.setFirstName(firstName);
            feedbackForm.setLastName(lastName);
        } else {
            feedbackForm.setNetworkId("");
            feedbackForm.setUserEmail("");
            feedbackForm.setUserName("");
            feedbackForm.setFirstName("");
            feedbackForm.setLastName("");
        }

        return mapping.findForward("start");
    }

    public ActionForward sendFeedback(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	FeedbackForm feedbackForm = (FeedbackForm)form;    	
        EmailContentService emailContentService = KEWServiceLocator.getEmailContentService();
        String fromAddress = determineFromAddress(emailContentService, feedbackForm);
    	String toAddress = emailContentService.getApplicationEmailAddress();
        EmailContent content = emailContentService.generateFeedback(feedbackForm);
        CoreApiServiceLocator.getMailer().sendEmail(new EmailFrom(fromAddress), new EmailTo(toAddress), new EmailSubject(content.getSubject()), new EmailBody(content.getBody()), content.isHtml());
    	return mapping.findForward("sent");
    }

    private String determineFromAddress(EmailContentService emailContentService, FeedbackForm form) {
    	DocumentType docType = null;
    	if (!StringUtils.isEmpty(form.getDocumentType())) {
    		docType = KEWServiceLocator.getDocumentTypeService().findByName(form.getDocumentType());
    		if (docType == null) {
    			LOG.warn("Couldn't locate document type for the given name to determine feedback from address! " + form.getDocumentType());
    		}
    	}
    	// if we pass null to this method it will return us the application email address
    	return emailContentService.getDocumentTypeEmailAddress(docType);
    }

    private static UserSession getUserSession() {
        return GlobalVariables.getUserSession();
    }
}

