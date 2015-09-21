package org.kuali.ole.deliver.keyvalue.drools;

import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 7/15/15.
 */
public class LoanTypeKeyValueFinder extends KeyValuesBase {
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("", ""));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.REGULAR_LOANS_NOTICE_CONFIG,"Regular Loan"));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.SHORT_TERM_LOANS_NOTICE_CONFIG, "Short Loan"));
        return keyValues;
    }
}