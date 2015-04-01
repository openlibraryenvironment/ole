package org.kuali.ole.describe.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleWorkBibDocument;
import org.kuali.ole.describe.bo.OleWorkHoldingsDocument;
import org.kuali.ole.describe.bo.SearchResultDisplayRow;
import org.kuali.ole.describe.form.WorkbenchForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OleDocstoreResponse;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: PP7788
 * Date: 11/21/12
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */


@Controller
@RequestMapping(value = "/describeworkbenchcontroller")
public class WorkbenchController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(WorkbenchController.class);
    private OLEEResourceSearchService oleEResourceSearchService;
    private DocumentService documentService;
    private int totalRecCount;
    private int start;
    private int pageSize;
    private List<OleWorkBibDocument> finalDocumentList = new ArrayList<>();
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }


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

    public boolean getWorkbenchPreviousFlag() {
        if (this.start == 0)
            return false;
        return true;
    }

    public boolean getWorkbenchNextFlag() {
        if (this.start + this.pageSize < this.totalRecCount)
            return true;
        return false;
    }

    public String getWorkbenchPageShowEntries() {
        return "Showing " + ((this.start == 0) ? 1 : this.start + 1) + " to "
                + (((this.start + this.pageSize) > this.totalRecCount) ? this.totalRecCount : (this.start + this.pageSize))
                + " of " + this.totalRecCount + " entries";
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

    boolean hasSearchPermission = false;
    private String eResourceId;
    private String tokenId;

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        return new WorkbenchForm();
    }

    /**
     * This method converts UifFormBase to WorkbenchForm
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
        LOG.debug("Inside the workbenchForm start method");
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        workbenchForm.setWorkBibDocumentList(null);
        workbenchForm.setWorkHoldingsDocumentList(null);
        workbenchForm.setWorkItemDocumentList(null);
        workbenchForm.setWorkEHoldingsDocumentList(null);
        workbenchForm.setShowRequestXML(false);
        if (request.getParameter(OLEConstants.E_RESOURCE_ID) != null) {
            eResourceId = request.getParameter(OLEConstants.E_RESOURCE_ID);
        }
        if (request.getParameter(OLEConstants.TOKEN_ID) != null) {
            tokenId = request.getParameter(OLEConstants.TOKEN_ID);
        }
        /*if (workbenchForm.getSearchParams().getSearchConditions().get(0).getSearchField().getDocType() == null) {
            workbenchForm.getSearchParams().getSearchConditions().get(0).getSearchField().setDocType(DocType.BIB.getDescription());
        }*/
        if (workbenchForm.getDocType() == null || workbenchForm.getSearchParams() == null || workbenchForm.getSearchParams().getSearchConditions().size() == 0) {
            //workbenchForm.getSearchParams().getSearchConditions().add(workbenchForm.getSearchParams().buildSearchCondition("", workbenchForm.getSearchParams().buildSearchField(DocType.BIB.getDescription(), "", ""), ""));
            // workbenchForm.getSearchParams().getSearchConditions().add(workbenchForm.getSearchParams().buildSearchCondition("", workbenchForm.getSearchParams().buildSearchField(DocType.BIB.getDescription(), "", ""), ""));
            workbenchForm.setDocType("bibliographic");


        }

        String eInstance = request.getParameter(OLEConstants.E_INSTANCE);
        if (eInstance != null && eInstance.equalsIgnoreCase(OLEConstants.LINK_EXISTING_INSTANCE)) {
            //workbenchForm.getSearchParams().setDocType(DocType.HOLDINGS.getDescription());
            workbenchForm.setLinkExistingInstance(eInstance);
        }
        //String docType1 = workbenchForm.getDocType();
        String docType = "";
        if(null != workbenchForm.getSearchParams().getSearchConditions() && null != workbenchForm.getSearchParams().getSearchConditions().get(0).getSearchField()){
            docType = workbenchForm.getSearchParams().getSearchConditions().get(0).getSearchField().getDocType();
        }
    /*    if ("".equals(docType)) {
            docType = workbenchForm.getDocType();
        } else if (!"".equals(docType) && docType!=null && docType1!=null && docType1.equalsIgnoreCase(docType)) {

        } else {
            workbenchForm.getSearchParams().getSearchConditions().get(0).getSearchField().setFieldName(null);
            workbenchForm.getSearchParams().getSearchConditions().get(0).getSearchField().setFieldValue(null);
        }*/
        //String docType = workbenchForm.getSearchParams().getSearchConditions().add(workbenchForm.getSearchParams().buildSearchCondition(workbenchForm.getSearchParams().buildSearchField(DocType.BIB.getDescription());
        //String docType = workbenchForm.getSearchParams().getSearchConditions().add(workbenchForm.getSearchParams().buildSearchCondition("",workbenchForm.getSearchParams().buildSearchField(DocType.BIB.getDescription(),"",""),""));
        boolean hasSearchPermission = canSearch(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasSearchPermission && docType.equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
            boolean hasLinkPermission = canLinkBibForRequisition(GlobalVariables.getUserSession().getPrincipalId());
            /*boolean hasPermission = canSearchBib(GlobalVariables.getUserSession().getPrincipalId());*/
            if (!hasLinkPermission) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
                return super.navigate(workbenchForm, result, request, response);
            }
        /*} else if (!hasSearchPermission && docType.equalsIgnoreCase(OLEConstants.ITEM_DOC_TYPE)) {
            boolean hasPermission = canSearchItem(GlobalVariables.getUserSession().getPrincipalId());
            if (!hasPermission) {
                workbenchForm.setMessage("<font size='4' color='red'>" + OLEConstants.SEARCH_AUTHORIZATION_ERROR + "</font>");
                return super.navigate(workbenchForm, result, request, response);
            }*/
        } else if (!hasSearchPermission) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
            return super.navigate(workbenchForm, result, request, response);
        }
        workbenchForm.setWorkBibDocumentList(null);
        workbenchForm.setWorkHoldingsDocumentList(null);
        workbenchForm.setWorkItemDocumentList(null);
        workbenchForm.setWorkEHoldingsDocumentList(null);
        workbenchForm.setShowRequestXML(false);
        workbenchForm.setMessage(null);
        workbenchForm.setSearchResultDisplayRowList(null);
        workbenchForm.setBibSearchResultDisplayRowList(null);
        workbenchForm.setHoldingSearchResultDisplayRowList(null);
        GlobalVariables.getMessageMap().clearErrorMessages();
        return super.navigate(workbenchForm, result, request, response);
    }

    /**
     * Used for Test-case
     *
     * @param result
     * @param request
     * @param response
     * @param workbenchForm
     * @return ModelAndView
     */
    protected ModelAndView callSuper(BindingResult result, HttpServletRequest request, HttpServletResponse response, WorkbenchForm workbenchForm) {
        return super.navigate(workbenchForm, result, request, response);
    }

    /*private boolean canSearchBib(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.OlePatron.PATRON_NAMESPACE, OLEConstants.SEARCH_BIB);
    }

    private boolean canSearchItem(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.OlePatron.PATRON_NAMESPACE, OLEConstants.SEARCH_ITEM);
    }*/

    private boolean canSearch(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.DESC_WORKBENCH_SEARCH);
    }

    private boolean canLinkBibForRequisition(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.SELECT_NMSPC, OLEConstants.LINK_EXISTING_BIB);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.info("*** WorkbenchController - Inside Search Method ***");
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        workbenchForm.setMessage(null);
        SearchResponse searchResponse = null;
        //String docType = workbenchForm.getSearchParams().getSearchConditions().get(0).getSearchField().getDocType();
        String docType = workbenchForm.getDocType();
        boolean hasSearchPermission = canSearch(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasSearchPermission && docType.equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
            boolean hasLinkPermission = canLinkBibForRequisition(GlobalVariables.getUserSession().getPrincipalId());
            if (!hasLinkPermission) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
                return super.navigate(workbenchForm, result, request, response);
            }
        } else if (!hasSearchPermission) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
            return super.navigate(workbenchForm, result, request, response);
        }
        workbenchForm.setShowRequestXML(false);
        try {
            SearchParams searchParams = workbenchForm.getSearchParams();
            searchParams.getSearchResultFields().clear();
            // List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
            if ("true".equals(workbenchForm.getSortFlag())) {
                searchParams.setPageSize(workbenchForm.getPageSize());
                searchParams.setStartIndex(this.start);
                searchParams.getSortConditions().clear();
                searchParams.getSortConditions().add(searchParams.buildSortCondition(workbenchForm.getSortField(), workbenchForm.getSortOrder()));
                //searchParams.setSortField(workbenchForm.getSortField());
                //searchParams.setSortOrder(workbenchForm.getSortOrder());
            } else {
                searchParams.setPageSize(workbenchForm.getPageSize());
                searchParams.setStartIndex(workbenchForm.getStart());
            }
            if (DocType.BIB.getDescription().equalsIgnoreCase(docType)) {
                List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "LocalId_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title_sort"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Author_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "PublicationDate_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "DocFormat"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "bibIdentifier"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "id"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "staffOnlyFlag"));
                searchParams.getSearchConditions().get(0).getSearchField().setDocType(docType);
                searchParams.getSearchConditions().get(1).getSearchField().setDocType(docType);
                LOG.info("*** WorkbenchController - Inside Search Method - search Bibs ***");
                workbenchForm.setShowExport(true);
                workbenchForm.setLinkToERSFlag(true);
                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                workbenchForm.setSearchResponse(searchResponse);
                LOG.info("*** WorkbenchController - Inside Search Method - search Bibs - bibList size *** - " + searchResponse.getPageSize());
                if (searchResponse.getSearchResults() == null) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
                    workbenchForm.setWorkBibDocumentList(null);
                    workbenchForm.setWorkHoldingsDocumentList(null);
                    workbenchForm.setWorkItemDocumentList(null);
                    workbenchForm.setWorkEHoldingsDocumentList(null);
                    return super.navigate(workbenchForm, result, request, response);
                }

                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        // if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode())) {
                        if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                            searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("Title_sort")) {
                            searchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("Author_display")) {
                            searchResultDisplayRow.setAuthor(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("PublicationDate_display")) {
                            searchResultDisplayRow.setPublicationDate(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
                            searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                            searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                            searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("staffOnlyFlag")) {
                            searchResultDisplayRow.setStaffOnly(searchResultField.getFieldValue());
                        }
                        if (eResourceId != null) {
                            searchResultDisplayRow.setOleERSIdentifier(eResourceId);
                        }
                        if (tokenId != null) {
                            searchResultDisplayRow.setTokenId(tokenId);
                        }
                        // }
                    }
                    searchResultDisplayRows.add(searchResultDisplayRow);
                }
                workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
                workbenchForm.setBibSearchResultDisplayRowList(searchResultDisplayRows);
                if (searchResultDisplayRows.size() == 0) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
                }
                workbenchForm.setWorkHoldingsDocumentList(null);
                workbenchForm.setWorkItemDocumentList(null);
                workbenchForm.setWorkEHoldingsDocumentList(null);
                request.getSession().setAttribute("searchResultDisplayRowList", searchResultDisplayRows);
                setPageNextPreviousAndEntriesInfo(workbenchForm);
                //   request.getSession().setAttribute("oleWorkBibDocumentList", searchResultDisplayRowList);
            }
            if (DocType.HOLDINGS.getCode().equalsIgnoreCase(docType)) {
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "LocalId_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title_sort"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "Location_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "CallNumber_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "DocFormat"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "bibIdentifier"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "itemIdentifier"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "holdingsIdentifier"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "Location_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "staffOnlyFlag"));
                List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
                LOG.info("*** WorkbenchController - Inside Search Method - search Holdings ***");
                workbenchForm.setShowExport(true);
                searchParams.getSearchConditions().get(0).getSearchField().setDocType(docType);
                searchParams.getSearchConditions().get(1).getSearchField().setDocType(docType);
                List<OleWorkHoldingsDocument> oleWorkHoldingsDocuments = new ArrayList<>();
                LOG.info("*** WorkbenchController - Inside Search Method - search Holdings - searchParams *** - " + searchParams.toString());

                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                workbenchForm.setSearchResponse(searchResponse);
                LOG.info("*** WorkbenchController - Inside Search Method - search Holdings - holdingList size *** - " + searchResponse.getPageSize());
                if (searchResponse == null) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
                    workbenchForm.setWorkBibDocumentList(null);
                    workbenchForm.setWorkHoldingsDocumentList(null);
                    workbenchForm.setWorkItemDocumentList(null);
                    workbenchForm.setWorkEHoldingsDocumentList(null);
                    return super.navigate(workbenchForm, result, request, response);
                }
                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {

                        if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                            searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("Title_sort")) {
                            searchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                            searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                            searchResultDisplayRow.setCallNumber(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
                            searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                            searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                            searchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("itemIdentifier")) {
                            searchResultDisplayRow.setItemIdentifier(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                            searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("staffOnlyFlag")) {
                            searchResultDisplayRow.setStaffOnly(searchResultField.getFieldValue());
                        }

                    }
                    searchResultDisplayRows.add(searchResultDisplayRow);
                }
                workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
                workbenchForm.setHoldingSearchResultDisplayRowList(searchResultDisplayRows);
                if (searchResultDisplayRows.size() == 0) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
                }
                workbenchForm.setWorkBibDocumentList(null);
                workbenchForm.setWorkItemDocumentList(null);
                workbenchForm.setWorkEHoldingsDocumentList(null);
                setPageNextPreviousAndEntriesInfo(workbenchForm);
            }
            if (DocType.ITEM.getCode().equalsIgnoreCase(docType)) {
                List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
                LOG.info("*** WorkbenchController - Inside Search Method - search Items ***");
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "LocalId_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title_sort"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Location_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CallNumber_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "DocFormat"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "bibIdentifier"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "holdingsIdentifier"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "id"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemBarcode_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Location_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "staffOnlyFlag"));
                searchParams.getSearchConditions().get(0).getSearchField().setDocType(docType);
                searchParams.getSearchConditions().get(1).getSearchField().setDocType(docType);
                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                workbenchForm.setSearchResponse(searchResponse);
                LOG.info("*** WorkbenchController - Inside Search Method - search Items - itemList size *** - " + searchResponse.getPageSize());
                if (searchResponse == null) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
                    workbenchForm.setWorkBibDocumentList(null);
                    workbenchForm.setWorkHoldingsDocumentList(null);
                    workbenchForm.setWorkItemDocumentList(null);
                    workbenchForm.setWorkEHoldingsDocumentList(null);
                    return super.navigate(workbenchForm, result, request, response);
                }

                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();

                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {

                        if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                            searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("Title_sort")) {
                            searchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("locationName")) {
                            searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                            searchResultDisplayRow.setCallNumber(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("BarcodeARSL_display")) {
                            searchResultDisplayRow.setBarcode(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("docFormat")) {
                            searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                            searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                            searchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                            searchResultDisplayRow.setItemIdentifier(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("ItemBarcode_display")) {
                            searchResultDisplayRow.setBarcode(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                            searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                        }

                        if (searchResultField.getFieldName().equalsIgnoreCase("staffOnlyFlag")) {
                            searchResultDisplayRow.setStaffOnly(searchResultField.getFieldValue());
                        }
                    }
                    searchResultDisplayRows.add(searchResultDisplayRow);
                }

                workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
                if (searchResultDisplayRows.size() == 0) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
                }
                workbenchForm.setWorkBibDocumentList(null);
                workbenchForm.setWorkHoldingsDocumentList(null);
                workbenchForm.setWorkEHoldingsDocumentList(null);
                setPageNextPreviousAndEntriesInfo(workbenchForm);
            }
            if (DocType.EHOLDINGS.getCode().equalsIgnoreCase(docType)) {
                List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
                LOG.info("***** Inside Search Method - search EHoldings *****");
                workbenchForm.setShowExport(true);
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "LocalId_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "AccessStatus_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "Platform_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "Imprint_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "StatisticalSearchingFullValue_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "StatisticalSearchingCodeValue_display"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "DocFormat"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "bibIdentifier"));
                searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "holdingsIdentifier"));
                searchParams.getSearchConditions().get(0).getSearchField().setDocType(docType);
                searchParams.getSearchConditions().get(1).getSearchField().setDocType(docType);
                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                workbenchForm.setSearchResponse(searchResponse);
                if (searchParams == null) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
                    workbenchForm.setWorkBibDocumentList(null);
                    workbenchForm.setWorkHoldingsDocumentList(null);
                    workbenchForm.setWorkItemDocumentList(null);
                    workbenchForm.setWorkEHoldingsDocumentList(null);
                    return super.navigate(workbenchForm, result, request, response);
                }

                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();


                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        //if (searchResultField.getDocType().equalsIgnoreCase(DocType.EHOLDINGS.getCode())) {
                        if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                            searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("AccessStatus_display")) {
                            searchResultDisplayRow.setAccessStatus(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("Platform_display")) {
                            searchResultDisplayRow.setPlatForm(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("Imprint_display")) {
                            searchResultDisplayRow.setImprint(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("StatisticalSearchingFullValue_display")) {
                            searchResultDisplayRow.setStatisticalCode(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("StatisticalSearchingCodeValue_display")) {
                            searchResultDisplayRow.setStatisticalCode(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
                            searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                            searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                            searchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                        }

                        //  }

                    }
                    searchResultDisplayRows.add(searchResultDisplayRow);
                }


                workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
                workbenchForm.setHoldingSearchResultDisplayRowList(searchResultDisplayRows);
                workbenchForm.setSearchResponse(searchResponse);
                if (searchResultDisplayRows.size() == 0) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
                }
                workbenchForm.setWorkBibDocumentList(null);
                workbenchForm.setWorkHoldingsDocumentList(null);
                workbenchForm.setWorkItemDocumentList(null);
                setPageNextPreviousAndEntriesInfo(workbenchForm);

            }


        } catch (Exception e) {
            LOG.error("Exception :", e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            LOG.error("Workbenchcontroller Search Exception:" + e);
        }
        return navigate(workbenchForm, result, request, response);
    }

    /**
     * Enable, disable the next and previous and also show the message for number of entries
     *
     * @param workbenchForm
     * @return
     */
    public void setPageNextPreviousAndEntriesInfo(WorkbenchForm workbenchForm) {
        this.totalRecCount = workbenchForm.getSearchResponse().getTotalRecordCount();
        this.start = workbenchForm.getSearchResponse().getStartIndex();
        this.pageSize = workbenchForm.getSearchResponse().getPageSize();
        workbenchForm.setPreviousFlag(getWorkbenchPreviousFlag());
        workbenchForm.setNextFlag(getWorkbenchNextFlag());
        workbenchForm.setPageShowEntries(getWorkbenchPageShowEntries());
    }

    /**
     * search to Get the next documents
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=nextSearch")
    public ModelAndView nextSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {

        LOG.debug("Inside the nextSearch method");
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        SearchParams searchParams = workbenchForm.getSearchParams();
        SearchResponse searchResponse = null;
        List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
        searchParams.getSearchResultFields().clear();

        this.start = Math.max(0, this.start + this.pageSize);
        searchParams.setStartIndex(this.start);
        if (searchParams.getSearchConditions().get(0).getSearchField().getDocType().equalsIgnoreCase("item")) {


            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "LocalId_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Location_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CallNumber_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "DocFormat"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "bibIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "holdingsIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "id"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemBarcode_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Location_display"));


            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();

                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {

                    if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                        searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                        searchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("locationName")) {
                        searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                        searchResultDisplayRow.setCallNumber(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("BarcodeARSL_display")) {
                        searchResultDisplayRow.setBarcode(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("docFormat")) {
                        searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                        searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                        searchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                        searchResultDisplayRow.setItemIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("ItemBarcode_display")) {
                        searchResultDisplayRow.setBarcode(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                        searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                    }

                }
                searchResultDisplayRows.add(searchResultDisplayRow);
            }

            workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
            workbenchForm.setSearchResponse(searchResponse);
            if (searchResultDisplayRows.size() == 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
            }
            setPageNextPreviousAndEntriesInfo(workbenchForm);
            return navigate(workbenchForm, result, request, response);
        } else if (searchParams.getSearchConditions().get(0).getSearchField().getDocType().equalsIgnoreCase("holdings")) {

            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "LocalId_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "Location_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "CallNumber_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "DocFormat"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "bibIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "itemIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "holdingsIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "Location_display"));

            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                        searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                        searchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                        searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                        searchResultDisplayRow.setCallNumber(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
                        searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                        searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                        searchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("itemIdentifier")) {
                        searchResultDisplayRow.setItemIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                        searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                    }

                }
                searchResultDisplayRows.add(searchResultDisplayRow);
            }
            workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
            workbenchForm.setSearchResponse(searchResponse);
            if (searchResultDisplayRows.size() == 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
            }
            setPageNextPreviousAndEntriesInfo(workbenchForm);
            return navigate(workbenchForm, result, request, response);
        } else if (searchParams.getSearchConditions().get(0).getSearchField().getDocType().equalsIgnoreCase("bibliographic")) {

            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "LocalId_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Author_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "PublicationDate_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "DocFormat"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "bibIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "id"));

            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    // if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode())) {
                    if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                        searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                        searchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Author_display")) {
                        searchResultDisplayRow.setAuthor(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("PublicationDate_display")) {
                        searchResultDisplayRow.setPublicationDate(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
                        searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                        searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                        searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                    }
                    if (eResourceId != null) {
                        searchResultDisplayRow.setOleERSIdentifier(eResourceId);
                    }
                    if (tokenId != null) {
                        searchResultDisplayRow.setTokenId(tokenId);
                    }
                    // }
                }
                searchResultDisplayRows.add(searchResultDisplayRow);
            }
            workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
            workbenchForm.setSearchResponse(searchResponse);
            //List<Bib> bibList = getBibList(searchParams);
            setPageNextPreviousAndEntriesInfo(workbenchForm);
            return navigate(workbenchForm, result, request, response);
        } else if (searchParams.getSearchConditions().get(0).getSearchField().getDocType().equalsIgnoreCase("eholdings")) {
            /*List<OleWorkEHoldingsDocument> oleWorkEHoldingsDocuments = queryService.getDocuments(searchParams);
            workbenchForm.setWorkEHoldingsDocumentList(oleWorkEHoldingsDocuments);*/
            /*List<Holdings> holdingsList = null;
            holdingsList = getHoldingsList(searchParams);
            workbenchForm.setHoldingsList(holdingsList);*/

            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "LocalId_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "AccessStatus_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "Platform_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "Imprint_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "StatisticalSearchingFullValue_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "StatisticalSearchingCodeValue_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "DocFormat"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "bibIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("eHoldings", "holdingsIdentifier"));

            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();


                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                        searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("AccessStatus_display")) {
                        searchResultDisplayRow.setAccessStatus(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Platform_display")) {
                        searchResultDisplayRow.setPlatForm(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Imprint_display")) {
                        searchResultDisplayRow.setImprint(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("StatisticalSearchingFullValue_display")) {
                        searchResultDisplayRow.setStatisticalCode(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("StatisticalSearchingCodeValue_display")) {
                        searchResultDisplayRow.setStatisticalCode(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
                        searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                        searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                        searchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                    }

                }
                searchResultDisplayRows.add(searchResultDisplayRow);
            }


            workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
            workbenchForm.setSearchResponse(searchResponse);
            setPageNextPreviousAndEntriesInfo(workbenchForm);
            return navigate(workbenchForm, result, request, response);
        }
        return navigate(workbenchForm, result, request, response);
    }

    /**
     * search to Get the previous documents
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=previousSearch")
    public ModelAndView previousSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        LOG.debug("Inside the previousSearch method");
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        SearchParams searchParams = workbenchForm.getSearchParams();
        searchParams.getSearchResultFields().clear();
        SearchResponse searchResponse = null;
        List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
        this.start = Math.max(0, this.start - this.pageSize);
        searchParams.setStartIndex((this.start == 0) ? 0 : this.start);
        if (searchParams.getSearchConditions().get(0).getSearchField().getDocType().equalsIgnoreCase("item")) {

            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "LocalId_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Location_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CallNumber_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "DocFormat"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "bibIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "holdingsIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "id"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemBarcode_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Location_display"));


            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();

                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {

                    if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                        searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                        searchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("locationName")) {
                        searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                        searchResultDisplayRow.setCallNumber(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("BarcodeARSL_display")) {
                        searchResultDisplayRow.setBarcode(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("docFormat")) {
                        searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                        searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                        searchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                        searchResultDisplayRow.setItemIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("ItemBarcode_display")) {
                        searchResultDisplayRow.setBarcode(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                        searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                    }



                }
                searchResultDisplayRows.add(searchResultDisplayRow);
            }

            workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
            workbenchForm.setSearchResponse(searchResponse);
            if (searchResultDisplayRows.size() == 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
            }
            setPageNextPreviousAndEntriesInfo(workbenchForm);
            return navigate(workbenchForm, result, request, response);
        } else if (searchParams.getSearchConditions().get(0).getSearchField().getDocType().equalsIgnoreCase("holdings")) {
            /*List<OleWorkHoldingsDocument> oleWorkHoldingsDocuments = queryService.getDocuments(searchParams);
            workbenchForm.setWorkHoldingsDocumentList(oleWorkHoldingsDocuments);*/
            /*List<Holdings> holdingsList = null;
            holdingsList = getHoldingsList(searchParams);*/

            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "LocalId_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "Location_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "CallNumber_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "DocFormat"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "bibIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "itemIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "holdingsIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "Location_display"));

            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {

                    if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                        searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                        searchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                        searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                        searchResultDisplayRow.setCallNumber(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
                        searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                        searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                        searchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("itemIdentifier")) {
                        searchResultDisplayRow.setItemIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                        searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                    }




                }
                searchResultDisplayRows.add(searchResultDisplayRow);
            }
            workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
            workbenchForm.setSearchResponse(searchResponse);
            if (searchResultDisplayRows.size() == 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
            }
            setPageNextPreviousAndEntriesInfo(workbenchForm);
            return navigate(workbenchForm, result, request, response);
        } else if (searchParams.getSearchConditions().get(0).getSearchField().getDocType().equalsIgnoreCase("bibliographic")) {

            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "LocalId_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Author_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "PublicationDate_display"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "DocFormat"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "bibIdentifier"));
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "id"));

            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                        searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                        searchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Author_display")) {
                        searchResultDisplayRow.setAuthor(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("PublicationDate_display")) {
                        searchResultDisplayRow.setPublicationDate(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
                        searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                        searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                        searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                    }
                    if (eResourceId != null) {
                        searchResultDisplayRow.setOleERSIdentifier(eResourceId);
                    }
                    if (tokenId != null) {
                        searchResultDisplayRow.setTokenId(tokenId);
                    }
                }
                searchResultDisplayRows.add(searchResultDisplayRow);
            }
            workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
            workbenchForm.setSearchResponse(searchResponse);
            setPageNextPreviousAndEntriesInfo(workbenchForm);
            return navigate(workbenchForm, result, request, response);
        } else if (searchParams.getSearchConditions().get(0).getSearchField().getDocType().equalsIgnoreCase("eholdings")) {
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();


                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if (searchResultField.getFieldName().equalsIgnoreCase("LocalId_display")) {
                        searchResultDisplayRow.setLocalId(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("AccessStatus_display")) {
                        searchResultDisplayRow.setAccessStatus(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Platform_display")) {
                        searchResultDisplayRow.setPlatForm(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("Imprint_display")) {
                        searchResultDisplayRow.setImprint(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("StatisticalSearchingFullValue_display")) {
                        searchResultDisplayRow.setStatisticalCode(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("StatisticalSearchingCodeValue_display")) {
                        searchResultDisplayRow.setStatisticalCode(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("DocFormat")) {
                        searchResultDisplayRow.setDocFormat(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                        searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                    }
                    if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                        searchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                    }



                }
                searchResultDisplayRows.add(searchResultDisplayRow);
            }


            workbenchForm.setSearchResultDisplayRowList(searchResultDisplayRows);
            workbenchForm.setSearchResponse(searchResponse);
            setPageNextPreviousAndEntriesInfo(workbenchForm);
            return navigate(workbenchForm, result, request, response);
        }

        return navigate(workbenchForm, result, request, response);

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
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        workbenchForm.setSearchParams(new SearchParams());
        List<SearchCondition> searchConditions = workbenchForm.getSearchParams().getSearchConditions();
        searchConditions.add(new SearchCondition());
        searchConditions.add(new SearchCondition());
        workbenchForm.setWorkBibDocumentList(null);
        workbenchForm.setWorkHoldingsDocumentList(null);
        workbenchForm.setWorkItemDocumentList(null);
        workbenchForm.setWorkEHoldingsDocumentList(null);
        workbenchForm.setMessage(null);
        return getUIFModelAndView(workbenchForm);
//        return navigate(workbenchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=select")
    public ModelAndView select(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        String selectedRecord = workbenchForm.getWorkBibDocumentList().get(index).getId();
        LOG.info("selectedRecord--->" + selectedRecord);
        return super.navigate(workbenchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=selectRecords")
    public ModelAndView selectRecords(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        List<String> selectedRecordIds = new ArrayList<String>();
        List<OleWorkBibDocument> oleWorkBibDocuments = workbenchForm.getWorkBibDocumentList();
        for (OleWorkBibDocument oleWorkBibDocument : oleWorkBibDocuments) {
            if (oleWorkBibDocument.isSelect()) {
                selectedRecordIds.add(oleWorkBibDocument.getId());
            }
        }
        LOG.info("selectedRecords--->" + selectedRecordIds);
        return getUIFModelAndView(workbenchForm);
    }

    /**
     * Exports the selected bib records as request XML.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping(params = "methodToCall=export")
    public ModelAndView export(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        boolean hasPermission = canExportToRequestXml(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasPermission) {
            workbenchForm.setJumpToId("breadcrumb_label");
            workbenchForm.setFocusId("breadcrumb_label");
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_AUTHORIZATION);
            return navigate(workbenchForm, result, request, response);
        }
        workbenchForm.setShowRequestXML(true);
        Request docStoreRequest = new Request();
        docStoreRequest.setOperation(Request.Operation.ingest.toString());
        docStoreRequest.setUser("ole-khuntley");
        List<String> selectedRecordIds = new ArrayList<String>();
        List<SearchResultDisplayRow> searchResultDisplayRows = workbenchForm.getSearchResultDisplayRowList();
        for (SearchResultDisplayRow searchResultDisplayRow : searchResultDisplayRows) {
            if (searchResultDisplayRow.isSelect()) {
                selectedRecordIds.add(searchResultDisplayRow.getBibIdentifier());
            }
        }
        LOG.info("selectedRecords--->" + selectedRecordIds);
        String bibTreesContent = "";
        BibTrees bibTrees = new BibTrees();
        for (int i = 0; i < selectedRecordIds.size(); i++) {
            String id = selectedRecordIds.get(i);
            BibTree bibTree = new BibTree();
            Map findBibTreeMap = new HashMap();
            findBibTreeMap.put("DocType", "bibliographic");
            findBibTreeMap.put("ID", id);
            bibTree = getDocstoreClientLocator().getDocstoreClient().findBibTree(findBibTreeMap);
            bibTrees.getBibTrees().add(bibTree);
        }
        bibTreesContent = BibTrees.serialize(bibTrees);
        workbenchForm.setRequestXMLTextArea(bibTreesContent);
        return getUIFModelAndView(workbenchForm);
    }

    @RequestMapping(params = "methodToCall=submit")
    public ModelAndView submit(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        boolean isValid = false;
        BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
        Map<String, String> map = new HashMap<>();
        List<Integer> resultList = new ArrayList<>();
        for (SearchResultDisplayRow searchResultDisplayRow : workbenchForm.getSearchResultDisplayRowList()) {
            if (searchResultDisplayRow.isSelect()) {
                map.put(OLEConstants.BIB_ID, searchResultDisplayRow.getBibIdentifier());
                List<OleCopy> listOfValues = (List<OleCopy>) boService.findMatching(OleCopy.class, map);
                if (listOfValues.size() > 0 && (workbenchForm.getMessage() == null || workbenchForm.getMessage().equals(""))) {
                    for (OleCopy oleCopy : listOfValues) {
                        resultList.add(oleCopy.getReqDocNum());
                    }
                    Set<Integer> resultSet = new HashSet<>(resultList);
                    resultList = new ArrayList<>(resultSet);
                    StringBuffer reqIds = new StringBuffer("");
                    if (resultList.size() > 0) {
                        int count = 0;
                        for (; count < resultList.size() - 1; count++) {
                            reqIds.append(resultList.get(count) + ",");
                        }
                        reqIds.append(resultList.get(count));
                    }
                    workbenchForm.setMessage(OLEConstants.POPUP_MESSAGE + reqIds.toString() + OLEConstants.PROCEED_MESSAGE);
                    return getUIFModelAndView(workbenchForm);
                }
                workbenchForm.setMessage("");
                processNewRecordResponseForOLE(searchResultDisplayRow.getLocalId(), workbenchForm.getTokenId());
                workbenchForm.setSuccessMessage(OLEConstants.LINK_SUCCESS_MESSAGE);
                isValid = true;
                break;
            }
        }
        if (isValid == false) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.BIB_SELECT);
            return getUIFModelAndView(workbenchForm);
        }
        return getUIFModelAndView(workbenchForm);
    }

    private void processNewRecordResponseForOLE(String bibId, String tokenId) throws Exception {
        String instanceUUID = null;
        BibTree bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibId);
        OLEEditorResponse oleEditorResponse = new OLEEditorResponse();
        if (bibTree.getHoldingsTrees() != null && bibTree.getHoldingsTrees().size() > 0) {
            instanceUUID = bibTree.getHoldingsTrees().get(0).getHoldings().getId();
        }
        oleEditorResponse.setLinkedInstanceId(instanceUUID);
        oleEditorResponse.setBib(bibTree.getBib());
        oleEditorResponse.setTokenId(tokenId);
        HashMap<String, OLEEditorResponse> oleEditorResponseMap = new HashMap<String, OLEEditorResponse>();
        oleEditorResponseMap.put(tokenId, oleEditorResponse);
        OleDocstoreResponse.getInstance().setEditorResponse(oleEditorResponseMap);
    }

    public String getURL() {
        String url = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.OLE_EXPOSED_WEB_SERVICE_url);
        return url;
    }

    @RequestMapping(params = "methodToCall=linkToBib")
    public ModelAndView linkToBib(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        List<SearchResultDisplayRow> searchResultDisplayRowList = workbenchForm.getHoldingSearchResultDisplayRowList();
        workbenchForm.setSuccessMessage(null);
        if (searchResultDisplayRowList != null && searchResultDisplayRowList.size() > 0) {
            for (SearchResultDisplayRow searchResultDisplayRow : searchResultDisplayRowList) {
                if (searchResultDisplayRow.isSelect()) {
                    processNewHoldingsResponse(searchResultDisplayRow, workbenchForm.getTokenId());
                    Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(searchResultDisplayRow.getHoldingsIdentifier());
                    if (holdings.getHoldingsType().equalsIgnoreCase("electronic")) {
                        saveRecordToDocstore(searchResultDisplayRow, eResourceId);
                    }
                    workbenchForm.setSuccessMessage("");
                    break;
                } else {
                    workbenchForm.setSuccessMessage(OLEConstants.HOLDINGS_ERROR_MESSAGE);
                }
            }
        }
        if (eResourceId != null && !eResourceId.isEmpty()) {
            Map<String, String> tempId = new HashMap<String, String>();
            tempId.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, eResourceId);
            OLEEResourceRecordDocument tempDocument = (OLEEResourceRecordDocument) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, tempId);
            try {
                Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
                tempDocument.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(tempDocument.getDocumentNumber(), principalPerson));
                if (tempDocument != null) {
                    try {
                        tempDocument.setSelectInstance(OLEConstants.OLEEResourceRecord.LINK_EXIST_INSTANCE);
                        tempDocument.seteInstanceFlag(true);
                        getOleEResourceSearchService().getNewInstance(tempDocument, tempDocument.getDocumentNumber());
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
        return getUIFModelAndView(workbenchForm);
    }

    @RequestMapping(params = "methodToCall=getHoldingsList")
    public ModelAndView getHoldingsList(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        workbenchForm.setErrorMessage(null);
        workbenchForm.setHoldingSearchResultDisplayRowList(null);
        workbenchForm.seteHoldingsFlag("false");
        List<SearchResultDisplayRow> searchResultDisplayRowList = new ArrayList<>();

        if (workbenchForm.getSearchResultDisplayRowList() != null && workbenchForm.getSearchResultDisplayRowList().size() > 0) {
            for (SearchResultDisplayRow searchResultDisplay : workbenchForm.getSearchResultDisplayRowList()) {
                if (searchResultDisplay.isSelect()) {
                    BibTree bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(searchResultDisplay.getLocalId());
                    List<HoldingsTree> holdingsTreeList = bibTree.getHoldingsTrees();
                    if (holdingsTreeList.size() > 0) {
                        workbenchForm.setHoldingsFlag("true");
                        for (HoldingsTree holdingsTree : holdingsTreeList) {
                            if (holdingsTree.getHoldings().getHoldingsType().equalsIgnoreCase("print")) {
                                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                                searchResultDisplayRow.setHoldingsIdentifier(holdingsTree.getHoldings().getId());
                                searchResultDisplayRow.setTitle(holdingsTree.getHoldings().getBib().getTitle());
                                searchResultDisplayRow.setBibIdentifier(holdingsTree.getHoldings().getBib().getId());
                                searchResultDisplayRow.setCallNumber(holdingsTree.getHoldings().getCallNumber());
                                // searchResultDisplayRow.setCallNumberPrefix(holdingsTree.getHoldings().getCallNumberPrefix());
                                // searchResultDisplayRow.setCallNumberType(holdingsTree.getHoldings().getCallNumberType());
                                // searchResultDisplayRow.setCopyNumber(holdingsTree.getHoldings().getCopyNumber());
                                //  searchResultDisplayRow.setStaffOnlyFlag(holdingsTree.getStaffOnlyFlag());
                                // searchResultDisplayRow.setLinkedBibCount(holdingsTree.getLinkedBibCount());
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
            workbenchForm.setErrorMessage("selected bib doesnt have Holdings");
        }
        workbenchForm.setHoldingSearchResultDisplayRowList(searchResultDisplayRowList);
        workbenchForm.setWorkEHoldingsDocumentList(null);
        return navigate(workbenchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=getEHoldingsList")
    public ModelAndView getEHoldingsList(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        WorkbenchForm workbenchForm = (WorkbenchForm) form;
        workbenchForm.setErrorMessage(null);
        workbenchForm.setHoldingsFlag("false");
        workbenchForm.setHoldingSearchResultDisplayRowList(null);
        List<SearchResultDisplayRow> searchResultDisplayRowArrayList = new ArrayList<>();
        if (workbenchForm.getSearchResultDisplayRowList() != null && workbenchForm.getSearchResultDisplayRowList().size() > 0) {
            for (SearchResultDisplayRow searchResultDisplay : workbenchForm.getSearchResultDisplayRowList()) {
                if (searchResultDisplay.isSelect()) {
                    BibTree bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(searchResultDisplay.getLocalId());
                    if (bibTree.getHoldingsTrees().size() > 0) {
                        workbenchForm.seteHoldingsFlag("true");
                        for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                            if (holdingsTree.getHoldings().getHoldingsType().equalsIgnoreCase("electronic")) {
                                OleHoldings oleHoldings = new OleHoldings();
                                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                                HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
                                oleHoldings = (OleHoldings) holdingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
                                searchResultDisplayRow.setAccessStatus(oleHoldings.getAccessStatus());
                                searchResultDisplayRow.setPlatForm(oleHoldings.getPlatform() != null ? oleHoldings.getPlatform().getPlatformName() : null);
                                searchResultDisplayRow.setImprint(oleHoldings.getImprint());
                                searchResultDisplayRow.setStatisticalCode(oleHoldings.getStatisticalSearchingCode() != null ? oleHoldings.getStatisticalSearchingCode().getCodeValue() : null);
                                searchResultDisplayRow.setLocationName(holdingsTree.getHoldings().getLocationName());
                                searchResultDisplayRow.setBibIdentifier(holdingsTree.getHoldings().getBib().getId());
                                searchResultDisplayRow.setInstanceIdentifier(holdingsTree.getHoldings().getId());
                                searchResultDisplayRow.setHoldingsIdentifier(holdingsTree.getHoldings().getId());
                                searchResultDisplayRow.setLocalId(DocumentUniqueIDPrefix.getDocumentId(holdingsTree.getHoldings().getId()));
                                //searchResultDisplayRow.setUrl(oleHoldings.getLink().getUrl());
                                //oleWorkEHoldingsDocument.seteResourceName(holdingsTree.geteResourceName());
                                searchResultDisplayRowArrayList.add(searchResultDisplayRow);
                            }
                        }
                    }
                }
            }
        }
        if (searchResultDisplayRowArrayList.size() == 0) {
            workbenchForm.setErrorMessage("selected bib doesnt have EHoldings");
        }
        workbenchForm.setHoldingSearchResultDisplayRowList(searchResultDisplayRowArrayList);
        workbenchForm.setWorkHoldingsDocumentList(null);
        return navigate(workbenchForm, result, request, response);
    }

    private void saveRecordToDocstore(SearchResultDisplayRow searchResultDisplayRow, String eResourceId) throws Exception {
        Holdings eHoldings = new org.kuali.ole.docstore.common.document.EHoldings();
        OleHoldings oleHoldings = new OleHoldings();
        eHoldings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(searchResultDisplayRow.getHoldingsIdentifier());
        oleHoldings = new HoldingOlemlRecordProcessor().fromXML(eHoldings.getContent());
        oleHoldings.setEResourceId(eResourceId);
        eHoldings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
        getDocstoreClientLocator().getDocstoreClient().updateHoldings(eHoldings);
    }

    private void processNewHoldingsResponse(SearchResultDisplayRow searchResultDisplayRow, String tokenId) throws Exception {
        OLEEditorResponse oleEditorResponse = new OLEEditorResponse();
        oleEditorResponse.setLinkedInstanceId(searchResultDisplayRow.getHoldingsIdentifier());
        oleEditorResponse.setTokenId(tokenId);
        HashMap<String, OLEEditorResponse> oleEditorResponseMap = new HashMap<String, OLEEditorResponse>();
        oleEditorResponseMap.put(tokenId, oleEditorResponse);
        OleDocstoreResponse.getInstance().setEditorResponse(oleEditorResponseMap);
    }

    private boolean canExportToRequestXml(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.DESC_WORKBENCH_EXPORT_XML);
    }

}
