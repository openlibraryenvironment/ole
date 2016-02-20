package org.kuali.ole.gobi.service.impl;

import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.*;

public class ListedPrintSerialGobiOrderRecordServiceImpl extends OleGobiOrderRecordServiceImpl {

    private PurchaseOrder.Order.ListedPrintSerial.OrderDetail orderDetail;

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
        orderDetail = getOrder().getOrder().getListedPrintSerial().getOrderDetail();
    }
}
