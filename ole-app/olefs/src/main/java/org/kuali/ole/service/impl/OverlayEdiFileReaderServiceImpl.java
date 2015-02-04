package org.kuali.ole.service.impl;

import org.kuali.ole.pojo.edi.*;
import org.kuali.ole.service.OverlayFileReaderService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 2/22/13
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayEdiFileReaderServiceImpl implements OverlayFileReaderService {

    private Object object;

    @Override
    public void setObject(Object object) {
        this.object = object;
    }

    public String getInputFieldValue(String incomingField){
        Map<String, String> referenceNumberMap = new HashMap<String, String>();
        if(object!=null && object instanceof LineItemOrder){
            LineItemOrder lineItemOrder = (LineItemOrder) object;
            for(BuyerReferenceInformation buyerReferenceInformation : lineItemOrder.getBuyerReferenceInformation()){
                for(BuyerLineItemReference buyerLineItemReference : buyerReferenceInformation.getBuyerLineItemReference()){
                    referenceNumberMap.put(buyerLineItemReference.getBuyersOrderLine(),buyerLineItemReference.getOrderLineNumber());
                    break;
                }
            }

            for(SupplierReferenceInformation supplierReferenceInformation : lineItemOrder.getSupplierReferenceInformation()){
                for(SupplierLineItemReference supplierLineItemReference : supplierReferenceInformation.getSupplierLineItemReference()){
                    referenceNumberMap.put(supplierLineItemReference.getSuppliersOrderLine(),supplierLineItemReference.getVendorReferenceNumber());
                    break;
                }
            }
        }
        return referenceNumberMap.get(incomingField)!=null?referenceNumberMap.get(incomingField):"";
    }
}
