package org.kuali.ole.describe.form;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.deliver.bo.OLEReturnHistoryRecord;
import org.kuali.ole.describe.bo.*;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;
import org.kuali.ole.select.bo.OLESerialReceivingHistory;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: PP7788
 * Date: 12/6/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditorForm extends UifFormBase {

    protected String message;

    private String docCategory;
    private String docType;
    private String docFormat;
    private String uuid;
    private String docId;
    private String eResourceId;
    private String eResourceTitle;
    private String bibStatus;
    private OleBibliographicRecordStatus oleBibliographicRecordStatus;
    private String updatedBy;
    private String createdBy;
    private String holdingUpdatedBy;
    private String holdingCreatedBy;
    private String itemUpdatedBy;
    private String itemCreatedBy;
    private List<WorkDublinEditorField> dublinFieldList = new ArrayList<WorkDublinEditorField>();
    private List<WorkDublinEditorField> existingDublinFieldList = new ArrayList<WorkDublinEditorField>();
    private String callNumberFlag;
    private String deleteVerifyResponse;
    private List<String> uuidList;
    private String headerText =" ";
    private String tokenId;
    private boolean hasLink;
    private String titleField;
    private boolean fromDublin;
    private String recStatus;
    private boolean select;
    private String recordOpenedTime;
    private boolean showLeftTree = false;
    private String locationValue;
    private Tree<DocumentTreeNode, String> DocTree = new Tree<DocumentTreeNode, String>();
    private Tree<DocumentTreeNode, String> leftTree = new Tree<DocumentTreeNode, String>();
    private boolean staffOnlyFlagForBib;
    private boolean staffOnlyFlagForHoldings;
    private boolean staffOnlyFlagForItem;
    private String bibLocalIdentifier;
    private String holdingLocalIdentifier;
    private String itemLocalIdentifier;
    private String issn;
    private String createdDate;
    private String updatedDate;
    private boolean serialFlag=false;
    private String holdingCreatedDate;
    private String holdingUpdatedDate;
    private String itemCreatedDate;
    private String itemUpdatedDate;
    private String allowUpdate="true";
    private String linkToOrderOption;
    private String coverageDateStartFlag="false";
    private String coverageDateEndFlag="false";
    private String perpetualDateStartFlag="false";
    private String perpetualDateEndFlag="false";
    private String noOfUserFlag="false";
    private String authenticationTypeFlag="false";
    private String  accessLocationFlag="false";
    private String statisticalCodeFlag="false";
    private boolean bibFlag = false;
    private boolean holdingFlag = false;
    private boolean eHoldingsFlag = false;
    private boolean itemFlag = false;
    private boolean staffOnlyFlagInGlobalEdit;

    // Document record pojos
    private WorkBibMarcForm workBibMarcForm;
    private EditorForm documentForm;
    private String checkOverwriteFlag;
    private String unboundLocation;
    private String publicDisplay;
    //Left Pane details
    private String existing = "false";
    private String treeData;
    private String hdnUuid;
    private int hdnIndex = 0;
    private String bibId;
    private String instanceId;
    private String holdingsId;
    private String editable;
    private String displayField006 = "false";
    private String displayField007 = "false";
    private String displayField008 = "false";


    private String title;
    private boolean fromSearch;


    private List<WorkBibDocument> workBibDocumentList;
    private List<Bib> bibList;

    private boolean needToCreateInstance = false;
    private boolean showLeftPane = true;
    private boolean showEditorFooter = true;
    private boolean hideFooter = true;
    private boolean showClose = false;
    private boolean isValidInput = true;
    private String statusUpdatedBy;
    private String statusUpdatedOn;

    private boolean showDeleteTree = true;
    private boolean itemStatusNonEditable = false;
    private boolean itemBarcodeNonEditable = false;
    private boolean holdingsDataInItemReadOnly = false;


    private boolean canDelete;
    private boolean canAdd;
    private boolean canDeleteEInstance;
    private boolean canCopyBib;
    private String holdingItem = "item";
    private String itemStatusSelection = "false";
    private String serialReceivingDocId;
    private BusinessObjectService businessObjectService;
    private List<BibTree> bibTreeList;
    private List<OLESerialReceivingHistory> mainSerialReceivingHistoryList;
    private List<OLESerialReceivingHistory> supplementSerialReceivingHistoryList;
    private List<OLESerialReceivingHistory> indexSerialReceivingHistoryList;
    private List<OLEReturnHistoryRecord> oleReturnHistoryRecords;
    private String globalEditFlag = "false";

    private GlobalEditHoldingsFieldsFlagBO globalEditHoldingsFieldsFlagBO;

    private GlobalEditItemFieldsFlagBO globalEditItemFieldsFlagBO;

    private GlobalEditEHoldingsFieldsFlagBO globalEditEHoldingsFieldsFlagBO;

    private String totalTime;
    private String solrTime;
    private String serverTime;
    private boolean showTime = false;
    private String channelUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.editor.url");
    private boolean canDeleteEHoldings = true;
    private String externalHelpUrl;
   private boolean bibliographic;
    private boolean item;
    private boolean newDocument;
    private boolean copyFlag;
    private boolean workFormViewFlag;
    private String deleteMessage;
    private boolean shortcutAddDataField = false;
    private boolean showPrint = false;
    private boolean openLocation = false;
    private boolean supressHoldingsShelving = false;
    private boolean supressItemShelving = false;
    private boolean addSpaceField;

    public boolean isAddSpaceField() {
        return addSpaceField;
    }

    public void setAddSpaceField(boolean addSpaceField) {
        this.addSpaceField = addSpaceField;
    }

    public String getExternalHelpUrl() {
        return externalHelpUrl;
    }

    public void setExternalHelpUrl(String externalHelpUrl) {
        this.externalHelpUrl = externalHelpUrl;
    }

    public String getCheckOverwriteFlag() {
        return checkOverwriteFlag;
    }

    public void setCheckOverwriteFlag(String checkOverwriteFlag) {
        this.checkOverwriteFlag = checkOverwriteFlag;
    }

    public GlobalEditEHoldingsFieldsFlagBO getGlobalEditEHoldingsFieldsFlagBO() {
        return globalEditEHoldingsFieldsFlagBO;
    }

    public void setGlobalEditEHoldingsFieldsFlagBO(GlobalEditEHoldingsFieldsFlagBO globalEditEHoldingsFieldsFlagBO) {
        this.globalEditEHoldingsFieldsFlagBO = globalEditEHoldingsFieldsFlagBO;
    }

    public GlobalEditHoldingsFieldsFlagBO getGlobalEditHoldingsFieldsFlagBO() {
        return globalEditHoldingsFieldsFlagBO;
    }

    public String getCoverageDateStartFlag() {
        return coverageDateStartFlag;
    }

    public void setCoverageDateStartFlag(String coverageDateStartFlag) {
        this.coverageDateStartFlag = coverageDateStartFlag;
    }

    public String getCoverageDateEndFlag() {
        return coverageDateEndFlag;
    }

    public void setCoverageDateEndFlag(String coverageDateEndFlag) {
        this.coverageDateEndFlag = coverageDateEndFlag;
    }

    public String getPerpetualDateStartFlag() {
        return perpetualDateStartFlag;
    }

    public void setPerpetualDateStartFlag(String perpetualDateStartFlag) {
        this.perpetualDateStartFlag = perpetualDateStartFlag;
    }

    public String getPerpetualDateEndFlag() {
        return perpetualDateEndFlag;
    }

    public void setPerpetualDateEndFlag(String perpetualDateEndFlag) {
        this.perpetualDateEndFlag = perpetualDateEndFlag;
    }

    public String getNoOfUserFlag() {
        return noOfUserFlag;
    }

    public void setNoOfUserFlag(String noOfUserFlag) {
        this.noOfUserFlag = noOfUserFlag;
    }

    public String getAuthenticationTypeFlag() {
        return authenticationTypeFlag;
    }

    public void setAuthenticationTypeFlag(String authenticationTypeFlag) {
        this.authenticationTypeFlag = authenticationTypeFlag;
    }

    public String getAccessLocationFlag() {
        return accessLocationFlag;
    }

    public void setAccessLocationFlag(String accessLocationFlag) {
        this.accessLocationFlag = accessLocationFlag;
    }

    public String getStatisticalCodeFlag() {
        return statisticalCodeFlag;
    }

    public void setStatisticalCodeFlag(String statisticalCodeFlag) {
        this.statisticalCodeFlag = statisticalCodeFlag;
    }

    public boolean isSerialFlag() {
        return serialFlag;
    }

    public void setSerialFlag(boolean serialFlag) {
        this.serialFlag = serialFlag;
    }

    public void setGlobalEditHoldingsFieldsFlagBO(GlobalEditHoldingsFieldsFlagBO globalEditHoldingsFieldsFlagBO) {
        this.globalEditHoldingsFieldsFlagBO = globalEditHoldingsFieldsFlagBO;
    }

    public GlobalEditItemFieldsFlagBO getGlobalEditItemFieldsFlagBO() {
        return globalEditItemFieldsFlagBO;
    }

    public String getPublicDisplay() {
        return publicDisplay;
    }

    public void setPublicDisplay(String publicDisplay) {
        this.publicDisplay = publicDisplay;
    }

    public String getUnboundLocation() {
        return unboundLocation;
    }

    public List<OLESerialReceivingHistory> getMainSerialReceivingHistoryList() {
        if (mainSerialReceivingHistoryList == null) {
            mainSerialReceivingHistoryList = new ArrayList<OLESerialReceivingHistory>();
        }
        return this.mainSerialReceivingHistoryList;
    }

    public void setMainSerialReceivingHistoryList(List<OLESerialReceivingHistory> mainSerialReceivingHistoryList) {
        this.mainSerialReceivingHistoryList = mainSerialReceivingHistoryList;
    }

    public List<OLESerialReceivingHistory> getSupplementSerialReceivingHistoryList() {
        if (supplementSerialReceivingHistoryList == null) {
            supplementSerialReceivingHistoryList = new ArrayList<OLESerialReceivingHistory>();
        }
        return supplementSerialReceivingHistoryList;
    }

    public void setSupplementSerialReceivingHistoryList(List<OLESerialReceivingHistory> supplementSerialReceivingHistoryList) {
        this.supplementSerialReceivingHistoryList = supplementSerialReceivingHistoryList;
    }

    public List<OLESerialReceivingHistory> getIndexSerialReceivingHistoryList() {
        if (indexSerialReceivingHistoryList == null) {
            indexSerialReceivingHistoryList = new ArrayList<OLESerialReceivingHistory>();
        }
        return indexSerialReceivingHistoryList;
    }

    public void setIndexSerialReceivingHistoryList(List<OLESerialReceivingHistory> indexSerialReceivingHistoryList) {
        this.indexSerialReceivingHistoryList = indexSerialReceivingHistoryList;
    }

    public void setUnboundLocation(String unboundLocation) {
        this.unboundLocation = unboundLocation;
    }

    public void setGlobalEditItemFieldsFlagBO(GlobalEditItemFieldsFlagBO globalEditItemFieldsFlagBO) {
        this.globalEditItemFieldsFlagBO = globalEditItemFieldsFlagBO;
    }

    public String getGlobalEditFlag() {
        return globalEditFlag;
    }

    public void setGlobalEditFlag(String globalEditFlag) {
        if(globalEditFlag.contains(",")){
            String globalEdit[] = globalEditFlag.split(",");
            this.globalEditFlag = globalEdit[0];
        }
        else {
            this.globalEditFlag = globalEditFlag;
        }
    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

    public boolean isBibFlag() {
        return bibFlag;
    }

    public void setBibFlag(boolean bibFlag) {
        this.bibFlag = bibFlag;
    }

    public boolean isHoldingFlag() {
        return holdingFlag;
    }

    public void setHoldingFlag(boolean holdingFlag) {
        this.holdingFlag = holdingFlag;
    }

    public boolean iseHoldingsFlag() {
        return eHoldingsFlag;
    }

    public void seteHoldingsFlag(boolean eHoldingsFlag) {
        this.eHoldingsFlag = eHoldingsFlag;
    }

    public boolean isItemFlag() {
        return itemFlag;
    }

    public void setItemFlag(boolean itemFlag) {
        this.itemFlag = itemFlag;
    }

    public List<BibTree> getBibTreeList() {
        return bibTreeList;
    }

    public void setBibTreeList(List<BibTree> bibTreeList) {
        this.bibTreeList = bibTreeList;
    }

    public String getItemStatusSelection() {
        return itemStatusSelection;
    }

    public void setItemStatusSelection(String itemStatusSelection) {
        this.itemStatusSelection = itemStatusSelection;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getBibLocalIdentifier() {
        return bibLocalIdentifier;
    }

    public void setBibLocalIdentifier(String bibLocalIdentifier) {
        if (bibLocalIdentifier != null && bibLocalIdentifier.length() > 0) {
            this.bibLocalIdentifier = String.valueOf(DocumentLocalId.getDocumentId(bibLocalIdentifier));
        } else {
            this.bibLocalIdentifier = bibLocalIdentifier;
        }
    }

    public String getHoldingLocalIdentifier() {
        return holdingLocalIdentifier;
    }

    public void setHoldingLocalIdentifier(String holdingLocalIdentifier) {
        if (holdingLocalIdentifier != null && holdingLocalIdentifier.length() > 0) {
            this.holdingLocalIdentifier = String.valueOf(DocumentLocalId.getDocumentId(holdingLocalIdentifier));
        } else {
            this.holdingLocalIdentifier = holdingLocalIdentifier;
        }
    }

    public String getItemLocalIdentifier() {
        return itemLocalIdentifier;
    }

    public void setItemLocalIdentifier(String itemLocalIdentifier) {
        if (itemLocalIdentifier != null && itemLocalIdentifier.length() > 0) {
            this.itemLocalIdentifier = String.valueOf(DocumentLocalId.getDocumentId(itemLocalIdentifier));
        } else {
            this.itemLocalIdentifier = itemLocalIdentifier;
        }
    }

    public boolean isStaffOnlyFlagForBib() {
        return staffOnlyFlagForBib;
    }

    public void setStaffOnlyFlagForBib(boolean staffOnlyFlagForBib) {
        this.staffOnlyFlagForBib = staffOnlyFlagForBib;
    }

    public String geteResourceTitle() {
        return eResourceTitle;
    }

    public void seteResourceTitle(String eResourceTitle) {
        this.eResourceTitle = eResourceTitle;
    }

    public boolean isStaffOnlyFlagForHoldings() {
        return staffOnlyFlagForHoldings;
    }

    public void setStaffOnlyFlagForHoldings(boolean staffOnlyFlagForHoldings) {
        this.staffOnlyFlagForHoldings = staffOnlyFlagForHoldings;
    }

    public boolean isStaffOnlyFlagForItem() {
        return staffOnlyFlagForItem;
    }

    public void setStaffOnlyFlagForItem(boolean staffOnlyFlagForItem) {
        this.staffOnlyFlagForItem = staffOnlyFlagForItem;
    }

    public String getRecordOpenedTime() {
        return recordOpenedTime;
    }

    public void setRecordOpenedTime(String recordOpenedTime) {
        this.recordOpenedTime = recordOpenedTime;
    }

    public List<WorkDublinEditorField> getExistingDublinFieldList() {
        return existingDublinFieldList;
    }

    public void setExistingDublinFieldList(List<WorkDublinEditorField> existingDublinFieldList) {
        this.existingDublinFieldList = existingDublinFieldList;
    }

    public List<WorkDublinEditorField> getDublinFieldList() {
        return dublinFieldList;
    }

    public void setDublinFieldList(List<WorkDublinEditorField> dublinFieldList) {
        this.dublinFieldList = dublinFieldList;
    }

    public List<Bib> getBibList() {
        return bibList;
    }

    public void setBibList(List<Bib> bibList) {
        this.bibList = bibList;
    }

    public String getBibStatus() {
        return bibStatus;
    }

    public void setBibStatus(String bibStatus) {
        this.bibStatus = bibStatus;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCallNumberFlag() {
        return callNumberFlag;
    }

    public void setCallNumberFlag(String callNumberFlag) {
        this.callNumberFlag = callNumberFlag;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public void setCanAdd(boolean canAdd) {
        this.canAdd = canAdd;
    }

    public boolean isCanDeleteEInstance() {
        return canDeleteEInstance;
    }

    public void setCanDeleteEInstance(boolean canDeleteEInstance) {
        this.canDeleteEInstance = canDeleteEInstance;
    }

    public EditorForm() {
        super();
        globalEditEHoldingsFieldsFlagBO = new GlobalEditEHoldingsFieldsFlagBO();
        globalEditItemFieldsFlagBO = new GlobalEditItemFieldsFlagBO();
        globalEditHoldingsFieldsFlagBO = new GlobalEditHoldingsFieldsFlagBO();
        dublinFieldList.add(new WorkDublinEditorField());
        existingDublinFieldList.add(new WorkDublinEditorField());
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        if (docType != null && docType.contains(","))
            this.docType = docType.substring(0, docType.indexOf(","));
        else
            this.docType = docType;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        if (docFormat != null && docFormat.contains(","))
            this.docFormat = docFormat.substring(0, docFormat.indexOf(","));
        else
            this.docFormat = docFormat;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getExisting() {
        return existing;
    }

    public void setExisting(String existing) {
        this.existing = existing;
    }

    public String getTreeData() {
        return treeData;
    }

    public void setTreeData(String treeData) {
        this.treeData = treeData;
    }

    public String getHdnUuid() {
        return hdnUuid;
    }

    public void setHdnUuid(String hdnUuid) {
        this.hdnUuid = hdnUuid;
    }

    public int getHdnIndex() {
        return hdnIndex;
    }

    public void setHdnIndex(int hdnIndex) {
        this.hdnIndex = hdnIndex;
    }

    public List<WorkBibDocument> getWorkBibDocumentList() {
        return workBibDocumentList;
    }

    public void setWorkBibDocumentList(List<WorkBibDocument> workBibDocumentList) {
        this.workBibDocumentList = workBibDocumentList;
    }

    public String getDocCategory() {
        return docCategory;
    }

    public void setDocCategory(String docCategory) {
        if (docCategory != null && docCategory.contains(","))
            this.docCategory = docCategory.substring(0, docCategory.indexOf(","));
        else
            this.docCategory = docCategory;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String geteResourceId() {
        return eResourceId;
    }

    public void seteResourceId(String eResourceId) {
        this.eResourceId = eResourceId;
    }

    public WorkBibMarcForm getWorkBibMarcForm() {
        return workBibMarcForm;
    }

    public void setWorkBibMarcForm(WorkBibMarcForm workBibMarcForm) {
        this.workBibMarcForm = workBibMarcForm;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EditorForm getDocumentForm() {
        return documentForm;
    }

    public void setDocumentForm(EditorForm documentForm) {
        this.documentForm = documentForm;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public boolean isNeedToCreateInstance() {
        return needToCreateInstance;
    }

    public void setNeedToCreateInstance(boolean needToCreateInstance) {
        this.needToCreateInstance = needToCreateInstance;
    }

    public boolean isShowEditorFooter() {
        return showEditorFooter;
    }

    public void setShowEditorFooter(boolean showEditorFooter) {
        this.showEditorFooter = showEditorFooter;
    }

    public boolean isShowLeftPane() {
        return showLeftPane;
    }

    public void setShowLeftPane(boolean showLeftPane) {
        this.showLeftPane = showLeftPane;
    }

    public OleBibliographicRecordStatus getOleBibliographicRecordStatus() {
        return oleBibliographicRecordStatus;
    }

    public void setOleBibliographicRecordStatus(OleBibliographicRecordStatus oleBibliographicRecordStatus) {
        this.oleBibliographicRecordStatus = oleBibliographicRecordStatus;
    }

    public List<String> getUuidList() {
        return uuidList;
    }

    public void setUuidList(List<String> uuidList) {
        this.uuidList = uuidList;
    }

    public Tree<DocumentTreeNode, String> getDocTree() {
        return DocTree;
    }

    public void setDocTree(Tree<DocumentTreeNode, String> docTree) {
        DocTree = docTree;
    }

    public String getDeleteVerifyResponse() {
        return deleteVerifyResponse;
    }

    public void setDeleteVerifyResponse(String deleteVerifyResponse) {
        this.deleteVerifyResponse = deleteVerifyResponse;
    }

    public String getLocationValue() {
        return "";
    }

    public void setLocationValue(String locationValue) {
        this.locationValue = locationValue;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public boolean isHideFooter() {
        return hideFooter;
    }

    public void setHideFooter(boolean hideFooter) {
        this.hideFooter = hideFooter;
    }

    public boolean isValidInput() {
        return isValidInput;
    }

    public void setValidInput(boolean validInput) {
        isValidInput = validInput;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public boolean isHasLink() {
        return hasLink;
    }

    public void setHasLink(boolean hasLink) {
        this.hasLink = hasLink;
    }

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }

    public boolean isFromDublin() {
        return fromDublin;
    }

    public void setFromDublin(boolean fromDublin) {
        this.fromDublin = fromDublin;
    }

    public String getStatusUpdatedBy() {
        return statusUpdatedBy;
    }

    public void setStatusUpdatedBy(String statusUpdatedBy) {
        this.statusUpdatedBy = statusUpdatedBy;
    }

    public String getStatusUpdatedOn() {
        return statusUpdatedOn;
    }

    public void setStatusUpdatedOn(String statusUpdatedOn) {
        this.statusUpdatedOn = statusUpdatedOn;
    }

    public boolean isShowDeleteTree() {
        return showDeleteTree;
    }

    public void setShowDeleteTree(boolean showDeleteTree) {
        this.showDeleteTree = showDeleteTree;
    }

    public boolean isItemStatusNonEditable() {
        return itemStatusNonEditable;
    }

    public void setItemStatusNonEditable(boolean itemStatusNonEditable) {
        this.itemStatusNonEditable = itemStatusNonEditable;
    }

    public boolean isItemBarcodeNonEditable() {
        return itemBarcodeNonEditable;
    }

    public void setItemBarcodeNonEditable(boolean itemBarcodeNonEditable) {
        this.itemBarcodeNonEditable = itemBarcodeNonEditable;
    }

    public String getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(String recStatus) {
        this.recStatus = recStatus;
    }

    public boolean isHoldingsDataInItemReadOnly() {
        return holdingsDataInItemReadOnly;
    }

    public void setHoldingsDataInItemReadOnly(boolean holdingsDataInItemReadOnly) {
        this.holdingsDataInItemReadOnly = holdingsDataInItemReadOnly;
    }

    public void setLeftTree(Tree<DocumentTreeNode, String> leftTree) {
        this.leftTree = leftTree;
    }

    public Tree<DocumentTreeNode, String> getLeftTree() {
        return leftTree;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }


    public boolean isShowLeftTree() {
        return showLeftTree;
    }

    public void setShowLeftTree(boolean showLeftTree) {
        this.showLeftTree = showLeftTree;
    }
    
    public boolean isFilterControlFields() {
    	return !"true".equals(editable);
    }

    public String getDisplayField006() {
        return displayField006;
    }

    public void setDisplayField006(String displayField006) {
        this.displayField006 = displayField006;
    }

    public boolean isFilterField006() {
    	return isFilterControlFields() || !"true".equals(displayField006);
    }
    
    public String getDisplayField007() {
        return displayField007;
    }

    public void setDisplayField007(String displayField007) {
        this.displayField007 = displayField007;
    }

    public boolean isFilterField007() {
    	return isFilterControlFields() || !"true".equals(displayField007);
    }
    
    public String getDisplayField008() {
        return displayField008;
    }

    public void setDisplayField008(String displayField008) {
        this.displayField008 = displayField008;
    }

    public boolean isFilterField008() {
    	return isFilterControlFields() || !"true".equals(displayField008);
    }
    
    public boolean isShowClose() {
        return showClose;
    }

    public void setShowClose(boolean showClose) {
        this.showClose = showClose;
    }

    public String getHoldingUpdatedBy() {
        return holdingUpdatedBy;
    }

    public void setHoldingUpdatedBy(String holdingUpdatedBy) {
        this.holdingUpdatedBy = holdingUpdatedBy;
    }

    public String getHoldingCreatedBy() {
        return holdingCreatedBy;
    }

    public void setHoldingCreatedBy(String holdingCreatedBy) {
        this.holdingCreatedBy = holdingCreatedBy;
    }

    public String getHoldingCreatedDate() {
        return holdingCreatedDate;
    }

    public void setHoldingCreatedDate(String holdingCreatedDate) {
        this.holdingCreatedDate = holdingCreatedDate;
    }

    public String getHoldingUpdatedDate() {
        return holdingUpdatedDate;
    }

    public void setHoldingUpdatedDate(String holdingUpdatedDate) {
        this.holdingUpdatedDate = holdingUpdatedDate;
    }

    public String getItemUpdatedBy() {
        return itemUpdatedBy;
    }

    public void setItemUpdatedBy(String itemUpdatedBy) {
        this.itemUpdatedBy = itemUpdatedBy;
    }

    public String getItemCreatedBy() {
        return itemCreatedBy;
    }

    public void setItemCreatedBy(String itemCreatedBy) {
        this.itemCreatedBy = itemCreatedBy;
    }

    public String getItemCreatedDate() {
        return itemCreatedDate;
    }

    public void setItemCreatedDate(String itemCreatedDate) {
        this.itemCreatedDate = itemCreatedDate;
    }

    public String getItemUpdatedDate() {
        return itemUpdatedDate;
    }

    public void setItemUpdatedDate(String itemUpdatedDate) {
        this.itemUpdatedDate = itemUpdatedDate;
    }


    public String getAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(String allowUpdate) {
        this.allowUpdate = allowUpdate;
    }

    public String getHoldingItem() {
        return holdingItem;
    }

    public void setHoldingItem(String holdingItem) {
        this.holdingItem = holdingItem;
    }

    public String getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(String holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getLinkToOrderOption() {
        return linkToOrderOption;
    }

    public void setLinkToOrderOption(String linkToOrderOption) {
        this.linkToOrderOption = linkToOrderOption;
    }

    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public String getSerialReceivingDocId() {
        Map docMap = new HashMap<>();
        String bibId = this.getBibId();
        String instanceId = this.getInstanceId();
        if (!StringUtils.isBlank(bibId) && !StringUtils.isBlank(instanceId)) {
            docMap.put("bibId", bibId);
            docMap.put("instanceId", this.getInstanceId());
            docMap.put("active", Boolean.TRUE);
            OLESerialReceivingDocument oleSerialReceivingDocument = getBusinessObjectService().findByPrimaryKey(OLESerialReceivingDocument.class, docMap);
            if (oleSerialReceivingDocument != null) {
                return oleSerialReceivingDocument.getDocumentNumber();
            }
        }
        return null;
    }

    public void setSerialReceivingDocId(String serialReceivingDocId) {
        this.serialReceivingDocId = serialReceivingDocId;
    }

    public boolean isStaffOnlyFlagInGlobalEdit() {
        return staffOnlyFlagInGlobalEdit;
    }

    public void setStaffOnlyFlagInGlobalEdit(boolean staffOnlyFlagInGlobalEdit) {
        this.staffOnlyFlagInGlobalEdit = staffOnlyFlagInGlobalEdit;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }
    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public String getSolrTime() {
        return solrTime;
    }

    public void setSolrTime(String solrTime) {
        this.solrTime = solrTime;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public boolean isCanDeleteEHoldings() {
        return canDeleteEHoldings;
    }

    public void setCanDeleteEHoldings(boolean canDeleteEHoldings) {
        this.canDeleteEHoldings = canDeleteEHoldings;
    }

    public List<OLEReturnHistoryRecord> getOleReturnHistoryRecords() {
        return oleReturnHistoryRecords;
    }

    public void setOleReturnHistoryRecords(List<OLEReturnHistoryRecord> oleReturnHistoryRecords) {
        this.oleReturnHistoryRecords = oleReturnHistoryRecords;
    }

    public boolean isBibliographic() {
        return bibliographic;
    }

    public void setBibliographic(boolean bibliographic) {
        this.bibliographic = bibliographic;
    }

    public boolean isItem() {
        return item;
    }

    public void setItem(boolean item) {
        this.item = item;
    }

    public boolean isNewDocument() {
        return newDocument;
    }

    public void setNewDocument(boolean newDocument) {
        this.newDocument = newDocument;
    }

    public boolean isCopyFlag() {
        return copyFlag;
    }

    public void setCopyFlag(boolean copyFlag) {
        this.copyFlag = copyFlag;
    }

    public String getDeleteMessage() {
        return deleteMessage;
    }

    public void setDeleteMessage(String deleteMessage) {
        this.deleteMessage = deleteMessage;
    }

    public boolean isWorkFormViewFlag() {
        return workFormViewFlag;
    }

    public void setWorkFormViewFlag(boolean workFormViewFlag) {
        this.workFormViewFlag = workFormViewFlag;
    }

    public boolean isShortcutAddDataField() {
        return shortcutAddDataField;
    }

    public void setShortcutAddDataField(boolean shortcutAddDataField) {
        this.shortcutAddDataField = shortcutAddDataField;
    }

    public boolean isShowPrint() {
        return showPrint;
    }

    public void setShowPrint(boolean showPrint) {
        this.showPrint = showPrint;
    }

    public boolean isOpenLocation() {
        return openLocation;
    }

    public void setOpenLocation(boolean openLocation) {
        this.openLocation = openLocation;
    }

    public boolean isSupressHoldingsShelving() {
        return supressHoldingsShelving;
    }

    public void setSupressHoldingsShelving(boolean supressHoldingsShelving) {
        this.supressHoldingsShelving = supressHoldingsShelving;
    }

    public boolean isSupressItemShelving() {
        return supressItemShelving;
    }

    public void setSupressItemShelving(boolean supressItemShelving) {
        this.supressItemShelving = supressItemShelving;
    }

    public boolean isCanCopyBib() {
        return canCopyBib;
    }

    public void setCanCopyBib(boolean canCopyBib) {
        this.canCopyBib = canCopyBib;
    }
}
