package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.oleng.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 9/3/15.
 */
public class VendorInstructionsNoteValueResolver extends TxValueResolver {

    @Override
    public boolean isInterested(String attributeName) {
        return OleNGConstants.BatchProcess.VNDR_INSTR_NOTE.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {

        List<String> vendorInstrNotes = oleTxRecord.getVendorInstrNotes();
        if (null == vendorInstrNotes) {
            vendorInstrNotes = new ArrayList<>();
        }
        vendorInstrNotes.add(attributeValue);
        oleTxRecord.setVendorInstrNotes(vendorInstrNotes);
        oleTxRecord.setVendorInstrNote("Vendor Instructions Note");



    }
}
