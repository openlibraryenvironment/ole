package org.kuali.ole.describe.controller;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleWorkHoldingsDocument;
import org.kuali.ole.describe.bo.SearchResultDisplayFields;
import org.kuali.ole.describe.bo.SearchResultDisplayRow;
import org.kuali.ole.describe.form.*;
import org.kuali.ole.describe.form.GlobalEditForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.config.DocFieldConfig;
import org.kuali.ole.docstore.common.document.config.DocFormatConfig;
import org.kuali.ole.docstore.common.document.config.DocTypeConfig;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 2/21/14
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/globaleditController")
public class GlobalEditController extends OLESearchController {

    private static final Logger LOG = Logger.getLogger(GlobalEditController.class);
    private DocstoreClientLocator docstoreClientLocator;
    private String eResourceId;
    private int totalRecCount;
    private String tokenId;
    private int start;
    private int pageSize;

    DocumentSearchConfig documentSearchConfig = DocumentSearchConfig.getDocumentSearchConfig();

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        GlobalEditForm globalEditForm = new GlobalEditForm();
        globalEditForm.setDocType("holdings");
        return globalEditForm;
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        this.start = 0;
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        globalEditForm.getSearchConditions().clear();
        boolean viewGlobalEditDispMessageFlag=false;
        SearchCondition searchCondition = new SearchCondition();
        SearchField searchField = new SearchField();
        searchField.setFieldName("any");
        searchField.setDocType(globalEditForm.getDocType());
        searchCondition.setSearchField(searchField);
        searchCondition.setOperator("AND");
        globalEditForm.getSearchConditions().add(searchCondition);
        GlobalVariables.getMessageMap().getInfoMessages().clear();
        globalEditForm.setViewGlobalEditFlag(false);
        globalEditForm.setStart(0);

        if (globalEditForm.getSearchResultDisplayRowList() != null && globalEditForm.getSearchResultDisplayRowList().size() > 0) {
            globalEditForm.getSearchResultDisplayRowList().clear();
        }
        if (globalEditForm.getGlobalEditMap() != null && globalEditForm.getGlobalEditMap().size() > 0) {
            globalEditForm.getGlobalEditMap().clear();
        }
        if (globalEditForm.getDocType() == null) {
            globalEditForm.setDocType("bibliographic");
        }
        if (globalEditForm.getSearchType() != null && globalEditForm.getSearchType().equalsIgnoreCase("Import")) {
            globalEditForm.setSearchFlag(false);
        } else {
            globalEditForm.setSearchFlag(true);
            globalEditForm.setSearchType("search");
        }
        if (StringUtils.isEmpty(globalEditForm.getFieldType())) {
            globalEditForm.setFieldType("LocalId");
        }

        if (globalEditForm.getSearchParams() != null) {
            globalEditForm.getSearchParams().getSearchConditions().clear();
            globalEditForm.getSearchParams().getSearchResultFields().clear();
        }
        globalEditForm.setMatchedCount(0);
        globalEditForm.setUnMatchedRecords("");
        globalEditForm.setTotalRecords(0);
        globalEditForm.setUnMatchedCount(0);
        globalEditForm.setSelectedFileName("");
        globalEditForm.setViewGlobalEditDispMessageFlag(viewGlobalEditDispMessageFlag);
        globalEditForm.setGlobalEditRecords(null);
        globalEditForm.getGlobalEditMap().clear();
        globalEditForm.setSelectAll(false);
        List<Integer> pageSizes = documentSearchConfig.getPageSizes();
        if(!pageSizes.isEmpty() || pageSizes.size() > 0) {
            globalEditForm.setPageSize(pageSizes.get(0));
        }
        GlobalVariables.getMessageMap().clearErrorMessages();
        boolean hasPermission = canGloballyEdit(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasPermission) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
            return super.navigate(globalEditForm, result, request, response);
        }
        return navigate(globalEditForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=pageNumberSearch")
    public ModelAndView pageNumberSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        SearchParams searchParams = globalEditForm.getSearchParams();
        try {
            int start = Math.max(0,
                    (Integer.parseInt(globalEditForm.getPageNumber()) - 1)
                            * globalEditForm.getPageSize());
            List<SearchResultDisplayRow> searchResultDisplayRows = globalEditForm.getOriginalSearchResultDisplayRowList();
            int end = Integer.parseInt(globalEditForm.getPageNumber()) * globalEditForm.getPageSize();
            searchParams.setStartIndex(start);
            if (searchResultDisplayRows !=null && searchResultDisplayRows.size() >= start) {
                if (searchResultDisplayRows.size() > end) {
                    globalEditForm.setSearchResultDisplayRowList(searchResultDisplayRows.subList(globalEditForm.getStart(), end));
                } else {
                    globalEditForm.setSearchResultDisplayRowList(searchResultDisplayRows.subList(start, searchResultDisplayRows.size()));
                }

            }
            if(globalEditForm.getSearchConditions().size()>0){
                searchDocstoreData(globalEditForm, request);
            }else{
                loadPageSearch(globalEditForm,start);
            }
        } catch (NumberFormatException e) {
            LOG.warn("Invalid page number " + globalEditForm.getPageNumber(), e);
        }

        return navigate(globalEditForm, result, request, response);
    }



    @RequestMapping(params = "methodToCall=lastPageSearch")
    public ModelAndView lastPageSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        List<SearchResultDisplayRow> searchResultDisplayRows = globalEditForm.getOriginalSearchResultDisplayRowList();
        SearchParams searchParams = globalEditForm.getSearchParams();
        try {
            int totalcount = globalEditForm.getTotalRecordCount();
            int pageSize = globalEditForm.getPageSize();
            int totlaPages = totalcount/pageSize;
            int lastNumber= pageSize*totlaPages;
            int lastPage = totalcount - pageSize;
            globalEditForm.setPageNumber(Integer.toString(totlaPages));
            if(lastNumber < totalcount){
                lastPage = lastNumber;
                globalEditForm.setPageNumber(Integer.toString(totlaPages+1));
            }
            int start = Math.max(0, lastPage);
            searchParams.setStartIndex(start);
            if(searchResultDisplayRows!=null){
                globalEditForm.setSearchResultDisplayRowList(searchResultDisplayRows.subList(start, searchResultDisplayRows.size()));
            }
            if(globalEditForm.getSearchConditions().size()>0) {
                searchDocstoreData(globalEditForm, request);
            }else {
                loadPageSearch(globalEditForm,start);
            }

        } catch (NumberFormatException e) {
            LOG.warn("Invalid page number " + globalEditForm.getPageNumber(), e);
        }
        return navigate(globalEditForm, result, request, response);
    }


    @Override
    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        LOG.info("*** GlobalEditController - Inside Search Method ***");
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        boolean hasPermission = canGloballyEdit(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasPermission) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
            return super.navigate(globalEditForm, result, request, response);
        }
        if(globalEditForm.getSearchConditions().size()>0){
            searchDocstoreData(globalEditForm, request);
        } else {
            loadSearch(globalEditForm);
        }
        return super.navigate(globalEditForm, result, request, response);
    }

    private void loadSearch(GlobalEditForm globalEditForm){
        if(globalEditForm.getPageSize() <= globalEditForm.getOriginalSearchResultDisplayRowList().size()) {
            globalEditForm.setSearchResultDisplayRowList(globalEditForm.getOriginalSearchResultDisplayRowList().subList(0, globalEditForm.getPageSize()));
        } else {
            globalEditForm.setSearchResultDisplayRowList(globalEditForm.getOriginalSearchResultDisplayRowList());
        }
        globalEditForm.setPageNumber("1");
        globalEditForm.getSearchParams().setStartIndex(0);
    }

    private void loadPageSearch(GlobalEditForm globalEditForm,int start){
        int end = start+globalEditForm.getPageSize();
        if(end>globalEditForm.getOriginalSearchResultDisplayRowList().size()){
            end =globalEditForm.getOriginalSearchResultDisplayRowList().size();
        }
        globalEditForm.setSearchResultDisplayRowList(globalEditForm.getOriginalSearchResultDisplayRowList().subList(start,end));
    }

    @RequestMapping(params = "methodToCall=load")
    public ModelAndView load(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        boolean viewGlobalEditDispMessageFlag = false;
        boolean hasPermission = canGloballyEdit(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasPermission) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
            return super.navigate(globalEditForm, result, request, response);
        }
        GlobalVariables.getMessageMap().getInfoMessages().clear();
        setShowPageSizeEntries(globalEditForm);
        List<String> inputData = new ArrayList<>();
        String fileName = null;
        MultipartFile file = globalEditForm.getFile();
        if (file != null && (file.getContentType().equalsIgnoreCase("application/octet-stream") ||
                file.getContentType().equalsIgnoreCase("text/plain") ||
                file.getOriginalFilename().endsWith(".txt"))) {
            fileName = file.getOriginalFilename();
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            for (String line; (line = reader.readLine()) != null; ) {
               if(StringUtils.isNotEmpty(line))
                 inputData.add(line);
            }
        } else {
            //GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_GLOBAL_SEARCH_MESSAGE);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_FILE_NOT_FOUND);
            return navigate(globalEditForm, result, request, response);
        }

        List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
        if (inputData.size() > 0) {
            SearchParams searchParams = null;
            List<SearchCondition> searchConditions = null;
            String docType = globalEditForm.getDocType();
            List<List<String>> partition = Lists.partition(inputData, 100);
            for (Iterator<List<String>> iterator = partition.iterator(); iterator.hasNext(); ) {
                List<String> idLists = iterator.next();
                searchConditions = new ArrayList<>();
                searchParams = new SearchParams();
                for (String id : idLists) {
                    if (StringUtils.isNotEmpty(id)) {
                        if (globalEditForm.getFieldType().equalsIgnoreCase("Barcode")) {
                            if (DocType.HOLDINGS.getCode().equals(globalEditForm.getDocType())) {
                                searchConditions.add(searchParams.buildSearchCondition("NONE", searchParams.buildSearchField("item", "ItemBarcode_display", id), "OR"));
                            } else {
                                searchConditions.add(searchParams.buildSearchCondition("NONE", searchParams.buildSearchField(docType, "ItemBarcode_display", id), "OR"));
                            }
                        } else {
                            searchConditions.add(searchParams.buildSearchCondition("NONE", searchParams.buildSearchField(globalEditForm.getDocType(), "LocalId_display", id), "OR"));
                        }
                    }
                }
                if (globalEditForm.getFieldType() != null && globalEditForm.getFieldType().equalsIgnoreCase("Barcode") && DocType.HOLDINGS.getCode().equals(globalEditForm.getDocType())) {
                    searchParams.getSearchConditions().addAll(searchConditions);
                    searchResultDisplayRows.addAll(getSearchResults(searchParams, globalEditForm));
                    Set<String> holdingsIdList = new HashSet();
                    for (SearchResultDisplayRow searchResultDisplayRow : searchResultDisplayRows) {
                        holdingsIdList.add(searchResultDisplayRow.getHoldingsIdentifier());
                    }
                    for (String id : holdingsIdList) {
                        searchConditions.add(searchParams.buildSearchCondition("NONE", searchParams.buildSearchField(docType, "LocalId_display", id), "OR"));
                    }
                    searchParams.getSearchConditions().addAll(searchConditions);
                    searchResultDisplayRows.addAll(getSearchResults(searchParams, globalEditForm));
                }

                searchParams.getSearchConditions().addAll(searchConditions);
                searchResultDisplayRows.addAll(getSearchResults(searchParams, globalEditForm));
            }
            globalEditForm.setTotalRecordCount(searchResultDisplayRows.size());
        }
        List<String> listFromDB = new ArrayList<>();
        //List<String> matchedList = new ArrayList<>();
        List<String> unMatchedList = new ArrayList<>();
        if (inputData.size() > 0) {
            viewGlobalEditDispMessageFlag = true;
            if (searchResultDisplayRows.size() > 0) {
                for (SearchResultDisplayRow searchResultDisplayRow : searchResultDisplayRows) {
                    if ("Barcode".equals(globalEditForm.getFieldType())) {
                        listFromDB.add(searchResultDisplayRow.getBarcode());
                    } else {
                        listFromDB.add(searchResultDisplayRow.getLocalId());
                    }
                }
            }
        }

        for (int i = 0; i < inputData.size(); i++) {
            if (!listFromDB.contains(inputData.get(i))) {
                //matchedList.add(listFromDB.get(i));
                unMatchedList.add(inputData.get(i));
            }
        }

        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer1 =new StringBuffer();
        List<String> stringList = new ArrayList<>();
        if(unMatchedList.size()>0){
        for (int i = 0; i < unMatchedList.size(); i++) {
            stringList.add(unMatchedList.get(i));
            if (stringList.size() > 0 && stringList.size() <= 5) {
                stringBuffer.append(unMatchedList.get(i));
                stringBuffer.append(",");
            } else {
                stringBuffer1.append(".....");
            }

        }
            stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer = stringBuffer.append(stringBuffer1);
        globalEditForm.setTotalRecords(inputData.size());
        globalEditForm.setUnMatchedRecords(stringBuffer.toString());
        globalEditForm.setUnMatchedCount(inputData.size() - globalEditForm.getTotalRecordCount());
        globalEditForm.setMatchedCount(globalEditForm.getTotalRecordCount());
        globalEditForm.setSelectedFileName(fileName);

        globalEditForm.setViewGlobalEditDispMessageFlag(viewGlobalEditDispMessageFlag);
        globalEditForm.setOriginalSearchResultDisplayRowList(searchResultDisplayRows);
        if(searchResultDisplayRows.size() > globalEditForm.getStart()){
             if(searchResultDisplayRows.size() > globalEditForm.getPageSize()){
                globalEditForm.setSearchResultDisplayRowList(searchResultDisplayRows.subList(globalEditForm.getStart(),globalEditForm.getPageSize()));
            }else{
                 globalEditForm.setSearchResultDisplayRowList(searchResultDisplayRows.subList(globalEditForm.getStart(),searchResultDisplayRows.size()));
            }

        }
        if (searchResultDisplayRows.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_GLOBAL_SEARCH_MESSAGE);
        }

        return navigate(globalEditForm, result, request, response);
    }

    private List<SearchResultDisplayRow> getSearchResults(SearchParams searchParams, GlobalEditForm globalEditForm) throws Exception {
        List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
        searchParams.setStartIndex(this.start);
        searchParams.setPageSize(searchParams.getSearchConditions().size());
        for (SearchCondition searchCondition : searchParams.getSearchConditions()) {
            if (searchCondition.getSearchField() == null) {
                searchCondition.setSearchField(new SearchField());
            }
            //added comment for Global edit change for searching barcode through hodlings.
            if (!"holdings".equals(globalEditForm.getDocType())) {
                searchCondition.getSearchField().setDocType(globalEditForm.getDocType());
            }
        }
        if (!globalEditForm.isMoreFacets()) {
            searchParams.getFacetFields().clear();
            Set<String> facetFields = getFacetFields(globalEditForm.getDocType());
            searchParams.getFacetFields().addAll(facetFields);
            searchParams.setFacetLimit(documentSearchConfig.getFacetPageSizeShort());
        }
        globalEditForm.setFacetLimit(documentSearchConfig.getFacetPageSizeShort() - 1);
        SearchResponse searchResponse = null;
        globalEditForm.setSearchResultDisplayFields(getDisplayFields(globalEditForm));
        searchParams.buildSearchParams(searchParams, globalEditForm.getDocType());
        //added comment for Global edit change for searching barcode through hodlings
        if ("holdings".equals(globalEditForm.getDocType())) {
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemBarcode_display"));
        }

        try {
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }
        globalEditForm.setSearchResultDisplayFields(getDisplayFields(globalEditForm));
        globalEditForm.setSearchResponse(searchResponse);
        for (SearchResult searchResult : searchResponse.getSearchResults()) {
            SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
            if (org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode().equalsIgnoreCase(globalEditForm.getDocType())) {
                searchResultDisplayRow.buildBibSearchResultField(searchResult.getSearchResultFields(), eResourceId);
            } else if (org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode().equals(globalEditForm.getDocType())) {
                searchResultDisplayRow.buildHoldingSearchResultField(searchResult.getSearchResultFields());
            } else if (org.kuali.ole.docstore.common.document.content.enums.DocType.EHOLDINGS.getCode().equals(globalEditForm.getDocType())) {
                searchResultDisplayRow.buildEHoldingSearchResultField(searchResult.getSearchResultFields());
            } else if (org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode().equals(globalEditForm.getDocType())) {
                searchResultDisplayRow.buildItemSearchResultField(searchResult.getSearchResultFields());
            }
            searchResultDisplayRows.add(searchResultDisplayRow);
        }
        globalEditForm.setSearchResultDisplayRowList(searchResultDisplayRows);
        if (searchResponse != null && searchResponse.getFacetResult() != null) {
            globalEditForm.setFacetResultFields(searchResponse.getFacetResult().getFacetResultFields());
        }
        if (searchResultDisplayRows.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
        }
        setPageNextPreviousAndEntriesInfo(globalEditForm);
        if (searchResponse != null && searchResponse.getFacetResult() != null) {
            globalEditForm.setFacetResultFields(searchResponse.getFacetResult().getFacetResultFields());
        }
        return globalEditForm.getSearchResultDisplayRowList();
    }


    @RequestMapping(params = "methodToCall=viewGlobalEditRecords")
    public ModelAndView viewGlobalEditRecords(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        GlobalVariables.getMessageMap().getInfoMessages().clear();
        if (globalEditForm.getGlobalEditRecords().size() == 0) {
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, OLEConstants.GLOBAL_EDIT_VIEW_RECORDS_MESSAGE);
        } else {
            globalEditForm.setViewGlobalEditFlag(true);
            for (SearchResultDisplayRow searchResultDisplayRow : globalEditForm.getGlobalEditRecords()) {
                searchResultDisplayRow.setSelect(false);
            }
            globalEditForm.setTotalRecordCount(globalEditForm.getGlobalEditRecords().size());
            globalEditForm.setPageSize(globalEditForm.getGlobalEditRecords().size());
            globalEditForm.setPageNumber("1");
            globalEditForm.getSearchParams().setStartIndex(0);
        }

        return navigate(globalEditForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=addGlobalEditRecords")
    public ModelAndView addGlobalEditRecords(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        GlobalVariables.getMessageMap().getInfoMessages().clear();
        if (globalEditForm.getSearchResultDisplayRowList() == null || globalEditForm.getSearchResultDisplayRowList().size() == 0) {
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, OLEConstants.DESCRIBE_GLOBAL_SEARCH_MESSAGE);
            return navigate(globalEditForm, result, request, response);
        }
        boolean selectFlag = false;
        List<SearchResultDisplayRow> searchResultDisplayRows = globalEditForm.getGlobalEditRecords();
        Map<String, SearchResultDisplayRow> searchResultDisplayRowMap = globalEditForm.getGlobalEditMap();
        int pageSize = globalEditForm.getPageSize();
        if (globalEditForm.isSelectAll()) {
            globalEditForm.setPageSize(globalEditForm.getTotalRecords());
            searchDocstoreData(globalEditForm, request);
            globalEditForm.setPageSize(pageSize);
            super.setPageNextPreviousAndEntriesInfo(globalEditForm);
        }
        for (SearchResultDisplayRow searchResultDisplayRow : globalEditForm.getSearchResultDisplayRowList()) {
            if (globalEditForm.isSelectAll() || searchResultDisplayRow.isSelect()) {
                if ((globalEditForm.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode())
                        || globalEditForm.getDocType().equalsIgnoreCase(DocType.EHOLDINGS.getCode())) &&
                        !searchResultDisplayRowMap.containsKey(searchResultDisplayRow.getLocalId())) {
                    selectFlag = true;
                    searchResultDisplayRow.setDocType(globalEditForm.getDocType());
                    //globalEditForm.getGlobalEditRecords().add(searchResultDisplayRow);
                    searchResultDisplayRows.add(searchResultDisplayRow);
                    searchResultDisplayRowMap.put(searchResultDisplayRow.getLocalId(), searchResultDisplayRow);
                } else if (globalEditForm.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) &&
                        !searchResultDisplayRowMap.containsKey(searchResultDisplayRow.getItemIdentifier())) {
                    selectFlag = true;
                    searchResultDisplayRows.add(searchResultDisplayRow);
                    searchResultDisplayRow.setDocType(globalEditForm.getDocType());
                    //globalEditForm.getGlobalEditRecords().add(searchResultDisplayRow);
                    searchResultDisplayRowMap.put(searchResultDisplayRow.getItemIdentifier(), searchResultDisplayRow);
                }
            }
        }
        if (!selectFlag) {
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, OLEConstants.GLOBAL_EDIT_ADD_RECORDS_MESSAGE);
        }else{
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, OLEConstants.GLOBAL_EDIT_SELECTED_RECORDS_ADD_MESSAGE);
        }
        return navigate(globalEditForm, result, request, response);
    }

    private boolean canExportToRequestXml(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.DESC_WORKBENCH_EXPORT_XML);
    }

    private void browseDocstoreForLocalIds(OLESearchForm oleSearchForm, HttpServletRequest request) {
        int pageSize = oleSearchForm.getPageSize();
        oleSearchForm.setPageSize(40);
        StringBuilder ids = new StringBuilder();
        if ("title".equals(oleSearchForm.getBrowseField())) {
            oleSearchForm.setDocType(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode());
            List<SearchResultDisplayRow> searchResultDisplayRowList = getBrowseService().browse(oleSearchForm);
            for( SearchResultDisplayRow searchResultDisplayRow:searchResultDisplayRowList){
                ids.append("," + searchResultDisplayRow.getLocalId() +"#"+ searchResultDisplayRow.getBibIdentifier());
            }
        } else {
            String location = getBrowseService().validateLocation(oleSearchForm.getLocation());
            oleSearchForm.setLocation(location);
            if (oleSearchForm.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                List<Item> itemList = getBrowseService().browse(oleSearchForm);
                for( Item item:itemList){
                    ids.append("," + item.getLocalId() + "#" + item.getHolding().getBib().getId());
                }
            } else {
                List<Holdings> holdingsList = getBrowseService().browse(oleSearchForm);
                for( Holdings holdings:holdingsList){
                    ids.append("," + holdings.getLocalId() + "#" + holdings.getBib().getId());
                }
            }
        }
        oleSearchForm.setPageSize(pageSize);
        oleSearchForm.setIdsToBeOpened(ids.toString().replaceFirst(",", ""));
    }


    @RequestMapping(params = "methodToCall=exportToXml")
    public ModelAndView exportToXml(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        GlobalEditForm oleSearchForm = (GlobalEditForm) form;
        boolean hasPermission = canExportToRequestXml(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasPermission) {
            oleSearchForm.setJumpToId("breadcrumb_label");
            oleSearchForm.setFocusId("breadcrumb_label");
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
            return navigate(oleSearchForm, result, request, response);
        }
        List<String> idsToExport = new ArrayList<>();
        if(oleSearchForm.isSelectAllRecords()){
            if(StringUtils.isEmpty(oleSearchForm.getIdsToBeOpened())){
                if("browse".equalsIgnoreCase(oleSearchForm.getSearchType())){
                    browseDocstoreForLocalIds(oleSearchForm, request);
                }else{
                    searchDocstoreForLocalIds(oleSearchForm, request);
                }
            }
            String[]  localIds=oleSearchForm.getIdsToBeOpened().split(",");
            for(String localId:localIds){
                String[] ids= localId.split("#");
                idsToExport.add(ids[0]);
            }

        }else{
            for (SearchResultDisplayRow searchResultDisplayRow : oleSearchForm.getSearchResultDisplayRowList()) {
                if(searchResultDisplayRow.isSelect()) {
                    idsToExport.add(searchResultDisplayRow.getLocalId());
                }
            }
        }


        String requestXml = "";
        if(oleSearchForm.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode())) {

            try {
                BibTrees bibTrees = new BibTrees();
                for(String id : idsToExport) {
                    BibTree bibTree =getDocstoreLocalClient().retrieveBibTree(id);
                    setControlFields(bibTree);
                    bibTrees.getBibTrees().add(bibTree);
                }
                requestXml = BibTrees.serialize(bibTrees);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(oleSearchForm.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode())) {
            try {
                HoldingsTrees holdingsTrees = new HoldingsTrees();
                for (String id : idsToExport) {
                    HoldingsTree holdingsTree = getDocstoreLocalClient().retrieveHoldingsTree(id);
                    holdingsTrees.getHoldingsTrees().add(holdingsTree);
                }
                requestXml = HoldingsTrees.serialize(holdingsTrees);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(oleSearchForm.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
            try {
                List<Item> itemList = getDocstoreLocalClient().retrieveItems(idsToExport);
                Items items = new Items();
                items.getItems().addAll(itemList);
                requestXml = Items.serialize(items);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        oleSearchForm.setShowRequestXml(true);
        oleSearchForm.setRequestXMLTextArea(requestXml);
        return super.navigate(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=globalEdit")
    public ModelAndView globalEdit(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        GlobalVariables.getMessageMap().getInfoMessages().clear();
        List<SearchResultDisplayRow> searchResultDisplayRowList = globalEditForm.getGlobalEditRecords();
        Iterator<SearchResultDisplayRow> iterator = searchResultDisplayRowList.iterator();
        List<String> ids = new ArrayList();
        while (iterator.hasNext()) {
            SearchResultDisplayRow searchResultDisplayRow = iterator.next();
            if (searchResultDisplayRow.getDocType() != null && (searchResultDisplayRow.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode())
                    || searchResultDisplayRow.getDocType().equalsIgnoreCase(DocType.EHOLDINGS.getCode()))) {
                ids.add(searchResultDisplayRow.getHoldingsIdentifier());
            } else if (searchResultDisplayRow.getDocType() != null && searchResultDisplayRow.getDocType().equalsIgnoreCase(DocType.ITEM.getCode())) {
                ids.add(searchResultDisplayRow.getItemIdentifier());
            }
        }
        request.getSession().setAttribute("Ids", ids);
        return navigate(globalEditForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=close")
    public ModelAndView close(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        globalEditForm.setViewGlobalEditFlag(false);
//        globalEditForm.getGlobalEditRecords().clear();
//        globalEditForm.getGlobalEditMap().clear();
//        globalEditForm.getSearchResultDisplayRowList().clear();
        globalEditForm.setSelectAll(false);
        globalEditForm.getSearchConditions().clear();
        globalEditForm.getSearchConditions().addAll(globalEditForm.getSearchParams().getSearchConditions());
        return navigate(globalEditForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=globalClear")
    public ModelAndView globalClear(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        if (globalEditForm.getGlobalEditRecords().size() > 0) {
            globalEditForm.getGlobalEditRecords().clear();
        }
        return navigate(globalEditForm, result, request, response);
    }

    public SearchResultDisplayFields getDisplayFields(GlobalEditForm globalEditForm) {
        SearchResultDisplayFields searchResultDisplayFields = new SearchResultDisplayFields();
        searchResultDisplayFields.buildSearchResultDisplayFields(documentSearchConfig.getDocTypeConfigs(), globalEditForm.getDocType());
        return searchResultDisplayFields;
    }

    private void setShowPageSizeEntries(GlobalEditForm globalEditForm) {
        super.setShowPageSizeEntries(globalEditForm);
    }

    private boolean canGloballyEdit(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.GLOBAL_EDIT_PERMISSION);
    }

    /**
     * This method clears the Search criteria and the search Results.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */

    @Override
    @RequestMapping(params = "methodToCall=clear")
    public ModelAndView clear(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the clear method");
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        /* List<SearchCondition> searchConditions = globalEditForm.getSearchParams().getSearchConditions();
       searchConditions.add(new SearchCondition());*/
        globalEditForm.setFile(null);
        globalEditForm.getGlobalEditRecords().clear();
        globalEditForm.getGlobalEditMap().clear();
        globalEditForm.setSearchResultDisplayFields(new SearchResultDisplayFields());
        globalEditForm.setMessage(null);
        globalEditForm.setHoldingsList(null);
        globalEditForm.setItemList(null);
        globalEditForm.setPageSize(10);
        globalEditForm.setPreviousFlag(false);
        globalEditForm.setNextFlag(false);
        globalEditForm.setCallNumberBrowseText("");
        globalEditForm.setSelectAll(false);
        globalEditForm.setSearchParams(new SearchParams());
        if (globalEditForm.getSearchResultDisplayRowList() != null && globalEditForm.getSearchResultDisplayRowList().size() > 0) {
            globalEditForm.getSearchResultDisplayRowList().clear();
        }
        if (globalEditForm.getSearchParams() != null && globalEditForm.getSearchParams().getFacetFields() != null) {
            globalEditForm.getSearchParams().getFacetFields().clear();
        }
        if (globalEditForm.getFacetResultFields() != null) {
            globalEditForm.getFacetResultFields().clear();
        }
        return start(globalEditForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=removeFromList")
    public ModelAndView removeFromList(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        GlobalEditForm globalEditForm = (GlobalEditForm) form;
        List<SearchResultDisplayRow> rowsListToRemove = new ArrayList<>();
//        Map<String, SearchResultDisplayRow> globalEditMap = globalEditForm.getGlobalEditMap();
        for (SearchResultDisplayRow searchResultDisplayRow : globalEditForm.getGlobalEditRecords()) {
            if (searchResultDisplayRow.isSelect()) {
                searchResultDisplayRow.setSelect(false);
                rowsListToRemove.add(searchResultDisplayRow);
                globalEditForm.getGlobalEditMap().remove(searchResultDisplayRow.getLocalId());
            }
        }
        globalEditForm.getGlobalEditRecords().removeAll(rowsListToRemove);
        return navigate(globalEditForm, result, request, response);
    }
}
