package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by pvsubrah on 9/3/15.
 */
public class AssignToUserValueResolver extends TxValueResolver {

    @Override
    public boolean isInterested(String attributeName) {
        return OleNGConstants.BatchProcess.ASSIGN_TO_USER.equals(attributeName);
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        oleTxRecord.setAssignToUser(attributeValue);
    }
}
