package org.kuali.ole.gobi.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;
import org.kuali.ole.pojo.OleTxRecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListedPrintMonographGobiOrderRecordServiceImpl extends OleGobiOrderRecordServiceImpl {

    private PurchaseOrder.Order.ListedPrintMonograph.OrderDetail orderDetail;

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
        orderDetail = getOrder().getOrder().getListedPrintMonograph().getOrderDetail();
    }
}
