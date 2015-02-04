package org.kuali.ole.batch.keyvalue;

import org.kuali.ole.batch.bo.OLEBatchProcessFilterCriteriaBo;
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
 * Date: 7/7/13
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchFilterCriteriaFieldNameValuesFinder extends KeyValuesBase {

    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEBatchProcessFilterCriteriaBo> oleBatchProcessFilterCriteriaBos = KRADServiceLocator.getBusinessObjectService().findAll(OLEBatchProcessFilterCriteriaBo.class);
        keyValues.add(new ConcreteKeyValue("",""));
        for (OLEBatchProcessFilterCriteriaBo oleBatchProcessFilterCriteriaBo : oleBatchProcessFilterCriteriaBos) {
            if(oleBatchProcessFilterCriteriaBo.isActiveIndicator()) {
                keyValues.add(new ConcreteKeyValue(oleBatchProcessFilterCriteriaBo.getFieldId(),oleBatchProcessFilterCriteriaBo.getFieldName()));
            }
        }
        return keyValues;
    }
}
