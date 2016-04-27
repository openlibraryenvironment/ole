package org.kuali.ole.oleng.resolvers.invoiceimport;

import org.kuali.ole.oleng.util.InvoiceImportHelperUtil;
import org.kuali.ole.pojo.OleInvoiceRecord;

/**
 * Created by SheikS on 1/27/2016.
 */
public abstract class InvoiceRecordResolver extends InvoiceImportHelperUtil {

    public abstract boolean isInterested(String attributeName);

    public abstract void setAttributeValue(OleInvoiceRecord oleInvoiceRecord, String attributeValue);

}
