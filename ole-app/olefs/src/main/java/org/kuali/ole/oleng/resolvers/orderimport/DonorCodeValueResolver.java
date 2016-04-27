package org.kuali.ole.oleng.resolvers.orderimport;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
        List<String> oleDonors = oleTxRecord.getOleDonors();
        if (null == oleDonors) {
            oleDonors = new ArrayList<>();
        }
        if(StringUtils.isNotBlank(attributeValue)) {
            StringTokenizer stringTokenizer = new StringTokenizer(attributeValue, ",");
            while(stringTokenizer.hasMoreTokens()) {
                String donor = stringTokenizer.nextToken();
                oleDonors.add(donor);
            }
        }
        oleTxRecord.setOleDonors(oleDonors);
    }
}
