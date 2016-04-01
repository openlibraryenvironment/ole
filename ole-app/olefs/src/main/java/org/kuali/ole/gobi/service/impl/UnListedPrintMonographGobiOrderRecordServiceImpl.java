package org.kuali.ole.gobi.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;

public class UnListedPrintMonographGobiOrderRecordServiceImpl extends OleGobiOrderRecordServiceImpl {

    private PurchaseOrder.Order.UnlistedPrintMonograph.OrderDetail orderDetail;

    protected String getSubAccount() {
        return getOrder().getCustomerDetail().getSubAccount();
    }

    @Override
    protected void preProcess() {
        orderDetail = getOrder().getOrder().getUnlistedPrintMonograph().getOrderDetail();
    }

    @Override
    protected void setDefaultLocation() {
        setDefaultLocation(orderDetail.getLocation());
    }

    @Override
    protected void setListPrice() {
        String listPrice = orderDetail.getListPrice().getAmount().toString();
        if (StringUtils.isNotBlank(listPrice) && !listPrice.equals("0") && !listPrice.equals("0.00")) {
            setListPrice(listPrice);
        }
    }

    @Override
    protected void setQuantity() {
        setQuantity(orderDetail.getQuantity().toString());
    }

    @Override
    protected void setAccountNumber() {
        setAccountNumber(orderDetail.getFundCode());     // CustomerDetails object is there with baseAccount and subAccount

    }

    @Override
    protected void setReceiptNote() {
        setReceiptNote(orderDetail.getOrderNotes());

    }

    @Override
    protected void setVendorItemIdentifier() {
        String vendorItemIdentifier = orderDetail.getYBPOrderKey().toString();
        setVendorItemIdentifier(vendorItemIdentifier);
        set980SubFielda(vendorItemIdentifier);

    }
}
