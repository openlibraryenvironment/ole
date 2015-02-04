package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleStatisticalSearchingCodes;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 8/5/13
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEStatisticalSearchingCodesValueFinder extends KeyValuesBase {
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleStatisticalSearchingCodes> oleStatisticalSearchingCodes = KRADServiceLocator.getBusinessObjectService().findAll(OleStatisticalSearchingCodes.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleStatisticalSearchingCodes type : oleStatisticalSearchingCodes) {
            if (type.isActive()) {
                options.add(new ConcreteKeyValue(type.getStatisticalSearchingCodeId().toString(), type.getStatisticalSearchingName()));
            }
        }
        return options;
    }
}
