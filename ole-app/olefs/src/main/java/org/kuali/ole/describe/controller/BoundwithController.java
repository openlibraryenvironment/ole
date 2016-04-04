package org.kuali.ole.describe.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.*;
import org.kuali.ole.describe.form.BoundwithForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.config.DocFieldConfig;
import org.kuali.ole.docstore.common.document.config.DocFormatConfig;
import org.kuali.ole.docstore.common.document.config.DocTypeConfig;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Request;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.select.util.TransferUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 11/26/12
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/boundwithController")
public class BoundwithController extends OLESearchController {

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    DocumentSearchConfig documentSearchConfig = DocumentSearchConfig.getDocumentSearchConfig();
    private static final Logger LOG = Logger.getLogger(BoundwithController.class);
    public int totalRecCount;
    public int start;
    public int pageSize;

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


    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new BoundwithForm();
    }

    protected ModelAndView callSuper(BindingResult result, HttpServletRequest request, HttpServletResponse response, BoundwithForm boundwithForm) {
        return super.navigate(boundwithForm, result, request, response);
    }

    /*private boolean canPerformTransferSearch(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.TRANSFER_SEARCH);
    }*/

    /*private boolean canPerformBoundWithSearch(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.BOUND_WITH_SEARCH);
    }*/

    private boolean canPerformTransfer(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.TRANSFER_HOLDING_OR_ITEM);
    }

    private boolean canPerformBoundWith(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.BOUND_WITH);
    }

    private boolean canPerformAnalytics(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.ANALYTICS_PERMISSION);
    }

    @RequestMapping(params = "methodToCall=clear")
    public ModelAndView clear(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Inside clearSearch Method");
        BoundwithForm boundwithForm = (BoundwithForm) form;
        boundwithForm.setHoldingsList(null);
        boundwithForm.setItemList(null);
        boundwithForm.setSearchResultDisplayRowList(null);
        boundwithForm.setPageSize(10);
        boundwithForm.setPreviousFlag(false);
        boundwithForm.setNextFlag(false);
        for (SearchCondition searchCondition : boundwithForm.getSearchConditions()) {
            searchCondition.getSearchField().setFieldName("");
            searchCondition.getSearchField().setFieldValue("");
        }
        if (boundwithForm.getSearchResultDisplayRowList() != null && boundwithForm.getSearchResultDisplayRowList().size() > 0) {
            boundwithForm.getSearchResultDisplayRowList().clear();
        }
        if (boundwithForm.getSearchParams() != null && boundwithForm.getSearchParams().getFacetFields() != null) {
            boundwithForm.getSearchParams().getFacetFields().clear();
        }
        if (boundwithForm.getFacetResultFields() != null) {
            boundwithForm.getFacetResultFields().clear();
        }
        clearForm(boundwithForm);

        return navigate(boundwithForm, result, request, response);
    }


    private void clearForm(BoundwithForm boundwithForm) {

        List<Integer> pageSizes = documentSearchConfig.getPageSizes();
        if(!pageSizes.isEmpty() || pageSizes.size() > 0) {
            boundwithForm.setPageSize(pageSizes.get(0));
        }
        boundwithForm.setPreviousFlag(false);
        boundwithForm.setNextFlag(false);
        boundwithForm.setBrowseText(null);
        boundwithForm.setShowRequestXml(false);
        boundwithForm.setHoldingsList(null);
        boundwithForm.setItemList(null);
        boundwithForm.setSearchResultDisplayRowList(null);
        boundwithForm.setCallNumberBrowseText(null);
        boundwithForm.setLocation(null);
        boundwithForm.setClassificationScheme("LCC");
        boundwithForm.setFacetLimit(0);
        boundwithForm.setTotalRecordCount(0);
        boundwithForm.setSearchParams(new SearchParams());
        boundwithForm.setPageShowEntries(null);
        boundwithForm.setShowPageSize(null);
        boundwithForm.setShowFieldSort(null);
        boundwithForm.setBibSearchResultDisplayRowList(null);
        boundwithForm.setHoldingSearchResultDisplayRowList(null);
        boundwithForm.setSearchResponse(null);
        boundwithForm.setFacetResultFields(null);

        boundwithForm.setWorkBibDocumentList(null);
        boundwithForm.setWorkHoldingsDocumentList(null);
        boundwithForm.setWorkItemDocumentList(null);
        boundwithForm.setWorkEHoldingsDocumentList(null);
        boundwithForm.setSearchTypeField("OLESearch");
        boundwithForm.setSelectAllRecords(false);

        if (boundwithForm.getSearchParams() != null) {
            for (SearchCondition searchCondition : boundwithForm.getSearchConditions()) {
                if (searchCondition.getSearchField() != null) {
                    searchCondition.getSearchField().setFieldName("");
                    searchCondition.getSearchField().setFieldValue("");
                }
            }

            if (boundwithForm.getSearchParams().getFacetFields() != null) {
                boundwithForm.getSearchParams().getFacetFields().clear();
            }
            if (boundwithForm.getSearchParams().getFacetConditions() != null) {
                boundwithForm.getSearchParams().getFacetConditions().clear();
            }
            boundwithForm.getSearchParams().getSearchResultFields().clear();

        }
        if (boundwithForm.getSearchResultDisplayRowList() != null && boundwithForm.getSearchResultDisplayRowList().size() > 0) {
            boundwithForm.getSearchResultDisplayRowList().clear();
        }

        if (boundwithForm.getFacetResultFields() != null) {
            boundwithForm.getFacetResultFields().clear();
        }

    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the boundwithForm start method");
        boolean hasPermission = false;
        if (request.getSession().getAttribute(OLEConstants.LEFT_LIST) != null && request.getSession().getAttribute(OLEConstants.RIGHT_LIST) != null) {
            request.getSession().removeAttribute(OLEConstants.LEFT_LIST);
            request.getSession().removeAttribute(OLEConstants.RIGHT_LIST);
        }
        BoundwithForm boundwithForm = (BoundwithForm) form;
        boundwithForm.getSearchConditions().clear();
        if (boundwithForm.getDocType() == null) {
            boundwithForm.setDocType(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode());
        }
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setOperator("AND");
        SearchField searchField = new SearchField();
        searchField.setFieldName("any");
        searchField.setDocType(boundwithForm.getDocType());
        searchCondition.setSearchField(searchField);
        boundwithForm.getSearchConditions().add(searchCondition);


        boundwithForm.setSearchTypeField("Boundwith");
        if (boundwithForm.getSearchResultDisplayRowList() != null && boundwithForm.getSearchResultDisplayRowList().size() > 0) {
            boundwithForm.getSearchResultDisplayRowList().clear();
        }
        if (boundwithForm.getSearchParams() != null && boundwithForm.getSearchParams().getFacetFields() != null) {
            boundwithForm.getSearchParams().getFacetFields().clear();
        }

        if (boundwithForm.getFacetResultFields() != null) {
            boundwithForm.getFacetResultFields().clear();
        }
        if (boundwithForm.getSearchParams() != null) {
            boundwithForm.getSearchParams().getSearchConditions().clear();
            boundwithForm.getSearchParams().getSearchResultFields().clear();
            boundwithForm.setStart(0);
        }
        request.getSession().setAttribute("selectedFacetResults", null);
        if (StringUtils.isEmpty(boundwithForm.getSearchType())) {
            boundwithForm.setSearchType("search");
        }
        if (boundwithForm.getDocType() == null) {
            boundwithForm.setDocType(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode());
        }

        if (boundwithForm.getViewId().equalsIgnoreCase("TransferView")) {
            hasPermission = canPerformTransfer(GlobalVariables.getUserSession().getPrincipalId());
        } else if (boundwithForm.getViewId().equalsIgnoreCase("BoundwithView")) {
            hasPermission = canPerformBoundWith(GlobalVariables.getUserSession().getPrincipalId());
        } else if (boundwithForm.getViewId().equalsIgnoreCase(OLEConstants.ANALYTICS_VIEW)) {
            hasPermission = canPerformAnalytics(GlobalVariables.getUserSession().getPrincipalId());
        } else if (boundwithForm.getViewId().equalsIgnoreCase(OLEConstants.ANALYTICS_SUMMARY_VIEW)) {
            hasPermission = canPerformAnalytics(GlobalVariables.getUserSession().getPrincipalId());
        }
        if (!hasPermission) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_AUTHORIZATION);
            return navigate(boundwithForm, result, request, response);
        }
        boundwithForm.setWorkBibDocumentList(null);

        boundwithForm.setWorkHoldingsDocumentList(null);
        boundwithForm.setWorkItemDocumentList(null);
        boundwithForm.setWorkEHoldingsDocumentList(null);
        boundwithForm.setMessage(null);
        boundwithForm.setSearchResultDisplayRowList(null);
//        boundwithForm.setPageSize(10);
        List<Integer> pageSizes = documentSearchConfig.getPageSizes();
        if (!pageSizes.isEmpty() || pageSizes.size() > 0) {
            boundwithForm.setPageSize(pageSizes.get(0));
        }
        boundwithForm.setHoldingsList(null);
        boundwithForm.setItemList(null);
        clearForm(boundwithForm);
        GlobalVariables.getMessageMap().clearErrorMessages();
        return navigate(boundwithForm, result, request, response);

    }

    /**
     * Returns the search results for the bib,holdings and item.
     *
     * @param boundwithForm
     * @return
     */

    private void setShowPageSizeEntries(BoundwithForm boundwithForm) {
        List<Integer> pageSizes = documentSearchConfig.getPageSizes();
        if (org.apache.commons.collections.CollectionUtils.isEmpty(pageSizes)) {
            pageSizes.add(10);
            pageSizes.add(25);
            pageSizes.add(50);
            pageSizes.add(100);
        }
        boundwithForm.setShowPageSize(pageSizes.toString());
    }

//    @RequestMapping(params = "methodToCall=search")
//    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
//                               HttpServletRequest request, HttpServletResponse response) {
//        LOG.info("*** BoundWithController - Inside Search Method ***");
//        BoundwithForm boundwithForm = (BoundwithForm) form;
//        boolean isRemoveSearchCondition = false;
//        Boolean hasPermission = null;
//        if (boundwithForm.getViewId().equalsIgnoreCase("TransferView")) {
//            hasPermission = canPerformTransfer(GlobalVariables.getUserSession().getPrincipalId());
//        } else if (boundwithForm.getViewId().equalsIgnoreCase("BoundwithView")) {
//            hasPermission = canPerformBoundWith(GlobalVariables.getUserSession().getPrincipalId());
//        } else if (boundwithForm.getViewId().equalsIgnoreCase(OLEConstants.ANALYTICS_VIEW)) {
//            hasPermission = canPerformAnalytics(GlobalVariables.getUserSession().getPrincipalId());
//        } else if (boundwithForm.getViewId().equalsIgnoreCase(OLEConstants.ANALYTICS_SUMMARY_VIEW)) {
//            hasPermission = true;
//        }
//        if (!hasPermission) {
//            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_AUTHORIZATION);
//            return navigate(boundwithForm, result, request, response);
//        }
//        setShowPageSizeEntries(boundwithForm);
//        SearchParams searchParams = boundwithForm.getSearchParams();
//        searchParams.getSearchConditions().clear();
//        searchParams.getSearchConditions().addAll(boundwithForm.getSearchConditions());
//        if ("true".equals(boundwithForm.getSortFlag())) {
//            searchParams.setPageSize(boundwithForm.getPageSize());
//            searchParams.setStartIndex(this.start);
//            searchParams.getSortConditions().clear();
//            searchParams.getSortConditions().add(searchParams.buildSortCondition(boundwithForm.getSortField(), boundwithForm.getSortOrder()));
//        } else {
//            searchParams.setPageSize(boundwithForm.getPageSize());
//            searchParams.setStartIndex(this.start);
//        }
//        for (SearchCondition searchCondition : searchParams.getSearchConditions()) {
//            searchCondition.getSearchField().setDocType(boundwithForm.getDocType());
//        }
//        if (org.apache.commons.collections.CollectionUtils.isEmpty(searchParams.getSearchConditions())) {
//            isRemoveSearchCondition = true;
//            searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(boundwithForm.getDocType(), "", ""), ""));
//        }
//        request.getSession().setAttribute("searchParams", searchParams);
//        if (CollectionUtils.isEmpty(searchParams.getFacetFields())) {
//            Set<String> facetFields = getFacetFields(boundwithForm.getDocType());
//            searchParams.getFacetFields().addAll(facetFields);
//            searchParams.setFacetLimit(documentSearchConfig.getFacetPageSizeShort());
//        }
//        boundwithForm.setFacetLimit(documentSearchConfig.getFacetPageSizeShort() - 1);
//        SearchResponse searchResponse = null;
//        boundwithForm.setSearchResultDisplayFields(getDisplayFields(boundwithForm));
//        searchParams.buildSearchParams(searchParams, boundwithForm.getDocType());
//
//            try {
//                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
//                boundwithForm.setSearchResponse(searchResponse);
//            } catch (Exception e) {
//                LOG.error("Exception : ", e);
//            }
//            List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
//            for (SearchResult searchResult : searchResponse.getSearchResults()) {
//                SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
//                if (DocType.BIB.getCode().equalsIgnoreCase(boundwithForm.getDocType())) {
//                    searchResultDisplayRow.buildBibSearchResultField(searchResult.getSearchResultFields(), null);
//                } else if (DocType.HOLDINGS.getCode().equals(boundwithForm.getDocType())) {
//                    searchResultDisplayRow.buildHoldingSearchResultField(searchResult.getSearchResultFields());
//                } else if (DocType.EHOLDINGS.getCode().equals(boundwithForm.getDocType())) {
//                    searchResultDisplayRow.buildEHoldingSearchResultField(searchResult.getSearchResultFields());
//                } else if (DocType.ITEM.getCode().equals(boundwithForm.getDocType())) {
//                    searchResultDisplayRow.buildItemSearchResultField(searchResult.getSearchResultFields());
//                }
//                searchResultDisplayRows.add(searchResultDisplayRow);
//            }
//            boundwithForm.setSearchResultDisplayRowList(searchResultDisplayRows);
//            if (searchResponse != null && searchResponse.getFacetResult() != null) {
//                boundwithForm.setFacetResultFields(searchResponse.getFacetResult().getFacetResultFields());
//            }
//            if (searchResultDisplayRows.size() == 0) {
//                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_SEARCH_MESSAGE);
//            }
//            request.getSession().setAttribute("searchResultDisplayRowList", searchResultDisplayRows);
//
//        setPageNextPreviousAndEntriesInfo(boundwithForm);
//
//        if (isRemoveSearchCondition) {
//            boundwithForm.getSearchParams().getSearchConditions().clear();
//        }
//
//        return super.navigate(boundwithForm, result, request, response);
//    }
//
//    @RequestMapping(params = "methodToCall=nextSearch")
//    public ModelAndView nextSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
//                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//        BoundwithForm boundwithForm = (BoundwithForm) form;
//        SearchParams searchParams = boundwithForm.getSearchParams();
//        this.start = Math.max(0, this.start + this.pageSize);
//        searchParams.setStartIndex(this.start);
//        return search(boundwithForm, result, request, response);
//    }
//
//    /**
//     * search to Get the previous documents
//     *
//     * @param form
//     * @param result
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(params = "methodToCall=previousSearch")
//    public ModelAndView previousSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
//                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
//        BoundwithForm boundwithForm = (BoundwithForm) form;
//        SearchParams searchParams = boundwithForm.getSearchParams();
//        this.start = Math.max(0, this.start - this.pageSize);
//        searchParams.setStartIndex((this.start == 0) ? 0 : this.start);
//        return search(boundwithForm, result, request, response);
//    }

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
        BoundwithForm boundwithForm = (BoundwithForm) form;
        GlobalVariables.getMessageMap().clearErrorMessages();
        boundwithForm.setSearchParams(new SearchParams());
        boundwithForm.setBibList(null);
        boundwithForm.setHoldingsList(null);
        boundwithForm.setItemList(null);
        boundwithForm.setSearchResultDisplayRowList(null);
        boundwithForm.setPageSize(10);
        boundwithForm.setPreviousFlag(false);
        boundwithForm.setNextFlag(false);
        for (SearchCondition searchCondition : boundwithForm.getSearchConditions()) {
            searchCondition.getSearchField().setFieldName("");
            searchCondition.getSearchField().setFieldValue("");
        }
        if (boundwithForm.getSearchResultDisplayRowList() != null && boundwithForm.getSearchResultDisplayRowList().size() > 0) {
            boundwithForm.getSearchResultDisplayRowList().clear();
        }
        if (boundwithForm.getSearchParams() != null && boundwithForm.getSearchParams().getFacetFields() != null) {
            boundwithForm.getSearchParams().getFacetFields().clear();
        }
        if (boundwithForm.getFacetResultFields() != null) {
            boundwithForm.getFacetResultFields().clear();
        }
        clearForm(boundwithForm);
        return navigate(boundwithForm, result, request, response);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(params = "methodToCall=select")
    public ModelAndView select(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        String selectedRecord = boundwithForm.getBibList().get(index).getId();
        LOG.debug("selectedRecord--->" + selectedRecord);
        return super.navigate(boundwithForm, result, request, response);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=unSelectAll")
    public ModelAndView unSelectAll(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        /*List<OleWorkBibDocument> oleWorkBibDocuments = boundwithForm.getWorkBibDocumentList();
        for (OleWorkBibDocument oleWorkBibDocument : oleWorkBibDocuments) {
            if (oleWorkBibDocument.isSelect()) {
                oleWorkBibDocument.setSelect(false);
            }
        }*/
        if (!CollectionUtils.isEmpty(boundwithForm.getSearchResultDisplayRowList())) {
            for (SearchResultDisplayRow searchResultDisplayRow : boundwithForm.getSearchResultDisplayRowList()) {
                if (searchResultDisplayRow.isSelect()) {
                    searchResultDisplayRow.setSelect(false);
                }
            }
        }
        return navigate(boundwithForm, result, request, response);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=unSelectAllFromLeftTree")
    public ModelAndView unSelectAllFormLeftTree(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        Tree<DocumentTreeNode, String> tree = boundwithForm.getLeftTree();
        unselectDataInTree(tree);
        return navigate(boundwithForm, result, request, response);
    }

    private void unselectDataInTree(Tree<DocumentTreeNode, String> tree) {
        DocumentTreeNode bibDocument;
        Node<DocumentTreeNode, String> rootElement = tree.getRootElement();
        List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
        for (Node<DocumentTreeNode, String> node : list) {
            bibDocument = node.getData();
            LOG.info("is tree1 selected-->" + bibDocument.isSelect());
            if (bibDocument.isSelect()) {
                bibDocument.setSelect(false);
            }
            List<Node<DocumentTreeNode, String>> childrenInstanceList = node.getChildren();
            for (Node<DocumentTreeNode, String> instanceNode : childrenInstanceList) {
                bibDocument = instanceNode.getData();
                if (bibDocument.isSelect()) {
                    bibDocument.setSelect(false);
                }
                List<Node<DocumentTreeNode, String>> childrenItemList = instanceNode.getChildren();
                for (Node<DocumentTreeNode, String> itemNode : childrenItemList) {
                    bibDocument = itemNode.getData();
                    if (bibDocument.isSelect()) {
                        bibDocument.setSelect(false);
                    }
                }

            }
        }

    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=unSelectAllFromRightTree")
    public ModelAndView unSelectAllFormRightTree(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                 HttpServletRequest request, HttpServletResponse response) {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        Tree<DocumentTreeNode, String> tree = boundwithForm.getRightTree();
        unselectDataInTree(tree);
        return navigate(boundwithForm, result, request, response);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=refreshLeftTree")
    public ModelAndView refreshLeftTree(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        Map<String, String> uuidLeftList = (Map<String, String>) request.getSession().getAttribute(OLEConstants.LEFT_LIST);
        if (uuidLeftList == null) {
            uuidLeftList = new HashMap<String, String>();
        }
        Collection<String> uuids = uuidLeftList.values();
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        Node<DocumentTreeNode, String> rootNode = null;
        try {
            rootNode = documentSelectionTree.add(uuids, boundwithForm.getDocType(), true);
            showBoundWithBibs(boundwithForm, rootNode);
        } catch (SolrServerException e) {
            //e.printStackTrace();
            LOG.error("refreshLeftTree Exception:" + e);
        }
        boundwithForm.getLeftTree().setRootElement(rootNode);
        return navigate(boundwithForm, result, request, response);
    }

    private void showBoundWithBibs(BoundwithForm boundwithForm, Node<DocumentTreeNode, String> rootNode) {
        for (Node<DocumentTreeNode, String> documentTreeNode : rootNode.getChildren()) {
            for(Node<DocumentTreeNode, String> documentChildTreeNode : documentTreeNode.getChildren()){
                if (documentChildTreeNode.getNodeLabel().contains("viewId=ShowBibView")) {
                    boundwithForm.setShowBoundWithBIbs(documentChildTreeNode.getNodeLabel().contains("viewId=ShowBibView"));
                } else {
                    boundwithForm.setShowBoundWithBIbs(documentChildTreeNode.getNodeLabel().contains("viewId=ShowBibView"));
                }
            }
            if (boundwithForm.isShowBoundWithBIbs())
                break;
        }
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=refreshRightTree")
    public ModelAndView refreshRightTree(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        Map<String, String> uuidRightList = (Map<String, String>) request.getSession().getAttribute(OLEConstants.RIGHT_LIST);
        if (uuidRightList == null) {
            uuidRightList = new HashMap<String, String>();
        }
        Collection<String> uuids = uuidRightList.values();
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        Node<DocumentTreeNode, String> rootNode = null;
        try {
            rootNode = documentSelectionTree.add(uuids, boundwithForm.getDocType(), true);
        } catch (SolrServerException e) {
            //e.printStackTrace();
            LOG.error("refreshRightTree Exception:" + e);
        }
        boundwithForm.getRightTree().setRootElement(rootNode);
        return navigate(boundwithForm, result, request, response);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws SolrServerException
     */
    @RequestMapping(params = "methodToCall=removeFromLeftTree")
    public ModelAndView removeFromLeftTree(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws SolrServerException {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        Map<String, String> uuidLeftList = (Map<String, String>) request.getSession().getAttribute(OLEConstants.LEFT_LIST);
        if (uuidLeftList == null) {
            uuidLeftList = new HashMap<String, String>();
        }
        DocumentTreeNode bibDocument;
        Tree<DocumentTreeNode, String> tree = boundwithForm.getLeftTree();
        Node<DocumentTreeNode, String> rootElement = tree.getRootElement();
        List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
        for (Node<DocumentTreeNode, String> node : list) {
            bibDocument = node.getData();
            if (bibDocument.isSelect()) {
                uuidLeftList.remove(DocumentUniqueIDPrefix.getDocumentId(node.getNodeType()));
            }
        }
        request.getSession().setAttribute(OLEConstants.LEFT_LIST, uuidLeftList);
        Collection<String> uuids = uuidLeftList.values();
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        Node<DocumentTreeNode, String> rootNode = documentSelectionTree.add(uuids, boundwithForm.getDocType(), true);
        boundwithForm.getLeftTree().setRootElement(rootNode);
        return navigate(boundwithForm, result, request, response);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws SolrServerException
     */
    @RequestMapping(params = "methodToCall=removeFromRightTree")
    public ModelAndView removeFromRightTree(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) throws SolrServerException {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        Map<String, String> uuidRightList = (Map<String, String>) request.getSession().getAttribute(OLEConstants.RIGHT_LIST);
        if (uuidRightList == null) {
            uuidRightList = new HashMap<String, String>();
        }
        DocumentTreeNode bibDocument;
        Tree<DocumentTreeNode, String> tree = boundwithForm.getRightTree();
        Node<DocumentTreeNode, String> rootElement = tree.getRootElement();
        List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
        for (Node<DocumentTreeNode, String> node : list) {
            bibDocument = node.getData();
            if (bibDocument.isSelect()) {
                uuidRightList.remove(DocumentUniqueIDPrefix.getDocumentId(node.getNodeType()));
            }
        }
        request.getSession().setAttribute(OLEConstants.RIGHT_LIST, uuidRightList);
        Collection<String> uuids = uuidRightList.values();
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        Node<DocumentTreeNode, String> rootNode = documentSelectionTree.add(uuids, boundwithForm.getDocType(), true);
        boundwithForm.getRightTree().setRootElement(rootNode);
        return navigate(boundwithForm, result, request, response);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=selectRecords")
    public ModelAndView selectRecords(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        List<String> selectedRecordIds = new ArrayList<String>();
        List<OleWorkBibDocument> oleWorkBibDocuments = boundwithForm.getWorkBibDocumentList();
        for (OleWorkBibDocument oleWorkBibDocument : oleWorkBibDocuments) {
            if (oleWorkBibDocument.isSelect()) {
                selectedRecordIds.add(oleWorkBibDocument.getId());
            }
        }
        return getUIFModelAndView(boundwithForm);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws SolrServerException
     */
    @RequestMapping(params = "methodToCall=copyToTree")
    public ModelAndView copyToTree(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws SolrServerException {

        BoundwithForm boundwithForm = (BoundwithForm) form;
        String treeId = boundwithForm.getActionParamaterValue(OLEConstants.TREE_ID);
        if (boundwithForm.getViewId().equalsIgnoreCase(OLEConstants.ANALYTICS_VIEW) || boundwithForm.getViewId().equalsIgnoreCase("BoundwithView")) {
            Map<String, String> uuidLeftList = (Map<String, String>) request.getSession().getAttribute(OLEConstants.LEFT_LIST);
            Map<String, String> uuidRightList = (Map<String, String>) request.getSession().getAttribute(OLEConstants.RIGHT_LIST);
            if (!validateAnalyticsSelection(boundwithForm, uuidLeftList, uuidRightList, treeId)) {
                return super.navigate(boundwithForm, result, request, response);
            }
        }
        copyToTree(boundwithForm, treeId, request);
        return navigate(boundwithForm, result, request, response);
    }

    private Boolean validateAnalyticsSelection(BoundwithForm boundwithForm, Map<String, String> uuidLeftList, Map<String, String> uuidRightList, String treeId) {
        Collection<String> leftTreeUuids = null;
        Collection<String> rightTreeUuids = null;
        Collection<String> selectedRecordsUuidsList = null;
        String docType = boundwithForm.getDocType();
        if (StringUtils.isNotBlank(docType) && (DocType.HOLDINGS.getCode().equalsIgnoreCase(docType)
                || DocType.ITEM.getCode().equalsIgnoreCase(docType)
                || DocType.EHOLDINGS.getCode().equalsIgnoreCase(docType)
                || DocType.LICENSE.getCode().equalsIgnoreCase(docType))) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_ONLY_BIBS);
        }
        if (treeId.equalsIgnoreCase(OLEConstants.LEFT_TREE)) {
            if (!CollectionUtils.isEmpty(uuidLeftList)) {
                if (boundwithForm.getViewId().equalsIgnoreCase("BoundwithView")) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.LEFT_TREE_SECTION, OLEConstants.ERROR_BOUNDWITH_SELECT_BIBS);
                } else {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_ONLY_ONE_BIB_SERIES);
                }

            } else {
                uuidLeftList = selectedRecordsUuids(boundwithForm.getActionParamaterValue(OLEConstants.TREE_ID), boundwithForm);
                if (uuidLeftList.size() > 1) {
                    if (boundwithForm.getViewId().equalsIgnoreCase("BoundwithView")) {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.LEFT_TREE_SECTION, OLEConstants.ERROR_BOUNDWITH_SELECT_BIBS);
                    } else {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_ONLY_ONE_BIB_SERIES);
                    }
                }
                if (!CollectionUtils.isEmpty(uuidRightList)) {
                    rightTreeUuids = uuidRightList.values();
                    selectedRecordsUuidsList = selectedRecordsUuids(treeId, boundwithForm).values();
                    for (String uuid : selectedRecordsUuidsList) {
                        if (rightTreeUuids.contains(uuid)) {
                            if (boundwithForm.getViewId().equalsIgnoreCase("BoundwithView")) {
                                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.BOUNDWITH_SELECTION_SECTION, OLEConstants.ERROR_BOUNDWITH_SELECT_BIBS_TREE2);
                            } else {
                                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_BIB_IS_SERIES);
                            }
                        }
                    }
                }
            }

        } else if (treeId.equalsIgnoreCase(OLEConstants.RIGHT_TREE) && !CollectionUtils.isEmpty(uuidLeftList)) {
            leftTreeUuids = uuidLeftList.values();
            selectedRecordsUuidsList = selectedRecordsUuids(treeId, boundwithForm).values();
            for (String uuid : selectedRecordsUuidsList) {
                if (leftTreeUuids.contains(uuid)) {
                    if (boundwithForm.getViewId().equalsIgnoreCase("BoundwithView")) {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.BOUNDWITH_SELECTION_SECTION, OLEConstants.ERROR_BOUNDWITH_SELECT_BIBS_TREE1);
                    } else {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_BIB_IS_ANALYTIC);
                    }
                }
            }
        }
        int errorCount = GlobalVariables.getMessageMap().getErrorCount();
        if (errorCount > 0) {
            return false;
        } else {
            return true;
        }
    }

    private Map<String, String> selectedRecordsUuids(String treeId, BoundwithForm boundwithForm) {
        Map<String, String> uuidList = new HashMap<String, String>();
        List<SearchResultDisplayRow> searchResultDisplayRowList = boundwithForm.getSearchResultDisplayRowList();
        if (searchResultDisplayRowList != null && searchResultDisplayRowList.size() > 0) {
            for (SearchResultDisplayRow searchResultDisplayRow : searchResultDisplayRowList) {
                if (searchResultDisplayRow.isSelect()) {
                    if (treeId.equalsIgnoreCase(OLEConstants.LEFT_TREE)) {
                        uuidList.put(searchResultDisplayRow.getLocalId(), searchResultDisplayRow.getLocalId());
                    }
                    if (treeId.equalsIgnoreCase(OLEConstants.RIGHT_TREE)) {
                        uuidList.put(searchResultDisplayRow.getLocalId(), searchResultDisplayRow.getLocalId());
                    }
                }
            }
        }
        return uuidList;
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws SolrServerException
     */
    @RequestMapping(params = "methodToCall=clearTree")
    public ModelAndView clearTree(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) throws SolrServerException {

        BoundwithForm boundwithForm = (BoundwithForm) form;
        String treeId = boundwithForm.getActionParamaterValue(OLEConstants.TREE_ID);
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        Map<String, String> newMap = new HashMap<String, String>();
        Node<DocumentTreeNode, String> rootNode = documentSelectionTree.add(newMap.values(), DocType.BIB.getDescription(), true);
        if (treeId.equalsIgnoreCase(OLEConstants.LEFT_TREE)) {
            request.getSession().setAttribute(OLEConstants.LEFT_LIST, newMap);
            boundwithForm.getLeftTree().setRootElement(rootNode);
        } else if (treeId.equalsIgnoreCase(OLEConstants.RIGHT_TREE)) {
            request.getSession().setAttribute(OLEConstants.RIGHT_LIST, newMap);
            boundwithForm.getRightTree().setRootElement(rootNode);
        }
        return navigate(boundwithForm, result, request, response);
    }

    private void copyToTree(BoundwithForm boundwithForm, String treeId, HttpServletRequest request) throws SolrServerException {
        Collection<String> uuids = null;

        Map<String, String> uuidLeftList = (Map<String, String>) request.getSession().getAttribute(OLEConstants.LEFT_LIST);

        if (uuidLeftList == null) {
            uuidLeftList = new HashMap<String, String>();
        }
        Map<String, String> uuidRightList = (Map<String, String>) request.getSession().getAttribute(OLEConstants.RIGHT_LIST);

        if (uuidRightList == null) {
            uuidRightList = new HashMap<String, String>();
        }

        List<SearchResultDisplayRow> searchResultDisplayRowList = ((BoundwithForm) boundwithForm).getSearchResultDisplayRowList();
        if (searchResultDisplayRowList != null && searchResultDisplayRowList.size() > 0) {
            for (SearchResultDisplayRow searchResultDisplayRow : searchResultDisplayRowList) {
                if (searchResultDisplayRow.isSelect()) {
                    if (treeId.equalsIgnoreCase(OLEConstants.LEFT_TREE)) {
                        uuidLeftList.put(searchResultDisplayRow.getLocalId(), searchResultDisplayRow.getLocalId());
                        uuids = uuidLeftList.values();
                    }
                    if (treeId.equalsIgnoreCase(OLEConstants.RIGHT_TREE)) {
                        uuidRightList.put(searchResultDisplayRow.getLocalId(), searchResultDisplayRow.getLocalId());
                        uuids = uuidRightList.values();
                    }
                    searchResultDisplayRow.setSelect(false);
                }
            }

            request.getSession().setAttribute(OLEConstants.LEFT_LIST, uuidLeftList);
            request.getSession().setAttribute(OLEConstants.RIGHT_LIST, uuidRightList);

            DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
            Node<DocumentTreeNode, String> rootNode = documentSelectionTree.add(uuids, boundwithForm.getDocType(), false);
            LOG.info("Tree id-->" + treeId);
            if (treeId != null) {
                if (treeId.equalsIgnoreCase(OLEConstants.LEFT_TREE)) {
                    boundwithForm.setShowLeftTree(true);
                    boundwithForm.getLeftTree().setRootElement(rootNode);
                    boundwithForm.setLabelText("select");
                    showBoundWithBibs(boundwithForm, rootNode);
                }
                if (treeId.equalsIgnoreCase(OLEConstants.RIGHT_TREE)) {
                    boundwithForm.setShowRightTree(true);
                    boundwithForm.getRightTree().setRootElement(rootNode);
                    boundwithForm.setTree2LabelText("select");
                }
            }
        }

    }

    /**
     * This method displays the bound-wth bibs for the selected instance from left tree.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws SolrServerException
     */
    @RequestMapping(params = "methodToCall=showBoundwithBibs")
    public ModelAndView showBoundwithBibs(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        BoundwithForm boundwithForm = (BoundwithForm) form;

        Tree<DocumentTreeNode, String> tree1 = boundwithForm.getLeftTree();
        Node<DocumentTreeNode, String> rootElement = tree1.getRootElement();
        if (rootElement != null) {
            selectCheckedNodesForTree1(boundwithForm, rootElement);
            boundwithForm.setShowBoundwithTree(true);
        }
        boundwithForm.getDocumentTreeNode().setReturnCheck(true);
        List<String> selectedInstancesList = boundwithForm.getSelectedHoldings();
        List<String> boundwithBibs = new ArrayList<>();
        if (boundwithForm.getBibTree() != null) {
            String holdingsId = boundwithForm.getBibTree().getHoldingsTrees().get(0).getHoldings().getId();
            Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsId);
            if ((selectedInstancesList.size() > 0) && (selectedInstancesList.size() > 1)) {
                GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithTreeSection1", "error.boundwith.tree1");
                boundwithForm.setShowBoundwithTree(false);
            } else if ((selectedInstancesList.size() > 0) && (selectedInstancesList.size() == 1)) {
                String locationName = null;
                locationName = holdings.getLocationName();
                if (locationName != null) {
                    if (locationName.contains("<")) {
                        GlobalVariables.getMessageMap().putInfoForSectionId("BoundwithResultsSection", "info.boundwith.success", locationName.substring(0, locationName.indexOf("<")));
                    } else {
                        GlobalVariables.getMessageMap().putInfoForSectionId("BoundwithResultsSection", "info.boundwith.success", locationName);
                    }
                }
                boundwithForm.getDocumentTreeNode().setReturnCheck(true);
                LOG.info("selected instance-->" + holdings.getId());
                if (holdings.isBoundWithBib()) {
                    for (Bib bib : holdings.getBibs().getBibs()) {
                        boundwithBibs.add(bib.getId());
                    }
                } else {
                    boundwithBibs.add(holdings.getBib().getId());
                }
                DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
                Node<DocumentTreeNode, String> rootNode = documentSelectionTree.add(boundwithBibs, DocType.BIB.getDescription(), true);
                boundwithForm.getBoundwithTree().setRootElement(rootNode);
                boundwithForm.setShowBoundwithTree(true);
            } else {
                GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithTreeSection1", "error.boundwith.tree1");
                boundwithForm.setShowBoundwithTree(false);
            }

        } else {
            GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithTreeSection1", "error.boundwith.tree1");
            boundwithForm.setShowBoundwithTree(false);
        }
        boundwithForm.setSelectedHoldings(null);
        return getUIFModelAndView(boundwithForm);
    }
/*
    @RequestMapping(params = "methodToCall=submitTree1CheckBoxValues")
    public ModelAndView submitCheckBoxValues(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        List<DocumentTreeNode> bibDocumentList = boundwithForm.getDisplayRecordList();
        bibDocumentList.clear();
        DocumentTreeNode bibDocument;
        Tree<DocumentTreeNode, String> tree2 = boundwithForm.getLeftTree();
        Node<DocumentTreeNode, String> rootElement = tree2.getRootElement();
        List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
        for (Node<DocumentTreeNode, String> node : list) {
            bibDocument = node.getData();                        selectCheckedNodesForTree1
            LOG.info("is tree1 selected-->" + bibDocument.isSelect());
            if (bibDocument.isSelect()) {
                bibDocumentList.add(bibDocument);
            }
            List<Node<DocumentTreeNode, String>> childrenList = node.getChildren();
            for (Node<DocumentTreeNode, String> subNode : childrenList) {
                bibDocument = subNode.getData();
                if (bibDocument.isSelect()) {
                    bibDocumentList.add(bibDocument);
                }

            }
        }

        return getUIFModelAndView(boundwithForm);
    }

    @RequestMapping(params = "methodToCall=submitTree2CheckBoxValues")
    public ModelAndView submitTree2CheckBoxValues(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                  HttpServletRequest request, HttpServletResponse response) {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        List<DocumentTreeNode> bibDocumentList = boundwithForm.getDisplayRecordList();
        bibDocumentList.clear();
        DocumentTreeNode bibDocument;
        Tree<DocumentTreeNode, String> tree2 = boundwithForm.getRightTree();
        Node<DocumentTreeNode, String> rootElement = tree2.getRootElement();
        List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
        for (Node<DocumentTreeNode, String> node : list) {
            bibDocument = node.getData();
            if (bibDocument.isSelect()) {

                bibDocumentList.add(bibDocument);
            }
            List<Node<DocumentTreeNode, String>> childrenList = node.getChildren();
            for (Node<DocumentTreeNode, String> subNode : childrenList) {
                bibDocument = subNode.getData();
                if (bibDocument.isSelect()) {
                    bibDocumentList.add(bibDocument);
                }

            }
        }

        return navigate(boundwithForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=submitBoundwithTreeCheckBoxValues")
    public ModelAndView submitBoundwithTreeCheckBoxValues(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                          HttpServletRequest request, HttpServletResponse response) {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        List<DocumentTreeNode> bibDocumentList = boundwithForm.getDisplayRecordList();
        bibDocumentList.clear();
        DocumentTreeNode bibDocument;
        Tree<DocumentTreeNode, String> tree2 = boundwithForm.getBoundwithTree();
        Node<DocumentTreeNode, String> rootElement = tree2.getRootElement();
        List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
        for (Node<DocumentTreeNode, String> node : list) {
            bibDocument = node.getData();
            LOG.info("is tree1 selected-->" + bibDocument.isSelect());
            if (bibDocument.isSelect()) {
                bibDocumentList.add(bibDocument);
            }
            List<Node<DocumentTreeNode, String>> childrenList = node.getChildren();
            for (Node<DocumentTreeNode, String> subNode : childrenList) {
                bibDocument = subNode.getData();
                if (bibDocument.isSelect()) {
                    bibDocumentList.add(bibDocument);
                }

            }
        }

        return navigate(boundwithForm, result, request, response);
    }

*/

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    /*@RequestMapping(params = "methodToCall=selectTreeNodes")
    public ModelAndView selectTreeNodes(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        String treeId = boundwithForm.getActionParamaterValue(OLEConstants.TREE_ID);
        LOG.info("treeId-->" + treeId);
        if (treeId.equalsIgnoreCase(OLEConstants.LEFT_TREE)) {
            Tree<DocumentTreeNode, String> tree2 = boundwithForm.getLeftTree();
            Node<DocumentTreeNode, String> rootElement = tree2.getRootElement();
            selectCheckedNodesForTree1(boundwithForm, rootElement);
        }
        if (treeId.equalsIgnoreCase(OLEConstants.RIGHT_TREE)) {
            Tree<DocumentTreeNode, String> tree2 = boundwithForm.getRightTree();
            Node<DocumentTreeNode, String> rootElement = tree2.getRootElement();
            selectCheckedNodesForTree2(boundwithForm, rootElement);
        }
        if (treeId.equalsIgnoreCase(("boundwithTree"))) {
            Tree<DocumentTreeNode, String> boundwithTree = boundwithForm.getBoundwithTree();
            Node<DocumentTreeNode, String> rootElement = boundwithTree.getRootElement();
            selectCheckedNodesForBoundwith(boundwithForm, rootElement);
        }
        return getUIFModelAndView(boundwithForm);
    }*/


    /**
     * This method displays the bound with results if the uses clicks on Bound-with button.
     *
     * @param form
     * @param result
     * @param httpResponse
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=performBoundwith")
    public ModelAndView performBoundwith(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletResponse httpResponse, HttpServletRequest httpServletRequest) throws Exception {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        /*boolean hasPermission = canPerformBoundWith(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasPermission) {
            GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", OLEConstants.ERROR_AUTHORIZATION);
            return navigate(boundwithForm, result, httpServletRequest, httpResponse);
        }*/
        Tree<DocumentTreeNode, String> tree1 = boundwithForm.getLeftTree();
        Node<DocumentTreeNode, String> rootElement = tree1.getRootElement();
        selectCheckedNodesForTree1(boundwithForm, rootElement);

        Tree<DocumentTreeNode, String> tree2 = boundwithForm.getRightTree();
        Node<DocumentTreeNode, String> tree2RootElement = tree2.getRootElement();
        selectCheckedNodesForTree2(boundwithForm, tree2RootElement);
        boundwithForm.getDocumentTreeNode().setReturnCheck(true);
        String validateMsg = validateInput(boundwithForm);
        LOG.info("validate msg -->" + validateMsg);
        if (validateMsg.contains("success")) {
            performBoundwith(boundwithForm);
        } /*else {
            boundwithForm.setSelectedInstance(validateMsg);
        }*/
        return navigate(boundwithForm, result, httpServletRequest, httpResponse);
    }


    /**
     * @param form
     * @param result
     * @param request
     * @param httpResponse
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=unbind")
    public ModelAndView unbind(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse httpResponse) throws Exception {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        /*String operation = "unbind";

        Tree<DocumentTreeNode, String> boundwithTree = boundwithForm.getBoundwithTree();
        Node<DocumentTreeNode, String> boundwithTreerootElement = boundwithTree.getRootElement();
        selectCheckedNodesForBoundwith(boundwithForm, boundwithTreerootElement);
        LOG.info("Unbind status -->" + boundwithForm.getSelectedInstance());
        if ((boundwithForm.getSelectedInstance().contains("success"))) {

            DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
            //boundwithForm.setSelectedInstance("Instance " + boundwithForm.getWorkInstanceDocumentForTree1().getHoldingsDocument().getLocationName() + "\t bounded with the following bibs :");
            GlobalVariables.getMessageMap().putInfoForSectionId("BoundwithResultsSection", "info.boundwith.success", boundwithForm.getWorkInstanceDocumentForTree1().getHoldingsDocument().getLocationName());
            String unbindResponse = getResponseFromDocStore(operation, boundwithForm);
            Response response = new ResponseHandler().toObject(unbindResponse);

            List<ResponseDocument> responseDocumentList = response.getDocuments();
            List<String> uuidList = new ArrayList<String>();
            for (ResponseDocument responseDocument : responseDocumentList) {
                List<ResponseDocument> linkedResponseDocuments = responseDocument.getLinkedDocuments();
                for (ResponseDocument linkedResponseDocument : linkedResponseDocuments) {
                    uuidList.add(linkedResponseDocument.getId());
                }
            }
            Set<String> selectedBibsList = boundwithForm.getSelectedBibsList();
            if (selectedBibsList != null) {
                selectedBibsList.add(boundwithForm.getTree1BibId());
                Node<DocumentTreeNode, String> rootNode = documentSelectionTree.add(selectedBibsList, DocType.BIB.getDescription());
                boundwithForm.getBoundwithTree().setRootElement(rootNode);
                boundwithForm.setBoundwithTreeLabelText("select");
            }
        }*/
        return getUIFModelAndView(boundwithForm);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param httpResponse
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=deleteVerify")
    public ModelAndView deleteVerify(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse httpResponse) throws Exception {

        BoundwithForm boundwithForm = (BoundwithForm) form;
        boundwithForm.setInDelete("false");
        Tree<DocumentTreeNode, String> boundwithTree = boundwithForm.getBoundwithTree();
        Node<DocumentTreeNode, String> rootElement = boundwithTree.getRootElement();
        selectCheckedNodesForTree1(boundwithForm, rootElement);
        List<String> selectedInstancesList = boundwithForm.getSelectedHoldings();

        if (selectedInstancesList != null && selectedInstancesList.size() > 0) {
            boundwithForm.setDocCategory(DocCategory.WORK.getCode());
            boundwithForm.setDocType(DocType.HOLDINGS.getCode());
            boundwithForm.setDocFormat(DocFormat.OLEML.getCode());
            String deleteResponse = TransferUtil.getInstance().checkItemExistsInOleForHoldings(selectedInstancesList);
            LOG.info("delete response-->" + deleteResponse);
            boolean isBoundwith = TransferUtil.getInstance().checkItemIsBoundWith(selectedInstancesList);
            boundwithForm.setDeleteVerifyResponse(deleteResponse);
            if (!isBoundwith) {
                if (boundwithForm.getDeleteVerifyResponse().equalsIgnoreCase("success")) {
                    boundwithForm.setInDelete("true");
                    DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
                    Node<DocumentTreeNode, String> docTree = documentSelectionTree.add(selectedInstancesList, boundwithForm.getDocType(), true);
                    boundwithForm.getDeleteConfirmationTree().setRootElement(docTree);
                } else {
                    GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "error.boundwith.delete.instance");
                }
            } else {
                GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "info.boundwith.delete.failure");
            }
        }/*else{
            GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "delete.boundwith.failure");
        }*/
        return getUIFModelAndView(boundwithForm);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param httpResponse
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=delete")
    public ModelAndView delete(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse httpResponse) throws Exception {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        boundwithForm.setInDelete("true");
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        String deleteVerifyResponse = boundwithForm.getDeleteVerifyResponse();
        if (deleteVerifyResponse.contains("Failed")) {
            GlobalVariables.getMessageMap().putInfoForSectionId("BoundwithResultsSection", "error.boundwith.delete.instance");
        } else if (deleteVerifyResponse.contains("success")) {
            List<String> selectedHoldings = boundwithForm.getSelectedHoldings();
            for (String id : selectedHoldings) {
                getDocstoreClientLocator().getDocstoreClient().deleteHoldings(id);
            }
            Node<DocumentTreeNode, String> rootNode = documentSelectionTree.add(boundwithForm.getSelectedBibs(), DocType.BIB.getDescription(), true);
            boundwithForm.getBoundwithTree().setRootElement(rootNode);
            boundwithForm.setShowBoundwithTree(true);
            GlobalVariables.getMessageMap().putInfoForSectionId("BoundwithResultsSection", "info.boundwith.delete.success");
            boundwithForm.setInDelete("false");
        }
        return getUIFModelAndView(boundwithForm);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=selectBoundwithTreeNodes")
    public ModelAndView selectBoundwithTree1Nodes(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        Tree<DocumentTreeNode, String> tree2 = boundwithForm.getBoundwithTree();
        Node<DocumentTreeNode, String> rootElement = tree2.getRootElement();
        selectCheckedNodesForBoundwith(boundwithForm, rootElement);
        return getUIFModelAndView(boundwithForm);
    }

    private void performBoundwith(BoundwithForm boundwithForm) {
        try {
            getDocstoreClientLocator().getDocstoreClient().boundWithBibs(boundwithForm.getSelectedHoldingsFromTree1().get(0), boundwithForm.getSelectedBibsFromTree2());
            boundwithForm.setShowBoundwithTree(true);
            List<String> uuidList = new ArrayList<String>();
            uuidList = boundwithForm.getSelectedBibs();
            uuidList.add(boundwithForm.getTree1BibId());
            DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
            Node<DocumentTreeNode, String> rootNode = documentSelectionTree.add(uuidList, DocType.BIB.getDescription(), true);
            boundwithForm.getBoundwithTree().setRootElement(rootNode);
            GlobalVariables.getMessageMap().putInfo("BoundwithResultsSection", "info.boundwith.success", boundwithForm.getMessage());
            boundwithForm.setBoundwithTreeLabelText("select");
        } catch (DocstoreException e) {
            GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", e.getErrorCode());
            boundwithForm.setShowBoundwithTree(false);
        } catch (Exception e) {
            GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "error.boundwith.failed", e.getMessage());
            boundwithForm.setShowBoundwithTree(false);
        }
        boundwithForm.setSelectedHoldings(null);
        //String operation = "bind";
        //return getResponseFromDocStore(operation, boundwithForm);

    }

    /*private String getResponseFromDocStore(String operation, BoundwithForm boundwithForm) throws Exception {

        String bindResponse = null;

        *//*Request request = buildRequest(operation, boundwithForm);
        if (validateBoundwithRequest(request)) {
            String stringContent = new RequestHandler().toXML(request);
            LOG.info("request-->" + stringContent);
            String restfulUrl = ConfigContext.getCurrentContextConfig().getProperty("docstore.restful.url");
            restfulUrl = restfulUrl.concat("/") + "bind";
            LOG.info("restful url-->" + restfulUrl);
            HttpClient client = new HttpClient();
            PutMethod putMethod = new PutMethod(restfulUrl);
            NameValuePair nvp1 = new NameValuePair("stringContent", stringContent);
            putMethod.setQueryString(new NameValuePair[]{nvp1});
            int statusCode = client.executeMethod(putMethod);
            InputStream inputStream = putMethod.getResponseBodyAsStream();
            bindResponse = IOUtils.toString(inputStream, "UTF-8");
            LOG.info("bindResponse-->" + bindResponse);
        }*//*
        return bindResponse;

    }*/

    private boolean validateBoundwithRequest(Request request) {
        boolean isValid = false;
        List<RequestDocument> requestDocumentList = request.getRequestDocuments();
        if ((requestDocumentList.size() > 0) && (requestDocumentList.size() == 1)) {
            for (RequestDocument requestDocument : requestDocumentList) {
                if (requestDocument.getType().equalsIgnoreCase(DocType.INSTANCE.getCode())) {
                    isValid = true;
                }
            }
        } else {
            isValid = false;
        }
        return isValid;
    }

    private String validateInput(BoundwithForm boundwithForm) {
        String validate = validate(boundwithForm);
        StringBuilder validateMsg = new StringBuilder();
        if (validate.startsWith("failiure")) {
            boundwithForm.setShowBoundwithTree(false);
            validateMsg.append(validate);
            return validateMsg.toString();
        } else if (validate.startsWith("success")) {
            GlobalVariables.getMessageMap().clearErrorMessages();
        }
        List<String> selectedHoldingsFromTree1 = boundwithForm.getSelectedHoldingsFromTree1();
        List<String> selectedBibs = boundwithForm.getSelectedBibsFromTree2();
        if (selectedHoldingsFromTree1 != null && selectedBibs != null) {
            if ((selectedHoldingsFromTree1.size() > 0) && (selectedHoldingsFromTree1.size() == 1)) {
                List<Item> items = boundwithForm.getBibTree().getHoldingsTrees().get(0).getItems();
                if (items.size() > 1) {
                    GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "error.boundwith.more.items.failure");
                } else if (selectedBibs.size() > 0) {
                    validateMsg.append("success");
                } else {
                    //GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "error.boundwith.tree2");
                }
            } else {
                GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "error.boundwith.tree1");
            }
        }
        return validateMsg.toString();
    }

   /* private String getDeleteResponseFromDocStore(String operation, StringBuilder instanceIdList, BoundwithForm boundwithForm) throws IOException {
        String restfulUrl = ConfigContext.getCurrentContextConfig().getProperty("docstore.restful.url");
        restfulUrl = restfulUrl.concat("/") + instanceIdList.toString();
        HttpClient httpClient = new HttpClient();
        DeleteMethod deleteMethod = new DeleteMethod(restfulUrl);
        NameValuePair nvp1 = new NameValuePair("identifierType", "UUID");
        NameValuePair nvp2 = new NameValuePair("operation", operation);

        NameValuePair category = new NameValuePair("docCategory", boundwithForm.getDocCategory());
        NameValuePair type = new NameValuePair("docType", boundwithForm.getDocType());
        NameValuePair format = new NameValuePair("docFormat", boundwithForm.getDocFormat());
        deleteMethod.setQueryString(new NameValuePair[]{nvp1, nvp2, category, type, format});
        int statusCode = httpClient.executeMethod(deleteMethod);
        LOG.info("statusCode-->" + statusCode);
        InputStream inputStream = deleteMethod.getResponseBodyAsStream();
        return IOUtils.toString(inputStream);
    }*/


    /*private Node<DocumentTreeNode, String> buildDocSelectionTree(String responseXml) throws SolrServerException {
        Response response = new ResponseHandler().toObject(responseXml);
        List<ResponseDocument> responseDocumentList = response.getDocuments();
        List<WorkItemDocument> workItemDocumentList = new ArrayList<WorkItemDocument>();
        List<WorkBibDocument> bibDocumentList = new ArrayList<WorkBibDocument>();
        List<WorkInstanceDocument> instanceDocumentList = new ArrayList<WorkInstanceDocument>();
        WorkHoldingsDocument workHoldingsDocument = null;
        WorkInstanceDocument workInstanceDocument = null;
        WorkBibDocument workBibDocument = null;
        WorkItemDocument workItemDocument = null;
        List<String> uuidList = new ArrayList<String>();
        String docType = null;

        for (ResponseDocument responseDocument : responseDocumentList) {

            if (responseDocument.getType().equalsIgnoreCase(DocType.ITEM.getCode())) {
                workItemDocument = new WorkItemDocument();
                workItemDocument.setItemIdentifier(responseDocument.getUuid());
                uuidList.add(responseDocument.getUuid());

                docType = responseDocument.getType();
                workItemDocumentList.add(workItemDocument);

            } else if (responseDocument.getType().equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
                workHoldingsDocument = new WorkHoldingsDocument();
                docType = responseDocument.getType();
                workHoldingsDocument.setHoldingsIdentifier(responseDocument.getUuid());
                uuidList.add(responseDocument.getUuid());
            } else if (responseDocument.getType().equalsIgnoreCase(DocType.BIB.getDescription())) {
                workBibDocument = new WorkBibDocument();
                uuidList.add(responseDocument.getUuid());
                workBibDocument.setId(responseDocument.getUuid());
                docType = responseDocument.getType();
                bibDocumentList.add(workBibDocument);


            } else if (responseDocument.getType().equalsIgnoreCase(DocType.INSTANCE.getCode())) {
                workInstanceDocument = new WorkInstanceDocument();
                workInstanceDocument.setInstanceIdentifier(responseDocument.getUuid());
                uuidList.add(responseDocument.getUuid());
                docType = responseDocument.getType();
                instanceDocumentList.add(workInstanceDocument);
            }
        }
        if (workItemDocumentList.size() > 0) {
            if (workInstanceDocument != null) {
                workInstanceDocument.setHoldingsDocument(workHoldingsDocument);
                workInstanceDocument.setItemDocumentList(workItemDocumentList);
            }
        }
        if (instanceDocumentList.size() > 0) {
            if (workBibDocument != null) {
                workBibDocument.setWorkInstanceDocumentList(instanceDocumentList);
            }
        }

        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();

        Node<DocumentTreeNode, String> rootNode = documentSelectionTree.add(uuidList, docType);


        return rootNode;
    }*/

    /*private Request buildRequest(String operation, BoundwithForm boundwithForm) {
        List<String> selectedInstancesList = boundwithForm.getSelectedInstancesList();
        Set<String> selectedBibsList = boundwithForm.getSelectedBibsList();
        Request request = new Request();
        request.setUser("ole-khuntley");
        request.setOperation(operation);
        RequestDocument requestDocument = new RequestDocument();
        List<RequestDocument> requestDocumentList = new ArrayList<RequestDocument>();
        List<RequestDocument> linkedRequestDocumentList = new ArrayList<RequestDocument>();

        requestDocument.setUuid(selectedInstancesList.get(0));
        requestDocument.setId(selectedInstancesList.get(0));
        requestDocument.setType(DocType.INSTANCE.getCode());
        requestDocument.setCategory(DocCategory.WORK.getCode());
        requestDocument.setFormat(DocFormat.OLEML.getCode());
        for (String bibId : selectedBibsList) {
            RequestDocument linkedRequestDocument = new RequestDocument();
            linkedRequestDocument.setUuid(bibId);
            linkedRequestDocument.setCategory(DocCategory.WORK.getCode());
            linkedRequestDocument.setType(DocType.BIB.getDescription());
            linkedRequestDocument.setId(bibId);
            linkedRequestDocument.setFormat(DocFormat.MARC.getCode());
            linkedRequestDocumentList.add(linkedRequestDocument);

        }
        requestDocument.setLinkedRequestDocuments(linkedRequestDocumentList);
        requestDocumentList.add(requestDocument);
        request.setRequestDocuments(requestDocumentList);

        return request;

    }*/


    private void selectCheckedNodesForTree1(BoundwithForm boundwithForm, Node<DocumentTreeNode, String> rootElement) {
        DocumentTreeNode documentTreeNode;
        BibTree bibTree = new BibTree();
        HoldingsTree holdingsTree = new HoldingsTree();
        Holdings holdings = new Holdings();
        Set<String> selectedBibsList = new HashSet<String>();
        List<String> selectedBibs = new ArrayList<String>();
        List<String> selectedBibsFromTree1 = new ArrayList<String>();
        List<String> selectedInstancesList = new ArrayList<String>();
        List<String> selectedHoldings = new ArrayList<String>();
        List<String> selectedHoldingsFromTree1 = new ArrayList<String>();
        String tree1BibId = null;
        Bib bib = new Bib();
        List<Item> items = new ArrayList<Item>();
        List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
        for (Node<DocumentTreeNode, String> bibNode : list) {
            documentTreeNode = bibNode.getData();
            LOG.info("documentTreeNode.isSelectTree1()-->" + documentTreeNode.isSelect());
            if (documentTreeNode.isSelect()) {
                GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "error.select.instance");
            } else {
                List<Node<DocumentTreeNode, String>> instanceList = bibNode.getChildren();
                for (Node<DocumentTreeNode, String> instance : instanceList) {
                    documentTreeNode = instance.getData();
                    LOG.info("node1.getData()-->" + instance.getData().getTitle());
                    if (documentTreeNode.isSelect()) {
                        bib.setId(bibNode.getNodeType());
                        selectedBibsList.add(bibNode.getNodeType());
                        selectedBibs.add(bibNode.getNodeType());
                        selectedBibsFromTree1.add(bibNode.getNodeType());
                        bib.setTitle(bibNode.getNodeLabel());
                        tree1BibId = bibNode.getNodeType();
                        boundwithForm.setTree1BibId(tree1BibId);
                        LOG.info("documentTreeNode.isSelectTree1() in else-->" + documentTreeNode.isSelect());
                        LOG.info("inst id-->" + instance.getNodeType());
                        holdings.setId(instance.getNodeType());
                        selectedInstancesList.add(instance.getNodeType());
                        selectedHoldings.add(instance.getNodeType());
                        selectedHoldingsFromTree1.add(instance.getNodeType());
                        holdings.setLocationName(instance.getNodeLabel());
                        boundwithForm.setMessage(instance.getNodeLabel());
                        documentTreeNode.setSelect(true);
                        List<Node<DocumentTreeNode, String>> itemList = instance.getChildren();
                        for (Node<DocumentTreeNode, String> item : itemList) {
                            Item itemDoc = new Item();
                            documentTreeNode = item.getData();
                            itemDoc.setId(item.getNodeType());
                            itemDoc.setCallNumber(item.getNodeLabel());
                            documentTreeNode.setSelect(true);
                            items.add(itemDoc);
                        }
                        holdingsTree.getItems().addAll(items);
                        holdingsTree.setHoldings(holdings);
                        bibTree.getHoldingsTrees().add(holdingsTree);
                        bibTree.setBib(bib);
                        boundwithForm.setBibTree(bibTree);
                        boundwithForm.setSelectedBibsList(selectedBibsList);
                        boundwithForm.setSelectedInstancesList(selectedInstancesList);
                        boundwithForm.setSelectedBibs(selectedBibs);
                        boundwithForm.setSelectedHoldings(selectedHoldings);
                        boundwithForm.setSelectedBibsFromTree1(selectedBibs);
                        boundwithForm.setSelectedHoldingsFromTree1(selectedHoldings);
                    }
                }
            }
        }
    }

    private String validate(BoundwithForm boundwithForm) {
        String string = new String();
        Tree<DocumentTreeNode, String> leftTree = boundwithForm.getLeftTree();
        DocumentTreeNode documentTreeNode;
        int count = 0;
        List<Node<DocumentTreeNode, String>> list = leftTree.getRootElement().getChildren();
        for (Node<DocumentTreeNode, String> bibNode : list) {
            documentTreeNode = bibNode.getData();
            //LOG.info("documentTreeNode.isSelectTree1()-->" + documentTreeNode.isSelect());
            /*if (documentTreeNode.isSelect()) {
                stringBuilder.append("failuire");
                GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "error.select.instance");
            }else {*/
            List<Node<DocumentTreeNode, String>> instanceList = bibNode.getChildren();
            for (Node<DocumentTreeNode, String> instance : instanceList) {
                documentTreeNode = instance.getData();
                if (documentTreeNode.isSelect()) {
                    count++;
                }
                    /*if (!documentTreeNode.isSelect()) {
                        *//*boundwithForm.setMessage(instance.getNodeLabel());
                        documentTreeNode.setSelect(true);
                        List<Node<DocumentTreeNode, String>> itemList = instance.getChildren();
                        for (Node<DocumentTreeNode, String> item : itemList) {
                            Item itemDoc = new Item();
                            documentTreeNode = item.getData();
                            itemDoc.setId(item.getNodeType());
                            itemDoc.setCallNumber(item.getNodeLabel());
                            if (documentTreeNode.isSelect()) {
                                stringBuilder.append("failuire");
                                GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "error.select.instance");
                            }
                        }*//*
                        string = new String("failiure");
                        GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "error.select.instance");
                    }*/
            }
            if (count == 0) {
                GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "error.select.instance");
                string = new String("failiure");
            }
            //}
        }
        List<Node<DocumentTreeNode, String>> rightlist = boundwithForm.getRightTree().getRootElement().getChildren();
        count = 0;
        for (Node<DocumentTreeNode, String> bibNode : rightlist) {
            documentTreeNode = bibNode.getData();
            if (documentTreeNode.isSelect()) {
                count++;
            }
        }
        if (count == 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithResultsSection", "error.boundwith.select.bib");
            string = new String("failiure");
        }
        return string;
    }


    private void selectCheckedNodesForTree2(BoundwithForm boundwithForm, Node<DocumentTreeNode, String> rootElement) {
        DocumentTreeNode documentTreeNode;
        BibTree bibTree = new BibTree();
        HoldingsTree holdingsTree = new HoldingsTree();
        Set<String> selectedBibsList = new HashSet<String>();
        ArrayList<String> setSelectedBibsFromTree2 = new ArrayList<String>();
        ArrayList<String> selectedBibs = new ArrayList<String>();
        if (rootElement != null) {
            List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
            for (Node<DocumentTreeNode, String> node : list) {
                documentTreeNode = node.getData();
                LOG.info("documentTreeNode.isSelect()-->" + documentTreeNode.isSelect());
                if (documentTreeNode.isSelect()) {
                    Bib bib = new Bib();
                    Holdings holdings = new Holdings();
                    List<Item> items = new ArrayList<Item>();
                    bib.setId(node.getNodeType());
                    selectedBibsList.add(node.getNodeType());
                    selectedBibs.add(node.getNodeType());
                    setSelectedBibsFromTree2.add(node.getNodeType());
                    bib.setTitle(node.getNodeLabel());
                    List<Node<DocumentTreeNode, String>> childrenList = node.getChildren();
                    for (Node<DocumentTreeNode, String> subNode : childrenList) {
                        documentTreeNode = subNode.getData();
                        holdings.setId(subNode.getNodeType());
                        documentTreeNode.setSelect(true);
                        holdings.setLocationName(subNode.getNodeLabel());
                        List<Node<DocumentTreeNode, String>> childrenList1 = subNode.getChildren();
                        for (Node<DocumentTreeNode, String> subNode1 : childrenList1) {
                            Item item = new Item();
                            documentTreeNode = subNode1.getData();
                            item.setId(subNode1.getNodeType());
                            item.setCallNumber(subNode1.getNodeLabel());
                            items.add(item);
                            documentTreeNode.setSelect(true);
                        }
                    }
                    boundwithForm.setSelectedBibsList(selectedBibsList);
                    boundwithForm.setSelectedBibsFromTree2(setSelectedBibsFromTree2);
                    boundwithForm.setSelectedBibs(selectedBibs);
                    holdingsTree.getItems().addAll(items);
                    holdingsTree.setHoldings(holdings);
                    bibTree.getHoldingsTrees().add(holdingsTree);
                    bibTree.setBib(bib);
                } else {
                    List<Node<DocumentTreeNode, String>> childrenList = node.getChildren();
                    for (Node<DocumentTreeNode, String> node1 : childrenList) {
                        documentTreeNode = node1.getData();
                        if (documentTreeNode.isSelect()) {
                            List<Node<DocumentTreeNode, String>> childrenList1 = node1.getChildren();
                            for (Node<DocumentTreeNode, String> subNode : childrenList1) {
                                documentTreeNode = subNode.getData();
                                documentTreeNode.setSelect(true);

                            }
                        }
                    }
                }
            }
        }
    }

    private void selectCheckedNodesForBoundwith(BoundwithForm boundwithForm, Node<DocumentTreeNode, String> rootElement) {
        DocumentTreeNode documentTreeNode;
        List<String> selectedInstancesList = new ArrayList<String>();
        Set<String> selectedBibsList = new HashSet<String>();
        List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
        for (Node<DocumentTreeNode, String> node : list) {
            documentTreeNode = node.getData();
            if (documentTreeNode.isSelect()) {
                GlobalVariables.getMessageMap().putErrorForSectionId("BoundwithTreeSection1", "error.select.node.instance");

            } else {
                List<Node<DocumentTreeNode, String>> childrenList = node.getChildren();
                for (Node<DocumentTreeNode, String> node1 : childrenList) {
                    documentTreeNode = node1.getData();

                    if (documentTreeNode.isSelect()) {
                        LOG.info("documentTreeNode.isSelectboundwithTree() in else-->" + documentTreeNode.isSelect());
                        selectedBibsList.add(node.getNodeType());
                        selectedInstancesList.add(node1.getNodeType());
                        boundwithForm.setSelectedBibsList(selectedBibsList);
                        boundwithForm.setSelectedInstancesList(selectedInstancesList);
                        documentTreeNode.setSelect(true);
                        List<Node<DocumentTreeNode, String>> childrenList1 = node1.getChildren();
                        for (Node<DocumentTreeNode, String> subNode : childrenList1) {
                            documentTreeNode = subNode.getData();
                            documentTreeNode.setSelect(true);

                        }
                    }
                }
            }
        }
    }

    /**
     * Enable, disable the next and previous and also show the message for number of entries
     *
     * @param boundwithForm
     * @return
     */

    public SearchResultDisplayFields getDisplayFields(BoundwithForm boundwithForm) {
        SearchResultDisplayFields searchResultDisplayFields = new SearchResultDisplayFields();
        searchResultDisplayFields.buildSearchResultDisplayFields(documentSearchConfig.getDocTypeConfigs(), boundwithForm.getDocType());
        return searchResultDisplayFields;
    }

    public void setPageNextPreviousAndEntriesInfo(BoundwithForm boundwithForm) {
        this.totalRecCount = boundwithForm.getSearchResponse().getTotalRecordCount();
        this.start = boundwithForm.getSearchResponse().getStartIndex();
        this.pageSize = boundwithForm.getSearchResponse().getPageSize();
        boundwithForm.setPreviousFlag(getWorkbenchPreviousFlag());
        boundwithForm.setNextFlag(getWorkbenchNextFlag());
        boundwithForm.setPageShowEntries(getWorkbenchPageShowEntries());
    }

    @RequestMapping(params = "methodToCall=facetSearch")
    public ModelAndView facetSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        String docType = request.getParameter("docType");
        String selectedFacet = request.getParameter("selectedFacet");
        String selectedFacetName = request.getParameter("selectedFacetName");
        boundwithForm.setDocType(docType);
        if (boundwithForm.getSearchParams() == null) {
            SearchParams searchParams = (SearchParams) request.getSession().getAttribute("searchParams");
            boundwithForm.setSearchParams(searchParams);
        }
        boundwithForm.getSearchParams().getFacetFields().addAll(getFacetFields(boundwithForm.getDocType()));
        boundwithForm.getSearchParams().setFacetPrefix("");
        boundwithForm.getSearchParams().setFacetLimit(documentSearchConfig.getFacetPageSizeShort());
        FacetCondition facetCondition = new FacetCondition();
        facetCondition.setFieldName(selectedFacetName);
        facetCondition.setFieldValue(selectedFacet);
        boundwithForm.getSearchParams().getFacetConditions().add(facetCondition);
        boundwithForm.setSearchType("search");
        GlobalVariables.getMessageMap().clearErrorMessages();
        return search(boundwithForm, result, request, response);
    }

    public Set<String> getFacetFields(String docType) {
        Set<String> facetFields = new TreeSet<String>();
        for (DocTypeConfig docTypeConfig : documentSearchConfig.getDocTypeConfigs()) {
            if (docTypeConfig.getName().equalsIgnoreCase(docType)) {
                for (DocFormatConfig docFormatConfig : docTypeConfig.getDocFormatConfigList()) {
                    if (docFormatConfig.getName().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocFormat.MARC.getCode())) {
                        for (DocFieldConfig docFieldConfig : docFormatConfig.getDocFieldConfigList()) {
                            if (docFieldConfig.getName().endsWith("_facet") && docFieldConfig.getDocType().getName().equalsIgnoreCase(docType)) {
                                facetFields.add(docFieldConfig.getName());
                            }
                        }
                    }
                }
            }
        }
        return facetFields;
    }


    @RequestMapping(params = "methodToCall=addLineField")
    public ModelAndView addLineField(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {

        String selectedCollectionPath = uifForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for add line action, cannot add new line");
        }
        BoundwithForm boundwithForm = (BoundwithForm) uifForm;
        int index = Integer.parseInt(boundwithForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if (boundwithForm.getSearchConditions().get(index).getSearchField().getFieldName().isEmpty() &&
                boundwithForm.getSearchConditions().get(index).getSearchField().getFieldValue().isEmpty()) {
            return getUIFModelAndView(uifForm);
        }
        List<SearchCondition> searchConditions = boundwithForm.getSearchConditions();
        String previousDocType =boundwithForm.getSearchConditions().get(index).getSearchField().getDocType();
        index++;
        SearchCondition searchCondition=new SearchCondition();
        searchCondition.setOperator("AND");
        SearchField searchField = new SearchField();
        searchField.setDocType(previousDocType);
        searchField.setFieldName("any");
        searchCondition.setSearchField(searchField);
        searchConditions.add(index,searchCondition);
        return getUIFModelAndView(uifForm);
    }

    @RequestMapping(params = "methodToCall=deleteLineField")
    public ModelAndView deleteLineField(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {

        String selectedCollectionPath = uifForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set for add line action, cannot add new line");
        }
        BoundwithForm boundwithForm = (BoundwithForm) uifForm;
        int index = Integer.parseInt(boundwithForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        List<SearchCondition> searchConditions = boundwithForm.getSearchConditions();
        if (searchConditions.size() > 1) {
            searchConditions.remove(index);
        }
        return getUIFModelAndView(uifForm);
    }

    @RequestMapping(params = "methodToCall=undoOneBoundwith")
    public ModelAndView undoOneBoundwith(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletResponse httpResponse, HttpServletRequest httpServletRequest) throws Exception {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        boolean selectFlag1 = false;
        boolean selectFlag2 = false;
        Tree<DocumentTreeNode, String> tree1 = boundwithForm.getLeftTree();
        Node<DocumentTreeNode, String> rootElement = tree1.getRootElement();
        Tree<DocumentTreeNode, String> tree2 = boundwithForm.getRightTree();
        Node<DocumentTreeNode, String> tree2RootElement = tree2.getRootElement();
        List<String> holdingsIds = new ArrayList<String>();
        Set<String> holdingsIdsToDelete = new HashSet<>();
        boundwithForm.getDocumentTreeNode().setReturnCheck(true);
        String validateMsg = validateSelectedBibs(boundwithForm);
        if (!validateMsg.contains("failure")) {
            if (rootElement.getChildren().size() != 0) {
                selectCheckedNodesForUnBound(boundwithForm, rootElement);
                selectFlag1 = true;
            } else if (tree2RootElement.getChildren().size() != 0) {
                selectCheckedNodesForUnBound(boundwithForm, tree2RootElement);
                selectFlag2 = true;
            }
            if (selectFlag1 || selectFlag2) {
                BibTree bibTree = boundwithForm.getBibTree();
                List<HoldingsTree> holdingsTrees = bibTree.getHoldingsTrees();
                for (HoldingsTree holdingsTree : holdingsTrees) {
                    Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsTree.getHoldings().getId());
                    if (holdings.isBoundWithBib()) {
                        holdingsIdsToDelete.add(holdings.getId());
                    }
                }
                holdingsIds.addAll(holdingsIdsToDelete);
                try {
                    if (holdingsIds.size() > 0) {
                        getDocstoreClientLocator().getDocstoreClient().unbindWithOneBib(holdingsIds, bibTree.getBib().getId());
                        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "info.undooneboundwith.success");
                    } else {
                        GlobalVariables.getMessageMap().putErrorForSectionId(KRADConstants.GLOBAL_ERRORS, "error.holdings.not.bounded");
                    }
                } catch (DocstoreException e) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(KRADConstants.GLOBAL_ERRORS, e.getErrorCode());
                    boundwithForm.setShowBoundwithTree(false);
                } catch (Exception e) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(KRADConstants.GLOBAL_ERRORS, "error.unboundwith.failed", e.getMessage());
                    boundwithForm.setShowBoundwithTree(false);
                }
            }
        }
        return navigate(boundwithForm, result, httpServletRequest, httpResponse);
    }

    @RequestMapping(params = "methodToCall=undoAllBoundwith")
    public ModelAndView undoAllBoundwith(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletResponse httpResponse, HttpServletRequest httpServletRequest) throws Exception {
        BoundwithForm boundwithForm = (BoundwithForm) form;
        boolean selectFlag1 = false;
        boolean selectFlag2 = false;
        Tree<DocumentTreeNode, String> tree1 = boundwithForm.getLeftTree();
        Node<DocumentTreeNode, String> rootElement = tree1.getRootElement();
        Tree<DocumentTreeNode, String> tree2 = boundwithForm.getRightTree();
        Node<DocumentTreeNode, String> tree2RootElement = tree2.getRootElement();
        List<String> holdingsIds = new ArrayList<String>();
        Set<String> holdingsIdsToDelete = new HashSet<>();
        boundwithForm.getDocumentTreeNode().setReturnCheck(true);
        String validateMsg = validateSelectedBibs(boundwithForm);
        if (!validateMsg.contains("failure")) {
            if (rootElement.getChildren().size() != 0) {
                selectCheckedNodesForUnBound(boundwithForm, rootElement);
                selectFlag1 = true;
            } else if (tree2RootElement.getChildren().size() != 0) {
                selectCheckedNodesForUnBound(boundwithForm, tree2RootElement);
                selectFlag2 = true;
            }
            if (selectFlag1 || selectFlag2) {
                BibTree bibTree = boundwithForm.getBibTree();
                List<HoldingsTree> holdingsTrees = bibTree.getHoldingsTrees();
                for (HoldingsTree holdingsTree : holdingsTrees) {
                    Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsTree.getHoldings().getId());
                    if (holdings.isBoundWithBib()) {
                        holdingsIds.add(holdings.getId());
                    }
                }
                holdingsIds.addAll(holdingsIdsToDelete);
                try {
                    if (holdingsIds.size() > 0) {
                        getDocstoreClientLocator().getDocstoreClient().unbindWithAllBibs(holdingsIds, bibTree.getBib().getId());
                        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "info.undoallboundwith.success");
                    } else {
                        GlobalVariables.getMessageMap().putErrorForSectionId(KRADConstants.GLOBAL_ERRORS, "error.holdings.not.bounded");
                    }
                } catch (DocstoreException e) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(KRADConstants.GLOBAL_ERRORS, e.getErrorCode());
                    boundwithForm.setShowBoundwithTree(false);
                } catch (Exception e) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(KRADConstants.GLOBAL_ERRORS, "error.unboundwith.failed", e.getMessage());
                    boundwithForm.setShowBoundwithTree(false);
                }
            }
        }

        return navigate(boundwithForm, result, httpServletRequest, httpResponse);
    }

    public String validateSelectedBibs(BoundwithForm boundwithForm) {
        String string = new String();
        Tree<DocumentTreeNode, String> leftTree = boundwithForm.getLeftTree();
        boolean selectedFlag1 = false;
        boolean selectedFlag2 = false;
        DocumentTreeNode documentTreeNode;
        int count = 0;
        List<Node<DocumentTreeNode, String>> list = leftTree.getRootElement().getChildren();
        for (Node<DocumentTreeNode, String> bibNode : list) {
            documentTreeNode = bibNode.getData();
            if (documentTreeNode.isSelect()) {
                selectedFlag1 = true;
            }
            List<Node<DocumentTreeNode, String>> instanceList = bibNode.getChildren();
            for (Node<DocumentTreeNode, String> instance : instanceList) {
                documentTreeNode = instance.getData();
                if (documentTreeNode.isSelect()) {
                    count++;
                }
            }
            if (count != 0) {
                GlobalVariables.getMessageMap().putErrorForSectionId(KRADConstants.GLOBAL_ERRORS, "error.select.only.bib");
                string = new String("failure");
            }
        }
        count = 0;
        if (boundwithForm.getRightTree().getRootElement() != null) {
            List<Node<DocumentTreeNode, String>> rightlist = boundwithForm.getRightTree().getRootElement().getChildren();
            for (Node<DocumentTreeNode, String> bibNode : rightlist) {
                documentTreeNode = bibNode.getData();
                if (documentTreeNode.isSelect()) {
                    selectedFlag2 = true;
                }
                List<Node<DocumentTreeNode, String>> instanceList = bibNode.getChildren();
                for (Node<DocumentTreeNode, String> instance : instanceList) {
                    documentTreeNode = instance.getData();
                    if (documentTreeNode.isSelect()) {
                        count++;
                    }
                }
                if (count != 0) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(KRADConstants.GLOBAL_ERRORS, "error.select.only.bib");
                    string = new String("failure");
                }
            }
        }
        if (selectedFlag1 && selectedFlag2) {
            GlobalVariables.getMessageMap().putErrorForSectionId(KRADConstants.GLOBAL_ERRORS, "error.select.one.bib");
            string = new String("failure");
        }
        return string;
    }

    private void selectCheckedNodesForUnBound(BoundwithForm boundwithForm, Node<DocumentTreeNode, String> rootElement) {
        DocumentTreeNode documentTreeNode;
        BibTree bibTree = new BibTree();
        List<HoldingsTree> holdingsTreeList = new ArrayList<>();
        Bib bib = new Bib();
        List<Item> items = new ArrayList<Item>();
        List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
        for (Node<DocumentTreeNode, String> bibNode : list) {
            List<Node<DocumentTreeNode, String>> instanceList = bibNode.getChildren();
            for (Node<DocumentTreeNode, String> instance : instanceList) {
                HoldingsTree holdingsTree = new HoldingsTree();
                Holdings holdings = new Holdings();
                documentTreeNode = instance.getData();
                bib.setId(bibNode.getNodeType());
                bib.setTitle(bibNode.getNodeLabel());
                holdings.setId(instance.getNodeType());
                holdings.setLocationName(instance.getNodeLabel());
                boundwithForm.setMessage(instance.getNodeLabel());
                documentTreeNode.setSelect(true);
                List<Node<DocumentTreeNode, String>> itemList = instance.getChildren();
                for (Node<DocumentTreeNode, String> item : itemList) {
                    Item itemDoc = new Item();
                    documentTreeNode = item.getData();
                    itemDoc.setId(item.getNodeType());
                    itemDoc.setCallNumber(item.getNodeLabel());
                    documentTreeNode.setSelect(true);
                    items.add(itemDoc);
                }
                holdingsTree.getItems().addAll(items);
                holdingsTree.setHoldings(holdings);
                bibTree.setBib(bib);
                holdingsTreeList.add(holdingsTree);
            }
            bibTree.getHoldingsTrees().addAll(holdingsTreeList);
        }
        boundwithForm.setBibTree(bibTree);

    }

}