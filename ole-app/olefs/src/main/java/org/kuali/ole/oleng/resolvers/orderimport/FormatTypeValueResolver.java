package org.kuali.ole.oleng.resolvers.orderimport;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;


/**
 * Created by rajeshgp on 15/2/17.
 */
public class FormatTypeValueResolver extends TxValueResolver {
    @Override
    public boolean isInterested(String attributeName) {
        //TODO : Need to remove the 'OR' condition once old batch is deprecated.
        return OleNGConstants.BatchProcess.FORMAT.equals(attributeName) ||
                OLEConstants.OLEBatchProcess.FORMAT.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        oleTxRecord.setFormatTypeId(attributeValue);
    }
}
