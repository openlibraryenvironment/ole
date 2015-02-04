package org.kuali.ole.batch.keyvalue;

import org.kuali.ole.describe.bo.OleBibliographicRecordStatus;
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
 * Date: 7/26/13
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileBibStatusValuesFinder extends KeyValuesBase {

    public List<KeyValue> getKeyValues() {

        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleBibliographicRecordStatus> oleBatchProcessFilterCriteriaBos = KRADServiceLocator.getBusinessObjectService().findAll(OleBibliographicRecordStatus.class);
        keyValues.add(new ConcreteKeyValue("",""));
        for (OleBibliographicRecordStatus oleBatchProcessFilterCriteriaBo : oleBatchProcessFilterCriteriaBos) {
            keyValues.add(new ConcreteKeyValue(oleBatchProcessFilterCriteriaBo.getBibliographicRecordStatusName(),oleBatchProcessFilterCriteriaBo.getBibliographicRecordStatusName()));

        }
        return keyValues;
    }
}
