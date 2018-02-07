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

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.KimConstants.PermissionNames;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.authorization.AuthorizationConstants;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.document.authorization.PessimisticLock;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.service.impl.PessimisticLockServiceImpl;
import org.kuali.rice.krad.test.document.AccountRequestDocument;
import org.kuali.rice.krad.test.document.AccountRequestDocument2;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestSql;
import org.kuali.rice.krad.test.KRADTestCase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;


/**
 * PessimisticLockServiceTest tests {@link PessimisticLockServiceImpl}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class PessimisticLockServiceTest extends KRADTestCase {

    String sessionId = "ad4d6c83-4d0f-4309-a528-c2f81ec00396";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setUserSession(new UserSession("quickstart"));
        GlobalVariables.getUserSession().setKualiSessionId(sessionId);
    }

    /**
     * tests deleting {@link PessimisticLock} objects. Tests that invalid deletes throw exceptions and valid
     * deletes by owner users as well as lock admin users do work as expected
     *
     * @throws Exception
     */
    @UnitTestData(
            sqlStatements = {
                    @UnitTestSql("DELETE FROM KRNS_PESSIMISTIC_LOCK_T"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1111, '4f6bc9e2-7df8-102c-97b6-ed716fdaf540', 0, NULL, '1234', {d '2007-07-01'}, 'employee', 'aa5d6c83-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1112, '5add9cba-7df8-102c-97b6-ed716fdaf540', 0, NULL, '1235', {d '2007-10-01'}, 'frank', 'dd4d6c83-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1113, '69e42b8e-7df8-102c-97b6-ed716fdaf540', 0, NULL, '1236', {d '2007-08-01'}, 'fred', 'ad4d6c83-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1114, '76504650-7df8-102c-97b6-ed716fdaf540', 0, NULL, '1237', {d '2007-08-01'}, 'fred', 'ad4d6c83-4d0f-4309-a528-c2f81ec00396')")
                    }
            )
    @Test
    public void testDeleteLocks() throws Exception {
    	
        List<PessimisticLock> locks = (List<PessimisticLock>) KRADServiceLocator.getBusinessObjectService().findAll(PessimisticLock.class);
        assertEquals("Should be 4 locks in DB", 4, locks.size());

        String userId = "employee";
        String[] lockIdsToVerify = new String[]{"1112", "1113"};
        assertFalse("User " + userId + " should not be member of pessimistic lock admin permission", KimApiServiceLocator.getPermissionService().isAuthorized(new UserSession(userId).getPerson().getPrincipalId(), KRADConstants.KNS_NAMESPACE, PermissionNames.ADMIN_PESSIMISTIC_LOCKING,
                Collections.<String, String>emptyMap() ) );
        verifyDelete(userId, Arrays.asList(lockIdsToVerify), AuthorizationException.class, true);
        userId = "frank";
        lockIdsToVerify = new String[]{"1111", "1113"};
        assertFalse("User " + userId + " should not be member of pessimistic lock admin permission", KimApiServiceLocator.getPermissionService().isAuthorized(new UserSession(userId).getPerson().getPrincipalId(), KRADConstants.KNS_NAMESPACE, PermissionNames.ADMIN_PESSIMISTIC_LOCKING, Collections.<String, String>emptyMap() ) );
        verifyDelete(userId, Arrays.asList(lockIdsToVerify), AuthorizationException.class, true);
        userId = "fred";
        lockIdsToVerify = new String[]{"1111", "1112"};
        assertFalse("User " + userId + " should not be member of pessimistic lock admin permission", KimApiServiceLocator.getPermissionService().isAuthorized(new UserSession(userId).getPerson().getPrincipalId(), KRADConstants.KNS_NAMESPACE, PermissionNames.ADMIN_PESSIMISTIC_LOCKING, Collections.<String, String>emptyMap() ) );
        verifyDelete(userId, Arrays.asList(lockIdsToVerify), AuthorizationException.class, true);

        verifyDelete("employee", Arrays.asList(new String[]{"1111"}), null, false);
        verifyDelete("frank", Arrays.asList(new String[]{"1112"}), null, false);
        verifyDelete("fred", Arrays.asList(new String[]{"1113"}), null, false);
        locks = (List<PessimisticLock>) KRADServiceLocator.getBusinessObjectService().findAll(PessimisticLock.class);
        assertEquals("Should be 1 lock left in DB", 1, locks.size());

        // test admin user can delete any lock
        userId = "fran";
        assertTrue("User " + userId + " should be member of pessimistic lock admin permission", KimApiServiceLocator.getPermissionService().isAuthorized(new UserSession(userId).getPerson().getPrincipalId(), KRADConstants.KNS_NAMESPACE, PermissionNames.ADMIN_PESSIMISTIC_LOCKING, Collections.<String, String>emptyMap() ) );
        userId = "admin";
        assertTrue("User " + userId + " should be member of pessimistic lock admin permission", KimApiServiceLocator.getPermissionService().isAuthorized(new UserSession(userId).getPerson().getPrincipalId(), KRADConstants.KNS_NAMESPACE, PermissionNames.ADMIN_PESSIMISTIC_LOCKING, Collections.<String, String>emptyMap() ) );
        verifyDelete(userId, Arrays.asList(new String[]{"1114"}), null, false);
        locks = (List<PessimisticLock>) KRADServiceLocator.getBusinessObjectService().findAll(PessimisticLock.class);
        assertEquals("Should be 0 locks left in DB", 0, locks.size());
    }

    /**
     *  deletes the provided locks while checking for error conditions
     *
     * @param userId - the user id to use in creating a session
     * @param lockIds - a list lock ids to delete
     * @param expectedException - the expected exception's class
     * @param expectException - true if an exception is expected on delete
     * @see PessimisticLockService#delete(String)
     * @throws WorkflowException
     */
    private void verifyDelete(String userId, List<String> lockIds, Class expectedException, boolean expectException) throws WorkflowException {
        GlobalVariables.setUserSession(new UserSession(userId));
        for (String lockId : lockIds) {
            try {
                KRADServiceLocatorWeb.getPessimisticLockService().delete(lockId);
                if (expectException) {
                    fail("Expected exception when deleting lock with id '" + lockId + "' for user '" + userId + "'");
                }
            } catch (Exception e) {
                if (!expectException) {
                    fail("Did not expect exception when deleting lock with id '" + lockId + "' for user '" + userId + "' but got exception of type '" + e.getClass().getName() + "'");
                }
                if (expectedException != null) {
                    // if we have an expected exception
                    if (!expectedException.isAssignableFrom(e.getClass())) {
                        fail("Expected exception of type '" + expectedException.getName() + "' when deleting lock with id '" + lockId + "' for user '" + userId + "' but got exception of type '" + e.getClass().getName() + "'");
                    }
                }
            }
        }
    }

    /**
     * tests the generation of new {@link PessimisticLock} objects
     *
     * @throws Exception
     */
    @Test
    public void testGenerateNewLocks() throws Exception {
        PessimisticLockService lockService = KRADServiceLocatorWeb.getPessimisticLockService();

        // test generating lock with no given lock descriptor
        String documentNumber = "1243";
        PessimisticLock lock = lockService.generateNewLock(documentNumber);
        assertNotNull("Generated lock should have id", lock.getId());
        assertEquals("Document Number should match", documentNumber, lock.getDocumentNumber());
        assertNotNull("Generated lock should have a generated timestamp ", lock.getGeneratedTimestamp());
        assertEquals("Generated lock should have default lock descriptor", PessimisticLock.DEFAULT_LOCK_DESCRIPTOR, lock.getLockDescriptor());
        assertEquals("Generated lock should be owned by current user", GlobalVariables.getUserSession().getPerson().getPrincipalName(), lock.getOwnedByUser().getPrincipalName());
        Map primaryKeys = new HashMap();
        primaryKeys.put(KRADPropertyConstants.ID, lock.getId());
        lock = null;
        lock = (PessimisticLock) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(PessimisticLock.class, primaryKeys);
        assertNotNull("Generated lock should be available from BO Service", lock);
        assertNotNull("Generated lock should have id", lock.getId());
        assertEquals("Document Number should match", documentNumber, lock.getDocumentNumber());
        assertNotNull("Generated lock should have a generated timestamp ", lock.getGeneratedTimestamp());
        assertEquals("Generated lock should have default lock descriptor", PessimisticLock.DEFAULT_LOCK_DESCRIPTOR, lock.getLockDescriptor());
        assertEquals("Generated lock should be owned by current user", GlobalVariables.getUserSession().getPerson().getPrincipalName(), lock.getOwnedByUser().getPrincipalName());

        // test generating lock with given lock descriptor
        lock = null;
        documentNumber = "4321";
        String lockDescriptor = "this is a test lock descriptor";
        lock = lockService.generateNewLock(documentNumber, lockDescriptor);
        assertNotNull("Generated lock should have id", lock.getId());
        assertEquals("Document Number should match", documentNumber, lock.getDocumentNumber());
        assertNotNull("Generated lock should have a generated timestamp ", lock.getGeneratedTimestamp());
        assertEquals("Generated lock should have lock descriptor set", lockDescriptor, lock.getLockDescriptor());
        assertEquals("Generated lock should be owned by current user", GlobalVariables.getUserSession().getPerson().getPrincipalName(), lock.getOwnedByUser().getPrincipalName());
        primaryKeys = new HashMap();
        primaryKeys.put(KRADPropertyConstants.ID, lock.getId());
        lock = null;
        lock = (PessimisticLock) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(PessimisticLock.class, primaryKeys);
        assertNotNull("Generated lock should be available from BO Service", lock);
        assertNotNull("Generated lock should have id", lock.getId());
        assertEquals("Document Number should match", documentNumber, lock.getDocumentNumber());
        assertNotNull("Generated lock should have a generated timestamp ", lock.getGeneratedTimestamp());
        assertEquals("Generated lock should have lock descriptor set", lockDescriptor, lock.getLockDescriptor());
        assertEquals("Generated lock should be owned by current user", GlobalVariables.getUserSession().getPerson().getPrincipalName(), lock.getOwnedByUser().getPrincipalName());
    }

    /**
     * tests retrieving {@link PessimisticLock} objects by document number
     *
     * @throws Exception
     */
    @UnitTestData(
            sqlStatements = {
                    @UnitTestSql("DELETE FROM KRNS_PESSIMISTIC_LOCK_T"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1111, 'fbcb0362-7dfb-102c-97b6-ed716fdaf540', 0, NULL, '1234', {d '2007-07-01'}, 'fran', 'aa5d6c83-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1112, '055bef4a-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1237', {d '2007-10-01'}, 'frank', 'dd5d6c83-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1113, '0e0144ec-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1236', {d '2007-10-01'}, 'frank', 'dd5d6c83-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1114, '1891526c-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1237', {d '2007-08-01'}, 'fred', 'ab4d6c83-4d0f-4309-a528-c2f81ec00396')")
                    }
            )
    @Test
    public void testGetPessimisticLocksForDocument() throws Exception {
        PessimisticLockService lockService = KRADServiceLocatorWeb.getPessimisticLockService();
        String docId = "1234";
        assertEquals("Document " + docId + " expected lock count incorrect", 1, lockService.getPessimisticLocksForDocument(docId).size());
        docId = "1237";
        assertEquals("Document " + docId + " expected lock count incorrect", 2, lockService.getPessimisticLocksForDocument(docId).size());
        docId = "1236";
        assertEquals("Document " + docId + " expected lock count incorrect", 1, lockService.getPessimisticLocksForDocument(docId).size());
        docId = "3948";
        assertEquals("Document " + docId + " expected lock count incorrect", 0, lockService.getPessimisticLocksForDocument(docId).size());
    }

    /**
     * This method tests retrieving {@link PessimisticLock} objects by session id
     *
     * @throws Exception
     */
    @UnitTestData(
            sqlStatements = {
                    @UnitTestSql("DELETE FROM KRNS_PESSIMISTIC_LOCK_T"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1111, '24c40cd2-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1234', {d '2007-07-01'}, 'fran', 'ad4d6c83-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1112, '32602e8e-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1235', {d '2007-10-01'}, 'fran', 'bc5d6c66-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1113, '3acfc1ce-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1236', {d '2007-10-01'}, 'fran', 'ad4d6c83-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1114, '463cc642-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1237', {d '2007-08-01'}, 'fran', 'bc5d6c66-4d0f-4309-a528-c2f81ec00396')")
            }
    )
    @Test
    public void testGetPessimisticLocksForSession() throws Exception {
        List<PessimisticLock> locks = KRADServiceLocatorWeb.getPessimisticLockService().getPessimisticLocksForSession(sessionId);
        assertEquals("Should return 2 locks for session " + sessionId, 2, locks.size());

        ArrayList<String> documentNumbers = new ArrayList<String>();
        for (PessimisticLock lock : locks) {
            documentNumbers.add(lock.getDocumentNumber());
        }
        assertTrue("Locks should contain a lock for document number 1234 but contained " + documentNumbers, documentNumbers.contains("1234"));
        assertTrue("Locks should contain a lock for document number 1236 but contained " + documentNumbers, documentNumbers.contains("1236"));
    }

    /**
     * This method tests releasing {@link PessimisticLock} objects for a specific user
     *
     * @throws Exception
     */
    @UnitTestData(
            sqlStatements = {
                    @UnitTestSql("DELETE FROM KRNS_PESSIMISTIC_LOCK_T"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1111, '24c40cd2-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1234', {d '2007-07-01'}, 'fran', 'ad4d6c83-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1112, '32602e8e-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1235', {d '2007-10-01'}, 'frank', 'bc5d6c66-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1113, '3acfc1ce-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1236', {d '2007-10-01'}, 'frank', 'bc5d6c66-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1114, '463cc642-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1237', {d '2007-08-01'}, 'fred', 'dd5d6c66-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1115, '4e66c4b2-7dfc-102c-97b6-ed716fdaf540', 0, 'Temporary Lock', '1234', {d '2007-07-01'}, 'fran', 'ad4d6c83-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1116, '55d99b02-7dfc-102c-97b6-ed716fdaf540', 0, 'Temporary Lock', '1235', {d '2007-10-01'}, 'frank', 'bc5d6c66-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1117, '5e47fb26-7dfc-102c-97b6-ed716fdaf540', 0, 'Temporary Lock', '1236', {d '2007-10-01'}, 'frank', 'bc5d6c66-4d0f-4309-a528-c2f81ec00396')"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1118, '65c366d8-7dfc-102c-97b6-ed716fdaf540', 0, 'Temporary Lock', '1237', {d '2007-08-01'}, 'fred', 'dd5d6c66-4d0f-4309-a528-c2f81ec00396')")
                    }
            )
    @Test
    public void testReleaseAllLocksForUser() throws Exception {
        String lockDescriptor = "Temporary Lock";
        List<PessimisticLock> locks = (List<PessimisticLock>) KRADServiceLocator.getBusinessObjectService().findAll(PessimisticLock.class);
        assertEquals("Should be 8 manually inserted locks", 8, locks.size());

        KRADServiceLocatorWeb.getPessimisticLockService().releaseAllLocksForUser(locks, KimApiServiceLocator.getPersonService().getPerson("fran"), lockDescriptor);
        locks = (List<PessimisticLock>) KRADServiceLocator.getBusinessObjectService().findAll(PessimisticLock.class);
        assertEquals("Should be 7 locks left after releasing locks for fran using lock descriptor " + lockDescriptor, 7, locks.size());

        KRADServiceLocatorWeb.getPessimisticLockService().releaseAllLocksForUser(locks, KimApiServiceLocator.getPersonService().getPerson("frank"), lockDescriptor);
        locks = (List<PessimisticLock>) KRADServiceLocator.getBusinessObjectService().findAll(PessimisticLock.class);
        assertEquals("Should be 5 locks left after releasing locks for fran and frank using lock descriptor " + lockDescriptor, 5, locks.size());

        KRADServiceLocatorWeb.getPessimisticLockService().releaseAllLocksForUser(locks, KimApiServiceLocator.getPersonService().getPerson("fred"), lockDescriptor);
        locks = (List<PessimisticLock>) KRADServiceLocator.getBusinessObjectService().findAll(PessimisticLock.class);
        assertEquals("Should be 4 locks left after releasing locks for fran, frank, and fred using lock descriptor " + lockDescriptor, 4, locks.size());

        KRADServiceLocatorWeb.getPessimisticLockService().releaseAllLocksForUser(locks, KimApiServiceLocator.getPersonService().getPerson("fran"));
        locks = (List<PessimisticLock>) KRADServiceLocator.getBusinessObjectService().findAll(PessimisticLock.class);
        assertEquals("Should be 3 locks left after releasing locks for fran with no lock descriptor", 3, locks.size());

        KRADServiceLocatorWeb.getPessimisticLockService().releaseAllLocksForUser(locks, KimApiServiceLocator.getPersonService().getPerson("frank"));
        locks = (List<PessimisticLock>) KRADServiceLocator.getBusinessObjectService().findAll(PessimisticLock.class);
        assertEquals("Should be 1 lock left after releasing locks for fran and frank with no lock descriptor", 1, locks.size());

        KRADServiceLocatorWeb.getPessimisticLockService().releaseAllLocksForUser(locks, KimApiServiceLocator.getPersonService().getPerson("fred"));
        locks = (List<PessimisticLock>) KRADServiceLocator.getBusinessObjectService().findAll(PessimisticLock.class);
        assertEquals("Should be no locks left after releasing locks for fran, frank, and fred with no lock descriptor", 0, locks.size());
    }

    /**
     * tests saving {@link PessimisticLock} objects
     *
     * @throws Exception
     */
    @UnitTestData(
            sqlStatements = {
                    @UnitTestSql("DELETE FROM KRNS_PESSIMISTIC_LOCK_T"),
                    @UnitTestSql("INSERT INTO KRNS_PESSIMISTIC_LOCK_T (PESSIMISTIC_LOCK_ID,OBJ_ID,VER_NBR,LOCK_DESC_TXT,DOC_HDR_ID,GNRT_DT,PRNCPL_ID,SESN_ID) VALUES (1111, '73f340de-7dfc-102c-97b6-ed716fdaf540', 0, NULL, '1234', {d '2007-07-01'}, 'fran', 'ad4d6c83-4d0f-4309-a528-c2f81ec00396')")
                    }
            )
    @Test
    public void testSaveLock() throws Exception {
        String lockDescriptor = "new test lock descriptor";
        // get existing lock and update lock descriptor and save
        Map primaryKeys = new HashMap();
        primaryKeys.put(KRADPropertyConstants.ID, Long.valueOf("1111"));
        PessimisticLock lock = (PessimisticLock) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(PessimisticLock.class, primaryKeys);
        lock.setLockDescriptor(lockDescriptor);
        KRADServiceLocatorWeb.getPessimisticLockService().save(lock);

        // verify retrieved lock has lock descriptor set previously
        PessimisticLock savedLock = (PessimisticLock) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(PessimisticLock.class, primaryKeys);
        assertEquals("Lock descriptor is not correct from lock that was saved", lockDescriptor, savedLock.getLockDescriptor());
    }
    
    /**
     * tests the PessimisticLockService.establishLocks method and the PessimisticLockService.getDocumentActions method
     * 
     * @throws Exception
     */
    @Test
    public void testEstablishLocks() throws Exception {
    	PessimisticLockService lockService = KRADServiceLocatorWeb.getPessimisticLockService();
    	AccountRequestDocument accountDoc = (AccountRequestDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument("AccountRequest");
    	
    	assertTrue("The AccountRequestDocument should be using pessimistic locking",
    			KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry(accountDoc.getClass().getName()).getUsePessimisticLocking());
    	
    	// Have "quickstart" establish a pessimistic lock on the account request document.
    	UserSession quickstartSession = new UserSession("quickstart");
    	Person[] quickstartPerson = { quickstartSession.getPerson() };
    	Map<String,String> editMode = new HashMap<String,String>();
    	editMode.put(AuthorizationConstants.EditMode.FULL_ENTRY, KRADConstants.KUALI_DEFAULT_TRUE_VALUE);
    	Map <?,?> finalModes = lockService.establishLocks(accountDoc, editMode, quickstartSession.getPerson());
    	
    	// Verify that the lock was actually established.
    	assertCorrectLocksAreInPlace(true, finalModes, 1, accountDoc.getPessimisticLocks(), quickstartPerson, null);
    	
    	// Now check to make sure that a different user (such as "admin") cannot save, route, cancel, or blanket approve the document.
    	UserSession adminSession = new UserSession("admin");
    	Set<String> documentActions = new HashSet<String>(Arrays.asList(new String[] { KRADConstants.KUALI_ACTION_CAN_CANCEL,
    			KRADConstants.KUALI_ACTION_CAN_SAVE, KRADConstants.KUALI_ACTION_CAN_ROUTE, KRADConstants.KUALI_ACTION_CAN_BLANKET_APPROVE }));
    	Set<?> finalActions = lockService.getDocumentActions(accountDoc, adminSession.getPerson(), documentActions);
    	assertFalse("'admin' should not be able to cancel the locked document", finalActions.contains(KRADConstants.KUALI_ACTION_CAN_CANCEL));
    	assertFalse("'admin' should not be able to save the locked document", finalActions.contains(KRADConstants.KUALI_ACTION_CAN_SAVE));
    	assertFalse("'admin' should not be able to route the locked document", finalActions.contains(KRADConstants.KUALI_ACTION_CAN_ROUTE));
    	assertFalse("'admin' should not be able to blanket approve the locked document", finalActions.contains(KRADConstants.KUALI_ACTION_CAN_BLANKET_APPROVE));
    	
    	// Verify that "quickstart" can save, route, and cancel the document since he is the owner of the lock.
    	documentActions = new HashSet<String>(Arrays.asList(new String[] {
    			KRADConstants.KUALI_ACTION_CAN_CANCEL, KRADConstants.KUALI_ACTION_CAN_SAVE, KRADConstants.KUALI_ACTION_CAN_ROUTE }));
    	finalActions = lockService.getDocumentActions(accountDoc, quickstartSession.getPerson(), documentActions);
    	assertTrue("'quickstart' should have had cancel privileges", finalActions.contains(KRADConstants.KUALI_ACTION_CAN_CANCEL));
    	assertTrue("'quickstart' should have had save privileges", finalActions.contains(KRADConstants.KUALI_ACTION_CAN_SAVE));
    	assertTrue("'quickstart' should have had route privileges", finalActions.contains(KRADConstants.KUALI_ACTION_CAN_ROUTE));
    	
    	// Check that "admin" cannot establish a lock when one is already in place.
    	editMode = new HashMap<String,String>();
    	editMode.put(AuthorizationConstants.EditMode.FULL_ENTRY, KRADConstants.KUALI_DEFAULT_TRUE_VALUE);
    	finalModes = lockService.establishLocks(accountDoc, editMode, adminSession.getPerson());
    	assertCorrectLocksAreInPlace(false, finalModes, 1, accountDoc.getPessimisticLocks(), quickstartPerson, null);
    	
    	// Make sure that "quickstart" cannot create a second lock if custom lock descriptors are not in use.
    	editMode = new HashMap<String,String>();
    	editMode.put(AuthorizationConstants.EditMode.FULL_ENTRY, KRADConstants.KUALI_DEFAULT_TRUE_VALUE);
    	finalModes = lockService.establishLocks(accountDoc, editMode, quickstartSession.getPerson());
    	assertCorrectLocksAreInPlace(true, finalModes, 1, accountDoc.getPessimisticLocks(), quickstartPerson, null);
    }
    
    /**
     * tests the PessimistLockService's workflow pessimistic locking capabilities
     * 
     * @throws Exception
     */
    @Test
    public void testWorkflowPessimisticLocking() throws Exception {
    	PessimisticLockService lockService = KRADServiceLocatorWeb.getPessimisticLockService();
    	AccountRequestDocument accountDoc = (AccountRequestDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument("AccountRequest");
    	assertTrue("The AccountRequestDocument should be using pessimistic locking",
    			KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry(accountDoc.getClass().getName()).getUsePessimisticLocking());
    	
    	// Have the system user create a workflow pessimistic lock.
    	UserSession systemSession = new UserSession(KRADConstants.SYSTEM_USER);
    	Person[] systemPerson = { systemSession.getPerson() };
    	lockService.establishWorkflowPessimisticLocking(accountDoc);
       	assertCorrectLocksAreInPlace(false, null, 1, accountDoc.getPessimisticLocks(), systemPerson, null);
       	
       	// Make sure that no other users can lock when the workflow lock is in place.
       	UserSession adminSession = new UserSession("admin");
    	Map<String,String> editMode = new HashMap<String,String>();
    	editMode.put(AuthorizationConstants.EditMode.FULL_ENTRY, KRADConstants.KUALI_DEFAULT_TRUE_VALUE);
    	Map<?,?> finalModes = lockService.establishLocks(accountDoc, editMode, adminSession.getPerson());
    	assertCorrectLocksAreInPlace(false, finalModes, 1, accountDoc.getPessimisticLocks(), systemPerson, null);
       	
       	// Ensure that workflow pessimistic locks can also be released.
       	lockService.releaseWorkflowPessimisticLocking(accountDoc);
       	assertTrue("There should not be any pessimistic locks present on the document", accountDoc.getPessimisticLocks().isEmpty());
    }
    
    /**
     * tests the PessimisticLockService's ability to establish pessimistic locks for documents supporting custom lock descriptors
     * 
     * @throws Exception
     */
    @Test
    public void testPessimisticLockingWithCustomDocumentLockDescriptors() throws Exception {
       	AccountRequestDocument2 accountDoc2 = (AccountRequestDocument2) KRADServiceLocatorWeb.getDocumentService().getNewDocument("AccountRequest2");
       	assertTrue("The AccountRequestDocument2 should be using pessimistic locking", KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary(
       			).getDocumentEntry(accountDoc2.getClass().getName()).getUsePessimisticLocking());
       	assertTrue("The AccountRequestDocument2 should be using custom lock descriptors", accountDoc2.useCustomLockDescriptors());
       	
       	// Perform the custom lock descriptor unit testing operations.
       	assertCustomLockDescriptorsAreWorking(accountDoc2, AccountRequestDocument2.ACCT_REQ_DOC_2_EDITABLE_FIELDS,
       			AccountRequestDocument2.EDIT_ALL_BUT_REASONS, AccountRequestDocument2.EDIT_REASONS_ONLY);
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
