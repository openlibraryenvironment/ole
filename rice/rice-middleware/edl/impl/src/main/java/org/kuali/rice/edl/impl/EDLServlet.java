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
package org.kuali.rice.edl.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.edl.impl.service.EdlServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kns.util.IncidentReportUtils;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.exception.AuthenticationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Takes edl web requests.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class EDLServlet extends HttpServlet {

	private static final long serialVersionUID = -6344765194278430690L;

	private static final Logger LOG = Logger.getLogger(EDLServlet.class);

	@Override
	public void init() throws ServletException {
		try {
			EdlServiceLocator.getEDocLiteService().initEDLGlobalConfig();
		} catch (Exception e) {
			LOG.error("Error initializing EDL", e);
		}

	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String documentId = null;
		try {
		    UserSession userSession = KRADUtils.getUserSessionFromRequest(request);
		    if (userSession == null) {
		        throw new AuthenticationException("Failed to locate a user session for request.");
		    }
		    GlobalVariables.setUserSession(userSession);
		    
		    RequestParser requestParser = new RequestParser(request);
		    String inputCommand = requestParser.getParameterValue("command");
		    if (StringUtils.equals(inputCommand, "initiate")){
		    	requestParser.setParameterValue("userAction","initiate");
		    }
		    String edlName = requestParser.getParameterValue("edlName");
		    if (edlName == null) {
		        edlName = requestParser.getParameterValue("docTypeName");//this is for 'WorkflowQuicklinks'
		    }
		    EDLController edlController = null;
		    
		    if (edlName == null) {
		        documentId = requestParser.getParameterValue("docId");
		        if (documentId == null) {
		        	String docFormKey = requestParser.getParameterValue(KRADConstants.DOC_FORM_KEY);
		        	if (docFormKey != null) {
		        		Document document = (Document) GlobalVariables.getUserSession().retrieveObject(docFormKey);
		        		Element documentState = EDLXmlUtils.getDocumentStateElement(document);
		        		documentId = EDLXmlUtils.getChildElementTextValue(documentState, "docId");
		        		requestParser.setAttribute(KRADConstants.DOC_FORM_KEY, docFormKey);
		        	}
		        	if (documentId == null) {
		        		throw new WorkflowRuntimeException("No edl name or document id detected");
		        	}
		        }
		        requestParser.setAttribute("docId", documentId);
		        edlController = EdlServiceLocator.getEDocLiteService().getEDLControllerUsingDocumentId(documentId);
		    } else {
		        edlController = EdlServiceLocator.getEDocLiteService().getEDLControllerUsingEdlName(edlName);
		    }

		    //TODO Fix this in a better way (reworking the command structure maybe?)
		    //fix for KULRICE-4057 to make sure we don't destory docContent on empty command params
		    if(inputCommand == null && requestParser.getParameterValue("docId") != null && !"POST".equals(request.getMethod())){
		    	//make sure these are the only params on the request (paging passed undefined input command as well...
		    	if(!(request.getParameterMap().size() > 2)){//ensures ONLY documentId was passed
		    		requestParser.setParameterValue("command", "displayDocSearchView");
		    		LOG.info("command parameter was not passed with the request, and only document ID was. Defaulted command to 'displayDocSearchView' to ensure docContent remains.");
		    	}
		    }

		    EDLControllerChain controllerChain = new EDLControllerChain();
		    controllerChain.addEdlController(edlController);
			//TODO Do we not want to set the content type for the response?		   
		    controllerChain.renderEDL(requestParser, response);

		} catch (Exception e) {
			LOG.error("Error processing EDL", e);
			outputError(request, response, e, documentId);
		} finally {
		    GlobalVariables.setUserSession(null);
		}
	}

	private void outputError(HttpServletRequest request, HttpServletResponse response, Exception exception, String documentId) throws ServletException, IOException {
			IncidentReportUtils.populateRequestForIncidentReport(exception, "" + documentId, "eDoc Lite", request);
	        RequestDispatcher rd = getServletContext().getRequestDispatcher(request.getServletPath() + "/../../kr/kualiExceptionIncidentReport.do");
	        rd.forward(request, response);
	}

}
