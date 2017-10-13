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
import org.junit.Test;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.test.document.bo.AccountManager;
import org.kuali.rice.location.api.country.Country;
import org.kuali.rice.location.impl.country.CountryBo;
import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.krad.test.BaseMaintenanceDocumentTest;

/**
 * CountryMaintenanceDocumentTest tests some maintenance document operations on the document type 'CountryMaintenanceDocument'
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CountryMaintenanceDocumentTest extends BaseMaintenanceDocumentTest {

    @Override
    protected Object getNewMaintainableObject()  {
        CountryBo country = new CountryBo();
        country.setCode("KE");
        // country.setName("Kenya"); //commented out deliberately to cause a validation exception
        return country;
    }

    @Override
    protected String getDocumentTypeName() {
        return "CountryMaintenanceDocument";
    }

    @Override
    protected String getInitiatorPrincipalName() {
        return "admin";
    }

    @Override
    protected Object getOldMaintainableObject() {
        return getNewMaintainableObject();
    }

    @Test(expected = ValidationException.class)
    /**
     * test that a validation error occurs when a business object is missing required fields
     */
    public void test_RouteNewDoc() throws WorkflowException {
        setupNewAccountMaintDoc(getDocument());
        KRADServiceLocatorWeb.getDocumentService().routeDocument(getDocument(), "submit", null);
        Assert.assertTrue(getDocument().getDocumentHeader().getWorkflowDocument().isEnroute());
    }
}
