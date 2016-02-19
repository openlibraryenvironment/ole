package org.kuali.ole.gobi.resolvers;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 9/3/15.
 */
public class ReceiptNoteValueResolver extends TxValuResolver {

    @Override
    public boolean isInterested(String attributeName) {
        return OLEConstants.OLEBatchProcess.RCPT_NOTE.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        List<String> receiptNotes = oleTxRecord.getReceiptNotes();
        if (null == receiptNotes) {
            receiptNotes = new ArrayList<>();
        }
        receiptNotes.add(attributeValue);
        oleTxRecord.setReceiptNotes(receiptNotes);
        oleTxRecord.setReceiptNote("Receipt Note");
    }
}
