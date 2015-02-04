package org.kuali.ole;

import org.kuali.ole.pojo.edi.*;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/8/12
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
public enum MessageFixture {
    MessageBeginning("220",
            "34",
            "9", null, null
    ),
    MessageCreationInformation(
            null, null, null, null, null
    ),
    BuyerPartyQualifier(
            null, null, null, "BY", null
    ),
    MessageReference(
            null, null, null, null, null
    ),
    SupplierPartyQualifier(
            null, null, null, null, "SU"
    ),
    CurrencyDetails(
            null, null, null, null, null
    ),;


    private String messageBeginningOrder;
    private String messageBeginningInterchangeControlReference;
    private String messageBeginningCodeListAgency;
    private String buyer;
    private String supplier;

    private MessageFixture(String messageBeginningOrder, String messageBeginningInterchangeControlReference,
                           String messageBeginningCodeListAgency,
                           String buyer, String supplier) {
        this.messageBeginningOrder = messageBeginningOrder;
        this.messageBeginningInterchangeControlReference = messageBeginningInterchangeControlReference;
        this.messageBeginningCodeListAgency = messageBeginningCodeListAgency;
        this.buyer = buyer;
        this.supplier = supplier;
    }

    public MessageBeginning createMessageBeginningPojo(Class clazz) {
        MessageBeginning messageBeginning = null;
        try {
            messageBeginning = (MessageBeginning) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("MessageBeginning creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("MessageBeginning creation failed. class = " + clazz);
        }
        messageBeginning.setMessageBeginningOrder(messageBeginningOrder);
        messageBeginning.setMessageBeginningInterchangeControlReference(messageBeginningInterchangeControlReference);
        messageBeginning.setMessageBeginningCodeListAgency(messageBeginningCodeListAgency);
        return messageBeginning;
    }

    public MessageCreationInformation createMessageCreationInformationPojo(Class clazz) {
        MessageCreationInformation messageCreationInformation = null;
        try {
            messageCreationInformation = (MessageCreationInformation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("MessageCreationInformation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("MessageCreationInformation creation failed. class = " + clazz);
        }

        messageCreationInformation.setMessageCreationInfoDetails(MessageCreationInfoDetailsFixture.
                MessageCreationInformation.createMessageCreationInfoDetails(MessageCreationInfoDetails.class));
        return messageCreationInformation;
    }

    public BuyerPartyQualifier createBuyerPartyQualifierPojo(Class clazz) {
        BuyerPartyQualifier buyerPartyQualifier = null;
        try {
            buyerPartyQualifier = (BuyerPartyQualifier) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("BuyerPartyQualifier creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("BuyerPartyQualifier creation failed. class = " + clazz);
        }
        buyerPartyQualifier.setBuyer(buyer);
        buyerPartyQualifier.setBuyerInformation(BuyerInformationFixture.
                BuyerInformation.createBuyerInformation(BuyerInformation.class));
        return buyerPartyQualifier;
    }

    public MessageReference createMessageReferencePojo(Class clazz) {
        MessageReference messageReference = null;
        try {
            messageReference = (MessageReference) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("MessageReference creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("MessageReference creation failed. class = " + clazz);
        }
        messageReference.setPartyDetails(PartyDetailsFixture.
                PartyDetails.createPartyDetails(PartyDetails.class));
        return messageReference;
    }


    public SupplierPartyQualifier createSupplierPartyQualifierPojo(Class clazz) {
        SupplierPartyQualifier supplierPartyQualifier = null;
        try {
            supplierPartyQualifier = (SupplierPartyQualifier) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("SupplierPartyQualifier creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SupplierPartyQualifier creation failed. class = " + clazz);
        }
        supplierPartyQualifier.setSupplier(supplier);
        supplierPartyQualifier.setSupplierInformation(SupplierInformationFixture.
                SupplierInformation.createSupplierInformation(SupplierInformation.class));
        return supplierPartyQualifier;
    }

    public CurrencyDetails createCurrencyDetailsPojo(Class clazz) {
        CurrencyDetails currencyDetails = null;
        try {
            currencyDetails = (CurrencyDetails) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("CurrencyDetails creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("CurrencyDetails creation failed. class = " + clazz);
        }

        currencyDetails.setCurrencyDetailsSupplierInformation(CurrencyDetailsSupplierInformationFixture.
                CurrencyDetailsSupplierInformation.
                createCurrencyDetailsSupplierInformation(CurrencyDetailsSupplierInformation.class));
        return currencyDetails;
    }
}
