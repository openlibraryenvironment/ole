package org.kuali.ole.oleng.resolvers.invoiceimport;

import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleInvoiceRecord;

/**
 * Created by SheikS on 1/27/2016.
 */
public class InvoicedForeignPriceValueResolver extends InvoiceRecordResolver {

    @Override
    public boolean isInterested(String attributeName) {
        return OleNGConstants.BatchProcess.INVOICE_FOREIGN_PRICE.equalsIgnoreCase(attributeName);
    }

    @Override
    public void setAttributeValue(OleInvoiceRecord oleInvoiceRecord, String attributeValue) {
        oleInvoiceRecord.setForeignListPrice(attributeValue);
    }
}
