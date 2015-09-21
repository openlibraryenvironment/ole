package org.kuali.ole.deliver.bo.drools;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by pvsubrah on 7/8/15.
 */
public class FinesAndLimitsBo extends PersistableBusinessObjectBase{
    private String id;
    private String borrowerType;
    private String limitAmount;
    private String ruleId;
    private String overDueLimits;
    private String operator;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }

    public String getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(String limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getOverDueLimits() {
        return overDueLimits;
    }

    public void setOverDueLimits(String overDueLimits) {
        this.overDueLimits = overDueLimits;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
