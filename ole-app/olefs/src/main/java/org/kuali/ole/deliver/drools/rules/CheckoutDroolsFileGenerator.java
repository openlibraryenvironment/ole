package org.kuali.ole.deliver.drools.rules;

import org.kuali.ole.deliver.bo.drools.DroolsRuleBo;
import org.kuali.ole.deliver.drools.DroolFileGenerator;
import org.kuali.ole.deliver.drools.DroolsConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 7/5/15.
 */
public class CheckoutDroolsFileGenerator extends DroolFileGenerator {
    private String fileDir;
    private List<RuleFormulator> ruleFormulators;

    public List<RuleFormulator> getRuleFormulators() {
        if(null == ruleFormulators){
            ruleFormulators = new ArrayList<>();
            ruleFormulators.add(new CheckoutRuleFormulator());
        }
        return ruleFormulators;
    }

    public void setRuleFormulators(List<RuleFormulator> ruleFormulators) {
        this.ruleFormulators = ruleFormulators;
    }

    @Override
    public String getFilePath(String fileName) {
        if (null == fileDir) {
            fileDir = getDroolBaseDirectory() + File.separator + "checkout" + File.separator + fileName + ".drl";
        }
        return fileDir;
    }

    @Override
    protected List<Map> getCustomRules() {
       return new ArrayList<>();
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
        return value.equals(DroolsConstants.EDITOR_TYPE.CHECKOUT);
    }

    @Override
    protected String getThenCustomRules() {
        List<DroolsRuleBo> droolsRuleBos = getDroolsRuleBos();
        for (Iterator<DroolsRuleBo> iterator = droolsRuleBos.iterator(); iterator.hasNext(); ) {
            DroolsRuleBo droolsRuleBo = iterator.next();
            for (Iterator<RuleFormulator> ruleFormulatorIterator = getRuleFormulators().iterator(); ruleFormulatorIterator.hasNext(); ) {
                RuleFormulator ruleFormulator = ruleFormulatorIterator.next();
                if(ruleFormulator.isInterestedForThenCustomRules(droolsRuleBo.getRuleType())){
                    return ruleFormulator.formulateRules(droolsRuleBo);
                }
            }
        }
        return "";
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }
}
