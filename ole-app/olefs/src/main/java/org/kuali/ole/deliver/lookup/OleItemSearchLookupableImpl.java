package org.kuali.ole.deliver.lookup;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleLookupableImpl;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.Location;
import org.kuali.ole.docstore.common.document.content.instance.LocationLevel;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.*;

/**
 * OlePatronLookupableImpl makes validation  and populate the search criteria and return the search results
 */
public class OleItemSearchLookupableImpl extends OleLookupableImpl {


    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleItemSearchLookupableImpl.class);
    private DocstoreClientLocator docstoreClientLocator;
    private DocstoreUtil docstoreUtil;
    private int totalRecCount;
    private int start;
    private int pageSize;

    public DocstoreUtil getDocstoreUtil() {
        if (docstoreUtil == null) {
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);
        }
        return docstoreUtil;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public boolean getPreviousFlag() {
        if (this.start == 0)
            return false;
        return true;
    }

    public boolean getNextFlag() {
        if (this.start + this.pageSize < this.totalRecCount)
            return true;
        return false;
    }

    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        LOG.debug("Inside performSearch()");
        boolean searchFlag = true;

        Iterator<Map.Entry<String, String>> iter = searchCriteria.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            if (entry.getKey().equalsIgnoreCase("title") || entry.getKey().equalsIgnoreCase("author") || entry.getKey().equalsIgnoreCase("publisher") || entry.getKey().equalsIgnoreCase("callNumber") || entry.getKey().equalsIgnoreCase("itemType") || entry.getKey().equalsIgnoreCase("itemBarCode")) {
                if (!entry.getValue().trim().equals("")) {
                    searchFlag = false;
                    break;
                }
            }
        }

        if (searchFlag) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ITM_BLANK_SEARCH_ERROR_MSG);
            return new ArrayList<Object>();
        }
        String title = searchCriteria.get("title") != null ? searchCriteria.get("title") : "";
        String author = searchCriteria.get("author") != null ? searchCriteria.get("author") : "";
        String publisher = searchCriteria.get("publisher") != null ? searchCriteria.get("publisher") : "";
        String callNumber = searchCriteria.get("callNumber") != null ? searchCriteria.get("callNumber") : "";
        String itemType = searchCriteria.get("itemType") != null ? searchCriteria.get("itemType") : "";
        String itemBarCode = searchCriteria.get("itemBarCode") != null ? searchCriteria.get("itemBarCode") : "";
        String itemUUID = searchCriteria.get("itemUUID") != null ? searchCriteria.get("itemUUID") : "";
        String pageDisplay = searchCriteria.get("pageDisplay") != null ? searchCriteria.get("pageDisplay") : "";
        String page_Size = searchCriteria.get("pageSize") != null ? searchCriteria.get("pageSize") : "";

        String startIndex = searchCriteria.get("startIndex") != null ? searchCriteria.get("startIndex") : "";
        List<OleItemSearch> oleItemSearches = new ArrayList<>();
        //this.pageSize = Integer.parseInt(getParameter(OLEParameterConstants.ITM_SEARCH_DOCSTORE_ROW_SIZE_VALUE));
        this.pageSize = Integer.parseInt(page_Size);


        if (!pageDisplay.isEmpty() && pageDisplay.equalsIgnoreCase("search")) {
            this.start = 0;
            startIndex = "0";


        }
        if (!pageDisplay.isEmpty() && pageDisplay.equalsIgnoreCase("next")) {

            if (!startIndex.isEmpty()) {
                this.start = Integer.parseInt(startIndex);
                int nextIndex = Math.max(0, this.start + this.pageSize);
                this.start = nextIndex;
                startIndex = String.valueOf(nextIndex);
            }

        }
        if (!pageDisplay.isEmpty() && pageDisplay.equalsIgnoreCase("previous")) {
            if (!startIndex.isEmpty()) {
                this.start = Integer.parseInt(startIndex);
                int previousIndex = Math.max(0, this.start - this.pageSize);
                this.start = previousIndex;
                startIndex = String.valueOf(previousIndex);
            }

        }

        try {
            SearchParams item_search_Params = new SearchParams();
            if (((!title.isEmpty() || !author.isEmpty() || !publisher.isEmpty()) || (!itemBarCode.isEmpty() || !callNumber.isEmpty() || !itemType.isEmpty()))) {
                item_search_Params.setStartIndex(this.start);
                item_search_Params.setPageSize(Integer.parseInt(page_Size));
                if (!callNumber.isEmpty()) {
                    item_search_Params.getSearchConditions().add(item_search_Params.buildSearchCondition("AND", item_search_Params.buildSearchField(DocType.ITEM.getCode(), "HoldingsCallNumber_display", callNumber), "AND"));
                    item_search_Params.getSearchConditions().add(item_search_Params.buildSearchCondition("AND", item_search_Params.buildSearchField(DocType.ITEM.getCode(), "CallNumber_display", callNumber), "OR"));
                }
                if (!title.isEmpty()) {
                    item_search_Params.getSearchConditions().add(item_search_Params.buildSearchCondition("AND", item_search_Params.buildSearchField(DocType.ITEM.getCode(), Bib.TITLE, title), "AND"));
                }
                if (!author.isEmpty()) {
                    item_search_Params.getSearchConditions().add(item_search_Params.buildSearchCondition("AND", item_search_Params.buildSearchField(DocType.ITEM.getCode(), Bib.AUTHOR, author), "AND"));
                }
                if (!publisher.isEmpty()) {
                    item_search_Params.getSearchConditions().add(item_search_Params.buildSearchCondition("AND", item_search_Params.buildSearchField(DocType.ITEM.getCode(), Bib.PUBLISHER, publisher), "AND"));
                }
                if (!itemType.isEmpty()) {
                    item_search_Params.getSearchConditions().add(item_search_Params.buildSearchCondition("AND", item_search_Params.buildSearchField(DocType.ITEM.getCode(), "TemporaryItemTypeCodeValue_search", itemType), "AND"));
                    item_search_Params.getSearchConditions().add(item_search_Params.buildSearchCondition("AND", item_search_Params.buildSearchField(DocType.ITEM.getCode(), "ItemTypeCodeValue_search", itemType), "OR"));
                }
                if (!itemBarCode.isEmpty()) {
                    item_search_Params.getSearchConditions().add(item_search_Params.buildSearchCondition("phrase", item_search_Params.buildSearchField(DocType.ITEM.getCode(), Item.ITEM_BARCODE, itemBarCode), "AND"));
                }
                if (!itemUUID.isEmpty()) {
                    item_search_Params.getSearchConditions().add(item_search_Params.buildSearchCondition("AND", item_search_Params.buildSearchField(DocType.ITEM.getCode(), Item.ID, itemUUID), "AND"));
                }
                getDocstoreUtil().getSearchResultFields(item_search_Params);
                SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(item_search_Params);
                this.totalRecCount = searchResponse.getTotalRecordCount();
                form.getLookupCriteria().put("totalRec", getPageShowEntries());
                form.getLookupCriteria().put("nextFlag", Boolean.toString(getNextFlag()));
                form.getLookupCriteria().put("previousFlag", Boolean.toString(getPreviousFlag()));
                form.getLookupCriteria().put("startIndex", startIndex);
                oleItemSearches = getDocstoreUtil().getSearchResults(searchResponse);
                //this.totalRecCount = oleItemSearches.size();
                //form.getLookupCriteria().put("pageSize",getParameter(OLEParameterConstants.ITM_SEARCH_DOCSTORE_ROW_SIZE_VALUE));

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (oleItemSearches.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }

        return oleItemSearches;
    }

    public String getPageShowEntries() {
        return "Showing " + ((this.start == 0) ? 1 : this.start + 1) + " to "
                + (((this.start + this.pageSize) > this.totalRecCount) ? this.totalRecCount : (this.start + this.pageSize))
                + " of " + this.totalRecCount + " entries";
    }


    private String getShelvingLocation(Location oleLocation) {
        if (oleLocation == null) {
            return "";
        }
        LocationLevel locationLevel =
                oleLocation.getLocationLevel();
        while (locationLevel.getLocationLevel() != null && !locationLevel.getLevel().equalsIgnoreCase(OLEConstants.OleDeliverRequest.SHELVING)) {
            locationLevel = locationLevel.getLocationLevel();
        }
        return locationLevel.getName();
    }

    public String getHoldingLinkURL(String holdingUuid, String bibUuid) {
        String url = "";
        try {
            url = "editorcontroller?viewId=EditorView&amp;methodToCall=load&amp;docCategory=work&amp;docType=holdings&amp;editable=false&amp;docFormat=oleml&amp;docId=" + holdingUuid + "&amp;bibId=" + bibUuid;
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return url;
    }

    public String getParameter(String name) {
        String parameter = "";
        try {
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put("namespaceCode", OLEConstants.DLVR_NMSPC);
            criteriaMap.put("componentCode", OLEConstants.DLVR_CMPNT);
            criteriaMap.put("name", name);
            List<ParameterBo> parametersList = (List<ParameterBo>) KRADServiceLocator.getBusinessObjectService().findMatching(ParameterBo.class, criteriaMap);
            for (ParameterBo parameterBo : parametersList) {
                parameter = parameterBo.getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return parameter;
    }


    @Override
    public Map<String, String> performClear(LookupForm form, Map<String, String> searchCriteria) {
        super.performClear(form, searchCriteria);
        Map<String, String> clearedSearchCriteria = new HashMap<String, String>();
        for (Map.Entry<String, String> searchKeyValue : searchCriteria.entrySet()) {
            String searchPropertyName = searchKeyValue.getKey();
            if (searchPropertyName.equals("pageDisplay")) {
                clearedSearchCriteria.put(searchPropertyName, searchKeyValue.getValue());
            }

            if (searchPropertyName.equals("totalRec")) {
                clearedSearchCriteria.put(searchPropertyName, searchKeyValue.getValue());
            }

            if (searchPropertyName.equals("nextFlag")) {
                clearedSearchCriteria.put(searchPropertyName, searchKeyValue.getValue());
            }

            if (searchPropertyName.equals("previousFlag")) {
                clearedSearchCriteria.put(searchPropertyName, searchKeyValue.getValue());
            }

            if (searchPropertyName.equals("startIndex")) {
                clearedSearchCriteria.put(searchPropertyName, searchKeyValue.getValue());
            }
            if (searchPropertyName.equals("pageSize")) {
                clearedSearchCriteria.put(searchPropertyName, searchKeyValue.getValue());
            }
        }
        return clearedSearchCriteria;
    }


}

