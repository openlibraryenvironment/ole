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
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class UniversityIdRoleAttributeTest extends KEWTestCase {
    
    private static final String UNIVERSITY_ID_PROP = "universityId";
    
    @Test
    public void testUniversityIdAttribute() throws Exception {
	loadXmlFile("UniversityIdRoleAttributeTestConfig.xml");
	
	// network id to university id mapping as defined in DefaultSuiteTestData.xml
	// -----------------------------------
	// ewestfal     ->     2001
	// rkirkend     ->     2002
	// bmcgough     ->     2004
	
	WorkflowDocument document = WorkflowDocumentFactory.createDocument("2001", "UniversityIdRoleAttributeTest");
	
	WorkflowAttributeDefinition.Builder universityIdDef1 = WorkflowAttributeDefinition.Builder.create("UniversityIdRoleAttribute");
	PropertyDefinition universityIdProp1 = PropertyDefinition.create(UNIVERSITY_ID_PROP, "2002");
	universityIdDef1.addPropertyDefinition(universityIdProp1);
	
	WorkflowAttributeDefinition.Builder universityIdDef2 = WorkflowAttributeDefinition.Builder.create("UniversityIdRoleAttribute");
	PropertyDefinition universityIdProp2 = PropertyDefinition.create(UNIVERSITY_ID_PROP, "2004");
	universityIdDef2.addPropertyDefinition(universityIdProp2);
	
	document.addAttributeDefinition(universityIdDef1.build());
	document.addAttributeDefinition(universityIdDef2.build());
	
	document.route("Routing!");
	
	// load the document as rkirkend
	
	document = WorkflowDocumentFactory.loadDocument("2002", document.getDocumentId());
	assertTrue("Document should be ENROUTE", document.isEnroute());
	assertTrue("rkirkend should have an approve request.", document.isApprovalRequested());
	
	// load the document as bmcgough
	document = WorkflowDocumentFactory.loadDocument("2004", document.getDocumentId());
	assertTrue("bmcgough should have an approve request.", document.isApprovalRequested());
	
	// submit an approve as bmcgough
	document.approve("i approve");
	
	// reload as rkirkend, verify still enroute
	document = WorkflowDocumentFactory.loadDocument("2002", document.getDocumentId());
	assertTrue("Document should be ENROUTE", document.isEnroute());
	assertTrue("rkirkend should have an approve request.", document.isApprovalRequested());
	document.approve("i also approve");
	
	// now the document should be FINAL
	assertTrue("Document should be FINAL", document.isFinal());
	
    }
    
    @Test
    public void testParameterizedUniversityIdAttribute() throws Exception {
	loadXmlFile("ParameterizedUniversityIdRoleAttributeTestConfig.xml");
	
	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "UniversityIdRoleAttributeTest");
	
	WorkflowAttributeDefinition.Builder univIdDef1 = WorkflowAttributeDefinition.Builder.create("UniversityIdRoleAttribute");
	PropertyDefinition univIdProp1 = PropertyDefinition.create(UNIVERSITY_ID_PROP, "2002");
	univIdDef1.addPropertyDefinition(univIdProp1);
		
	document.addAttributeDefinition(univIdDef1.build());
	
	document.route("Routing!");
	
	// load the document as rkirkend
	
	document = WorkflowDocumentFactory.loadDocument("2002", document.getDocumentId());
	assertTrue("Document should be ENROUTE", document.isEnroute());
	assertTrue("rkirkend should have an approve request.", document.isApprovalRequested());
	
	// now let's verify the XML
	
	XPath xPath = XPathHelper.newXPath();
	assertTrue("Should have found the ID.", (Boolean)xPath.evaluate("//UniversityIdRoleAttribute/thisIdRocks", new InputSource(new StringReader(document.getDocumentContent().getFullContent())), XPathConstants.BOOLEAN));
	
    }

}
