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
package org.kuali.rice.krad.test;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import static org.junit.Assert.fail;

/**
 *  BaseMaintenanceDocumentTest is a base class for testing maintenance documents
 *
 *  <p>It provides test methods for setting up, editing, saving and copying a maintenance document</p>
 *
 *  @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class BaseMaintenanceDocumentTest extends KRADTestCase {
    private MaintenanceDocument document;
    private String documentTypeName;
    private String initiatorPrincipalName;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setUserSession(new UserSession(getInitiatorPrincipalName()));
        try {
            MaintenanceDocument maintenanceDocument =
                (MaintenanceDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument(getDocumentTypeName());
            maintenanceDocument.getDocumentHeader().setDocumentDescription("test maintenance document");
            setDocument(maintenanceDocument);
        } catch (org.kuali.rice.krad.datadictionary.exception.UnknownDocumentTypeException udte) {
            if (udte.getMessage().contains("AccountManagerMaintenanceDocument")) {
                fail("CI failure - https://jira.kuali.org/browse/KULRICE-9285 " + udte.getMessage() + " "  +ExceptionUtils.getStackTrace(udte));
            }
        }
    }

    @Before
    public void setUpBeforeTest() {
        GlobalVariables.getMessageMap().clearErrorMessages();
    }

    /**
     * Override this method to provide different value
     *
     * @return the document type name to use
     */
    protected String getDocumentTypeName() {
        return documentTypeName;
    }

    /**
     * Override this method to provide different initiator
     *
     * @return the principal name to use as the initiator for the specified document type
     */
    protected String getInitiatorPrincipalName() {
        return initiatorPrincipalName;
    }

    /**
     *  setup a new maintenance document
     *
     * @param document - the maintenance document being tested
     */
    protected void setupNewAccountMaintDoc(MaintenanceDocument document) {

        Object am = getNewMaintainableObject();

        document.getOldMaintainableObject().setDataObject(null);
        document.getOldMaintainableObject().setDataObjectClass(am.getClass());
        document.getNewMaintainableObject().setDataObject(am);
        document.getNewMaintainableObject().setDataObjectClass(am.getClass());

        document.getNewMaintainableObject().setMaintenanceAction(KRADConstants.MAINTENANCE_NEW_ACTION);
    }

    /**
     *
     * @return an object to set as the new maintainable object
     */
    protected abstract Object getNewMaintainableObject();

    /**
     *
     * @return an object to set as the old maintainable object
     */
    protected abstract Object getOldMaintainableObject();

    /**
     * populate maintenance document with objects for editing
     *
     * @param document - the maintenance document being tested
     */
    protected void setupEditAccountMaintDoc(MaintenanceDocument document) {

        Object newAm = getNewMaintainableObject();
        Object oldAm = getOldMaintainableObject();

        document.getOldMaintainableObject().setDataObject(oldAm);
        document.getOldMaintainableObject().setDataObjectClass(oldAm.getClass());
        document.getNewMaintainableObject().setDataObject(newAm);
        document.getNewMaintainableObject().setDataObjectClass(newAm.getClass());

        document.getNewMaintainableObject().setMaintenanceAction(KRADConstants.MAINTENANCE_EDIT_ACTION);
    }

    @Test
    /**
     * test creating a new maintenance document
     */
    public void test_NewDoc() {

        setupNewAccountMaintDoc(getDocument());

        Assert.assertEquals("Document should indicate New.", true, getDocument().isNew());
        Assert.assertEquals("Document should not indicate Edit.", false, getDocument().isEdit());
        Assert.assertEquals("Old BO should not be present.", false, getDocument().isOldDataObjectInDocument());
    }

    @Test
    /**
     * test editing a maintenance document
     */
    public void test_EditDoc() {

        setupEditAccountMaintDoc(getDocument());

        Assert.assertEquals("Document should not indicate New.", false, getDocument().isNew());
        Assert.assertEquals("Document should indicate Edit.", true, getDocument().isEdit());
        Assert.assertEquals("Old BO should be present.", true, getDocument().isOldDataObjectInDocument());

    }

    @Test
    /**
     * test copying a maintenance document
     */
    public void test_CopyDoc() {

        setupEditAccountMaintDoc(getDocument());
        getDocument().getNewMaintainableObject().setMaintenanceAction(KRADConstants.MAINTENANCE_COPY_ACTION);

        Assert.assertEquals("Document should indicate New.", true, getDocument().isNew());
        Assert.assertEquals("Document should not indicate Edit.", false, getDocument().isEdit());
        Assert.assertEquals("Old BO should be present.", true, getDocument().isOldDataObjectInDocument());

    }

    @Test
    /**
     * test saving a maintenance document
     */
    public void test_SaveNewDoc() throws WorkflowException {
        setupNewAccountMaintDoc(getDocument());
        KRADServiceLocatorWeb.getDocumentService().saveDocument(getDocument());
        Assert.assertTrue(getDocument().getDocumentHeader().getWorkflowDocument().isSaved());
    }

    /**
     * gets the maintenance document that is created in the constructor
     *
     * @return a maintenance document of the type returned by {@link #getDocumentTypeName()}
     */
    public MaintenanceDocument getDocument() {
        return document;
    }

    /**
     * set the maintenance document to use in the test
     * @param document - the maintenance document
     */
    public void setDocument(MaintenanceDocument document) {
        this.document = document;
    }
}
