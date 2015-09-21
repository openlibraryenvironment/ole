package org.kuali.ole.deliver.keyvalue.drools;

import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 7/10/15.
 */
public class DroolEditorTypesKeyValuesFinder extends KeyValuesBase {
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("", ""));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.EDITOR_TYPE.GENERAL_CHECK, DroolsConstants.EDITOR_TYPE.GENERAL_CHECK));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.EDITOR_TYPE.CHECKOUT, DroolsConstants.EDITOR_TYPE.CHECKOUT));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.EDITOR_TYPE.CHECKIN, DroolsConstants.EDITOR_TYPE.CHECKIN));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.EDITOR_TYPE.RENEW, DroolsConstants.EDITOR_TYPE.RENEW));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.EDITOR_TYPE.NOTICE, DroolsConstants.EDITOR_TYPE.NOTICE));
        return keyValues;
    }
}
