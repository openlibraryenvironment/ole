package org.kuali.ole.gobi.service.impl;

import org.kuali.ole.gobi.datobjects.PurchaseOrder;

public class UnListedPrintSerialGobiOrderRecordServiceImpl extends OleGobiOrderRecordServiceImpl {

    private PurchaseOrder.Order.UnlistedPrintSerial.OrderDetail orderDetail;

    @Override
    protected void setDefaultLocation() {
    }

    @Override
    protected void setListPrice() {

    }

    @Override
    protected void setQuantity() {
        setQuantity(orderDetail.getQuantity().toString());
    }

    @Override
    protected void setAccountNumber() {
        setAccountNumber(orderDetail.getFundCode());
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

    @Override
    protected String getSubAccount() {
        return getOrder().getCustomerDetail().getSubAccount();
    }

    @Override
    protected void preProcess() {
        orderDetail = getOrder().getOrder().getUnlistedPrintSerial().getOrderDetail();
    }
}
