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
import org.kuali.rice.core.api.uif.RemotableAttributeErrorContract;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.test.KEWTestCase;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests that an attribute implementing WorkflowAttributeXmlValidator interface can be validated from the 
 * client application, including and especially edl.
 * 
 * An attribute that doesn't implement the interface should record no errors when validated.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class AttributeClientRoutingDataValidationTest extends KEWTestCase {

    @Override
    protected void loadTestData() throws Exception {
        loadXmlFile("AttributeClientRoutingDataValidationTest.xml");
    }    
	
	@Test public void testClientApplicationValidationImplementsWorkflowAttributeXmlValidator() throws Exception {
		WorkflowAttributeDefinition attDef = WorkflowAttributeDefinition.Builder.create("TestRuleAttributeThree").build();
        List<? extends RemotableAttributeErrorContract> validationErrors = KewApiServiceLocator.getWorkflowDocumentActionsService().validateWorkflowAttributeDefinition(attDef);
		assertTrue("Validation errors should not be empty", !validationErrors.isEmpty());
		assertEquals("Should be 2 validation errors", 2, validationErrors.size());
		boolean foundKey1 = false;
		boolean foundKey2 = false;
		for (RemotableAttributeErrorContract error : validationErrors) {
			if (error.getAttributeName().equals("key1")) {
				assertEquals("key1 key should have message of value1", "value1", error.getMessage());
				foundKey1 = true;
			} else if (error.getAttributeName().equals("key2")) {
				assertEquals("key2 key should have message of value2", "value2", error.getMessage());
				foundKey2 = true;
			}
		}
		
		assertTrue("should have found a key1 error", foundKey1);
		assertTrue("should have found a key2 error", foundKey2);
	}
	
	@Test public void testClientApplicationValidationNoImplementsWorkflowAttributeXmlValidator() throws Exception {
		WorkflowAttributeDefinition attDef = WorkflowAttributeDefinition.Builder.create("TestRuleAttributeDuex").build();
		List<? extends RemotableAttributeErrorContract> validationErrors = KewApiServiceLocator.getWorkflowDocumentActionsService().validateWorkflowAttributeDefinition(attDef);
		assertTrue("Validation errors should be empty because WorkflowAttributeXmlValidator interface is not implemented", validationErrors.isEmpty());
	}
	
	@Test public void testThrowWorkflowExceptionNoneExistentAttribute() throws Exception {
        WorkflowAttributeDefinition attDef = WorkflowAttributeDefinition.Builder.create("FakeyMcAttribute").build();
		try {
            KewApiServiceLocator.getWorkflowDocumentActionsService().validateWorkflowAttributeDefinition(attDef);
			fail("Should have thrown WorkflowException attempting to lookup non-existent attribute");
		} catch (WorkflowRuntimeException e) {
			assertTrue("This is the correct exception to throw", true);
		}
	}
}
