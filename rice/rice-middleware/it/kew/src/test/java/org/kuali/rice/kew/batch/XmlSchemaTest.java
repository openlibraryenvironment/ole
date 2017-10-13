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
package org.kuali.rice.kew.batch;

import org.junit.Test;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.core.impl.impex.xml.ClassLoaderEntityResolver;
import org.kuali.rice.kew.rule.xmlrouting.WorkflowNamespaceContext;
import org.kuali.rice.test.RiceTestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.fail;


/**
 * Test schema validation
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XmlSchemaTest extends RiceTestCase {
    private Document validate(InputStream stream) throws ParserConfigurationException, IOException , SAXException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        dbf.setNamespaceAware(true);
        dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", XMLConstants.W3C_XML_SCHEMA_NS_URI);
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(new ClassLoaderEntityResolver()); //  new FileEntityResolver(new File("conf/schema")));
        db.setErrorHandler(new ErrorHandler() {
            public void warning(SAXParseException se) {
                log.warn("Warning parsing xml", se);
            }
            public void error(SAXParseException se) throws SAXException  {
                log.error("Error parsing xml", se);
                throw se;
            }
            public void fatalError(SAXParseException se) throws SAXException {
                log.error("Fatal error parsing xml", se);
                throw se;
            }
        });
        return db.parse(stream);
    }

    @Test public void testValidation() throws ParserConfigurationException, IOException, SAXException {
        Properties filesToIngest = new Properties();
        filesToIngest.load(getClass().getResourceAsStream("XmlSchemaTest.txt"));
        Iterator entries = filesToIngest.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String filePath = entry.getKey().toString();
            filePath = filePath.replace("${basedir}", getBaseDir());
            File testFile = new File(filePath);
            boolean shouldSucceed = Boolean.valueOf(entry.getValue().toString()).booleanValue();
            System.out.println("Validating " + testFile);
            try {
                validate(new FileInputStream(testFile));
                if (!shouldSucceed) {
                    fail("Invalid test file '" + testFile + "' passed validation");
                }
            } catch (Exception e) {
                if (shouldSucceed) {
                    e.printStackTrace();
                    fail("Valid test file '" + testFile + "' failed validation");
                }
            }
        }
    }

    /**
     * Tests that default attribute value is visible to XPath from a schema-validated W3C Document
     * TODO: finish this test when we figure out how to conveniently use namespaces with
     * XPath
     */
    @Test public void testDefaultAttributeValue() throws Exception {
        URL url = getClass().getResource("XmlConfig.xml");
        Document d = validate(url.openStream());
        //d = XmlHelper.trimXml(url.openStream());
        System.out.println(XmlJotter.jotNode(d));
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new WorkflowNamespaceContext());
        Node node = (Node) xpath.evaluate("/data/ruleAttributes/ruleAttribute[name='XMLSearchableAttribute']/searchingConfig/fieldDef[@name='givenname' and @workflowType='ALL']/@title",
                d, XPathConstants.NODE);
        System.out.println("n: " + node);
        //System.out.println("n: " + node.getNodeName());
        //System.out.println("n: " + node.getLocalName());
        //System.out.println("n: " + node.getNamespaceURI());
    }

    @Override
    protected String getModuleName() {
        return "kew";
    }
}
