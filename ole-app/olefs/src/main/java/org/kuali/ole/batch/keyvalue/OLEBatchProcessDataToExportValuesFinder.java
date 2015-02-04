package org.kuali.ole.batch.keyvalue;

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
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessDataToExportValuesFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue(OLEBatchProcess.EXPORT_BIB_ONLY,"Bibliographic Data Only"));
        keyValues.add(new ConcreteKeyValue(OLEBatchProcess.EXPORT_BIB_AND_INSTANCE,"Bibliographic and Instance Data"));
        keyValues.add(new ConcreteKeyValue(OLEBatchProcess.EXPORT_BIB_INSTANCE_AND_EINSTANCE,"Bibliographic,Instance and EInstance Data"));
        return keyValues;
    }
}
