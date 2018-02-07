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
package org.kuali.rice.kns.document;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.krad.test.KRADTestCase;

@Ignore
public class DocumentAuthorizerBaseTest extends KRADTestCase {
	// TODO rewrite this test
//    private static final Logger LOG = Logger.getLogger(DocumentAuthorizerBaseTest.class);
//
//    private static final String SUPERVISOR_USER = "ABNEY";
//    private static final String SUPERVISOR_UNIVERSAL = "2237202707";
//    private static final String NONSUPER_USER = "BARTH";
//    private static final String NONSUPER_UNIVERSAL = "5998202207";
//    private static final String NONSUPER_2_USER = "COOTER";
//    private static final String NONSUPER_2_UNIVERSAL = "598746310";
//
//    private DocumentAuthorizer documentAuthorizer;
//    private PessimisticLockService pessimisticLock;
//
//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        documentAuthorizer = new DocumentAuthorizerBase();
//        pessimisticLock = KRADServiceLocatorInternal.getPessimisticLockService();
//    }
//
//    @Override
//    public void tearDown() throws Exception {
//        try {
//            new ClearDatabaseLifecycle(Arrays.asList(new String[]{"KNS_PESSIMISTIC_LOCK_T"}), null).start();
//        } catch (Exception e) {
//            LOG.warn("Caught exception trying to clear Pessimistic Lock table");
//        }
//        super.tearDown();
//    }
//
//    // following is the Supervisor & Initiator grid
//
//    // ## Supervisor? UserIsInitiator? ApprovalRequested? canSupervise Result
//    // -----------------------------------------------------------------------------------
//    // A true true true true
//    //
//    // B true true false false
//    //
//    // C true false true true
//    //
//    // D true false false true
//    //
//    // E false * * false
//    //

    /* This test can no longer use mock objects because the isSupervisor() method is no longer on the Person object.
     * To check for supervisor status you know must to through the group service and check membership in the supervisor group.
     */
	@Test
    public void testCanSuperviseAsInitiatorA() {
//
//        DocumentActionFlags flags;
//        Document document;
//
//        // scenario A
//        //document = new MockDocument(getSuperUser(), true);
//        //flags = documentAuthorizer.getDocumentActionFlags(document, getSuperUser());
//        //assertTrue(flags.getCanSupervise());
//
//        // scenario B
//        document = new MockDocument(getSuperUser(), false);
//        flags = documentAuthorizer.getDocumentActionFlags(document, getSuperUser());
//        assertFalse(flags.getCanSupervise());
//
//        // scenario C
//        document = new MockDocument(getNonSuperUser(), true);
//        flags = documentAuthorizer.getDocumentActionFlags(document, getSuperUser());
//        assertTrue(flags.getCanSupervise());
//
//        // scenario D
//        document = new MockDocument(getNonSuperUser(), false);
//        flags = documentAuthorizer.getDocumentActionFlags(document, getSuperUser());
//        assertTrue(flags.getCanSupervise());
//
    }

//
//    private void verifyEditModeExists(Map editMode, String editModeToTest, String expectedValue) {
//        assertNotNull("Edit mode map should have a valid value for " + editModeToTest, editMode.get(editModeToTest));
//        assertEquals("Edit mode map should contain entry with key '" + editModeToTest + "' and value '" + expectedValue + "'", expectedValue, editMode.get(editModeToTest));
//    }
//
//    private void verifyEditModeDoesNotExist(Map editMode, String editModeToTest) {
//        assertNull("Edit mode map should not have an entry for " + editModeToTest, editMode.get(editModeToTest));
//    }
//
//    protected Person getWorkflowPessimisticLockOwnerUser() {
//        Person person = KimImplServiceLocator.getPersonService().getPersonByPrincipalName(KNSConstants.SYSTEM_USER);
//        if (person == null) {
//            throw new RuntimeException("Cannot find user with network id '" + KNSConstants.SYSTEM_USER);
//        }
//        return person;
//    }
//
//    /**
//     * This method tests the Pessimistic locking mechanism when no custom lock descriptors are being used by the document in
//     * the default manner (locks are unique to user and document)
//     *
//     * @throws Exception
//     */
    @Test
    public void testEstablishLocks_DefaultDocumentAuthorizerBase() throws Exception {
//        PessimisticLockService lockService = KRADServiceLocatorInternal.getPessimisticLockService();
//
//        // test no lock creation needed
//        Document document = new MockDocument(getNonSuperUser(), false);
//        int originalExpectedLockCount = 0;
//        // no lock should be created
//        int expectedLockCountIncrease = 0;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        Map oldEditMode = getEditMode_ReadOnly();
//        String testString = "dummyString";
//        oldEditMode.put(AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        Map newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//
//        // test valid lock creation where no user has any locks and edit modes returned are same as passed in
//        document = new MockDocument(getNonSuperUser(), false);
//        originalExpectedLockCount = 0;
//        // one lock should be created
//        expectedLockCountIncrease = 1;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_FullEntry();
//        testString = "dummyString";
//        oldEditMode.put(AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//
//        // test invalid lock status (2 locks by 2 different users) throws exception
//        document = new MockDocument(getNonSuperUser(), false);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), getNonSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), getSuperUser()));
//        try {
//            this.pessimisticLock.establishLocks(document, getEditMode_FullEntry(), getNonSuperUser());
//            fail("Document authorizer should have thrown exception of type PessimisticLockingException");
//        } catch (PessimisticLockingException e) {
//            // expected result
//        } catch (Exception e) {
//            fail("Document authorizer threw an exception but it was not of type PessimisticLockingException... it was a " + e.getClass().getName());
//        }
//
//        // test valid lock status (2 locks by 2 different users with one user being workflow lock user) does not throw
//        // exception
//        document = new MockDocument(getNonSuperUser(), false);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), getNonSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), getWorkflowPessimisticLockOwnerUser()));
//        try {
//            this.pessimisticLock.establishLocks(document, getEditMode_FullEntry(), getNonSuperUser());
//        } catch (Exception e) {
//            fail("Document authorizer should not have thrown an exception but did with type '" + e.getClass().getName() + "' and message: " + e.getMessage());
//        }
//
//        // test other user already has lock should return editMode of ReadOnly
//        document = new MockDocument(getNonSuperUser(), false);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), getSuperUser()));
//        originalExpectedLockCount = 1;
//        // one lock should be created
//        expectedLockCountIncrease = 0;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_FullEntry();
//        testString = "dummyString";
//        oldEditMode.put(AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//
//        // test current user already has lock and that edit modes returned are same as passed in
//        document = new MockDocument(getNonSuperUser(), false);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), getNonSuperUser()));
//        originalExpectedLockCount = 1;
//        // one lock should be created
//        expectedLockCountIncrease = 0;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_FullEntry();
//        testString = "dummyString";
//        oldEditMode.put(AuthorizationConstants.EditMode.VIEW_ONLY, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY, testString);
//
    }
//
//    /**
//     * This method tests the Pessimistic locking mechanism when custom lock descriptors are being used by the document in the
//     * default manner (locks are unique to user, document, and lock descriptor)
//     *
//     * @throws Exception
//     */
    @Test
    public void testEstablishLocks_CustomDocumentAuthorizerBase() throws Exception {
//        GlobalVariables.setUserSession(new UserSession("quickstart"));
//        PessimisticLockService lockService = KRADServiceLocatorInternal.getPessimisticLockService();
//        DocumentAuthorizer documentAuthorizer = new TestDocumentAuthorizerBase();
//        String lockDescriptor1 = "test the first lock";
//        String lockDescriptor2 = "this is the second lock";
//        String lockDescriptor3 = "booyah locks";
//
//        // test no lock creation needed
//        Document document = new MockDocument(getNonSuperUser(), false);
//        String expectedLockDescriptor = lockDescriptor1;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        int originalExpectedLockCount = 0;
//        // no lock should be created
//        int expectedLockCountIncrease = 0;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        Map oldEditMode = getEditMode_ReadOnly();
//        String testString = "dummyString";
//        oldEditMode.put(AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        Map newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//
//        // test lock descriptors nullify PessimisticLockingException throwing
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor3;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor1, getNonSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor2, getSuperUser()));
//        try {
//            this.pessimisticLock.establishLocks(document, getEditMode_FullEntry(), getNonSuperUser());
//        } catch (Exception e) {
//            fail("Document authorizer should not have thrown an exception but did with type '" + e.getClass().getName() + "' and message: " + e.getMessage());
//        }
//
//        // test valid lock status (2 locks by 2 different users with one user being workflow lock user) does not throw
//        // exception
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor3;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor1, getNonSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor2, getWorkflowPessimisticLockOwnerUser()));
//        try {
//            //documentAuthorizer.establishLocks(document, getEditMode_FullEntry(), getNonSuperUser());
//            this.pessimisticLock.establishLocks(document,getEditMode_FullEntry(), getNonSuperUser());
//        } catch (Exception e) {
//            fail("Document authorizer should not have thrown an exception but did with type '" + e.getClass().getName() + "' and message: " + e.getMessage());
//        }
//
//        // test valid lock creation with descriptor where no user has any locks and edit modes returned are same as passed in
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor3;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        originalExpectedLockCount = 0;
//        // one lock should be created
//        expectedLockCountIncrease = 1;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_FullEntry();
//        testString = "dummyString";
//        oldEditMode.put(AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        assertEquals("Lock descriptor from new lock is incorrect", expectedLockDescriptor, document.getPessimisticLocks().get(0).getLockDescriptor());
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//
//        // test other user already has lock with same descriptor should return editMode of ReadOnly
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor1;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor1, getSuperUser()));
//        originalExpectedLockCount = 1;
//        // no lock should be created
//        expectedLockCountIncrease = 0;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_FullEntry();
//        testString = "dummyString";
//        oldEditMode.put(AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        assertNotNull("Establish locks should return a UNVIEWABLE edit mode", newEditMode.get(AuthorizationConstants.EditMode.UNVIEWABLE));
//        assertEquals("Establish locks method returned invalid UNVIEWABLE edit mode", testString, newEditMode.get(AuthorizationConstants.EditMode.UNVIEWABLE));
//
//        // test other user already has lock with different descriptor should return given edit modes
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor1;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        PessimisticLock oldLock = lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor2, getNonSuperUser());
//        document.addPessimisticLock(oldLock);
//        originalExpectedLockCount = 1;
//        // one lock should be created
//        expectedLockCountIncrease = 1;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_FullEntry();
//        testString = "is this the string";
//        oldEditMode.put(AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        for (PessimisticLock lock : document.getPessimisticLocks()) {
//            if (!lock.getId().equals(oldLock.getId())) {
//                assertEquals("Lock descriptor of new lock is incorrect", expectedLockDescriptor, lock.getLockDescriptor());
//            }
//        }
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//
//        // test current user has lock with same lock descriptor and other users have no locks should return given edit modes
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor1;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), expectedLockDescriptor, getNonSuperUser()));
//        originalExpectedLockCount = 1;
//        // no lock should be created
//        expectedLockCountIncrease = 0;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_FullEntry();
//        testString = "is this the string";
//        oldEditMode.put(AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//
//        // test current user has lock with different lock descriptor and other users have no locks should return given edit
//        // modes
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor1;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        oldLock = lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor3, getNonSuperUser());
//        document.addPessimisticLock(oldLock);
//        originalExpectedLockCount = 1;
//        // one lock should be created
//        expectedLockCountIncrease = 1;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_FullEntry();
//        testString = "is this the string";
//        oldEditMode.put(AuthorizationConstants.EditMode.VIEW_ONLY, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        for (PessimisticLock lock : document.getPessimisticLocks()) {
//            if (!lock.getId().equals(oldLock.getId())) {
//                assertEquals("Lock descriptor of new lock is incorrect", expectedLockDescriptor, lock.getLockDescriptor());
//            }
//        }
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY, testString);
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE);
//
//        // test invalid lock status (2 locks with same lock descriptor by 2 different users) throws exception
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor3;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor1, getNonSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor3, getSecondNonSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor1, getSuperUser()));
//        try {
//            this.pessimisticLock.establishLocks(document, getEditMode_FullEntry(), getNonSuperUser());
//            fail("Document authorizer should have thrown exception of type PessimisticLockingException");
//        } catch (PessimisticLockingException e) {
//            // expected result
//        } catch (Exception e) {
//            fail("Document authorizer threw an exception but it was not of type PessimisticLockingException... it was a " + e.getClass().getName());
//        }
//
//        // test valid lock status (2 locks by 2 different users with one user being workflow lock user) does not throw
//        // exception
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor3;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        Person user = getWorkflowPessimisticLockOwnerUser();
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor3, user));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor1, getSuperUser()));
//        try {
//            this.pessimisticLock.establishLocks(document, getEditMode_FullEntry(), getNonSuperUser());
//        } catch (Exception e) {
//            fail("Document authorizer should not have thrown an exception but did with type '" + e.getClass().getName() + "' and message: " + e.getMessage());
//        }
//
//        // test current user has lock with different descriptor and other users have locks with same descriptors should
//        // return read only editmodes
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor3;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor1, getNonSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor3, getSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor2, getSecondNonSuperUser()));
//        originalExpectedLockCount = 3;
//        // no lock should be created
//        expectedLockCountIncrease = 0;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_FullEntry();
//        testString = "is this the string";
//        oldEditMode.put(AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//
//        // test current user has lock with same descriptor and other users have locks with different descriptors should
//        // return given editmodes
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor3;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), expectedLockDescriptor, getNonSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor1, getSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor2, getSecondNonSuperUser()));
//        originalExpectedLockCount = 3;
//        // no lock should be created
//        expectedLockCountIncrease = 0;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_FullEntry();
//        testString = "is this the string";
//        oldEditMode.put(AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//
//        // test current user has lock with different lock descriptor and other users have locks with different lock
//        // descriptor should return given edit modes
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor1;
//        List<Long> existingLockIds = new ArrayList<Long>();
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        oldLock = lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor3, getSecondNonSuperUser());
//        existingLockIds.add(oldLock.getId());
//        document.addPessimisticLock(oldLock);
//        oldLock = lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor2, getSecondNonSuperUser());
//        existingLockIds.add(oldLock.getId());
//        document.addPessimisticLock(oldLock);
//        oldLock = lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor3, getSuperUser());
//        existingLockIds.add(oldLock.getId());
//        document.addPessimisticLock(oldLock);
//        originalExpectedLockCount = existingLockIds.size();
//        // one lock should be created
//        expectedLockCountIncrease = 1;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_FullEntry();
//        testString = "is this the string";
//        oldEditMode.put(AuthorizationConstants.EditMode.VIEW_ONLY, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        for (PessimisticLock lock : document.getPessimisticLocks()) {
//            if (!existingLockIds.contains(lock.getId())) {
//                // this must be the newly created lock
//                assertEquals("Lock descriptor of new lock is incorrect", expectedLockDescriptor, lock.getLockDescriptor());
//            }
//        }
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY, testString);
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE);
//
//        // test current user has lock with wrong descriptor and no entry edit modes return given editmodes
//        document = new MockDocument(getNonSuperUser(), false);
//        expectedLockDescriptor = lockDescriptor3;
//        GlobalVariables.getUserSession().addObject(TestDocumentAuthorizerBase.USER_SESSION_OBJECT_KEY, (Object) expectedLockDescriptor);
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor1, getNonSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor3, getSuperUser()));
//        document.addPessimisticLock(lockService.generateNewLock(document.getDocumentNumber(), lockDescriptor2, getSecondNonSuperUser()));
//        originalExpectedLockCount = 3;
//        // no lock should be created
//        expectedLockCountIncrease = 0;
//        assertEquals("Lock count is incorrect", originalExpectedLockCount, document.getPessimisticLocks().size());
//        oldEditMode = getEditMode_ReadOnly();
//        testString = "is this the string";
//        oldEditMode.put(AuthorizationConstants.EditMode.UNVIEWABLE, testString);
//        newEditMode = this.pessimisticLock.establishLocks(document, oldEditMode, getNonSuperUser());
//        assertEquals("Lock count is incorrect", originalExpectedLockCount + expectedLockCountIncrease, document.getPessimisticLocks().size());
//        assertEquals("Establish locks method returned invalid number of edit modes", oldEditMode.size(), newEditMode.size());
//        verifyEditModeDoesNotExist(newEditMode, AuthorizationConstants.EditMode.FULL_ENTRY);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.VIEW_ONLY, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        verifyEditModeExists(newEditMode, AuthorizationConstants.EditMode.UNVIEWABLE, testString);
    }
//
    @Test
    public void testHasPreRouteEditAuthorization() throws Exception {
//    	//TODO: the test for preRouteEditAuthorization should be removed
//        GlobalVariables.setUserSession(new UserSession("quickstart"));
//        PessimisticLockTestDocumentAuthorizer lockDocumentAuthorizer = new PessimisticLockTestDocumentAuthorizer();
//        lockDocumentAuthorizer.USES_PESSIMISTIC_LOCKING = false;
//
//        Person initiatorUser = getNonSuperUser();
//        Person authorizerUser = getNonSuperUser();
//        Document document = new MockDocument(initiatorUser, false);
//        assertTrue(authorizerUser + " should have Initiate Authorization due to initator being " + initiatorUser, lockDocumentAuthorizer.hasInitiateAuthorization(document, authorizerUser));
//        //assertTrue(authorizerUser + " should have Pre Route Edit Authorization", lockDocumentAuthorizer.hasPreRouteEditAuthorization(document, authorizerUser));
//
//        initiatorUser = getNonSuperUser();
//        authorizerUser = getSuperUser();
//        document = new MockDocument(initiatorUser, false);
//        assertFalse(authorizerUser + " should not have Initiate Authorization due to initator being " + initiatorUser, lockDocumentAuthorizer.hasInitiateAuthorization(document, authorizerUser));
//        //assertFalse(authorizerUser + " should not have Pre Route Edit Authorization", lockDocumentAuthorizer.hasPreRouteEditAuthorization(document, authorizerUser));
//
//        // switch to using pessimistic locking for the next tests
//        lockDocumentAuthorizer.USES_PESSIMISTIC_LOCKING = true;
//
//        initiatorUser = getNonSuperUser();
//        authorizerUser = getSuperUser();
//        document = new MockDocument(initiatorUser, false);
//        assertFalse(authorizerUser + " should not have Initiate Authorization due to initator being " + initiatorUser, lockDocumentAuthorizer.hasInitiateAuthorization(document, authorizerUser));
//        //assertFalse(authorizerUser + " should not have Pre Route Edit Authorization", lockDocumentAuthorizer.hasPreRouteEditAuthorization(document, authorizerUser));
//
//        initiatorUser = getNonSuperUser();
//        authorizerUser = getNonSuperUser();
//        document = new MockDocument(initiatorUser, false);
//        document.addPessimisticLock(KRADServiceLocatorInternal.getPessimisticLockService().generateNewLock(document.getDocumentNumber(), getSecondNonSuperUser()));
//        assertTrue(authorizerUser + " should have Initiate Authorization due to initator being " + initiatorUser, lockDocumentAuthorizer.hasInitiateAuthorization(document, authorizerUser));
//        //assertFalse(authorizerUser + " should not have Pre Route Edit Authorization", lockDocumentAuthorizer.hasPreRouteEditAuthorization(document, authorizerUser));
//
//        initiatorUser = getNonSuperUser();
//        authorizerUser = getNonSuperUser();
//        document = new MockDocument(initiatorUser, false);
//        document.addPessimisticLock(KRADServiceLocatorInternal.getPessimisticLockService().generateNewLock(document.getDocumentNumber(), authorizerUser));
//        assertTrue(authorizerUser + " should have Initiate Authorization due to initator being " + initiatorUser, lockDocumentAuthorizer.hasInitiateAuthorization(document, authorizerUser));
//        //assertTrue(authorizerUser + " should have Pre Route Edit Authorization", lockDocumentAuthorizer.hasPreRouteEditAuthorization(document, authorizerUser));
    }
//
    @Test
    public void testGetDocumentActionFlagsUsingPessimisticLocking() throws Exception {
//        GlobalVariables.setUserSession(new UserSession("quickstart"));
//        String editModeTrueValue = "TRUE";
//        String editModeFalseValue = "FALSE";
//        PessimisticLockTestDocumentAuthorizer lockDocumentAuthorizer = new PessimisticLockTestDocumentAuthorizer();
//
//        Person initiatorUser = getNonSuperUser();
//        Person authorizerUser = getNonSuperUser();
//        Document document = new MockDocument(initiatorUser, false);
//        lockDocumentAuthorizer.USES_PESSIMISTIC_LOCKING = false;
//        Map editMode = new HashMap();
//        lockDocumentAuthorizer.setEditMode(MapUtils.putAll(editMode, new Map.Entry[]{new DefaultMapEntry(AuthorizationConstants.EditMode.VIEW_ONLY,"TRUE")}));
//        DocumentFormPlaceholder formDummy = implementDocumentAuthorizerMethods(lockDocumentAuthorizer, document, new MockPerson(GlobalVariables.getUserSession().getPerson()));
    }
//
//    /**
//     * This is a stand-in object for a {@link KualiDocumentFormBase} object
//     */
//    private class DocumentFormPlaceholder {
//        public Map editingMode;
//        public DocumentActionFlags documentActionFlags;
//    }
//
//    /**
//     * This method is a stand-in method to duplicate the behavior of
//     * {@link KualiDocumentFormBase#populateAuthorizationFields(DocumentAuthorizer)} and
//     * {@link KualiDocumentFormBase#useDocumentAuthorizer(DocumentAuthorizer)}
//     *
//     * @param document -
//     *            document to use for edit modes and action flags
//     * @param kualiUser -
//     *            user to use in place of what may be in GlobalVariables
//     * @return a stand-in object for {@link KualiDocumentFormBase} that holds the edit mode map and the
//     *         {@link DocumentActionFlags} object that would be held in the {@link KualiDocumentFormBase}
//     */
//    private DocumentFormPlaceholder implementDocumentAuthorizerMethods(PessimisticLockTestDocumentAuthorizer documentAuthorizer, Document document, Person kualiUser) {
//        // Person kualiUser = GlobalVariables.getUserSession().getPerson();
//        // DocumentAuthorizer documentAuthorizer = KRADServiceLocatorInternal.getDocumentAuthorizationService().getDocumentAuthorizer(document);
//        Map editMode = documentAuthorizer.getEditMode(document, kualiUser);
//        if (documentAuthorizer.usesPessimisticLocking(document)) {
//            editMode = this.pessimisticLock.establishLocks(document, editMode, kualiUser);
//        }
//        DocumentFormPlaceholder formPlaceholder = new DocumentFormPlaceholder();
//        formPlaceholder.editingMode = editMode;
//        formPlaceholder.documentActionFlags = documentAuthorizer.getDocumentActionFlags(document, kualiUser);
//        return formPlaceholder;
//    }
//
//    private Person getSuperUser() {
//        return new MockPerson(SUPERVISOR_UNIVERSAL, SUPERVISOR_USER, true);
//    }
//
//    private Person getNonSuperUser() {
//        return new MockPerson(NONSUPER_UNIVERSAL, NONSUPER_USER, false);
//    }
//
//    private Person getSecondNonSuperUser() {
//        return new MockPerson(NONSUPER_2_UNIVERSAL, NONSUPER_2_USER, false);
//    }
//
//    private Map getEditMode_ReadOnly() {
//        return constructEditMode(AuthorizationConstants.EditMode.VIEW_ONLY);
//    }
//
//    private Map getEditMode_FullEntry() {
//        return constructEditMode(AuthorizationConstants.EditMode.FULL_ENTRY);
//    }
//
//    private Map constructEditMode(String editModeValue) {
//        Map editMode = new HashMap();
//        editMode.put(editModeValue, DocumentAuthorizerBase.EDIT_MODE_DEFAULT_TRUE_VALUE);
//        return editMode;
//    }
//
//    private class MockDocument extends DocumentBase {
//
//        private MockDocument() {
//            super();
//        }
//
//        public MockDocument(Person initiator, boolean isApprovalRequested) {
//            this();
//            this.documentNumber = "1234567890";
//            getDocumentHeader().setWorkflowDocument(new MockWorkflowDocument(initiator, isApprovalRequested));
//        }
//
//        @Override
//        protected LinkedHashMap toStringMapper() {
//            LinkedHashMap map = new LinkedHashMap();
//            map.put("class", "MockDocument");
//            map.put(KNSPropertyConstants.DOCUMENT_NUMBER, documentNumber);
//            map.put("initiator", getDocumentHeader().getWorkflowDocument().getInitiatorNetworkId());
//            return map;
//        }
//
//        public boolean getAllowsCopy() {
//            return false;
//        }
//
//    }
//
//    private class MockWorkflowDocument extends MockWorkflowDocument {
//
//        private Person initiator;
//        private boolean approvalRequested;
//
//        private MockWorkflowDocument() {};
//
//        public MockWorkflowDocument(Person initiator, boolean isApprovalRequested) {
//            this.initiator = initiator;
//            this.approvalRequested = isApprovalRequested;
//        }
//
//        public String getInitiatorNetworkId() {
//            return initiator.getPrincipalName();
//        }
//
//        public boolean isApprovalRequested() {
//            return approvalRequested;
//        }
//
//        public boolean userIsInitiator(Person user) {
//            return initiator.getPrincipalId().equalsIgnoreCase(user.getPrincipalId());
//        }
//
//		/**
//		 * This overridden method ...
//		 *
//		 * @see org.kuali.rice.krad.workflow.service.KualiWorkflowDocument#appSpecificRouteDocumentToGroup(java.lang.String, java.lang.String, int, java.lang.String, org.kuali.rice.kim.bo.group.dto.GroupInfo, java.lang.String, boolean)
//		 */
//		public void appSpecificRouteDocumentToGroup(String actionRequested,
//				String routeTypeName, String annotation,
//				GroupIdDTO groupId, String responsibilityDesc,
//				boolean forceAction) throws WorkflowException {
//			// TODO sp20369 - THIS METHOD NEEDS JAVADOCS
//
//		}
//
//    }
//
//    private class MockPerson extends PersonImpl {
//        private boolean supervisor;
//        private String principalId;
//        private String principalName;
//
//        private MockPerson() {};
//
//        public MockPerson(Person user) {
//            this(user.getPrincipalId(), user.getPrincipalName(), KimApiServiceLocator.getIdentityManagementService().isMemberOfGroup(user.getPrincipalId(), org.kuali.rice.kim.util.KimApiConstants.KIM_GROUP_KFS_NAMESPACE_CODE, KRADServiceLocatorInternal.getKualiConfigurationService().getPropertyValueAsString(KNSConstants.KNS_NAMESPACE, KNSConstants.DetailTypes.DOCUMENT_DETAIL_TYPE, KNSConstants.CoreApcParms.SUPERVISOR_WORKGROUP)));
//        }
//
//        public MockPerson(String universalId, String userId, boolean supervisor) {
//            this.principalId = universalId;
//            this.principalName = userId;
//            this.supervisor = supervisor;
//        }
//
//        public boolean isSupervisorUser() {
//            return supervisor;
//        }
//
//        public String toString() {
//            return ((supervisor) ? "Supervisor " : "") + "User (" + getPrincipalName() + ")";
//        }
//
//		public String getPrincipalId() {
//			return this.principalId == null ? super.getPrincipalId() : this.principalId;
//		}
//
//		public String getPrincipalName() {
//			return this.principalName == null ? super.getPrincipalName() : this.principalName;
//		}
//
//    }
}
