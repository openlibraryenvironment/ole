package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/8/13
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessFieldNameValuesFinder extends KeyValuesBase {

    public List<KeyValue> getKeyValues() {

        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        /*Collection<OLEBatchProcessFilterCriteriaBo> oleBatchProcessFilterCriteriaBos = KRADServiceLocator.getBusinessObjectService().findAll(OLEBatchProcessFilterCriteriaBo.class);
        keyValues.add(new ConcreteKeyValue(" ", " "));
        for (OLEBatchProcessFilterCriteriaBo oleBatchProcessFilterCriteriaBo : oleBatchProcessFilterCriteriaBos) {
            keyValues.add(new ConcreteKeyValue( oleBatchProcessFilterCriteriaBo.getFieldName(), oleBatchProcessFilterCriteriaBo.getFieldName() ));
        }*/

        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("Creation Date","Creation Date"));
        keyValues.add(new ConcreteKeyValue("Updation Date","Updation Date"));
        keyValues.add(new ConcreteKeyValue("Record Status","Record Status"));
        keyValues.add(new ConcreteKeyValue("Record Number","Record Number"));
        keyValues.add(new ConcreteKeyValue("Title","Title"));
        keyValues.add(new ConcreteKeyValue("Author","Author"));
        return keyValues;
    }
}
