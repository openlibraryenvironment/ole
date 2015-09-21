package org.kuali.ole.deliver.drools.rules;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.deliver.bo.drools.DroolsRuleBo;
import org.kuali.ole.deliver.drools.DroolsConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 7/15/15.
 */
public class CheckoutDroolsFileGenerator_UT {
    @Test
    public void generateCheckoutDroolsFile() throws Exception {
        CheckoutDroolsFileGenerator droolFileGenerator = new CheckoutDroolsFileGenerator();
        droolFileGenerator.setFileDir(System.getProperty("java.io.tmpdir") + "/2-H.drl");

        List<DroolsRuleBo> droolsRuleBoList = new ArrayList<>();

        DroolsRuleBo droolsRuleBo = new DroolsRuleBo();
        droolsRuleBo.setAgendaGroup("checkout-validation");
        droolsRuleBo.setActivationGroup("checkout-validation");
        droolsRuleBo.setRuleName("Check out Circ Policy Set 2-H for Max Loaned Items less than 3");
        droolsRuleBo.setRuleType(DroolsConstants.CHECKOUT_RULE_TYPE.CHECKOUT);
        droolsRuleBo.setLoanType(DroolsConstants.SHORT_TERM_LOANS_NOTICE_CONFIG);
        droolsRuleBo.setCirculationPolicyId("Check out Circ Policy Set 2-H");
        droolsRuleBo.setLoanPeriod("2-H");
        droolsRuleBo.setDefaultRecallPeriod(null);
        droolsRuleBo.setItemTypes("stks-regular,book,cd,audio");
        droolsRuleBo.setItemTypesOperator("not in");
        droolsRuleBo.setInstitutionLocations("UC");
        droolsRuleBo.setInstitutionLocationsOperator("in");
        droolsRuleBo.setLibraryLocations("JRL,JCL");
        droolsRuleBo.setLibraryLocationsOperator("in");
        droolsRuleBo.setShelvingLocations("GEN,MAIN");
        droolsRuleBo.setShelvingLocationsOperator("in");
        droolsRuleBo.setCollectionLocations("GEN,MAIN");
        droolsRuleBo.setCollectionLocationsOperator("in");
        droolsRuleBo.setBorrowerTypes("QGRAD,UGRAD");
        droolsRuleBo.setBorrowerTypesOperator("in");
        droolsRuleBo.setItemTypeCountOperator(">");
        droolsRuleBo.setItemTypeCount("5");
        droolsRuleBo.setCirculationPolicyId("Check out Circ Policy Set 2-H");
        droolsRuleBo.setLoanPeriod("2-H");
        droolsRuleBo.setDefaultRecallPeriod("null");
        droolsRuleBo.setLoanType(DroolsConstants.SHORT_TERM_LOANS_NOTICE_CONFIG);

        droolsRuleBoList.add(droolsRuleBo);

        File generateFile = droolFileGenerator.generateFile(droolsRuleBoList,"2-H");

        assertNotNull(generateFile);
        String readFileToString = FileUtils.readFileToString(generateFile);
        System.out.println(readFileToString);

    }

}