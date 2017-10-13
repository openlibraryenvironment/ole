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
package org.kuali.rice.kns.document;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.test.document.bo.AccountManager;
import org.kuali.rice.krad.test.BaseMaintenanceDocumentTest;

/**
 * AccountManagerMaintenanceDocumentTest tests some maintenance document operations on the document type 'AccountManagerMaintenanceDocument'
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AccountManagerMaintenanceDocumentTest extends BaseMaintenanceDocumentTest {

    @Override
    protected Object getNewMaintainableObject()  {
        AccountManager am = new AccountManager();
        am.setAmId(new Long(1));
        am.setUserName("userName");
        return am;
    }

    @Override
    protected String getDocumentTypeName() {
        return "AccountManagerMaintenanceDocument";
    }

    @Override
    protected String getInitiatorPrincipalName() {
        return "quickstart";
    }

    @Override
    protected Object getOldMaintainableObject() {
        return getNewMaintainableObject();
    }

     @Test
    /**
     * tests that user {@link #getInitiatorPrincipalName()}, on routing a new maintenance document,
     * results in a final document
     */
    public void testRouteNewDoc() throws WorkflowException {
        setupNewAccountMaintDoc(getDocument());
        KRADServiceLocatorWeb.getDocumentService().routeDocument(getDocument(), "submit", null);
        Assert.assertTrue(getDocument().getDocumentHeader().getWorkflowDocument().isFinal());
    }
}
