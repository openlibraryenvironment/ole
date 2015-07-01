package org.kuali.ole.deliver.util;

import org.kuali.ole.docstore.common.search.SearchParams;

/**
 * Created by pvsubrah on 5/18/15.
 */
public class SearchParmsBuilder {

    public static void buildSearchParams(SearchParams searchParams){
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemBarcode_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "id"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "bibIdentifier"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Title_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Author_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "LocalId_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "holdingsIdentifier"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ClaimsReturnedFlag_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "claimsReturnedFlagCreateDate"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "claimsReturnedNote"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CallNumber_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "TemporaryItemTypeFullValue_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemTypeFullValue_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Enumeration_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Chronology_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemStatus_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemDamagedStatus_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "DamagedItemNote_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "MissingPieceFlagNote_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "MissingPieceFlag_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Location_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "HoldingsCopyNumber_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "HoldingsCallNumber_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "HoldingsLocation_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "MissingPieceCount_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "NumberOfPieces_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CallNumberPrefix_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CopyNumber_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "proxyBorrower"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "VolumeNumberLabel_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "dueDateTime"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "NumberOfRenew_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item" , "checkOutDateTime"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item","ClaimsReturnedFlag_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item","itemStatusEffectiveDate"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item","HoldingsCallNumberPrefix_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item","HoldingsCallNumberPrefix_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item","HoldingsShelvingSchemeCode_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item","HoldingsShelvingSchemeCode_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item","HoldingsShelvingSchemeValue_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item","HoldingsShelvingSchemeValue_display"));
    }

}
