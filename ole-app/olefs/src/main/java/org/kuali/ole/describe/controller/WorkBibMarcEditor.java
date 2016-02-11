package org.kuali.ole.describe.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleEditorResponseHandler;
import org.kuali.ole.deliver.bo.SystemGeneratedBill;
import org.kuali.ole.describe.bo.DocumentSelectionTree;
import org.kuali.ole.describe.bo.DocumentTreeNode;
import org.kuali.ole.describe.bo.MarcEditorControlField;
import org.kuali.ole.describe.bo.MarcEditorDataField;
import org.kuali.ole.describe.bo.MarcEditorFormDataHandler;
import org.kuali.ole.describe.bo.OleBibliographicRecordStatus;
import org.kuali.ole.describe.bo.marc.structuralfields.ControlFields;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield006.ControlField006Text;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield007.ControlField007Text;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield008.ControlField008;
import org.kuali.ole.describe.form.EditorForm;
import org.kuali.ole.describe.form.WorkBibMarcForm;
import org.kuali.ole.describe.form.WorkEInstanceOlemlForm;
import org.kuali.ole.describe.service.DiscoveryHelperService;
import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.engine.client.DocstoreLocalClient;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.bo.WorkHoldingsDocument;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.kuali.ole.docstore.model.bo.WorkItemDocument;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.docstore.validation.DocStoreValidationError;
import org.kuali.ole.docstore.validation.WorkBibMarcRecordValidator;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleEditorResponse;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.businessobject.OleDocstoreResponse;
import org.kuali.ole.select.document.OLEEResourceInstance;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.service.impl.OleExposedWebServiceImpl;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.*;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class for handling all editor operations for WorkBibMarc documents.
 */
public class WorkBibMarcEditor extends AbstractEditor implements
        DocumentEditor {

    private static final Logger LOG = Logger.getLogger(WorkBibMarcEditor.class);

    private MarcEditorFormDataHandler marcEditorFormDataHandler;
    private BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
    private OLEEResourceSearchService oleEResourceSearchService;
    private DocumentService documentService;
    private String tokenId;
    private static WorkBibMarcEditor workBibMarcEditor = new WorkBibMarcEditor();
    private DocstoreClient docstoreClient = getDocstoreLocalClient();
    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }


    public static WorkBibMarcEditor getInstance() {
        return workBibMarcEditor;
    }

    private WorkBibMarcEditor() {
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

    private boolean canEditBib(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.MARC_EDITOR_EDIT_BIB);
    }

    private boolean canAddBib(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.MARC_EDITOR_ADD_BIB);
    }

    @Override
    public EditorForm loadDocument(EditorForm editorForm) {
        WorkBibMarcForm workBibMarcForm = new WorkBibMarcForm();
        String docId = editorForm.getDocId();
        //Modified title display for left pane
        editorForm.setHasLink(true);
        editorForm.setBibliographic(true);
        workBibMarcForm.setHideFooter(true);
        String parameter = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                .DESCRIBE_COMPONENT, OLEConstants.BIB_RENDER_PRINT);
        editorForm.setShowPrint(Boolean.valueOf(parameter));
        // When copied and cancelled
        if (editorForm.isCopyFlag() && StringUtils.isBlank(docId)) {
            if (CollectionUtils.isNotEmpty(editorForm.getBibTreeList())) {
                Bib bib = editorForm.getBibTreeList().get(0).getBib();
                if (null != bib) {
                    docId = bib.getId();
                    editorForm.setDocId(docId);
                    editorForm.setNewDocument(false);
                }
            }
        }

        List<BibTree> bibTreeList = new ArrayList<>();
        BibTree bibTree = null;
        String directory = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(org.kuali.ole.sys.OLEConstants.EXTERNALIZABLE_HELP_URL_KEY);
        editorForm.setExternalHelpUrl(directory+"/reference/webhelp/OLE/content/ch04s01.html#_Editors");
        editorForm.setHeaderText("Bib");
        if (null != docId && StringUtils.isNotEmpty(docId)) {
            try {
                editorForm.setBibLocalIdentifier(docId);
                bibTree = docstoreClient.retrieveBibTree(docId);
                bibTreeList.add(bibTree);
                workBibMarcForm.setBibTreeList(bibTreeList);
                Bib bib = bibTree.getBib();
                BibMarcRecords bibMarcRecords = new BibMarcRecordProcessor().fromXML(bib.getContent());


                editorForm.setCreatedBy(bib.getCreatedBy());
                editorForm.setCreatedDate(bib.getCreatedOn());
                editorForm.setUpdatedBy(bib.getUpdatedBy());
                editorForm.setUpdatedDate(bib.getUpdatedOn());
                editorForm.setStatusUpdatedBy(bib.getStatusUpdatedBy());
                editorForm.setStatusUpdatedOn(bib.getStatusUpdatedOn());
                editorForm.setStaffOnlyFlagForBib(bib.isStaffOnly());

                List<BibMarcRecord> bibMarcRecordList = bibMarcRecords.getRecords();
                BibMarcRecord bibMarcRecord = bibMarcRecordList.get(0);
                Collections.sort(bibMarcRecord.getDataFields());
                //editorForm.setTitle(getMarcFormDataHandler().buildMarcEditorTitleField(bibMarcRecord.getDataFields())+" / "+ DocumentUniqueIDPrefix.getDocumentId(bib.getId()));
                editorForm.setTitle(bib.getTitle() + " / " + bib.getAuthor() + " / " + DocumentUniqueIDPrefix.getDocumentId(bib.getId()));
                workBibMarcForm.setLeader(bibMarcRecord.getLeader());
                workBibMarcForm.setControlFields(getMarcFormDataHandler().buildMarcEditorControlFields(workBibMarcForm, bibMarcRecord.getControlFields()));
                List<MarcEditorDataField> marcEditorDataFields = new ArrayList<MarcEditorDataField>();
                marcEditorDataFields = getMarcFormDataHandler().buildMarcEditorDataFields(bibMarcRecord.getDataFields());
                if (!editorForm.getEditable().equalsIgnoreCase("true")) {
                    for (MarcEditorDataField marcEditorDataField : marcEditorDataFields) {
                        String tempValue = null;
                        if (marcEditorDataField.getValue() != null) {
                            tempValue = marcEditorDataField.getValue().replace("&quot;", "\"");
                            tempValue = tempValue.replaceAll("&nbsp;", " ");
                            marcEditorDataField.setValue(tempValue);
                        }
                    }
                }
                if (CollectionUtils.isEmpty(marcEditorDataFields)) {
                    marcEditorDataFields.add(new MarcEditorDataField());
                }
                workBibMarcForm.setDataFields(marcEditorDataFields);
                BusinessObjectService boService = getBusinessObjectService();
                Map parentCriteria = new HashMap();
                String bibStatusName = bib.getStatus();
                parentCriteria.put("bibliographicRecordStatusName", bibStatusName);
                OleBibliographicRecordStatus bibliographicRecordStatus = boService.findByPrimaryKey(OleBibliographicRecordStatus.class, parentCriteria);
                editorForm.setOleBibliographicRecordStatus(bibliographicRecordStatus);
                //workBibMarcForm.setMessage("Please edit details for the Bib record.");
                if (editorForm.getEditable().equalsIgnoreCase("true")) {
                    boolean hasEditPermission = canEditBib(GlobalVariables.getUserSession().getPrincipalId());
                    if (!hasEditPermission) {
                        editorForm.setHideFooter(false);
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_BIB_EDIT_AUTHORIZATION);
                    } else {
                        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "info.edit.details.bib.record");
                    }
                } else {
                    GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "info.edit.details.bib.record");
                }
            } catch (DocstoreException e) {
                LOG.error(e);
                DocstoreException docstoreException = (DocstoreException) e;
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                } else {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                }
                return workBibMarcForm;
            } catch (Exception e) {
                LOG.error("Exception ", e);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,"docstore.response", e.getMessage() );
            }
        } else {
            //workBibMarcForm.setMessage("Please enter details for new Bib record.");
            boolean hasAddPermission = canAddBib(GlobalVariables.getUserSession().getPrincipalId());
            if (!hasAddPermission) {
                workBibMarcForm.setEditable("false");
                editorForm.setHideFooter(false);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_BIB_CREATE_AUTHORIZATION);
            } else {
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "info.edit.details.bib.new.record");
                if (editorForm.getViewId().equalsIgnoreCase(OLEConstants.EDITOR_WORKFORM_VIEW)) {
                    workBibMarcForm.getDataFields().clear();
                    addDefaultDataFields(workBibMarcForm);
                }
            }

        }
        // Add a node for this document to the left pane tree.
        addDocumentToTree(editorForm);
        setControlFields(workBibMarcForm);
        if (StringUtils.isNotBlank(editorForm.getViewId())) {
            if (editorForm.getViewId().equalsIgnoreCase(OLEConstants.EDITOR_VIEW)) {
                workBibMarcForm.setViewId(OLEConstants.WORK_BIB_EDITOR_VIEW_PAGE);
            } else if (editorForm.getViewId().equalsIgnoreCase(OLEConstants.EDITOR_WORKFORM_VIEW)) {
                workBibMarcForm.setViewId(OLEConstants.EDITOR_WORKFORM_VIEW_PAGE);
                editorForm.setWorkFormViewFlag(true);
            }
        }
        return workBibMarcForm;
    }

    /**
     * This method adds the default data fields for Editor Workform.
     * @param workBibMarcForm
     */
    private void addDefaultDataFields(WorkBibMarcForm workBibMarcForm) {
        List<MarcEditorDataField> dataFields = new ArrayList<>();
        String[] subFields = new String[]{OLEConstants.A};
        dataFields.add(getDataField(OLEConstants.DATAFIELD_020, OLEConstants.DELIMITER_HASH, OLEConstants.DELIMITER_HASH, subFields));
        dataFields.add(getDataField(OLEConstants.DATAFIELD_022, OLEConstants.DELIMITER_HASH, OLEConstants.DELIMITER_HASH, subFields));
        dataFields.add(getDataField(OLEConstants.DATAFIELD_100, OLEConstants.INDICATOR_1, OLEConstants.DELIMITER_HASH, subFields));
        dataFields.add(getDataField(OLEConstants.DATAFIELD_245, OLEConstants.INDICATOR_1, OLEConstants.INDICATOR_0, subFields));
        dataFields.add(getDataField(OLEConstants.DATAFIELD_250, OLEConstants.DELIMITER_HASH, OLEConstants.DELIMITER_HASH, subFields));
        dataFields.add(getDataField(OLEConstants.DATAFIELD_260, OLEConstants.DELIMITER_HASH, OLEConstants.DELIMITER_HASH, new String[]{OLEConstants.A, OLEConstants.B, OLEConstants.C}));
        dataFields.add(getDataField(OLEConstants.DATAFIELD_300, OLEConstants.DELIMITER_HASH, OLEConstants.DELIMITER_HASH, subFields));
        dataFields.add(getDataField(OLEConstants.DATAFIELD_490, OLEConstants.INDICATOR_0, OLEConstants.DELIMITER_HASH, subFields));
        workBibMarcForm.setDataFields(dataFields);

    }

    /**
     * This method is used to get the marc editor data field object.
     * @param tag
     * @param ind1
     * @param ind2
     * @param subFields
     * @return
     */
    private MarcEditorDataField getDataField(String tag, String ind1, String ind2, String[] subFields) {
        MarcEditorDataField dataField = new MarcEditorDataField();
        dataField.setTag(tag);
        dataField.setInd1(ind1);
        dataField.setInd2(ind2);
        StringBuilder valueBuilder = new StringBuilder();
        for (String subField : subFields) {
            if (tag.equalsIgnoreCase(OLEConstants.DATAFIELD_260)) {
                if (subField.equalsIgnoreCase(OLEConstants.A)) {
                    dataField.setValue260a(OLEConstants.PIPE + subField + " ");
                } else if (subField.equalsIgnoreCase(OLEConstants.B)) {
                    dataField.setValue260b(OLEConstants.PIPE + subField + " ");
                } else if (subField.equalsIgnoreCase(OLEConstants.C)) {
                    dataField.setValue260c(OLEConstants.PIPE + subField + " ");
                }
            } else {
                valueBuilder.append(OLEConstants.PIPE + subField + " ");
            }
        }
        if (!tag.equalsIgnoreCase(OLEConstants.DATAFIELD_260)) {
            dataField.setValue(valueBuilder.toString());
        }
        return dataField;
    }
    @Override
    public EditorForm saveDocument(EditorForm editorForm) {

        Bib bib = null;
        String editorStatusMessage = "";
        editorForm.setHasLink(true);
        editorForm.setBibliographic(true);
        WorkBibMarcForm workBibMarcForm = (WorkBibMarcForm) editorForm.getDocumentForm();
        List<MarcEditorDataField> emptyDataFieldList = new ArrayList<>();

        if (editorForm.isWorkFormViewFlag()) {
            editorForm.setOleBibliographicRecordStatus(new OleBibliographicRecordStatus());
            workBibMarcForm.setLeader(OLEConstants.LEADER_DEFAULT);
            if (null != workBibMarcForm.getMarcControlFields()) {
                ControlFields controlFields = workBibMarcForm.getMarcControlFields();
                if (null == controlFields.getControlField008()) {
                    ControlField008 controlField008 = new ControlField008();
                    controlField008.setRawText(OLEConstants.DEFAULT_008);
                    controlFields.setControlField008(controlField008);
                } else {
                    controlFields.getControlField008().setRawText(OLEConstants.DEFAULT_008);
                }
            }
            append260Values(workBibMarcForm);
            emptyDataFieldList = removeEmptyDataFields(workBibMarcForm);
            workBibMarcForm.getDataFields().removeAll(emptyDataFieldList);
        }
        String bibliographicRecordStatusCode = editorForm.getOleBibliographicRecordStatus()
                .getBibliographicRecordStatusCode();
        boolean staffOnlyFlag = editorForm.isStaffOnlyFlagForBib();
        tokenId = editorForm.getTokenId();
        String eResourceID = editorForm.geteResourceId();
        String holdingsId = "";
        HoldingsTree holdingsTree = null;
        buildControlFieldList(workBibMarcForm);
        if (!editorForm.isWorkFormViewFlag()) {
            buildDataFieldList(workBibMarcForm);
        }
        boolean valid = validateMarcEditorData(workBibMarcForm);
        if (valid) {
            buildLeader(workBibMarcForm);
        } else {
            workBibMarcForm.getDataFields().addAll(emptyDataFieldList);
            Collections.sort(workBibMarcForm.getDataFields());
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateStr = sdf.format(date);
        String user = GlobalVariables.getUserSession().getLoggedInUserPrincipalName();
        BusinessObjectService boService = getBusinessObjectService();
        OleBibliographicRecordStatus bibliographicRecordStatus = null;
        if (bibliographicRecordStatusCode != null) {
            Map parentCriteria = new HashMap();
            parentCriteria.put("bibliographicRecordStatusCode", bibliographicRecordStatusCode);
            bibliographicRecordStatus = boService.findByPrimaryKey(OleBibliographicRecordStatus.class, parentCriteria);
        }
        if (valid) {
            MarcEditorFormDataHandler marcEditorFormDataHandler = getMarcFormDataHandler();
            String content = marcEditorFormDataHandler.buildBibRecordForDocStore(workBibMarcForm);
            String uuid = editorForm.getDocId();


            try {
                if (null != uuid && !uuid.trim().equals("")) {
                    editorForm.setBibLocalIdentifier(uuid);
                    if (boService != null) {
                        try {
                            bib = docstoreClient.retrieveBib(uuid);
                        }
                        catch (Exception e) {
                            LOG.error("Exception :", e);
                            DocstoreException docstoreException = (DocstoreException) e;
                            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                            } else {
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                            }
                            return workBibMarcForm;
                        }
                        if (bibliographicRecordStatus != null) {
                            bib.setStatus(bibliographicRecordStatus.getBibliographicRecordStatusName());

                        } else {
                            bib.setStatus("");
                        }
                    }
                    bib.setStaffOnly(staffOnlyFlag);
                    bib.setContent(content);
                    bib.setUpdatedBy(GlobalVariables.getUserSession().getPrincipalName());
                    bib.setUpdatedOn(dateStr);
                    bib.deserializeContent(bib);
                    long startTime = System.currentTimeMillis();
                    try {
                        bib = docstoreClient.updateBib(bib);
                    }
                    catch (Exception e) {
                        DocstoreException docstoreException = (DocstoreException) e;
                        if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode(), bib.getId());
                        } else {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                        }
                        return workBibMarcForm;
                    }
                    long endTime = System.currentTimeMillis();
                    editorForm.setSolrTime(String.valueOf((endTime-startTime)/1000));
                    editorStatusMessage = OLEConstants.BIB_EDITOR_UPDATE_SUCCESS;
                    String bibStatusName = bib.getStatus();
                    editorForm.setCreatedBy(bib.getCreatedBy());
                    editorForm.setCreatedDate(bib.getCreatedOn());
                    editorForm.setUpdatedBy(bib.getUpdatedBy());
                    editorForm.setUpdatedDate(bib.getUpdatedOn());
                    editorForm.setStatusUpdatedOn(bib.getStatusUpdatedOn());
                    editorForm.setStatusUpdatedBy(bib.getStatusUpdatedBy());
                    Map parentCriteria = new HashMap();
                    parentCriteria.put("bibliographicRecordStatusName", bibStatusName);
                    bibliographicRecordStatus = boService
                            .findByPrimaryKey(OleBibliographicRecordStatus.class, parentCriteria);
                    if (bibliographicRecordStatus != null) {
                        editorForm.setOleBibliographicRecordStatus(bibliographicRecordStatus);
                    }
                    //TODO:Edit the processResponse methods
                    //processResponse(responseFromDocstore, workBibMarcForm, uuid);

                } else {
                    BibTree bibTree = new BibTree();
                    bib = new BibMarc();
                    bib.setCategory(DocCategory.WORK.getCode());
                    bib.setType(DocType.BIB.getCode());
                    bib.setFormat(DocFormat.MARC.getCode());
                    bib.setCreatedBy(user);
                    bib.setCreatedOn(dateStr);
                    bib.setStaffOnly(staffOnlyFlag);
                    bib.setContent(content);
                    if (bibliographicRecordStatus != null) {
                        bib.setStatus(bibliographicRecordStatus.getBibliographicRecordStatusName());
                    } else {
                        bib.setStatus("");
                    }
                    bibTree.setBib(bib);
                    holdingsTree = getHoldingsTree(eResourceID);
                    if (holdingsTree != null && holdingsTree.getHoldings() != null) {
                        holdingsTree.getHoldings().setCreatedBy(user);
                        holdingsTree.getHoldings().setCreatedOn(dateStr);
                    /*if (!(StringUtils.isNotEmpty(eResourceID) || (StringUtils.isNotBlank(editorForm.getLinkToOrderOption()) && editorForm.getLinkToOrderOption().equals(OLEConstants.NB_ELECTRONIC)))) {
                        holdingsTree.getItems().get(0).setCreatedBy(user);
                    }*/
                        bibTree.getHoldingsTrees().add(holdingsTree);
                    }
                    try {
                        try {
                            docstoreClient.createBib(bib);
                        } catch (DocstoreException e) {
                            LOG.error("Exception :", e);
                            DocstoreException docstoreException = (DocstoreException) e;
                            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                            } else {
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                            }
                            return workBibMarcForm;
                        } catch (Exception e) {
                            LOG.error("Exception ", e);
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,"docstore.response", e.getMessage() );
                        }
                        if (holdingsTree != null && holdingsTree.getHoldings() != null) {
                            holdingsTree.getHoldings().setBib(bib);
                            try {
                                docstoreClient.createHoldingsTree(holdingsTree);
                            } catch (DocstoreException e) {
                                LOG.error("Exception :", e);
                                DocstoreException docstoreException = (DocstoreException) e;
                                if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                                } else {
                                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                                }
                                return workBibMarcForm;
                            } catch (Exception e) {
                                LOG.error("Exception ", e);
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "docstore.response", e.getMessage());
                            }
                        }
                        editorForm.setDocId(bib.getId());
                        editorForm.setBibId(bib.getId());
                        editorForm.setCreatedBy(bib.getCreatedBy());
                        editorForm.setCreatedDate(bib.getCreatedOn());
                        editorForm.setUpdatedBy(bib.getUpdatedBy());
                        editorForm.setUpdatedDate(bib.getUpdatedOn());
                        editorForm.setStatusUpdatedOn(bib.getStatusUpdatedOn());
                        editorForm.setStatusUpdatedBy(bib.getStatusUpdatedBy());
                        List<BibTree> bibTreeList = new ArrayList<>();
                        bibTreeList.add(bibTree);
                        editorForm.setBibTreeList(bibTreeList);
                        editorStatusMessage = OLEConstants.BIB_EDITOR_CREATE_SUCCESS;
                        //TODO:Edit the processResponse methods
                        //processResponse(responseFromDocstore, workBibMarcForm, uuid);
                        processResponse(bib,editorForm.getLinkToOrderOption());
                        workBibMarcForm.setLeader("");
                        workBibMarcForm.setControlFields(Arrays.asList(new MarcEditorControlField()));
                        workBibMarcForm.setDataFields(Arrays.asList(new MarcEditorDataField()));
                        workBibMarcForm.setUuid("");
                        if (holdingsTree != null && holdingsTree.getHoldings() != null) {
                            holdingsId = holdingsTree.getHoldings().getId();
                            if (holdingsTree.getHoldings().getHoldingsType().equals(OLEConstants.OleHoldings.ELECTRONIC)) {
                                WorkEInstanceOlemlForm eHoldingForm = new WorkEInstanceOlemlForm();
                                HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
                                eHoldingForm.seteResourceId(editorForm.geteResourceId());
                                eHoldingForm.seteResourceTitle(editorForm.geteResourceTitle());
                                eHoldingForm.setTokenId(editorForm.getTokenId());
                                //getOleEResourceSearchService().getEResourcesFields(eResourceID, holdingsTree.getHoldings().getContentObject(), eHoldingForm);
                                holdingsTree.getHoldings().setId(holdingsId);
                                holdingsTree.getHoldings().getContentObject().setAccessStatus(OLEConstants.OleHoldings.ACTIVE);
                                holdingsTree.getHoldings().setContent(holdingOlemlRecordProcessor.toXML(holdingsTree.getHoldings().getContentObject()));
                                docstoreClient.updateHoldings(holdingsTree.getHoldings());
                            }
                        }
                    } catch (Exception e) {
                        LOG.error("Exception :", e);
                        StringBuffer marcEditorErrorMessage = new StringBuffer(OLEConstants.MARC_EDITOR_FAILURE)
                                .append("\n" + e.getMessage());
                        GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, "docstore.response", marcEditorErrorMessage.toString());
                    }
                }
                if (StringUtils.isNotEmpty(bib.getId())) {
                    Map<String, String> tempId = new HashMap<String, String>();
                    tempId.put(OLEConstants.BIB_ID, bib.getId());
                    List<OLEEResourceInstance> oleERSInstances = (List<OLEEResourceInstance>)getBusinessObjectService().findMatching(OLEEResourceInstance.class, tempId);
                    if (oleERSInstances.size() > 0) {
                        for (OLEEResourceInstance oleeResourceInstance : oleERSInstances) {
                            if (oleeResourceInstance.getBibId().equals(bib.getId())) {
                                if (StringUtils.isNotEmpty(oleeResourceInstance.getOleERSIdentifier())) {
                                    Map<String, String> eResId = new HashMap<String, String>();
                                    eResId.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, oleeResourceInstance.getOleERSIdentifier());
                                    OLEEResourceRecordDocument tempDocument = (OLEEResourceRecordDocument)getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, eResId);
                                    Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
                                    tempDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(tempDocument.getDocumentNumber()));
                                    tempDocument.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(tempDocument.getDocumentNumber(), principalPerson));
                                    if (tempDocument != null) {
                                        try {
                                            tempDocument.setSelectInstance(OLEConstants.OLEEResourceRecord.CREATE_NEW_INSTANCE);
                                            tempDocument.seteInstanceFlag(true);
                                            List<Holdings> holdings = new ArrayList<>();
                                            tempDocument.setOleERSInstances(getEResourceInstance(oleeResourceInstance.getOleEResourceInstanceId()));
                                            for (OLEEResourceInstance oleERSInstance : tempDocument.getOleERSInstances()) {
                                                if (oleERSInstance.getBibId().equals(bib.getId())) {
                                                    processResponseForEResource(oleERSInstance.getInstanceId(), bib, tempDocument.getDocumentNumber());
                                                    holdings.add(docstoreClient.retrieveHoldings(oleERSInstance.getInstanceId()));
                                                }
                                            }
                                            for(Holdings holdings1:holdings){
                                                getOleEResourceSearchService().getNewInstance(tempDocument, tempDocument.getDocumentNumber(), holdings1);
                                            }
                                            getBusinessObjectService().save(tempDocument.getOleERSInstances());
                                            getDocumentService().updateDocument(tempDocument);
                                        } catch (Exception e) {
                                            LOG.error("Exception :", e);
                                            throw new RiceRuntimeException(
                                                    "Exception trying to save document: " + tempDocument
                                                            .getDocumentNumber(), e);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (StringUtils.isNotEmpty(eResourceID)) {
                            Map<String, String> eResId = new HashMap<String, String>();
                            eResId.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, eResourceID);
                            OLEEResourceRecordDocument tempDocument = (OLEEResourceRecordDocument) getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, eResId);
                            Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
                            tempDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(tempDocument.getDocumentNumber()));
                            tempDocument.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(tempDocument.getDocumentNumber(), principalPerson));
                            if (tempDocument != null) {
                                try {
                                    tempDocument.setSelectInstance(OLEConstants.OLEEResourceRecord.CREATE_NEW_INSTANCE);
                                    tempDocument.seteInstanceFlag(true);
                                    processResponseForEResource(holdingsId, bib, tempDocument.getDocumentNumber());
                                    getOleEResourceSearchService().getNewInstance(tempDocument, tempDocument.getDocumentNumber(), holdingsTree.getHoldings());
                                    getBusinessObjectService().save(tempDocument.getOleERSInstances());
                                    getDocumentService().updateDocument(tempDocument);
                                } catch (Exception e) {
                                    LOG.error("Exception :", e);
                                    throw new RiceRuntimeException(
                                            "Exception trying to save document: " + tempDocument
                                                    .getDocumentNumber(), e);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Exception :", e);
                StringBuffer marcEditorErrorMessage = new StringBuffer(OLEConstants.MARC_EDITOR_FAILURE)
                        .append("\n" + e.getMessage());
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, "record.save.fail.message");
            }
        } else {

            return workBibMarcForm;
        }
        workBibMarcForm.setViewId("WorkBibEditorViewPage");

        WorkBibMarcForm workBibMarcForm1 = (WorkBibMarcForm) loadDocument(editorForm);
        GlobalVariables.getMessageMap().getInfoMessages().clear();
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, editorStatusMessage);
        editorForm.setShowClose(true);
        return workBibMarcForm1;
    }

    /**
     * This method appends the 260 a,b,c values in the workform.
     * @param workBibMarcForm
     */
    private void append260Values(WorkBibMarcForm workBibMarcForm) {
        for (MarcEditorDataField dataField : workBibMarcForm.getDataFields()) {
            if (StringUtils.isNotBlank(dataField.getTag()) && dataField.getTag().equalsIgnoreCase(OLEConstants.DATAFIELD_260)) {
                StringBuilder value260Builder = new StringBuilder();
                if (StringUtils.isNotBlank(dataField.getValue260a()) && !dataField.getValue260a().trim().equalsIgnoreCase(OLEConstants.PIPE + OLEConstants.A)) {
                    value260Builder.append(dataField.getValue260a() + " ");
                }
                if (StringUtils.isNotBlank(dataField.getValue260b()) && !dataField.getValue260b().trim().equalsIgnoreCase(OLEConstants.PIPE + OLEConstants.B)) {
                    value260Builder.append(dataField.getValue260b() + " ");
                }
                if (StringUtils.isNotBlank(dataField.getValue260c()) && !dataField.getValue260c().trim().equalsIgnoreCase(OLEConstants.PIPE + OLEConstants.C)) {
                    value260Builder.append(dataField.getValue260c() + " ");
                }
                if (StringUtils.isBlank(dataField.getValue())) {
                    dataField.setValue(value260Builder.toString());
                }
            }
        }
    }

    /**
     * This method removes the empty fields from workform.
     * @param workBibMarcForm
     * @return
     */
    private List<MarcEditorDataField> removeEmptyDataFields(WorkBibMarcForm workBibMarcForm) {
        List<MarcEditorDataField> dataFields = workBibMarcForm.getDataFields();
        List<MarcEditorDataField> emptyDataFieldList = new ArrayList<>();
        if (!dataFields.isEmpty()) {
            for (MarcEditorDataField dataField : dataFields) {
                if (null != dataField && (StringUtils.isBlank(dataField.getValue()) || dataField.getValue().trim().equalsIgnoreCase(OLEConstants.PIPE + OLEConstants.A))) {
                    if (StringUtils.isNotBlank(dataField.getTag()) && dataField.getTag().equalsIgnoreCase(OLEConstants.DATAFIELD_245)) {
                        continue;
                    }
                    emptyDataFieldList.add(dataField);
                }
            }
        }
        return emptyDataFieldList;
    }
    /**
     * Gets the isNewRecord attribute.
     * if uuid is null return true else return false.
     *
     * @param uuid
     * @return isNewRecord.
     */
    private boolean isNewRecord(String uuid) {
        return null == uuid;
    }

    /**
     * This method will processResponse,provided the docStore should return response and should have a tokenId
     *
     * @param responseFromDocstore
     * @param editorForm
     */
    public void processResponse(String responseFromDocstore, EditorForm editorForm, String uuid) {
        ResponseHandler responseHandler = new ResponseHandler();
        Response docStoreResponse = responseHandler.toObject(responseFromDocstore);
        if (isSuccessfull(docStoreResponse)) {
            if (responseToOLESentRequired()) {
                if (uuid == null || StringUtils.isEmpty(uuid)) {
                    processNewRecordResponseForOLE(responseFromDocstore);
                } else {
                    processEditRecordResponseForOLE(responseFromDocstore);
                }
            }
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "marc.editor.success.message");
        } else
            editorForm.setMessage(docStoreResponse.getMessage());
    }

    public void processResponseForEResource(String instanceId, Bib bib, String tokenId) throws Exception {
        OLEEditorResponse oleEditorResponse = new OLEEditorResponse();
        oleEditorResponse.setTokenId(tokenId);
        oleEditorResponse.setLinkedInstanceId(instanceId);
        oleEditorResponse.setBib(bib);
        HashMap<String, OLEEditorResponse> oleEditorResponseMap = new HashMap<String, OLEEditorResponse>();
        oleEditorResponseMap.put(tokenId, oleEditorResponse);
        OleDocstoreResponse.getInstance().setEditorResponse(oleEditorResponseMap);
    }

    public void processResponse(Bib bib, String linkToOrderOption) {
        OLEEditorResponse oleEditorResponse = new OLEEditorResponse();
        bib = (Bib) bib.deserializeContent(bib);
        oleEditorResponse.setBib(bib);
        oleEditorResponse.setTokenId(tokenId);
        oleEditorResponse.setLinkToOrderOption(linkToOrderOption);
        HashMap<String, OLEEditorResponse> oleEditorResponseMap = new HashMap<String, OLEEditorResponse>();
        oleEditorResponseMap.put(tokenId, oleEditorResponse);
        OleDocstoreResponse.getInstance().setEditorResponse(oleEditorResponseMap);
    }

    /**
     * The method will process the new record if it receives response form docStore and should have tokenId.
     *
     * @param responseFromDocstore
     */
    private void processNewRecordResponseForOLE(String responseFromDocstore) {
        String instanceUUID = null;
        List bibInfo = null;
        DiscoveryHelperService discoveryHelperService = GlobalResourceLoader.getService(OLEConstants.DISCOVERY_HELPER_SERVICE);
        Response responseRecordForDocstore = getResponseRecordForDocstore(responseFromDocstore);

        OleEditorResponse oleEditorResponse = new OleEditorResponse();
        OleBibRecord oleBibRecord = new OleBibRecord();

        instanceUUID = getUUID(responseRecordForDocstore, OLEConstants.INSTANCE_DOC_TYPE);
        bibInfo = discoveryHelperService.getBibInformationFromInsatnceId(instanceUUID);
        if (bibInfo.isEmpty()) {
            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                LOG.error("Exception :", ex);
            }
            bibInfo = discoveryHelperService.getBibInformationFromInsatnceId(instanceUUID);
        }
        oleBibRecord.setBibAssociatedFieldsValueMap((Map<String, ?>) bibInfo.get(0));
        oleBibRecord.setLinkedInstanceId(instanceUUID);
        oleBibRecord.setBibUUID(getUUID(responseRecordForDocstore, OLEConstants.BIB_DOC_TYPE));

        oleEditorResponse.setOleBibRecord(oleBibRecord);
        oleEditorResponse.setTokenId(tokenId);

        OleEditorResponseHandler oleEditorResponseHandler = new OleEditorResponseHandler();
        String editorResponseXMLForOLE = oleEditorResponseHandler.toXML(oleEditorResponse);


        OleExposedWebServiceImpl oleExposedWebService = (OleExposedWebServiceImpl) SpringContext.getBean("oleExposedWebService");

        oleExposedWebService.addDoctoreResponse(editorResponseXMLForOLE);
    }

    /**
     * The method will process the edit record if it receives response form docStore and should have tokenId.
     *
     * @param responseFromDocstore
     */
    private void processEditRecordResponseForOLE(String responseFromDocstore) {
        String bibUUID = null;
        List bibInfo = null;
        DiscoveryHelperService discoveryHelperService = GlobalResourceLoader.getService(OLEConstants.DISCOVERY_HELPER_SERVICE);
        Response responseRecordForDocstore = getResponseRecordForDocstore(responseFromDocstore);
        OleEditorResponse oleEditorResponse = new OleEditorResponse();
        OleBibRecord oleBibRecord = new OleBibRecord();
        bibUUID = getUUID(responseRecordForDocstore, OLEConstants.BIB_DOC_TYPE);
        bibInfo = discoveryHelperService.getBibInformationFromBibId(bibUUID);
        oleBibRecord.setBibAssociatedFieldsValueMap((Map<String, ?>) bibInfo.get(0));
        HashMap instanceIdentifierHashMap = (HashMap) bibInfo.get(0);
        ArrayList instanceIdentifierList = (ArrayList) instanceIdentifierHashMap.get(OLEConstants.BIB_INSTANCE_ID);
        String instanceId = (String) instanceIdentifierList.get(0);
        oleBibRecord.setLinkedInstanceId(instanceId);
        oleBibRecord.setBibUUID(getUUID(responseRecordForDocstore, OLEConstants.BIB_DOC_TYPE));
        oleEditorResponse.setOleBibRecord(oleBibRecord);
        oleEditorResponse.setTokenId(tokenId);
        OleEditorResponseHandler oleEditorResponseHandler = new OleEditorResponseHandler();
        String editorResponseXMLForOLE = oleEditorResponseHandler.toXML(oleEditorResponse);
        OleExposedWebServiceImpl oleExposedWebService = (OleExposedWebServiceImpl) SpringContext.getBean("oleExposedWebService");
        oleExposedWebService.addDoctoreResponse(editorResponseXMLForOLE);
    }


    /**
     * Gets the isSuccessfull attribute
     *
     * @param docStoreResponse
     * @return Returns isSuccessfull
     */
    private boolean isSuccessfull(Response docStoreResponse) {
        return docStoreResponse.getStatus().equalsIgnoreCase(OLEConstants.OLE_DOCSTORE_RESPONSE_STATUS);
    }


    /**
     * This method returns True if tokenId is not null,else return False.
     *
     * @return boolean
     */
    public boolean responseToOLESentRequired() {
        return null != tokenId;
    }

    /**
     * This method invokes another method to get uuid based on response and docType.
     *
     * @param response
     * @param docType
     * @return uuid
     */
    private String getUUID(Response response, String docType) {
        List<ResponseDocument> documents = response.getDocuments();
        return getUUID(documents, docType);
    }

    /**
     * This method gets the uuid based on the List of responseDocument and docType
     *
     * @param documents
     * @param docType
     * @return uuid
     */
    private String getUUID(List<ResponseDocument> documents, String docType) {
        for (Iterator<ResponseDocument> iterator = documents.iterator(); iterator.hasNext(); ) {
            ResponseDocument responseDocument = iterator.next();
            if (responseDocument.getType().equals(docType)) {
                return responseDocument.getUuid();
            } else {
                return getUUID(responseDocument.getLinkedDocuments(), docType);
            }
        }
        return null;
    }


    /**
     * Gets the webservice URL using PropertyUtil.
     *
     * @return String
     */
    public String getURL() {
        String url = ConfigContext.getCurrentContextConfig().getProperty("oleExposedWebService.url");
        return url;
    }

    /**
     * This method returns a response record based on the responseFromDocstore.
     *
     * @param responseFromDocstore
     * @return Response
     */
    private Response getResponseRecordForDocstore(String responseFromDocstore) {
        return new ResponseHandler().toObject(responseFromDocstore);
    }


    private void updateTreeData(EditorForm editorForm) {
        int indexOfDocument = 0;
       /* List<WorkBibDocument> workBibDocumentList = editorForm.getWorkBibDocumentList();
        int i = 0;
        for (WorkBibDocument bibDocument : workBibDocumentList) {
            if (editorForm.getDocId().equals(bibDocument.getId())) {
                indexOfDocument = i;
                break;
            }
            i++;
        }
        DocstoreHelperService docstoreHelperService = new DocstoreHelperService();
        WorkBibDocument workBibDocument = new WorkBibDocument();
        workBibDocument.setId(editorForm.getDocId());
        workBibDocument = docstoreHelperService.getInfoForBibTree(workBibDocument);
        workBibDocumentList.set(indexOfDocument, workBibDocument);
        editorForm.setWorkBibDocumentList(workBibDocumentList);*/

        List<Bib> bibList = editorForm.getBibList();
        int i = 0;
        for (Bib bib : bibList) {
            if (editorForm.getDocId().equals(bib.getId())) {
                indexOfDocument = i;
                break;
            }
            i++;
        }
        Bib bib = null;
        try {
            bib = docstoreClient.retrieveBib(editorForm.getDocId());
        } catch (Exception e) {
            LOG.error("Exception :", e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            };
        }
        bibList.set(indexOfDocument, bib);
        editorForm.setBibList(bibList);
    }

    /**
     * This method will Validate the MarcEditorForm and return either True or False
     * Validation happen based on existence of leader,Tags and values for controlFields and dataFields .
     *
     * @param workBibMarcForm
     * @return valid
     */
    public boolean validateMarcEditorData(WorkBibMarcForm workBibMarcForm) {
        boolean valid;
        WorkBibMarcRecordValidator wbmRecordValidator = new WorkBibMarcRecordValidator();
        WorkBibMarcRecord workBibMarcRecord = ConvertMarcFormToMarcRecord(workBibMarcForm);
        List<DocStoreValidationError> docStoreValidationErrors = wbmRecordValidator.validate(workBibMarcRecord);
        if (docStoreValidationErrors.size() > 0) {
            for (DocStoreValidationError docStoreValidationError : docStoreValidationErrors) {
                List<String> errorParams = docStoreValidationError.getErrorParams();
                String param1 = "";
                String param2 = "";
                if (errorParams != null) {
                    if (errorParams.size() == 1) {
                        param1 = errorParams.get(0);
                    }
                    if (errorParams.size() == 2) {
                        param1 = errorParams.get(0);
                        param2 = errorParams.get(1);
                    }
                }
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,
                        docStoreValidationError.getErrorId(), param1, param2);
            }
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

    private WorkBibMarcRecord ConvertMarcFormToMarcRecord(WorkBibMarcForm workBibMarcForm) {
        WorkBibMarcRecord workBibMarcRecord = new WorkBibMarcRecord();
        List<ControlField> controlFields = new ArrayList<>();
        List<DataField> dataFields = new ArrayList<DataField>();
        for (MarcEditorControlField MEControlFiled : workBibMarcForm.getControlFields()) {
            ControlField controlField = new ControlField();
            controlField.setTag(MEControlFiled.getTag());
            controlField.setValue(MEControlFiled.getValue());
            controlFields.add(controlField);
        }
        for (MarcEditorDataField MEDataField : workBibMarcForm.getDataFields()) {
            buildDataField(MEDataField, dataFields);
        }
        workBibMarcRecord.setLeader(workBibMarcForm.getLeader());
        workBibMarcRecord.setControlFields(controlFields);
        workBibMarcRecord.setDataFields(dataFields);
        return workBibMarcRecord;
    }

    private void buildDataField(MarcEditorDataField meDataField, List<DataField> dataFields) {
        DataField dataField = new DataField();
        dataField.setTag(meDataField.getTag());

        if (meDataField.getValue().length() > 1 && !meDataField.getValue().startsWith("|") && !meDataField.getValue().contains("|a")) {
            meDataField.setValue("|a " + meDataField.getValue());
        }


        if (meDataField.getValue().startsWith("|") && meDataField.getValue().length() > 1) {
            String[] values = meDataField.getValue().split("\\|");
            for (int i = 1; i < values.length; i++) {
                SubField subField = new SubField();
                subField.setCode(values[i].substring(0, 1));
                subField.setValue(values[i].substring(1, values[i].length()));
                dataField.addSubField(subField);
            }
        } else {
            SubField subField = new SubField();
            subField.setValue(meDataField.getValue());
            subField.setCode("");
            dataField.addSubField(subField);
        }

        if (meDataField.getInd1() != null && !meDataField.getInd1().isEmpty()) {
            dataField.setInd1(meDataField.getInd1());
        } else {
            dataField.setInd1(" ");
        }

        if (meDataField.getInd2() != null && !meDataField.getInd2().isEmpty()) {
            dataField.setInd2(meDataField.getInd2());
        } else {
            dataField.setInd2(" ");
        }
        dataFields.add(dataField);
    }

    private void buildDataFieldList(WorkBibMarcForm workBibMarcForm) {
        List<MarcEditorDataField> dataFieldList = workBibMarcForm.getDataFields();
        List<MarcEditorDataField> dataFieldList1 = new ArrayList<>();
        int count = 0;
        if (dataFieldList.size() > 1) {
            for (MarcEditorDataField dataField : dataFieldList) {

                if (dataField.getInd1().equals("") || dataField.getInd1().equals("#") || dataField.getInd1() == null) {
                    dataField.setInd1(" ");
                }

                if (dataField.getInd2().equals("") || dataField.getInd2().equals("#") || dataField.getInd2() == null) {
                    dataField.setInd2(" ");
                }

                if (dataField.getTag().equals("") && dataField.getValue().equals("") && (dataField.getInd1().equals("") || dataField.getInd1().equals(" ") || dataField.getInd1().equals("#") || dataField.getInd1() == null) && (dataField.getInd2().equals("") || dataField.getInd2().equals(" ") || dataField.getInd2().equals("#") || dataField.getInd2() == null)) {
                    count++;
                }
                if (!dataField.getTag().equals("") || !dataField.getValue().equals("") || !(dataField.getInd1().equals("") || dataField.getInd1().equals(" ") || dataField.getInd1().equals("#") || dataField.getInd1() == null) || !(dataField.getInd2().equals("") || dataField.getInd2().equals(" ") || dataField.getInd2().equals("#") || dataField.getInd2() == null)) {

                    dataFieldList1.add(dataField);
                }
                if (count == dataFieldList.size()) {
                    if (dataField.getTag().equals("") && dataField.getValue().equals("") && (dataField.getInd1().equals("") || dataField.getInd1().equals(" ") || dataField.getInd1().equals("#") || dataField.getInd1() == null) && (dataField.getInd2().equals("") || dataField.getInd2().equals(" ") || dataField.getInd2().equals("#") || dataField.getInd2() == null)) {

                        dataFieldList1.add(dataFieldList.get(0));

                    }
                }


            }
            workBibMarcForm.setDataFields(dataFieldList1);
        } else {
            workBibMarcForm.setDataFields(dataFieldList);
        }

    }


    private void buildLeader(WorkBibMarcForm workBibMarcForm) {
        workBibMarcForm.setLeader(workBibMarcForm.getLeader().replace("#", " "));
    }

    @Override
    public EditorForm deleteDocument(EditorForm editorForm) {
        return new WorkBibMarcForm();
    }

    @Override
    public String saveDocument(BibTree bibTree, EditorForm form) {
        String str = " success";
        try {
            docstoreClient.updateBib(bibTree.getBib());
        } catch (DocstoreException e) {
            LOG.error("Exception :", e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
        } catch (Exception e) {
            LOG.error("Exception ", e);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,"docstore.response", e.getMessage() );
        }
        return str;
    }

    @Override
    public EditorForm createNewRecord(EditorForm editorForm, BibTree bibTree) {
//        editorForm.setHeaderText("Import Bib Step-4 Bibliographic Editor - MARC Format");
        editorForm.setHeaderText("Import Bib Step-4 Bibliographic Editor");
        WorkBibMarcForm workBibMarcForm = new WorkBibMarcForm();

        Bib bib = bibTree.getBib();
        if(StringUtils.isEmpty(bib.getCreatedBy())) {
            bib.setCreatedBy(GlobalVariables.getUserSession().getPrincipalName());
        }
        if (bib.getContent() != null) {

            BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(bib.getContent());

            List<BibMarcRecord> bibMarcRecordsRecords = bibMarcRecords.getRecords();
            for (BibMarcRecord bibMarcRecord : bibMarcRecordsRecords) {
                workBibMarcForm.setLeader(bibMarcRecord.getLeader());
                workBibMarcForm.setControlFields(getMarcFormDataHandler().buildMarcEditorControlFields(workBibMarcForm,
                        bibMarcRecord.getControlFields()));
                editorForm.setTitle(getMarcFormDataHandler().buildMarcEditorTitleField(bibMarcRecord.getDataFields())+" / "+ DocumentUniqueIDPrefix.getDocumentId(bib.getId()));
                workBibMarcForm.setDataFields(
                        getMarcFormDataHandler().buildMarcEditorDataFields(bibMarcRecord.getDataFields()));
                OleBibliographicRecordStatus bibliographicRecordStatus = null;
                BusinessObjectService boService = getBusinessObjectService();
                String bibStatusName = bib.getStatus();

                editorForm.setBibStatus(bibStatusName);
                Map parentCriteria = new HashMap();
                parentCriteria.put("bibliographicRecordStatusName", bibStatusName);
                bibliographicRecordStatus = boService
                        .findByPrimaryKey(OleBibliographicRecordStatus.class, parentCriteria);
                if (bibliographicRecordStatus != null) {
                    editorForm.setOleBibliographicRecordStatus(bibliographicRecordStatus);
                }
                editorForm.setMessage("Please edit details for the Bib record.");
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_ERRORS, "info.edit.details.bib.record");
            }
        }

        // Add a node for this document to the left pane tree.
        addDocumentToTree(editorForm);
        editorForm.setNeedToCreateInstance(true);

        workBibMarcForm.setViewId("WorkBibEditorViewPage");
        setControlFields(workBibMarcForm);
        return workBibMarcForm;
    }


    public void setControlFields(WorkBibMarcForm workBibMarcForm) {
        List<ControlField006Text> controlField006Texts = workBibMarcForm.getMarcControlFields().getControlFields006List();
        List<ControlField006Text> controlField006EditText = new ArrayList<>();
        for (ControlField006Text controlField006Text : controlField006Texts) {
            if (controlField006Text.getRawText() != null) {
                controlField006Text.setRawText(controlField006Text.getRawText().replaceAll(" ", "#"));
                controlField006EditText.add(controlField006Text);
            }
        }
        if (controlField006EditText.size() > 0) {
            workBibMarcForm.getMarcControlFields().setControlFields006List(controlField006EditText);
        }
        List<ControlField007Text> controlField007Texts = workBibMarcForm.getMarcControlFields().getControlFields007List
                ();
        List<ControlField007Text> controlField007EditText = new ArrayList<>();
        for (ControlField007Text controlField007Text : controlField007Texts) {
            if (controlField007Text.getRawText() != null) {
                controlField007Text.setRawText(controlField007Text.getRawText().replaceAll(" ", "#"));
                controlField007EditText.add(controlField007Text);
            }
        }
        if (controlField007EditText.size() > 0) {
            workBibMarcForm.getMarcControlFields().setControlFields007List(controlField007EditText);
        }
        ControlField008 controlField008 = workBibMarcForm.getMarcControlFields().getControlField008();
        if (controlField008 != null) {
            controlField008.setRawText(controlField008.getRawText().replaceAll(" ", "#"));
            workBibMarcForm.getMarcControlFields().setControlField008(controlField008);
        }
        if (workBibMarcForm.getLeader() != null) {
            workBibMarcForm.setLeader(workBibMarcForm.getLeader().replaceAll(" ", "#"));
        }
    }

    @Override
    public EditorForm editNewRecord(EditorForm editorForm, BibTree bibTree) {
        WorkBibMarcForm workBibMarcForm = (WorkBibMarcForm) editorForm.getDocumentForm();
        if (!validateMarcEditorData(workBibMarcForm)) {
            workBibMarcForm.setValidInput(false);
            return workBibMarcForm;
        }
        String bibliographicRecordStatusCode = editorForm.getOleBibliographicRecordStatus()
                .getBibliographicRecordStatusCode();
        String content = getMarcFormDataHandler().buildBibRecordForDocStore(workBibMarcForm);
        bibTree.getBib().setContent(content);
        editorForm.setCreatedBy(bibTree.getBib().getCreatedBy());
        editorForm.setCreatedDate(bibTree.getBib().getCreatedOn());
        OleBibliographicRecordStatus bibliographicRecordStatus = null;
        BusinessObjectService boService = getBusinessObjectService();
        if (bibliographicRecordStatusCode != null) {
            Map parentCriteria = new HashMap();
            parentCriteria.put("bibliographicRecordStatusCode", bibliographicRecordStatusCode);
            bibliographicRecordStatus = boService
                    .findByPrimaryKey(OleBibliographicRecordStatus.class, parentCriteria);
        }

        if (bibliographicRecordStatus != null) {
            bibTree.getBib().setStatus(bibliographicRecordStatus.getBibliographicRecordStatusName());
            editorForm.setOleBibliographicRecordStatus(bibliographicRecordStatus);

        } else {
            bibTree.getBib().setStatus("");
        }
        bibTree.getBib().setStaffOnly(editorForm.isStaffOnlyFlagForBib());

        return editorForm;
    }

    private void addDocumentToTree(EditorForm editorForm) {
        // TODO: Make sure the same document is not added more than once.

        List<BibTree> bibTreeList = editorForm.getBibTreeList();
        List<Bib> bibList = new ArrayList<>();
        int indexOfDocument = 0;
        int i = 0;
        String title = "New Bib1";
        boolean updateTitle = false;
        if (null == bibTreeList) {
            bibTreeList = new ArrayList<BibTree>();
        }
        BibTree tempBibTree = new BibTree();
        if (null == editorForm.getDocId()) {
            LOG.info("bibTreeList size before remove-->" + bibTreeList.size());
            for (BibTree bibTree : bibTreeList) {
                if (bibTree.getBib().getTitle().equalsIgnoreCase("New Bib1")) {
                    tempBibTree = bibTree;
                    // workBibDocumentList.remove(workBibDocument);
                }
            }
            bibTreeList.remove(tempBibTree);
            LOG.info("workBibDocumentList size after remove-->" + bibTreeList.size());

            BibTree bibTree = new BibTree();
            Bib bib = new Bib();
            bib.setTitle(title);
            bibTree.setBib(bib);
            /*bibTree.getBib().setTitle(title);*/
            bibTreeList.add(bibTree);
        } else {
            boolean isUpdated = false;
            for (BibTree bibTree : bibTreeList) {
                if (bibTree.getBib() != null) {
                    if (bibTree.getBib().getId() != null && bibTree.getBib().getId().equals(editorForm.getDocId())) {
                        isUpdated = true;
                        break;
                    } else if (bibTree.getBib().getTitle().equals(title)) {
                        indexOfDocument = i;
                        updateTitle = true;
                        break;
                    }
                }
                i++;
            }
            if (!isUpdated) {
                Bib bib = null;
                try {
                    bib = docstoreClient.retrieveBib(editorForm.getDocId());
                } catch (Exception e) {
                    LOG.error("Exception :", e);
                    DocstoreException docstoreException = (DocstoreException) e;
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                    } else {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                    }
                }
                if (updateTitle && bibList.size() > 0) {
                    bibList.set(indexOfDocument, bib);
                } else {
                    bibList.add(bib);
                }

            }

        }
        editorForm.setBibTreeList(bibTreeList);
    }

    @Override
    public EditorForm addORDeleteFields(EditorForm editorForm, HttpServletRequest request) {

        String methodName = request.getParameter("methodToCall");
        if (methodName.equalsIgnoreCase("addControlField006")) {
            WorkBibMarcForm marcEditorForm = (WorkBibMarcForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            index++;
            List<ControlField006Text> marcEditorControlFieldList = marcEditorForm.getMarcControlFields().getControlFields006List();
            marcEditorControlFieldList.add(index, new ControlField006Text());
            marcEditorForm.getMarcControlFields().setControlFields006List(marcEditorControlFieldList);
            editorForm.setDocumentForm(marcEditorForm);
        } else if (methodName.equalsIgnoreCase("removeControlField006")) {
            WorkBibMarcForm marcEditorForm = (WorkBibMarcForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            List<ControlField006Text> marcEditorControlFieldList = marcEditorForm.getMarcControlFields().getControlFields006List();
            if (marcEditorControlFieldList.size() > 1) {
                marcEditorControlFieldList.remove(index);
            }
            editorForm.setDocumentForm(marcEditorForm);
        } else if (methodName.equalsIgnoreCase("addControlField007")) {
            WorkBibMarcForm marcEditorForm = (WorkBibMarcForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            index++;
            List<ControlField007Text> marcEditorControlFieldList = marcEditorForm.getMarcControlFields().getControlFields007List();
            marcEditorControlFieldList.add(index, new ControlField007Text());
            marcEditorForm.getMarcControlFields().setControlFields007List(marcEditorControlFieldList);
            editorForm.setDocumentForm(marcEditorForm);
        } else if (methodName.equalsIgnoreCase("removeControlField007")) {
            WorkBibMarcForm marcEditorForm = (WorkBibMarcForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            List<ControlField007Text> marcEditorControlFieldList = marcEditorForm.getMarcControlFields().getControlFields007List();
            if (marcEditorControlFieldList.size() > 1) {
                marcEditorControlFieldList.remove(index);
            }
            editorForm.setDocumentForm(marcEditorForm);
        } else if (methodName.equalsIgnoreCase("addDataField")) {
            WorkBibMarcForm marcEditorForm = (WorkBibMarcForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            index++;
            List<MarcEditorDataField> editorDataFieldList = marcEditorForm.getDataFields();
            for (MarcEditorDataField editorDataField : editorDataFieldList) {
                if (null != editorDataField.getValue()) {
                    String modifiedValue = editorDataField.getValue().replace("\"", "&quot;");
                    editorDataField.setValue(modifiedValue);
                }
            }
            MarcEditorDataField marcEditorDataField = new MarcEditorDataField();
            if (editorForm.isShortcutAddDataField()) {
                marcEditorDataField.setTag("035");
                marcEditorDataField.setValue("(OCoLC)");
                editorForm.setShortcutAddDataField(false);
            }
            editorDataFieldList.add(index, marcEditorDataField);
            marcEditorForm.setDataFields(editorDataFieldList);
            editorForm.setDocumentForm(marcEditorForm);
        } else if (methodName.equalsIgnoreCase("removeDataField")) {
            WorkBibMarcForm marcEditorForm = (WorkBibMarcForm) editorForm.getDocumentForm();
            List<MarcEditorDataField> marcEditorDataFieldList = marcEditorForm.getDataFields();
            List<MarcEditorDataField> removeEditorDataFieldList = new ArrayList<>();
            if (marcEditorDataFieldList.size() > 1) {
                for (MarcEditorDataField marcEditorDataField : marcEditorDataFieldList) {
                    if (marcEditorDataField.isSelected()) {
                        removeEditorDataFieldList.add(marcEditorDataField);
                    }
                }
                marcEditorDataFieldList.removeAll(removeEditorDataFieldList);
            }
            for (MarcEditorDataField marcDataField : marcEditorDataFieldList) {
                String modifiedValue = marcDataField.getValue().replace("\"", "&quot;");
                marcDataField.setValue(modifiedValue);
            }
            if (CollectionUtils.isEmpty(marcEditorDataFieldList)) {
                marcEditorDataFieldList.add(new MarcEditorDataField());
            }
            marcEditorForm.setDataFields(marcEditorDataFieldList);
            editorForm.setDocumentForm(marcEditorForm);
        }
        return editorForm;
    }

    /**
     * Gets the MarcEditorFormDataHandler attribute.
     *
     * @return MarcEditorFormDataHandler
     */
    private MarcEditorFormDataHandler getMarcFormDataHandler() {
        if (null == marcEditorFormDataHandler) {
            marcEditorFormDataHandler = new MarcEditorFormDataHandler();
        }
        return marcEditorFormDataHandler;
    }


    @Override
    public EditorForm deleteVerify(EditorForm editorForm) throws Exception {
        WorkBibMarcForm workBibMarcForm = new WorkBibMarcForm();
        String docId = editorForm.getDocId();
        editorForm.setShowDeleteTree(true);
        List<String> uuidList = new ArrayList<>(0);
        uuidList.add(editorForm.getDocId());
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        Node<DocumentTreeNode, String> docTree = documentSelectionTree.add(uuidList, DocType.BIB.getDescription());
        editorForm.getDocTree().setRootElement(docTree);
        workBibMarcForm.setViewId("DeleteViewPage");
        int holdingsCount = 0;
        int itemCount = 0;
        BibTree bibTree = docstoreClient.retrieveBibTree(docId);
        List<HoldingsTree> holdingsTrees = bibTree.getHoldingsTrees();
        holdingsCount = holdingsTrees.size();
        for(HoldingsTree holdingsTree : holdingsTrees){
            int size = holdingsTree.getItems().size();
            itemCount = itemCount + size;
        }
        StringBuffer deleteMessage = new StringBuffer();
        deleteMessage.append("WARNING : All attached holdings (");
        deleteMessage.append(holdingsCount +")");
        deleteMessage.append(" and  items (");
        deleteMessage.append(itemCount + ") on the following bibliographic record will be deleted.");
        editorForm.setDeleteMessage(deleteMessage.toString());
        return workBibMarcForm;
    }


    private Node<DocumentTreeNode, String> buildDocSelectionTree(String responseXml) throws SolrServerException {
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
    }

    /**
     * This method deletes the bib record from docstore by the doc id.
     * @param editorForm
     * @return
     * @throws Exception
     */
    @Override
    public EditorForm delete(EditorForm editorForm) throws Exception {
        return deleteFromDocStore(editorForm);
    }

    @Override
    public EditorForm addORRemoveExtentOfOwnership(EditorForm editorForm, HttpServletRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm addORRemoveAccessInformationAndHoldingsNotes(EditorForm editorForm, HttpServletRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm addORRemoveItemNote(EditorForm editorForm, HttpServletRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Set tag and value for Control field
     *
     * @param tag
     * @param value
     * @return
     */
    private MarcEditorControlField getMarcEditorControlField(String tag, String value) {
        MarcEditorControlField marcEditorControlField = new MarcEditorControlField();
        marcEditorControlField.setTag(tag);
        marcEditorControlField.setValue(value);
        return marcEditorControlField;
    }

    /**
     * Build controlField list with tag and value pairs.
     *
     * @param workBibMarcForm
     */
    private void buildControlFieldList(WorkBibMarcForm workBibMarcForm) {
        ControlFields controlFields = workBibMarcForm.getMarcControlFields();

        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss.S");
        controlFields.setControlField005(sdf.format(dt).substring(0, 16));

        List<MarcEditorControlField> marcEditorControlFields = new ArrayList<MarcEditorControlField>();
        if (StringUtils.isNotEmpty(controlFields.getControlField001())) {
            marcEditorControlFields.add(getMarcEditorControlField(ControlFields.CONTROL_FIELD_001,
                    controlFields.getControlField001()));
        }
        if (StringUtils.isNotEmpty(controlFields.getControlField003())) {
            marcEditorControlFields.add(getMarcEditorControlField(ControlFields.CONTROL_FIELD_003,
                    controlFields.getControlField003()));
        }
        if (StringUtils.isNotEmpty(controlFields.getControlField005())) {
            marcEditorControlFields.add(getMarcEditorControlField(ControlFields.CONTROL_FIELD_005,
                    controlFields.getControlField005()));
        }
        if (controlFields.getControlFields006List().size() > 0) {
            for (ControlField006Text controlField006Text : controlFields.getControlFields006List()) {
                if (controlField006Text.getRawText() != null && StringUtils.isNotEmpty(controlField006Text.getRawText()))
                    marcEditorControlFields.add(getMarcEditorControlField(ControlFields.CONTROL_FIELD_006,
                            controlField006Text.getRawText().replace("#", " ")));
            }
        }
        if (controlFields.getControlFields007List().size() > 0) {
            for (ControlField007Text controlField007Text : controlFields.getControlFields007List()) {
                if (controlField007Text.getRawText() != null && StringUtils.isNotEmpty(controlField007Text.getRawText()))
                    marcEditorControlFields.add(getMarcEditorControlField(ControlFields.CONTROL_FIELD_007,
                            controlField007Text.getRawText().replace("#", " ")));
            }
        }
        if (StringUtils.isNotEmpty(controlFields.getControlField008().getRawText())) {
            sdf = new SimpleDateFormat("yyMMdd");
            if (controlFields.getControlField008().getRawText().length() == 40 && (controlFields.getControlField008().getRawText().substring(0, 6).contains("#") || workBibMarcForm.isCopyFlag())) {
                controlFields.getControlField008().setRawText(sdf.format(dt) + controlFields.getControlField008().getRawText
                        ().substring(6, 40));
            }

            marcEditorControlFields.add(getMarcEditorControlField(ControlFields.CONTROL_FIELD_008,
                    controlFields.getControlField008().getRawText().replace("#", " ")));
        }
        workBibMarcForm.setControlFields(marcEditorControlFields);
    }

    @Override
    public EditorForm showBibs(EditorForm editorForm) {
        return null;
    }

    public EditorForm copy(EditorForm editorForm) {
        editorForm.setDocId(null);
        editorForm.setBibId(null);
        editorForm.setUpdatedBy(null);
        editorForm.setUpdatedDate(null);
        editorForm.setCreatedBy(null);
        editorForm.setCreatedDate(null);
        editorForm.setStaffOnlyFlagForBib(false);
        editorForm.setStatusUpdatedBy(null);
        editorForm.setStatusUpdatedOn(null);
        editorForm.setNewDocument(true);
        editorForm.setLeftTree(new Tree<DocumentTreeNode, String>());
        editorForm.setBibTreeList(null);
        editorForm.setTitle(null);
        WorkBibMarcForm workBibMarcForm = (WorkBibMarcForm) editorForm.getDocumentForm();
        workBibMarcForm.getMarcControlFields().setLocalId(null);
        workBibMarcForm.getMarcControlFields().setControlField001(null);
        workBibMarcForm.getMarcControlFields().setControlField003(null);
        workBibMarcForm.getMarcControlFields().setControlField005(null);
        workBibMarcForm.setDocId(null);
        workBibMarcForm.setBibTreeList(null);
        editorForm.setOleBibliographicRecordStatus(new OleBibliographicRecordStatus());
        eliminate9xxFields(workBibMarcForm);
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, OLEConstants.MARC_EDITOR_BIB_COPY_MESSAGE);
        return editorForm;
    }

    /**
     * This method eliminates 9xx fields if any.
     * @param workBibMarcForm
     */
    private void eliminate9xxFields(WorkBibMarcForm workBibMarcForm) {
        List<MarcEditorDataField> dataFields = workBibMarcForm.getDataFields();
        List<MarcEditorDataField> newDataFields = new ArrayList<>();
        for (MarcEditorDataField dataField : dataFields) {
            if (null != dataField.getTag()) {
                if (!dataField.getTag().startsWith("9")) {
                    newDataFields.add(dataField);
                }
            }
        }
        workBibMarcForm.setDataFields(newDataFields);
    }

    @Override
    public Boolean isValidUpdate(EditorForm editorForm) {
        String docId = editorForm.getDocId();
        Boolean isValidUpdate = true;
        if (StringUtils.isNotEmpty(docId)) {
            try {
                if (editorForm.getDocType().equalsIgnoreCase("bibliographic")) {
                    Bib bib = docstoreClient.retrieveBib(editorForm.getDocId());
                    if (bib.getUpdatedOn() != null) {
                        if (editorForm.getUpdatedDate() == null) {
                            isValidUpdate = false;
                        }
                    }
                    if (!editorForm.getUpdatedDate().equals(bib.getUpdatedOn())) {
                        isValidUpdate = false;
                    }
                }
                if (editorForm.getDocType().equalsIgnoreCase("holdings")) {
                    Holdings holdings = docstoreClient.retrieveHoldings(editorForm.getDocId());
                    if (editorForm.getUpdatedDate().equals(holdings.getUpdatedOn())) {
                        isValidUpdate = false;
                    }

                }
                if (editorForm.getDocType().equalsIgnoreCase("item")) {
                    Item item = docstoreClient.retrieveItem(editorForm.getDocId());
                    if (editorForm.getUpdatedDate().equals(item.getUpdatedOn())) {
                        isValidUpdate = false;
                    }

                }
            } catch (Exception e) {
                LOG.error("New Record....." + docId);
            }
        }
        return isValidUpdate;
    }

    public  List<OLEEResourceInstance> getEResourceInstance(String oleERSIdentifier) {
        List<OLEEResourceInstance> oleeResourceInstances = new ArrayList<>();
        if (oleERSIdentifier != null) {
            Map ids = new HashMap();
            ids.put("oleEResourceInstanceId", oleERSIdentifier);
            OLEEResourceInstance oleeResourceInstance = (OLEEResourceInstance) getBusinessObjectService().findByPrimaryKey(OLEEResourceInstance.class, ids);
            oleeResourceInstances.add(oleeResourceInstance);
        }
        return oleeResourceInstances;
    }

}