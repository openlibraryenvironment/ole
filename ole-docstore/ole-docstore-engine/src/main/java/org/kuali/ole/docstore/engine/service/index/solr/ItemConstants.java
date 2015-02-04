package org.kuali.ole.docstore.engine.service.index.solr;

/**
 * Created with IntelliJ IDEA.
 * User: sundarr
 * Date: 12/18/13
 * Time: 4:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ItemConstants {

     /*
        *Instance Common fields
     */

    public static final String FORMER_RESOURCE_IDENTIFIER_SOURCE_SEARCH = "Source_search";
    public static final String FORMER_RESOURCE_IDENTIFIER_SOURCE_DISPLAY = "Source_display";
    public static final String BIB_IDENTIFIER = "bibIdentifier";
    public static final String ITEM_IDENTIFIER = "itemIdentifier";
    public static final String HOLDINGS_IDENTIFIER = "holdingsIdentifier";
    public static final String INSTANCE_IDENTIFIER = "instanceIdentifier";


    /**
     * Item & Holdings Common Fields.
     */

    public static final String CALL_NUMBER_TYPE_SEARCH = "CallNumberType_search";
    public static final String CALL_NUMBER_SEARCH = "CallNumber_search";
    public static final String CALL_NUMBER_PREFIX_SEARCH = "CallNumberPrefix_search";
    public static final String CLASSIFICATION_PART_SEARCH = "ClassificationPart_search";
    public static final String SHELVING_SCHEME_VALUE_SEARCH = "ShelvingSchemeValue_search";
    public static final String SHELVING_SCHEME_CODE_SEARCH = "ShelvingSchemeCode_search";
    public static final String SHELVING_ORDER_CODE_SEARCH = "ShelvingOrderCode_search";
    public static final String SHELVING_ORDER_SEARCH = "ShelvingOrder_search";
    public static final String SHELVING_ORDER_VALUE_SEARCH = "ShelvingOrder_search";

    public static final String CALL_NUMBER_TYPE_DISPLAY = "CallNumberType_display";
    public static final String CALL_NUMBER_DISPLAY = "CallNumber_display";
    public static final String CALL_NUMBER_PREFIX_DISPLAY = "CallNumberPrefix_display";
    public static final String CLASSIFICATION_PART_DISPLAY = "ClassificationPart_display";
    public static final String SHELVING_SCHEME_VALUE_DISPLAY = "ShelvingSchemeValue_display";
    public static final String SHELVING_SCHEME_CODE_DISPLAY = "ShelvingSchemeCode_display";
    public static final String SHELVING_ORDER_CODE_DISPLAY = "ShelvingOrderCode_display";
    public static final String SHELVING_ORDER_DISPLAY = "ShelvingOrder_display";
    public static final String SHELVING_ORDER_VALUE_DISPLAY = "ShelvingOrder_display";
    public static final String LOCATION_LEVEL_SEARCH = "Location_search";
    public static final String LOCATION_LEVEL_NAME_SEARCH = "LocationLevelName_search";
    public static final String DONOR_CODE_SEARCH = "DonorCode_search";

    //Sort fields
    public static final String SHELVING_ORDER_SORT = "ShelvingOrder_sort";
    public static final String CALLNUMBER_SORT = "CallNumber_sort";
    public static final String CALLNUMBER_PREFIX_SORT = "CallNumberPrefix_sort";
    public static final String ENUMERATION_SORT = "Enumeration_sort";
    public static final String CHRONOLOGY_SORT = "Chronology_sort";
    public static final String COPYNUMBER_SORT = "CopyNumber_sort";
    public static final String ITEM_BARCODE_SORT = "ItemBarcode_sort";

    /*
    *Holdings common fields
     */

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
    public static final String STATISTICAL_SEARCHING_CODE_VALUE_SEARCH = "StatisticalSearchingCodeValue_search";
    public static final String STATISTICAL_SEARCHING_FULL_VALUE_SEARCH = "StatisticalSearchingFullValue_search";
    public static final String ITEM_TYPE_FULL_VALUE_SEARCH = "ItemTypeFullValue_search";
    public static final String ITEM_TYPE_CODE_VALUE_SEARCH = "ItemTypeCodeValue_search";
    public static final String COPY_NUMBER_SEARCH = "CopyNumber_search";
    public static final String COPY_NUMBER_LABEL_SEARCH = "CopyNumberLabel_search";
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
    public static final String STATISTICAL_SEARCHING_CODE_VALUE_DISPLAY = "StatisticalSearchingCodeValue_display";
    public static final String STATISTICAL_SEARCHING_FULL_VALUE_DISPLAY = "StatisticalSearchingFullValue_display";
    public static final String ITEM_TYPE_FULL_VALUE_DISPLAY = "ItemTypeFullValue_display";
    public static final String ITEM_TYPE_CODE_VALUE_DISPLAY = "ItemTypeCodeValue_display";
    public static final String COPY_NUMBER_DISPLAY = "CopyNumber_display";
    public static final String COPY_NUMBER_LABEL_DISPLAY = "CopyNumberLabel_display";
    public static final String VOLUME_NUMBER_DISPLAY = "VolumeNumber_display";
    public static final String VOLUME_NUMBER_LABEL_DISPLAY = "VolumeNumberLabel_display";
    public static final String ENUMERATION_DISPLAY = "Enumeration_display";
    public static final String CHRONOLOGY_DISPLAY = "Chronology_display";
    public static final String ITEM_STATUS_DISPLAY = "ItemStatus_display";
    public static final String ITEM_STATUS_SEARCH = "ItemStatus_search";
    public static final String LOCATION_LEVEL_SORT = "Location_sort";
    public static final String DONOR_CODE_DISPLAY = "DonorCode_display";
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
    public static final String DONOR_PUBLIC_DISPLAY = "DonorPublic_display";
    public static final String DONOR_NOTE_DISPLAY = "DonorNote_display";
    public static final String HIGHDENSITYSTORAGE_ROW_DISPLAY = "HighDensityStorage_Row_display";
    public static final String HIGHDENSITYSTORAGE_MODULE_DISPLAY = "HighDensityStorage_Module_display";
    public static final String HIGHDENSITYSTORAGE_SHELF_DISPLAY = "HighDensityStorage_Shelf_display";
    public static final String HIGHDENSITYSTORAGE_TRAY_DISPLAY = "HighDensityStorage_Tray_display";
    public static final String ITEMNOTE_VALUE_DISPLAY = "ItemNote_Value_display";
    public static final String ITEMNOTE_TYPE_DISPLAY = "ItemNote_Type_display";
    public static final String HOLDINGS_CALLNUMBER_SEARCH = "HoldingsCallNumber_search";
    public static final String HOLDINGS_LOCATION_SEARCH = "HoldingsLocation_search";
    public static final String HOLDINGS_CALLNUMBER_DISPLAY = "HoldingsCallNumber_display";
    public static final String HOLDINGS_LOCATION_DISPLAY = "HoldingsLocation_display";
    public static final String HOLDINGS_COPYNUMBER_SEARCH = "HoldingsCopyNumber_search";
    public static final String HOLDINGS_COPYNUMBER_DISPLAY = "HoldingsCopyNumber_display";
    public static final String MISSING_PIECE_COUNT_DISPLAY = "MissingPieceCount_display";
    public static final String MISSING_PIECE_COUNT_SEARCH = "MissingPieceCount_search";
    public static final String NUMBER_OF_PIECES_SEARCH = "NumberOfPieces_search";
    public static final String NUMBER_OF_PIECES_DISPLAY = "NumberOfPieces_display";
    public static final String NUMBER_OF_RENEW = "NumberOfRenew_display";


}
