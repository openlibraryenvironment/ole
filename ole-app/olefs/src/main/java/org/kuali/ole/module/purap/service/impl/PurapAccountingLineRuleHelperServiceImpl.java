/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.module.purap.service.impl;

import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.service.PurapAccountingLineRuleHelperService;
import org.kuali.ole.sys.businessobject.AccountingLine;
import org.kuali.ole.sys.document.service.impl.AccountingLineRuleHelperServiceImpl;

public class PurapAccountingLineRuleHelperServiceImpl extends AccountingLineRuleHelperServiceImpl implements PurapAccountingLineRuleHelperService {
    private PurchasingAccountsPayableDocument document;

    public PurchasingAccountsPayableDocument getDocument() {
        return document;
    }

    public void setDocument(PurchasingAccountsPayableDocument document) {
        this.document = document;
    }

    /**
     * @see org.kuali.ole.sys.document.service.impl.AccountingLineRuleHelperServiceImpl#hasRequiredOverrides(org.kuali.ole.sys.businessobject.AccountingLine, java.lang.String)
     *      in purap implementation this does nothing since it is handled in our rule classes
     */
    @Override
    public boolean hasRequiredOverrides(AccountingLine line, String overrideCode) {
        return true;
    }
}
