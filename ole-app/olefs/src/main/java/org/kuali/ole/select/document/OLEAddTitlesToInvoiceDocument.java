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
package org.kuali.ole.select.document;

import org.kuali.ole.select.bo.OLEInvoiceBo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OLEAddTitlesToInvoiceDocument {

    private String invoiceId;
    private String invoiceNumber;
    private Date invoiceDate;
    private OLEInvoiceBo oleInvoiceBo;
    private OleInvoiceDocument invoiceDocument;
    private List<OleInvoiceDocument> invoiceDocumentList = new ArrayList<OleInvoiceDocument>();

    public OleInvoiceDocument getInvoiceDocument() {
        return invoiceDocument;
    }

    public void setInvoiceDocument(OleInvoiceDocument invoiceDocument) {
        this.invoiceDocument = invoiceDocument;
    }

    public List<OleInvoiceDocument> getInvoiceDocumentList() {
        return invoiceDocumentList;
    }

    public void setInvoiceDocumentList(List<OleInvoiceDocument> invoiceDocumentList) {
        this.invoiceDocumentList = invoiceDocumentList;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public OLEInvoiceBo getOleInvoiceBo() {
        return oleInvoiceBo;
    }

    public void setOleInvoiceBo(OLEInvoiceBo oleInvoiceBo) {
        this.oleInvoiceBo = oleInvoiceBo;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}
