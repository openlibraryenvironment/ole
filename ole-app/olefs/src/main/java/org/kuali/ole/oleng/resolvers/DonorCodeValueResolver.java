package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.oleng.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.Collections;

/**
 * Created by pvsubrah on 9/3/15.
 */
public class DonorCodeValueResolver extends TxValueResolver {

    @Override
    public boolean isInterested(String attributeName) {
        return OleNGConstants.BatchProcess.DESTINATION_FIELD_DONOR_CODE.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        oleTxRecord.setOleDonors(Collections.singletonList(attributeValue));
    }
}
