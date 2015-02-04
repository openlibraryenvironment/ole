package org.kuali.ole.select.document.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.businessobject.OlePaymentRequestItem;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.businessobject.OlePurchaseOrderLineForInvoice;
import org.kuali.ole.select.businessobject.OlePurchaseOrderTotal;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.service.OleInvoiceItemService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 7/23/13
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleInvoiceItemServiceImpl implements OleInvoiceItemService {


    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public List<OlePurchaseOrderLineForInvoice> getOlePurchaseOrderLineForInvoice(OlePurchaseOrderDocument oleInvoiceDocument) {
        List<OlePurchaseOrderLineForInvoice> olePurchaseOrderLineForInvoiceList = new ArrayList<OlePurchaseOrderLineForInvoice>();
        OlePurchaseOrderLineForInvoice olePurchaseOrderLineForInvoice;
        for (OlePurchaseOrderItem item : (List<OlePurchaseOrderItem>) oleInvoiceDocument.getItems()) {
            if (item.getItemType().getItemTypeCode().equals(OLEConstants.ITM_TYP_CODE)) {
                if (item.isItemForInvoice()) {
                    olePurchaseOrderLineForInvoice = new OlePurchaseOrderLineForInvoice();
                    olePurchaseOrderLineForInvoice.setItemId(item.getItemTitleId());
                    olePurchaseOrderLineForInvoice.setItemTitle(item.getItemDescription() != null ? item.getItemDescription() : "");
                    KualiDecimal noOfCopiesReceived = new KualiDecimal(0.0);
                    if (item.getNoOfCopiesInvoiced() != null) {
                        noOfCopiesReceived = item.getNoOfCopiesInvoiced().kualiDecimalValue();
                    }
                    olePurchaseOrderLineForInvoice.setAmount((item.getItemListPrice().multiply(noOfCopiesReceived)).toString());
                    olePurchaseOrderLineForInvoiceList.add(olePurchaseOrderLineForInvoice);
                }
            }
        }
        return olePurchaseOrderLineForInvoiceList;
    }


    public List<OlePurchaseOrderTotal> getOlePurchaseOrderTotal(OlePurchaseOrderDocument oleInvoiceDocument) {

        OlePurchaseOrderTotal olePurchaseOrderTotal;
        BigDecimal encumAmt = new BigDecimal(0.0);
        BigDecimal encumbranceTotalAmount = new BigDecimal(0.0);
        List<OlePurchaseOrderTotal> olePurchaseOrderTotalList = new ArrayList<OlePurchaseOrderTotal>();
        for (OlePurchaseOrderItem item : (List<OlePurchaseOrderItem>) oleInvoiceDocument.getItems()) {
            olePurchaseOrderTotal = new OlePurchaseOrderTotal();
            String poId = oleInvoiceDocument.getPurapDocumentIdentifier().toString();
            olePurchaseOrderTotal.setPoNumber(poId);
            if (item.getItemType().getItemTypeCode().equals(OLEConstants.ITM_TYP_CODE)) {
                KualiDecimal noOfCopiesRcvd = new KualiDecimal(0.0);
                if (item.getNoOfCopiesInvoiced() != null) {
                    noOfCopiesRcvd = item.getNoOfCopiesInvoiced().kualiDecimalValue();
                }
                encumAmt = encumAmt.add((item.getItemUnitPrice().multiply(noOfCopiesRcvd.bigDecimalValue())));
                olePurchaseOrderTotal.setEncumbranceAmount(encumAmt.toString());
                BigDecimal paidAmount = getTotalPaidItem(item.getItemIdentifier());
                if (paidAmount.intValue() > 0) {
                    if (noOfCopiesRcvd != null && item.getItemListPrice() != null) {
                        encumbranceTotalAmount = ((noOfCopiesRcvd.bigDecimalValue()).multiply(item.getItemListPrice().bigDecimalValue()));
                        olePurchaseOrderTotal.setEncumbranceTotalAmount(encumbranceTotalAmount);
                    }
                }
                olePurchaseOrderTotal.setPaidAmount(paidAmount);
            }
            olePurchaseOrderTotalList.add(olePurchaseOrderTotal);
        }
        return olePurchaseOrderTotalList;
    }


    public BigDecimal getTotalPaidItem(Integer poItemId) {
        Map preqMap = new HashMap();
        preqMap.put("poItemIdentifier", poItemId);
        preqMap.put("itemTypeCode", OLEConstants.ITM_TYP_CODE);
        List<OlePaymentRequestItem> olePaymentRequestItem = (List<OlePaymentRequestItem>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePaymentRequestItem.class, preqMap);
        BigDecimal paidAmt = new BigDecimal(0.0);
        if (olePaymentRequestItem.size() > 0) {
            BigDecimal itmQty = new BigDecimal(olePaymentRequestItem.get(0).getItemQuantity().longValue());
            paidAmt = olePaymentRequestItem.get(0).getItemUnitPrice().multiply(itmQty);
        }
        return paidAmt;
    }


    public List<OlePurchaseOrderLineForInvoice> getOlePurchaseOrderLineForInvoiceForAddItem(OlePurchaseOrderDocument olePurchaseOrderDocument) {

        List<OlePurchaseOrderLineForInvoice> olePurchaseOrderLineForInvoiceList = new ArrayList<OlePurchaseOrderLineForInvoice>();
        OlePurchaseOrderLineForInvoice olePurchaseOrderLineForInvoice;
        for (OlePurchaseOrderItem item : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
            if (item.getItemType().getItemTypeCode().equals(OLEConstants.ITM_TYP_CODE)) {
                olePurchaseOrderLineForInvoice = new OlePurchaseOrderLineForInvoice();
                olePurchaseOrderLineForInvoice.setItemId(item.getItemTitleId());
                olePurchaseOrderLineForInvoice.setItemTitle(item.getItemDescription() != null ? item.getItemDescription() : "");
                KualiDecimal noOfCopiesReceived = item.getItemQuantity();
                olePurchaseOrderLineForInvoice.setAmount((item.getItemListPrice().multiply(noOfCopiesReceived)).toString());
                olePurchaseOrderLineForInvoiceList.add(olePurchaseOrderLineForInvoice);
            }
        }
        return olePurchaseOrderLineForInvoiceList;

    }


    public List<OlePurchaseOrderTotal> getOlePurchaseOrderTotalForAddItem(OlePurchaseOrderDocument olePurchaseOrderDocument) {

        OlePurchaseOrderTotal olePurchaseOrderTotal;
        List<OlePurchaseOrderTotal> olePurchaseOrderTotalList = new ArrayList<OlePurchaseOrderTotal>();
        BigDecimal encumAmt = new BigDecimal(0.0);
        BigDecimal encumbranceTotalAmount = new BigDecimal(0.0);
        olePurchaseOrderTotal = new OlePurchaseOrderTotal();
        String poId = olePurchaseOrderDocument.getPurapDocumentIdentifier().toString();
        olePurchaseOrderTotal.setPoNumber(poId);
        for (OlePurchaseOrderItem item : (List<OlePurchaseOrderItem>) olePurchaseOrderDocument.getItems()) {
            if (item.getItemType().getItemTypeCode().equals(OLEConstants.ITM_TYP_CODE)) {
                KualiDecimal noOfCopiesRcvd = item.getItemQuantity();
                encumAmt = encumAmt.add((item.getItemUnitPrice().multiply(noOfCopiesRcvd.bigDecimalValue())));
                olePurchaseOrderTotal.setEncumbranceAmount(encumAmt.toString());
                BigDecimal paidAmount = getTotalPaidItem(item.getItemIdentifier());
                olePurchaseOrderTotal.setPaidAmount(paidAmount);
                if (paidAmount.intValue() > 0) {
                    if (noOfCopiesRcvd != null && item.getItemListPrice() != null) {
                        encumbranceTotalAmount = ((noOfCopiesRcvd.bigDecimalValue()).multiply(item.getItemListPrice().bigDecimalValue()));
                        olePurchaseOrderTotal.setEncumbranceTotalAmount(encumbranceTotalAmount);
                    }
                }
            }
        }
        olePurchaseOrderTotalList.add(olePurchaseOrderTotal);

        return olePurchaseOrderTotalList;
    }


}