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
package org.kuali.rice.krad.service.impl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.impl.document.WorkflowDocumentImpl;
import org.kuali.rice.krad.UserSession;
import org.mockito.Mockito;


import java.util.concurrent.*;


/**
 * Copyright 2005-2012 The Kuali Foundation
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
@Ignore //SessionDocumentServiceImpl has been Deprecated and removed from use in KRAD
public class SessionDocumentServiceImplTest {

    // Generate sample objects for mocks and testing
    private SessionDocumentServiceImpl sessionDocumentServiceImpl = new SessionDocumentServiceImpl();
    private UserSession session;
    private WorkflowDocument doc;
    private String docId = "1";
    private ConcurrentHashMap<String, WorkflowDocument> map = new ConcurrentHashMap<String, WorkflowDocument>();

    @Before
    public void setUp() {
        session = Mockito.mock(UserSession.class);
        doc = Mockito.mock(WorkflowDocumentImpl.class);
        Mockito.when(doc.getDocumentId()).thenReturn(docId);
        map.put(docId,doc);
    }

    @Test
    @Ignore //SessionDocumentServiceImpl has been Deprecated and removed from use in KRAD
    public void testAddDocumentToUserSession() {
        try {
            sessionDocumentServiceImpl.addDocumentToUserSession(session, doc);
        } catch(Exception e) {
            Assert.fail("Exception occurred adding document to user session");
        }
    }
    @Test
    @Ignore //SessionDocumentServiceImpl has been Deprecated and removed from use in KRAD
    public void testGetDocumentFromSessionWithEntry() {
        try {
            Mockito.when(session.retrieveObject(KewApiConstants.WORKFLOW_DOCUMENT_MAP_ATTR_NAME)).thenReturn(map);
            WorkflowDocument returnedDoc = sessionDocumentServiceImpl.getDocumentFromSession(session,docId);
            Assert.assertEquals("Document should match mock document", doc, returnedDoc);
        } catch(Exception e) {
            Assert.fail("Exception occurred retrieving document to user session");
        }
    }

    @Test
    @Ignore //SessionDocumentServiceImpl has been Deprecated and removed from use in KRAD
    public void testGetDocumentFromSessionWithNoEntry() {
        try {
            Mockito.when(session.retrieveObject(KewApiConstants.WORKFLOW_DOCUMENT_MAP_ATTR_NAME)).thenReturn(null);
            WorkflowDocument returnedDoc = sessionDocumentServiceImpl.getDocumentFromSession(session,docId);
            Assert.assertNull("Document should have returned null", returnedDoc);
        } catch(Exception e) {
            Assert.fail("Exception occurred retrieving document to user session");
        }
    }

}

