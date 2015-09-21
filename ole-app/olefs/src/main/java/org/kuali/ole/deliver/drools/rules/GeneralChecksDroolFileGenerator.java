package org.kuali.ole.deliver.drools.rules;

import org.kuali.ole.deliver.bo.drools.DroolsRuleBo;
import org.kuali.ole.deliver.drools.DroolFileGenerator;
import org.kuali.ole.deliver.drools.DroolsConstants;

import java.io.File;
import java.util.*;

/**
 * Created by pvsubrah on 7/2/15.
 */
public class GeneralChecksDroolFileGenerator extends DroolFileGenerator {
    List<RuleFormulator> ruleFormulators;

    private String fileDir;
    private String activationGroup;

    public List<RuleFormulator> getRuleFormulators() {
        if (null == ruleFormulators) {
            ruleFormulators = new ArrayList<>();
            ruleFormulators.add(new IsActiveRuleFormulator());
            ruleFormulators.add(new PatronActivationDateRuleFormulator());
            ruleFormulators.add(new IsAddressVerifiedRuleFormulator());
            ruleFormulators.add(new GeneralBlockRuleFormulator());
            ruleFormulators.add(new PatronExpiredRuleFormulator());
            ruleFormulators.add(new PatronAllChargesRuleFormulator());
            ruleFormulators.add(new PatronReplacementAmountRuleFormulator());
            ruleFormulators.add(new PatronOverdueDaysRuleFormulator());
            ruleFormulators.add(new PatronRecallAndOverDueDaysRuleFormulator());
            ruleFormulators.add(new ItemOverdueFineAmountRuleFormulator());
        }
        return ruleFormulators;
    }

    public void setRuleFormulators(List<RuleFormulator> ruleFormulators) {
        this.ruleFormulators = ruleFormulators;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    @Override
    public String getFilePath(String fileName) {
        if (null == fileDir) {
            fileDir = getDroolBaseDirectory() + File.separator + "general" + File.separator + "general-checks.drl";
        }
        return fileDir;
    }

    @Override
    protected List<Map> getCustomRules() {
        List<Map> rules = new ArrayList<>();
        List<DroolsRuleBo> droolsRuleBos = getDroolsRuleBos();
        for (Iterator<DroolsRuleBo> iterator = droolsRuleBos.iterator(); iterator.hasNext(); ) {
            DroolsRuleBo droolsRuleBo = iterator.next();
            for (Iterator<RuleFormulator> ruleFormulatorIterator = getRuleFormulators().iterator(); ruleFormulatorIterator.hasNext(); ) {
                RuleFormulator ruleFormulator = ruleFormulatorIterator.next();
                if(ruleFormulator.isInterested(droolsRuleBo.getRuleType())){
                    rules.add(ruleFormulator.formulateRuleMap(droolsRuleBo));
                    break;
                }
            }
        }
        return rules;
    }

    public void setActivationGroup(String activationGroup) {
        this.activationGroup = activationGroup;
    }

    @Override
    public List<Map> getCustomRulesWithParameters() {
        List<Map> rules = new ArrayList<>();

        List<DroolsRuleBo> droolsRuleBos = getDroolsRuleBos();

        for (Iterator<DroolsRuleBo> iterator = droolsRuleBos.iterator(); iterator.hasNext(); ) {
            DroolsRuleBo droolsRuleBo = iterator.next();
            for (Iterator<RuleFormulator> ruleFormulatorIterator = getRuleFormulators().iterator(); ruleFormulatorIterator.hasNext(); ) {
                RuleFormulator ruleFormulator = ruleFormulatorIterator.next();
                if(ruleFormulator.isInterestedForParameters(droolsRuleBo.getRuleType())){
                    rules.add(ruleFormulator.formulateRuleMap(droolsRuleBo));
                    break;
                }
            }
        }
        return rules;
    }

    @Override
    public boolean isInterested(String value) {
        return value.equals(DroolsConstants.EDITOR_TYPE.GENERAL_CHECK);
    }

    @Override
    protected String getThenCustomRules() {
        return "";
    }
}
