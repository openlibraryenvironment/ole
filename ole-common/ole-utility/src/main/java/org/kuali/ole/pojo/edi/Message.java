package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/2/12
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Message {

    private MessageBeginning messageBeginning;
    private MessageCreationInformation messageCreationInformation;
    private PurchaseOrderQualifier purchaseOrderQualifier;
    private BuyerPartyQualifier buyerPartyQualifier;
    private MessageReference messageReference;
    private SupplierPartyQualifier supplierPartyQualifier;
    private CurrencyDetails currencyDetails;
    private AllowanceOrCharge allowanceOrCharge;
    private Monetary monetary;

    public MessageBeginning getMessageBeginning() {
        return messageBeginning;
    }

    public void setMessageBeginning(MessageBeginning messageBeginning) {
        this.messageBeginning = messageBeginning;
    }

    public PurchaseOrderQualifier getPurchaseOrderQualifier() {
        return purchaseOrderQualifier;
    }

    public void setPurchaseOrderQualifier(PurchaseOrderQualifier purchaseOrderQualifier) {
        this.purchaseOrderQualifier = purchaseOrderQualifier;
    }

    public MessageCreationInformation getMessageCreationInformation() {
        return messageCreationInformation;
    }

    public void setMessageCreationInformation(MessageCreationInformation messageCreationInformation) {
        this.messageCreationInformation = messageCreationInformation;
    }

    public BuyerPartyQualifier getBuyerPartyQualifier() {
        return buyerPartyQualifier;
    }

    public void setBuyerPartyQualifier(BuyerPartyQualifier buyerPartyQualifier) {
        this.buyerPartyQualifier = buyerPartyQualifier;
    }

    public MessageReference getMessageReference() {
        return messageReference;
    }

    public void setMessageReference(MessageReference messageReference) {
        this.messageReference = messageReference;
    }

    public SupplierPartyQualifier getSupplierPartyQualifier() {
        return supplierPartyQualifier;
    }

    public void setSupplierPartyQualifier(SupplierPartyQualifier supplierPartyQualifier) {
        this.supplierPartyQualifier = supplierPartyQualifier;
    }

    public CurrencyDetails getCurrencyDetails() {
        return currencyDetails;
    }

    public void setCurrencyDetails(CurrencyDetails currencyDetails) {
        this.currencyDetails = currencyDetails;
    }

    public AllowanceOrCharge getAllowanceOrCharge() {
        return allowanceOrCharge;
    }

    public void setAllowanceOrCharge(AllowanceOrCharge allowanceOrCharge) {
        this.allowanceOrCharge = allowanceOrCharge;
    }

    public Monetary getMonetary() {
        return monetary;
    }

    public void setMonetary(Monetary monetary) {
        this.monetary = monetary;
    }
}
