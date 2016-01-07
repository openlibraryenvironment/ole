package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.oleng.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by pvsubrah on 10/4/15.
 */
public class ItemListPriceValueResolver extends TxValueResolver {
    @Override
    public boolean isInterested(String attributeName) {
        return attributeName.equalsIgnoreCase(OleNGConstants.BatchProcess.LIST_PRICE);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        if (null == oleTxRecord.getListPrice()) {
            oleTxRecord.setListPrice(attributeValue);
        }
    }
}
