package org.kuali.ole.batch.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.impl.OLEBatchProcess;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 7/31/13
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessExportScopeValuesFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue(OLEBatchProcess.EXPORT_FULL,"Full"));
        keyValues.add(new ConcreteKeyValue(OLEBatchProcess.EXPORT_EX_STAFF,"Full except staff-only"));
        keyValues.add(new ConcreteKeyValue(OLEBatchProcess.EXPORT_FILTER,"Filter"));
        keyValues.add(new ConcreteKeyValue(OLEBatchProcess.EXPORT_INC,"Incremental"));
        keyValues.add(new ConcreteKeyValue(OLEBatchProcess.INCREMENTAL_EXPORT_EX_STAFF,"Incremental except staff-only"));
        return keyValues;
    }
}
