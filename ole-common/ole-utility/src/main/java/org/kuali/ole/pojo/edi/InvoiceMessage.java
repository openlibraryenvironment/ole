package org.kuali.ole.pojo.edi;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 9/26/13
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceMessage {



        private MessageBeginning messageBeginning;
        private MessageCreationInformation messageCreationInformation;
        private PurchaseOrderQualifier purchaseOrderQualifier;
        private List<PartyQualifier> partyQualifier;
        private SupplierAdditionalPartyIdentifier supplierAdditionalPartyIdentifier;
        private List<BuyerQualifier> buyerPartyQualifier;
        private BuyerAdditionalPartyIdentifier buyerAdditionalPartyIdentifier;
        private MessageReference messageReference;
        private CurrencyDetails currencyDetails;
        private AllowanceOrCharge allowanceOrCharge;
        private Monetary monetary;

        public void addPartyQualifier(PartyQualifier partyQualifierInfo) {
            if (!this.partyQualifier.contains(partyQualifierInfo)) {
                this.partyQualifier.add(partyQualifierInfo);
            }
        }

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


        public MessageReference getMessageReference() {
            return messageReference;
        }

        public void setMessageReference(MessageReference messageReference) {
            this.messageReference = messageReference;
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

    public List<PartyQualifier> getPartyQualifier() {
        return partyQualifier;
    }

    public void setPartyQualifier(List<PartyQualifier> partyQualifier) {
        this.partyQualifier = partyQualifier;
    }

    public SupplierAdditionalPartyIdentifier getSupplierAdditionalPartyIdentifier() {
        return supplierAdditionalPartyIdentifier;
    }

    public void setSupplierAdditionalPartyIdentifier(SupplierAdditionalPartyIdentifier supplierAdditionalPartyIdentifier) {
        this.supplierAdditionalPartyIdentifier = supplierAdditionalPartyIdentifier;
    }

    public List<BuyerQualifier> getBuyerPartyQualifier() {
        return buyerPartyQualifier;
    }

    public void setBuyerPartyQualifier(List<BuyerQualifier> buyerPartyQualifier) {
        this.buyerPartyQualifier = buyerPartyQualifier;
    }

    public BuyerAdditionalPartyIdentifier getBuyerAdditionalPartyIdentifier() {
        return buyerAdditionalPartyIdentifier;
    }

    public void setBuyerAdditionalPartyIdentifier(BuyerAdditionalPartyIdentifier buyerAdditionalPartyIdentifier) {
        this.buyerAdditionalPartyIdentifier = buyerAdditionalPartyIdentifier;
    }
}


