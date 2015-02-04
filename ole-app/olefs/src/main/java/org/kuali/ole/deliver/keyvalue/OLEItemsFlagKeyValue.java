package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 1/13/14
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEItemsFlagKeyValue extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("", ""));
        keyValues.add(new ConcreteKeyValue(OLEConstants.FLAG_TYP_ITM_DAMAGED, OLEConstants.FLAG_TYP_ITM_DAMAGED));
        keyValues.add(new ConcreteKeyValue(OLEConstants.FLAG_TYP_ITM_MISSING, OLEConstants.FLAG_TYP_ITM_MISSING));
        keyValues.add(new ConcreteKeyValue(OLEConstants.FLAG_TYP_ITM_CLAIMS_RETURNED, OLEConstants.FLAG_TYP_ITM_CLAIMS_RETURNED));
        return keyValues;
    }
}
