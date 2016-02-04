package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 9/3/15.
 */
public class RequestorNoteValueResolver extends TxValueResolver {

    @Override
    public boolean isInterested(String attributeName) {
        return OleNGConstants.BatchProcess.RQST_NOTE.equals(attributeName);
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
