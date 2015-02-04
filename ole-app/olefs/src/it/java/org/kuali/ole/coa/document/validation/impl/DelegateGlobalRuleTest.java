/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.coa.document.validation.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.kuali.ole.coa.businessobject.AccountDelegateGlobalDetail;
import org.kuali.ole.coa.document.validation.impl.DelegateGlobalRule;
import org.kuali.ole.sys.OLEConstants;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNull;

public class DelegateGlobalRuleTest extends ChartRuleTestBase {

    private AccountDelegateGlobalDetail delegateGlobal;
    private List<AccountDelegateGlobalDetail> delegateGlobals;

    @Test
    public void testCheckPrimaryRoutePerDocType_InputValidations() {
        DelegateGlobalRule rule = new DelegateGlobalRule();
        delegateGlobal = null;
        delegateGlobals = null;
        Integer offendingLine = null;
        Integer result = rule.checkPrimaryRoutePerDocType(delegateGlobal, delegateGlobals, null);
        assertNull(result);

        delegateGlobal = new AccountDelegateGlobalDetail();
        delegateGlobals = null;
        offendingLine = null;
        result = rule.checkPrimaryRoutePerDocType(delegateGlobal, delegateGlobals, null);
        assertNull(result);

        delegateGlobal = new AccountDelegateGlobalDetail();
        delegateGlobals = new ArrayList<AccountDelegateGlobalDetail>();
        offendingLine = null;
        result = rule.checkPrimaryRoutePerDocType(delegateGlobal, delegateGlobals, null);
        assertNull(result);

        delegateGlobal = new AccountDelegateGlobalDetail();
        delegateGlobal.setAccountDelegatePrimaryRoutingIndicator(false);
        delegateGlobal.setFinancialDocumentTypeCode(OLEConstants.ROOT_DOCUMENT_TYPE);
        delegateGlobals = new ArrayList<AccountDelegateGlobalDetail>();
        offendingLine = null;
        result = rule.checkPrimaryRoutePerDocType(delegateGlobal, delegateGlobals, null);
        assertNull(result);

        delegateGlobal = new AccountDelegateGlobalDetail();
        delegateGlobal.setAccountDelegatePrimaryRoutingIndicator(true);
        delegateGlobal.setFinancialDocumentTypeCode("A21");
        delegateGlobals = new ArrayList<AccountDelegateGlobalDetail>();
        offendingLine = null;
        result = rule.checkPrimaryRoutePerDocType(delegateGlobal, delegateGlobals, null);
        assertNull(result);

    }

    @Test
    public void testCheckPrimaryRoutePerDocType_NewLine_NoOtherPrimariesWithSameDocType() {
        DelegateGlobalRule rule = new DelegateGlobalRule();
        delegateGlobals = new ArrayList<AccountDelegateGlobalDetail>();
        AccountDelegateGlobalDetail listItem;

        delegateGlobal = new AccountDelegateGlobalDetail();
        delegateGlobal.setAccountDelegatePrimaryRoutingIndicator(true);
        delegateGlobal.setFinancialDocumentTypeCode("ABC");

        listItem = new AccountDelegateGlobalDetail();
        listItem.setAccountDelegatePrimaryRoutingIndicator(true);
        listItem.setFinancialDocumentTypeCode("A21");
        delegateGlobals.add(listItem);

        Integer result = rule.checkPrimaryRoutePerDocType(delegateGlobal, delegateGlobals, null);
        assertNull(result);
    }

    @Test
    public void testCheckPrimaryRoutePerDocType_NewLine_AddingIdenticalLine() {
        DelegateGlobalRule rule = new DelegateGlobalRule();
        delegateGlobals = new ArrayList<AccountDelegateGlobalDetail>();
        AccountDelegateGlobalDetail listItem;

        delegateGlobal = new AccountDelegateGlobalDetail();
        delegateGlobal.setAccountDelegatePrimaryRoutingIndicator(true);
        delegateGlobal.setFinancialDocumentTypeCode("ABC");

        listItem = new AccountDelegateGlobalDetail();
        listItem.setAccountDelegatePrimaryRoutingIndicator(true);
        listItem.setFinancialDocumentTypeCode("ABC");
        delegateGlobals.add(listItem);

        Integer result = rule.checkPrimaryRoutePerDocType(delegateGlobal, delegateGlobals, null);
        assertEquals(new Integer(0), result);
    }

    @Test
    public void testCheckOnlyOnePrimaryRoute_NewLine_OnePrimaryWithSameDocType() {
        DelegateGlobalRule rule = new DelegateGlobalRule();
        delegateGlobals = new ArrayList<AccountDelegateGlobalDetail>();
        AccountDelegateGlobalDetail listItem;

        delegateGlobal = new AccountDelegateGlobalDetail();
        delegateGlobal.setAccountDelegatePrimaryRoutingIndicator(true);
        delegateGlobal.setFinancialDocumentTypeCode("A21");

        listItem = new AccountDelegateGlobalDetail();
        listItem.setAccountDelegatePrimaryRoutingIndicator(true);
        listItem.setFinancialDocumentTypeCode("ABC");
        delegateGlobals.add(listItem);

        listItem = new AccountDelegateGlobalDetail();
        listItem.setAccountDelegatePrimaryRoutingIndicator(true);
        listItem.setFinancialDocumentTypeCode("A21");
        delegateGlobals.add(listItem);

        Integer result = rule.checkPrimaryRoutePerDocType(delegateGlobal, delegateGlobals, null);
        assertEquals(new Integer(1), result);
    }

}
