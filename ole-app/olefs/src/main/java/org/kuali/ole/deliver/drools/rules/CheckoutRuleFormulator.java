package org.kuali.ole.deliver.drools.rules;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.bo.drools.DroolsRuleBo;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.milyn.util.CollectionsUtil;

import java.util.*;

/**
 * Created by sheiksalahudeenm on 7/14/15.
 */
public class CheckoutRuleFormulator extends RuleFormulatorUtil implements RuleFormulator {
    @Override
    public Map formulateRuleMap(DroolsRuleBo droolsRuleBo) {
        return super.formulateRule(droolsRuleBo);
    }

    @Override
    public String formulateRules(DroolsRuleBo droolsRuleBo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTemplateStringForRuleFromFile("checkout/circulation-policy.txt")).append("\n");
        stringBuilder.append(getTemplateStringForRuleFromFile("checkout/loan-period.txt")).append("\n");
        stringBuilder.append(getTemplateStringForRuleFromFile("checkout/loan-type.txt")).append("\n");
        return stringBuilder.toString();
    }


    /*
    This method is overridden in this class because its responsible for combining and building the template rule
    string from various template rule files. In other cases, this should not be required as each class handles
    one template rule file.
     */
    @Override
    protected String getTemplateString(DroolsRuleBo droolsRuleBo) {
        StringBuilder ruleTemplateString = new StringBuilder();
        ruleTemplateString.append(getTemplateStringForRuleFromFile("loan-document.txt")).append("\n");
        ruleTemplateString.append(getTemplateStringForRuleFromFile("notice-info.txt")).append("\n");
        ruleTemplateString.append(buildItemCriteria(droolsRuleBo)).append("\n");
        ruleTemplateString.append(getTemplateStringForRuleFromFile("checkout/patron-document.txt")).append("\n");

        return ruleTemplateString.toString();
    }

    private String buildItemCriteria(DroolsRuleBo droolsRuleBo) {

        Map map = new HashMap();
        StrSubstitutor sub = new StrSubstitutor(map);
        String templateStringForRuleFromFile = getTemplateStringForRuleFromFile("item-record-for-circ.txt");
        String itemCriteriaRule = getItemCriteriaRule(droolsRuleBo);
        map.put("itemCriteriaRule", itemCriteriaRule);


        String substituedString = sub.replace(templateStringForRuleFromFile);
        return substituedString;
    }

    @Override
    protected String getTemplateFileName() {
        return null;
    }

    @Override
    protected void processExtraRules(DroolsRuleBo droolsRuleBo, Map map) {
        if (StringUtils.isNotBlank(droolsRuleBo.getItemTypes())) {
            String processDataByDelimiter = processDataByDelimiter(droolsRuleBo.getItemTypes());
            map.put("itemTypesOperator", droolsRuleBo.getItemTypesOperator());
            map.put("itemTypes", processDataByDelimiter);
        }
        if (StringUtils.isNotBlank(droolsRuleBo.getInstitutionLocations())) {
            map.put("institutionLocationOperator", droolsRuleBo.getInstitutionLocationsOperator());
            map.put("institutionLocation", processDataByDelimiter(droolsRuleBo.getInstitutionLocations()));
        }
        if (StringUtils.isNotBlank(droolsRuleBo.getCampusLocations())) {
            map.put("campusLocationOperator", droolsRuleBo.getCampusLocationsOperator());
            map.put("campusLocation", processDataByDelimiter(droolsRuleBo.getCampusLocations()));
        }
        if (StringUtils.isNotBlank(droolsRuleBo.getLibraryLocations())) {
            map.put("libraryLocationOperator", droolsRuleBo.getLibraryLocationsOperator());
            map.put("libraryLocation", processDataByDelimiter(droolsRuleBo.getLibraryLocations()));
        }
        if (StringUtils.isNotBlank(droolsRuleBo.getCollectionLocations())) {
            map.put("collectionLocationOperator", droolsRuleBo.getCollectionLocationsOperator());
            map.put("collectionLocation", processDataByDelimiter(droolsRuleBo.getCollectionLocations()));
        }
        if (StringUtils.isNotBlank(droolsRuleBo.getShelvingLocations())) {
            map.put("shelvingLocationOperator", droolsRuleBo.getShelvingLocationsOperator());
            map.put("shelvingLocation", processDataByDelimiter(droolsRuleBo.getShelvingLocations()));
        }
        map.put("circulationPolicy", processQuotesForString(droolsRuleBo.getCirculationPolicyId())) ;
        map.put("loanPeriod", processQuotesForString(droolsRuleBo.getLoanPeriod()));
        map.put("recallPeriod", processQuotesForString(droolsRuleBo.getDefaultRecallPeriod()));
        map.put("loanType", droolsRuleBo.getLoanType());
        map.put("borrowerType",processDataByDelimiter( droolsRuleBo.getBorrowerTypes()));
        map.put("borrowerTypeOperator", droolsRuleBo.getBorrowerTypesOperator());
        map.put("itemTypeCount", droolsRuleBo.getItemTypeCount());
        map.put("itemTypeCountOperator", droolsRuleBo.getItemTypeCountOperator());
    }

    public String processQuotesForString(String data){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\"")
                .append(data)
                .append("\"");

        return stringBuilder.toString();
    }


    @Override
    public boolean isInterested(String value) {
        return false;
    }

    @Override
    public boolean isInterestedForParameters(String value) {
        return value.equals(DroolsConstants.EDITOR_TYPE.CHECKOUT);
    }

    @Override
    public boolean isInterestedForThenCustomRules(String value) {
        return true;
    }

    public String getItemCriteriaRule(DroolsRuleBo droolsRuleBo) {
        List<String> ruleStrings = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(droolsRuleBo.getItemTypes())) {
            ruleStrings.add(getTemplateStringForRuleFromFile("checkout/item-type.txt"));
        }
        if (StringUtils.isNotBlank(droolsRuleBo.getInstitutionLocations())) {
            ruleStrings.add(getTemplateStringForRuleFromFile("checkout/institution-location.txt"));
        }
        if (StringUtils.isNotBlank(droolsRuleBo.getCampusLocations())) {
            ruleStrings.add(getTemplateStringForRuleFromFile("checkout/campus-location.txt"));
        }
        if (StringUtils.isNotBlank(droolsRuleBo.getLibraryLocations())) {
            ruleStrings.add(getTemplateStringForRuleFromFile("checkout/library-location.txt"));
        }
        if (StringUtils.isNotBlank(droolsRuleBo.getCollectionLocations())) {
            ruleStrings.add(getTemplateStringForRuleFromFile("checkout/collection-location.txt"));
        }
        if (StringUtils.isNotBlank(droolsRuleBo.getShelvingLocations())) {
            ruleStrings.add(getTemplateStringForRuleFromFile("checkout/shelving-location.txt"));
        }

        for (Iterator<String> iterator = ruleStrings.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            stringBuilder.append(next);
            if (iterator.hasNext()) {
                stringBuilder.append(",").append("\n");
            }
        }

        return stringBuilder.toString();
    }
}
