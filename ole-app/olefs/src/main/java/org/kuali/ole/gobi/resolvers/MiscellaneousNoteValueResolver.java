package org.kuali.ole.gobi.resolvers;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.oleng.resolvers.orderimport.TxValueResolver;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 12/18/15.
 */
public class MiscellaneousNoteValueResolver extends TxValueResolver {
    @Override
    public boolean isInterested(String attributeName) {
        return OLEConstants.OLEBatchProcess.MISC_NOTE.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        List<String> miscNotes = oleTxRecord.getMiscellaneousNotes();
        if (null == miscNotes) {
            miscNotes = new ArrayList<>();
        }
        miscNotes.add(attributeValue);
        oleTxRecord.setMiscellaneousNotes(miscNotes);
        oleTxRecord.setMiscellaneousNote(OLEConstants.OLEBatchProcess.MISC_NOTE);
    }
}
