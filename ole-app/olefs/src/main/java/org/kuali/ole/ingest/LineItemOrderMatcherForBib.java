package org.kuali.ole.ingest;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.pojo.edi.BuyerLineItemReference;
import org.kuali.ole.pojo.edi.BuyerReferenceInformation;
import org.kuali.ole.pojo.edi.LineItemOrder;

import java.util.Iterator;
import java.util.List;

/**
 * LineItemOrderMatcherForBib is for getting lineItemOrder based on vendorProfileCode and vendorReferenceNumber
 */
public class LineItemOrderMatcherForBib {
    /**
     *  This method returns lineItemOrder based on vendorProfileCode and vendorReferenceNumber.
     * @param lineItemOrders
     * @param bibMarcRecord
     * @param vendorProfileCode
     * @return  lineItemOrder
     */
    public LineItemOrder getLineItemOrder(List<LineItemOrder> lineItemOrders, BibMarcRecord bibMarcRecord, String vendorProfileCode) {
        String valuefor035Field = get035FieldValue(bibMarcRecord);
        for (Iterator<LineItemOrder> iterator = lineItemOrders.iterator(); iterator.hasNext(); ) {
            LineItemOrder lineItemOrder = iterator.next();
            BuyerReferenceInformation buyerReferenceInformation = lineItemOrder.getBuyerReferenceInformation().get(0);
            BuyerLineItemReference buyerLineItemReference = buyerReferenceInformation.getBuyerLineItemReference().get(0);
            String orderLineNumber = buyerLineItemReference.getOrderLineNumber();
            String buyersOrderLine = buyerLineItemReference.getBuyersOrderLine();
            String vendorReferenceNumber = buyersOrderLine.equals("SLI") ? orderLineNumber : "";
            if (valuefor035Field.equals("(" + vendorProfileCode + ")" + vendorReferenceNumber)) {
                return lineItemOrder;
            }
        }
        return null;

    }

    /**
     *  This method returns "035" field value based on bibliographicRecord.
     * @param bibMarcRecord
     * @return  035fieldValue.
     */
    private String get035FieldValue(BibMarcRecord bibMarcRecord) {
        List<org.kuali.ole.docstore.common.document.content.bib.marc.DataField> datafields = bibMarcRecord.getDataFields();
        for (Iterator<org.kuali.ole.docstore.common.document.content.bib.marc.DataField> iterator = datafields.iterator(); iterator.hasNext(); ) {
            org.kuali.ole.docstore.common.document.content.bib.marc.DataField marcDataField = iterator.next();
            if (marcDataField.getTag().equals("035")) {
                List<org.kuali.ole.docstore.common.document.content.bib.marc.SubField> subfields = marcDataField.getSubFields();
                for (Iterator<org.kuali.ole.docstore.common.document.content.bib.marc.SubField> marcSubFieldIterator = subfields.iterator(); marcSubFieldIterator.hasNext(); ) {
                    org.kuali.ole.docstore.common.document.content.bib.marc.SubField marcSubField = marcSubFieldIterator.next();
                    if (marcSubField.getCode().equals("a")) {
                        return marcSubField.getValue();
                    }
                }
            }
        }
        return null;
    }
}
