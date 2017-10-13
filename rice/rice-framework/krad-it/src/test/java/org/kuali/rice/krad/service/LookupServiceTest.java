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
import org.kuali.rice.krad.test.document.bo.Account;
import org.kuali.rice.krad.test.document.bo.AccountManager;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.data.PerTestUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestFile;
import org.kuali.rice.test.data.UnitTestSql;
import org.kuali.rice.krad.test.KRADTestCase;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * LookupServiceTest tests KULRICE-984: Lookups - Relative Limit Gap
 *
 * <p>Making sure that lookup resultSetLimits set in the DD for
 * a BO will override the system wide default.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

@PerTestUnitTestData(
        value = @UnitTestData(
                order = {UnitTestData.Type.SQL_STATEMENTS, UnitTestData.Type.SQL_FILES},
                sqlStatements = {
                        @UnitTestSql("delete from trv_acct where acct_fo_id between 101 and 301")
                        ,@UnitTestSql("delete from trv_acct_fo where acct_fo_id between 101 and 301")
                },
                sqlFiles = {
                        @UnitTestFile(filename = "classpath:testAccountManagers.sql", delimiter = ";")
                        , @UnitTestFile(filename = "classpath:testAccounts.sql", delimiter = ";")
                }
        ),
        tearDown = @UnitTestData(
                sqlStatements = {
                        @UnitTestSql("delete from trv_acct where acct_fo_id between 101 and 301")
                        ,@UnitTestSql("delete from trv_acct_fo where acct_fo_id between 101 and 301")
                }
       )
)
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class LookupServiceTest extends KRADTestCase {

    public LookupServiceTest() {}

    /**
     * tests lookup return limits
     *
     * @throws Exception
     */
    @Test
    public void testLookupReturnLimits() throws Exception {
        LookupService lookupService = KRADServiceLocatorWeb.getLookupService();
        Map formProps = new HashMap();
        Collection accountManagers = lookupService.findCollectionBySearchHelper(AccountManager.class, formProps, false);
        assertEquals(90, accountManagers.size());

        accountManagers = null;
        accountManagers = lookupService.findCollectionBySearch(AccountManager.class, formProps);
        assertEquals(90, accountManagers.size());
    }

    /**
     * tests a lookup with the default limit
     *
     * @throws Exception
     */
    @Test
    public void testLookupReturnDefaultLimit() throws Exception {
        LookupService lookupService = KRADServiceLocatorWeb.getLookupService();
        Map formProps = new HashMap();
        Collection travelAccounts = lookupService.findCollectionBySearchHelper(Account.class, formProps, false);
        assertEquals(200, travelAccounts.size());

        travelAccounts = null;
        travelAccounts = lookupService.findCollectionBySearch(Account.class, formProps);
        assertEquals(200, travelAccounts.size());
    }

    /**
     * tests an unbounded lookup
     *
     * @throws Exception
     */
    @Test
    public void testLookupReturnDefaultUnbounded() throws Exception {
        LookupService lookupService = KRADServiceLocatorWeb.getLookupService();
        Map formProps = new HashMap();
        Collection accountManagers = lookupService.findCollectionBySearchHelper(AccountManager.class, formProps, true);
        int size = accountManagers.size();
        assertTrue("# of Fiscal Officers should be > 200", size > 200);

        accountManagers = null;
        accountManagers = lookupService.findCollectionBySearchUnbounded(AccountManager.class, formProps);
        size = accountManagers.size();
        assertTrue("# of Fiscal Officers should be > 200", size > 200);
    }

    /**
     * tests an unbounded lookup
     *
     * @throws Exception
     */
    @Test
    public void testLookupReturnDefaultUnbounded2() throws Exception {
        LookupService lookupService = KRADServiceLocatorWeb.getLookupService();
        Map formProps = new HashMap();
        Collection travelAccounts = lookupService.findCollectionBySearchHelper(Account.class, formProps, true);
        int size = travelAccounts.size();
        assertTrue("# of Travel Accounts should be > 200", size > 200);

        travelAccounts = null;
        travelAccounts = lookupService.findCollectionBySearchUnbounded(Account.class, formProps);
        size = travelAccounts.size();
        assertTrue("# of Travel Accounts should be > 200", size > 200);
    }

}
