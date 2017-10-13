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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.util.GlobalVariables;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathFactory;
import java.util.Stack;


/**
 * Contains a stack of EDLControllers.  Allows EDL components to add new controllers to the chain runtime.  The idea
 * being that this is how you would page between edls.  Uses the template associated with the last edl controller
 * to render the dom of the last edl controller.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class EDLControllerChain {

	private static final Logger LOG = Logger.getLogger(EDLControllerChain.class);

	private Stack<EDLController> edlControllers = new Stack<EDLController>();

	public void renderEDL(RequestParser requestParser, HttpServletResponse response) throws Exception {
		EDLController edlController = edlControllers.pop();
		edlController.setEdlContext(getInitialEDLContext(edlController, requestParser, edlController.getStyle().newTransformer()));

		Document dom = edlController.notifyComponents();
		if (edlControllers.isEmpty()) {
			transform(edlController.getEdlContext(), dom, response);
		} else {
			renderEDL(requestParser, response);
		}
	}

	public void addEdlController(EDLController edlController) {
		edlControllers.add(edlController);
	}

	private void transform(EDLContext edlContext, Document dom, HttpServletResponse response) throws Exception {
		if (StringUtils.isNotBlank(edlContext.getRedirectUrl())) {
			response.sendRedirect(edlContext.getRedirectUrl());
			return;
		}
	    response.setContentType("text/html; charset=UTF-8");
		Transformer transformer = edlContext.getTransformer();

        transformer.setOutputProperty("indent", "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        String user = null;
        String loggedInUser = null;
        if (edlContext.getUserSession() != null) {
            Person wu = edlContext.getUserSession().getPerson();
            if (wu != null) user = wu.getPrincipalId();
            wu = edlContext.getUserSession().getPerson();
            if (wu != null) loggedInUser = wu.getPrincipalId();
        }
        transformer.setParameter("user", user);
        transformer.setParameter("loggedInUser", loggedInUser);
        if (LOG.isDebugEnabled()) {
        	LOG.debug("Transforming dom " + XmlJotter.jotNode(dom, true));
        }
        transformer.transform(new DOMSource(dom), new StreamResult(response.getOutputStream()));
	}

	private EDLContext getInitialEDLContext(EDLController edlController, RequestParser requestParser, Transformer transformer) {
		EDLContext edlContext = new EDLContext();
		edlContext.setEdlControllerChain(this);
		edlContext.setEdocLiteAssociation(edlController.getEdocLiteAssociation());
		edlContext.setUserSession(GlobalVariables.getUserSession());
		edlContext.setTransformer(transformer);
		edlContext.setRequestParser(requestParser);
        edlContext.setXpath(XPathFactory.newInstance().newXPath());
        return edlContext;
	}

}
