package org.kuali.ole.describe.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.form.SerialsReceivingRecordForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.discovery.model.SearchCondition;
import org.kuali.ole.docstore.discovery.model.SearchParams;
import org.kuali.ole.pojo.OLESerialReceivingRecord;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;
import org.kuali.ole.service.SerialReceivingSearchService;
import org.kuali.ole.service.impl.SerialReceivingSearchServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sundarr
 * Date: 7/2/13
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/serialsReceivingRecordController")
public class SerialsReceivingRecordSearchController extends UifControllerBase {
    private static final Logger LOG = Logger.getLogger(SerialsReceivingRecordSearchController.class);
    private DocstoreClientLocator docstoreClientLocator;
    private int totalRecCount;
    private int start;
    private int pageSize;


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


    public String getPageShowEntries() {
        return "Showing " + ((this.start == 0) ? 1 : this.start + 1) + " to "
                + (((this.start + this.pageSize) > this.totalRecCount) ? this.totalRecCount : (this.start + this.pageSize))
                + " of " + this.totalRecCount + " entries";
    }


    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }


    @Override
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        return new SerialsReceivingRecordForm();
    }

    /**
     * This method converts UifFormBase to SerialsReceivingRecordForm
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the serialsReceivingRecordForm start method");
        SerialsReceivingRecordForm serialsReceivingRecordForm = (SerialsReceivingRecordForm) form;
        return super.navigate(serialsReceivingRecordForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.info("Inside Search Method");
        SerialsReceivingRecordForm serialsReceivingRecordForm = (SerialsReceivingRecordForm) form;
        PermissionService service = KimApiServiceLocator.getPermissionService();
        boolean isAuthorized = service.hasPermission(GlobalVariables.getUserSession().getPrincipalId(), "OLE-SELECT", "SERIAL_RECEIVING_SEARCH");
        if (!isAuthorized) {
            serialsReceivingRecordForm.setErrorAuthorisedUserMessage(OLEConstants.OLE_SERIALS_RECEIVING_NOT_AUTHORIZED);
            return navigate(serialsReceivingRecordForm, result, request, response);
        }
        List<SearchCondition> searchConditionList = serialsReceivingRecordForm.getSearchParams().getSearchFieldsList();
        String title = "";
        String issn = "";
        String serialRecordNo = "";
        String localIdentifier = "";
        String poId = "";
        String journalTitle = "";
        for (SearchCondition sc : searchConditionList) {
            if (sc.getDocField().equalsIgnoreCase(OLEConstants.TITLE_SEARCH) && sc.getSearchText() != null && !sc.getSearchText().isEmpty()) {
                title = sc.getSearchText() != null ? sc.getSearchText() : "";
            } else if (sc.getDocField().equalsIgnoreCase(OLEConstants.ISSN_SEARCH) && sc.getSearchText() != null && !sc.getSearchText().isEmpty()) {
                issn = sc.getSearchText() != null ? sc.getSearchText() : "";
            } else if (sc.getDocField().equalsIgnoreCase(OLEConstants.SERIAL_SEARCH) && sc.getSearchText() != null && !sc.getSearchText().isEmpty()) {
                serialRecordNo = sc.getSearchText() != null ? sc.getSearchText() : "";
            } else if (sc.getDocField().equalsIgnoreCase(OLEConstants.LOCALID_SEARCH) && sc.getSearchText() != null && !sc.getSearchText().isEmpty()) {
                localIdentifier = sc.getSearchText() != null ? sc.getSearchText() : "";
            } else if (sc.getDocField().equalsIgnoreCase(OLEConstants.PO_SEARCH) && sc.getSearchText() != null && !sc.getSearchText().isEmpty()) {
                poId = sc.getSearchText() != null ? sc.getSearchText() : "";
            }else if (sc.getDocField().equalsIgnoreCase(OLEConstants.JOURNAL_TITLE_SEARCH) && sc.getSearchText() != null && !sc.getSearchText().isEmpty()) {
                journalTitle = sc.getSearchText() != null ? sc.getSearchText() : "";
            }
        }
        if (serialsReceivingRecordForm.getSearchFlag().equalsIgnoreCase("search")) {
            this.start = 0;
        }
        this.pageSize = Integer.parseInt(serialsReceivingRecordForm.getSearchLimit());

        List<OLESerialReceivingRecord> oleSerialReceivingRecordList = new ArrayList<>();
        SerialReceivingSearchService serialReceivingSearchService = new SerialReceivingSearchServiceImpl();
        HashMap<String,String> criteriaMap=new HashMap<>();
        criteriaMap.put(OLEConstants.TITLE,title);
        criteriaMap.put(OLEConstants.ISSN,issn);
        criteriaMap.put(OLEConstants.LOCAL_IDENTIFIER,localIdentifier);
        criteriaMap.put(OLEConstants.JOURNAL_TITLE_SEARCH,journalTitle);
        if (title.isEmpty() && issn.isEmpty() && poId.isEmpty() && localIdentifier.isEmpty() && serialRecordNo.isEmpty() && journalTitle.isEmpty()) {
            SearchResponse searchResponse = serialReceivingSearchService.holdingSearch(this.start, Integer.parseInt(serialsReceivingRecordForm.getSearchLimit()),serialsReceivingRecordForm.getSortOrder());
            this.totalRecCount = searchResponse.getTotalRecordCount();
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                oleSerialReceivingRecordList.add(serialReceivingSearchService.getSerialRecord(searchResult));
            }
            serialsReceivingRecordForm.setPaginationFlag("true");
            serialsReceivingRecordForm.setPageResultDisplay(getPageShowEntries());
        } else if ((!title.isEmpty() || !issn.isEmpty() || !localIdentifier.isEmpty() || !journalTitle.isEmpty()) && (poId.isEmpty() && serialRecordNo.isEmpty())) {
            SearchResponse searchResponse = serialReceivingSearchService.searchDataFromDocstore(this.start, Integer.parseInt(serialsReceivingRecordForm.getSearchLimit()),
                    new HashSet<String>(), criteriaMap,serialsReceivingRecordForm.getSortOrder());

            this.totalRecCount = searchResponse.getTotalRecordCount();
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                oleSerialReceivingRecordList.addAll(serialReceivingSearchService.getOleSerialReceivingListFromHoldings(searchResult));
            }
            serialsReceivingRecordForm.setPaginationFlag("true");
            this.totalRecCount = oleSerialReceivingRecordList.size();
            serialsReceivingRecordForm.setPageResultDisplay(getPageShowEntries());
        } else if ((!poId.isEmpty() && !poId.contains("*") && serialRecordNo.isEmpty() && StringUtils.isNumeric(poId))) {
            Set<String> instanceIds = serialReceivingSearchService.getInstanceIdList(poId);
            if ((instanceIds != null && instanceIds.size() > 0)) {
                SearchResponse searchResponse = serialReceivingSearchService.searchDataFromDocstore(this.start, Integer.parseInt(serialsReceivingRecordForm.getSearchLimit()),
                        instanceIds, criteriaMap,serialsReceivingRecordForm.getSortOrder());
                this.totalRecCount = searchResponse.getTotalRecordCount();
                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    oleSerialReceivingRecordList.add(serialReceivingSearchService.getSerialRecord(searchResult));
                }
                serialsReceivingRecordForm.setPaginationFlag("true");
                serialsReceivingRecordForm.setPageResultDisplay(getPageShowEntries());
            }
        } else if (!serialRecordNo.isEmpty() && !serialRecordNo.contains("*") && StringUtils.isNumeric(serialRecordNo)) {
            List<OLESerialReceivingDocument> oleSerialReceivingDocuments = serialReceivingSearchService.getOleSerialReceivingDocuments(serialsReceivingRecordForm);
            if (oleSerialReceivingDocuments != null && oleSerialReceivingDocuments.size() > 0) {
                if (!title.isEmpty() || !issn.isEmpty()) {
                    Set<String> instanceIds = new HashSet<>();
                    for (OLESerialReceivingDocument oleSerialReceivingDocument : oleSerialReceivingDocuments) {
                        instanceIds.add(oleSerialReceivingDocument.getInstanceId());
                    }
                    SearchResponse searchResponse = serialReceivingSearchService.searchDataFromDocstore(this.start, Integer.parseInt(serialsReceivingRecordForm.getSearchLimit()),
                            instanceIds, criteriaMap,serialsReceivingRecordForm.getSortOrder());
                    this.totalRecCount = searchResponse.getTotalRecordCount();
                    for (SearchResult searchResult : searchResponse.getSearchResults()) {
                        oleSerialReceivingRecordList.add(serialReceivingSearchService.getSerialRecord(searchResult));
                    }
                    serialsReceivingRecordForm.setPaginationFlag("true");
                    serialsReceivingRecordForm.setPageResultDisplay(getPageShowEntries());
                } else {
                    oleSerialReceivingRecordList.addAll(serialReceivingSearchService.getOleSerialReceivingList(oleSerialReceivingDocuments));
                }
            }
        }
        serialsReceivingRecordForm.setPreviousFlag(getPreviousFlag());
        serialsReceivingRecordForm.setNextFlag(getNextFlag());
        if (oleSerialReceivingRecordList != null && oleSerialReceivingRecordList.size() > 0) {
            //serialsReceivingRecordForm.setOleSerialReceivingRecordList(oleSerialReceivingRecordList.size() > pageSize ? oleSerialReceivingRecordList.subList(0, pageSize) : oleSerialReceivingRecordList);
            serialsReceivingRecordForm.setOleSerialReceivingRecordList(oleSerialReceivingRecordList);
            //serialsReceivingRecordForm.setTempSerialReceivingRecordList(oleSerialReceivingRecordList);
            serialsReceivingRecordForm.setPageResultDisplay(getPageShowEntries());
        }
        if (oleSerialReceivingRecordList.size() == 0) {
            serialsReceivingRecordForm.setOleSerialReceivingRecordList(null);
            serialsReceivingRecordForm.setTempSerialReceivingRecordList(null);
            serialsReceivingRecordForm.setErrorAuthorisedUserMessage(OLEConstants.OLE_SERIALS_RECEIVING_NO_RECORD);
        }
        return navigate(serialsReceivingRecordForm, result, request, response);
    }


    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Inside clearSearch Method");
        SerialsReceivingRecordForm serialsReceivingRecordForm = (SerialsReceivingRecordForm) form;
        serialsReceivingRecordForm.setSearchParams(new SearchParams());
        List<SearchCondition> searchConditions = serialsReceivingRecordForm.getSearchParams().getSearchFieldsList();
        SearchCondition title = new SearchCondition();
        title.setDocField(OLEConstants.TITLE_SEARCH);
        SearchCondition issn = new SearchCondition();
        issn.setDocField(OLEConstants.ISSN_SEARCH);
        SearchCondition serialId = new SearchCondition();
        serialId.setDocField(OLEConstants.SERIAL_SEARCH);
        SearchCondition bibId = new SearchCondition();
        bibId.setDocField(OLEConstants.LOCALID_SEARCH);
        SearchCondition poId = new SearchCondition();
        poId.setDocField(OLEConstants.PO_SEARCH);
        SearchCondition journalTitle = new SearchCondition();
        journalTitle.setDocField(OLEConstants.JOURNAL_TITLE_SEARCH);
        searchConditions.add(title);
        searchConditions.add(issn);
        searchConditions.add(serialId);
        searchConditions.add(bibId);
        searchConditions.add(poId);
        searchConditions.add(journalTitle);
        serialsReceivingRecordForm.setOleSerialReceivingRecordList(null);
        serialsReceivingRecordForm.setTempSerialReceivingRecordList(null);
        serialsReceivingRecordForm.setErrorAuthorisedUserMessage("");
        return getUIFModelAndView(serialsReceivingRecordForm);

    }

    @RequestMapping(params = "methodToCall=nextSearch")
    public ModelAndView nextSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {

        SerialsReceivingRecordForm serialsReceivingRecordForm = (SerialsReceivingRecordForm) form;
        //int listSize = serialsReceivingRecordForm.getTempSerialReceivingRecordList().size();
        //List<OLESerialReceivingRecord> tempSerialReceivingRecordList;
        SearchParams searchParams = serialsReceivingRecordForm.getSearchParams();
        int start = Math.max(0, this.start + this.pageSize);
        /*if (listSize > pageSize) {
            searchParams.setStart(start);
            serialsReceivingRecordForm.setSearchFlag("next");
            this.start = start;
            tempSerialReceivingRecordList = serialsReceivingRecordForm.getTempSerialReceivingRecordList().subList(start, serialsReceivingRecordForm.getTempSerialReceivingRecordList().size());
            serialsReceivingRecordForm.setOleSerialReceivingRecordList(tempSerialReceivingRecordList.size() > pageSize ? tempSerialReceivingRecordList.subList(0, pageSize) : tempSerialReceivingRecordList);
            serialsReceivingRecordForm.setPaginationFlag("true");
            serialsReceivingRecordForm.setPageResultDisplay(getPageShowEntries());

            serialsReceivingRecordForm.setNextFlag(getNextFlag());
            serialsReceivingRecordForm.setPreviousFlag(getPreviousFlag());
            return getUIFModelAndView(serialsReceivingRecordForm);

        }*/
            searchParams.setStart(start);
            serialsReceivingRecordForm.setSearchFlag("next");
            this.start = start;
            return search(serialsReceivingRecordForm, result, request, response);


    }

    @RequestMapping(params = "methodToCall=previousSearch")
    public ModelAndView previousSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        SerialsReceivingRecordForm serialsReceivingRecordForm = (SerialsReceivingRecordForm) form;
        //int listSize = serialsReceivingRecordForm.getTempSerialReceivingRecordList().size();
        //List<OLESerialReceivingRecord> tempSerialReceivingRecordList;
        SearchParams searchParams = serialsReceivingRecordForm.getSearchParams();
        int start = Math.max(0, this.start - this.pageSize);
       /* if (listSize < pageSize) {
            searchParams.setStart(start);
            serialsReceivingRecordForm.setSearchFlag("previous");
            this.start = start;
            tempSerialReceivingRecordList = serialsReceivingRecordForm.getTempSerialReceivingRecordList().subList(start, serialsReceivingRecordForm.getTempSerialReceivingRecordList().size());
            serialsReceivingRecordForm.setOleSerialReceivingRecordList(tempSerialReceivingRecordList.size() > pageSize ? tempSerialReceivingRecordList.subList(0, pageSize) : tempSerialReceivingRecordList);
            serialsReceivingRecordForm.setOleSerialReceivingRecordList(tempSerialReceivingRecordList.size() > pageSize ? tempSerialReceivingRecordList.subList(0, pageSize) : tempSerialReceivingRecordList);
            serialsReceivingRecordForm.setPaginationFlag("true");
            serialsReceivingRecordForm.setPageResultDisplay(getPageShowEntries());

            serialsReceivingRecordForm.setNextFlag(getNextFlag());
            serialsReceivingRecordForm.setPreviousFlag(getPreviousFlag());
            return getUIFModelAndView(serialsReceivingRecordForm);

        } */
            searchParams.setStart(start);
            serialsReceivingRecordForm.setSearchFlag("previous");
            this.start = start;
            return search(serialsReceivingRecordForm, result, request, response);


    }


    @RequestMapping(params = "methodToCall=descendingSort")
    public ModelAndView descendingSort(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        SerialsReceivingRecordForm serialsReceivingRecordForm = (SerialsReceivingRecordForm) form;
        SearchParams searchParams = serialsReceivingRecordForm.getSearchParams();
        int start = 0;
        searchParams.setStart(start);
        serialsReceivingRecordForm.setSearchFlag("next");
        this.start = start;
        serialsReceivingRecordForm.setSortOrder("desc");
        return search(serialsReceivingRecordForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=ascendingSort")
    public ModelAndView ascendingSort(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {

        SerialsReceivingRecordForm serialsReceivingRecordForm = (SerialsReceivingRecordForm) form;
        SearchParams searchParams = serialsReceivingRecordForm.getSearchParams();
        int start = 0;
        searchParams.setStart(start);
        serialsReceivingRecordForm.setSearchFlag("next");
        this.start = start;
        serialsReceivingRecordForm.setSortOrder("asc");
        return search(serialsReceivingRecordForm, result, request, response);
    }


}
