package org.kuali.ole.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/30/13
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleInvoiceRecordHandler {

    private Map<String, List<OleInvoiceRecord>> oleInvoiceLineItemRecords = new HashMap<>();

    public Map<String, List<OleInvoiceRecord>> getOleInvoiceLineItemRecords() {
        return oleInvoiceLineItemRecords;
    }

    public void setOleInvoiceLineItemRecords(Map<String, List<OleInvoiceRecord>> oleInvoiceLineItemRecords) {
        this.oleInvoiceLineItemRecords = oleInvoiceLineItemRecords;
    }
}
