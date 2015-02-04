package org.kuali.ole;

import org.kuali.ole.pojo.edi.*;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/7/12
 * Time: 8:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class EDIOrderFixture {

    public SenderAndReceiver createSenderAndReceiver(Class clazz) {
        SenderAndReceiver senderAndReceiver = null;
        try {
            senderAndReceiver = (SenderAndReceiver) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("SenderAndReceiver creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SenderAndReceiver creation failed. class = " + clazz);
        }
        senderAndReceiver.setSendersAndReceiversConstant(SendersAndReceiverFixture.SendersAndReceiversConstant.
                createSenderAndReceiverConstant(SendersAndReceiversConstant.class));
        senderAndReceiver.setSenderInformation(SendersAndReceiverFixture.SenderInformation.
                createSenderInformation(SenderInformation.class));
        senderAndReceiver.setReceiverInformation(SendersAndReceiverFixture.ReceiverInformation.
                createReceiverInformation(ReceiverInformation.class));
        senderAndReceiver.setSchedule(SendersAndReceiverFixture.Schedule.
                createSchedule(Schedule.class));
        senderAndReceiver.setInterChangeControlReference(SendersAndReceiverFixture.InterchangeControlReference.
                createInterchangeControlReference());

        return senderAndReceiver;
    }

    public MessageHeader createMessageHeader(Class clazz) {
        MessageHeader messageHeader = null;
        try {
            messageHeader = (MessageHeader) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("MessageHeader creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("MessageHeader creation failed. class = " + clazz);
        }
        messageHeader.setMesssageTypeInformation(MessageHeaderFixture.MesssageTypeInformation.
                createMesssageTypeInformation(MesssageTypeInformation.class));
        messageHeader.setInterchangeControlReference(MessageHeaderFixture.InterChangeControlRef.
                createInterchangeControlReference());

        return messageHeader;
    }

    public Message createMessage(Class clazz) {
        Message message = null;
        try {
            message = (Message) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Message creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Message creation failed. class = " + clazz);
        }
        message.setMessageBeginning(MessageFixture.MessageBeginning.
                createMessageBeginningPojo(MessageBeginning.class));
        message.setMessageCreationInformation(MessageFixture.MessageCreationInformation.
                createMessageCreationInformationPojo(MessageCreationInformation.class));
        message.setBuyerPartyQualifier(MessageFixture.BuyerPartyQualifier.
                createBuyerPartyQualifierPojo(BuyerPartyQualifier.class));
        message.setMessageReference(MessageFixture.MessageReference.
                createMessageReferencePojo(MessageReference.class));
        message.setSupplierPartyQualifier(MessageFixture.SupplierPartyQualifier.
                createSupplierPartyQualifierPojo(SupplierPartyQualifier.class));
        message.setCurrencyDetails(MessageFixture.CurrencyDetails.
                createCurrencyDetailsPojo(CurrencyDetails.class));

        return message;
    }

    public LineItemOrder createLineItemOrder(Class clazz) {
        LineItemOrder lineItemOrder = null;
        try {
            lineItemOrder = (LineItemOrder) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("LineItemOrder creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("LineItemOrder creation failed. class = " + clazz);
        }
        lineItemOrder.addLineItem(LineItemOrderFixture.LINEITEM.createLineItemPojo(LineItem.class));
        lineItemOrder.addProductFunction(LineItemOrderFixture.PRODUCTFUNCTION.createProductFunctionPojo(ProductFunction.class));
        lineItemOrder.addItemDescription(LineItemOrderFixture.ITEMDESCRIPTION.createItemDescriptionPojo(ItemDescription.class));
        lineItemOrder.addQuantityInformation(LineItemOrderFixture.QUANTITYINFORMATION.createQuantityInformationPojo(QuantityInformation.class));
        lineItemOrder.addPriceInformation(LineItemOrderFixture.PRICEINFORMATION.createPriceInformationPojo(PriceInformation.class));
        lineItemOrder.addBuyerReferenceInformation(LineItemOrderFixture.REFERENCEINFORMATION.createBuyerReferenceInformationPojo(BuyerReferenceInformation.class));
        lineItemOrder.addSupplierReferenceInformation(LineItemOrderFixture.SUPPLIERSREFERENCEINFORMATION.createSupplierReferenceInformationPojo(SupplierReferenceInformation.class));
        lineItemOrder.addFundNumberReference(LineItemOrderFixture.FUNDNUMBERREFERENCE.createFundNumberReferencePojo(FundNumberReference.class));
        lineItemOrder.addTransportStageQualifier(LineItemOrderFixture.TRANSPORTSTAGEQUALIFIER.createTransportStageQualifierPojo(TransportStageQualifier.class));
        return lineItemOrder;
    }

    public Summary createSummaryOrder(Class clazz) {
        Summary summary = null;
        try {
            summary = (Summary) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Summary creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Summary creation failed. class = " + clazz);
        }
        summary.setSummarySection(SummaryFixture.SUMMARYSECTION.createSummarySectionPojo(SummarySection.class));
        summary.addControlInfomation(SummaryFixture.CONTROLINFORMATION.createControlInfomationPojo(ControlInfomation.class));
        summary.setUntSummary(SummaryFixture.UNT.createUNTSummaryPojo(UNTSummary.class));
        return summary;
    }
}


