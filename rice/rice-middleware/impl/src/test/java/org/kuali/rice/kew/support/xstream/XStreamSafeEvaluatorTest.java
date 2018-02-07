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
package org.kuali.rice.kew.support.xstream;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.kew.xml.xstream.XStreamSafeEvaluator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class XStreamSafeEvaluatorTest {
		
	private static final String XML =
			"<document>"+
			"  <beans>"+
			"    <testBean1>"+
			"      <bean1Name>Bean One #1</bean1Name>"+
			"      <bean2>" +
			"        <seeHowFarTheRabbitHoleGoes><value>20</value></seeHowFarTheRabbitHoleGoes>"+
			"        <bean2Name>Bean Two #1</bean2Name>"+
			"        <redPill reference=\"../../../redPill\"/>"+
			"        <redPill reference=\"../seeHowFarTheRabbitHoleGoes\"/>"+
			"        <redPill><value>30</value></redPill>"+
			"      </bean2>" +
			"    </testBean1>"+
			"    <testBean1>" +
			"      <bean1Name>Bean One #2</bean1Name>" +
			"      <bean2 reference=\"../../testBean1/bean2\"/>" +
			"    </testBean1>" +
			"    <redPill><value>10</value></redPill>" +
			"  </beans>" +
			"</document>";
	
	private static final String XML2 = 
		"<document>"+
		"  <test1 reference=\"../test2\"/>"+
		"  <test2 reference=\"../test3\"/>"+
		"  <test3>test3</test3>"+
		"</document>";

	private static final String XPATH_NO_REF = "//document/beans/testBean1/bean1Name";
	private static final String XPATH_THROUGH_REF = "//document/beans/testBean1/bean2/bean2Name"; 
	private static final String XPATH_RED_PILL = "//document/beans/testBean1/bean2/redPill/value";
	
	private static final String XPATH2_TEST1 = "/document/test1";
	private static final String XPATH2_TEST2 = "//test2";
	private static final String XPATH2_TEST3 = "//document/test3";
	
	private static final String XPATH_GET_FOR_RELATIVE = "/document/beans/testBean1/bean2";
	//private static final String XPATH_GLOBAL_VALUE = ".//value";
	private static final String XPATH_VALUE_20_RELATIVE = "./seeHowFarTheRabbitHoleGoes/value";
	private static final String XPATH_VALUE_10_20_30_RELATIVE = "./redPill/value";
	
	
	private Document document;
	private XStreamSafeEvaluator eval = new XStreamSafeEvaluator();
	private XPath xpath;
	
	/**
	 * Set up an XPath instance using our XPathHelper which should configure the namespace and
	 * WorkflowFunctionResolver for us.
	 */
    @Before
	public void setUp() throws Exception {
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(XML.getBytes()));
		xpath = XPathHelper.newXPath(document);
	}
	
	@Test public void testEvaluation() throws Exception {
		// test against straight xpath first
		NodeList nodeList = (NodeList)xpath.evaluate(XPATH_NO_REF, document, XPathConstants.NODESET);
		assertEquals("Should find 2 nodes.", 2, nodeList.getLength());
		nodeList = (NodeList)xpath.evaluate(XPATH_THROUGH_REF, document, XPathConstants.NODESET);
		assertEquals("Should find 1 nodes.", 1, nodeList.getLength());
		
		// test against our evaluator, it should be able to follow our reference path
		nodeList = eval.evaluate(XPATH_NO_REF, document);
		assertEquals("Should find 2 nodes.", 2, nodeList.getLength());
		nodeList = eval.evaluate(XPATH_THROUGH_REF, document);
		assertEquals("Should find 2 nodes.", 2, nodeList.getLength());
		
		// now test our evaluator exposed as an XPath function
		nodeList = (NodeList)xpath.evaluate(wrapXStreamSafe(XPATH_NO_REF), document, XPathConstants.NODESET);
		assertEquals("Should find 2 nodes.", 2, nodeList.getLength());
		nodeList = (NodeList)xpath.evaluate(wrapXStreamSafe(XPATH_THROUGH_REF), document, XPathConstants.NODESET);
		assertEquals("Should find 2 nodes.", 2, nodeList.getLength());
		
		// now test a bit more complicated XML
		nodeList = (NodeList)xpath.evaluate(XPATH_RED_PILL, document, XPathConstants.NODESET);
		assertEquals("Without XStream safe evaulation, should only find 1 node.", 1, nodeList.getLength());
		System.out.println("\n\n\n\n");
		nodeList = (NodeList)xpath.evaluate(wrapXStreamSafe(XPATH_RED_PILL), document, XPathConstants.NODESET);
		assertEquals("Should have 6 nodes.", 6, nodeList.getLength());
		String totalValue = xpath.evaluate("sum("+wrapXStreamSafe(XPATH_RED_PILL)+")", document);
		assertEquals("Sum should be 120.", "120", totalValue);
	}
	
	/**
	 * Tests the case where the last node that resulted from the evaulation of the xpath has a "reference" attribute
	 * on it.  I found a bug where this last "reference" attribute was not being evaluated and resolved to the
	 * proper node.
	 * 
	 * This method also does some testing of chaining of reference nodes (i.e. a node has a reference to a node
	 * which has a reference to another node).
	 */
	@Test public void testTerminalReferences() throws Exception {
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(XML2.getBytes()));
		xpath = XPathHelper.newXPath(document);
		
		// test against straight xpath first
		NodeList nodeList = (NodeList)xpath.evaluate(XPATH2_TEST1, document, XPathConstants.NODESET);
		assertEquals("There should be one node.", 1, nodeList.getLength());
		assertEquals("The first node should be named 'test1'", "test1", nodeList.item(0).getNodeName());
		assertEquals("The node should have no children.", 0, nodeList.item(0).getChildNodes().getLength());
		assertNotNull("The node should have a reference attribute.", nodeList.item(0).getAttributes().getNamedItem("reference"));
		
		String test3Value = xpath.evaluate(XPATH2_TEST3, document);
		assertEquals("test3", test3Value);
		
		// test using the xstreamsafe function
		String test1Value = xpath.evaluate(wrapXStreamSafe(XPATH2_TEST1), document);
		assertEquals("test3", test1Value);
		
		String test2Value = xpath.evaluate(wrapXStreamSafe(XPATH2_TEST2), document);
		assertEquals("test3", test2Value);
		
		test3Value = xpath.evaluate(wrapXStreamSafe(XPATH2_TEST3), document);
		assertEquals("test3", test3Value);
	}
	
	/**
	 * Tests the usage of XPath expressions which are evaluated relative to a particular context (i.e. starts with a ".").
	 * This tests the resolution to EN-100.
	 */
	@Test public void testContextRelativeExpressions() throws Exception {
		// drill down in the document to an element which we will use as our context
		NodeList nodeList = (NodeList)xpath.evaluate(XPATH_GET_FOR_RELATIVE, document, XPathConstants.NODESET);
		assertEquals("There should be two nodes.", 2, nodeList.getLength());
		// run the same xpath through the xstreamsafe evaluator
		nodeList = (NodeList)xpath.evaluate(wrapXStreamSafe(XPATH_GET_FOR_RELATIVE), document, XPathConstants.NODESET);
		assertEquals("There should be two nodes.", 2, nodeList.getLength());
		
		// now drill down using the first node in the list as the context
		Node node = nodeList.item(0);
		// reconstruct the XPath instance for the given root node
		xpath = XPathHelper.newXPath(node);
		// look for <value> elements, there should be two
		
		// TODO we currently can't support xpath expressions that start with .//  When we can, uncomment this test code.
//		nodeList = (NodeList)xpath.evaluate(XPATH_GLOBAL_VALUE, node, XPathConstants.NODESET);
//		assertEquals("There should be two value elements.", 2, nodeList.getLength());
//		// evaluate as xstreamsafe
//		nodeList = (NodeList)xpath.evaluate(wrapXStreamSafe(XPATH_GLOBAL_VALUE), node, XPathConstants.NODESET);
//		assertEquals("There should be three value elements now.", 3, nodeList.getLength());
		
		// try to find the value inside of redpill section
		String value = xpath.evaluate(XPATH_VALUE_20_RELATIVE, node);
		assertEquals("20", value);
		// do with xstreamsafe
		value = xpath.evaluate(wrapXStreamSafe(XPATH_VALUE_20_RELATIVE), node);
		assertEquals("20", value);
				
		// test non xstreamsafe, should only be 1 node
		nodeList = (NodeList)xpath.evaluate(XPATH_VALUE_10_20_30_RELATIVE, node, XPathConstants.NODESET);
		assertEquals("NodeList should have 1 elements.", 1, nodeList.getLength());
	
		// test with xstreamsafe, should be 3 nodes
		nodeList = (NodeList)xpath.evaluate(wrapXStreamSafe(XPATH_VALUE_10_20_30_RELATIVE), node, XPathConstants.NODESET);
		assertEquals("NodeList should have 3 elements.", 3, nodeList.getLength());
	}
	
	private String wrapXStreamSafe(String xPathExpression) {
		return "wf:xstreamsafe('"+xPathExpression+"')";
	}
	
}
