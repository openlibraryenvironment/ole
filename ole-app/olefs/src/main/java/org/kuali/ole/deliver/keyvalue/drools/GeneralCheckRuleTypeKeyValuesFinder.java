package org.kuali.ole.deliver.keyvalue.drools;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 7/8/15.
 */
public class GeneralCheckRuleTypeKeyValuesFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<>();
        keyValues.add(new ConcreteKeyValue("", ""));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.GENERAL_CHECK_RULE_TYPE.IS_ACTIVE, DroolsConstants.GENERAL_CHECK_RULE_TYPE.IS_ACTIVE));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.GENERAL_CHECK_RULE_TYPE.IS_BLOCKED, DroolsConstants.GENERAL_CHECK_RULE_TYPE.IS_BLOCKED));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.GENERAL_CHECK_RULE_TYPE.ADDRESS_VERIFIED, DroolsConstants.GENERAL_CHECK_RULE_TYPE.ADDRESS_VERIFIED));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.GENERAL_CHECK_RULE_TYPE.IS_PATRON_EXPIRED, DroolsConstants.GENERAL_CHECK_RULE_TYPE.IS_PATRON_EXPIRED));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.GENERAL_CHECK_RULE_TYPE.ACTIVATION_DATE, DroolsConstants.GENERAL_CHECK_RULE_TYPE.ACTIVATION_DATE));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.GENERAL_CHECK_RULE_TYPE.ALL_CHARGES, DroolsConstants.GENERAL_CHECK_RULE_TYPE.ALL_CHARGES));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.GENERAL_CHECK_RULE_TYPE.REPLACEMENT_FEE_AMOUNT, DroolsConstants.GENERAL_CHECK_RULE_TYPE.REPLACEMENT_FEE_AMOUNT));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.GENERAL_CHECK_RULE_TYPE.OVERDUE_CHECK, DroolsConstants.GENERAL_CHECK_RULE_TYPE.OVERDUE_CHECK));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.GENERAL_CHECK_RULE_TYPE.RECALL_AND_OVERDUE_DAYS, DroolsConstants.GENERAL_CHECK_RULE_TYPE.RECALL_AND_OVERDUE_DAYS));
        keyValues.add(new ConcreteKeyValue(DroolsConstants.GENERAL_CHECK_RULE_TYPE.OVERDUE_FINE_AMOUNT, DroolsConstants.GENERAL_CHECK_RULE_TYPE.OVERDUE_FINE_AMOUNT));
        return keyValues;
    }
}
