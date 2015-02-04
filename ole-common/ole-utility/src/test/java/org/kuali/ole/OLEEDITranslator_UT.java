package org.kuali.ole;


import org.junit.Test;
import org.kuali.ole.converter.OLEEDIConverter;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.kuali.ole.pojo.edi.*;
import org.kuali.ole.pojo.edi.MesssageTypeInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/1/12
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEDITranslator_UT {

    protected static final Logger LOG = LoggerFactory.getLogger(OLEEDITranslator_UT.class);

    /**
     * This method is used to convert an Raw EDI file to XML.
     *
     * @throws Exception
     */
    @Test
    public void testConvertEDIToXML() throws Exception {

        OLEEDIConverter OLEEDITranslator = new OLEEDIConverter();
        assertNotNull(OLEEDITranslator);
        URL ediResource = getClass().getResource("iu.edi");
        File ediFile = new File(ediResource.toURI());
        String fileContent = new FileUtil().readFile(ediFile);

        String javaResult = OLEEDITranslator.convertToXML(fileContent);
        LOG.info(javaResult.toString());


    }

    /**
     * This method is used to generate POJOS from XML.
     *
     * @throws Exception
     */
    @Test
    public void testGenerateXMLToPojos() throws Exception {
        OLEEDIConverter OLEEDITranslator = new OLEEDIConverter();
        assertNotNull(OLEEDITranslator);
        URL ediResource = getClass().getResource("iu.edi");
        File ediFile = new File(ediResource.toURI());
        String fileContent = new FileUtil().readFile(ediFile);
        String xml = OLEEDITranslator.convertToXML(fileContent);

        OLETranscationalRecordGenerator oleTranscationalRecordGenerator = new OLETranscationalRecordGenerator();
        EDIOrders ediOrders = oleTranscationalRecordGenerator.fromXml(xml);

        for (Iterator<EDIOrder> iterator = ediOrders.getOrders().iterator(); iterator.hasNext(); ) {
            EDIOrder ediOrder = iterator.next();
            LOG.info("==============Object Message Out=============");
            LOG.info(ediOrder.getSenderAndReceiver().getSendersAndReceiversConstant().getCode());
            LOG.info(ediOrder.getSenderAndReceiver().getSenderInformation().getSenderId());
            LOG.info(ediOrder.getSenderAndReceiver().getReceiverInformation().getReceiverId());
            LOG.info(ediOrder.getSenderAndReceiver().getSchedule().getPreparationDate());
            LOG.info(ediOrder.getSenderAndReceiver().getInterChangeControlReference());
            LOG.info("======================================\n\n");
        }
    }

    /**
     * This method is used to generate XML from POJOS.
     *
     * @throws Exception
     */
    @Test
    public void testGeneratePojosToXML() throws Exception {
        EDIOrder ediOrder = new EDIOrder();
        EDIOrderFixture ediOrderFixture = new EDIOrderFixture();
        SenderAndReceiver senderAndReceiver = ediOrderFixture.createSenderAndReceiver(SenderAndReceiver.class);
        MessageHeader messageHeader = ediOrderFixture.createMessageHeader(MessageHeader.class);
        Message message = ediOrderFixture.createMessage(Message.class);
        LineItemOrder lineItemOrder = ediOrderFixture.createLineItemOrder(LineItemOrder.class);
        Summary summary = ediOrderFixture.createSummaryOrder(Summary.class);
        FreeTextNotes freeTextNotes = new FreeTextNotes();
        OrderItem orderItem = new OrderItem();
        Trailer trailer = new Trailer();
        BuyerReferenceInformation buyerReferenceInformation = new BuyerReferenceInformation();
        ControlInfomation controlInfomation = new ControlInfomation();
        FundNumberReference fundNumberReference = new FundNumberReference();
        BuyerLineItemReference buyerLineItemReference = new BuyerLineItemReference();
        BuyerPartyQualifier buyerPartyQualifier = new BuyerPartyQualifier();
        BuyerInformation buyerInformation = new BuyerInformation();
        PartyDetails partyDetails = new PartyDetails();
        ItemDescription itemDescription = new ItemDescription();

        List<BuyerLineItemReference> buyerLineItemReferenceList = new ArrayList<BuyerLineItemReference>();

        ediOrder.setSenderAndReceiver(senderAndReceiver);
        ediOrder.setMessageHeader(messageHeader);
        ediOrder.setMessage(message);
        ediOrder.addLineItemOrder(lineItemOrder);
        ediOrder.setSummary(summary);
        OLETranscationalRecordGenerator oleTranscationalRecordGenerator = new OLETranscationalRecordGenerator();
        assertNotNull(ediOrder);
        String ediXml = oleTranscationalRecordGenerator.toXml(ediOrder);
        LOG.info(ediXml);
        freeTextNotes.setNoteLineItem("noteLineItem");
        freeTextNotes.setNoteempty1("noteempty1");
        freeTextNotes.setNoteempty2("noteempty2");
        freeTextNotes.setNoteempty3("noteempty3");
        assertNotNull(freeTextNotes.getNoteLineItem());
        assertNotNull(freeTextNotes.getNoteempty1());
        assertNotNull(freeTextNotes.getNoteempty2());
        assertNotNull(freeTextNotes.getNoteempty3());
        List<FreeTextNotes> freeTextNotesList = new ArrayList<FreeTextNotes>();
        freeTextNotesList.add(freeTextNotes);

        BigDecimal bigDecimal = new BigDecimal("12345.678");
        orderItem.setPrice(bigDecimal);
        orderItem.setProductId("123");
        orderItem.setQuantity(2);
        orderItem.setTitle("title");
        assertNotNull(orderItem.getPrice());
        assertNotNull(orderItem.getProductId());
        assertNotNull(orderItem.getQuantity());
        assertNotNull(orderItem.getTitle());
        LOG.info(orderItem.toString());

        trailer.setUnknown1("unknown1");
        trailer.setUnknown2("unknown2");
        assertNotNull(trailer.getUnknown1());
        assertNotNull(trailer.getUnknown2());

        buyerLineItemReference.setBuyersOrderLine("buyersorderline");
        buyerLineItemReference.setOrderLineNumber("orderlinenumber");
        buyerLineItemReferenceList.add(buyerLineItemReference);
        assertNotNull(buyerLineItemReference.getBuyersOrderLine());
        assertNotNull(buyerLineItemReference.getOrderLineNumber());

        buyerInformation.setBuyerCodeIdentification("00");
        buyerInformation.setBuyerCodeListAgency("001");
        buyerInformation.setBuyerPartyIdentificationCode("0001");
        assertNotNull(buyerInformation.getBuyerCodeIdentification());
        assertNotNull(buyerInformation.getBuyerCodeListAgency());
        assertNotNull(buyerInformation.getBuyerPartyIdentificationCode());

        buyerPartyQualifier.setBuyer("buyer");
        buyerPartyQualifier.setBuyerInformation(buyerInformation);
        assertNotNull(buyerPartyQualifier.getBuyer());
        assertNotNull(buyerPartyQualifier.getBuyerInformation());

        partyDetails.setAdditionalPartyIdentification("0001");
        partyDetails.setPartyName("party");
        assertNotNull(partyDetails.getAdditionalPartyIdentification());
        assertNotNull(partyDetails.getPartyName());

        buyerReferenceInformation.setBuyerLineItemReference(buyerLineItemReferenceList);
        assertNotNull(buyerReferenceInformation.getBuyerLineItemReference());
        List<BuyerReferenceInformation> buyerReferenceInformationList = new ArrayList<BuyerReferenceInformation>();

        itemDescription.setData("data");
        itemDescription.setItemCharacteristicCode("a123");
        itemDescription.setText("itemText");
        assertNotNull(itemDescription.getData());
        assertNotNull(itemDescription.getItemCharacteristicCode());
        assertNotNull(itemDescription.getText());
        List<ItemDescription> itemDescriptionList = new ArrayList<ItemDescription>();
        itemDescriptionList.add(itemDescription);


        SendersAndReceiversConstant sendersAndReceiversConstant = new SendersAndReceiversConstant();
        sendersAndReceiversConstant.setCode("s00");
        sendersAndReceiversConstant.setValue("123");
        assertNotNull(sendersAndReceiversConstant.getCode());
        assertNotNull(sendersAndReceiversConstant.getValue());

        SenderInformation senderInformation = new SenderInformation();
        senderInformation.setSenderId("012");
        senderInformation.setSenderIdType("type");
        assertNotNull(senderInformation.getSenderId());
        assertNotNull(senderInformation.getSenderIdType());

        Schedule schedule = new Schedule();
        schedule.setPreparationDate("12-12-2-2012");
        schedule.setPreparationTime("23:23:00");
        assertNotNull(schedule.getPreparationDate());
        assertNotNull(schedule.getPreparationTime());

        ReceiverInformation receiverInformation = new ReceiverInformation();
        receiverInformation.setReceiverId("r001");
        receiverInformation.setReceiverIdType("typr");
        assertNotNull(receiverInformation.getReceiverId());
        assertNotNull(receiverInformation.getReceiverIdType());

        SummarySection summarySection = new SummarySection();
        summarySection.setSeparatorInformation("sepinfo");
        assertNotNull(summarySection.getSeparatorInformation());

        MessageReference messageReference = new MessageReference();
        messageReference.setPartyDetails(partyDetails);
        assertNotNull(messageReference.getPartyDetails());

        MessageCreationInfoDetails messageCreationInfoDetails = new MessageCreationInfoDetails();
        messageCreationInfoDetails.setMessageCreationInfoConstant("const");
        messageCreationInfoDetails.setMessageCreationInfoDate("12-12-2012");
        messageCreationInfoDetails.setMessageCreationInfoDateFormat("dd-mm-yyyy");
        assertNotNull(messageCreationInfoDetails.getMessageCreationInfoConstant());
        assertNotNull(messageCreationInfoDetails.getMessageCreationInfoDate());
        assertNotNull(messageCreationInfoDetails.getMessageCreationInfoDateFormat());

        MessageCreationInformation messageCreationInformation = new MessageCreationInformation();
        messageCreationInformation.setMessageCreationInfoDetails(messageCreationInfoDetails);
        assertNotNull(messageCreationInformation.getMessageCreationInfoDetails());

        UNTSummary untSummary = new UNTSummary();
        untSummary.setLinSegmentTotal("100");
        untSummary.setSegmentNumber("10");
        assertNotNull(untSummary.getLinSegmentTotal());
        assertNotNull(untSummary.getSegmentNumber());

        TransportStageQualifier transportStageQualifier = new TransportStageQualifier();
        transportStageQualifier.setSurfaceMail("surfacemail");
        transportStageQualifier.setTransportStageConstant("transportStageConstant");
        assertNotNull(transportStageQualifier.getSurfaceMail());
        assertNotNull(transportStageQualifier.getTransportStageConstant());
        List<TransportStageQualifier> transportStageQualifierList = new ArrayList<TransportStageQualifier>();
        transportStageQualifierList.add(transportStageQualifier);

        SupplierLineItemReference supplierLineItemReference = new SupplierLineItemReference();
        supplierLineItemReference.setSuppliersOrderLine("suppliersOrderLine");
        supplierLineItemReference.setVendorReferenceNumber("v0012");
        assertNotNull(supplierLineItemReference.getSuppliersOrderLine());
        assertNotNull(supplierLineItemReference.getVendorReferenceNumber());

        SupplierInformation supplierInformation = new SupplierInformation();
        supplierInformation.setSupplierCodeIdentification("s023");
        supplierInformation.setSupplierCodeListAgency("agency");
        supplierInformation.setSupplierPartyIdentificationCode("p001");
        assertNotNull(supplierInformation.getSupplierCodeIdentification());
        assertNotNull(supplierInformation.getSupplierCodeListAgency());
        assertNotNull(supplierInformation.getSupplierPartyIdentificationCode());

        SupplierPartyQualifier supplierPartyQualifier = new SupplierPartyQualifier();
        supplierPartyQualifier.setSupplier("ABC Vendors");
        supplierPartyQualifier.setSupplierInformation(supplierInformation);
        assertNotNull(supplierPartyQualifier.getSupplier());
        assertNotNull(supplierPartyQualifier.getSupplierInformation());

        LineItemArticleNumber lineItemArticleNumber = new LineItemArticleNumber();
        lineItemArticleNumber.setLineItemIsbn("1234-324-2344");
        lineItemArticleNumber.setLineItemNumberType("type");
        assertNotNull(lineItemArticleNumber.getLineItemIsbn());
        assertNotNull(lineItemArticleNumber.getLineItemNumberType());

        List<LineItemArticleNumber> lineItemArticleNumberList = new ArrayList<LineItemArticleNumber>();
        lineItemArticleNumberList.add(lineItemArticleNumber);

        Qunatity qunatity = new Qunatity();
        qunatity.setQuantity("3");
        qunatity.setQuantityConstant("1");
        assertNotNull(qunatity.getQuantity());
        assertNotNull(qunatity.getQuantityConstant());

        ProductArticleNumber productArticleNumber = new ProductArticleNumber();
        productArticleNumber.setProductIsbn("123-1234-243");
        productArticleNumber.setProductItemNumberType("type");
        assertNotNull(productArticleNumber.getProductIsbn());
        assertNotNull(productArticleNumber.getProductItemNumberType());

        List<ProductArticleNumber> productArticleNumberList = new ArrayList<ProductArticleNumber>();
        productArticleNumberList.add(productArticleNumber);

        ItemPrice itemPrice = new ItemPrice();
        itemPrice.setGrossPrice("23.34");
        itemPrice.setPrice("23");
        assertNotNull(itemPrice.getPrice());
        assertNotNull(itemPrice.getGrossPrice());

        LineItem lineItem = new LineItem();
        lineItem.setItemNumberId("01");
        lineItem.setSequenceNumber("1");
        lineItem.setLineItemArticleNumber(lineItemArticleNumberList);
        assertNotNull(lineItem.getItemNumberId());
        assertNotNull(lineItem.getLineItemArticleNumber());
        assertNotNull(lineItem.getSequenceNumber());
        List<LineItem> lineItemList = new ArrayList<LineItem>();
        lineItemList.add(lineItem);


        ProductFunction productFunction = new ProductFunction();
        productFunction.setProductArticleNumber(productArticleNumberList);
        productFunction.setProductId("p001");
        assertNotNull(productFunction.getProductArticleNumber());
        assertNotNull(productFunction.getProductId());
        List<ProductFunction> productFunctionList = new ArrayList<ProductFunction>();
        productFunctionList.add(productFunction);

        PriceInformation priceInformation = new PriceInformation();
        List<ItemPrice> itemPriceList = new ArrayList<ItemPrice>();
        itemPriceList.add(itemPrice);
        priceInformation.setItemPrice(itemPriceList);
        assertNotNull(priceInformation.getItemPrice());
        List<PriceInformation> priceInformationList = new ArrayList<PriceInformation>();

        QuantityInformation quantityInformation = new QuantityInformation();
        List<Qunatity> qunatityList = new ArrayList<Qunatity>();
        qunatityList.add(qunatity);
        quantityInformation.setQunatity(qunatityList);
        assertNotNull(quantityInformation.getQunatity());
        List<QuantityInformation> quantityInformationList = new ArrayList<QuantityInformation>();
        quantityInformationList.add(quantityInformation);

        Control control = new Control();
        control.setControlQualifier("qualifier");
        control.setTotalQuantitySegments("12");
        assertNotNull(control.getControlQualifier());
        assertNotNull(control.getTotalQuantitySegments());


        MessageBeginning messageBeginning = new MessageBeginning();
        messageBeginning.setMessageBeginningCodeListAgency("messageBeginningCodeListAgency");
        messageBeginning.setMessageBeginningInterchangeControlReference("messageBeginningInterchangeControlReference");
        messageBeginning.setMessageBeginningOrder("messageBeginningOrder");
        assertNotNull(messageBeginning.getMessageBeginningCodeListAgency());
        assertNotNull(messageBeginning.getMessageBeginningInterchangeControlReference());
        assertNotNull(messageBeginning.getMessageBeginningOrder());


        CurrencyDetailsSupplierInformation currencyDetailsSupplierInformation = new CurrencyDetailsSupplierInformation();
        currencyDetailsSupplierInformation.setCurrencyType("dollars");
        currencyDetailsSupplierInformation.setDefaultCurrency("rupees");
        currencyDetailsSupplierInformation.setOrderCurrency("orderCurrency");
        assertNotNull(currencyDetailsSupplierInformation.getCurrencyType());
        assertNotNull(currencyDetailsSupplierInformation.getDefaultCurrency());
        assertNotNull(currencyDetailsSupplierInformation.getOrderCurrency());


        List<Control> controlList = new ArrayList<Control>();
        controlList.add(control);
        controlInfomation.addControlField(control);
        controlInfomation.setControl(controlList);
        assertNotNull(controlInfomation.getControl());

        CurrencyDetails currencyDetails = new CurrencyDetails();
        currencyDetails.setCurrencyDetailsSupplierInformation(currencyDetailsSupplierInformation);
        assertNotNull(currencyDetails.getCurrencyDetailsSupplierInformation());


        BuyersFundNumberReference buyersFundNumberReference = new BuyersFundNumberReference();
        buyersFundNumberReference.setBudgetNumber("123");
        buyersFundNumberReference.setBuyersFundNumber("1");
        assertNotNull(buyersFundNumberReference.getBudgetNumber());
        assertNotNull(buyersFundNumberReference.getBuyersFundNumber());

        SupplierReferenceInformation supplierReferenceInformation = new SupplierReferenceInformation();
        List<SupplierLineItemReference> supplierLineItemReferenceList = new ArrayList<SupplierLineItemReference>();
        supplierLineItemReferenceList.add(supplierLineItemReference);
        supplierReferenceInformation.setSupplierLineItemReference(supplierLineItemReferenceList);
        assertNotNull(supplierReferenceInformation.getSupplierLineItemReference());
        List<SupplierReferenceInformation> supplierReferenceInformationList = new ArrayList<SupplierReferenceInformation>();
        supplierReferenceInformationList.add(supplierReferenceInformation);


        List<BuyersFundNumberReference> buyersFundNumberReferenceList = new ArrayList<BuyersFundNumberReference>();
        buyersFundNumberReferenceList.add(buyersFundNumberReference);
        fundNumberReference.setBuyersFundNumberReference(buyersFundNumberReferenceList);
        assertNotNull(fundNumberReference.getBuyersFundNumberReference());
        List<FundNumberReference> fundNumberReferenceList = new ArrayList<FundNumberReference>();
        fundNumberReferenceList.add(fundNumberReference);

        message.setBuyerPartyQualifier(buyerPartyQualifier);
        message.setCurrencyDetails(currencyDetails);
        message.setMessageBeginning(messageBeginning);
        message.setMessageCreationInformation(messageCreationInformation);
        message.setMessageReference(messageReference);
        message.setSupplierPartyQualifier(supplierPartyQualifier);
        assertNotNull(message.getBuyerPartyQualifier());
        assertNotNull(message.getCurrencyDetails());
        assertNotNull(message.getMessageBeginning());
        assertNotNull(message.getMessageCreationInformation());
        assertNotNull(message.getMessageReference());
        assertNotNull(message.getSupplierPartyQualifier());


        List<ControlInfomation> controlInfomationList = new ArrayList<ControlInfomation>();
        controlInfomationList.add(controlInfomation);

        summary.setControlInfomation(controlInfomationList);
        summary.setSummarySection(summarySection);
        summary.setUntSummary(untSummary);
        assertNotNull(summary.getControlInfomation());
        assertNotNull(summary.getSummarySection());
        assertNotNull(summary.getUntSummary());

        MesssageTypeInformation messageTypeInformation = new MesssageTypeInformation();
        messageTypeInformation.setConstant1("const1");
        messageTypeInformation.setConstant2("const2");
        messageTypeInformation.setConstant3("const3");
        messageTypeInformation.setConstant4("const4");
        messageTypeInformation.setMessageTypeId("12");
        assertNotNull(messageTypeInformation.getConstant1());
        assertNotNull(messageTypeInformation.getConstant2());
        assertNotNull(messageTypeInformation.getConstant3());
        assertNotNull(messageTypeInformation.getConstant4());
        assertNotNull(messageTypeInformation.getMessageTypeId());


        messageHeader.setInterchangeControlReference("interchangeControlReference");
        messageHeader.setMesssageTypeInformation(messageTypeInformation);
        assertNotNull(messageHeader.getInterchangeControlReference());
        assertNotNull(messageHeader.getMesssageTypeInformation());


        lineItemOrder.setBuyerReferenceInformation(buyerReferenceInformationList);
        lineItemOrder.setFundNumberReference(fundNumberReferenceList);
        lineItemOrder.setItemDescriptionList(itemDescriptionList);
        lineItemOrder.setNote(freeTextNotesList);
        lineItemOrder.setPriceInformation(priceInformationList);
        lineItemOrder.setProductFunction(productFunctionList);
        lineItemOrder.setQuantityInformation(quantityInformationList);
        lineItemOrder.setSupplierReferenceInformation(supplierReferenceInformationList);
        lineItemOrder.setTransportStageQualifier(transportStageQualifierList);
        lineItemOrder.setLineItem(lineItemList);
        lineItemOrder.addNote(freeTextNotes);
        assertNotNull(lineItemOrder.getBuyerReferenceInformation());
        assertNotNull(lineItemOrder.getFundNumberReference());
        assertNotNull(lineItemOrder.getItemDescriptionList());
        assertNotNull(lineItemOrder.getLineItem());
        assertNotNull(lineItemOrder.getNote());
        assertNotNull(lineItemOrder.getPriceInformation());
        assertNotNull(lineItemOrder.getQuantityInformation());
        assertNotNull(lineItemOrder.getProductFunction());
        assertNotNull(lineItemOrder.getQuantityInformation());
        assertNotNull(lineItemOrder.getSupplierReferenceInformation());
        assertNotNull(lineItemOrder.getTransportStageQualifier());
        assertNotNull(lineItemOrder.getLineItem());

        List<LineItemOrder> lineItemOrderList = new ArrayList<LineItemOrder>();
        lineItemOrderList.add(lineItemOrder);
        ediOrder.setLineItemOrder(lineItemOrderList);
        ediOrder.setMessage(message);
        ediOrder.setMessageHeader(messageHeader);
        ediOrder.setSenderAndReceiver(senderAndReceiver);
        ediOrder.setSeperators("seperators");
        ediOrder.setSummary(summary);
        ediOrder.setTrailer(trailer);
        assertNotNull(ediOrder.getLineItemOrder());
        assertNotNull(ediOrder.getMessage());
        assertNotNull(ediOrder.getMessageHeader());
        assertNotNull(ediOrder.getSenderAndReceiver());
        assertNotNull(ediOrder.getSeperators());
        assertNotNull(ediOrder.getSummary());
        assertNotNull(ediOrder.getTrailer());


    }

    /**
     * This method is used to generate SenderAndReceiverConstant POJO.
     */
    @Test
    public void testGenerateSenderAndReceiverConstantPojo() {
        SendersAndReceiversConstant sendersAndReceiversConstant = SendersAndReceiverFixture.SendersAndReceiversConstant.
                createSenderAndReceiverConstant(SendersAndReceiversConstant.class);
        assertNotNull(sendersAndReceiversConstant);
    }

    /**
     * This method is used to generate SenderInformation POJO.
     */
    @Test
    public void testGenerateSenderInformationPojo() {
        SenderInformation senderInformation = SendersAndReceiverFixture.SenderInformation.
                createSenderInformation(SenderInformation.class);
        assertNotNull(senderInformation);
    }

    /**
     * This method is used to generate ReceiverInformation POJO.
     */
    @Test
    public void testGenerateReceiverInformationPojo() {
        ReceiverInformation receiverInformation = SendersAndReceiverFixture.ReceiverInformation.
                createReceiverInformation(ReceiverInformation.class);
        assertNotNull(receiverInformation);
    }

    /**
     * This method is used to generate Schedule POJO.
     */
    @Test
    public void testGenerateSchedulePojo() {
        Schedule schedule = SendersAndReceiverFixture.Schedule.
                createSchedule(Schedule.class);
        assertNotNull(schedule);
    }

    /**
     * This method is used to generate SenderAndReceiver POJO.
     */
    @Test
    public void testGenerateSenderAndReceiverPojo() {
        EDIOrderFixture ediOrderFixture = new EDIOrderFixture();
        SenderAndReceiver senderAndReceiver = ediOrderFixture.createSenderAndReceiver(SenderAndReceiver.class);
        assertNotNull(senderAndReceiver);
    }

    /**
     * This method is used to generate MessageTypeInformation POJO.
     */
    @Test
    public void testGenerateMessageTypeInformationPojo() {
        MesssageTypeInformation messsageTypeInformation = MessageHeaderFixture.MesssageTypeInformation.
                createMesssageTypeInformation(MesssageTypeInformation.class);
        assertNotNull(messsageTypeInformation);
    }

    /**
     * This method is used to generate MessageHeader POJO.
     */
    @Test
    public void testGenerateMessageHeaderPojo() {
        EDIOrderFixture ediOrderFixture = new EDIOrderFixture();
        MessageHeader messageHeader = ediOrderFixture.createMessageHeader(MessageHeader.class);
        assertNotNull(messageHeader);
    }

    /**
     * This method is used to generate MessageBeginning POJO.
     */
    @Test
    public void testGenerateMessageBeginningPojo() {
        MessageBeginning messageBeginning = MessageFixture.MessageBeginning.
                createMessageBeginningPojo(MessageBeginning.class);
        assertNotNull(messageBeginning);
    }

    /**
     * This method is used to generate MessageCreationInformation POJO.
     */
    @Test
    public void testGenerateMessageCreationInformationPojo() {
        MessageCreationInformation messageCreationInformation = MessageFixture.MessageCreationInformation.
                createMessageCreationInformationPojo(MessageCreationInformation.class);
        assertNotNull(messageCreationInformation);
    }

    /**
     * This method is used to generate BuyerPartyQualifier POJO.
     */
    @Test
    public void testGenerateBuyerPartyQualifierPojo() {
        BuyerPartyQualifier buyerPartyQualifier = MessageFixture.BuyerPartyQualifier.
                createBuyerPartyQualifierPojo(BuyerPartyQualifier.class);
        assertNotNull(buyerPartyQualifier);
    }

    /**
     * This method is used to generate MessageReference POJO.
     */
    @Test
    public void testGenerateMessageReferencePojo() {
        MessageReference messageReference = MessageFixture.MessageReference.
                createMessageReferencePojo(MessageReference.class);
        assertNotNull(messageReference);
    }

    /**
     * This method is used to generate SupplierPartyQualifier POJO.
     */
    @Test
    public void testGenerateSupplierPartyQualifierPojo() {
        SupplierPartyQualifier supplierPartyQualifier = MessageFixture.SupplierPartyQualifier.
                createSupplierPartyQualifierPojo(SupplierPartyQualifier.class);
        assertNotNull(supplierPartyQualifier);
    }

    /**
     * This method is used to generate CurrencyDetails POJO.
     */
    @Test
    public void testGenerateCurrencyDetailsPojo() {
        CurrencyDetails currencyDetails = MessageFixture.CurrencyDetails.
                createCurrencyDetailsPojo(CurrencyDetails.class);
        assertNotNull(currencyDetails);
    }

    /**
     * This method is used to generate Message POJO.
     */
    @Test
    public void testGenerateMessagePojo() {
        EDIOrderFixture ediOrderFixture = new EDIOrderFixture();
        Message message = ediOrderFixture.createMessage(Message.class);
        assertNotNull(message);
    }


    @Test
    public void testGeneratePojosToXMLFromOrders() throws Exception {
        EDIOrder ediOrder = new EDIOrder();
        EDIOrderFixture ediOrderFixture = new EDIOrderFixture();
        SenderAndReceiver senderAndReceiver = ediOrderFixture.createSenderAndReceiver(SenderAndReceiver.class);
        MessageHeader messageHeader = ediOrderFixture.createMessageHeader(MessageHeader.class);
        Message message = ediOrderFixture.createMessage(Message.class);
        LineItemOrder lineItemOrder = ediOrderFixture.createLineItemOrder(LineItemOrder.class);
        Summary summary = ediOrderFixture.createSummaryOrder(Summary.class);

        ediOrder.setSenderAndReceiver(senderAndReceiver);
        ediOrder.setMessageHeader(messageHeader);
        ediOrder.setMessage(message);
        ediOrder.addLineItemOrder(lineItemOrder);
        ediOrder.setSummary(summary);

        EDIOrder ediOrder1 = new EDIOrder();

        SenderAndReceiver senderAndReceiver1 = new SenderAndReceiver();

        MessageHeader messageHeader1 = new MessageHeader();
        Message message1 = new Message();
        LineItemOrder lineItemOrder1 = new LineItemOrder();
        Summary summary1 = new Summary();

        ediOrder1.setSenderAndReceiver(senderAndReceiver1);
        ediOrder1.setMessageHeader(messageHeader1);
        ediOrder1.setMessage(message1);
        ediOrder1.addLineItemOrder(lineItemOrder1);
        ediOrder1.setSummary(summary1);

        List<EDIOrder> ediOrderList = new ArrayList<EDIOrder>();
        ediOrderList.add(ediOrder);

        ediOrderList.add(ediOrder1);

        EDIOrders ediOrders = new EDIOrders();
        ediOrders.setOrders(ediOrderList);


        OLETranscationalRecordGenerator oleTranscationalRecordGenerator = new OLETranscationalRecordGenerator();
        assertNotNull(ediOrder);
        String ediXml = oleTranscationalRecordGenerator.toXml(ediOrders);
        LOG.info(ediXml);
        LOG.info(ediXml);
    }


    @Test
    public void testGenerateXMLFromOrders() throws Exception {
        URL resource = getClass().getResource("EDIOrders.xml");
        String xmlContent = new FileUtil().readFile(new File(resource.toURI()));
        OLETranscationalRecordGenerator oleTranscationalRecordGenerator = new OLETranscationalRecordGenerator();

        EDIOrders eDIOrders = oleTranscationalRecordGenerator.fromXml(xmlContent);
        assertNotNull(eDIOrders);

        for (int i = 0; i < eDIOrders.getOrders().size(); i++) {
            LOG.info(eDIOrders.getOrders().get(i).toString());
        }


    }

}
