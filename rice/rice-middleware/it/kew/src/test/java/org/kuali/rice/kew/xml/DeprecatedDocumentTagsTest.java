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

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.rule.RuleResponsibility;
import org.kuali.rice.kew.docsearch.DocumentSearchTestBase;
import org.kuali.rice.kew.docsearch.xml.StandardGenericXMLSearchableAttribute;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.util.FieldUtils;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This test case just ensures that deprecated document type XML elements can still be parsed properly. When the deprecated elements are eventually
 * removed from the system, this test case and its related XML file should be removed as well.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DeprecatedDocumentTagsTest extends DocumentSearchTestBase {
    private static final Logger LOG = Logger.getLogger(DeprecatedDocumentTagsTest.class);
    
    private static final String TEST_GROUP_NAME = "TestWorkgroup";
    
    protected void loadTestData() throws Exception {
        ConfigContext.getCurrentContextConfig().putProperty("test.doctype.workgroup", TEST_GROUP_NAME);
        loadXmlFile("DeprecatedDocumentTags.xml");
    }
    
    /**
     * Tests if the deprecated group-related elements that are direct children of the "documentType" element (such as "superUserWorkgroupName",
     * "blanketApproveWorkgroupName", etc.) are still being parsed and assigned properly. However, "defaultExceptionWorkgroupName" cannot be
     * found in that particular spot because it is not stored on the doc type (see KEW's DocumentType class for details).
     * 
     * @throws Exception
     */
    @Test
    public void testDocTypeDirectDescendantWorkgroupNames() throws Exception {
    	// Ensure that the document type called "DocumentType02" has the correct groups defined after parsing.
    	DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName("DocumentType02");
    	assertGroupIsCorrect("superUserWorkgroupName", TEST_GROUP_NAME, "KR-WKFLW", docType.getSuperUserWorkgroup());
    	assertGroupIsCorrect("blanketApproveWorkgroupName", "TestWorkgroup", "KR-WKFLW", docType.getBlanketApproveWorkgroup());
    	// Ensure that the document type called "DocTypeWithSecurity" has the correct groups defined after parsing.
    	docType = KEWServiceLocator.getDocumentTypeService().findByName("DocTypeWithSecurity");
    	assertGroupIsCorrect("superUserWorkgroupName", "TestWorkgroup", "KR-WKFLW", docType.getSuperUserWorkgroup());
    	assertGroupIsCorrect("reportingWorkgroupName", "NonSIT", "KR-WKFLW", docType.getReportingWorkgroup());
    	assertGroupIsCorrect("blanketApproveWorkgroupName", "TestWorkgroup", "KR-WKFLW", docType.getBlanketApproveWorkgroup());
    	// Ensure that the document type called "SeqDocType" has the correct groups defined after parsing.
    	docType = KEWServiceLocator.getDocumentTypeService().findByName("SeqDocType");
    	assertGroupIsCorrect("superUserWorkgroupName", "TestWorkgroup", "KR-WKFLW", docType.getSuperUserWorkgroup());
    	assertGroupIsCorrect("blanketApproveWorkgroupName", "NonSIT", "KR-WKFLW", docType.getBlanketApproveWorkgroup());
    }
    
    /**
     * Tests if the deprecated "workgroup" elements on the "security" and "responsibility" tags are being parsed and assigned properly.
     * Also tests the functionality of the "isMemberOfWorkgroup" element on rule attributes.
     * 
     * @throws Exception
     */
    @Test
    public void testDocTypeSecurityAndResponsibilityAndVisibilityWorkgroupNames() throws Exception {
    	// Ensure that the document type called "DocumentType02" has the correct group defined for its responsibility on "TestRule1".
    	RuleBaseValues testRule = KEWServiceLocator.getRuleService().getRuleByName("TestRule1");
    	assertNotNull("TestRule1 should not be null", testRule);
    	assertEquals("There should be exactly one responsibility on TestRule1", 1, testRule.getRuleResponsibilities().size());
    	RuleResponsibilityBo testResp = testRule.getRuleResponsibilities().get(0);
    	assertNotNull("The responsibility on TestRule1 should not be null", testResp);
    	assertEquals("The responsibility on TestRule1 has the wrong type", KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID, testResp.getRuleResponsibilityType());
    	Group testGroup = KimApiServiceLocator.getGroupService().getGroup(testResp.getRuleResponsibilityName());
    	assertGroupIsCorrect("<responsibility><workgroup>", "TestWorkgroup", "KR-WKFLW", testGroup);
    	// Ensure that the "DocumentType02" document type has a properly-working "isMemberOfWorkgroup" element on its attribute.

        String[] testPrincipalNames = {"rkirkend", "quickstart"};
        boolean[] visibleStates = {true, false};
        // Make sure that the rule attribute is visible for "rkirkend" but not for "quickstart".
        for (int i = 0; i < testPrincipalNames.length; i++) {
        	LOG.info("Testing visibility of the rule attribute for user '" + testPrincipalNames[i] + "'");
        	// Set up a new KEW UserSession in a manner similar to that of WorkflowInfoTest.
            GlobalVariables.setUserSession(null);
            String testPrincipalId = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(testPrincipalNames[i]).getPrincipalId();
            GlobalVariables.setUserSession(new UserSession(testPrincipalNames[i]));
            // Get the visibility of the rule attribute, which should be dependent on how the UserSession compares with the "isMemberOfWorkgroup" element.
            StandardGenericXMLSearchableAttribute searchableAttribute = new StandardGenericXMLSearchableAttribute();
            ExtensionDefinition ed = createExtensionDefinition("SearchableAttributeVisible");
            List<RemotableAttributeField> remotableAttributeFields = searchableAttribute.getSearchFields(ed, "DocumentType02");
            List<Row> rowList = FieldUtils.convertRemotableAttributeFields(remotableAttributeFields);
            assertEquals("The searching rows list should have exactly one element", 1, rowList.size());
            assertEquals("The searching row should have exactly one field", 1, rowList.get(0).getFields().size());
            assertEquals("The rule attribute field does not have the expected visibility", visibleStates[i],
                    rowList.get(0).getField(0).isColumnVisible());
        }
    	// Ensure that the document type called "DocTypeWithSecurity" has the correct group defined for its <security> section.
    	DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName("DocTypeWithSecurity");
    	List<Group> testGroups = docType.getDocumentTypeSecurity().getWorkgroups();
    	assertEquals("docTypeSecurity should have exactly one group in its security section", 1, testGroups.size());
    	assertGroupIsCorrect("<security><workgroup>", "NonSIT", "KR-WKFLW", testGroups.get(0));
    }

    /**
     * Tests if the deprecated "defaultExceptionWorkgroupName", "exceptionWorkgroupName", and "exceptionWorkgroup" elements are still being
     * parsed and utilized properly.
     * 
     * @throws Exception
     */
    @Test
    public void testDeprecatedExceptionWorkgroupNameElements() throws Exception {
    	DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName("SeqDocType");
    	RouteNode rtNode = docType.getPrimaryProcess().getInitialRouteNode();
    	String[] descriptiveNames = {"defaultExceptionWorkgroupName", "exceptionWorkgroupName", "defaultExceptionWorkgroupName", "exceptionWorkgroup"};
    	String[] expectedNames = {"TestWorkgroup", "NonSIT", "TestWorkgroup" , "NonSIT"};
    	String[] expectedNamespaces = {"KR-WKFLW", "KR-WKFLW", "KR-WKFLW", "KR-WKFLW"};
    	int i = 0;
    	// Verify that the exception groups, whether default or explicit, are being set properly.
    	assertTrue("No exception group was defined for node " + rtNode.getRouteNodeName(), rtNode.isExceptionGroupDefined());
		assertGroupIsCorrect(descriptiveNames[i], expectedNames[i], expectedNamespaces[i], rtNode.getExceptionWorkgroup());
    	while (!rtNode.getNextNodes().isEmpty()) {
    		i++;
    		rtNode = rtNode.getNextNodes().get(0);
    		assertTrue("No exception group was defined for node " + rtNode.getRouteNodeName(), rtNode.isExceptionGroupDefined());
    		assertGroupIsCorrect(descriptiveNames[i], expectedNames[i], expectedNamespaces[i], rtNode.getExceptionWorkgroup());
    	}
    	assertEquals("Final route node index is incorrect", 3, i);
    }
    
    /**
     * Verifies that a particular group has the expected values.
     * 
     * @param propertyName The group-related property being tested (this parameter is just descriptive text for use in generating error messages).
     * @param expectedName The expected name of the group.
     * @param expectedNamespace The expected namespace of the group.
     * @param groupToTest The actual Group object being tested.
     * @throws Exception
     */
    private void assertGroupIsCorrect(String propertyName, String expectedName, String expectedNamespace, Group groupToTest) throws Exception {
    	assertNotNull(propertyName + " should not be null", groupToTest);
    	assertEquals(propertyName + " has wrong namespace code", expectedNamespace, groupToTest.getNamespaceCode());
    	assertEquals(propertyName + " has wrong group name", expectedName, groupToTest.getName());
    }
}
