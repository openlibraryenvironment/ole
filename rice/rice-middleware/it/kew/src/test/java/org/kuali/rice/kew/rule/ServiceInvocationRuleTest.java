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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.test.FakeService;
import org.kuali.rice.kew.test.FakeServiceImpl.Invocation;
import org.kuali.rice.kew.test.KEWTestCase;


/**
 * Tests that a groovy expression in a rule can invoke an arbitrary service on the KSB 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ServiceInvocationRuleTest extends KEWTestCase {
    protected void loadTestData() throws Exception {
        loadXmlFile("ServiceInvokingRule.xml");
    }

    @Test public void testServiceInvokingRule() throws WorkflowException {
        // test that we can get the service to start with
        FakeService fakeService = (FakeService) GlobalResourceLoader.getService(new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, "fakeService-remote"));
        assertNotNull(fakeService);
        
        
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("arh14"), "ServiceInvocationRuleTest");
        doc.route("routing");

        // no requests whatsoever were sent...we're done
        assertTrue(doc.isFinal());
        
        fakeService = (FakeService) GlobalResourceLoader.getService(new QName(KewApiConstants.Namespaces.KEW_NAMESPACE_2_0, "fakeService-remote"));
        
        assertEquals(1, fakeService.getInvocations().size());
        Invocation invocation = fakeService.getInvocations().get(0);
        assertEquals("method2", invocation.methodName);
    }
}
