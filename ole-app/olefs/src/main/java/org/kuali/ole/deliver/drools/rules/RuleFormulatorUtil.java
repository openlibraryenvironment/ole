package org.kuali.ole.deliver.drools.rules;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.bo.drools.DroolsRuleBo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 7/13/15.
 */
public abstract  class RuleFormulatorUtil {

    protected Map formulateRule(DroolsRuleBo droolsRuleBo){
        Map ruleMap = new HashMap();

        Map map = new HashMap();

        processRuleName(droolsRuleBo, map);

        processAgendaGroup(droolsRuleBo, map);

        processActivationGroup(droolsRuleBo, map);

        processExtraRules(droolsRuleBo, map);

        processErrorMessage(droolsRuleBo, map);

        processOverridePermissions(droolsRuleBo, map);

        String templateStringForRule = null == getTemplateFileName() ? getTemplateString(droolsRuleBo) : getTemplateStringForRuleFromFile(getTemplateFileName());

        ruleMap.put(templateStringForRule, map);

        return ruleMap;
    }

    protected String getTemplateStringForRuleFromFile(String fileName) {
        String templateStringForActiveIndicator = "";
        URL fileURL = getClass().getResource(fileName);
        try {
            File file = new File(fileURL.toURI());
            templateStringForActiveIndicator = FileUtils.readFileToString(file);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return templateStringForActiveIndicator;
    }

    protected void processOverridePermissions(DroolsRuleBo droolsRuleBo, Map map) {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        String isPatronActiveOverridePermission = droolsRuleBo.getOverridePermission();
        if(StringUtils.isNotBlank(isPatronActiveOverridePermission)){
            stringBuilder.append("\"").append(isPatronActiveOverridePermission).append("\"");
            map.put("overridePermissions", stringBuilder.toString());
        }
    }

    protected void processErrorMessage(DroolsRuleBo droolsRuleBo, Map map) {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        String errorMessage = droolsRuleBo.getErrorMessage();
        if (StringUtils.isNotBlank(errorMessage)) {
            stringBuilder.append("\"").append(errorMessage).append("\"");
            map.put("errorMessage", stringBuilder.toString());
        }
    }

    protected void processRuleName(DroolsRuleBo droolsRuleBo, Map map) {
        Object ruleName = droolsRuleBo.getRuleName();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\"").append(ruleName).append("\"");
        map.put("ruleName", stringBuilder.toString());
    }

    protected void processAgendaGroup(DroolsRuleBo droolsRuleBo, Map map) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\"").append(droolsRuleBo.getAgendaGroup()).append("\"");
        map.put("agendaGroup", stringBuilder.toString());
    }

    protected void processActivationGroup(DroolsRuleBo droolsRuleBo, Map map) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\"").append(droolsRuleBo.getActivationGroup()).append("\"");
        map.put("activationGroup", stringBuilder.toString());
    }

    protected String processDataByDelimiter(String dataString) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        StringTokenizer stringTokenizer = new StringTokenizer(dataString, ",");
        while(stringTokenizer.hasMoreTokens()){
            String data =  stringTokenizer.nextToken();
            stringBuilder.append("\"").append(data).append("\"");
            if(stringTokenizer.hasMoreTokens()){
                stringBuilder.append(",");
            }
        }

        stringBuilder.append(")");
        return stringBuilder.toString();
    }




    protected abstract String getTemplateFileName();

    protected abstract void processExtraRules(DroolsRuleBo droolsRuleBo, Map map);

    protected String getTemplateString(DroolsRuleBo droolsRuleBo) {
        return "";
    }
}
