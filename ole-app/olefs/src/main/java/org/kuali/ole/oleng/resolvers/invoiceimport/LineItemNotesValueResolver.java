package org.kuali.ole.oleng.resolvers.invoiceimport;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.select.businessobject.OleInvoiceNote;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by SheikS on 3/31/2016.
 */
public class LineItemNotesValueResolver extends InvoiceRecordResolver {

    @Override
    public boolean isInterested(String attributeName) {
        return OleNGConstants.BatchProcess.LINE_ITEM_NOTE.equalsIgnoreCase(attributeName);
    }

    @Override
    public void setAttributeValue(OleInvoiceRecord oleInvoiceRecord, String attributeValue) {
        List<OleInvoiceNote> lineItemNote = oleInvoiceRecord.getItemNote();
        if (null == lineItemNote) {
            lineItemNote = new ArrayList<>();
        }
        if(StringUtils.isNotBlank(attributeValue)) {
            StringTokenizer stringTokenizer = new StringTokenizer(attributeValue, ",");
            while(stringTokenizer.hasMoreTokens()) {
                String note = stringTokenizer.nextToken();
                OleInvoiceNote oleInvoiceNote = new OleInvoiceNote();
                oleInvoiceNote.setNote(note);
                lineItemNote.add(oleInvoiceNote);
            }
        }
        oleInvoiceRecord.setItemNote(lineItemNote);
    }
}
