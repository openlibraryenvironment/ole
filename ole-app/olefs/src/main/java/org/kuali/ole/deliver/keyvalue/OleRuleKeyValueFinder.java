package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.drools.DroolsKieEngine;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.impl.repository.RuleBo;

import java.util.*;

/**
 * OleBorrowerKeyValue returns BorrowerTypeId and BorrowerTypeName for OleBorrowerType.
 */
public class OleRuleKeyValueFinder extends KeyValuesBase {
    /**
     * This method will populate the code as a key and name as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<String> agendaGroups = new ArrayList<>();
        agendaGroups.add("checkout validation");
        agendaGroups.add("renewal validation");
        List<String> allLoadedRules = DroolsKieEngine.getInstance().getRulesByAgendaGroup(agendaGroups);

        List<KeyValue> keyValues = new ArrayList<KeyValue>();

        List<ConcreteKeyValue> concreteKeyValues = new ArrayList<ConcreteKeyValue>();
        keyValues.add(new ConcreteKeyValue("", ""));
        for (Iterator<String> iterator = allLoadedRules.iterator(); iterator.hasNext(); ) {
            String ruleName = iterator.next();
            concreteKeyValues.add(new ConcreteKeyValue(ruleName, ruleName));
        }
        concreteKeyValues.add(new ConcreteKeyValue(OLEConstants.NO_CIRC_POLICY_FOUND, OLEConstants.NO_CIRC_POLICY_FOUND));
        Collections.sort(concreteKeyValues);
        keyValues.addAll(concreteKeyValues);
        return keyValues;
    }
}
