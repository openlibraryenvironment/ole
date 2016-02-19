package org.kuali.ole.gobi.resolvers;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by pvsubrah on 10/4/15.
 */
public class ItemListPriceValueResolver extends TxValuResolver {
    @Override
    public boolean isInterested(String attributeName) {
        return attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.LIST_PRICE);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        if (null == oleTxRecord.getListPrice()) {
            oleTxRecord.setListPrice(attributeValue);
        }
    }
}
