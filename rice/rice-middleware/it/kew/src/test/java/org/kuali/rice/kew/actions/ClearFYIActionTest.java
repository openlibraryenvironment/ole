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
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author delyea
 */
public class ClearFYIActionTest extends KEWTestCase {

    private String getSavedStatusDisplayValue() {
        return (String) KewApiConstants.DOCUMENT_STATUSES.get(KewApiConstants.ROUTE_HEADER_SAVED_CD);
    }

    @Test public void testSavedDocumentAdhocRequest() throws Exception {
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "TestDocumentType");
        doc.saveDocument("");
        doc.adHocToPrincipal(ActionRequestType.FYI, "annotation1", getPrincipalIdForName("dewey"), "respDesc1", false);
        String userId = getPrincipalIdForName("dewey");
        doc = WorkflowDocumentFactory.loadDocument(userId, doc.getDocumentId());
        assertTrue("FYI should be requested of user " + userId, doc.isFYIRequested());
        try {
            doc.fyi();
        } catch (Exception e) {
            fail("A non-initator with an FYI request should be allowed to take the FYI action on a " + getSavedStatusDisplayValue() + " document");
        }
        assertTrue("Document should be " + getSavedStatusDisplayValue(), doc.isSaved());

        String workgroupUserId = getPrincipalIdForName("dewey");
        doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "TestDocumentType");
        doc.saveDocument("");

        doc.adHocToGroup(ActionRequestType.FYI, "annotation1", getGroupIdForName(KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE, "NonSIT"), "respDesc1", false);
        doc = WorkflowDocumentFactory.loadDocument(workgroupUserId, doc.getDocumentId());
        assertTrue("FYI should be requested of user " + workgroupUserId, doc.isFYIRequested());
        try {
            doc.fyi();
        } catch (Exception e) {
            fail("A non-initator with an FYI request should be allowed to take the FYI action on a " + getSavedStatusDisplayValue() + " document");
        }
        assertTrue("Document should be " + getSavedStatusDisplayValue(), doc.isSaved());
    }
}
