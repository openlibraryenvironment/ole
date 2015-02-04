package org.kuali.ole.pojo.edi;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/26/13
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpecialServiceIdentification {

    private String specialServiceCode;
    private String codeListQualifier;
    private String codeListQualifierAgency;
    private String specialService;

    public String getSpecialServiceCode() {
        return specialServiceCode;
    }

    public void setSpecialServiceCode(String specialServiceCode) {
        this.specialServiceCode = specialServiceCode;
    }

    public String getCodeListQualifier() {
        return codeListQualifier;
    }

    public void setCodeListQualifier(String codeListQualifier) {
        this.codeListQualifier = codeListQualifier;
    }

    public String getCodeListQualifierAgency() {
        return codeListQualifierAgency;
    }

    public void setCodeListQualifierAgency(String codeListQualifierAgency) {
        this.codeListQualifierAgency = codeListQualifierAgency;
    }

    public String getSpecialService() {
        return specialService;
    }

    public void setSpecialService(String specialService) {
        this.specialService = specialService;
    }
}
