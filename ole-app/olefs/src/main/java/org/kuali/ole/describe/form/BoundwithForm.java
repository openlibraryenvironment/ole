package org.kuali.ole.describe.form;

import org.kuali.ole.describe.bo.*;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.search.FacetResultField;
import org.kuali.ole.docstore.common.search.SearchCondition;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.model.bo.*;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 11/26/12
 * Time: 01:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BoundwithForm extends OLESearchForm {

    private SearchParams searchParams;
    private SearchResponse searchResponse;
    private List<OleWorkBibDocument> workBibDocumentList;
    private List<WorkHoldingsDocument> workHoldingsDocumentList;
    private List<WorkItemDocument> workItemDocumentList;
    private List<WorkEHoldingsDocument> workEHoldingsDocumentList;
    private List<SearchResultDisplayRow> searchResultDisplayRowList;
    private List<Bib> bibList;
    private List<Holdings> holdingsList;
    private List<Item> itemList;
    private List<DocumentTreeNode> displayRecordList = new ArrayList<DocumentTreeNode>();
    private String message;
    private String author;
    private String searchType;
    private String id;
    private String title;
    private String description;
    private String publicationDate;
    private String barcode;
    private String searchResults;
    private String control;
    private boolean select;
    private boolean selectTree1;
    private boolean selectTree2;
    private boolean selectBoundwithTree;
    private String selectedInstance;
    private String labelText;
    private String tree2LabelText;
    private String boundwithTreeLabelText;
    private String docCategory;
    private String docType;
    private String docFormat;
    private String docId;
    private String inDelete;
    private String inDeleteLeftTree;
    private String inDeleteRightTree;
    private boolean transferLeftTree;
    private boolean transferRighttree;
    private int pageSize = 10;
    private int start;
  //  public boolean nextFlag;
  //  public boolean previousFlag;
    public String pageShowEntries;
    private String sortOrder;
    private String sortField;
    private String sortFlag;
    private List<String> selectedHoldings;
    private List<String> selectedHoldingsFromTree1;
    private List<String> selectedBibs;
    private List<String> selectedBibsFromTree2;
    private List<String> selectedBibsFromTree1;
    private List<String> deleteIds;
    private BibTree bibTree;
    private String showPageSize;
    private String browseField;
    private String browseText;
    private int  facetLimit;
    private String searchTypeField;
    private List<FacetResultField> facetResultFields;
    private SearchResultDisplayFields searchResultDisplayFields;
    private String showFieldSort;

    public String getShowFieldSort() {
        return searchResultDisplayFields.getSortFieldString();
    }

    public void setShowFieldSort(String showFieldSort) {
        this.showFieldSort = showFieldSort;
    }

    public List<FacetResultField> getFacetResultFields() {
        return facetResultFields;
    }

    public void setFacetResultFields(List<FacetResultField> facetResultFields) {
        this.facetResultFields = facetResultFields;
    }

    public int getFacetLimit() {
        return facetLimit;
    }

    public String getBrowseField() {
        return browseField;
    }

    public void setBrowseField(String browseField) {
        this.browseField = browseField;
    }

    public String getSearchTypeField() {
        return searchTypeField;
    }

    public void setSearchTypeField(String searchTypeField) {
        this.searchTypeField = searchTypeField;
    }

    public String getBrowseText() {
        return browseText;
    }

    public void setBrowseText(String browseText) {
        this.browseText = browseText;
    }

    public void setFacetLimit(int facetLimit) {
        this.facetLimit = facetLimit;
    }

    public String getShowPageSize() {
        return showPageSize;
    }

    public void setShowPageSize(String showPageSize) {
        this.showPageSize = showPageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

   /* public boolean isNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(boolean nextFlag) {
        this.nextFlag = nextFlag;
    }

    public boolean isPreviousFlag() {
        return previousFlag;
    }

    public void setPreviousFlag(boolean previousFlag) {
        this.previousFlag = previousFlag;
    }  */

    public String getPageShowEntries() {
        return pageShowEntries;
    }

    public void setPageShowEntries(String pageShowEntries) {
        this.pageShowEntries = pageShowEntries;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean getTransferLeftTree() {
        return transferLeftTree;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void setTransferLeftTree(boolean transferLeftTree) {
        this.transferLeftTree = transferLeftTree;
    }

    public boolean getTransferRighttree() {
        return transferRighttree;
    }

    public void setTransferRighttree(boolean transferRighttree) {
        this.transferRighttree = transferRighttree;
    }


    public String getInDeleteLeftTree() {
        return inDeleteLeftTree;
    }

    public void setInDeleteLeftTree(String inDeleteLeftTree) {
        this.inDeleteLeftTree = inDeleteLeftTree;
    }

    public String getInDeleteRightTree() {
        return inDeleteRightTree;
    }

    public void setInDeleteRightTree(String inDeleteRightTree) {
        this.inDeleteRightTree = inDeleteRightTree;
    }


    public SearchResponse getSearchResponse() {
        return searchResponse;
    }

    public void setSearchResponse(SearchResponse searchResponse) {
        this.searchResponse = searchResponse;
    }

    private DocumentTreeNode showLeftTreeStatusMessages;
    private DocumentTreeNode showRightTreeStatusMessages;
    private String status;
    private boolean isSelected;
    private DocumentTreeNode documentTreeNode;
    private Tree<BoundwithSelection, String> bibTree1 = new Tree<BoundwithSelection, String>();
    private Tree<BoundwithSelection, String> bibTree2 = new Tree<BoundwithSelection, String>();
    private Tree<BoundwithSelection, String> boundWithTree = new Tree<BoundwithSelection, String>();
    private Map<String, String> instanceMap = new HashMap<String, String>();
    private Map<String, String> bibMap = new HashMap<String, String>();

    private Tree<DocumentTreeNode, String> leftTree = new Tree<DocumentTreeNode, String>();
    private Tree<DocumentTreeNode, String> rightTree = new Tree<DocumentTreeNode, String>();
    private Tree<DocumentTreeNode, String> boundwithTree = new Tree<DocumentTreeNode, String>();
    private Tree<DocumentTreeNode, String> deleteConfirmationTree = new Tree<DocumentTreeNode, String>();

    private String deleteVerifyResponse;

    private boolean showRightTree = false;
    private boolean showLeftTree = false;
    private boolean showBoundwithTree = false;

    protected Map<String, String> actionParameters = new HashMap<String, String>();
    private WorkInstanceDocument workInstanceDocumentForTree1 = new WorkInstanceDocument();
    private List<String> selectedInstancesList = new ArrayList<String>();
    private String tree1BibId;
    private Set<String> selectedBibsList = new HashSet<String>();
    private List<WorkBibDocument> bibDocumentList = new ArrayList<WorkBibDocument>();
    private List<String> bibInstanceListForTree1 = new ArrayList<String>();
    private String destBibIdentifier;
    private List<String> bibItemListForTree1 = new ArrayList<String>();
    private boolean showExport=false;
    private boolean showRequestXML=false;

    public DocumentTreeNode getShowLeftTreeStatusMessages() {
        return showLeftTreeStatusMessages;
    }

    public void setShowLeftTreeStatusMessages(DocumentTreeNode showLeftTreeStatusMessages) {
        this.showLeftTreeStatusMessages = showLeftTreeStatusMessages;
    }

    public DocumentTreeNode getShowRightTreeStatusMessages() {
        return showRightTreeStatusMessages;
    }

    public void setShowRightTreeStatusMessages(DocumentTreeNode showRightTreeStatusMessages) {
        this.showRightTreeStatusMessages = showRightTreeStatusMessages;
    }

    public List<String> getBibItemListForTree1() {
        return bibItemListForTree1;
    }

    public void setBibItemListForTree1(List<String> bibItemListForTree1) {
        this.bibItemListForTree1 = bibItemListForTree1;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortFlag() {
        return sortFlag;
    }

    public void setSortFlag(String sortFlag) {
        this.sortFlag = sortFlag;
    }

    public String getDestInstanceIdentifier() {
        return destInstanceIdentifier;
    }

    public void setDestInstanceIdentifier(String destInstanceIdentifier) {
        this.destInstanceIdentifier = destInstanceIdentifier;
    }

    private String destInstanceIdentifier;

    public List<String> getBibInstanceListForTree1() {
        return bibInstanceListForTree1;
    }

    public void setBibInstanceListForTree1(List<String> bibInstanceListForTree1) {
        this.bibInstanceListForTree1 = bibInstanceListForTree1;
    }

    public String getDestBibIdentifier() {
        return destBibIdentifier;
    }

    public void setDestBibIdentifier(String destBibIdentifier) {
        this.destBibIdentifier = destBibIdentifier;
    }


    public BoundwithForm() {
        searchResultDisplayFields = new SearchResultDisplayFields();
    }


    /**
     * @see org.kuali.rice.krad.uif.view.ViewModel#getActionParameters()
     */
    @Override
    public Map<String, String> getActionParameters() {
        return this.actionParameters;
    }

    public SearchResultDisplayFields getSearchResultDisplayFields() {
        return searchResultDisplayFields;
    }

    public void setSearchResultDisplayFields(SearchResultDisplayFields searchResultDisplayFields) {
        this.searchResultDisplayFields = searchResultDisplayFields;
    }

    /**
     * Returns the action parameters map as a {@code Properties} instance
     *
     * @return Properties action parameters
     */
    public Properties getActionParametersAsProperties() {
        return KRADUtils.convertMapToProperties(actionParameters);
    }

    /**
     * @see org.kuali.rice.krad.uif.view.ViewModel#setActionParameters(java.util.Map<java.lang.String,java.lang.String>)
     */
    @Override
    public void setActionParameters(Map<String, String> actionParameters) {
        this.actionParameters = actionParameters;
    }

    /**
     * Retrieves the value for the given action parameter, or empty string if
     * not found
     *
     * @param actionParameterName - name of the action parameter to retrieve value for
     * @return String parameter value or empty string
     */
    public String getActionParamaterValue(String actionParameterName) {
        if ((actionParameters != null) && actionParameters.containsKey(actionParameterName)) {
            return actionParameters.get(actionParameterName);
        }

        return "";
    }

    /**
     * Returns the action event that was sent in the action parameters (if any)
     * <p/>
     * <p>
     * The action event is a special action parameter that can be sent to indicate a type of action being taken. This
     * can be looked at by the view or components to render differently
     * </p>
     *
     * @return String action event name or blank if action event was not sent
     */
    public String getActionEvent() {
        if ((actionParameters != null) && actionParameters.containsKey(UifConstants.UrlParams.ACTION_EVENT)) {
            return actionParameters.get(UifConstants.UrlParams.ACTION_EVENT);
        }

        return "";
    }


    public Map<String, String> getActionParameters(Map<String, String> actionParametersMap) {
        this.actionParameters = actionParametersMap;
        return actionParametersMap;
    }


    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    public List<OleWorkBibDocument> getWorkBibDocumentList() {
        return workBibDocumentList;
    }

    public DocumentTreeNode getDocumentTreeNode() {
        if (null == documentTreeNode) {
            documentTreeNode = new DocumentTreeNode();
        }
        return documentTreeNode;
    }

    public void setDocumentTreeNode(DocumentTreeNode documentTreeNode) {
        this.documentTreeNode = documentTreeNode;
    }

    public void setWorkBibDocumentList(List<OleWorkBibDocument> workBibDocumentList) {
        this.workBibDocumentList = workBibDocumentList;
    }

    public List<WorkBibDocument> getBibDocumentList() {
        return bibDocumentList;
    }

    public void setBibDocumentList(List<WorkBibDocument> bibDocumentList) {
        this.bibDocumentList = bibDocumentList;
    }

    public List<Bib> getBibList() {
        return bibList;
    }

    public void setBibList(List<Bib> bibList) {
        this.bibList = bibList;
    }

    public List<Holdings> getHoldingsList() {
        return holdingsList;
    }

    public void setHoldingsList(List<Holdings> holdingsList) {
        this.holdingsList = holdingsList;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }


    public String getMessage() {
        return message;
    }

    public String getSelectedInstance() {
        return selectedInstance;
    }

    public void setSelectedInstance(String selectedInstance) {
        this.selectedInstance = selectedInstance;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(String searchResults) {
        this.searchResults = searchResults;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getTree2LabelText() {
        return tree2LabelText;
    }

    public void setTree2LabelText(String tree2LabelText) {
        this.tree2LabelText = tree2LabelText;
    }

    public boolean isSelectTree1() {
        return selectTree1;
    }

    public void setSelectTree1(boolean selectTree1) {
        this.selectTree1 = selectTree1;
    }

    public boolean isSelectTree2() {
        return selectTree2;
    }

    public void setSelectTree2(boolean selectTree2) {
        this.selectTree2 = selectTree2;
    }

    public List<DocumentTreeNode> getDisplayRecordList() {
        return displayRecordList;
    }

    public void setDisplayRecordList(List<DocumentTreeNode> displayRecordList) {
        this.displayRecordList = displayRecordList;
    }


    public void clearFormFields() {
        this.setId("");
        this.setAuthor("");
        this.setTitle("");
        this.setDescription("");
        this.setPublicationDate("");
        this.setBarcode("");
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tree<BoundwithSelection, String> getBibTree1() {
        return bibTree1;
    }

    public void setBibTree1(Tree<BoundwithSelection, String> bibTree1) {
        this.bibTree1 = bibTree1;
    }

    public Tree<BoundwithSelection, String> getBibTree2() {
        return bibTree2;
    }

    public void setBibTree2(Tree<BoundwithSelection, String> bibTree2) {
        this.bibTree2 = bibTree2;
    }

    public Tree<BoundwithSelection, String> getBoundWithTree() {
        return boundWithTree;
    }

    public void setBoundWithTree(Tree<BoundwithSelection, String> boundWithTree) {
        this.boundWithTree = boundWithTree;
    }

    public Map<String, String> getInstanceMap() {
        return instanceMap;
    }

    public void setInstanceMap(Map<String, String> instanceMap) {
        this.instanceMap = instanceMap;
    }

    public String getBoundwithTreeLabelText() {
        return boundwithTreeLabelText;
    }

    public void setBoundwithTreeLabelText(String boundwithTreeLabelText) {
        this.boundwithTreeLabelText = boundwithTreeLabelText;
    }

    public boolean isSelectBoundwithTree() {
        return selectBoundwithTree;
    }

    public void setSelectBoundwithTree(boolean selectBoundwithTree) {
        this.selectBoundwithTree = selectBoundwithTree;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Map<String, String> getBibMap() {
        return bibMap;
    }

    public void setBibMap(Map<String, String> bibMap) {
        this.bibMap = bibMap;
    }

    public Tree<DocumentTreeNode, String> getLeftTree() {
        return leftTree;
    }

    public void setLeftTree(Tree<DocumentTreeNode, String> leftTree) {
        this.leftTree = leftTree;
    }

    public Tree<DocumentTreeNode, String> getRightTree() {
        return rightTree;
    }

    public void setRightTree(Tree<DocumentTreeNode, String> rightTree) {
        this.rightTree = rightTree;
    }

    public Tree<DocumentTreeNode, String> getBoundwithTree() {
        return boundwithTree;
    }

    public void setBoundwithTree(Tree<DocumentTreeNode, String> boundwithTree) {
        this.boundwithTree = boundwithTree;
    }

    public Tree<DocumentTreeNode, String> getDeleteConfirmationTree() {
        return deleteConfirmationTree;
    }

    public void setDeleteConfirmationTree(Tree<DocumentTreeNode, String> deleteConfirmationTree) {
        this.deleteConfirmationTree = deleteConfirmationTree;
    }

    public String getDeleteVerifyResponse() {
        return deleteVerifyResponse;
    }

    public void setDeleteVerifyResponse(String deleteVerifyResponse) {
        this.deleteVerifyResponse = deleteVerifyResponse;
    }

    public String getDocCategory() {
        return docCategory;
    }

    public void setDocCategory(String docCategory) {
        this.docCategory = docCategory;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getInDelete() {
        return inDelete;
    }

    public void setInDelete(String inDelete) {
        this.inDelete = inDelete;
    }

    public boolean isShowRightTree() {
        return showRightTree;
    }

    public void setShowRightTree(boolean showRightTree) {
        this.showRightTree = showRightTree;
    }

    public boolean isShowLeftTree() {
        return showLeftTree;
    }

    public void setShowLeftTree(boolean showLeftTree) {
        this.showLeftTree = showLeftTree;
    }

    public boolean isShowBoundwithTree() {
        return showBoundwithTree;
    }

    public void setShowBoundwithTree(boolean showBoundwithTree) {
        this.showBoundwithTree = showBoundwithTree;
    }

    public WorkInstanceDocument getWorkInstanceDocumentForTree1() {
        if (workInstanceDocumentForTree1 == null) {
            workInstanceDocumentForTree1 = new WorkInstanceDocument();
        }
        return workInstanceDocumentForTree1;
    }

    public void setWorkInstanceDocumentForTree1(WorkInstanceDocument workInstanceDocumentForTree1) {
        this.workInstanceDocumentForTree1 = workInstanceDocumentForTree1;
    }

    public List<String> getSelectedInstancesList() {
        return selectedInstancesList;
    }

    public void setSelectedInstancesList(List<String> selectedInstancesList) {
        this.selectedInstancesList = selectedInstancesList;
    }

    public String getTree1BibId() {
        return tree1BibId;
    }

    public void setTree1BibId(String tree1BibId) {
        this.tree1BibId = tree1BibId;
    }

    public Set<String> getSelectedBibsList() {
        return selectedBibsList;
    }

    public void setSelectedBibsList(Set<String> selectedBibsList) {
        this.selectedBibsList = selectedBibsList;
    }
    public List<SearchResultDisplayRow> getSearchResultDisplayRowList() {
        return searchResultDisplayRowList;
    }

    public void setSearchResultDisplayRowList(List<SearchResultDisplayRow> searchResultDisplayRowList) {
        this.searchResultDisplayRowList = searchResultDisplayRowList;
    }

    public List<String> getSelectedHoldings() {
        return selectedHoldings;
    }

    public void setSelectedHoldings(List<String> selectedHoldings) {
        this.selectedHoldings = selectedHoldings;
    }

    public List<String> getSelectedBibs() {
        return selectedBibs;
    }

    public void setSelectedBibs(List<String> selectedBibs) {
        this.selectedBibs = selectedBibs;
    }

    public BibTree getBibTree() {
        return bibTree;
    }

    public void setBibTree(BibTree bibTree) {
        this.bibTree = bibTree;
    }

    public List<String> getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(List<String> deleteIds) {
        this.deleteIds = deleteIds;
    }

    public List<String> getSelectedBibsFromTree1() {
        return selectedBibsFromTree1;
    }

    public void setSelectedBibsFromTree1(List<String> selectedBibsFromTree1) {
        this.selectedBibsFromTree1 = selectedBibsFromTree1;
    }

    public List<String> getSelectedBibsFromTree2() {
        return selectedBibsFromTree2;
    }

    public void setSelectedBibsFromTree2(List<String> selectedBibsFromTree2) {
        this.selectedBibsFromTree2 = selectedBibsFromTree2;
    }

    public List<String> getSelectedHoldingsFromTree1() {
        return selectedHoldingsFromTree1;
    }

    public void setSelectedHoldingsFromTree1(List<String> selectedHoldingsFromTree1) {
        this.selectedHoldingsFromTree1 = selectedHoldingsFromTree1;
    }

    public boolean isShowExport() {

        return showExport;
    }

    public void setShowExport(boolean showExport) {
        this.showExport = showExport;
    }

    public boolean isShowRequestXML() {
        return showRequestXML;
    }

    public void setShowRequestXML(boolean showRequestXML) {
        this.showRequestXML = showRequestXML;
    }


}
