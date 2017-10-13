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
package org.kuali.rice.kew.engine.node.hierarchyrouting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.engine.node.hierarchyrouting.HierarchyProvider.Stop;
import org.kuali.rice.kew.engine.node.hierarchyrouting.SimpleHierarchyProvider.SimpleStop;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Tests HeirarchyRoutingNode
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class HierarchyRoutingNodeTest extends KEWTestCase {

    private static final String HIERARCHY =
    "<stop id=\"root\" type=\"user\" recipient=\"dewey\">" +
      "<stop id=\"child1\" type=\"user\" recipient=\"user3\">" +
        "<stop id=\"child1-1\" type=\"user\" recipient=\"user2\"/>" +
        "<stop id=\"child1-2\" type=\"user\" recipient=\"user1\"/>" +
      "</stop>" +
      "<stop id=\"child2\" type=\"user\" recipient=\"quickstart\">" +
        "<stop id=\"child2-1\" type=\"user\" recipient=\"temay\"/>" +
        "<stop id=\"child2-2\" type=\"user\" recipient=\"jhopf\"/>" +
      "</stop>" +
    "</stop>";
    private static final String HIERARCHY_UPDATED =
    "<stop id=\"root\" type=\"user\" recipient=\"dewey\">" +
      "<stop id=\"child1\" type=\"user\" recipient=\"user3\">" +
        "<stop id=\"child1-1\" type=\"user\" recipient=\"user2\"/>" +
        "<stop id=\"child1-2\" type=\"user\" recipient=\"user1\"/>" +
        "<stop id=\"child1-3\" type=\"user\" recipient=\"delyea\"/>" +
      "</stop>" +
      "<stop id=\"child2\" type=\"user\" recipient=\"quickstart\">" +
        "<stop id=\"child2-1\" type=\"user\" recipient=\"temay\"/>" +
        "<stop id=\"child2-2\" type=\"user\" recipient=\"jhopf\"/>" +
        "<stop id=\"child2-3\" type=\"user\" recipient=\"pzhang\"/>" +
      "</stop>" +
      "<stop id=\"child3\" type=\"user\" recipient=\"shenl\"/>" +
    "</stop>";

    protected void assertStop(HierarchyProvider provider, String name, String parentName, String[] childNames) {
        Stop stop = provider.getStopByIdentifier(name);
        assertNotNull(stop);
        if (parentName == null) {
            assertNull(provider.getParent(stop));
        } else {
            Stop parent = provider.getStopByIdentifier(parentName);
            assertNotNull(parent);
            assertEquals(parent, ((SimpleStop) stop).parent);
        }
        assertEquals(childNames.length, ((SimpleStop) stop).children.size());
        List<SimpleStop> children = ((SimpleStop) stop).children;
        for (String childName: childNames) {
            Stop child = provider.getStopByIdentifier(childName);
            assertTrue(children.contains(child));
        }
    }

    @Test
    public void testParseHierarchy() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(HIERARCHY)));
        SimpleHierarchyProvider provider = new SimpleHierarchyProvider();
        provider.init(doc.getDocumentElement());
        
        assertStop(provider, "root", null, new String[] { "child1", "child2" });
        
        assertStop(provider, "child1", "root", new String[] { "child1-1", "child1-2" });
        assertStop(provider, "child1-1", "child1", new String[] { });
        assertStop(provider, "child1-2", "child1", new String[] { });
        
        assertStop(provider, "child2", "root", new String[] { "child2-1", "child2-2" });
        assertStop(provider, "child2-1", "child2", new String[] { });
        assertStop(provider, "child2-2", "child2", new String[] { });
        
        List<Stop> leaves = provider.getLeafStops(null);
        assertEquals(4, leaves.size());
        assertTrue(leaves.contains(provider.getStopByIdentifier("child1-1")));
        assertTrue(leaves.contains(provider.getStopByIdentifier("child1-2")));
        assertTrue(leaves.contains(provider.getStopByIdentifier("child2-1")));
        assertTrue(leaves.contains(provider.getStopByIdentifier("child2-2")));
    }

    @Test
    public void testHierarchyRoutingNode() throws WorkflowException {
        loadXmlFile("HierarchyRoutingNodeConfig.xml");
        
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("arh14"), "HierarchyDocType");
        
        
        doc.setApplicationContent(HIERARCHY);
        doc.route("initial route");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user2", "user1", "temay", "jhopf" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), doc.getDocumentId());
        doc.approve("approving as user2");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "temay", "jhopf" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user2", "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), doc.getDocumentId());
        doc.approve("approving as jhopf");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "temay" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "jhopf", "user2", "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), doc.getDocumentId());
        doc.approve("approving as user1");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "temay" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "jhopf", "user2", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), doc.getDocumentId());
        doc.approve("approving as temay");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "quickstart" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "temay", "user1", "jhopf", "user2", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user3"), doc.getDocumentId());
        doc.approve("approving as user3");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "quickstart" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "temay", "user1", "jhopf", "user2", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("quickstart"), doc.getDocumentId());
        doc.approve("approving as quickstart");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "dewey" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "temay", "user1", "jhopf", "user2", "quickstart" }, false);
        

        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("dewey"), doc.getDocumentId());
        doc.approve("approving as dewey");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "dewey", "user3", "temay", "user1", "jhopf", "user2", "quickstart" }, false);
        
        assertTrue(doc.isFinal());
    }
    
    @Test
    public void testHierarchyRoutingNodeUnevenApproval() throws WorkflowException {
        loadXmlFile("HierarchyRoutingNodeConfig.xml");
        
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("arh14"), "HierarchyDocType");
        
        doc.setApplicationContent(HIERARCHY);
        doc.route("initial route");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user2", "user1", "temay", "jhopf" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), doc.getDocumentId());
        doc.approve("approving as user2");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "temay", "jhopf" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user2", "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), doc.getDocumentId());
        doc.approve("approving as jhopf");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "temay" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "jhopf", "user2", "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), doc.getDocumentId());
        doc.approve("approving as user1");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "temay" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "jhopf", "user2", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user3"), doc.getDocumentId());
        doc.approve("approving as user3");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "temay" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "user1", "jhopf", "user2", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), doc.getDocumentId());
        doc.approve("approving as temay");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "quickstart" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "temay", "user1", "jhopf", "user2", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("quickstart"), doc.getDocumentId());
        doc.approve("approving as quickstart");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "dewey" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "temay", "user1", "jhopf", "user2", "quickstart" }, false);

        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("dewey"), doc.getDocumentId());
        doc.approve("approving as dewey");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "dewey", "user3", "temay", "user1", "jhopf", "user2", "quickstart" }, false);
        
        assertTrue(doc.isFinal());
    }
    
    @Test
    public void testHierarchyRoutingNodeUnevenApprovalExtraStops() throws WorkflowException {
        loadXmlFile("HierarchyRoutingNodeConfig.xml");
        
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("arh14"), "HierarchyDocType");
        
        doc.setApplicationContent(HIERARCHY);
        doc.route("initial route");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user2", "user1", "temay", "jhopf" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), doc.getDocumentId());
        doc.approve("approving as user2");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "temay", "jhopf" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user2", "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), doc.getDocumentId());
        doc.approve("approving as jhopf");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "temay" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "jhopf", "user2", "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), doc.getDocumentId());
        doc.setApplicationContent(HIERARCHY_UPDATED);
        doc.approve("approving as user1");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "temay", "delyea", "pzhang", "shenl" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "jhopf", "user2", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user3"), doc.getDocumentId());
        doc.approve("approving as user3");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "temay", "delyea", "pzhang", "shenl" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "user1", "jhopf", "user2", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("temay"), doc.getDocumentId());
        doc.approve("approving as temay");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "delyea", "pzhang", "shenl" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "temay", "user1", "jhopf", "user2", "dewey", "quickstart" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("delyea"), doc.getDocumentId());
        doc.approve("approving as delyea");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "pzhang", "shenl" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "delyea", "temay", "user1", "jhopf", "user2", "quickstart", "dewey" }, false);

        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user3"), doc.getDocumentId());
        doc.approve("approving as user3");

        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "pzhang", "shenl" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "delyea", "temay", "user1", "jhopf", "user2", "quickstart", "dewey" }, false);

        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("pzhang"), doc.getDocumentId());
        doc.approve("approving as pzhang");

        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "quickstart", "shenl" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "pzhang", "delyea", "temay", "user1", "jhopf", "user2", "dewey" }, false);

        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("quickstart"), doc.getDocumentId());
        doc.approve("approving as quickstart");

        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "shenl" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "pzhang", "delyea", "temay", "user1", "jhopf", "user2", "quickstart", "dewey" }, false);

        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("shenl"), doc.getDocumentId());
        doc.approve("approving as shenl");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "dewey" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "pzhang", "delyea", "temay", "user1", "jhopf", "user2", "quickstart", "shenl" }, false);

        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("dewey"), doc.getDocumentId());
        doc.approve("approving as dewey");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "shenl", "dewey", "pzhang", "delyea", "user3", "temay", "user1", "jhopf", "user2", "quickstart" }, false);
        
        assertTrue(doc.isFinal());
    }

    @Test
    public void testHierarchyRoutingNodeUnevenApprovalDisapprove() throws WorkflowException {
        loadXmlFile("HierarchyRoutingNodeConfig.xml");
        
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("arh14"), "HierarchyDocType");
        
        doc.setApplicationContent(HIERARCHY);
        doc.route("initial route");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user2", "user1", "temay", "jhopf" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), doc.getDocumentId());
        doc.approve("approving as user2");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "temay", "jhopf" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user2", "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), doc.getDocumentId());
        doc.approve("approving as jhopf");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "temay" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "jhopf", "user2", "user3", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), doc.getDocumentId());
        doc.approve("approving as user1");
        
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "temay" }, true);
        TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user1", "jhopf", "user2", "quickstart", "dewey" }, false);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user3"), doc.getDocumentId());
        doc.disapprove("disapproving as user3");
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("arh14"), doc.getDocumentId());

        //TestUtilities.assertApprovals(doc.getDocumentId(), new String[] { "user3", "temay", "user1", "jhopf", "user2", "quickstart", "dewey" }, false);

        assertTrue(doc.isDisapproved());
 
        TestUtilities.logActionRequests(doc.getDocumentId());

        // these are ok, these are the ACKs for the previous approvers
        int numActionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(doc.getDocumentId()).size();
        assertEquals("Incorrect number of action requests", 4, numActionRequests);
        int numActionItems = KEWServiceLocator.getActionListService().findByDocumentId(doc.getDocumentId()).size();
        assertEquals("Incorrect number of action items.", 4, numActionItems);
        
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), doc.getDocumentId());
        doc.acknowledge("acknowledging disapproval as user2");
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("jhopf"), doc.getDocumentId());
        doc.acknowledge("acknowledging disapproval as jhopf");
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), doc.getDocumentId());
        doc.acknowledge("acknowledging disapproval as user1");
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("arh14"), doc.getDocumentId());
        doc.acknowledge("acknowledging disapproval as arh14");
        
        assertTrue(doc.isDisapproved());

        numActionRequests = KEWServiceLocator.getActionRequestService().findPendingByDoc(doc.getDocumentId()).size();
        assertEquals("Incorrect number of action requests", 0, numActionRequests);
        numActionItems = KEWServiceLocator.getActionListService().findByDocumentId(doc.getDocumentId()).size();
        assertEquals("Incorrect number of action items.", 0, numActionItems);
        
    }
}
