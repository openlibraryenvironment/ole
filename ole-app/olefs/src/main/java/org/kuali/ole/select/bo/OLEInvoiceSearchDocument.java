/*
 * Copyright 2011 The Kuali Foundation.
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

package org.kuali.ole.select.bo;

import org.kuali.ole.select.businessobject.OleInvoiceSubType;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.Date;

public class OLEInvoiceSearchDocument extends PersistableBusinessObjectBase{

    private String documentNumber;
    private String invoiceNumber;
    private Date invoiceDate;
    private String invoiceType;
    private String invoiceTypeId;
    private Date invoicePayDate;
    private String documentStatus;
    private String workFlowDocumentStatus;
    private String invoiceSubType;
    private Integer invoiceSubTypeId;
    private OleInvoiceSubType oleInvoiceSubType;

    private String purapDocumentIdentifier;
    private OlePurchaseOrderItem olePurchaseOrderItem;
    private String vendorNumber;
    private String vendorName;
    private String invoiceNbr;

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(String invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public Integer getInvoiceSubTypeId() {
        return invoiceSubTypeId;
    }

    public void setInvoiceSubTypeId(Integer invoiceSubTypeId) {
        this.invoiceSubTypeId = invoiceSubTypeId;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Date getInvoicePayDate() {
        return invoicePayDate;
    }

    public void setInvoicePayDate(Date invoicePayDate) {
        this.invoicePayDate = invoicePayDate;
    }

    public String getInvoiceSubType() {
        return invoiceSubType;
    }

    public void setInvoiceSubType(String invoiceSubType) {
        this.invoiceSubType = invoiceSubType;
    }

    public OleInvoiceSubType getOleInvoiceSubType() {
        return oleInvoiceSubType;
    }

    public void setOleInvoiceSubType(OleInvoiceSubType oleInvoiceSubType) {
        this.oleInvoiceSubType = oleInvoiceSubType;
    }

    public String getPurapDocumentIdentifier() {
        return purapDocumentIdentifier;
    }

    public void setPurapDocumentIdentifier(String purapDocumentIdentifier) {
        this.purapDocumentIdentifier = purapDocumentIdentifier;
    }

    public OlePurchaseOrderItem getOlePurchaseOrderItem() {
        return olePurchaseOrderItem;
    }

    public void setOlePurchaseOrderItem(OlePurchaseOrderItem olePurchaseOrderItem) {
        this.olePurchaseOrderItem = olePurchaseOrderItem;
    }

    public String getWorkFlowDocumentStatus() {
        return workFlowDocumentStatus;
    }

    public void setWorkFlowDocumentStatus(String workFlowDocumentStatus) {
        this.workFlowDocumentStatus = workFlowDocumentStatus;
    }

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getInvoiceNbr() {
        return invoiceNbr;
    }

    public void setInvoiceNbr(String invoiceNbr) {
        this.invoiceNbr = invoiceNbr;
    }
}
