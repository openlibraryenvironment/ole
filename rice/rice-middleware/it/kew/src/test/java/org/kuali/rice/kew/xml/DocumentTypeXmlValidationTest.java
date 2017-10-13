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
package org.kuali.rice.kew.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.kuali.rice.core.impl.impex.xml.ClassLoaderEntityResolver;
import org.kuali.rice.test.BaseRiceTestCase;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class DocumentTypeXmlValidationTest extends BaseRiceTestCase {
    private final XPath xpath = XPathFactory.newInstance().newXPath();

    /**
     * Emulates the policy value parsing code
     * @param name policy name
     * @param expected expected value
     * @param xml input xml
     * @throws XPathExpressionException
     */
    private void testPolicyValue(String name, boolean expected, String xml) throws XPathExpressionException {
        Boolean b = (Boolean) xpath.evaluate("/doc/policies/policy[name='" + name + "']/value", new InputSource(new StringReader(xml)), XPathConstants.BOOLEAN);
        assertNotNull(b);
        if (b.booleanValue()) {
            String value = (String) xpath.evaluate("/doc/policies/policy[name='" + name + "']/value", new InputSource(new StringReader(xml)), XPathConstants.STRING);
            log.error(name + " value: " + value);
            b = Boolean.valueOf(value);
        } else {
            b = Boolean.FALSE;
        }
        assertNotNull(b);
        assertEquals(name + " policy value was not " + expected, expected, b.booleanValue());
    }

    /**
     * Exercises the type of XPath expressions that are used to evaluate policy values
     * @throws XPathExpressionException
     */
    @Test public void testXPathBooleanEvaluation() throws XPathExpressionException {
        String xml = "<doc>" +
                       "<policies>" +
                         "<policy>" +
                           "<inherited>false</inherited>" +
                           "<name>DEFAULT_APPROVE</name>" +
                           "<value>false</value>" +
                         "</policy>" +
                         "<policy>" +
                           "<inherited>false</inherited>" +
                           "<name>TEST1</name>" +
                           "<value>blah</value>" +
                         "</policy>" +
                         "<policy>" +
                           "<inherited>false</inherited>" +
                           "<name>TEST2</name>" +
                           "<value></value>" +
                         "</policy>" +
                         "<policy>" +
                           "<inherited>false</inherited>" +
                           "<name>TEST3</name>" +
                         "</policy>" +
                       "</policies>" +
                     "</doc>";

        testPolicyValue("DEFAULT_APPROVE", false, xml);
        testPolicyValue("TEST1", false, xml);
        testPolicyValue("TEST2", false, xml);
        testPolicyValue("TEST3", false, xml);
        testPolicyValue("BOGUS", false, xml);
    }

    @Test public void testValidActivationTypes() throws Exception {
       validate(new InputSource(getClass().getResourceAsStream("ValidActivationTypes.xml")));
    }

    @Test public void testBadActivationType() throws Exception {
        try {
            validate(new InputSource(getClass().getResourceAsStream("BadActivationType.xml")));
            fail("Bad activation type passed validation");
        } catch (SAXParseException spe) {
            // expected
            log.error("Bad activation type exception: " + spe);
        }
    }

    @Test public void testValidPolicyNames() throws Exception {
        validate(new InputSource(getClass().getResourceAsStream("ValidPolicyNames.xml")));
    }

    @Test public void testBadPolicyName() throws Exception {
        try {
            validate(new InputSource(getClass().getResourceAsStream("BadPolicyName.xml")));
            fail("Bad policy name passed validation");
        } catch (SAXParseException spe) {
            // expected
            log.error("Bad policy name exception: " + spe);
        }
    }

    /*
     validation can't catch this;
     we could possibly catch a *missing* nextNode though,
     but that would complicate the schema somewhat so is not
     implemented at this point
    public void testBadNextNode() throws Exception {
        try {
            XmlHelper.validate(new InputSource(getClass().getResourceAsStream("BadNextNode.xml")));
            fail("Bad nextNode passed validation");
        } catch (SAXParseException spe) {
            // expected
            log.error("Bad nextNode exception: " + spe);
        }
    }
    */

    @Test public void testNoDocHandler() throws Exception {
        validate(new InputSource(getClass().getResourceAsStream("NoDocHandler.xml")));
    }

    @Test public void testInvalidParentDocType() throws Exception {
        // although semantically invalid, this should actually pass XML validation as it is syntactically valid
        validate(new InputSource(getClass().getResourceAsStream("InvalidParent.xml")));
    }
    
    /**
     * This method tests the potential ingestion of both document type elements in a single 'document types' element
     * 
     * @throws Exception
     */
    @Test public void testDualDocumentTypeIngestion() throws Exception {
        validate(new InputSource(getClass().getResourceAsStream("DualDocumentTypes.xml")));
    }

    /**
     * This method tests the potential ingestion of only the route definition section of a document
     * 
     * @throws Exception
     */
    @Test public void testRoutePathOnly() throws Exception {
        validate(new InputSource(getClass().getResourceAsStream("RoutePathOnlyDocument.xml")));
    }

    /**
     * This method tests that a document type using overwrite mode is possible with just the 'name' element
     * 
     * @throws Exception
     */
    @Test public void testNoRoutePathOnly() throws Exception {
        validate(new InputSource(getClass().getResourceAsStream("NoRoutePathOnlyDocument.xml")));
    }


    public static void validate(final InputSource source) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        dbf.setNamespaceAware( true );
        dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", XMLConstants.W3C_XML_SCHEMA_NS_URI);
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(new ClassLoaderEntityResolver());
        db.setErrorHandler(new ErrorHandler() {
            public void warning(SAXParseException se) throws SAXException {
                throw se;
            }
            public void error(SAXParseException se) throws SAXException {
                throw se;
            }
            public void fatalError(SAXParseException se) throws SAXException {
                throw se;
            }
        });
        db.parse(source);
    }
}
