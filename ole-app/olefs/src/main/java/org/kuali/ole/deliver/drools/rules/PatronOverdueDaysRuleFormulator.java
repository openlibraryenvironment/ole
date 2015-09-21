package org.kuali.ole.deliver.drools.rules;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.bo.drools.DroolsRuleBo;
import org.kuali.ole.deliver.bo.drools.FinesAndLimitsBo;
import org.kuali.ole.deliver.drools.DroolsConstants;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 7/9/15.
 */
public class PatronOverdueDaysRuleFormulator extends RuleFormulatorUtil implements RuleFormulator {
    @Override
    public Map formulateRuleMap(DroolsRuleBo droolsRuleBo) {
        return super.formulateRule(droolsRuleBo);
    }

    @Override
    public String formulateRules(DroolsRuleBo droolsRuleBo) {
        return "";
    }

    @Override
    protected String getTemplateFileName() {
        return "general-checks/is-recalled-and-overdue.txt";
    }

    @Override
    protected void processExtraRules(DroolsRuleBo droolsRuleBo, Map map) {
        FinesAndLimitsBo finesAndLimitsBo = droolsRuleBo.getFinesAndLimitsBo();
        StringBuilder stringBuilder = new StringBuilder();
        Object patronOverdueDays = finesAndLimitsBo.getOverDueLimits();
        stringBuilder.append(patronOverdueDays);
        map.put("overdueDays", stringBuilder.toString());
    }

    @Override
    public boolean isInterested(String value) {
        return false;
    }

    @Override
    public boolean isInterestedForParameters(String value) {
        return value.equals(DroolsConstants.GENERAL_CHECK_RULE_TYPE.OVERDUE_CHECK);
    }

    @Override
    public boolean isInterestedForThenCustomRules(String value) {
        return false;
    }
}
