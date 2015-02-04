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
package org.kuali.ole.select.batch.service.impl;

import org.kuali.ole.module.purap.PurapConstants.RequisitionStatuses;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.RequisitionAccount;
import org.kuali.ole.module.purap.businessobject.RequisitionItem;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.batch.service.OleRequisitionCreateDocumentService;
import org.kuali.ole.select.batch.service.RequisitionCreateDocumentService;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.document.service.OleRequestSourceService;
import org.kuali.ole.select.document.service.OleRequestorService;
import org.kuali.ole.select.service.BibInfoService;
import org.kuali.ole.select.validation.BibInfoValidation;
import org.kuali.ole.select.validation.StandardNumberValidation;
import org.kuali.ole.select.validation.impl.BibinfoValidationImpl;
import org.kuali.ole.select.validation.impl.StandardNumberValidationImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEConstants.FinancialDocumentTypeCodes;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorContract;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;

public class OleRequisitionCreateDocumentServiceImpl extends RequisitionCreateDocumentServiceImpl implements OleRequisitionCreateDocumentService {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleRequisitionCreateDocumentServiceImpl.class);

    protected RequisitionCreateDocumentService requisitionCreateDocumentService;
    protected OleRequestorService oleRequestorService;
    protected VendorService vendorService;
    protected OleRequestSourceService oleRequestSourceService;
    protected BusinessObjectService businessObjectService;
    protected DocumentService documentService;
    protected BibInfoService bibInfoService;
    protected ConfigurationService kualiConfigurationService;
    protected List reqList;

    public RequisitionCreateDocumentService getRequisitionCreateDocumentService() {
        return requisitionCreateDocumentService;
    }

    public void setRequisitionCreateDocumentService(RequisitionCreateDocumentService requisitionCreateDocumentService) {
        this.requisitionCreateDocumentService = requisitionCreateDocumentService;
    }

    public OleRequestorService getOleRequestorService() {
        return oleRequestorService;
    }

    public void setOleRequestorService(OleRequestorService oleRequestorService) {
        this.oleRequestorService = oleRequestorService;
    }

    public VendorService getVendorService() {
        return vendorService;
    }

    public void setVendorService(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    public OleRequestSourceService getOleRequestSourceService() {
        return oleRequestSourceService;
    }

    public void setOleRequestSourceService(OleRequestSourceService oleRequestSourceService) {
        this.oleRequestSourceService = oleRequestSourceService;
    }

    public BibInfoService getBibInfoService() {
        return bibInfoService;
    }

    public void setBibInfoService(BibInfoService bibInfoService) {
        this.bibInfoService = bibInfoService;
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public List getReqList() {
        return reqList;
    }

    public ConfigurationService getConfigurationService() {
        return kualiConfigurationService;
    }

    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

    /**
     * Set the values for the Requisition Document and save.
     *
     * @param bibInfoBeanList ArrayList
     */
    public String saveRequisitionDocument(List<BibInfoBean> bibInfoBeanList, boolean vendorRecordMappingFlag) throws Exception {
        String docNumber = "";
        vendorService = getVendorService();
        oleRequestorService = getOleRequestorService();
        requisitionCreateDocumentService = getRequisitionCreateDocumentService();
        oleRequestSourceService = getOleRequestSourceService();
        if (vendorRecordMappingFlag) {
            reqList = new ArrayList(0);
            HashMap<String, String> dataMap = new HashMap<String, String>();
            if (bibInfoBeanList.iterator().hasNext()) {
                if (bibInfoBeanList.iterator().next().getRequisitionSource().equals(OleSelectConstant.REQUISITON_SRC_TYPE_MANUALINGEST)) {
                    dataMap.put(OleSelectConstant.DOC_CATEGORY_TYPE, OleSelectConstant.DOC_CATEGORY_TYPE_ITEM);
                    bibInfoBeanList = bibInfoService.getUUID(bibInfoBeanList, dataMap);
                }
            }
            for (BibInfoBean bibInfoBean : bibInfoBeanList) {
                StandardNumberValidation validation = new StandardNumberValidationImpl();
                BibInfoValidation mandatoryValidation = new BibinfoValidationImpl();
                String message = validation.isValidStandardNumbers(bibInfoBean);
                message = mandatoryValidation.validateMadatory(bibInfoBean);
                if (message.equalsIgnoreCase(OleSelectConstant.STATUS) || message.equals("")) {
                    OleRequisitionDocument requisitionDocument = createRequisitionDocument();
                    requisitionDocument.setItems(generateItemList(bibInfoBean));
                    //requisitionDocument.setBoNotes(generateBoNotesList(bibInfoBean));
                    requisitionDocument.setRequisitionSourceCode(bibInfoBean.getRequisitionSource());
                    if (bibInfoBeanList != null && bibInfoBeanList.size() > 0) {

                        if (bibInfoBean.getPurchaseOrderType() != null) {
                            requisitionDocument.setPurchaseOrderTypeId(bibInfoBean.getPurchaseOrderType());
                        } /*else{
                         requisitionDocument.setPurchaseOrderTypeId("firm_fixed_ybp");
                     }*/
                        setDocumentValues(requisitionDocument, bibInfoBean);
                    }
                    // requisitionCreateDocumentService.saveRequisitionDocuments(requisitionDocument);
                    docNumber = requisitionCreateDocumentService.saveRequisitionDocuments(requisitionDocument);
                    reqList.add(requisitionDocument.getPurapDocumentIdentifier());

                } else {
                    docNumber = message;
                }
            }

        } else {
            OleRequisitionDocument requisitionDocument = createRequisitionDocument();
            requisitionDocument.setItems(generateMultipleItemsForOneRequisition(bibInfoBeanList));
            if (bibInfoBeanList != null && bibInfoBeanList.size() > 0) {
                if (bibInfoBeanList.get(1).getPurchaseOrderType() != null) {
                    requisitionDocument.setPurchaseOrderTypeId(bibInfoBeanList.get(1).getPurchaseOrderType());
                } /*else{
                 requisitionDocument.setPurchaseOrderTypeId("firm_fixed_ybp");
             }*/

                setDocumentValues(requisitionDocument, bibInfoBeanList.get(1));
            }
            docNumber = requisitionCreateDocumentService.saveRequisitionDocuments(requisitionDocument);

        }
        return docNumber;
    }

    /**
     * To create the Requisition document object
     *
     * @return OleRequisitionDocument
     */
    protected OleRequisitionDocument createRequisitionDocument() throws WorkflowException {
        return (OleRequisitionDocument) SpringContext.getBean(DocumentService.class).getNewDocument(FinancialDocumentTypeCodes.REQUISITION);
    }

    /**
     * To create the requisition document
     *
     * @param bibInfoBean BibInfoBean
     * @return RequisitionDocument
     */
    protected RequisitionDocument setDocumentValues(OleRequisitionDocument requisitionDocument, BibInfoBean bibInfoBean) throws Exception {
        // ******************Document Overview Section******************
        //requisitionDocument.getDocumentHeader().setDocumentDescription(bibInfoBean.getIsbn());
        //  requisitionDocument.setStatusCode(PurapConstants.RequisitionStatuses.APPDOC_IN_PROCESS);
        requisitionDocument.setApplicationDocumentStatus(RequisitionStatuses.APPDOC_IN_PROCESS);
        requisitionDocument.setVendorPoNumber(bibInfoBean.getYbp());
        // ******************Financial Document Detail Section******************
        //requisitionDocument.setFinancialYear(bibInfoBean.getFinancialYear());


        // ******************Requisition Detail Section******************

        requisitionDocument.setChartOfAccountsCode(bibInfoBean.getChartOfAccountsCode());
        requisitionDocument.setOrganizationCode(bibInfoBean.getOrganizationCode());
        requisitionDocument.setDocumentFundingSourceCode(bibInfoBean.getDocumentFundingSourceCode());
        requisitionDocument.setUseTaxIndicator(bibInfoBean.isUseTaxIndicator());

        // ******************Delivery Section******************

        setDeliveryDetails(requisitionDocument, bibInfoBean);
        requisitionDocument.setDeliveryCampusCode(bibInfoBean.getDeliveryCampusCode());

        // ******************Vendor Section******************

        setVendorDetails(requisitionDocument, bibInfoBean);

        // ******************Items Section******************
        // requisitionDocument.setListprice(bibInfoBean.getListprice());
        // requisitionDocument.setQuantity(bibInfoBean.getQuantity());

        // ******************Capital Assets Section******************
        // ******************Payment INfo Section******************
        if (LOG.isDebugEnabled())
            LOG.debug("----------Billing Name in OleRequisition--------------" + bibInfoBean.getBillingName());
        requisitionDocument.setBillingName(bibInfoBean.getBillingName());
        requisitionDocument.setBillingLine1Address(bibInfoBean.getBillingLine1Address());
        requisitionDocument.setBillingCityName(bibInfoBean.getBillingCityName());
        requisitionDocument.setBillingStateCode(bibInfoBean.getBillingStateCode());
        requisitionDocument.setBillingPostalCode(bibInfoBean.getBillingPostalCode());
        requisitionDocument.setBillingPhoneNumber(bibInfoBean.getBillingPhoneNumber());
        requisitionDocument.setBillingCountryCode(bibInfoBean.getBillingCountryCode());
        requisitionDocument.setDeliveryBuildingRoomNumber(bibInfoBean.getDeliveryBuildingRoomNumber());
        // ******************Additional Institutional Info Section******************

        requisitionDocument.setPurchaseOrderTransmissionMethodCode(bibInfoBean.getPurchaseOrderTransmissionMethodCode());
        requisitionDocument.setPurchaseOrderCostSourceCode(bibInfoBean.getPurchaseOrderCostSourceCode());
        requisitionDocument.setRequestorPersonName(bibInfoBean.getRequestorPersonName());
        requisitionDocument.setRequestorPersonPhoneNumber(bibInfoBean.getRequestorPersonPhoneNumber());
        requisitionDocument.setRequestorPersonEmailAddress(bibInfoBean.getRequestorPersonEmailAddress());

        // ******************Account Summary Section******************
        // ******************View Related Documents Section******************
        // ******************View Payment History Section******************
        // ******************Notes and Attachments Section******************
        // ******************Ad Hoc Recipients Section******************
        // ******************Route Log Section ******************


        // ******************Common Document Properties..******************
        //requisitionDocument.setFundcode(bibInfoBean.getAccountNumber());
        //requisitionDocument.setLocation(bibInfoBean.getLocation());
        //requsitionDocument.setSubAccountNumber(bibInfoBean.getSubAccountNumber());
        //requisitionDocument.setRequisitionSourceCode(PurapConstants.RequisitionSources.STANDARD_ORDER);
        requisitionDocument.setOrganizationAutomaticPurchaseOrderLimit(new KualiDecimal(bibInfoBean.getOrganizationAutomaticPurchaseOrderLimit()));
        requisitionDocument.setPurchaseOrderAutomaticIndicator(bibInfoBean.isPurchaseOrderAutomaticIndicator());
        requisitionDocument.setReceivingDocumentRequiredIndicator(bibInfoBean.isReceivingDocumentRequiredIndicator());
        requisitionDocument.setPaymentRequestPositiveApprovalIndicator(bibInfoBean.isPaymentRequestPositiveApprovalIndicator());
        
        /*requisitionDocument.setLicensingRequirementIndicator(bibInfoBean.isLicensingRequirementIndicator());*/
        /*requisitionDocument.setLicensingRequirementCode(bibInfoBean.getLicensingRequirementCode());*/

        return requisitionDocument;

    }

    /**
     * To set the delivery details for the Requisition Document
     *
     * @param bibInfoBean BibInfoBean
     * @return requisitionDocument OleRequisitionDocument
     */
    private void setDeliveryDetails(OleRequisitionDocument requisitionDocument, BibInfoBean bibInfoBean) {
        if (LOG.isDebugEnabled())
            LOG.debug("bibInfoBean.getDeliveryBuildingCode----------->" + bibInfoBean.getDeliveryBuildingCode());

        if (bibInfoBean.getDeliveryCampusCode() != null && bibInfoBean.getDeliveryBuildingCode() != null) {
            Building building = vendorService.getBuildingDetails(bibInfoBean.getDeliveryCampusCode(), bibInfoBean.getDeliveryBuildingCode());
            if (building != null) {
                requisitionDocument.setDeliveryBuildingCode(building.getBuildingCode());
                requisitionDocument.setDeliveryCampusCode(building.getCampusCode());
                requisitionDocument.setDeliveryBuildingLine1Address(building.getBuildingStreetAddress());
                requisitionDocument.setDeliveryBuildingName(building.getBuildingName());
                requisitionDocument.setDeliveryCityName(building.getBuildingAddressCityName());
                requisitionDocument.setDeliveryStateCode(building.getBuildingAddressStateCode());
                requisitionDocument.setDeliveryPostalCode(building.getBuildingAddressZipCode());
                requisitionDocument.setDeliveryCountryCode(building.getBuildingAddressCountryCode());
            }
        }
        requisitionDocument.setDeliveryToName(bibInfoBean.getDeliveryToName());
        requisitionDocument.setDeliveryBuildingOtherIndicator(bibInfoBean.isDeliveryBuildingOtherIndicator());
    }

    /**
     * To set the vendor details for the Requisition Document
     *
     * @param bibInfoBean BibInfoBean
     * @return requisitionDocument OleRequisitionDocument
     */
    private void setVendorDetails(OleRequisitionDocument requisitionDocument, BibInfoBean bibInfoBean) {
        if (bibInfoBean.getVendorCode() != null) {
            VendorDetail vendorDetail = vendorService.getVendorDetail(bibInfoBean.getVendorCode());

            requisitionDocument.setVendorCustomerNumber(bibInfoBean.getVendorCustomerNumber());
            requisitionDocument.setVendorNumber(bibInfoBean.getVendorCode());
            requisitionDocument.setVendorNumber(vendorDetail.getVendorNumber());
            requisitionDocument.setVendorName(vendorDetail.getVendorName());
            requisitionDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            requisitionDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            requisitionDocument.setVendorDetail(vendorDetail);

            String deliveryCampus = bibInfoBean.getDeliveryCampusCode();
            Integer headerId = null;
            Integer detailId = null;
            int dashInd = vendorDetail.getVendorNumber().indexOf('-');
            // make sure there's at least one char before and after '-'
            if (dashInd > 0 && dashInd < vendorDetail.getVendorNumber().length() - 1) {
                headerId = new Integer(vendorDetail.getVendorNumber().substring(0, dashInd));
                detailId = new Integer(vendorDetail.getVendorNumber().substring(dashInd + 1));
            }
            VendorAddress vendorAddress = vendorService.getVendorDefaultAddress(headerId, detailId, VendorConstants.AddressTypes.PURCHASE_ORDER, deliveryCampus);
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
     * @param bibInfoBean BibInfoBean
     * @return ArrayList
     */
    private List<RequisitionItem> generateItemList(BibInfoBean bibInfoBean) throws Exception {
        List<RequisitionItem> items = new ArrayList<RequisitionItem>();
        int itemLineNumber = 1;
        items.add(createRequisitionItem(bibInfoBean, itemLineNumber));
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
     * @param bibInfoBeanList List<BibInfoBean>
     * @return ArrayList
     */
    private List<RequisitionItem> generateMultipleItemsForOneRequisition(List<BibInfoBean> bibInfoBeanList) throws Exception {
        List<RequisitionItem> items = new ArrayList<RequisitionItem>();
        int itemLineNumber = 0;
        // set items to document
        for (BibInfoBean tempBibInfoBean : bibInfoBeanList) {
            itemLineNumber++;
            items.add(createRequisitionItem(tempBibInfoBean, itemLineNumber));
        }
        return items;
    }

    /**
     * To create the requisition item for the Requisition Document.
     *
     * @param bibInfoBean BibInfoBean
     * @return RequisitionItem
     */
    @SuppressWarnings("deprecation")
    protected RequisitionItem createRequisitionItem(BibInfoBean bibInfoBean, int itemLineNumber) throws Exception {
        OleRequisitionItem item = new OleRequisitionItem();

        item.setBibInfoBean(bibInfoBean);
        item.setItemLineNumber(itemLineNumber);
        item.setItemUnitOfMeasureCode(bibInfoBean.getUom());
        item.setItemQuantity(new KualiDecimal(bibInfoBean.getQuantity()));
        item.setItemDescription(bibInfoBean.getIsbn());
        item.setItemUnitPrice(new BigDecimal(bibInfoBean.getListprice()));
        item.setItemTypeCode(bibInfoBean.getItemTypeCode());
        item.setItemListPrice(new KualiDecimal(bibInfoBean.getListprice()));
        if (ObjectUtils.isNotNull(bibInfoBean.getTitleId())) {
            item.setItemTitleId(bibInfoBean.getTitleId());
        }

        if (item.getItemType() == null) {
            org.kuali.ole.module.purap.businessobject.ItemType itemType = businessObjectService.findBySinglePrimaryKey(org.kuali.ole.module.purap.businessobject.ItemType.class, bibInfoBean.getItemTypeCode());
            item.setItemType(itemType);
        }

        RequisitionAccount requisitionAccount = new RequisitionAccount();
        requisitionAccount.setChartOfAccountsCode(bibInfoBean.getChart());
        requisitionAccount.setAccountNumber(bibInfoBean.getAccountNumber());
        requisitionAccount.setFinancialObjectCode(bibInfoBean.getObjectCode());
        requisitionAccount.setDebitCreditCode(OLEConstants.GL_DEBIT_CODE);
        if (bibInfoBean.getListprice() != null) {
            requisitionAccount.setAmount(new KualiDecimal(bibInfoBean.getListprice()));
        }
        if (bibInfoBean.getPercent() != null) {
            requisitionAccount.setAccountLinePercent(new BigDecimal(bibInfoBean.getPercent()));
        }
        if (bibInfoBean.getAccountNumber() != null) {
            List<PurApAccountingLine> sourceAccountingLines = item.getSourceAccountingLines();
            if (sourceAccountingLines.size() == 0) {
                sourceAccountingLines = new ArrayList<PurApAccountingLine>(0);
            }
            sourceAccountingLines.add((PurApAccountingLine) requisitionAccount);
            item.setSourceAccountingLines(sourceAccountingLines);
        }
      
/*        OleRequestor oleRequestor = checkRequestorExist(bibInfoBean); 
        if(oleRequestor == null) {
            
            oleRequestor =  new OleRequestor();
            oleRequestor = saveRequestor(bibInfoBean, oleRequestor);
        }
        
        item.setOleRequestor(oleRequestor);
       
        item.setRequestorId(oleRequestor.getRequestorId());
//        item.setRequestSourceTypeId(oleRequestSourceService.getRequestSourceTypeId(bibInfoBean.getRequestSource()));
        item.setRequestorFirstName(oleRequestor.getRequestorFirstName());
       
        item.setRequestorLastName(oleRequestor.getRequestorLastName());*/
        item.setRequestorId(bibInfoBean.getRequestorId());
        item.setRequestSourceUrl(bibInfoBean.getRequestSourceUrl());

        //getOleRequestorService().saveRequestor(oleRequestor);

        String requestorType = bibInfoBean.getRequestorType();

        if (requestorType == null || "".equals(requestorType)) {

            requestorType = OleSelectConstant.REQUESTOR_TYPE_STAFF;
        }

        int requestorTypeId = getRequestorTypeId(requestorType);
        item.setRequestorTypeId(requestorTypeId);
        if (bibInfoBean.getRequisitionSource().equals(OleSelectConstant.REQUISITON_SRC_TYPE_WEBFORM) && !bibInfoBean.getRequestersNotes().trim().equals("")) {
            OleRequisitionNotes note = new OleRequisitionNotes();
            Map notes = new HashMap();
            String noteType = OleSelectConstant.REQUESTOR_NOTES_PRE_ORDER_SERVICE;
            notes.put("noteType", noteType);
            List<OleNoteType> noteTypeList = (List) businessObjectService.findMatching(org.kuali.ole.select.businessobject.OleNoteType.class, notes);
            note.setNoteTypeId(noteTypeList.get(0).getNoteTypeId());
            note.setNote(bibInfoBean.getRequestersNotes());
            item.getNotes().add(note);
        }
        return item;
    }

    public int getRequestorTypeId(String requestorType) {
        int requestorTypeId;
        Map requestorTypeMap = new HashMap();
        requestorTypeMap.put("requestorType", requestorType);
        BusinessObjectService businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        List<OleRequestorType> requestorTypeIdList = (List) businessObjectService.findMatching(OleRequestorType.class, requestorTypeMap);
        Iterator itr = requestorTypeIdList.iterator();
        requestorTypeId = requestorTypeIdList.iterator().next().getRequestorTypeId();
        return requestorTypeId;
    }
    
/*    public Integer getRequestSourceTypeId(String requestSourceType)throws Exception{
        Integer requestSourceTypeId = null;
        Map requestorSourceTypeMap = new HashMap();
        requestorSourceTypeMap.put("requestSourceType", requestSourceType);
        BusinessObjectService businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        List<OleRequestSourceType> requestorSourceTypeList = (List) businessObjectService.findMatching(OleRequestSourceType.class, requestorSourceTypeMap);
        if(requestorSourceTypeList.iterator().hasNext()){
            requestSourceTypeId = requestorSourceTypeList.iterator().next().getRequestSourceTypeId();
        }
        return requestSourceTypeId;
    }*/

    /**
     * This method will check whether Requestor exist ,if exist returns existing record if not save the requester.
     *
     * @param bibInfoBean
     * @return OleRequestor
     */
    protected OleRequestor checkRequestorExist(BibInfoBean bibInfoBean) {

        String requestorFirstName = bibInfoBean.getRequestorsFirstName();
        String requestorLastName = bibInfoBean.getRequestorsLastName();
        String firstName;
        String lastName;
        Map requestorName = new HashMap();
        requestorName.put("requestorFirstName", requestorFirstName);
        requestorName.put("requestorLastName", requestorLastName);
        List<OleRequestor> requestorList = (List) businessObjectService.findMatching(OleRequestor.class, requestorName);
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
    protected OleRequestor saveRequestor(BibInfoBean bibInfoBean, OleRequestor oleRequestor) {

        if ((bibInfoBean.getRequestorsFirstName() != null && bibInfoBean.getRequestorsFirstName().length() > 0)
                || (bibInfoBean.getRequestorsLastName() != null && bibInfoBean.getRequestorsLastName().length() > 0)
                || (bibInfoBean.getRequestorsEmail() != null && bibInfoBean.getRequestorsEmail().length() > 0)) {
            oleRequestor.setRequestorFirstName(bibInfoBean.getRequestorsFirstName() != null ? bibInfoBean.getRequestorsFirstName() : "");
            oleRequestor.setRequestorLastName(bibInfoBean.getRequestorsLastName() != null ? bibInfoBean.getRequestorsLastName() : "");
            oleRequestor.setRequestorAddress1(bibInfoBean.getRequestorsAddress1() != null ? bibInfoBean.getRequestorsAddress1() : "");
            oleRequestor.setRequestorAddress2(bibInfoBean.getRequestorsAddress2() != null ? bibInfoBean.getRequestorsAddress2() : "");
            oleRequestor.setRequestorCityName(bibInfoBean.getRequestorsCity() != null ? bibInfoBean.getRequestorsCity() : "");
            oleRequestor.setRequestorStateCode(bibInfoBean.getRequestorsState() != null ? bibInfoBean.getRequestorsState() : "");
            oleRequestor.setRequestorPostalCode(bibInfoBean.getRequestorsZipCode() != null ? bibInfoBean.getRequestorsZipCode() : "");
            oleRequestor.setRequestorCountryCode(bibInfoBean.getRequestorsCountryCode() != null ? bibInfoBean.getRequestorsCountryCode() : "");
            oleRequestor.setRequestorPhoneNumber(bibInfoBean.getRequestorsPhone() != null ? bibInfoBean.getRequestorsPhone() : "");
            oleRequestor.setRequestorEmail(bibInfoBean.getRequestorsEmail() != null ? bibInfoBean.getRequestorsEmail() : "");
            oleRequestor.setRequestorSms(bibInfoBean.getRequestorsSMS() != null ? bibInfoBean.getRequestorsSMS() : "");
            oleRequestor.setRequestorTypeId(bibInfoBean.getRequestorType() != null ? Integer.toString(getRequestorTypeId(bibInfoBean.getRequestorType())) : "");
            getOleRequestorService().saveRequestor(oleRequestor);
        }
                /*else{
                    Properties properties = loadPropertiesFromClassPath("org/kuali/ole/select/batch/service/impl/bibinfo.properties");
                    oleRequestor.setRequestorFirstName(properties.getProperty("firstName"));
                    oleRequestor.setRequestorLastName(properties.getProperty("lastName"));
                    oleRequestor.setRequestorAddress1(properties.getProperty("address1"));
                    oleRequestor.setRequestorAddress2(properties.getProperty("address2"));
                    oleRequestor.setRequestorCityName(properties.getProperty("city"));
                    oleRequestor.setRequestorStateCode(properties.getProperty("stateCode"));
                    oleRequestor.setRequestorPostalCode(properties.getProperty("postalCode"));
                    oleRequestor.setRequestorCountryCode(properties.getProperty("countryCode"));
                    oleRequestor.setRequestorPhoneNumber(properties.getProperty("phoneNumber"));
                    oleRequestor.setRequestorEmail(properties.getProperty("email"));
                    oleRequestor.setRequestorSms(properties.getProperty("sms"));
                }
        getOleRequestorService().saveRequestor(oleRequestor);*/


        return oleRequestor;
    }


    /**
     * Set the Vendor address of the given ID.
     *
     * @param vendorAddress       VendorAddress
     * @param requisitionDocument RequisitionDocument
     */
    protected void setVendorAddress(VendorAddress vendorAddress, RequisitionDocument requisitionDocument) {

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


}
