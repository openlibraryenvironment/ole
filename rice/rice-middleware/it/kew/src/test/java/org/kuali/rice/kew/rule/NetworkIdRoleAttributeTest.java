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
package org.kuali.rice.kew.rule;

import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.PropertyDefinition;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.kew.test.KEWTestCase;
import org.xml.sax.InputSource;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class NetworkIdRoleAttributeTest extends KEWTestCase {

	private static final String ATTRIBUTE_NAME = "NetworkIdRoleAttribute";
	private static final String NETWORK_ID_PROP = "networkId";

	@Test
	public void testNetworkIdAttribute() throws Exception {
		loadXmlFile("NetworkIdRoleAttributeTestConfig.xml");

		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(
				"ewestfal"), "NetworkIdRoleAttributeTest");

		WorkflowAttributeDefinition.Builder networkIdDef1 = WorkflowAttributeDefinition.Builder.create("NetworkIdRoleAttribute");
		PropertyDefinition networkIdProp1 = PropertyDefinition.create(
				NETWORK_ID_PROP, "rkirkend");
		networkIdDef1.addPropertyDefinition(networkIdProp1);

		WorkflowAttributeDefinition.Builder networkIdDef2 = WorkflowAttributeDefinition.Builder.create(
				"NetworkIdRoleAttribute");
		PropertyDefinition networkIdProp2 = PropertyDefinition.create(
				NETWORK_ID_PROP, "bmcgough");
		networkIdDef2.addPropertyDefinition(networkIdProp2);

		document.addAttributeDefinition(networkIdDef1.build());
		document.addAttributeDefinition(networkIdDef2.build());

		document.route("Routing!");

		// load the document as rkirkend

		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document
				.getDocumentId());
		assertTrue("Document should be ENROUTE", document.isEnroute());
		assertTrue("rkirkend should have an approve request.", document
				.isApprovalRequested());

		// load the document as bmcgough
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document
				.getDocumentId());
		assertTrue("bmcgough should have an approve request.", document
				.isApprovalRequested());

		// submit an approve as bmcgough
		document.approve("i approve");

		// reload as rkirkend, verify still enroute
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document
				.getDocumentId());
		assertTrue("Document should be ENROUTE", document.isEnroute());
		assertTrue("rkirkend should have an approve request.", document
				.isApprovalRequested());
		document.approve("i also approve");

		// now the document should be FINAL
		assertTrue("Document should be FINAL", document.isFinal());

	}

	@Test
	public void testParameterizedNetworkIdAttribute() throws Exception {
		loadXmlFile("ParameterizedNetworkIdRoleAttributeTestConfig.xml");

		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(
				"ewestfal"), "NetworkIdRoleAttributeTest");

		WorkflowAttributeDefinition.Builder networkIdDef1 = WorkflowAttributeDefinition.Builder.create(
				"NetworkIdRoleAttribute");
		PropertyDefinition networkIdProp1 = PropertyDefinition.create(
				NETWORK_ID_PROP, "rkirkend");
		networkIdDef1.addPropertyDefinition(networkIdProp1);

		document.addAttributeDefinition(networkIdDef1.build());

		document.route("Routing!");

		// load the document as rkirkend

		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document
				.getDocumentId());
		assertTrue("Document should be ENROUTE", document.isEnroute());
		assertTrue("rkirkend should have an approve request.", document
				.isApprovalRequested());

		// now let's verify the XML

		XPath xPath = XPathHelper.newXPath();
		assertTrue("Should have found the ID.", (Boolean) xPath.evaluate(
				"//" + ATTRIBUTE_NAME + "/thisIdRocks", new InputSource(
						new StringReader(document.getDocumentContent()
								.getFullContent())), XPathConstants.BOOLEAN));

	}

}
