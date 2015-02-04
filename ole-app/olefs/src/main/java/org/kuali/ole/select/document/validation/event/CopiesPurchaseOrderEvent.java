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
package org.kuali.ole.select.document.validation.event;

import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.validation.impl.OleValidationRule;
import org.kuali.ole.select.document.validation.impl.OleValidationRuleBase;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEventBase;

public class CopiesPurchaseOrderEvent extends KualiDocumentEventBase {


    protected CopiesPurchaseOrderEvent(String errorPathPrefix, Document document) {
        super("Creating Copies Tag" + getDocumentId(document), errorPathPrefix, document);
    }

    public CopiesPurchaseOrderEvent(Document document, OlePurchaseOrderItem olePurchaseOrderItem) {

        this("", document);
        this.olePurchaseOrderItem = olePurchaseOrderItem;
    }

    private OlePurchaseOrderItem olePurchaseOrderItem;

    @Override
    public Class getRuleInterfaceClass() {
        return OleValidationRuleBase.class;
    }

    @Override
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((OleValidationRule) rule).processCustomAddCopiesPurchaseOrderBusinessRules(document, olePurchaseOrderItem);
    }


}
