package org.kuali.ole.oleng.resolvers.orderimport;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by pvsubrah on 9/3/15.
 */
public class PreqPosstiveApprovalReqValueResolver extends TxValueResolver {

    @Override
    public boolean isInterested(String attributeName) {
        //TODO : Need to remove the 'OR' condition once old batch is deprecated.
        return OleNGConstants.BatchProcess.PREQ_POSITIVE_APPROVAL_REQ.equals(attributeName) ||
                OLEConstants.OLEBatchProcess.PREQ_POSITIVE_APPROVAL_REQ.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        oleTxRecord.setPayReqPositiveApprovalReq(Boolean.parseBoolean(attributeValue));
    }
}
