package org.kuali.ole.oleng.service;

import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.select.document.OleInvoiceDocument;

/**
 * Created by SheikS on 12/17/2015.
 */
public interface InvoiceService {
    public OleInvoiceDocument createInvoiceDocument(OleInvoiceRecord oleInvoiceRecord) throws Exception;
}
