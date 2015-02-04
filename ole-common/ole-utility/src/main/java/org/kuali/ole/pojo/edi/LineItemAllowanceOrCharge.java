package org.kuali.ole.pojo.edi;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 9/20/13
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class LineItemAllowanceOrCharge {

    private String allowanceOrChargeQualifier;
    private String freeText;
    private String settlement;
    private String calculationSequenceIndicator;
    private List<LineItemSpecialServiceIdentification> lineItemSpecialServiceIdentification;


    public void addLineItemSpecialServiceIdentification(LineItemSpecialServiceIdentification lineItemSpecialServiceIdentification) {
        if (!this.lineItemSpecialServiceIdentification.contains(lineItemSpecialServiceIdentification)) {
            this.lineItemSpecialServiceIdentification.add(lineItemSpecialServiceIdentification);
        }
    }

    public String getAllowanceOrChargeQualifier() {
        return allowanceOrChargeQualifier;
    }

    public void setAllowanceOrChargeQualifier(String allowanceOrChargeQualifier) {
        this.allowanceOrChargeQualifier = allowanceOrChargeQualifier;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public String getCalculationSequenceIndicator() {
        return calculationSequenceIndicator;
    }

    public void setCalculationSequenceIndicator(String calculationSequenceIndicator) {
        this.calculationSequenceIndicator = calculationSequenceIndicator;
    }

    public List<LineItemSpecialServiceIdentification> getLineItemSpecialServiceIdentification() {
        return lineItemSpecialServiceIdentification;
    }

    public void setLineItemSpecialServiceIdentification(List<LineItemSpecialServiceIdentification> lineItemSpecialServiceIdentification) {
        this.lineItemSpecialServiceIdentification = lineItemSpecialServiceIdentification;
    }
}
