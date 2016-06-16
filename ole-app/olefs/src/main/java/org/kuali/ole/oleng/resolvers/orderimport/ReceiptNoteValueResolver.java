package org.kuali.ole.oleng.resolvers.orderimport;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 9/3/15.
 */
public class ReceiptNoteValueResolver extends TxValueResolver {

    @Override
    public boolean isInterested(String attributeName) {
        //TODO : Need to remove the 'OR' condition once old batch is deprecated.
        return OleNGConstants.BatchProcess.RCPT_NOTE.equals(attributeName) ||
                OLEConstants.OLEBatchProcess.RCPT_NOTE.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        List<String> receiptNotes = oleTxRecord.getReceiptNotes();
        if (null == receiptNotes) {
            receiptNotes = new ArrayList<>();
        }
        if(StringUtils.isNotBlank(attributeValue)) {
            StringTokenizer stringTokenizer = new StringTokenizer(attributeValue, ",");
            while(stringTokenizer.hasMoreTokens()) {
                String note = stringTokenizer.nextToken();
                receiptNotes.add(note);
            }
        }
        oleTxRecord.setReceiptNotes(receiptNotes);
        oleTxRecord.setReceiptNote("Receipt Note");
    }
}
