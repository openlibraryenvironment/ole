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
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kim.api.KimConstants;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ReleaseWorkgroupAuthorityTest extends KEWTestCase {

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

    @Test public void testReleaseWorkgroupAuthority() throws Exception {
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), TakeWorkgroupAuthorityTest.DOC_TYPE);
        doc.route("");

        String groupId = getGroupIdForName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, "TestWorkgroup");
        //have member rkirkend take authority
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), doc.getDocumentId());
        doc.takeGroupAuthority("", groupId);

        // verify there is only one action item and that it's to rkirkend
        ActionListService aiService = KEWServiceLocator.getActionListService();
        Collection<ActionItem> actionItems = aiService.findByDocumentId(doc.getDocumentId());
        assertEquals("There should be only one action item", 1, actionItems.size());
        ActionItem ai = (ActionItem)actionItems.iterator().next();
        assertEquals("action item should be to rkirkend", getPrincipalIdForName("rkirkend"), ai.getPrincipalId());
        assertEquals("action item should be to group", groupId, ai.getGroupId());
        
        //have rkirkend release authority
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), doc.getDocumentId());

        doc.releaseGroupAuthority("", groupId);

        //verify that all members have the action item
        actionItems = aiService.findByDocumentId(doc.getDocumentId());
        assertTrue("There should be more than one action item", actionItems.size() > 1);
        for (Iterator<ActionItem> iter = actionItems.iterator(); iter.hasNext();) {
            ActionItem actionItem = iter.next();
            assertTrue("Action Item not to workgroup member", TakeWorkgroupAuthorityTest.WORKGROUP_MEMBERS.contains(getPrincipalNameForId(actionItem.getPrincipalId())));
        }
    }
}
