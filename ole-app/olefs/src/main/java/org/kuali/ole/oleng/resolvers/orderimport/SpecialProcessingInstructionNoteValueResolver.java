package org.kuali.ole.oleng.resolvers.orderimport;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 10/4/15.
 */
public class SpecialProcessingInstructionNoteValueResolver extends TxValueResolver {
    @Override
    public boolean isInterested(String attributeName) {
        //TODO : Need to remove the 'OR' condition once old batch is deprecated.
        return attributeName.equalsIgnoreCase(OleNGConstants.BatchProcess.SPL_PROCESS_NOTE) ||
                attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.SPL_PROCESS_NOTE);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        List<String> splProcessInstrNotes = oleTxRecord.getSplProcessInstrNotes();
        if (null == splProcessInstrNotes) {
            splProcessInstrNotes = new ArrayList<>();
        }
        if(StringUtils.isNotBlank(attributeValue)) {
            StringTokenizer stringTokenizer = new StringTokenizer(attributeValue, ",");
            while(stringTokenizer.hasMoreTokens()) {
                String note = stringTokenizer.nextToken();
                splProcessInstrNotes.add(note);
            }
        }
        oleTxRecord.setSplProcessInstrNotes(splProcessInstrNotes);
        oleTxRecord.setSplProcessInstrNote("Special Processing Instruction Note");
    }
}
