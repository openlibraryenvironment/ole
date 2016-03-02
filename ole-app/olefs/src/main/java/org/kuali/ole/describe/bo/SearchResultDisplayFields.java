package org.kuali.ole.describe.bo;

import org.kuali.ole.docstore.common.document.config.DocFieldConfig;
import org.kuali.ole.docstore.common.document.config.DocFormatConfig;
import org.kuali.ole.docstore.common.document.config.DocTypeConfig;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 3/15/14
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchResultDisplayFields {

    private boolean localId = false;
    private boolean title = false;
    private boolean journalTitle = false;
    private boolean author = false;
    private boolean publisher = false;
    private boolean isbn = false;
    private boolean issn = false;
    private boolean subject = false;
    private boolean publicationPlace = false;
    private boolean edition = false;
    private boolean format = false;
    private boolean formGenre = false;
    private boolean language = false;
    private boolean description = false;
    private boolean publicationDate = false;
    private boolean resourceType = false;
    private boolean carrier = false;

    private boolean location = false;
    private boolean callNumber = false;
    private boolean callNumberPrefix = false;
    private boolean classificationPart = false;
    private boolean shelvingOrder = false;
    private boolean shelvingOrderCode = false;
    private boolean shelvingSchemeCode = false;
    private boolean shelvingSchemeValue = false;
    private boolean uri = false;
    private boolean receiptStatus = false;
    private boolean copyNumber = false;
    private boolean copyNumberLabel = false;
    private boolean itemPart = false;
    private boolean locationLevel = false;
    private boolean locationLevelName = false;
    private boolean itemIdentifier = false;
    private boolean vendorLineItemIdentifier = false;

    private boolean barcode = false;
    private boolean barcodeArsl = false;
    private boolean volumeNumber = false;
    private boolean volumeNumberLabel = false;
    private boolean enumeration = false;
    private boolean chronology = false;
    private boolean itemStatus = false;
    private boolean itemTypeCodeValue = false;
    private boolean itemTypeFullValue = false;

    private boolean accessStatus = false;
    private boolean platform = false;
    private boolean imprint = false;
    private boolean statisticalCode = false;
    private boolean holdingsNote = false;
    private boolean coverageDate = false;
    private boolean perpetualAccess = false;
    private boolean publicNote = false;
    private boolean url = false;
    private boolean numberOfSimultaneousUses =false;
    private boolean persistLink =false;
    private boolean accessLocation =false;
    private boolean adminUserName =false;
    private boolean accessUserName =false;
    private boolean accessPassword =false;
    private boolean adminUrl =false;
    private boolean authentication =false;
    private boolean proxied =false;
    private boolean ill =false;
    private boolean subscription =false;
    private boolean linkText =false;
    private boolean adminPassword =false;
    private boolean donorPublic =false;
    private boolean donorNote =false;
    private boolean donorCode =false;
    private boolean extentOfOwnershipNoteType = false;
    private boolean extentOfOwnershipNoteValue = false;
    private boolean extentOfOwnershipType = false;
    private boolean itemType = false;
    private boolean dueDateTime = false;
    private boolean holdingsLocation = false;
    private boolean holdingsCallNumber = false;
    private String documentType;

    public SearchResultDisplayFields() {
    }

    public boolean isLocalId() {
        return localId;
    }

    public void setLocalId(boolean localId) {
        this.localId = localId;
    }

    public boolean isTitle() {
        return title;
    }

    public void setTitle(boolean title) {
        this.title = title;
    }

    public boolean isJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(boolean journalTitle) {
        this.journalTitle = journalTitle;
    }

    public boolean isAuthor() {
        return author;
    }

    public void setAuthor(boolean author) {
        this.author = author;
    }

    public boolean isPublisher() {
        return publisher;
    }

    public void setPublisher(boolean publisher) {
        this.publisher = publisher;
    }

    public boolean isIsbn() {
        return isbn;
    }

    public void setIsbn(boolean isbn) {
        this.isbn = isbn;
    }

    public boolean isIssn() {
        return issn;
    }

    public void setIssn(boolean issn) {
        this.issn = issn;
    }

    public boolean isSubject() {
        return subject;
    }

    public void setSubject(boolean subject) {
        this.subject = subject;
    }

    public boolean isPublicationPlace() {
        return publicationPlace;
    }

    public void setPublicationPlace(boolean publicationPlace) {
        this.publicationPlace = publicationPlace;
    }

    public boolean isEdition() {
        return edition;
    }

    public void setEdition(boolean edition) {
        this.edition = edition;
    }

    public boolean isFormat() {
        return format;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }

    public boolean isFormGenre() {
        return formGenre;
    }

    public void setFormGenre(boolean formGenre) {
        this.formGenre = formGenre;
    }

    public boolean isLanguage() {
        return language;
    }

    public void setLanguage(boolean language) {
        this.language = language;
    }

    public boolean isDescription() {
        return description;
    }

    public void setDescription(boolean description) {
        this.description = description;
    }

    public boolean isPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(boolean publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean isLocation() {
        return location;
    }

    public void setLocation(boolean location) {
        this.location = location;
    }

    public boolean isCallNumber() {
        return callNumber;
    }

    public void setCallNumber(boolean callNumber) {
        this.callNumber = callNumber;
    }

    public boolean isCallNumberPrefix() {
        return callNumberPrefix;
    }

    public void setCallNumberPrefix(boolean callNumberPrefix) {
        this.callNumberPrefix = callNumberPrefix;
    }

    public boolean isClassificationPart() {
        return classificationPart;
    }

    public void setClassificationPart(boolean classificationPart) {
        this.classificationPart = classificationPart;
    }

    public boolean isShelvingOrder() {
        return shelvingOrder;
    }

    public void setShelvingOrder(boolean shelvingOrder) {
        this.shelvingOrder = shelvingOrder;
    }

    public boolean isUri() {
        return uri;
    }

    public void setUri(boolean uri) {
        this.uri = uri;
    }

    public boolean isReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(boolean receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public boolean isBarcode() {
        return barcode;
    }

    public void setBarcode(boolean barcode) {
        this.barcode = barcode;
    }

    public boolean isVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(boolean volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public boolean isEnumeration() {
        return enumeration;
    }

    public void setEnumeration(boolean enumeration) {
        this.enumeration = enumeration;
    }

    public boolean isChronology() {
        return chronology;
    }

    public void setChronology(boolean chronology) {
        this.chronology = chronology;
    }

    public boolean isAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(boolean accessStatus) {
        this.accessStatus = accessStatus;
    }

    public boolean isPlatform() {
        return platform;
    }

    public void setPlatform(boolean platform) {
        this.platform = platform;
    }

    public boolean isImprint() {
        return imprint;
    }

    public void setImprint(boolean imprint) {
        this.imprint = imprint;
    }

    public boolean isStatisticalCode() {
        return statisticalCode;
    }

    public void setStatisticalCode(boolean statisticalCode) {
        this.statisticalCode = statisticalCode;
    }

    public boolean isItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(boolean itemStatus) {
        this.itemStatus = itemStatus;
    }

    public boolean isCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(boolean copyNumber) {
        this.copyNumber = copyNumber;
    }

    public boolean isItemPart() {
        return itemPart;
    }

    public void setItemPart(boolean itemPart) {
        this.itemPart = itemPart;
    }

    public boolean isLocationLevel() {
        return locationLevel;
    }

    public void setLocationLevel(boolean locationLevel) {
        this.locationLevel = locationLevel;
    }

    public boolean isLocationLevelName() {
        return locationLevelName;
    }

    public void setLocationLevelName(boolean locationLevelName) {
        this.locationLevelName = locationLevelName;
    }

    public boolean isShelvingOrderCode() {
        return shelvingOrderCode;
    }

    public void setShelvingOrderCode(boolean shelvingOrderCode) {
        this.shelvingOrderCode = shelvingOrderCode;
    }

    public boolean isShelvingSchemeCode() {
        return shelvingSchemeCode;
    }

    public void setShelvingSchemeCode(boolean shelvingSchemeCode) {
        this.shelvingSchemeCode = shelvingSchemeCode;
    }


    public boolean isShelvingSchemeValue() {
        return shelvingSchemeValue;
    }

    public void setShelvingSchemeValue(boolean shelvingSchemeValue) {
        this.shelvingSchemeValue = shelvingSchemeValue;
    }

    public boolean isHoldingsNote() {
        return holdingsNote;
    }

    public void setHoldingsNote(boolean holdingsNote) {
        this.holdingsNote = holdingsNote;
    }

    public boolean isCopyNumberLabel() {
        return copyNumberLabel;
    }

    public void setCopyNumberLabel(boolean copyNumberLabel) {
        this.copyNumberLabel = copyNumberLabel;
    }

    public boolean isItemIdentifier() {
        return itemIdentifier;
    }

    public void setItemIdentifier(boolean itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public boolean isVendorLineItemIdentifier() {
        return vendorLineItemIdentifier;
    }

    public void setVendorLineItemIdentifier(boolean vendorLineItemIdentifier) {
        this.vendorLineItemIdentifier = vendorLineItemIdentifier;
    }

    public boolean isBarcodeArsl() {
        return barcodeArsl;
    }

    public void setBarcodeArsl(boolean barcodeArsl) {
        this.barcodeArsl = barcodeArsl;
    }

    public boolean isVolumeNumberLabel() {
        return volumeNumberLabel;
    }

    public void setVolumeNumberLabel(boolean volumeNumberLabel) {
        this.volumeNumberLabel = volumeNumberLabel;
    }

    public boolean isItemTypeCodeValue() {
        return itemTypeCodeValue;
    }

    public void setItemTypeCodeValue(boolean itemTypeCodeValue) {
        this.itemTypeCodeValue = itemTypeCodeValue;
    }

    public boolean isItemTypeFullValue() {
        return itemTypeFullValue;
    }

    public void setItemTypeFullValue(boolean itemTypeFullValue) {
        this.itemTypeFullValue = itemTypeFullValue;
    }

    public boolean isNumberOfSimultaneousUses() {
        return numberOfSimultaneousUses;
    }

    public void setNumberOfSimultaneousUses(boolean numberOfSimultaneousUses) {
        this.numberOfSimultaneousUses = numberOfSimultaneousUses;
    }

    public boolean isPersistLink() {
        return persistLink;
    }

    public void setPersistLink(boolean persistLink) {
        this.persistLink = persistLink;
    }

    public boolean isAccessLocation() {
        return accessLocation;
    }

    public void setAccessLocation(boolean accessLocation) {
        this.accessLocation = accessLocation;
    }

    public boolean isAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(boolean adminUserName) {
        this.adminUserName = adminUserName;
    }

    public boolean isAccessUserName() {
        return accessUserName;
    }

    public void setAccessUserName(boolean accessUserName) {
        this.accessUserName = accessUserName;
    }

    public boolean isAccessPassword() {
        return accessPassword;
    }

    public void setAccessPassword(boolean accessPassword) {
        this.accessPassword = accessPassword;
    }

    public boolean isAdminUrl() {
        return adminUrl;
    }

    public void setAdminurl(boolean adminUrl) {
        this.adminUrl = adminUrl;
    }

    public boolean isAuthentication() {
        return authentication;
    }

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }

    public boolean isProxied() {
        return proxied;
    }

    public void setProxied(boolean proxied) {
        this.proxied = proxied;
    }

    public boolean isIll() {
        return ill;
    }

    public void setIll(boolean ill) {
        this.ill = ill;
    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }

    public boolean isLinkText() {
        return linkText;
    }

    public void setLinkText(boolean linkText) {
        this.linkText = linkText;
    }

    public boolean isAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(boolean adminPassword) {
        this.adminPassword = adminPassword;
    }

    public boolean isDonorPublic() {
        return donorPublic;
    }

    public void setDonorPublic(boolean donorPublic) {
        this.donorPublic = donorPublic;
    }

    public boolean isDonorNote() {
        return donorNote;
    }

    public void setDonorNote(boolean donorNote) {
        this.donorNote = donorNote;
    }

    public boolean isDonorCode() {
        return donorCode;
    }

    public void setDonorCode(boolean donorCode) {
        this.donorCode = donorCode;
    }

    public boolean isCoverageDate() {
        return coverageDate;
    }

    public void setCoverageDate(boolean coverageDate) {
        this.coverageDate = coverageDate;
    }

    public boolean isPerpetualAccess() {
        return perpetualAccess;
    }

    public void setPerpetualAccess(boolean perpetualAccess) {
        this.perpetualAccess = perpetualAccess;
    }

    public boolean isPublicNote() {
        return publicNote;
    }

    public void setPublicNote(boolean publicNote) {
        this.publicNote = publicNote;
    }

    public boolean isUrl() {
        return url;
    }

    public void setUrl(boolean url) {
        this.url = url;
    }

    public boolean isExtentOfOwnershipNoteType() {
        return extentOfOwnershipNoteType;
    }

    public void setExtentOfOwnershipNoteType(boolean extentOfOwnershipNoteType) {
        this.extentOfOwnershipNoteType = extentOfOwnershipNoteType;
    }

    public boolean isExtentOfOwnershipNoteValue() {
        return extentOfOwnershipNoteValue;
    }

    public void setExtentOfOwnershipNoteValue(boolean extentOfOwnershipNoteValue) {
        this.extentOfOwnershipNoteValue = extentOfOwnershipNoteValue;
    }

    public boolean isExtentOfOwnershipType() {
        return extentOfOwnershipType;
    }

    public void setExtentOfOwnershipType(boolean extentOfOwnershipType) {
        this.extentOfOwnershipType = extentOfOwnershipType;
    }

    public boolean isHoldingsLocation() {
        return holdingsLocation;
    }

    public void setHoldingsLocation(boolean holdingsLocation) {
        this.holdingsLocation = holdingsLocation;
    }

    public boolean isHoldingsCallNumber() {
        return holdingsCallNumber;
    }

    public void setHoldingsCallNumber(boolean holdingsCallNumber) {
        this.holdingsCallNumber = holdingsCallNumber;
    }

    public boolean isResourceType() {
        return resourceType;
    }

    public void setResourceType(boolean resourceType) {
        this.resourceType = resourceType;
    }

    public boolean isCarrier() {
        return carrier;
    }

    public void setCarrier(boolean carrier) {
        this.carrier = carrier;
    }
    public boolean isItemType() {
        return itemType;
    }

    public void setItemType(boolean itemType) {
        this.itemType = itemType;
    }

    public boolean isDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(boolean dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public void buildSearchResultDisplayFields(List<DocTypeConfig> docTypeConfigs, String docType) {
        setDocumentType(docType);
        for (DocTypeConfig docTypeConfig : docTypeConfigs) {
            if (docTypeConfig.getName().equals(docType)) {
                for (DocFormatConfig docFormatConfig : docTypeConfig.getDocFormatConfigList()) {
                    if (docFormatConfig.getDocTypeId().equals(docTypeConfig.getId())) {
                        for (DocFieldConfig docFieldConfig : docFormatConfig.getDocFieldConfigList()) {
                            if (docFieldConfig.isDisplayable() && docFieldConfig.getName().endsWith("_display") || ((docFormatConfig.getName().equalsIgnoreCase(DocFormat.MARC.getCode()) || docFormatConfig.getName().equalsIgnoreCase(DocFormat.OLEML.getCode())) && docFieldConfig.getName().equalsIgnoreCase("Title_display"))) {
                                // Condition : If a field is displayable or if it is a title field of marc or oleml doc format. Renders title column for all doc types as default, independent of displayable flag.

                                if(DocType.BIB.getCode().equalsIgnoreCase(docType)){
                                    if (docFieldConfig.getName().equalsIgnoreCase("LocalId_display")) {
                                        setLocalId(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Author_display")) {
                                        setAuthor(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Title_display")) {
                                        setTitle(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("JournalTitle_display")) {
                                        setJournalTitle(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Publisher_display")) {
                                        setPublisher(true);
                                    } else if (docFieldConfig.getName().equalsIgnoreCase("Isbn_display")) {
                                        setIsbn(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Issn_display")) {
                                        setIssn(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Subject_display")) {
                                        setSubject(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("PublicationPlace_display")) {
                                        setPublicationPlace(true);
                                    } else if (docFieldConfig.getName().equalsIgnoreCase("Edition_display")) {
                                        setEdition(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Format_display")) {
                                        setFormat(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("FormGenre_display")) {
                                        setFormGenre(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Language_display")) {
                                        setLanguage(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Description_display")) {
                                        setDescription(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("PublicationDate_display")) {
                                        setPublicationDate(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ResourceType_display")) {
                                        setResourceType(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Carrier_display")) {
                                        setCarrier(true);

                                    }
                                }
                                else {

                                    if (docFieldConfig.getName().equalsIgnoreCase("LocalId_display")) {
                                        setLocalId(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Title_display")) {
                                        setTitle(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Location_display")) {
                                        setLocation(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Location_display")) {
                                        setLocation(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("CallNumber_display")) {
                                        setCallNumber(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("CallNumberPrefix_display")) {
                                        setCallNumberPrefix(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ClassificationPart_display")) {
                                        setClassificationPart(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ShelvingOrder_display")) {
                                        setShelvingOrder(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ShelvingOrderCode_display")) {
                                        setShelvingOrderCode(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Uri_display")) {
                                        setUri(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ReceiptStatus_display")) {
                                        setReceiptStatus(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("CopyNumber_display")) {
                                        setCopyNumber(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("CopyNumberLabel_display")) {
                                        setCopyNumberLabel(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("HoldingsNote_display")) {
                                        setHoldingsNote(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ItemStatus_display")) {
                                        setItemStatus(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Barcode_display")) {
                                        setBarcode(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ItemBarcode_display")) {
                                        setBarcode(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("BarcodeARSL_display")) {
                                        setBarcode(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("VolumeNumber_display")) {
                                        setVolumeNumber(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("VolumeNumberLabel_display")) {
                                        setVolumeNumberLabel(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Enumeration_display")) {
                                        setEnumeration(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Chronology_display")) {
                                        setChronology(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("AccessStatus_display")) {
                                        setAccessStatus(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Platform_display")) {
                                        setPlatform(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Imprint_display")) {
                                        setImprint(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("StatisticalSearchingCodeValue_display")) {
                                        setStatisticalCode(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ItemPart_display")) {
                                        setItemPart(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Location_display")) {
                                        setLocationLevel(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("LocationLevelName_display")) {
                                        setLocationLevelName(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ShelvingSchemeCode_display")) {
                                        setShelvingSchemeCode(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ShelvingSchemeValue_display")) {
                                        setShelvingSchemeValue(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ItemTypeCodeValue_display")) {
                                        setItemTypeCodeValue(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ItemTypeFullValue_display")) {
                                        setItemTypeFullValue(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ItemIdentifier_display")) {
                                        setItemIdentifier(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ExtentOfOwnership_Note_Type_display")) {
                                        setExtentOfOwnershipNoteType(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ExtentOfOwnership_Note_Value_display")) {
                                        setExtentOfOwnershipNoteValue(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ExtentOfOwnership_Type_display")) {
                                        setExtentOfOwnershipType(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("NumberOfSimultaneousUses_display")) {
                                        setNumberOfSimultaneousUses(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Persist_Link_display")) {
                                        setPersistLink(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("AccessLocation_display")) {
                                        setAccessLocation(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Admin_UserName_display")) {
                                        setAdminUserName(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Access_UserName_display")) {
                                        setAccessUserName(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Access_Password_display")) {
                                        setAccessPassword(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Admin_url_display")) {
                                        setAdminurl(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Authentication_display")) {
                                        setAuthentication(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Proxied_display")) {
                                        setProxied(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ILL_display")) {
                                        setIll(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("HoldingsNote_display")) {
                                        setHoldingsNote(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Subscription_display")) {
                                        setSubscription(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Link_Text_display")) {
                                        setLinkText(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Admin_Password_display")) {
                                        setAdminPassword(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("DonorPublic_display")) {
                                        setDonorPublic(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("DonorNote_display")) {
                                        setDonorNote(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("DonorCode_display")) {
                                        setDonorCode(true);
                                    }

                                    else if (docFieldConfig.getName().equalsIgnoreCase("CoverageDate_display")) {
                                        setCoverageDate(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("PerpetualAccess_display")) {
                                        setPerpetualAccess(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Public_Note_display")) {
                                        setPublicNote(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("Url_display")) {
                                        setUrl(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("E_Publisher_display")) {
                                        setPublisher(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("ItemType_display")) {
                                        setItemType(true);
                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("dueDateTime_display")) {
                                        setDueDateTime(true);
                                    }

                                    else if (docFieldConfig.getName().equalsIgnoreCase("HoldingsLocation_display")) {
                                        setHoldingsLocation(true);

                                    }
                                    else if (docFieldConfig.getName().equalsIgnoreCase("HoldingsCallNumber_display")) {
                                        setHoldingsCallNumber(true);

                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "SearchResultDisplayFields{" +
                "localId=" + localId +
                ", title=" + title +
                ", journalTitle=" + journalTitle +
                ", author=" + author +
                ", publisher=" + publisher +
                ", isbn=" + isbn +
                ", issn=" + issn +
                ", subject=" + subject +
                ", publicationPlace=" + publicationPlace +
                ", edition=" + edition +
                ", format=" + format +
                ", formGenre=" + formGenre +
                ", language=" + language +
                ", description=" + description +
                ", publicationDate=" + publicationDate +
                ", resourceType=" + resourceType +
                ", carrier=" + carrier +
                ", location=" + location +
                ", callNumber=" + callNumber +
                ", callNumberPrefix=" + callNumberPrefix +
                ", classificationPart=" + classificationPart +
                ", shelvingOrder=" + shelvingOrder +
                ", shelvingOrderCode=" + shelvingOrderCode +
                ", shelvingSchemeCode=" + shelvingSchemeCode +
                ", shelvingSchemeValue=" + shelvingSchemeValue +
                ", uri=" + uri +
                ", receiptStatus=" + receiptStatus +
                ", copyNumber=" + copyNumber +
                ", copyNumberLabel=" + copyNumberLabel +
                ", itemPart=" + itemPart +
                ", locationLevel=" + locationLevel +
                ", locationLevelName=" + locationLevelName +
                ", itemIdentifier=" + itemIdentifier +
                ", vendorLineItemIdentifier=" + vendorLineItemIdentifier +
                ", barcode=" + barcode +
                ", barcodeArsl=" + barcodeArsl +
                ", volumeNumber=" + volumeNumber +
                ", volumeNumberLabel=" + volumeNumberLabel +
                ", enumeration=" + enumeration +
                ", chronology=" + chronology +
                ", itemStatus=" + itemStatus +
                ", itemTypeCodeValue=" + itemTypeCodeValue +
                ", itemTypeFullValue=" + itemTypeFullValue +
                ", accessStatus=" + accessStatus +
                ", platform=" + platform +
                ", imprint=" + imprint +
                ", statisticalCode=" + statisticalCode +
                ", holdingsNote=" + holdingsNote +
                ", coverageDate=" + coverageDate +
                ", perpetualAccess=" + perpetualAccess +
                ", publicNote=" + publicNote +
                ", url=" + url +
                ", numberOfSimultaneousUses=" + numberOfSimultaneousUses +
                ", persistLink=" + persistLink +
                ", accessLocation=" + accessLocation +
                ", adminUserName=" + adminUserName +
                ", accessUserName=" + accessUserName +
                ", accessPassword=" + accessPassword +
                ", adminUrl=" + adminUrl +
                ", authentication=" + authentication +
                ", proxied=" + proxied +
                ", ill=" + ill +
                ", subscription=" + subscription +
                ", linkText=" + linkText +
                ", adminPassword=" + adminPassword +
                ", donorPublic=" + donorPublic +
                ", donorNote=" + donorNote +
                ", donorCode=" + donorCode +
                ", extentOfOwnershipNoteType=" + extentOfOwnershipNoteType +
                ", extentOfOwnershipNoteValue=" + extentOfOwnershipNoteValue +
                ", extentOfOwnershipType=" + extentOfOwnershipType +
                ", itemType=" + itemType +
                ", dueDateTime=" + dueDateTime +
                ", holdingsLocation=" + holdingsLocation +
                ", holdingsCallNumber=" + holdingsCallNumber +
                '}';
    }
}
