package org.kuali.ole.oleng.resolvers.orderimport;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by pvsubrah on 10/4/15.
 */
public class ItemListPriceValueResolver extends TxValueResolver {
    @Override
    public boolean isInterested(String attributeName) {
        //TODO : Need to remove the 'OR' condition once old batch is deprecated.
        return attributeName.equalsIgnoreCase(OleNGConstants.BatchProcess.LIST_PRICE) ||
                attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.LIST_PRICE);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        if (null == oleTxRecord.getListPrice()) {
            oleTxRecord.setListPrice(attributeValue);
        }
    }
}
