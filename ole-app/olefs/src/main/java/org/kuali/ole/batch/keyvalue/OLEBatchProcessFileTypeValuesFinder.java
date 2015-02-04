package org.kuali.ole.batch.keyvalue;

import org.kuali.ole.batch.bo.OLEBatchProcessFileTypeBo;
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
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessFileTypeValuesFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEBatchProcessFileTypeBo> oleBatchProcessFileTypeBos = KRADServiceLocator.getBusinessObjectService().findAll(OLEBatchProcessFileTypeBo.class);
        keyValues.add(new ConcreteKeyValue(" ", " "));
        for (OLEBatchProcessFileTypeBo oleBatchProcessFileTypeBo : oleBatchProcessFileTypeBos) {
            keyValues.add(new ConcreteKeyValue( oleBatchProcessFileTypeBo.getFileTypeDecsription(), oleBatchProcessFileTypeBo.getFileType() ));
        }

        /*keyValues.add(new ConcreteKeyValue(" "," "));
        keyValues.add(new ConcreteKeyValue("mrc","mrc"));
        keyValues.add(new ConcreteKeyValue("edi","edi"));
        keyValues.add(new ConcreteKeyValue("xml","xml"));*/

        return keyValues;
    }
}
