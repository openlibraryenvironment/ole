package org.kuali.ole.gobi.resolvers;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by pvsubrah on 10/4/15.
 */
public class RequisitionSourceValueResolver extends TxValuResolver {
    @Override
    public boolean isInterested(String attributeName) {
        return attributeName.equalsIgnoreCase(OLEConstants.OLEBatchProcess.REQUISITION_SOURCE);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        oleTxRecord.setRequisitionSource(attributeValue);
    }
}
