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

import static org.junit.Assert.assertTrue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.xml.XPathTest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class is used to test edoc lite xml xpath operations
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@Ignore
public class EDocLiteXPathTest extends KEWTestCase {
    private static final Logger LOG = Logger.getLogger(XPathTest.class);

    private static final String STYLESHEET_RESOURCE = "org/kuali/rice/kew/edoclite/DefaultStyle.xsl";
    private static final String INITIAL_EDOC_XML = "initial_edldoc.xml";
    private static final String SAMPLE_EDOC_XML = "sample_edldoc.xml";

    @Test public void testTransformInitialDoc() throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source styleSheet = new StreamSource(this.getClass().getClassLoader().getResourceAsStream(STYLESHEET_RESOURCE));
        Templates templates = templates = factory.newTemplates(styleSheet);
        Transformer transformer = templates.newTransformer();
        transformer.setOutputProperty("indent", "yes");
        transformer.setParameter("readOnly", "false");
        //transformer.setParameter("docType", docType);
        //transformer.setParameter("schema", schema);

        Source input = new StreamSource(this.getClass().getResourceAsStream(INITIAL_EDOC_XML));
        transformer.transform(input, new StreamResult(System.out));
    }

    @Test public void testFieldHasMatchingUserValues() throws Exception {
        LOG.info("testFieldHasMatchingUserValues");
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        XPath xpath = XPathFactory.newInstance().newXPath();
        Document doc = db.parse(this.getClass().getResourceAsStream(SAMPLE_EDOC_XML));
        // enumerate all fields
        final String fieldDefs = "/edlContent/edl/field/display/values";
        NodeList nodes = (NodeList) xpath.evaluate(fieldDefs, doc, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String name = (String) xpath.evaluate("../../@name", node, XPathConstants.STRING);
            LOG.debug("Name: " + name);
            LOG.debug("Value: " + node.getFirstChild().getNodeValue());
            final String expr = "/edlContent/data/version[@current='true']/fieldEntry[@name=current()/../../@name and value=current()]";
            NodeList matchingUserValues = (NodeList) xpath.evaluate(expr, node, XPathConstants.NODESET);
            LOG.debug(matchingUserValues + "");
            LOG.debug(matchingUserValues.getLength() + "");
            if ("gender".equals(name)) {
                assertTrue("Matching values > 0", matchingUserValues.getLength() > 0);
            }
            for (int j = 0; j < matchingUserValues.getLength(); j++) {
                LOG.debug(matchingUserValues.item(j).getFirstChild().getNodeValue());    
            }
        }
    }

/*
    @Test public void testUpdateEDLDocument() throws Exception {
        final Map params = new HashMap();
        params.put("givenname", new String[] { "Frank" });
        params.put("surname", new String[] { "Miller" });
        params.put("email", new String[] { "frank@bogus.blah.asdlajsd.co.uk" });
        params.put("gender", new String[] { "male" });
        params.put("color", new String[] { "blue" });
        params.put("food", new String[] { "sandwiches", "soup" });

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        XPath xpath = XPathFactory.newInstance().newXPath();
        String versionsExpression = "/edlContent/data/version";

        // try an initial empty doc
        EDLDocument edlDoc = new EDLDocument(db.parse(this.getClass().getResourceAsStream(INITIAL_EDOC_XML))); 
        int numVersionsBefore = ((NodeList) xpath.evaluate(versionsExpression, edlDoc.getDocument(), XPathConstants.NODESET)).getLength();
        LOG.debug("Initial before:");
        LOG.debug(edlDoc);
        edlDoc.update(null, params);
        LOG.debug("Initial after:");
        LOG.debug(edlDoc);
        int numVersionsAfter = ((NodeList) xpath.evaluate(versionsExpression, edlDoc.getDocument(), XPathConstants.NODESET)).getLength();
        assertEquals(numVersionsBefore + 1, numVersionsAfter);

        numVersionsBefore = ((NodeList) xpath.evaluate(versionsExpression, edlDoc.getDocument(), XPathConstants.NODESET)).getLength();
        LOG.debug("Initial 2nd time before:");
        LOG.debug(edlDoc);
        edlDoc.update(null, params);
        LOG.debug("Initial 2nd time after:");
        LOG.debug(edlDoc);
        numVersionsAfter = ((NodeList) xpath.evaluate(versionsExpression, edlDoc.getDocument(), XPathConstants.NODESET)).getLength();
        assertEquals(numVersionsBefore + 1, numVersionsAfter);
        
        // try a sample doc
        edlDoc = new EDLDocument(db.parse(this.getClass().getResourceAsStream(SAMPLE_EDOC_XML)));
        numVersionsBefore = ((NodeList) xpath.evaluate(versionsExpression, edlDoc.getDocument(), XPathConstants.NODESET)).getLength();
        LOG.debug("Sample before:");
        LOG.debug(edlDoc);
        edlDoc.update(null, params);
        LOG.debug("Sample after:");
        LOG.debug(edlDoc);
        numVersionsAfter = ((NodeList) xpath.evaluate(versionsExpression, edlDoc.getDocument(), XPathConstants.NODESET)).getLength();
        assertEquals(numVersionsBefore + 1, numVersionsAfter);
    }

    @Test public void testXPathStuff() throws Exception {
        InputStream edlDocContent = new TestUtilities().loadResource(this.getClass(), "edldoccontent.xml");
        org.w3c.dom.Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(edlDocContent);
        // Document document = new DOMBuilder().build(w3cDocument);
        // DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        // Document routeDocument = builder.parse(new File("ParallelRouting.xml"));

        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.getXPathFunctionResolver();
        // String expression = "//version[@current='true']/fieldEntry[@name='name']/value";
        // xpath.getXPathFunctionResolver().resolveFunction();s
        String expression = "//version[@current='true']/fieldEntry[@name=concat('n', 'ame')]/value";
        String expression2 = "local-name(//field[@name='name']/@name)";
        String expression3 = "//version[@current='true']/fieldEntry[@name=local-name(//field[@name='name']/@name)]/value";
        Node node = (Node) xpath.evaluate(expression3, w3cDocument, XPathConstants.NODE);
        xpath.evaluate(expression3, w3cDocument);
        node.getNodeValue();
        node.getNodeType();
        ((Text)node.getFirstChild()).getNodeValue();
        int i =1;
    }
*/
}
