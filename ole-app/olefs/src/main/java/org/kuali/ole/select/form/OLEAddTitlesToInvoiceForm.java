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
package org.kuali.ole.select.form;

import org.kuali.ole.select.bo.OLEInvoiceBo;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
public class OLEAddTitlesToInvoiceForm extends UifFormBase {

    private String selectedSection;
    private String invoiceId;
    private String invoiceNumber;
    private OLEInvoiceBo oleInvoiceBo;
    private Date invoiceDate;
    private String errorMsg;
    private String successMsg;
    private String invoiceAmount;
    private String paymentMethod;
    private String invoiceNbr;
    private boolean payAndReceive;
    private boolean cancelBox;
    private String documentNumber;
    private boolean continueReceiveAndPay;
    private boolean skipValidation;

    public boolean isCancelBox() {
        return cancelBox;
    }

    public void setCancelBox(boolean cancelBox) {
        this.cancelBox = cancelBox;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    public String getInvoiceNbr() {
        return invoiceNbr;
    }

    public void setInvoiceNbr(String invoiceNbr) {
        this.invoiceNbr = invoiceNbr;
    }

    public boolean isPayAndReceive() {
        return payAndReceive;
    }

    public void setPayAndReceive(boolean payAndReceive) {
        this.payAndReceive = payAndReceive;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    List<OlePurchaseOrderItem> olePurchaseOrderItems = new ArrayList<OlePurchaseOrderItem>();

    public List<OlePurchaseOrderDocument> olePurchaseOrderDocuments = new ArrayList<OlePurchaseOrderDocument>();

    public List<OlePurchaseOrderDocument> getOlePurchaseOrderDocuments() {
        return olePurchaseOrderDocuments;
    }

    public void setOlePurchaseOrderDocuments(List<OlePurchaseOrderDocument> olePurchaseOrderDocuments) {
        this.olePurchaseOrderDocuments = olePurchaseOrderDocuments;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public OLEAddTitlesToInvoiceForm() {
        /*this.selectedSection="newToInvoice";*/
    }

    public String getSelectedSection() {
        return selectedSection;
    }

    public void setSelectedSection(String selectedSection) {
        this.selectedSection = selectedSection;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public OLEInvoiceBo getOleInvoiceBo() {
        return oleInvoiceBo;
    }

    public void setOleInvoiceBo(OLEInvoiceBo oleInvoiceBo) {
        this.oleInvoiceBo = oleInvoiceBo;
    }

    public List<OlePurchaseOrderItem> getOlePurchaseOrderItems() {
        return olePurchaseOrderItems;
    }

    public void setOlePurchaseOrderItems(List<OlePurchaseOrderItem> olePurchaseOrderItems) {
        this.olePurchaseOrderItems = olePurchaseOrderItems;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public boolean isContinueReceiveAndPay() {
        return continueReceiveAndPay;
    }

    public void setContinueReceiveAndPay(boolean continueReceiveAndPay) {
        this.continueReceiveAndPay = continueReceiveAndPay;
    }

    public boolean isSkipValidation() {
        return skipValidation;
    }

    public void setSkipValidation(boolean skipValidation) {
        this.skipValidation = skipValidation;
    }
}
