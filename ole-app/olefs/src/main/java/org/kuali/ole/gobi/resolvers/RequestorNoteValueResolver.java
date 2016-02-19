package org.kuali.ole.gobi.resolvers;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 9/3/15.
 */
public class RequestorNoteValueResolver extends TxValuResolver {

    @Override
    public boolean isInterested(String attributeName) {
        return OLEConstants.OLEBatchProcess.RQST_NOTE.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        List<String> requestorNotes = oleTxRecord.getRequestorNotes();
        if (null == requestorNotes) {
            requestorNotes = new ArrayList<>();
        }
        requestorNotes.add(attributeValue);
        oleTxRecord.setRequestorNotes(requestorNotes);
        oleTxRecord.setRequestorNote("Requestor Note");
    }
}
