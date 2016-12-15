package org.kuali.ole.oleng.gobi.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;

/**
 * Created by SheikS on 8/3/2016.
 */
public class OleNGUnListedPrintSerialGobiOrderRecordServiceImpl extends OleNgGobiOrderImportServiceImpl {

    private PurchaseOrder.Order.UnlistedPrintSerial.OrderDetail orderDetail;

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

    @Override
    public String getLinkToOption(BatchProcessProfile batchProcessProfile) {
        return OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT;
    }
}
