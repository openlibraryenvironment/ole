package org.kuali.ole.describe.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.ASRItem;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.describe.bo.DocumentSelectionTree;
import org.kuali.ole.describe.bo.DocumentTreeNode;
import org.kuali.ole.describe.bo.InstanceEditorFormDataHandler;
import org.kuali.ole.describe.form.EditorForm;
import org.kuali.ole.describe.form.WorkBibMarcForm;
import org.kuali.ole.describe.form.WorkInstanceOlemlForm;
import org.kuali.ole.describe.keyvalue.LocationValuesBuilder;
import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.engine.client.DocstoreLocalClient;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;
import org.kuali.ole.select.bo.OLESerialReceivingHistory;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.service.impl.OLESerialReceivingServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 1/7/14
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkHoldingsOlemlEditor extends AbstractEditor {

    private static final Logger LOG = LoggerFactory.getLogger(WorkHoldingsOlemlEditor.class);
    private InstanceEditorFormDataHandler instanceEditorFormDataHandler = null;
    private static WorkHoldingsOlemlEditor workHoldingsOlemlEditor = null;
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor ;
    private ASRHelperServiceImpl asrHelperService ;
    private BusinessObjectService businessObjectService ;
    private DocstoreClient docstoreClient = getDocstoreLocalClient();

    public static WorkHoldingsOlemlEditor getInstance() {
        if (workHoldingsOlemlEditor == null) {
            workHoldingsOlemlEditor = new WorkHoldingsOlemlEditor();
        }
        return workHoldingsOlemlEditor;
    }

    public ItemOlemlRecordProcessor getItemOlemlRecordProcessor(){
        if(itemOlemlRecordProcessor == null){
            itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        }
        return itemOlemlRecordProcessor;
    }


    public ASRHelperServiceImpl getAsrHelperService(){
        if(asrHelperService == null){
            asrHelperService = new ASRHelperServiceImpl();
        }
        return asrHelperService;

    }

    public BusinessObjectService getBusinessObjectService(){
        if(businessObjectService == null){
          businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    private WorkHoldingsOlemlEditor() {

    }

    @Override
    public EditorForm loadDocument(EditorForm editorForm) {

        //TODO: set title info
        WorkInstanceOlemlForm workInstanceOlemlForm = new WorkInstanceOlemlForm();
        String directory = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(org.kuali.ole.sys.OLEConstants.EXTERNALIZABLE_HELP_URL_KEY);
        editorForm.setExternalHelpUrl(directory+"/reference/webhelp/OLE/content/ch04s01.html#_Instance_Editor_1");
        editorForm.setHeaderText("Holdings");
        editorForm.setHasLink(true);
        editorForm.setShowEditorFooter(true);
        String parameter = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                .DESCRIBE_COMPONENT, OLEConstants.HOLDINGS_SUPRESS_SHELVINGORDER);
        if(Boolean.valueOf(parameter)){
            editorForm.setSupressHoldingsShelving(false);
        }else{
            editorForm.setSupressHoldingsShelving(true);
        }
        String bibId = editorForm.getBibId();
        List<BibTree> bibTreeList = new ArrayList();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" HH:mm:ss");
        String dateStr = sdf.format(date);
        BibTree bibTree = null;
        if (!StringUtils.isNotEmpty(bibId)) {
            workInstanceOlemlForm.setMessage("Error: Invalid bibId id:" + bibId);
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "invalid.bib");
            return workInstanceOlemlForm;
        }

        Bib bib = null;
        try {
            bibTree = docstoreClient.retrieveBibTree(bibId);
        }catch (DocstoreException e) {
            LOG.error("Exception : ", e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
            return workInstanceOlemlForm;
        } catch (Exception e) {
            LOG.error("Exception ", e);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,"docstore.response", e.getMessage() );
        }
          bibTreeList.add(bibTree);
          workInstanceOlemlForm.setBibTreeList(bibTreeList);
          bib = bibTree.getBib();
          String titleField = bib.getTitle() + " / " + bib.getAuthor() + " / " + DocumentUniqueIDPrefix.getDocumentId(bib.getId());
          editorForm.setTitle(titleField);
          editorForm.setHasLink(true);
        if (!isValidBib(bib.getContent())) {
            workInstanceOlemlForm.setMessage("Error: Invalid bibId id:" + bibId);
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "invalid.bib");
            return workInstanceOlemlForm;
        }

            String docId = editorForm.getDocId();
            if (StringUtils.isNotEmpty(docId)) {
                editorForm.setHoldingLocalIdentifier(DocumentUniqueIDPrefix.getDocumentId(editorForm.getDocId()));
                Holdings holdings = null;
                try {
                    holdings = docstoreClient.retrieveHoldings(editorForm.getDocId());
                }catch (DocstoreException e) {
                    LOG.error("Exception : ", e);
                    DocstoreException docstoreException = (DocstoreException) e;
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                    } else {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                    }
                    return workInstanceOlemlForm;
                } catch (Exception e) {
                    LOG.error("Exception ", e);
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,"docstore.response", e.getMessage() );
                }                String docStoreData = holdings.getContent();
                OleHoldings oleHoldings = null;
                if (StringUtils.isNotEmpty(docStoreData)) {
                    oleHoldings = new HoldingOlemlRecordProcessor().fromXML(docStoreData);
                    editorForm.setStaffOnlyFlagForHoldings(holdings.isStaffOnly());
                    editorForm.setHoldingCreatedDate(holdings.getCreatedOn());
                    editorForm.setHoldingCreatedBy(holdings.getCreatedBy());
                    editorForm.setHoldingUpdatedDate(holdings.getUpdatedOn());
                    editorForm.setHoldingUpdatedBy(holdings.getUpdatedBy());
//                  editorForm.setHoldingLocalIdentifier(holdings.getLocalId());

                    ensureMultipleValuesInOleHoldings(oleHoldings);


                    workInstanceOlemlForm.setSelectedHolding(oleHoldings);
                    getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                } else {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "ds.error.holdings.notfound");
                    workInstanceOlemlForm.setViewId("WorkHoldingsViewPage");
                    return workInstanceOlemlForm;
                }
            //workInstanceOlemlForm.setMessage("Holdings record loaded successfully.");
            if (editorForm.getEditable().equalsIgnoreCase("false")) {
                if(editorForm.getMethodToCall() != null && editorForm.getMethodToCall().equalsIgnoreCase("copy")){
                    GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO,"record.copy.instance.message");
                } else {
                    GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO,"holdings.record.load.message");
                }
            } else {
                boolean hasPermission = canEditInstance(GlobalVariables.getUserSession().getPrincipalId());
                if (hasPermission) {
                    if(editorForm.getMethodToCall() != null && editorForm.getMethodToCall().equalsIgnoreCase("copyInstance")){
                        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO,"record.copy.instance.message");
                    } else {
                        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "holdings.record.load.message");
                    }
                } else {
                    editorForm.setHideFooter(false);
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_EDIT_INSTANCE);
                }
            }
        } else {
                editorForm.setHoldingLocalIdentifier("");
                editorForm.setHoldingUpdatedBy("");
                editorForm.setHoldingUpdatedDate("");
                setStaffOnly(editorForm);
                editorForm.setHoldingCreatedBy(GlobalVariables.getUserSession().getPrincipalName());
                editorForm.setHoldingCreatedDate(dateStr);
            boolean hasPermission = canCreateInstance(GlobalVariables.getUserSession().getPrincipalId());
            if (hasPermission) {
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "holdings.record.new.load.message");
            } else {
                editorForm.setHideFooter(false);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_CREATE_INSTANCE);
            }
        }
        if (editorForm.getDocId() != null) {
            OLESerialReceivingServiceImpl oleSerialReceivingService = new OLESerialReceivingServiceImpl();
            Map<String, String> map = new HashMap<>();
            map.put("instanceId", editorForm.getDocId());
            List<OLESerialReceivingDocument> oleSerialReceivingDocuments = (List<OLESerialReceivingDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OLESerialReceivingDocument.class, map);
            for (OLESerialReceivingDocument oleSerialReceivingDocument : oleSerialReceivingDocuments) {
                if (oleSerialReceivingDocument.isActive()) {
                    editorForm.setSerialFlag(true);
                    oleSerialReceivingService.readReceivingRecordType(oleSerialReceivingDocument);
                    oleSerialReceivingService.updateEnumCaptionValues(oleSerialReceivingDocument, null);
                    if (oleSerialReceivingDocument.getOleSerialReceivingHistoryList() != null && oleSerialReceivingDocument.getOleSerialReceivingHistoryList().size() > 0) {
                        for (int serialReceivingHistoryList = 0; serialReceivingHistoryList < oleSerialReceivingDocument.getOleSerialReceivingHistoryList().size(); serialReceivingHistoryList++) {
                            oleSerialReceivingService.setEnumerationAndChronologyValues(oleSerialReceivingDocument.getOleSerialReceivingHistoryList().get(serialReceivingHistoryList));
                        }
                    }
                    if (oleSerialReceivingDocument.getUnboundLocation() != null) {
                        editorForm.setUnboundLocation(oleSerialReceivingDocument.getUnboundLocation());
                    }
                    oleSerialReceivingService.sortById(oleSerialReceivingDocument.getOleSerialReceivingHistoryList());
                    for (OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingDocument.getOleSerialReceivingHistoryList()) {
                        if (oleSerialReceivingHistory.isPublicDisplay()) {
                            if (oleSerialReceivingHistory.getReceivingRecordType().equalsIgnoreCase("Main")) {
                                editorForm.getMainSerialReceivingHistoryList().add(oleSerialReceivingHistory);
                            }
                            if (oleSerialReceivingHistory.getReceivingRecordType().equalsIgnoreCase("Supplementary")) {
                                editorForm.getSupplementSerialReceivingHistoryList().add(oleSerialReceivingHistory);
                            }
                            if (oleSerialReceivingHistory.getReceivingRecordType().equalsIgnoreCase("Index")) {
                                editorForm.getIndexSerialReceivingHistoryList().add(oleSerialReceivingHistory);
                            }
                        }
                    }
                }
            }
        }

        OleHoldings oleHoldings = workInstanceOlemlForm.getSelectedHolding();
        if(oleHoldings!=null && oleHoldings.getCallNumber() !=null){
            if(oleHoldings.getCallNumber().getShelvingScheme() == null) {
                String callNumberDefaultValue = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                        .DESCRIBE_COMPONENT, OLEConstants.HOLDINGS_CALL_NUMBER_TYPE);
                ShelvingScheme shelvingScheme = new ShelvingScheme();
                shelvingScheme.setCodeValue(callNumberDefaultValue);
                oleHoldings.getCallNumber().setShelvingScheme(shelvingScheme);
            }
        }
        workInstanceOlemlForm.setViewId("WorkHoldingsViewPage");

        return workInstanceOlemlForm;
    }

    private void ensureMultipleValuesInOleHoldings(OleHoldings oleHoldings) {
        List<ExtentOfOwnership> extentOfOwnerships = ensureAtleastOneExtentOfOwnership(
                oleHoldings.getExtentOfOwnership());
        oleHoldings.setExtentOfOwnership(extentOfOwnerships);

        List<Note> notes = ensureAtleastOneNote(oleHoldings.getNote());
        oleHoldings.setNote(notes);

        List<Uri> uriList = ensureAtleastOneUri(oleHoldings.getUri());
        oleHoldings.setUri(uriList);
    }

    public List<ExtentOfOwnership> removeDuplicateAndEmptyExtentOfOwnership(OleHoldings holdingData){
        List<ExtentOfOwnership> extentOfOwnershipList = holdingData.getExtentOfOwnership();
        List<ExtentOfOwnership> basic = new ArrayList<>();
        List<ExtentOfOwnership> supplementary = new ArrayList<>();
        List<ExtentOfOwnership> indexes = new ArrayList<>();
    //    Set<String> extentOwnerShipType = new HashSet<>();
        for (ExtentOfOwnership extentOfOwnership1 : extentOfOwnershipList) {
            if(StringUtils.isNotBlank(extentOfOwnership1.getType())){
                if (extentOfOwnership1.getType().equalsIgnoreCase("Basic Bibliographic Unit")) {
                    basic.add(extentOfOwnership1);
                } else if (extentOfOwnership1.getType().equalsIgnoreCase("Supplementary Material")) {
                    supplementary.add(extentOfOwnership1);
                } else if (extentOfOwnership1.getType().equalsIgnoreCase("Indexes")) {
                    indexes.add(extentOfOwnership1);
                }
            }
        }
        extentOfOwnershipList.clear();
        extentOfOwnershipList.addAll(basic);
        extentOfOwnershipList.addAll(supplementary);
        extentOfOwnershipList.addAll(indexes);
        return extentOfOwnershipList;
    }

    @Override
    public EditorForm saveDocument(EditorForm editorForm) {
        WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
        String bibId = editorForm.getBibId();
        List<BibTree> bibTreeList = null;
        BibTree bibTree = null;
        HoldingsTree holdingsTree = new HoldingsTree();
        String editorMessage = "";
        Bib bib = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" HH:mm:ss");
        String dateStr = sdf.format(date);
        String user = GlobalVariables.getUserSession().getPrincipalName();
        try {
            bibTree = docstoreClient.retrieveBibTree(bibId);
        }catch (DocstoreException e) {
            LOG.error("Exception : ", e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
            return workInstanceOlemlForm;
        } catch (Exception e) {
            LOG.error("Exception ", e);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,"docstore.response", e.getMessage() );
        }
        bibTreeList = new ArrayList();
        bibTreeList.add(bibTree);
        workInstanceOlemlForm.setBibTreeList(bibTreeList);
        bib = bibTree.getBib();
        String titleField = bib.getTitle() + " / " + bib.getAuthor() + " / " + DocumentUniqueIDPrefix.getDocumentId(bib.getId());
        editorForm.setTitle(titleField);
        editorForm.setHasLink(true);
//        editorForm.setHeaderText("Instance Editor (Holdings) - OLEML Format");
        editorForm.setHeaderText("Holdings");
        editorForm.setShowEditorFooter(true);
        String docId = editorForm.getDocId();
        if (StringUtils.isNotEmpty(docId)) {
            List<HoldingsTree> holdingsTreeList = new ArrayList<>();
            for(HoldingsTree holdingsTree1 : bibTree.getHoldingsTrees()) {
                if(!holdingsTree1.getHoldings().getId().equals(docId)) {
                    holdingsTreeList.add(holdingsTree1);
                }
            }
            bibTree.getHoldingsTrees().clear();
            bibTree.getHoldingsTrees().addAll(holdingsTreeList);
            Holdings holdings = new PHoldings();
            OleHoldings holdingData = workInstanceOlemlForm.getSelectedHolding();
            List<ExtentOfOwnership> extentOfOwnershipList = removeDuplicateAndEmptyExtentOfOwnership(holdingData);
            holdingData.setExtentOfOwnership(extentOfOwnershipList);
            workInstanceOlemlForm.setSelectedHolding(holdingData);
            if (!isValidHoldingsData(workInstanceOlemlForm)) {
                return workInstanceOlemlForm;
            }
            try {
                String holdingXmlContent = getInstanceEditorFormDataHandler().buildHoldingContent(holdingData);
                holdings.setContent(holdingXmlContent);
            } catch (Exception e) {
                LOG.error("Exception :", e);
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                        "docstore.response", e.getMessage());
                getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                return workInstanceOlemlForm;
            }
            holdings.setId(docId);
            holdings.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
            holdings.setCreatedBy(editorForm.getCreatedBy());
            holdings.setCreatedOn(editorForm.getCreatedDate());
            holdings.setUpdatedBy(user);
            holdings.setUpdatedOn(dateStr);
            String holdingsCreatedDate = null;
            Format formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
            holdingsCreatedDate = formatter.format(new Date());
            holdings.setLastUpdated(holdingsCreatedDate);
            holdings.setBib(bib);
            holdings.setCategory(editorForm.getDocCategory());
            holdings.setType(editorForm.getDocType());
            holdings.setFormat(editorForm.getDocFormat());
            /*holdingsTree.setHoldings(holdings);
            holdingsTree.getItems().add(getItemRecord());*/
            long startTime = System.currentTimeMillis();
            try {
                docstoreClient.updateHoldings(holdings);
            } catch (Exception e) {
                LOG.error("Exception :", e);
                DocstoreException docstoreException = (DocstoreException) e;
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                } else {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                }
                getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                return workInstanceOlemlForm;
            }
            long endTime = System.currentTimeMillis();
            editorForm.setSolrTime(String.valueOf((endTime-startTime)/1000));
            editorForm.setUpdatedDate(holdings.getUpdatedOn());
            editorForm.setUpdatedBy(holdings.getUpdatedBy());
            getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
            editorMessage = "holdings.record.update.message";
            workInstanceOlemlForm.setViewId("WorkHoldingsViewPage");
        } else {

            OleHoldings holdingData = workInstanceOlemlForm.getSelectedHolding();
            List<ExtentOfOwnership> extentOfOwnershipList = removeDuplicateAndEmptyExtentOfOwnership(holdingData);
            holdingData.setExtentOfOwnership(extentOfOwnershipList);
            workInstanceOlemlForm.setSelectedHolding(holdingData);
            if (!isValidHoldingsData(workInstanceOlemlForm)) {
                return workInstanceOlemlForm;
            }
            String content = null;
            try {
                content = getInstanceEditorFormDataHandler().buildHoldingContent(workInstanceOlemlForm.getSelectedHolding());
            } catch (Exception e) {
                LOG.error("Exception :", e);
                e.printStackTrace();
                return workInstanceOlemlForm;
            }

            String staffOnlyFlagForHoldings = String.valueOf(editorForm.isStaffOnlyFlagForHoldings());

//            String content = getInstanceEditorFormDataHandler()
//                    .buildInstanceRecordForDocStore(workInstanceOlemlForm, docId, staffOnlyFlagForHoldings);
            Holdings holdings = new PHoldings();
            holdings.setCategory(DocCategory.WORK.getCode());
            holdings.setType(DocType.HOLDINGS.getCode());
            holdings.setFormat(DocFormat.OLEML.getCode());
            holdings.setCreatedOn(dateStr);
            holdings.setCreatedBy(user);
            holdings.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
            holdings.setContent(content);
            holdings.setBib(bib);

            holdingsTree.setHoldings(holdings);
            holdingsTree.getItems().add(getItemRecord());
            //TODO: set additional attributes
            long startTime = System.currentTimeMillis();
            try {
                docstoreClient.createHoldingsTree(holdingsTree);
                editorForm.setDocId(holdingsTree.getHoldings().getId());
                editorForm.setHoldingCreatedBy(holdingsTree.getHoldings().getCreatedBy());
                editorForm.setHoldingCreatedDate(holdingsTree.getHoldings().getCreatedOn());
                if (workInstanceOlemlForm.getBibTreeList() == null && workInstanceOlemlForm.getBibTreeList().size() == 0) {
                    bibTreeList = new ArrayList<>();
                    bibTree = new BibTree();
                    bibTree.setBib(bib);
                    bibTreeList.add(bibTree);
                    workInstanceOlemlForm.setBibTreeList(bibTreeList);
                } else {
                    bibTreeList = workInstanceOlemlForm.getBibTreeList();
                    bibTree = bibTreeList.get(0);
                }

                List<HoldingsTree> holdingsTreeList = new ArrayList<>();
                holdingsTreeList.add(holdingsTree);
//                bibTree.getHoldingsTrees().addAll(holdingsTreeList);
            }catch (DocstoreException e) {
                LOG.error("Exception : ", e);
                DocstoreException docstoreException = (DocstoreException) e;
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                } else {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                }
                getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                return workInstanceOlemlForm;
            } catch (Exception e) {
                LOG.error("Exception ", e);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,"docstore.response", e.getMessage() );
            }
            long endTime = System.currentTimeMillis();
            editorForm.setSolrTime(String.valueOf((endTime-startTime)/1000));
            getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
            editorMessage =  "record.create.message";
        }


// Separated  ASR code
        if (processAsr(editorForm, workInstanceOlemlForm, bibTree, holdingsTree, editorMessage, bib)){
            return workInstanceOlemlForm;
        }

        return workInstanceOlemlForm;
    }

    private boolean processAsr(EditorForm editorForm, WorkInstanceOlemlForm workInstanceOlemlForm, BibTree bibTree, HoldingsTree holdingsTree, String editorMessage, Bib bib) {
        try {
            if(holdingsTree.getHoldings() == null) {
                holdingsTree = docstoreClient.retrieveHoldingsTree(editorForm.getDocId());
            }
            String holdingLocation = null;
            if (workInstanceOlemlForm.getSelectedHolding() != null && workInstanceOlemlForm.getSelectedHolding().getLocation() != null && workInstanceOlemlForm.getSelectedHolding().getLocation().getLocationLevel() != null) {
                holdingLocation = instanceEditorFormDataHandler.getLocationCode(workInstanceOlemlForm.getSelectedHolding().getLocation().getLocationLevel());
            }
            String callNumber;
            String prefix;
            if(holdingLocation != null){
                if(getAsrHelperService().isAnASRItem(holdingLocation)){
                    List<Item> items = holdingsTree.getItems();
                    List<ASRItem> asrItems = new ArrayList<ASRItem>();
                    ASRItem asrItem = null;
                    if(items != null && items.size()>0){
                        for(Item item : items){
                            org.kuali.ole.docstore.common.document.content.instance.Item itemData = getItemOlemlRecordProcessor().fromXML(item.getContent());
                            asrItem = new ASRItem();
                            if(itemData.getAccessInformation()!=null && itemData.getAccessInformation().getBarcode()!=null){
                                asrItem.setItemBarcode(itemData.getAccessInformation().getBarcode());
                            }

                            Map<String,String> asrItemMap = new HashMap<String,String>();
                            asrItemMap.put("itemBarcode", asrItem.getItemBarcode());
                            List<ASRItem> existingASRItems = (List<ASRItem>)getBusinessObjectService().findMatching(ASRItem.class,asrItemMap);

                            if(existingASRItems.size()==0){
                            if(bib.getTitle()!=null){
                                asrItem.setTitle((bib.getTitle().length()>37)?bib.getTitle().substring(0,36):bib.getTitle());
                            }
                            if(bib.getAuthor()!=null){
                                asrItem.setAuthor((bib.getAuthor().length()>37)?bib.getAuthor().substring(0,36):bib.getAuthor());
                            }
                            if (itemData.getCallNumber() != null && itemData.getCallNumber().getNumber() != null && !itemData.getCallNumber().getNumber().isEmpty()) {
                                    callNumber=(itemData.getCallNumber().getNumber().length() > 37) ? itemData.getCallNumber().getNumber().substring(0, 36) : itemData.getCallNumber().getNumber();
                                    prefix=itemData.getCallNumber().getPrefix()!=null&&!itemData.getCallNumber().getPrefix().isEmpty()?itemData.getCallNumber().getPrefix():"";
                                    asrItem.setCallNumber(prefix+" "+callNumber);
                            } else if(workInstanceOlemlForm.getSelectedHolding() !=null && workInstanceOlemlForm.getSelectedHolding().getCallNumber() !=null &&
                                    workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber() !=null && !workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().isEmpty()){
                                callNumber=(workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().length() > 37) ? workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().substring(0, 36) : workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber();
                                prefix=workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix()!=null&&!workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix().isEmpty()?workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix():"";
                                asrItem.setCallNumber(prefix+" "+callNumber);
                            }

                            asrItems.add(asrItem);
                        }
                        }
                    }
                    getBusinessObjectService().save(asrItems);
                }
            }
            Holdings holdings = holdingsTree.getHoldings();
            bibTree.getHoldingsTrees().add(holdingsTree);
            Collections.sort(bibTree.getHoldingsTrees());
            String docStoreData  = holdings.getContent();
            OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(docStoreData);
            workInstanceOlemlForm.setSelectedHolding(oleHoldings);
            getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
            editorForm.setHoldingLocalIdentifier(DocumentUniqueIDPrefix.getDocumentId(holdings.getId()));
            editorForm.setHoldingUpdatedBy(holdings.getUpdatedBy());
            editorForm.setHoldingCreatedDate(holdings.getCreatedOn());
            editorForm.setHoldingCreatedBy(holdings.getCreatedBy());
            editorForm.setHoldingUpdatedDate(holdings.getUpdatedOn());
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, editorMessage);
            ensureMultipleValuesInOleHoldings(oleHoldings);
        }catch (DocstoreException e) {
            LOG.error("Exception : ", e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
            getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
            return true;
        } catch (Exception e) {
            LOG.error("Exception ", e);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,"docstore.response", e.getMessage() );
        }
        return false;
    }

    private List<Uri> ensureAtleastOneUri(List<Uri> uris) {
        if (uris == null) {
            uris = new ArrayList<Uri>();
        }
        if (uris.size() == 0) {
            Uri uri = new Uri();
            uris.add(uri);
        }
        return uris;
    }

    private List<Note> ensureAtleastOneNote(List<Note> notes) {
        if (notes == null) {
            notes = new ArrayList<Note>();
        }
        if (notes.size() == 0) {
            Note note = new Note();
            notes.add(note);
        }
        return notes;
    }


    private List<ExtentOfOwnership> ensureAtleastOneExtentOfOwnership(List<ExtentOfOwnership> extentOfOwnerships) {
        if (extentOfOwnerships == null) {
            extentOfOwnerships = new ArrayList<ExtentOfOwnership>();
        }
        if (extentOfOwnerships.size() == 0) {
            ExtentOfOwnership extentOfOwnership = new ExtentOfOwnership();
            extentOfOwnership.getNote().add(new Note());
            extentOfOwnerships.add(0, extentOfOwnership);
        }
        return extentOfOwnerships;
    }

    /**
     * Gets the InstanceEditorFormDataHandler attribute.
     *
     * @return Returns InstanceEditorFormDataHandler.
     */
    private InstanceEditorFormDataHandler getInstanceEditorFormDataHandler() {
        if (null == instanceEditorFormDataHandler) {
            instanceEditorFormDataHandler = new InstanceEditorFormDataHandler();
        }
        return instanceEditorFormDataHandler;
    }

    private boolean canCreateInstance(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.INSTANCE_EDITOR_ADD_INSTANCE);
    }

    private boolean canEditInstance(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.INSTANCE_EDITOR_EDIT_INSTANCE);
    }
    /**
     * Validates the Holdings data and returns true if it is valid
     *
     * @param workInstanceOlemlForm
     * @return
     */
    private boolean isValidHoldingsData(WorkInstanceOlemlForm workInstanceOlemlForm) {
        OleHoldings oleHoldings = workInstanceOlemlForm.getSelectedHolding();
        String location = null;
        if (oleHoldings != null && oleHoldings.getLocation() != null && oleHoldings.getLocation().getLocationLevel() != null) {
            location = oleHoldings.getLocation().getLocationLevel().getName();
        }
        if (location != null && location.length() != 0 && !isValidLocation(location)) {
            //workInstanceOlemlForm.setMessage("<font size='3' color='red'>Please enter valid location.</font>");
            GlobalVariables.getMessageMap().putError("documentForm.selectedHolding.location.locationLevel.name", "error.location");
            workInstanceOlemlForm.setValidInput(false);
            return false;
        }
        return true;
    }

    private boolean isValidLocation(String location) {

        List<String> locationList = LocationValuesBuilder.retrieveLocationDetailsForSuggest(location);
        if (locationList != null && locationList.size() > 0) {
            for (String locationValue : locationList) {
                if (locationValue.equalsIgnoreCase(location)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public EditorForm addORRemoveExtentOfOwnership(EditorForm editorForm, HttpServletRequest request) {
        String methodName = request.getParameter("methodToCall");
        if (methodName.equalsIgnoreCase("addExtentOfOwnership")) {
            WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            index++;
            List<ExtentOfOwnership> extentOfOwnershipForUI = workInstanceOlemlForm.getSelectedHolding().getExtentOfOwnership();
            ExtentOfOwnership extentOfOwnership = new ExtentOfOwnership();
            extentOfOwnership.getNote().add(new Note());
           // Set<String> extentOwnerShipType = new HashSet<>();
           /* for (ExtentOfOwnership extentOfOwnership1 : extentOfOwnershipForUI) {
                if (!extentOwnerShipType.add(extentOfOwnership1.getType())) {
                    GlobalVariables.getMessageMap().putErrorForSectionId("ExtentOfOwnershipRepeatableSections", OLEConstants.EXTENTOFOWNERSHIP_ALREADY_EXISTS, extentOfOwnership1.getType());
                    return editorForm;
                }
            }*/
            extentOfOwnershipForUI.add(index, extentOfOwnership);
            /*List<ExtentOfOwnership> basic = new ArrayList<>();
            List<ExtentOfOwnership> supplementary = new ArrayList<>();
            List<ExtentOfOwnership> indexes = new ArrayList<>();
            extentOfOwnership = new ExtentOfOwnership();
            for (ExtentOfOwnership extentOfOwnership1 : extentOfOwnershipForUI) {
                if (StringUtils.isNotBlank(extentOfOwnership1.getType())) {
                    if (extentOfOwnership1.getType().equalsIgnoreCase("Basic Bibliographic Unit")) {
                        basic.add(extentOfOwnership1);
                    } else if (extentOfOwnership1.getType().equalsIgnoreCase("Supplementary Material")) {
                        supplementary.add(extentOfOwnership1);
                    } else if (extentOfOwnership1.getType().equalsIgnoreCase("Indexes")) {
                        indexes.add(extentOfOwnership1);
                    }
                } else {
                    extentOfOwnership = extentOfOwnership1;
                }
            }
            extentOfOwnershipForUI.clear();
            extentOfOwnershipForUI.add(extentOfOwnership);
            extentOfOwnershipForUI.addAll(basic);
            extentOfOwnershipForUI.addAll(supplementary);
            extentOfOwnershipForUI.addAll(indexes);*/
            editorForm.setDocumentForm(workInstanceOlemlForm);
        } else if (methodName.equalsIgnoreCase("removeExtentOfOwnership")) {
            WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            List<ExtentOfOwnership> extentOfOwnershipForUI = workInstanceOlemlForm.getSelectedHolding().getExtentOfOwnership();
            if (extentOfOwnershipForUI.size() > 1) {
                extentOfOwnershipForUI.remove(index);
            } else {
                if (extentOfOwnershipForUI.size() == 1) {
                    extentOfOwnershipForUI.remove(index);
                    ExtentOfOwnership extentOfOwnership = new ExtentOfOwnership();
                    extentOfOwnershipForUI.add(extentOfOwnership);
                    extentOfOwnership.getNote().add(new Note());
                }
            }
            editorForm.setDocumentForm(workInstanceOlemlForm);
        } else if (methodName.equalsIgnoreCase("addEOWHoldingNotes")) {
            WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            String selectedPath = editorForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
            int selectedExtentIndex = Integer.parseInt(StringUtils.substring(selectedPath,
                    (StringUtils.indexOf(selectedPath, "[") + 1),
                    StringUtils.lastIndexOf(selectedPath, "]")));
            index++;
            List<Note> holdingsNote = workInstanceOlemlForm.getSelectedHolding().getExtentOfOwnership()
                    .get(selectedExtentIndex).getNote();
            holdingsNote.add(index, new Note());
            editorForm.setDocumentForm(workInstanceOlemlForm);
        } else if (methodName.equalsIgnoreCase("removeEOWHoldingNotes")) {
            WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            String selectedPath = editorForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
            int selectedExtentIndex = Integer.parseInt(StringUtils.substring(selectedPath,
                    (StringUtils.indexOf(selectedPath, "[") + 1),
                    StringUtils.lastIndexOf(selectedPath, "]")));
            List<Note> holdingsNote = workInstanceOlemlForm.getSelectedHolding().getExtentOfOwnership()
                    .get(selectedExtentIndex).getNote();
            if (holdingsNote.size() > 1) {
                holdingsNote.remove(index);
            } else {
                if (holdingsNote.size() == 1) {
                    holdingsNote.remove(index);
                    Note note = new Note();
                    holdingsNote.add(note);
                }
            }
            editorForm.setDocumentForm(workInstanceOlemlForm);
        }
        return editorForm;
    }

    @Override
    public EditorForm addORRemoveAccessInformationAndHoldingsNotes(EditorForm editorForm, HttpServletRequest request) {
        String methodName = request.getParameter("methodToCall");
        if (methodName.equalsIgnoreCase("addAccessInformation")) {
            WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            index++;
            List<Uri> accessInformation = workInstanceOlemlForm.getSelectedHolding().getUri();
            accessInformation.add(index, new Uri());
            editorForm.setDocumentForm(workInstanceOlemlForm);
        } else if (methodName.equalsIgnoreCase("removeAccessInformation")) {
            WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            List<Uri> accessInformation = workInstanceOlemlForm.getSelectedHolding().getUri();
            if (accessInformation.size() > 1) {
                accessInformation.remove(index);
            } else {
                if (accessInformation.size() == 1) {
                    accessInformation.remove(index);
                    Uri uri = new Uri();
                    accessInformation.add(uri);
                }
            }
            editorForm.setDocumentForm(workInstanceOlemlForm);
        } else if (methodName.equalsIgnoreCase("addHoldingNotes")) {
            WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            index++;
            List<Note> holdingsNote = workInstanceOlemlForm.getSelectedHolding().getNote();
            holdingsNote.add(index, new Note());
            editorForm.setDocumentForm(workInstanceOlemlForm);
        } else if (methodName.equalsIgnoreCase("removeHoldingNotes")) {
            WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            List<Note> holdingsNote = workInstanceOlemlForm.getSelectedHolding().getNote();
            if (holdingsNote.size() > 1) {
                holdingsNote.remove(index);
            } else {
                if (holdingsNote.size() == 1) {
                    holdingsNote.remove(index);
                    Note note = new Note();
                    holdingsNote.add(note);
                }
            }
            editorForm.setDocumentForm(workInstanceOlemlForm);
        }
        return editorForm;
    }

    private boolean isValidBib(String bibData) {
        if (bibData.contains("collection") || bibData.contains("OAI-PMH")) {
            return true;
        }
        return false;
    }

    @Override
    public EditorForm createNewRecord(EditorForm editorForm, BibTree bibTree) {

        if (editorForm.getDocumentForm().getViewId().equalsIgnoreCase("WorkHoldingsViewPage")) {
            editorForm.setNeedToCreateInstance(true);
        }
        editNewRecord(editorForm, bibTree);
        return editorForm.getDocumentForm();
    }


    @Override
    public EditorForm editNewRecord(EditorForm editorForm, BibTree bibTree) {
        WorkInstanceOlemlForm workInstanceOlemlForm = new WorkInstanceOlemlForm();
        if ((editorForm.getDocumentForm() instanceof WorkInstanceOlemlForm)) {
            workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
            workInstanceOlemlForm.setViewId(editorForm.getDocumentForm().getViewId());
        }

        workInstanceOlemlForm.setDocCategory("work");
        workInstanceOlemlForm.setDocType("holdings");
        workInstanceOlemlForm.setDocFormat("oleml");

        if (bibTree != null && bibTree.getHoldingsTrees() != null) {
            HoldingsTree holdingsTree = bibTree.getHoldingsTrees().get(0);
            HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
            if (editorForm.getDocumentForm().getViewId().equalsIgnoreCase("WorkHoldingsViewPage")) {
//                editorForm.setHeaderText("Import Bib Step-4 Instance Editor (Item)- OLEML Format");
                editorForm.setHeaderText("Import Bib Step-4 Item");
                // validate user entered holding data before going to item tab
                if (!isValidHoldingsData(workInstanceOlemlForm)) {
                    return workInstanceOlemlForm;
                }
                holdingsTree.getHoldings().setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
                holdingsTree.getHoldings().setCreatedBy(GlobalVariables.getUserSession().getPrincipalName());
                org.kuali.ole.docstore.common.document.content.instance.Item item = itemOlemlRecordProcessor.fromXML(holdingsTree.getItems().get(0).getContent());
                List<Note> notes = ensureAtleastOneNote(item.getNote());
                item.setNote(notes);
                workInstanceOlemlForm.setSelectedItem(item);
                workInstanceOlemlForm.setViewId("WorkItemViewPage");
                //workInstanceOlemlForm.setMessage("Please enter details for new Item record.");
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO,
                        "item.details.new.message");
            } else if (editorForm.getDocumentForm().getViewId().equalsIgnoreCase("WorkBibEditorViewPage")) {
//                editorForm.setHeaderText("Import Bib Step-4 Instance Editor (Holdings)- OLEML Format");
                editorForm.setHeaderText("Import Bib Step-4 Holdings");
                workInstanceOlemlForm.setViewId("WorkHoldingsViewPage");
                //workInstanceOlemlForm.setMessage("Please enter details for new Holdings record.");
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO,
                        "holdings.record.new.load.message");
                workInstanceOlemlForm
                        .setSelectedHolding(oleHoldings);
            }
        }
        editorForm.setDocumentForm(workInstanceOlemlForm);
        return editorForm;
    }

    public EditorForm deleteVerify(EditorForm editorForm) {
        //LOG.info("in instance editor class");
        WorkInstanceOlemlForm workInstanceOlemlForm = new WorkInstanceOlemlForm();
        String docId = editorForm.getDocId();
        String operation = "deleteVerify";
        //        String responseXml = getResponseFromDocStore(editorForm, docId, operation);
        //        LOG.info("deleteVerify responseXml-->" + responseXml);
        //        editorForm.setDeleteVerifyResponse(responseXml);
        editorForm.setShowDeleteTree(true);
        editorForm.setHasLink(true);
        //        Node<DocumentTreeNode, String> docTree = buildDocSelectionTree(responseXml);
        List<String> uuidList = new ArrayList<>(0);
        uuidList.add(editorForm.getDocId());
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        Node<DocumentTreeNode, String> docTree = null;
        try {
            docTree = documentSelectionTree.add(uuidList, editorForm.getDocType());
        } catch (SolrServerException e) {
            LOG.error("Exception :", e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        StringBuffer deleteMessage = new StringBuffer();
        deleteMessage.append("WARNING : All attached items (");
        HoldingsTree holdingsTree = docstoreClient.retrieveHoldingsTree(docId);
        int itemCount = holdingsTree.getItems().size();
        deleteMessage.append(itemCount + ") on the following holdings record will be deleted.");
        editorForm.setDeleteMessage(deleteMessage.toString());
        editorForm.getDocTree().setRootElement(docTree);
        editorForm.setViewId("DeleteViewPage");
        return editorForm;

    }

    /**
     * This method deletes the holdings record from docstore by the doc id.
     * @param editorForm
     * @return
     * @throws Exception
     */
    public EditorForm delete(EditorForm editorForm) throws Exception {
        return deleteFromDocStore(editorForm);
    }

    @Override
    public EditorForm showBibs(EditorForm editorForm) {
        String instanceId = "";
        String docType = editorForm.getDocType();
        List<Bib> bibs = new ArrayList<Bib>();
        if ((docType.equalsIgnoreCase(DocType.HOLDINGS.getCode())) || (docType
                .equalsIgnoreCase(DocType.ITEM.getCode()))) {
            instanceId = editorForm.getHoldingsId();
        }
        if (StringUtils.isNotEmpty(instanceId)) {
            try {
                Holdings holdings = docstoreClient.retrieveHoldings(instanceId);
                bibs = holdings.getBibs().getBibs();
            } catch (Exception e) {
                LOG.error("Exception :", e);
                e.printStackTrace();
            }
        }
        editorForm.setBibList(bibs);
        editorForm.setViewId("ShowBibViewPage");
        return editorForm;
    }


    public EditorForm bulkUpdate(EditorForm editorForm,List<String> holdingIds) {
        WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
        String bibId = editorForm.getBibId();
        List<BibTree> bibTreeList = new ArrayList<BibTree>();
        BibTree bibTree = null;
        String editorMessage = "";
        Bib bib = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" HH:mm:ss");
        String dateStr = sdf.format(date);
        String user = GlobalVariables.getUserSession().getPrincipalName();
        workInstanceOlemlForm.setBibTreeList(bibTreeList);
        editorForm.setHeaderText("Global Instance Editor");
        String docId = editorForm.getDocId();
            Holdings holdings = new PHoldings();
            OleHoldings holdingData = workInstanceOlemlForm.getSelectedHolding();
            if (!isValidHoldingsData(workInstanceOlemlForm)) {
                return workInstanceOlemlForm;
            }
            try {
                String holdingXmlContent = getInstanceEditorFormDataHandler().buildHoldingContent(holdingData);
                holdings.setCategory(holdingXmlContent);
                holdings.setContent(holdingXmlContent);
            } catch (Exception e) {
                LOG.error("Exception :", e);
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                        "docstore.response", e.getMessage());
                getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                return workInstanceOlemlForm;
            }
            holdings.setId(docId);
            String canUpdateStaffOnlyFlag = "false";
            if (editorForm.isStaffOnlyFlagInGlobalEdit()) {
                canUpdateStaffOnlyFlag = "true";
                editorForm.setStaffOnlyFlagForHoldings(true);
                holdings.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
            }
            else if (!editorForm.isStaffOnlyFlagInGlobalEdit()) {
                canUpdateStaffOnlyFlag = "true";
                editorForm.setStaffOnlyFlagForHoldings(false);
                holdings.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
            }
            holdings.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
            holdings.setCreatedBy(editorForm.getCreatedBy());
            holdings.setCreatedOn(editorForm.getCreatedDate());
            holdings.setUpdatedBy(user);
            holdings.setUpdatedOn(dateStr);
            String holdingsCreatedDate = null;
            Format formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
            holdingsCreatedDate = formatter.format(new Date());
            holdings.setLastUpdated(holdingsCreatedDate);
            //holdings.setBib(bib);
            holdings.setCategory(editorForm.getDocCategory());
            holdings.setType(editorForm.getDocType());
            holdings.setFormat(editorForm.getDocFormat());
            try {
                getDocstoreClientLocator().getDocstoreClient().bulkUpdateHoldings(holdings, holdingIds,canUpdateStaffOnlyFlag);
            }
            catch (DocstoreException e) {
                LOG.error("Exception :", e);
                DocstoreException docstoreException = (DocstoreException) e;
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                } else {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                }
                // getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                return workInstanceOlemlForm;
            }
            catch(Exception ne){
                LOG.error("Exception :", ne);
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "record.submit.fail.message");
                return workInstanceOlemlForm;
            }
            editorForm.setUpdatedDate(holdings.getUpdatedOn());
            editorForm.setUpdatedBy(holdings.getUpdatedBy());
            getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
            editorMessage = "holdings.record.update.message";
            workInstanceOlemlForm.setViewId("WorkHoldingsViewPage");
        //}

        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, editorMessage);
        return workInstanceOlemlForm;

    }

    @Override
    public EditorForm copy(EditorForm editorForm) {
        editorForm.setDocId(null);
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, OLEConstants.MARC_EDITOR_HOLDINGS_COPY_MESSAGE);
        return editorForm;
    }

}
