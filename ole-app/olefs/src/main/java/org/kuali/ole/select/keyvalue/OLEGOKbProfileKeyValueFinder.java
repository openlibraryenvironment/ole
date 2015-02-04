package org.kuali.ole.select.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by srirams on 27/10/14.
 */
public class OLEGOKbProfileKeyValueFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {


        List<KeyValue> options = new ArrayList<KeyValue>();

        Map map = new HashMap();
        map.put("batchProcessProfileType", OLEConstants.OLEBatchProcess.GOKB_IMPORT);

        List<OLEBatchProcessProfileBo> oleBatchProcessProfileBos = (List<OLEBatchProcessProfileBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEBatchProcessProfileBo.class, map);
//        options.add(new ConcreteKeyValue("", ""));
        for (OLEBatchProcessProfileBo oleBatchProcessProfileBo : oleBatchProcessProfileBos) {
            options.add(new ConcreteKeyValue(String.valueOf(oleBatchProcessProfileBo.getBatchProcessProfileId()), oleBatchProcessProfileBo.getBatchProcessProfileName()));
        }


        return options;
    }
}
