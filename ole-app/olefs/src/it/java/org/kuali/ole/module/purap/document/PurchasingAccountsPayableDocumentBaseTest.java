/*
 * Copyright 2007-2008 The Kuali Foundation
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
package org.kuali.ole.module.purap.document;

import org.junit.Test;
import org.kuali.ole.ConfigureContext;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.kns.util.MessageList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


public class PurchasingAccountsPayableDocumentBaseTest extends KualiTestBase {

    PurchasingAccountsPayableDocument purapDoc;
    Integer currentFY;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        KNSGlobalVariables.setMessageList(new MessageList());
        purapDoc = new PurchaseOrderDocument();
        currentFY = SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear();
        changeCurrentUser(UserNameFixture.khuntley);
    }

    @Override
    public void tearDown() throws Exception {
        purapDoc = null;
        super.tearDown();
    }

    @Test
    public void testIsPostingYearNext_UseCurrent() {
        purapDoc.setPostingYear(currentFY);
        assertFalse(purapDoc.isPostingYearNext());
    }

    @Test
    public void testIsPostingYearNext_UseNext() {
        purapDoc.setPostingYear(currentFY + 1);
        assertTrue(purapDoc.isPostingYearNext());
    }

    @Test
    public void testIsPostingYearNext_UsePast() {
        purapDoc.setPostingYear(currentFY - 1);
        assertFalse(purapDoc.isPostingYearNext());
    }

    @Test
    public void testGetPostingYearNextOrCurrent_UseCurrent() {
        purapDoc.setPostingYear(currentFY);
        assertEquals(purapDoc.getPostingYearNextOrCurrent(), currentFY);
    }

    @Test
    public void testGetPostingYearNextOrCurrent_UseNext() {
        Integer nextFY = currentFY + 1;
        purapDoc.setPostingYear(nextFY);
        assertEquals(purapDoc.getPostingYearNextOrCurrent(), nextFY);
    }

    @Test
    public void testGetPostingYearNextOrCurrent_UsePast() {
        purapDoc.setPostingYear(currentFY - 1);
        assertEquals(purapDoc.getPostingYearNextOrCurrent(), currentFY);
    }

}

