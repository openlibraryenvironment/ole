/*
 * Copyright 2012 The Kuali Foundation.
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

import org.kuali.ole.select.businessobject.OleLineItemReceivingItem;
import org.kuali.ole.select.document.validation.impl.OleLineItemReceivingDocumentRule;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEventBase;

public class OleLineItemReceivingDescEvent extends KualiDocumentEventBase {

    private OleLineItemReceivingItem lineItem;


    protected OleLineItemReceivingDescEvent(String errorPathPrefix, Document document) {
        super("Creating Note Tag" + getDocumentId(document), errorPathPrefix, document);
    }

    public OleLineItemReceivingDescEvent(Document document, OleLineItemReceivingItem lineItem) {
        this("", document);
        this.lineItem = lineItem;
    }

    @Override
    public Class getRuleInterfaceClass() {
        return OleLineItemReceivingDocumentRule.class;
    }

    @Override
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((OleLineItemReceivingDocumentRule) rule).processCustomLineItemReceivingDescriptionBusinessRules(document, lineItem);
    }

    public OleLineItemReceivingItem getLineItem() {
        return lineItem;
    }

    public void setPayItem(OleLineItemReceivingItem lineItem) {
        this.lineItem = lineItem;
    }
}
