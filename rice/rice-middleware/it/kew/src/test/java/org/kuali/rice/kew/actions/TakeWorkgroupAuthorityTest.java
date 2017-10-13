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

import org.junit.Test;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class TakeWorkgroupAuthorityTest extends KEWTestCase {

    public static final String DOC_TYPE = "TakeWorkgroupAuthorityDoc";
    public static List<String> WORKGROUP_MEMBERS = new ArrayList<String>();

    static {
        WORKGROUP_MEMBERS.add("ewestfal");
        WORKGROUP_MEMBERS.add("rkirkend");
        WORKGROUP_MEMBERS.add("jhopf");
        WORKGROUP_MEMBERS.add("bmcgough");
        WORKGROUP_MEMBERS.add("temay");
        WORKGROUP_MEMBERS.add("xqi");
        WORKGROUP_MEMBERS.add("natjohns");
        WORKGROUP_MEMBERS.add("pmckown");
        WORKGROUP_MEMBERS.add("jthomas");
        WORKGROUP_MEMBERS.add("jitrue");
    }

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

    @Test public void testTakeWorkgroupAuthorityAction() throws Exception {

        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), DOC_TYPE);
        doc.route("");

        String groupId = getGroupIdForName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, "TestWorkgroup");

        //verify that all members have the action item
        ActionListService aiService = KEWServiceLocator.getActionListService();
        Collection<ActionItem> actionItems = aiService.findByDocumentId(doc.getDocumentId());
        assertTrue("There should be more than one action item", actionItems.size() > 1);
        for (Iterator<ActionItem> iter = actionItems.iterator(); iter.hasNext();) {
            ActionItem actionItem = iter.next();
            assertTrue("Action Item not to workgroup member", WORKGROUP_MEMBERS.contains(getPrincipalNameForId(actionItem.getPrincipalId())));
        }

        //have member rkirkend take authority
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), doc.getDocumentId());
        doc.takeGroupAuthority("", groupId);

        //verify that only rkirkend has an action item now.
        actionItems = aiService.findByDocumentId(doc.getDocumentId());
        assertEquals("There should only be a single action item to rkirkend", 1, actionItems.size());
        for (Iterator<ActionItem> iter = actionItems.iterator(); iter.hasNext();) {
            ActionItem actionItem = iter.next();
            assertEquals("Action item should be to rkirkend", "rkirkend", getPrincipalNameForId(actionItem.getPrincipalId()));
        }

        //verify the action was recorded and by rkirkend
        List<ActionTaken> actionsTaken = KewApiServiceLocator.getWorkflowDocumentService().getActionsTaken(
                doc.getDocumentId());
        boolean rkirkendATFound = false;
        for (ActionTaken at : actionsTaken) {
            if (at.getPrincipalId().equals(getPrincipalIdForName("rkirkend"))) {
                assertEquals("Incorrect action code recorded", KewApiConstants.ACTION_TAKEN_TAKE_WORKGROUP_AUTHORITY_CD, at.getActionTaken().getCode());
                rkirkendATFound = true;
            }
        }

        assertTrue("should have found action taken for rkirkend", rkirkendATFound);
    }
}
