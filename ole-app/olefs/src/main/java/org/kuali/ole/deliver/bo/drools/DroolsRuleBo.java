package org.kuali.ole.deliver.bo.drools;

import org.apache.commons.lang3.StringUtils;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by pvsubrah on 7/8/15.
 */
public class DroolsRuleBo extends PersistableBusinessObjectBase {
    private String ruleId;
    private String agendaGroup;
    private String activationGroup;
    private String ruleType;
    private String editorId;
    private String ruleName;
    private String errorMessage;
    private String overridePermission;
    private FinesAndLimitsBo finesAndLimitsBo;
    private String itemTypes;
    private String itemTypesOperator;
    private String borrowerTypes;
    private String borrowerTypesOperator;
    private String institutionLocations;
    private String institutionLocationsOperator;
    private String campusLocations;
    private String campusLocationsOperator;
    private String libraryLocations;
    private String libraryLocationsOperator;
    private String collectionLocations;
    private String collectionLocationsOperator;
    private String shelvingLocations;
    private String shelvingLocationsOperator;
    private String circulationPolicyId;
    private String loanPeriod;
    private String defaultRecallPeriod;
    private String loanType;
    private String itemTypeCount;
    private String itemTypeCountOperator;


    public String getAgendaGroup() {
        return agendaGroup;
    }

    public void setAgendaGroup(String agendaGroup) {
        this.agendaGroup = agendaGroup;
    }

    public String getActivationGroup() {
        return activationGroup;
    }

    public void setActivationGroup(String activationGroup) {
        this.activationGroup = activationGroup;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOverridePermission() {
        return overridePermission;
    }

    public void setOverridePermission(String overridePermission) {
        this.overridePermission = overridePermission;
    }

    public FinesAndLimitsBo getFinesAndLimitsBo() {
        return finesAndLimitsBo;
    }

    public void setFinesAndLimitsBo(FinesAndLimitsBo finesAndLimitsBo) {
        this.finesAndLimitsBo = finesAndLimitsBo;
    }

    public String getItemTypes() {
        return itemTypes;
    }

    public void setItemTypes(String itemTypes) {
        this.itemTypes = itemTypes;
    }

    public String getBorrowerTypes() {
        return borrowerTypes;
    }

    public void setBorrowerTypes(String borrowerTypes) {
        this.borrowerTypes = borrowerTypes;
    }

    public String getInstitutionLocations() {
        return institutionLocations;
    }

    public void setInstitutionLocations(String institutionLocations) {
        this.institutionLocations = institutionLocations;
    }

    public String getCampusLocations() {
        return campusLocations;
    }

    public void setCampusLocations(String campusLocations) {
        this.campusLocations = campusLocations;
    }

    public String getLibraryLocations() {
        return libraryLocations;
    }

    public void setLibraryLocations(String libraryLocations) {
        this.libraryLocations = libraryLocations;
    }

    public String getCollectionLocations() {
        return collectionLocations;
    }

    public void setCollectionLocations(String collectionLocations) {
        this.collectionLocations = collectionLocations;
    }

    public String getShelvingLocations() {
        return shelvingLocations;
    }

    public void setShelvingLocations(String shelvingLocations) {
        this.shelvingLocations = shelvingLocations;
    }

    public String getCirculationPolicyId() {
        return circulationPolicyId;
    }

    public void setCirculationPolicyId(String circulationPolicyId) {
        this.circulationPolicyId = circulationPolicyId;
    }

    public String getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getDefaultRecallPeriod() {
        return defaultRecallPeriod;
    }

    public void setDefaultRecallPeriod(String defaultRecallPeriod) {
        this.defaultRecallPeriod = defaultRecallPeriod;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getItemTypesOperator() {
        return itemTypesOperator;
    }

    public void setItemTypesOperator(String itemTypesOperator) {
        this.itemTypesOperator = itemTypesOperator;
    }

    public String getBorrowerTypesOperator() {
        return borrowerTypesOperator;
    }

    public void setBorrowerTypesOperator(String borrowerTypesOperator) {
        this.borrowerTypesOperator = borrowerTypesOperator;
    }

    public String getInstitutionLocationsOperator() {
        return institutionLocationsOperator;
    }

    public void setInstitutionLocationsOperator(String institutionLocationsOperator) {
        this.institutionLocationsOperator = institutionLocationsOperator;
    }

    public String getCampusLocationsOperator() {
        return campusLocationsOperator;
    }

    public void setCampusLocationsOperator(String campusLocationsOperator) {
        this.campusLocationsOperator = campusLocationsOperator;
    }

    public String getLibraryLocationsOperator() {
        return libraryLocationsOperator;
    }

    public void setLibraryLocationsOperator(String libraryLocationsOperator) {
        this.libraryLocationsOperator = libraryLocationsOperator;
    }

    public String getCollectionLocationsOperator() {
        return collectionLocationsOperator;
    }

    public void setCollectionLocationsOperator(String collectionLocationsOperator) {
        this.collectionLocationsOperator = collectionLocationsOperator;
    }

    public String getShelvingLocationsOperator() {
        return shelvingLocationsOperator;
    }

    public void setShelvingLocationsOperator(String shelvingLocationsOperator) {
        this.shelvingLocationsOperator = shelvingLocationsOperator;
    }

    public String getItemTypeCount() {
        return itemTypeCount;
    }

    public void setItemTypeCount(String itemTypeCount) {
        this.itemTypeCount = itemTypeCount;
    }

    public String getItemTypeCountOperator() {
        return itemTypeCountOperator;
    }

    public void setItemTypeCountOperator(String itemTypeCountOperator) {
        this.itemTypeCountOperator = itemTypeCountOperator;
    }
}
