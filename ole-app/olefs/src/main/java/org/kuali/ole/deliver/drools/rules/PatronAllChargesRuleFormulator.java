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
import java.util.*;

/**
 * Created by pvsubrah on 7/9/15.
 */
public class PatronAllChargesRuleFormulator extends RuleFormulatorUtil implements RuleFormulator {
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
        return "general-checks/all-charges.txt";
    }

    @Override
    protected void processExtraRules(DroolsRuleBo droolsRuleBo, Map map) {
        FinesAndLimitsBo finesAndLimitsBo = droolsRuleBo.getFinesAndLimitsBo();
        StringBuilder stringBuilder = new StringBuilder();
        Object patronAllCharges = finesAndLimitsBo.getLimitAmount();
        stringBuilder.append(patronAllCharges);
        map.put("allCharges", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        String borrowerTypes = finesAndLimitsBo.getBorrowerType();
        if (StringUtils.isNotEmpty(borrowerTypes)) {
            stringBuilder.append(processDataByDelimiter(borrowerTypes));
            map.put("borrowerTypes", stringBuilder.toString());
            map.put("operator", "in");
        }

    }

    @Override
    public boolean isInterested(String value) {
       return false;
    }

    @Override
    public boolean isInterestedForParameters(String value) {
        return value.equals(DroolsConstants.GENERAL_CHECK_RULE_TYPE.ALL_CHARGES);
    }

    @Override
    public boolean isInterestedForThenCustomRules(String value) {
        return false;
    }
}
