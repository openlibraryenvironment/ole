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
package org.kuali.rice.kns.lookup;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kns.KNSTestCase;
import org.kuali.rice.kns.lookup.KualiLookupableImpl;
import org.kuali.rice.kns.lookup.LookupableHelperService;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.test.document.bo.Account;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.data.PerTestUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestFile;
import org.kuali.rice.test.data.UnitTestSql;
import org.kuali.rice.krad.test.KRADTestCase;
import org.kuali.rice.krad.test.KRADTestConstants.TestConstants;

/**
 * KualiLookupableTest tests {@link KualiLookupableImpl} methods
 * 
 * 
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
public class KualiLookupableTest extends KNSTestCase {
    private KualiLookupableImpl lookupableImpl;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        lookupableImpl = new KualiLookupableImpl();
        lookupableImpl.setLookupableHelperService((LookupableHelperService) GlobalResourceLoader.getService(
                "lookupableHelperService"));
        try {
            lookupableImpl.setBusinessObjectClass(Account.class);
        } catch (RuntimeException re) {
            if (re.getMessage().contains("Lookup not defined for business object class org.kuali.rice.krad.test.document.bo.Account")) {
                fail("CI Failure Jira https://jira.kuali.org/browse/KULRICE-9287 " + re.getMessage() + " " + ExceptionUtils.getStackTrace(re));
            }
        }
    }

    /**
     * Tests generation of lookup form rows
     *
     * @throws Exception
     */
    @Test public void testGetRows() throws Exception {
        // rows should have been populated by business object class initialization
        List<? extends Row> rows = lookupableImpl.getRows();
        Assert.assertEquals(4, rows.size());

        Field f = rows.get(0).getField(0);
        Assert.assertEquals("number", f.getPropertyName());
        Assert.assertEquals("Account Number", f.getFieldLabel());
        Assert.assertEquals("text", f.getFieldType());

        f = rows.get(1).getField(0);
        Assert.assertEquals("name", f.getPropertyName());
        Assert.assertEquals("Account Name", f.getFieldLabel());
        Assert.assertEquals("text", f.getFieldType());

        f = rows.get(2).getField(0);
        Assert.assertEquals("extension.accountTypeCode", f.getPropertyName());
        Assert.assertEquals("Account Type Code", f.getFieldLabel());
        Assert.assertEquals("dropdown", f.getFieldType());

        f = rows.get(3).getField(0);
        Assert.assertEquals("amId", f.getPropertyName());
        Assert.assertEquals("Account Manager Id", f.getFieldLabel());
        Assert.assertEquals("text", f.getFieldType());
    }

    /**
     * Test that the return url for a business object is getting set correctly based on the defined return fields
     * 
     * @throws Exception
     */
    @Test public void testReturnUrl() throws Exception {
    	Map<String, String> lookupProps = new HashMap<String, String>();
    	lookupProps.put("number", "b101");
    	lookupProps.put("name", "b101");
    	
    	Account account = (Account) KRADServiceLocatorWeb.getLookupService().findObjectBySearch(Account.class, lookupProps);
//        ObjectCode objCode = getObjectCodeService().getCountry(TestConstants.Data1.UNIVERSITY_FISCAL_YEAR, TestConstants.Data1.CHART_OF_ACCOUNTS_CODE, TestConstants.Data1.OBJECT_CODE);

        Map fieldConversions = new HashMap();
        lookupableImpl.setDocFormKey("8888888");
        lookupableImpl.setBackLocation(TestConstants.BASE_PATH + "ib.do");

        String returnUrl = lookupableImpl.getReturnUrl(account, fieldConversions, "kualiLookupable", null).constructCompleteHtmlTag();

        // check url has our doc form key
        checkURLContains("Lookup return url does not contain docFormKey", KRADConstants.DOC_FORM_KEY + "=8888888", returnUrl);

        // check url goes back to our back location
        checkURLContains("Lookup return url does not go back to back location", TestConstants.BASE_PATH + "ib.do", returnUrl);

        Assert.assertEquals(returnUrl,
                "<a title=\"return valueAccount Number=b101 \" href=\"http://localhost:8080/ib.do?refreshCaller=kualiLookupable&number=b101&methodToCall=refresh&docFormKey=8888888\"  >return value</a>");

        // check that field conversions are working correctly for keys
        fieldConversions.put("number", "myAccount[0].chartCode");

        returnUrl = lookupableImpl.getReturnUrl(account, fieldConversions, "kualiLookupable", null).constructCompleteHtmlTag();

        // check keys have been mapped properly
        checkURLContains("Lookup return url does not map key", "myAccount[0].chartCode=b101", returnUrl);
    }


    /**
     * Checks the url string contains a substring
     * 
     * @param message - an informational message to show if the test fails
     * @param containString - the string to check for in the url
     * @param url - a url to look for a string in
     */
    private void checkURLContains(String message, String containString, String url) {
        Assert.assertTrue(message, url.indexOf(containString) > 0);
    }
}
