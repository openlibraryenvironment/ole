package org.kuali.ole.describe.bo;

import org.kuali.ole.docstore.common.search.SearchResultField;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: krishnamohanv
 * Date: 1/9/14
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.                                     OleDocument
 */
public class SearchResultDisplayRow  {

    // Common fields for All
    protected String localId;
    protected String docFormat;
    protected String docType;

    private String publisher;
    private boolean select;
    private String staffOnly = "false";
    private String bibIdentifier;
    private String holdingsIdentifier;
    private String instanceIdentifier;
    private String locationName;
    private String linkedBibCount;
    private String callNumber;
    private String tokenId;
    private String oleERSIdentifier;
    // Bib
    private String title;
    private String journalTitle;
    private String titleDisplay;
    private String resourceType;
    private String carrier;
    private String author;
    private String publicationDate;
    private String isbn;
    private String issn;
    private String subject;
    private String publicationPlace;
    private String edition;
    private String format;
    private String formGenre;
    private String language;
    private String description;
    private String holdingsNote;
    private String extentOfOwnershipNoteType;
    private String extentOfOwnershipNoteValue;
    private String extentOfOwnershipType;
    private boolean  selectFlag;

    // Item
    private String barcode;
    private String barcodeArsl;
    private String volumeNumber;
    private String volumeNumberLabel;
    private String holdingsLocation;
    private String holdingsCallNumber;
    private String analyticItem = "false";
    //Holdings
    private String callNumberPrefix;
    private String classificationPart;
    private String shelvingOrder;
    private String uri;
    private String receiptStatus;
    private String shelvingOrderCode;
    private String shelvingSchemeCode;
    private String shelvingSchemeValue;
    private String locationLevel;
    private String locationLevelName;
    private String itemPart;
    private String seriesHolding = "false";
    private String boundWithHolding = "false";
    private String dueDateTime;

    // eHoldings
    private String accessStatus;
    private String platForm;
    private String imprint;
    private String statisticalCode;
    private String prefixAndCallnumber;
    private String enumeration;
    private String chronology;
    private String copyNumber;
    private String copyNumberLabel;
    private String itemStatus;
    private String PoLineItemId;
    private List<Integer> poLineItemIds=new ArrayList<>();
    private String itemTypeCodeValue;
    private String itemTypeFullValue;
    private String itemIdentifier;
    private String coverageDate;
    private String perpetualAccess;
    private String publicNote;
    private String url;
    private String numberOfSimultaneousUses;
    private String persistLink;
    private String accessLocation;
    private String adminUserName;
    private String accessUserName;
    private String accessPassword;
    private String adminUrl;
    private String authentication;
    private String proxied;
    private String ill;
    private String subscription;
    private String linkText;
    private String adminPassword;
    private String donorPublic;
    private String donorNote;
    private String donorCode;

    private SearchResultDisplayFields searchResultDisplayFields;

    public String getBoundWithHolding() {
        return boundWithHolding;
    }

    public void setBoundWithHolding(String boundWithHolding) {
        if(boundWithHolding!=null){
            this.boundWithHolding = boundWithHolding;
        }
    }

    public String getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(String dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public String getAnalyticItem() {
        return analyticItem;
    }

    public void setAnalyticItem(String analyticItem) {
        if(analyticItem!=null){
            this.analyticItem = analyticItem;
        }
    }

    public String getSeriesHolding() {
        return seriesHolding;
    }

    public void setSeriesHolding(String seriesHolding) {
        if(seriesHolding!=null){
            this.seriesHolding = seriesHolding;
        }
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(this.title == null || this.title == "") {
            this.title = title;
        }
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public String getTitleDisplay() {
        return titleDisplay;
    }

    public void setTitleDisplay(String titleDisplay) {
        this.titleDisplay = titleDisplay;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getCallNumberPrefix() {
        return callNumberPrefix;
    }

    public void setCallNumberPrefix(String callNumberPrefix) {
        this.callNumberPrefix = callNumberPrefix;
    }

    public String getClassificationPart() {
        return classificationPart;
    }

    public void setClassificationPart(String classificationPart) {
        this.classificationPart = classificationPart;
    }

    public String getShelvingOrder() {
        return shelvingOrder;
    }

    public void setShelvingOrder(String shelvingOrder) {
        this.shelvingOrder = shelvingOrder;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPublicationPlace() {
        return publicationPlace;
    }

    public void setPublicationPlace(String publicationPlace) {
        this.publicationPlace = publicationPlace;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormGenre() {
        return formGenre;
    }

    public void setFormGenre(String formGenre) {
        this.formGenre = formGenre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        if(this.bibIdentifier == null || this.bibIdentifier == ""){
            this.bibIdentifier = bibIdentifier;
        }
    }

    public String getHoldingsIdentifier() {
        return holdingsIdentifier;
    }

    public void setHoldingsIdentifier(String holdingsIdentifier) {
        this.holdingsIdentifier = holdingsIdentifier;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public String getStaffOnly() {
        return staffOnly;
    }

    public void setStaffOnly(String staffOnly) {
        this.staffOnly = staffOnly;
    }

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    public void setInstanceIdentifier(String instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLinkedBibCount() {
        return linkedBibCount;
    }

    public void setLinkedBibCount(String linkedBibCount) {
        this.linkedBibCount = linkedBibCount;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    public String getPlatForm() {
        return platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }

    public String getImprint() {
        return imprint;
    }

    public void setImprint(String imprint) {
        this.imprint = imprint;
    }

    public String getStatisticalCode() {
        return statisticalCode;
    }

    public void setStatisticalCode(String statisticalCode) {
        this.statisticalCode = statisticalCode;
    }

    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }

    public String getPrefixAndCallnumber() {
        return prefixAndCallnumber;
    }

    public void setPrefixAndCallnumber(String prefixAndCallnumber) {
        this.prefixAndCallnumber = prefixAndCallnumber;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getChronology() {
        return chronology;
    }

    public void setChronology(String chronology) {
        this.chronology = chronology;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getPoLineItemId() {
        return PoLineItemId;
    }

    public String getShelvingOrderCode() {
        return shelvingOrderCode;
    }

    public void setShelvingOrderCode(String shelvingOrderCode) {
        this.shelvingOrderCode = shelvingOrderCode;
    }

    public void setPoLineItemId(String poLineItemId) {
        PoLineItemId = poLineItemId;
    }

    public String getShelvingSchemeCode() {
        return shelvingSchemeCode;
    }

    public void setShelvingSchemeCode(String shelvingSchemeCode) {
        this.shelvingSchemeCode = shelvingSchemeCode;
    }

    public String getShelvingSchemeValue() {
        return shelvingSchemeValue;
    }

    public void setShelvingSchemeValue(String shelvingSchemeValue) {
        this.shelvingSchemeValue = shelvingSchemeValue;
    }

    public String getLocationLevel() {
        return locationLevel;
    }

    public void setLocationLevel(String locationLevel) {
        this.locationLevel = locationLevel;
    }

    public String getLocationLevelName() {
        return locationLevelName;
    }

    public void setLocationLevelName(String locationLevelName) {
        this.locationLevelName = locationLevelName;
    }

    public SearchResultDisplayFields getSearchResultDisplayFields() {
        return searchResultDisplayFields;
    }

    public void setSearchResultDisplayFields(SearchResultDisplayFields searchResultDisplayFields) {
        this.searchResultDisplayFields = searchResultDisplayFields;
    }

    public String getItemPart() {
        return itemPart;
    }

    public void setItemPart(String itemPart) {
        this.itemPart = itemPart;
    }

    public String getHoldingsNote() {
        return holdingsNote;
    }

    public void setHoldingsNote(String holdingsNote) {
        this.holdingsNote = holdingsNote;
    }


    public String getExtentOfOwnershipNoteType() {
        return extentOfOwnershipNoteType;
    }

    public void setExtentOfOwnershipNoteType(String extentOfOwnershipNoteType) {
        this.extentOfOwnershipNoteType = extentOfOwnershipNoteType;
    }

    public String getExtentOfOwnershipNoteValue() {
        return extentOfOwnershipNoteValue;
    }

    public void setExtentOfOwnershipNoteValue(String extentOfOwnershipNoteValue) {
        this.extentOfOwnershipNoteValue = extentOfOwnershipNoteValue;
    }

    public String getExtentOfOwnershipType() {
        return extentOfOwnershipType;
    }

    public void setExtentOfOwnershipType(String extentOfOwnershipType) {
        this.extentOfOwnershipType = extentOfOwnershipType;
    }

    public String getBarcodeArsl() {
        return barcodeArsl;
    }

    public void setBarcodeArsl(String barcodeArsl) {
        this.barcodeArsl = barcodeArsl;
    }

    public String getVolumeNumberLabel() {
        return volumeNumberLabel;
    }

    public void setVolumeNumberLabel(String volumeNumberLabel) {
        this.volumeNumberLabel = volumeNumberLabel;
    }

    public String getCopyNumberLabel() {
        return copyNumberLabel;
    }

    public void setCopyNumberLabel(String copyNumberLabel) {
        this.copyNumberLabel = copyNumberLabel;
    }

    public String getItemTypeCodeValue() {
        return itemTypeCodeValue;
    }

    public void setItemTypeCodeValue(String itemTypeCodeValue) {
        this.itemTypeCodeValue = itemTypeCodeValue;
    }

    public String getItemTypeFullValue() {
        return itemTypeFullValue;
    }

    public void setItemTypeFullValue(String itemTypeFullValue) {
        this.itemTypeFullValue = itemTypeFullValue;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public List<Integer> getPoLineItemIds() {
        return poLineItemIds;
    }

    public void setPoLineItemIds(List<Integer> poLineItemIds) {
        this.poLineItemIds = poLineItemIds;
    }

    public String getNumberOfSimultaneousUses() {
        return numberOfSimultaneousUses;
    }

    public void setNumberOfSimultaneousUses(String numberOfSimultaneousUses) {
        this.numberOfSimultaneousUses = numberOfSimultaneousUses;
    }

    public String getPersistLink() {
        return persistLink;
    }

    public void setPersistLink(String persistLink) {
        this.persistLink = persistLink;
    }

    public String getAccessLocation() {
        return accessLocation;
    }

    public void setAccessLocation(String accessLocation) {
        this.accessLocation = accessLocation;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAccessUserName() {
        return accessUserName;
    }

    public void setAccessUserName(String accessUserName) {
        this.accessUserName = accessUserName;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
    }

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getProxied() {
        return proxied;
    }

    public void setProxied(String proxied) {
        this.proxied = proxied;
    }

    public String getIll() {
        return ill;
    }

    public void setIll(String ill) {
        this.ill = ill;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getDonorPublic() {
        return donorPublic;
    }

    public void setDonorPublic(String donorPublic) {
        this.donorPublic = donorPublic;
    }

    public String getDonorNote() {
        return donorNote;
    }

    public void setDonorNote(String donorNote) {
        this.donorNote = donorNote;
    }

    public String getCoverageDate() {
        return coverageDate;
    }

    public void setCoverageDate(String coverageDate) {
        this.coverageDate = coverageDate;
    }

    public String getPerpetualAccess() {
        return perpetualAccess;
    }

    public void setPerpetualAccess(String perpetualAccess) {
        this.perpetualAccess = perpetualAccess;
    }

    public String getPublicNote() {
        return publicNote;
    }

    public void setPublicNote(String publicNote) {
        this.publicNote = publicNote;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHoldingsLocation() {
        return holdingsLocation;
    }

    public void setHoldingsLocation(String holdingsLocation) {
        this.holdingsLocation = holdingsLocation;
    }

    public String getHoldingsCallNumber() {
        return holdingsCallNumber;
    }

    public void setHoldingsCallNumber(String holdingsCallNumber) {
        this.holdingsCallNumber = holdingsCallNumber;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public void buildBibSearchResultField(List<SearchResultField> searchResultFields, String eResourceId) {
        for (SearchResultField searchResultField : searchResultFields) {
            // if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode())) {
            if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                setLocalId(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                    setTitle(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Author_display")) {
                setAuthor(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("PublicationDate_display")) {
                setPublicationDate(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
               setDocFormat(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                setBibIdentifier(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
               setBibIdentifier(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("staffOnlyFlag")) {
                setStaffOnly(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ISBN_display")) {
                setIsbn(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ISSN_display")) {
                setIssn(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Publisher_display")) {
                setPublisher(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Subject_display")) {
                setSubject(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("PublicationPlace_display")) {
                setPublicationPlace(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Edition_display")) {
                setEdition(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Format_display")) {
                setFormat(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("FormGenre_display")) {
                setFormGenre(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Language_display")) {
                setLanguage(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Description_display")) {
                setDescription(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("staffOnlyFlag")) {
                setStaffOnly(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("JournalTitle_display")) {
                setJournalTitle(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                setTitleDisplay(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ResourceType_display")) {
                setResourceType(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Carrier_display")) {
                setCarrier(searchResultField.getFieldValue());
            }
            if (eResourceId != null) {
                setOleERSIdentifier(eResourceId);
            }
            if (this.tokenId != null) {
                setTokenId(this.tokenId);
            }

        }
    }

    public void buildHoldingSearchResultField(List<SearchResultField> searchResultFields) {
        for (SearchResultField searchResultField : searchResultFields) {
            if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                setLocalId(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                setTitle(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                setCallNumber(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
                setDocFormat(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("DocType")) {
                setDocType(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                setBibIdentifier(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                setHoldingsIdentifier(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("itemIdentifier")) {
                setItemIdentifier(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                setLocationName(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("LocationLevelName_display")) {
                setLocationLevelName(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("staffOnlyFlag")) {
                setStaffOnly(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsNote_display")) {
                setHoldingsNote(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("CallNumberPrefix_display")) {
                setCallNumberPrefix(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ClassificationPart_display")) {
                setClassificationPart(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ShelvingOrder_display")) {
                setShelvingOrder(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ShelvingSchemeCode_display")) {
                setShelvingSchemeCode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ShelvingSchemeValue_display")) {
                setShelvingSchemeValue(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ShelvingOrderCode_display")) {
                setShelvingOrderCode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Uri_display")) {
                setUri(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ReceiptStatus_display")) {
                setReceiptStatus(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsNote_display")) {
                setHoldingsNote(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("isSeries")) {
               setSeriesHolding(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("isAnalytic")) {
                setAnalyticItem(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("isBoundwith")) {
               setBoundWithHolding(searchResultField.getFieldValue());
            }
            //added below code for global edit change for comparing text files into result field.
            if (searchResultField.getFieldName().equalsIgnoreCase("ItemBarcode_display")) {
                setBarcode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ExtentOfOwnership_Note_Type_display")) {
                setExtentOfOwnershipNoteType(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ExtentOfOwnership_Note_Value_display")) {
                setExtentOfOwnershipNoteValue(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ExtentOfOwnership_Type_display")) {
                setExtentOfOwnershipType(searchResultField.getFieldValue());
            }
        }
    }

    public void buildItemSearchResultField(List<SearchResultField> searchResultFields) {
        for (SearchResultField searchResultField : searchResultFields) {
            if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                setLocalId(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                setTitle(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("locationName")) {
                setLocationName(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                setCallNumber(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("CallNumberPrefix_display")) {
                setCallNumberPrefix(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("BarcodeARSL_display")) {
                setBarcode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("docFormat")) {
                setDocFormat(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("DocType")) {
                setDocType(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                setBibIdentifier(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                setHoldingsIdentifier(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                setItemIdentifier(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ItemBarcode_display")) {
                setBarcode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                setLocationName(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("staffOnlyFlag")) {
                setStaffOnly(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ClassificationPart_display")) {
                setClassificationPart(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ShelvingOrder_display")) {
                setShelvingOrder(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ShelvingSchemeCode_display")) {
                setShelvingSchemeCode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ShelvingSchemeValue_display")) {
                setShelvingSchemeValue(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ShelvingOrderCode_display")) {
                setShelvingOrderCode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ItemStatus_display")) {
                setItemStatus(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("CopyNumber_display")) {
                setCopyNumber(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("CopyNumberLabel_display")) {
                setCopyNumberLabel(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("VolumeNumber_display")) {
                setVolumeNumber(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("VolumeNumberLabel_display")) {
                setVolumeNumberLabel(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Enumeration_display")) {
                setEnumeration(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Chronology_display")) {
                setChronology(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ItemIdentifier_display")) {
                setItemIdentifier(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ItemTypeCodeValue_display")) {
                setItemTypeCodeValue(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ItemTypeFullValue_display")) {
                setItemTypeFullValue(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("isAnalytic")) {
                setAnalyticItem(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("DonorCode_display")) {
                setDonorCode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("DueDateTime")) {
                setDueDateTime(searchResultField.getFieldValue());
            }
			 if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsLocation_display")) {
                setHoldingsLocation(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsCallNumber_display")) {
                setHoldingsCallNumber(searchResultField.getFieldValue());
            }

        }
    }

    public void buildEHoldingSearchResultField(List<SearchResultField> searchResultFields) {
        for (SearchResultField searchResultField : searchResultFields) {

            if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
                setDocFormat(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("DocType")) {
                setDocType(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                setBibIdentifier(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                setHoldingsIdentifier(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("staffOnlyFlag")) {
                setStaffOnly(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Access_Password_display")) {
                setAccessPassword(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Access_UserName_display")) {
                setAccessUserName(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("AccessLocation_display")) {
                setAccessLocation(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("AccessStatus_display")) {
                setAccessStatus(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Admin_Password_display")) {
                setAdminPassword(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Admin_url_display")) {
                setAdminUrl(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Admin_UserName_display")) {
                setAdminUserName(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Authentication_display")) {
                setAuthentication(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                setCallNumber(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsLocation_display")) {
                setHoldingsLocation(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsCallNumber_display")) {
                setHoldingsCallNumber(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("CallNumberPrefix_display")) {
                setCallNumberPrefix(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ClassificationPart")) {
                setClassificationPart(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("CoverageDate_display")) {
                setCoverageDate(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("DonorPublic_display")) {
                setDonorPublic(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("DonorNote_display")) {
                setDonorNote(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("DonorCode_display")) {
                setDonorCode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("E_Publisher_display")) {
                setPublisher(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsNote_display")) {
                setHoldingsNote(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ILL_display")) {
                setIll(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Imprint_display")) {
                setImprint(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ItemPart_display")) {
                setItemPart(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Link_Text_display")) {
                setLinkText(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                setLocalId(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                setLocationName(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("LocationLevel")) {
                setLocationLevel(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("LocationLevelName_display")) {
                setLocationLevelName(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("NumberOfSimultaneousUses_display")) {
                setNumberOfSimultaneousUses(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("PerpetualAccess_display")) {
                setPerpetualAccess(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Persist_Link_display")) {
                setPersistLink(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Platform_display")) {
                setPlatForm(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Proxied_display")) {
                setProxied(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ShelvingOrderCode_display")) {
                setShelvingOrderCode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ShelvingSchemeCode_display")) {
                setShelvingSchemeCode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("ShelvingSchemeValue_display")) {
                setShelvingSchemeValue(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("StatisticalSearchingCodeValue_display")) {
                setStatisticalCode(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Subscription_display")) {
                setSubscription(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Title_sort")) {
                setTitle(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                setTitle(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("Url_display")) {
                setUrl(searchResultField.getFieldValue());
            }
            if (searchResultField.getFieldName().equalsIgnoreCase("DonorPublic_display")) {
                setPublicNote(searchResultField.getFieldValue());
            }
        }
    }

}
