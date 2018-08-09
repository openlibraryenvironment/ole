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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.StopWatch;
import org.joda.time.DateTime;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.module.purap.document.service.ReceivingService;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OleLineItemReceivingService;
import org.kuali.ole.select.document.service.OleNoteTypeService;
import org.kuali.ole.select.document.service.ReceivingQueueDAOService;
import org.kuali.ole.select.document.service.impl.OleLineItemReceivingServiceImpl;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.select.service.OleDocStoreLookupService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.exception.WorkflowServiceError;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.exception.DocumentAuthorizationException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OleReceivingQueueSearchDocument extends TransactionalDocumentBase implements DocstoreConstants {

    private String purchaseOrderNumber;

    private String standardNumber;

    private String title;

    //private String journal;

    private String vendorName;

    private String beginDate;

    private String endDate;

    /*private boolean serials;

    private boolean standingOrders;

    private boolean vendor;*/

    private boolean monograph;

    //private boolean purchaseOrderDate;

    //private boolean status;

    private VendorDetail vendorDetail;

    private String purchaseOrderStatusDescription;

    private PurchaseOrderType orderType;

    private String purchaseOrderType;

    private boolean receive;

    private String author;

    private String publisher;

    private String edition;

    private String quatityOrdered;

    private String points;

    private String instructions;

    private PurchaseOrderDocument purchaseOrderDocument;

    public boolean isDateLookup = false;

    private List<String> receivingDocumentsList = new ArrayList<String>();

   /* private int poId=0;*/

    private String documentNumber = null;

    private DateTimeService dateTimeService;

    private PurchaseOrderService purchaseOrderService;

    private BusinessObjectService businessObjectService;

    private ConfigurationService configurationService;

    private ReceivingService receivingService;

    private OleNoteTypeService oleNoteTypeService;

    private List<PurchaseOrderType> purchaseOrderTypeDocumentList = new ArrayList<>();

    private Map<String, Object> queryCriteriaMap = new HashMap<>();

    public DateTimeService getDateTimeService() {
        if (dateTimeService == null) {
            dateTimeService = SpringContext.getBean(DateTimeService.class);
        }
        return dateTimeService;
    }

    public List<OlePurchaseOrderItem> purchaseOrderItems = new ArrayList<OlePurchaseOrderItem>(0);

    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleReceivingQueueSearchDocument.class);

    private boolean claimFilter;


    public boolean isClaimFilter() {
        return claimFilter;
    }

    public void setClaimFilter(boolean claimFilter) {
        this.claimFilter = claimFilter;
    }

    /**
     * Gets the purchaseOrderStatusDescription attribute.
     *
     * @return Returns the purchaseOrderStatusDescription.
     */
    public String getPurchaseOrderStatusDescription() {
        return purchaseOrderStatusDescription;
    }

    /**
     * Sets the purchaseOrderStatusDescription attribute value.
     *
     * @param purchaseOrderStatusDescription The purchaseOrderStatusDescription to set.
     */
    public void setPurchaseOrderStatusDescription(String purchaseOrderStatusDescription) {
        this.purchaseOrderStatusDescription = purchaseOrderStatusDescription;
    }

    /**
     * Gets the orderType attribute.
     *
     * @return Returns the orderType.
     */
    public PurchaseOrderType getOrderType() {
        return orderType;
    }

    /**
     * Sets the orderType attribute value.
     *
     * @param orderType The orderType to set.
     */
    public void setOrderType(PurchaseOrderType orderType) {
        this.orderType = orderType;
    }

    /**
     * Gets the purchaseOrderType attribute.
     *
     * @return Returns the purchaseOrderType.
     */
    public String getPurchaseOrderType() {
        return purchaseOrderType;
    }

    /**
     * Sets the purchaseOrderType attribute value.
     *
     * @param purchaseOrderType The purchaseOrderType to set.
     */
    public void setPurchaseOrderType(String purchaseOrderType) {
        this.purchaseOrderType = purchaseOrderType;
    }

    /**
     * Gets the purchaseOrderNumber attribute.
     *
     * @return Returns the purchaseOrderNumber.
     */
    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    /**
     * Sets the purchaseOrderNumber attribute value.
     *
     * @param purchaseOrderNumber The purchaseOrderNumber to set.
     */
    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    /**
     * Gets the standardNumber attribute.
     *
     * @return Returns the standardNumber.
     */
    public String getStandardNumber() {
        return standardNumber;
    }

    /**
     * Sets the standardNumber attribute value.
     *
     * @param standardNumber The standardNumber to set.
     */
    public void setStandardNumber(String standardNumber) {
        this.standardNumber = standardNumber;
    }

    /**
     * Gets the title attribute.
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title attribute value.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the journal attribute.
     * @return Returns the journal.
     *//*
    public String getJournal() {
        return journal;
    }

    *//**
     * Sets the journal attribute value.
     * @param journal The journal to set.
     *//*
    public void setJournal(String journal) {
        this.journal = journal;
    }*/

    /**
     * Gets the vendorName attribute.
     *
     * @return Returns the vendorName.
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * Sets the vendorName attribute value.
     *
     * @param vendorName The vendorName to set.
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * Gets the serials attribute.
     * @return Returns the serials.
     *//*
    public boolean isSerials() {
        return serials;
    }

    *//**
     * Sets the serials attribute value.
     * @param serials The serials to set.
     *//*
    public void setSerials(boolean serials) {
        this.serials = serials;
    }

    *//**
     * Gets the standingOrders attribute.
     * @return Returns the standingOrders.
     *//*
    public boolean isStandingOrders() {
        return standingOrders;
    }

    *//**
     * Sets the standingOrders attribute value.
     * @param standingOrders The standingOrders to set.
     *//*
    public void setStandingOrders(boolean standingOrders) {
        this.standingOrders = standingOrders;
    }

    *//**
     * Gets the vendor attribute.
     * @return Returns the vendor.
     *//*
    public boolean isVendor() {
        return vendor;
    }

    *//**
     * Sets the vendor attribute value.
     * @param vendor The vendor to set.
     *//*
    public void setVendor(boolean vendor) {
        this.vendor = vendor;
    }*/

    /**
     * Gets the monograph attribute.
     *
     * @return Returns the monograph.
     */
    public boolean isMonograph() {
        return monograph;
    }

    /**
     * Sets the monograph attribute value.
     *
     * @param monograph The monograph to set.
     */
    public void setMonograph(boolean monograph) {
        this.monograph = monograph;
    }

    /**
     * Gets the purchaseOrderDate attribute.
     * @return Returns the purchaseOrderDate.
     *//*
    public boolean isPurchaseOrderDate() {
        return purchaseOrderDate;
    }

    *//**
     * Sets the purchaseOrderDate attribute value.
     * @param purchaseOrderDate The purchaseOrderDate to set.
     *//*
    public void setPurchaseOrderDate(boolean purchaseOrderDate) {
        this.purchaseOrderDate = purchaseOrderDate;
    }*/

    /**
     * Gets the status attribute.
     * @return Returns the status.
     *//*
    public boolean isStatus() {
        return status;
    }

    *//**
     * Sets the status attribute value.
     * @param status The status to set.
     *//*
    public void setStatus(boolean status) {
        this.status = status;
    }*/

    /**
     * Gets the vendorDetail attribute.
     *
     * @return Returns the vendorDetail.
     */
    public VendorDetail getVendorDetail() {
        return vendorDetail;
    }

    /**
     * Sets the vendorDetail attribute value.
     *
     * @param vendorDetail The vendorDetail to set.
     */
    public void setVendorDetail(VendorDetail vendorDetail) {
        this.vendorDetail = vendorDetail;
    }

    /**
     * Gets the receive attribute.
     *
     * @return Returns the receive.
     */
    public boolean isReceive() {
        return receive;
    }

    /**
     * Sets the receive attribute value.
     *
     * @param receive The receive to set.
     */
    public void setReceive(boolean receive) {
        this.receive = receive;
    }

    /**
     * Gets the author attribute.
     *
     * @return Returns the author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author attribute value.
     *
     * @param author The author to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the publisher attribute.
     *
     * @return Returns the publisher.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher attribute value.
     *
     * @param publisher The publisher to set.
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Gets the edition attribute.
     *
     * @return Returns the edition.
     */
    public String getEdition() {
        return edition;
    }

    /**
     * Sets the edition attribute value.
     *
     * @param edition The edition to set.
     */
    public void setEdition(String edition) {
        this.edition = edition;
    }

    /**
     * Gets the quatityOrdered attribute.
     *
     * @return Returns the quatityOrdered.
     */
    public String getQuatityOrdered() {
        return quatityOrdered;
    }

    /**
     * Sets the quatityOrdered attribute value.
     *
     * @param quatityOrdered The quatityOrdered to set.
     */
    public void setQuatityOrdered(String quatityOrdered) {
        this.quatityOrdered = quatityOrdered;
    }

    /**
     * Gets the points attribute.
     *
     * @return Returns the points.
     */
    public String getPoints() {
        return points;
    }

    /**
     * Sets the points attribute value.
     *
     * @param points The points to set.
     */
    public void setPoints(String points) {
        this.points = points;
    }

    /**
     * Gets the instructions attribute.
     *
     * @return Returns the instructions.
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the instructions attribute value.
     *
     * @param instructions The instructions to set.
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * Gets the purchaseOrderDocument attribute.
     *
     * @return Returns the purchaseOrderDocument.
     */
    public PurchaseOrderDocument getPurchaseOrderDocument() {
        return purchaseOrderDocument;
    }

    /**
     * Sets the purchaseOrderDocument attribute value.
     *
     * @param purchaseOrderDocument The purchaseOrderDocument to set.
     */
    public void setPurchaseOrderDocument(PurchaseOrderDocument purchaseOrderDocument) {
        this.purchaseOrderDocument = purchaseOrderDocument;
    }

    /**
     * Gets the purchaseOrderItems attribute.
     *
     * @return Returns the purchaseOrderItems.
     */
    public List<OlePurchaseOrderItem> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }

    /**
     * Sets the purchaseOrderItems attribute value.
     *
     * @param purchaseOrderItems The purchaseOrderItems to set.
     */
    public void setPurchaseOrderItems(List<OlePurchaseOrderItem> purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    /**
     * Gets the receivingDocumentsList attribute.
     *
     * @return Returns the receivingDocumentsList.
     */

    public List<String> getReceivingDocumentsList() {
        return receivingDocumentsList;
    }

    /**
     * Sets the receivingDocumentsList attribute value.
     *
     * @param receivingDocumentsList The receivingDocumentsList to set.
     */
    public void setReceivingDocumentsList(List<String> receivingDocumentsList) {
        this.receivingDocumentsList = receivingDocumentsList;
    }

    @SuppressWarnings("rawtypes")

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap m = new LinkedHashMap();
        return m;
    }

    public boolean isPurchaseOrderDocumentAdded() {
        if (this.purchaseOrderItems != null) {
            return this.purchaseOrderItems.size() > 0;
        } else {
            return false;
        }
    }

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    /**
     * This method Takes Value from UI and return results after selecting search
     *
     * @throws Exception
     */
    public void receiveingQueueRecordSearch() {
        Long b1 = System.currentTimeMillis();
        Set<String> bibIds = new HashSet<String>();
        List<OlePurchaseOrderItem> purchaseOrderItemList = new ArrayList<>();
        List<OlePurchaseOrderItem> results = new ArrayList<>();
        boolean isOnlyDocCriteria = false;
        boolean isDbCriteriaExist = false;
        if ((StringUtils.isNotBlank(purchaseOrderNumber) || StringUtils.isNotBlank(purchaseOrderType) || StringUtils.isNotBlank(vendorName)
                || StringUtils.isNotBlank(purchaseOrderStatusDescription) || ObjectUtils.isNotNull(beginDate) || ObjectUtils.isNotNull(endDate))) {
            isDbCriteriaExist = true;
        }
        BigDecimal orderTypeId = null;
        if (StringUtils.isNotBlank(this.purchaseOrderType)) {
            Map<String, String> orderTypeMap = new HashMap<String, String>();
            orderTypeMap.put("purchaseOrderType", this.purchaseOrderType);
            List<PurchaseOrderType> items = (List<PurchaseOrderType>) KRADServiceLocator.getBusinessObjectService().findMatching(PurchaseOrderType.class, orderTypeMap);
            if (CollectionUtils.isNotEmpty(items)) {
                orderTypeId = items.get(0).getPurchaseOrderTypeId();
            }
        }
        if (!isDbCriteriaExist && (StringUtils.isNotBlank(this.title) || (StringUtils.isNotBlank(this.standardNumber)))) {
            isOnlyDocCriteria = true;
            List<String> newBibIds = new ArrayList<>();
            List<DocData> docDatas=getDocDatas(this.title, this.standardNumber);
            for (DocData docData : docDatas) {
                Map<String, String> poItemMap = new HashMap<String, String>();
                poItemMap.put("itemTitleId", docData.getBibIdentifier());
                poItemMap.put("itemTypeCode", "ITEM");
                List<OlePurchaseOrderItem> items = (List<OlePurchaseOrderItem>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, poItemMap);
                if (CollectionUtils.isNotEmpty(items)) {
                    for (OlePurchaseOrderItem orderItem : items) {
                        if (orderItem.isItemActiveIndicator() &&
                                orderItem.getItemType().isQuantityBasedGeneralLedgerIndicator() &&
                                orderItem.getItemType().isLineItemIndicator()) {
                            orderItem.setDocData(docData);
                            int purAppNum = orderItem.getPurchaseOrder().getPurapDocumentIdentifier();
                            String docNumber = orderItem.getPurchaseOrder().getDocumentNumber();
                            if (validatePurchaseOrderItem(orderItem) && validateRecords(purAppNum, docNumber)) {
                                newBibIds.add(orderItem.getItemTitleId());
                                results.add(orderItem);
                            }
                        }

                    }
                }
            }
            bibIds.clear();
            bibIds.addAll(newBibIds);
            this.setPurchaseOrderItems(results);
        } else {

            if (StringUtils.isNotBlank(purchaseOrderNumber)) {
                String[] purchaseOrderNumbers = (this.purchaseOrderNumber.toString()).split(",");
                for (int i = 0; i < purchaseOrderNumbers.length; i++) {
                    results.addAll(getSearchResults(purchaseOrderNumbers[i], bibIds, orderTypeId));
                }

            } else {
                results.addAll(getSearchResults("", bibIds, orderTypeId));
            }
            try {
                if (CollectionUtils.isNotEmpty(bibIds)) {
                    List<Bib> bibs = new ArrayList<>();
                    bibs.addAll(getDocstoreClientLocator().getDocstoreClient().acquisitionSearchRetrieveBibs(new ArrayList<String>(bibIds)));
                    if (bibIds != null && bibs != null) {
                        for (OlePurchaseOrderItem orderItem : results) {
                            if (orderItem.isItemActiveIndicator() &&
                                    orderItem.getItemType().isQuantityBasedGeneralLedgerIndicator() &&
                                    orderItem.getItemType().isLineItemIndicator()) {
                                inner:
                                for (Bib bib : bibs) {
                                    if (bib.getId().equals(orderItem.getItemTitleId())) {
                                        boolean isAllowed = true;
                                        boolean isTitle = true;
                                        boolean isIsbn = true;
                                        if (StringUtils.isNotBlank(this.title)) {
                                            if (!bib.getTitle().contains(this.title)) {
                                                isTitle = false;
                                            }
                                            isAllowed = false;
                                        }
                                        if (StringUtils.isNotBlank(this.standardNumber)) {
                                            if (!bib.getIsbn().equals(this.standardNumber)) {
                                                isIsbn = false;
                                            }
                                            isAllowed = false;
                                        }
                                        if (!isAllowed) {
                                            isAllowed = isIsbn && isTitle;
                                        }
                                        if (isAllowed) {
                                            DocData docData = new DocData();
                                            docData.setTitle(bib.getTitle());
                                            docData.setAuthor(bib.getAuthor());
                                            docData.setPublisher(bib.getPublisher());
                                            if (StringUtils.isNotBlank(bib.getIsbn())) {
                                                docData.setIsbn(bib.getIsbn());
                                            } else {
                                                docData.setIsbn(bib.getIssn());
                                            }

                                            docData.setLocalIdentifier(DocumentUniqueIDPrefix.getDocumentId(bib.getId()));
                                            docData.setBibIdentifier(bib.getId());
                                            orderItem.setDocData(docData);
                                            purchaseOrderItemList.add(orderItem);
                                            break inner;
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {

            }
            this.setPurchaseOrderItems(purchaseOrderItemList);
        }

    }

    public void receiveingQueueRecordSearchs() {
        queryCriteriaMap = new HashMap<>();
        StopWatch searchRecordWatch=new StopWatch();
        searchRecordWatch.start();
        Set<String> bibIds = new HashSet<String>();
        Map<String,DocData> docDataMap = new HashMap<>();
        List<OlePurchaseOrderItem> purchaseOrderItemList = new ArrayList<>();
        if ((StringUtils.isNotBlank(this.title) || (StringUtils.isNotBlank(this.standardNumber)))) {
            List<DocData> docDatas=getDocDatas(this.title, this.standardNumber);
            for (DocData docData : docDatas) {
                bibIds.add(docData.getBibIdentifier());
                docDataMap.put(docData.getBibIdentifier(),docData);
            }
            if(docDatas.size()==0){//If no title in docstore then title criteria is used to look for the title from eResource record, some item title of REQ and PO are set with eResource title
                queryCriteriaMap.put("title", title);
            }
            if(docDataMap.size()>0 || (docDataMap.size()==0 &&(this.standardNumber==null&&this.title==null))){
                purchaseOrderItemList=getSearchResultsFromQuery(null, bibIds);
                for(OlePurchaseOrderItem olePurchaseOrderItem:purchaseOrderItemList){
                    if(docDataMap.get(olePurchaseOrderItem.getItemTitleId())!=null){
                        olePurchaseOrderItem.setDocData(docDataMap.get(olePurchaseOrderItem.getItemTitleId()));
                    }
                }
            }
            this.setPurchaseOrderItems(purchaseOrderItemList);
        } else {
            purchaseOrderItemList.addAll(getSearchResultsFromQuery(null, bibIds));
            setBibInformations(purchaseOrderItemList,bibIds);
            //results.addAll(purchaseOrderItemList);
            this.setPurchaseOrderItems(purchaseOrderItemList);
        }
        if(purchaseOrderItemList.size()==0){
            GlobalVariables.getMessageMap().putInfo(OleSelectConstant.NO_RECORDS_FOUND,
                    OLEKeyConstants.MESSAGE_NO_RECORD_FOUND);
        }
        searchRecordWatch.stop();
    }

    private void setBibInformations(List<OlePurchaseOrderItem> purchaseOrderItemList,Set<String> bibIds){
        try {
            if (CollectionUtils.isNotEmpty(bibIds)) {
                List<Bib> bibs = new ArrayList<>();
                bibs.addAll(getDocstoreClientLocator().getDocstoreClient().acquisitionSearchRetrieveBibs(new ArrayList<String>(bibIds)));
                if (bibIds!=null && bibs!=null) {
                    for (OlePurchaseOrderItem orderItem : purchaseOrderItemList) {
                        inner:
                        for (Bib bib : bibs) {
                            if (bib.getId().equals(orderItem.getItemTitleId())) {
                                boolean isAllowed = true;
                                boolean isTitle = true;
                                boolean isIsbn = true;
                                if (StringUtils.isNotBlank(this.title)) {
                                    if (!bib.getTitle().contains(this.title)) {
                                        isTitle = false;
                                    }
                                    isAllowed = false;
                                }
                                if (StringUtils.isNotBlank(this.standardNumber)) {
                                    if (!bib.getIsbn().equals(this.standardNumber)) {
                                        isIsbn = false;
                                    }
                                    isAllowed = false;
                                }
                                if (!isAllowed) {
                                    isAllowed = isIsbn && isTitle;
                                }
                                if (isAllowed) {
                                    DocData docData = new DocData();
                                    docData.setTitle(bib.getTitle());
                                    docData.setAuthor(bib.getAuthor());
                                    docData.setPublisher(bib.getPublisher());
                                    if(StringUtils.isNotBlank(bib.getIsbn())){
                                        docData.setIsbn(bib.getIsbn());
                                    } else {
                                        docData.setIsbn(bib.getIssn());
                                    }

                                    docData.setLocalIdentifier(DocumentUniqueIDPrefix.getDocumentId(bib.getId()));
                                    docData.setBibIdentifier(bib.getId());
                                    orderItem.setDocData(docData);
                                    //purchaseOrderItemList.add(orderItem);
                                    break inner;
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<OlePurchaseOrderItem> getSearchResults(String poNumber, Set<String> bibIds, BigDecimal orderTypeId) {
        List<OlePurchaseOrderItem> results = new ArrayList<>();
        DocumentSearchCriteria.Builder docSearchCriteria = DocumentSearchCriteria.Builder.create();
        docSearchCriteria.setDocumentTypeName(PurapConstants.PurapDocTypeCodes.PO_DOCUMENT);
        List<DocumentStatus> documentStatuses = new ArrayList<>();
        documentStatuses.add(DocumentStatus.FINAL);
        Map<String, List<String>> fixedParameters = new HashMap<>();
        Map<String, List<String>> itemTitleIds = new HashMap<>();
        Map<String, List<String>> attributes = new HashMap<String, List<String>>();
        if (StringUtils.isNotBlank(poNumber))
            fixedParameters.put("purapDocumentIdentifier", Arrays.asList(poNumber));
        if (StringUtils.isNotBlank(vendorName)) {
            fixedParameters.put("vendorName", Arrays.asList(vendorName));
        }
        if (docSearchCriteria != null) {
            if (!fixedParameters.isEmpty()) {
                for (String propertyField : fixedParameters.keySet()) {
                    if (fixedParameters.get(propertyField) != null) {
                        attributes.put(propertyField, fixedParameters.get(propertyField));
                    }
                }
            }
        }
        docSearchCriteria.setDocumentAttributeValues(attributes);
        //docSearchCriteria.setSearchOptions(itemTitleIds);
        Date currentDate = new Date();
        docSearchCriteria.setDateCreatedTo(new DateTime(currentDate));
        //docSearchCriteria.setMaxResults(500);
        //docSearchCriteria.setApplicationDocumentStatus(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN);
        List<String> applicationStatus = new ArrayList<String>();
        applicationStatus.add(PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN);
        if (StringUtils.isNotBlank(this.purchaseOrderStatusDescription)) {
            applicationStatus.clear();
            applicationStatus.add(this.purchaseOrderStatusDescription);
        }
        docSearchCriteria.setApplicationDocumentStatuses(applicationStatus);
        try {
            if (ObjectUtils.isNotNull(this.beginDate)) {
                docSearchCriteria.setDateCreatedFrom(new DateTime(this.beginDate));
            }
            if (ObjectUtils.isNotNull(this.endDate)) {
                docSearchCriteria.setDateCreatedTo(new DateTime(this.endDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //docSearchCriteria.setDocumentStatuses(documentStatuses);
        List<String> purchaseOrderIds = new ArrayList<>();
        DocumentSearchCriteria docSearchCriteriaDTO = docSearchCriteria.build();
        DocumentSearchResults components = null;
        components = KEWServiceLocator.getDocumentSearchService().lookupDocuments(GlobalVariables.getUserSession().getPrincipalId(), docSearchCriteriaDTO);
        List<DocumentSearchResult> docSearchResults = components.getSearchResults();
        for (DocumentSearchResult searchResult : docSearchResults) {
            if (StringUtils.isNotBlank(searchResult.getDocument().getDocumentId())) {
                purchaseOrderIds.add(searchResult.getDocument().getDocumentId());
                Map<String, String> poItemMap = new HashMap<String, String>();
                poItemMap.put("documentNumber", searchResult.getDocument().getDocumentId());
                poItemMap.put("itemTypeCode", "ITEM");
                List<OlePurchaseOrderItem> items = (List<OlePurchaseOrderItem>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, poItemMap);
                if (CollectionUtils.isNotEmpty(items)) {
                    if (orderTypeId != null) {
                        for (OlePurchaseOrderItem orderItem : items) {
                            if (orderItem.getPurchaseOrder() != null && orderItem.getPurchaseOrder().getPurchaseOrderTypeId() != null && orderItem.getPurchaseOrder().getPurchaseOrderTypeId().equals(orderTypeId)) {
                                if (validatePurchaseOrderItem(orderItem)) {
                                    bibIds.add(orderItem.getItemTitleId());
                                    results.add(orderItem);
                                }
                            }
                        }
                    } else {
                        for (OlePurchaseOrderItem orderItem : items) {
                            if (validatePurchaseOrderItem(orderItem)) {
                                bibIds.add(orderItem.getItemTitleId());
                                results.add(orderItem);
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    public List<OlePurchaseOrderItem> getSearchResultsFromQuery(String poNumber, Set<String> bibIds) {
        List<OlePurchaseOrderItem> results = new ArrayList<>();
        queryCriteriaMap.put("bibIds",getBidsString(bibIds));
        queryCriteriaMap.put("purchaseOrderNumber", purchaseOrderNumber);
        queryCriteriaMap.put("vendorName", vendorName);
        queryCriteriaMap.put("claimFilter", claimFilter);
        //queryCriteria.put("monograph",monograph);
        queryCriteriaMap.put("standardNumber", standardNumber);
        queryCriteriaMap.put("purchaseOrderStatus", purchaseOrderStatusDescription);
        queryCriteriaMap.put("purchaseOrderType", purchaseOrderType);
        DocumentSearchCriteria.Builder docSearchCriteria = DocumentSearchCriteria.Builder.create();
        docSearchCriteria.setDocumentTypeName(PurapConstants.PurapDocTypeCodes.PO_DOCUMENT);
        Map<String, List<String>> fixedParameters = new HashMap<>();
        if (StringUtils.isNotBlank(poNumber))
            fixedParameters.put("purapDocumentIdentifier", Arrays.asList(poNumber));
        if (StringUtils.isNotBlank(vendorName)) {
            fixedParameters.put("vendorName", Arrays.asList(vendorName));
        }
        try {
            if (ObjectUtils.isNotNull(this.beginDate)) {
                queryCriteriaMap.put("poCreateFromDate",getFormattedDateForQuery(this.beginDate,false));
            }
            if (ObjectUtils.isNotNull(this.endDate)) {
                queryCriteriaMap.put("poCreateToDate",getFormattedDateForQuery(this.endDate,true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String watchName = "searchPO";
        StopWatch watch = new StopWatch();
        watch.start();
        ReceivingQueueDAOService receivingQueueDAOService = SpringContext.getBean(ReceivingQueueDAOService.class);
        List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = receivingQueueDAOService.getPODocumentList(queryCriteriaMap);
        watch.stop();
        StopWatch forPOWatch = new StopWatch();
        forPOWatch.start();
        for(OlePurchaseOrderDocument olePurchaseOrderDocument:olePurchaseOrderDocumentList){
            for(OlePurchaseOrderItem olePurchaseOrderItem:(List<OlePurchaseOrderItem>)olePurchaseOrderDocument.getItems()){
                if (olePurchaseOrderItem.getItemTitleId()!=null&&!bibIds.contains(olePurchaseOrderItem.getItemTitleId())) {
                    bibIds.add(olePurchaseOrderItem.getItemTitleId());
                }
                results.add(olePurchaseOrderItem);
            }
        }
        forPOWatch.stop();
        return results;
    }

    private String getBidsString(Set<String> bibIds){
        StringBuffer bibIdsString=new StringBuffer();
        Iterator bibIdIterator = bibIds.iterator();
        while(bibIdIterator.hasNext()){
            if(bibIdsString.length()>0){
                bibIdsString.append("','").append(bibIdIterator.next());
            }else{
                bibIdsString.append(bibIdIterator.next());
            }
        }
        if(bibIdsString.length()>0){
            return bibIdsString.toString();
        }
        return null;
    }
    private String getFormattedDateForQuery(String dateString,boolean toDate){
        SimpleDateFormat simpleDateFormat = null;
        String outputDate = null;
        try {
            String inputDateFormat = "MM/dd/yyyy";
            simpleDateFormat = new SimpleDateFormat(inputDateFormat);
            Date inputDate=simpleDateFormat.parse(dateString);
            if(toDate){
                inputDate=DateUtils.addDays(inputDate,1);//Incremented date, in sql if the input todate is passed it will fetch the record till the previous day,
                // so one day needs to be added inorder to bring the records till input todate
            }
            String outputDateFormat = "yyyy-MM-dd";
            simpleDateFormat = new SimpleDateFormat(outputDateFormat);
            outputDate = simpleDateFormat.format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }
    public boolean validatePurchaseOrderItem(OlePurchaseOrderItem olePurchaseOrderItem) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = dateFormat.format(new Date());
        String actionDateString = olePurchaseOrderItem.getClaimDate() != null ? dateFormat.format(olePurchaseOrderItem.getClaimDate()) : "";
        boolean isValid = true;
        PurchaseOrderDocument olePurchaseOrderDocument = olePurchaseOrderItem.getPurapDocument();
      /*  int purAppNum = olePurchaseOrderDocument.getPurapDocumentIdentifier();
        olePurchaseOrderDocument = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(purAppNum);*/

        Map purchaseOrderTypeIdMap = new HashMap();
        purchaseOrderTypeIdMap.put("purchaseOrderTypeId", olePurchaseOrderDocument.getPurchaseOrderTypeId());
        org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        List<PurchaseOrderType> purchaseOrderTypeDocumentList = (List) businessObject.findMatching(PurchaseOrderType.class, purchaseOrderTypeIdMap);

        int purAppNum = olePurchaseOrderDocument.getPurapDocumentIdentifier();
        olePurchaseOrderDocument = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(purAppNum);
        String docNumber = olePurchaseOrderDocument.getDocumentNumber();
        StringBuffer specialNotesPOIDStringBuffer = new StringBuffer();
        isValid = isValid && validateRecords(purAppNum, docNumber);
        isValid = isValid && validatePurchaseOrderStatus(purchaseOrderTypeDocumentList, olePurchaseOrderDocument);
        isValid = isValid && validatePoByRetiredVersionStatus(olePurchaseOrderDocument);
        isValid = isValid && !(checkSpecialHandlingNotesExsist(olePurchaseOrderItem,specialNotesPOIDStringBuffer));
        if (specialNotesPOIDStringBuffer.length()>0) {
            GlobalVariables.getMessageMap().putWarning(OleSelectConstant.NOTES_WARNING, OLEKeyConstants.WARNING_SPECAIL_NOTES,new String[]{specialNotesPOIDStringBuffer.toString()});
        }
        //isValid =isValid && validateCopiesAndParts(olePurchaseOrderItem);

        if (isValid) {
            boolean serialPOLink = olePurchaseOrderItem.getCopyList() != null && olePurchaseOrderItem.getCopyList().size() > 0 ? olePurchaseOrderItem.getCopyList().get(0).getSerialReceivingIdentifier() != null : false;
            boolean continuing = (purchaseOrderTypeDocumentList != null && purchaseOrderTypeDocumentList.size() > 0 ?
                    purchaseOrderTypeDocumentList.get(0).getPurchaseOrderType().equalsIgnoreCase("Continuing") : false);
            if (olePurchaseOrderItem.getReceiptStatusId() != null && olePurchaseOrderItem.getReceiptStatusId().toString().equalsIgnoreCase((String.valueOf(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_FULLY_RECEIVED))))) {
                GlobalVariables.clear();
                GlobalVariables.getMessageMap().putInfo(OleSelectConstant.RECEIVING_QUEUE_SEARCH,
                        OLEKeyConstants.ERROR_NO_PURCHASEORDERS_FOUND_FOR_FULLY_RECEIVED);
            } else if (this.isClaimFilter()) {
                if (!olePurchaseOrderItem.isDoNotClaim() && olePurchaseOrderItem.getClaimDate() != null && (actionDateString.equalsIgnoreCase(dateString) || olePurchaseOrderItem.getClaimDate().before(new Date()))
                        && !serialPOLink && !continuing) {
                    olePurchaseOrderItem.setClaimFilter(true);
                    isValid = isValid & true;
                }
            } else {
                if (!olePurchaseOrderItem.isDoNotClaim() && olePurchaseOrderItem.getClaimDate() != null && (actionDateString.equalsIgnoreCase(dateString) || olePurchaseOrderItem.getClaimDate().before(new Date()))
                        && !serialPOLink && !continuing) {
                    olePurchaseOrderItem.setClaimFilter(true);
                }
                isValid = isValid & true;
            }
        }

        return isValid;
    }


    public Set<String> getDocSearchResults(String title, String isbn) {
        int maxLimit = Integer.parseInt(SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSEARCH_ORDERQUEUE_LIMIT_KEY));
        Set<String> itemTitles = new HashSet<>();
        try {
            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
            org.kuali.ole.docstore.common.search.SearchParams searchParams = new org.kuali.ole.docstore.common.search.SearchParams();
            searchParams.setPageSize(maxLimit);
            if (StringUtils.isNotBlank(title)) {
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), TITLE_SEARCH, title), "AND"));
            }
            if (StringUtils.isNotBlank(isbn)) {
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), ISBN_SEARCH, isbn), "OR"));
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), ISSN_SEARCH, isbn), "OR"));
            }
            SearchResponse searchResponse = null;
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), BIB_IDENTIFIER));
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if (StringUtils.isNotBlank(searchResultField.getFieldValue())) {
                        if (searchResultField.getFieldName().equals("bibIdentifier")) {
                            itemTitles.add(searchResultField.getFieldValue());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "Item Exists");
            LOG.error(org.kuali.ole.OLEConstants.ITEM_EXIST + ex);
        }

        return itemTitles;
    }

    public List<DocData> getDocDatas(String title, String isbn) {
        List<DocData> docDatas=new ArrayList<>();
        int maxLimit = Integer.parseInt(getConfigurationService().getPropertyValueAsString(OLEConstants.DOCSEARCH_ORDERQUEUE_LIMIT_KEY));
        Set<String> itemTitles = new HashSet<>();
        try {
            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
            org.kuali.ole.docstore.common.search.SearchParams searchParams = new org.kuali.ole.docstore.common.search.SearchParams();
            searchParams.setPageSize(maxLimit);
            if (StringUtils.isNotBlank(title)) {
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), TITLE_SEARCH, title), "AND"));
            }
            if (StringUtils.isNotBlank(isbn)) {
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), ISBN_SEARCH, isbn), "OR"));
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), ISSN_SEARCH, isbn), "OR"));
            }
            SearchResponse searchResponse = null;
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), TITLE_DISPLAY));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), AUTHOR_DISPLAY));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), PUBLISHER_DISPLAY));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), ISBN_DISPLAY));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), BIB_IDENTIFIER));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), ISSN_DISPLAY));
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                DocData docData=new DocData();
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if (StringUtils.isNotBlank(searchResultField.getFieldValue())) {
                        if (searchResultField.getFieldName().equals(BIB_IDENTIFIER)) {
                            docData.setBibIdentifier(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equals(TITLE_DISPLAY)) {
                            docData.setTitle(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equals(AUTHOR_DISPLAY)) {
                            docData.setAuthor(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equals(PUBLISHER_DISPLAY)) {
                            docData.setPublisher(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equals(ISBN_DISPLAY) && StringUtils.isNotBlank(searchResultField.getFieldValue())) {
                            docData.setIsbn(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equals(ISSN_DISPLAY) && StringUtils.isNotBlank(searchResultField.getFieldValue())) {
                            docData.setIsbn(searchResultField.getFieldValue());
                        }

                    }
                }
                docDatas.add(docData);
            }
        } catch (Exception ex) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "Item Exists");
            LOG.error(org.kuali.ole.OLEConstants.ITEM_EXIST + ex);
        }

        return docDatas;
    }

    private boolean validatePoByRetiredVersionStatus(PurchaseOrderDocument olePurchaseOrderDocument) {
        return (olePurchaseOrderDocument.getPurchaseOrderCurrentIndicatorForSearching());
    }

    private boolean validatePurchaseOrderStatus(List<PurchaseOrderType> purchasrOrderTypeList, PurchaseOrderDocument po) {
        boolean valid = false;
        for (PurchaseOrderType purchaseOrderTypes : purchasrOrderTypeList) {
            BigDecimal poTypeId = purchaseOrderTypes.getPurchaseOrderTypeId();
            if (poTypeId.compareTo(po.getPurchaseOrderTypeId()) == 0) {
                valid = true;
            }
        }
        return valid;
    }


    public void valueSearch() throws Exception {
        String[] purchaseOrderNumbers = {};
        Collection results = new ArrayList<OlePurchaseOrderItem>();
        Map purapDocumentIdentifierMap = new HashMap();
        if (this.purchaseOrderNumber != null) {
            purchaseOrderNumbers = (this.purchaseOrderNumber.toString()).split(",");
            for (int i = 0; i < purchaseOrderNumbers.length; i++) {
                // TODO Auto-generated method stub
                List<String> poDocNumbers = new ArrayList<String>();
                LOG.debug(" OleReceivingQueueSearchDocument.valueSearch method starts ");
                purapDocumentIdentifierMap = new HashMap();

                if (purchaseOrderNumbers[i] != null) {
                    purapDocumentIdentifierMap.put("purchaseOrder.purapDocumentIdentifier", purchaseOrderNumbers[i]);
                }
                if (this.vendorName != null) {
                    purapDocumentIdentifierMap.put("purchaseOrder.vendorName", this.vendorName);
                }
                if (this.title != null) {
                    purapDocumentIdentifierMap.put("docData.title", this.title);
                }
                if (this.standardNumber != null) {
                    purapDocumentIdentifierMap.put("docData.isbn", this.standardNumber);
                }
                results.addAll(SpringContext.getBean(OleDocStoreLookupService.class).findCollectionBySearch(OlePurchaseOrderItem.class, purapDocumentIdentifierMap));
            }

        } else {
            purapDocumentIdentifierMap = new HashMap();
            if (this.purchaseOrderNumber != null) {
                purapDocumentIdentifierMap.put("purchaseOrder.purapDocumentIdentifier", this.purchaseOrderNumber);
            }
            if (this.vendorName != null) {
                purapDocumentIdentifierMap.put("purchaseOrder.vendorName", this.vendorName);
            }
            if (this.title != null) {
                purapDocumentIdentifierMap.put("docData.title", this.title);
            }
            if (this.standardNumber != null) {
                purapDocumentIdentifierMap.put("docData.isbn", this.standardNumber);
            }
            results.addAll(SpringContext.getBean(OleDocStoreLookupService.class).findCollectionBySearch(OlePurchaseOrderItem.class, purapDocumentIdentifierMap));
        }


        //results from docstore
        // Collection results = SpringContext.getBean(OleDocStoreLookupService.class).findCollectionBySearch(OlePurchaseOrderItem.class, purapDocumentIdentifierMap);
        boolean isbeginEndDateExsist = false;
        boolean isEndDateSmallerThanBeginDate = false;
        List<OlePurchaseOrderItem> tempResult = (List<OlePurchaseOrderItem>) results;
        List<String> docNumberList = new ArrayList<String>();

        //This code executes if begin date and end date is not null and checks whether enddate is greatter than begin date
        if (this.beginDate != null && this.endDate != null) {
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date startDate = formatter.parse(this.beginDate);
            Date endDate = formatter.parse(this.endDate);
            if (startDate.compareTo(endDate) > 0) {
                isEndDateSmallerThanBeginDate = true;
                GlobalVariables.getMessageMap().putError(OleSelectConstant.RECEIVING_QUEUE_SEARCH, OLEKeyConstants.PUR_ORD_DATE_TO_NOT_LESSER_THAN_PUR_ORD_DATE_FROM, new String[]{});
            }
        }

        //if enddate is greater than begin date this code will not execute
        /*if((this.beginDate!=null || this.endDate!=null) && !isEndDateSmallerThanBeginDate) {
            docNumberList= filterOtherSearchCriteria();
            isbeginEndDateExsist = true;
        }*/

        if (!isEndDateSmallerThanBeginDate) {
            if ((this.beginDate != null || this.endDate != null)) {
                docNumberList = filterOtherSearchCriteria();
                isbeginEndDateExsist = true;
            }
            for (int i = 0; i < tempResult.size(); i++) {
                int purAppNum = tempResult.get(i).getPurchaseOrder().getPurapDocumentIdentifier();
                String docNumber = tempResult.get(i).getPurchaseOrder().getDocumentNumber();

                // int itemLineNumber = tempResult.get(i).getItemLineNumber();
                boolean isValidRecord = validateRecords(purAppNum, docNumber);
                boolean isSpecHandlingNotesExist = false;
                /*
                 * if(isValidRecord) { isValidRecord = checkForReceivingLineItem(purAppNum,itemLineNumber); }
                 */
                if (isValidRecord) {
                    isValidRecord = validateCopiesAndParts(tempResult.get(i));
                }
                if (tempResult.get(i).getNotes() != null) {
                    isSpecHandlingNotesExist = checkSpecialHandlingNotesExsist(tempResult.get(i));
                }
                if (this.purchaseOrderType != null && isValidRecord) {
                    isValidRecord = validatePurchaseOrderStatus(this.purchaseOrderType, purAppNum);
                }
                if (isbeginEndDateExsist) {
                    if (!docNumberList.contains(docNumber)) {
                        isValidRecord = false;
                    }
                }
                boolean isRetiredVersionPo = !(validatePoByRetiredVersionStatus((OlePurchaseOrderItem) tempResult.get(i)));
                OlePurchaseOrderItem olePurchaseOrderItem = tempResult.get(i);
                if (!isValidRecord || isRetiredVersionPo || isSpecHandlingNotesExist) {
                    tempResult.remove(i);
                    i--;
                }
            }

            if (tempResult.size() <= 0) {
                if(!GlobalVariables.getMessageMap().hasInfo()) {
                    GlobalVariables.getMessageMap().putInfo(OleSelectConstant.RECEIVING_QUEUE_SEARCH,
                            OLEKeyConstants.ERROR_NO_PURCHASEORDERS_FOUND);
                }
            }
            this.setPurchaseOrderItems(removeReceivedTitles(tempResult));
            if(this.getPurchaseOrderItems().size()>0){
                GlobalVariables.clear();
            }
        }

        LOG.debug(" OleReceivingQueueSearchDocument.valueSearch method ends ");
    }

    private List<OlePurchaseOrderItem> removeReceivedTitles(List<OlePurchaseOrderItem> purchaseOrderItems){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = dateFormat.format(new Date());
        List<OlePurchaseOrderItem> result=new ArrayList<OlePurchaseOrderItem>();
        for(OlePurchaseOrderItem olePurchaseOrderItem:purchaseOrderItems){
            String actionDateString = olePurchaseOrderItem.getClaimDate()!=null ? dateFormat.format(olePurchaseOrderItem.getClaimDate()) : "";
            boolean serialPOLink = olePurchaseOrderItem.getCopyList()!=null && olePurchaseOrderItem.getCopyList().size()>0 ? olePurchaseOrderItem.getCopyList().get(0).getSerialReceivingIdentifier()!=null : false ;
            OlePurchaseOrderDocument olePurchaseOrderDocument = olePurchaseOrderItem.getPurapDocument();
            Map purchaseOrderTypeIdMap = new HashMap();
            purchaseOrderTypeIdMap.put("purchaseOrderTypeId", olePurchaseOrderDocument.getPurchaseOrderTypeId());
            org.kuali.rice.krad.service.BusinessObjectService businessObject = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
            List<PurchaseOrderType> purchaseOrderTypeDocumentList = (List) businessObject.findMatching(PurchaseOrderType.class, purchaseOrderTypeIdMap);
            boolean  continuing = purchaseOrderTypeDocumentList!=null && purchaseOrderTypeDocumentList.size()>0?
                    purchaseOrderTypeDocumentList.get(0).getPurchaseOrderType().equalsIgnoreCase("Continuing"):false;
            if(olePurchaseOrderItem.getReceiptStatusId()!=null&&olePurchaseOrderItem.getReceiptStatusId().toString().equalsIgnoreCase((String.valueOf(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_FULLY_RECEIVED))))){
                GlobalVariables.clear();
                GlobalVariables.getMessageMap().putInfo(OleSelectConstant.RECEIVING_QUEUE_SEARCH,
                        OLEKeyConstants.ERROR_NO_PURCHASEORDERS_FOUND_FOR_FULLY_RECEIVED);
            }
            else if(this.isClaimFilter()){
                if(!olePurchaseOrderItem.isDoNotClaim() && olePurchaseOrderItem.getClaimDate()!=null && (actionDateString.equalsIgnoreCase(dateString) || olePurchaseOrderItem.getClaimDate().before(new Date()))
                        && !serialPOLink && !continuing){
                    olePurchaseOrderItem.setClaimFilter(true);
                    result.add(olePurchaseOrderItem);
                }
            }else {
                if(!olePurchaseOrderItem.isDoNotClaim() && olePurchaseOrderItem.getClaimDate()!=null && (actionDateString.equalsIgnoreCase(dateString) || olePurchaseOrderItem.getClaimDate().before(new Date()))
                        && !serialPOLink && !continuing){
                    olePurchaseOrderItem.setClaimFilter(true);
                }
                result.add(olePurchaseOrderItem);
            }

        }
        return result;
    }
    private List<OlePurchaseOrderItem> removeFullyReceivedPO(List<OlePurchaseOrderItem> purchaseOrderItems){
        List<OlePurchaseOrderItem> result=new ArrayList<OlePurchaseOrderItem>();
        Map<String,List<OlePurchaseOrderItem>> listMap=new HashMap<String,List<OlePurchaseOrderItem>>();
        for (OlePurchaseOrderItem olePurchaseOrderItem : purchaseOrderItems) {
            if(olePurchaseOrderItem.getItemTypeCode().equalsIgnoreCase("Item")){
                if(!listMap.containsKey(olePurchaseOrderItem.getDocumentNumber())){
                    List<OlePurchaseOrderItem> orderItems=new ArrayList<OlePurchaseOrderItem>();
                    orderItems.add(olePurchaseOrderItem);
                    listMap.put(olePurchaseOrderItem.getDocumentNumber(),orderItems);
                } else {
                    for(Map.Entry<String,List<OlePurchaseOrderItem>> entry:listMap.entrySet()){
                        if(entry.getKey().equalsIgnoreCase(olePurchaseOrderItem.getDocumentNumber())){
                            List<OlePurchaseOrderItem> orderItems=entry.getValue();
                            orderItems.add(olePurchaseOrderItem);
                            entry.setValue(orderItems);
                        }
                    }
                }

            }
        }
        for(Map.Entry<String,List<OlePurchaseOrderItem>> entry:listMap.entrySet()){
            int size=((List<OlePurchaseOrderItem>)entry.getValue()).size();
            boolean isFullyReceived=false;
            int count=0;
            List<OlePurchaseOrderItem> orderItems=entry.getValue();
            for(OlePurchaseOrderItem olePurchaseOrderItem:orderItems){
                if(olePurchaseOrderItem.getReceiptStatusId()!=null&&olePurchaseOrderItem.getReceiptStatusId().toString().equalsIgnoreCase("5")){
                    count++;
                }
            }
            if(size!=count){
                result.addAll(orderItems);
            }
        }


        return result;
    }

    private boolean validatePoByRetiredVersionStatus(OlePurchaseOrderItem olePurchaseOrderItem) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("documentNumber", olePurchaseOrderItem.getDocumentNumber());
        PurchaseOrderDocument po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(olePurchaseOrderItem.getPurapDocumentIdentifier());
       /* po.getPurchaseOrderCurrentIndicatorForSearching(); */   /**/
        List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = (List<OlePurchaseOrderDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, map);
        OlePurchaseOrderDocument olePurchaseOrderDocument = olePurchaseOrderDocumentList.get(0);
        return (olePurchaseOrderDocument.getPurchaseOrderCurrentIndicatorForSearching());
    }

    private boolean validatePurchaseOrderStatus(String purchaseOrderType, Integer purAppNum) {
        boolean valid = false;
        PurchaseOrderDocument po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(purAppNum);
        /*
         * if(purchaseOrderStatus.equalsIgnoreCase(po.getStatusCode())) valid = true;
         */
        Map purchaseOrderTypeMap = new HashMap();
        purchaseOrderTypeMap.put("purchaseOrderType", purchaseOrderType);
        List<PurchaseOrderType> purchasrOrderTypeList = (List) SpringContext.getBean(BusinessObjectService.class)
                .findMatching(PurchaseOrderType.class, purchaseOrderTypeMap);
        for (PurchaseOrderType purchaseOrderTypes : purchasrOrderTypeList) {
            BigDecimal poTypeId = purchaseOrderTypes.getPurchaseOrderTypeId();
            if (poTypeId.compareTo(po.getPurchaseOrderTypeId()) == 0) {
                valid = true;
            }
        }
        return valid;
    }

    public boolean checkSpecialHandlingNotesExsist(OlePurchaseOrderItem olePurchaseOrderItem) {
        for (OleNotes poNote : olePurchaseOrderItem.getNotes()) {
            OleNoteType oleNoteType = SpringContext.getBean(OleNoteTypeService.class).getNoteTypeDetails(
                    poNote.getNoteTypeId());
            String noteType = oleNoteType.getNoteType();
            if (noteType.equalsIgnoreCase(OLEConstants.SPECIAL_PROCESSING_INSTRUCTION_NOTE)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("PO ID " + olePurchaseOrderItem.getPurapDocumentIdentifier()
                            + "has special handling notes");
                }
                return true;
            }
        }
        return false;
    }

    public boolean checkSpecialHandlingNotesExsist(OlePurchaseOrderItem olePurchaseOrderItem,StringBuffer specialNotesPOIDStringBuffer) {
        for (OleNotes poNote : olePurchaseOrderItem.getNotes()) {
            OleNoteType oleNoteType = getOleNoteTypeService().getNoteTypeDetails(
                    poNote.getNoteTypeId());
            String noteType = oleNoteType.getNoteType();
            if (noteType.equalsIgnoreCase(OLEConstants.SPECIAL_PROCESSING_INSTRUCTION_NOTE)) {
                if(specialNotesPOIDStringBuffer.length()>0){
                    specialNotesPOIDStringBuffer.append(",");
                }
                specialNotesPOIDStringBuffer.append(olePurchaseOrderItem.getPurchaseOrder().getPurapDocumentIdentifier());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("PO ID " + olePurchaseOrderItem.getPurapDocumentIdentifier()
                            + "has special handling notes");
                }
                return true;
            }
        }
        return false;
    }

    /*
     * public boolean checkForReceivingLineItem(Integer purAppNum, int itemLineNumber){ boolean valid=true; Map<String,Object>
     * poIdMap = new HashMap<String,Object>(); poIdMap.put("purchaseOrderIdentifier", purAppNum); List<OleLineItemReceivingDocument>
     * oleLineItemReceivingDocumentList =
     * (List)SpringContext.getBean(BusinessObjectService.class).findMatching(OleLineItemReceivingDocument.class, poIdMap); for(int
     * i=0;i<oleLineItemReceivingDocumentList.size();i++) { String docNum =
     * oleLineItemReceivingDocumentList.get(i).getDocumentNumber(); Map<String,Object> lineItemDocNumMap = new
     * HashMap<String,Object>(); lineItemDocNumMap.put("documentNumber", docNum); List<OleLineItemReceivingItem>
     * oleLineItemReceivingItemList =
     * (List)SpringContext.getBean(BusinessObjectService.class).findMatching(OleLineItemReceivingItem.class, lineItemDocNumMap);
     * if(oleLineItemReceivingItemList.size()>0) valid &= false; //Iterator itr = oleLineItemReceivingItemList.iterator(); //int
     * itmLineNumber = oleLineItemReceivingItemList.iterator().next().getItemLineNumber(); //if(itemLineNumber == itmLineNumber) {
     * // } } return valid; }
     */

    /**
     * This method validate records and returns boolean based on the validation
     *
     * @param purAppNum
     * @param docNumber
     * @return boolean
     */
    public boolean validateRecords(Integer purAppNum, String docNumber) {
        boolean valid = true;
        // Commented for checking receiving document checking

        /*
         * PurchaseOrderDocument po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(purAppNum); if
         * (ObjectUtils.isNull(po)) { valid &= false; } Map<String,Object> poIdMap = new HashMap<String,Object>();
         * poIdMap.put("purchaseOrderIdentifier", purAppNum); List<OleLineItemReceivingDocument> oleLineItemReceivingDocumentList =
         * (List)SpringContext.getBean(BusinessObjectService.class).findMatching(OleLineItemReceivingDocument.class, poIdMap);
         * if(oleLineItemReceivingDocumentList.size()>0) { valid &= false; } if
         * (!SpringContext.getBean(ReceivingService.class).isPurchaseOrderActiveForLineItemReceivingDocumentCreation(purAppNum)){
         * valid &= false; } if( SpringContext.getBean(ReceivingService.class).canCreateLineItemReceivingDocument(purAppNum, null)
         * == false){ String inProcessDocNum = ""; List<String> inProcessDocNumbers =
         * SpringContext.getBean(ReceivingService.class).getLineItemReceivingDocumentNumbersInProcessForPurchaseOrder(purAppNum,
         * null); if (!inProcessDocNumbers.isEmpty()) { // should not be empty if we reach this point inProcessDocNum =
         * inProcessDocNumbers.get(0); } valid &= false; }
         */
        /*if(!po.getApplicationDocumentStatus().equalsIgnoreCase(OleSelectConstant.PURCHASEORDER_STATUS_OPEN) && valid){
            valid &= false;
        }
        else if(po.getApplicationDocumentStatus().equalsIgnoreCase(OleSelectConstant.PURCHASEORDER_STATUS_OPEN) && valid) {
            Map purchaseOrderIdMap = new HashMap();
            purchaseOrderIdMap.put("documentNumber", docNumber);
            List<OlePurchaseOrderDocument> purchasrOrderList = (List)SpringContext.getBean(BusinessObjectService.class).findMatching(OlePurchaseOrderDocument.class, purchaseOrderIdMap);
            for(int i=0;i<purchasrOrderList.size();i++){
                if(!purchasrOrderList.get(i).getApplicationDocumentStatus().equalsIgnoreCase(OleSelectConstant.PURCHASEORDER_STATUS_OPEN)){
                    valid &= false;
                }
            }
        }*/
        PurchaseOrderDocument po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(purAppNum);
        valid &= getReceivingService().canCreateLineItemReceivingDocument(purAppNum, null);
        if (this.purchaseOrderStatusDescription != null && valid) {
            if (!purchaseOrderStatusDescription.equalsIgnoreCase(po.getApplicationDocumentStatus())) {
                valid &= false;
            }

        }
        if(po.getApplicationDocumentStatus().equalsIgnoreCase("closed")){
            valid &= false;
            GlobalVariables.clear();
            GlobalVariables.getMessageMap().putInfo(OleSelectConstant.RECEIVING_QUEUE_SEARCH,
                    OLEKeyConstants.ERROR_NO_PURCHASEORDERS_FOUND_FOR_CLOSED);

        }
        // Check for existence of Special Handling Notes in PO. Create Line Item Receiving Document only if there are no Special handling notes
        /*if(hasSpecialHandlingNotes(po)){
            valid &= false;
        }*/
        return valid;
    }


    /**
     * This method checks whether Purchase order exists with given begin date and end date is exists
     *
     * @return List of Document Numbers
     * @throws WorkflowException
     */
    public List<String> filterOtherSearchCriteria() throws WorkflowException, ParseException {
        // date lookup
        Map<String, List<String>> fixedParameters = new HashMap<String, List<String>>();
        if (ObjectUtils.isNotNull(this.beginDate)) {
            fixedParameters.put(OleSelectConstant.FROM_DATE_CREATED, Collections.singletonList(this.beginDate));
        } else {
            fixedParameters.put(OleSelectConstant.FROM_DATE_CREATED, Collections.singletonList(""));
        }
        if (ObjectUtils.isNotNull(this.endDate)) {
            fixedParameters.put(OleSelectConstant.TO_DATE_CREATED, Collections.singletonList(this.endDate));
        }

        List<String> docNumber = new ArrayList<String>();
        if (!((fixedParameters.get(OleSelectConstant.FROM_DATE_CREATED) == null || fixedParameters.get(OleSelectConstant.FROM_DATE_CREATED).get(0).isEmpty()) &&
                (fixedParameters.get(OleSelectConstant.TO_DATE_CREATED) == null || fixedParameters.get(OleSelectConstant.TO_DATE_CREATED).get(0).isEmpty()))) {
            docNumber = filterWorkflowStatusDate(fixedParameters);
            isDateLookup = true;
        }

        return docNumber;
    }

    /**
     * This method checks whether Purchase order exists with given begin date and end date
     *
     * @param fixedParameters
     * @return List of Document Numbers
     */
    public List<String> filterWorkflowStatusDate(Map<String, List<String>> fixedParameters) throws ParseException {
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentAttributeValues(fixedParameters);
        if (StringUtils.isNotEmpty(this.beginDate)) {
            criteria.setDateCreatedFrom(new DateTime((SpringContext.getBean(DateTimeService.class)).convertToDate(beginDate)));
        }
        if (StringUtils.isNotEmpty(this.endDate)) {
            criteria.setDateCreatedTo(new DateTime((SpringContext.getBean(DateTimeService.class)).convertToDate(endDate)));
        }
        List<String> documentNumberList = new ArrayList();
        boolean isDateSpecified = true;
        try {
            DocumentSearchResults components = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(GlobalVariables.getUserSession().getPrincipalId(), criteria.build());
            List<DocumentSearchResult> docSearchResults = components.getSearchResults();
            for (DocumentSearchResult docSearchResult : docSearchResults) {
                documentNumberList.add(docSearchResult.getDocument().getDocumentId());
            }
        } catch (WorkflowServiceErrorException wsee) {
            for (WorkflowServiceError workflowServiceError : (List<WorkflowServiceError>) wsee.getServiceErrors()) {
                if (workflowServiceError.getMessageMap() != null && workflowServiceError.getMessageMap().hasErrors()) {
                    // merge the message maps
                    GlobalVariables.getMessageMap().merge(workflowServiceError.getMessageMap());
                } else {
                    //TODO: can we add something to this to get it to highlight the right field too?  Maybe in arg1
                    GlobalVariables.getMessageMap().putError(workflowServiceError.getMessage(), RiceKeyConstants.ERROR_CUSTOM, workflowServiceError.getMessage());
                }
            }
            ;
        }
        return documentNumberList;
    }

    /**
     * This method validates if a receiving document can be created from a POID.
     * If all validations are passed, it initiates and submits receiving document for a POID.
     * This method does only complete receiving and populates error messages in case of exceptions.
     *
     * @param rlDoc                   OleLineItemReceivingDocument
     * @param purchaseOrderIdentifier Purchase Order Id
     */
    public boolean receivePO(OleLineItemReceivingDocument rlDoc, Integer purchaseOrderIdentifier, boolean isCreateRCV) {
        LOG.debug("Inside receivePO of OleReceivingQueueSearchDocument");
        GlobalVariables.getMessageMap().clearErrorPath();
        GlobalVariables.getMessageMap().addToErrorPath(OLEPropertyConstants.DOCUMENT);

        boolean receivePOSuccess = false;

        // Setting defaults
        rlDoc.setPurchaseOrderIdentifier(purchaseOrderIdentifier);
        DateTimeService dateTimeService = SpringContext.getBean(DateTimeService.class);
        rlDoc.setShipmentReceivedDate(dateTimeService.getCurrentSqlDate());

        if (LOG.isDebugEnabled()) {
            LOG.debug("PO ID in OleReceivingQueueSearchDocument.receivePO -" + purchaseOrderIdentifier);
        }

        // Validations Start
        boolean valid = true;

        PurchaseOrderDocument po = SpringContext.getBean(PurchaseOrderService.class).getCurrentPurchaseOrder(purchaseOrderIdentifier);

        if (ObjectUtils.isNotNull(po)) {
            rlDoc.setAccountsPayablePurchasingDocumentLinkIdentifier(po.getAccountsPayablePurchasingDocumentLinkIdentifier());
            if (!SpringContext.getBean(DocumentHelperService.class).getDocumentAuthorizer(rlDoc).isAuthorizedByTemplate(rlDoc, KRADConstants.KNS_NAMESPACE, KimConstants.PermissionTemplateNames.OPEN_DOCUMENT, GlobalVariables.getUserSession().getPrincipalId())) {
                throw new DocumentAuthorizationException(GlobalVariables.getUserSession().getPerson().getPrincipalName(), "initiate document", rlDoc.getDocumentNumber());
            }
        } else {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_RECEIVING_LINE_PO_NOT_EXIST, rlDoc.getPurchaseOrderIdentifier().toString());
        }


        if (!SpringContext.getBean(ReceivingService.class).isPurchaseOrderActiveForLineItemReceivingDocumentCreation(rlDoc.getPurchaseOrderIdentifier())) {
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_RECEIVING_LINE_PONOTACTIVE, rlDoc.getPurchaseOrderIdentifier().toString());
            valid &= false;
        }

        if (SpringContext.getBean(ReceivingService.class).canCreateLineItemReceivingDocument(rlDoc.getPurchaseOrderIdentifier(), rlDoc.getDocumentNumber()) == false) {
            String inProcessDocNum = "";
            List<String> inProcessDocNumbers = SpringContext.getBean(ReceivingService.class).getLineItemReceivingDocumentNumbersInProcessForPurchaseOrder(rlDoc.getPurchaseOrderIdentifier(), rlDoc.getDocumentNumber());
            if (!inProcessDocNumbers.isEmpty()) {    // should not be empty if we reach this point
                inProcessDocNum = inProcessDocNumbers.get(0);
            }
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_RECEIVING_LINE_DOCUMENT_ACTIVE_FOR_PO, rlDoc.getPurchaseOrderIdentifier().toString(), inProcessDocNum);
            valid &= false;
        }

        // Check for existence of Special Handling Notes in PO. Create Line Item Receiving Document only if there are no Special handling notes
        /*if(hasSpecialHandlingNotes(po)){
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_PO_HAS_SPECIAL_HANDLING_NOTES, po.getPurapDocumentIdentifier().toString());
            valid &= false;
        }*/

        // Validations Ends
        if (LOG.isDebugEnabled()) {
            LOG.debug("PO ID in OleReceivingQueueSearchDocument.receivePO " + purchaseOrderIdentifier + "passed all validations");
        }

        try {
            if (valid) {

                SpringContext.getBean(ReceivingService.class).populateAndSaveLineItemReceivingDocument(rlDoc);

                List<OleLineItemReceivingItem> itemList = new ArrayList<OleLineItemReceivingItem>();
                for (Object item : rlDoc.getItems()) {
                    OleLineItemReceivingItem rlItem = (OleLineItemReceivingItem) item;
                    // Receiving 100pc
                    boolean isPOItemPresent = false;
                    for (OlePurchaseOrderItem poItem : this.getPurchaseOrderItems()) {
                        if (poItem.isPoAdded()) {
                            if (!isPOItemPresent
                                    && poItem.getItemIdentifier().equals(rlItem.getPurchaseOrderIdentifier())) {
                                rlItem.setItemReceivedTotalQuantity(rlItem.getItemReceivedToBeQuantity());
                                rlItem.setItemReceivedTotalParts(rlItem.getItemReceivedToBeParts());
                                rlItem.setPoSelected(true);
                                /*
                                 * rlItem.setItemReceivedTotalQuantity(rlItem.getItemOrderedQuantity());
                                 * rlItem.setItemReceivedTotalParts(rlItem.getItemOrderedParts());
                                 */
                                isPOItemPresent = true;
                            } else if (!isPOItemPresent) {
                                rlItem.setItemReceivedTotalQuantity(KualiDecimal.ZERO);
                                rlItem.setItemReceivedTotalParts(KualiDecimal.ZERO);
                            }
                        }
                    }

                    itemList.add(rlItem);
                }

                if (ObjectUtils.isNotNull(itemList) && itemList.size() > 0) {
                    rlDoc.setItems(itemList);

                    Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
                    //rlDoc.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().createWorkflowDocument(rlDoc.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(), principalPerson));
                    rlDoc.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(rlDoc.getDocumentNumber(), principalPerson));
                    //rlDoc.setAdHocRoutePersons(buildFyiRecipient());
                    if (isCreateRCV) {
                        SpringContext.getBean(DocumentService.class).saveDocument(rlDoc);
                    } else {
                        List<OleLineItemReceivingItem> items = rlDoc.getItems();
                        for (OleLineItemReceivingItem item : items) {
                            OleLineItemReceivingService oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingServiceImpl.class);
                            OlePurchaseOrderItem olePurchaseOrderItem = oleLineItemReceivingService.getOlePurchaseOrderItem(item.getPurchaseOrderIdentifier());
                            if (olePurchaseOrderItem != null) {
                                if (item.isPoSelected()) {
                                    for (OleCopy oleCopy : item.getCopyList()) {
                                        oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.RECEIVED_STATUS);
                                    }
                                    OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);
                                    oleCopyHelperService.updateRequisitionAndPOItems(olePurchaseOrderItem, item, null, rlDoc.getIsATypeOfRCVGDoc());
                                }
                            }
                        }
                        SpringContext.getBean(DocumentService.class).routeDocument(rlDoc,
                                "Line Item Receiving from Receiving Queue Search page", null);
                    }
                    //GlobalVariables.getMessageMap().putInfo(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.MESSAGE_RECEIVING_LINE_SUBMITTED, new String[]{rlDoc.getPurchaseOrderIdentifier().toString(), rlDoc.getDocumentNumber()});
                    receivePOSuccess = true;
                }
            }
        } catch (WorkflowException wfe) {
            String rcvDocNum = rlDoc.getDocumentNumber();
            String poId = rlDoc.getPurchaseOrderIdentifier().toString();
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.PURCHASE_ORDER_IDENTIFIER, OLEKeyConstants.ERROR_RECEIVING_LINE_SAVE_OR_SUBMIT, new String[]{poId, rcvDocNum, wfe.getMessage()});
            wfe.printStackTrace();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Receive PO status for PO ID " + purchaseOrderIdentifier + " is " + receivePOSuccess);
            LOG.debug("Leaving receivePO of OleReceivingQueueSearchDocument");
        }

        return receivePOSuccess;
    }

    public void updateRequisitionAndPOItems(OlePurchaseOrderItem olePurchaseOrderItem,
                                            OleLineItemReceivingItem oleLineItemReceivingItem) {

        List<OleCopy> copyList = oleLineItemReceivingItem.getCopyList() != null ? oleLineItemReceivingItem.getCopyList() : new ArrayList<OleCopy>();
        Integer receivedCount = 0;
        for (OleCopy oleCopy : copyList) {
            if (oleCopy.getReceiptStatus().equalsIgnoreCase("Received")) {
                receivedCount++;
            }
        }
        if (receivedCount == 0) {
            oleLineItemReceivingItem
                    .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_NOT_RECEIVED));
        } else if (receivedCount == copyList.size()) {
            oleLineItemReceivingItem
                    .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_FULLY_RECEIVED));
        } else {
            oleLineItemReceivingItem
                    .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_PARTIALLY_RECEIVED));
        }
        olePurchaseOrderItem.setReceiptStatusId(oleLineItemReceivingItem.getReceiptStatusId());
        if (olePurchaseOrderItem.getItemQuantity().equals(new KualiDecimal(1)) && olePurchaseOrderItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
            olePurchaseOrderItem.setNoOfCopiesReceived("N/A");
            olePurchaseOrderItem.setNoOfPartsReceived(receivedCount.toString());
        } else if (olePurchaseOrderItem.getItemQuantity().isGreaterThan(new KualiDecimal(1)) && olePurchaseOrderItem.getItemNoOfParts().equals(new KualiDecimal(1))) {
            olePurchaseOrderItem.setNoOfCopiesReceived(receivedCount.toString());
            olePurchaseOrderItem.setNoOfPartsReceived("N/A");
        } else if (olePurchaseOrderItem.getItemQuantity().isGreaterThan(new KualiDecimal(1)) && olePurchaseOrderItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
            olePurchaseOrderItem.setNoOfCopiesReceived("See Copies Section");
            olePurchaseOrderItem.setNoOfPartsReceived("See Copies Section");
        }
        SpringContext.getBean(BusinessObjectService.class).save(olePurchaseOrderItem);

        String reqsItemId = copyList.size() > 0 ? copyList.get(0).getReqItemId().toString() : "";
        Map<String, String> reqItemMap = new HashMap<String, String>();
        reqItemMap.put("itemIdentifier", reqsItemId);
        OleRequisitionItem oleRequisitionItem = SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(OleRequisitionItem.class, reqItemMap);
        if (oleRequisitionItem != null) {
            oleRequisitionItem.setNoOfCopiesReceived(olePurchaseOrderItem.getNoOfCopiesReceived());
            oleRequisitionItem.setNoOfPartsReceived(olePurchaseOrderItem.getNoOfPartsReceived());
            oleRequisitionItem.setReceiptStatusId(olePurchaseOrderItem.getReceiptStatusId());
            SpringContext.getBean(BusinessObjectService.class).save(oleRequisitionItem);
        }
       /* olePurchaseOrderItem.setNoOfCopiesReceived((olePurchaseOrderItem.getNoOfCopiesReceived().add(new KualiInteger(
            oleLineItemReceivingItem.getItemReceivedTotalQuantity().bigDecimalValue()))).subtract(new KualiInteger(
            oleLineItemReceivingItem.getItemReturnedTotalQuantity().bigDecimalValue())));
        olePurchaseOrderItem.setNoOfPartsReceived((olePurchaseOrderItem.getNoOfPartsReceived().add(new KualiInteger(
            oleLineItemReceivingItem.getItemReceivedTotalParts().bigDecimalValue()))).subtract(new KualiInteger(
            oleLineItemReceivingItem.getItemReturnedTotalParts().bigDecimalValue())));
        if (olePurchaseOrderItem.getItemQuantity().intValue() == olePurchaseOrderItem.getNoOfCopiesReceived()
                .intValue()
                && olePurchaseOrderItem.getItemNoOfParts().intValue() == olePurchaseOrderItem.getNoOfPartsReceived()
                        .intValue()) {
            olePurchaseOrderItem
                    .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_FULLY_RECEIVED));
        }
        else {
            if (olePurchaseOrderItem.getNoOfPartsReceived().isZero()
                    && olePurchaseOrderItem.getNoOfCopiesReceived().isZero()) {
                olePurchaseOrderItem
                        .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_NOT_RECEIVED));
            }
            else {
                olePurchaseOrderItem
                        .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_PARTIALLY_RECEIVED));
            }
        }
        SpringContext.getBean(BusinessObjectService.class).save(olePurchaseOrderItem);
        Map<String, String> purDocNumMap = new HashMap<String, String>();
        purDocNumMap.put(OLEConstants.DOC_NUMBER, olePurchaseOrderItem.getDocumentNumber());
        List<OlePurchaseOrderDocument> poDocList = (List) SpringContext.getBean(BusinessObjectService.class)
                .findMatching(OlePurchaseOrderDocument.class, purDocNumMap);
        for (OlePurchaseOrderDocument olePurchaseOrderDocument : poDocList) {
            String reqsDocIdentifier = olePurchaseOrderDocument.getRequisitionIdentifier().toString();
            Map<String, String> reqDocNumMap = new HashMap<String, String>();
            reqDocNumMap.put(OLEConstants.PUR_DOC_IDENTIFIER, reqsDocIdentifier);
            List<OleRequisitionItem> reqItemList = (List) SpringContext.getBean(BusinessObjectService.class)
                    .findMatching(OleRequisitionItem.class, reqDocNumMap);
            for (OleRequisitionItem oleRequisitionItem : reqItemList) {
                if (null != oleRequisitionItem.getItemLineNumber()
                        && OLEConstants.ITEM.equalsIgnoreCase(oleRequisitionItem.getItemTypeCode())) {
                    if (oleLineItemReceivingItem.getItemLineNumber().intValue() == oleRequisitionItem
                            .getItemLineNumber().intValue()) {
                        oleRequisitionItem.setNoOfCopiesReceived(olePurchaseOrderItem.getNoOfCopiesReceived());
                        oleRequisitionItem.setNoOfPartsReceived(olePurchaseOrderItem.getNoOfPartsReceived());
                        oleRequisitionItem.setReceiptStatusId(olePurchaseOrderItem.getReceiptStatusId());
                        SpringContext.getBean(BusinessObjectService.class).save(oleRequisitionItem);
                    }
                }
            }
        }*/
    }

    public int getReceiptStatusDetails(String receiptStatusCd) {
        int receiptStatusId = 0;
        Map<String, String> receiptStatusCdMap = new HashMap<String, String>();
        receiptStatusCdMap.put(OLEConstants.RCPT_STATUS_CD, receiptStatusCd);
        List<OleReceiptStatus> oleReceiptStatusList = (List) SpringContext.getBean(BusinessObjectService.class)
                .findMatching(OleReceiptStatus.class, receiptStatusCdMap);
        for (OleReceiptStatus oleReceiptStatus : oleReceiptStatusList) {
            receiptStatusId = oleReceiptStatus.getReceiptStatusId().intValue();
        }
        return receiptStatusId;
    }

    /**
     * This method check if any item in PurchaseOrderDocument has special handling notes in it.
     *
     * @param po
     * @return
     */
    private boolean hasSpecialHandlingNotes(PurchaseOrderDocument po) {
        LOG.debug("Inside hasSpecialHandlingNotes of OleReceivingQueueSearchDocument");
        for (OlePurchaseOrderItem poItem : (List<OlePurchaseOrderItem>) po.getItems()) {
            for (OleNotes poNote : poItem.getNotes()) {
                OleNoteType oleNoteType = SpringContext.getBean(OleNoteTypeService.class).getNoteTypeDetails(poNote.getNoteTypeId());
                String noteType = oleNoteType.getNoteType();
                if (noteType.equalsIgnoreCase(OLEConstants.SPECIAL_PROCESSING_INSTRUCTION_NOTE)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("PO ID " + poItem.getPurapDocumentIdentifier() + "has special handling notes");
                    }
                    return true;
                }
            }
        }
        LOG.debug("Leaving hasSpecialHandlingNotes of OleReceivingQueueSearchDocument");
        return false;
    }

    /**
     * Gets the beginDate attribute.
     *
     * @return Returns the beginDate.
     */
    public String getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the beginDate attribute value.
     *
     * @param beginDate The beginDate to set.
     */
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * Gets the endDate attribute.
     *
     * @return Returns the endDate.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the endDate attribute value.
     *
     * @param endDate The endDate to set.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    private boolean validateCopiesAndParts(OlePurchaseOrderItem poItem) {
        boolean isValid = true;
      /*  if ((poItem.getNoOfCopiesReceived() != null && poItem.getItemQuantity().compareTo(
                poItem.getNoOfCopiesReceived().kualiDecimalValue()) == 0)
                && (poItem.getNoOfPartsReceived() != null && poItem.getItemNoOfParts().compareTo(
                        poItem.getNoOfPartsReceived()) == 0)) {
            isValid = false;
        }*/
        return isValid;
    }

    private List<AdHocRoutePerson> buildFyiRecipient() {
        List<AdHocRoutePerson> persons = new ArrayList<AdHocRoutePerson>();
        AdHocRoutePerson adHocRoutePerson = new AdHocRoutePerson();
        adHocRoutePerson.setActionRequested(KewApiConstants.ACTION_REQUEST_FYI_REQ);
        adHocRoutePerson.setId(GlobalVariables.getUserSession().getPrincipalName());
        persons.add(adHocRoutePerson);
        return persons;
    }


    public PurchaseOrderService getPurchaseOrderService() {
        if(purchaseOrderService == null) {
            purchaseOrderService = SpringContext.getBean(PurchaseOrderService.class);
        }
        return purchaseOrderService;
    }

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null) {
            businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        }
        return businessObjectService;
    }

    public ReceivingService getReceivingService() {
        if(receivingService == null) {
            receivingService = SpringContext.getBean(ReceivingService.class);
        }
        return receivingService;
    }

    public OleNoteTypeService getOleNoteTypeService() {
        if(oleNoteTypeService == null) {
            oleNoteTypeService = SpringContext.getBean(OleNoteTypeService.class);
        }
        return oleNoteTypeService;
    }

    public ConfigurationService getConfigurationService() {
        if(configurationService == null) {
            configurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return configurationService;
    }

    public List<PurchaseOrderType> getPurchaseOrderTypeDocumentList() {
        if(purchaseOrderTypeDocumentList.size() == 0) {
            purchaseOrderTypeDocumentList = (List) getBusinessObjectService().findAll(PurchaseOrderType.class);
            if(purchaseOrderTypeDocumentList.size() > 0) {
                return purchaseOrderTypeDocumentList;
            }
        }
        return purchaseOrderTypeDocumentList;
    }
}
