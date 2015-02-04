package org.kuali.ole.pojo.edi;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/26/13
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AllowanceOrCharge {
    private String allowanceOrChargeQualifier;
    private String freeText;
    private String settlement;
    private String calculationSequenceIndicator;
    private SpecialServiceIdentification specialServiceIdentification;


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

    public SpecialServiceIdentification getSpecialServiceIdentification() {
        return specialServiceIdentification;
    }

    public void setSpecialServiceIdentification(SpecialServiceIdentification specialServiceIdentification) {
        this.specialServiceIdentification = specialServiceIdentification;
    }
}
