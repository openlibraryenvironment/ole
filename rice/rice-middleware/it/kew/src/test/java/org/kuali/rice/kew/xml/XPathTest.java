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
import static org.junit.Assert.assertNull;

import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.kuali.rice.test.BaseRiceTestCase;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class XPathTest extends BaseRiceTestCase {

	private static final String XSTREAM_SAFE_PREFIX = "wf:xstreamsafe('";
	private static final String XSTREAM_SAFE_SUFFIX = "')";
    public static final String XSTREAM_MATCH_ANYWHERE_PREFIX = "//";
    public static final String XSTREAM_MATCH_RELATIVE_PREFIX = "./";
	
    private static final String TEST_DOC = "<root name=\"root\">\n" +
                                           "  <child name=\"child1\">\n" +
                                           "    <child_1 name=\"child1_1\">\n" +
                                           "      <closedSimple/>\n" +
                                           "      <emptySimple></emptySimple>\n" +
                                           "      <textSimple>some text 1</textSimple>\n" +
                                           "    </child_1>\n" +
                                           "  </child>\n" +
                                           "  <child name=\"child2\">\n" +
                                           "    <child_2 name=\"child2_1\">\n" +
                                           "      <closedSimple/>\n" +
                                           "      <emptySimple></emptySimple>\n" +
                                           "      <textSimple>some text 2</textSimple>\n" +
                                           "    </child_2>\n" +
                                           "  </child>\n" +
                                           "</root>";

    private static final String TEST_ATTRIBUTE_DOC = "<root name=\"root\">\n" +
                                                     "  <field name=\"one\" type=\"ALL\"/>\n" +
                                                     "  <field name=\"two\" type=\"REPORT\"/>\n" +
                                                     "  <field name=\"three\"/>\n" +
                                                     "</root>";

    private static final XPath XPATH = XPathFactory.newInstance().newXPath();

    private static final InputSource getTestInputSource() {
        return new InputSource(new StringReader(TEST_DOC));
    }

    @Test public void testAttributeAbsence() throws XPathExpressionException {
        NodeList nodes = (NodeList) XPATH.evaluate("/root/child[not(@nonExistentAttribute)]", getTestInputSource(), XPathConstants.NODESET);
        assertEquals(2, nodes.getLength());
        assertEquals("child1", nodes.item(0).getAttributes().getNamedItem("name").getNodeValue());
        assertEquals("child2", nodes.item(1).getAttributes().getNamedItem("name").getNodeValue());

        // now try with an equivalent compound expression
        nodes = (NodeList) XPATH.evaluate("/root/*[local-name(.) = 'child' or (@nonExistentAttribute)]", getTestInputSource(), XPathConstants.NODESET);
        assertEquals(2, nodes.getLength());
        assertEquals("child1", nodes.item(0).getAttributes().getNamedItem("name").getNodeValue());
        assertEquals("child2", nodes.item(1).getAttributes().getNamedItem("name").getNodeValue());

        nodes = (NodeList) XPATH.evaluate("/root/child[not(@name)]", getTestInputSource(), XPathConstants.NODE);
        assertNull(nodes);

        // now use a more specific test document
        nodes = (NodeList) XPATH.evaluate("/root/field[@type='ALL' or not(@type)]", new InputSource(new StringReader(TEST_ATTRIBUTE_DOC)), XPathConstants.NODESET);
        assertEquals(2, nodes.getLength());
        assertEquals("one", nodes.item(0).getAttributes().getNamedItem("name").getNodeValue());
        assertEquals("three", nodes.item(1).getAttributes().getNamedItem("name").getNodeValue());
    }

    @Test public void testSelectJustChilds() throws XPathExpressionException {
        NodeList nodes = (NodeList) XPATH.evaluate("/root/child", getTestInputSource(), XPathConstants.NODESET);
        assertEquals(2, nodes.getLength());
        assertEquals("child1", nodes.item(0).getAttributes().getNamedItem("name").getNodeValue());
        assertEquals("child2", nodes.item(1).getAttributes().getNamedItem("name").getNodeValue());
    }

    @Test public void testSelectAbsoluteChild() throws XPathExpressionException {
        Node node = (Node) XPATH.evaluate("/root/child", getTestInputSource(), XPathConstants.NODE);
        assertEquals("child1", node.getAttributes().getNamedItem("name").getNodeValue());
    }

    @Test public void testSelectAnyChild() throws XPathExpressionException {
        Node anyNode = (Node) XPATH.evaluate("//child", getTestInputSource(), XPathConstants.NODE);
        assertEquals("child1", anyNode.getAttributes().getNamedItem("name").getNodeValue());
    }

    @Test public void testNonexistent()  throws XPathExpressionException {
        final String expr = "//child/child_1/nonExistent";
        Node nonexistent = (Node) XPATH.evaluate(expr, getTestInputSource(), XPathConstants.NODE);
        assertNull(nonexistent);
        String valueOfNonexistentElement = (String) XPATH.evaluate(expr, getTestInputSource(), XPathConstants.STRING);
        // a non-existent element does not have a 'null' text value but a zero-length string
        assertEquals("", valueOfNonexistentElement);
    }

    @Test public void testClosedSimple() throws XPathExpressionException {
        final String expr = "//child/child_1/closedSimple";
        Node closedSimple = (Node) XPATH.evaluate(expr, getTestInputSource(), XPathConstants.NODE);
        assertNotNull(closedSimple);
        assertNull(closedSimple.getFirstChild());
        String valueOfClosedTag = (String) XPATH.evaluate(expr, getTestInputSource(), XPathConstants.STRING);
        // a closed element does not have a 'null' text value but a zero-length string
        assertEquals("", valueOfClosedTag);
    }

    @Test public void testEmptySimple() throws XPathExpressionException {
        final String expr = "//child/child_1/emptySimple";
        Node emptySimple = (Node) XPATH.evaluate(expr, getTestInputSource(), XPathConstants.NODE);
        assertNotNull(emptySimple);
        assertNull(emptySimple.getFirstChild());
        String valueOfEmptyTag = (String) XPATH.evaluate(expr, getTestInputSource(), XPathConstants.STRING);
        // a closed element does not have a 'null' text value but a zero-length string
        assertEquals("", valueOfEmptyTag);
    }

    @Test public void testText() throws XPathExpressionException {
        final String expr = "//child/child_2[@name='child2_1']/textSimple";
        final String expected = "some text 2";
        Node textSimple = (Node) XPATH.evaluate(expr, getTestInputSource(), XPathConstants.NODE);
        assertNotNull(textSimple);
        assertNotNull(textSimple.getFirstChild());
        String valueOfTextTag = (String) XPATH.evaluate(expr, getTestInputSource(), XPathConstants.STRING);
        // a closed element does not have a 'null' text value but a zero-length string
        assertEquals(expected, valueOfTextTag);
    }
    
/*    @Test public void testBooleanTranslation() throws Exception {
        String KUALI_CAMPUS_TYPE_ACTIVE_INDICATOR_XSTREAMSAFE = XSTREAM_SAFE_PREFIX + XSTREAM_MATCH_ANYWHERE_PREFIX + "campus/campusType/dataObjectMaintenanceCodeActiveIndicator" + XSTREAM_SAFE_SUFFIX;
        String KUALI_INITIATOR_UNIVERSAL_USER_STUDENT_INDICATOR_XSTREAMSAFE = XSTREAM_SAFE_PREFIX + XSTREAM_MATCH_ANYWHERE_PREFIX + "kualiTransactionalDocumentInformation/documentInitiator/person/student" + XSTREAM_SAFE_SUFFIX;
        DocumentContent docContent = new StandardDocumentContent(
              "<documentContent><applicationContent><org.kuali.rice.krad.workflow.KualiDocumentXmlMaterializer>" +
              "  <kualiTransactionalDocumentInformation>" + 
              "  <documentInitiator>" + 
              "     <person>" +
              "        <student>false</student>" +
              "        <campus class=\"org.kuali.rice.krad.bo.CampusImpl--EnhancerByCGLIB--4968dd25\">" +
              "          <campusType>" +
              "            <dataObjectMaintenanceCodeActiveIndicator>true</dataObjectMaintenanceCodeActiveIndicator>" +
              "          </campusType>" + 
              "        </campus>" + 
              "      </person>" + 
              "    </documentInitiator>" + 
              "  </kualiTransactionalDocumentInformation>" + 
              "  <document>" +
              "    <purchaseOrderCreateDate>11-09-2007</purchaseOrderCreateDate>" +
              "    <oldPurchaseOrderCreateDate></oldPurchaseOrderCreateDate>" +
              "  </document>" +
              "</org.kuali.rice.krad.workflow.KualiDocumentXmlMaterializer></applicationContent></documentContent>"
              );
//        DocumentContent docContent = KualiAttributeTestUtil.getDocumentContentFromXmlFileAndPath(KualiAttributeTestUtil.PURCHASE_ORDER_DOCUMENT, KualiAttributeTestUtil.RELATIVE_PATH_IN_PROJECT_WORKFLOW, "PurchaseOrderDocument");
        XPath xpath = XPathHelper.newXPath(docContent.getDocument());

        String valueForTrue = "Yes";
        String valueForFalse = "No";

        // test campus active indicator field translation to 'Yes'
        String xpathConditionStatement = KUALI_CAMPUS_TYPE_ACTIVE_INDICATOR_XSTREAMSAFE + " = 'true'";
        String xpathExpression = constructXpathExpression(valueForTrue, valueForFalse, xpathConditionStatement);
        String xpathResult = (String) xpath.evaluate(xpathExpression, docContent.getDocument(), XPathConstants.STRING);
        assertEquals("Using translated xpath expression '" + xpathExpression + "'", valueForTrue, xpathResult);

        // test user student indicator translation to 'No'
        xpathConditionStatement = KUALI_INITIATOR_UNIVERSAL_USER_STUDENT_INDICATOR_XSTREAMSAFE + " = 'true'";
        xpathExpression = constructXpathExpression(valueForTrue, valueForFalse, xpathConditionStatement);
        xpathResult = (String) xpath.evaluate(xpathExpression, docContent.getDocument(), XPathConstants.STRING);
        assertEquals("Using translated xpath expression '" + xpathExpression + "'", valueForFalse, xpathResult);

        // test filled in date field translates to 'Yes'
        String expression = XSTREAM_SAFE_PREFIX + XSTREAM_MATCH_ANYWHERE_PREFIX + "document/purchaseOrderCreateDate" + XSTREAM_SAFE_SUFFIX;
        xpathConditionStatement = "boolean(" + expression + ") and not(" + expression + " = '')";
        xpathExpression = constructXpathExpression(valueForTrue, valueForFalse, xpathConditionStatement);
        xpathResult = (String) xpath.evaluate(xpathExpression, docContent.getDocument(), XPathConstants.STRING);
        assertEquals("Using translated xpath expression '" + xpathExpression + "'", valueForTrue, xpathResult);

        // test empty date field translates to 'No'
        expression = XSTREAM_SAFE_PREFIX + XSTREAM_MATCH_ANYWHERE_PREFIX + "document/oldPurchaseOrderCreateDate" + XSTREAM_SAFE_SUFFIX;
        xpathConditionStatement = "boolean(" + expression + ") and not(" + expression + " = '')";
        xpathExpression = constructXpathExpression(valueForTrue, valueForFalse, xpathConditionStatement);
        xpathResult = (String) xpath.evaluate(xpathExpression, docContent.getDocument(), XPathConstants.STRING);
        assertEquals("Using translated xpath expression '" + xpathExpression + "'", valueForFalse, xpathResult);

        // test non-existent date field translates to 'No'
        expression = XSTREAM_SAFE_PREFIX + XSTREAM_MATCH_ANYWHERE_PREFIX + "document/newPurchaseOrderCreateDate" + XSTREAM_SAFE_SUFFIX;
        xpathConditionStatement = "boolean(" + expression + ") and not(" + expression + " = '')";
        xpathExpression = constructXpathExpression(valueForTrue, valueForFalse, xpathConditionStatement);
        xpathResult = (String) xpath.evaluate(xpathExpression, docContent.getDocument(), XPathConstants.STRING);
        assertEquals("Using translated xpath expression '" + xpathExpression + "'", valueForFalse, xpathResult);
    }*/

    private String constructXpathExpression(String valueForTrue, String valueForFalse, String booleanXPathExpression) {
        String[] xpathElementsToInsert = new String[3];
        xpathElementsToInsert[0] = "concat( substring('" + valueForTrue + "', number(not(";
        xpathElementsToInsert[1] = "))*string-length('" + valueForTrue + "')+1), substring('" + valueForFalse + "', number(";
        xpathElementsToInsert[2] = ")*string-length('" + valueForFalse + "')+1))";

        StringBuffer returnableString = new StringBuffer();
        for (int i = 0; i < xpathElementsToInsert.length; i++) {
            String newXpathElement = xpathElementsToInsert[i];
            returnableString.append(newXpathElement);

            /*
             * Append the given xpath expression onto the end of the stringbuffer only in the following cases - if there is only one
             * element in the string array - if there is more than one element in the string array and if the current element is not
             * the last element
             */
            if (((i + 1) != xpathElementsToInsert.length) || (xpathElementsToInsert.length == 1)) {
                returnableString.append(booleanXPathExpression);
            }
        }
        return returnableString.toString();

    }
}
