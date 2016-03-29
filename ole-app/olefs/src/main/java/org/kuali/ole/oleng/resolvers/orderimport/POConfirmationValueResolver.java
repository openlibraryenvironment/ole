package org.kuali.ole.oleng.resolvers.orderimport;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by pvsubrah on 9/3/15.
 */
public class POConfirmationValueResolver extends TxValueResolver {

    @Override
    public boolean isInterested(String attributeName) {
        //TODO : Need to remove the 'OR' condition once old batch is deprecated.
        return OleNGConstants.BatchProcess.PO_CONFIRMATION_INDICATOR.equals(attributeName) ||
                OLEConstants.OLEBatchProcess.PO_CONFIRMATION_INDICATOR.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        oleTxRecord.setPurchaseOrderConfirmationIndicator(Boolean.parseBoolean(attributeValue));
    }
}
