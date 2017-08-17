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
package org.kuali.ole.select.businessobject;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibInfoRecord;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.bo.OLEPOClaimHistory;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;


public class OlePurchaseOrderItem extends PurchaseOrderItem implements OlePurchasingItem {

    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleRequisitionItem.class);
    private boolean selected;
    private BibInfoBean bibInfoBean;
    protected String itemTitleId;
    protected String linkToOrderOption;
    protected String localTitleId;
    protected String itemTitle;
    protected String itemAuthor;
    protected String requestorId;
    protected String requestorFirstName;
    private DocData docData;
    protected KualiInteger itemNoOfParts;
    private String noOfItemParts;
    protected String internalRequestorId;
    protected Integer requestorTypeId;
    protected KualiDecimal itemListPrice;
    protected KualiDecimal itemDiscount;
    protected String itemDiscountType;
    protected String vendorItemPoNumber;
    protected String itemCurrencyType;
    protected String donorSearchCodes;
    protected KualiDecimal itemForeignListPrice;
    protected KualiDecimal itemForeignDiscount;
    protected String itemForeignDiscountType;
    protected KualiDecimal itemForeignDiscountAmt;
    protected KualiDecimal itemForeignUnitCost;
    protected BigDecimal itemExchangeRate;
    protected KualiDecimal itemUnitCostUSD;
    protected boolean latestExchangeRate;
    protected Integer categoryId;
    protected Integer formatTypeId;
    protected Integer requestSourceTypeId;
    protected Integer receiptStatusId;
    private OleCategory category;
    private OleFormatType formatTypeName;
    private OleRequestSourceType oleRequestSourceType;
    private OleReceiptStatus oleReceiptStatus;
    protected Integer itemPriceSourceId;
    private OleItemPriceSource itemPriceSource;
    private String purposeId;
    protected boolean itemRouteToRequestorIndicator;
    protected boolean itemPublicViewIndicator;
    private PurchaseOrderDocument purchaseOrder;
    private OleOrderRecord oleOrderRecord;
    private boolean poAdded = false;
    protected String bibUUID;
    private String noOfCopiesReceived;
    private String noOfPartsReceived;
    private KualiDecimal invoicePrice;
    private KualiDecimal itemForeignUnitPrice;
    private KualiInteger itemPoQty;
    private String singleCopyNumber;
    private String oleERSIdentifier;
    private String eResLink;

    private String donorId;
    private String donorCode;
    private List<OLELinkPurapDonor> oleDonors=new ArrayList<>();

    protected KualiDecimal itemForiegnExtendedPrice;
    private String itemLocation;
    /**
     * For List of copies
     */
    private Integer itemCopiesId;
    private KualiInteger parts;
    private KualiDecimal itemCopies;
    private String partEnumeration;
    private String locationCopies;
    private KualiInteger startingCopyNumber;
    private List<OleCopies> copies = new ArrayList<OleCopies>();
    private List<OleRequisitionCopies> oleCopies = new ArrayList<OleRequisitionCopies>();
    private List<OleCopy> deletedCopiesList = new ArrayList<>();

    //For Displaying the Invoice List
    private List<OleInvoiceDocument> invoiceDocuments = new ArrayList<OleInvoiceDocument>();
  //  private List<InvoiceView> paymentHistoryInvoiceViews = null;
    //For Invoice
    private String invoiceItemListPrice;
    private String invoiceForeignItemListPrice;
    private String invoiceForeignDiscount;
    private String invoiceExchangeRate;
    private String invoiceForeignUnitCost;
    private String invoiceForeignCurrency;
    private String invoiceForeignDiscountType;

    private boolean doNotClaim;

    private KualiInteger claimCount;
    private boolean claimPoAdded;
    private Date claimDate;
    private String claimNote;
    private List<OLEPOClaimHistory> claimHistories = new ArrayList<>();

    private boolean claimFilter = false;
    private String title;
    private BibInfoRecord bibInfoRecord;
    private String fundCode;

    public List<OLEPOClaimHistory> getClaimHistories() {
        return claimHistories;
    }

    public String getDonorSearchCodes() {
        return donorSearchCodes;
    }

    public void setDonorSearchCodes(String donorSearchCodes) {
        this.donorSearchCodes = donorSearchCodes;
    }

    public void setClaimHistories(List<OLEPOClaimHistory> claimHistories) {
        this.claimHistories = claimHistories;
    }

    public String getClaimNote() {
        return claimNote;
    }

    public void setClaimNote(String claimNote) {
        this.claimNote = claimNote;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public boolean isClaimPoAdded() {
        return claimPoAdded;
    }

    public void setClaimPoAdded(boolean claimPoAdded) {
        this.claimPoAdded = claimPoAdded;
    }

    public String getSingleCopyNumber() {
        return singleCopyNumber;
    }

    public void setSingleCopyNumber(String singleCopyNumber) {
        this.singleCopyNumber = singleCopyNumber;
    }

    public KualiInteger getClaimCount() {
        return claimCount;
    }

    public void setClaimCount(KualiInteger claimCount) {
        this.claimCount = claimCount;
    }

    public boolean isDoNotClaim() {
        return doNotClaim;
    }

    public void setDoNotClaim(boolean doNotClaim) {
        this.doNotClaim = doNotClaim;
    }

    public List<OLELinkPurapDonor> getOleDonors() {
        return oleDonors;
    }

    public void setOleDonors(List<OLELinkPurapDonor> oleDonors) {
        this.oleDonors = oleDonors;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }
    /**
     * For List Payment History
     */
    private String paymentHistory;
    private List<OleRequisitionPaymentHistory> requisitionPaymentHistory = new ArrayList<OleRequisitionPaymentHistory>();
    private String itemTypeDescription;

    /**
     * For Quantity As Integer
     */
    private KualiInteger oleItemQuantity;
    private String copiesOrdered;


    private OLERequestorPatronDocument olePatronDocument;
    protected String olePatronId;
    private String firstName;
    private String enumeration;
    private String location;
    private String copyNumber;
    private String receiptStatus;
    private String caption;
    private String volumeNumber;
    private List<OleCopy> copyList = new ArrayList<>();

    private String                               itemStatus;

    private String vendorName;
    private OlePurchaseOrderDocument olePurchaseOrderDocument;
    // Subscription Date for Invoice Document
    private Date subscriptionFromDate;
    private Date subscriptionToDate;
    private boolean subscriptionOverlap;

    public OlePurchaseOrderDocument getOlePurchaseOrderDocument() {
        return olePurchaseOrderDocument;
    }

    public void setOlePurchaseOrderDocument(OlePurchaseOrderDocument olePurchaseOrderDocument) {
        this.olePurchaseOrderDocument = olePurchaseOrderDocument;
    }
    private String docFormat;
    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }
    // For Invoice Document
    private boolean itemForInvoice = true;
    private String openQuantity;
    private KualiDecimal poOutstandingQuantity;
    private List<OleInvoiceNote> invoiceNotes = new ArrayList<OleInvoiceNote>();
    private boolean locationFlag;
    private boolean itemLocationChangeFlag;
    private KualiDecimal previousItemQuantity;
    private KualiInteger previousItemNoOfParts;

    public String getLocalTitleId() {
        return localTitleId;
    }

    public void setLocalTitleId(String localTitleId) {
        this.localTitleId = localTitleId;
    }

    //Invoice Changes
    private BigDecimal itemSurcharge;

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public BigDecimal getItemSurcharge() {
        return itemSurcharge;
    }

    public void setItemSurcharge(BigDecimal itemSurcharge) {
        this.itemSurcharge = itemSurcharge;
    }

    public List<OleRequisitionCopies> getOleCopies() {
        return oleCopies;
    }

    public void setOleCopies(List<OleRequisitionCopies> oleCopies) {
        this.oleCopies = oleCopies;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public List<OleCopy> getCopyList() {
        return copyList;
    }

    public void setCopyList(List<OleCopy> copyList) {
        this.copyList = copyList;
    }

    /**
     * Gets the value of the receiptStatusId property
     *
     * @return receiptStatusId
     */
    public Integer getReceiptStatusId() {
        return receiptStatusId;
    }

    /**
     * Sets the receiptStatusId value
     *
     * @param receiptStatusId
     */
    public void setReceiptStatusId(Integer receiptStatusId) {
        this.receiptStatusId = receiptStatusId;
    }

    /**
     * Gets the instance of OleReceiptStatus
     *
     * @return oleReceiptStatus
     */
    public OleReceiptStatus getOleReceiptStatus() {
        return oleReceiptStatus;
    }

    /**
     * Sets the instance value for OleReceiptStatus
     *
     * @param oleReceiptStatus
     */
    public void setOleReceiptStatus(OleReceiptStatus oleReceiptStatus) {
        this.oleReceiptStatus = oleReceiptStatus;
    }

    /**
     * Gets the poAdded attribute.
     *
     * @return Returns the poAdded.
     */
    public boolean isPoAdded() {
        return poAdded;
    }

    /**
     * Sets the poAdded attribute value.
     *
     * @param poAdded The poAdded to set.
     */
    public void setPoAdded(boolean poAdded) {
        this.poAdded = poAdded;
    }

    /**
     * This for List of Notes
     */
    /*private List<OleNotes> notes;
    private Integer noteTypeId;
    private String note;*/

    private KualiDecimal itemReceivedTotalParts; // Added for OLE-2058
    private KualiDecimal itemDamagedTotalParts; // Adde for OLE-2058

    /**
     * This method... for getting NoteType Id
     *
     * @return noteTypeId
     */
   /* public Integer getNoteTypeId() {
        return noteTypeId;
    }

    *//**
     * This method... for setting the NoteType Id
     *
     * @param noteTypeId
     *//*
    public void setNoteTypeId(Integer noteTypeId) {
        this.noteTypeId = noteTypeId;
    }

    *//**
     * This method... for getting the Note description
     *
     * @return note
     *//*
    public String getNote() {
        return note;
    }

    *//**
     * This method... for setting the Note description
     *
     * @param note
     *//*
    public void setNote(String note) {
        this.note = note;
    }

    *//**
     * This method... for setting List of notes
     *
     * @param notes
     *//*
    public void setNotes(List<OleNotes> notes) {
        this.notes = notes;
    }

    *//**
     * This method... for getting List of Notes
     *
     * @return notes
     *//*
    public List<OleNotes> getNotes() {
        return notes;
    }*/

    public OleOrderRecord getOleOrderRecord() {
        return oleOrderRecord;
    }

    public void setOleOrderRecord(OleOrderRecord oleOrderRecord) {
        this.oleOrderRecord = oleOrderRecord;
    }

    public OlePurchaseOrderItem() {
        this.setItemNoOfParts(new KualiInteger(1));
        this.setItemPublicViewIndicator(true);
        this.setItemUnitOfMeasureCode(PurapConstants.PODocumentsStrings.CUSTOMER_INVOICE_DETAIL_UOM_DEFAULT);
        this.setOleItemQuantity(new KualiInteger(1));
        this.setItemListPrice(new KualiDecimal(0.00));
        this.setNoOfCopiesReceived("");
        this.getNewSourceLine().setAccountLinePercent(new BigDecimal(0));
        this.setNoOfPartsReceived("");
        /*if (GlobalVariables.getUserSession() != null) {
            this.internalRequestorId = GlobalVariables.getUserSession().getPrincipalId();
            this.setRequestorFirstName(getPersonName(GlobalVariables.getUserSession().getPrincipalId()));
        }*/
        /*    if(SpringContext.getBean(OleRequisitionService.class).getRequestor()!=null) {
            String requestor=SpringContext.getBean(OleRequisitionService.class).getRequestor();
            this.setRequestorFirstName(requestor);
        }
        OleRequisitionService oleRequestionService=new OleRequisitionServiceImpl();
        if(oleRequestionService.getRequestor()!=null) {
             String requestor=oleRequestionService.getRequestor();
             this.setRequestorFirstName(requestor);
         }*/
        /**
         * Initializing poNotes List
         */
        LOG.debug("Initializing OlePurchaseOrderNotes List");
        //notes = new ArrayList<OleNotes>();
    }


    @Override
    public PurchaseOrderDocument getPurchaseOrder() {
        // TODO Auto-generated method stub
        return super.getPurchaseOrder();
    }


    @Override
    public void setPurchaseOrder(PurchaseOrderDocument purchaseOrder) {
        // TODO Auto-generated method stub
        setPurapDocument(purchaseOrder);
        super.setPurchaseOrder(purchaseOrder);
    }


    public OlePurchaseOrderItem(OleRequisitionItem ri, OlePurchaseOrderDocument po, RequisitionCapitalAssetItem reqCamsItem) {
        this();
        // po.setVendorPoNumber(ri.getVendorItemPoNumber());
        this.setPurchaseOrder(po);
        SequenceAccessorService sas = SpringContext.getBean(SequenceAccessorService.class);
        Integer itemIdentifier = sas.getNextAvailableSequenceNumber("PO_ITM_ID", PurchaseOrderDocument.class).intValue();
        this.setItemIdentifier(itemIdentifier);
        this.setItemLineNumber(ri.getItemLineNumber());
        this.setItemUnitOfMeasureCode(ri.getItemUnitOfMeasureCode());
        this.setItemNoOfParts(ri.getItemNoOfParts());
        this.setItemQuantity(ri.getItemQuantity());
        this.setItemCatalogNumber(ri.getItemCatalogNumber());
        this.setPreviousItemQuantity(ri.getItemQuantity());
        this.setPreviousItemNoOfParts(ri.getItemNoOfParts());
        this.setItemDescription(ri.getItemDescription());
        this.setSingleCopyNumber(ri.getSingleCopyNumber());
        if(ri.getItemTitleId()!=null){
            this.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(ri.getItemTitleId()));
        }
        if(ri.getItemUnitPrice()!=null){
            this.setItemUnitPrice(ri.getItemUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        else{
            this.setItemUnitPrice(ri.getItemUnitPrice());
        }
        this.setItemAuxiliaryPartIdentifier(ri.getItemAuxiliaryPartIdentifier());
        this.setItemAssignedToTradeInIndicator(ri.getItemAssignedToTradeInIndicator());
        this.setItemTaxAmount(ri.getItemTaxAmount());
        this.setItemTitleId(ri.getItemTitleId());
        this.setOleERSIdentifier(ri.getOleERSIdentifier());
        this.setRequestorId(ri.getRequestorId());
        this.setInternalRequestorId(ri.getInternalRequestorId());
        this.setRequestorTypeId(ri.getRequestorTypeId());
        this.setItemListPrice(ri.getItemListPrice());
        this.setItemDiscount(ri.getItemDiscount());
        this.setItemDiscountType(ri.getItemDiscountType());
        this.setItemRouteToRequestorIndicator(ri.isItemRouteToRequestorIndicator());
        this.setItemPublicViewIndicator(ri.isItemPublicViewIndicator());
        this.setVendorItemPoNumber(ri.getVendorItemPoNumber());
        // Foreign Currency Conversion
        this.setItemCurrencyType(ri.getItemCurrencyType());
        this.setItemForeignListPrice(ri.getItemForeignListPrice());
        this.setItemForeignDiscount(ri.getItemForeignDiscount());
        this.setItemForeignDiscountAmt(ri.getItemForeignDiscountAmt());
        this.setItemForeignDiscountType(ri.getItemForeignDiscountType());
        this.setItemForeignUnitCost(ri.getItemForeignUnitCost());
        this.setItemExchangeRate(ri.getItemExchangeRate());
        this.setItemUnitCostUSD(ri.getItemUnitCostUSD());
        this.setCategory(ri.getCategory());
        this.setCategoryId(ri.getCategoryId());
        this.setFormatTypeName(ri.getFormatTypeName());
        this.setFormatTypeId(ri.getFormatTypeId());
        this.setOleRequestSourceType(ri.getOleRequestSourceType());
        this.setRequestSourceTypeId(ri.getRequestSourceTypeId());
        this.setReceiptStatusId(ri.getReceiptStatusId());
        this.setItemPriceSource(ri.getItemPriceSource());
        this.setItemPriceSourceId(ri.getItemPriceSourceId());
        this.setPurposeId(ri.getPurposeId());
        this.setItemLocation(ri.getItemLocation());
        this.setItemStatus(ri.getItemStatus());
        this.setOleDonors(ri.getOleDonors());
        this.setDoNotClaim(ri.isDoNotClaim());
        this.setClaimDate(ri.getClaimDate());
        this.setLinkToOrderOption(ri.getLinkToOrderOption());


        //copy use tax items over, and blank out keys (useTaxId and itemIdentifier)
        for (PurApItemUseTax useTaxItem : ri.getUseTaxItems()) {
            PurchaseOrderItemUseTax newItemUseTax = new PurchaseOrderItemUseTax(useTaxItem);
            newItemUseTax.setItemIdentifier(itemIdentifier);
            this.getUseTaxItems().add(newItemUseTax);

        }

        this.setExternalOrganizationB2bProductReferenceNumber(ri.getExternalOrganizationB2bProductReferenceNumber());
        this.setExternalOrganizationB2bProductTypeName(ri.getExternalOrganizationB2bProductTypeName());

        this.setItemReceivedTotalQuantity(org.kuali.rice.core.api.util.type.KualiDecimal.ZERO);
        this.setItemDamagedTotalQuantity(org.kuali.rice.core.api.util.type.KualiDecimal.ZERO);

        this.setItemTypeCode(ri.getItemTypeCode());

        if (ri.getSourceAccountingLines() != null && ri.getSourceAccountingLines().size() > 0 &&
                !StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ORDER_DISCOUNT_CODE, ri.getItemType().getItemTypeCode())) {
            List accounts = new ArrayList();
            for (PurApAccountingLine account : ri.getSourceAccountingLines()) {
                PurchaseOrderAccount poAccount = new PurchaseOrderAccount(account);
                poAccount.setPurchaseOrderItem(this);
                accounts.add(poAccount);
            }
            this.setSourceAccountingLines(accounts);
        }
        /**
         * Setting the Notes of each item from OleRequisitionNotes to OlePurchaseOrderNotes List
         */
        if (ri.getNotes() != null && ri.getNotes().size() > 0 &&
                !StringUtils.equals(PurapConstants.ItemTypeCodes.ITEM_TYPE_ORDER_DISCOUNT_CODE, ri.getItemType().getItemTypeCode())) {
            List notes = new ArrayList();
            LOG.debug("Adding Notes from RequisitionItem to corresponding PurchaseOrderItem");
            for (OleNotes note : ri.getNotes()) {
                OlePurchaseOrderNotes poNote = new OlePurchaseOrderNotes(note);
                poNote.setOlePurchaseOrderItem(this);
                poNote.setNoteType(note.getNoteType());
                notes.add(poNote);
            }
            this.setNotes(notes);
        }
        if(ri.getRequisition()!=null) {
            if (ri.getRequisition().getPurapDocumentIdentifier() != null) {
                for (OleCopy olecopy : ri.getCopyList()) {
                    olecopy.setReqDocNum(ri.getRequisition().getPurapDocumentIdentifier());
                }
            }
        }
        this.setCopyList(ri.getCopyList());
        // By default, set the item active indicator to true.
        // In amendment, the user can set it to false when they click on
        // the inactivate button.
        this.setItemActiveIndicator(true);

        this.setPurchasingCommodityCode(ri.getPurchasingCommodityCode());
        this.setCommodityCode(getCommodityCode());

        // If the RequisitionItem has a CapitalAssetItem, create a new PurchasingCapitalAssetItem and add it to the PO.
        if (ObjectUtils.isNotNull(reqCamsItem)) {
            PurchaseOrderCapitalAssetItem newPOCapitalAssetItem = new PurchaseOrderCapitalAssetItem(reqCamsItem, itemIdentifier);
            po.getPurchasingCapitalAssetItems().add(newPOCapitalAssetItem);
        }
    }


    @Override
    public Integer getRequestorTypeId() {
        return requestorTypeId;
    }


    public void setRequestorTypeId(Integer requestorTypeId) {
        this.requestorTypeId = requestorTypeId;
    }


    @Override
    public String getInternalRequestorId() {
        return internalRequestorId;
    }


    public void setInternalRequestorId(String internalRequestorId) {
        this.internalRequestorId = internalRequestorId;
    }

    @Override
    public String getItemTitleId() {
        return itemTitleId;
    }

    public void setItemTitleId(String itemTitleId) {
        this.itemTitleId = itemTitleId;
    }

    public String getLinkToOrderOption() {
        return linkToOrderOption;
    }

    public void setLinkToOrderOption(String linkToOrderOption) {
        this.linkToOrderOption = linkToOrderOption;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemAuthor() {
        return itemAuthor;
    }

    public void setItemAuthor(String itemAuthor) {
        this.itemAuthor = itemAuthor;
    }

    @Override
    public String getRequestorFirstName() {
        return requestorFirstName;
    }

    @Override
    public void setRequestorFirstName(String requestorFirstName) {
        this.requestorFirstName = requestorFirstName;
    }

    @Override
    public String getRequestorId() {
        return requestorId;
    }


    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    public BibInfoBean getBibInfoBean() {
        return bibInfoBean;
    }


    @Override
    public void setBibInfoBean(BibInfoBean bibInfoBean) {
        this.bibInfoBean = bibInfoBean;
    }

    public DocData getDocData() {
        return docData;
    }

    public void setDocData(DocData docData) {
        this.docData = docData;
    }

    public String getNoOfItemParts() {
        if (this.itemNoOfParts != null) {
            return String.valueOf(itemNoOfParts);
        } else {
            return String.valueOf(itemNoOfParts);
        }
    }

    public void setNoOfItemParts(String noOfItemParts) {
        this.itemNoOfParts = new KualiInteger(noOfItemParts);
    }

    public KualiInteger getItemNoOfParts() {
        if (this.itemNoOfParts != null) {
            return itemNoOfParts;
        } else {
            return itemNoOfParts;
        }
    }


    public void setItemNoOfParts(KualiInteger itemNoOfParts) {
        this.itemNoOfParts = itemNoOfParts;
    }

    public KualiDecimal getItemListPrice() {
        return itemListPrice;
    }


    public void setItemListPrice(KualiDecimal itemListPrice) {
        this.itemListPrice = itemListPrice;
    }


    public KualiDecimal getItemDiscount() {
        return itemDiscount;
    }


    public void setItemDiscount(KualiDecimal itemDiscount) {
        this.itemDiscount = itemDiscount;
    }


    public String getItemDiscountType() {
        return itemDiscountType;
    }


    public void setItemDiscountType(String itemDiscountType) {
        this.itemDiscountType = itemDiscountType;
    }

    public String getItemCurrencyType() {
        return itemCurrencyType;
    }

    public void setItemCurrencyType(String itemCurrencyType) {
        this.itemCurrencyType = itemCurrencyType;
    }

    public KualiDecimal getItemForeignListPrice() {
        return itemForeignListPrice;
    }

    public void setItemForeignListPrice(KualiDecimal itemForeignListPrice) {
        this.itemForeignListPrice = itemForeignListPrice;
    }

    public KualiDecimal getItemForeignDiscount() {
        return itemForeignDiscount;
    }

    public void setItemForeignDiscount(KualiDecimal itemForeignDiscount) {
        this.itemForeignDiscount = itemForeignDiscount;
    }

    public String getItemForeignDiscountType() {
        return itemForeignDiscountType;
    }

    public void setItemForeignDiscountType(String itemForeignDiscountType) {
        this.itemForeignDiscountType = itemForeignDiscountType;
    }

    public KualiDecimal getItemForeignDiscountAmt() {
        return itemForeignDiscountAmt;
    }

    public void setItemForeignDiscountAmt(KualiDecimal itemForeignDiscountAmt) {
        this.itemForeignDiscountAmt = itemForeignDiscountAmt;
    }

    public KualiDecimal getItemForeignUnitCost() {
        return itemForeignUnitCost;
    }

    public void setItemForeignUnitCost(KualiDecimal itemForeignUnitCost) {
        this.itemForeignUnitCost = itemForeignUnitCost;
    }

    public BigDecimal getItemExchangeRate() {
        return itemExchangeRate;
    }

    public void setItemExchangeRate(BigDecimal itemExchangeRate) {
        this.itemExchangeRate = itemExchangeRate;
    }

    public KualiDecimal getItemUnitCostUSD() {
        return itemUnitCostUSD;
    }

    public void setItemUnitCostUSD(KualiDecimal itemUnitCostUSD) {
        this.itemUnitCostUSD = itemUnitCostUSD;
    }


    public Integer getCategoryId() {
        return categoryId;
    }


    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }


    public Integer getFormatTypeId() {
        return formatTypeId;
    }


    public void setFormatTypeId(Integer formatTypeId) {
        this.formatTypeId = formatTypeId;
    }

    public Integer getRequestSourceTypeId() {
        return requestSourceTypeId;
    }


    public void setRequestSourceTypeId(Integer requestSourceTypeId) {
        this.requestSourceTypeId = requestSourceTypeId;
    }

    public OleCategory getCategory() {
        return category;
    }


    public void setCategory(OleCategory category) {
        this.category = category;
    }


    public OleFormatType getFormatTypeName() {
        return formatTypeName;
    }


    public void setFormatTypeName(OleFormatType formatTypeName) {
        this.formatTypeName = formatTypeName;
    }


    public OleRequestSourceType getOleRequestSourceType() {
        return oleRequestSourceType;
    }


    public void setOleRequestSourceType(OleRequestSourceType oleRequestSourceType) {
        this.oleRequestSourceType = oleRequestSourceType;
    }


    public Integer getItemPriceSourceId() {
        return itemPriceSourceId;
    }


    public void setItemPriceSourceId(Integer itemPriceSourceId) {
        this.itemPriceSourceId = itemPriceSourceId;
    }


    public OleItemPriceSource getItemPriceSource() {
        return itemPriceSource;
    }


    public void setItemPriceSource(OleItemPriceSource itemPriceSource) {
        this.itemPriceSource = itemPriceSource;
    }


    // Added for OLE-2058 Starts

    /**
     * Gets the total parts received in a purchase order item.
     *
     * @return itemReceivedTotalParts.
     */
    public KualiDecimal getItemReceivedTotalParts() {
        return itemReceivedTotalParts;
    }

    /**
     * Sets the total parts received in a purchase order item.
     *
     * @param itemReceivedTotalParts to set.
     */
    public void setItemReceivedTotalParts(KualiDecimal itemReceivedTotalParts) {
        this.itemReceivedTotalParts = itemReceivedTotalParts;
    }

    /**
     * Gets the total parts damaged in a purchase order item.
     *
     * @return itemDamagedTotalParts.
     */
    public KualiDecimal getItemDamagedTotalParts() {
        return itemDamagedTotalParts;
    }

    /**
     * Sets the total parts damaged in a purchase order item.
     *
     * @param itemDamagedTotalParts to set.
     */
    public void setItemDamagedTotalParts(KualiDecimal itemDamagedTotalParts) {
        this.itemDamagedTotalParts = itemDamagedTotalParts;
    }
    // Added for OLE-2058 Ends

    public boolean isItemRouteToRequestorIndicator() {
        return itemRouteToRequestorIndicator;
    }

    public void setItemRouteToRequestorIndicator(boolean itemRouteToRequestorIndicator) {
        this.itemRouteToRequestorIndicator = itemRouteToRequestorIndicator;
    }

    public boolean isItemPublicViewIndicator() {
        return itemPublicViewIndicator;
    }

    public void setItemPublicViewIndicator(boolean itemPublicViewIndicator) {
        this.itemPublicViewIndicator = itemPublicViewIndicator;
    }

    public boolean isLatestExchangeRate() {
        return latestExchangeRate;
    }

    public void setLatestExchangeRate(boolean latestExchangeRate) {
        this.latestExchangeRate = latestExchangeRate;
    }


    /**
     * @see org.kuali.ole.module.purap.businessobject.PurchasingItemBase#getAccountingLineClass()
     */
    @Override
    public Class getAccountingLineClass() {
        return OlePurchaseOrderAccount.class;
    }


    public String getBibUUID() {
        return bibUUID;
    }

    public void setBibUUID(String bibUUID) {
        this.bibUUID = bibUUID;
    }

    public String getVendorItemPoNumber() {
        return vendorItemPoNumber;
    }

    public void setVendorItemPoNumber(String vendorItemPoNumber) {
        this.vendorItemPoNumber = vendorItemPoNumber;
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

    public String getPersonName(String principalId) {
        Person person = SpringContext.getBean(PersonService.class).getPerson(principalId);
        if (person != null) {
            return person.getName();
        } else {
            return null;
        }
    }

    /**
     * Gets the no Of CopiesReceived in a purchase order item.
     *
     * @return noOfCopiesReceived.
     */
    public String getNoOfCopiesReceived() {
        return noOfCopiesReceived;
    }

    /**
     * Sets the no Of CopiesReceived in a purchase order item.
     *
     * @param noOfCopiesReceived to set.
     */
    public void setNoOfCopiesReceived(String noOfCopiesReceived) {
        this.noOfCopiesReceived = noOfCopiesReceived;
    }

    /**
     * Gets the no Of Parts Received in a purchase order item.
     *
     * @return noOfPartsReceived.
     */
    public String getNoOfPartsReceived() {
        return noOfPartsReceived;
    }

    /**
     * Sets the no Of Parts Received in a purchase order item.
     *
     * @param noOfPartsReceived to set.
     */
    public void setNoOfPartsReceived(String noOfPartsReceived) {
        this.noOfPartsReceived = noOfPartsReceived;
    }

    public KualiDecimal getInvoicePrice() {
        return invoicePrice;
    }

    public void setInvoicePrice(KualiDecimal invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public KualiDecimal getItemForeignUnitPrice() {
        return itemForeignUnitPrice;
    }

    public void setItemForeignUnitPrice(KualiDecimal itemForeignUnitPrice) {
        this.itemForeignUnitPrice = itemForeignUnitPrice;
    }

    public KualiInteger getItemPoQty() {
        if (itemPoQty == null || itemPoQty.equals(KualiInteger.ZERO)) {
            itemPoQty = new KualiInteger(this.getItemQuantity().bigDecimalValue());
        }
        return itemPoQty;
    }

    public void setItemPoQty(KualiInteger itemPoQty) {
        this.itemPoQty = itemPoQty;
    }

    /**
     * Gets the itemForiegnExtendedPrice attribute.
     *
     * @return Returns the itemForiegnExtendedPrice.
     */
    public KualiDecimal getItemForiegnExtendedPrice() {
        return itemForiegnExtendedPrice;
    }

    /**
     * Sets the itemForiegnExtendedPrice attribute value.
     *
     * @param itemForiegnExtendedPrice The itemForiegnExtendedPrice to set.
     */
    public void setItemForiegnExtendedPrice(KualiDecimal itemForiegnExtendedPrice) {
        this.itemForiegnExtendedPrice = itemForiegnExtendedPrice;
    }

    /**
     * Gets the itemLocation attribute.
     *
     * @return Returns the itemLocation.
     */
    public String getItemLocation() {
        return itemLocation;
    }

    /**
     * Sets the itemLocation attribute value.
     *
     * @param itemLocation The itemLocation to set.
     */
    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    /**
     * Gets the itemCopiesId attribute.
     *
     * @return Returns the itemCopiesId.
     */
    public Integer getItemCopiesId() {
        return itemCopiesId;
    }

    /**
     * Sets the itemCopiesId attribute value.
     *
     * @param itemCopiesId The itemCopiesId to set.
     */
    public void setItemCopiesId(Integer itemCopiesId) {
        this.itemCopiesId = itemCopiesId;
    }

    /**
     * Gets the parts attribute.
     *
     * @return Returns the parts.
     */
    public KualiInteger getParts() {
        return parts;
    }

    /**
     * Sets the parts attribute value.
     *
     * @param parts The parts to set.
     */
    public void setParts(KualiInteger parts) {
        this.parts = parts;
    }

    /**
     * Gets the itemCopies attribute.
     *
     * @return Returns the itemCopies.
     */
    public KualiDecimal getItemCopies() {
        return itemCopies;
    }

    /**
     * Sets the itemCopies attribute value.
     *
     * @param itemCopies The itemCopies to set.
     */
    public void setItemCopies(KualiDecimal itemCopies) {
        this.itemCopies = itemCopies;
    }

    /**
     * Gets the partEnumeration attribute.
     *
     * @return Returns the partEnumeration.
     */
    public String getPartEnumeration() {
        return partEnumeration;
    }

    /**
     * Sets the partEnumeration attribute value.
     *
     * @param partEnumeration The partEnumeration to set.
     */
    public void setPartEnumeration(String partEnumeration) {
        this.partEnumeration = partEnumeration;
    }

    /**
     * Gets the locationCopies attribute.
     *
     * @return Returns the locationCopies.
     */
    public String getLocationCopies() {
        return locationCopies;
    }

    /**
     * Sets the locationCopies attribute value.
     *
     * @param locationCopies The locationCopies to set.
     */
    public void setLocationCopies(String locationCopies) {
        this.locationCopies = locationCopies;
    }

    /**
     * Gets the startingCopyNumber attribute.
     *
     * @return Returns the startingCopyNumber.
     */
    public KualiInteger getStartingCopyNumber() {
        return startingCopyNumber;
    }

    /**
     * Sets the startingCopyNumber attribute value.
     *
     * @param startingCopyNumber The startingCopyNumber to set.
     */
    public void setStartingCopyNumber(KualiInteger startingCopyNumber) {
        this.startingCopyNumber = startingCopyNumber;
    }

    /**
     * Gets the copies attribute.
     *
     * @return Returns the copies.
     */
    public List<OleCopies> getCopies() {
        return copies;
    }

    /**
     * Sets the copies attribute value.
     *
     * @param copies The copies to set.
     */
    public void setCopies(List<OleCopies> copies) {
        this.copies = copies;
    }

    /**
     * Gets the paymentHistory attribute.
     *
     * @return Returns the paymentHistory.
     */
    public String getPaymentHistory() {
        return paymentHistory;
    }

    /**
     * Sets the paymentHistory attribute value.
     *
     * @param paymentHistory The paymentHistory to set.
     */
    public void setPaymentHistory(String paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    /**
     * Gets the requisitionPaymentHistory attribute.
     *
     * @return Returns the requisitionPaymentHistory.
     */
    public List<OleRequisitionPaymentHistory> getRequisitionPaymentHistory() {
        return requisitionPaymentHistory;
    }

    /**
     * Sets the requisitionPaymentHistory attribute value.
     *
     * @param requisitionPaymentHistory The requisitionPaymentHistory to set.
     */
    public void setRequisitionPaymentHistory(List<OleRequisitionPaymentHistory> requisitionPaymentHistory) {
        this.requisitionPaymentHistory = requisitionPaymentHistory;
    }

    /**
     * Gets the itemTypeDescription attribute.
     *
     * @return Returns the itemTypeDescription.
     */
    public String getItemTypeDescription() {
        return itemTypeDescription;
    }

    /**
     * Sets the itemTypeDescription attribute value.
     *
     * @param itemTypeDescription The itemTypeDescription to set.
     */
    public void setItemTypeDescription(String itemTypeDescription) {
        this.itemTypeDescription = itemTypeDescription;
    }

    public String getCopiesOrdered() {
        return String.valueOf(super.getItemQuantity().intValue());
    }

    public void setCopiesOrdered(String copiesOrdered) {
        super.setItemQuantity(new KualiDecimal(copiesOrdered));
    }

    /**
     * Gets the oleItemQuantity attribute.
     *
     * @return Returns the oleItemQuantity.
     */
    public KualiInteger getOleItemQuantity() {
        return new KualiInteger(super.getItemQuantity().intValue());
    }

    /**
     * Sets the oleItemQuantity attribute value.
     *
     * @param oleItemQuantity The oleItemQuantity to set.
     */
    public void setOleItemQuantity(KualiInteger oleItemQuantity) {
        super.setItemQuantity(new KualiDecimal(oleItemQuantity.intValue()));
    }

    /**
     * Gets the olePatronDocument attribute.
     *
     * @return Returns the olePatronDocument.
     */
    public OLERequestorPatronDocument getOlePatronDocument() {
        return olePatronDocument;
    }

    /**
     * Sets the olePatronDocument attribute value.
     *
     * @param olePatronDocument The olePatronDocument to set.
     */
    public void setOlePatronDocument(OLERequestorPatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    /**
     * Gets the olePatronId attribute.
     *
     * @return Returns the olePatronId.
     */
    public String getOlePatronId() {
        return olePatronId;
    }

    /**
     * Sets the olePatronId attribute value.
     *
     * @param olePatronId The olePatronId to set.
     */
    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    /**
     * Sets the firstName attribute value.
     *
     * @param firstName The firstName to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the firstName attribute.
     *
     * @return Returns the firstName.
     */
    public String getFirstName() {
        return firstName;
    }


    //Invoice Document
    private String copiesInvoicedNumber;
    private KualiInteger noOfCopiesInvoiced;
    private String partsInvoicedNumber;
    private KualiInteger noOfPartsInvoiced;

    public String getPartsInvoicedNumber() {
        return String.valueOf(noOfPartsInvoiced);
    }

    public void setPartsInvoicedNumber(String partsInvoicedNumber) {
        this.noOfPartsInvoiced = new KualiInteger(partsInvoicedNumber);
    }

    public String getCopiesInvoicedNumber() {
        return String.valueOf(noOfCopiesInvoiced);
    }

    public void setCopiesInvoicedNumber(String copiesInvoicedNumber) {
        // this.copiesInvoicedNumber = copiesInvoicedNumber;
        this.noOfCopiesInvoiced = new KualiInteger(copiesInvoicedNumber);
    }

    public KualiInteger getNoOfCopiesInvoiced() {
        return noOfCopiesInvoiced;
    }

    public void setNoOfCopiesInvoiced(KualiInteger noOfCopiesInvoiced) {
        this.noOfCopiesInvoiced = noOfCopiesInvoiced;
    }

    public KualiInteger getNoOfPartsInvoiced() {
        return noOfPartsInvoiced;
    }

    public void setNoOfPartsInvoiced(KualiInteger noOfPartsInvoiced) {
        this.noOfPartsInvoiced = noOfPartsInvoiced;
    }

    public boolean isItemForInvoice() {
        return itemForInvoice;
    }

    public void setItemForInvoice(boolean itemForInvoice) {
        this.itemForInvoice = itemForInvoice;
    }

    public List<OleInvoiceNote> getInvoiceNotes() {
        return invoiceNotes;
    }

    public void setInvoiceNotes(List<OleInvoiceNote> invoiceNotes) {
        this.invoiceNotes = invoiceNotes;
    }

    public String getOpenQuantity() {
        return String.valueOf(poOutstandingQuantity);
    }

    public void setOpenQuantity(String openQuantity) {
        this.poOutstandingQuantity = new KualiDecimal(openQuantity);
    }

    public KualiDecimal getPoOutstandingQuantity() {
        return poOutstandingQuantity;
    }

    public void setPoOutstandingQuantity(KualiDecimal poOutstandingQuantity) {
        this.poOutstandingQuantity = poOutstandingQuantity;
    }

    public String getInvoiceItemListPrice() {
        return invoiceItemListPrice;
    }

    public void setInvoiceItemListPrice(String invoiceItemListPrice) {
        this.invoiceItemListPrice = invoiceItemListPrice;
    }

    public String getInvoiceForeignItemListPrice() {
        return invoiceForeignItemListPrice;
    }

    public void setInvoiceForeignItemListPrice(String invoiceForeignItemListPrice) {
        this.invoiceForeignItemListPrice = invoiceForeignItemListPrice;
    }

    public String getInvoiceExchangeRate() {
        return invoiceExchangeRate;
    }

    public void setInvoiceExchangeRate(String invoiceExchangeRate) {
        this.invoiceExchangeRate = invoiceExchangeRate;
    }

    public String getInvoiceForeignDiscount() {
        return invoiceForeignDiscount;
    }

    public void setInvoiceForeignDiscount(String invoiceForeignDiscount) {
        this.invoiceForeignDiscount = invoiceForeignDiscount;
    }

    public String getInvoiceForeignUnitCost() {
        return invoiceForeignUnitCost;
    }

    public void setInvoiceForeignUnitCost(String invoiceForeignUnitCost) {
        this.invoiceForeignUnitCost = invoiceForeignUnitCost;
    }

    public String getInvoiceForeignCurrency() {
        return invoiceForeignCurrency;
    }

    public void setInvoiceForeignCurrency(String invoiceForeignCurrency) {
        this.invoiceForeignCurrency = invoiceForeignCurrency;
    }

    public String getInvoiceForeignDiscountType() {
        return invoiceForeignDiscountType;
    }

    public void setInvoiceForeignDiscountType(String invoiceForeignDiscountType) {
        this.invoiceForeignDiscountType = invoiceForeignDiscountType;
    }

    public Date getSubscriptionFromDate() {
        return subscriptionFromDate;
    }

    public void setSubscriptionFromDate(Date subscriptionFromDate) {
        this.subscriptionFromDate = subscriptionFromDate;
    }

    public Date getSubscriptionToDate() {
        return subscriptionToDate;
    }

    public void setSubscriptionToDate(Date subscriptionToDate) {
        this.subscriptionToDate = subscriptionToDate;
    }

    public boolean isSubscriptionOverlap() {
        return subscriptionOverlap;
    }

    public void setSubscriptionOverlap(boolean subscriptionOverlap) {
        this.subscriptionOverlap = subscriptionOverlap;
    }

    public List<OleCopy> getDeletedCopiesList() {
        return deletedCopiesList;
    }

    public void setDeletedCopiesList(List<OleCopy> deletedCopiesList) {
        this.deletedCopiesList = deletedCopiesList;
    }

    /**
     * This method returns the Invoice History for the given Item
     * @return paymentHistoryInvoiceViews
     */
    /*public List<InvoiceView> getPaymentHistoryInvoiceViews() {
        if (this.getPurapDocument() != null && ((OlePurchaseOrderDocument)this.getPurapDocument()).getRelatedViews() != null) {
            paymentHistoryInvoiceViews = ((OlePurchaseOrderDocument)this.getPurapDocument()).getRelatedViews().updateRelatedView
                    (InvoiceView.class, paymentHistoryInvoiceViews, false);
        }

        return paymentHistoryInvoiceViews;
    }
*/
    /**
     * This method sets the Invoice History for the Given Items
     * @param paymentHistoryInvoiceViews
     */
   /* public void setPaymentHistoryInvoiceViews(List<InvoiceView> paymentHistoryInvoiceViews) {
        this.paymentHistoryInvoiceViews = paymentHistoryInvoiceViews;
    }*/

    public List<OleInvoiceDocument> getInvoiceDocuments() {
        return invoiceDocuments;
    }

    public void setInvoiceDocuments(List<OleInvoiceDocument> invoiceDocuments) {
        this.invoiceDocuments = invoiceDocuments;
    }

    public boolean isLocationFlag() {
        return locationFlag;
    }

    public void setLocationFlag(boolean locationFlag) {
        this.locationFlag = locationFlag;
    }

    public boolean isClaimFilter() {
        return claimFilter;
    }

    public void setClaimFilter(boolean claimFilter) {
        this.claimFilter = claimFilter;
    }

    public boolean isItemLocationChangeFlag() {
        return itemLocationChangeFlag;
    }

    public void setItemLocationChangeFlag(boolean itemLocationChangeFlag) {
        this.itemLocationChangeFlag = itemLocationChangeFlag;
    }

    public KualiDecimal getPreviousItemQuantity() {
        return previousItemQuantity;
    }

    public void setPreviousItemQuantity(KualiDecimal previousItemQuantity) {
        this.previousItemQuantity = previousItemQuantity;
    }

    public KualiInteger getPreviousItemNoOfParts() {
        return previousItemNoOfParts;
    }

    public void setPreviousItemNoOfParts(KualiInteger previousItemNoOfParts) {
        this.previousItemNoOfParts = previousItemNoOfParts;
    }

    //Added for Jira OLE-5370
    public String getTitle() {
        title = getItemDescription();
        if(title.contains(OLEConstants.ANGLE_BRACKET_LESS_THAN)){
            title = title.replace(OLEConstants.ANGLE_BRACKET_LESS_THAN,"&#60;");
        }
        if(title.contains(OLEConstants.ANGLE_BRACKET_GREATER_THAN)){
            title = title.replace(OLEConstants.ANGLE_BRACKET_GREATER_THAN,"&#62;");
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BibInfoRecord getBibInfoRecord() {
        return bibInfoRecord;
    }

    public void setBibInfoRecord(BibInfoRecord bibInfoRecord) {
        this.bibInfoRecord = bibInfoRecord;
    }

    public String getOleERSIdentifier() {
        if (this.getCopyList()!=null && this.getCopyList().size()>0){
            return this.getCopyList().get(0).getOleERSIdentifier();
        }
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String geteResLink() {
        if (this.getOleERSIdentifier() != null) {
            OLEEResourceRecordDocument oleeResourceRecordDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OLEEResourceRecordDocument.class, this.getOleERSIdentifier());
            if (oleeResourceRecordDocument != null) {
                String eResourceurl = ConfigContext.getCurrentContextConfig().getProperty("ole.eresource.url");
                String url = eResourceurl + "oleERSController?viewId=OLEEResourceRecordView&amp;" + KRADConstants.DISPATCH_REQUEST_PARAMETER + "=" + KRADConstants.DOC_HANDLER_METHOD + "&" + KRADConstants.PARAMETER_DOC_ID + "=" + oleeResourceRecordDocument.getDocumentNumber() + "&" + KRADConstants.PARAMETER_COMMAND + "=" + KewApiConstants.DOCSEARCH_COMMAND;
                return url;

            }
        }
        return eResLink;
    }

    public void seteResLink(String eResLink) {
        this.eResLink = eResLink;
    }

    public String getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(String purposeId) {
        this.purposeId = purposeId;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
