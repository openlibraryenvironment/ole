/**
 * Copyright 2005-2013 The Kuali Foundation
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

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.edl.impl.service.EDocLiteService;
import org.kuali.rice.edl.impl.service.EdlServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.krad.UserSession;
import org.w3c.dom.Document;


public class EDLCreationTest extends KEWTestCase {

	protected void loadTestData() throws Exception {
		super.loadXmlFile("widgets.xml");
		super.loadXmlFile("edlstyle.xml");
		super.loadXmlFile("TestEDL1.xml");
		super.loadXmlFile("EDLRoutingData.xml");
	}

	@Ignore("This test needs to be implemented!")
	@Test public void testEDLDOMCreationAndTransformation() throws Exception {
//		ConfigContext.getCurrentContextConfig().overrideProperty(Config.EDL_CONFIG_LOCATION, "classpath:org/kuali/rice/kew/edl/EDLConfig1.xml");
//		WorkflowUser user = SpringServiceLocator.getUserService().getWorkflowUser(new NetworkIdVO("user1"));
//		EDLController edlController = getEDLService().getEDLController("TestEDL1");
//
//		MockHttpServletRequest request = new MockHttpServletRequest();
//		request.setContentType("text/html");
//		request.setMethod("POST");
//		Transformer transformer = TransformerFactory.newInstance().newTransformer();
//		edlController.setEdlContext(getEDLContext(edlController, request, transformer, user));
//		Document dom = edlController.notifyComponents();
//		System.out.print(XmlJotter.jotNode(dom));
//		transform(dom, edlController.getStyle().newTransformer());
	}

	@Ignore("This test needs to be implemented!")
	@Test public void testEDLCreationWithWorkflowDocument() throws Exception {
//		ConfigContext.getCurrentContextConfig().overrideProperty(Config.EDL_CONFIG_LOCATION, "classpath:org/kuali/rice/kew/edl/EDLConfig1.xml");
//		WorkflowUser user = SpringServiceLocator.getUserService().getWorkflowUser(new NetworkIdVO("user1"));
//		EDLController edlController = getEDLService().getEDLController("TestEDL1");
//
//		MockHttpServletRequest request = new MockHttpServletRequest();
//		request.setContentType("text/html");
//		//make our params create the document
//		request.addParameter("userAction", "create");
//		request.setMethod("POST");
//		Transformer transformer = edlController.getStyle().newTransformer();
//		EDLContext edlContext = getEDLContext(edlController, request, transformer, user);
//		edlController.setEdlContext(edlContext);
//		Document dom = edlController.notifyComponents();
//		System.out.print(XmlJotter.jotNode(dom));
//		transform(dom, transformer);
//
//		//now let's route our document
//		HttpSession session = request.getSession();
//		request = new MockHttpServletRequest();
//		request.setContentType("text/html");
//		request.setMethod("POST");
//		//make our params create the document
//		request.addParameter("userAction", "route");
//		request.addParameter("department", "AFRI COAS");
//		request.addParameter("creditType", "undergraduateCredit");
//		request.addParameter("academicSubjectCode", "American Studies");
//		request.addParameter("courseNumber", "A321");
//		request.addParameter("currentVersion", "0");
//		request.setSession(session);
//		edlContext.getRequestParser().setRequest(request);
//
//		transformer = edlController.getStyle().newTransformer();
//		edlController.setEdlContext(getEDLContext(edlController, request, transformer, user));
//		dom = edlController.notifyComponents();
//		System.out.print(XmlJotter.jotNode(dom));
//		transform(dom, transformer);
	}

	private void transform(Document dom, Transformer transformer) throws Exception {
        transformer.setOutputProperty("indent", "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(dom), new StreamResult(System.out));
	}

	private EDLContext getEDLContext(EDLController edlController, HttpServletRequest request, Transformer transformer, String principalId) throws TransformerConfigurationException {
		EDLContext edlContext = new EDLContext();
		edlContext.setEdocLiteAssociation(edlController.getEdocLiteAssociation());
		edlContext.setUserSession(new UserSession(principalId));
		edlContext.setTransformer(transformer);
//		edlContext.setSessionAccessor(new SessionAccessor(request));
		edlContext.setRequestParser(new RequestParser(request));
		return edlContext;
	}

	private EDocLiteService getEDLService() {
		return EdlServiceLocator.getEDocLiteService();
	}

}
