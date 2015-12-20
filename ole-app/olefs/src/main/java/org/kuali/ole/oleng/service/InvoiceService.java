package org.kuali.ole.oleng.service;

import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.select.document.OleInvoiceDocument;

import java.util.List;

/**
 * Created by SheikS on 12/17/2015.
 */
public interface InvoiceService {
    public OleInvoiceDocument createInvoiceDocument(List<OleInvoiceRecord> oleInvoiceRecords) throws Exception;
    public OleInvoiceDocument saveInvoiceDocument(OleInvoiceDocument oleInvoiceDocument);
}
