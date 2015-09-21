package org.kuali.ole.deliver.drools;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.deliver.bo.drools.DroolsRuleBo;
import org.kuali.ole.deliver.bo.drools.FinesAndLimitsBo;
import org.kuali.ole.deliver.drools.rules.GeneralChecksDroolFileGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by pvsubrah on 7/1/15.
 */
public class GeneralChecksFileGenerator_UT {

    @Test
    public void generateGeneralChecksDroolsFile() throws Exception {
        GeneralChecksDroolFileGenerator droolFileGenerator = new GeneralChecksDroolFileGenerator();
        droolFileGenerator.setFileDir(System.getProperty("java.io.tmpdir") + "/general-checks.drl");

        List<DroolsRuleBo> droolsRuleBoList = new ArrayList<>();

        setupIsActive(droolsRuleBoList);
        setupIsAddressVerfied(droolsRuleBoList);
        setupActivationDate(droolsRuleBoList);
        setupIsGeneralBlock(droolsRuleBoList);
        setupIsPatronExpired(droolsRuleBoList);
        setupAllCharges(droolsRuleBoList);
        setupReplacementFineAmout(droolsRuleBoList);
        setupOverDueCheck(droolsRuleBoList);
        setupRecallAndOverDueDays(droolsRuleBoList);
        setupOverdueFineAmount(droolsRuleBoList);

        File generateFile = droolFileGenerator.generateFile(droolsRuleBoList,"general-checks-test");
        assertNotNull(generateFile);
        String readFileToString = FileUtils.readFileToString(generateFile);
        System.out.println(readFileToString);
    }

    private void setupActivationDate(List<DroolsRuleBo> droolsRuleBoList) {
        DroolsRuleBo setupActivationDate = new DroolsRuleBo();
        setupActivationDate.setRuleType(DroolsConstants.GENERAL_CHECK_RULE_TYPE.ACTIVATION_DATE);
        setupActivationDate.setAgendaGroup("general-checks");
        setupActivationDate.setRuleName("Patron's Activation date");
        setupActivationDate.setErrorMessage("Patron has an invalid activation date.");
        droolsRuleBoList.add(setupActivationDate);
    }

    private void setupOverdueFineAmount(List<DroolsRuleBo> droolsRuleBoList) {
        DroolsRuleBo overdueFineAmount = new DroolsRuleBo();
        overdueFineAmount.setBorrowerTypes("STFQTR,ALUM25");
        overdueFineAmount.setRuleType(DroolsConstants.GENERAL_CHECK_RULE_TYPE.OVERDUE_FINE_AMOUNT);
        overdueFineAmount.setAgendaGroup("general-checks");
        overdueFineAmount.setRuleName("Patron Has Atleast One Item which has exceeded the overdue limit");
        overdueFineAmount.setErrorMessage("Patron Has Atleast One Item which has exceeded overdue fine limit.");
        FinesAndLimitsBo finesAndLimitsBo = new FinesAndLimitsBo();
        finesAndLimitsBo.setLimitAmount("300");
        overdueFineAmount.setFinesAndLimitsBo(finesAndLimitsBo);
        droolsRuleBoList.add(overdueFineAmount);
    }

private void setupRecallAndOverDueDays(List<DroolsRuleBo> droolsRuleBoList) {
        DroolsRuleBo recallAndOverdueDaysRule = new DroolsRuleBo();
        recallAndOverdueDaysRule.setRuleType(DroolsConstants.GENERAL_CHECK_RULE_TYPE.RECALL_AND_OVERDUE_DAYS);
        recallAndOverdueDaysRule.setAgendaGroup("general-checks");
        recallAndOverdueDaysRule.setRuleName("Patron Has Atleast One Item which has been recalled and has exceeded the overdue limit");
        recallAndOverdueDaysRule.setErrorMessage("Patron Has Atleast One Item which has been recalled and is overdue");
        FinesAndLimitsBo finesAndLimitsBo = new FinesAndLimitsBo();
        finesAndLimitsBo.setOverDueLimits("3");
        recallAndOverdueDaysRule.setFinesAndLimitsBo(finesAndLimitsBo);
        droolsRuleBoList.add(recallAndOverdueDaysRule);
    }

    private void setupOverDueCheck(List<DroolsRuleBo> droolsRuleBoList) {
        DroolsRuleBo overdueRule = new DroolsRuleBo();
        overdueRule.setRuleType(DroolsConstants.GENERAL_CHECK_RULE_TYPE.OVERDUE_CHECK);
        overdueRule.setAgendaGroup("general-checks");
        overdueRule.setRuleName("Patron Has Atleast One Item which is over the allow due limit");
        overdueRule.setErrorMessage("Patron Has Atleast One Item which is over the allowed due limit");
        FinesAndLimitsBo finesAndLimitsBo = new FinesAndLimitsBo();
        finesAndLimitsBo.setOverDueLimits("3");
        overdueRule.setFinesAndLimitsBo(finesAndLimitsBo);
        droolsRuleBoList.add(overdueRule);
    }

    private void setupReplacementFineAmout(List<DroolsRuleBo> droolsRuleBoList) {

        DroolsRuleBo replacementFineRule = new DroolsRuleBo();
        replacementFineRule.setRuleType(DroolsConstants.GENERAL_CHECK_RULE_TYPE.REPLACEMENT_FEE_AMOUNT);
        replacementFineRule.setAgendaGroup("general-checks");
        replacementFineRule.setRuleName("Check Replacement fine");
        replacementFineRule.setErrorMessage("Patron has replacement fees of $150 or more");
        FinesAndLimitsBo finesAndLimitsBo = new FinesAndLimitsBo();
        finesAndLimitsBo.setLimitAmount("150");
        finesAndLimitsBo.setOperator(">");
        finesAndLimitsBo.setBorrowerType("ALUM25,UGRAD");
        replacementFineRule.setFinesAndLimitsBo(finesAndLimitsBo);
        droolsRuleBoList.add(replacementFineRule);
    }

    private void setupIsGeneralBlock(List<DroolsRuleBo> droolsRuleBoList) {
        DroolsRuleBo generalBlock = new DroolsRuleBo();
        generalBlock.setRuleType(DroolsConstants.GENERAL_CHECK_RULE_TYPE.IS_BLOCKED);
        generalBlock.setAgendaGroup("general-checks");
        generalBlock.setEditorId("general-checks");
        generalBlock.setRuleName("Patron Block check");
        generalBlock.setErrorMessage("Patron has a general block");
        generalBlock.setOverridePermission("Patron has a general block");
        droolsRuleBoList.add(generalBlock);
    }

    private void setupIsAddressVerfied(List<DroolsRuleBo> droolsRuleBoList) {
        DroolsRuleBo addressVerified = new DroolsRuleBo();
        addressVerified.setRuleType(DroolsConstants.GENERAL_CHECK_RULE_TYPE.ADDRESS_VERIFIED);
        addressVerified.setAgendaGroup("general-checks");
        addressVerified.setEditorId("general-checks");
        addressVerified.setRuleName("Is Address Verified");
        addressVerified.setErrorMessage("Is Address Verified");
        droolsRuleBoList.add(addressVerified);
    }

    private void setupIsActive(List<DroolsRuleBo> droolsRuleBoList) {
        DroolsRuleBo isActiveRule = new DroolsRuleBo();
        isActiveRule.setRuleType(DroolsConstants.GENERAL_CHECK_RULE_TYPE.IS_ACTIVE);
        isActiveRule.setAgendaGroup("general-checks");
        isActiveRule.setEditorId("general-checks");
        isActiveRule.setRuleName("Is Patron Active");
        isActiveRule.setErrorMessage("Patron is InActive");
        droolsRuleBoList.add(isActiveRule);
    }

    private void setupIsPatronExpired(List<DroolsRuleBo> droolsRuleBoList) {
        DroolsRuleBo patronExpired = new DroolsRuleBo();
        patronExpired.setRuleType(DroolsConstants.GENERAL_CHECK_RULE_TYPE.IS_PATRON_EXPIRED);
        patronExpired.setAgendaGroup("general-checks");
        patronExpired.setEditorId("general-checks");
        patronExpired.setRuleName("Is Patron Expired");
        patronExpired.setErrorMessage("Patron  is Expired");
        droolsRuleBoList.add(patronExpired);
    }

    public void setupAllCharges(List<DroolsRuleBo> droolsRuleBoList) {
        DroolsRuleBo allChargesRule = new DroolsRuleBo();
        allChargesRule.setRuleType(DroolsConstants.GENERAL_CHECK_RULE_TYPE.ALL_CHARGES);
        allChargesRule.setAgendaGroup("general-checks");
        allChargesRule.setRuleName("Patron All Charges");
        allChargesRule.setErrorMessage("Patron's overall charges are $200 or more.");


        FinesAndLimitsBo finesAndLimitsBo = new FinesAndLimitsBo();
        finesAndLimitsBo.setLimitAmount("200");
        finesAndLimitsBo.setOperator(">");
        finesAndLimitsBo.setBorrowerType("QGRAD,UGRAD,ALUM25");

        allChargesRule.setFinesAndLimitsBo(finesAndLimitsBo);
        droolsRuleBoList.add(allChargesRule);
    }
}
