package org.kuali.ole.describe.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.ASRItem;
import org.kuali.ole.deliver.bo.OLEReturnHistoryRecord;
import org.kuali.ole.deliver.bo.OleCirculationHistory;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.describe.bo.DocumentSelectionTree;
import org.kuali.ole.describe.bo.DocumentTreeNode;
import org.kuali.ole.describe.bo.InstanceEditorFormDataHandler;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.describe.form.EditorForm;
import org.kuali.ole.describe.form.WorkInstanceOlemlForm;
import org.kuali.ole.describe.keyvalue.LocationValuesBuilder;
import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.Constants;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 1/9/14
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkItemOlemlEditor extends AbstractEditor {

    private static final Logger LOG = Logger.getLogger(WorkItemOlemlEditor.class);
    HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
    ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
    ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();
    BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();

    private static WorkItemOlemlEditor workItemOlemlEditor = null;
    private InstanceEditorFormDataHandler instanceEditorFormDataHandler = null;
    private OleLoanDocumentDaoOjb loanDaoOjb;

    DocstoreClient docstoreClient = getDocstoreLocalClient();

    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;

    private OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }

    public static WorkItemOlemlEditor getInstance() {
        if (workItemOlemlEditor == null) {
            workItemOlemlEditor = new WorkItemOlemlEditor();
        }
        return workItemOlemlEditor;
    }

    private WorkItemOlemlEditor() {

    }

    @Override
    public EditorForm loadDocument(EditorForm editorForm) {
        WorkInstanceOlemlForm workInstanceOlemlForm = new WorkInstanceOlemlForm();
        String directory = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(org.kuali.ole.sys.OLEConstants.EXTERNALIZABLE_HELP_URL_KEY);
        editorForm.setExternalHelpUrl(directory+"/reference/webhelp/OLE/content/ch04s01.html#_Item");
        editorForm.setHeaderText("Item");
        editorForm.setItem(true);
        editorForm.setHasLink(true);
        String parameter = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                .DESCRIBE_COMPONENT, OLEConstants.ITEM_LOCATION_DISCLOSURE);
        editorForm.setOpenLocation(Boolean.valueOf(parameter));
        String shelvingOrder = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                .DESCRIBE_COMPONENT, OLEConstants.ITEM_SUPRESS_SHELVINGORDER);
        if(Boolean.valueOf(shelvingOrder)){
            editorForm.setSupressItemShelving(false);
        }else{
            editorForm.setSupressItemShelving(true);
        }
        String bibId = editorForm.getBibId();
        String holdingsId = editorForm.getInstanceId();
        String docId = editorForm.getDocId();
        OleHoldings oleHoldings = new OleHoldings();
        String docStoreData = null;
        List<BibTree> bibTreeList = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" HH:mm:ss");
        String dateStr = sdf.format(date);
        BibTree bibTree = null;
        try {
            bibTree = docstoreClient.retrieveBibTree(bibId);
            bibTreeList.add(bibTree);
            workInstanceOlemlForm.setBibTreeList(bibTreeList);
            if (!org.kuali.ole.docstore.model.enums.DocFormat.DUBLIN_UNQUALIFIED.getCode().equals(bibTree.getBib().getFormat())) {
                String titleField = bibTree.getBib().getTitle() + " / " + bibTree.getBib().getAuthor() + " / " + DocumentUniqueIDPrefix.getDocumentId(bibTree.getBib().getId());
                editorForm.setTitle(titleField);
            }
            Holdings holdings = docstoreClient.retrieveHoldings(holdingsId);
            oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
            workInstanceOlemlForm.setSelectedHolding(oleHoldings);
        } catch (DocstoreException e) {
            LOG.error(e);
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

        try {
            if (StringUtils.isNotEmpty(docId)) {
                editorForm.setItemLocalIdentifier(DocumentUniqueIDPrefix.getDocumentId(docId));
                org.kuali.ole.docstore.common.document.Item itemDocument = docstoreClient.retrieveItem(editorForm.getDocId());
                docStoreData = itemDocument.getContent();
                Item item = itemOlemlRecordProcessor.fromXML(docStoreData);
                ensureAccessInformation(item);
                //TODO : set additional attributes
                String itemStatus = null;
                if (item.getItemStatus() != null && item.getItemStatus().getCodeValue() != null) {
                    itemStatus = item.getItemStatus().getCodeValue();
                    workInstanceOlemlForm.setOldItemStatus(itemStatus);
                }

                String itemProperty = getInstanceEditorFormDataHandler().getParameter("OLE-DESC", "Describe", "ITEM_STATUS_READONLY");
                String[] itemArray = itemProperty.split(",");
                for (String status : itemArray) {
                    if (status.equalsIgnoreCase(itemStatus)) {
                        workInstanceOlemlForm.setItemStatusNonEditable(true);
                        break;
                    }
                    workInstanceOlemlForm.setItemStatusNonEditable(false);
                }

                itemProperty = getInstanceEditorFormDataHandler().getParameter("OLE-DESC", "Describe", "ITEM_BARCODE_READONLY");
                String[] itemArrayBarcode = itemProperty.split(",");
                for (String status : itemArrayBarcode) {
                    if (status.equalsIgnoreCase(itemStatus)) {
                        workInstanceOlemlForm.setItemBarcodeNonEditable(true);
                        break;
                    }
                    workInstanceOlemlForm.setItemBarcodeNonEditable(false);
                }

                itemProperty = getInstanceEditorFormDataHandler().getParameter("OLE-DESC", "Describe", "EDIT_HOLDINGS_INFO_IN_ITEM_SCREEN");
                boolean editable;
                if (itemProperty != null) {
                    editable = Boolean.valueOf(itemProperty);
                    workInstanceOlemlForm.setHoldingsDataInItemReadOnly(editable);
                }
                editorForm.setStaffOnlyFlagForItem(itemDocument.isStaffOnly());
                editorForm.setItemCreatedDate(itemDocument.getCreatedOn());
                editorForm.setItemCreatedBy(itemDocument.getCreatedBy());
                editorForm.setItemUpdatedDate(itemDocument.getUpdatedOn());
                editorForm.setItemUpdatedBy(itemDocument.getUpdatedBy());

                List<Note> notes = ensureAtleastOneNote(item.getNote());
                item.setNote(notes);
                List<DonorInfo> donorInfos = ensureAtleastOneDonor(item.getDonorInfo());
                for (DonorInfo donorInformation : donorInfos) {
                    if (null != donorInformation.getDonorNote()) {
                        String modifiedValue = donorInformation.getDonorNote().replaceAll("\"","&quot;");
                        donorInformation.setDonorNote(modifiedValue);
                    }
                    if (null != donorInformation.getDonorPublicDisplay()) {
                        String modifiedValue = donorInformation.getDonorPublicDisplay().replaceAll("\"","&quot;");
                        donorInformation.setDonorPublicDisplay(modifiedValue);
                    }
                }
                item.setDonorInfo(donorInfos);
                ensureAccessInformation(item);
                workInstanceOlemlForm.setSelectedItem(item);
                getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                workInstanceOlemlForm.setViewId("WorkItemViewPage");
                if (item.getAccessInformation()!=null && StringUtils.isNotEmpty(item.getAccessInformation().getBarcode())) {
                    Map map = new HashMap();
                    map.put("itemBarcode", item.getAccessInformation().getBarcode());
                    List<OLEReturnHistoryRecord> oleReturnHistoryRecords = (List<OLEReturnHistoryRecord>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEReturnHistoryRecord.class, map);
                    if (CollectionUtils.isNotEmpty(oleReturnHistoryRecords)) {
                        editorForm.setOleReturnHistoryRecords(oleReturnHistoryRecords);
                    }
                }
                if (editorForm.getEditable().equalsIgnoreCase("false")) {
                    GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "item.record.load.message");
                } else {
                    boolean hasPermission = canEditItem(GlobalVariables.getUserSession().getPrincipalId());
                    if (hasPermission) {
                        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "item.record.load.message");
                    } else {
                        editorForm.setHideFooter(false);
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_EDIT_ITEM);
                    }
                }
            } else {

                Item item = new Item();
                String itemProperty = getInstanceEditorFormDataHandler().getParameter("OLE-DESC", "Describe", "EDIT_HOLDINGS_INFO_IN_ITEM_SCREEN");
                String[] itemArray = null;
                if (itemProperty != null) {
                    itemArray = itemProperty.split(",");
                }
                if (itemArray != null) {
                    for (String status : itemArray) {
                        if (status.equalsIgnoreCase("TRUE")) {
                            workInstanceOlemlForm.setHoldingsDataInItemReadOnly(true);
                        } else if (status.equalsIgnoreCase("FALSE")) {
                            workInstanceOlemlForm.setHoldingsDataInItemReadOnly(false);
                        }
                    }
                }
                List<Note> notes = ensureAtleastOneNote(item.getNote());
                item.setNote(notes);
                List<DonorInfo> donorInfos = ensureAtleastOneDonor(item.getDonorInfo());
                item.setDonorInfo(donorInfos);
                ensureAccessInformation(item);
                workInstanceOlemlForm.setSelectedItem(item);
                String itemTypeCode = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                        .DESCRIBE_COMPONENT, OLEConstants.DEFAULT_ITEM_TYPE_CODE);
                if(StringUtils.isNotBlank(itemTypeCode)){
                    Map map = new HashMap();
                    map.put("instanceItemTypeCode", itemTypeCode);
                    List<OleInstanceItemType> oleInstanceItemTypeList = (List<OleInstanceItemType>) KRADServiceLocator.getBusinessObjectService().findMatching(OleInstanceItemType.class, map);
                    if (oleInstanceItemTypeList.size() > 0) {
                        ItemType type = new ItemType();
                        OleInstanceItemType oleInstanceItemType = oleInstanceItemTypeList.get(0);
                        type.setCodeValue(oleInstanceItemType.getInstanceItemTypeCode());
                        type.setFullValue(oleInstanceItemType.getInstanceItemTypeName());
                        workInstanceOlemlForm.getSelectedItem().setItemType(type);
                    }
                }
                this.addItemInformation(editorForm);
                setStaffOnly(editorForm);
                editorForm.setItemCreatedBy(GlobalVariables.getUserSession().getPrincipalName());
                editorForm.setItemCreatedDate(dateStr);
                editorForm.setItemUpdatedBy(null);
                editorForm.setItemUpdatedDate(null);
                workInstanceOlemlForm.setViewId("WorkItemViewPage");
                boolean hasPermission = canCreateItem(GlobalVariables.getUserSession().getPrincipalId());
                if (hasPermission) {
                    GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO,
                            "item.details.new.message");
                } else {
                    editorForm.setHideFooter(false);
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_CREATE_ITEM);
                }
                getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
            }
        } catch (DocstoreException e) {
            LOG.error(e);
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
        Item oleItem = workInstanceOlemlForm.getSelectedItem();
        if (oleItem != null) {
            if (oleItem.getCallNumber() == null) {
                CallNumber callNumber = new CallNumber();
                String callNumberDefaultValue = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                        .DESCRIBE_COMPONENT, OLEConstants.ITEM_CALL_NUMBER_TYPE);
                ShelvingScheme shelvingScheme = new ShelvingScheme();
                shelvingScheme.setCodeValue(callNumberDefaultValue);
                callNumber.setShelvingScheme(shelvingScheme);
                oleItem.setCallNumber(callNumber);
            } else {
                if (oleItem.getCallNumber().getShelvingScheme() == null) {
                    ShelvingScheme shelvingScheme = new ShelvingScheme();
                    String callNumberDefaultValue = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                            .DESCRIBE_COMPONENT, OLEConstants.ITEM_CALL_NUMBER_TYPE);
                    shelvingScheme.setCodeValue(callNumberDefaultValue);
                    oleItem.getCallNumber().setShelvingScheme(shelvingScheme);
                }
            }
            OleCirculationHistory oleCirculationHistory = getLoanDaoOjb().retrieveCircHistoryRecord(oleItem.getItemIdentifier());
            if(null != oleCirculationHistory) {
                oleItem.setLastBorrower(oleCirculationHistory.getPatronId());
                oleItem.setLastCheckinDate(new Timestamp(oleCirculationHistory.getCheckInDate().getTime()));
            }
        }
        return workInstanceOlemlForm;
    }

    private void ensureAccessInformation(Item item) {
        if(item.getAccessInformation() == null) {
            AccessInformation accessInformation = new AccessInformation();
            Uri uri = new Uri();
            uri.setValue("");
            uri.setResolvable("");
            accessInformation.setUri(uri);
            item.setAccessInformation(accessInformation);
        }
    }

    @Override
    public EditorForm saveDocument(EditorForm editorForm) {
        WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
        Item itemData = workInstanceOlemlForm.getSelectedItem();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        String dateStr = sdf.format(date);
        String user = GlobalVariables.getUserSession().getLoggedInUserPrincipalName();
        String docId = editorForm.getDocId();
        String instanceId = editorForm.getInstanceId();
        String editorMessage = "";
        String callNumber;
        String prefix;
        Bib bib = null;
        int noOfPieces = 0;
        boolean isExceedMaxCount = false;
        boolean isNoOfPiecesCount = false;
//        editorForm.setHeaderText("Instance Editor (Item)- OLEML Format");
        editorForm.setHeaderText("Item");
        editorForm.setItem(true);
        try {
            bib = docstoreClient.retrieveBib(editorForm.getBibId());
            if (!org.kuali.ole.docstore.model.enums.DocFormat.DUBLIN_UNQUALIFIED.getCode().equals(bib.getFormat())) {
                String titleField = bib.getTitle() + " / " + bib.getAuthor() + " / " + DocumentUniqueIDPrefix.getDocumentId(bib.getId());
                editorForm.setTitle(titleField);
            }
        } catch (DocstoreException e) {
            LOG.error(e);
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
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "docstore.response", e.getMessage());
        }

        try {
            if (StringUtils.isNotEmpty(docId)) {
                //Item itemData = workInstanceOlemlForm.getSelectedItem();
                if (editorForm.getItemStatusSelection().equals("true")) {
                    Format formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    itemData.setItemStatusEffectiveDate(formatter.format(new Date()));
                }

                if (!itemData.isClaimsReturnedFlag()) {
                    itemData.setClaimsReturnedNote(null);
                    itemData.setClaimsReturnedFlagCreateDate(null);
                } else {
                    addClaimsReturnedItemHistory(itemData, user);
                    getOleDeliverRequestDocumentHelperService().cancelPendingRequestForClaimsReturnedItem(itemData.getItemIdentifier());
                }

                if (itemData.isItemDamagedStatus()) {
                    addItemDamagedHistory(itemData, user);
                }
                List<DonorInfo> donorInfos = itemData.getDonorInfo();
                if(donorInfos.size() > 0) {
                for (DonorInfo donorInformation : donorInfos) {
                    if (null != donorInformation.getDonorNote()) {
                        String modifiedValue = donorInformation.getDonorNote().replaceAll("\"","&quot;");
                        donorInformation.setDonorNote(modifiedValue);
                    }
                    if (null != donorInformation.getDonorPublicDisplay()) {
                        String modifiedValue = donorInformation.getDonorPublicDisplay().replaceAll("\"","&quot;");
                        donorInformation.setDonorPublicDisplay(modifiedValue);
                    }
                }
                itemData.setDonorInfo(donorInfos);
                }
                try {
                    org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemData.getItemIdentifier());
                    ItemOlemlRecordProcessor itemOlemlRecordProcessor1 = new ItemOlemlRecordProcessor();
                    org.kuali.ole.docstore.common.document.content.instance.Item oleItem = itemOlemlRecordProcessor1.fromXML(item.getContent());
                    String numberOfPiecesString = ((WorkInstanceOlemlForm) editorForm.getDocumentForm()).getSelectedItem().getNumberOfPieces();
                    if(itemData.isMissingPieceFlag()){
                        if(org.apache.commons.lang.StringUtils.isNotBlank(numberOfPiecesString)){
                            if(Integer.parseInt(numberOfPiecesString) >= 1){
                                noOfPieces = Integer.parseInt(numberOfPiecesString);
                            }else{
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, DocstoreResources.INVALID_NO_OF_PIECE);
                                return workInstanceOlemlForm;
                            }

                        }else{
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, DocstoreResources.INVALID_NO_OF_PIECE);
                            return workInstanceOlemlForm;
                        }
                    }

                    MissingPieceItemRecord missingPieceItemRecord = new MissingPieceItemRecord();
                    List<MissingPieceItemRecord> missingPieceItemRecordList = new ArrayList<>();
                    DateFormat parsedDate = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE);

                    if (itemData.isMissingPieceFlag() && !oleItem.isMissingPieceFlag()) {
                        missingPieceItemRecord.setMissingPieceFlagNote(itemData.getMissingPieceFlagNote());
                        missingPieceItemRecord.setMissingPieceCount(itemData.getMissingPiecesCount());
                        missingPieceItemRecord.setMissingPieceDate(parsedDate.format(getDateTimeService().getCurrentDate()));
                        missingPieceItemRecord.setOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                        missingPieceItemRecord.setPatronBarcode(null);
                        missingPieceItemRecord.setPatronId(itemData.getCurrentBorrower());
                        missingPieceItemRecord.setItemId(itemData.getItemIdentifier());
                       if(org.apache.commons.lang.StringUtils.isNotBlank(missingPieceItemRecord.getMissingPieceCount()) && (Integer.parseInt(missingPieceItemRecord.getMissingPieceCount()) >= 1)){
                        if (noOfPieces >= Integer.parseInt(missingPieceItemRecord.getMissingPieceCount())) {
                            if (!missingPieceItemRecord.getMissingPieceCount().isEmpty() ) {
                                if (itemData.getMissingPieceItemRecordList() != null && itemData.getMissingPieceItemRecordList().size() > 0) {
                                    itemData.getMissingPieceItemRecordList().add(missingPieceItemRecord);
                                } else {
                                    missingPieceItemRecordList.add(missingPieceItemRecord);
                                    itemData.setMissingPieceItemRecordList(missingPieceItemRecordList);
                                }
                            }
                        } else {
                            isExceedMaxCount = true;
                        }
                       }   else{
                           GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, DocstoreResources.INVALID_MISSING_PIECE_COUNT);
                           return workInstanceOlemlForm;
                       }


                    } else {
                        if (itemData.isMissingPieceFlag() && oleItem.isMissingPieceFlag()) {
                            Map<String, String> map = new HashMap<>();
                            map.put("itemId", DocumentUniqueIDPrefix.getDocumentId(itemData.getItemIdentifier()));
                            List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> missingPieceItemRecordList1 =
                                    (List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord>) KRADServiceLocator.getBusinessObjectService()
                                    .findMatchingOrderBy(org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord.class, map, "missingPieceItemId", true);
                            List<MissingPieceItemRecord> missingPieceItemRecords = new ArrayList<>();
                            for (int index = 0; index < missingPieceItemRecordList1.size(); index++) {
                                MissingPieceItemRecord missingPieceItemRecord1 = new MissingPieceItemRecord();
                                if (index == missingPieceItemRecordList1.size() - 1) {
                                    DateFormat dfs = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE);
                                    String missingPieceItemDate = dfs.format(getDateTimeService().getCurrentDate());
                                    missingPieceItemRecord1.setMissingPieceDate(missingPieceItemDate);
                                    if(org.apache.commons.lang.StringUtils.isNotBlank(itemData.getMissingPiecesCount()) && (Integer.parseInt(itemData.getMissingPiecesCount()) >= 1 )){
                                    if (noOfPieces > Integer.parseInt(itemData.getMissingPiecesCount()) ) {

                                        missingPieceItemRecord1.setMissingPieceCount(itemData.getMissingPiecesCount());
                                    } else {
                                        missingPieceItemRecord1.setMissingPieceCount(missingPieceItemRecordList1.get(index).getMissingPieceCount());
                                        isExceedMaxCount = true;
                                    }
                                    }else{
                                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, DocstoreResources.INVALID_MISSING_PIECE_COUNT);
                                        return workInstanceOlemlForm;
                                    }
                                    missingPieceItemRecord1.setPatronBarcode(null);
                                    missingPieceItemRecord1.setPatronId(itemData.getCurrentBorrower());
                                    missingPieceItemRecord1.setOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                                    missingPieceItemRecord1.setItemId(itemData.getItemIdentifier());
                                    missingPieceItemRecord1.setMissingPieceFlagNote(itemData.getMissingPieceFlagNote());
                                        missingPieceItemRecords.add(missingPieceItemRecord1);

                                } else {
                                    if (missingPieceItemRecordList1.get(index).getMissingPieceDate() != null && !missingPieceItemRecordList1.get(index).getMissingPieceDate().toString().isEmpty()) {
                                        SimpleDateFormat dateToSimpleDateFormat = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE);
                                        Date missingPieceItemDate = null;
                                        if(null != missingPieceItemRecordList1.get(index).getMissingPieceDate()){
                                            try {
                                                missingPieceItemDate = new Date(missingPieceItemRecordList1.get(index).getMissingPieceDate().getTime());
                                            } catch (org.kuali.ole.sys.exception.ParseException e) {
                                                LOG.error("format string to Date " + e);
                                            }
                                        }
                                        missingPieceItemRecord1.setMissingPieceDate(dateToSimpleDateFormat.format(missingPieceItemDate));
                                    }
                                    missingPieceItemRecord1.setMissingPieceFlagNote(missingPieceItemRecordList1.get(index).getMissingPieceFlagNote());


                                    missingPieceItemRecord1.setOperatorId(missingPieceItemRecordList1.get(index).getOperatorId());
                                    missingPieceItemRecord1.setPatronBarcode(missingPieceItemRecordList1.get(index).getPatronBarcode());
                                    missingPieceItemRecord1.setPatronId(missingPieceItemRecordList1.get(index).getPatronId());
                                    missingPieceItemRecord1.setItemId(missingPieceItemRecordList1.get(index).getItemId());
                                    missingPieceItemRecord1.setMissingPieceCount(missingPieceItemRecordList1.get(index).getMissingPieceCount());
                                    if(org.apache.commons.lang.StringUtils.isNotBlank(missingPieceItemRecordList1.get(index).getMissingPieceCount()) && (Integer.parseInt(missingPieceItemRecordList1.get(index).getMissingPieceCount()) >= 1 )){
                                    if (noOfPieces >= Integer.parseInt(missingPieceItemRecordList1.get(index).getMissingPieceCount())) {

                                            missingPieceItemRecords.add(missingPieceItemRecord1);

                                    } else {
                                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,OLEConstants.EXCEED_MAX_COUNT);
                                        return workInstanceOlemlForm;
                                    }
                                    }  else{
                                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, DocstoreResources.INVALID_MISSING_PIECE_COUNT);
                                        return workInstanceOlemlForm;
                                    }

                                }
                            }
                            itemData.setMissingPieceItemRecordList(missingPieceItemRecords);
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Exception ", e);
                }
                if (!isExceedMaxCount) {
                    workInstanceOlemlForm.setViewId("WorkItemViewPage");
                    if (!isValidItemData(workInstanceOlemlForm)) {
//                    getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                        return workInstanceOlemlForm;
                    }

                    String itemId = editorForm.getDocId();
                    itemData.setItemIdentifier(itemId);
                    String itemXmlContent = getInstanceEditorFormDataHandler().buildItemContent(itemData);

                    ItemOleml itemOleml = new ItemOleml();
                    itemOleml.setContent(itemXmlContent);
                    itemOleml.setId(itemId);
                    itemOleml.setType(editorForm.getDocType());
                    itemOleml.setFormat(editorForm.getDocFormat());
                    itemOleml.setUpdatedBy(user);
                    itemOleml.setUpdatedOn(dateStr);
                    itemOleml.setStaffOnly(editorForm.isStaffOnlyFlagForItem());
                    itemOleml.setCategory(editorForm.getDocCategory());
                    long startTime = System.currentTimeMillis();
                    try {
                        docstoreClient.updateItem(itemOleml);
                    } catch (DocstoreException e) {
                        LOG.error(e);
                        DocstoreException docstoreException = (DocstoreException) e;
                        if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                        } else {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                        }

                        Holdings holdings = null;
                        try {
                            holdings = docstoreClient.retrieveHoldings(editorForm.getInstanceId());
                        } catch (Exception e1) {
                            LOG.error("Exception :", e1);
                            docstoreException = (DocstoreException) e1;
                            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                            } else {
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                            }
                        }
                        String docStoreData = holdings.getContent();
                        OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(docStoreData);
                        workInstanceOlemlForm.setSelectedHolding(oleHoldings);

                        getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                        return workInstanceOlemlForm;
                    } catch (Exception e) {
                        LOG.error("Exception ", e);
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "docstore.response", e.getMessage());
                    }
                    long endTime = System.currentTimeMillis();
                    editorForm.setSolrTime(String.valueOf((endTime - startTime) / 1000));
                    if (itemData.getLocation() != null) {
                        String location = instanceEditorFormDataHandler.getLocationCode(itemData.getLocation().getLocationLevel());
                        if (asrHelperService.isAnASRItem(location)) {

                            Map<String, String> asrItemMap = new HashMap<String, String>();
                            asrItemMap.put("itemBarcode", itemData.getAccessInformation().getBarcode());
                            List<ASRItem> asrItems = (List<ASRItem>) businessObjectService.findMatching(ASRItem.class, asrItemMap);
                            if (asrItems.size() == 0) {
                                ASRItem asrItem = new ASRItem();
                                if (itemData.getAccessInformation() != null && itemData.getAccessInformation().getBarcode() != null) {
                                    asrItem.setItemBarcode(itemData.getAccessInformation().getBarcode());
                                }
                                if (bib.getTitle() != null) {
                                    asrItem.setTitle((bib.getTitle().length() > 37) ? bib.getTitle().substring(0, 36) : bib.getTitle());
                                }
                                if (bib.getAuthor() != null) {
                                    asrItem.setAuthor((bib.getAuthor().length() > 37) ? bib.getAuthor().substring(0, 36) : bib.getAuthor());
                                }
                                if (itemData.getCallNumber() != null && itemData.getCallNumber().getNumber() != null && !itemData.getCallNumber().getNumber().isEmpty()) {
                                    callNumber = (itemData.getCallNumber().getNumber().length() > 37) ? itemData.getCallNumber().getNumber().substring(0, 36) : itemData.getCallNumber().getNumber();
                                    prefix = itemData.getCallNumber().getPrefix() != null && !itemData.getCallNumber().getPrefix().isEmpty() ? itemData.getCallNumber().getPrefix() : "";
                                    asrItem.setCallNumber(prefix + " " + callNumber);
                                } else if (workInstanceOlemlForm.getSelectedHolding() != null && workInstanceOlemlForm.getSelectedHolding().getCallNumber() != null && workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber() != null && !workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().isEmpty()) {
                                    callNumber = (workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().length() > 37) ? workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().substring(0, 36) : workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber();
                                    prefix = workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix() != null && !workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix().isEmpty() ? workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix() : "";
                                    asrItem.setCallNumber(prefix + " " + callNumber);
                                }
                                businessObjectService.save(asrItem);
                            }
                        }
                    } else if (workInstanceOlemlForm.getSelectedHolding().getLocation() != null) {
                        OleHoldings oleHoldings = workInstanceOlemlForm.getSelectedHolding();
                        String location = oleHoldings.getLocation().getLocationLevel().getName();
                        if (asrHelperService.isAnASRItem(location)) {
                            Map<String, String> asrItemMap = new HashMap<String, String>();
                            asrItemMap.put("itemBarcode", itemData.getAccessInformation().getBarcode());
                            List<ASRItem> asrItems = (List<ASRItem>) businessObjectService.findMatching(ASRItem.class, asrItemMap);
                            if (asrItems.size() == 0) {
                                ASRItem asrItem = new ASRItem();
                                if (itemData.getAccessInformation() != null && itemData.getAccessInformation().getBarcode() != null) {
                                    asrItem.setItemBarcode(itemData.getAccessInformation().getBarcode());
                                }
                                if (bib.getTitle() != null) {
                                    asrItem.setTitle((bib.getTitle().length() > 37) ? bib.getTitle().substring(0, 36) : bib.getTitle());
                                }
                                if (bib.getAuthor() != null) {
                                    asrItem.setAuthor((bib.getAuthor().length() > 37) ? bib.getAuthor().substring(0, 36) : bib.getAuthor());
                                }
                                if (itemData.getCallNumber() != null && itemData.getCallNumber().getNumber() != null && !itemData.getCallNumber().getNumber().isEmpty()) {
                                    callNumber = (itemData.getCallNumber().getNumber().length() > 37) ? itemData.getCallNumber().getNumber().substring(0, 36) : itemData.getCallNumber().getNumber();
                                    prefix = itemData.getCallNumber().getPrefix() != null && !itemData.getCallNumber().getPrefix().isEmpty() ? itemData.getCallNumber().getPrefix() : "";
                                    asrItem.setCallNumber(prefix + " " + callNumber);
                                } else if (workInstanceOlemlForm.getSelectedHolding() != null && workInstanceOlemlForm.getSelectedHolding().getCallNumber() != null && workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber() != null && !workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().isEmpty()) {
                                    callNumber = (workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().length() > 37) ? workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().substring(0, 36) : workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber();
                                    prefix = workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix() != null && !workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix().isEmpty() ? workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix() : "";
                                    asrItem.setCallNumber(prefix + " " + callNumber);

                                }
                                businessObjectService.save(asrItem);
                            }
                        }
                    }
                    String itemProperty = getInstanceEditorFormDataHandler().getParameter("OLE-DESC", "Describe", "EDIT_HOLDINGS_INFO_IN_ITEM_SCREEN");
                    String[] itemArray = null;
                    if (itemProperty != null) {
                        itemArray = itemProperty.split(",");
                    }
                    for (String status : itemArray) {
                        if (status.equalsIgnoreCase("TRUE")) {
                            workInstanceOlemlForm.setHoldingsDataInItemReadOnly(true);
                        } else if (status.equalsIgnoreCase("FALSE")) {
                            workInstanceOlemlForm.setHoldingsDataInItemReadOnly(false);
                        }
                    }
                    String holdingId = workInstanceOlemlForm.getSelectedHolding().getHoldingsIdentifier();
                    OleHoldings holdingData = workInstanceOlemlForm.getSelectedHolding();
                    String holdingXmlContent = getInstanceEditorFormDataHandler().buildHoldingContent(holdingData);
                    Holdings holdings = docstoreClient.retrieveHoldings(holdingId);
                    holdings.setBib(bib);
                    holdings.setContent(holdingXmlContent);
                    holdings.setCategory(editorForm.getDocCategory());
                    holdings.setType(DocType.HOLDINGS.getCode());
                    holdings.setFormat(editorForm.getDocFormat());
                    docstoreClient.updateHoldings(holdings);
                    getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                    if (!isValidHoldingsData(workInstanceOlemlForm)) {
                        getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                        return workInstanceOlemlForm;
                    }
                    editorMessage = "item.record.update.message";
                } else {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,OLEConstants.EXCEED_MAX_COUNT);
                    //editorMessage = ;
                    return workInstanceOlemlForm;
                }

            } else {

                if (!isValidItemData(workInstanceOlemlForm)) {
//                    getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                    return workInstanceOlemlForm;
                }
                String staffOnlyFlagForItem = String.valueOf(editorForm.isStaffOnlyFlagForItem());
                Item oleItem = workInstanceOlemlForm.getSelectedItem();
                String itemXmlContent = getInstanceEditorFormDataHandler().buildItemContent(oleItem, staffOnlyFlagForItem);
                ItemOleml itemDoc = new ItemOleml();
                itemDoc.setCategory(DocCategory.WORK.getCode());
                itemDoc.setType(DocType.ITEM.getCode());
                itemDoc.setFormat(DocFormat.OLEML.getCode());
                itemDoc.setCreatedOn(dateStr);
                itemDoc.setCreatedBy(user);
                itemDoc.setStaffOnly(editorForm.isStaffOnlyFlagForItem());
                itemDoc.setContent(itemXmlContent);
                Holdings holdings = new PHoldings();
                holdings.setId(editorForm.getInstanceId());
                itemDoc.setHolding(holdings);
                long startTime = System.currentTimeMillis();
                try {
                    docstoreClient.createItem(itemDoc);
                } catch (Exception e) {
                    LOG.error("Exception :", e);
                    DocstoreException docstoreException = (DocstoreException) e;
                    String errorCode = docstoreException.getErrorCode();
                    if (StringUtils.isNotEmpty(errorCode)) {
                        Map<String, String> paramsMap = docstoreException.getErrorParams();
                        if (paramsMap != null && paramsMap.size() > 0 && paramsMap.containsKey("barcode")) {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, errorCode, paramsMap.get("barcode"));
                        } else {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, errorCode);
                        }
                    } else {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                    }

                    try {
                        holdings = docstoreClient.retrieveHoldings(editorForm.getInstanceId());
                    } catch (Exception e1) {
                        LOG.error("Exception :", e1);
                        docstoreException = (DocstoreException) e1;
                        if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                        } else {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                        }
                    }
                    String docStoreData = holdings.getContent();
                    OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(docStoreData);
                    workInstanceOlemlForm.setSelectedHolding(oleHoldings);

                    getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                    return workInstanceOlemlForm;
                }
                long endtime = System.currentTimeMillis();
                editorForm.setSolrTime(String.valueOf((endtime - startTime) / 1000));
                if (oleItem.getLocation() != null) {
                    String location = oleItem.getLocation().getLocationLevel().getName();
                    if (asrHelperService.isAnASRItem(location)) {
                        Map<String, String> asrItemMap = new HashMap<String, String>();
                        asrItemMap.put("itemBarcode", oleItem.getAccessInformation().getBarcode());
                        List<ASRItem> asrItems = (List<ASRItem>) businessObjectService.findMatching(ASRItem.class, asrItemMap);
                        if (asrItems.size() == 0) {
                            ASRItem asrItem = new ASRItem();
                            if (oleItem.getAccessInformation() != null && oleItem.getAccessInformation().getBarcode() != null) {
                                asrItem.setItemBarcode(oleItem.getAccessInformation().getBarcode());
                            }
                            if (bib.getTitle() != null) {
                                asrItem.setTitle((bib.getTitle().length() > 37) ? bib.getTitle().substring(0, 36) : bib.getTitle());
                            }
                            if (bib.getAuthor() != null) {
                                asrItem.setAuthor((bib.getAuthor().length() > 37) ? bib.getAuthor().substring(0, 36) : bib.getAuthor());
                            }
                            if (oleItem.getCallNumber() != null && oleItem.getCallNumber().getNumber() != null && !oleItem.getCallNumber().getNumber().isEmpty()) {
                                callNumber = (oleItem.getCallNumber().getNumber().length() > 37) ? oleItem.getCallNumber().getNumber().substring(0, 36) : oleItem.getCallNumber().getNumber();
                                prefix = oleItem.getCallNumber().getPrefix() != null && !oleItem.getCallNumber().getPrefix().isEmpty() ? oleItem.getCallNumber().getPrefix() : "";
                                asrItem.setCallNumber(prefix + " " + callNumber);
                            } else if (workInstanceOlemlForm.getSelectedHolding() != null && workInstanceOlemlForm.getSelectedHolding().getCallNumber() != null && workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber() != null && !workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().isEmpty()) {
                                callNumber = (workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().length() > 37) ? workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().substring(0, 36) : workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber();
                                prefix = workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix() != null && !workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix().isEmpty() ? workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix() : "";
                                asrItem.setCallNumber(prefix + " " + callNumber);
                            }
                            businessObjectService.save(asrItem);
                        }
                    }
                } else if (workInstanceOlemlForm.getSelectedHolding().getLocation() != null) {
                    OleHoldings oleHoldings = workInstanceOlemlForm.getSelectedHolding();
                    String location = oleHoldings.getLocation().getLocationLevel().getName();
                    if (asrHelperService.isAnASRItem(location)) {
                        Map<String, String> asrItemMap = new HashMap<String, String>();
                        asrItemMap.put("itemBarcode", oleItem.getAccessInformation().getBarcode());
                        List<ASRItem> asrItems = (List<ASRItem>) businessObjectService.findMatching(ASRItem.class, asrItemMap);
                        if (asrItems.size() == 0) {
                            ASRItem asrItem = new ASRItem();
                            if (oleItem.getAccessInformation() != null && oleItem.getAccessInformation().getBarcode() != null) {
                                asrItem.setItemBarcode(oleItem.getAccessInformation().getBarcode());
                            }
                            if (bib.getTitle() != null) {
                                asrItem.setTitle((bib.getTitle().length() > 37) ? bib.getTitle().substring(0, 36) : bib.getTitle());
                            }
                            if (bib.getAuthor() != null) {
                                asrItem.setAuthor((bib.getAuthor().length() > 37) ? bib.getAuthor().substring(0, 36) : bib.getAuthor());
                            }
                            if (oleItem.getCallNumber() != null && oleItem.getCallNumber().getNumber() != null && !oleItem.getCallNumber().getNumber().isEmpty()) {
                                callNumber = (oleItem.getCallNumber().getNumber().length() > 37) ? oleItem.getCallNumber().getNumber().substring(0, 36) : oleItem.getCallNumber().getNumber();
                                prefix = oleItem.getCallNumber().getPrefix() != null && !oleItem.getCallNumber().getPrefix().isEmpty() ? oleItem.getCallNumber().getPrefix() : "";
                                asrItem.setCallNumber(prefix + " " + callNumber);
                            } else if (workInstanceOlemlForm.getSelectedHolding() != null && workInstanceOlemlForm.getSelectedHolding().getCallNumber() != null && workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber() != null && !workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().isEmpty()) {
                                callNumber = (workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().length() > 37) ? workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber().substring(0, 36) : workInstanceOlemlForm.getSelectedHolding().getCallNumber().getNumber();
                                prefix = workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix() != null && !workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix().isEmpty() ? workInstanceOlemlForm.getSelectedHolding().getCallNumber().getPrefix() : "";
                                asrItem.setCallNumber(prefix + " " + callNumber);

                            }
                            businessObjectService.save(asrItem);
                        }
                    }
                }
                editorForm.setDocId(itemDoc.getId());
                getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                editorMessage = "item.record.load.message";
            }
        } catch (Exception e) {
            LOG.error("Exception :", e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
            getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
            return workInstanceOlemlForm;
        }
        org.kuali.ole.docstore.common.document.Item itemDocument = null;
        try {
            Holdings holdings = null;
            try {
                holdings = docstoreClient.retrieveHoldings(editorForm.getInstanceId());
            } catch (Exception e) {
                LOG.error("Exception :", e);
                DocstoreException docstoreException = (DocstoreException) e;
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                } else {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                }
            }
            String docStoreData = holdings.getContent();
            OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(docStoreData);
            workInstanceOlemlForm.setSelectedHolding(oleHoldings);
            try {
                itemDocument = docstoreClient.retrieveItem(editorForm.getDocId());
            } catch (DocstoreException e) {
                LOG.error(e);
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
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "docstore.response", e.getMessage());
            }
            Item item = itemOlemlRecordProcessor.fromXML(itemDocument.getContent());
            editorForm.setItemLocalIdentifier(DocumentUniqueIDPrefix.getDocumentId(item.getItemIdentifier()));
            editorForm.setItemCreatedBy(itemDocument.getCreatedBy());
            editorForm.setItemCreatedDate(itemDocument.getCreatedOn());
            editorForm.setItemUpdatedBy(itemDocument.getUpdatedBy());
            editorForm.setItemUpdatedDate(itemDocument.getUpdatedOn());
            workInstanceOlemlForm.setSelectedItem(item);
            getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, editorMessage);
        } catch (Exception e) {
            LOG.error("Exception :", e);
            GlobalVariables.getMessageMap().clearErrorMessages();
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "record.submit.fail.message");
        }
        if (StringUtils.isNotBlank(workInstanceOlemlForm.getSelectedItem().getPurchaseOrderLineItemIdentifier())) {
            Map poMap = new HashMap();
            poMap.put(OLEConstants.PURAP_DOC_IDENTIFIER, workInstanceOlemlForm.getSelectedItem().getPurchaseOrderLineItemIdentifier());
            poMap.put("PO_CUR_IND", "Y" );
            OlePurchaseOrderDocument olePurchaseOrderDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderDocument.class, poMap);
            if (olePurchaseOrderDocument != null) {
                String poId = olePurchaseOrderDocument.getDocumentNumber();
                Map<String, String> map = new HashMap<>();
                map.put(org.kuali.ole.sys.OLEConstants.OleCopy.PO_DOC_NUM, poId);
                KRADServiceLocator.getBusinessObjectService().deleteMatching(OLELinkPurapDonor.class, map);
                if (workInstanceOlemlForm.getSelectedItem().getDonorInfo().size() > 0) {
                    for (DonorInfo donorInfo : workInstanceOlemlForm.getSelectedItem().getDonorInfo()) {
                        if (donorInfo.getDonorCode() != null && !donorInfo.getDonorCode().isEmpty()) {
                            map.clear();
                            map.put(OLEConstants.DONOR_CODE, donorInfo.getDonorCode());
                            OLELinkPurapDonor newOleLinkPurapDonor = new OLELinkPurapDonor();
                            List<OLEDonor> oleDonors = (List<OLEDonor>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEDonor.class, map);
                            map.clear();
                            if (StringUtils.isNotBlank(workInstanceOlemlForm.getSelectedItem().getItemIdentifier())) {
                                map.put(OLEConstants.OleDeliverRequest.ITEM_UUID, workInstanceOlemlForm.getSelectedItem().getItemIdentifier());
                            }
                            map.put(org.kuali.ole.sys.OLEConstants.OleCopy.PO_DOC_NUM, poId);
                            List<OleCopy> oleCopy = (List<OleCopy>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCopy.class, map);
                            if (oleCopy != null && oleCopy.size() > 0) {
                                newOleLinkPurapDonor.setCorrectionItemId(oleCopy.get(0).getCorrectionItemId());
                                newOleLinkPurapDonor.setReqItemId(oleCopy.get(0).getReqItemId());
                                newOleLinkPurapDonor.setPoDocNum(oleCopy.get(0).getPoDocNum());
                                newOleLinkPurapDonor.setReceivingItemId(oleCopy.get(0).getReceivingItemId());
                                newOleLinkPurapDonor.setDonorId(oleDonors.get(0).getDonorId());
                                newOleLinkPurapDonor.setDonorCode(donorInfo.getDonorCode());
                                newOleLinkPurapDonor.setPoItemId(oleCopy.get(0).getPoItemId());
                                KRADServiceLocator.getBusinessObjectService().save(newOleLinkPurapDonor);
                            }
                            map.clear();
                        }
                    }
                }
                map.clear();
                map.put(OLEConstants.OleDeliverRequest.ITEM_UUID, itemData.getItemIdentifier());
                map.put(org.kuali.ole.sys.OLEConstants.OleCopy.PO_DOC_NUM, poId);
                List<OleCopy> oleCopy = (List<OleCopy>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCopy.class, map);
                if (CollectionUtils.isNotEmpty(oleCopy)) {
                    oleCopy.get(0).setEnumeration(itemData.getEnumeration());
                    oleCopy.get(0).setCopyNumber(itemData.getCopyNumber());
                    KRADServiceLocator.getBusinessObjectService().save(oleCopy);
                }
            }
        }
        // To remove existing item from bib tree
        removeDocumentFromTree(editorForm);
        // To add new item or updated item in bib tree
        addItemToBibTree(workInstanceOlemlForm.getBibTreeList(), itemDocument);
        return workInstanceOlemlForm;
    }

    private void addItemDamagedHistory(Item itemData, String user) throws Exception {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemData.getItemIdentifier());
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        Item oleItem = itemOlemlRecordProcessor.fromXML(item.getContent());
        ItemDamagedRecord itemDamagedRecord = new ItemDamagedRecord();
        List<ItemDamagedRecord> itemDamagedRecords = new ArrayList<>();
        if(!oleItem.isItemDamagedStatus()){
            itemDamagedRecord.setDamagedItemNote(itemData.getDamagedItemNote());
            itemDamagedRecord.setDamagedItemDate(df.format(getDateTimeService().getCurrentDate()));
            itemDamagedRecord.setPatronBarcode(null);
            itemDamagedRecord.setDamagedPatronId(itemData.getCurrentBorrower());
            itemDamagedRecord.setOperatorId(user);
            itemDamagedRecord.setItemId(itemData.getItemIdentifier());
            if(itemData.getItemDamagedRecords() != null && itemData.getItemDamagedRecords().size() > 0){
                itemData.getItemDamagedRecords().add(itemDamagedRecord);
            } else {
                itemDamagedRecords.add(itemDamagedRecord);
                itemData.setItemDamagedRecords(itemDamagedRecords);
            }
        } else {
            Map<String,String> map = new HashMap<>();
            map.put("itemId", DocumentUniqueIDPrefix.getDocumentId(itemData.getItemIdentifier()));
            List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord> itemDamagedRecordList = (List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord.class, map, "itemDamagedId", true);
            List<ItemDamagedRecord> damagedRecordList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(itemDamagedRecordList)){
                for(int index=0 ; index < itemDamagedRecordList.size() ; index++){
                    ItemDamagedRecord damagedRecord = new ItemDamagedRecord();
                    if(index == itemDamagedRecordList.size()-1){
                        damagedRecord.setDamagedItemNote(itemData.getDamagedItemNote());
                        damagedRecord.setDamagedItemDate(df.format(getDateTimeService().getCurrentDate()));
                        damagedRecord.setPatronBarcode(null);
                        damagedRecord.setDamagedPatronId(itemData.getCurrentBorrower());
                        damagedRecord.setOperatorId(user);
                        damagedRecord.setItemId(itemData.getItemIdentifier());
                    } else {
                        if (itemDamagedRecordList.get(index).getDamagedItemDate() != null && !itemDamagedRecordList.get(index).getDamagedItemDate().toString().isEmpty()) {
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date itemDamagedDate = null;
                            try {
                                itemDamagedDate = format2.parse(itemDamagedRecordList.get(index).getDamagedItemDate().toString());
                            } catch (ParseException e) {
                                LOG.error("format string to Date " + e);
                            }
                            damagedRecord.setDamagedItemDate(df.format(itemDamagedDate).toString());
                        }
                        damagedRecord.setDamagedItemNote(itemDamagedRecordList.get(index).getDamagedItemNote());
                        damagedRecord.setPatronBarcode(itemDamagedRecordList.get(index).getPatronBarcode());
                        damagedRecord.setDamagedPatronId(itemDamagedRecordList.get(index).getDamagedPatronId());
                        damagedRecord.setOperatorId(itemDamagedRecordList.get(index).getOperatorId());
                        damagedRecord.setItemId(itemDamagedRecordList.get(index).getItemId());
                    }
                    damagedRecordList.add(damagedRecord);
                }
                itemData.setItemDamagedRecords(damagedRecordList);
            }
        }
    }

    private void addClaimsReturnedItemHistory(Item itemData, String user) throws Exception {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemData.getItemIdentifier());
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        Item oleItem = itemOlemlRecordProcessor.fromXML(item.getContent());
        ItemClaimsReturnedRecord itemClaimsReturnedRecord = new ItemClaimsReturnedRecord();
        List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords = new ArrayList<>();
        if(!oleItem.isClaimsReturnedFlag()){
            itemClaimsReturnedRecord.setClaimsReturnedNote(itemData.getClaimsReturnedNote());
            if (itemData.getClaimsReturnedFlagCreateDate() != null && !itemData.getClaimsReturnedFlagCreateDate().isEmpty()) {
                itemClaimsReturnedRecord.setClaimsReturnedFlagCreateDate(itemData.getClaimsReturnedFlagCreateDate());
            }
            else{
                itemClaimsReturnedRecord.setClaimsReturnedFlagCreateDate(df.format(getDateTimeService().getCurrentDate()));
            }
            itemClaimsReturnedRecord.setClaimsReturnedOperatorId(user);
            itemClaimsReturnedRecord.setItemId(itemData.getItemIdentifier());
            itemClaimsReturnedRecord.setClaimsReturnedPatronBarcode(null);
            itemClaimsReturnedRecord.setClaimsReturnedPatronId(itemData.getCurrentBorrower());
            if(itemData.getItemClaimsReturnedRecords() != null && itemData.getItemClaimsReturnedRecords().size() > 0){
                itemData.getItemClaimsReturnedRecords().add(itemClaimsReturnedRecord);
            }
            else{
                itemClaimsReturnedRecords.add(itemClaimsReturnedRecord);
                itemData.setItemClaimsReturnedRecords(itemClaimsReturnedRecords);
            }
        } else{
            Map<String,String> map = new HashMap<>();
            map.put("itemId", DocumentUniqueIDPrefix.getDocumentId(itemData.getItemIdentifier()));
            List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord> claimsReturnedRecordList = (List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord.class, map, "claimsReturnedId", true);
            List<ItemClaimsReturnedRecord> itemClaimsReturnedRecordList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(claimsReturnedRecordList)){
                for(int index=0 ; index < claimsReturnedRecordList.size() ; index++){
                    ItemClaimsReturnedRecord claimsReturnedRecord = new ItemClaimsReturnedRecord();
                    if(index == claimsReturnedRecordList.size()-1){
                        if (itemData.getClaimsReturnedFlagCreateDate() != null && !itemData.getClaimsReturnedFlagCreateDate().isEmpty()) {
                            claimsReturnedRecord.setClaimsReturnedFlagCreateDate(itemData.getClaimsReturnedFlagCreateDate());
                        }
                        else{
                            claimsReturnedRecord.setClaimsReturnedFlagCreateDate(df.format(getDateTimeService().getCurrentDate()));
                        }
                        claimsReturnedRecord.setClaimsReturnedNote(itemData.getClaimsReturnedNote());
                        claimsReturnedRecord.setClaimsReturnedPatronBarcode(null);
                        claimsReturnedRecord.setClaimsReturnedPatronId(itemData.getCurrentBorrower());
                        claimsReturnedRecord.setClaimsReturnedOperatorId(user);
                        claimsReturnedRecord.setItemId(itemData.getItemIdentifier());
                    } else {
                        if (claimsReturnedRecordList.get(index).getClaimsReturnedFlagCreateDate().toString() != null) {
                            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date claimsReturnedDate = null;
                            try {
                                claimsReturnedDate = format2.parse(claimsReturnedRecordList.get(index).getClaimsReturnedFlagCreateDate().toString());
                            } catch (ParseException e) {
                                LOG.error("format string to Date " + e);
                            }
                            claimsReturnedRecord.setClaimsReturnedFlagCreateDate(format1.format(claimsReturnedDate).toString());
                        }
                        claimsReturnedRecord.setClaimsReturnedNote(claimsReturnedRecordList.get(index).getClaimsReturnedNote());
                        claimsReturnedRecord.setClaimsReturnedPatronBarcode(claimsReturnedRecordList.get(index).getClaimsReturnedPatronBarcode());
                        claimsReturnedRecord.setClaimsReturnedPatronId(claimsReturnedRecordList.get(index).getClaimsReturnedPatronId());
                        claimsReturnedRecord.setClaimsReturnedOperatorId(claimsReturnedRecordList.get(index).getClaimsReturnedOperatorId());
                        claimsReturnedRecord.setItemId(claimsReturnedRecordList.get(index).getItemId());
                    }
                    itemClaimsReturnedRecordList.add(claimsReturnedRecord);
                }
                itemData.setItemClaimsReturnedRecords(itemClaimsReturnedRecordList);
            }
        }
    }

    /**
     * This methods adds the new item or updated item to the bib tree to build left pane tree.
     * @param bibTreeList
     * @param selectedItemDocument
     */
    private void addItemToBibTree(List<BibTree> bibTreeList, org.kuali.ole.docstore.common.document.Item selectedItemDocument) {
        if (CollectionUtils.isNotEmpty(bibTreeList)) {
            for (BibTree bibTree : bibTreeList) {
                if (CollectionUtils.isNotEmpty(bibTree.getHoldingsTrees())) {
                    for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                        if (null != holdingsTree.getHoldings() && null != selectedItemDocument.getHolding()) {
                            String holdingsId = holdingsTree.getHoldings().getId();
                            String selectedItemHoldingsId = selectedItemDocument.getHolding().getId();
                            if (null != holdingsId && null != selectedItemHoldingsId) {
                                if (holdingsId.equals(selectedItemHoldingsId)) {
                                    holdingsTree.setHoldings(selectedItemDocument.getHolding());
                                    holdingsTree.getItems().add(selectedItemDocument);
                                }
                            }
                        }
                    }
                }
            }
        }
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
        workInstanceOlemlForm.setDocType("item");
        workInstanceOlemlForm.setDocFormat("oleml");

        if (bibTree != null && bibTree.getHoldingsTrees() != null) {
            HoldingsTree holdingsTree = bibTree.getHoldingsTrees().get(0);
            if (editorForm.getDocumentForm().getViewId().equalsIgnoreCase("WorkHoldingsViewPage")) {
//                editorForm.setHeaderText("Import Bib Step-4 Instance Editor (Item)- OLEML Format");
                editorForm.setHeaderText("Import Bib Step-4 Item");
                // validate user entered holding data before going to item tab
                if (!isValidHoldingsData(workInstanceOlemlForm)) {
                    return workInstanceOlemlForm;
                }
                holdingsTree.getHoldings().setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
                holdingsTree.getHoldings().setCreatedBy(GlobalVariables.getUserSession().getPrincipalName());
                Item item = itemOlemlRecordProcessor.fromXML(holdingsTree.getItems().get(0).getContent());
                List<Note> notes = ensureAtleastOneNote(item.getNote());
                item.setNote(notes);
                workInstanceOlemlForm.setSelectedItem(item);
                workInstanceOlemlForm.setViewId("WorkItemViewPage");
                //workInstanceOlemlForm.setMessage("Please enter details for new Item record.");
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO,
                        "item.details.new.message");
            } else if (editorForm.getDocumentForm().getViewId().equalsIgnoreCase("WorkItemViewPage")) {
                if (!isValidItemData(workInstanceOlemlForm)) {
                    return workInstanceOlemlForm;
                }
                Item item = workInstanceOlemlForm.getSelectedItem();
                Map<String, String> mapObject = new HashMap<String, String>();
                String staffOnlyFlagForItem = String.valueOf(editorForm.isStaffOnlyFlagForItem());
                try {
                    String holdingXmlContent = getInstanceEditorFormDataHandler()
                            .buildHoldingContent(workInstanceOlemlForm.getSelectedHolding());
                    holdingsTree.getHoldings().setContent(holdingXmlContent);

                    String itemXmlContent = getInstanceEditorFormDataHandler().buildItemContent(item, staffOnlyFlagForItem);
                    holdingsTree.getItems().get(0).setContent(itemXmlContent);

                } catch (Exception e) {
                    LOG.error("Exception :", e);
                    StringBuffer instanceEditorErrorMessage = new StringBuffer(
                            OLEConstants.INSTANCE_EDITOR_FAILURE).append(" :: \n" + e.getMessage());
                    workInstanceOlemlForm.setMessage(instanceEditorErrorMessage.toString());
                    LOG.error(instanceEditorErrorMessage);
                }
            }
        }
        editorForm.setDocumentForm(workInstanceOlemlForm);
        return editorForm;
    }

    @Override
    public String saveDocument(BibTree bibTree, EditorForm editorForm) {
        String responseFromDocstore = "success";
        WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
        if (bibTree != null) {
            try {
                docstoreClient.createBibTree(bibTree);
            } catch (DocstoreException e) {
                LOG.error(e);
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
        }
        editorForm.setDocumentForm(workInstanceOlemlForm);
        return responseFromDocstore;
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

    private List<DonorInfo> ensureAtleastOneDonor(List<DonorInfo> donorInfos) {
        if (donorInfos == null) {
            donorInfos = new ArrayList<DonorInfo>();
        }
        if (donorInfos.size() == 0) {
            DonorInfo donorInfo = new DonorInfo();
            donorInfos.add(donorInfo);
        }
        return donorInfos;
    }

    private boolean canCreateItem(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.INSTANCE_EDITOR_ADD_ITEM);
    }

    private boolean canEditItem(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.INSTANCE_EDITOR_EDIT_ITEM);
    }

    private void addItemInformation(EditorForm editorForm) {
        //TODO: do as per tree structure
    }

    /**
     * Validates the item data and returns true if it is valid
     *
     * @param workInstanceOlemlForm
     * @return
     */
    private boolean isValidItemData(WorkInstanceOlemlForm workInstanceOlemlForm) {
        Item item = workInstanceOlemlForm.getSelectedItem();
        boolean isValidItem = true;
//        StringBuffer stringBuffer = new StringBuffer();
        String oldItemStatus = workInstanceOlemlForm.getOldItemStatus();
        String itemStatus = item.getItemStatus().getCodeValue();
        String itemProperty = getInstanceEditorFormDataHandler()
                .getParameter(OLEConstants.DESC_NMSPC, OLEConstants.DESCRIBE_COMPONENT, OLEParameterConstants.ITEM_STATUS_READONLY);
        String[] itemArray = itemProperty.split(",");
        //If current item status is changed when compared with previous status
        if ((org.apache.commons.lang.StringUtils.isNotEmpty(itemStatus)) && (!itemStatus.equals(oldItemStatus))) {
            for (String status : itemArray) {
                if (status.equalsIgnoreCase(itemStatus)) {
//                    stringBuffer.append("<font size='3' color='red'>" + OLEConstants.ITEM_STATUS_INVALID + "</font>");
                    //workInstanceOlemlForm.setMessage(stringBuffer.toString());
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "invalid.item.status");
                    return false;
                }
            }
        }
        String location = "";
        String holdingsLocation = "";
        if (item.getLocation() != null) {
            location = item.getLocation().getLocationLevel().getName();
        }
        OleHoldings holdings = workInstanceOlemlForm.getSelectedHolding();
        if (holdings.getLocation() != null) {
            holdingsLocation = holdings.getLocation().getLocationLevel().getName();
        }


        //Display error message when item status is empty
        if ( workInstanceOlemlForm.getGlobalEditFlag().equalsIgnoreCase("false") &&
                (item.getItemStatus() == null || item.getItemStatus().getCodeValue() == "" ||
                        item.getItemStatus().getCodeValue().length() < 0)) {
            //stringBuffer.append("<font size='3' color='red'>" + OLEConstants.ITEM_STATUS_ERRORMESSAGE + "</font>");
            GlobalVariables.getMessageMap().putError("documentForm.selectedItem.itemStatus.codeValue", "error.item.status.required");
            workInstanceOlemlForm.setValidInput(false);
            isValidItem = false;
        }

        //Display error message when item type is empty
        if (workInstanceOlemlForm.getGlobalEditFlag().equalsIgnoreCase("false") &&
                (item.getItemType() == null || item.getItemType().getCodeValue() == null || item.getItemType().getCodeValue()=="" || item.getItemType().getCodeValue().length()==0 ||   item.getItemType().getCodeValue().length() < 0)) {
//            stringBuffer.append("<br>");
            //stringBuffer.append("<font size='3' color='red'>" + OLEConstants.ITEM_TYPE_ERRORMESSAGE + "</font>");
            GlobalVariables.getMessageMap().putError("documentForm.selectedItem.itemType.codeValue", "error.item.type.required");
            workInstanceOlemlForm.setValidInput(false);
            isValidItem = false;
        }

        if (holdingsLocation != null && holdingsLocation.length() != 0 && !isValidLocation(holdingsLocation)) {
//            stringBuffer.append("<br>");
            GlobalVariables.getMessageMap().putError("documentForm.selectedHolding.location.locationLevel.name", "error.location");
            workInstanceOlemlForm.setValidInput(false);
            isValidItem = false;
        }
        //Display error message when location is invalid
        if (location != null && location.length() != 0 && !isValidLocation(location)) {
//            stringBuffer.append("<br>");
            //stringBuffer.append("<font size='3' color='red'>Please enter the valid location.</font>");
            GlobalVariables.getMessageMap().putError("documentForm.selectedItem.location.locationLevel.name", "error.location");
            workInstanceOlemlForm.setValidInput(false);
            isValidItem = false;
        }

        boolean hasPermission = canUpdateItemStatus(GlobalVariables.getUserSession().getPrincipalId());
        if (hasPermission) {
//            if (org.apache.commons.lang.StringUtils.isEmpty(stringBuffer.toString())) {
//                isValidItem = true;
//            } else {
//                isValidItem = false;
//            }
        } else {
            //stringBuffer.append("<font size='3' color='red'>" + OLEConstants.ITEM_TYPE_ERROR_MESSAGE + "</font>");
            //workInstanceOlemlForm.setMessage(stringBuffer.toString());
            GlobalVariables.getMessageMap().putError("documentForm.selectedItem.itemStatus", "error.item.type.authorization");
            return false;
        }
        return isValidItem;
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

    private boolean canUpdateItemStatus(String principalId) {
        LOG.debug("Inside the canUpdateItemStatus method");
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.OlePatron.PATRON_NAMESPACE, OLEConstants.CAN_UPDATE_IEM_STATUS);
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


    public EditorForm deleteVerify(EditorForm editorForm) {
        //LOG.info("in instance editor class");
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
        editorForm.getDocTree().setRootElement(docTree);
        StringBuffer deleteMessage = new StringBuffer();
        if(StringUtils.isEmpty(deleteMessage)){
            deleteMessage.append("The following item record will be deleted.");
        }
        editorForm.setDeleteMessage(deleteMessage.toString());
        editorForm.setViewId("DeleteViewPage");
        return editorForm;

    }

    /**
     * This method deletes the item record from docstore by the doc id.
     * @param editorForm
     * @return
     * @throws Exception
     */
    public EditorForm delete(EditorForm editorForm) throws Exception {
        return deleteFromDocStore(editorForm);
    }

    @Override
    public EditorForm addORRemoveItemNote(EditorForm editorForm, HttpServletRequest request) {
        String methodName = request.getParameter("methodToCall");
        if (methodName.equalsIgnoreCase("addItemNote")) {
            WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            index++;
            List<Note> itemNote = workInstanceOlemlForm.getSelectedItem().getNote();
            itemNote.add(index, new Note());
            editorForm.setDocumentForm(workInstanceOlemlForm);
        } else if (methodName.equalsIgnoreCase("removeItemNote")) {
            WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            List<Note> itemNote = workInstanceOlemlForm.getSelectedItem().getNote();
            if (itemNote.size() > 1) {
                itemNote.remove(index);
            } else {
                if (itemNote.size() == 1) {
                    itemNote.remove(index);
                    Note note = new Note();
                    itemNote.add(note);
                }
            }
            editorForm.setDocumentForm(workInstanceOlemlForm);
        }
        return editorForm;
    }

    @Override
    public EditorForm addORRemoveItemDonor(EditorForm editorForm, HttpServletRequest request) {
        String methodName = request.getParameter("methodToCall");
        WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
        int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if (methodName.equalsIgnoreCase("addDonorToItem")) {
            index++;
            List<DonorInfo> donorInfo = workInstanceOlemlForm.getSelectedItem().getDonorInfo();
             for (DonorInfo donorInformation : donorInfo) {
                if (null != donorInformation.getDonorNote()) {
                    String modifiedValue = donorInformation.getDonorNote().replaceAll("\"", "&quot;");
                    donorInformation.setDonorNote(modifiedValue);
                }
                if (null != donorInformation.getDonorPublicDisplay()) {
                    String modifiedValue = donorInformation.getDonorPublicDisplay().replaceAll("\"", "&quot;");
                    donorInformation.setDonorPublicDisplay(modifiedValue);
                }
            }
            donorInfo.add(index, new DonorInfo());
            editorForm.setDocumentForm(workInstanceOlemlForm);
        } else if (methodName.equalsIgnoreCase("removeDonorFromItem")) {
            List<DonorInfo> donorInfo = workInstanceOlemlForm.getSelectedItem().getDonorInfo();
            for (DonorInfo donorInformation : donorInfo) {
                if (null != donorInformation.getDonorNote()) {
                    String modifiedValue = donorInformation.getDonorNote().replaceAll("\"", "&quot;");
                    donorInformation.setDonorNote(modifiedValue);
                }
                if (null != donorInformation.getDonorPublicDisplay()) {
                    String modifiedValue = donorInformation.getDonorPublicDisplay().replaceAll("\"", "&quot;");
                    donorInformation.setDonorPublicDisplay(modifiedValue);
                }
            }
            if(donorInfo.size() > 1){
                donorInfo.remove(index);
            }else{
                if(donorInfo.size() ==1){
                    donorInfo.remove(index);
                    DonorInfo donor = new DonorInfo();
                    donorInfo.add(donor);
                }
            }
            editorForm.setDocumentForm(workInstanceOlemlForm);
        }
        return editorForm;
    }

    @Override
    public EditorForm bulkUpdate(EditorForm editorForm, List<String> ids) {
        WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" HH:mm:ss");
        String dateStr = sdf.format(date);
        String user = GlobalVariables.getUserSession().getLoggedInUserPrincipalName();
        String principalName = GlobalVariables.getUserSession().getPrincipalName();
        //String docId = editorForm.getDocId();
        //String instanceId = editorForm.getInstanceId();
        String editorMessage = "";
        //Bib bib = null;
        editorForm.setHeaderText("Global Edit - Item");

        try {

            workInstanceOlemlForm.setGlobalEditFlag("true");
            if (!isValidItemData(workInstanceOlemlForm)) {
                getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                return workInstanceOlemlForm;
            }
            String staffOnlyFlagForItem = String.valueOf(editorForm.isStaffOnlyFlagForItem());
            Item oleItem = workInstanceOlemlForm.getSelectedItem();
            String itemXmlContent = getInstanceEditorFormDataHandler().buildItemContent(oleItem, staffOnlyFlagForItem);
            ItemOleml itemDoc = new ItemOleml();
            itemDoc.setCategory(DocCategory.WORK.getCode());
            itemDoc.setType(DocType.ITEM.getCode());
            itemDoc.setFormat(DocFormat.OLEML.getCode());
            itemDoc.setCreatedOn(dateStr);
            itemDoc.setCreatedBy(user);
            itemDoc.setUpdatedBy(principalName);
            String canUpdateStaffOnlyFlag = "false";
            if (editorForm.isStaffOnlyFlagInGlobalEdit()) {
                canUpdateStaffOnlyFlag = "true";
                editorForm.setStaffOnlyFlagForItem(true);
                itemDoc.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
            }
            else if (!editorForm.isStaffOnlyFlagInGlobalEdit()) {
                canUpdateStaffOnlyFlag = "true";
                editorForm.setStaffOnlyFlagForItem(false);
                itemDoc.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
            }
            itemDoc.setStaffOnly(editorForm.isStaffOnlyFlagForItem());
            itemDoc.setContent(itemXmlContent);
            /*Holdings holdings = new PHoldings();
            holdings.setId(editorForm.getInstanceId());
            itemDoc.setHolding(holdings);*/
            try {
                getDocstoreClientLocator().getDocstoreClient().bulkUpdateItem(itemDoc,ids,canUpdateStaffOnlyFlag);
            } catch (Exception e) {
                LOG.error("Exception :", e);
                DocstoreException docstoreException = (DocstoreException) e;
                String errorCode = docstoreException.getErrorCode();
                if (StringUtils.isNotEmpty(errorCode)) {
                    Map<String, String> paramsMap = docstoreException.getErrorParams();
                    if (paramsMap != null && paramsMap.size() > 0 && paramsMap.containsKey("barcode")) {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, errorCode, paramsMap.get("barcode"));
                    } else {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, errorCode);
                    }
                } else {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                }
                getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
                return workInstanceOlemlForm;
            }
            editorForm.setDocId(itemDoc.getId());
            getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
            editorMessage = "item.record.load.message";

        } catch (Exception e) {
            LOG.error("Exception :", e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
            getInstanceEditorFormDataHandler().setLocationDetails(workInstanceOlemlForm);
            return workInstanceOlemlForm;
        }
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, editorMessage);
        return workInstanceOlemlForm;
    }

    @Override
    public EditorForm copy(EditorForm editorForm) {
        editorForm.setDocId(null);
        editorForm.setOleReturnHistoryRecords(Collections.EMPTY_LIST);

        WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
        Item selectedItem = workInstanceOlemlForm.getSelectedItem();
        if (null != selectedItem) {
            AccessInformation accessInformation = selectedItem.getAccessInformation();
            if (null != accessInformation) {
                accessInformation.setBarcode(null);
            }
            selectedItem.setEnumeration(null);
            selectedItem.setPurchaseOrderLineItemIdentifier(null);
            selectedItem.setVendorLineItemIdentifier(null);
            selectedItem.setFund(null);
            selectedItem.setPrice(null);

            selectedItem.setCurrentBorrower(null);
            selectedItem.setProxyBorrower(null);
            selectedItem.setItemStatusEffectiveDate(null);
            selectedItem.setFastAddFlag(false);
            selectedItem.setDueDateTime(null);
            selectedItem.setClaimsReturnedFlag(false);
            selectedItem.setClaimsReturnedFlagCreateDate(null);
            selectedItem.setClaimsReturnedNote(null);
            selectedItem.setItemDamagedStatus(false);
            selectedItem.setDamagedItemNote(null);
            selectedItem.setMissingPieceFlag(false);
            selectedItem.setMissingPiecesCount(null);
            selectedItem.setMissingPieceFlagNote(null);
            selectedItem.getNote().clear();
            selectedItem.getNote().add(new Note());
        }
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, OLEConstants.MARC_EDITOR_ITEM_COPY_MESSAGE);
        return editorForm;
    }

    public OleLoanDocumentDaoOjb getLoanDaoOjb() {
        if (null == loanDaoOjb) {
            loanDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return loanDaoOjb;
    }


    public DateTimeService getDateTimeService() {
        return (DateTimeService)SpringContext.getService("dateTimeService");
    }
}