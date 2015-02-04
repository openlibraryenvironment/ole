package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/11/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessFileDataTypeValuesFinder extends KeyValuesBase {

    public List<KeyValue> getKeyValues() {

        List<KeyValue> keyValues = new ArrayList<KeyValue>();
       /* Collection<OLEBatchProcessFilterCriteriaBo> oleBatchProcessFilterCriteriaBos = KRADServiceLocator.getBusinessObjectService().findAll(OLEBatchProcessFilterCriteriaBo.class);
        keyValues.add(new ConcreteKeyValue(" ", " "));
        for (OLEBatchProcessFilterCriteriaBo oleBatchProcessFilterCriteriaBo : oleBatchProcessFilterCriteriaBos) {
            keyValues.add(new ConcreteKeyValue( oleBatchProcessFilterCriteriaBo.getFieldType(), oleBatchProcessFilterCriteriaBo.getFieldType() ));
        }*/
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("Bibmarc"," Bibmarc"));
        keyValues.add(new ConcreteKeyValue("Holdings"," Holdings"));
        keyValues.add(new ConcreteKeyValue("Item","Item "));
        keyValues.add(new ConcreteKeyValue("EHoldings","EHoldings"));
        /*keyValues.add(new ConcreteKeyValue("OrderImport","Order Import"));*/
       /* keyValues.add(new ConcreteKeyValue("Order","Order"));
        keyValues.add(new ConcreteKeyValue("Patron","Patron"));
        keyValues.add(new ConcreteKeyValue("Location","Location"));
        keyValues.add(new ConcreteKeyValue("Invoice","Invoice"));*/
        /*keyValues.add(new ConcreteKeyValue("User","User"));
        keyValues.add(new ConcreteKeyValue("User","User"));*/
        return keyValues;
    }
}
