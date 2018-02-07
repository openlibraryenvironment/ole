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
package org.kuali.rice.kew.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.test.KEWTestCase;

/**
 * Test case that tests setting and getting variables, both programmatically
 * and via the "SetVar" node; stolen directly from ApproveActionTest.testPreapprovals
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class VariablesTest extends KEWTestCase {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(VariablesTest.class);

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

    private void dumpInfoAboutDoc(WorkflowDocument doc) throws WorkflowException {
        LOG.info("\tDoc: class=" + doc.getDocumentTypeName() + " title=" + doc.getTitle() + " status=" + doc.getStatus());
        LOG.info("\tActionRequests:");
        for (ActionRequest ar: doc.getRootActionRequests()) {
            LOG.info("\t\tId: " + ar.getId() + " PrincipalId: " + ar.getPrincipalId() + " ActionRequested: " + ar.getActionRequested() + " ActionTaken: " + (ar.getActionTaken() != null ? ar.getActionTaken().getActionTaken() : null) + " NodeName: " + ar.getNodeName() + " Status:" + ar.getStatus());
        }
        LOG.info("\tActionTakens:");
        for (ActionTaken at: doc.getActionsTaken()) {
            LOG.info("\t\tId: " + at.getId() + " PrincipalId: " + at.getPrincipalId() + " ActionTaken: " + at.getActionTaken());
        }
        LOG.info("\tNodeNames:");
        for (String name: doc.getNodeNames()) {
            LOG.info("\t\t" + name);
        }
    }

    public void dumpBranch(Branch b) {
        LOG.info("Branch: " + b.getBranchId() + " " + b.getName());
        for (BranchState bs: b.getBranchState()) {
            LOG.info(bs.getBranchStateId() + " " + bs.getKey() + " " + bs.getValue());
        }
    }

    @Test public void testVariables() throws Exception {
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "VariablesTest");
        doc.route("");

        //rock some preapprovals and other actions...
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), doc.getDocumentId());
        dumpInfoAboutDoc(doc);
        doc.setVariable("myexcellentvariable", "righton");
        doc.approve("");
        assertEquals("startedVariableValue", doc.getVariableValue("started"));
        assertEquals("startedVariableValue", doc.getVariableValue("copiedVar"));

        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), doc.getDocumentId());
        assertEquals("righton", doc.getVariableValue("myexcellentvariable"));
        doc.setVariable("vartwo", "two");
        doc.setVariable("myexcellentvariable", "ichangedit");
        doc.acknowledge("");

        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user3"), doc.getDocumentId());
        assertEquals("ichangedit", doc.getVariableValue("myexcellentvariable"));
        assertEquals("two", doc.getVariableValue("vartwo"));
        doc.setVariable("another", "another");
        doc.setVariable("vartwo", null);
        doc.complete("");

        //approve as the person the doc is routed to so we can move the documen on and hopefully to final
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), doc.getDocumentId());
        assertEquals("ichangedit", doc.getVariableValue("myexcellentvariable"));
        assertEquals(null, doc.getVariableValue("vartwo"));
        assertEquals("another", doc.getVariableValue("another"));
        doc.approve("");

        assertEquals("endedVariableValue", doc.getVariableValue("ended"));
        assertNotNull(doc.getVariableValue("google"));
        LOG.info(doc.getVariableValue("google"));
        assertEquals("documentContentendedVariableValue", doc.getVariableValue("xpath"));
        LOG.info(doc.getVariableValue("xpath"));

        assertEquals("aNewStartedVariableValue", doc.getVariableValue("started"));

        assertTrue("the document should be final", doc.isFinal());
    }
}
