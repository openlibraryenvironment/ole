package org.kuali.ole;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.pojo.edi.*;

import java.util.Arrays;


/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/2/12
 * Time: 8:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLETranscationalRecordGenerator {

    public String toXml(EDIOrder ediOrder) {
        EDIOrders ediOrders = new EDIOrders();
        ediOrders.setOrders(Arrays.asList(ediOrder));
        String xml = toXml(ediOrders);
        return xml;
    }

    public String toXml(EDIOrders ediOrders) {
        XStream xStream = intXStream();
        String order = xStream.toXML(ediOrders);
        return order;
    }

    public EDIOrders fromXml(String requestXML) {
        XStream xStream = intXStream();
        return (EDIOrders) xStream.fromXML(requestXML);
    }

    public INVOrders fromInvoiceXml(String requestXML) {
        XStream xStream = intInvoiceXStream();
        return (INVOrders) xStream.fromXML(requestXML);
    }

    public String toInvoiceXml(INVOrders invOrders) {
        XStream xStream = intInvoiceXStream();
        return xStream.toXML(invOrders);
    }

    private XStream intXStream() {
        XStream xStream = new XStream();
        xStream.alias("orders", EDIOrders.class);
        xStream.alias("order", EDIOrder.class);
        xStream.addImplicitCollection(EDIOrders.class, "orders", EDIOrder.class);

        xStream.aliasField("sendersandreceiversidentification", EDIOrder.class, "senderAndReceiver");
        xStream.aliasField("sendersandreceiversconstant", SenderAndReceiver.class, "sendersAndReceiversConstant");
        xStream.aliasField("code", SendersAndReceiversConstant.class, "code");
        xStream.aliasField("value", SendersAndReceiversConstant.class, "value");
        xStream.aliasField("senderinformation", SenderAndReceiver.class, "senderInformation");
        xStream.aliasField("senderid", SenderInformation.class, "senderId");
        xStream.aliasField("senderidtype", SenderInformation.class, "senderIdType");
        xStream.aliasField("receiverinformation", SenderAndReceiver.class, "receiverInformation");
        xStream.aliasField("receiverid", ReceiverInformation.class, "receiverId");
        xStream.aliasField("receiveridtype", ReceiverInformation.class, "receiverIdType");
        xStream.aliasField("schedule", SenderAndReceiver.class, "schedule");
        xStream.aliasField("preparationdate", Schedule.class, "preparationDate");
        xStream.aliasField("preparationtime", Schedule.class, "preparationTime");
        xStream.aliasField("interchangecontrolref", SenderAndReceiver.class, "interChangeControlReference");

        xStream.aliasField("messageheader", EDIOrder.class, "messageHeader");
        xStream.aliasField("interchangecontrolref", MessageHeader.class, "interchangeControlReference");
        xStream.aliasField("messagetypeinformation", MessageHeader.class, "messsageTypeInformation");
        xStream.aliasField("messagetypeid", MesssageTypeInformation.class, "messageTypeId");
        xStream.aliasField("constant1", MesssageTypeInformation.class, "constant1");
        xStream.aliasField("constant2", MesssageTypeInformation.class, "constant2");
        xStream.aliasField("constant3", MesssageTypeInformation.class, "constant3");
        xStream.aliasField("constant4", MesssageTypeInformation.class, "constant4");

        xStream.aliasField("message", EDIOrder.class, "message");
        xStream.aliasField("messagebeginning", Message.class, "messageBeginning");
        xStream.aliasField("order", MessageBeginning.class, "messageBeginningOrder");
        xStream.aliasField("interchangecontrolref", MessageBeginning.class, "messageBeginningInterchangeControlReference");
        xStream.aliasField("codelistagency", MessageBeginning.class, "messageBeginningCodeListAgency");

        xStream.aliasField("messagecreationinfo", Message.class, "messageCreationInformation");
        xStream.aliasField("details", MessageCreationInformation.class, "messageCreationInfoDetails");
        xStream.aliasField("constant", MessageCreationInfoDetails.class, "messageCreationInfoConstant");
        xStream.aliasField("date", MessageCreationInfoDetails.class, "messageCreationInfoDate");
        xStream.aliasField("dateformat", MessageCreationInfoDetails.class, "messageCreationInfoDateFormat");

        xStream.aliasField("buyerpartyqualifier", Message.class, "buyerPartyQualifier");
        xStream.aliasField("buyer", BuyerPartyQualifier.class, "buyer");
        xStream.aliasField("buyerinfo", BuyerPartyQualifier.class, "buyerInformation");
        xStream.aliasField("codedidentification", BuyerInformation.class, "buyerCodeIdentification");
        xStream.aliasField("partyidentificationcode", BuyerInformation.class, "buyerPartyIdentificationCode");
        xStream.aliasField("codelistagency", BuyerInformation.class, "buyerCodeListAgency");

        xStream.aliasField("reference", Message.class, "messageReference");
        xStream.aliasField("partydetails", MessageReference.class, "partyDetails");
        xStream.aliasField("additionalpartyidentification", PartyDetails.class, "additionalPartyIdentification");
        xStream.aliasField("partyname", PartyDetails.class, "partyName");

        xStream.aliasField("supplierpartyqualifier", Message.class, "supplierPartyQualifier");
        xStream.aliasField("supplier", SupplierPartyQualifier.class, "supplier");
        xStream.aliasField("supplierinfo", SupplierPartyQualifier.class, "supplierInformation");
        xStream.aliasField("codedidentification", SupplierInformation.class, "supplierCodeIdentification");
        xStream.aliasField("partyidentificationcode", SupplierInformation.class, "supplierPartyIdentificationCode");
        xStream.aliasField("codelistagency", SupplierInformation.class, "supplierCodeListAgency");

        xStream.aliasField("currencydetails", Message.class, "currencyDetails");
        xStream.aliasField("supplierinfo", CurrencyDetails.class, "currencyDetailsSupplierInformation");
        xStream.aliasField("defaultcurrency", CurrencyDetailsSupplierInformation.class, "defaultCurrency");
        xStream.aliasField("currencytype", CurrencyDetailsSupplierInformation.class, "currencyType");
        xStream.aliasField("ordercurrency", CurrencyDetailsSupplierInformation.class, "orderCurrency");

        xStream.alias("lineitemOrder", LineItemOrder.class);
        xStream.alias("lineitem", LineItem.class);
        xStream.alias("articlenumber", LineItemArticleNumber.class);
        xStream.alias("productfunction", ProductFunction.class);
        xStream.alias("articlenumber", ProductArticleNumber.class);
        xStream.alias("itemdescription", ItemDescription.class);
        xStream.alias("quantityinformation", QuantityInformation.class);
        xStream.alias("quantity", Qunatity.class);
        xStream.alias("priceinformation", PriceInformation.class);
        xStream.alias("price", ItemPrice.class);
        xStream.alias("referenceinformation", BuyerReferenceInformation.class);
        xStream.alias("reference", BuyerLineItemReference.class);
        xStream.alias("suppliersreferenceinformation", SupplierReferenceInformation.class);
        xStream.alias("suppliersreference", SupplierLineItemReference.class);
        xStream.alias("fundnumberreference", FundNumberReference.class);
        xStream.alias("buyers", BuyersFundNumberReference.class);
        xStream.alias("note", FreeTextNotes.class);
        xStream.alias("transportstagequalifier", TransportStageQualifier.class);

        xStream.addImplicitCollection(EDIOrder.class, "lineItemOrder", LineItemOrder.class);
        xStream.addImplicitCollection(LineItemOrder.class, "lineItem", LineItem.class);

        xStream.aliasField("sequencenumber", LineItem.class, "sequenceNumber");
        xStream.aliasField("itemnumberid", LineItem.class, "itemNumberId");

        xStream.addImplicitCollection(LineItem.class, "lineItemArticleNumber", LineItemArticleNumber.class);

        xStream.aliasField("isbn", LineItemArticleNumber.class, "lineItemIsbn");
        xStream.aliasField("itemnumbertype", LineItemArticleNumber.class, "lineItemNumberType");

        xStream.addImplicitCollection(LineItemOrder.class, "productFunction", ProductFunction.class);

        xStream.aliasField("productid", ProductFunction.class, "productId");

        xStream.addImplicitCollection(ProductFunction.class, "productArticleNumber", ProductArticleNumber.class);

        xStream.aliasField("isbn", ProductArticleNumber.class, "productIsbn");
        xStream.aliasField("itemnumbertype", ProductArticleNumber.class, "productItemNumberType");

        xStream.addImplicitCollection(LineItemOrder.class, "itemDescriptionList", ItemDescription.class);

        xStream.aliasField("text", ItemDescription.class, "text");
        xStream.aliasField("itemcharacteristiccode", ItemDescription.class, "itemCharacteristicCode");
        xStream.aliasField("data", ItemDescription.class, "data");

        xStream.addImplicitCollection(LineItemOrder.class, "quantityInformation", QuantityInformation.class);
        xStream.addImplicitCollection(QuantityInformation.class, "qunatity", Qunatity.class);

        xStream.aliasField("constant", Qunatity.class, "quantityConstant");
        xStream.aliasField("quantity", Qunatity.class, "quantity");

        xStream.addImplicitCollection(LineItemOrder.class, "priceInformation", PriceInformation.class);
        xStream.addImplicitCollection(PriceInformation.class, "itemPrice", ItemPrice.class);

        xStream.aliasField("grossprice", ItemPrice.class, "grossPrice");
        xStream.aliasField("price", ItemPrice.class, "price");

        xStream.addImplicitCollection(LineItemOrder.class, "buyerReferenceInformation", BuyerReferenceInformation.class);
        xStream.addImplicitCollection(BuyerReferenceInformation.class, "buyerLineItemReference", BuyerLineItemReference.class);

        xStream.aliasField("buyersorderline", BuyerLineItemReference.class, "buyersOrderLine");
        xStream.aliasField("orderlinenumber", BuyerLineItemReference.class, "orderLineNumber");


        xStream.addImplicitCollection(LineItemOrder.class, "supplierReferenceInformation", SupplierReferenceInformation.class);
        xStream.addImplicitCollection(SupplierReferenceInformation.class, "supplierLineItemReference", SupplierLineItemReference.class);

        xStream.aliasField("suppliersorderline", SupplierLineItemReference.class, "suppliersOrderLine");
        xStream.aliasField("vendorreferencenumber", SupplierLineItemReference.class, "vendorReferenceNumber");


        xStream.addImplicitCollection(LineItemOrder.class, "fundNumberReference", FundNumberReference.class);
        xStream.addImplicitCollection(FundNumberReference.class, "buyersFundNumberReference", BuyersFundNumberReference.class);

        xStream.aliasField("buyersfundnumber", BuyersFundNumberReference.class, "buyersFundNumber");
        xStream.aliasField("budgetnumber", BuyersFundNumberReference.class, "budgetNumber");

        xStream.addImplicitCollection(LineItemOrder.class, "note", FreeTextNotes.class);

        xStream.aliasField("notelineitem", FreeTextNotes.class, "noteLineItem");
        xStream.aliasField("noteempty1", FreeTextNotes.class, "noteempty1");
        xStream.aliasField("noteempty2", FreeTextNotes.class, "noteempty2");
        xStream.aliasField("noteempty3", FreeTextNotes.class, "noteempty3");


        xStream.addImplicitCollection(LineItemOrder.class, "transportStageQualifier", TransportStageQualifier.class);

        xStream.aliasField("transportstagequalifierconstant", TransportStageQualifier.class, "transportStageConstant");
        xStream.aliasField("surfacemail", TransportStageQualifier.class, "surfaceMail");


        xStream.aliasField("summary", LineItemOrder.class, "supplierReferenceInformation");
        xStream.aliasField("summarysection", Summary.class, "summarySection");
        xStream.aliasField("separatorinfo", SummarySection.class, "separatorInformation");

        xStream.alias("controlinfomation", ControlInfomation.class);
        xStream.alias("control", Control.class);

        xStream.addImplicitCollection(Summary.class, "controlInfomation", ControlInfomation.class);
        xStream.addImplicitCollection(ControlInfomation.class, "control", Control.class);

        xStream.aliasField("controlqualifier", Control.class, "controlQualifier");
        xStream.aliasField("totalqtysegments", Control.class, "totalQuantitySegments");


        xStream.aliasField("unt", Summary.class, "untSummary");
        xStream.aliasField("messagesegmentsnumber", UNTSummary.class, "segmentNumber");
        xStream.aliasField("linsegmentstotal", UNTSummary.class, "linSegmentTotal");
        xStream.addImplicitCollection(EDIOrders.class, "orders");
        return xStream;
    }


    private XStream intInvoiceXStream() {
        XStream xStream = new XStream();
        xStream.alias("orders", INVOrders.class);

        xStream.aliasField("sendersandreceiversidentification", INVOrders.class, "senderAndReceiver");
        xStream.aliasField("sendersandreceiversconstant", SenderAndReceiver.class, "sendersAndReceiversConstant");
        xStream.aliasField("code", SendersAndReceiversConstant.class, "code");
        xStream.aliasField("value", SendersAndReceiversConstant.class, "value");
        xStream.aliasField("senderinformation", SenderAndReceiver.class, "senderInformation");
        xStream.aliasField("senderid", SenderInformation.class, "senderId");
        xStream.aliasField("senderidtype", SenderInformation.class, "senderIdType");
        xStream.aliasField("receiverinformation", SenderAndReceiver.class, "receiverInformation");
        xStream.aliasField("receiverid", ReceiverInformation.class, "receiverId");
        xStream.aliasField("receiveridtype", ReceiverInformation.class, "receiverIdType");
        xStream.aliasField("schedule", SenderAndReceiver.class, "schedule");
        xStream.aliasField("preparationdate", Schedule.class, "preparationDate");
        xStream.aliasField("preparationtime", Schedule.class, "preparationTime");
        xStream.aliasField("interchangecontrolref", SenderAndReceiver.class, "interChangeControlReference");
        xStream.aliasField("field6", SenderAndReceiver.class, "field6");
        xStream.aliasField("field7", SenderAndReceiver.class, "field7");
        xStream.aliasField("field8", SenderAndReceiver.class, "field8");
        xStream.aliasField("field9", SenderAndReceiver.class, "field9");
        xStream.aliasField("field10", SenderAndReceiver.class, "field10");
        xStream.aliasField("field11", SenderAndReceiver.class, "field11");

        xStream.alias("order", INVOrder.class);
        xStream.addImplicitCollection(INVOrders.class, "invOrder", INVOrder.class);


        xStream.aliasField("messageheader", INVOrder.class, "messageHeader");
        xStream.aliasField("interchangecontrolref", MessageHeader.class, "interchangeControlReference");
        xStream.aliasField("messagetypeinformation", MessageHeader.class, "messsageTypeInformation");
        xStream.aliasField("messagetypeid", MesssageTypeInformation.class, "messageTypeId");
        xStream.aliasField("constant1", MesssageTypeInformation.class, "constant1");
        xStream.aliasField("constant2", MesssageTypeInformation.class, "constant2");
        xStream.aliasField("constant3", MesssageTypeInformation.class, "constant3");
        xStream.aliasField("constant4", MesssageTypeInformation.class, "constant4");

        xStream.aliasField("message", INVOrder.class, "message");
        xStream.aliasField("messagebeginning", InvoiceMessage.class, "messageBeginning");
        xStream.aliasField("order", MessageBeginning.class, "messageBeginningOrder");
        xStream.aliasField("interchangecontrolref", MessageBeginning.class, "messageBeginningInterchangeControlReference");
        xStream.aliasField("codelistagency", MessageBeginning.class, "messageBeginningCodeListAgency");

        xStream.aliasField("messagecreationinfo", InvoiceMessage.class, "messageCreationInformation");
        xStream.aliasField("details", MessageCreationInformation.class, "messageCreationInfoDetails");
        xStream.aliasField("constant", MessageCreationInfoDetails.class, "messageCreationInfoConstant");
        xStream.aliasField("date", MessageCreationInfoDetails.class, "messageCreationInfoDate");
        xStream.aliasField("dateformat", MessageCreationInfoDetails.class, "messageCreationInfoDateFormat");

        xStream.aliasField("purchaseordernumber", InvoiceMessage.class, "purchaseOrderQualifier");
        xStream.aliasField("purchaseorderreference", PurchaseOrderQualifier.class, "purchaseOrderReference");
        xStream.aliasField("purchaseorderline", PurchaseOrderReference.class, "purchaseOrderLine");
        xStream.aliasField("purchaseordernumber", PurchaseOrderReference.class, "purchaseOrderNumber");

        xStream.alias("partyqualifier", PartyQualifier.class);
        xStream.alias("supplierpartyname", SupplierPartyName.class);
        xStream.addImplicitCollection(InvoiceMessage.class, "partyQualifier", PartyQualifier.class);

        xStream.aliasField("partyqualifier", InvoiceMessage.class, "partyQualifier");
        xStream.aliasField("partycode", PartyQualifier.class, "partyCode");
        xStream.aliasField("partyinfo", PartyQualifier.class, "partyInformation");
        xStream.aliasField("codedidentification", PartyInformation.class, "codeIdentification");
        xStream.aliasField("partyidentificationcode", PartyInformation.class, "partyIdentificationCode");
        xStream.aliasField("codelistagency", PartyInformation.class, "codeListAgency");
        xStream.aliasField("nameandaddress", PartyQualifier.class, "nameAndAddress");

        xStream.aliasField("supplierpartyname", PartyQualifier.class, "supplierPartyName");

        xStream.aliasField("partyname1", SupplierPartyName.class, "partyName1");
        xStream.aliasField("partyname2", SupplierPartyName.class, "partyName2");
        xStream.aliasField("partyname3", SupplierPartyName.class, "partyName3");
        xStream.aliasField("partyname4", SupplierPartyName.class, "partyName4");
        xStream.aliasField("partyname5", SupplierPartyName.class, "partyName5");


        xStream.aliasField("partystreetname", PartyQualifier.class, "partyStreetName");

        xStream.aliasField("streetname1", PartyStreetName.class, "partyStreetName1");
        xStream.aliasField("streetname2", PartyStreetName.class, "partyStreetName2");
        xStream.aliasField("streetname3", PartyStreetName.class, "partyStreetName3");

        xStream.aliasField("partycityname", PartyQualifier.class, "partyCityName");
        xStream.aliasField("partycountrysubentity", PartyQualifier.class, "partyCountrySubEntity");
        xStream.aliasField("partypostalcode", PartyQualifier.class, "partyPostalCode");
        xStream.aliasField("partycountrycode", PartyQualifier.class, "partyCountryCode");

        xStream.aliasField("supplieradditionalpartyidentifier", InvoiceMessage.class, "supplierAdditionalPartyIdentifier");
        xStream.aliasField("supplierreference", SupplierAdditionalPartyIdentifier.class, "supplierIdentifier");
        xStream.aliasField("referencequalifier", SupplierIdentifier.class, "referenceQualifier");
        xStream.aliasField("referencenumber", SupplierIdentifier.class, "referenceNumber");


        xStream.alias("buyerpartyqualifier", BuyerQualifier.class);
        xStream.alias("buyername", BuyerPartyName.class);
        xStream.addImplicitCollection(InvoiceMessage.class, "buyerPartyQualifier", BuyerQualifier.class);

        xStream.aliasField("buyerpartyqualifier", InvoiceMessage.class, "buyerPartyQualifier");
        xStream.aliasField("buyercode", BuyerQualifier.class, "buyerCode");
        xStream.aliasField("buyerinfo", BuyerQualifier.class, "buyerInformation");
        xStream.aliasField("buyercodedidentification", PartyInformation.class, "codeIdentification");
        xStream.aliasField("buyeridentificationcode", PartyInformation.class, "partyIdentificationCode");
        xStream.aliasField("buyercodelistagency", PartyInformation.class, "codeListAgency");
        xStream.aliasField("nameandaddress", BuyerQualifier.class, "nameAndAddress");

        xStream.aliasField("buyername", BuyerQualifier.class, "buyerName");
        xStream.aliasField("buyername1", BuyerPartyName.class, "partyName1");
        xStream.aliasField("buyername2", BuyerPartyName.class, "partyName2");
        xStream.aliasField("buyername3", BuyerPartyName.class, "partyName3");
        xStream.aliasField("buyername4", BuyerPartyName.class, "partyName4");
        xStream.aliasField("buyername5", BuyerPartyName.class, "partyName5");

        xStream.aliasField("buyerstreetname", BuyerQualifier.class, "buyerStreetName");
        xStream.aliasField("buyerstreetname1", PartyStreetName.class, "partyStreetName1");
        xStream.aliasField("buyerstreetname2", PartyStreetName.class, "partyStreetName2");
        xStream.aliasField("buyerstreetname3", PartyStreetName.class, "partyStreetName3");

        xStream.aliasField("buyercityname", BuyerQualifier.class, "buyerCityName");
        xStream.aliasField("buyercountrysubentity", BuyerQualifier.class, "buyerCountrySubEntity");
        xStream.aliasField("buyerpostalcode", BuyerQualifier.class, "buyerPostalCode");
        xStream.aliasField("buyercountrycode", BuyerQualifier.class, "buyerCountryCode");


        xStream.aliasField("buyeradditionalpartyidentifier", InvoiceMessage.class, "buyerAdditionalPartyIdentifier");
        xStream.aliasField("buyerreference", BuyerAdditionalPartyIdentifier.class, "buyerIdentifier");
        xStream.aliasField("buyerreferencequalifier", BuyerIdentifier.class, "buyerReferenceQualifier");
        xStream.aliasField("buyerreferencenumber", BuyerIdentifier.class, "buyerReferenceNumber");


        xStream.aliasField("reference", InvoiceMessage.class, "messageReference");
        xStream.aliasField("partydetails", MessageReference.class, "partyDetails");
        xStream.aliasField("additionalpartyidentification", PartyDetails.class, "additionalPartyIdentification");
        xStream.aliasField("partyname", PartyDetails.class, "partyName");

        xStream.aliasField("currencydetails", InvoiceMessage.class, "currencyDetails");
        xStream.aliasField("supplierinfo", CurrencyDetails.class, "currencyDetailsSupplierInformation");
        xStream.aliasField("defaultcurrency", CurrencyDetailsSupplierInformation.class, "defaultCurrency");
        xStream.aliasField("currencytype", CurrencyDetailsSupplierInformation.class, "currencyType");
        xStream.aliasField("ordercurrency", CurrencyDetailsSupplierInformation.class, "orderCurrency");

        xStream.aliasField("allowanceorcharge", InvoiceMessage.class, "allowanceOrCharge");
        xStream.aliasField("allowanceorchargequalifier", AllowanceOrCharge.class, "allowanceOrChargeQualifier");
        xStream.aliasField("freetext", AllowanceOrCharge.class, "freeText");
        xStream.aliasField("settlement", AllowanceOrCharge.class, "settlement");
        xStream.aliasField("calculationsequenceindicator", AllowanceOrCharge.class, "calculationSequenceIndicator");
        xStream.aliasField("specialserviceidentification", AllowanceOrCharge.class, "specialServiceIdentification");
        xStream.aliasField("specialservicecode", SpecialServiceIdentification.class, "specialServiceCode");
        xStream.aliasField("codelistqualifier", SpecialServiceIdentification.class, "codeListQualifier");
        xStream.aliasField("codelistqualifieragency", SpecialServiceIdentification.class, "codeListQualifierAgency");
        xStream.aliasField("specialservice", SpecialServiceIdentification.class, "specialService");

        xStream.aliasField("monetary", InvoiceMessage.class, "monetary");
        xStream.aliasField("monetaryinfo", Monetary.class, "monetaryInformation");
        xStream.aliasField("monetaryamounttype", MonetaryInformation.class, "amountType");
        xStream.aliasField("monetaryamount", MonetaryInformation.class, "amount");


        xStream.alias("lineitemOrder", LineItemOrder.class);
        xStream.alias("lineitem", LineItem.class);
        xStream.alias("linearticlenumber", LineItemArticleNumber.class);
        xStream.alias("productfunction", ProductFunction.class);
        xStream.alias("articlenumber", ProductArticleNumber.class);
        xStream.alias("supplierarticlenumber", SupplierArticleNumber.class);
        xStream.alias("itemdescription", ItemDescription.class);
        xStream.alias("quantityinformation", QuantityInformation.class);
        xStream.alias("quantity", Qunatity.class);
        xStream.alias("datetimedetail", DateTimeDetail.class);
        xStream.alias("datetimeinfo", DateTimeInformation.class);
        xStream.alias("priceinformation", PriceInformation.class);
        xStream.alias("price", ItemPrice.class);
        xStream.alias("suppliersreferenceinformation", SupplierReferenceInformation.class);
        xStream.alias("suppliersreference", SupplierLineItemReference.class);

        xStream.alias("note", FreeTextNotes.class);
        xStream.alias("monetary", MonetaryDetail.class);
        xStream.alias("monetaryinfo", MonetaryLineItemInformation.class);

        xStream.alias("lineitemallowanceorcharge", LineItemAllowanceOrCharge.class);
        xStream.alias("lineitemspecialserviceidentification", LineItemSpecialServiceIdentification.class);

        xStream.alias("allowancemonetaryadditionalcharge", AllowanceMonetaryDetail.class);
        xStream.alias("allowancemonetaryinfo", AllowanceMonetaryLineItemInformation.class);

        xStream.addImplicitCollection(INVOrder.class, "lineItemOrder", LineItemOrder.class);
        xStream.addImplicitCollection(LineItemOrder.class, "lineItem", LineItem.class);

        xStream.aliasField("sequencenumber", LineItem.class, "sequenceNumber");
        xStream.aliasField("itemnumberid", LineItem.class, "itemNumberId");

        xStream.addImplicitCollection(LineItem.class, "lineItemArticleNumber", LineItemArticleNumber.class);

        xStream.aliasField("isbn", LineItemArticleNumber.class, "lineItemIsbn");
        xStream.aliasField("itemnumbertype", LineItemArticleNumber.class, "lineItemNumberType");

        xStream.addImplicitCollection(LineItemOrder.class, "productFunction", ProductFunction.class);

        xStream.aliasField("productid", ProductFunction.class, "productId");

        xStream.addImplicitCollection(ProductFunction.class, "productArticleNumber", ProductArticleNumber.class);

        xStream.aliasField("isbn", ProductArticleNumber.class, "productIsbn");
        xStream.aliasField("itemnumbertype", ProductArticleNumber.class, "productItemNumberType");
        xStream.aliasField("unknown1", ProductArticleNumber.class, "unknown1");
        xStream.aliasField("unknown2", ProductArticleNumber.class, "unknown2");
        xStream.aliasField("unknown3", ProductArticleNumber.class, "unknown3");

        xStream.addImplicitCollection(ProductFunction.class, "supplierArticleNumber", SupplierArticleNumber.class);

        xStream.aliasField("isbn", SupplierArticleNumber.class, "isbn");
        xStream.aliasField("supplierarticle", SupplierArticleNumber.class, "supplierArticle");

        xStream.addImplicitCollection(LineItemOrder.class, "itemDescriptionList", ItemDescription.class);

        xStream.aliasField("text", ItemDescription.class, "text");
        xStream.aliasField("itemcharacteristiccode", ItemDescription.class, "itemCharacteristicCode");
        xStream.aliasField("data", ItemDescription.class, "data");

        xStream.addImplicitCollection(LineItemOrder.class, "quantityInformation", QuantityInformation.class);
        xStream.addImplicitCollection(QuantityInformation.class, "qunatity", Qunatity.class);

        xStream.aliasField("constant", Qunatity.class, "quantityConstant");
        xStream.aliasField("quantity", Qunatity.class, "quantity");

        xStream.addImplicitCollection(LineItemOrder.class, "dateTimeDetail", DateTimeDetail.class);
        xStream.addImplicitCollection(DateTimeDetail.class, "dateTimeInformationList", DateTimeInformation.class);

        xStream.aliasField("periodqualifier", DateTimeInformation.class, "periodQualifier");
        xStream.aliasField("period", DateTimeInformation.class, "period");
        xStream.aliasField("periodformat", DateTimeInformation.class, "periodFormat");

        xStream.addImplicitCollection(LineItemOrder.class, "note", FreeTextNotes.class);

        xStream.aliasField("notelineitem", FreeTextNotes.class, "noteLineItem");
        xStream.aliasField("noteempty1", FreeTextNotes.class, "noteempty1");
        xStream.aliasField("noteempty2", FreeTextNotes.class, "noteempty2");
        xStream.aliasField("noteempty3", FreeTextNotes.class, "noteempty3");

        xStream.addImplicitCollection(LineItemOrder.class, "monetaryDetail", MonetaryDetail.class);
        xStream.addImplicitCollection(MonetaryDetail.class, "monetaryLineItemInformation", MonetaryLineItemInformation.class);

        xStream.aliasField("monetaryamounttype", MonetaryLineItemInformation.class, "amountType");
        xStream.aliasField("monetaryamount", MonetaryLineItemInformation.class, "amount");





        xStream.addImplicitCollection(LineItemOrder.class, "lineItemAllowanceOrCharge", LineItemAllowanceOrCharge.class);
        xStream.addImplicitCollection(LineItemAllowanceOrCharge.class, "lineItemSpecialServiceIdentification", LineItemSpecialServiceIdentification.class);

        xStream.aliasField("allowanceorchargequalifier", LineItemAllowanceOrCharge.class, "allowanceOrChargeQualifier");
        xStream.aliasField("freetext", LineItemAllowanceOrCharge.class, "freeText");
        xStream.aliasField("settlement", LineItemAllowanceOrCharge.class, "settlement");
        xStream.aliasField("calculationsequenceindicator", LineItemAllowanceOrCharge.class, "calculationSequenceIndicator");

        xStream.aliasField("specialservicecode", LineItemSpecialServiceIdentification.class, "specialServiceCode");
        xStream.aliasField("codelistqualifier", LineItemSpecialServiceIdentification.class, "codeListQualifier");
        xStream.aliasField("codelistqualifieragency", LineItemSpecialServiceIdentification.class, "codeListQualifierAgency");
        xStream.aliasField("specialservice", LineItemSpecialServiceIdentification.class, "specialService");




        xStream.addImplicitCollection(LineItemOrder.class, "allowanceMonetaryDetail", AllowanceMonetaryDetail.class);
        xStream.addImplicitCollection(AllowanceMonetaryDetail.class, "allowanceMonetaryLineItemInformation", AllowanceMonetaryLineItemInformation.class);

        xStream.aliasField("monetaryamounttype", AllowanceMonetaryLineItemInformation.class, "amountType");
        xStream.aliasField("monetaryamount", AllowanceMonetaryLineItemInformation.class, "amount");

        xStream.addImplicitCollection(LineItemOrder.class, "priceInformation", PriceInformation.class);
        xStream.addImplicitCollection(PriceInformation.class, "itemPrice", ItemPrice.class);

        xStream.aliasField("grossprice", ItemPrice.class, "grossPrice");
        xStream.aliasField("price", ItemPrice.class, "price");

        xStream.addImplicitCollection(LineItemOrder.class, "supplierReferenceInformation", SupplierReferenceInformation.class);
        xStream.addImplicitCollection(SupplierReferenceInformation.class, "supplierLineItemReference", SupplierLineItemReference.class);

        xStream.aliasField("suppliersorderline", SupplierLineItemReference.class, "suppliersOrderLine");
        xStream.aliasField("vendorreferencenumber", SupplierLineItemReference.class, "vendorReferenceNumber");

        xStream.aliasField("summary", LineItemOrder.class, "supplierReferenceInformation");
        xStream.aliasField("summarysection", Summary.class, "summarySection");
        xStream.aliasField("separatorinfo", SummarySection.class, "separatorInformation");

        xStream.alias("controlinfomation", ControlInfomation.class);
        xStream.alias("control", Control.class);

        xStream.alias("monetarysummary", MonetarySummary.class);
        xStream.alias("monetaryinfosummary", MonetarySummaryInformation.class);

        xStream.addImplicitCollection(Summary.class, "controlInfomation", ControlInfomation.class);
        xStream.addImplicitCollection(ControlInfomation.class, "control", Control.class);

        xStream.aliasField("controlqualifier", Control.class, "controlQualifier");
        xStream.aliasField("totalqtysegments", Control.class, "totalQuantitySegments");

        xStream.addImplicitCollection(Summary.class, "monetarySummary", MonetarySummary.class);
        xStream.addImplicitCollection(MonetarySummary.class, "monetarySummaryInformation", MonetarySummaryInformation.class);

        xStream.aliasField("monetaryamounttype", MonetarySummaryInformation.class, "amountType");
        xStream.aliasField("monetaryamount", MonetarySummaryInformation.class, "amount");


        xStream.aliasField("unt", Summary.class, "untSummary");
        xStream.aliasField("messagesegmentsnumber", UNTSummary.class, "segmentNumber");
        xStream.aliasField("linsegmentstotal", UNTSummary.class, "linSegmentTotal");
        xStream.addImplicitCollection(INVOrders.class, "invOrder");
        return xStream;
    }

}