package org.kuali.ole.batch.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hemalathas on 12/31/14.
 */
public class OLEBatchProcessProfileNameValueFinder extends KeyValuesBase {
    @Override
    public List getKeyValues() {
        List labels = new ArrayList();
        labels.add(new ConcreteKeyValue("", ""));
        labels.add(new ConcreteKeyValue("Order Record Import", OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT));
        labels.add(new ConcreteKeyValue("Invoice Import", OLEConstants.OLEBatchProcess.INVOICE_IMPORT));
        labels.add(new ConcreteKeyValue("Batch Delete", OLEConstants.OLEBatchProcess.BATCH_DELETE));
        labels.add(new ConcreteKeyValue("Batch Export", OLEConstants.OLEBatchProcess.BATCH_EXPORT));
        labels.add(new ConcreteKeyValue("Patron Import", OLEConstants.OLEBatchProcess.PATRON_IMPORT));
        labels.add(new ConcreteKeyValue("Bib Import", OLEConstants.OLEBatchProcess.BATCH_BIB_IMPORT));
        labels.add(new ConcreteKeyValue("Location Import", OLEConstants.OLEBatchProcess.LOCATION_IMPORT));
        labels.add(new ConcreteKeyValue("Claim Report", OLEConstants.OLEBatchProcess.CLAIM_REPORT));
        labels.add(new ConcreteKeyValue("Serial Record Import", OLEConstants.OLEBatchProcess.SERIAL_RECORD_IMPORT));
        labels.add(new ConcreteKeyValue("Fund Record Import", OLEConstants.OLEBatchProcess.FUND_RECORD_IMPORT));
        return labels;
    }

}
