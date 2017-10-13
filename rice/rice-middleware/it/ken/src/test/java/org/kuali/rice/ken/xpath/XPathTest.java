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
package org.kuali.rice.ken.xpath;

import org.apache.commons.io.IOUtils;
import org.apache.xerces.jaxp.JAXPConstants;
import org.junit.Test;
import org.kuali.rice.ken.test.KENTestCase;
import org.kuali.rice.ken.util.DocumentNamespaceContext;
import org.kuali.rice.ken.util.SimpleErrorHandler;
import org.kuali.rice.ken.util.Util;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;


/**
 * Unit test that tests the affects of various document parsing (DocumentBuilderFactory)
 * and XPath (XPath) flags, such as validation, namespace awareness, and namespace context.
 * Lessons learned:
 * <ul>
 *   <li>DocumentBuilder namespace awareness needs to be turned on for validation to work</li>
 *   <li>XPath absolutely requires a working NamespaceContext and qualified node names in expressions
 *       if operating against a DOM which is the result of a validating parse</li>
 *   <li>There is no apparent way to set the "default" namespace for XPath...so even when NamespaceContext
 *       is set, nodes must be qualified.  The only way to obtain the "default" namespace is to explicitly
 *       qualify with an empty namespace, e.g. /:notification/:channel (which is ugly and potentially confusing)</li>
 *   <li>When deriving NamespaceContext from validated DOM, the "default" namespace must therefore be explicitly
 *       registered with a prefix (which is redundant) so the NamespaceContext lookup can succeed.  The alternative
 *       is to predefine the prefix in the NamespaceContent (which can be done by using a CompositeNamespaceContenxt
 *       consisting of a ConfiguredNamespaceContext and a DocumentNamespaceContext).</li>
 * </ul>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineMode(Mode.CLEAR_DB)
public class XPathTest extends KENTestCase {
    private static final String TEST_XML = "sample_message_event_type.xml";

    protected InputSource getTestXMLInputSource() {
        InputStream is = XPathTest.class.getResourceAsStream(TEST_XML);
        if (is != null) {
            try {
                LOG.info(IOUtils.toString(is));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(is);
            }

            is = XPathTest.class.getResourceAsStream(TEST_XML);
        }
        return new InputSource(is);
    }

    protected XPath getXPath(Document doc) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        if (doc != null) {
            xpath.setNamespaceContext(new DocumentNamespaceContext(doc));
        } else {
            xpath.setNamespaceContext(Util.NOTIFICATION_NAMESPACE_CONTEXT);
        }
        return xpath;
    }

    protected Document getDocument(boolean namespaceAware, boolean validate) throws Exception {
        // TODO: optimize this
        final InputSource source = getTestXMLInputSource();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(validate);
        dbf.setNamespaceAware(namespaceAware);
        dbf.setAttribute(JAXPConstants.JAXP_SCHEMA_LANGUAGE, JAXPConstants.W3C_XML_SCHEMA);
        DocumentBuilder db = dbf.newDocumentBuilder();
        LOG.info("Setting entityresolver");
        db.setEntityResolver(Util.getNotificationEntityResolver(services.getNotificationContentTypeService()));
        db.setErrorHandler(new SimpleErrorHandler(LOG));
        return db.parse(source);
    }

    @Test
    public void testXPathWithPlainDOM() throws Exception {
        Document doc = getDocument(false, false);
        XPath xpath = getXPath(null);
        String channelName = (String) xpath.evaluate("/notification/channel", doc.getDocumentElement(), XPathConstants.STRING);
        assertEquals("Test Channel #1", channelName);
    }
    @Test
    public void testXPathWithNamespaceAwareDOM() throws Exception {
        Document doc = getDocument(true, false);
        XPath xpath = getXPath(null);
        String channelName = (String) xpath.evaluate("/nreq:notification/nreq:channel", doc.getDocumentElement(), XPathConstants.STRING);
        assertEquals("Test Channel #1", channelName);
    }
    @Test
    public void testXPathWithValidatedDOMFixedNamespace() throws Exception {
        LOG.debug("TEST");
        Document doc = getDocument(true, true);
        LOG.info("Default namespace: " + doc.lookupNamespaceURI(null));
        XPath xpath = getXPath(null);
        String channelName = (String) xpath.evaluate("/nreq:notification/nreq:channel", doc.getDocumentElement(), XPathConstants.STRING);
        assertEquals("Test Channel #1", channelName);
    }
    @Test
    public void testXPathWithValidatedDOMDocNamespace() throws Exception {
        LOG.debug("TEST");
        Document doc = getDocument(true, true);
        LOG.info("Default namespace: " + doc.lookupNamespaceURI(null));
        LOG.info("default prefix: " + doc.lookupPrefix(doc.lookupNamespaceURI(null)));
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(Util.getNotificationNamespaceContext(doc));
        String channelName = (String) xpath.evaluate("/nreq:notification/nreq:channel", doc.getDocumentElement(), XPathConstants.STRING);
        assertEquals("Test Channel #1", channelName);
    }
}
