package org.kuali.ole.batch.keyvalue;

import org.kuali.ole.batch.bo.OLEBatchProcessFileTypeBo;
import org.kuali.ole.batch.bo.OLEBatchProcessFilterCriteriaBo;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/25/13
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchFilterDisplayNameValuesFinder  extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
      List<OLEBatchProcessFilterCriteriaBo> oleBatchProcessFileTypeBos=(List<OLEBatchProcessFilterCriteriaBo>) KRADServiceLocator.getBusinessObjectService().findAll(OLEBatchProcessFilterCriteriaBo.class);
      List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
         for(OLEBatchProcessFilterCriteriaBo oleBatchProcessFilterCriteriaBo:oleBatchProcessFileTypeBos){
             if(oleBatchProcessFilterCriteriaBo.getFieldName()!=null &&oleBatchProcessFilterCriteriaBo.getFieldDisplayName()!=null){
                 keyValues.add(new ConcreteKeyValue(oleBatchProcessFilterCriteriaBo.getFieldName(),oleBatchProcessFilterCriteriaBo.getFieldDisplayName().toString()));
             }

         }
        return keyValues;
    }
}