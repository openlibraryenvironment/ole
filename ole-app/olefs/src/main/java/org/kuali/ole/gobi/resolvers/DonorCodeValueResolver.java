package org.kuali.ole.gobi.resolvers;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.Collections;

/**
 * Created by pvsubrah on 9/3/15.
 */
public class DonorCodeValueResolver extends TxValuResolver {

    @Override
    public boolean isInterested(String attributeName) {
        return OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_CODE.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        oleTxRecord.setOleDonors(Collections.singletonList(attributeValue));
    }
}
