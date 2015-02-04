package org.kuali.ole.pojo.edi;

/**
 * Created by palanivel on 14/8/14.
 */
public class BuyerQualifier {
    private String buyerCode;
    private PartyInformation buyerInformation;
    private String nameAndAddress;
    private BuyerPartyName buyerName;
    private PartyStreetName buyerStreetName;
    private String buyerCityName;
    private String buyerCountrySubEntity;
    private String buyerPostalCode;
    private String buyerCountryCode;

    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode;
    }

    public PartyInformation getBuyerInformation() {
        return buyerInformation;
    }

    public void setBuyerInformation(PartyInformation buyerInformation) {
        this.buyerInformation = buyerInformation;
    }

    public String getNameAndAddress() {
        return nameAndAddress;
    }

    public void setNameAndAddress(String nameAndAddress) {
        this.nameAndAddress = nameAndAddress;
    }

    public BuyerPartyName getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(BuyerPartyName buyerName) {
        this.buyerName = buyerName;
    }

    public PartyStreetName getBuyerStreetName() {
        return buyerStreetName;
    }

    public void setBuyerStreetName(PartyStreetName buyerStreetName) {
        this.buyerStreetName = buyerStreetName;
    }

    public String getBuyerCityName() {
        return buyerCityName;
    }

    public void setBuyerCityName(String buyerCityName) {
        this.buyerCityName = buyerCityName;
    }

    public String getBuyerCountrySubEntity() {
        return buyerCountrySubEntity;
    }

    public void setBuyerCountrySubEntity(String buyerCountrySubEntity) {
        this.buyerCountrySubEntity = buyerCountrySubEntity;
    }

    public String getBuyerPostalCode() {
        return buyerPostalCode;
    }

    public void setBuyerPostalCode(String buyerPostalCode) {
        this.buyerPostalCode = buyerPostalCode;
    }

    public String getBuyerCountryCode() {
        return buyerCountryCode;
    }

    public void setBuyerCountryCode(String buyerCountryCode) {
        this.buyerCountryCode = buyerCountryCode;
    }
}
