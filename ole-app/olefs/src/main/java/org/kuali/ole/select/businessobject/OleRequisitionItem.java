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


import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.document.ids.BibId;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibInfoRecord;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.RequisitionItem;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.bo.OLEEResourceOrderRecord;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.bo.OLEPOClaimHistory;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.document.OleInvoiceDocument;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Requisition Item Business Object.
 */
public class OleRequisitionItem extends RequisitionItem implements OlePurchasingItem {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleRequisitionItem.class);
    private BibInfoBean bibInfoBean;
    private OleOrderRecord oleOrderRecord;
    private OLEEResourceOrderRecord oleEResourceOrderRecord;
    private String docFormat;
    protected String itemTitleId;
    protected String linkToOrderOption;
    protected String localTitleId;
    protected String bibUUID;
    protected String requestSourceUrl;
    protected Integer requestorTypeId;
    protected String requestorId;
    protected String internalRequestorId;
    protected String requestorFirstName;
    protected String requestorLastName;
    protected String requestorName;
    protected OleRequestor oleRequestor;
    private DocData docData;
    private PersonImpl personImpl;
    private PersonImpl internalRequestor;
    protected String vendorItemPoNumber;
    private BusinessObjectService businessObjectService;
    private String singleCopyNumber;
    private String donorId;
    private String donorCode;
    private List<OLELinkPurapDonor> oleDonors=new ArrayList<>();
    private BibId bibTree;
    private String oleERSIdentifier;
    private String eResLink;

    private RequisitionDocument requisition;
    protected String selector;
    protected String statusCode;

    protected boolean itemAdded = false;
    protected String itemTitle;
    protected String itemAuthor;
    protected String fromDateLastModified;
    protected String toDateLastModified;
    protected String dateModified;
    protected KualiDecimal itemPrice;
    protected String internalRequestorName;
    protected String externalRequestorName;
    protected KualiInteger itemNoOfParts;
    protected KualiDecimal itemListPrice;
    protected KualiDecimal itemDiscount;
    protected String itemDiscountType;
    // private DiscountType discountType;

    // Foreign Currency Conversion
    protected String itemCurrencyType;
    protected KualiDecimal itemForeignListPrice;
    protected KualiDecimal itemForeignDiscount;
    protected String itemForeignDiscountType;
    protected KualiDecimal itemForeignDiscountAmt;
    protected KualiDecimal itemForeignUnitCost;
    protected BigDecimal itemExchangeRate;
    protected KualiDecimal itemUnitCostUSD;
    protected boolean itemRouteToRequestorIndicator;
    protected boolean itemPublicViewIndicator;


    //private OleRequestSourceType oleRequestSourceType;
    protected Integer categoryId;
    protected Integer formatTypeId;
    protected Integer requestSourceTypeId;
    protected Integer receiptStatusId;
    protected Integer itemPriceSourceId;
    private OleItemPriceSource itemPriceSource;
    private String purposeId;
    private OleCategory category;
    private OleFormatType formatTypeName;
    private OleRequestSourceType oleRequestSourceType;
    private OleReceiptStatus oleReceiptStatus;
    private List<OleCopy> deletedCopiesList = new ArrayList<>();

    private List<OleInvoiceDocument> invoiceDocuments = new ArrayList<OleInvoiceDocument>();


    //AcquiisitionsSearch
    protected String initialDate;
    protected String documentType;
    protected String initiator;

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
    private String caption;
    private String volumeNumber;
    private OleRequisitionCopies oleRequisitionCopy;
    //private String                               instanceId;
    private List<OleCopies> copies = new ArrayList<OleCopies>();
    private List<OleCopy> copyList = new ArrayList<>();

    /**
     * For List Payment History
     */
    private String paymentHistory;
    private List<OleRequisitionPaymentHistory> requisitionPaymentHistory = new ArrayList<OleRequisitionPaymentHistory>();

    private String noOfCopiesReceived;
    private String noOfPartsReceived;
    private String itemTypeDescription;

    private OLERequestorPatronDocument olePatronDocument;
    protected String olePatronId;
    private String firstName;
    private String enumeration;
    private String location;
    private String copyNumber;
    private String receiptStatus;

    private String                               itemStatus;

    /**
     * For getting the NoteTypeId and Note
     */
    private Integer noteTypeId;
    private String note;

    /**
     * For List of Notes
     */
    private List<OleNotes> notes;

    /**
     * For Quantity As Integer
     */
    private KualiInteger oleItemQuantity;

    private boolean doNotClaim;
    private Date claimDate;
    private String claimNote;
    private KualiInteger claimCount;
    private BibInfoRecord bibInfoRecord;
    private String fundCode;

    private List<OLEPOClaimHistory> claimHistories = new ArrayList<>();

    public List<OLEPOClaimHistory> getClaimHistories() {
        return claimHistories;
    }

    public void setClaimHistories(List<OLEPOClaimHistory> claimHistories) {
        this.claimHistories = claimHistories;
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

    public boolean isDoNotClaim() {
        return doNotClaim;
    }

    public void setDoNotClaim(boolean doNotClaim) {
        this.doNotClaim = doNotClaim;
    }

    public String getLocalTitleId() {
        return localTitleId;
    }

    public void setLocalTitleId(String localTitleId) {
        this.localTitleId = localTitleId;
    }

    /**
     * Default constructor.
     */
    public OleRequisitionItem() {
        this.setItemNoOfParts(new KualiInteger(1));
        this.setItemPublicViewIndicator(true);
        this.setItemUnitOfMeasureCode(PurapConstants.PODocumentsStrings.CUSTOMER_INVOICE_DETAIL_UOM_DEFAULT);
        this.setOleItemQuantity(new KualiInteger(1));
        if (this.getItemListPrice() == null) {
            this.setItemListPrice(new KualiDecimal(0.00));
        }
        this.setNoOfCopiesReceived("");
        this.setNoOfPartsReceived("");
        /*if(GlobalVariables.getUserSession()!=null) {
            this.internalRequestorId=GlobalVariables.getUserSession().getPrincipalId();
            this.setRequestorFirstName(getPersonName(GlobalVariables.getUserSession().getPrincipalId()));
        }*/
/*        if(SpringContext.getBean(OleRequisitionService.class).getRequestor()!=null) {
            String requestor=SpringContext.getBean(OleRequisitionService.class).getRequestor();
            this.setRequestorFirstName(requestor);
        }*/
        /*OleRequisitionService oleRequestionService=new OleRequisitionServiceImpl();
       if(oleRequestionService.getRequestor()!=null) {
            String requestor=oleRequestionService.getRequestor();
            this.setRequestorFirstName(requestor);
        }*/
        /**
         * Initializing OleRequisitionNotes List
         */
        LOG.debug("Inside OleRequisitionItem Constructor");
        LOG.debug("Initializing OleRequisitionNotes List");
        notes = new ArrayList<OleNotes>();

    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public BibInfoBean getBibInfoBean() {
        return bibInfoBean;
    }

    @Override
    public void setBibInfoBean(BibInfoBean bibInfoBean) {
        this.bibInfoBean = bibInfoBean;
    }

    @Override
    public Integer getRequestorTypeId() {
        return requestorTypeId;
    }

    public void setRequestorTypeId(Integer requestorTypeId) {
        this.requestorTypeId = requestorTypeId;
    }

    @Override
    public String getItemTitleId() {
        return itemTitleId;
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

    public List<OLELinkPurapDonor> getOleDonors() {
        return oleDonors;
    }

    public void setOleDonors(List<OLELinkPurapDonor> oleDonors) {
        this.oleDonors = oleDonors;
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

    public String getBibUUID() {
        return bibUUID;
    }

    public void setBibUUID(String bibUUID) {
        this.bibUUID = bibUUID;
    }

    public String getRequestSourceUrl() {
        return requestSourceUrl;
    }

    public void setRequestSourceUrl(String requestSourceUrl) {
        this.requestSourceUrl = requestSourceUrl;
    }

    @Override
    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    @Override
    public String getRequestorFirstName() {
        return requestorFirstName;
    }

    @Override
    public void setRequestorFirstName(String requestorFirstName) {
        this.requestorFirstName = requestorFirstName;
    }

    public String getRequestorLastName() {
        return requestorLastName;
    }

    public void setRequestorLastName(String requestorLastName) {
        this.requestorLastName = requestorLastName;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public OleRequestor getOleRequestor() {
        return oleRequestor;
    }

    public void setOleRequestor(OleRequestor oleRequestor) {
        this.oleRequestor = oleRequestor;
    }

    public DocData getDocData() {
        return docData;
    }

    public void setDocData(DocData docData) {
        this.docData = docData;
    }

    public PersonImpl getPersonImpl() {
        return personImpl;
    }

    public void setPersonImpl(PersonImpl personImpl) {
        this.personImpl = personImpl;
    }

    @SuppressWarnings("unchecked")
    private List<String> getRolesForPrincipal(String principalId) {
        if (principalId == null) {
            return new ArrayList<String>();
        }
        Map<String, String> criteria = new HashMap<String, String>(2);
        criteria.put("members.memberId", principalId);
        criteria.put("members.memberTypeCode", MemberType.PRINCIPAL.getCode());
        return KimApiServiceLocator.getRoleService().getMemberParentRoleIds(MemberType.PRINCIPAL.getCode(), principalId);
    }

    public String getPersonName(String principalId) {
        Person person = SpringContext.getBean(PersonService.class).getPerson(principalId);
        return person.getName();
    }

    @Override
    public RequisitionDocument getRequisition() {
        return requisition;
    }

    @Override
    public void setRequisition(RequisitionDocument requisition) {
        setPurapDocument(requisition);
        this.requisition = requisition;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getStatusCode() {
        return getRequisition().getStatusCode();
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getFromDateLastModified() {
        return fromDateLastModified;
    }

    public void setFromDateLastModified(String fromDateLastModified) {
        this.fromDateLastModified = fromDateLastModified;
    }

    public String getToDateLastModified() {
        return toDateLastModified;
    }

    public void setToDateLastModified(String toDateLastModified) {
        this.toDateLastModified = toDateLastModified;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public boolean isItemAdded() {
        return itemAdded;
    }

    public void setItemAdded(boolean isItemAdded) {
        this.itemAdded = isItemAdded;
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
    public String getInternalRequestorId() {
        return internalRequestorId;
    }

    public void setInternalRequestorId(String internalRequestorId) {
        this.internalRequestorId = internalRequestorId;
    }

    public KualiDecimal getItemPrice() {
        return this.getTotalAmount();
    }

    public void setItemPrice(KualiDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public PersonImpl getInternalRequestor() {
        return internalRequestor;
    }

    public void setInternalRequestor(PersonImpl internalRequestor) {
        this.internalRequestor = internalRequestor;
    }

    public String getInternalRequestorName() {
        return internalRequestorName;
    }

    public void setInternalRequestorName(String internalRequestorName) {
        this.internalRequestorName = internalRequestorName;
    }

    public String getExternalRequestorName() {
        return externalRequestorName;
    }

    public void setExternalRequestorName(String externalRequestorName) {
        this.externalRequestorName = externalRequestorName;
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

/*    public OleRequestSourceType getOleRequestSourceType() {
        return oleRequestSourceType;
    }

    public void setOleRequestSourceType(OleRequestSourceType oleRequestSourceType) {
        this.oleRequestSourceType = oleRequestSourceType;
    }*/

    public Integer getRequestSourceTypeId() {
        return requestSourceTypeId;
    }

    public void setRequestSourceTypeId(Integer requestSourceTypeId) {
        this.requestSourceTypeId = requestSourceTypeId;
    }

    public OleRequestSourceType getOleRequestSourceType() {
        return oleRequestSourceType;
    }

    public void setOleRequestSourceType(OleRequestSourceType oleRequestSourceType) {
        this.oleRequestSourceType = oleRequestSourceType;
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

   /* public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }*/

    /**
     * This method... to get the noteType Id
     *
     * @return noteTypeId
     */
    public Integer getNoteTypeId() {
        return noteTypeId;
    }

    /**
     * This method to set noteType Id
     *
     * @param //noteTypeId
     */
    public void setNoteTypeId(Integer noteTypeId) {
        this.noteTypeId = noteTypeId;
    }

    /**
     * This method... to get the note description
     *
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * This method... to set note description
     *
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * This method... to get OleRequistionNotes List
     *
     * @return List of Notes(OleRequisitionNotes)
     */
    public List<OleNotes> getNotes() {
        return notes;
    }

    /**
     * This method... to set OleRequisitionNotes List
     */
    public void setNotes(List<OleNotes> notes) {
        this.notes = notes;
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

    public String getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    /**
     * @see org.kuali.ole.module.purap.businessobject.PurchasingItemBase#getAccountingLineClass()
     */
    @Override
    public Class getAccountingLineClass() {
        return OleRequisitionAccount.class;
    }

    public String getVendorItemPoNumber() {
        return vendorItemPoNumber;
    }

    public void setVendorItemPoNumber(String vendorItemPoNumber) {
        this.vendorItemPoNumber = vendorItemPoNumber;
    }

    public OleOrderRecord getOleOrderRecord() {
        return oleOrderRecord;
    }

    public void setOleOrderRecord(OleOrderRecord oleOrderRecord) {
        this.oleOrderRecord = oleOrderRecord;
    }

    public OLEEResourceOrderRecord getOleEResourceOrderRecord() {
        return oleEResourceOrderRecord;
    }

    public void setOleEResourceOrderRecord(OLEEResourceOrderRecord oleEResourceOrderRecord) {
        this.oleEResourceOrderRecord = oleEResourceOrderRecord;
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


    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    /**
     * Gets the noOfCopiesReceived attribute.
     *
     * @return Returns the noOfCopiesReceived.
     */
    public String getNoOfCopiesReceived() {
        return noOfCopiesReceived;
    }

    /**
     * Sets the noOfCopiesReceived attribute value.
     *
     * @param noOfCopiesReceived The noOfCopiesReceived to set.
     */
    public void setNoOfCopiesReceived(String noOfCopiesReceived) {
        this.noOfCopiesReceived = noOfCopiesReceived;
    }

    /**
     * Gets the noOfPartsReceived attribute.
     *
     * @return Returns the noOfPartsReceived.
     */
    public String getNoOfPartsReceived() {
        return noOfPartsReceived;
    }

    /**
     * Sets the noOfPartsReceived attribute value.
     *
     * @param noOfPartsReceived The noOfPartsReceived to set.
     */
    public void setNoOfPartsReceived(String noOfPartsReceived) {
        this.noOfPartsReceived = noOfPartsReceived;
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

    /**
     * Gets the oleRequisitionCopy attribute.
     *
     * @return Returns the oleRequisitionCopy.
     */
    public OleRequisitionCopies getOleRequisitionCopy() {
        return oleRequisitionCopy;
    }

    /**
     * Sets the oleRequisitionCopy attribute value.
     *
     * @param oleRequisitionCopy The oleRequisitionCopy to set.
     */
    public void setOleRequisitionCopy(OleRequisitionCopies oleRequisitionCopy) {
        this.oleRequisitionCopy = oleRequisitionCopy;
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
        if(oleItemQuantity!=null) {
            super.setItemQuantity(new KualiDecimal(oleItemQuantity.intValue()));
        }
    }

    /*
     * @Override public void setItemQuantity(KualiDecimal itemQuantity) { super.setItemQuantity(itemQuantity); this.oleItemQuantity
     * = itemQuantity.intValue(); }
     */

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

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public List<OleCopy> getCopyList() {
        return copyList;
    }

    public void setCopyList(List<OleCopy> copyList) {
        this.copyList = copyList;
    }

    public List<OleCopy> getDeletedCopiesList() {
        return deletedCopiesList;
    }

    public void setDeletedCopiesList(List<OleCopy> deletedCopiesList) {
        this.deletedCopiesList = deletedCopiesList;
    }

    public List<OleInvoiceDocument> getInvoiceDocuments() {
        return invoiceDocuments;
    }

    public void setInvoiceDocuments(List<OleInvoiceDocument> invoiceDocuments) {
        this.invoiceDocuments = invoiceDocuments;
    }

    public BibId getBibTree() {
        return bibTree;
    }

    public void setBibTree(BibId bibTree) {
        this.bibTree = bibTree;
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
}
