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
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.assertTrue;

/**
 * tests {@link org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService}
 */
public class MaintenanceDocumentDictionaryServiceTest extends KRADTestCase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MaintenanceDocumentDictionaryServiceTest.class);

    /**
     * tests to make sure <code>IllegalArgumentExceptions</code> are being thrown on null parameters
     */
    @Test public void testGetFieldDefaultValue_NullArguments() {

        boolean exceptionThrown;

        // test the boClass null argument
        exceptionThrown = false;
        try {
            KNSServiceLocator.getMaintenanceDocumentDictionaryService().getFieldDefaultValue((Class) null, "accountNumber");
        }
        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        catch (Exception e) {
            exceptionThrown = false;
        }
        assertTrue("An IllegalArgumentException should have been thrown.", exceptionThrown);

        // test the docTypeName null argument
        exceptionThrown = false;
        try {
            KNSServiceLocator.getMaintenanceDocumentDictionaryService().getFieldDefaultValue((String) null, "accountNumber");
        }
        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        catch (Exception e) {
            exceptionThrown = false;
        }
        assertTrue("An IllegalArgumentException should have been thrown.", exceptionThrown);

        // test the fieldName null argument
        exceptionThrown = false;
        try {
            KNSServiceLocator.getMaintenanceDocumentDictionaryService().getFieldDefaultValue("docTypeName", null);
        }
        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        catch (Exception e) {
            exceptionThrown = false;
        }
        assertTrue("An IllegalArgumentException should have been thrown.", exceptionThrown);
    }

//    @Test public void testGetFieldDefaultValue_Account() {
//
//        String result;
//        result = KRADServiceLocatorInternal.getMaintenanceDocumentDictionaryService().getFieldDefaultValue(AccountManager.class, "defaultType");
//        LOG.debug(result);
//        assertEquals("Standard", result);
//
//    }
//
//    @Test public void testGetFieldDefaultValue_SubAccount() {
//
//        String result;
//        result = KRADServiceLocatorInternal.getMaintenanceDocumentDictionaryService().getFieldDefaultValue(SubAccount.class, "subAccountActiveIndicator");
//        LOG.debug(result);
//        assertEquals("true", result);
//
//        result = KRADServiceLocatorInternal.getMaintenanceDocumentDictionaryService().getFieldDefaultValue(SubAccount.class, "a21SubAccount.subAccountTypeCode");
//        LOG.debug(result);
//        assertEquals("EX", result);
//
//        result = KRADServiceLocatorInternal.getMaintenanceDocumentDictionaryService().getFieldDefaultValue(SubAccount.class, "a21SubAccount.offCampusCode");
//        LOG.debug(result);
//        assertEquals("false", result);
//
//    }
//
//    @Test public void testGetFieldDefaultValue_SubObjectCode() {
//
//        String result;
//
//        result = KRADServiceLocatorInternal.getMaintenanceDocumentDictionaryService().getFieldDefaultValue(SubObjCd.class, "universityFiscalYear");
//        LOG.debug(result);
//        assertEquals(getValueFromFinder(FiscalYearFinder.class), result);
//
//        result = KRADServiceLocatorInternal.getMaintenanceDocumentDictionaryService().getFieldDefaultValue(SubObjCd.class, "financialSubObjectActiveIndicator");
//        LOG.debug(result);
//        assertEquals("true", result);
//
//    }
//
//    private String getValueFromFinder(Class finderClass) {
//
//        ValueFinder valueFinder = null;
//        try {
//            valueFinder = (ValueFinder) finderClass.newInstance();
//        }
//        catch (InstantiationException e) {
//            e.printStackTrace();
//            assertTrue("An InstantiationException should not have been thrown.", false);
//        }
//        catch (IllegalAccessException e) {
//            e.printStackTrace();
//            assertTrue("An IllegalAccessException should not have been thrown.", false);
//        }
//
//        assertNotNull("The valueFinder object should not be null.", valueFinder);
//
//        return valueFinder.getValue();
//    }
}
