package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

public class OleBudgetCode extends PersistableBusinessObjectBase {
    private String budgetCodeId;
    private String inputValue;
    private String chartCode;
    private String fundCode;
    private String objectCode;
    private boolean active;

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public void setBudgetCodeId(String budgetCodeId) {
        this.budgetCodeId = budgetCodeId;
    }

    public String getBudgetCodeId() {
        return budgetCodeId;
    }

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("budgetCodeId", budgetCodeId);
        toStringMap.put("chartCode", chartCode);
        toStringMap.put("fundCode", fundCode);
        toStringMap.put("objectCode", objectCode);
        return toStringMap;
    }
}