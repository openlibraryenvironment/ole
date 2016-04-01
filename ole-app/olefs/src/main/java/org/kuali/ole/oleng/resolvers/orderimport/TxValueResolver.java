package org.kuali.ole.oleng.resolvers.orderimport;

import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by pvsubrah on 9/3/15.
 */
public abstract class TxValueResolver {

    public abstract boolean isInterested(String attributeName);

    public abstract void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue);
}
