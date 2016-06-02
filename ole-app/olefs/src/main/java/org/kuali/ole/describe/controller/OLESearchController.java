package org.kuali.ole.describe.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.SearchResultDisplayFields;
import org.kuali.ole.describe.bo.SearchResultDisplayRow;
import org.kuali.ole.describe.form.GlobalEditForm;
import org.kuali.ole.describe.form.OLESearchForm;
import org.kuali.ole.describe.service.BrowseService;
import org.kuali.ole.describe.service.impl.BrowseServiceImpl;
import org.kuali.ole.docstore.OleException;
import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.config.DocFieldConfig;
import org.kuali.ole.docstore.common.document.config.DocFormatConfig;
import org.kuali.ole.docstore.common.document.config.DocTypeConfig;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.ControlField;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.engine.client.DocstoreLocalClient;
import org.kuali.ole.docstore.utility.ISBNUtil;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OleDocstoreResponse;
import org.kuali.ole.select.document.OLEEResourceInstance;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chandrasekharag
 * Date: 26/2/14
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/olesearchcontroller")
public class OLESearchController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(OLESearchController.class);
    private String eResourceId;
    private String tokenId;
    private int totalRecCount;
    private int start;
    private int pageSize;
    private DocstoreClient docstoreClient = getDocstoreLocalClient();
    private OLEEResourceSearchService oleEResourceSearchService;
    private BrowseService browseService;
    private DocumentService documentService;
    private BusinessObjectService businessObjectService;

    public DocstoreClient getDocstoreLocalClient() {
        if (null == docstoreClient) {
            return new DocstoreLocalClient();
        }
        return docstoreClient;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }


    public BrowseService getBrowseService() {
        if(browseService == null) {
            browseService = new BrowseServiceImpl();
        }
        return browseService;
    }
    public OLEEResourceSearchService getOleEResourceSearchService() {
        if (oleEResourceSearchService == null) {
            oleEResourceSearchService = GlobalResourceLoader.getService(OLEConstants.OLEEResourceRecord.ERESOURSE_SEARCH_SERVICE);
        }
        return oleEResourceSearchService;
    }
    public DocumentService getDocumentService() {
        if (this.documentService == null) {
            this.documentService = KRADServiceLocatorWeb.getDocumentService();
        }
        return this.documentService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }
    DocumentSearchConfig documentSearchConfig = DocumentSearchConfig.getDocumentSearchConfig();

    public int getTotalRecCount() {
        return totalRecCount;
    }

    public void setTotalRecCount(int totalRecCount) {
        this.totalRecCount = totalRecCount;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

    public String getPageShowEntries() {
        return "Showing " + ((this.start == 0) ? 1 : this.start + 1) + " to "
                + (((this.start + this.pageSize) > this.totalRecCount) ? this.totalRecCount : (this.start + this.pageSize))
                + " of " + this.totalRecCount + " entries";
    }

    public String getFacetShowEntries(SearchParams searchParams, int totalRecordCount) {
        return "Showing " + ((searchParams.getFacetOffset() == 0) ? 1 : searchParams.getFacetOffset() + 1) + " to "
                + (((searchParams.getFacetOffset()  + searchParams.getFacetLimit()) > totalRecordCount) ? totalRecordCount : (searchParams.getFacetOffset() + searchParams.getFacetLimit()))
                + " of " + totalRecordCount + " entries";
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        return new OLESearchForm();

    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        this.start = 0;
        LOG.debug("Inside the olesearchform start method");
        OLESearchForm oleSearchForm = (OLESearchForm) form;

        request.getSession().setAttribute("selectedFacetResults", null);
        if (oleSearchForm.getDocType() == null) {
            oleSearchForm.setDocType(DocType.BIB.getCode());
        }
        if (StringUtils.isEmpty(oleSearchForm.getSearchType())) {
            oleSearchForm.setSearchType("search");
        }
        if (StringUtils.isEmpty(oleSearchForm.getBrowseField())) {
            oleSearchForm.setBrowseField("title");
        }

        oleSearchForm.getSearchConditions().clear();
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setOperator("AND");
        SearchField searchField = new SearchField();
        searchField.setFieldName("any");
        searchField.setDocType(oleSearchForm.getDocType());
        searchCondition.setSearchField(searchField);
        oleSearchForm.getSearchConditions().add(searchCondition);
        String eInstance = request.getParameter(OLEConstants.E_INSTANCE);
        if (eInstance != null && eInstance.equalsIgnoreCase(OLEConstants.LINK_EXISTING_INSTANCE)) {
            oleSearchForm.setLinkExistingInstance(eInstance);
        }
        if (request.getParameter(OLEConstants.E_RESOURCE_ID) != null) {
            eResourceId = request.getParameter(OLEConstants.E_RESOURCE_ID);
        }
        if (request.getParameter(OLEConstants.TOKEN_ID) != null) {
            tokenId = request.getParameter(OLEConstants.TOKEN_ID);
        }
        oleSearchForm.setStart(0);
        if(oleSearchForm.getSearchParams() != null) {
            oleSearchForm.getSearchParams().setStartIndex(0);
        }
        clearForm(oleSearchForm);
        GlobalVariables.getMessageMap().clearErrorMessages();

        if ( oleSearchForm.getDocType().equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
            boolean hasLinkPermission = canLinkBibForRequisition(GlobalVariables.getUserSession().getPrincipalId());
            if (!hasLinkPermission && !oleSearchForm.getViewId().equalsIgnoreCase("OLESearchView")) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
                return super.navigate(oleSearchForm, result, request, response);
            }
        }
        oleSearchForm.setMessage(null);
        oleSearchForm.setSearchResultDisplayRowList(null);
        oleSearchForm.setBibSearchResultDisplayRowList(null);
        oleSearchForm.setHoldingSearchResultDisplayRowList(null);
        oleSearchForm.setShowTime(true);
        setDefaultParameter(oleSearchForm, null);
        return super.navigate(oleSearchForm, result, request, response);
    }

    private void setDefaultParameter(OLESearchForm oleSearchForm,String collectionIndex) {

         if(collectionIndex == null){
             collectionIndex = oleSearchForm.getCollectionIndex();
         }
        if(oleSearchForm.getSearchConditions().size()>0){
            SearchCondition searchCondition = oleSearchForm.getSearchConditions().get(Integer.valueOf(collectionIndex));
            SearchField searchField = searchCondition.getSearchField();

            if(searchField.getDocType().equalsIgnoreCase(DocType.BIB.getCode())){
                String parameter = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                        .DESCRIBE_COMPONENT, OLEConstants.BIB_SEARCH_SCOPE_FIELD);
                String []scopeField = parameter.split(",");
                if(scopeField.length == 2){
                    searchCondition.setSearchScope(scopeField[0]);
                    searchField.setFieldName(scopeField[1]);
                    searchCondition.setSearchField(searchField);
                }
            } else if(searchField.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode())){
                String parameter = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                        .DESCRIBE_COMPONENT, OLEConstants.HOLDINGS_SEARCH_SCOPE_FIELD);
                String []scopeField = parameter.split(",");
                if(scopeField.length == 2){
                    searchCondition.setSearchScope(scopeField[0]);
                    searchField.setFieldName(scopeField[1]);
                    searchCondition.setSearchField(searchField);
                }
            } else if(searchField.getDocType().equalsIgnoreCase(DocType.EHOLDINGS.getCode())){
                String parameter = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                        .DESCRIBE_COMPONENT, OLEConstants.EHOLDINGS_SEARCH_SCOPE_FIELD);
                String []scopeField = parameter.split(",");
                if(scopeField.length == 2){
                    searchCondition.setSearchScope(scopeField[0]);
                    searchField.setFieldName(scopeField[1]);
                    searchCondition.setSearchField(searchField);
                }
            } else if(searchField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode())){
                String parameter = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                        .DESCRIBE_COMPONENT, OLEConstants.ITEM_SEARCH_SCOPE_FIELD);
                String []scopeField = parameter.split(",");
                if(scopeField.length == 2){
                    searchCondition.setSearchScope(scopeField[0]);
                    searchField.setFieldName(scopeField[1]);
                    searchCondition.setSearchField(searchField);
                }
            }
        }
    }

    @RequestMapping(params = "methodToCall=changeDocType")
    public ModelAndView changeDocType(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        setDefaultParameter(oleSearchForm,null);
        oleSearchForm.setSearchResultDisplayRowList(null);
        oleSearchForm.setFacetResultFields(null);
        return getUIFModelAndView(oleSearchForm);
    }

    private boolean canSearch(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.DESC_WORKBENCH_SEARCH);
    }

    private boolean canLinkBibForRequisition(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.SELECT_NMSPC, OLEConstants.LINK_EXISTING_BIB);
    }

    @RequestMapping(params = "methodToCall=submit")
    public ModelAndView submit(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        boolean isValid = false;
        BusinessObjectService boService = getBusinessObjectService();
        Map<String, String> map = new HashMap<>();
        List<Integer> resultList = new ArrayList<>();
        for (SearchResultDisplayRow searchResultDisplayRow : oleSearchForm.getSearchResultDisplayRowList()) {
            if (searchResultDisplayRow.isSelect()) {
                map.put(OLEConstants.BIB_ID, DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC, searchResultDisplayRow.getLocalId()));
                List<OleCopy> listOfValues = (List<OleCopy>) boService.findMatching(OleCopy.class, map);
                if (listOfValues.size() > 0 && (oleSearchForm.getMessage() == null || oleSearchForm.getMessage().equals(""))) {
                    for (OleCopy oleCopy : listOfValues) {
                        if (oleCopy.getReqDocNum() != null) {
                            resultList.add(oleCopy.getReqDocNum());
                        }
                    }
                    if (resultList.size() > 0) {
                        Set<Integer> resultSet = new HashSet<>(resultList);
                        resultList = new ArrayList<>(resultSet);
                        StringBuffer reqIds = new StringBuffer();
                        if (resultList.size() > 0) {
                            int count = 0;
                            for (; count < resultList.size() - 1; count++) {
                                reqIds.append(resultList.get(count) + OLEConstants.COMMA);
                            }
                            reqIds.append(resultList.get(count));
                        }
                        oleSearchForm.setMessage(OLEConstants.POPUP_MESSAGE + reqIds.toString() + OLEConstants.PROCEED_MESSAGE);
                        return getUIFModelAndView(oleSearchForm);
                    }
                }
                oleSearchForm.setMessage(OLEConstants.OLEEResourceRecord.SPACE);
                processNewRecordResponseForOLE(searchResultDisplayRow.getLocalId(), oleSearchForm.getTokenId(), oleSearchForm.getLinkToOrderOption());
                oleSearchForm.setSuccessMessage(OLEConstants.LINK_SUCCESS_MESSAGE);
                isValid = true;
                break;
            }
        }
        if (isValid == false) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.BIB_SELECT);
            return getUIFModelAndView(oleSearchForm);
        }
        return getUIFModelAndView(oleSearchForm);
    }

    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        float start = System.currentTimeMillis() / 1000;
        OLESearchForm oleSearchForm = (OLESearchForm) form;

        if (oleSearchForm.getDocType().equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
            boolean hasLinkPermission = canLinkBibForRequisition(GlobalVariables.getUserSession().getPrincipalId());
            String viewId = oleSearchForm.getViewId();
            if (!hasLinkPermission && !viewId.equalsIgnoreCase("OLESearchView") && !viewId.equalsIgnoreCase("BoundwithView")
                    && !viewId.equalsIgnoreCase("TransferView") && !viewId.equalsIgnoreCase(OLEConstants.ANALYTICS_VIEW)
                    && !viewId.equalsIgnoreCase(OLEConstants.ANALYTICS_SUMMARY_VIEW) && !viewId.equalsIgnoreCase("GlobalEditView")) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
                return super.navigate(oleSearchForm, result, request, response);
            }
        }
        oleSearchForm.getSearchParams().setFacetOffset(0);
        searchDocstoreData(oleSearchForm, request);
        float end = System.currentTimeMillis() / 1000;
        oleSearchForm.setServerTime(String.valueOf(end - start));
        return super.navigate(oleSearchForm, result, request, response);
    }
    private void processNewRecordResponseForOLE(String bibId, String tokenId, String linkToOrderOption) throws Exception {
        String instanceUUID = null;
        BibTree bibTree = getDocstoreLocalClient().retrieveBibTree(bibId);
        OLEEditorResponse oleEditorResponse = new OLEEditorResponse();
        if (bibTree.getHoldingsTrees() != null && bibTree.getHoldingsTrees().size() > 0) {
            instanceUUID = bibTree.getHoldingsTrees().get(0).getHoldings().getId();
        }
        oleEditorResponse.setLinkedInstanceId(instanceUUID);
        oleEditorResponse.setBib(bibTree.getBib());
        oleEditorResponse.setTokenId(tokenId);
        oleEditorResponse.setLinkToOrderOption(linkToOrderOption);
        HashMap<String, OLEEditorResponse> oleEditorResponseMap = new HashMap<String, OLEEditorResponse>();
        oleEditorResponseMap.put(tokenId, oleEditorResponse);
        OleDocstoreResponse.getInstance().setEditorResponse(oleEditorResponseMap);
    }

    protected void setShowPageSizeEntries(OLESearchForm oleSearchForm) {
        List<Integer> pageSizes = documentSearchConfig.getPageSizes();
        if (CollectionUtils.isEmpty(pageSizes)) {
            pageSizes.add(10);
            pageSizes.add(25);
            pageSizes.add(50);
            pageSizes.add(100);
        }
        oleSearchForm.setShowPageSize(pageSizes.toString());
    }

    @RequestMapping(params = "methodToCall=pageNumberSearch")
    public ModelAndView pageNumberSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {

        OLESearchForm oleSearchForm = (OLESearchForm) form;
        SearchParams searchParams = oleSearchForm.getSearchParams();
        try {
            int start = Math.max(0,
                    (Integer.parseInt(oleSearchForm.getPageNumber()) - 1)
                            * searchParams.getPageSize());
            searchParams.setStartIndex(start);
        } catch (NumberFormatException e) {
            LOG.warn("Invalid page number " + oleSearchForm.getPageNumber(), e);
        }
        return search(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=lastPageSearch")
    public ModelAndView lastPageSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        OLESearchForm oleSearchForm = (OLESearchForm) form;
        SearchParams searchParams = oleSearchForm.getSearchParams();
        try {
            int totalcount = oleSearchForm.getTotalRecordCount();
            int pageSize = searchParams.getPageSize();
            int totlaPages = totalcount/pageSize;
            int lastNumber= pageSize*totlaPages;
            int lastPage = totalcount - pageSize;
            oleSearchForm.setPageNumber(Integer.toString(totlaPages));
            if(lastNumber < totalcount){
                lastPage = lastNumber;
                oleSearchForm.setPageNumber(Integer.toString(totlaPages+1));
            }
            int start = Math.max(0, lastPage);
            searchParams.setStartIndex(start);
        } catch (NumberFormatException e) {
            LOG.warn("Invalid page number " + oleSearchForm.getPageNumber(), e);
        }
        return search(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=nextSearch")
    public ModelAndView nextSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {

        OLESearchForm oleSearchForm = (OLESearchForm) form;
        SearchParams searchParams = oleSearchForm.getSearchParams();
        int start = Math.max(0, searchParams.getStartIndex() + searchParams.getPageSize());
        searchParams.setStartIndex(start);
        return search(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=previousSearch")
    public ModelAndView previousSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        OLESearchForm oleSearchForm = (OLESearchForm) form;
        SearchParams searchParams = oleSearchForm.getSearchParams();
        int start = Math.max(0, searchParams.getStartIndex() - oleSearchForm.getPageSize());
        searchParams.setStartIndex(start);
        return search(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=facetSearch")
    public ModelAndView facetSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        String docType = request.getParameter("docType");
        String selectedFacet = request.getParameter("selectedFacet");
        String selectedFacetName = request.getParameter("selectedFacetName");
        oleSearchForm.setDocType(docType);
        if (oleSearchForm.getSearchParams() == null) {
            SearchParams searchParams = (SearchParams) request.getSession().getAttribute("searchParams");
            oleSearchForm.setSearchParams(searchParams);
            searchParams.setStartIndex(0);
        }
        oleSearchForm.getSearchConditions().clear();
        SearchCondition searchCondition = new SearchCondition();
        List<Integer> pageSizes = documentSearchConfig.getPageSizes();
        if(!pageSizes.isEmpty() || pageSizes.size() > 0) {
            oleSearchForm.setPageSize(pageSizes.get(0));
        }
        searchCondition.setOperator("AND");
        oleSearchForm.getSearchConditions().addAll(oleSearchForm.getSearchParams().getSearchConditions());
        String eInstance = request.getParameter(OLEConstants.E_INSTANCE);
        if (eInstance != null && eInstance.equalsIgnoreCase(OLEConstants.LINK_EXISTING_INSTANCE)) {
            oleSearchForm.setLinkExistingInstance(eInstance);
        }
        if (request.getParameter(OLEConstants.TOKEN_ID) != null) {
            tokenId = request.getParameter(OLEConstants.TOKEN_ID);
        }
        oleSearchForm.getSearchParams().getFacetFields().addAll(getFacetFields(oleSearchForm.getDocType()));
        oleSearchForm.getSearchParams().setFacetPrefix("");
        oleSearchForm.getSearchParams().setFacetLimit(documentSearchConfig.getFacetPageSizeShort());
        FacetCondition facetCondition = new FacetCondition();
        facetCondition.setFieldName(selectedFacetName);
        facetCondition.setFieldValue(selectedFacet);
        oleSearchForm.getSearchParams().getFacetConditions().add(facetCondition);
        oleSearchForm.setSearchType("search");
        GlobalVariables.getMessageMap().clearErrorMessages();
        return search(oleSearchForm, result, request, response);
    }
    @RequestMapping(params = "methodToCall=removeFacet")
    public ModelAndView removeFacet(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        int index = Integer.parseInt(oleSearchForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if(!CollectionUtils.isEmpty(oleSearchForm.getSearchParams().getFacetConditions()) && oleSearchForm.getSearchParams().getFacetConditions().size() > index) {
            oleSearchForm.getSearchParams().getFacetConditions().remove(index);
        }
        return search(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=moreFacets")
    public ModelAndView moreFacets(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        if(StringUtils.isEmpty(oleSearchForm.getDocType())) {
            oleSearchForm.setDocType(request.getParameter("docType"));
        }
        SearchParams searchParams = oleSearchForm.getSearchParams();
        if(searchParams == null) {
            searchParams = (SearchParams) request.getSession().getAttribute("searchParams");
        }
        if(StringUtils.isNotEmpty(request.getParameter("facetField"))) {
            if(!CollectionUtils.isEmpty(searchParams.getFacetFields())) {
                searchParams.getFacetFields().clear();
            }
            searchParams.getFacetFields().add(request.getParameter("facetField"));
        }
        if(StringUtils.isNotEmpty(request.getParameter("facetPrefix"))) {
            searchParams.setFacetPrefix(request.getParameter("facetPrefix"));
        }
        oleSearchForm.setSearchParams(searchParams);
        oleSearchForm.getSearchConditions().addAll(searchParams.getSearchConditions());
        searchParams.setFacetOffset(0);
        searchParams.setFacetLimit(documentSearchConfig.getFacetPageSizeLong());
        oleSearchForm.setMoreFacets(true);
        searchDocstoreData(oleSearchForm, request);
        return super.navigate(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=nextFacet")
    public ModelAndView nextFacet(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        if(StringUtils.isEmpty(oleSearchForm.getDocType())) {
            oleSearchForm.setDocType(request.getParameter("docType"));
        }
        SearchParams searchParams = oleSearchForm.getSearchParams();
        if(searchParams == null) {
            searchParams = (SearchParams) request.getSession().getAttribute("searchParams");
        }
        if(StringUtils.isNotEmpty(request.getParameter("facetField"))) {
            if(!CollectionUtils.isEmpty(searchParams.getFacetFields())) {
                searchParams.getFacetFields().clear();
            }
            searchParams.getFacetFields().add(request.getParameter("facetField"));
        }
        int facetCount = oleSearchForm.getSearchResponse().getFacetResult().getFacetResultFields().get(0).getTotalCount();
        int facetOffset = searchParams.getFacetOffset() + documentSearchConfig.getFacetPageSizeLong();
        searchParams.setFacetOffset(facetOffset);
        oleSearchForm.setSearchParams(searchParams);
        oleSearchForm.getSearchConditions().addAll(searchParams.getSearchConditions());
        oleSearchForm.setMoreFacets(true);
        oleSearchForm.setFacetPageEntries(getFacetShowEntries(searchParams, facetCount));
        searchDocstoreData(oleSearchForm, request);
        return super.navigate(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=previousFacet")
    public ModelAndView previousFacet(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        if(StringUtils.isEmpty(oleSearchForm.getDocType())) {
            oleSearchForm.setDocType(request.getParameter("docType"));
        }
        SearchParams searchParams = oleSearchForm.getSearchParams();
        if(searchParams == null) {
            searchParams = (SearchParams) request.getSession().getAttribute("searchParams");
        }
        if(StringUtils.isNotEmpty(request.getParameter("facetField"))) {
            if(!CollectionUtils.isEmpty(searchParams.getFacetFields())) {
                searchParams.getFacetFields().clear();
            }
            searchParams.getFacetFields().add(request.getParameter("facetField"));
        }
        int facetCount = oleSearchForm.getSearchResponse().getFacetResult().getFacetResultFields().get(0).getTotalCount();
        int facetLimit = searchParams.getFacetOffset() - documentSearchConfig.getFacetPageSizeLong();
        searchParams.setFacetOffset(facetLimit > 0 ? facetLimit : 0);
        oleSearchForm.setSearchParams(searchParams);
        oleSearchForm.getSearchConditions().addAll(searchParams.getSearchConditions());
        oleSearchForm.setMoreFacets(true);
        oleSearchForm.setFacetPageEntries(getFacetShowEntries(searchParams, facetCount));
        searchDocstoreData(oleSearchForm, request);
        return super.navigate(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=browse")
    public ModelAndView browse(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        boolean hasPermission = performBrowse(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasPermission) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_AUTHORIZATION);
            return getUIFModelAndView(oleSearchForm);
        }
//        oleSearchForm.setPageSize(10);
        if ("title".equals(oleSearchForm.getBrowseField())) {
            oleSearchForm.setDocType(DocType.BIB.getCode());
            List<SearchResultDisplayRow> searchResultDisplayRowList = getBrowseService().browse(oleSearchForm);
            oleSearchForm.setSearchResultDisplayRowList(searchResultDisplayRowList);

        } else {
            String location = getBrowseService().validateLocation(oleSearchForm.getLocation());
            oleSearchForm.setLocation(location);
            if (oleSearchForm.getDocType().equalsIgnoreCase(DocType.ITEM.getCode())) {
                List<Item> itemList = getBrowseService().browse(oleSearchForm);
                oleSearchForm.setItemList(itemList);
            } else {
                List<Holdings> holdingsList = getBrowseService().browse(oleSearchForm);
                oleSearchForm.setHoldingsList(holdingsList);
            }
        }
        setBrowsePageNextPrevoiusAndEntriesInfo(oleSearchForm);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=workBenchBrowseClear")
    public ModelAndView workBenchClear(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        List<Integer> pageSizes = documentSearchConfig.getPageSizes();
        if(!pageSizes.isEmpty() || pageSizes.size() > 0) {
            oleSearchForm.setPageSize(pageSizes.get(0));
        }
        oleSearchForm.setBrowseText("");
        if(CollectionUtils.isNotEmpty(oleSearchForm.getSearchResultDisplayRowList())) {
            oleSearchForm.setSearchResultDisplayRowList(null);
        }
        return navigate(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=rowsBrowse")
    public ModelAndView rowsBrowse(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside the browse method");
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        if ("title".equals(oleSearchForm.getBrowseField())) {
            oleSearchForm.setDocType(DocType.BIB.getCode());
            List<SearchResultDisplayRow> searchResultDisplayRowList = getBrowseService().browseOnChange(oleSearchForm);
            oleSearchForm.setSearchResultDisplayRowList(searchResultDisplayRowList);

        } else {

            if (oleSearchForm.getDocType().equalsIgnoreCase(DocType.ITEM.getCode())) {
                List<Item> itemList = getBrowseService().browseOnChange(oleSearchForm);
                oleSearchForm.setItemList(itemList);
            } else {
                List<Holdings> holdingsList = getBrowseService().browseOnChange(oleSearchForm);
                oleSearchForm.setHoldingsList(holdingsList);
            }
        }
        setBrowsePageNextPrevoiusAndEntriesInfo(oleSearchForm);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=previous")
    public ModelAndView previous(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside the browse method");
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        if ("title".equals(oleSearchForm.getBrowseField())) {
            oleSearchForm.setDocType(DocType.BIB.getCode());
            List<SearchResultDisplayRow> searchResultDisplayRowList = getBrowseService().browsePrev(oleSearchForm);
            oleSearchForm.setSearchResultDisplayRowList(searchResultDisplayRowList);

        } else {

            if (oleSearchForm.getDocType().equalsIgnoreCase(DocType.ITEM.getCode())) {
                List<Item> itemList = getBrowseService().browsePrev(oleSearchForm);
                oleSearchForm.setItemList(itemList);
            } else {
                List<Holdings> holdingsList = getBrowseService().browsePrev(oleSearchForm);
                oleSearchForm.setHoldingsList(holdingsList);
            }
        }
        setBrowsePageNextPrevoiusAndEntriesInfo(oleSearchForm);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=next")
    public ModelAndView next(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside the browse method");
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        if ("title".equals(oleSearchForm.getBrowseField())) {
            oleSearchForm.setDocType(DocType.BIB.getCode());
            List<SearchResultDisplayRow> searchResultDisplayRowList = getBrowseService().browseNext(oleSearchForm);
            oleSearchForm.setSearchResultDisplayRowList(searchResultDisplayRowList);

        } else {

            if (oleSearchForm.getDocType().equalsIgnoreCase(DocType.ITEM.getCode())) {
                List<Item> itemList = getBrowseService().browseNext(oleSearchForm);
                oleSearchForm.setItemList(itemList);
            } else {
                List<Holdings> holdingsList = getBrowseService().browseNext(oleSearchForm);
                oleSearchForm.setHoldingsList(holdingsList);
            }
        }
        setBrowsePageNextPrevoiusAndEntriesInfo(oleSearchForm);
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=clear")
    public ModelAndView clear(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the clear method");
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        clearForm(oleSearchForm);
        return super.start(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=back")
    public ModelAndView back(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the clear method");
        return super.back(form, result, request, response);
    }

    @RequestMapping(params = "methodToCall=exportToXml")
    public ModelAndView exportToXml(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
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
        if(oleSearchForm.getDocType().equalsIgnoreCase(DocType.BIB.getCode())) {

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

        if(oleSearchForm.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
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

        if(oleSearchForm.getDocType().equalsIgnoreCase(DocType.ITEM.getCode())) {
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

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addLine")
    public ModelAndView addLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        OLESearchForm oleSearchForm = (OLESearchForm) uifForm;
        String selectedCollectionPath = oleSearchForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for add line action, cannot add new line");
        }

        View view = oleSearchForm.getPostedView();
        view.getViewHelperService().processCollectionAddLine(view, oleSearchForm, selectedCollectionPath);
        SearchCondition searchCondition = oleSearchForm.getSearchParams().getSearchConditions().get(oleSearchForm.getSearchParams().getSearchConditions().size() - 1);
        if(StringUtils.isEmpty(searchCondition.getSearchField().getFieldValue())) {
            oleSearchForm.getSearchParams().getSearchConditions().remove(oleSearchForm.getSearchParams().getSearchConditions().size() -1);
            GlobalVariables.getMessageMap().putErrorForSectionId("SearchConditionsSection", OLEConstants.DESCRIBE_ENTER_SEARCH_TEXT);
            return getUIFModelAndView(oleSearchForm);
        }
        oleSearchForm.getSearchParams().setStartIndex(0);
        return getUIFModelAndView(oleSearchForm);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=deleteLine")
    public ModelAndView deleteLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        OLESearchForm oleSearchForm = (OLESearchForm) uifForm;
        String selectedCollectionPath = oleSearchForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for delete line action, cannot delete line");
        }

        int selectedLineIndex = -1;
        String selectedLine = oleSearchForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX);
        if (StringUtils.isNotBlank(selectedLine)) {
            selectedLineIndex = Integer.parseInt(selectedLine);
        }

        if (selectedLineIndex == -1) {
            throw new RuntimeException("Selected line index was not set for delete line action, cannot delete line");
        }

        View view = oleSearchForm.getPostedView();
        view.getViewHelperService().processCollectionDeleteLine(view, oleSearchForm, selectedCollectionPath,
                selectedLineIndex);
        oleSearchForm.getSearchParams().setStartIndex(0);
        return getUIFModelAndView(oleSearchForm);
    }



    private boolean performBrowse(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.CALL_NUMBER_BROWSE);
    }

    public Set<String> getFacetFields(String docType) {
        Set<String> facetFields = new TreeSet<String>();
        for(DocTypeConfig docTypeConfig : documentSearchConfig.getDocTypeConfigs()) {
            if(docTypeConfig.getName().equalsIgnoreCase(docType)) {
                for( DocFormatConfig docFormatConfig : docTypeConfig.getDocFormatConfigList()) {
                    if(docFormatConfig.getName().equalsIgnoreCase(DocFormat.MARC.getCode())) {
                        for(DocFieldConfig docFieldConfig : docFormatConfig.getDocFieldConfigList()) {
                            if (docFieldConfig.isFacet() && docFieldConfig.getDocType().getName().equalsIgnoreCase(docType)) {
                                facetFields.add(docFieldConfig.getName());
                            }
                        }
                    }
                }
            }
        }
        return facetFields;
    }

    public SearchResultDisplayFields getDisplayFields(OLESearchForm oleSearchForm) {
        SearchResultDisplayFields searchResultDisplayFields = new SearchResultDisplayFields();
        searchResultDisplayFields.buildSearchResultDisplayFields(documentSearchConfig.getDocTypeConfigs(), oleSearchForm.getDocType());
        return searchResultDisplayFields;
    }

    public void setPageNextPreviousAndEntriesInfo(OLESearchForm oleSearchForm) {
      if( oleSearchForm.getSearchResponse() !=null){
          this.totalRecCount = oleSearchForm.getSearchResponse().getTotalRecordCount();
          this.start = oleSearchForm.getSearchResponse().getStartIndex();
      }else{
          this.totalRecCount = 0;
          this.start = 0;
      }

        this.pageSize = oleSearchForm.getPageSize();
        oleSearchForm.setPreviousFlag(getPreviousFlag());
        oleSearchForm.setNextFlag(getNextFlag());
        oleSearchForm.setPageShowEntries(getPageShowEntries());
        oleSearchForm.setTotalRecordCount(this.totalRecCount);
    }


    public void setBrowsePageNextPrevoiusAndEntriesInfo(OLESearchForm oleSearchForm) {
        oleSearchForm.setPreviousFlag(getBrowseService().getPreviosFlag());
        oleSearchForm.setNextFlag(getBrowseService().getNextFlag());
        oleSearchForm.setPageShowEntries(getBrowseService().getPageShowEntries());
    }

    private boolean canExportToRequestXml(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.DESC_WORKBENCH_EXPORT_XML);
    }


    @RequestMapping(params = "methodToCall=addLineField")
    public ModelAndView addLineField(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {

        String selectedCollectionPath = uifForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for add line action, cannot add new line");
        }
        OLESearchForm oleSearchForm = (OLESearchForm) uifForm;
        int index = Integer.parseInt(oleSearchForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if(oleSearchForm.getSearchConditions().get(index).getSearchField().getFieldName().isEmpty()&&
                oleSearchForm.getSearchConditions().get(index).getSearchField().getFieldValue().isEmpty()){
            return getUIFModelAndView(uifForm);
        }
        List<SearchCondition> searchConditions = oleSearchForm.getSearchConditions();
        String previousDocType =oleSearchForm.getSearchConditions().get(index).getSearchField().getDocType();
        index++;
        SearchCondition searchCondition=new SearchCondition();
        searchCondition.setOperator("AND");
        SearchField searchField = new SearchField();
        searchField.setDocType(previousDocType);
        searchField.setFieldName("any");
        searchCondition.setSearchField(searchField);
        searchConditions.add(index,searchCondition);
        setDefaultParameter(oleSearchForm,Integer.toString(index));
        return getUIFModelAndView(uifForm);
    }

    public void searchDocstoreData(OLESearchForm oleSearchForm,HttpServletRequest request) {

        oleSearchForm.getSearchParams().setDocType(oleSearchForm.getDocType());

        boolean isRemoveSearchCondition = false;
        setShowPageSizeEntries(oleSearchForm);
        SearchParams searchParams = oleSearchForm.getSearchParams();
        oleSearchForm.getSearchParams().getSearchConditions().clear();
        searchParams.getSearchConditions().addAll(oleSearchForm.getSearchConditions());
        searchParams.getSearchResultFields().clear();
        if ("true".equals(oleSearchForm.getSortFlag())) {
            searchParams.setPageSize(oleSearchForm.getPageSize());
//            searchParams.setStartIndex(this.start);
            searchParams.getSortConditions().clear();
            if (oleSearchForm.getDocType().equalsIgnoreCase("holdings") && oleSearchForm.getSortField().equalsIgnoreCase("Relations")) {
                searchParams.getSortConditions().add(searchParams.buildSortCondition("isBoundwith", oleSearchForm.getSortOrder()));
                searchParams.getSortConditions().add(searchParams.buildSortCondition("isSeries", oleSearchForm.getSortOrder()));
                searchParams.getSortConditions().add(searchParams.buildSortCondition("isAnalytic", oleSearchForm.getSortOrder()));
            } else if (oleSearchForm.getDocType().equalsIgnoreCase("item") && oleSearchForm.getSortField().equalsIgnoreCase("Relations")) {
                searchParams.getSortConditions().add(searchParams.buildSortCondition("isAnalytic", oleSearchForm.getSortOrder()));
                searchParams.getSortConditions().add(searchParams.buildSortCondition("Title_sort", oleSearchForm.getSortOrder()));
            } else {
                searchParams.getSortConditions().add(searchParams.buildSortCondition(oleSearchForm.getSortField(), oleSearchForm.getSortOrder()));
            }
        } else {
            searchParams.setPageSize(oleSearchForm.getPageSize());
//            searchParams.setStartIndex(this.start);
        }
        int startIndex = searchParams.getStartIndex();
        if(StringUtils.isNotEmpty(oleSearchForm.getPageNumber()) && oleSearchForm.getPageNumber().equalsIgnoreCase("1")){
            searchParams.setStartIndex(0);
        }else{
            searchParams.setStartIndex(startIndex - startIndex % searchParams.getPageSize());
        }
        for (SearchCondition searchCondition : oleSearchForm.getSearchConditions()) {
            if(searchCondition.getSearchField().getFieldName().equalsIgnoreCase(DocstoreConstants.ISBN_SEARCH)){
                String fieldValue= searchCondition.getSearchField().getFieldValue().replaceAll("-","");
                ISBNUtil isbnUtil = new ISBNUtil();
                try {
                    fieldValue = isbnUtil.normalizeISBN(fieldValue);
                } catch (OleException e) {
                    LOG.error("Exception "+e);
                }
                searchCondition.getSearchField().setFieldValue(fieldValue);
                searchCondition.setSearchScope("phrase");
            }
            if(DocType.ITEM.getCode().equals(oleSearchForm.getDocType()) && searchCondition.getSearchField().getFieldName().equalsIgnoreCase(DocstoreConstants.BIB_IDENTIFIER)){
                if(!DocumentUniqueIDPrefix.hasPrefix(searchCondition.getSearchField().getFieldValue())){
                    searchCondition.getSearchField().setFieldValue(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC+"-"+searchCondition.getSearchField().getFieldValue());
                }

            }
        }


        if(CollectionUtils.isEmpty(searchParams.getSearchConditions())) {
            isRemoveSearchCondition = true;
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(oleSearchForm.getDocType(), "", ""), ""));
        }
        request.getSession().setAttribute("searchParams", searchParams);
        if(!oleSearchForm.isMoreFacets()) {
            searchParams.getFacetFields().clear();
            Set<String> facetFields = getFacetFields(oleSearchForm.getDocType());
            searchParams.getFacetFields().addAll(facetFields);
            searchParams.setFacetLimit(documentSearchConfig.getFacetPageSizeShort());
        }
        oleSearchForm.setFacetLimit(documentSearchConfig.getFacetPageSizeShort()-1);
        SearchResponse searchResponse = null;
        oleSearchForm.setSearchResultDisplayFields(getDisplayFields(oleSearchForm));
        searchParams.buildSearchParams(searchParams,oleSearchForm.getDocType());
        if (oleSearchForm instanceof GlobalEditForm) {
            if (((GlobalEditForm) oleSearchForm).isSelectAll()) {
                searchParams.setStartIndex(0);
            }
        }
        float start = System.currentTimeMillis() / 1000;
        try {
            if (searchParams.getSortConditions() != null && searchParams.getSortConditions().size() > 0) {
                searchParams.getSortConditions().clear();
            }
            SortCondition sortCondition = new SortCondition();
            sortCondition.setSortField(oleSearchForm.getSortField());
            sortCondition.setSortOrder(oleSearchForm.getSortOrder());
            searchParams.getSortConditions().add(sortCondition);
            searchResponse = getDocstoreLocalClient().search(searchParams);
            oleSearchForm.setSearchResponse(searchResponse);
        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }



        float end = System.currentTimeMillis()/1000;
        oleSearchForm.setSolrTime(String.valueOf(end-start));
        List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
        List<SearchResultDisplayRow> bibSearchResultDisplayRows=new ArrayList<>();
        List<SearchResultDisplayRow> holdingsSearchResultDisplayRows=new ArrayList<>();
        if(searchResponse!=null){
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                if (DocType.BIB.getCode().equalsIgnoreCase(oleSearchForm.getDocType())) {
                    searchResultDisplayRow.buildBibSearchResultField(searchResult.getSearchResultFields(), eResourceId);
                    bibSearchResultDisplayRows.add(searchResultDisplayRow);
                } else if (DocType.HOLDINGS.getCode().equals(oleSearchForm.getDocType())) {
                    searchResultDisplayRow.buildHoldingSearchResultField(searchResult.getSearchResultFields());
                    holdingsSearchResultDisplayRows.add(searchResultDisplayRow);
                } else if (DocType.EHOLDINGS.getCode().equals(oleSearchForm.getDocType())) {
                    searchResultDisplayRow.buildEHoldingSearchResultField(searchResult.getSearchResultFields());
                    holdingsSearchResultDisplayRows.add(searchResultDisplayRow);
                } else if (DocType.ITEM.getCode().equals(oleSearchForm.getDocType())) {
                    searchResultDisplayRow.buildItemSearchResultField(searchResult.getSearchResultFields());
                }
                searchResultDisplayRows.add(searchResultDisplayRow);
            }
        } else {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
            List<Integer> pageSizes = documentSearchConfig.getPageSizes();
            clearForm(oleSearchForm, pageSizes);

            return;
        }
        oleSearchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
        oleSearchForm.setBibSearchResultDisplayRowList(bibSearchResultDisplayRows);
        oleSearchForm.setHoldingSearchResultDisplayRowList(holdingsSearchResultDisplayRows);
        if (searchResponse != null && searchResponse.getFacetResult() != null) {
            oleSearchForm.setFacetResultFields(searchResponse.getFacetResult().getFacetResultFields());
        }
        if (searchResultDisplayRows.size() == 0) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
            List<Integer> pageSizes = documentSearchConfig.getPageSizes();
            clearForm(oleSearchForm, pageSizes);
        }
        request.getSession().setAttribute("searchResultDisplayRowList", searchResultDisplayRows);
        setPageNextPreviousAndEntriesInfo(oleSearchForm);
        if(isRemoveSearchCondition) {
            oleSearchForm.getSearchParams().getSearchConditions().clear();
        }
//        oleSearchForm.getSearchParams().setFacetPrefix(null);
        if(oleSearchForm.isMoreFacets())  {
            showFacetPreviousNext(oleSearchForm);
        }
        if (oleSearchForm instanceof GlobalEditForm) {
            ((GlobalEditForm) oleSearchForm).setTotalRecords(totalRecCount);
        }

        for (SearchCondition searchCondition : oleSearchForm.getSearchConditions()) {
              if(DocType.ITEM.getCode().equals(oleSearchForm.getDocType()) && searchCondition.getSearchField().getFieldName().equalsIgnoreCase(DocstoreConstants.BIB_IDENTIFIER)){
                if(DocumentUniqueIDPrefix.hasPrefix(searchCondition.getSearchField().getFieldValue())){
                    searchCondition.getSearchField().setFieldValue(DocumentUniqueIDPrefix.getDocumentId(searchCondition.getSearchField().getFieldValue()));
                }
            }
        }
    }

    private void clearForm(OLESearchForm oleSearchForm, List<Integer> pageSizes) {
        if(!pageSizes.isEmpty() || pageSizes.size() > 0) {
            oleSearchForm.setFacetLimit(0);
            oleSearchForm.setTotalRecordCount(0);
            oleSearchForm.setPageShowEntries(null);
            oleSearchForm.setShowPageSize(null);
            oleSearchForm.setShowFieldSort(null);
            oleSearchForm.setBibSearchResultDisplayRowList(null);
            oleSearchForm.setHoldingSearchResultDisplayRowList(null);
            oleSearchForm.setSearchResponse(null);
            oleSearchForm.setFacetResultFields(null);
        }
    }

    private void showFacetPreviousNext(OLESearchForm oleSearchForm) {
        int offset = oleSearchForm.getSearchParams().getFacetOffset();
        int size = oleSearchForm.getSearchParams().getFacetLimit();
        int totalRecordCount = 0;
        if(oleSearchForm.getSearchResponse() != null && oleSearchForm.getSearchResponse().getFacetResult() != null && oleSearchForm.getSearchResponse().getFacetResult().getFacetResultFields() != null && oleSearchForm.getSearchResponse().getFacetResult().getFacetResultFields().get(0) != null) {
            totalRecordCount = oleSearchForm.getSearchResponse().getFacetResult().getFacetResultFields().get(0).getValueCounts().size();
        }
        if(offset - size >= 0) {
            oleSearchForm.setShowMoreFacetPrevious(true);
        }
        else {
            oleSearchForm.setShowMoreFacetPrevious(false);
        }

        if(totalRecordCount >= size) {
            oleSearchForm.setShowMoreFacetNext(true);
        }
        else {
            oleSearchForm.setShowMoreFacetNext(false);
        }
    }

    @RequestMapping(params = "methodToCall=deleteLineField")
    public ModelAndView deleteLineField(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {

        String selectedCollectionPath = uifForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for add line action, cannot add new line");
        }
        OLESearchForm oleSearchForm = (OLESearchForm) uifForm;
        int index = Integer.parseInt(oleSearchForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        List<SearchCondition> searchConditions = oleSearchForm.getSearchConditions();
        if (searchConditions.size() > 1) {
            searchConditions.remove(index);
        }
        return getUIFModelAndView(uifForm);
    }

    @RequestMapping(params = "methodToCall=showBibList")
    public ModelAndView showBibList(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        this.start = 0;
        LOG.debug("Inside the showBibList method");
        OLESearchForm oleSearchForm = (OLESearchForm) uifForm;
        oleSearchForm.setSearchTypeField("OLESearch");
        String listOfBib = request.getParameter("listOfBib");
        String bibList[] = listOfBib.split(",");
        SearchParams searchParams = new SearchParams();
        for(int bibCount = 0;bibCount<bibList.length;bibCount++){
            String uuid = listOfBib.split(",")[bibCount].replaceAll("\\D+","");
            SearchCondition searchCondition = searchParams.buildSearchCondition("",searchParams.buildSearchField("bibliographic","LocalId_search",uuid),"OR");
            searchParams.getSearchConditions().add(searchCondition);
            oleSearchForm.getSearchConditions().add(searchCondition);
        }
        oleSearchForm.setSearchParams(searchParams);
        request.getSession().setAttribute("selectedFacetResults", null);
        if (oleSearchForm.getDocType() == null) {
            oleSearchForm.setDocType(DocType.BIB.getCode());
        }
        if(StringUtils.isEmpty(oleSearchForm.getSearchType())) {
            oleSearchForm.setSearchType("search");
        }
        if(StringUtils.isEmpty(oleSearchForm.getBrowseField())) {
            oleSearchForm.setBrowseField("title");
        }
        oleSearchForm.setBrowseText(null);
        oleSearchForm.setShowRequestXml(false);
        oleSearchForm.setHoldingsList(null);
        oleSearchForm.setItemList(null);
        oleSearchForm.setSearchResultDisplayRowList(null);
        oleSearchForm.setCallNumberBrowseText(null);
        oleSearchForm.setLocation(null);
        GlobalVariables.getMessageMap().clearErrorMessages();

        if (oleSearchForm.getDocType().equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
            boolean hasLinkPermission = canLinkBibForRequisition(GlobalVariables.getUserSession().getPrincipalId());
            if (!hasLinkPermission) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
                return search(oleSearchForm, result, request, response);
            }
        }
        return search(oleSearchForm,result,request,response);
    }

    private void clearForm(OLESearchForm oleSearchForm) {

        List<Integer> pageSizes = documentSearchConfig.getPageSizes();
        if(!pageSizes.isEmpty() || pageSizes.size() > 0) {
            oleSearchForm.setPageSize(pageSizes.get(0));
        }
        oleSearchForm.setPreviousFlag(false);
        oleSearchForm.setNextFlag(false);
        oleSearchForm.setBrowseText(null);
        oleSearchForm.setShowRequestXml(false);
        oleSearchForm.setHoldingsList(null);
        oleSearchForm.setItemList(null);
        oleSearchForm.setSearchResultDisplayRowList(null);
        oleSearchForm.setCallNumberBrowseText(null);
        oleSearchForm.setLocation(null);
        oleSearchForm.setClassificationScheme("LCC");
        oleSearchForm.setFacetLimit(0);
        oleSearchForm.setTotalRecordCount(0);
        oleSearchForm.setSearchParams(new SearchParams());
        oleSearchForm.setPageShowEntries(null);
        oleSearchForm.setShowPageSize(null);
        oleSearchForm.setShowFieldSort(null);
        oleSearchForm.setBibSearchResultDisplayRowList(null);
        oleSearchForm.setHoldingSearchResultDisplayRowList(null);
        oleSearchForm.setSearchResponse(null);
        oleSearchForm.setFacetResultFields(null);

        oleSearchForm.setWorkBibDocumentList(null);
        oleSearchForm.setWorkHoldingsDocumentList(null);
        oleSearchForm.setWorkItemDocumentList(null);
        oleSearchForm.setWorkEHoldingsDocumentList(null);
        oleSearchForm.setSearchTypeField("OLESearch");
        oleSearchForm.setSelectAllRecords(false);

        if (oleSearchForm.getSearchParams() != null) {
            for (SearchCondition searchCondition : oleSearchForm.getSearchConditions()) {
                if (searchCondition.getSearchField() != null) {
                    searchCondition.getSearchField().setFieldName("");
                    searchCondition.getSearchField().setFieldValue("");
                }
            }

            if (oleSearchForm.getSearchParams().getFacetFields() != null) {
                oleSearchForm.getSearchParams().getFacetFields().clear();
            }
            if (oleSearchForm.getSearchParams().getFacetConditions() != null) {
                oleSearchForm.getSearchParams().getFacetConditions().clear();
            }
            oleSearchForm.getSearchParams().getSearchResultFields().clear();

        }
        if (oleSearchForm.getSearchResultDisplayRowList() != null && oleSearchForm.getSearchResultDisplayRowList().size() > 0) {
            oleSearchForm.getSearchResultDisplayRowList().clear();
        }

        if (oleSearchForm.getFacetResultFields() != null) {
            oleSearchForm.getFacetResultFields().clear();
        }

    }

    @RequestMapping(params = "methodToCall=getHoldingsList")
    public ModelAndView getHoldingsList(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        oleSearchForm.setErrorMessage(null);
        oleSearchForm.setHoldingSearchResultDisplayRowList(null);
        oleSearchForm.seteHoldingsFlag("false");
        List<SearchResultDisplayRow> searchResultDisplayRowList = new ArrayList<>();

        if (oleSearchForm.getSearchResultDisplayRowList() != null && oleSearchForm.getSearchResultDisplayRowList().size() > 0) {
            for (SearchResultDisplayRow searchResultDisplay : oleSearchForm.getSearchResultDisplayRowList()) {
                if (searchResultDisplay.isSelect()) {
                    BibTree bibTree = getDocstoreLocalClient().retrieveBibTree(searchResultDisplay.getLocalId());
                    List<HoldingsTree> holdingsTreeList = bibTree.getHoldingsTrees();
                    if (holdingsTreeList.size() > 0) {
                        oleSearchForm.setHoldingsFlag("true");
                        for (HoldingsTree holdingsTree : holdingsTreeList) {
                            if (holdingsTree.getHoldings().getHoldingsType().equalsIgnoreCase("print")) {
                                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                                searchResultDisplayRow.setHoldingsIdentifier(holdingsTree.getHoldings().getId());
                                searchResultDisplayRow.setTitle(holdingsTree.getHoldings().getBib().getTitle());
                                searchResultDisplayRow.setBibIdentifier(holdingsTree.getHoldings().getBib().getId());
                                searchResultDisplayRow.setCallNumber(holdingsTree.getHoldings().getCallNumber());
                                searchResultDisplayRow.setLocalId(DocumentUniqueIDPrefix.getDocumentId(holdingsTree.getHoldings().getId()));
                                searchResultDisplayRow.setLocationName(holdingsTree.getHoldings().getLocationName());
                                searchResultDisplayRow.setInstanceIdentifier(holdingsTree.getHoldings().getId());
                                searchResultDisplayRowList.add(searchResultDisplayRow);
                            }
                        }
                    }
                }
            }
        }
        if (searchResultDisplayRowList.size() == 0) {
            oleSearchForm.setErrorMessage("selected bib doesnt have Holdings");
        }
        oleSearchForm.setHoldingSearchResultDisplayRowList(searchResultDisplayRowList);
        oleSearchForm.setWorkEHoldingsDocumentList(null);
        return super.navigate(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=getEHoldingsList")
    public ModelAndView getEHoldingsList(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        oleSearchForm.setErrorMessage(null);
        oleSearchForm.setHoldingsFlag("false");
        oleSearchForm.setHoldingSearchResultDisplayRowList(null);
        List<SearchResultDisplayRow> searchResultDisplayRowArrayList = new ArrayList<>();
        if (oleSearchForm.getSearchResultDisplayRowList() != null && oleSearchForm.getSearchResultDisplayRowList().size() > 0) {
            for (SearchResultDisplayRow searchResultDisplay : oleSearchForm.getSearchResultDisplayRowList()) {
                if (searchResultDisplay.isSelect()) {
                    BibTree bibTree = getDocstoreLocalClient().retrieveBibTree(searchResultDisplay.getLocalId());
                    if (bibTree.getHoldingsTrees().size() > 0) {
                        oleSearchForm.seteHoldingsFlag("true");
                        for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                            if (holdingsTree.getHoldings().getHoldingsType().equalsIgnoreCase("electronic")) {
                                OleHoldings oleHoldings = new OleHoldings();
                                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                                HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
                                oleHoldings = (OleHoldings) holdingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
                                searchResultDisplayRow.setAccessStatus(oleHoldings.getAccessStatus());
                                searchResultDisplayRow.setPlatForm(oleHoldings.getPlatform() != null ? oleHoldings.getPlatform().getPlatformName() : null);
                                searchResultDisplayRow.setImprint(oleHoldings.getImprint());
                                searchResultDisplayRow.setTitle(holdingsTree.getHoldings().getBib().getTitle());
                                searchResultDisplayRow.setStatisticalCode(oleHoldings.getStatisticalSearchingCode() != null ? oleHoldings.getStatisticalSearchingCode().getCodeValue() : null);
                                searchResultDisplayRow.setLocationName(holdingsTree.getHoldings().getLocationName());
                                searchResultDisplayRow.setBibIdentifier(holdingsTree.getHoldings().getBib().getId());
                                searchResultDisplayRow.setInstanceIdentifier(holdingsTree.getHoldings().getId());
                                searchResultDisplayRow.setHoldingsIdentifier(holdingsTree.getHoldings().getId());
                                searchResultDisplayRow.setLocalId(DocumentUniqueIDPrefix.getDocumentId(holdingsTree.getHoldings().getId()));
                                searchResultDisplayRowArrayList.add(searchResultDisplayRow);
                            }
                        }
                    }
                }
            }
        }
        if (searchResultDisplayRowArrayList.size() == 0) {
            oleSearchForm.setErrorMessage("selected bib doesnt have EHoldings");
        }
        oleSearchForm.setHoldingSearchResultDisplayRowList(searchResultDisplayRowArrayList);
        oleSearchForm.setWorkHoldingsDocumentList(null);
        return navigate(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=linkToBib")
    public ModelAndView linkToBib(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        List<SearchResultDisplayRow> searchResultDisplayRowList = oleSearchForm.getHoldingSearchResultDisplayRowList();
        oleSearchForm.setSuccessMessage(null);
        if (searchResultDisplayRowList != null && searchResultDisplayRowList.size() > 0) {
            for (SearchResultDisplayRow searchResultDisplayRow : searchResultDisplayRowList) {
                if (searchResultDisplayRow.isSelect()) {
                    processNewHoldingsResponse(searchResultDisplayRow, oleSearchForm.getTokenId());
                    Holdings holdings =getDocstoreLocalClient().retrieveHoldings(searchResultDisplayRow.getHoldingsIdentifier());
                    if (holdings.getHoldingsType().equalsIgnoreCase("electronic")) {
                        saveRecordToDocstore(searchResultDisplayRow, eResourceId);
                    }
                    oleSearchForm.setSuccessMessage("");
                    break;
                } else {
                    oleSearchForm.setSuccessMessage(OLEConstants.HOLDINGS_ERROR_MESSAGE);
                }
            }
        }
        if (eResourceId != null && !eResourceId.isEmpty()) {
            Map<String, String> tempId = new HashMap<String, String>();
            tempId.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, eResourceId);
            OLEEResourceRecordDocument tempDocument = (OLEEResourceRecordDocument)getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, tempId);
            try {
                Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
                tempDocument.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(tempDocument.getDocumentNumber(), principalPerson));
                if (tempDocument != null) {
                    try {
                        tempDocument.setSelectInstance(OLEConstants.OLEEResourceRecord.LINK_EXIST_INSTANCE);
                        tempDocument.seteInstanceFlag(true);
                        getOleEResourceSearchService().getNewInstance(tempDocument, tempDocument.getDocumentNumber());
                        getBusinessObjectService().save(tempDocument.getOleERSInstances());
                        getDocumentService().updateDocument(tempDocument);
                    } catch (Exception e) {
                        throw new RiceRuntimeException(
                                "Exception trying to save document: " + tempDocument
                                        .getDocumentNumber(), e);
                    }
                }
            } catch (Exception e) {
                throw new RiceRuntimeException(
                        "Exception trying to save document: " + tempDocument
                                .getDocumentNumber(), e);
            }
        }
        return getUIFModelAndView(oleSearchForm);
    }

    private void processNewHoldingsResponse(SearchResultDisplayRow searchResultDisplayRow, String tokenId) throws Exception {
        OLEEditorResponse oleEditorResponse = new OLEEditorResponse();
        oleEditorResponse.setLinkedInstanceId(searchResultDisplayRow.getHoldingsIdentifier());
        oleEditorResponse.setTokenId(tokenId);
        HashMap<String, OLEEditorResponse> oleEditorResponseMap = new HashMap<String, OLEEditorResponse>();
        oleEditorResponseMap.put(tokenId, oleEditorResponse);
        OleDocstoreResponse.getInstance().setEditorResponse(oleEditorResponseMap);
    }

    private void saveRecordToDocstore(SearchResultDisplayRow searchResultDisplayRow, String eResourceId) throws Exception {
        Holdings eHoldings = new org.kuali.ole.docstore.common.document.EHoldings();
        OleHoldings oleHoldings = new OleHoldings();
        eHoldings = getDocstoreLocalClient().retrieveHoldings(searchResultDisplayRow.getHoldingsIdentifier());
        oleHoldings = new HoldingOlemlRecordProcessor().fromXML(eHoldings.getContent());
        oleHoldings.setEResourceId(eResourceId);
        eHoldings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
        getDocstoreLocalClient().updateHoldings(eHoldings);
    }

    @RequestMapping(params = "methodToCall=filterBibs")
    public ModelAndView filterBibs(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        OLESearchForm oleSearchForm = (OLESearchForm) uifForm;
        oleSearchForm.setDocType(DocType.BIB.getCode());
        oleSearchForm.setShowTime(true);
        oleSearchForm.setSearchTypeField("OLESearch");
        String author = request.getParameter("author");
        String isbn = request.getParameter("isbn");
        String oclcNumber = request.getParameter("oclcNumber");
        SearchParams searchParams = new SearchParams();
        SearchCondition searchCondition = new SearchCondition();
        SearchField searchField = new SearchField();
        if (StringUtils.isNotBlank(author)) {
            searchField.setFieldName("Author_search");
            searchField.setFieldValue(author);
            request.getSession().setAttribute("author", null);
        } else if (StringUtils.isNotBlank(isbn)) {
            searchField.setFieldName("ISBN_search");
            searchField.setFieldValue(isbn);
            request.getSession().setAttribute("isbn", null);
        } else if (StringUtils.isNotBlank(oclcNumber)) {
            searchField.setFieldName("mdf_035a");
            searchField.setFieldValue(oclcNumber);
            request.getSession().setAttribute("oclcNumber", null);
        }
        searchCondition.setOperator("AND");
        searchCondition.setSearchScope("AND");
        searchCondition.setSearchField(searchField);
        searchParams.getSearchConditions().add(searchCondition);
        oleSearchForm.getSearchConditions().add(searchCondition);
        oleSearchForm.setSearchParams(searchParams);
        if (StringUtils.isEmpty(oleSearchForm.getSearchType())) {
            oleSearchForm.setSearchType("search");
        }
        if (StringUtils.isEmpty(oleSearchForm.getBrowseField())) {
            oleSearchForm.setBrowseField("title");
        }
        oleSearchForm.setBrowseText(null);
        oleSearchForm.setShowRequestXml(false);
        oleSearchForm.setHoldingsList(null);
        oleSearchForm.setItemList(null);
        oleSearchForm.setSearchResultDisplayRowList(null);
        oleSearchForm.setCallNumberBrowseText(null);
        oleSearchForm.setLocation(null);
        GlobalVariables.getMessageMap().clearErrorMessages();
        oleSearchForm.setSearchResultDisplayFields(new SearchResultDisplayFields());
        return search(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=openAll")
    public ModelAndView openAll(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        OLESearchForm oleSearchForm = (OLESearchForm) form;
        oleSearchForm.getSearchParams().setFacetOffset(0);
        if("browse".equalsIgnoreCase(oleSearchForm.getSearchType())){
            browseDocstoreForLocalIds(oleSearchForm, request);
        }else{
            searchDocstoreForLocalIds(oleSearchForm, request);
        }
        return super.navigate(oleSearchForm, result, request, response);
    }

    private void browseDocstoreForLocalIds(OLESearchForm oleSearchForm, HttpServletRequest request) {
        int pageSize = oleSearchForm.getPageSize();
        oleSearchForm.setPageSize(40);
        StringBuilder ids = new StringBuilder();
        if ("title".equals(oleSearchForm.getBrowseField())) {
            oleSearchForm.setDocType(DocType.BIB.getCode());
            List<SearchResultDisplayRow> searchResultDisplayRowList = getBrowseService().browse(oleSearchForm);
            for( SearchResultDisplayRow searchResultDisplayRow:searchResultDisplayRowList){
                ids.append("," + searchResultDisplayRow.getLocalId() +"#"+ searchResultDisplayRow.getBibIdentifier());
            }
        } else {
            String location = getBrowseService().validateLocation(oleSearchForm.getLocation());
            oleSearchForm.setLocation(location);
            if (oleSearchForm.getDocType().equalsIgnoreCase(DocType.ITEM.getCode())) {
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

    public void searchDocstoreForLocalIds(OLESearchForm oleSearchForm,HttpServletRequest request) {
        setShowPageSizeEntries(oleSearchForm);
        SearchParams searchParams = oleSearchForm.getSearchParams();
        oleSearchForm.getSearchConditions().clear();
        oleSearchForm.getSearchConditions().addAll(searchParams.getSearchConditions());
        searchParams.getSearchResultFields().clear();
        searchParams.setPageSize(totalRecCount);
        searchParams.setStartIndex(0);
        for (SearchCondition searchCondition : oleSearchForm.getSearchConditions()) {
            if(searchCondition.getSearchField().getFieldName().equalsIgnoreCase(DocstoreConstants.ISBN_SEARCH)){
                String fieldValue= searchCondition.getSearchField().getFieldValue().replaceAll("-","");
                ISBNUtil isbnUtil = new ISBNUtil();
                try {
                    fieldValue = isbnUtil.normalizeISBN(fieldValue);
                } catch (OleException e) {
                    LOG.error("Exception "+e);
                }
                searchCondition.getSearchField().setFieldValue(fieldValue);
                searchCondition.setSearchScope("phrase");
            }
            if(DocType.ITEM.getCode().equals(oleSearchForm.getDocType()) && searchCondition.getSearchField().getFieldName().equalsIgnoreCase(DocstoreConstants.BIB_IDENTIFIER)){
                if(!DocumentUniqueIDPrefix.hasPrefix(searchCondition.getSearchField().getFieldValue())){
                    searchCondition.getSearchField().setFieldValue(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC+"-"+searchCondition.getSearchField().getFieldValue());
                }
            }
        }

        if(CollectionUtils.isEmpty(searchParams.getSearchConditions())) {
            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(oleSearchForm.getDocType(), "", ""), ""));
        }
        request.getSession().setAttribute("searchParams", searchParams);

        SearchResponse searchResponse = null;
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(oleSearchForm.getDocType(), "LocalId_display"));
        if(oleSearchForm.getDocType().equalsIgnoreCase("bibliographic")){
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(oleSearchForm.getDocType(), Bib.ID));
        }else{
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(oleSearchForm.getDocType(), Bib.BIBIDENTIFIER));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(oleSearchForm.getDocType(), Holdings.HOLDINGSIDENTIFIER));
        }

        if (oleSearchForm instanceof GlobalEditForm) {
            if (((GlobalEditForm) oleSearchForm).isSelectAll()) {
                searchParams.setStartIndex(0);
            }
        }
        try {
            if(oleSearchForm.getDocType().equalsIgnoreCase(DocType.BIB.getCode()) && searchParams.getSortConditions() != null && searchParams.getSortConditions().size() == 0) {
                SortCondition sortCondition = new SortCondition();
                sortCondition.setSortField("Title_sort");
                sortCondition.setSortOrder("asc");
                searchParams.getSortConditions().add(sortCondition);
            }
            searchResponse = getDocstoreLocalClient().search(searchParams);
            StringBuilder ids = new StringBuilder();
            for( SearchResult searchResult:searchResponse.getSearchResults()){
                if(oleSearchForm.getDocType().equalsIgnoreCase("bibliographic")){
                    ids.append("," + searchResult.getSearchResultFields().get(0).getFieldValue() +"#"+ searchResult.getSearchResultFields().get(1).getFieldValue());
                }else{
                    ids.append("," + searchResult.getSearchResultFields().get(0).getFieldValue() +"#"+ searchResult.getSearchResultFields().get(1).getFieldValue()+"#"+ searchResult.getSearchResultFields().get(2).getFieldValue());
                }

            }
            oleSearchForm.setIdsToBeOpened(ids.toString().replaceFirst(",",""));

        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }

    }

    public String getParameter(String applicationId, String namespace, String componentId, String parameterName) {
        ParameterKey parameterKey = ParameterKey.create(applicationId, namespace, componentId, parameterName);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);

        return parameter!=null?parameter.getValue():null;
    }

    public void setControlFields(BibTree bibTree) {
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords bibRecords = bibMarcRecordProcessor.fromXML(bibTree.getBib().getContent());
        for (BibMarcRecord bibMarcRecord : bibRecords.getRecords()) {
            for (ControlField controlField : bibMarcRecord.getControlFields()) {
                controlField.setValue(controlField.getValue().replaceAll(" ", "#"));
            }
        }
        bibTree.getBib().setContent(bibMarcRecordProcessor.toXml(bibRecords));
    }



}