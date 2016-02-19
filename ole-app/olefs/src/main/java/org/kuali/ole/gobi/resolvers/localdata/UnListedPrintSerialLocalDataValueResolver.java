package org.kuali.ole.gobi.resolvers.localdata;

import org.kuali.ole.gobi.GobiConstants;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.gobi.resolvers.LocalDataMappingValueResolver;
import org.kuali.ole.oleng.resolvers.orderimport.TxValueResolver;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.Iterator;

/**
 * Created by pvsubrah on 10/4/15.
 */
public class UnListedPrintSerialLocalDataValueResolver extends LocalDataMappingValueResolver {
    @Override
    public boolean isInterested(String attributeName) {
        boolean isInterested;

        isInterested = (
                attributeName.equalsIgnoreCase(GobiConstants.INITIALS) ||
                        attributeName.equalsIgnoreCase(GobiConstants.START_WITH_VOLUME) ||
                        attributeName.equalsIgnoreCase(GobiConstants.ORDER_NOTES));

        if (isInterested) {
            setSourceFieldName(attributeName);
        }

        return isInterested;
    }

    @Override
    public void setAttributeValue(OleTxRecord oleTxRecord, String attributeValue) {
        for (Iterator<TxValueResolver> iterator = getTxValueResolverList().iterator(); iterator.hasNext(); ) {
            TxValueResolver txValuResolver = iterator.next();
            if (txValuResolver.isInterested(getDestFieldName())) {
                String localValue = getAttributeValue();
                if (null != localValue) {
                    txValuResolver.setAttributeValue(oleTxRecord, localValue);
                }
            }
        }
    }

    private String getAttributeValue() {
        PurchaseOrder.Order order = getPurchaseOrder().getOrder();
        PurchaseOrder.Order.UnlistedPrintSerial unlistedPrintSerial = order.getUnlistedPrintSerial();
        if (null != unlistedPrintSerial) {
            PurchaseOrder.Order.UnlistedPrintSerial.OrderDetail orderDetail = unlistedPrintSerial.getOrderDetail();
            if (getSourceFieldName().equalsIgnoreCase(GobiConstants.INITIALS)) {
                return null != orderDetail ? orderDetail.getInitials() : null;
            } else if (getSourceFieldName().equalsIgnoreCase(GobiConstants.ORDER_NOTES)) {
                return null != orderDetail ? orderDetail.getOrderNotes() : null;
            }else if (getSourceFieldName().equalsIgnoreCase(GobiConstants.START_WITH_VOLUME)) {
                return null != orderDetail ? orderDetail.getStartWithVolume() : null;
            }
        }

        return null;
    }
}
