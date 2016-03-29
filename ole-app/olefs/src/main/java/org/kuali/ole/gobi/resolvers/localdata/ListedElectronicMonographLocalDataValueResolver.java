package org.kuali.ole.gobi.resolvers.localdata;

import org.kuali.ole.gobi.GobiConstants;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.gobi.resolvers.LocalDataMappingValueResolver;
import org.kuali.ole.oleng.resolvers.orderimport.TxValueResolver;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 10/4/15.
 */
public class ListedElectronicMonographLocalDataValueResolver extends LocalDataMappingValueResolver {
    @Override
    public boolean isInterested(String attributeName) {
        boolean isInterested;

        isInterested = ((attributeName.equalsIgnoreCase(GobiConstants.LOCAL_DATA1) ||
                attributeName.equalsIgnoreCase(GobiConstants.LOCAL_DATA2) ||
                attributeName.equalsIgnoreCase(GobiConstants.LOCAL_DATA3) ||
                attributeName.equalsIgnoreCase(GobiConstants.LOCAL_DATA4) ||
                attributeName.equalsIgnoreCase(GobiConstants.LOCAL_DATA5) ||
                attributeName.equalsIgnoreCase(GobiConstants.INITIALS) ||
                attributeName.equalsIgnoreCase(GobiConstants.ORDER_NOTES) ||
                attributeName.equalsIgnoreCase(GobiConstants.PURCHASE_OPTION) ||
                attributeName.equalsIgnoreCase(GobiConstants.LOCATION)));

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
        PurchaseOrder.Order.ListedElectronicMonograph listedElectronicMonograph = order.getListedElectronicMonograph();
        if (null != listedElectronicMonograph) {
            PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail orderDetail = listedElectronicMonograph.getOrderDetail();

            if ((getSourceFieldName().equalsIgnoreCase(GobiConstants.LOCAL_DATA1) ||
                    getSourceFieldName().equalsIgnoreCase(GobiConstants.LOCAL_DATA2) ||
                    getSourceFieldName().equalsIgnoreCase(GobiConstants.LOCAL_DATA3) ||
                    getSourceFieldName().equalsIgnoreCase(GobiConstants.LOCAL_DATA4) ||
                    getSourceFieldName().equalsIgnoreCase(GobiConstants.LOCAL_DATA5))) {

                orderDetail = listedElectronicMonograph.getOrderDetail();
                List<PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.LocalData> localData =
                        orderDetail.getLocalData();

                for (Iterator<PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.LocalData> iterator = localData.iterator(); iterator.hasNext(); ) {
                    PurchaseOrder.Order.ListedElectronicMonograph.OrderDetail.LocalData local = iterator.next();
                    if (getSourceFieldName().equalsIgnoreCase(local.getDescription())) {
                        return local.getValue();
                    }
                }

            } else if (getSourceFieldName().equalsIgnoreCase(GobiConstants.INITIALS)) {
                return null != orderDetail ? orderDetail.getInitials() : null;
            } else if (getSourceFieldName().equalsIgnoreCase(GobiConstants.LOCATION)) {
                return null != orderDetail ? orderDetail.getLocation() : null;
            } else if (getSourceFieldName().equalsIgnoreCase(GobiConstants.ORDER_NOTES)) {
                return null != orderDetail ? orderDetail.getOrderNotes() : null;
            }else if (getSourceFieldName().equalsIgnoreCase(GobiConstants.PURCHASE_OPTION)) {
                return null != orderDetail ? orderDetail.getPurchaseOption().getCode() : null;
            }
        }

        return null;
    }
}
