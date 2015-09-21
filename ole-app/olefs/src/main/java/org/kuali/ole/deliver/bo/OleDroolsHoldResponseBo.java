package org.kuali.ole.deliver.bo;

import java.math.BigDecimal;

/**
 * Created by chenchulakshmig on 8/14/15.
 */
public class OleDroolsHoldResponseBo {

    private boolean ruleMatched;
    private int requestExpirationDay;
    private String minimumLoanPeriod;
    private String recallLoanPeriod;
    private int courtesyNoticeInterval;
    private int numberOfOverdueToBeSent;
    private int intervalToGenerateOverdueNotice;
    private int replacementBill;

    public boolean isRuleMatched() {
        return ruleMatched;
    }

    public void setRuleMatched(boolean ruleMatched) {
        this.ruleMatched = ruleMatched;
    }

    public int getRequestExpirationDay() {
        return requestExpirationDay;
    }

    public void setRequestExpirationDay(int requestExpirationDay) {
        this.requestExpirationDay = requestExpirationDay;
    }

    public String getMinimumLoanPeriod() {
        return minimumLoanPeriod;
    }

    public void setMinimumLoanPeriod(String minimumLoanPeriod) {
        this.minimumLoanPeriod = minimumLoanPeriod;
    }

    public String getRecallLoanPeriod() {
        return recallLoanPeriod;
    }

    public void setRecallLoanPeriod(String recallLoanPeriod) {
        this.recallLoanPeriod = recallLoanPeriod;
    }

    public int getCourtesyNoticeInterval() {
        return courtesyNoticeInterval;
    }

    public void setCourtesyNoticeInterval(int courtesyNoticeInterval) {
        this.courtesyNoticeInterval = courtesyNoticeInterval;
    }

    public int getNumberOfOverdueToBeSent() {
        return numberOfOverdueToBeSent;
    }

    public void setNumberOfOverdueToBeSent(int numberOfOverdueToBeSent) {
        this.numberOfOverdueToBeSent = numberOfOverdueToBeSent;
    }

    public int getIntervalToGenerateOverdueNotice() {
        return intervalToGenerateOverdueNotice;
    }

    public void setIntervalToGenerateOverdueNotice(int intervalToGenerateOverdueNotice) {
        this.intervalToGenerateOverdueNotice = intervalToGenerateOverdueNotice;
    }

    public int getReplacementBill() {
        return replacementBill;
    }

    public void setReplacementBill(int replacementBill) {
        this.replacementBill = replacementBill;
    }
}
