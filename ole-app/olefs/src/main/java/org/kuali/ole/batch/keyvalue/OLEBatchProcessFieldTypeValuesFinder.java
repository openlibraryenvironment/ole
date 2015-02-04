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
public class OLEBatchProcessFieldTypeValuesFinder extends KeyValuesBase {

    public List<KeyValue> getKeyValues() {

        List<KeyValue> keyValues = new ArrayList<KeyValue>();
       /* Collection<OLEBatchProcessFilterCriteriaBo> oleBatchProcessFilterCriteriaBos = KRADServiceLocator.getBusinessObjectService().findAll(OLEBatchProcessFilterCriteriaBo.class);
        keyValues.add(new ConcreteKeyValue(" ", " "));
        for (OLEBatchProcessFilterCriteriaBo oleBatchProcessFilterCriteriaBo : oleBatchProcessFilterCriteriaBos) {
            keyValues.add(new ConcreteKeyValue( oleBatchProcessFilterCriteriaBo.getFieldType(), oleBatchProcessFilterCriteriaBo.getFieldType() ));
        }*/
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("Date","Date"));
       /* keyValues.add(new ConcreteKeyValue(" Date Range"," Date Range"));*/
        keyValues.add(new ConcreteKeyValue("String","String "));
       /* keyValues.add(new ConcreteKeyValue("Bib Status","Bib Status"));
        keyValues.add(new ConcreteKeyValue("Number","Number"));
        keyValues.add(new ConcreteKeyValue("Number Range","Number Range"));*/
        /*keyValues.add(new ConcreteKeyValue("User","User"));
        keyValues.add(new ConcreteKeyValue("User","User"));*/
        return keyValues;
    }
}
