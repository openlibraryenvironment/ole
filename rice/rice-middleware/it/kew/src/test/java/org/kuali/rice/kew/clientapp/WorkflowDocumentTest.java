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
package org.kuali.rice.kew.clientapp;

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;
import org.kuali.rice.kew.test.KEWTestCase;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Place to test WorkflowDocument.
 *
 */
public class WorkflowDocumentTest extends KEWTestCase {

    protected void loadTestData() throws Exception {
        loadXmlFile("ClientAppConfig.xml");
    }

    @Test public void testDirtyContent() {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "UnitTestDocument");
        document.setApplicationContent("application content");
        document.setAttributeContent("attribute content");
        document.setSearchableContent("searchable content");
        assertEquals("application content", document.getApplicationContent());
        assertEquals("application content", document.getDocumentContent().getApplicationContent());
        assertEquals("attribute content", document.getAttributeContent());
        assertEquals("attribute content", document.getDocumentContent().getAttributeContent());
        assertEquals("searchable content", document.getDocumentContent().getSearchableContent());
    }

    @Test public void testLoadNonExistentDocument() throws Exception {
    	try {
    		WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), "123456789");
    		fail("load of non-existent document should have thrown IllegalArgumentException");
    	} catch (IllegalArgumentException e) {}
    }

    @Test public void testWorkflowDocument() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "UnitTestDocument");
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        document.approve("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        document.approve("");

        boolean containsInitiated = false;
        boolean containsTemplate1 = false;
        boolean containsTemplate2 = false;
        for (RouteNodeInstance routeNodeInstance : document.getRouteNodeInstances()) {
            if (routeNodeInstance.getName().equals("Initiated")) {
                containsInitiated = true;
            } else if (routeNodeInstance.getName().equals("Template1")) {
                containsTemplate1 = true;
            } else if (routeNodeInstance.getName().equals("Template2")) {
                containsTemplate2 = true;
            }
        }

        assertTrue("Should have gone through initiated node", containsInitiated);
        assertTrue("Should have gone through template1 node", containsTemplate1);
        assertTrue("Should have gone through template2 node", containsTemplate2);
    }

    /**
     * Test that the document is being updated appropriately after a return to previous call
     *
     * @throws Exception
     */
    @Test public void testReturnToPreviousCorrectlyUpdatingDocumentStatus() throws Exception {

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "UnitTestDocument");
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        document.returnToPreviousNode("", "Initiated");

        assertFalse("ewestfal should no longer have approval status", document.isApprovalRequested());
        assertFalse("ewestfal should no long have blanket approve status", document.isBlanketApproveCapable());

        //just for good measure
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
        assertTrue("rkirkend should now have an approve request", document.isApprovalRequested());
    }

    @Test public void testGetPreviousRouteNodeNames() throws Exception {

    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "UnitTestDocument");
        document.route("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        document.approve("");

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        List<String> previousNodeNames = document.getPreviousNodeNames();
        assertEquals("Should have 2 previous Node Names", 2, previousNodeNames.size());
        assertEquals("Last node name should be the first visisted", "Initiated", previousNodeNames.get(0));
        assertEquals("First node name should be last node visited", "Template1", previousNodeNames.get(1));
        Set<String> currentNodes = document.getNodeNames();
        assertEquals("Should have 1 current node name", 1, currentNodes.size());
        assertEquals("Current node name incorrect", "Template2", currentNodes.iterator().next());
        document.returnToPreviousNode("", "Template1");
        previousNodeNames = document.getPreviousNodeNames();
        assertEquals("Should have 1 previous Node Name", 1, previousNodeNames.size());
        assertEquals("Previous Node name incorrect", "Initiated", previousNodeNames.get(0));

    }

    @Test public void testIsRouteCapable() throws Exception {

    	WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "UnitTestDocument");

    	verifyIsRouteCapable(false, getPrincipalIdForName("ewestfal"), doc.getDocumentId());
    	verifyIsRouteCapable(false, "2001", doc.getDocumentId());

    	verifyIsRouteCapable(true, getPrincipalIdForName("rkirkend"), doc.getDocumentId());
    	verifyIsRouteCapable(true, "2002", doc.getDocumentId());

        doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "NonInitiatorCanRouteDocument");

        verifyIsRouteCapable(true, getPrincipalIdForName("ewestfal"), doc.getDocumentId());
        verifyIsRouteCapable(true, "2001", doc.getDocumentId());

        verifyIsRouteCapable(true, getPrincipalIdForName("rkirkend"), doc.getDocumentId());
        verifyIsRouteCapable(true, "2002", doc.getDocumentId());
    }

    private void verifyIsRouteCapable(boolean routeCapable, String userId, String docId) throws Exception {
    	WorkflowDocument doc = WorkflowDocumentFactory.loadDocument(userId, docId);
    	assertEquals(routeCapable, doc.isRouteCapable());
    }

}
