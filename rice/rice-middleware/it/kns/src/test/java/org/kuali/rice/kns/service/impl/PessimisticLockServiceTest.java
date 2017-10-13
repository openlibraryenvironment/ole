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
package org.kuali.rice.kns.service.impl;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.KimConstants.PermissionNames;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.KNSTestCase;
import org.kuali.rice.kns.authorization.AuthorizationConstants;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.document.authorization.PessimisticLock;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.PessimisticLockService;
import org.kuali.rice.krad.service.impl.PessimisticLockServiceImpl;
import org.kuali.rice.krad.test.KRADTestCase;
import org.kuali.rice.krad.test.document.AccountRequestDocument;
import org.kuali.rice.krad.test.document.AccountRequestDocument2;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.kns.maintainable.AccountType2MaintainableImpl;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestSql;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * PessimisticLockServiceTest tests {@link PessimisticLockServiceImpl} for maintainable
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class PessimisticLockServiceTest extends KNSTestCase {

    String sessionId = "ad4d6c83-4d0f-4309-a528-c2f81ec00395";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setUserSession(new UserSession("quickstart"));
        GlobalVariables.getUserSession().setKualiSessionId(sessionId);
    }

    /**
     * tests the PessimisticLockService's ability to establish pessimistic locks for maintenance documents (via maintainables) that
     * support custom lock descriptors
     * 
     * @throws Exception
     */
    @Test
    public void testPessimisticLockingWithCustomMaintainableLockDescriptors() throws Exception {
    	MaintenanceDocument maintDoc = (MaintenanceDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument("AccountType2MaintenanceDocument");
    	assertTrue("The AccountType2MaintenanceDocument should be using pessimistic locking", KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary(
			).getDocumentEntry(maintDoc.getNewMaintainableObject().getDataObjectClass().getSimpleName() + "MaintenanceDocument").getUsePessimisticLocking());
    	assertTrue("The AccountType2MaintenanceDocument should be using custom lock descriptors", maintDoc.useCustomLockDescriptors());
    	assertTrue("The AccountType2MaintenanceDocument's new maintainable uses the wrong class",
    			maintDoc.getNewMaintainableObject() instanceof AccountType2MaintainableImpl);
    	AccountType2MaintainableImpl newMaint = (AccountType2MaintainableImpl) maintDoc.getNewMaintainableObject();
    	assertTrue("The AccountType2MaintainableImpl should be using custom lock descriptors", newMaint.useCustomLockDescriptors());
    	
    	// Perform the custom lock descriptor unit testing operations.
       	assertCustomLockDescriptorsAreWorking(maintDoc, AccountType2MaintainableImpl.ACCT_TYPE_2_MAINT_FIELDS_TO_EDIT,
       			AccountType2MaintainableImpl.EDIT_CODE_ONLY, AccountType2MaintainableImpl.EDIT_NAME_ONLY);
    }
    
    /**
     * A convenience method for testing the custom lock descriptors of documents (and on the maintainables of maintenance documents).
     *
     * @param testDoc The document to test pessimistic locking on (or the maintenance document with maintainables to test on).
     * @param LOCK_KEY The UserSession object key to use for storing the lock descriptor's key.
     * @param LOCK_VALUE1 One possible object to store in a UserSession for generating lock descriptors on the testDoc.
     * @param LOCK_VALUE2 Another possible object to store in a UserSession for generating lock descriptors on the testDoc.
     *
     * @throws Exception
     */
    private void assertCustomLockDescriptorsAreWorking(Document testDoc, final String LOCK_KEY, final Serializable LOCK_VALUE1,
    		final Serializable LOCK_VALUE2) throws Exception {
    	PessimisticLockService lockService = KRADServiceLocatorWeb.getPessimisticLockService();

    	// Have "quickstart" establish a pessimistic lock on the document by using a custom lock descriptor that only locks part of the document.
       	UserSession quickstartSession = new UserSession("quickstart");
       	Person[] allPersons = { quickstartSession.getPerson(), null };
    	Map<String,String> editMode = new HashMap<String,String>();
    	editMode.put(AuthorizationConstants.EditMode.FULL_ENTRY, KRADConstants.KUALI_DEFAULT_TRUE_VALUE);
    	GlobalVariables.getUserSession().addObject(LOCK_KEY, LOCK_VALUE1);
    	String[] allDescriptors = { testDoc.getCustomLockDescriptor(quickstartSession.getPerson()), null };
   		assertNotNull("The document should have generated a custom lock descriptor", allDescriptors[0]);
    	Map <?,?> finalModes = lockService.establishLocks(testDoc, editMode, quickstartSession.getPerson());

    	// Verify that the lock was actually established and that the expected custom lock descriptor was used.
    	assertCorrectLocksAreInPlace(true, finalModes, 1, testDoc.getPessimisticLocks(), allPersons, allDescriptors);

    	// Attempt to establish the same lock again, which should change nothing since "quickstart" already has the lock.
    	editMode = new HashMap<String,String>();
    	editMode.put(AuthorizationConstants.EditMode.FULL_ENTRY, KRADConstants.KUALI_DEFAULT_TRUE_VALUE);
    	GlobalVariables.getUserSession().addObject(LOCK_KEY, LOCK_VALUE1);
    	lockService.establishLocks(testDoc, editMode, quickstartSession.getPerson());
    	assertCorrectLocksAreInPlace(false, null, 1, testDoc.getPessimisticLocks(), allPersons, allDescriptors);

    	// Now check to make sure that a different user (such as "admin") cannot establish a lock using the same lock descriptor.
    	UserSession adminSession = new UserSession("admin");
    	editMode = new HashMap<String,String>();
    	editMode.put(AuthorizationConstants.EditMode.FULL_ENTRY, KRADConstants.KUALI_DEFAULT_TRUE_VALUE);
       	GlobalVariables.getUserSession().addObject(LOCK_KEY, LOCK_VALUE1);
    	assertEquals("The document should have generated the same lock descriptors for both 'quickstart' and 'admin'",
    			allDescriptors[0], testDoc.getCustomLockDescriptor(adminSession.getPerson()));
    	finalModes = lockService.establishLocks(testDoc, editMode, adminSession.getPerson());
    	assertCorrectLocksAreInPlace(false, finalModes, 1, testDoc.getPessimisticLocks(), allPersons, allDescriptors);

    	// Ensure that "admin" can establish a lock that has a different lock descriptor.
    	allPersons[1] = adminSession.getPerson();
    	editMode = new HashMap<String,String>();
    	editMode.put(AuthorizationConstants.EditMode.FULL_ENTRY, KRADConstants.KUALI_DEFAULT_TRUE_VALUE);
    	GlobalVariables.getUserSession().addObject(LOCK_KEY, LOCK_VALUE2);
    	allDescriptors[1] = testDoc.getCustomLockDescriptor(adminSession.getPerson());
    	assertNotNull("The document should have generated a custom lock descriptor", allDescriptors[1]);
    	assertNotSame("'quickstart' and 'admin' should have different custom lock descriptors now", allDescriptors[0], allDescriptors[1]);
    	finalModes = lockService.establishLocks(testDoc, editMode, adminSession.getPerson());
    	assertCorrectLocksAreInPlace(true, finalModes, 2, testDoc.getPessimisticLocks(), allPersons, allDescriptors);

    	// Verify that "quickstart" cannot acquire the lock owned by "admin".
    	editMode = new HashMap<String,String>();
    	editMode.put(AuthorizationConstants.EditMode.FULL_ENTRY, KRADConstants.KUALI_DEFAULT_TRUE_VALUE);
    	GlobalVariables.getUserSession().addObject(LOCK_KEY, LOCK_VALUE2);
    	lockService.establishLocks(testDoc, editMode, quickstartSession.getPerson());
    	assertCorrectLocksAreInPlace(false, null, 2, testDoc.getPessimisticLocks(), allPersons, allDescriptors);

    	// After "admin" releases his lock, check to make sure that "quickstart" can now acquire it.
    	lockService.releaseAllLocksForUser(testDoc.getPessimisticLocks(), allPersons[1], allDescriptors[1]);
    	testDoc.refreshPessimisticLocks();
    	assertCorrectLocksAreInPlace(false, null, 1, testDoc.getPessimisticLocks(), allPersons, allDescriptors);
    	allPersons[1] = allPersons[0];
    	editMode = new HashMap<String,String>();
    	editMode.put(AuthorizationConstants.EditMode.FULL_ENTRY, KRADConstants.KUALI_DEFAULT_TRUE_VALUE);
    	GlobalVariables.getUserSession().addObject(LOCK_KEY, LOCK_VALUE2);
    	finalModes = lockService.establishLocks(testDoc, editMode, quickstartSession.getPerson());
    	assertCorrectLocksAreInPlace(true, finalModes, 2, testDoc.getPessimisticLocks(), allPersons, allDescriptors);

    	// Release all the locks when done.
    	GlobalVariables.getUserSession().removeObject(LOCK_KEY);
    	lockService.releaseAllLocksForUser(testDoc.getPessimisticLocks(), allPersons[0]);
    	testDoc.refreshPessimisticLocks();
    	assertTrue("There should not be any pessimistic locks present on the document", testDoc.getPessimisticLocks().isEmpty());
    }

    /**
     * A convenience method for checking to ensure that the proper pessimistic locks are in place.
     *
     * @param latestUserHasFullEntry Indicates if the map returned by PessimisticLockService.establishLocks should have a true "fullEntry" parameter.
     * @param finalModes The map returned by the call to PessimisticLockService.establishLocks. This parameter can be null if checking it is not needed.
     * @param expectedLockQuantity The expected number of pessimistic locks.
     * @param pessimisticLocks The list of pessimistic locks to check for proper quantity and proper state.
     * @param expectedOwners The users who are expected to own the corresponding locks in the previous list.
     * @param expectedDescriptors The expected lock descriptors for the corresponding locks in the other list. This parameter can be set to null if
     * the pessimistic locks are not using custom lock descriptors or if custom lock descriptors are not the concern of the test.
     * @throws Exception
     */
    private void assertCorrectLocksAreInPlace(boolean latestUserHasFullEntry, Map<?,?> finalModes, int expectedLockQuantity,
    		List<PessimisticLock> pessimisticLocks, Person[] expectedOwners, String[] expectedDescriptors) throws Exception {
    	// Ensure that the last user to attempt to establish locks has the expected finalModes entry (or lack of it).
    	if (finalModes != null) {
    		assertEquals("The last user that tried to establish locks does not have the expected status on their full entry privileges",
    				latestUserHasFullEntry, StringUtils.equalsIgnoreCase(KRADConstants.KUALI_DEFAULT_TRUE_VALUE, (String)(finalModes.get(
                    AuthorizationConstants.EditMode.FULL_ENTRY))));
    	}
    	// Ensure that the expected number of locks are present.
    	assertEquals("The wrong number of pessimistic locks are in place", expectedLockQuantity, pessimisticLocks.size());
    	// Verify that each lock has the expected owners.
    	for (int i = pessimisticLocks.size() - 1; i > -1; i--) {
    		assertTrue("The lock at index " + i + " did not have the expected owner of " + expectedOwners[i].getPrincipalName(),
    			pessimisticLocks.get(i).isOwnedByUser(expectedOwners[i]));
    		if (expectedDescriptors != null) {
    			assertTrue("The lock at index " + i + " did not have the expected lock descriptor of " + expectedDescriptors[i],
    					pessimisticLocks.get(i).getLockDescriptor().equals(expectedDescriptors[i]));
    		}
    	}
    }
}
