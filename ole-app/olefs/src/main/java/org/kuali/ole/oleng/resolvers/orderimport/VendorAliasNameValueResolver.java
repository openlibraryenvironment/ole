package org.kuali.ole.oleng.resolvers.orderimport;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by chenchulakshmig on 4/25/16.
 */
public class VendorAliasNameValueResolver extends TxValueResolver {

    @Override
    public boolean isInterested(String attributeName) {
        //TODO : Need to remove the 'OR' condition once old batch is deprecated.
        return OleNGConstants.BatchProcess.VENDOR_ALIAS_NAME.equals(attributeName) ||
                OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        oleTxRecord.setVendorAliasName(attributeValue);
    }
}
