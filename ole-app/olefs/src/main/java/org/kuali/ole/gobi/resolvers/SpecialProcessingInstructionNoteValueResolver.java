package org.kuali.ole.gobi.resolvers;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 10/4/15.
 */
public class SpecialProcessingInstructionNoteValueResolver extends TxValuResolver {
    @Override
    public boolean isInterested(String attributeName) {
        return attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SPL_PROCESS_NOTE);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        List<String> splProcessInstrNotes = oleTxRecord.getSplProcessInstrNotes();
        if (null == splProcessInstrNotes) {
            splProcessInstrNotes = new ArrayList<>();
        }
        splProcessInstrNotes.add(attributeValue);
        oleTxRecord.setSplProcessInstrNotes(splProcessInstrNotes);
        oleTxRecord.setSplProcessInstrNote("Special Processing Instruction Note");
    }
}
