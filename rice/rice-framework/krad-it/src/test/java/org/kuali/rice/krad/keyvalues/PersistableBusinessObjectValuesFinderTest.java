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
package org.kuali.rice.krad.keyvalues;

import org.junit.Test;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.PersistableBusinessObjectValuesFinder;
import org.kuali.rice.krad.test.document.bo.AccountManager;
import org.kuali.rice.krad.test.document.bo.AccountType;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.data.PerTestUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestFile;
import org.kuali.rice.test.data.UnitTestSql;
import org.kuali.rice.krad.test.KRADTestCase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * PersistableBusinessObjectValuesFinderTest tests the {@link PersistableBusinessObjectValuesFinder}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@PerTestUnitTestData(
        value = @UnitTestData(
                order = {UnitTestData.Type.SQL_STATEMENTS, UnitTestData.Type.SQL_FILES},
                sqlStatements = {
                        @UnitTestSql("delete from trv_acct_type")
                        ,@UnitTestSql("delete from trv_acct_fo where acct_fo_id between 101 and 301")
                },
                sqlFiles = {
                        @UnitTestFile(filename = "classpath:testAccountManagers.sql", delimiter = ";")
                        , @UnitTestFile(filename = "classpath:testAccountType.sql", delimiter = ";")
                }
        ),
        tearDown = @UnitTestData(
                sqlStatements = {
                        @UnitTestSql("delete from trv_acct_type")
                        ,@UnitTestSql("delete from trv_acct_fo where acct_fo_id between 101 and 301")
                }
       )
)
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class PersistableBusinessObjectValuesFinderTest extends KRADTestCase {

    private List<KeyValue> testKeyValues = new ArrayList<KeyValue>();
    private List<KeyValue> testKeyValuesKeyInLabel = new ArrayList<KeyValue>();
    private List<KeyValue> testKeyValuesLongKey = new ArrayList<KeyValue>();
    private List<KeyValue> testKeyValuesKeyInLabelLongKey = new ArrayList<KeyValue>();

    /**
     * Default Constructor builds KeyValue Lists used for tests.
     *
     */
    public PersistableBusinessObjectValuesFinderTest() {
    	testKeyValues.add(new ConcreteKeyValue("CAT", "Clearing Account Type"));
    	testKeyValues.add(new ConcreteKeyValue("EAT", "Expense Account Type"));
    	testKeyValues.add(new ConcreteKeyValue("IAT", "Income Account Type"));
    
    	testKeyValuesKeyInLabel.add(new ConcreteKeyValue("CAT", "CAT - Clearing Account Type"));
    	testKeyValuesKeyInLabel.add(new ConcreteKeyValue("EAT", "EAT - Expense Account Type"));
    	testKeyValuesKeyInLabel.add(new ConcreteKeyValue("IAT", "IAT - Income Account Type"));
     
    	
    	for (int x=101;x<302;x++) {
    	    testKeyValuesLongKey.add(new ConcreteKeyValue(new Long(x).toString(), "fo-" + x));
    	    testKeyValuesKeyInLabelLongKey.add(new ConcreteKeyValue(new Long(x).toString(), x + " - fo-" + x));
    	}
    }

    /**
     * tests to make sure the <code>PersistableBusinessObjectValuesFinder</code> works
     * as expected for the TravelAccountType BO
     *
     * @throws Exception
     */
    @Test public void testGetKeyValues() throws Exception {
    	PersistableBusinessObjectValuesFinder valuesFinder = new PersistableBusinessObjectValuesFinder();
    	valuesFinder.setBusinessObjectClass(AccountType.class);
    	valuesFinder.setKeyAttributeName("accountTypeCode");
    	valuesFinder.setLabelAttributeName("name");
    	valuesFinder.setIncludeKeyInDescription(false);
    	List<KeyValue> keyValues = valuesFinder.getKeyValues();
    	assertEquals(testKeyValues.size(), keyValues.size());
    	for (KeyValue testKeyValue: testKeyValues) {
                assertEquals(testKeyValue.getValue(), valuesFinder.getKeyLabel(testKeyValue.getKey()));
    	}
    }

    /**
     * tests to make sure the <code>PersistableBusinessObjectValuesFinder</code> works
     * as expected for the TravelAccountType BO with the key included in the label
     *
     * @throws Exception
     */
    @Test public void testGetKeyValuesKeyInLabel() throws Exception {
    	PersistableBusinessObjectValuesFinder valuesFinder = new PersistableBusinessObjectValuesFinder();
    	valuesFinder.setBusinessObjectClass(AccountType.class);
    	valuesFinder.setKeyAttributeName("accountTypeCode");
    	valuesFinder.setLabelAttributeName("name");
    	valuesFinder.setIncludeKeyInDescription(true);
    	List<KeyValue> keyValues = valuesFinder.getKeyValues();
    	assertEquals(testKeyValuesKeyInLabel.size(), keyValues.size());
    	for (KeyValue testKeyValue: testKeyValuesKeyInLabel) {
                assertEquals(testKeyValue.getValue(), valuesFinder.getKeyLabel(testKeyValue.getKey()));
    	}
    }

    /**
     * tests to make sure the <code>PersistableBusinessObjectValuesFinder</code> works
     * as expected for the FiscalOfficer BO.
     *
     * @throws Exception
     */
    @Test public void testGetKeyValuesLongKey() throws Exception {
    	PersistableBusinessObjectValuesFinder valuesFinder = new PersistableBusinessObjectValuesFinder();
    	valuesFinder.setBusinessObjectClass(AccountManager.class);
    	valuesFinder.setKeyAttributeName("amId");
    	valuesFinder.setLabelAttributeName("userName");
    	valuesFinder.setIncludeKeyInDescription(false);
    	List<KeyValue> keyValues = valuesFinder.getKeyValues();
    	assertEquals(testKeyValuesLongKey.size(), keyValues.size());
    	for (KeyValue testKeyValue: testKeyValuesLongKey) {
                assertEquals(testKeyValue.getValue(), valuesFinder.getKeyLabel(testKeyValue.getKey()));
    	}
    }

    /**
     * tests that <code>PersistableBusinessObjectValuesFinder</code> works
     * as expected for the FiscalOfficer BO with the key included in the label
     *
     * @throws Exception
     */
    @Test public void testGetKeyValuesKeyInLabelLongKey() throws Exception {
    	PersistableBusinessObjectValuesFinder valuesFinder = new PersistableBusinessObjectValuesFinder();
    	valuesFinder.setBusinessObjectClass(AccountManager.class);
    	valuesFinder.setKeyAttributeName("amId");
    	valuesFinder.setLabelAttributeName("userName");
    	valuesFinder.setIncludeKeyInDescription(true);
    	List<KeyValue> keyValues = valuesFinder.getKeyValues();
    	assertEquals(testKeyValuesKeyInLabelLongKey.size(), keyValues.size());
    	for (KeyValue testKeyValue: testKeyValuesKeyInLabelLongKey) {
                assertEquals(testKeyValue.getValue(), valuesFinder.getKeyLabel(testKeyValue.getKey()));
    	}
    }

}
