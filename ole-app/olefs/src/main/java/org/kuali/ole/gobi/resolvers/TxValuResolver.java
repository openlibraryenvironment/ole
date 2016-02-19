package org.kuali.ole.gobi.resolvers;

import org.kuali.ole.pojo.OleTxRecord;

/**
 * Created by pvsubrah on 9/3/15.
 */
public abstract class TxValuResolver {

    public abstract boolean isInterested(String attributeName);

    public abstract void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue);
}
