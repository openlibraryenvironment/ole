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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.doctype.IllegalDocumentTypeException;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;

public class CreateDocumentTest extends KEWTestCase {

    @Override
    protected void loadTestData() throws Exception {
        loadXmlFile("ActionsConfig.xml");
    }

    /**
     * Tests the attempt to create a document from a non-existent document type.
     */
    @Test(expected=RiceIllegalArgumentException.class)
    public void testCreateNonExistentDocumentType() throws Exception {
        WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "flim-flam-flooey");
    }

    /**
     * Tests the attempt to create a document from a document type with no routing path.
     */
    @Test(expected=IllegalDocumentTypeException.class)
    public void testCreateNonRoutableDocumentType() throws Exception {
        // the BlanketApproveTest is a parent document type that has no routing path defined.  Attempts to
        // create documents of this type should return a DocumentCreationException
        WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "BlanketApproveTest");
    }

    /**
     * Tests the attempt to create a document from a document type with no routing path.
     */
    @Test(expected=IllegalDocumentTypeException.class)
    public void testCreateInactiveDocumentType() throws Exception {
        // the CreatedDocumentInactive document type is inactive and should not be able to 
        // be initiated for a new document
        WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "CreatedDocumentInactive");
    }

    /**
     * Tests creation of a simple document and verifies it's state.
     */
    @Test
    public void testCreateSimpleDocumentType() throws Exception {
        String principalId = getPrincipalIdForName("ewestfal");
        WorkflowDocument workflowDocument = WorkflowDocumentFactory.createDocument(principalId, "TestDocumentType");
        assertNotNull(workflowDocument);

        assertNotNull(workflowDocument.getDocumentId());
        Document document = workflowDocument.getDocument();
        assertNotNull(document);

        assertNotNull(document.getDateCreated());
        assertNotNull(document.getDateLastModified());
        assertNull(document.getDateApproved());
        assertNull(document.getDateFinalized());

        assertEquals("", document.getTitle());
        assertNotNull(document.getDocumentTypeId());
        assertEquals("TestDocumentType", document.getDocumentTypeName());
        assertEquals(principalId, document.getInitiatorPrincipalId());
        assertEquals(DocumentStatus.INITIATED, document.getStatus());

    }

    protected String getPrincipalIdForName(String principalName) {
        return KEWServiceLocator.getIdentityHelperService().getIdForPrincipalName(principalName);
    }
}
