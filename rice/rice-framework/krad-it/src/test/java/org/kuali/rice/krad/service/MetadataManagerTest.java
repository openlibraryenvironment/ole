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
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.coreservice.impl.parameter.ParameterId;
import org.kuali.rice.coreservice.impl.parameter.ParameterTypeBo;
import org.kuali.rice.core.framework.persistence.jpa.metadata.MetadataManager;
import org.kuali.rice.krad.test.document.bo.Account;
import org.kuali.rice.krad.test.document.bo.AccountExtension;
import org.kuali.rice.location.impl.country.CountryBo;
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

import static org.junit.Assert.*;

/**
 * MetadataManagerTest tests {@link MetadataManager} methods
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
public class MetadataManagerTest extends KRADTestCase {
	/**
	 * Tests that MetadataManager can convert a primary key Map to a key object correctly
	 */
	@Test
	public void testPKMapToObject() {
		Map<String, Object> pkMap = new HashMap<String, Object>();
		Object pkValue = MetadataManager.convertPrimaryKeyMapToObject(CountryBo.class, pkMap);
		assertNull("An empty map should return a null key", pkValue);
		
		pkMap.put("code", "AN");
		pkValue = MetadataManager.convertPrimaryKeyMapToObject(CountryBo.class, pkMap);
		assertEquals("Single pkValue should be of class String", String.class, pkValue.getClass());
		assertEquals("Single pkValue should be \"AN\"", "AN", pkValue);
		
		pkMap.put("name", "ANDORRA");
		boolean exceptionThrown = false;
		try {
			pkValue = MetadataManager.convertPrimaryKeyMapToObject(CountryBo.class, pkMap);
		} catch (IllegalArgumentException iae) {
			exceptionThrown = true;
		}
		assertTrue("Multiple keys did not lead to exception", exceptionThrown);
		
		pkMap.clear();
		
		pkMap.put("countryCode", "US");
		pkMap.put("code", "WV");
		pkValue = MetadataManager.convertPrimaryKeyMapToObject(StateBo.class, pkMap);
		org.junit.Assert.assertEquals("Composite pkValue for State should have class of StateId", StateId.class, pkValue.getClass());
		StateId stateId = (StateId)pkValue;
		assertEquals("Country code was not correctly set", "US", stateId.getCountryCode());
		assertEquals("State code was not correctly set", "WV", stateId.getCode());
		
		pkMap.put("name", "WEST VIRGINIA");
		exceptionThrown = false;
		try {
			pkValue = MetadataManager.convertPrimaryKeyMapToObject(StateBo.class, pkMap);
		} catch (Exception e) {
			exceptionThrown = true;
		}
		assertTrue("Non primary key field caused exception", exceptionThrown);
	}
	
	/**
	 * Tests that MetadataManager.getEntityPrimaryKeyObject correctly pulls primary keys
	 * from a BO
	 */
	@Test
	public void testPKObjectForEntity() {
		ParameterTypeBo parameterType = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(ParameterTypeBo.class, "CONFG");
		assertNotNull("ParameterType should not be null", parameterType);
		
		Object pkValue = MetadataManager.getEntityPrimaryKeyObject(parameterType);
		assertEquals("Single pkValue should be of class String", String.class, pkValue.getClass());
		assertEquals("Single pkValue should be \"CONFG\"", "CONFG", pkValue);
		
		Parameter parameter = CoreFrameworkServiceLocator.getParameterService().getParameter("KR-NS", "Lookup", "MULTIPLE_VALUE_RESULTS_PER_PAGE");
		assertNotNull("State should not be null", parameter);
		
		pkValue = MetadataManager.getEntityPrimaryKeyObject(ParameterBo.from(parameter));
		org.junit.Assert.assertEquals("Composite pkValue for Parameter should have class of ParameterId", ParameterId.class, pkValue.getClass());
		ParameterId parameterId = (ParameterId)pkValue;
		assertEquals("namespace code was not correctly set", "KR-NS", parameterId.getNamespaceCode());
		assertEquals("parameter detail type code was not correctly set", "Lookup", parameterId.getComponentCode());
		assertEquals("parameter name was not correctly set", "MULTIPLE_VALUE_RESULTS_PER_PAGE", parameterId.getName());
		assertEquals("parameterApplicationNamespaceCode was not correctly set", "KUALI", parameterId.getApplicationId());
	}
	
	/**
	 * Tests that MetadataManager.getPersistableBusinessObjectPrimaryKeyObjectWithValuesForExtension works
	 */
	@Test
	public void testPKObjectForExtension() {
		final Account account = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(Account.class, "b101");
		assertNotNull("Account should not be null", account);
		final AccountExtension accountExtension = (AccountExtension)account.getExtension();
		
		final Object pkValue = MetadataManager.getPersistableBusinessObjectPrimaryKeyObjectWithValuesForExtension(account, accountExtension);
		assertEquals("Single pkValue should be of class String", String.class, pkValue.getClass());
		assertEquals("Single pkValue should be \"b101\"", "b101", pkValue);
	}
}
