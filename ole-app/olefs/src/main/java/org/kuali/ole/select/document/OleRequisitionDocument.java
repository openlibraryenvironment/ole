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
package org.kuali.ole.select.document;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibMarc;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.gl.service.SufficientFundsService;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapConstants.RequisitionStatuses;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.PurapWorkflowConstants;
import org.kuali.ole.module.purap.businessobject.DefaultPrincipalAddress;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.businessobject.RequisitionItem;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.VendorCreditMemoDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.document.service.RequisitionService;
import org.kuali.ole.module.purap.service.PurapAccountingService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.service.*;
import org.kuali.ole.select.service.BibInfoService;
import org.kuali.ole.select.service.BibInfoWrapperService;
import org.kuali.ole.select.service.FileProcessingService;
import org.kuali.ole.select.service.impl.BibInfoServiceImpl;
import org.kuali.ole.select.service.impl.exception.DocStoreConnectionException;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.GeneralLedgerPendingEntry;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.businessobject.SufficientFundsItem;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.GeneralLedgerPendingEntryService;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.framework.parameter.ParameterConstants.COMPONENT;
import org.kuali.rice.coreservice.framework.parameter.ParameterConstants.NAMESPACE;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.RoutingReportCriteria;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.rules.rule.event.ApproveDocumentEvent;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.rules.rule.event.RouteDocumentEvent;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentHeaderService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@NAMESPACE(namespace = OleParameterConstants.PURCHASING_NAMESPACE)
@COMPONENT(component = "Requisition")
public class OleRequisitionDocument extends RequisitionDocument {

    /*
     * Modified as per review comments OLE-24
     */
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleRequisitionDocument.class);
    // protected DateTimeService dateTimeService;
    private static final String dateTimeFormat = "MMddHHmm";
    private static final String dateFormat = "MMddyy";
    private static final String timeStampFormat = "MMddyyHHmm";
    private String vendorPoNumber;
    private PurchasingAccountsPayableDocument purchasingAccountsPayableDocument;
    private String vendorAliasName;
    private DocstoreClientLocator docstoreClientLocator;
    private static transient ConfigurationService kualiConfigurationService;
    private static transient BibInfoWrapperService bibInfoWrapperService;
    private static transient DateTimeService dateTimeService;
    private static transient OleRequestSourceService oleRequestSourceService;
    private static transient PurchaseOrderTypeService purchaseOrderTypeService;
    private static transient FileProcessingService fileProcessingService;
    private static transient BusinessObjectService businessObjectService;
    private static transient ParameterService parameterService;
    private static transient RequisitionService RequisitionService;
    private static transient OlePurchaseOrderService olePurchaseOrderService;
    private static transient WorkflowDocumentService workflowDocumentService;
    private static transient OlePurapAccountingService olePurapAccountingService;
    private static transient PurapService purapServiceImpl;
    private static transient BibInfoService bibInfoService;
    private static transient OlePurapService olePurapService;
    private static transient OlePatronDocumentList olePatronDocumentList;
    private static transient OleCopyHelperService oleCopyHelperService;

    public String getVendorAliasName() {
        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    @Override
    public boolean answerSplitNodeQuestion(String nodeName) throws UnsupportedOperationException {
        if (nodeName.equals(PurapWorkflowConstants.HAS_ACCOUNTING_LINES)) {
            return !isMissingAccountingLines();
        }
        /*if (nodeName.equals(PurapWorkflowConstants.HAS_LICENSE_REQUEST)) {
            return isLicenseRequested();
        }*/
        else if (nodeName.equals(PurapWorkflowConstants.AMOUNT_REQUIRES_SEPARATION_OF_DUTIES_REVIEW_SPLIT)) {
            return isSeparationOfDutiesReviewRequired();
            /*
         * Modified as per review comments for OLE-24 Methods are commented as Notification is not implemented currently To revisit
         * the code later based on notification methodology to be adopted
             */
        }

        /*
         * if (nodeName.equals(PurapWorkflowConstants.FOR_INFORMATION)) return isLineItemGreater(); if
         * (nodeName.equals(PurapWorkflowConstants.DUPLICATE_RECORD_CHECK)) return isDuplicateRecord(); if
         * (nodeName.equals(PurapWorkflowConstants.NEW_VENDOR_CHECK)) return isNewVendor();
        */
        else if (nodeName.equals(PurapWorkflowConstants.YBP_ORDERS)) {
            return true;
            //return isRequiredOrderType(this.getPurchaseOrderTypeId(), PurapConstants.ORDER_TYPE_FIRM);
        }
        else if (nodeName.equals(PurapWorkflowConstants.STANDING_ORDERS)) {
            return true;
            //return isRequiredOrderType(this.getPurchaseOrderTypeId(), PurapConstants.ORDER_TYPE_STANDING);
        }
        else if (nodeName.equals(PurapWorkflowConstants.SUBSCRIPTION_ORDERS)) {
            return true;
            //return isRequiredOrderType(this.getPurchaseOrderTypeId(), PurapConstants.ORDER_TYPE_SUBSCRIPTION);
        }
        else if (nodeName.equals(PurapWorkflowConstants.APPROVAL_ORDERS)) {
            return true;
            //return isRequiredOrderType(this.getPurchaseOrderTypeId(), PurapConstants.ORDER_TYPE_APPROVAL);
        }
        else if (nodeName.equals(PurapWorkflowConstants.HAS_VENDOR)) {
            return isMissingVendor();
        }
        else if (nodeName.equals(PurapWorkflowConstants.HAS_FIRMFIXED_WITH_LR)) {
            return true;
            //return isRequiredOrderType(this.getPurchaseOrderTypeId(), PurapConstants.ORDER_TYPE_FIRM);
        }
        else if (nodeName.equals(PurapWorkflowConstants.BUDGET_REVIEW_REQUIRED)) {
            return isBudgetReviewRequired();
        }
        else if (nodeName.equals(PurapWorkflowConstants.NOTIFY_BUDGET_REVIEW)) {
            return isNotificationRequired();
        }
        throw new UnsupportedOperationException("Cannot answer split question for this node you call \"" + nodeName + "\"");
    }

    /*
     * Modified as per review comments OLE-24
     */
    protected boolean isMissingVendor() {
        if (this.getVendorHeaderGeneratedIdentifier() == null || this.getVendorDetailAssignedIdentifier() == null) {
            return false;
        } else {
            return true;
        }
    }

    /*protected boolean isLicenseRequested() {
        if (this.isLicensingRequirementIndicator()) {
            return true;
        }
        else {
            return false;
        }
    }
*/
    @Override
    protected boolean isMissingAccountingLines() {
        for (Iterator iterator = getItems().iterator(); iterator.hasNext(); ) {
            RequisitionItem item = (RequisitionItem) iterator.next();
            if (item.isConsideredEntered() && item.isAccountListEmpty()) {
                return true;
            }
        }

        return false;
    }

    /*
     * Modified as per review comments for OLE-24
     */
    protected boolean isRequiredOrderType(BigDecimal purchaseOrderTypeId, String orderType) {
        LOG.debug("----Inside isRequiredOrderType------");
        /*   Map purchaseOrderTypeIdMap = new HashMap();
        String purchaseOrderType = "";
        purchaseOrderTypeIdMap.put("purchaseOrderTypeId", this.getPurchaseOrderTypeId());

        org.kuali.rice.krad.service.BusinessObjectService
                businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        List<PurchaseOrderType> purchaseOrderTypeList = (List) businessObjectService.findMatching(PurchaseOrderType.class, purchaseOrderTypeIdMap);
        if (purchaseOrderTypeList.size() > 0) {
            purchaseOrderType = purchaseOrderTypeList.get(0).getPurchaseOrderType();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Purchase Order Type is >>>>>>" + purchaseOrderType);
        }

        if (purchaseOrderType.equals(orderType)) {
            return true;
        }*/

        return true;
    }

    public static OlePurapAccountingService getOlePurapAccountingService() {
        if (olePurapAccountingService == null) {
            olePurapAccountingService = SpringContext.getBean(OlePurapAccountingService.class);
        }
        return olePurapAccountingService;
    }

    public static void setOlePurapAccountingService(OlePurapAccountingService olePurapAccountingService) {
        OleRequisitionDocument.olePurapAccountingService = olePurapAccountingService;
    }

    public static PurapService getPurapServiceImpl() {
        if (purapServiceImpl == null) {
            purapServiceImpl = SpringContext.getBean(PurapService.class);
        }
        return purapServiceImpl;
    }

    public static void setPurapServiceImpl(PurapService purapServiceImpl) {
        OleRequisitionDocument.purapServiceImpl = purapServiceImpl;
    }

    public static OlePurchaseOrderService getOlePurchaseOrderService() {
        if (olePurchaseOrderService == null) {
            olePurchaseOrderService = SpringContext.getBean(OlePurchaseOrderService.class);
        }
        return olePurchaseOrderService;
    }

    public static void setOlePurchaseOrderService(OlePurchaseOrderService olePurchaseOrderService) {
        OleRequisitionDocument.olePurchaseOrderService = olePurchaseOrderService;
    }

    public static WorkflowDocumentService getWorkflowDocumentService() {
        if (workflowDocumentService == null) {
            workflowDocumentService = SpringContext.getBean(WorkflowDocumentService.class);
        }
        return workflowDocumentService;
    }

    public static void setWorkflowDocumentService(WorkflowDocumentService workflowDocumentService) {
        OleRequisitionDocument.workflowDocumentService = workflowDocumentService;
    }

    @Override
    public ParameterService getParameterService() {
        if (parameterService == null) {
            parameterService = SpringContext.getBean(ParameterService.class);
        }
        return parameterService;
    }

    public static void setParameterService(ParameterService parameterService) {
        OleRequisitionDocument.parameterService = parameterService;
    }

    public static RequisitionService getRequisitionService() {
        if (RequisitionService == null) {
            RequisitionService = SpringContext.getBean(RequisitionService.class);
        }
        return RequisitionService;
    }

    public static void setRequisitionService(RequisitionService RequisitionService) {
        OleRequisitionDocument.RequisitionService = RequisitionService;
    }

    public static ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    public static void setConfigurationService(ConfigurationService kualiConfigurationService) {
        OleRequisitionDocument.kualiConfigurationService = kualiConfigurationService;
    }

    public static BibInfoWrapperService getBibInfoWrapperService() {
        if (bibInfoWrapperService == null) {
            bibInfoWrapperService = SpringContext.getBean(BibInfoWrapperService.class);
        }
        return bibInfoWrapperService;
    }

    public static void setBibInfoWrapperService(BibInfoWrapperService bibInfoWrapperService) {
        OleRequisitionDocument.bibInfoWrapperService = bibInfoWrapperService;
    }

    public static OleRequestSourceService getOleRequestSourceService() {
        if (oleRequestSourceService == null) {
            oleRequestSourceService = SpringContext.getBean(OleRequestSourceService.class);
        }
        return oleRequestSourceService;
    }

    public static void setOleRequestSourceService(OleRequestSourceService oleRequestSourceService) {
        OleRequisitionDocument.oleRequestSourceService = oleRequestSourceService;
    }


    public static PurchaseOrderTypeService getPurchaseOrderTypeService() {
        if (purchaseOrderTypeService == null) {
            purchaseOrderTypeService = SpringContext.getBean(PurchaseOrderTypeService.class);
        }
        return purchaseOrderTypeService;
    }

    public static void setPurchaseOrderTypeService(PurchaseOrderTypeService purchaseOrderTypeService) {
        OleRequisitionDocument.purchaseOrderTypeService = purchaseOrderTypeService;
    }


    public static FileProcessingService getFileProcessingService() {
        if (fileProcessingService == null) {
            fileProcessingService = SpringContext.getBean(FileProcessingService.class);
        }
        return fileProcessingService;
    }

    public static void setFileProcessingService(FileProcessingService fileProcessingService) {
        OleRequisitionDocument.fileProcessingService = fileProcessingService;
    }

    public static DateTimeService getDateTimeService() {
        if (dateTimeService == null) {
            dateTimeService = SpringContext.getBean(DateTimeService.class);
        }
        return dateTimeService;
    }

    public static void setDateTimeService(DateTimeService dateTimeService) {
        OleRequisitionDocument.dateTimeService = dateTimeService;
    }

    @Override
    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public static BibInfoService getBibInfoService() {
        if (bibInfoService == null) {
            bibInfoService = SpringContext.getBean(BibInfoServiceImpl.class);
        }
        return bibInfoService;
    }

    public static void setBibInfoService(BibInfoService bibInfoService) {
        OleRequisitionDocument.bibInfoService = bibInfoService;
    }

    public static OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public static void setOlePurapService(OlePurapService olePurapService) {
        OleRequisitionDocument.olePurapService = olePurapService;
    }

    public static OlePatronDocumentList getOlePatronDocumentList() {
        if (olePatronDocumentList == null) {
            olePatronDocumentList = SpringContext.getBean(OlePatronDocumentList.class);
        }
        return olePatronDocumentList;
    }

    public static void setOlePatronDocumentList(OlePatronDocumentList olePatronDocumentList) {
        OleRequisitionDocument.olePatronDocumentList = olePatronDocumentList;
    }

    public static OleCopyHelperService getOleCopyHelperService() {
        if (oleCopyHelperService == null) {
            oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
        }
        return oleCopyHelperService;
    }

    public static void setOleCopyHelperService(OleCopyHelperService oleCopyHelperService) {
        OleRequisitionDocument.oleCopyHelperService = oleCopyHelperService;
    }

    @Override
    public void prepareForSave() {
        try {
            LOG.debug("###########Inside OleRequisitionDocument prepareForSave###########");
            if (this.getRequisitionSourceCode() == null) {
                this.setRequisitionSourceCode(OleSelectConstant.REQUISITON_SRC_TYPE_DIRECTINPUT);
            }
            List<OleRequisitionItem> items = this.getItems();
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                LOG.debug("###########inside prepareForSave ole requisition item###########");
                OleRequisitionItem singleItem = (OleRequisitionItem) iterator.next();
                if(singleItem.getItemTypeCode().equals(org.kuali.ole.OLEConstants.ITM_TYP_CODE)) {
                    KRADServiceLocator.getBusinessObjectService().delete(singleItem.getDeletedCopiesList());
                    setItemDetailWhilePrepareForSave(singleItem);
                    setDocumentHeaderDescription(singleItem);
                    //Creatbib method is executed when the order is through Preorderservice
                    if (this.getRequisitionSourceCode().equalsIgnoreCase(OleSelectConstant.REQUISITON_SRC_TYPE_WEBFORM)) {
                        createBib(singleItem);
                    }
                }
            }
        } catch (DocStoreConnectionException dsce) {
            GlobalVariables.getMessageMap().putError("error.requisition.docstore.connectionError", RiceKeyConstants.ERROR_CUSTOM, "Error while connecting to document storage server, contact system administrator.");
        } catch (Exception e) {
            LOG.error("Exception during prepareForSave() in OleRequisitionDocument", e);
            GlobalVariables.getMessageMap().putError("error.requisition.docstore.connectionError", RiceKeyConstants.ERROR_CUSTOM, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to set the item while prepareforsave method is executed
     *
     * @param singleItem
     */
    private void setItemDetailWhilePrepareForSave(OleRequisitionItem singleItem) {
        String requisitionSourceCode = this.getRequisitionSourceCode();
        Integer requestSourceTypeId = singleItem.getRequestSourceTypeId();
        String internalRequestorId = singleItem.getInternalRequestorId();
        Integer requestorTypeId = singleItem.getRequestorTypeId();

        //TODO: Why are we checking for this?
//        if (!requisitionSourceCode.equalsIgnoreCase(
//                OleSelectConstant.REQUISITON_SRC_TYPE_DIRECTINPUT) && !requisitionSourceCode.equalsIgnoreCase(
//                OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST)) {
//            singleItem.setVendorItemPoNumber(this.vendorPoNumber);
//        }

        //TODO: Why are we checking for null?
        //if (requestSourceTypeId == null) {
        setRequestSourceTypeId(singleItem);
        //TODO: Why are we checking this again?
        //if (requisitionSourceCode.equalsIgnoreCase(OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST)) {
        if (singleItem.getCopyList() == null || singleItem.getCopyList().size() == 0) {
            if(!this.getIsSaved()){
                singleItem.setCopyList(getCopyList(singleItem));
            }
        }
        //}
        if (singleItem.getBibInfoBean() != null) {
            singleItem.getBibInfoBean().setRequestSource(OleSelectConstant.REQUEST_SRC_TYPE_STAFF);
        }
        // }

        if (!StringUtils.isEmpty(internalRequestorId) && requestorTypeId == null) {
            singleItem.setRequestorTypeId(getOlePurapService().getRequestorTypeId(OleSelectConstant.REQUESTOR_TYPE_STAFF));
        }
    }

    /**
     * This method is used to creat bib for the given Bib information
     *
     * @param singleItem
     */
    private void createBib(OleRequisitionItem singleItem) {
        LOG.debug("### Inside createBib() on OleRequisitionDocument ###");
        try {
            if (singleItem.getBibInfoBean() != null && singleItem.getBibInfoBean().getTitle() != null && singleItem.getItemTitleId() == null) {
                List<BibMarcRecord> bibMarcRecordList = new ArrayList<>();
                BibMarcRecords bibMarcRecords = new BibMarcRecords();
                BibMarcRecord bibMarcRecord = new BibMarcRecord();
                getOlePurapService().setBibMarcRecord(bibMarcRecord, singleItem.getBibInfoBean());
                bibMarcRecordList.add(bibMarcRecord);
                bibMarcRecords.setRecords(bibMarcRecordList);
                BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
                Bib newBib = new BibMarc();
                newBib.setCategory(DocCategory.WORK.getCode());
                newBib.setType(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode());
                newBib.setFormat(DocFormat.MARC.getCode());
                newBib.setContent(bibMarcRecordProcessor.toXml(bibMarcRecords));
                getDocstoreClientLocator().getDocstoreClient().createBib(newBib);
                newBib.deserializeContent(newBib);
                if (newBib.getId() != null) {
                    singleItem.setItemTitleId(newBib.getId());
                    singleItem.getBibInfoBean().setTitleId(newBib.getId());
                    singleItem.setItemDescription(getOlePurapService().getItemDescription(newBib));
                    //setItemDescription(singleItem, newBib);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while creating bib for Preorderservice in OleRequisitionDocument class", e);
        }
    }

    /**
     * This method is used to set the item description on item for the given Bib
     *
     * @param oleRequisitionItem
     * @param newBib
     */
/*    private void setItemDescription(OleRequisitionItem oleRequisitionItem, Bib newBib){
        LOG.debug("### Inside setItemDescription() of OleRequisitionDocument ###");
        String itemDescription = ((newBib.getTitle() != null && !newBib.getTitle().isEmpty()) ? newBib.getTitle() + "," : "") + ((newBib.getAuthor() != null && !newBib.getAuthor().isEmpty()) ? newBib.getAuthor() + "," : "") + ((newBib.getPublisher() != null && !newBib.getPublisher().isEmpty()) ? newBib.getPublisher() + "," : "") + ((newBib.getIsbn() != null && !newBib.getIsbn().isEmpty()) ? newBib.getIsbn() + "," : "");
        itemDescription = itemDescription.substring(0, itemDescription.lastIndexOf(","));
        if(LOG.isDebugEnabled()){
            LOG.debug("Item Description---------->"+itemDescription);
        }
        StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
        itemDescription = stringEscapeUtils.unescapeHtml(itemDescription);
        oleRequisitionItem.setItemDescription(itemDescription);
    }*/
    private void setRequestSourceTypeId(OleRequisitionItem singleItem) {
        try {
            if (LOG.isInfoEnabled()) {
                LOG.info("RequisitionSourceCode---->" + this.getRequisitionSourceCode());
            }
            if (this.getRequisitionSourceCode().equalsIgnoreCase(OleSelectConstant.REQUISITON_SRC_TYPE_DIRECTINPUT)) {
                singleItem.setRequestSourceTypeId(getOleRequestSourceService().getRequestSourceTypeId(OleSelectConstant.REQUEST_SRC_TYPE_STAFF));
            } else if (this.getRequisitionSourceCode().equalsIgnoreCase(OleSelectConstant.REQUISITON_SRC_TYPE_WEBFORM)) {
                singleItem.setRequestSourceTypeId(getOleRequestSourceService().getRequestSourceTypeId(OleSelectConstant.REQUEST_SRC_TYPE_WEBFORM));
            } else {
                Integer requestSourceTypeId = singleItem.getRequestSourceTypeId();
                if (null != requestSourceTypeId) {
                    singleItem.setRequestSourceTypeId(requestSourceTypeId);
                } else {
                    singleItem.setRequestSourceTypeId(getOleRequestSourceService().getRequestSourceTypeId(OleSelectConstant.REQUEST_SRC_TYPE_BATCHINGEST));
                }
            }
        } catch (Exception e) {
            LOG.error("Error while setting RequestSourceTypeId");
        }
    }

    /**
     * This method is used to get the copies list for the given line item
     *
     * @param singleItem
     * @return
     */
    private List<OleCopy> getCopyList(OleRequisitionItem singleItem) {
        LOG.debug("### Inside getCopyList() of OleRequisitionDocument ###");
        List<OleCopies> itemCopies = new ArrayList<>();
        OleRequisitionCopies oleRequisitionCopies = new OleRequisitionCopies();
        oleRequisitionCopies.setItemCopies(singleItem.getItemQuantity());
        oleRequisitionCopies.setParts(singleItem.getItemNoOfParts());
        oleRequisitionCopies.setLocationCopies(singleItem.getItemLocation());
        if (singleItem.getOleOrderRecord() != null && singleItem.getOleOrderRecord().getOleTxRecord() != null) {
            oleRequisitionCopies.setCaption(singleItem.getOleOrderRecord().getOleTxRecord().getCaption());
            if (singleItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 1; i <= singleItem.getItemNoOfParts().intValue(); i++) {
                    stringBuffer.append(i + ",");
                }
                oleRequisitionCopies.setVolumeNumber(stringBuffer.toString());
            } else {
                oleRequisitionCopies.setVolumeNumber(singleItem.getOleOrderRecord().getOleTxRecord().getVolumeNumber());
            }
        }
        oleRequisitionCopies.setSingleCopyNumber(singleItem.getSingleCopyNumber()!=null?new KualiInteger(singleItem.getSingleCopyNumber()):null);
        itemCopies.add(oleRequisitionCopies);
        List<OleCopy> oleCopyList = getOleCopyHelperService().setCopyValuesForList(itemCopies, singleItem.getItemTitleId(), singleItem.getBibTree(),singleItem.getOleERSIdentifier());
        if (oleCopyList != null && oleCopyList.size() > 0) {
            if (oleCopyList.size() == 1) {
                OleCopy oleCopy = oleCopyList.get(0);
                singleItem.setItemLocation(oleCopy.getLocation());
            } else {
                singleItem.setItemLocation(OLEConstants.MULTIPLE_ITEM_LOC);
            }
        }
        return oleCopyList;
    }

    private void setDocumentHeaderDescription(OleRequisitionItem singleItem) throws Exception {
        LOG.debug("### Inside setDocumentHeaderDescription of OleRequisitionDocument ###");
        if (this.getDocumentHeader().getDocumentDescription() == null || StringUtils.isEmpty(this.getDocumentHeader().getDocumentDescription())) {
            if (this.getRequisitionSourceCode().equalsIgnoreCase(OleSelectConstant.REQUISITON_SRC_TYPE_DIRECTINPUT)) {
                String operatorInitials = getOlePurapService().getOperatorInitials();
                this.getDocumentHeader().setDocumentDescription(OleSelectConstant.STAFF_REQUEST + (StringUtils.isEmpty(operatorInitials) ? "" : "_" + operatorInitials) + "_" + getCurrentDateTime());
            } else if (this.getRequisitionSourceCode().equalsIgnoreCase(OleSelectConstant.REQUISITON_SRC_TYPE_WEBFORM)) {
                String operatorInitials = getOlePurapService().getOperatorInitials();
                this.getDocumentHeader().setDocumentDescription(OleSelectConstant.WEBFORM_REQUEST + (StringUtils.isEmpty(operatorInitials) ? "" : "_" + operatorInitials) + "_" + getCurrentDateTime());
            } else if (this.getRequisitionSourceCode().equalsIgnoreCase(OleSelectConstant.REQUISITON_SRC_TYPE_MANUALINGEST)) {
                String operatorInitials = getOlePurapService().getOperatorInitials();
                this.getDocumentHeader().setDocumentDescription(OLEConstants.MANUAL_INGEST_DOCUMENT_DESCRIPTION + (StringUtils.isEmpty(operatorInitials) ? "" : "_" + operatorInitials) + "_" + getCurrentDateTime() + (StringUtils.isEmpty(singleItem.getBibInfoBean().getYbp()) ? "" : "_" + singleItem.getBibInfoBean().getYbp()));
            }
        }
    }

    //TODO: Refactor to call the docsearch api to get the title, author, isbn etc information for the description field
    @Override
    public void processAfterRetrieve() {
        //  super.processAfterRetrieve();
        try {
            LOG.debug("###########inside OleRequisitionDocument processAfterRetrieve###########");
            PurchaseOrderType purchaseOrderTypeDoc = getOlePurapService().getPurchaseOrderType(this.getPurchaseOrderTypeId());
            if (purchaseOrderTypeDoc != null) {
                this.setOrderType(purchaseOrderTypeDoc);
            }
            if (this.getVendorAliasName() == null) {
                populateVendorAliasName();
            }
            List<OleRequisitionItem> items = this.getItems();
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                LOG.debug("###Inside while loop of processAfterRetrive()###");
                OleRequisitionItem singleItem = (OleRequisitionItem) iterator.next();
                if(singleItem.getItemTypeCode().equals(org.kuali.ole.OLEConstants.ITM_TYP_CODE)) {
                    setItemDetailWhileProcessAfterRetrive(singleItem);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in during processAfterRetrieve for OleRequisitionDocument " + e);
            throw new RuntimeException(e);
        }
    }


    /**
     * This method is used to set the item detail while processafterretrive method is executed
     *
     * @param singleItem
     */
    private void setItemDetailWhileProcessAfterRetrive(OleRequisitionItem singleItem) {
        try {
            if (LOG.isInfoEnabled()) {
                LOG.info("Title id while retriving in REQ------>" + singleItem.getItemTitleId());
            }
            // Added for jira OLE-2811 starts
            //Modified for jiar OLE-6032
            if (StringUtils.isNotEmpty(singleItem.getRequestorId())) {
                singleItem.setRequestorFirstName(getOlePurapService().getPatronName(singleItem.getRequestorId()));
            }
            if (singleItem.getItemUnitPrice() != null) {
                singleItem.setItemUnitPrice(singleItem.getItemUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            // Added for jira OLE-2811 ends
            if (singleItem.getItemTitleId() != null) {
                LOG.debug("###########inside processAfterRetrieve ole requisition item###########");
                Bib bib = new BibMarc();
                if (singleItem.getItemTitleId() != null && singleItem.getItemTitleId() != "") {
                    bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(singleItem.getItemTitleId());
                    singleItem.setBibUUID(bib.getId());
                }
                singleItem.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(singleItem.getItemTitleId()));
                singleItem.setBibInfoBean(new BibInfoBean());
                singleItem.setItemDescription(getOlePurapService().getItemDescription(bib));
            }
            if (singleItem.getItemType() != null && singleItem.getItemType().isQuantityBasedGeneralLedgerIndicator()) {
                populateCopiesSection(singleItem);
            }
            if (singleItem.getCopyList().size() > 0) {
                getOlePurapService().setInvoiceDocumentsForRequisition(singleItem);
            }
            if (singleItem.getClaimDate() == null) {
                getOlePurapService().setClaimDateForReq(singleItem, this.vendorDetail);
            }
        } catch (Exception e) {
            LOG.error("Error while setting the requisition item detail.", e);
        }
    }

    /**
     * This method is used to populated the copies section details
     *
     * @param singleItem
     */
    private void populateCopiesSection(OleRequisitionItem singleItem) {
        if (singleItem.getCopies().size() > 0) {
            List<OleCopy> copyList = getOleCopyHelperService().setCopyValuesForList(singleItem.getCopies(), singleItem.getItemTitleId(), null, null);
            if (copyList.size() >= singleItem.getCopyList().size()) {
                int copyCount = 0;
                for (OleCopy oleCopy : singleItem.getCopyList()) {
                    OleCopy copy = copyList.get(copyCount);
                    oleCopy.setLocation(copy.getLocation());
                    //oleCopy.setEnumeration(copy.getEnumeration());
                    //oleCopy.setCopyNumber(copy.getCopyNumber());
                    oleCopy.setPartNumber(copy.getPartNumber());
                    oleCopy.setReqDocNum(this.getPurapDocumentIdentifier());
                    copyCount++;
                }
                for (int i = copyCount; i < copyList.size(); i++) {
                    singleItem.getCopyList().add(copyList.get(copyCount));
                    copyCount++;
                }
            }
        } else if (singleItem.getCopyList() != null && singleItem.getCopyList().size() == 1) {
            singleItem.getCopyList().get(0).setReqDocNum(this.getPurapDocumentIdentifier());
        } else {
            if (singleItem.getItemQuantity() != null && singleItem.getItemNoOfParts() != null && (singleItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                    || singleItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1)))) {
                singleItem.setCopies(getOleCopyHelperService().setCopiesToLineItem(singleItem.getCopyList(), singleItem.getItemNoOfParts(), singleItem.getItemTitleId()));
            }
        }
        if (singleItem.getItemQuantity() != null && singleItem.getItemNoOfParts() != null && !singleItem.getItemQuantity().isGreaterThan(new KualiDecimal(1))
                && !singleItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1)) && singleItem.getCopyList().size() > 0) {
            singleItem.setSingleCopyNumber(singleItem.getCopyList().get(0).getCopyNumber());
        }
    }

    @Override
    public Class getItemClass() {
        // TODO Auto-generated method stub
        return OleRequisitionItem.class;
    }

    /**
     * Returns current Date in MMddyyHHmm format to be appended in the document description
     *
     * @return current getCurrentDateTime in String
     */
    public String getCurrentDateTime() {
        LOG.debug("Inside getCurrentDateTime()");
        Date date = getDateTimeService().getCurrentDate();
        String currentDate = SpringContext.getBean(DateTimeService.class).toString(date, timeStampFormat);
        LOG.debug("End of getCurrentDateTime()");
        return currentDate;
    }

    /**
     * Prepopulates Requisition Document description to Library Material_operatorinitials_datetime
     *
     * @return void
     */
    @Override
    public void initiateDocument() throws WorkflowException {
        String description = getOlePurapService().getParameter(OLEConstants.REQ_DESC);
        description = getOlePurapService().setDocumentDescription(description, null);
        this.getDocumentHeader().setDocumentDescription(description);
        this.setPurchaseOrderTypeId(getParameterService().getParameterValueAsString(RequisitionDocument.class, PurapParameterConstants.DEFAULT_ORDER_TYPE));
        /*this.setLicensingRequirementCode(getParameterService().getParameterValueAsString(RequisitionDocument.class, PurapParameterConstants.DEFAULT_LICENSE_STATUS));*/
        super.initiateDocument();
        setDeliveryDetail(this);
        this.setPurchaseOrderTransmissionMethodCode(OleSelectConstant.METHOD_OF_PO_TRANSMISSION_NOPR);
        // populating the initialCollapseSections
        getOlePurapService().getInitialCollapseSections(this);
    }

    public boolean getIsFinalReqs() {
        if (this.getDocumentHeader().getWorkflowDocument().isFinal()) {
            return true;
        }
        return false;
    }

    private void setDeliveryDetail(OleRequisitionDocument document) {
        Person currentUser = GlobalVariables.getUserSession().getPerson();
        DefaultPrincipalAddress defaultPrincipalAddress = new DefaultPrincipalAddress(currentUser.getPrincipalId());
        Map addressKeys = getPersistenceService().getPrimaryKeyFieldValues(defaultPrincipalAddress);
        defaultPrincipalAddress = getBusinessObjectService().findByPrimaryKey(DefaultPrincipalAddress.class, addressKeys);
        boolean isDefaultAddress = false;
        if (ObjectUtils.isNotNull(defaultPrincipalAddress) && ObjectUtils.isNotNull(defaultPrincipalAddress.getBuilding())) {
            isDefaultAddress = defaultPrincipalAddress.getBuilding().isActive();
        }
        if (!isDefaultAddress) {
            Person personImpl = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPrincipalId());
            //Modified as per review comments OLE-24
            String defaultRoomNumber = getParameter("DELIVERY_DEFAULT_ROOMNUMBER");
            if (defaultRoomNumber != null) {
                defaultRoomNumber = defaultRoomNumber.trim();
            }
            document.setDeliveryBuildingLine1Address(personImpl.getAddressLine1());
            document.setDeliveryBuildingLine2Address(personImpl.getAddressLine2());
            //Modified as per review comments OLE-24
            if (document.getDeliveryBuildingLine1Address() != null && !(document.getDeliveryBuildingLine1Address().trim().equals(""))) {
                document.setDeliveryBuildingRoomNumber(defaultRoomNumber);
            }
            document.setDeliveryCityName(personImpl.getAddressCity());
            document.setDeliveryStateCode(personImpl.getAddressStateProvinceCode());
            document.setDeliveryPostalCode(personImpl.getAddressPostalCode());
            document.setDeliveryToName(personImpl.getName());
            document.setDeliveryCountryCode(personImpl.getAddressCountryCode());

        }

    }

    /**
     * Returns current Date in MMddyy(100311) format to be appended in the document description
     *
     * @return current DateTime in String
     */
    public String getCurrentDate() {
        LOG.debug("Inside getCurrentDate()");
        //Modified as per review comments OLE-24
        Date date = getDateTimeService().getCurrentDate();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String currentDate = sdf.format(date);
        LOG.debug("End of getCurrentDate()");
        return currentDate;
    }

    /**
     * @see org.kuali.rice.krad.document.DocumentBase#doRouteStatusChange()
     */
    @Override
    public void doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) {
        LOG.debug("doRouteStatusChange() started");
        super.doRouteStatusChange(statusChangeEvent);
        try {
            if (this.getDocumentHeader().getWorkflowDocument().isProcessed()) {
                String newRequisitionStatus = PurapConstants.RequisitionStatuses.APPDOC_IN_PROCESS;
                if (SpringContext.getBean(RequisitionService.class).isAutomaticPurchaseOrderAllowed(this)) {
                    newRequisitionStatus = PurapConstants.RequisitionStatuses.APPDOC_CLOSED;
                    if (this.getDocumentHeader().getDocumentNumber() == null) {
                        this.setDocumentHeader((SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(this.getDocumentNumber())));
                        SpringContext.getBean(OlePurchaseOrderService.class).createAutomaticPurchaseOrderDocument(this);
                    } else {
                        SpringContext.getBean(OlePurchaseOrderService.class).createAutomaticPurchaseOrderDocument(this);
                    }
                }
                this.setApplicationDocumentStatus(newRequisitionStatus);
            }
            // DOCUMENT DISAPPROVED
            else if (this.getDocumentHeader().getWorkflowDocument().isDisapproved()) {
                String nodeName = getDocumentHeader().getWorkflowDocument().getCurrentNodeNames().iterator().next();
                //NodeDetails currentNode = NodeDetailEnum.getNodeDetailEnumByName(nodeName);
                HashMap<String, String> disApprovedStatusMap = PurapConstants.RequisitionStatuses.getRequistionAppDocStatuses();
                if (ObjectUtils.isNotNull(nodeName)) {
                    if (StringUtils.isNotBlank(disApprovedStatusMap.get(nodeName))) {
                        this.setApplicationDocumentStatus(disApprovedStatusMap.get(nodeName));
                        //   updateStatusAndSave(disApprovedStatusMap.get(nodeName));
                        return;
                    }
                }
                logAndThrowRuntimeException("No status found to set for document being disapproved in node '" + nodeName + "'");
            }
            // DOCUMENT CANCELED
            else if (this.getDocumentHeader().getWorkflowDocument().isCanceled()) {
                this.setApplicationDocumentStatus(RequisitionStatuses.APPDOC_CANCELLED);
            }
        } catch (Exception e) {
            logAndThrowRuntimeException("Error saving routing data while saving document with id " + getDocumentNumber(), e);
        }
        LOG.debug("doRouteStatusChange() ending");
    }

    /**
     * @see org.kuali.rice.kns.document.DocumentBase#handleRouteLevelChange(org.kuali.rice.kew.clientapp.vo.DocumentRouteLevelChangeDTO)
     */
    @Override
    public void doRouteLevelChange(DocumentRouteLevelChange change) {
        LOG.debug("handleRouteLevelChange() started");
        super.doRouteLevelChange(change);
        try {
            String newNodeName = change.getNewNodeName();
            if (StringUtils.isNotBlank(newNodeName)) {
                RoutingReportCriteria.Builder reportCriteria = RoutingReportCriteria.Builder
                        .createByDocumentIdAndTargetNodeName(getDocumentNumber(), newNodeName);
                List<String> desiredActions = new ArrayList<String>(2);
                desiredActions.add(ActionRequestType.APPROVE.getCode());
                desiredActions.add(ActionRequestType.COMPLETE.getCode());
                String note = "";
                if (newNodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE)
                        || newNodeName.equalsIgnoreCase(PurapWorkflowConstants.FYI_BUDGET)) {
                    if (newNodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE)) {
                        note = OLEConstants.SufficientFundCheck.REQ_NOTE;
                    }
                    if (newNodeName.equalsIgnoreCase(PurapWorkflowConstants.FYI_BUDGET)) {
                        note = OLEConstants.SufficientFundCheck.FYI_NOTE;
                    }
                    DocumentService documentService = SpringContext.getBean(DocumentService.class);
                    Note apoNote = documentService.createNoteFromDocument(this, note);
                    this.addNote(apoNote);
                    documentService.saveDocumentNotes(this);

                }
            }
        } catch (Exception e) {
            String errorMsg = "Workflow Error found checking actions requests on document with id "
                    + getDocumentNumber() + ". *** WILL NOT UPDATE PURAP STATUS ***";
            LOG.error(errorMsg, e);
        }
    }

    /**
     * This method is used to get the bibedtior creat url from propertie file
     *
     * @return Bibeditor creat url string
     */
    public String getBibeditorCreateURL() {
        String bibeditorCreateURL = getConfigurationService().getPropertyValueAsString(OLEConstants.BIBEDITOR_CREATE_URL_KEY);
        return bibeditorCreateURL;
    }

    public String getBibSearchURL() {
        String bibSearchURL = getConfigurationService().getPropertyValueAsString(OLEConstants.BIBEDITOR_SEARCH_URL_KEY);
        return bibSearchURL;
    }

    /**
     * This method is used to get the dublinedtior edit url from propertie file
     *
     * @return Dublineditor edit url string
     */
    public String getDublinEditorEditURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getDublinEditorEditURL();
    }

    /**
     * This method is used to get the bibedtior edit url from propertie file
     *
     * @return Bibeditor edit url string
     */
    public String getBibeditorEditURL() {
        String bibeditorEditURL = getConfigurationService().getPropertyValueAsString(OLEConstants.BIBEDITOR_URL_KEY);
        return bibeditorEditURL;
    }

    /**
     * This method is used to get the Instanceeditor url from propertie file
     *
     * @return Instanceeditor url string
     */
    public String getInstanceEditorURL() {
        String instanceEditorURL = getConfigurationService().getPropertyValueAsString(
                OLEConstants.INSTANCEEDITOR_URL_KEY);
        return instanceEditorURL;
    }

    /**
     * This method is used to get the dublinedtior view url from propertie file
     *
     * @return dublineditor view url string
     */
    public String getDublinEditorViewURL() {
        return SpringContext.getBean(OlePurchaseOrderDocumentHelperService.class).getDublinEditorViewURL();
    }

    /**
     * This method is used to get the bibedtior view url from propertie file
     *
     * @return Bibeditor view url string
     */
    public String getBibeditorViewURL() {
        String bibeditorViewURL = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSTORE_APP_URL_KEY);
        return bibeditorViewURL;
    }

    /**
     * This method is used to get the directory path where the marc xml files need to be created
     *
     * @return Directory path string
     */
    public String getMarcXMLFileDirLocation() throws Exception {
        String externaleDirectory = getFileProcessingService().getMarcXMLFileDirLocation();
        return externaleDirectory;
    }

    public boolean getIsSplitPO() {
        LOG.debug("Inside getIsSplitPO of OleRequisitionDocument");
        if (this.getDocumentHeader().getWorkflowDocument().getDocumentTypeName().equalsIgnoreCase(OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_SPLIT) && this.getDocumentHeader().getWorkflowDocument().isSaved()) {
            return true;
        }
        return false;
    }

    public boolean getIsReOpenPO() {
        LOG.debug("Inside getIsReOpenPO of OleRequisitionDocument");
        if (this.getDocumentHeader().getWorkflowDocument().getDocumentTypeName().equalsIgnoreCase(OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER_REOPEN) && this.getDocumentHeader().getWorkflowDocument().isSaved()) {
            return true;
        }
        return false;
    }

    public String getVendorPoNumber() {
        return vendorPoNumber;
    }

    public void setVendorPoNumber(String vendorPoNumber) {
        this.vendorPoNumber = vendorPoNumber;
    }


    @Override
    public void customPrepareForSave(KualiDocumentEvent event) {
        // Need this here so that it happens before the GL work is done
        getOlePurapAccountingService().updateAccountAmounts(this);

        if (event instanceof RouteDocumentEvent || event instanceof ApproveDocumentEvent) {
            if (purchasingAccountsPayableDocument instanceof VendorCreditMemoDocument && ((VendorCreditMemoDocument) purchasingAccountsPayableDocument).isSourceVendor()) {
                return;
            }
            getPurapServiceImpl().calculateTax(this);
        }
    }

    /**
     * This method is used to check the status of the document for displaying view and edit buttons in line item
     *
     * @return boolean
     */
    public boolean getIsSaved() {
        if (this.getDocumentHeader().getWorkflowDocument().isSaved() || this.getDocumentHeader().getWorkflowDocument().isInitiated()) {
            return true;
        }
        return false;
    }

    @Override
    public String getLicenseURL() {
        return ConfigContext.getCurrentContextConfig().getProperty("license.web.service.url");
    }


    public boolean isBudgetReviewRequired() {
        OleRequisitionDocumentService oleRequisitionDocumentService = (OleRequisitionDocumentService) SpringContext
                .getBean("oleRequisitionDocumentService");
        List<SourceAccountingLine> sourceAccountingLineList = this.getSourceAccountingLines();
        boolean sufficientFundCheck = false;
        for (SourceAccountingLine accLine : sourceAccountingLineList) {
            String notificationOption = null;
            Map<String, Object> key = new HashMap<String, Object>();
            String chartCode = accLine.getChartOfAccountsCode();
            String accNo = accLine.getAccountNumber();
            key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
            key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
            OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                    OleSufficientFundCheck.class, key);
            if (account != null) {
                notificationOption = account.getNotificationOption();
            }
            if (notificationOption != null
                    && (notificationOption.equals(OLEPropertyConstants.BUD_REVIEW))) {
                sufficientFundCheck = oleRequisitionDocumentService.hasSufficientFundsOnRequisition(accLine, notificationOption, SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear());
                if (sufficientFundCheck) {
                    return sufficientFundCheck;
                }

            }
        }
        return sufficientFundCheck;
    }

    private boolean isNotificationRequired() {
        OleRequisitionDocumentService oleRequisitionDocumentService = (OleRequisitionDocumentService) SpringContext
                .getBean("oleRequisitionDocumentService");
        List<SourceAccountingLine> sourceAccountingLineList = this.getSourceAccountingLines();
        boolean sufficientFundCheck = false;
        for (SourceAccountingLine accLine : sourceAccountingLineList) {
            String notificationOption = null;
            Map<String, Object> key = new HashMap<String, Object>();
            String chartCode = accLine.getChartOfAccountsCode();
            String accNo = accLine.getAccountNumber();
            key.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
            key.put(OLEPropertyConstants.ACCOUNT_NUMBER, accNo);
            OleSufficientFundCheck account = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(
                    OleSufficientFundCheck.class, key);
            if (account != null) {
                notificationOption = account.getNotificationOption();
            }
            if (notificationOption != null && notificationOption.equals(OLEPropertyConstants.NOTIFICATION)) {
                sufficientFundCheck = oleRequisitionDocumentService.hasSufficientFundsOnRequisition(accLine, notificationOption, SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear());
                if (sufficientFundCheck) {
                    return sufficientFundCheck;
                }
            }
        }
        return sufficientFundCheck;
    }

    @Override
    protected void populateAccountsForRouting() {

        List<SufficientFundsItem> fundsItems = new ArrayList<SufficientFundsItem>();
        try {
            String nodeName = getFinancialSystemDocumentHeader().getWorkflowDocument().getCurrentNodeNames().iterator()
                    .next();
            if (nodeName != null
                    && (nodeName.equalsIgnoreCase(PurapWorkflowConstants.BUDGET_NODE) || nodeName
                    .equalsIgnoreCase(PurapWorkflowConstants.BUDGET_REVIEW_REQUIRED))) {
               /* if (SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear()
                        .compareTo(getPostingYear()) >= 0) {
                    List<GeneralLedgerPendingEntry> pendingEntries = getPendingLedgerEntriesForSufficientFundsChecking();
                    for (GeneralLedgerPendingEntry glpe : pendingEntries) {
                        glpe.getChartOfAccountsCode();
                    }
                    SpringContext.getBean(GeneralLedgerPendingEntryService.class).delete(getDocumentNumber());
                    fundsItems = SpringContext.getBean(SufficientFundsService.class).checkSufficientFunds(
                            pendingEntries);
                    SpringContext.getBean(GeneralLedgerPendingEntryService.class).generateGeneralLedgerPendingEntries(
                            this);
                    SpringContext.getBean(BusinessObjectService.class).save(getGeneralLedgerPendingEntries());
                }*/
                SpringContext.getBean(PurapAccountingService.class).updateAccountAmounts(this);
                accountsForRouting = (SpringContext.getBean(PurapAccountingService.class).generateSummary(getItems()));
               /* List<String> fundsItemList = new ArrayList<String>();
                for (SufficientFundsItem fundsItem : fundsItems) {
                    fundsItemList.add(fundsItem.getAccount().getChartOfAccountsCode());
                }*/
                setAccountsForRouting(accountsForRouting);
                refreshNonUpdateableReferences();
                for (SourceAccountingLine sourceLine : getAccountsForRouting()) {
                    sourceLine.refreshNonUpdateableReferences();
                }
            } else {
                super.populateAccountsForRouting();
            }
        } catch (Exception e) {
            logAndThrowRuntimeException("Error in populateAccountsForRouting while submitting document with id "
                    + getDocumentNumber(), e);
        }

    }

    private void populateVendorAliasName() {
        Map vendorDetailMap = new HashMap();
        vendorDetailMap.put(OLEConstants.VENDOR_HEADER_IDENTIFIER, this.getVendorHeaderGeneratedIdentifier());
        vendorDetailMap.put(OLEConstants.VENDOR_DETAIL_IDENTIFIER, this.getVendorDetailAssignedIdentifier());
        List<VendorAlias> vendorDetailList = (List) getBusinessObjectService().findMatching(VendorAlias.class, vendorDetailMap);
        if (vendorDetailList != null && vendorDetailList.size() > 0) {
            this.setVendorAliasName(vendorDetailList.get(0).getVendorAliasName());
        }
    }

    public boolean getIsATypeOfRCVGDoc() {
        return false;
    }

    public boolean getIsATypeOfCORRDoc() {
        return false;
    }

    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(org.kuali.ole.OLEConstants.APPL_ID, org.kuali.ole.OLEConstants.SELECT_NMSPC, org.kuali.ole.OLEConstants.SELECT_CMPNT, name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }
}
