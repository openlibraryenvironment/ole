/*
 * Copyright 2013 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document;

import org.kuali.ole.coa.document.KualiAccountMaintainableImpl;
import org.kuali.ole.select.service.OleAccountService;
import org.kuali.ole.sys.context.SpringContext;

public class OleAccountMaintainableImpl extends KualiAccountMaintainableImpl {

    /**
     * @see org.kuali.ole.sys.document.FinancialSystemMaintainable#answerSplitNodeQuestion(java.lang.String)
     */
    @Override
    protected boolean answerSplitNodeQuestion(String nodeName) throws UnsupportedOperationException {
        if (nodeName.equals("HasDepositAccount")) {
            return SpringContext.getBean(OleAccountService.class).shouldAccountRouteForApproval(getDocumentNumber());
        }
        return super.answerSplitNodeQuestion(nodeName);
    }


}
