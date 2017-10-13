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

import org.kuali.rice.core.api.mail.EmailContent;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


/**
 * A class which has some convenience methods for handling Emails and stylesheets. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class EmailStyleHelper {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(EmailStyleHelper.class);
    
    public EmailContent generateEmailContent(Templates style, Document document) {
	DOMResult result = new DOMResult();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Input document: " + XmlJotter.jotNode(document.getDocumentElement(), true));
        }
        try {
            style.newTransformer().transform(new DOMSource(document), result);
        } catch (TransformerException te) {
            String message = "Error transforming immediate reminder DOM";
            LOG.error(message, te);
            throw new WorkflowRuntimeException(message, te);
        }

        Node node = result.getNode();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Email to be sent: " + XmlJotter.jotNode(node));
        }
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        try {
            String subject = (String) xpath.evaluate("/email/subject", node, XPathConstants.STRING);
            String body = (String) xpath.evaluate("/email/body", node, XPathConstants.STRING);
            // simple heuristic to determine whether content is HTML
            return new EmailContent(subject, body, body.matches("(?msi).*<(\\w+:)?html.*"));
        } catch (XPathExpressionException xpee) {
            throw new WorkflowRuntimeException("Error evaluating generated email content", xpee);
        }
    }

}
