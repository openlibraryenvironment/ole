package org.kuali.ole.batch.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 10/17/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessInvoiceImportDataTypeValuesFinder extends KeyValuesBase {
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.VENDOR_ITEM_IDENTIFIER, "Vendor Item Identifier"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.VENDOR_NUMBER, "Vendor Number"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.REQUESTOR, "Requestor"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.EBOOK, "E-Book"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.LIST_PRICE, "Invoiced Price"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.FOREIGN_LIST_PRICE, "Invoiced Foreign Price"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.QUANTITY, "Quantity"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.BOOK_PLATE, "Book Plate"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.INVOICE_NUMBER, "Invoice Number"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.INVOICE_DATE, "Invoice Date"));
        //keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE, "Chart Code"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ITEM_DESCRIPTION, "Item Description"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER, "Account Number"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.OBJECT_CODE, "Object Code"));
        Collections.sort(keyValues, new Comparator<KeyValue>() {
            public int compare(KeyValue keyValue1, KeyValue keyValue2) {
                return keyValue1.getValue().compareTo(keyValue2.getValue());

            }
        });

        return keyValues;

    }
}
