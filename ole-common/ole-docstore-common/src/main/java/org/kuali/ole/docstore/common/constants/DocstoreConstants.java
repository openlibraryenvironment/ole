package org.kuali.ole.docstore.common.constants;

import java.text.SimpleDateFormat;

/**
 * Created by pvsubrah on 9/18/15.
 */
public interface DocstoreConstants {

    public static final String UNIQUE_ID = "uniqueId";
    public static final String DOC_TYPE = "DocType";
    public static final String DOC_FORMAT = "DocFormat";
    public static final String DOC_CATEGORY = "DocCategory";

    public static final String DOC_TYPE_ITEM_VALUE = "item";
    public static final String DOC_TYPE_HOLDING_VALUE = "holding";
    public static final String DOC_TYPE_INSTANCE_VALUE = "instance";

    public static final String DOC_CATEGORY_VALUE = "work";

    public static final String DOC_FORMAT_INSTANCE_VALUE = "oleml";

    public static final String ID = "id";

    public static final String ISBN_DISPLAY = "ISBN_display";
    public static final String ISSN_DISPLAY = "ISSN_display";
    public static final String LOCALID_SEARCH = "LocalId_search";
    public static final String LOCALID_DISPLAY = "LocalId_display";

    public static final String ALL_TEXT = "all_text";

    public static final String AUTHOR_SORT = "Author_sort";
    public static final String AUTHOR_DISPLAY = "Author_display";
    public static final String AUTHOR_SEARCH = "Author_search";
    public static final String AUTHOR_FACET = "Author_facet";

    public static final String DESCRIPTION_DISPLAY = "Description_display";
    public static final String DESCRIPTION_SEARCH = "Description_search";

    public static final String FORMAT_DISPLAY = "Format_display";
    public static final String FORMAT_SEARCH = "Format_search";
    public static final String FORMAT_FACET = "Format_facet";

    public static final String LANGUAGE_DISPLAY = "Language_display";
    public static final String LANGUAGE_SEARCH = "Language_search";
    public static final String LANGUAGE_FACET = "Language_facet";

    public static final String PUBLICATIONDATE_SORT = "PublicationDate_sort";
    public static final String PUBLICATIONDATE_DISPLAY = "PublicationDate_display";
    public static final String PUBLICATIONDATE_SEARCH = "PublicationDate_search";
    public static final String PUBLICATIONDATE_FACET = "PublicationDate_facet";


    public static final String PUBLISHER_DISPLAY = "Publisher_display";
    public static final String PUBLISHER_SEARCH = "Publisher_search";
    public static final String PUBLISHER_SORT = "Publisher_sort";

    public static final String PUBLICATIONPLACE_DISPLAY = "PublicationPlace_display";

    public static final String SUBJECT_DISPLAY = "Subject_display";
    public static final String SUBJECT_SEARCH = "Subject_search";
    public static final String SUBJECT_FACET = "Subject_facet";

    public static final String TITLE_SORT = "Title_sort";
    public static final String TITLE_DISPLAY = "Title_display";
    public static final String TITLE_SEARCH = "Title_search";
    public static final String MDF_035A = "mdf_035a";

    public static final String TYPE_SEARCH = "Type_search";
    public static final String TYPE_DISPLAY = "Type_display";

    public static final String EDITION_SEARCH = "Edition_search";
    public static final String EDITION_DISPLAY = "Edition_display";

    public static final String GENRE_FACET = "Genre_facet";

    public static final String ISBN_NOT_NORMALIZED = "not Normalized";

    public static final String COVERAGE_SEARCH = "Coverage_search";
    public static final String RELATION_SEARCH = "Relation_search";
    public static final String COVERAGE_DISPLAY = "Coverage_display";
    public static final String RELATION_DISPLAY = "Relation_display";
    public static final String SYSTEM_CONTROL_NUMBER = "SystemControlNumber";
    public static final String ISBN_SEARCH = "ISBN_search";
    public static final String ISSN_SEARCH = "ISSN_search";
    public static final String STAFF_ONLY_FLAG = "staffOnlyFlag";
    public static final String CREATED_BY = "createdBy";
    public static final String UPDATED_BY = "updatedBy";
    public static final String DATE_ENTERED = "dateEntered";
    public static final String DATE_UPDATED = "dateUpdated";
    public static final String STATUS_SEARCH = "Status_search";
    public static final String STATUS_DISPLAY = "Status_display";
    public static final String BIB_ID= "bibIdentifier";
    public static final String BIBLIOGRAPHIC_DELETE= "bibliographic_delete";

    public static final String CLMS_RET_FLAG="claimsReturnedFlag";
    public static final String CLMS_RET_FLAG_CRE_DATE="claimsReturnedFlagCreateDate";
    public static final String CLMS_RET_NOTE="claimsReturnedNote";
    public static final String CURRENT_BORROWER="currentBorrower";
    public static final String PROXY_BORROWER="proxyBorrower";
    public static final String DUE_DATE_TIME="dueDateTime";
    public static final String ORG_DUE_DATE_TIME="originalDueDate";
    public static final String ITEM_STATUS_EFFECTIVE_DATE="itemStatusEffectiveDate";
    public static final String CHECK_OUT_DUE_DATE_TIME="checkOutDateTime";

    public static final String STATUS_UPDATED_ON = "statusUpdatedOn";
    public static final String LEADER = "leader";
    public static final String IS_SERIES = "isSeries";
    public static final String IS_ANALYTIC = "isAnalytic";
    public static final String JOURNAL_TITLE_SEARCH = "JournalTitle_search";
    public static final String JOURNAL_TITLE_DISPLAY = "JournalTitle_display";
    public static final String JOURNAL_TITLE_SORT = "JournalTitle_sort";

    public static final String CREATE_RELATION = "CREATE";
    public static final String BREAK_RELATION = "BREAK";
    public static final String RESOURCETYPE_DISPLAY = "ResourceType_display";
    public static final String RESOURCETYPE_SEARCH = "ResourceType_search";
    public static final String CARRIER_DISPLAY = "Carrier_display";
    public static final String CARRIER_SEARCH = "Carrier_search";
    public static final String LEVEL1LOCATION_SEARCH = "Level1Location_search";
    public static final String LEVEL2LOCATION_SEARCH = "Level2Location_search";
    public static final String LEVEL3LOCATION_SEARCH = "Level3Location_search";
    public static final String LEVEL4LOCATION_SEARCH = "Level4Location_search";
    public static final String LEVEL5LOCATION_SEARCH = "Level5Location_search";
    public static final String LEVEL1LOCATION_DISPLAY = "Level1Location_display";
    public static final String LEVEL2LOCATION_DISPLAY = "Level2Location_display";
    public static final String LEVEL3LOCATION_DISPLAY = "Level3Location_display";
    public static final String LEVEL4LOCATION_DISPLAY = "Level4Location_display";
    public static final String LEVEL5LOCATION_DISPLAY = "Level5Location_display";


    public static final String LOCATION_LEVEL_SHELVING = "Shelving Location";
    public static final String LOCATION_LEVEL_SHELVING_1 = "Shelving";
    public static final String LOCATION_LEVEL_COLLECTION = "Collection";
    public static final String LOCATION_LEVEL_LIBRARY = "Library";
    public static final String LOCATION_LEVEL_INSTITUTION = "Institution";
    public static final String LOCATION_LEVEL_CAMPUS = "Campus";


     /*
        *Instance Common fields
     */

    public static final String FORMER_RESOURCE_IDENTIFIER_SOURCE_SEARCH = "Source_search";
    public static final String FORMER_RESOURCE_IDENTIFIER_SOURCE_DISPLAY = "Source_display";


    /**
     * Item & Holdings Common Fields.
     */



    //Sort fields
    public static final String CALLNUMBER_SORT = "CallNumber_sort";
    public static final String CALLNUMBER_PREFIX_SORT = "CallNumberPrefix_sort";
    public static final String ENUMERATION_SORT = "Enumeration_sort";
    public static final String CHRONOLOGY_SORT = "Chronology_sort";
    public static final String COPYNUMBER_SORT = "CopyNumber_sort";
    public static final String ITEM_BARCODE_SORT = "ItemBarcode_sort";

    /*
    *Holdings common fields
     */





    /*
   Item common fields
    */

    public static final String ITEM_IDENTIFIER_SEARCH = "ItemIdentifier_search";
    public static final String ITEM_TYPE_SEARCH = "ItemType_search";
    public static final String ITEM_BARCODE_SEARCH = "ItemBarcode_search";
    public static final String ITEM_URI_SEARCH = "ItemUri_search";
    public static final String PURCHASE_ORDER_LINE_ITEM_IDENTIFIER_SEARCH = "PurchaseOrderLineItemIdentifier_search";
    public static final String VENDOR_LINE_ITEM_IDENTIFIER_SEARCH = "VendorLineItemIdentifier_search";
    public static final String BARCODE_ARSL_SEARCH = "BarcodeARSL_search";
    public static final String STATISTICAL_SEARCHING_FULL_VALUE_SEARCH = "StatisticalSearchingFullValue_search";
    public static final String ITEM_TYPE_FULL_VALUE_SEARCH = "ItemTypeFullValue_search";
    public static final String ITEM_TYPE_CODE_VALUE_SEARCH = "ItemTypeCodeValue_search";

    public static final String VOLUME_NUMBER_SEARCH = "VolumeNumber_search";
    public static final String VOLUME_NUMBER_LABEL_SEARCH = "VolumeNumberLabel_search";
    public static final String ENUMERATION_SEARCH = "Enumeration_search";
    public static final String CHRONOLOGY_SEARCH = "Chronology_search";

    public static final String ITEM_IDENTIFIER_DISPLAY = "ItemIdentifier_display";
    public static final String ITEM_BARCODE_DISPLAY = "ItemBarcode_display";
    public static final String ITEM_URI_DISPLAY = "ItemUri_display";
    public static final String ITEM_TYPE_DISPLAY = "ItemType_display";
    public static final String PURCHASE_ORDER_LINE_ITEM_IDENTIFIER_DISPLAY = "PurchaseOrderLineItemIdentifier_display";
    public static final String VENDOR_LINE_ITEM_IDENTIFIER_DISPLAY = "VendorLineItemIdentifier_display";
    public static final String BARCODE_ARSL_DISPLAY = "BarcodeARSL_display";
    public static final String STATISTICAL_SEARCHING_FULL_VALUE_DISPLAY = "StatisticalSearchingFullValue_display";
    public static final String ITEM_TYPE_FULL_VALUE_DISPLAY = "ItemTypeFullValue_display";
    public static final String ITEM_TYPE_CODE_VALUE_DISPLAY = "ItemTypeCodeValue_display";
    public static final String VOLUME_NUMBER_DISPLAY = "VolumeNumber_display";
    public static final String VOLUME_NUMBER_LABEL_DISPLAY = "VolumeNumberLabel_display";
    public static final String ENUMERATION_DISPLAY = "Enumeration_display";
    public static final String CHRONOLOGY_DISPLAY = "Chronology_display";
    public static final String ITEM_STATUS_DISPLAY = "ItemStatus_display";
    public static final String ITEM_STATUS_SEARCH = "ItemStatus_search";
    public static final String ITEM_STATUS_SORT = "ItemStatus_sort";
    public static final String CLAIMS_RETURNED_NOTE_DISPLAY = "ClaimsReturnedNote_display";
    public static final String DAMAGED_ITEM_NOTE_DISPLAY = "DamagedItemNote_display";
    public static final String MISSING_PIECE_FLAG_NOTE_DISPLAY = "MissingPieceFlagNote_display";
    public static final String CLAIMS_RETURNED_FLAG_DISPLAY = "ClaimsReturnedFlag_display";
    public static final String ITEM_DAMAGED_FLAG_DISPLAY = "ItemDamagedStatus_display";
    public static final String MISSING_PIECE_FLAG_DISPLAY = "MissingPieceFlag_display";
    public static final String CLAIMS_RETURNED_NOTE_SEARCH = "ClaimsReturnedNote_search";
    public static final String DAMAGED_ITEM_NOTE_SEARCH = "DamagedItemNote_search";
    public static final String MISSING_PIECE_FLAG_NOTE_SEARCH = "MissingPieceFlagNote_search";
    public static final String CLAIMS_RETURNED_FLAG_SEARCH = "ClaimsReturnedFlag_search";
    public static final String ITEM_DAMAGED_FLAG_SEARCH= "ItemDamagedStatus_search";
    public static final String MISSING_PIECE_FLAG_SEARCH = "MissingPieceFlag_search";
    public static final String TEMPORARY_ITEM_TYPE_FULL_VALUE_SEARCH = "TemporaryItemTypeFullValue_search";
    public static final String TEMPORARY_ITEM_TYPE_CODE_VALUE_SEARCH = "TemporaryItemTypeCodeValue_search";
    public static final String TEMPORARY_ITEM_TYPE_FULL_VALUE_DISPLAY = "TemporaryItemTypeFullValue_display";
    public static final String TEMPORARY_ITEM_TYPE_CODE_VALUE_DISPLAY = "TemporaryItemTypeCodeValue_display";
    public static final String HIGHDENSITYSTORAGE_ROW_DISPLAY = "HighDensityStorage_Row_display";
    public static final String HIGHDENSITYSTORAGE_MODULE_DISPLAY = "HighDensityStorage_Module_display";
    public static final String HIGHDENSITYSTORAGE_SHELF_DISPLAY = "HighDensityStorage_Shelf_display";
    public static final String HIGHDENSITYSTORAGE_TRAY_DISPLAY = "HighDensityStorage_Tray_display";
    public static final String ITEMNOTE_VALUE_DISPLAY = "ItemNote_Value_display";
    public static final String ITEMNOTE_TYPE_DISPLAY = "ItemNote_Type_display";
    public static final String HOLDINGS_CALLNUMBER_SEARCH = "HoldingsCallNumber_search";
    public static final String HOLDINGS_CALLNUMBER_PREFIX_SEARCH = "HoldingsCallNumberPrefix_search";
    public static final String HOLDINGS_SHELVING_SCHEME_CODE_SEARCH = "HoldingsShelvingSchemeCode_search";
    public static final String HOLDINGS_SHELVING_SCHEME_VALUE_SEARCH = "HoldingsShelvingSchemeValue_search";
    public static final String HOLDINGS_LOCATION_SEARCH = "HoldingsLocation_search";
    public static final String HOLDINGS_CALLNUMBER_DISPLAY = "HoldingsCallNumber_display";
    public static final String HOLDINGS_CALLNUMBER_PREFIX_DISPLAY = "HoldingsCallNumberPrefix_display";
    public static final String HOLDINGS_SHELVING_SCHEME_CODE_DISPLAY = "HoldingsShelvingSchemeCode_display";
    public static final String HOLDINGS_SHELVING_SCHEME_VALUE_DISPLAY = "HoldingsShelvingSchemeValue_display";
    public static final String HOLDINGS_LOCATION_DISPLAY = "HoldingsLocation_display";
    public static final String HOLDINGS_COPYNUMBER_SEARCH = "HoldingsCopyNumber_search";
    public static final String HOLDINGS_COPYNUMBER_DISPLAY = "HoldingsCopyNumber_display";
    public static final String MISSING_PIECE_COUNT_DISPLAY = "MissingPieceCount_display";
    public static final String MISSING_PIECE_COUNT_SEARCH = "MissingPieceCount_search";
    public static final String NUMBER_OF_PIECES_SEARCH = "NumberOfPieces_search";
    public static final String NUMBER_OF_PIECES_DISPLAY = "NumberOfPieces_display";
    public static final String NUMBER_OF_RENEW = "NumberOfRenew_display";
    public static final String GREGORIAN_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DAT_FORMAT_EFFECTIVE = "MM/dd/yyyy hh:mm:ssa";
    public static final String DATE_FORMAT_EFFECTIVE = "MM/dd/yyyy hh:mm:ss a";
    public static final String DAT_FORMAT_EFFECTIVE_NOTICE = "MM/dd/yyyy HH:mm:ss";
    public static final String SOLR_DOC_DATE_FORMAT="E MMM dd HH:mm:ss Z yyyy";
    public static final String DESCRIBE_EFFECTIVE_DATE = "00:00:00";
    public static final String BIB_IDENTIFIER = "bibIdentifier";
    public static final String HOLDINGS_IDENTIFIER = "holdingsIdentifier";
    public static final String ITEM_IDENTIFIER = "itemIdentifier";

    public static final String COPY_NUMBER_SEARCH = "CopyNumber_search";

    public static final String GOKB_IDENTIFIER = "gokbIdentifier";
    public static final String URI_SEARCH = "Uri_search";
    public static final String RECEIPT_STATUS_SEARCH = "ReceiptStatus_search";
    public static final String HOLDING_NOTE_SEARCH = "HoldingsNote_search";
    public static final String ITEM_PART_SEARCH = "ItemPart_search";

    public static final String URI_DISPLAY = "Uri_display";
    public static final String RECEIPT_STATUS_DISPLAY = "ReceiptStatus_display";
    public static final String HOLDING_NOTE_DISPLAY = "HoldingsNote_display";
    public static final String LOCATION_LEVEL_DISPLAY = "Location_display";
    public static final String LOCATION_LEVEL_NAME_DISPLAY = "LocationLevelName_display";
    public static final String ITEM_PART_DISPLAY = "ItemPart_display";

    public static final String CALL_NUMBER_TYPE_SEARCH = "CallNumberType_search";
    public static final String CALL_NUMBER_SORT = "CallNumber_sort";
    public static final String CALL_NUMBER_SEARCH = "CallNumber_search";
    public static final String CALL_NUMBER_PREFIX_SEARCH = "CallNumberPrefix_search";
    public static final String CLASSIFICATION_PART_SEARCH = "ClassificationPart_search";
    public static final String SHELVING_SCHEME_VALUE_SEARCH = "ShelvingSchemeValue_search";
    public static final String SHELVING_SCHEME_CODE_SEARCH = "ShelvingSchemeCode_search";
    public static final String SHELVING_ORDER_SEARCH = "ShelvingOrder_search";

    public static final String CALL_NUMBER_TYPE_DISPLAY = "CallNumberType_display";
    public static final String CALL_NUMBER_DISPLAY = "CallNumber_display";
    public static final String CALL_NUMBER_PREFIX_DISPLAY = "CallNumberPrefix_display";
    public static final String CLASSIFICATION_PART_DISPLAY = "ClassificationPart_display";
    public static final String SHELVING_SCHEME_VALUE_DISPLAY = "ShelvingSchemeValue_display";
    public static final String SHELVING_SCHEME_CODE_DISPLAY = "ShelvingSchemeCode_display";
    public static final String SHELVING_ORDER_DISPLAY = "ShelvingOrder_display";

    public static final String LOCATION_LEVEL_SEARCH = "Location_search";
    public static final String LOCATION_LEVEL_NAME_SEARCH = "LocationLevelName_search";
    //Sort fields
    public static final String SHELVING_ORDER_SORT = "ShelvingOrder_sort";
    public static final String COPY_NUMBER_LABEL_SEARCH = "CopyNumberLabel_search";
    public static final String COPY_NUMBER_DISPLAY = "CopyNumber_display";
    public static final String COPY_NUMBER_LABEL_DISPLAY = "CopyNumberLabel_display";
    public static final String LOCATION_LEVEL_SORT = "Location_sort";
    public static final String INSTANCE_IDENTIFIER = "instanceIdentifier";


    public static final String ACCESS_STATUS_SEARCH = "AccessStatus_search";
    public static final String ACCESS_STATUS_DISPLAY = "AccessStatus_display";

    public static final String IMPRINT_DISPLAY = "Imprint_display";
    public static final String IMPRINT_SEARCH = "Imprint_search";

    public static final String PLATFORM_DISPLAY = "Platform_display";
    public static final String PLATFORM_SEARCH = "Platform_search";
    public static final String SUBSCRIPTION_DISPLAY = "Subscription_display";
    public static final String SUBSCRIPTION_SEARCH = "Subscription_search";
    public static final String SUBSCRIPTION_STATUS_DISPLAY= "SubscriptionStatus_display";
    public static final String CALL_NUMBER_TYPE_NAME_SEARCH = "CallNumberTypeName_search";
    public static final String URL_SEARCH = "URL_search";
    public static final String PROXIED_SEARCH = "Proxied_search";
    public static final String AUTHENTICATION_SEARCH = "Authentication_search";
    public static final String NUMBER_OF_SIMULTANEOUS_USERS_SEARCH = "NumberOfSimultaneousUses_search";
    public static final String ACCESS_LOCATION_SEARCH = "AccessLocation_search";

    public static final String E_PUBLISHER_DISPLAY = "E_Publisher_display";
    public static final String E_PUBLISHER_SEARCH = "E_Publisher_search";
    public static final String URL_DISPLAY = "Url_display";
    public static final String PUBLIC_NOTE_DISPLAY = "Public_Note_display";
    public static final String ERESOURCE_NAME_DISPLAY = "EResource_name_display";
    public static final String E_INSTANCE_COVERAGE_DATE = "CoverageDate_display";

    public static final String STATISTICAL_SEARCHING_CODE_VALUE_SEARCH = "StatisticalSearchingCodeValue_search";
    public static final String STATISTICAL_SEARCHING_CODE_VALUE_DISPLAY = "StatisticalSearchingCodeValue_display";
    public static final String EXTENT_OF_OWNERSHIP_NOTE_VALUE_DISPLAY = "ExtentOfOwnership_Note_Value_display";
    public static final String EXTENT_OF_OWNERSHIP_NOTE_TYPE_DISPLAY = "ExtentOfOwnership_Note_Type_display";
    public static final String EXTENT_OF_OWNERSHIP_TYPE_DISPLAY = "ExtentOfOwnership_Type_display";
    public static final String PROXIED_DISPLAY = "Proxied_display";
    public static final String ADMIN_URL_DISPLAY = "Admin_url_display";
    public static final String ADMIN_URL_SEARCH = "Admin_url_search";

    public static final String AUTHENTICATION_DISPLAY = "Authentication_display";
    public static final String NUMBER_OF_SIMULTANEOUS_USERS_DISPLAY = "NumberOfSimultaneousUses_display";
    public static final String ACCESS_LOCATION_DISPLAY = "AccessLocation_display";
    public static final String E_INSTANCE_PERPETUAL_ACCESS_DATE = "PerpetualAccess_display";
    public static final String DONOR_CODE_SEARCH = "DonorCode_search";
    public static final String DONOR_CODE_DISPLAY = "DonorCode_display";
    public static final String DONOR_PUBLIC_DISPLAY = "DonorPublic_display";
    public static final String DONOR_PUBLIC_SEARCH = "DonorPublic_search";
    public static final String DONOR_NOTE_DISPLAY = "DonorNote_display";
    public static final String DONOR_NOTE_SEARCH = "DonorNote_search";

    public static final String ADMIN_USERNAME_DISPLAY = "Admin_UserName_display";
    public static final String ADMIN_USERNAME_SEARCH = "Admin_UserName_search";
    public static final String ADMIN_PASSWORD_DISPLAY = "Admin_Password_display";
    public static final String ADMIN_PASSWORD_SEARCH = "Admin_Password_search";
    public static final String LINK_TEXT_SEARCH = "Link_Text_search";
    public static final String LINK_TEXT_DISPLAY = "Link_Text_display";
    public static final String ACCESS_USERNAME_DISPLAY = "Access_UserName_display";
    public static final String ACCESS_USERNAME_SEARCH = "Access_UserName_search";
    public static final String ACCESS_PASSWORD_DISPLAY = "Access_Password_display";
    public static final String ACCESS_PASSWORD_SEARCH = "Access_Password_search";
    public static final String PERSIST_LINK_DISPLAY = "Persist_Link_display";
    public static final String PERSIST_LINK_SEARCH = "Persist_Link_search";
    public static final String ILL_DISPLAY = "ILL_display";
    public static final String ILL_SEARCH = "ILL_search";
    public static final String IS_BOUND_WITH = "isBoundwith";

    public static final String APPL_ID_OLE = "OLE";
    public static final String DESC_NMSPC = "OLE-DESC";
    public static final String DESCRIBE_COMPONENT = "Describe";
    public static final String DEFAULT_ITEM_TYPE_CODE = "DEFAULT_ITEM_TYPE_CODE";

    public static final String TRANSFER_SUCCESS_MESSAGE = "Document transferred successfully";
    public static final String TRANSFER_BOUND_WITH_ERROR_MESSAGE = "Holdings can not be transfered. Holdings is bound-with more than one bib";

    public static final SimpleDateFormat DOCSTORE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    public static final String FORWARD_SLASH = "/";

    public static final String TRANSFER_HOLDINGS_ANALYTIC_ERROR_MESSAGE = "Holdings can not be transfered. Holdings is in analytic relation.";
    public static final String TRANSFER_HOLDINGS_ITEM_ANALYTIC_ERROR_MESSAGE = "Holdings can not be transfered. Item attached with Holdings is in analytic relation.";
    public static final String TRANSFER_ITEM_ANALYTIC_ERROR_MESSAGE = "Item can not be transfered. Item is in analytic relation.";

}
