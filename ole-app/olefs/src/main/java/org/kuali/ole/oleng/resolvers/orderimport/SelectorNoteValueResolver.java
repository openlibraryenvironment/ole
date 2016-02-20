package org.kuali.ole.oleng.resolvers.orderimport;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 9/3/15.
 */
public class SelectorNoteValueResolver extends TxValueResolver {

    @Override
    public boolean isInterested(String attributeName) {
        //TODO : Need to remove the 'OR' condition once old batch is deprecated.
        return OleNGConstants.BatchProcess.SELECTOR_NOTE.equals(attributeName) ||
                OLEConstants.OLEBatchProcess.SELECTOR_NOTE.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        List<String> selectorNotes = oleTxRecord.getSelectorNotes();
        if (null == selectorNotes) {
            selectorNotes = new ArrayList<>();
        }
        selectorNotes.add(attributeValue);
        oleTxRecord.setSelectorNotes(selectorNotes);
        oleTxRecord.setSelectorNote("Selector Note");
    }
}
