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
import org.kuali.rice.kew.actions.BlanketApproveTest.NotifySetup;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class LogDocumentActionTest extends KEWTestCase {

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

    @Test public void testLogAnnotation() throws Exception {
        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), NotifySetup.DOCUMENT_TYPE_NAME);
        Collection actionsTaken = KEWServiceLocator.getActionTakenService().getActionsTaken(document.getDocumentId());
        assertEquals(0, actionsTaken.size());
        
        document.logAnnotation("going to route doc");
        actionsTaken = KEWServiceLocator.getActionTakenService().getActionsTaken(document.getDocumentId());
        assertEquals(1, actionsTaken.size());
        ActionTakenValue actionTaken = (ActionTakenValue) actionsTaken.iterator().next();
        assertTrue(actionTaken.getCurrentIndicator());
    }
}