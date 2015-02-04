package org.kuali.ole.docstore.engine.service.search;

import org.kuali.ole.docstore.common.search.BrowseParams;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/13/13
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocstoreSearchService {

    public static final String BIBLIOGRAPHIC = "bibliographic";
    public static final String INSTANCE = "instance";
    public static final String HOLDINGS = "holdings";
    public static final String EHOLDINGS="eHoldings";
    public static final String ITEM = "item";
    public static final String BIB_LINK_FIELDS = "bibIdentifier,holdingsIdentifier";
    public static final String HOLDINGS_LINK_FIELDS = "bibIdentifier,itemIdentifier,holdingsIdentifier,linkedBibCount";
    public static final String EHOLDINGS_LINK_FIELDS ="bibIdentifier,holdingsIdentifier";
    public static final String ITEM_LINK_FIELDS = "bibIdentifier,holdingsIdentifier,itemIdentifier";
    public static final String BIB_FIELDS = "id,Author_display,Title_sort,Title_display,PublicationDate_display,Publisher_display,ISBN_display,ISSN_display,Edition_display,Description_display,Format_display,Language_display,DocFormat,LocalId_display,DocCategory,DocType," + BIB_LINK_FIELDS;
    public static final String HOLDINGS_FIELDS = "Location_display,Title_display,CallNumber_display,CallNumberPrefix_display,CallNumberType_display,CopyNumber_display,bibIdentifier,itemIdentifier,LocalId_display,Location_display,DocFormat," + HOLDINGS_LINK_FIELDS;
    public static final String EHOLDINGS_FIELDS = "LocalId_display,Location_display,Platform_display,ShelvingSchemeCode_display,Url_display,AccessStatus_display,DocFormat,StatisticalSearchingCodeValue_display," + EHOLDINGS_LINK_FIELDS;
    public static final String ITEM_FIELDS = "ShelvingOrder_display,Title_display,ItemBarcode_display,Enumeration_display,Chronology_display,ItemType_display,VolumeNumber_display,ItemStatus_display," + ITEM_LINK_FIELDS;

    public SearchResponse search(SearchParams searchParams);

    public List<String> callNumberBrowse(BrowseParams browseParams);

    public String findBib(Map<String, String> map);

    public String findHoldings(Map<String, String> map);

    public String findItem(Map<String, String> map);

    public String findHoldingsTree(Map<String, String> map);

    public String findBibTree(Map<String, String> map);



}
