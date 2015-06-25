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
package org.kuali.ole.coa.businessobject;


import org.junit.Test;
import org.kuali.ole.DocumentTestUtils;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.ole.fp.document.TransferOfFundsDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.AccountingDocument;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

public class ObjectCodeGlobalDetailTest extends KFSTestCaseBase {
    private AccountingDocument document;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GlobalVariables.setUserSession(new UserSession("dev2"));
        document = DocumentTestUtils.createDocument(SpringContext.getBean(DocumentService.class), TransferOfFundsDocument.class);
        SpringContext.getBean(DocumentService.class).saveDocument(document);
        changeCurrentUser(UserNameFixture.khuntley);
    }

    @Test
    public void testSave() {
        GlobalVariables.setUserSession(new UserSession("dev2"));
        ObjectCodeGlobalDetail detail = new ObjectCodeGlobalDetail();
        ObjectCodeGlobal doc = new ObjectCodeGlobal();

        doc.setDocumentNumber(document.getDocumentNumber());

        detail.setDocumentNumber(document.getDocumentNumber());
        detail.setUniversityFiscalYear(document.getPostingYear());
        detail.setChartOfAccountsCode("BL");

        List<ObjectCodeGlobalDetail> list = new ArrayList<ObjectCodeGlobalDetail>();
        list.add(detail);
        doc.setObjectCodeGlobalDetails(list);

        SpringContext.getBean(BusinessObjectService.class).save(doc);
    }

}

