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

import org.junit.Ignore;
import org.kuali.rice.krad.test.KRADTestCase;


/**
 * Test case for InactivateableFromToService. Note on usage that the tests sets the activeAsOfDate so that the results
 * will be consistent over time. However this is not necessary and the service will default to using the current date if
 * not given.
 * 
 * @see org.kuali.rice.krad.service.InactivateableFromToService
 */
/*@PerTestUnitTestData(
        value = @UnitTestData(
                order = {UnitTestData.Type.SQL_STATEMENTS, UnitTestData.Type.SQL_FILES},
                sqlStatements = {
                        @UnitTestSql("delete from trv_acct_use_rt_t")
                },
                sqlFiles = {
                        @UnitTestFile(filename = "classpath:testAccountUseRate.sql", delimiter = ";")
                }
        ),
        tearDown = @UnitTestData(
                sqlStatements = {
                		@UnitTestSql("delete from trv_acct_use_rt_t")
                }
       )
)*/
@Ignore
public class InactivateableFromToServiceTest extends KRADTestCase {


//	/**
//	 * Test finding active records
//	 *
//	 * @see InactivateableFromToService#findMatchingActive(Class, Map)
//	 */
//	@Test
//	public void testFindMatchingActive() throws Exception {
//		InactivateableFromToService inactivateableFromToService = KRADServiceLocatorInternal.getInactivateableFromToService();
//
//		Map fieldValues = new HashMap();
//		fieldValues.put(KNSPropertyConstants.ACTIVE_AS_OF_DATE, "04/01/2010");
//		fieldValues.put("number", "a2");
//
//		List<InactivateableFromTo> results = inactivateableFromToService.findMatchingActive(TravelAccountUseRate.class,
//				fieldValues);
//		assertEquals(2, results.size());
//
//		TravelAccountUseRate useRate = (TravelAccountUseRate) results.get(0);
//		assertTrue("Incorrect active records returned, do not match expected ids",
//				"2".equals(useRate.getId()) || "3".equals(useRate.getId()));
//
//		useRate = (TravelAccountUseRate) results.get(1);
//		assertTrue("Incorrect active records returned, do not match expected ids",
//				"2".equals(useRate.getId()) || "3".equals(useRate.getId()));
//
//		fieldValues = new HashMap();
//		fieldValues.put(KNSPropertyConstants.ACTIVE_AS_OF_DATE, "04/01/2010");
//		fieldValues.put("number", "a1");
//
//		results = inactivateableFromToService.findMatchingActive(TravelAccountUseRate.class, fieldValues);
//		assertEquals(1, results.size());
//
//		useRate = (TravelAccountUseRate) results.get(0);
//		assertTrue("Incorrect active record returned, does not match expected id", "1".equals(useRate.getId()));
//
//		fieldValues = new HashMap();
//		fieldValues.put(KNSPropertyConstants.ACTIVE_AS_OF_DATE, "01/01/2010 01:31 PM");
//		fieldValues.put("number", "b1");
//
//		results = inactivateableFromToService.findMatchingActive(TravelAccountUseRate.class, fieldValues);
//		assertEquals(1, results.size());
//
//		useRate = (TravelAccountUseRate) results.get(0);
//		assertTrue("Incorrect active record returned, does not match expected id", "9".equals(useRate.getId()));
//	}
//
//	/**
//	 * Test finding inactive records through LookupService
//	 */
//	@Test
//	public void testFindMatchingActive_inactive() throws Exception {
//		LookupService lookupService = KRADServiceLocatorWeb.getLookupService();
//
//		Map fieldValues = new HashMap();
//		fieldValues.put(KNSPropertyConstants.ACTIVE, "N");
//		fieldValues.put(KNSPropertyConstants.ACTIVE_AS_OF_DATE, "04/01/2010");
//		fieldValues.put("number", "a2");
//
//		List<InactivateableFromTo> results = (List<InactivateableFromTo>) lookupService.findCollectionBySearchUnbounded(TravelAccountUseRate.class,
//				fieldValues);
//		assertEquals(1, results.size());
//
//		TravelAccountUseRate useRate = (TravelAccountUseRate) results.get(0);
//		assertTrue("Incorrect inactive record returned, does not match expected id",
//				"4".equals(useRate.getId()));
//
//		fieldValues = new HashMap();
//		fieldValues.put(KNSPropertyConstants.ACTIVE, "N");
//		fieldValues.put(KNSPropertyConstants.ACTIVE_AS_OF_DATE, "07/01/2010");
//		fieldValues.put("number", "a3");
//
//		results = (List<InactivateableFromTo>) lookupService.findCollectionBySearchUnbounded(TravelAccountUseRate.class, fieldValues);
//		assertEquals(1, results.size());
//
//		useRate = (TravelAccountUseRate) results.get(0);
//		assertTrue("Incorrect inactive record returned, does not match expected id", "5".equals(useRate.getId()));
//	}
//
//	/**
//	 * Test query results when begin date is null
//	 *
//	 * @see InactivateableFromToService#findMatchingActive(Class, Map)
//	 */
//	@Test
//	public void testFindMatchingActive_nullBeginDate() throws Exception {
//		InactivateableFromToService inactivateableFromToService = KRADServiceLocatorInternal.getInactivateableFromToService();
//
//		Map fieldValues = new HashMap();
//		fieldValues.put(KNSPropertyConstants.ACTIVE_AS_OF_DATE, "04/01/2010");
//		fieldValues.put("number", "a4");
//
//		List<InactivateableFromTo> results = inactivateableFromToService.findMatchingActive(TravelAccountUseRate.class,
//				fieldValues);
//		assertEquals(1, results.size());
//
//		TravelAccountUseRate useRate = (TravelAccountUseRate) results.get(0);
//		assertTrue("Incorrect inactive record returned, does not match expected id", "6".equals(useRate.getId()));
//
//		LookupService lookupService = KRADServiceLocatorWeb.getLookupService();
//
//		fieldValues = new HashMap();
//		fieldValues.put(KNSPropertyConstants.ACTIVE, "N");
//		fieldValues.put(KNSPropertyConstants.ACTIVE_AS_OF_DATE, "04/01/2010");
//		fieldValues.put("number", "a4");
//
//		results = (List<InactivateableFromTo>) lookupService.findCollectionBySearchUnbounded(TravelAccountUseRate.class, fieldValues);
//		assertEquals(0, results.size());
//	}
//
//	/**
//	 * Test query results when end date is null
//	 *
//	 * @see InactivateableFromToService#findMatchingActive(Class, Map)
//	 */
//	@Test
//	public void testFindMatchingActive_nullEndDate() throws Exception {
//		InactivateableFromToService inactivateableFromToService = KRADServiceLocatorInternal.getInactivateableFromToService();
//
//		Map fieldValues = new HashMap();
//		fieldValues.put(KNSPropertyConstants.ACTIVE_AS_OF_DATE, "04/01/2030");
//		fieldValues.put("number", "a5");
//
//		List<InactivateableFromTo> results = inactivateableFromToService.findMatchingActive(TravelAccountUseRate.class,
//				fieldValues);
//		assertEquals(1, results.size());
//
//		TravelAccountUseRate useRate = (TravelAccountUseRate) results.get(0);
//		assertTrue("Incorrect active record returned, does not match expected id", "7".equals(useRate.getId()));
//	}
//
//	/**
//	 * Test query results when the begin and end date is null
//	 *
//	 * @see InactivateableFromToService#findMatchingActive(Class, Map)
//	 */
//	@Test
//	public void testFindMatchingActive_nullBeginEndDate() throws Exception {
//		InactivateableFromToService inactivateableFromToService = KRADServiceLocatorInternal.getInactivateableFromToService();
//
//		Map fieldValues = new HashMap();
//		fieldValues.put(KNSPropertyConstants.ACTIVE_AS_OF_DATE, "04/01/2010");
//		fieldValues.put("number", "a6");
//
//		List<InactivateableFromTo> results = inactivateableFromToService.findMatchingActive(TravelAccountUseRate.class,
//				fieldValues);
//		assertEquals(1, results.size());
//
//		TravelAccountUseRate useRate = (TravelAccountUseRate) results.get(0);
//		assertTrue("Incorrect active record returned, does not match expected id", "8".equals(useRate.getId()));
//	}
//
//	/**
//	 * Test for the filterOutNonActive method
//	 *
//	 * @see InactivateableFromToService#filterOutNonActive(List)
//	 */
//	@Test
//	public void testFilterOutNonActive() throws Exception {
//		InactivateableFromToService inactivateableFromToService = KRADServiceLocatorInternal.getInactivateableFromToService();
//
//		List<InactivateableFromTo> filterList = new ArrayList<InactivateableFromTo>();
//		filterList.add(constructUseRate("1", "a1", "01/01/2010", "01/01/2011"));
//		filterList.add(constructUseRate("2", "a1", "01/01/2012", "01/01/2013"));
//		filterList.add(constructUseRate("3", "a2", "01/01/2009", "01/01/2010"));
//		filterList.add(constructUseRate("4", "a3", "01/01/2010", "05/16/2010"));
//		filterList.add(constructUseRate("5", "a4", null, "01/01/2011"));
//
//		Date activeAsOfDate = KRADServiceLocator.getDateTimeService().convertToSqlDate("06/01/2010");
//		List<InactivateableFromTo> accessibleList = inactivateableFromToService.filterOutNonActive(filterList,
//				activeAsOfDate);
//		assertEquals(2, accessibleList.size());
//
//		TravelAccountUseRate useRate = (TravelAccountUseRate) accessibleList.get(0);
//		assertTrue("Incorrect active records returned, do not match expected ids", "1".equals(useRate.getId()) || "5".equals(useRate.getId()));
//
//		useRate = (TravelAccountUseRate) accessibleList.get(1);
//		assertTrue("Incorrect active records returned, do not match expected ids", "1".equals(useRate.getId()) || "5".equals(useRate.getId()));
//	}
//
//	/**
//	 * Test finding current records through the findMatchingCurrent method
//	 *
//	 * @see InactivateableFromToService#findMatchingCurrent(Class, Map)
//	 */
//	@Test
//	public void testFindMatchingCurrent() throws Exception {
//		InactivateableFromToService inactivateableFromToService = KRADServiceLocatorInternal.getInactivateableFromToService();
//
//		Map fieldValues = new HashMap();
//		fieldValues.put(KNSPropertyConstants.ACTIVE_AS_OF_DATE, "06/01/2010");
//		fieldValues.put("number", "a2");
//
//		List<InactivateableFromTo> results = inactivateableFromToService.findMatchingCurrent(TravelAccountUseRate.class,
//				fieldValues);
//		assertEquals(1, results.size());
//
//		TravelAccountUseRate useRate = (TravelAccountUseRate) results.get(0);
//		assertTrue("Incorrect current record returned, does not match expected id", "3".equals(useRate.getId()));
//	}
//
//	/**
//	 * Test for the filterOutNonCurrent method
//	 *
//	 * @see InactivateableFromToService#filterOutNonCurrent(List)
//	 */
//	@Test
//	public void testFilterOutNonCurrent() throws Exception {
//		InactivateableFromToService inactivateableFromToService = KRADServiceLocatorInternal.getInactivateableFromToService();
//
//		List<InactivateableFromTo> filterList = new ArrayList<InactivateableFromTo>();
//		filterList.add(constructUseRate("1", "a1", "01/01/2010", "01/01/2011"));
//		filterList.add(constructUseRate("2", "a1", "01/16/2010", "01/01/2011"));
//		filterList.add(constructUseRate("3", "a1", "01/01/2012", "01/01/2013"));
//		filterList.add(constructUseRate("4", "a2", "01/01/2009", "01/01/2010"));
//		filterList.add(constructUseRate("5", "a3", "01/01/2010", "05/16/2011"));
//		filterList.add(constructUseRate("6", "a3", "06/01/2010", "05/16/2011"));
//
//		Date activeAsOfDate = KRADServiceLocator.getDateTimeService().convertToSqlDate("06/10/2010");
//		List<InactivateableFromTo> accessibleList = inactivateableFromToService.filterOutNonCurrent(filterList,
//				activeAsOfDate);
//		assertEquals(2, accessibleList.size());
//
//		TravelAccountUseRate useRate = (TravelAccountUseRate) accessibleList.get(0);
//		assertTrue("Incorrect curren records returned, do not match expected ids",
//				"2".equals(useRate.getId()) || "6".equals(useRate.getId()));
//
//		useRate = (TravelAccountUseRate) accessibleList.get(1);
//		assertTrue("Incorrect curren records returned, do not match expected ids",
//				"2".equals(useRate.getId()) || "6".equals(useRate.getId()));
//	}
//
//	protected TravelAccountUseRate constructUseRate(String id, String number, String fromDate, String toDate)
//			throws Exception {
//		TravelAccountUseRate useRate = new TravelAccountUseRate();
//
//		DateTimeService dateTimeService = KRADServiceLocator.getDateTimeService();
//		useRate.setId(id);
//		useRate.setNumber(number);
//		if (fromDate != null) {
//			useRate.setActiveFromDate(new Timestamp(dateTimeService.convertToSqlDate(fromDate).getTime()));
//		}
//		if (toDate != null) {
//			useRate.setActiveToDate(new Timestamp(dateTimeService.convertToSqlDate(toDate).getTime()));
//		}
//
//		return useRate;
//	}
}
