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
package org.kuali.rice.kew.rule.xmlrouting;

import org.junit.Test;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.rule.RuleExtension;
import org.kuali.rice.kew.docsearch.xml.StandardGenericXMLSearchableAttribute;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.exception.WorkflowServiceError;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.routeheader.StandardDocumentContent;
import org.kuali.rice.kew.rule.RuleExtensionBo;
import org.kuali.rice.kew.rule.RuleExtensionValue;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StandardGenericXMLRuleAttributeTest extends KEWTestCase {

	private DocumentContent docContent;
	private StandardGenericXMLRuleAttribute attribute;
	private List<RuleExtension> extensions;

	public void setUp() throws Exception {
        super.setUp();
		attribute = new StandardGenericXMLRuleAttribute();
		String documentcontent =
			"<documentContent>" +
				"<attributeContent>"+
					"<xmlContent>"+
						"<fieldDef name=\"givenname\">"+
							"<value>Dave</value>"+
		                                "</fieldDef>"+
						"<fieldDef name=\"gender\">"+
							"<value>female</value>"+
						"</fieldDef>"+
						"<fieldDef name=\"color\">"+
							"<value>green</value>"+
						"</fieldDef>"+
						"<fieldDef name=\"totalDollar\">"+
							"<value>500</value>"+
						"</fieldDef>"+
					"</xmlContent>"+
					"<xmlContent>"+
						"<fieldDef name=\"givenname\">"+
							"<value>Jane</value>"+
		                                "</fieldDef>"+
						"<fieldDef name=\"gender\">"+
							"<value>female</value>"+
						"</fieldDef>"+
						"<fieldDef name=\"color\">"+
							"<value>blue</value>"+
						"</fieldDef>"+
						"<fieldDef name=\"totalDollar\">"+
							"<value>400</value>"+
						"</fieldDef>"+
					"</xmlContent>"+
				"</attributeContent>"+
                            "</documentContent>";

		String routingConfig =
			"<routingConfig>"+
				"<globalEvaluations>" +
					"<xpathexpression>//fieldDef/value != 'Nothing'</xpathexpression>" +
				"</globalEvaluations>"+

				"<fieldDef name=\"givenname\" title=\"First name\" workflowType=\"ALL\">"+
					"<value>Joe</value>"+
					"<display>"+
						"<type>text</type>"+
						"<meta><name>size</name><value>20</value></meta>"+
					"</display>" +
					"<validation required=\"true\">"+
						"<regex>^[a-zA-Z ]+$</regex>"+
						"<message>Invalid first name</message>"+
        			"</validation>"+
					"<fieldEvaluation><xpathexpression>//xmlContent/fieldDef[@name='givenname']/value = wf:ruledata('givenname')</xpathexpression></fieldEvaluation>"+
				"</fieldDef>"+
				"<fieldDef name=\"gender\" title=\"Gender\" workflowType=\"ALL\">"+
					"<value>male</value>"+
					"<display>"+
						"<type>radio</type>"+
						"<values title=\"Male\">male</values>"+
						"<values title=\"Female\">female</values>"+
					"</display>"+
			        "<validation required=\"true\">" +
		        		"<regex>(male|female)</regex>"+
		        		"<message>Invalid gender</message>"+
		        	"</validation>"+
					"<fieldEvaluation><xpathexpression>//xmlContent/fieldDef[@name='gender']/value = wf:ruledata('gender')</xpathexpression></fieldEvaluation>"+
				"</fieldDef>"+
				"<fieldDef name=\"color\" title=\"Color\" workflowType=\"ALL\">" +
					"<value>blue</value>" +
					"<display>" +
						"<type>select</type>" +
						"<values title=\"Red\">red</values>" +
						"<values title=\"Green\">green</values>" +
						"<values title=\"Blue\" selected=\"true\">blue</values>" +
					"</display>" +
					"<validation required=\"true\">"+
			        	"<regex>(red|green|blue|)</regex>"+
			        	"<message>Invalid color</message>"+
			        "</validation>"+
					"<fieldEvaluation><xpathexpression>//xmlContent/fieldDef[@name='color']/value = wf:ruledata('color')</xpathexpression></fieldEvaluation>"+
				"</fieldDef>"+
				"<fieldDef name=\"maxDollar\" title=\"Max dollar\" workflowType=\"RULE\">" +
					"<display>" +
						"<type>text</type>" +
					"</display>" +
					"<fieldEvaluation><xpathexpression>//xmlContent/fieldDef[@name='totalDollar']/value &lt;= wf:ruledata('maxDollar')</xpathexpression></fieldEvaluation>"+
				"</fieldDef>"+
				"<fieldDef name=\"minDollar\" title=\"Min dollar\" workflowType=\"RULE\">" +
					"<display>" +
						"<type>text</type>" +
					"</display>" +
					"<fieldEvaluation><xpathexpression>//xmlContent/fieldDef[@name='totalDollar']/value &gt;= wf:ruledata('minDollar')</xpathexpression></fieldEvaluation>"+
			    "</fieldDef>"+
				"<fieldDef name=\"totalDollar\" title=\"Total dollar\" workflowType=\"REPORT\">" +
					"<display>" +
						"<type>text</type>" +
					"</display>" +
		        "</fieldDef>" +
            "</routingConfig>";

        // stub the RouteContext - it is needed for xpath caching optimization
        RouteContext ctx = new RouteContext();

		docContent = new StandardDocumentContent(documentcontent, ctx);

		RuleAttribute ruleAttribute = new RuleAttribute();
		ruleAttribute.setXmlConfigData(routingConfig);
		ruleAttribute.setName("MyUniqueRuleAttribute1");
        ruleAttribute.setType("SearchableXmlAttribute");
        ruleAttribute.setResourceDescriptor(StandardGenericXMLSearchableAttribute.class.getName());
		attribute.setExtensionDefinition(RuleAttribute.to(ruleAttribute));
	}

	@Test public void testValidateRoutingData(){
        Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("givenname", "Dave");
		paramMap.put("gender", "female");
		paramMap.put("color", "green");
		paramMap.put("totalDollar", "500");

		assertTrue("Errors found", attribute.validateRoutingData(paramMap).isEmpty());
	}

	@Test public void testValidateRuleData(){
        Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("givenname", "Dave");
		paramMap.put("gender", "female");
		paramMap.put("color", "green");
		paramMap.put("totalDollar", "500");

		assertTrue("Errors found", attribute.validateRuleData(paramMap).isEmpty());

		paramMap = new HashMap<String, String>();
		paramMap.put("givenname", "4444");
		paramMap.put("gender", "crap");
		paramMap.put("color", "green");
		paramMap.put("totalDollar", "500");

        assertFalse("Error list should contain at least one error.", attribute.validateRuleData(paramMap).isEmpty());

		paramMap = new HashMap<String, String>();
		paramMap.put("givenname", "");
		paramMap.put("gender", "female");
		paramMap.put("color", "green");
		paramMap.put("totalDollar", "500");

		assertFalse("givenname should be required.", attribute.validateRuleData(paramMap).isEmpty());
	}

    @Test public void testRuleDataAttributeErrorTypesAreConformant() {
        Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("givenname", "4444");
		paramMap.put("gender", "crap");
		paramMap.put("color", "green");
		paramMap.put("totalDollar", "500");

		List<RemotableAttributeError> errors = attribute.validateRuleData(paramMap);
        assertFalse("Error list should contain at least one error.", errors.isEmpty());
        for (Object e: errors) {
            assertTrue(RemotableAttributeError.class.isAssignableFrom(e.getClass()));
        }
    }

    @Test public void testRoutingDataAttributeErrorTypesAreConformant(){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("givenname", "4444");
        paramMap.put("gender", "crap");
        paramMap.put("color", "green");
        paramMap.put("totalDollar", "500");

        List<RemotableAttributeError> errors = attribute.validateRoutingData(paramMap);
        assertFalse("Error list should contain at least one error.", errors.isEmpty());
        for (Object e: errors) {
            assertTrue(RemotableAttributeError.class.isAssignableFrom(e.getClass()));
        }
    }

    /**
     * Tests SGXA attribute matching behavior with extension value keys that do not necessarily match
     * a field defined in the attribute
     */
    @Test public void testNonMatchingExtensionKey() throws WorkflowException {
        loadXmlFile("TestExtensionValueMatching.xml");
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("arh14"), "TestDocument");

        WorkflowAttributeDefinition.Builder attr = WorkflowAttributeDefinition.Builder.create(StandardGenericXMLRuleAttribute.class.getName());
        attr.setAttributeName("Attr1");
        // expected to match RuleTemplate with extension value: value='1' with xpath expression /xmlRouting/field[@name=attr1] = wf:ruledata('value')
        attr.addPropertyDefinition("attr1", "2");
        doc.addAttributeDefinition(attr.build());

        doc.route("");

        String id = doc.getDocumentId();

        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), id);
        assertTrue("Request should have been generated to user1", doc.isApprovalRequested());

        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), id);
        assertTrue("Expected approval request to user2", doc.isApprovalRequested());
    }

	/*
	 * Test method for 'org.kuali.rice.kew.rule.xmlrouting.StandardGenericXMLRuleAttribute.isMatch(DocumentContent, List)'
	 */
	@Test public void testIsMatch() {
		RuleExtensionBo extension = new RuleExtensionBo();

		List<RuleExtensionValue> values = new ArrayList<RuleExtensionValue>();
		RuleExtensionValue value = new RuleExtensionValue();
		value.setKey("givenname");
		value.setValue("Dave");
		values.add(value);

		RuleExtensionValue value2 = new RuleExtensionValue();
		value2.setKey("gender");
		value2.setValue("female");
		values.add(value2);

		RuleExtensionValue value3 = new RuleExtensionValue();
		value3.setKey("color");
		value3.setValue("green");
		values.add(value3);

		extension.setExtensionValues(values);
		RuleTemplateAttributeBo ruleTemplateAttribute = new RuleTemplateAttributeBo();

		RuleAttribute ruleAttribute = new RuleAttribute();
		ruleAttribute.setName("MyUniqueRuleAttribute1");
        ruleAttribute.setType("RuleAttribute");
        ruleAttribute.setResourceDescriptor("noClass");

		ruleTemplateAttribute.setRuleAttribute(ruleAttribute);
        ruleTemplateAttribute.setRuleTemplateId("ruleTemplateId1");
        ruleTemplateAttribute.setDisplayOrder(new Integer(1));
		extension.setRuleTemplateAttribute(ruleTemplateAttribute);
		RuleExtensionBo extension2 = new RuleExtensionBo();

		List values2 = new ArrayList();
		RuleExtensionValue valueNew = new RuleExtensionValue();
		valueNew.setKey("givenname");
		valueNew.setValue("Jack");

		RuleExtensionValue minDollar = new RuleExtensionValue();
		minDollar.setKey("minDollar");
		minDollar.setValue("300");
        RuleExtensionValue maxDollar = new RuleExtensionValue();
        maxDollar.setKey("maxDollar");
        maxDollar.setValue("600");


		values2.add(valueNew);
		values2.add(minDollar);
		values2.add(maxDollar);
		extension2.setExtensionValues(values2);
		RuleTemplateAttributeBo ruleTemplateAttribute2 = new RuleTemplateAttributeBo();

		RuleAttribute ruleAttribute2 = new RuleAttribute();
		ruleAttribute2.setName("MyUniqueRuleAttribute2");
        ruleAttribute2.setType("RuleAttribute");
        ruleAttribute2.setResourceDescriptor("noClass");

		ruleTemplateAttribute2.setRuleAttribute(ruleAttribute2);
        ruleTemplateAttribute2.setRuleTemplateId("ruleTemplateId2");
        ruleTemplateAttribute2.setDisplayOrder(new Integer(2));
		extension2.setRuleTemplateAttribute(ruleTemplateAttribute2);

		extensions = new ArrayList();
		extensions.add(RuleExtensionBo.to(extension));
		extensions.add(RuleExtensionBo.to(extension2));

		assertTrue("Givenname did not match Dave, gender did not match female, or color did not match green", attribute.isMatch(docContent, extensions));

		extension = new RuleExtensionBo();
		values = new ArrayList();
		RuleExtensionValue value4 = new RuleExtensionValue();
		value4.setKey("givenname");
		value4.setValue("Dave");
		values.add(value4);

		RuleExtensionValue value5 = new RuleExtensionValue();
		value5.setKey("gender");
		value5.setValue("male");
		values.add(value5);

		RuleExtensionValue value6 = new RuleExtensionValue();
		value6.setKey("color");
		value6.setValue("green");
		values.add(value6);

		extension.setExtensionValues(values);
		ruleTemplateAttribute = new RuleTemplateAttributeBo();

		ruleAttribute = new RuleAttribute();
		ruleAttribute.setName("MyUniqueRuleAttribute1");
        ruleAttribute.setType("RuleAttribute");
        ruleAttribute.setResourceDescriptor("noClass");


		ruleTemplateAttribute.setRuleAttribute(ruleAttribute);
        ruleTemplateAttribute.setRuleTemplateId("ruleTemplateId");
        ruleTemplateAttribute.setDisplayOrder(new Integer(1));
		extension.setRuleTemplateAttribute(ruleTemplateAttribute);


		values2 = new ArrayList();
		valueNew = new RuleExtensionValue();
		valueNew.setKey("givenname");
		valueNew.setValue("Jack");

		values2.add(valueNew);
		extension2.setExtensionValues(values2);
		ruleTemplateAttribute2 = new RuleTemplateAttributeBo();

		ruleAttribute2 = new RuleAttribute();
		ruleAttribute2.setName("MyUniqueRuleAttribute2");
        ruleAttribute2.setType("RuleAttribute");
        ruleAttribute2.setResourceDescriptor("noClass");


		ruleTemplateAttribute2.setRuleAttribute(ruleAttribute2);
        ruleTemplateAttribute2.setRuleTemplateId("ruleTemplateId2");
        ruleTemplateAttribute2.setDisplayOrder(new Integer(2));
		extension2.setRuleTemplateAttribute(ruleTemplateAttribute2);

		extensions = new ArrayList();
		extensions.add(RuleExtensionBo.to(extension));
		extensions.add(RuleExtensionBo.to(extension2));
		assertFalse("Gender female != male.", attribute.isMatch(docContent, extensions));

		///////

		extension = new RuleExtensionBo();
		values = new ArrayList();

		RuleExtensionValue value7 = new RuleExtensionValue();
		value7.setKey("maxDollar");
		value7.setValue("500");


		RuleExtensionValue value8 = new RuleExtensionValue();
		value8.setKey("minDollar");
		value8.setValue("100");

        values.add(value7);
		values.add(value8);
		extension.setExtensionValues(values);
		ruleTemplateAttribute = new RuleTemplateAttributeBo();
		ruleAttribute = new RuleAttribute();
		ruleAttribute.setName("MyUniqueRuleAttribute1");
        ruleAttribute.setType("RuleAttribute");
        ruleAttribute.setResourceDescriptor("noClass");

		ruleTemplateAttribute.setRuleAttribute(ruleAttribute);
        ruleTemplateAttribute.setRuleTemplateId("ruleTemplateId");
        ruleTemplateAttribute.setDisplayOrder(new Integer(1));
		extension.setRuleTemplateAttribute(ruleTemplateAttribute);

		values2 = new ArrayList();

		valueNew = new RuleExtensionValue();
		valueNew.setKey("givenname");
		valueNew.setValue("Jack");
		values2.add(valueNew);

		extension2.setExtensionValues(values2);
		ruleTemplateAttribute2 = new RuleTemplateAttributeBo();

		ruleAttribute2 = new RuleAttribute();
		ruleAttribute2.setName("MyUniqueRuleAttribute2");
        ruleAttribute2.setType("RuleAttribute");
        ruleAttribute2.setResourceDescriptor("noClass");


		ruleTemplateAttribute2.setRuleAttribute(ruleAttribute2);
        ruleTemplateAttribute2.setRuleTemplateId("ruleTemplateId2");
        ruleTemplateAttribute2.setDisplayOrder(new Integer(2));
		extension2.setRuleTemplateAttribute(ruleTemplateAttribute2);

		extensions = new ArrayList();
		extensions.add(RuleExtensionBo.to(extension));
		extensions.add(RuleExtensionBo.to(extension2));
		assertTrue("Total dollar is greater than the max or less than the min.", attribute.isMatch(docContent, extensions));
	}

	/*
	 * Test method for 'org.kuali.rice.kew.rule.xmlrouting.StandardGenericXMLRuleAttribute.getRuleRows()'
	 */
	@Test public void testGetRuleRows() {
		assertTrue("Invalid number of rule rows", attribute.getRuleRows().size() == 5);

		String routingConfigWithQuickfinders =
			"<routingConfig>"+
				"<globalEvaluations>" +
					"<xpathexpression>//field/value != 'Nothing'</xpathexpression>" +
				"</globalEvaluations>"+

				"<fieldDef name=\"chart\" title=\"Chart\" workflowType=\"ALL\">"+
					"<value>BL</value>"+
					"<display>"+
						"<type>text</type>"+
						"<meta><name>size</name><value>20</value></meta>"+
					"</display>" +
					"<fieldEvaluation><xpathexpression>//xmlContent/field[@name='chart']/value = wf:ruledata('chart')</xpathexpression></fieldEvaluation>"+
				"</fieldDef>"+
				"<fieldDef name=\"org\" title=\"Org\" workflowType=\"ALL\">"+
					"<display>"+
						"<type>text</type>"+
					"</display>" +
					"<lookup businessObjectClass=\"ChartOrgLookupableImplService\">" +
					    "<fieldConversions>" +
				          "<fieldConversion lookupFieldName=\"fin_coa_cd\" localFieldName=\"chart\"/>" +
					      "<fieldConversion lookupFieldName=\"org_cd\" localFieldName=\"org\"/>" +
					    "</fieldConversions>" +
					"</lookup>" +
					"<fieldEvaluation><xpathexpression>//xmlContent/field[@name='gender']/value = wf:ruledata('gender')</xpathexpression></fieldEvaluation>"+
				"</fieldDef>" +
            "</routingConfig>";

		RuleAttribute ruleAttribute = new RuleAttribute();
		ruleAttribute.setXmlConfigData(routingConfigWithQuickfinders);
		ruleAttribute.setName("MyUniqueRuleAttribute3");
        ruleAttribute.setType("SearchableXmlAttribute");
        ruleAttribute.setResourceDescriptor(StandardGenericXMLSearchableAttribute.class.getName());
		StandardGenericXMLRuleAttribute myAttribute = new StandardGenericXMLRuleAttribute();
		myAttribute.setExtensionDefinition(RuleAttribute.to(ruleAttribute));

		for (Iterator iter = myAttribute.getRuleRows().iterator(); iter.hasNext();) {
			Row row = (Row) iter.next();
			for (Iterator iterator = row.getFields().iterator(); iterator.hasNext();) {
				Field field = (Field) iterator.next();
				if(field.getFieldType().equals(Field.QUICKFINDER)){
					assertTrue("Did not find quickfinder.", true);
				}
			}
		}
		assertTrue("Should have 2 rows and 3 fields: chart, org, and quickfinder.", myAttribute.getRuleRows().size() == 2);
	}

	@Test public void testGetRoutingDataRows() {
		assertTrue("Invalid number of routing data rows",attribute.getRoutingDataRows().size() == 4);
	}

	@Test public void testGetDocContent() {
		//test the standard doc content...
        Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("givenname", "Dave");
		paramMap.put("gender", "female");
		paramMap.put("color", "green");
		paramMap.put("totalDollar", "500");

		attribute.setParamMap(paramMap);
		try{
			XPath xpath = XPathFactory.newInstance().newXPath();
			Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new BufferedReader(new StringReader(attribute.getDocContent())))).getDocumentElement();

			String findStuff = "//xmlRouting/field[@name='givenname']/value";
			assertTrue("Document content does not contain field givenname with value of Dave.", "Dave".equals(xpath.evaluate(findStuff, element, XPathConstants.STRING)));

			findStuff = "//xmlRouting/field[@name='gender']/value";
			assertTrue("Document content does not contain field gender with value of female.", "female".equals(xpath.evaluate(findStuff, element, XPathConstants.STRING)));

			findStuff = "//xmlRouting/field[@name='color']/value";
			assertTrue("Document content does not contain field color with value of green.", "green".equals(xpath.evaluate(findStuff, element, XPathConstants.STRING)));

			findStuff = "//xmlRouting/field[@name='totalDollar']/value";
			assertTrue("Document content does not contain field totalDollar with value of 500.", "500".equals(xpath.evaluate(findStuff, element, XPathConstants.STRING)));
		} catch (Exception e){
			e.printStackTrace();
		}

		// Now test the custom doc content...
		String routingConfig =
			"<routingConfig>" +
				"<fieldDef name=\"myname\" title=\"First name\" workflowType=\"ALL\">"+
					"<value>Joe</value>"+
					"<display>"+
						"<type>text</type>"+
						"<meta><name>size</name><value>20</value></meta>"+
					"</display>"+
					"<fieldEvaluation><xpathexpression>//putWhateverWordsIwantInsideThisTag/myname/value = wf:ruledata('myname')</xpathexpression></fieldEvaluation>"+
				"</fieldDef>"+
				"<fieldDef name=\"theGender\" title=\"Gender\" workflowType=\"ALL\">"+
					"<value>male</value>"+
					"<display>"+
						"<type>radio</type>"+
						"<values title=\"Male\">male</values>"+
						"<values title=\"Female\">female</values>"+
					"</display>"+
					"<fieldEvaluation><xpathexpression>//putWhateverWordsIwantInsideThisTag/theGender/value = wf:ruledata('theGender')</xpathexpression></fieldEvaluation>"+
				"</fieldDef>"+
				"<fieldDef name=\"myFavoriteColor\" title=\"Color\" workflowType=\"ALL\">" +
					"<value>blue</value>" +
					"<display>" +
						"<type>select</type>" +
						"<values title=\"Red\">red</values>" +
						"<values title=\"Green\">green</values>" +
						"<values title=\"Blue\" selected=\"true\">blue</values>" +
					"</display>" +
					"<fieldEvaluation><xpathexpression>//putWhateverWordsIwantInsideThisTag/myFavoriteColor/value = wf:ruledata('myFavoriteColor')</xpathexpression></fieldEvaluation>"+
				"</fieldDef>"+
				"<fieldDef name=\"maxDollar\" title=\"Max dollar\" workflowType=\"RULE\">" +
					"<display>" +
						"<type>text</type>" +
					"</display>" +
					"<fieldEvaluation><xpathexpression>//putWhateverWordsIwantInsideThisTag/myMoney/value &lt;= wf:ruledata('maxDollar')</xpathexpression></fieldEvaluation>"+
				"</fieldDef>"+
				"<fieldDef name=\"minDollar\" title=\"Min dollar\" workflowType=\"RULE\">" +
					"<display>" +
						"<type>text</type>" +
					"</display>" +
					"<fieldEvaluation><xpathexpression>//putWhateverWordsIwantInsideThisTag/myMoney/value &gt;= wf:ruledata('minDollar')</xpathexpression></fieldEvaluation>"+
			    "</fieldDef>"+
				"<fieldDef name=\"myMoney\" title=\"Total dollar\" workflowType=\"REPORT\">" +
					"<display>" +
						"<type>text</type>" +
					"</display>" +
		        "</fieldDef>"+

		        "<xmlDocumentContent>"+
		        	"<putWhateverWordsIwantInsideThisTag>"+
		        		"<myname>"+
		        			"<value>%myname%</value>"+
	        		    "</myname>"+
	        		    "<theGender>"+
	        		    	"<value>%theGender%</value>"+
	    				"</theGender>"+
	    				"<myFavoriteColor>"+
	    					"<value>%myFavoriteColor%</value>"+
						"</myFavoriteColor>"+
		        		"<myMoney>"+
	        				"<value>%myMoney%</value>"+
	        			"</myMoney>"+
					"</putWhateverWordsIwantInsideThisTag>"+
		        "</xmlDocumentContent>"+
            "</routingConfig>";
		try {
			paramMap = new HashMap<String, String>();
			paramMap.put("myname", "jack");
			paramMap.put("theGender", "male");
			paramMap.put("myFavoriteColor", "blue");
			paramMap.put("myMoney", "10");

			attribute.setParamMap(paramMap);

			RuleAttribute ruleAttribute = new RuleAttribute();
			ruleAttribute.setXmlConfigData(routingConfig);
			ruleAttribute.setName("MyUniqueRuleAttribute2");
			attribute.setExtensionDefinition(RuleAttribute.to(ruleAttribute));

			String docContent = attribute.getDocContent();
			assertTrue("DocContent was not found.", docContent != null && docContent.length() > 0);


			XPath xpath = XPathFactory.newInstance().newXPath();
			Element foundDocContent = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new BufferedReader(new StringReader(docContent)))).getDocumentElement();

			String findStuff = "//putWhateverWordsIwantInsideThisTag/myMoney/value";
			assertTrue("Document content does not contain field myMoney with a value of 10.", "10".equals(xpath.evaluate(findStuff, foundDocContent, XPathConstants.STRING)));

			findStuff = "//putWhateverWordsIwantInsideThisTag/myFavoriteColor/value";
			assertTrue("Document content does not contain field myFavoriteColor with value of blue.", "blue".equals(xpath.evaluate(findStuff, foundDocContent, XPathConstants.STRING)));

			findStuff = "//putWhateverWordsIwantInsideThisTag/theGender/value";
			assertTrue("Document content does not contain field theGender with value of male.", "male".equals(xpath.evaluate(findStuff, foundDocContent, XPathConstants.STRING)));

			findStuff = "//putWhateverWordsIwantInsideThisTag/myname/value";
			assertTrue("Document content does not contain field myname with value of jack.", "jack".equals(xpath.evaluate(findStuff, foundDocContent, XPathConstants.STRING)));

		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
