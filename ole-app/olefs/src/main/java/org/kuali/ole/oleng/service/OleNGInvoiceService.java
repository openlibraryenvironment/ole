package org.kuali.ole.oleng.service;

import org.kuali.ole.Exchange;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.select.document.OleInvoiceDocument;

import java.util.List;

/**
 * Created by SheikS on 12/17/2015.
 */
public interface OleNGInvoiceService {
    public OleInvoiceDocument createNewInvoiceDocument() throws Exception;
    public OleInvoiceDocument populateInvoiceDocWithOrderInformation(OleInvoiceDocument oleInvoiceDocument, List<OleInvoiceRecord> oleInvoiceRecords, Exchange exchange) throws Exception;
    public OleInvoiceDocument saveInvoiceDocument(OleInvoiceDocument oleInvoiceDocument);
}
