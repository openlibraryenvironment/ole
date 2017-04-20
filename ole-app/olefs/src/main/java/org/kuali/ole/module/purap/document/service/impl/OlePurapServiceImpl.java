/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.module.purap.document.service.impl;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibInfoRecord;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.document.*;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.document.OleVendorCreditMemoDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentHeaderService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;

import java.math.BigDecimal;
import java.util.*;

public class OlePurapServiceImpl implements OlePurapService {

    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePurapServiceImpl.class);
    protected ParameterService parameterService;
    private static transient BusinessObjectService businessObjectService;
    private DateTimeService dateTimeService;
    private static final String timeStampFormat = "MMddyyHHmm";

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public DateTimeService getDateTimeService() {
        if (dateTimeService == null) {
            dateTimeService = SpringContext.getBean(DateTimeService.class);
        }
        return dateTimeService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }
    @Override
    public BigDecimal calculateDiscount(OleRequisitionItem oleRequisition) {

        if (oleRequisition.getItemListPrice() != null && oleRequisition.getItemDiscountType() != null) {

            if (oleRequisition.getItemDiscount() == null) {
                oleRequisition.setItemDiscount(new KualiDecimal(0));
            }
            if (oleRequisition.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                LOG.debug("###### Percentage ##### ");
                return oleRequisition.getItemListPrice().bigDecimalValue().subtract((oleRequisition.getItemDiscount().bigDecimalValue().multiply(new BigDecimal(0.01))).multiply(oleRequisition.getItemListPrice().bigDecimalValue()));
            } else {
                LOG.debug("###### Dollor ##### ");
                if (LOG.isDebugEnabled()) {
                    LOG.debug("oleRequisition.getItemDiscountType()**********" + oleRequisition.getItemDiscountType());
                }
                return oleRequisition.getItemListPrice().bigDecimalValue().subtract(oleRequisition.getItemDiscount().bigDecimalValue());
            }
        } else {
            oleRequisition.setItemListPrice(new KualiDecimal(0.0));
            return new BigDecimal(0.0);
        }

    }

    @Override
    public BigDecimal calculateDiscount(OlePurchaseOrderItem olePurchaseOrder) {

        if (olePurchaseOrder.getItemListPrice() != null && olePurchaseOrder.getItemDiscountType() != null) {
            if (olePurchaseOrder.getItemDiscount() == null) {
                olePurchaseOrder.setItemDiscount(new KualiDecimal(0));
            }
            if (olePurchaseOrder.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                return olePurchaseOrder.getItemListPrice().bigDecimalValue().subtract((olePurchaseOrder.getItemDiscount().bigDecimalValue().multiply(new BigDecimal(0.01))).multiply(olePurchaseOrder.getItemListPrice().bigDecimalValue()));
            } else {
                return olePurchaseOrder.getItemListPrice().bigDecimalValue().subtract(olePurchaseOrder.getItemDiscount().bigDecimalValue());
            }
        } else {
            olePurchaseOrder.setItemListPrice(new KualiDecimal(0.0));
            return new BigDecimal(0.0);
        }

    }


// Foreign Currency Conversion

    @Override
    public OleRequisitionItem calculateForeignCurrency(OleRequisitionItem oleRequisition) {
        if (oleRequisition.getItemForeignListPrice() != null && oleRequisition.getItemForeignDiscountType() != null) {
            if (oleRequisition.getItemForeignDiscount() == null) {
                oleRequisition.setItemForeignDiscount(new KualiDecimal(0));
            }
            if (oleRequisition.getItemForeignDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                BigDecimal calculatedForeignDiscountAmt = oleRequisition.getItemForeignDiscount().bigDecimalValue().multiply(new BigDecimal(0.01)).multiply(oleRequisition.getItemForeignListPrice().bigDecimalValue());
                oleRequisition.setItemForeignDiscountAmt(new KualiDecimal(calculatedForeignDiscountAmt.setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR)));
                oleRequisition.setItemForeignUnitCost(oleRequisition.getItemForeignListPrice().subtract(oleRequisition.getItemForeignDiscountAmt()));
                return oleRequisition;
            } else {
                oleRequisition.setItemForeignDiscountAmt((oleRequisition.getItemForeignDiscount()));
                oleRequisition.setItemForeignUnitCost(new KualiDecimal(oleRequisition.getItemForeignListPrice().bigDecimalValue().subtract(oleRequisition.getItemForeignDiscount().bigDecimalValue())));
                return oleRequisition;
            }
        } else {
            return oleRequisition;
        }
    }

    @Override
    public OlePurchaseOrderItem calculateForeignCurrency(OlePurchaseOrderItem olePurchaseOrder) {
        if (olePurchaseOrder.getItemForeignListPrice() != null && olePurchaseOrder.getItemForeignDiscountType() != null) {
            if (olePurchaseOrder.getItemForeignDiscount() == null) {
                olePurchaseOrder.setItemForeignDiscount(new KualiDecimal(0));
            }
            if (olePurchaseOrder.getItemForeignDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                BigDecimal calculatedForeignDiscountAmt = olePurchaseOrder.getItemForeignDiscount().bigDecimalValue().multiply(new BigDecimal(0.01)).multiply(olePurchaseOrder.getItemForeignListPrice().bigDecimalValue());
                olePurchaseOrder.setItemForeignDiscountAmt(new KualiDecimal(calculatedForeignDiscountAmt.setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR)));
                olePurchaseOrder.setItemForeignUnitCost(olePurchaseOrder.getItemForeignListPrice().subtract(olePurchaseOrder.getItemForeignDiscountAmt()));
                return olePurchaseOrder;
            } else {
                olePurchaseOrder.setItemForeignDiscountAmt(olePurchaseOrder.getItemForeignDiscount());
                olePurchaseOrder.setItemForeignUnitCost(olePurchaseOrder.getItemForeignListPrice().subtract(olePurchaseOrder.getItemForeignDiscount()));
                return olePurchaseOrder;
            }
        } else {
            return olePurchaseOrder;
        }
    }

    @Override
    public BigDecimal calculateDiscount(OlePaymentRequestItem olePaymentRequestOrder) {

        if (olePaymentRequestOrder.getItemListPrice() != null) {
            if (olePaymentRequestOrder.getItemDiscount() == null) {
                olePaymentRequestOrder.setItemDiscount(new KualiDecimal(0));
            }
            if (olePaymentRequestOrder.getItemDiscountType() != null && olePaymentRequestOrder.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                return olePaymentRequestOrder.getItemListPrice().bigDecimalValue().subtract((olePaymentRequestOrder.getItemDiscount().bigDecimalValue().multiply(new BigDecimal(0.01))).multiply(olePaymentRequestOrder.getItemListPrice().bigDecimalValue()));
            } else {
                return olePaymentRequestOrder.getItemListPrice().bigDecimalValue().subtract(olePaymentRequestOrder.getItemDiscount().bigDecimalValue());
            }
        } else {
            olePaymentRequestOrder.setItemListPrice(new KualiDecimal(0.0));
            return new BigDecimal(0.0);
        }

    }

    @Override
    public BigDecimal calculateDiscount(OleInvoiceItem oleInvoiceOrder) {

        if (oleInvoiceOrder.getItemListPrice() != null) {
            if (oleInvoiceOrder.getItemDiscount() == null) {
                oleInvoiceOrder.setItemDiscount(new KualiDecimal(0));
            }
            if (oleInvoiceOrder.getItemDiscountType() != null && oleInvoiceOrder.getItemDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                return oleInvoiceOrder.getItemListPrice().bigDecimalValue().subtract((oleInvoiceOrder.getItemDiscount().bigDecimalValue().multiply(new BigDecimal(0.01))).multiply(oleInvoiceOrder.getItemListPrice().bigDecimalValue()));
            } else {
                return oleInvoiceOrder.getItemListPrice().bigDecimalValue().subtract(oleInvoiceOrder.getItemDiscount().bigDecimalValue());
            }
        } else {
            oleInvoiceOrder.setItemListPrice(new KualiDecimal(0.0));
            return new BigDecimal(0.0);
        }

    }

    @Override
    public OlePaymentRequestItem calculateForeignCurrency(OlePaymentRequestItem item) {
        if (item.getItemForeignListPrice() != null) {
            if (item.getItemForeignDiscount() == null) {
                item.setItemForeignDiscount(new KualiDecimal(0));
            }
            if (item.getItemForeignDiscountType() != null && item.getItemForeignDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                BigDecimal calculatedForeignDiscountAmt = item.getItemForeignDiscount().bigDecimalValue().multiply(new BigDecimal(0.01)).multiply(item.getItemForeignListPrice().bigDecimalValue());
                item.setItemForeignDiscountAmt(new KualiDecimal(calculatedForeignDiscountAmt.setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR)));
                item.setItemForeignUnitCost(item.getItemForeignListPrice().subtract(item.getItemForeignDiscountAmt()));
                return item;
            } else {
                item.setItemForeignDiscountAmt(item.getItemForeignDiscount());
                item.setItemForeignUnitCost(item.getItemForeignListPrice().subtract(item.getItemForeignDiscount()));
                return item;
            }
        } else {
            return item;
        }
    }


    @Override
    public OleInvoiceItem calculateForeignCurrency(OleInvoiceItem item) {
        if (item.getItemForeignListPrice() != null) {
            if (item.getItemForeignDiscount() == null) {
                item.setItemForeignDiscount(new KualiDecimal(0));
            }
            if (item.getItemForeignDiscountType() != null && item.getItemForeignDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                BigDecimal calculatedForeignDiscountAmt = item.getItemForeignDiscount().bigDecimalValue().multiply(new BigDecimal(0.01)).multiply(item.getItemForeignListPrice().bigDecimalValue());
                item.setItemForeignDiscountAmt(new KualiDecimal(calculatedForeignDiscountAmt.setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR)));
                item.setItemForeignUnitCost(item.getItemForeignListPrice().subtract(item.getItemForeignDiscountAmt()));
                return item;
            } else {
                item.setItemForeignDiscountAmt(item.getItemForeignDiscount());
                item.setItemForeignUnitCost(item.getItemForeignListPrice().subtract(item.getItemForeignDiscount()));
                return item;
            }
        } else {
            return item;
        }
    }


    /**
     * Returns first, middle and last initials of logged in user
     * to be appended in the document description
     *
     * @return initials of logged in user
     */
    public String getOperatorInitials() {
        LOG.debug("Inside getOperatorInitials()");
        StringBuffer operatorInitials = new StringBuffer();
        Person person = GlobalVariables.getUserSession().getPerson();
        operatorInitials.append(StringUtils.isEmpty(person.getFirstName()) ? "" : person.getFirstName().toLowerCase().charAt(0));
        operatorInitials.append(StringUtils.isEmpty(person.getMiddleName()) ? "" : person.getMiddleName().toLowerCase().charAt(0));
        operatorInitials.append(StringUtils.isEmpty(person.getLastName()) ? "" : person.getLastName().toLowerCase().charAt(0));
        LOG.debug("End of getOperatorInitials()");
        return operatorInitials.toString();
    }

    public OleCreditMemoItem calculateForeignCurrency(OleCreditMemoItem oleCreditMemoItem) {
        if (oleCreditMemoItem.getItemForeignListPrice() != null && oleCreditMemoItem.getItemForeignDiscountType() != null) {
            if (oleCreditMemoItem.getItemForeignDiscount() == null) {
                oleCreditMemoItem.setItemForeignDiscount(new KualiDecimal(0));
            }
            if (oleCreditMemoItem.getItemForeignDiscountType().equalsIgnoreCase(OleSelectConstant.DISCOUNT_TYPE_PERCENTAGE)) {
                BigDecimal calculatedForeignDiscountAmt = oleCreditMemoItem.getItemForeignDiscount().bigDecimalValue().multiply(new BigDecimal(0.01)).multiply(oleCreditMemoItem.getItemForeignListPrice().bigDecimalValue());
                oleCreditMemoItem.setItemForeignDiscountAmt(new KualiDecimal(calculatedForeignDiscountAmt.setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR)));
                oleCreditMemoItem.setItemForeignUnitCost(oleCreditMemoItem.getItemForeignListPrice().subtract(oleCreditMemoItem.getItemForeignDiscountAmt()));
                return oleCreditMemoItem;
            } else {
                oleCreditMemoItem.setItemForeignDiscountAmt(oleCreditMemoItem.getItemForeignDiscount());
                oleCreditMemoItem.setItemForeignUnitCost(oleCreditMemoItem.getItemForeignListPrice().subtract(oleCreditMemoItem.getItemForeignDiscount()));
                return oleCreditMemoItem;
            }
        } else {
            return oleCreditMemoItem;
        }
    }

    /**
     * This method returns the section Name that are to be collapsed while opening the document
     * @param document
     * @return collapseSections
     */
    public void getInitialCollapseSections(PurchasingAccountsPayableDocument document) {
        LOG.debug("Inside getInitialCollapseSections()");
        String[] collapseSections = new String[]{};
        try {
            if (document instanceof RequisitionDocument) {
                OleRequisitionDocument requisitionDocument = (OleRequisitionDocument)document;
                collapseSections = parameterService.getParameterValuesAsString(RequisitionDocument.class,
                        OLEConstants.INITIAL_COLLAPSE_SECTIONS).toArray(new String[]{});
                requisitionDocument.setOverviewFlag(canCollapse(OLEConstants.OVERVIEW_SECTION, collapseSections));
                requisitionDocument.setDeliveryFlag(canCollapse(OLEConstants.DELIVERY_SECTION, collapseSections));
                requisitionDocument.setVendorFlag(canCollapse(OLEConstants.VENDOR_SECTION, collapseSections));
                requisitionDocument.setTitlesFlag(canCollapse(OLEConstants.TITLES_SECTION, collapseSections));
                requisitionDocument.setPaymentInfoFlag(canCollapse(OLEConstants.PAYMENT_INFO_SECTION, collapseSections));
                requisitionDocument.setAdditionalInstitutionalInfoFlag(canCollapse(OLEConstants.ADDITIONAL_INSTUT_SECTION, collapseSections));
                requisitionDocument.setAccountSummaryFlag(canCollapse(OLEConstants.ACCOUNT_SUMMARY_SECTION, collapseSections));
                requisitionDocument.setRelatedDocumentsFlag(canCollapse(OLEConstants.RELATED_DOCUMENT_SECTION, collapseSections));
                requisitionDocument.setPaymentHistoryFlag(canCollapse(OLEConstants.PAYMENT_HISTORY_SECTION, collapseSections));
                requisitionDocument.setNotesAndAttachmentFlag(canCollapse(OLEConstants.NOTES_AND_ATTACH_SECTION, collapseSections));
                requisitionDocument.setAdHocRecipientsFlag(canCollapse(OLEConstants.ADHOC_RECIPIENT_SECTION, collapseSections));
                requisitionDocument.setRouteLogFlag(canCollapse(OLEConstants.ROUTE_LOG_SECTION, collapseSections));
            }
            else if (document instanceof PurchaseOrderDocument) {
                PurchaseOrderDocument poDocument = (PurchaseOrderDocument)document;
                collapseSections = parameterService.getParameterValuesAsString(PurchaseOrderDocument.class,
                        OLEConstants.INITIAL_COLLAPSE_SECTIONS).toArray(new String[]{});
                poDocument.setOverviewFlag(canCollapse(OLEConstants.OVERVIEW_SECTION,collapseSections));
                poDocument.setDeliveryFlag(canCollapse(OLEConstants.DELIVERY_SECTION,collapseSections));
                poDocument.setVendorFlag(canCollapse(OLEConstants.VENDOR_SECTION,collapseSections));
                poDocument.setTitlesFlag(canCollapse(OLEConstants.TITLES_SECTION,collapseSections));
                poDocument.setPaymentInfoFlag(canCollapse(OLEConstants.PAYMENT_INFO_SECTION,collapseSections));
                poDocument.setAdditionalInstitutionalInfoFlag(canCollapse(OLEConstants.ADDITIONAL_INSTUT_SECTION,collapseSections));
                poDocument.setAccountSummaryFlag(canCollapse(OLEConstants.ACCOUNT_SUMMARY_SECTION,collapseSections));
                poDocument.setRelatedDocumentsFlag(canCollapse(OLEConstants.RELATED_DOCUMENT_SECTION,collapseSections));
                poDocument.setPaymentHistoryFlag(canCollapse(OLEConstants.PAYMENT_HISTORY_SECTION,collapseSections));
                poDocument.setNotesAndAttachmentFlag(canCollapse(OLEConstants.NOTES_AND_ATTACH_SECTION,collapseSections));
                poDocument.setAdHocRecipientsFlag(canCollapse(OLEConstants.ADHOC_RECIPIENT_SECTION,collapseSections));
                poDocument.setRouteLogFlag(canCollapse(OLEConstants.ROUTE_LOG_SECTION,collapseSections));
            }
            else if (document instanceof PaymentRequestDocument) {
                PaymentRequestDocument paymentRequestDoc = (PaymentRequestDocument)document;
                collapseSections = parameterService.getParameterValuesAsString(PaymentRequestDocument.class,
                        OLEConstants.INITIAL_COLLAPSE_SECTIONS).toArray(new String[]{});
                paymentRequestDoc.setOverviewFlag(canCollapse(OLEConstants.OVERVIEW_SECTION,collapseSections));
                paymentRequestDoc.setDeliveryFlag(canCollapse(OLEConstants.DELIVERY_SECTION,collapseSections));
                paymentRequestDoc.setInvoiceInfoFlag(canCollapse(OLEConstants.INVOICE_SECTION,collapseSections));
                paymentRequestDoc.setProcessItemsFlag(canCollapse(OLEConstants.PROCESS_ITEM_SECTION,collapseSections));
                paymentRequestDoc.setAccountSummaryFlag(canCollapse(OLEConstants.ACCOUNT_SUMMARY_SECTION,collapseSections));
                paymentRequestDoc.setRelatedDocumentsFlag(canCollapse(OLEConstants.RELATED_DOCUMENT_SECTION,collapseSections));
                paymentRequestDoc.setPaymentHistoryFlag(canCollapse(OLEConstants.PAYMENT_HISTORY_SECTION,collapseSections));
                paymentRequestDoc.setGeneralEntriesFlag(canCollapse(OLEConstants.GENERAL_ENTRY_SECTION,collapseSections));
                paymentRequestDoc.setNotesAndAttachmentFlag(canCollapse(OLEConstants.NOTES_AND_ATTACH_SECTION,collapseSections));
                paymentRequestDoc.setAdHocRecipientsFlag(canCollapse(OLEConstants.ADHOC_RECIPIENT_SECTION,collapseSections));
                paymentRequestDoc.setRouteLogFlag(canCollapse(OLEConstants.ROUTE_LOG_SECTION,collapseSections));
            }
            else if (document instanceof VendorCreditMemoDocument) {
                VendorCreditMemoDocument vmDocument = (VendorCreditMemoDocument)document;
                collapseSections = parameterService.getParameterValuesAsString(VendorCreditMemoDocument.class,
                        OLEConstants.INITIAL_COLLAPSE_SECTIONS).toArray(new String[]{});
                vmDocument.setOverviewFlag(canCollapse(OLEConstants.OVERVIEW_SECTION,collapseSections));
                vmDocument.setVendorFlag(canCollapse(OLEConstants.VENDOR_SECTION,collapseSections));
                vmDocument.setCreditMemoInfoFlag(canCollapse(OLEConstants.VENDOR_SECTION,collapseSections));
                vmDocument.setProcessItemsFlag(canCollapse(OLEConstants.PROCESS_ITEM_SECTION,collapseSections));
                vmDocument.setAccountSummaryFlag(canCollapse(OLEConstants.ACCOUNT_SUMMARY_SECTION,collapseSections));
                vmDocument.setRelatedDocumentsFlag(canCollapse(OLEConstants.RELATED_DOCUMENT_SECTION,collapseSections));
                vmDocument.setPaymentHistoryFlag(canCollapse(OLEConstants.PAYMENT_HISTORY_SECTION,collapseSections));
                vmDocument.setGeneralEntriesFlag(canCollapse(OLEConstants.GENERAL_ENTRY_SECTION,collapseSections));
                vmDocument.setNotesAndAttachmentFlag(canCollapse(OLEConstants.NOTES_AND_ATTACH_SECTION,collapseSections));
                vmDocument.setAdHocRecipientsFlag(canCollapse(OLEConstants.ADHOC_RECIPIENT_SECTION,collapseSections));
                vmDocument.setRouteLogFlag(canCollapse(OLEConstants.ROUTE_LOG_SECTION,collapseSections));
            }

        }
        catch (Exception e) {
            LOG.error("Error while getting the default Collapse section on PurchasingAccountsPayable Document");
            throw new RuntimeException(e);
        }
        LOG.debug("Leaving getInitialCollapseSections()");

    }

    private boolean canCollapse(String sectionName, String[] collapseSections) {
        LOG.debug("Inside method canCollapse()");
        List<String> sectionLists = Arrays.asList(collapseSections);
        if (sectionLists.contains(sectionName)) {
            return false;
        }
        return true;
    }


    /*
    Method to return the First and Last name of a patron given the patronid
    This method is used only for the purpose of displaying the Patron name information on the Requisition Document..
     */
    public String getPatronName(String patronId){
        StringBuffer patronName = new StringBuffer();
        try{
            if(LOG.isDebugEnabled()){
                LOG.debug("Getting name for patron -" + patronId);
            }
            Map<String,String> map=new HashMap<String,String>();
            map.put("entityId",patronId);
            EntityNameBo entityNameBo=(EntityNameBo) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(EntityNameBo.class,map);
            patronName.append(entityNameBo.getLastName()).append(", ").append(entityNameBo.getFirstName());
            if(LOG.isDebugEnabled()){
                LOG.debug("Returning Name:" + patronName + ": for patronId :" + patronId + ":");
            }
        } catch(Exception e){
            LOG.error("Exception while trying to get name for patron ",e);
        }
        return patronName.toString();
    }//end getPatronName method...

    /**
     * This method is used to set the BibMarcRecord which in turn used for creation of Bib record into docstore.
     * @param bibMarcRecord
     * @param bibInfoBean
     */
    public void setBibMarcRecord(BibMarcRecord bibMarcRecord,BibInfoBean bibInfoBean) {
        LOG.debug("Inside setBibMarcRecord method.....");
        List<org.kuali.ole.docstore.common.document.content.bib.marc.DataField> dataFieldList = new ArrayList<DataField>()
                ;
        if (StringUtils.isNotEmpty(bibInfoBean.getTitle())) {
            dataFieldList.add(setDataField(org.kuali.ole.OLEConstants.MARC_EDITOR_TITLE_245, bibInfoBean.getTitle(), org.kuali.ole.OLEConstants.A, org.kuali.ole.OLEConstants.MARC_EDITOR_IND1,dataFieldList));
        }
        if (StringUtils.isNotEmpty(bibInfoBean.getAuthor())) {
            dataFieldList.add(setDataField(org.kuali.ole.OLEConstants.MARC_EDITOR_AUTHOR_100, bibInfoBean.getAuthor(), org.kuali.ole.OLEConstants.A, org.kuali.ole.OLEConstants.MARC_EDITOR_IND1,dataFieldList));
        }

        if (StringUtils.isNotEmpty(bibInfoBean.getEdition())) {
            dataFieldList.add(setDataField(org.kuali.ole.OLEConstants.MARC_EDITOR_EDITION_250, bibInfoBean.getEdition(), org.kuali.ole.OLEConstants.A, org.kuali.ole.OLEConstants.MARC_EDITOR_IND1,dataFieldList));
        }

        if (StringUtils.isNotEmpty(bibInfoBean.getSeries())) {
            dataFieldList.add(setDataField(org.kuali.ole.OLEConstants.MARC_EDITOR_SERIES_490, bibInfoBean.getSeries(), org.kuali.ole.OLEConstants.A, org.kuali.ole.OLEConstants.MARC_EDITOR_IND1,dataFieldList));
        }

        if (StringUtils.isNotEmpty(bibInfoBean.getPlaceOfPublication())) {
            dataFieldList.add(setDataField(org.kuali.ole.OLEConstants.MARC_EDITOR_POP_260, bibInfoBean.getPlaceOfPublication(), org.kuali.ole.OLEConstants.A, org.kuali.ole.OLEConstants.MARC_EDITOR_IND1,dataFieldList));
        }

        if (StringUtils.isNotEmpty(bibInfoBean.getPublisher())) {
            dataFieldList.add(setDataField(org.kuali.ole.OLEConstants.MARC_EDITOR_PUBLISHER_260, bibInfoBean.getPublisher(), org.kuali.ole.OLEConstants.B, org.kuali.ole.OLEConstants.MARC_EDITOR_IND1,dataFieldList));
        }

        if (StringUtils.isNotEmpty(bibInfoBean.getYearOfPublication())) {
            dataFieldList.add(setDataField(org.kuali.ole.OLEConstants.MARC_EDITOR_YOP_260, bibInfoBean.getYearOfPublication(), org.kuali.ole.OLEConstants.C, org.kuali.ole.OLEConstants.MARC_EDITOR_IND1,dataFieldList));
        }

        if (StringUtils.isNotEmpty(bibInfoBean.getTypeOfStandardNumber())) {
            String standardType = bibInfoBean.getTypeOfStandardNumber();
            boolean isIssn = false;
            boolean isIsbn = false;
            if (standardType.equalsIgnoreCase(org.kuali.ole.OLEConstants.OLEBatchProcess.ISSN)) {
                isIssn = true;
            } else if (standardType.equalsIgnoreCase(org.kuali.ole.OLEConstants.ISBN)) {
                isIsbn = true;
            }
            if (StringUtils.isNotEmpty(bibInfoBean.getStandardNumber())) {
                if (isIssn) {
                    dataFieldList.add(setDataField(org.kuali.ole.OLEConstants.MARC_EDITOR_022, bibInfoBean.getStandardNumber(), org.kuali.ole.OLEConstants.A, org.kuali.ole.OLEConstants.MARC_EDITOR_IND1,dataFieldList));
                } else if (isIsbn) {
                    dataFieldList.add(setDataField(org.kuali.ole.OLEConstants.MARC_EDITOR_020, bibInfoBean.getStandardNumber(), org.kuali.ole.OLEConstants.A, org.kuali.ole.OLEConstants.MARC_EDITOR_IND1,dataFieldList));
                } else {
                    dataFieldList.add(setDataField(org.kuali.ole.OLEConstants.MARC_EDITOR_024, bibInfoBean.getStandardNumber(), org.kuali.ole.OLEConstants.A, org.kuali.ole.OLEConstants.MARC_EDITOR_IND1_8,dataFieldList));
                }
            }
        }
        bibMarcRecord.setDataFields(dataFieldList);
        LOG.debug("setBibMarcRecord method successfull.....");
    }



    /**
     * This method is used the set the Datafield for the given tag and value
     * @param tag
     * @param value
     * @param dataFieldList
     * @return
     */
    private DataField setDataField(String tag, String value, String subFieldCode, String ind1, List<DataField> dataFieldList){
        if(LOG.isDebugEnabled()){
            LOG.debug("inside setDataField method.....");
            LOG.debug("tag---->"+tag+" "+"value---->"+value);
        }
        DataField dataField = new DataField();
        for(DataField dataFields:dataFieldList){
            if(dataFields.getTag().equalsIgnoreCase(tag)){
                dataField=dataFields;
            }
        }
        dataFieldList.remove(dataField);
        dataField.setTag(tag);
        dataField.setInd1(ind1);
        List<org.kuali.ole.docstore.common.document.content.bib.marc.SubField> subFields = new ArrayList<>();
        org.kuali.ole.docstore.common.document.content.bib.marc.SubField subField = new org.kuali.ole.docstore.common.document.content.bib.marc.SubField();
        subField.setCode(subFieldCode);
        subField.setValue(value);
        subFields.add(subField);
        dataField.getSubFields().add(subField);
        return dataField;
    }

    /**
     * This method is to retrieve the Invoice Documents related to Requisition
     * @param purApItem
     */
    public void setInvoiceDocumentsForRequisition(PurApItem purApItem) {
        OleRequisitionItem singleItem = (OleRequisitionItem) purApItem;
        Map<String, OleInvoiceDocument> paidDocuments = new HashMap<String, OleInvoiceDocument>();
        Map<String, OlePaymentRequestDocument> paymentRequests = new HashMap<String, OlePaymentRequestDocument>();
        for (OleCopy oleCopy : singleItem.getCopyList()) {
            if (oleCopy.getOlePaidCopies().size() > 0) {
                for (OLEPaidCopy olePaidCopy : oleCopy.getOlePaidCopies()) {
                    OleInvoiceDocument invoiceDocument = null;
                    if (olePaidCopy.getInvoiceItemId() != null &&
                            paidDocuments.containsKey(olePaidCopy.getInvoiceItemId().toString())) {
                        OleInvoiceItem invoiceItem = olePaidCopy.getInvoiceItem();
                        invoiceDocument = paidDocuments.get(olePaidCopy.getInvoiceItemId());
                        if (invoiceItem.getPoItemIdentifier().compareTo(oleCopy.getPoItemId()) == 0 && invoiceDocument != null) {
                            ((OleInvoiceItem)invoiceDocument.getItem(invoiceItem.getItemLineNumber())).setRequisitionItemIdentifier(oleCopy.getReqItemId());
                        }
                    }
                    else if (olePaidCopy.getInvoiceItem().getInvoiceDocument() != null && olePaidCopy.getInvoiceItem().getInvoiceDocument().getPurapDocumentIdentifier() != null
                            && (paidDocuments.containsKey(olePaidCopy.getInvoiceItem().getInvoiceDocument().getPurapDocumentIdentifier().toString()))) {
                        OleInvoiceItem invoiceItem = olePaidCopy.getInvoiceItem();
                        invoiceDocument = paidDocuments.get(olePaidCopy.getInvoiceItem().getInvoiceDocument().getPurapDocumentIdentifier());
                        if (invoiceItem.getPoItemIdentifier().compareTo(oleCopy.getPoItemId()) == 0 && invoiceDocument != null) {
                            ((OleInvoiceItem)invoiceDocument.getItem(invoiceItem.getItemLineNumber())).setRequisitionItemIdentifier(oleCopy.getReqItemId());
                        }
                    }
                    else {
                        if (olePaidCopy.getInvoiceItem() != null && olePaidCopy.getInvoiceItem().isDebitItem()) {
                            OleInvoiceItem invoiceItem = olePaidCopy.getInvoiceItem();
                            invoiceDocument = (OleInvoiceDocument)olePaidCopy.getInvoiceItem().getInvoiceDocument();
                            if (invoiceDocument != null && SpringContext.getBean(DocumentHeaderService.class) != null) {
                            //    invoiceDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(invoiceDocument.getDocumentNumber()));
                                if (invoiceItem.getPoItemIdentifier().compareTo(oleCopy.getPoItemId()) == 0) {
                                    invoiceItem.setRequisitionItemIdentifier(oleCopy.getReqItemId());
                                }
                                paidDocuments.put(invoiceDocument.getPurapDocumentIdentifier().toString(),
                                        invoiceDocument);
                                invoiceDocument.setPaymentRequestDocuments(new ArrayList<OlePaymentRequestDocument>());
                                invoiceDocument.setCreditMemoDocuments(new ArrayList<OleVendorCreditMemoDocument>());
                            }
                        }
                    }
                    if (invoiceDocument != null && olePaidCopy.getPreqItem() != null) {
                        OlePaymentRequestDocument preq = (OlePaymentRequestDocument) olePaidCopy.getPreqItem().getPaymentRequestDocument();
                        if (preq != null && !paymentRequests.containsKey(preq.getDocumentNumber())) {
                            paymentRequests.put(preq.getDocumentNumber(), preq);
                            invoiceDocument.getPaymentRequestDocuments().add(preq);
                        }
                    }
                    if (invoiceDocument != null && SpringContext.getBean(DocumentHeaderService.class) != null) {
                    //    invoiceDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(invoiceDocument.getDocumentNumber()));
                        paidDocuments.put(invoiceDocument.getPurapDocumentIdentifier().toString(),
                                invoiceDocument);
                    }
                }
            }
        }
        Collection collection = (Collection) (paidDocuments.values());
        List list = new ArrayList(collection);
        singleItem.setInvoiceDocuments(list);
    }

    /**
     * This method is to retrieve the Invoice Documents related to PO
     * @param purApItem
     */
    public void setInvoiceDocumentsForPO(OlePurchaseOrderItem singleItem) {
     //   OlePurchaseOrderItem singleItem = (OlePurchaseOrderItem) purApItem;
        Map<String, OleInvoiceDocument> paidDocuments = new HashMap<String, OleInvoiceDocument>();
        Map<String, OlePaymentRequestDocument> paymentRequests = new HashMap<String, OlePaymentRequestDocument>();
        for (OleCopy oleCopy : singleItem.getCopyList()) {
            if (oleCopy.getOlePaidCopies().size() > 0) {
                for (OLEPaidCopy olePaidCopy : oleCopy.getOlePaidCopies()) {
                    OleInvoiceDocument invoiceDocument = null;
                    if (olePaidCopy.getInvoiceItemId() != null &&
                            paidDocuments.containsKey(olePaidCopy.getInvoiceItemId().toString())) {
                        invoiceDocument = paidDocuments.get(olePaidCopy.getInvoiceItemId());
                    }
                    else if (olePaidCopy.getInvoiceItem().getInvoiceDocument() != null && olePaidCopy.getInvoiceItem().getInvoiceDocument().getPurapDocumentIdentifier() != null
                            && (paidDocuments.containsKey(olePaidCopy.getInvoiceItem().getInvoiceDocument().getPurapDocumentIdentifier().toString()))) {
                        invoiceDocument = paidDocuments.get(olePaidCopy.getInvoiceItem().getInvoiceDocument().getPurapDocumentIdentifier());
                    }
                    else {
                        if (olePaidCopy.getInvoiceItem() != null) {
                            invoiceDocument = (OleInvoiceDocument)olePaidCopy.getInvoiceItem().getInvoiceDocument();
                            if (invoiceDocument != null && SpringContext.getBean(DocumentHeaderService.class) != null) {
                            //    invoiceDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(invoiceDocument.getDocumentNumber()));
                                paidDocuments.put(invoiceDocument.getPurapDocumentIdentifier().toString(),
                                        invoiceDocument);
                                invoiceDocument.setPaymentRequestDocuments(new ArrayList<OlePaymentRequestDocument>());
                                invoiceDocument.setCreditMemoDocuments(new ArrayList<OleVendorCreditMemoDocument>());
                            }
                        }
                    }
                    if (invoiceDocument != null && olePaidCopy.getPreqItem() != null) {
                        OlePaymentRequestDocument preq = (OlePaymentRequestDocument)olePaidCopy.getPreqItem().getPaymentRequestDocument();
                        if (preq != null && !paymentRequests.containsKey(preq.getDocumentNumber())) {
                            paymentRequests.put(preq.getDocumentNumber(),preq);
                            invoiceDocument.getPaymentRequestDocuments().add(preq);
                        }
                    }
                    if (invoiceDocument != null && SpringContext.getBean(DocumentHeaderService.class) != null) {
                     //   invoiceDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(invoiceDocument.getDocumentNumber()));
                        paidDocuments.put(invoiceDocument.getPurapDocumentIdentifier().toString(),
                                invoiceDocument);
                    }
                }
            }
        }
        Collection collection = (Collection)(paidDocuments.values());
        List list = new ArrayList(collection);
        singleItem.setInvoiceDocuments(list);
    }

    public void setInvoiceDocumentsForPO(PurchaseOrderDocument purchaseOrderDocument,OlePurchaseOrderItem singleItem) {
            if (singleItem.getInvoiceDocuments().size() == 0) {
                Map<String, OleInvoiceDocument> invoiceDocMap = new HashMap<>();
                List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = getRelatedPurchaseOrderList(purchaseOrderDocument);
                if (olePurchaseOrderDocumentList.size() > 0) {
                    for (OlePurchaseOrderDocument linkedOlePurchaseOrderDocument : olePurchaseOrderDocumentList) {
                        for (OlePurchaseOrderItem olePurchaseOrderItem : (List<OlePurchaseOrderItem>) linkedOlePurchaseOrderDocument.getItems()) {
                            if (StringUtils.isNotBlank(olePurchaseOrderItem.getItemTypeCode()) && olePurchaseOrderItem.getItemTypeCode().equals(org.kuali.ole.OLEConstants.ITM_TYP_CODE)) {
                                if ((StringUtils.isNotBlank(olePurchaseOrderItem.getLinkToOrderOption()) && olePurchaseOrderItem.getLinkToOrderOption().equals(OLEConstants.ERESOURCE)) || olePurchaseOrderItem.getItemTitleId().equals(singleItem.getItemTitleId())) {
                                    List<OleInvoiceItem> oleInvoiceItemList = getOleInvoiceItemList(purchaseOrderDocument, olePurchaseOrderItem);
                                    if (oleInvoiceItemList.size() > 0) {
                                        for (OleInvoiceItem oleInvoiceItem : oleInvoiceItemList) {
                                            if (oleInvoiceItem.getAccountsPayablePurchasingDocumentLinkIdentifier().equals(linkedOlePurchaseOrderDocument.getAccountsPayablePurchasingDocumentLinkIdentifier())) {
                                                if (!invoiceDocMap.containsKey(oleInvoiceItem.getInvoiceDocument().getPurapDocumentIdentifier().toString())) {
                                                    OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceItem.getInvoiceDocument();
                                                    try {
                                                        WorkflowDocument document = SpringContext.getBean(WorkflowDocumentService.class).loadWorkflowDocument(oleInvoiceDocument.getDocumentNumber(), GlobalVariables.getUserSession().getPerson());
                                                        oleInvoiceDocument.getDocumentHeader().setWorkflowDocument(document);
                                                    } catch (WorkflowException we) {
                                                        throw new RuntimeException(we);
                                                    }
                                                    if (oleInvoiceDocument.getPaymentRequestDocuments() != null) {
                                                        List<OlePaymentRequestDocument> olePaymentRequestDocumentList = getOlePaymentRequestDocumentList(oleInvoiceDocument, linkedOlePurchaseOrderDocument.getPurapDocumentIdentifier());
                                                        oleInvoiceDocument.setPaymentRequestDocuments(olePaymentRequestDocumentList);
                                                    }
                                                   if (oleInvoiceDocument.getCreditMemoDocuments() != null) {
                                                        List<OleVendorCreditMemoDocument> oleVendorCreditMemoDocumentList = getOleVendorCreditMemoDocumentList(oleInvoiceDocument, linkedOlePurchaseOrderDocument.getPurapDocumentIdentifier());
                                                        oleInvoiceDocument.setCreditMemoDocuments(oleVendorCreditMemoDocumentList);
                                                    }
                                                    invoiceDocMap.put(oleInvoiceItem.getInvoiceDocument().getPurapDocumentIdentifier().toString(), oleInvoiceDocument);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Collection collection = (Collection) (invoiceDocMap.values());
                List list = new ArrayList(collection);
                singleItem.setInvoiceDocuments(list);
            }
        }

    public void setInvoiceDocumentsForEResourcePO(PurApItem purApItem) {
        OlePurchaseOrderItem singleItem = (OlePurchaseOrderItem) purApItem;
        Map<String, OleInvoiceDocument> paidDocuments = new HashMap<String, OleInvoiceDocument>();
        Map<String, OlePaymentRequestDocument> paymentRequests = new HashMap<String, OlePaymentRequestDocument>();
        for (OleCopy oleCopy : singleItem.getCopyList()) {
            if (oleCopy.getOlePaidCopies().size() > 0) {
                for (OLEPaidCopy olePaidCopy : oleCopy.getOlePaidCopies()) {
                    OleInvoiceDocument invoiceDocument = null;
                    if (olePaidCopy.getInvoiceItemId() != null &&
                            paidDocuments.containsKey(olePaidCopy.getInvoiceItemId().toString())) {
                        OleInvoiceItem invoiceItem = olePaidCopy.getInvoiceItem();
                        invoiceDocument = paidDocuments.get(olePaidCopy.getInvoiceItemId());
                        if (invoiceItem.getPoItemIdentifier().compareTo(oleCopy.getPoItemId()) == 0 && invoiceDocument != null) {
                            ((OleInvoiceItem)invoiceDocument.getItem(invoiceItem.getItemLineNumber())).setRequisitionItemIdentifier(oleCopy.getReqItemId());
                        }
                    }
                    else if (olePaidCopy.getInvoiceItem().getInvoiceDocument() != null && olePaidCopy.getInvoiceItem().getInvoiceDocument().getPurapDocumentIdentifier() != null
                            && (paidDocuments.containsKey(olePaidCopy.getInvoiceItem().getInvoiceDocument().getPurapDocumentIdentifier().toString()))) {
                        OleInvoiceItem invoiceItem = olePaidCopy.getInvoiceItem();
                        invoiceDocument = paidDocuments.get(olePaidCopy.getInvoiceItem().getInvoiceDocument().getPurapDocumentIdentifier());
                        if (invoiceItem.getPoItemIdentifier().compareTo(oleCopy.getPoItemId()) == 0 && invoiceDocument != null) {
                            ((OleInvoiceItem)invoiceDocument.getItem(invoiceItem.getItemLineNumber())).setRequisitionItemIdentifier(oleCopy.getReqItemId());
                        }
                    }
                    else {
                        if (olePaidCopy.getInvoiceItem() != null && olePaidCopy.getInvoiceItem().isDebitItem()) {
                            OleInvoiceItem invoiceItem = olePaidCopy.getInvoiceItem();
                            invoiceDocument = (OleInvoiceDocument)olePaidCopy.getInvoiceItem().getInvoiceDocument();
                            if (invoiceDocument != null && SpringContext.getBean(DocumentHeaderService.class) != null) {
                           //     invoiceDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(invoiceDocument.getDocumentNumber()));
                                if (invoiceItem.getPoItemIdentifier().compareTo(oleCopy.getPoItemId()) == 0) {
                                    invoiceItem.setRequisitionItemIdentifier(oleCopy.getReqItemId());
                                }
                                paidDocuments.put(invoiceDocument.getPurapDocumentIdentifier().toString(),
                                        invoiceDocument);
                                invoiceDocument.setPaymentRequestDocuments(new ArrayList<OlePaymentRequestDocument>());
                                invoiceDocument.setCreditMemoDocuments(new ArrayList<OleVendorCreditMemoDocument>());
                            }
                        }
                    }
                    if (invoiceDocument != null && olePaidCopy.getPreqItem() != null) {
                        OlePaymentRequestDocument preq = (OlePaymentRequestDocument) olePaidCopy.getPreqItem().getPaymentRequestDocument();
                        if (preq != null && !paymentRequests.containsKey(preq.getDocumentNumber())) {
                            paymentRequests.put(preq.getDocumentNumber(), preq);
                            invoiceDocument.getPaymentRequestDocuments().add(preq);
                        }
                    }
                    if (invoiceDocument != null && SpringContext.getBean(DocumentHeaderService.class) != null) {
                    //    invoiceDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(invoiceDocument.getDocumentNumber()));
                        paidDocuments.put(invoiceDocument.getPurapDocumentIdentifier().toString(),
                                invoiceDocument);
                    }
                }
            }
        }
        Collection collection = (Collection) (paidDocuments.values());
        List list = new ArrayList(collection);
        singleItem.setInvoiceDocuments(list);
    }

    private List<OlePurchaseOrderDocument> getRelatedPurchaseOrderList(PurchaseOrderDocument purchaseOrderDocument){
        if (purchaseOrderDocument.getAccountsPayablePurchasingDocumentLinkIdentifier() != null) {
            Map<String, String> poCriteriaMap = new HashMap<>();
            poCriteriaMap.put(org.kuali.ole.OLEConstants.PurapInvoiceHistory.PURAP_DOC_LINK, Integer.toString(purchaseOrderDocument.getAccountsPayablePurchasingDocumentLinkIdentifier()));
            List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = (List<OlePurchaseOrderDocument>) getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, poCriteriaMap);
            return olePurchaseOrderDocumentList;
        }
        return new ArrayList<OlePurchaseOrderDocument>();
    }

    private List<OleInvoiceItem> getOleInvoiceItemList(PurchaseOrderDocument purchaseOrderDocument,OlePurchaseOrderItem olePurchaseOrderItem){
        Map<String,String> invoiceCriteriaMap = new HashMap<>();
        invoiceCriteriaMap.put(org.kuali.ole.OLEConstants.PurapInvoiceHistory.POID,Integer.toString(purchaseOrderDocument.getPurapDocumentIdentifier()));
        invoiceCriteriaMap.put(org.kuali.ole.OLEConstants.PurapInvoiceHistory.PO_ITM_ID,Integer.toString(olePurchaseOrderItem.getItemIdentifier()));
        List<OleInvoiceItem> oleInvoiceItemList = (List<OleInvoiceItem>)getBusinessObjectService().findMatching(OleInvoiceItem.class,invoiceCriteriaMap);
        return oleInvoiceItemList;
    }

    private List<OlePaymentRequestDocument> getOlePaymentRequestDocumentList(OleInvoiceDocument oleInvoiceDocument, Integer purchaseOrderIdentifier){
        Map<String,String> paymentRequestMap = new HashMap<>();
        paymentRequestMap.put(org.kuali.ole.OLEConstants.PurapInvoiceHistory.INVOICE_ID,Integer.toString(oleInvoiceDocument.getPurapDocumentIdentifier()));
        paymentRequestMap.put(org.kuali.ole.OLEConstants.PurapInvoiceHistory.POID,Integer.toString(purchaseOrderIdentifier));
        List<OlePaymentRequestDocument> olePaymentRequestDocumentList = (List<OlePaymentRequestDocument>)getBusinessObjectService().findMatching(OlePaymentRequestDocument.class,paymentRequestMap);
        return olePaymentRequestDocumentList;
    }

    private List<OleVendorCreditMemoDocument> getOleVendorCreditMemoDocumentList(OleInvoiceDocument oleInvoiceDocument, Integer purchaseOrderIdentifier){
        Map<String,String> creditMemoMap = new HashMap<>();
        creditMemoMap.put(org.kuali.ole.OLEConstants.PurapInvoiceHistory.INVOICE_ID,Integer.toString(oleInvoiceDocument.getPurapDocumentIdentifier()));
        creditMemoMap.put(org.kuali.ole.OLEConstants.PurapInvoiceHistory.POID,Integer.toString(purchaseOrderIdentifier));
        List<OleVendorCreditMemoDocument> oleVendorCreditMemoDocumentList = (List<OleVendorCreditMemoDocument>)getBusinessObjectService().findMatching(OleVendorCreditMemoDocument.class,creditMemoMap);
        return oleVendorCreditMemoDocumentList;
    }

    /**
     * This method is used to get the requestor type id for the given requestor type
     * @param requestorType
     * @return
     */
    public Integer getRequestorTypeId(String requestorType) {
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(OleSelectConstant.REQUESTOR_TYPE, requestorType);
        Collection<OleRequestorType> requestorTypeList = getBusinessObjectService().findMatching(OleRequestorType.class, criteria);
        return requestorTypeList.iterator().next().getRequestorTypeId();
    }

    /**
     * This method is used to set the claimdate for Requisition
     * @param oleRequisitionItem
     */
    public void setClaimDateForReq(OleRequisitionItem oleRequisitionItem,VendorDetail vendorDetail){
        if (vendorDetail != null) {
            String claimInterval = vendorDetail.getClaimInterval();
            if (StringUtils.isNotBlank(claimInterval)) {
                Integer actIntvl = Integer.parseInt(claimInterval);
                oleRequisitionItem.setClaimDate(new java.sql.Date(DateUtils.addDays(new java.util.Date(), actIntvl).getTime()));
            }
        }
    }

    /**
     * This method is used to set the claimdate for Purchase Order
     * @param olePurchaseOrderItem
     */
    public void setClaimDateForPO(OlePurchaseOrderItem olePurchaseOrderItem,VendorDetail vendorDetail){
        if (vendorDetail != null) {
            String claimInterval = vendorDetail.getClaimInterval();
            if (StringUtils.isNotBlank(claimInterval)) {
                Integer actIntvl = Integer.parseInt(claimInterval);
                olePurchaseOrderItem.setClaimDate(new java.sql.Date(DateUtils.addDays(new java.util.Date(), actIntvl).getTime()));
            }
        }
    }

    /**
     * This method is used to get the item description for the given Bib
     * @param bib
     */
    public String getItemDescription(Bib bib){
        LOG.debug("### Inside setItemDescription() of OleRequisitionDocument ###");
        String itemDescription = ((bib.getTitle() != null && !bib.getTitle().isEmpty()) ? bib.getTitle() + "," : "") + ((bib.getAuthor() != null && !bib.getAuthor().isEmpty()) ? bib.getAuthor() + "," : "") + ((bib.getPublisher() != null && !bib.getPublisher().isEmpty()) ? bib.getPublisher() + "," : "") + ((bib.getIsbn() != null && !bib.getIsbn().isEmpty()) ? bib.getIsbn() + "," : "");
        itemDescription = itemDescription.lastIndexOf(",") < 0 ? itemDescription :
                itemDescription.substring(0, itemDescription.lastIndexOf(","));
        if(LOG.isDebugEnabled()){
            LOG.debug("Item Description---------->"+itemDescription);
        }
        StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
        itemDescription = stringEscapeUtils.unescapeHtml(itemDescription);
        return itemDescription;
    }


    public String getItemDescription(OlePurchaseOrderItem olePurchaseOrderItem) {
        BibInfoRecord bibInfoRecord = olePurchaseOrderItem.getBibInfoRecord();
        String itemDescription = null;
        if (bibInfoRecord != null) {
            olePurchaseOrderItem.setBibUUID(bibInfoRecord.getBibIdStr());
            olePurchaseOrderItem.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(olePurchaseOrderItem.getItemTitleId()));

            itemDescription = ((bibInfoRecord.getTitle() != null && !bibInfoRecord.getTitle().isEmpty()) ? bibInfoRecord.getTitle().trim() + ", " : "")
                    + ((bibInfoRecord.getAuthor() != null && !bibInfoRecord
                    .getAuthor().isEmpty()) ? bibInfoRecord.getAuthor().trim() + ", "
                    : "")
                    + ((bibInfoRecord.getPublisher() != null && !bibInfoRecord
                    .getPublisher().isEmpty()) ? bibInfoRecord.getPublisher().trim()
                    + ", " : "")
                    + ((bibInfoRecord.getIsxn() != null && !bibInfoRecord.getIsxn()
                    .isEmpty()) ? bibInfoRecord.getIsxn().trim() + ", " : "");
        }
        if (itemDescription != null && !(itemDescription.equals(""))) {
            itemDescription = itemDescription.lastIndexOf(",") < 0 ? itemDescription :
                    itemDescription.substring(0, itemDescription.lastIndexOf(","));
            StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
            itemDescription = stringEscapeUtils.unescapeXml(itemDescription);
        }
        return itemDescription;
    }

    /**
     *  This method is used to get the Purchase Order Type object based on the given purchase order type id
     * @param purchaseOrderTypeId
     * @return
     */
    public PurchaseOrderType getPurchaseOrderType(BigDecimal purchaseOrderTypeId){
        Map purchaseOrderTypeIdMap = new HashMap();
        purchaseOrderTypeIdMap.put(org.kuali.ole.OLEConstants.PURCHASE_ORDER_TYPE_ID, purchaseOrderTypeId);
        List<PurchaseOrderType> purchaseOrderTypeDocumentList = (List) getBusinessObjectService().findMatching(PurchaseOrderType.class, purchaseOrderTypeIdMap);
        if (purchaseOrderTypeDocumentList != null && purchaseOrderTypeDocumentList.size() > 0) {
            return purchaseOrderTypeDocumentList.get(0);
        }else{
            return null;
        }
    }

    /**
     * This method returns the system parameter value for the given input.
     * @param name
     * @return
     */
    public String getParameter(String name) {
        LOG.debug("Inside getParameter()");
        String parameter = "";
        try {
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put(org.kuali.ole.OLEConstants.NAMESPACE_CODE, org.kuali.ole.OLEConstants.SELECT_NMSPC);
            criteriaMap.put(org.kuali.ole.OLEConstants.COMPONENT_CODE, org.kuali.ole.OLEConstants.SELECT_CMPNT);
            criteriaMap.put(org.kuali.ole.OLEConstants.NAME_SELECTOR, name);
            List<ParameterBo> parametersList = (List<ParameterBo>) getBusinessObjectService().findMatching(ParameterBo.class, criteriaMap);
            for (ParameterBo parameterBo : parametersList) {
                parameter = parameterBo.getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception while getting parameter value", e);
        }
        LOG.debug("End of getParameter()");
        return parameter;
    }

    /**
     * This method gives the current date time value.
     * @return
     */
    public String getCurrentDateTime() {
        LOG.debug("Inside getCurrentDateTime()");
        Date date = getDateTimeService().getCurrentDate();
        String currentDate = SpringContext.getBean(DateTimeService.class).toString(date, timeStampFormat);
        LOG.debug("End of getCurrentDateTime()");
        return currentDate;
    }

    /**
     * This method is used to set the Document Description from system parameters.
     * @param description
     * @param descMap
     * @return
     */
    public String setDocumentDescription(String description,Map descMap){
        LOG.debug("Inside setDocumentDescription()");
        if(description.contains(OLEConstants.OPERATOR_INITIALS)){
            description = description.replace(OLEConstants.OPERATOR_INITIALS,getOperatorInitials());
        }
        if(description.contains(OLEConstants.CURRENT_DATE_TIME)){
            description = description.replace(OLEConstants.CURRENT_DATE_TIME,getCurrentDateTime());
        }
        if(descMap != null){
            if(description.contains(OLEConstants.PO_DOC_ID)){
                description = description.replace(OLEConstants.PO_DOC_ID,descMap.get(OLEConstants.PO_DOC_ID) != null ?descMap.get(OLEConstants.PO_DOC_ID).toString():"");
            }
            if(description.contains(OLEConstants.VENDOR_NAME)){
                description = description.replace(OLEConstants.VENDOR_NAME,descMap.get(OLEConstants.VENDOR_NAME) != null ?descMap.get(OLEConstants.VENDOR_NAME).toString():"");
            }
            if(description.contains(OLEConstants.ORDER_TYP)){
                description = description.replace(OLEConstants.ORDER_TYP,descMap.get(OLEConstants.ORDER_TYP) != null ? descMap.get(OLEConstants.ORDER_TYP).toString():"");
            }
            if(description.contains(OLEConstants.VND_ITM_ID)){
                description = description.replace(OLEConstants.VND_ITM_ID,descMap.get(OLEConstants.VND_ITM_ID) != null ? descMap.get(OLEConstants.VND_ITM_ID).toString():"");
            }
        }
        if(LOG.isDebugEnabled()) {
            LOG.debug("Document Description ----------------->" +  description);
        }
        LOG.debug("End of setDocumentDescription()");
        return description;

    }
}
