package org.kuali.ole.pojo.edi;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 9/26/13
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class PartyQualifier {

    private String partyCode;
    private PartyInformation partyInformation;
    private String nameAndAddress;
    private SupplierPartyName supplierPartyName;
    private PartyStreetName partyStreetName;
    private String partyCityName;
    private String partyCountrySubEntity;
    private String partyPostalCode;
    private String partyCountryCode;

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public PartyInformation getPartyInformation() {
        return partyInformation;
    }

    public void setPartyInformation(PartyInformation partyInformation) {
        this.partyInformation = partyInformation;
    }

    public String getNameAndAddress() {
        return nameAndAddress;
    }

    public void setNameAndAddress(String nameAndAddress) {
        this.nameAndAddress = nameAndAddress;
    }

    public SupplierPartyName getSupplierPartyName() {
        return supplierPartyName;
    }

    public void setSupplierPartyName(SupplierPartyName supplierPartyName) {
        this.supplierPartyName = supplierPartyName;
    }

    public PartyStreetName getPartyStreetName() {
        return partyStreetName;
    }

    public void setPartyStreetName(PartyStreetName partyStreetName) {
        this.partyStreetName = partyStreetName;
    }

    public String getPartyCityName() {
        return partyCityName;
    }

    public void setPartyCityName(String partyCityName) {
        this.partyCityName = partyCityName;
    }

    public String getPartyCountrySubEntity() {
        return partyCountrySubEntity;
    }

    public void setPartyCountrySubEntity(String partyCountrySubEntity) {
        this.partyCountrySubEntity = partyCountrySubEntity;
    }

    public String getPartyPostalCode() {
        return partyPostalCode;
    }

    public void setPartyPostalCode(String partyPostalCode) {
        this.partyPostalCode = partyPostalCode;
    }

    public String getPartyCountryCode() {
        return partyCountryCode;
    }

    public void setPartyCountryCode(String partyCountryCode) {
        this.partyCountryCode = partyCountryCode;
    }
}
