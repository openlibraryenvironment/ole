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
package org.kuali.rice.kew.mail;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailContent;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.engine.node.SimpleNode;
import org.kuali.rice.kew.engine.node.SimpleResult;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import java.io.StringReader;


/**
 * A node which will send emails using the configured stylesheet to generate the email content.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EmailNode implements SimpleNode {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(EmailNode.class);

    private EmailStyleHelper emailStyleHelper = new EmailStyleHelper();
    private String styleName;
    private String from;
    private String to;
    
    public SimpleResult process(RouteContext context, RouteHelper helper) throws Exception {
    	if (context.isSimulation()) {
            if (!context.getActivationContext().isActivateRequests()) {
            	return new SimpleResult(true);
            }
        } 
	loadConfiguration(context);
	Document document = generateXmlInput(context);
	if (LOG.isDebugEnabled()) {
	    LOG.debug("XML input for email tranformation:\n" + XmlJotter.jotNode(document));
	}
	Templates style = loadStyleSheet(styleName);
	EmailContent emailContent = emailStyleHelper.generateEmailContent(style, document);
	if (!StringUtils.isBlank(to)) {
		CoreApiServiceLocator.getMailer().sendEmail(new EmailFrom(from), new EmailTo(to), new EmailSubject(emailContent.getSubject()), new EmailBody(emailContent.getBody()), emailContent.isHtml());
	}
	return new SimpleResult(true);
    }

    protected Document generateXmlInput(RouteContext context) throws Exception {
	DocumentBuilder db = getDocumentBuilder(true);
        Document doc = db.newDocument();
        Element emailNodeElem = doc.createElement("emailNode");
        doc.appendChild(emailNodeElem);
        String principalId = null;  // Added to the convertRouteHeader is not ambigious.
        org.kuali.rice.kew.api.document.Document routeHeaderVO = DocumentRouteHeaderValue.to(context.getDocument());
        RouteNodeInstance routeNodeInstanceVO = org.kuali.rice.kew.engine.node.RouteNodeInstance.to(context.getNodeInstance());
        Document documentContent = context.getDocumentContent().getDocument();
        XStream xstream = new XStream();
        Element docElem = XmlHelper.readXml(xstream.toXML(routeHeaderVO)).getDocumentElement();
        Element nodeElem = XmlHelper.readXml(xstream.toXML(routeNodeInstanceVO)).getDocumentElement();
        emailNodeElem.appendChild(doc.importNode(docElem, true));
        emailNodeElem.appendChild(doc.importNode(nodeElem, true));
        emailNodeElem.appendChild(doc.importNode(documentContent.getDocumentElement(), true));
        Element dConElem = context.getDocumentContent().getApplicationContent();//Add document Content element for
 	 	emailNodeElem.appendChild(doc.importNode(dConElem, true));//access by the stylesheet when creating the email
        return doc;
    }

    protected DocumentBuilder getDocumentBuilder(boolean coalesce) throws Exception {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setCoalescing(coalesce);
	return dbf.newDocumentBuilder();
    }

    protected Templates loadStyleSheet(String styleName) {
	try {
	    Templates style = CoreServiceApiServiceLocator.getStyleService().getStyleAsTranslet(styleName);
	    if (style == null) {
		throw new WorkflowRuntimeException("Failed to locate stylesheet with name '" + styleName + "'");
	    }
	    return style;
	} catch (TransformerConfigurationException tce) {
	    throw new WorkflowRuntimeException("Failed to load stylesheet with name '" + styleName + "'");
	}
    }

    protected boolean isProduction() {
        return ConfigContext.getCurrentContextConfig().isProductionEnvironment();
    }

    protected void loadConfiguration(RouteContext context) throws Exception {
	String contentFragment = context.getNodeInstance().getRouteNode().getContentFragment();
	DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = db.parse(new InputSource(new StringReader(contentFragment)));
	if (!isProduction()) {
	    NodeList testAddresses = document.getElementsByTagName("testAddress");
	    if (testAddresses.getLength() >= 1) {
		this.to = testAddresses.item(0).getTextContent();
	    }
	} else {
	    NodeList toAddresses = document.getElementsByTagName("to");
	    if (toAddresses.getLength() != 1) {
		throw new WorkflowRuntimeException("Must have exactly one 'to' address");
	    }
	    to = toAddresses.item(0).getTextContent();
	    if ("initiator".equalsIgnoreCase(to))
	    {	
	    	Person person = KimApiServiceLocator.getPersonService().getPerson(context.getDocument().getInitiatorWorkflowId());
			to = (person == null ? "" : person.getEmailAddressUnmasked());
	    }
	    if (StringUtils.isBlank(to)) {
	    	throw new WorkflowRuntimeException("Email Address is missing from user's profile.");
	    }
	}

	NodeList fromAddresses = document.getElementsByTagName("from");
	if (fromAddresses.getLength() != 1) {
	    throw new WorkflowRuntimeException("Must have exactly one 'from' address");
	}
	this.from = fromAddresses.item(0).getTextContent();

	if ("initiator".equalsIgnoreCase(this.from)) {
		Person initiator = KEWServiceLocator.getIdentityHelperService().getPerson(context.getDocument().getInitiatorWorkflowId());
		// contructs the email from so that it includes name as well as address
		// for example: "Doe, John D" <john@doe.com>
 	 	this.from = "\"" + initiator.getName() + "\" <";
 	 	this.from += initiator.getEmailAddress() + ">";
	}
	if (StringUtils.isBlank(this.from)) {
		throw new WorkflowRuntimeException("No email address could be found found for principal with id " + context.getDocument().getInitiatorWorkflowId());
	}
	
	if (LOG.isInfoEnabled()) {
 	 	LOG.info("Email From is set to:" + this.from);
 	 	LOG.info("Email To is set to:" + this.to);
	}
	
	NodeList styleNames = document.getElementsByTagName("style");
	if (styleNames.getLength() != 1) {
	    throw new WorkflowRuntimeException("Must have exactly one 'style'");
	}
	this.styleName = styleNames.item(0).getTextContent();
    }




}
