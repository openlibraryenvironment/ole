package org.kuali.ole;

import org.junit.Test;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.pojo.edi.EDIOrder;
import org.kuali.ole.pojo.edi.MessageHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.io.File;
import java.net.URL;
import java.util.*;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/9/12
 * Time: 7:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleOrderRecordHandler_UT {
    public static final Logger LOG = LoggerFactory.getLogger(OleOrderRecordHandler_UT.class);

    @Test
    public void testExtractOleOrderRecordsFromXML() throws Exception {
        OleOrderRecordHandler orderRecordHandlerNew =
                new OleOrderRecordHandler();

        URL resource = getClass().getResource("OleOrderRecord.xml");
        String xmlContent = new FileUtil().readFile(new File(resource.toURI()));

        OleOrderRecords oleOrderRecords = orderRecordHandlerNew.fromXML(xmlContent);
        assertNotNull(oleOrderRecords);
        assertTrue(!oleOrderRecords.getRecords().isEmpty());
        for (int i = 0; i < oleOrderRecords.getRecords().size(); i++) {
            LOG.info(oleOrderRecords.getRecords().get(i).toString());
        }

    }

    @Test
    public void testGenerateXML() throws Exception {
        OleOrderRecordHandler oleOrderRecordHandler = new OleOrderRecordHandler();


        OleOrderRecord oleOrderRecord = new OleOrderRecord();
        OleBibRecord oleBibRecord = new OleBibRecord();
        oleBibRecord.setLinkedInstanceId("123213");
        HashMap bibAssociatedFieldsValueMap = new HashMap();
        bibAssociatedFieldsValueMap.put("author", "mock_author");
        bibAssociatedFieldsValueMap.put("title", "mock_title");
        oleBibRecord.setBibAssociatedFieldsValueMap(bibAssociatedFieldsValueMap);
        oleBibRecord.setBibUUID("45643543");
        oleOrderRecord.setOleBibRecord(oleBibRecord);

        //
        if (oleOrderRecord.getOleBibRecord() != null) {
            LOG.info(oleOrderRecord.getOleBibRecord().toString());
        }
        OleTxRecord oleTxRecord = new OleTxRecord();
        oleTxRecord.setCostSource(null);
        if (oleTxRecord.getCostSource() != null) {
            LOG.info(oleTxRecord.getCostSource());
        }
        oleTxRecord.setFreeTextNote(null);
        if (oleTxRecord.getFreeTextNote() != null) {
            LOG.info(oleTxRecord.getFreeTextNote());
        }
        oleTxRecord.setYear(null);
        if (oleTxRecord.getYear() != null) {
            LOG.info(oleTxRecord.getYear());
        }
        oleTxRecord.setAmount(null);
        if (oleTxRecord.getAmount() != null) {
            LOG.info(oleTxRecord.getAmount());
        }
        oleTxRecord.setChartCode(null);
        if (oleTxRecord.getChartCode() != null) {
            LOG.info(oleTxRecord.getChartCode());
        }
        oleTxRecord.setOrgCode(null);
        if (oleTxRecord.getOrgCode() != null) {
            LOG.info(oleTxRecord.getOrgCode());
        }
        oleTxRecord.setItemChartCode(null);
        if (oleTxRecord.getItemChartCode() != null) {
            LOG.info(oleTxRecord.getItemChartCode());
        }
        oleTxRecord.setContractManager(null);
        if (oleTxRecord.getContractManager() != null) {
            LOG.info(oleTxRecord.getContractManager());
        }
        oleTxRecord.setAssignToUser(null);
        if (oleTxRecord.getAssignToUser() != null) {
            LOG.info(oleTxRecord.getAssignToUser());
        }
        oleTxRecord.setUseTaxIndicator(true);
        if (oleTxRecord.isUseTaxIndicator()) {
            LOG.info("Tax Indicator used");
        }
        oleTxRecord.setOrderType(null);
        if (oleTxRecord.getOrderType() != null) {
            LOG.info(oleTxRecord.getOrderType());
        }
        oleTxRecord.setFundingSource(null);
        if (oleTxRecord.getFundingSource() != null) {
            LOG.info(oleTxRecord.getFundingSource());
        }
        oleTxRecord.setPreviousPurchaseOrder(null);
        if (oleTxRecord.getPreviousPurchaseOrder() != null) {
            LOG.info(oleTxRecord.getPreviousPurchaseOrder());
        }
        oleTxRecord.setRequisitionSource(null);
        if (oleTxRecord.getRequisitionSource() != null) {
            LOG.info(oleTxRecord.getRequisitionSource());
        }
        oleTxRecord.setAdditionalInfo(null);
        if (oleTxRecord.getAdditionalInfo() != null) {
            LOG.info(oleTxRecord.getAdditionalInfo());
        }
        oleTxRecord.setDeliveryCampusCode(null);
        if (oleTxRecord.getDeliveryCampusCode() != null) {
            LOG.info(oleTxRecord.getDeliveryCampusCode());
        }
        oleTxRecord.setBuildingCode(null);
        if (oleTxRecord.getBuildingCode() != null) {
            LOG.info(oleTxRecord.getBuildingCode());
        }
        oleTxRecord.setDeliveryTo(null);
        if (oleTxRecord.getDeliveryTo() != null) {
            LOG.info(oleTxRecord.getDeliveryTo());
        }
        oleTxRecord.setDeliveryAddress1(null);
        if (oleTxRecord.getDeliveryAddress1() != null) {
            LOG.info(oleTxRecord.getDeliveryAddress1());
        }
        oleTxRecord.setDeliveryAddress2(null);
        if (oleTxRecord.getDeliveryAddress2() != null) {
            LOG.info(oleTxRecord.getDeliveryAddress2());
        }
        oleTxRecord.setDeliveryBuildingRoomNumber(null);
        if (oleTxRecord.getDeliveryBuildingRoomNumber() != null) {
            LOG.info(oleTxRecord.getDeliveryBuildingRoomNumber());
        }
        oleTxRecord.setDeliveryBuildingLine1Address(null);
        if (oleTxRecord.getDeliveryBuildingLine1Address() != null) {
            LOG.info(oleTxRecord.getDeliveryBuildingLine1Address());
        }
        oleTxRecord.setDeliveryCityName(null);
        if (oleTxRecord.getDeliveryCityName() != null) {
            LOG.info(oleTxRecord.getDeliveryCityName());
        }
        oleTxRecord.setDeliveryStateCode(null);
        if (oleTxRecord.getDeliveryStateCode() != null) {
            LOG.info(oleTxRecord.getDeliveryStateCode());
        }
        oleTxRecord.setDeliveryPostalCode(null);
        if (oleTxRecord.getDeliveryPostalCode() != null) {
            LOG.info(oleTxRecord.getDeliveryPostalCode());
        }
        oleTxRecord.setDeliveryCountryCode(null);
        if (oleTxRecord.getDeliveryCountryCode() != null) {
            LOG.info(oleTxRecord.getDeliveryCountryCode());
        }
        oleTxRecord.setDeliveryDateRequired(null);
        if (oleTxRecord.getDeliveryDateRequired() != null) {
            LOG.info(oleTxRecord.getDeliveryDateRequired().toString());
        }
        oleTxRecord.setDeliveryDateRequiredReason(null);
        if (oleTxRecord.getDeliveryDateRequiredReason() != null) {
            LOG.info(oleTxRecord.getDeliveryDateRequiredReason());
        }
        oleTxRecord.setDeliveryInstruction(null);
        if (oleTxRecord.getDeliveryInstruction() != null) {
            LOG.info(oleTxRecord.getDeliveryInstruction());
        }
        oleTxRecord.setReceivingAddress(null);
        if (oleTxRecord.getReceivingAddress() != null) {
            LOG.info(oleTxRecord.getReceivingAddress());
        }
        oleTxRecord.setReceivingAddressToVendorIndicator(null);
        if (oleTxRecord.getReceivingAddressToVendorIndicator() != null) {
            LOG.info(oleTxRecord.getReceivingAddressToVendorIndicator());
        }
        oleTxRecord.setVendor(null);
        if (oleTxRecord.getVendor() != null) {
            LOG.info(oleTxRecord.getVendor());
        }
        oleTxRecord.setVendorNumber(null);
        if (oleTxRecord.getVendorNumber() != null) {
            LOG.info(oleTxRecord.getVendorNumber());
        }
        oleTxRecord.setVendorAddress1(null);
        if (oleTxRecord.getVendorAddress1() != null) {
            LOG.info(oleTxRecord.getVendorAddress1());
        }
        oleTxRecord.setVendorAddress2(null);
        if (oleTxRecord.getVendorAddress2() != null) {
            LOG.info(oleTxRecord.getVendorAddress2());
        }

        oleTxRecord.setAttention(null);
        if (oleTxRecord.getAttention() != null) {
            LOG.info(oleTxRecord.getAttention());
        }
        oleTxRecord.setVendorCity(null);
        if (oleTxRecord.getVendorCity() != null) {
            LOG.info(oleTxRecord.getVendorCity());
        }
        oleTxRecord.setVendorState(null);
        if (oleTxRecord.getVendorState() != null) {
            LOG.info(oleTxRecord.getVendorState());
        }
        oleTxRecord.setProvince(null);
        if (oleTxRecord.getProvince() != null) {
            LOG.info(oleTxRecord.getProvince());
        }
        oleTxRecord.setVendorPostalCode(null);
        if (oleTxRecord.getVendorPostalCode() != null) {
            LOG.info(oleTxRecord.getVendorPostalCode());
        }
        oleTxRecord.setVendorCountry(null);
        if (oleTxRecord.getVendorCountry() != null) {
            LOG.info(oleTxRecord.getVendorCountry());
        }
        oleTxRecord.setVendorChoice(null);
        if (oleTxRecord.getVendorChoice() != null) {
            LOG.info(oleTxRecord.getVendorChoice());
        }
        oleTxRecord.setVendorInfoCustomer(null);
        if (oleTxRecord.getVendorInfoCustomer() != null) {
            LOG.info(oleTxRecord.getVendorInfoCustomer());
        }
        oleTxRecord.setVendorNotes(null);
        if (oleTxRecord.getVendorNotes() != null) {
            LOG.info(oleTxRecord.getVendorNotes());
        }
        oleTxRecord.setAlternativeNonPrimaryVendorPayment(null);
        if (oleTxRecord.getAlternativeNonPrimaryVendorPayment() != null) {
            LOG.info(oleTxRecord.getAlternativeNonPrimaryVendorPayment());
        }
        oleTxRecord.setContract(null);
        if (oleTxRecord.getContract() != null) {
            LOG.info(oleTxRecord.getContract());
        }
        oleTxRecord.setVendorPhoneNumber(null);
        if (oleTxRecord.getVendorPhoneNumber() != null) {
            LOG.info(oleTxRecord.getVendorPhoneNumber());
        }
        oleTxRecord.setVendorFaxNumber(null);
        if (oleTxRecord.getVendorFaxNumber() != null) {
            LOG.info(oleTxRecord.getVendorFaxNumber());
        }
        oleTxRecord.setVendorPaymentTerms(null);
        if (oleTxRecord.getVendorPaymentTerms() != null) {
            LOG.info(oleTxRecord.getVendorPaymentTerms());
        }
        oleTxRecord.setVendorShippingTitle(null);
        if (oleTxRecord.getVendorShippingTitle() != null) {
            LOG.info(oleTxRecord.getVendorShippingTitle());
        }
        oleTxRecord.setVendorShippingPaymentTerms(null);
        if (oleTxRecord.getVendorShippingPaymentTerms() != null) {
            LOG.info(oleTxRecord.getVendorShippingPaymentTerms());
        }
        oleTxRecord.setVendorContacts(null);
        if (oleTxRecord.getVendorContacts() != null) {
            LOG.info(oleTxRecord.getVendorContacts());
        }
        oleTxRecord.setVendorSupplierDiversity(null);
        if (oleTxRecord.getVendorSupplierDiversity() != null) {
            LOG.info(oleTxRecord.getVendorSupplierDiversity());
        }
        oleTxRecord.setNote(null);
        if (oleTxRecord.getNote() != null) {
            LOG.info(oleTxRecord.getNote());
        }
        oleTxRecord.setItemType(null);
        if (oleTxRecord.getItemType() != null) {
            LOG.info(oleTxRecord.getItemType());
        }
        oleTxRecord.setQuantity(null);
        if (oleTxRecord.getQuantity() != null) {
            LOG.info(oleTxRecord.getQuantity());
        }
        oleTxRecord.setItemDescription(null);
        if (oleTxRecord.getItemDescription() != null) {
            LOG.info(oleTxRecord.getItemDescription());
        }
        oleTxRecord.setListPrice(null);
        if (oleTxRecord.getListPrice() != null) {
            LOG.info(oleTxRecord.getListPrice());
        }
        oleTxRecord.setDiscount(null);
        if (oleTxRecord.getDiscount() != null) {
            LOG.info(oleTxRecord.getDiscount());
        }
        oleTxRecord.setDiscountType(null);
        if (oleTxRecord.getDiscountType() != null) {
            LOG.info(oleTxRecord.getDiscountType());
        }
        oleTxRecord.setCurrencyType(null);
        if (oleTxRecord.getCurrencyType() != null) {
            LOG.info(oleTxRecord.getCurrencyType());
        }
        oleTxRecord.setVendorItemIdentifier(null);
        if (oleTxRecord.getVendorItemIdentifier() != null) {
            LOG.info(oleTxRecord.getVendorItemIdentifier());
        }
        oleTxRecord.setAccountNumber(null);
        if (oleTxRecord.getAccountNumber() != null) {
            LOG.info(oleTxRecord.getAccountNumber());
        }
        oleTxRecord.setObjectCode(null);
        if (oleTxRecord.getObjectCode() != null) {
            LOG.info(oleTxRecord.getObjectCode());
        }
        oleTxRecord.setOrgRefId(null);
        if (oleTxRecord.getOrgRefId() != null) {
            LOG.info(oleTxRecord.getOrgRefId());
        }
        oleTxRecord.setPercent(null);
        if (oleTxRecord.getPercent() != null) {
            LOG.info(oleTxRecord.getPercent());
        }
        oleTxRecord.setFormat(null);
        if (oleTxRecord.getFormat() != null) {
            LOG.info(oleTxRecord.getFormat());
        }
        oleTxRecord.setInternalPurchasingLimit(null);
        if (oleTxRecord.getInternalPurchasingLimit() != null) {
            LOG.info(oleTxRecord.getInternalPurchasingLimit());
        }
        oleTxRecord.setBillingName(null);
        if (oleTxRecord.getBillingName() != null) {
            LOG.info(oleTxRecord.getBillingName());
        }
        oleTxRecord.setBillingLine1Address(null);
        if (oleTxRecord.getBillingLine1Address() != null) {
            LOG.info(oleTxRecord.getBillingLine1Address());
        }
        oleTxRecord.setBillingCityName(null);
        if (oleTxRecord.getBillingCityName() != null) {
            LOG.info(oleTxRecord.getBillingCityName());
        }
        oleTxRecord.setBillingStateCode(null);
        if (oleTxRecord.getBillingStateCode() != null) {
            LOG.info(oleTxRecord.getBillingStateCode());
        }
        oleTxRecord.setBillingPostalCode(null);
        if (oleTxRecord.getBillingPostalCode() != null) {
            LOG.info(oleTxRecord.getBillingPostalCode());
        }
        oleTxRecord.setBillingPhoneNumber(null);
        if (oleTxRecord.getBillingPhoneNumber() != null) {
            LOG.info(oleTxRecord.getBillingPhoneNumber());
        }
        oleTxRecord.setBillingCountryCode(null);
        if (oleTxRecord.getBillingCountryCode() != null) {
            LOG.info(oleTxRecord.getBillingCountryCode());
        }
        oleTxRecord.setMethodOfPOTransmission(null);
        if (oleTxRecord.getMethodOfPOTransmission() != null) {
            LOG.info(oleTxRecord.getMethodOfPOTransmission());
        }
        oleTxRecord.setReceivingRequired(true);
        if (oleTxRecord.isReceivingRequired()) {
            LOG.info("Receiving Required");
        }
        oleTxRecord.setPayReqPositiveApprovalReq(true);
        if (oleTxRecord.isPayReqPositiveApprovalReq()) {
            LOG.info("Payment Request Positive Approval Requires");
        }
        oleTxRecord.setPurchaseOrderConfirmationIndicator(true);
        if (oleTxRecord.isPurchaseOrderConfirmationIndicator()) {
            LOG.info("Purchase Order Confirmation");
        }
        oleTxRecord.setRouteToRequestor(true);
        if (oleTxRecord.isRouteToRequestor()) {
            LOG.info("Route to Requestor");
        }
        oleTxRecord.setPublicView(true);
        if (oleTxRecord.isPublicView()) {
            LOG.info("Public View");
        }
        oleTxRecord.setAccountNumber("1234567");
        oleTxRecord.setDeliveryDateRequired(new Date());
        oleOrderRecord.setOleTxRecord(oleTxRecord);
        if (oleOrderRecord.getOleTxRecord() != null) {
            LOG.info(oleOrderRecord.getOleTxRecord().toString());
        }
        BibMarcRecord bibMarcRecord = new BibMarcRecord();
        bibMarcRecord.setLeader("Lincoln");
        oleOrderRecord.setOriginalRecord(bibMarcRecord);
        if (oleOrderRecord.getOriginalRecord() != null) {
            LOG.info(oleOrderRecord.getOriginalRecord().toString());
        }
        EDIOrder eDIOrder = new EDIOrder();
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setInterchangeControlReference("reference");
        eDIOrder.setMessageHeader(messageHeader);
        oleOrderRecord.setOriginalEdi(eDIOrder);
        if (oleOrderRecord.getOriginalEdi() != null) {
            LOG.info(oleOrderRecord.getOriginalEdi().toString());
        }
        List messages = new ArrayList();
        messages.add("isbnCheckFailed");
        oleOrderRecord.addMessageToMap("message", messages);
        if (oleOrderRecord.getMessageMap() != null) {
            for (Map.Entry<String, Object> entry : oleOrderRecord.getMessageMap().entrySet()) {
                LOG.info(entry.getKey());
                LOG.info(entry.getValue().toString());
            }
        }
        oleOrderRecord.setAgendaName("mockAgenda");
        if (oleOrderRecord.getAgendaName() != null) {
            LOG.info(oleOrderRecord.getAgendaName());
        }
        oleOrderRecord.setOriginalEDIFileName("ediFileName.edi");
        if (oleOrderRecord.getOriginalEDIFileName() != null) {
            LOG.info(oleOrderRecord.getOriginalEDIFileName());
        }
        oleOrderRecord.setOleOriginalBibRecordFileName("bib.mrc");
        if (oleOrderRecord.getOleOriginalBibRecordFileName() != null) {
            LOG.info(oleOrderRecord.getOriginalEDIFileName());
        }
        OleOrderRecord oleOrderRecord1 = new OleOrderRecord();
        oleOrderRecord1.setAgendaName("Agenda");
        oleOrderRecord1.setOriginalEDIFileName("edifile");
        oleOrderRecord1.setOleOriginalBibRecordFileName("bibfile");
        oleOrderRecord1.addMessageToMap("messages", messages);
        OleBibRecord oleBibRecord1 = new OleBibRecord();
        oleBibRecord1.setBibUUID("123456789");
        oleBibRecord.setLinkedInstanceId("123213");
        HashMap bibAssociatedFieldsValueMap1 = new HashMap();
        bibAssociatedFieldsValueMap1.put("author1", "mock_author1");
        bibAssociatedFieldsValueMap1.put("title1", "mock_title1");
        oleBibRecord1.setBibAssociatedFieldsValueMap(bibAssociatedFieldsValueMap1);
        OleTxRecord oleTxRecord1 = new OleTxRecord();
        oleTxRecord1.setAccountNumber("56789");
        oleOrderRecord1.setOleBibRecord(oleBibRecord1);
        oleOrderRecord1.setOleTxRecord(oleTxRecord1);
        oleOrderRecord.setDescription("Description");
        oleOrderRecord1.setDescription("Descrip");
        if (oleOrderRecord.getDescription() != null) {
            LOG.info(oleOrderRecord.getDescription());
        }

        BibMarcRecord bibMarcRecord1 = new BibMarcRecord();
        bibMarcRecord1.setLeader("kennedy");
       oleOrderRecord1.setOriginalRecord(bibMarcRecord1);
        EDIOrder eDIOrder1 = new EDIOrder();
        MessageHeader messageHeader1 = new MessageHeader();
        messageHeader1.setInterchangeControlReference("reference");
        eDIOrder1.setMessageHeader(messageHeader1);
        oleOrderRecord1.setOriginalEdi(eDIOrder1);
        LOG.info(String.valueOf(oleOrderRecord1.hashCode()));
        OleOrderRecords record = new OleOrderRecords();
        List<OleOrderRecord> records = new ArrayList<OleOrderRecord>();

        records.add(oleOrderRecord);
        records.add(oleOrderRecord1);
        record.setRecords(records);

        String xml = oleOrderRecordHandler.toXML(record);
        LOG.info(xml);

        //for testing hashcode and equals in OleOrderRecord

        OleOrderRecord oleOrderRecord2 = new OleOrderRecord();
        oleOrderRecord2.setAgendaName("Agenda");
        oleOrderRecord2.setOriginalEDIFileName("edifile");
        oleOrderRecord2.setOleOriginalBibRecordFileName("bibfile");
        oleOrderRecord2.addMessageToMap("messages", messages);
        oleOrderRecord2.setOleBibRecord(oleBibRecord1);
        oleOrderRecord2.setOleTxRecord(oleTxRecord1);
        oleOrderRecord2.setDescription("Descrip");
        oleOrderRecord2.setOriginalRecord(bibMarcRecord1);
        oleOrderRecord2.setOriginalEdi(eDIOrder1);
        if (oleOrderRecord1.equals(oleOrderRecord2)) {
            LOG.info("Objects are equal");
        }

    }
}