package org.kuali.ole.oleng.resolvers.invoiceimport;

import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.pojo.OleInvoiceRecord;

/**
 * Created by SheikS on 1/27/2016.
 */
public class CurrencyTypeValueResolver extends InvoiceRecordResolver {

    @Override
    public boolean isInterested(String attributeName) {
        return OleNGConstants.BatchProcess.CURRENCY_TYPE.equalsIgnoreCase(attributeName);
    }

    @Override
    public void setAttributeValue(OleInvoiceRecord oleInvoiceRecord, String attributeValue) {
        oleInvoiceRecord.setCurrencyType(attributeValue);
        String currencyTypeId = getCurrencyTypeIdByCurrencyType(attributeValue);
        oleInvoiceRecord.setCurrencyTypeId(currencyTypeId);
    }
}
