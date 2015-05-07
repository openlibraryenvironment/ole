/*
 * Copyright 2006 The Kuali Foundation
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
package org.kuali.ole.coa.businessobject;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * This class...
 */
public class AccountTest extends KFSTestCaseBase {

    private static final String TEST_DATE_1_TODAY = "04/22/2002 07:48 PM";
    private static final String TEST_DATE_1_YESTERDAY = "04/21/2002 07:48 PM";
    private static final String TEST_DATE_1_TOMORROW = "04/23/2002 07:48 PM";

    private static final String TEST_DATE_2_TODAY = "04/22/2002 10:23 AM";
    private static final String TEST_DATE_2_YESTERDAY = "04/21/2002 10:23 AM";
    private static final String TEST_DATE_2_TOMORROW = "04/23/2002 10:23 AM";

    private static final String TEST_DATE_3_TODAY = "04/22/2002 06:14 AM";
    private static final String TEST_DATE_3_YESTERDAY = "04/21/2002 06:14 AM";
    private static final String TEST_DATE_3_TOMORROW = "04/23/2002 06:14 AM";

    // pass this a name, and it returns a setup timestamp instance
    private Timestamp getTimestamp(String timestampString) {
        Timestamp timestamp;
        try {
            timestamp = SpringContext.getBean(DateTimeService.class).convertToSqlTimestamp(timestampString);
        }
        catch (ParseException e) {
            assertNull("Timestamp String was not parseable", e);
            return null;
        }
        return timestamp;
    }

    private Date getDate(String dateString){
        return new Date (getTimestamp(dateString).getTime());
    }

    // since all the tests are doing the same thing, this is centralized
    private void doTest(String expirationDateString, String testDateString, boolean expectedResult) {

        Date expirationDate = getDate(expirationDateString);
        Date testDate = getDate(testDateString);

        // setup the account, and set its expiration date
        Account account = new Account();
        account.setAccountExpirationDate(expirationDate);

        // test against isExpired, and get the result
        boolean actualResult = account.isExpired(SpringContext.getBean(DateTimeService.class).getCalendar(testDate));

        // compare the result to what was expected
        assertEquals(expectedResult, actualResult);
    }

    // if date of expiration and date of today is the same date (time excluded)
    // then the account is not considered expired
    @Test
    public void testIsExpiredToday_ExpirationDateToday_ExpirationDateEarlierTime() {
        doTest(TEST_DATE_2_TODAY, TEST_DATE_1_TODAY, false);
    }

    // if date of expiration and date of today is the same date (time excluded)
    // then the account is not considered expired
    @Test
    public void testIsExpiredToday_ExpirationDateToday_ExpirationDateLaterTime() {
        doTest(TEST_DATE_2_TODAY, TEST_DATE_3_TODAY, false);
    }

    // if date of expiration is one day later than day of testDate, fail
    @Test
    public void testIsExpiredToday_ExpirationDateTomorrow() {
        doTest(TEST_DATE_2_TOMORROW, TEST_DATE_1_TODAY, false);
    }

    // if date of expiration is one day earlier than day of testDate, succeed
    @Test
    public void testIsExpiredToday_ExpirationDateYesterday() {
        doTest(TEST_DATE_2_YESTERDAY, TEST_DATE_1_TODAY, true);
    }

    /**
     *
     * This method inserts two records as temporary restricted and unrestricted and displays search results without temporary restricted record
     */
    @Test
    public void testSearch(){

        BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
        String chartOfAccountsCode = "BL";
        String accountNumber = "1222222";
        String accountName = "testTempRestricted";
        String organizationCode = "AAAI";
        Date accountEffectiveDate = SpringContext.getBean(DateTimeService.class).getCurrentSqlDate();//"03/07/2012";
        String subFundGroupCode = "AUXAMB";
        String universityAccountNumber = "1234";
        String fiscalOfficerUserName = "ole-cswinson";
        String accountRestrictedStatusCode = "T";
        Date accountRestrictedStatusDate = SpringContext.getBean(DateTimeService.class).getCurrentSqlDate();
        String accountSufficientFundsCode = "A";
        String accountExpenseGuidelineText = "test guide line text for temporary restricted";

        Chart chartOfAccounts = new Chart();
        chartOfAccounts.setChartOfAccountsCode(chartOfAccountsCode);

        AccountGuideline accountGuideline = new AccountGuideline();
        accountGuideline.setAccountExpenseGuidelineText(accountExpenseGuidelineText);
        accountGuideline.setChartOfAccountsCode(chartOfAccountsCode);
        accountGuideline.setAccountNumber(accountNumber);


        Account account = new Account();
        account.setChartOfAccounts(chartOfAccounts);
        account.setChartOfAccountsCode(chartOfAccountsCode);
        account.getChartOfAccountsCode();
        account.setAccountNumber(accountNumber);
        account.setAccountName(accountName);
        account.setOrganizationCode(organizationCode);
        account.setAccountEffectiveDate(accountEffectiveDate);
        account.setSubFundGroupCode(subFundGroupCode);
        account.setUniversityAccountNumber(universityAccountNumber);
        ((PersonImpl)account.getAccountFiscalOfficerUser()).setName(fiscalOfficerUserName);
        account.setAccountRestrictedStatusCode(accountRestrictedStatusCode);
        account.setAccountRestrictedStatusDate(accountRestrictedStatusDate);
        account.setAccountSufficientFundsCode(accountSufficientFundsCode);
        account.setAccountGuideline(accountGuideline);

        boService.save(account);

        String chartOfAccountsCode1 = "BL";
        String accountNumber1 = "1222223";
        String accountName1 = "testUnRestricted";
        String organizationCode1 = "AAAI";
        Date accountEffectiveDate1 = SpringContext.getBean(DateTimeService.class).getCurrentSqlDate();//"03/07/2012";
        String subFundGroupCode1 = "AUXAMB";
        String universityAccountNumber1 = "1245";
        String fiscalOfficerUserName1 = "ole-cswinson";
        String accountRestrictedStatusCode1 = "U";
        String accountSufficientFundsCode1 = "A";
        String accountExpenseGuidelineText1 = "test guide line text for un-restricted";

        Chart chartOfAccounts1 = new Chart();
        chartOfAccounts1.setChartOfAccountsCode(chartOfAccountsCode1);

        AccountGuideline accountGuideline1 = new AccountGuideline();
        accountGuideline1.setAccountExpenseGuidelineText(accountExpenseGuidelineText1);
        accountGuideline1.setChartOfAccountsCode(chartOfAccountsCode1);
        accountGuideline1.setAccountNumber(accountNumber1);

        Account account1 = new Account();
        account1.setChartOfAccounts(chartOfAccounts1);
        account1.setChartOfAccountsCode(chartOfAccountsCode1);
        account1.setAccountNumber(accountNumber1);
        account1.setAccountName(accountName1);
        account1.setOrganizationCode(organizationCode1);
        account1.setAccountEffectiveDate(accountEffectiveDate1);
        account1.setSubFundGroupCode(subFundGroupCode1);
        account1.setUniversityAccountNumber(universityAccountNumber1);
        ((PersonImpl)account1.getAccountFiscalOfficerUser()).setName(fiscalOfficerUserName1);
        account1.setAccountRestrictedStatusCode(accountRestrictedStatusCode1);
        account1.setAccountSufficientFundsCode(accountSufficientFundsCode1);
        account1.setAccountGuideline(accountGuideline1);

        boService.save(account1);


        List<Account> accounts = (List<Account>) boService.findAll(Account.class);
        for(int i=0;i<accounts.size();i++){
            if(accounts.get(i).getAccountNumber().equalsIgnoreCase(accountNumber) || accounts.get(i).getAccountNumber().equalsIgnoreCase(accountNumber1)){
                if(accounts.get(i).getAccountRestrictedStatusCode().equalsIgnoreCase("T")){
                    accounts.remove(i);
                }
                assertEquals(accounts.get(i).getAccountName(), accountName1);
            }
        }
    }

    /*public void testNotify(){

    }*/

}
