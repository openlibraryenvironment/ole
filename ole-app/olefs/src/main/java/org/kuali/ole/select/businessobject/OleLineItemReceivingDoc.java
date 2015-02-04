/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.businessobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

public class OleLineItemReceivingDoc extends PersistableBusinessObjectBase {

    private Integer lineItemId;
    private Integer receivingLineItemIdentifier;
    private String itemTitleId;
    private OleLineItemReceivingItem receivingLineItem;

    public Integer getLineItemId() {
        return lineItemId;
    }

    public void setLineItemID(Integer lineItemId) {
        this.lineItemId = lineItemId;
    }

    public Integer getReceivingLineItemIdentifier() {
        return receivingLineItemIdentifier;
    }

    public void setReceivingLineItemIdentifier(Integer receivingLineItemIdentifier) {
        this.receivingLineItemIdentifier = receivingLineItemIdentifier;
    }

    public String getItemTitleId() {
        return itemTitleId;
    }

    public void setItemTitleId(String itemTitleId) {
        this.itemTitleId = itemTitleId;
    }

    public OleLineItemReceivingItem getReceivingLineItem() {
        return receivingLineItem;
    }

    public void setReceivingLineItem(OleLineItemReceivingItem receivingLineItem) {
        this.receivingLineItem = receivingLineItem;
    }

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        // TODO Auto-generated method stub
        LinkedHashMap m = new LinkedHashMap();
        m.put("lineItemId", this.lineItemId);
        m.put("receivingItemIdentifier", this.receivingLineItemIdentifier);
        m.put("itemTitleId", this.itemTitleId);
        return m;
    }


}
