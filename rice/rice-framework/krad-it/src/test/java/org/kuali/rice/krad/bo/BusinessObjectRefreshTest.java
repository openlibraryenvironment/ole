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
package org.kuali.rice.krad.bo;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.test.document.bo.Account;
import org.kuali.rice.krad.test.document.bo.AccountManager;
import org.kuali.rice.location.impl.county.CountyBo;
import org.kuali.rice.location.impl.county.CountyId;
import org.kuali.rice.location.impl.state.StateBo;
import org.kuali.rice.location.impl.state.StateId;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.data.PerTestUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestFile;
import org.kuali.rice.test.data.UnitTestSql;
import org.kuali.rice.krad.test.KRADTestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests how refreshing works for Business Objects 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
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
public class BusinessObjectRefreshTest extends KRADTestCase {

	@Test
    /**
     * tests that {@link PersistableBusinessObjectBase#refreshReferenceObject(String)} works for a lazy loaded reference when the foreign key is changed
     */
	public void testLazyRefreshField() {
		final String accountNumber = "b101";
		Account account = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(Account.class, accountNumber);
		
		Assert.assertEquals("Retrieved account should have name b101", "b101", account.getName());
		Assert.assertEquals("Retrieved account should have a account manager with user name fo-101", "fo-101", account.getAccountManager().getUserName());
		
		account.setAmId(102L);
		account.refreshReferenceObject("accountManager");
		
		Assert.assertEquals("Account Manager should now have user name of fo-102", "fo-102", account.getAccountManager().getUserName());
		
	}
	
	@Test
    /**
     * tests that {@link PersistableBusinessObjectBase#refresh()} works for a lazy loaded reference when the foreign key is changed
     */
	public void testLazyRefreshWholeObject() {
		final String accountNumber = "b101";
		Account account = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(Account.class, accountNumber);
		
		Assert.assertEquals("Retrieved account should have name b101", "b101", account.getName());
		Assert.assertEquals("Retrieved account should have a account manager with user name fo-101", "fo-101", account.getAccountManager().getUserName());
		
		account.setAmId(102L);
		account.refresh();
		
		Assert.assertEquals("Account Manager should now have user name of fo-102", "fo-102", account.getAccountManager().getUserName());
	}
	
	@Ignore("until BO extensions work with JPA")
	@Test
	public void testLazyCollectionRefresh() {
		final Long fredManagerId = 101L;
		AccountManager manager = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(AccountManager.class, new Long(fredManagerId));
		
		Assert.assertEquals("Retrieve manager should have a name 'fo-101'", "fo-101", manager.getUserName());
		Assert.assertEquals("Manager should have one account", new Integer(101), new Integer(manager.getAccounts().size()));
		
		final String accountNumber = "b102";
		Account account = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(Account.class, accountNumber);

		account.setAmId(101L);
		account = (Account) KRADServiceLocator.getBusinessObjectService().save(account);
		
		manager.refreshReferenceObject("accounts");
		Assert.assertEquals("Manager should have one account", new Integer(2), new Integer(manager.getAccounts().size()));
	}

    @Test
    public void testEagerRefreshField() {
        // TODO: implement -- see testEagerRefreshEboField, but do without using EBOs
    }

	@Test
    /**
     * tests that {@link PersistableBusinessObjectBase#refresh()} works for an non lazy loaded reference when the foreign key is changed
     */
	public void testEagerRefreshEboField() {
        Map<String, String> primaryKeys = new HashMap<String, String>();
        primaryKeys.put("code", "COCONINO");
        primaryKeys.put("countryCode", "US");
        primaryKeys.put("stateCode","AZ");
		//final CountyId countyId = new CountyId("COCONINO", "US", "AZ");
		CountyBo county = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(CountyBo.class, primaryKeys);

        primaryKeys.clear();
        primaryKeys.put("countryCode","US");
        primaryKeys.put("code","AZ");
		//final StateId arizonaStateId = new StateId("US", "AZ");
		final StateBo arizonaState = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(StateBo.class, primaryKeys);
		
		Assert.assertEquals("On retrieval from database, state code should be AZ", arizonaState.getCode(), county.getState().getCode());
		Assert.assertEquals("On retrieval from database, state name should be ARIZONA", arizonaState.getName(), county.getState().getName());
		
		county.setStateCode("CA");
		county.setCode("VENTURA");
        // NOTE: since county is an EBO, whether or not refresh() fetches references is an implementation choice in the LocationModuleService
		county.refresh();
		
		//final StateId californiaStateId = new StateId("US", "CA");
        primaryKeys.clear();
        primaryKeys.put("countryCode","US");
        primaryKeys.put("code","CA");
		final StateBo californiaState = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(StateBo.class, primaryKeys);
		
		Assert.assertEquals("Does eager fetching automatically refresh?", californiaState.getCode(), county.getState().getCode());
		Assert.assertEquals("On refresh, state name should be CALIFORNIA", californiaState.getName(), county.getState().getName());
	}
}
