package org.kuali.ole.deliver.drools;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.bo.drools.DroolsRuleBo;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Created by pvsubrah on 7/2/15.
 */
public abstract class DroolFileGenerator {

    private List<DroolsRuleBo> droolsRuleBos;

    public List<DroolsRuleBo> getDroolsRuleBos() {
        return droolsRuleBos;
    }

    public void setDroolsRuleBos(List<DroolsRuleBo> droolsRuleBos) {
        this.droolsRuleBos = droolsRuleBos;
    }

    public File generateFile(List<DroolsRuleBo> droolsRuleBos, String fileName) throws Exception {

        this.droolsRuleBos = droolsRuleBos;

        StringBuilder droolFileContent = new StringBuilder();

        List<Map> customRules = getCustomRules();

        String templateStringForPackageImportTextFile = getTemplateStringForPackageImportTextFile();

        droolFileContent.append(templateStringForPackageImportTextFile).append("\n");

        generateRules(droolFileContent, customRules, false);

        List<Map> customRulesWithParameters = getCustomRulesWithParameters();

        generateRulesWithParameters(droolFileContent, customRulesWithParameters);

        String fileContent = droolFileContent.toString();

        File file = new File(getFilePath(fileName));
        FileUtils.write(file, fileContent);
        return file;
    }

    private void generateRulesWithParameters(StringBuilder droolFileContent, List<Map> customRulesWithParameters) {
        generateRules(droolFileContent, customRulesWithParameters, true);
    }

    private void generateRules(StringBuilder droolFileContent, List<Map> customRules, boolean parameters) {
        for (Iterator<Map> iterator = customRules.iterator(); iterator.hasNext(); ) {
            Map ruleMap = iterator.next();
            for (Iterator mapIterator = ruleMap.keySet().iterator(); mapIterator.hasNext(); ) {
                String rule = (String) mapIterator.next();

                String templateStringForRule = getTemplateStringForRule();

                String templateStringForErrorMessage = getTemplateStringForErrorMessage();

                String templateStringForErrorAndPermissions = getTemplateStringForErrorAndPermissions();

                droolFileContent.append(
                        generateDroolFileContent(templateStringForRule,
                                getTemplateStringForAgendaGroup(),
                                getTemplateStringForActivationGroup(),
                                rule,
                                (Map) ruleMap.get(rule),
                                templateStringForErrorMessage,
                                templateStringForErrorAndPermissions,
                                parameters));
            }
        }
    }

    private String generateDroolFileContent(String templateStringForRule,
                                            String templateStringForAgendaGroup,
                                            String templateStringForActivationGroup,
                                            String customRule,
                                            Map placeHolderValues,
                                            String templateStringForErrorMessage, String templateStringForErrorAndPermissions, boolean parameters) {
        StringBuilder stringBuilder = new StringBuilder();

        Map map = new HashMap();
        map.putAll(placeHolderValues);

        StrSubstitutor sub = new StrSubstitutor(map);

        stringBuilder
                .append(sub.replace(templateStringForRule)).append("\n")
                .append(sub.replace(templateStringForAgendaGroup)).append("\n")
                .append(processAgendaGroupString(templateStringForActivationGroup, sub,map)).append("\n")
                .append("when").append("\n")
                .append(getCustomRule(customRule, parameters, sub)).append("\n")
                .append(getTemplateStringForError()).append("\n")
                .append("then").append("\n")
                .append(sub.replace(getThenCustomRules()))
                .append(processErrorMessage(sub, map, templateStringForErrorMessage)).append("\n")
                .append(getOverridePermissions(templateStringForErrorAndPermissions, map, sub))
                .append("end").append("\n").append("\n");

        return stringBuilder.toString();
    }

    private String processErrorMessage(StrSubstitutor sub, Map map, String templateStringForErrorMessage) {
        if(map.containsKey("errorMessage")){
            return sub.replace(templateStringForErrorMessage);
        }
        return "";
    }

    private String processAgendaGroupString(String templateStringForActivationGroup, StrSubstitutor sub, Map map) {
       return map.containsKey("activation-group")? sub.replace(templateStringForActivationGroup) : "";
    }

    private String getCustomRule(String customRule, boolean parameters, StrSubstitutor sub) {
        if (parameters) {
            return sub.replace(customRule);
        }
        return customRule;
    }

    private String getOverridePermissions(String templateStringForErrorAndPermissions, Map map, StrSubstitutor sub) {
        String replacedString = sub.replace(templateStringForErrorAndPermissions);
        String overridePermissions = !StringUtils.isEmpty((String) map.get("overridePermissions")) ? replacedString : "";
        return overridePermissions;
    }

    private String getTemplateStringForError() {
        String templateStringForError = "";
        URL error = getClass().getResource("error.txt");

        try {
            File errorFile = new File(error.toURI());
            templateStringForError = FileUtils.readFileToString(errorFile);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return templateStringForError;
    }

    private String getTemplateStringForErrorAndPermissions() {
        String templateStringForOverridePermissions = "";
        URL errorMessage = getClass().getResource("override-permissions.txt");

        try {
            File errorMessageFile = new File(errorMessage.toURI());
            templateStringForOverridePermissions = FileUtils.readFileToString(errorMessageFile);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return templateStringForOverridePermissions;
    }

    private String getTemplateStringForErrorMessage() {
        String templateStringForErrorMessage = "";
        URL errorMessage = getClass().getResource("error-message.txt");

        try {
            File errorMessageFile = new File(errorMessage.toURI());
            templateStringForErrorMessage = FileUtils.readFileToString(errorMessageFile);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return templateStringForErrorMessage;
    }

    private String getTemplateStringForActivationGroup() {
        String templateStringForActivationGroup = "";
        URL activationGroup = getClass().getResource("activation-group.txt");
        try {
            File activationGroupFile = new File(activationGroup.toURI());
            templateStringForActivationGroup = FileUtils.readFileToString(activationGroupFile);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return templateStringForActivationGroup;
    }

    private String getTemplateStringForAgendaGroup() {
        String templateStringForAgendaGroup = "";
        URL agendaGroup = getClass().getResource("agenda-group.txt");
        try {
            File agendaGroupFile = new File(agendaGroup.toURI());
            templateStringForAgendaGroup = FileUtils.readFileToString(agendaGroupFile);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return templateStringForAgendaGroup;
    }

    private String getTemplateStringForRule() {
        String templateStringForRule = "";
        URL rule = getClass().getResource("rule.txt");
        try {
            File ruleFile = new File(rule.toURI());
            templateStringForRule = FileUtils.readFileToString(ruleFile);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return templateStringForRule;
    }

    private String getTemplateStringForPackageImportTextFile() {
        String templateStringForPackageImportTextFile = "";
        URL packageImportText = getClass().getResource("package-import.txt");
        try {
            File packageImportTextFile = new File(packageImportText.toURI());
            templateStringForPackageImportTextFile = FileUtils.readFileToString(packageImportTextFile);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return templateStringForPackageImportTextFile;
    }

    public abstract String getFilePath(String fileName);

    protected abstract List<Map> getCustomRules();

    public abstract List<Map> getCustomRulesWithParameters();

    public abstract boolean isInterested(String value);

    protected abstract String getThenCustomRules();

    protected String getDroolBaseDirectory() {
        return ConfigContext.getCurrentContextConfig().getProperty("rules.directory");
    }
}
