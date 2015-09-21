package org.kuali.ole.deliver.drools.rules;

import org.kuali.ole.deliver.bo.drools.DroolsRuleBo;

import java.util.Map;

/**
 * Created by pvsubrah on 7/8/15.
 */
public interface RuleFormulator {

    public Map formulateRuleMap(DroolsRuleBo droolsRuleBo);

    public String formulateRules(DroolsRuleBo droolsRuleBo);

    public boolean isInterested(String value);

    public boolean isInterestedForParameters(String value);

    public boolean isInterestedForThenCustomRules(String value);


}
