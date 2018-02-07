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

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.PropertyDefinition;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.io.StringReader;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class PrincipalIdRoleAttributeTest extends KEWTestCase {

	private static final String ATTRIBUTE_NAME = "PrincipalIdRoleAttribute";
	private static final String PRINCIPAL_ID_PROP = "principalId";

	@Test
	public void testPrincipalIdAttribute() throws Exception {
		loadXmlFile("PrincipalIdRoleAttributeTestConfig.xml");

		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(
				"ewestfal"), "PrincipalIdRoleAttributeTest");

		WorkflowAttributeDefinition.Builder principalIdDef1 = WorkflowAttributeDefinition.Builder.create(
				"PrincipalIdRoleAttribute");
		PropertyDefinition principalIdProp1 = PropertyDefinition.create(
				PRINCIPAL_ID_PROP, KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("rkirkend").getPrincipalId());
		principalIdDef1.addPropertyDefinition(principalIdProp1);

		WorkflowAttributeDefinition.Builder principalIdDef2 = WorkflowAttributeDefinition.Builder.create(
				"PrincipalIdRoleAttribute");
		PropertyDefinition principalIdProp2 = PropertyDefinition.create(
				PRINCIPAL_ID_PROP, KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("bmcgough").getPrincipalId());
		principalIdDef2.addPropertyDefinition(principalIdProp2);

		document.addAttributeDefinition(principalIdDef1.build());
		document.addAttributeDefinition(principalIdDef2.build());

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
	public void testParameterizedPrincipalIdAttribute() throws Exception {
		loadXmlFile("ParameterizedPrincipalIdRoleAttributeTestConfig.xml");

		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName(
				"ewestfal"), "PrincipalIdRoleAttributeTest");

		WorkflowAttributeDefinition.Builder principalIdDef1 = WorkflowAttributeDefinition.Builder.create(
				"PrincipalIdRoleAttribute");
		PropertyDefinition principalIdProp1 = PropertyDefinition.create(
				PRINCIPAL_ID_PROP, KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("rkirkend").getPrincipalId());
		principalIdDef1.addPropertyDefinition(principalIdProp1);

		document.addAttributeDefinition(principalIdDef1.build());

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
