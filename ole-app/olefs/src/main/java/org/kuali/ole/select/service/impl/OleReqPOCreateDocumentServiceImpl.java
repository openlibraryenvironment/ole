/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleOrderRecords;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OrderImportHelperBo;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.ids.HoldingsId;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.service.RequisitionService;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.OleSelectNotificationConstant;
import org.kuali.ole.select.batch.service.RequisitionCreateDocumentService;
import org.kuali.ole.select.batch.service.impl.RequisitionCreateDocumentServiceImpl;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.document.service.OleRequestSourceService;
import org.kuali.ole.select.document.service.OleRequestorService;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.select.service.BibInfoService;
import org.kuali.ole.select.service.OleReqPOCreateDocumentService;
import org.kuali.ole.sys.OLEConstants.FinancialDocumentTypeCodes;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class OleReqPOCreateDocumentServiceImpl extends RequisitionCreateDocumentServiceImpl implements OleReqPOCreateDocumentService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleReqPOCreateDocumentServiceImpl.class);

    protected RequisitionCreateDocumentService requisitionCreateDocumentService;
    protected OleRequestorService oleRequestorService;
    protected VendorService vendorService;
    protected OleRequestSourceService oleRequestSourceService;
    protected OlePurapService olePurapService;
    protected BusinessObjectService businessObjectService;
    protected DocumentService documentService;
    protected BibInfoService bibInfoService;
    protected ConfigurationService kualiConfigurationService;
    private DocstoreClientLocator docstoreClientLocator;
    private OlePatronDocumentList olePatronDocumentList;
    private OleSelectDocumentService oleSelectDocumentService;
    private boolean currencyTypeIndicator = true;

    public OleSelectDocumentService getOleSelectDocumentService() {
        if (oleSelectDocumentService == null) {
            oleSelectDocumentService = SpringContext.getBean(OleSelectDocumentService.class);
        }
        return oleSelectDocumentService;
    }

    public void setOleSelectDocumentService(OleSelectDocumentService oleSelectDocumentService) {
        this.oleSelectDocumentService = oleSelectDocumentService;
    }

    public OlePatronDocumentList getOlePatronDocumentList() {
        if (olePatronDocumentList == null) {
            olePatronDocumentList = SpringContext.getBean(OlePatronDocumentList.class);
        }
        return olePatronDocumentList;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    /**
     * Set the values for the Requisition Document and save.
     *
     * @param oleOrderRecords OleOrderRecords
     */
    public void saveRequisitionDocument(OleOrderRecords oleOrderRecords, OLEBatchProcessJobDetailsBo job) throws Exception {
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        String isValidBFN = "";
        vendorService = getVendorService();
        oleRequestorService = getOleRequestorService();
        requisitionCreateDocumentService = getRequisitionCreateDocumentService();
        oleRequestSourceService = getOleRequestSourceService();
        olePurapService = getOlePurapService();
        setUserFromStaffUpload(oleOrderRecords);
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = orderImportHelperBo.getOleBatchProcessProfileBo();
        String requisitionsForTitle = null;
        if (oleBatchProcessProfileBo != null) {
            requisitionsForTitle = oleBatchProcessProfileBo.getRequisitionsforTitle();
        }
        if (oleBatchProcessProfileBo != null && requisitionsForTitle.equalsIgnoreCase(OLEConstants.ONE_REQUISITION_PER_TITLE)) {
            orderImportHelperBo.setReqList(new ArrayList(0));
            List<OleOrderRecord> oleOrderRecordList = oleOrderRecords.getRecords();
            for (int recCount = 0; recCount < oleOrderRecordList.size(); recCount++) {
                OleOrderRecord oleOrderRecord = oleOrderRecordList.get(recCount);
                oleOrderRecord.getMessageMap().put(OLEConstants.IS_APO_RULE, false);
                String isValidRecord = ((oleOrderRecord.getMessageMap().get(OLEConstants.IS_VALID_RECORD))).toString();
                if (OLEConstants.TRUE.equalsIgnoreCase(isValidRecord)) {
                    isValidBFN = ((oleOrderRecord.getMessageMap().get(OLEConstants.IS_VALID_BFN))).toString();
                    if (isValidBFN.equalsIgnoreCase(OLEConstants.TRUE)) {
                        if (oleOrderRecord.getOleTxRecord() != null && oleOrderRecord.getOleBibRecord() != null) {
                            OleRequisitionDocument requisitionDocument = null;
                            try {
                                requisitionDocument = createRequisitionDocument();
                                requisitionDocument.setRequisitionSourceCode(oleOrderRecord.getOleTxRecord().getRequisitionSource());
                                if (oleOrderRecordList != null && oleOrderRecordList.size() > 0) {
                                    if (oleOrderRecord.getOleTxRecord().getOrderType() != null) {
                                        Map purchaseOrderTypeMap = new HashMap();
                                        purchaseOrderTypeMap.put(OLEConstants.PO_TYPE, oleOrderRecord.getOleTxRecord().getOrderType());
                                        List<PurchaseOrderType> purchaseOrderTypeDocumentList = (List) getBusinessObjectService().findMatching(PurchaseOrderType.class, purchaseOrderTypeMap);
                                        if (purchaseOrderTypeDocumentList != null && purchaseOrderTypeDocumentList.size() > 0) {
                                            requisitionDocument.setPurchaseOrderTypeId(purchaseOrderTypeDocumentList.get(0).getPurchaseOrderTypeId());
                                        }
                                    } else {
                                        requisitionDocument.setPurchaseOrderTypeId(OLEConstants.DEFAULT_ORDER_TYPE_VALUE);
                                    }
                                    setDocumentValues(requisitionDocument, oleOrderRecord, job, recCount);
                                }
                                requisitionDocument.setItems(generateItemList(oleOrderRecord, job, requisitionDocument));
                                RequisitionService requisitionService = SpringContext.getBean(RequisitionService.class);
                                boolean apoRuleFlag = requisitionService.isAutomaticPurchaseOrderAllowed(requisitionDocument);
                                if (!apoRuleFlag) {
                                    oleOrderRecord.getMessageMap().put(OLEConstants.IS_APO_RULE, true);
                                }
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("before calling saveRequisitionDocuments");
                                }
                                requisitionDocument.setApplicationDocumentStatus(PurapConstants.RequisitionStatuses.APPDOC_IN_PROCESS);
                                requisitionCreateDocumentService.saveRequisitionDocuments(requisitionDocument);
                                orderImportHelperBo.setOrderImportSuccessCount(orderImportHelperBo.getOrderImportSuccessCount() + 1);
                                if (!oleBatchProcessProfileBo.getMarcOnly()) {
                                    orderImportHelperBo.setCreateBibCount(orderImportHelperBo.getOrderImportSuccessCount());
                                }
                                orderImportHelperBo.getReqList().add(requisitionDocument.getPurapDocumentIdentifier());
                            } catch (Exception ex) {
                                LOG.error("####Rollback####" + ex);
                                orderImportHelperBo.setOrderImportFailureCount(orderImportHelperBo.getOrderImportFailureCount() + 1);
                                oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_RECORD, false);
                                GlobalVariables.setMessageMap(new MessageMap());
                            }
                        }
                    }
                }
            }
        } else if (oleBatchProcessProfileBo != null && requisitionsForTitle.equalsIgnoreCase(OLEConstants.ONE_REQUISITION_WITH_ALL_TITLES)) {
            orderImportHelperBo.setReqList(new ArrayList(0));
            List<OleOrderRecord> oleOrderRecordList = oleOrderRecords.getRecords();
            OleRequisitionDocument requisitionDocument = null;
            List<OleOrderRecord> validRecords = new ArrayList<OleOrderRecord>();
            for (OleOrderRecord oleOrderRecord : oleOrderRecordList) {
                oleOrderRecord.getMessageMap().put(OLEConstants.IS_APO_RULE, false);
                String isValidRecord = ((oleOrderRecord.getMessageMap().get(OLEConstants.IS_VALID_RECORD))).toString();
                if (OLEConstants.TRUE.equalsIgnoreCase(isValidRecord)) {
                    isValidBFN = ((oleOrderRecord.getMessageMap().get(OLEConstants.IS_VALID_BFN))).toString();
                    if (isValidBFN.equalsIgnoreCase(OLEConstants.TRUE)) {
                        if (oleOrderRecord.getOleTxRecord() != null && oleOrderRecord.getOleBibRecord() != null) {
                            validRecords.add(oleOrderRecord);
                        }
                    }
                }
            }
            try {
                if (oleOrderRecordList.size() == validRecords.size()) {
                    requisitionDocument = createRequisitionDocument();
                    requisitionDocument.setRequisitionSourceCode(oleOrderRecordList.get(0).getOleTxRecord().getRequisitionSource());
                    if (oleOrderRecordList != null && oleOrderRecordList.size() > 0) {
                        if (oleOrderRecordList.get(0).getOleTxRecord().getOrderType() != null) {
                            Map purchaseOrderTypeMap = new HashMap();
                            purchaseOrderTypeMap.put(OLEConstants.PO_TYPE, oleOrderRecordList.get(0).getOleTxRecord().getOrderType());
                            org.kuali.rice.krad.service.BusinessObjectService
                                    businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
                            List<PurchaseOrderType> purchaseOrderTypeDocumentList = (List) businessObjectService.findMatching(PurchaseOrderType.class, purchaseOrderTypeMap);
                            if (purchaseOrderTypeDocumentList != null && purchaseOrderTypeDocumentList.size() > 0) {
                                requisitionDocument.setPurchaseOrderTypeId(purchaseOrderTypeDocumentList.get(0).getPurchaseOrderTypeId());
                            }
                        } else {
                            requisitionDocument.setPurchaseOrderTypeId(OLEConstants.DEFAULT_ORDER_TYPE_VALUE);
                        }
                        setDocumentValues(requisitionDocument, oleOrderRecordList.get(0), job, 0);
                    }
                    requisitionDocument.setItems(generateMultipleItemsForOneRequisition(oleOrderRecordList, job, requisitionDocument));
                    requisitionDocument.getDocumentHeader().getWorkflowDocument();
                    requisitionDocument.setApplicationDocumentStatus(PurapConstants.RequisitionStatuses.APPDOC_IN_PROCESS);
                    getRequisitionCreateDocumentService().saveRequisitionDocuments(requisitionDocument);
                    //dataCarrierService.addData(OLEConstants.ORDER_IMPORT_SUCCESS_COUNT,(int) dataCarrierService.getData(OLEConstants.ORDER_IMPORT_SUCCESS_COUNT)+validRecords.size());
                    orderImportHelperBo.setOrderImportSuccessCount(orderImportHelperBo.getOrderImportSuccessCount() + validRecords.size());
                    orderImportHelperBo.getReqList().add(requisitionDocument.getPurapDocumentIdentifier());
                } else {
                    if (validRecords != null && validRecords.size() > 0) {
                        List<String> uuidList = new ArrayList<>();
                        for (int recCount = 0; recCount < validRecords.size(); recCount++) {
                            uuidList.add(validRecords.get(recCount).getOleBibRecord().getBibUUID());
                        }
                        //dataCarrierService.addData(OLEConstants.ORDER_IMPORT_FAILURE_COUNT,(int) dataCarrierService.getData(OLEConstants.ORDER_IMPORT_FAILURE_COUNT)+uuidList.size());
                        orderImportHelperBo.setOrderImportFailureCount(orderImportHelperBo.getOrderImportFailureCount() + uuidList.size());
                        /*getDocstoreClientLocator().getDocstoreClient().deleteBibs(uuidList);*/
                    }
                }
            } catch (Exception ex) {
                LOG.error("####Rollback####" + ex);
                //dataCarrierService.addData(OLEConstants.ORDER_IMPORT_FAILURE_COUNT,(int) dataCarrierService.getData(OLEConstants.ORDER_IMPORT_FAILURE_COUNT)+uuidList.size());
                List<String> uuidList = new ArrayList<>();
                for (int recCount = 0; recCount < oleOrderRecordList.size(); recCount++) {
                    uuidList.add(oleOrderRecordList.get(recCount).getOleBibRecord().getBibUUID());
                }
                orderImportHelperBo.setOrderImportFailureCount(orderImportHelperBo.getOrderImportFailureCount() + uuidList.size());
                /*getDocstoreClientLocator().getDocstoreClient().deleteBibs(uuidList);*/
            }
        }
    }


    /**
     * To create the Requisition document object
     *
     * @return OleRequisitionDocument
     */
    public OleRequisitionDocument createRequisitionDocument() throws WorkflowException {
        String user;
        if (GlobalVariables.getUserSession() == null) {
            user = getConfigurationService().getPropertyValueAsString(getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR));
            if (LOG.isDebugEnabled()) {
                LOG.debug("createRequisitionDocument - user from session" + user);
            }
            GlobalVariables.setUserSession(new UserSession(user));
        }

        return (OleRequisitionDocument) SpringContext.getBean(DocumentService.class).getNewDocument(FinancialDocumentTypeCodes.REQUISITION);
    }

    private void setUserFromStaffUpload(OleOrderRecords oleOrderRecords) {
        List<OleOrderRecord> oleOrderRecordList = oleOrderRecords.getRecords();
        String user = null;
        if (oleOrderRecordList.size() > 0) {
            user = GlobalVariables.getUserSession().getPrincipalName();
            if (user == null) {
                user = getConfigurationService().getPropertyValueAsString(
                        getOleSelectDocumentService().getSelectParameterValue(OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR));
            }
            GlobalVariables.setUserSession(new UserSession(user));
        }
    }

    /**
     * To create the requisition document
     *
     * @param requisitionDocument OleRequisitionDocument
     * @param oleOrderRecord      OleOrderRecord
     * @return RequisitionDocument
     */
    protected RequisitionDocument setDocumentValues(OleRequisitionDocument requisitionDocument, OleOrderRecord oleOrderRecord, OLEBatchProcessJobDetailsBo job, int recPosition) throws Exception {
        // ******************Document Overview Section******************
        requisitionDocument.setStatusCode(PurapConstants.RequisitionStatuses.APPDOC_IN_PROCESS);
        /**
         * Commented vendorPoNumber based on JIRA-2842
         */
        //requisitionDocument.setVendorPoNumber(oleOrderRecord.getOleTxRecord().getVendorNumber());
        requisitionDocument.setVendorPoNumber(oleOrderRecord.getOleTxRecord().getVendorItemIdentifier());
        // ******************Financial Document Detail Section******************
        // ******************Requisition Detail Section******************
        requisitionDocument.setChartOfAccountsCode(oleOrderRecord.getOleTxRecord().getChartCode());
        requisitionDocument.setOrganizationCode(oleOrderRecord.getOleTxRecord().getOrgCode());
        requisitionDocument.setDocumentFundingSourceCode(oleOrderRecord.getOleTxRecord().getFundingSource());
        requisitionDocument.setUseTaxIndicator(true);//oleOrderRecord.getOleTxRecord().getUseTaxIndicator()
        // ******************Delivery Section******************
        setDeliveryDetails(requisitionDocument, oleOrderRecord);
        requisitionDocument.setDeliveryCampusCode(oleOrderRecord.getOleTxRecord().getDeliveryCampusCode());
        // ******************Vendor Section******************
        setVendorDetails(requisitionDocument, oleOrderRecord);
        // ******************Items Section******************
        // ******************Capital Assets Section******************
        // ******************Payment INfo Section******************
        setRecurringPaymentInfo(requisitionDocument, oleOrderRecord);
        // ******************Additional Institutional Info Section******************
        requisitionDocument.getDocumentHeader().setDocumentDescription(getDocumentDescription(requisitionDocument, oleOrderRecord, job, recPosition));
        requisitionDocument.setPurchaseOrderTransmissionMethodCode(getTransmissionMethodCode(oleOrderRecord.getOleTxRecord().getMethodOfPOTransmission()));//FAX
        requisitionDocument.setPurchaseOrderCostSourceCode(oleOrderRecord.getOleTxRecord().getCostSource());//CON
        requisitionDocument.setRequestorPersonName(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_NAME));
        requisitionDocument.setRequestorPersonPhoneNumber(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_PHONE_NUMBER));
        requisitionDocument.setRequestorPersonEmailAddress(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_EMAIL_ADDRESS));
        requisitionDocument.setOrganizationAutomaticPurchaseOrderLimit(new KualiDecimal(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.VENDOR_CONTRACT_DEFAULT_APO_LIMIT)));
        requisitionDocument.setPurchaseOrderAutomaticIndicator(Boolean.parseBoolean(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.PURCHASE_ORDER_AUTOMATIC_INDICATIOR)));
        requisitionDocument.setReceivingDocumentRequiredIndicator(oleOrderRecord.getOleTxRecord().isReceivingRequired());
        requisitionDocument.setPaymentRequestPositiveApprovalIndicator(oleOrderRecord.getOleTxRecord().isPayReqPositiveApprovalReq());
        requisitionDocument.setRequisitionSourceCode(oleOrderRecord.getOleTxRecord().getRequisitionSource());
        return requisitionDocument;

    }

    private void setItemNotes(OleRequisitionItem item, OleTxRecord oleTxRecord) {
        if (StringUtils.isNotBlank(oleTxRecord.getMiscellaneousNote())) {
            setNoteTypeValues(item, oleTxRecord.getMiscellaneousNote(), oleTxRecord.getMiscellaneousNotes());
        }
        if (StringUtils.isNotBlank(oleTxRecord.getReceiptNote())) {
            setNoteTypeValues(item, oleTxRecord.getReceiptNote(), oleTxRecord.getReceiptNotes());
        }
        if (StringUtils.isNotBlank(oleTxRecord.getRequestorNote())) {
            setNoteTypeValues(item, oleTxRecord.getRequestorNote(), oleTxRecord.getRequestorNotes());
        }
        if (StringUtils.isNotBlank(oleTxRecord.getSelectorNote())) {
            setNoteTypeValues(item, oleTxRecord.getSelectorNote(), oleTxRecord.getSelectorNotes());
        }
        if (StringUtils.isNotBlank(oleTxRecord.getSplProcessInstrNote())) {
            setNoteTypeValues(item, oleTxRecord.getSplProcessInstrNote(), oleTxRecord.getSplProcessInstrNotes());
        }
        if (StringUtils.isNotBlank(oleTxRecord.getVendorInstrNote())) {
            setNoteTypeValues(item, oleTxRecord.getVendorInstrNote(), oleTxRecord.getVendorInstrNotes());
        }
    }

    public String getTransmissionMethodCode(String transmissionMethodDescription){
        Map<String, String> transmissionCodeMap = new HashMap<>();
        transmissionCodeMap.put(OLEConstants.OLEBatchProcess.PO_TRANSMISSION_METHOD_DESC, transmissionMethodDescription);
        List<PurchaseOrderTransmissionMethod> transmissionMethodList = (List<PurchaseOrderTransmissionMethod>) getBusinessObjectService().findMatching(PurchaseOrderTransmissionMethod.class, transmissionCodeMap);
        if (transmissionMethodList != null && transmissionMethodList.size() > 0) {
            return transmissionMethodList.get(0).getPurchaseOrderTransmissionMethodCode();
        }
        return null;
    }

    private void setNoteTypeValues(OleRequisitionItem item, String noteType, List<String> noteValues) {
        Map notes = new HashMap();
        notes.put(OLEConstants.NOTE_TYP, noteType);
        List<OleNoteType> noteTypeList = (List) getBusinessObjectService().findMatching(org.kuali.ole.select.businessobject.OleNoteType.class, notes);
        if (noteTypeList != null && noteTypeList.size() > 0) {
            for (int noteCount = 0; noteCount < noteValues.size(); noteCount++) {
                OleRequisitionNotes note = new OleRequisitionNotes();
                note.setNoteTypeId(noteTypeList.get(0).getNoteTypeId());
                note.setNote(noteValues.get(noteCount));
                item.getNotes().add(note);
            }
        }
    }

    /**
     * To set the delivery details for the Requisition Document
     *
     * @param requisitionDocument OleRequisitionDocument
     * @param oleOrderRecord      OleOrderRecord
     * @return requisitionDocument OleRequisitionDocument
     */
    public void setDeliveryDetails(OleRequisitionDocument requisitionDocument, OleOrderRecord oleOrderRecord) {
        if (LOG.isDebugEnabled())
            LOG.debug("bibInfoBean.getDeliveryBuildingCode----------->" + oleOrderRecord.getOleTxRecord().getBuildingCode());

        if (oleOrderRecord.getOleTxRecord().getDeliveryCampusCode() != null && oleOrderRecord.getOleTxRecord().getBuildingCode() != null && oleOrderRecord.getOleTxRecord().getDeliveryBuildingRoomNumber() != null) {
            Map<String, String> deliveryMap = new HashMap<>();
            deliveryMap.put(OLEConstants.OLEBatchProcess.BUILDING_CODE, oleOrderRecord.getOleTxRecord().getBuildingCode());
            deliveryMap.put(OLEConstants.OLEBatchProcess.CAMPUS_CODE, oleOrderRecord.getOleTxRecord().getDeliveryCampusCode());
            deliveryMap.put(OLEConstants.BUILDING_ROOM_NUMBER, oleOrderRecord.getOleTxRecord().getDeliveryBuildingRoomNumber());
            Room room = getBusinessObjectService().findByPrimaryKey(Room.class, deliveryMap);
            Building building = getVendorService().getBuildingDetails(oleOrderRecord.getOleTxRecord().getDeliveryCampusCode(), oleOrderRecord.getOleTxRecord().getBuildingCode());
            if (building != null && room != null) {
                requisitionDocument.setDeliveryBuildingCode(building.getBuildingCode());
                requisitionDocument.setDeliveryCampusCode(building.getCampusCode());
                requisitionDocument.setDeliveryBuildingLine1Address(building.getBuildingStreetAddress());
                requisitionDocument.setDeliveryBuildingName(building.getBuildingName());
                requisitionDocument.setDeliveryCityName(building.getBuildingAddressCityName());
                requisitionDocument.setDeliveryStateCode(building.getBuildingAddressStateCode());
                requisitionDocument.setDeliveryPostalCode(building.getBuildingAddressZipCode());
                requisitionDocument.setDeliveryCountryCode(building.getBuildingAddressCountryCode());
                requisitionDocument.setDeliveryBuildingRoomNumber(room.getBuildingRoomNumber());
                requisitionDocument.setDeliveryToName(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DELIVERY_TO_NAME));


                requisitionDocument.setBillingCountryCode(building.getBuildingCode());
                requisitionDocument.setBillingLine1Address(building.getBuildingStreetAddress());
                requisitionDocument.setBillingName(building.getBuildingName());
                requisitionDocument.setBillingCityName(building.getBuildingAddressCityName());
                requisitionDocument.setBillingStateCode(building.getBuildingAddressStateCode());
                requisitionDocument.setBillingPostalCode(building.getBuildingAddressZipCode());
                requisitionDocument.setBillingCountryCode(building.getBuildingAddressCountryCode());
                requisitionDocument.setBillingPhoneNumber(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.BILL_PHN_NBR));

            }
        }
    }

    private void setRecurringPaymentInfo(OleRequisitionDocument requisitionDocument, OleOrderRecord oleOrderRecord) throws Exception {
        if (oleOrderRecord.getOleTxRecord().getRecurringPaymentType() != null) {
            requisitionDocument.setRecurringPaymentTypeCode(oleOrderRecord.getOleTxRecord().getRecurringPaymentType());
            SimpleDateFormat sdf1 = new SimpleDateFormat(org.kuali.ole.OLEConstants.DATE_FORMAT);
            java.util.Date beginDate = sdf1.parse(oleOrderRecord.getOleTxRecord().getRecurringPaymentBeginDate());
            java.util.Date endDate = sdf1.parse(oleOrderRecord.getOleTxRecord().getRecurringPaymentEndDate());
            requisitionDocument.setPurchaseOrderBeginDate(new java.sql.Date(beginDate.getTime()));
            requisitionDocument.setPurchaseOrderEndDate(new java.sql.Date(endDate.getTime()));
        }
    }

    /**
     * To set the vendor details for the Requisition Document
     *
     * @param requisitionDocument OleRequisitionDocument
     * @return requisitionDocument OleRequisitionDocument
     */
    public void setVendorDetails(OleRequisitionDocument requisitionDocument, OleOrderRecord oleOrderRecord) {
        VendorDetail vendorDetail = null;
        if (StringUtils.isNotBlank(oleOrderRecord.getOleTxRecord().getVendorNumber())) {
            vendorDetail = getVendorService().getVendorDetail(oleOrderRecord.getOleTxRecord().getVendorNumber());
        }else if (StringUtils.isNotBlank(oleOrderRecord.getOleTxRecord().getVendorAliasName())){
            Map<String, String> vendorAliasMap = new HashMap<>();
            vendorAliasMap.put(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME, oleOrderRecord.getOleTxRecord().getVendorAliasName());
            List<VendorAlias> vendorAliasList = (List) KRADServiceLocatorWeb.getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
            if (CollectionUtils.isNotEmpty(vendorAliasList)){
                Map vendorDetailMap = new HashMap();
                vendorDetailMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_HEADER_IDENTIFIER, vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier());
                vendorDetailMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_DETAIL_IDENTIFIER, vendorAliasList.get(0).getVendorDetailAssignedIdentifier());
                vendorDetail = getBusinessObjectService().findByPrimaryKey(VendorDetail.class, vendorDetailMap);
            }
        }
        if (vendorDetail!=null){
            requisitionDocument.setVendorCustomerNumber(oleOrderRecord.getOleTxRecord().getVendorInfoCustomer());
            requisitionDocument.setVendorNumber(oleOrderRecord.getOleTxRecord().getVendorNumber());
            requisitionDocument.setVendorNumber(vendorDetail.getVendorNumber());
            requisitionDocument.setVendorName(vendorDetail.getVendorName());
            requisitionDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            requisitionDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            requisitionDocument.setVendorDetail(vendorDetail);
            String deliveryCampus = oleOrderRecord.getOleTxRecord().getDeliveryCampusCode();
            Integer headerId = null;
            Integer detailId = null;
            int dashInd = vendorDetail.getVendorNumber().indexOf('-');
            // make sure there's at least one char before and after '-'
            if (dashInd > 0 && dashInd < vendorDetail.getVendorNumber().length() - 1) {
                headerId = new Integer(vendorDetail.getVendorNumber().substring(0, dashInd));
                detailId = new Integer(vendorDetail.getVendorNumber().substring(dashInd + 1));
            }
            VendorAddress vendorAddress = getVendorService().getVendorDefaultAddress(headerId, detailId, VendorConstants.AddressTypes.PURCHASE_ORDER, deliveryCampus);
            setVendorAddress(vendorAddress, requisitionDocument);

            List<VendorContract> vendorContracts = vendorDetail.getVendorContracts();
            for (Iterator<VendorContract> vendorContract = vendorContracts.iterator(); vendorContract.hasNext(); ) {
                requisitionDocument.setVendorContractGeneratedIdentifier((vendorContract.next()).getVendorContractGeneratedIdentifier());
            }
        }

    }

    /**
     * To generate single Item list for the Requisition Document
     *
     * @param oleOrderRecord OleOrderRecord
     * @return ArrayList
     */
    private List<RequisitionItem> generateItemList(OleOrderRecord oleOrderRecord, OLEBatchProcessJobDetailsBo job, OleRequisitionDocument requisitionDocument) throws Exception {
        List<RequisitionItem> items = new ArrayList<RequisitionItem>();
        int itemLineNumber = 1;
        items.add(createRequisitionItem(oleOrderRecord, itemLineNumber, job, requisitionDocument));
        return items;
    }
    /*private List<Note> generateBoNotesList(BibInfoBean bibInfoBean) {
        List<Note> notes = new ArrayList<Note>();
        notes.add(createNotes(bibInfoBean));
        return notes;
    }*/

    /**
     * To generate multiple Item list for the Requisition Document
     *
     * @param oleOrderRecordList List<OleOrderRecord>
     * @return ArrayList
     */
    private List<RequisitionItem> generateMultipleItemsForOneRequisition(List<OleOrderRecord> oleOrderRecordList, OLEBatchProcessJobDetailsBo job, OleRequisitionDocument requisitionDocument) throws Exception {
        List<RequisitionItem> items = new ArrayList<RequisitionItem>();
        int itemLineNumber = 0;
        // set items to document
        for (OleOrderRecord oleOrderRecord : oleOrderRecordList) {
            itemLineNumber++;
            items.add(createRequisitionItem(oleOrderRecord, itemLineNumber, job, requisitionDocument));
        }
        return items;
    }

    /**
     * To create the requisition item for the Requisition Document.
     *
     * @param oleOrderRecord OleOrderRecord
     * @return RequisitionItem
     */
    @SuppressWarnings("deprecation")
    protected RequisitionItem createRequisitionItem(OleOrderRecord oleOrderRecord, int itemLineNumber, OLEBatchProcessJobDetailsBo job, OleRequisitionDocument requisitionDocument) throws Exception {
        OleRequisitionItem item = new OleRequisitionItem();
        item.setOleOrderRecord(oleOrderRecord);
        item.setSingleCopyNumber(oleOrderRecord.getOleTxRecord().getSingleCopyNumber());
        item.setItemPriceSourceId(getItemPriceSourceId(oleOrderRecord.getOleTxRecord()));
        //item.setBibInfoBean(bibInfoBean);
        item.setItemLineNumber(itemLineNumber);
        item.setItemUnitOfMeasureCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.UOM));
        item.setItemQuantity(new KualiDecimal(oleOrderRecord.getOleTxRecord().getQuantity()));
        if (oleOrderRecord.getOleTxRecord().getItemNoOfParts() != null) {
            item.setItemNoOfParts(new KualiInteger(oleOrderRecord.getOleTxRecord().getItemNoOfParts()));
        }
        //item.setItemDescription((String) ((List) oleOrderRecord.getOleBibRecord().getBibAssociatedFieldsValueMap().get("ISBN_search")).get(0));
        setItemDescription(oleOrderRecord, item);
        item.setItemUnitPrice(new BigDecimal(oleOrderRecord.getOleTxRecord().getListPrice()));
        item.setItemTypeCode(oleOrderRecord.getOleTxRecord().getItemType());
        item.setItemListPrice(new KualiDecimal(oleOrderRecord.getOleTxRecord().getListPrice()));
        item.setItemLocation(oleOrderRecord.getOleTxRecord().getDefaultLocation());
        if (!StringUtils.isBlank(oleOrderRecord.getOleTxRecord().getFormatTypeId())) {
            item.setFormatTypeId(Integer.parseInt(oleOrderRecord.getOleTxRecord().getFormatTypeId()));
        }
        if (oleOrderRecord.getOleTxRecord().getRequestSourceType() != null) {
            item.setRequestSourceTypeId(getRequestSourceTypeId(oleOrderRecord.getOleTxRecord().getRequestSourceType()));
        }
        if (oleOrderRecord.getOleTxRecord().getOleDonors() != null && oleOrderRecord.getOleTxRecord().getOleDonors().size() > 0) {
            List<OLELinkPurapDonor> oleLinkPurapDonorList = new ArrayList<>();
            for (String donor : oleOrderRecord.getOleTxRecord().getOleDonors()) {
                Map map = new HashMap();
                map.put(OLEConstants.DONOR_CODE, donor);
                OLEDonor oleDonor = getBusinessObjectService().findByPrimaryKey(OLEDonor.class, map);
                OLELinkPurapDonor oleLinkPurapDonor = new OLELinkPurapDonor();
                oleLinkPurapDonor.setDonorCode(donor);
                if (oleDonor != null) {
                    oleLinkPurapDonor.setDonorId(oleDonor.getDonorId());
                }
                oleLinkPurapDonorList.add(oleLinkPurapDonor);
            }
            item.setOleDonors(oleLinkPurapDonorList);
        }
        if (ObjectUtils.isNotNull(oleOrderRecord.getOleBibRecord().getBibUUID())) {
            item.setItemTitleId(oleOrderRecord.getOleBibRecord().getBibUUID());
            //item.setLinkToOrderOption(OLEConstants.NB_PRINT);
        }
        item.setBibTree(oleOrderRecord.getBibTree());
        item.setLinkToOrderOption(oleOrderRecord.getLinkToOrderOption());
        if (item.getLinkToOrderOption() != null) {
            if (item.getLinkToOrderOption().equals(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT_ELECTRONIC) || item.getLinkToOrderOption().equals(OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI_PRINT_ELECTRONIC)) {
                if (oleOrderRecord.getBibTree() != null) {
                    boolean printHolding = false;
                    boolean electronicHolding = false;
                    List<HoldingsId> electronicHoldingsIdList = new ArrayList<>();
                    List<HoldingsId> holdingsIds = oleOrderRecord.getBibTree().getHoldingsIds();
                    if (holdingsIds != null && holdingsIds.size() > 0) {
                        for (HoldingsId holdingsId : holdingsIds) {
                            if (holdingsId != null) {
                                Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsId.getId());
                                if (holdings != null && holdings.getHoldingsType() != null && holdings.getHoldingsType().equals(OLEConstants.OleHoldings.ELECTRONIC)) {
                                    electronicHolding = true;
                                    electronicHoldingsIdList.add(holdingsId);
                                } else if (holdings != null && holdings.getHoldingsType() != null && holdings.getHoldingsType().equals(OLEConstants.PRINT)) {
                                    printHolding = true;
                                }
                            }
                        }
                    }
                    if (!printHolding && electronicHolding) {
                        if (item.getLinkToOrderOption().equals(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT_ELECTRONIC)) {
                            item.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_ELECTRONIC);
                        } else if (item.getLinkToOrderOption().equals(OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI_PRINT_ELECTRONIC)) {
                            item.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI_ELECTRONIC);
                        }
                    } else {
                        if (electronicHoldingsIdList.size() > 0 && oleOrderRecord.getBibTree().getHoldingsIds() != null) {
                            oleOrderRecord.getBibTree().getHoldingsIds().removeAll(electronicHoldingsIdList);
                        }
                        if (item.getLinkToOrderOption().equals(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT_ELECTRONIC)) {
                            item.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT);
                        } else if (item.getLinkToOrderOption().equals(OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI_PRINT_ELECTRONIC)) {
                            item.setLinkToOrderOption(OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI);
                        }
                    }
                }
            }
            if (item.getLinkToOrderOption().equals(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_PRINT) || item.getLinkToOrderOption().equals(OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI)) {
                if (oleOrderRecord.getBibTree() != null) {
                    List<HoldingsId> holdingsIds = oleOrderRecord.getBibTree().getHoldingsIds();
                    if (holdingsIds != null && holdingsIds.size() > 0) {
                        int itemCount = 0;
                        for (HoldingsId holdingsId : holdingsIds) {
                            if (holdingsId != null) {
                                if (holdingsId.getItems() != null && holdingsId.getItems().size() == 0) {
                                    KualiInteger noOfItems = KualiInteger.ZERO;
                                    KualiInteger noOfCopies = new KualiInteger(item.getItemQuantity().intValue());
                                    noOfItems = item.getItemNoOfParts().multiply(noOfCopies);
                                    for (int count = 0; count < noOfItems.intValue(); count++) {
                                        holdingsId.getItems().add(null);
                                    }
                                }
                            }
                            if (holdingsId.getItems() != null && holdingsId.getItems().size() > 0) {
                                itemCount += holdingsId.getItems().size();
                            }
                        }
                        item.setItemQuantity(new KualiDecimal(itemCount));
                        item.setItemNoOfParts(new KualiInteger(1));
                    }
                }
            } else if (item.getLinkToOrderOption().equals(OLEConstants.ORDER_RECORD_IMPORT_MARC_ONLY_ELECTRONIC) || item.getLinkToOrderOption().equals(OLEConstants.ORDER_RECORD_IMPORT_MARC_EDI_ELECTRONIC)) {
                item.setItemQuantity(new KualiDecimal(1));
                item.setItemNoOfParts(new KualiInteger(1));
            }
        }
        //if (item.getItemType() == null) {
        org.kuali.ole.module.purap.businessobject.ItemType itemType = getBusinessObjectService().findBySinglePrimaryKey(org.kuali.ole.module.purap.businessobject.ItemType.class, "ITEM");
        item.setItemType(itemType);
        // }

        setSourceAccountingLinesToReqItem(oleOrderRecord, item);

        /*OleRequestor oleRequestor = checkRequestorExist(oleOrderRecord);
        if(oleRequestor == null) {
            oleRequestor =  new OleRequestor();
            oleRequestor = saveRequestor(oleOrderRecord, oleRequestor);
        }

        item.setOleRequestor(oleRequestor);

        item.setRequestorId(oleRequestor.getRequestorId());
         //item.setRequestSourceTypeId(oleRequestSourceService.getRequestSourceTypeId(bibInfoBean.getRequestSource()));
        item.setRequestorFirstName(oleRequestor.getRequestorFirstName());

        item.setRequestorLastName(oleRequestor.getRequestorLastName());*/
        //item.setRequestSourceUrl(oleOrderRecord.getOleTxRecord().getRequestSourceUrl());

        //getOleRequestorService().saveRequestor(oleRequestor);

        String requestorType = null;

        if (requestorType == null || "".equals(requestorType)) {

            requestorType = OleSelectConstant.REQUESTOR_TYPE_STAFF;
        }

        int requestorTypeId = getRequestorTypeId(requestorType);
        item.setRequestorTypeId(requestorTypeId);
        if (oleOrderRecord.getOleTxRecord().getRequisitionSource().equals(OleSelectConstant.REQUISITON_SRC_TYPE_WEBFORM)) {//&& !oleOrderRecord.getOleTxRecord().getRequestersNotes().trim().equals("")) {
            OleRequisitionNotes note = new OleRequisitionNotes();
            Map notes = new HashMap();
            String noteType = OleSelectConstant.REQUESTOR_NOTES_PRE_ORDER_SERVICE;
            notes.put(OLEConstants.NOTE_TYP, noteType);
            List<OleNoteType> noteTypeList = (List) getBusinessObjectService().findMatching(org.kuali.ole.select.businessobject.OleNoteType.class, notes);
            note.setNoteTypeId(noteTypeList.get(0).getNoteTypeId());
            //note.setNote(oleOrderRecord.getOleTxRecord().getRequestersNotes());
            item.getNotes().add(note);
        }
        setItemDescription(oleOrderRecord, item);
        populateValuesFromProfileAttributesAndDataMapping(item, job, requisitionDocument);
        setForeignCurrencyDetails(item, requisitionDocument);
        setItemNotes(item, oleOrderRecord.getOleTxRecord());
        item.setVendorItemPoNumber(oleOrderRecord.getOleTxRecord().getVendorItemIdentifier());
        return item;
    }

    private void setSourceAccountingLinesToReqItem(OleOrderRecord oleOrderRecord, OleRequisitionItem item) {
        if (oleOrderRecord.getOleTxRecord().getFundCode() != null) {
            Map fundCodeMap = new HashMap<>();
            fundCodeMap.put(OLEConstants.OLEEResourceRecord.FUND_CODE, oleOrderRecord.getOleTxRecord().getFundCode());
            List<OleFundCode> fundCodeList = (List) getBusinessObjectService().findMatching(OleFundCode.class, fundCodeMap);
            if (CollectionUtils.isNotEmpty(fundCodeList)) {
                List<PurApAccountingLine> sourceAccountingLines = item.getSourceAccountingLines();
                if (sourceAccountingLines == null) {
                    sourceAccountingLines = new ArrayList<>();
                }
                OleFundCode oleFundCode = fundCodeList.get(0);
                List<OleFundCodeAccountingLine> fundCodeAccountingLineList = oleFundCode.getOleFundCodeAccountingLineList();
                for (OleFundCodeAccountingLine oleFundCodeAccountingLine : fundCodeAccountingLineList) {
                    RequisitionAccount requisitionAccount = new RequisitionAccount();
                    requisitionAccount.setChartOfAccountsCode(oleFundCodeAccountingLine.getChartCode());
                    requisitionAccount.setAccountNumber(oleFundCodeAccountingLine.getAccountNumber());
                    if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getSubAccount())){
                        requisitionAccount.setSubAccountNumber(oleFundCodeAccountingLine.getSubAccount());
                    }
                    requisitionAccount.setFinancialObjectCode(oleFundCodeAccountingLine.getObjectCode());
                    if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getSubObject())){
                        requisitionAccount.setFinancialSubObjectCode(oleFundCodeAccountingLine.getSubObject());
                    }
                    if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getProject())){
                        requisitionAccount.setProjectCode(oleFundCodeAccountingLine.getProject());
                    }
                    if (StringUtils.isNotBlank(oleFundCodeAccountingLine.getOrgRefId())){
                        requisitionAccount.setOrganizationReferenceId(oleFundCodeAccountingLine.getOrgRefId());
                    }
                    requisitionAccount.setAccountLinePercent(oleFundCodeAccountingLine.getPercentage());
                    requisitionAccount.setDebitCreditCode(OLEConstants.GL_DEBIT_CODE);
                    sourceAccountingLines.add(requisitionAccount);
                }
                item.setSourceAccountingLines(sourceAccountingLines);
            }
        } else {
            RequisitionAccount requisitionAccount = new RequisitionAccount();
            /**
             * Below code commented based on JIRA-2617.
             */
            //requisitionAccount.setChartOfAccountsCode(oleOrderRecord.getOleTxRecord().getChartCode());
            requisitionAccount.setChartOfAccountsCode(oleOrderRecord.getOleTxRecord().getItemChartCode());
            requisitionAccount.setAccountNumber(oleOrderRecord.getOleTxRecord().getAccountNumber());
            requisitionAccount.setFinancialObjectCode(oleOrderRecord.getOleTxRecord().getObjectCode());
            requisitionAccount.setDebitCreditCode(OLEConstants.GL_DEBIT_CODE);
            if (oleOrderRecord.getOleTxRecord().getListPrice() != null) {
                requisitionAccount.setAmount(new KualiDecimal(oleOrderRecord.getOleTxRecord().getListPrice()));
            }
            if (oleOrderRecord.getOleTxRecord().getPercent() != null) {
                requisitionAccount.setAccountLinePercent(new BigDecimal(oleOrderRecord.getOleTxRecord().getPercent()));
            }
            if (oleOrderRecord.getOleTxRecord().getAccountNumber() != null) {
                List<PurApAccountingLine> sourceAccountingLines = item.getSourceAccountingLines();
                if (sourceAccountingLines.size() == 0) {
                    sourceAccountingLines = new ArrayList<>(0);
                }
                sourceAccountingLines.add(requisitionAccount);
                item.setSourceAccountingLines(sourceAccountingLines);
            }
        }
    }

    private Integer getItemPriceSourceId(OleTxRecord oleTxRecord) {
        HashMap<String, Object> map = new HashMap<>();
        if(null != oleTxRecord.getItemPriceSource()){
            map.put("itemPriceSource", oleTxRecord.getItemPriceSource());
        } else {
            map.put("itemPriceSource", "Publisher");
        }

        List<OleItemPriceSource> matching = (List<OleItemPriceSource>) getBusinessObjectService().findMatching(OleItemPriceSource.class, map);
        OleItemPriceSource oleItemPriceSource = matching.get(0);
        BigInteger bigInteger = oleItemPriceSource.getItemPriceSourceId().toBigInteger();
        return Integer.valueOf(bigInteger.toString());
    }

    private Integer getRequestSourceTypeId(String requestSourceType) {
        Map<String, String> requestSourceMap = new HashMap<>();
        requestSourceMap.put(OLEConstants.OLEBatchProcess.REQUEST_SRC, requestSourceType);
        List<OleRequestSourceType> requestSourceList = (List) getBusinessObjectService().findMatching(OleRequestSourceType.class, requestSourceMap);
        if (requestSourceList != null && requestSourceList.size() > 0) {
            return requestSourceList.get(0).getRequestSourceTypeId();
        }
        return null;
    }

    private void setForeignCurrencyDetails(OleRequisitionItem item, OleRequisitionDocument requisitionDocument) {
        if (requisitionDocument.getVendorDetail().getCurrencyType() != null) {
            if (requisitionDocument.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)) {
                currencyTypeIndicator = true;
            } else {
                currencyTypeIndicator = false;
            }
        }
        if (!currencyTypeIndicator) {
            item.setItemForeignListPrice(item.getItemListPrice());
            item.setItemForeignDiscountType(item.getItemDiscountType());
            item.setItemForeignDiscount(item.getItemDiscount());
            item.setItemListPrice(new KualiDecimal(0.00));
            getOlePurapService().calculateForeignCurrency(item);
            Long currencyTypeId = requisitionDocument.getVendorDetail().getCurrencyType().getCurrencyTypeId();
            Map documentNumberMap = new HashMap();
            documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, currencyTypeId);
            BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
            List<OleExchangeRate> exchangeRateList = (List) businessObjectService.findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
            Iterator iterator = exchangeRateList.iterator();
            if (iterator.hasNext()) {
                OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                item.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
            }
            if (item.getItemExchangeRate() != null && item.getItemForeignUnitCost() != null) {
                item.setItemUnitCostUSD(new KualiDecimal(item.getItemForeignUnitCost().bigDecimalValue().divide(item.getItemExchangeRate(), 4, RoundingMode.HALF_UP)));
                item.setItemUnitPrice(item.getItemUnitCostUSD().bigDecimalValue());
                item.setItemListPrice(item.getItemUnitCostUSD());
            }
        }
    }

    private void populateValuesFromProfileAttributesAndDataMapping(OleRequisitionItem singleItem, OLEBatchProcessJobDetailsBo job, OleRequisitionDocument requisitionDocument) {
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        OleTxRecord oleTxRecord = orderImportHelperBo.getOleTxRecord();
        if (oleTxRecord != null) {
            if (oleTxRecord.getRequestorName() != null) {
                setRequestorInformation(job, singleItem, requisitionDocument, oleTxRecord);
            }
            singleItem.setItemDiscount(new KualiDecimal(oleTxRecord.getDiscount()));
            singleItem.setItemDiscountType(oleTxRecord.getDiscountType());
            if (singleItem.getItemDiscount() != null && singleItem.getItemDiscountType() == null) {
                singleItem.setItemDiscountType(OLEConstants.PERCENTAGE);
            }
            singleItem.setItemUnitPrice(getOlePurapService().calculateDiscount(singleItem).setScale(2, BigDecimal.ROUND_HALF_UP));
            singleItem.setItemStatus(oleTxRecord.getItemStatus());

        }
    }

    private void setRequestorInformation(OLEBatchProcessJobDetailsBo job, OleRequisitionItem singleItem, OleRequisitionDocument requisitionDocument, OleTxRecord oleTxRecord) {
        if (job.getBatchProfileName().contains("GOBI")) {
            String principalId = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT, "GOBI_PRNCPL_INFO");
            StringTokenizer tokenizer = new StringTokenizer(principalId, ";");
            String requestorId = tokenizer.nextToken();
            singleItem.setRequestorId(requestorId);
            String requestorFirstName = tokenizer.nextToken();
            singleItem.setRequestorFirstName(requestorFirstName);
        } else {

            String fullName = oleTxRecord.getRequestorName();
            String[] requestorNames = fullName.split(", ");
            if (requestorNames.length == 2) {
                String lastName = requestorNames[0];
                String firstName = requestorNames[1];

                Map<String, String> requestorNameMap = new HashMap<>();
                requestorNameMap.put(OLEConstants.FIRST_NM, firstName);
                requestorNameMap.put(OLEConstants.LAST_NM, lastName);

                List<OLERequestorPatronDocument> olePatronDocumentList;

                olePatronDocumentList = getOleSelectDocumentService().getPatronDocumentListFromWebService();
                HashMap<String, List<OLERequestorPatronDocument>> patronListMap = new HashMap<>();
                patronListMap.put(requisitionDocument.getDocumentNumber(), olePatronDocumentList);
                getOlePatronDocumentList().setPatronListMap(patronListMap);
                if (olePatronDocumentList != null && olePatronDocumentList.size() > 0) {
                    for (int recCount = 0; recCount < olePatronDocumentList.size(); recCount++) {
                        if (olePatronDocumentList.get(recCount).getFirstName().equalsIgnoreCase(firstName) && olePatronDocumentList.get(recCount).getLastName().equalsIgnoreCase(lastName)) {
                            String patronId = olePatronDocumentList.get(recCount).getOlePatronId();
                            singleItem.setRequestorId(patronId);
                            singleItem.setRequestorFirstName(fullName);
                            break;
                        }
                    }
                }
            }
        }
        singleItem.setItemStatus(oleTxRecord.getItemStatus());
    }

    private void setItemDescription(OleOrderRecord oleOrderRecord, OleRequisitionItem item) throws Exception {

        String title = oleOrderRecord.getOleBibRecord().getBib().getTitle() != null ? oleOrderRecord.getOleBibRecord().getBib().getTitle() + "," : "";
        String author = oleOrderRecord.getOleBibRecord().getBib().getAuthor() != null ? oleOrderRecord.getOleBibRecord().getBib().getAuthor() + "," : "";
        String publisher = oleOrderRecord.getOleBibRecord().getBib().getPublisher() != null ? oleOrderRecord.getOleBibRecord().getBib().getPublisher() + "," : "";
        String isbn = oleOrderRecord.getOleBibRecord().getBib().getIsbn() != null ? oleOrderRecord.getOleBibRecord().getBib().getIsbn() + "," : "";
        String description = title + author
                + publisher + isbn;
        item.setItemDescription(description.substring(0, (description.lastIndexOf(","))));
        item.setItemTitle(oleOrderRecord.getOleBibRecord().getBib().getTitle());
        item.setItemAuthor(oleOrderRecord.getOleBibRecord().getBib().getAuthor());
        item.setBibUUID(oleOrderRecord.getOleBibRecord().getBibUUID());
    }

    public int getRequestorTypeId(String requestorType) {
        int requestorTypeId;
        Map requestorTypeMap = new HashMap();
        requestorTypeMap.put(OLEConstants.RQST_TYPE, requestorType);
        BusinessObjectService businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        List<OleRequestorType> requestorTypeIdList = (List) businessObjectService.findMatching(OleRequestorType.class, requestorTypeMap);
        Iterator itr = requestorTypeIdList.iterator();
        requestorTypeId = requestorTypeIdList.iterator().next().getRequestorTypeId();
        return requestorTypeId;
    }

    /**
     * This method will check whether Requestor exist ,if exist returns existing record if not save the requester.
     *
     * @param oleOrderRecord
     * @return OleRequestor
     */
    protected OleRequestor checkRequestorExist(OleOrderRecord oleOrderRecord) {

        String requestorFirstName = getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.FIRST_NAME);
        String requestorLastName = getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.LAST_NAME);
        String firstName;
        String lastName;
        Map requestorName = new HashMap();
        requestorName.put(OLEConstants.REQUESTOR_FIRST_NM, requestorFirstName);
        requestorName.put(OLEConstants.REQUESTOR_LAST_NM, requestorLastName);
        List<OleRequestor> requestorList = (List) getBusinessObjectService().findMatching(OleRequestor.class, requestorName);
        if (requestorList.size() < 1) {
            return null;
        } else {
            for (int i = 0; i < requestorList.size(); i++) {
                firstName = requestorList.get(i).getRequestorFirstName().toString();
                lastName = requestorList.get(i).getRequestorLastName().toString();
                if (requestorFirstName.equalsIgnoreCase(firstName) && requestorLastName.equalsIgnoreCase(lastName)) {
                    return requestorList.get(i);
                }
            }
            return null;
        }
    }

    /**
     * This method will set the values on OleRequestor and save.
     *
     * @return OleRequestor
     */
    protected OleRequestor saveRequestor(OleOrderRecord oleOrderRecord, OleRequestor oleRequestor) {
        oleRequestor.setRequestorFirstName(getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.FIRST_NAME));
        oleRequestor.setRequestorLastName(getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.LAST_NAME));
        oleRequestor.setRequestorAddress1(getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.ADDRESS1));
        oleRequestor.setRequestorAddress2(getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.ADDRESS2));
        oleRequestor.setRequestorCityName(getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.CITY));
        oleRequestor.setRequestorStateCode(getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.STATE_CODE));
        oleRequestor.setRequestorPostalCode(getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.POSTAL_CODE));
        oleRequestor.setRequestorCountryCode(getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.COUNTRY_CODE));
        oleRequestor.setRequestorPhoneNumber(getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.PHONE_NUMBER));
        oleRequestor.setRequestorEmail(getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.EMAIL));
        oleRequestor.setRequestorSms(getConfigurationService().getPropertyValueAsString(PurapPropertyConstants.SMS));
        oleRequestor.setRequestorTypeId(Integer.toString(getRequestorTypeId(OleSelectConstant.REQUESTOR_TYPE_BATCHINGEST)));
        getOleRequestorService().saveRequestor(oleRequestor);
        return oleRequestor;
    }


    /**
     * Set the Vendor address of the given ID.
     *
     * @param vendorAddress       VendorAddress
     * @param requisitionDocument RequisitionDocument
     */
    public void setVendorAddress(VendorAddress vendorAddress, RequisitionDocument requisitionDocument) {

        if (vendorAddress != null) {
            requisitionDocument.setVendorAddressGeneratedIdentifier(vendorAddress.getVendorAddressGeneratedIdentifier());
            requisitionDocument.setVendorAddressInternationalProvinceName(vendorAddress.getVendorAddressInternationalProvinceName());
            requisitionDocument.setVendorLine1Address(vendorAddress.getVendorLine1Address());
            requisitionDocument.setVendorLine2Address(vendorAddress.getVendorLine2Address());
            requisitionDocument.setVendorCityName(vendorAddress.getVendorCityName());
            requisitionDocument.setVendorStateCode(vendorAddress.getVendorStateCode());
            requisitionDocument.setVendorPostalCode(vendorAddress.getVendorZipCode());
            requisitionDocument.setVendorCountryCode(vendorAddress.getVendorCountryCode());
        }

    }


    public String getDocumentDescription(OleRequisitionDocument requisitionDocument, OleOrderRecord oleOrderRecord, OLEBatchProcessJobDetailsBo job, int recPosition) {
        String description = getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.ORDER_IMPORT_REQ_DESC);
        Map<String, String> descMap = new HashMap<>();
        if (requisitionDocument.getVendorDetail().getVendorAliases() != null && requisitionDocument.getVendorDetail().getVendorAliases().size() > 0 && requisitionDocument.getVendorDetail().getVendorAliases().get(0).getVendorAliasName() != null) {
            descMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_NAME, requisitionDocument.getVendorDetail().getVendorAliases().get(0).getVendorAliasName());
        }
        descMap.put(org.kuali.ole.sys.OLEConstants.ORDER_TYP, oleOrderRecord.getOleTxRecord().getOrderType());
        descMap.put(org.kuali.ole.sys.OLEConstants.VND_ITM_ID, oleOrderRecord.getOleTxRecord().getVendorItemIdentifier() != null && !oleOrderRecord.getOleTxRecord().getVendorItemIdentifier().isEmpty() ? oleOrderRecord.getOleTxRecord().getVendorItemIdentifier() + "_" : "");
        description = getOlePurapService().setDocumentDescription(description, descMap);
        if (!description.equals("") && description != null) {
            description = description.substring(0, description.lastIndexOf("_"));
        }
        if (description.startsWith("_")) {
            description = description.substring(1);
        }
        if (description.length() > 255) {
            if (job.getOrderImportHelperBo().getOleBatchProcessProfileBo().getRequisitionsforTitle().equalsIgnoreCase(OLEConstants.ONE_REQUISITION_PER_TITLE)) {
                job.getOrderImportHelperBo().getFailureReason().add(OLEConstants.OLEBatchProcess.REC_POSITION + (recPosition + 1) + " " + OLEConstants.OLEBatchProcess.DESC_MAX_LENG);
            } else {
                job.getOrderImportHelperBo().getFailureReason().add(OLEConstants.OLEBatchProcess.DESC_MAX_LENG);
            }
        }
        return description;
    }

    public RequisitionCreateDocumentService getRequisitionCreateDocumentService() {
        if (requisitionCreateDocumentService == null) {
            requisitionCreateDocumentService = SpringContext.getBean(RequisitionCreateDocumentService.class);
        }
        return requisitionCreateDocumentService;
    }

    public void setRequisitionCreateDocumentService(RequisitionCreateDocumentService requisitionCreateDocumentService) {
        this.requisitionCreateDocumentService = requisitionCreateDocumentService;
    }

    public OleRequestorService getOleRequestorService() {
        if (oleRequestorService == null) {
            oleRequestorService = SpringContext.getBean(OleRequestorService.class);
        }
        return oleRequestorService;
    }

    public void setOleRequestorService(OleRequestorService oleRequestorService) {
        this.oleRequestorService = oleRequestorService;
    }

    public VendorService getVendorService() {
        if (vendorService == null) {
            vendorService = SpringContext.getBean(VendorService.class);
        }
        return vendorService;
    }

    public void setVendorService(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public OleRequestSourceService getOleRequestSourceService() {
        if (oleRequestSourceService == null) {
            oleRequestSourceService = SpringContext.getBean(OleRequestSourceService.class);
        }
        return oleRequestSourceService;
    }

    public void setOleRequestSourceService(OleRequestSourceService oleRequestSourceService) {
        this.oleRequestSourceService = oleRequestSourceService;
    }

    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public void setOlePurapService(OlePurapService olePurapService) {
        this.olePurapService = olePurapService;
    }

    public BibInfoService getBibInfoService() {
        if (bibInfoService == null) {
            bibInfoService = SpringContext.getBean(BibInfoService.class);
        }
        return bibInfoService;
    }

    public void setBibInfoService(BibInfoService bibInfoService) {
        this.bibInfoService = bibInfoService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

}
