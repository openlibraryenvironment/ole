/*
 * Copyright 2009 The Kuali Foundation
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
package org.kuali.ole.module.purap.businessobject;

import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Auto Close Purchase Order View Business Object.
 */
public class AutoClosePurchaseOrderView extends PurchaseOrderView {

    private KualiDecimal totalEncumbrance;
    private KualiDecimal totalAmount;
    private Date purchaseOrderCreateTimestamp;
    private KualiDecimal invoicedQuantity;
    private String appDocStatus;
    private String purchaseOrderType;

    public Date getPurchaseOrderCreateTimestamp() {
        return purchaseOrderCreateTimestamp;
    }

    public void setPurchaseOrderCreateTimestamp(Date purchaseOrderCreateTimestamp) {
        this.purchaseOrderCreateTimestamp = purchaseOrderCreateTimestamp;
    }

    public KualiDecimal getTotalEncumbrance() {
        return totalEncumbrance;
    }

    public void setTotalEncumbrance(KualiDecimal totalEncumbrance) {
        this.totalEncumbrance = totalEncumbrance;
    }

    public KualiDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(KualiDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAppDocStatus() {
        return appDocStatus;
    }

    public KualiDecimal getInvoicedQuantity() {
        return invoicedQuantity;
    }

    public void setInvoicedQuantity(KualiDecimal invoicedQuantity) {
        this.invoicedQuantity = invoicedQuantity;
    }

    public String getPurchaseOrderType() {
        return purchaseOrderType;
    }

    public void setPurchaseOrderType(String purchaseOrderType) {
        this.purchaseOrderType = purchaseOrderType;
    }
}
