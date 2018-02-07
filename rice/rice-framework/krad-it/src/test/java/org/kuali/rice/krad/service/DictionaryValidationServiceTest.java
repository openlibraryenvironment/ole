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
package org.kuali.rice.krad.service;

import org.junit.Test;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.test.document.AccountRequestDocument;
import org.kuali.rice.krad.test.document.AccountRequestDocumentWithCyclicalReference;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the DictionaryValidationService (currently only recursive validation is tested).
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DictionaryValidationServiceTest extends KRADTestCase {

    public DictionaryValidationServiceTest() {
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setMessageMap(new MessageMap());
        GlobalVariables.setUserSession(new UserSession("quickstart"));
    }

    @Override
    public void tearDown() throws Exception {
        GlobalVariables.setMessageMap(new MessageMap());
        GlobalVariables.setUserSession(null);
        super.tearDown();
    }

    /**
     * This method tests recursive validation at a depth of zero
     *
     * @throws Exception
     */
    @Test public void testRecursiveValidation() throws Exception {
        AccountRequestDocument travelDocument = (AccountRequestDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument("AccountRequest");
        // set all required fields except 1
        travelDocument.getDocumentHeader().setDocumentDescription("test document");
        travelDocument.setReason1("reason1");
        travelDocument.setReason2("reason2");
        travelDocument.setRequester("requester");

        GlobalVariables.setMessageMap(new MessageMap());
        KRADServiceLocatorWeb.getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(travelDocument, 0, true);
        MessageMap errorMap = GlobalVariables.getMessageMap();
        int recursiveZeroMessageMapSize = errorMap.getNumberOfPropertiesWithErrors();

        // errors should be 'account type code' and 'request type' both being required
        assertEquals("Number of errors found is incorrect", 2, recursiveZeroMessageMapSize);
    }

    /**
     * This method tests recursive validation comparing multiple levels of recursion
     *
     * @throws Exception
     */
    @Test public void testRecursiveValidationMultiple() throws Exception {
        AccountRequestDocument travelDocument = (AccountRequestDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument("AccountRequest");
        // set all required fields except 1
        travelDocument.getDocumentHeader().setDocumentDescription("test document");
        travelDocument.setReason1("reason1");
        travelDocument.setReason2("reason2");
        travelDocument.setRequester("requester");

        GlobalVariables.setMessageMap(new MessageMap());
        KRADServiceLocatorWeb.getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(travelDocument, 0, true);
        MessageMap errorMap = GlobalVariables.getMessageMap();
        int recursiveZeroMessageMapSize = errorMap.getNumberOfPropertiesWithErrors();

        GlobalVariables.setMessageMap(new MessageMap());
        KRADServiceLocatorWeb.getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(travelDocument, 5, true);
        MessageMap errorMap2 = GlobalVariables.getMessageMap();
        int recursiveFiveMessageMapSize = errorMap2.getNumberOfPropertiesWithErrors();

        assertEquals("We should get the same number of errors no matter how deeply we recursively validate for this document", recursiveZeroMessageMapSize, recursiveFiveMessageMapSize);
    }

    @Test public void testRecursiveValidationParentChildLoop() throws Exception {
        AccountRequestDocumentWithCyclicalReference doc1 = (AccountRequestDocumentWithCyclicalReference) KRADServiceLocatorWeb.getDocumentService().getNewDocument("AccountRequest3");
        // set all required fields except 1
        doc1.getDocumentHeader().setDocumentDescription("test document 1");
        doc1.setReason1("reason1");
        doc1.setReason2("reason2");
        doc1.setRequester("requester");

        AccountRequestDocumentWithCyclicalReference doc2 = (AccountRequestDocumentWithCyclicalReference) KRADServiceLocatorWeb.getDocumentService().getNewDocument("AccountRequest3");
        doc2.getDocumentHeader().setDocumentDescription("test document 2");
        doc2.setReason1("reason1a");
        doc2.setReason2("reason2a");
        doc2.setRequester("requester2");
        doc2.setParent(doc1);
        doc1.setChild(doc2);

        GlobalVariables.setMessageMap(new MessageMap());
        KRADServiceLocatorWeb.getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(doc1, 5, true);
        MessageMap errorMap = GlobalVariables.getMessageMap();
        int recursiveFiveMessageMapSize = errorMap.getNumberOfPropertiesWithErrors();

        GlobalVariables.setMessageMap(new MessageMap());
        KRADServiceLocatorWeb.getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(doc1, 10, true);
        MessageMap errorMap2 = GlobalVariables.getMessageMap();
        int recursiveTenMessageMapSize = errorMap2.getNumberOfPropertiesWithErrors();

        assertEquals("We should get the same number of errors no matter how deeply we recursively validate for this document", recursiveFiveMessageMapSize, recursiveTenMessageMapSize);
    }
    
}
