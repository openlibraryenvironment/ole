package org.kuali.ole.batch.keyvalue;

import org.kuali.ole.batch.bo.OLEBatchProcessTypeBo;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/6/13
 * Time: 7:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessTypeValuesFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        //List<KeyValue> keyValues = new ArrayList<KeyValue>();
       /* keyValues.add(new ConcreteKeyValue("Order Record","Order Record"));
        keyValues.add(new ConcreteKeyValue("Import Bib","Import Bib"));
        keyValues.add(new ConcreteKeyValue("Patron Ingest","Patron Ingest"));
        keyValues.add(new ConcreteKeyValue("Location Ingest","Location Ingest"));
        keyValues.add(new ConcreteKeyValue("Invoice Ingest","Invoice Ingest"));
        keyValues.add(new ConcreteKeyValue("Batch Delete","Batch Delete"));
        keyValues.add(new ConcreteKeyValue("Batch Export","Batch Export"));*/

        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEBatchProcessTypeBo> oleBatchProcessTypes = KRADServiceLocator.getBusinessObjectService().findAll(OLEBatchProcessTypeBo.class);
        keyValues.add(new ConcreteKeyValue("",""));
        for (OLEBatchProcessTypeBo oleBatchProcessType : oleBatchProcessTypes) {
            if(oleBatchProcessType.isActive()) {
                keyValues.add(new ConcreteKeyValue(oleBatchProcessType.getBatchProcessTypeName(),oleBatchProcessType.getBatchProcessTypeName()));
            }
        }

        return keyValues;
    }
}
