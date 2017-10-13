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
package org.kuali.rice.location.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.location.api.services.LocationApiServiceLocator;
import org.kuali.rice.location.impl.country.CountryBo;
import org.kuali.rice.location.impl.county.CountyBo;
import org.kuali.rice.location.impl.state.StateBo;
import org.kuali.rice.krad.test.BaseMaintenanceDocumentTest;

import static org.junit.Assert.assertNotNull;

/**
 * Tests creating, editing, and savingn County maintenance doc
 */
public class CountyMaintenanceDocumentTest extends BaseMaintenanceDocumentTest {
    /**
     * Make sure we have a test country and state
     */
    @Before
    public void insertTestCountryAndState() {
        CountryBo country = new CountryBo();
        country.setCode("CC");
        country.setName("New Country");
        country.setActive(true);
        KRADServiceLocator.getBusinessObjectService().save(country);

        StateBo state = new StateBo();
        state.setCode("SS");
        state.setCountryCode("CC");
        state.setName("New State");
        state.setActive(true);
        KRADServiceLocator.getBusinessObjectService().save(state);
    }

    @Override
    protected Object getNewMaintainableObject()  {
        CountyBo county = new CountyBo();
        county.setName("Tompkins");
        county.setCode("TOMPKINS");
        county.setCountryCode("US");
        county.setStateCode("CA");
        return county;
    }

    @Override
    protected String getDocumentTypeName() {
        return "CountyMaintenanceDocument";
    }

    @Override
    protected String getInitiatorPrincipalName() {
        return "quickstart";
    }

    @Override
    protected Object getOldMaintainableObject() {
        return getNewMaintainableObject();
    }

    @Test(expected = ValidationException.class)
    /**
     * test that a validation error occurs when a business object is missing required fields
     */
    public void test_MismatchedStateCountry() throws WorkflowException {
        assertNotNull(LocationApiServiceLocator.getCountryService().getCountry("CC"));
        assertNotNull(LocationApiServiceLocator.getStateService().getState("CC", "SS"));

        CountyBo county = new CountyBo();
        county.setName("Tompkins");
        county.setCode("TOMPKINS");
        county.setCountryCode("US");
        // our test state is not in the US country
        // by virtue of the country/state join on State reference
        // and the State reference existence check
        // this will yield a validation error
        county.setStateCode("SS");

        MaintenanceDocument document = getDocument();
        document.getOldMaintainableObject().setDataObject(null);
        document.getOldMaintainableObject().setDataObjectClass(county.getClass());
        document.getNewMaintainableObject().setDataObject(county);
        document.getNewMaintainableObject().setDataObjectClass(county.getClass());

        document.getNewMaintainableObject().setMaintenanceAction(KRADConstants.MAINTENANCE_NEW_ACTION);

        KRADServiceLocatorWeb.getDocumentService().routeDocument(getDocument(), "submit", null);
        Assert.assertTrue("ValidationException should have been thrown instead of this assertion failure", getDocument().getDocumentHeader().getWorkflowDocument().isEnroute());
    }
}
