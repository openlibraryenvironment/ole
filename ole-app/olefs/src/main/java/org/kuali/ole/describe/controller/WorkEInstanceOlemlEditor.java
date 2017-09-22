package org.kuali.ole.describe.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.*;
import org.kuali.ole.describe.form.*;
import org.kuali.ole.describe.service.DiscoveryHelperService;
import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.engine.client.DocstoreLocalClient;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.describe.bo.InstanceEditorFormDataHandler;
import org.kuali.ole.describe.form.InstanceEditorForm;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLEEditorResponse;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.OLEEResourceInstance;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.service.*;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pp7788
 * Date: 12/11/12
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * InstanceEditorController is the controller class for Instance Editor
 */
//@Controller
//@RequestMapping(value = "/instanceeditor")
public class WorkEInstanceOlemlEditor
        extends AbstractEditor
        implements DocumentEditor {

    private static final Logger LOG = Logger.getLogger(WorkEInstanceOlemlEditor.class);

    //    private DocstoreHelperService docstoreHelperService;
    private InstanceEditorFormDataHandler instanceEditorFormDataHandler;
    private EInstanceFormDataHandler eInstanceFormDataHandler;
    private DiscoveryHelperService discoveryHelperService;
    private OLEEResourceSearchService oleEResourceSearchService;
    private DocumentService documentService;
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
    private DocstoreClient docstoreClient = getDocstoreLocalClient();

    private static WorkEInstanceOlemlEditor workEInstanceOlemlEditor = new WorkEInstanceOlemlEditor();

    public static WorkEInstanceOlemlEditor getInstance() {
        return workEInstanceOlemlEditor;
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

    private WorkEInstanceOlemlEditor() {
    }

    @Override
    public EditorForm copy(EditorForm editorForm) {
        loadDocument(editorForm);
        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        workEInstanceOlemlForm.getSelectedEHoldings().setEResourceId(workEInstanceOlemlForm.geteResourceId()!=null&&!workEInstanceOlemlForm.geteResourceId().isEmpty()?workEInstanceOlemlForm.geteResourceId():null);
//        workEInstanceOlemlForm.getSelectedEHoldings().setEResourceTitle(null);
        editorForm.seteResourceId(workEInstanceOlemlForm.geteResourceId()!=null&&!workEInstanceOlemlForm.geteResourceId().isEmpty()?workEInstanceOlemlForm.geteResourceId():null);
        editorForm.seteResourceTitle(workEInstanceOlemlForm.geteResourceTitle()!=null&&!workEInstanceOlemlForm.geteResourceTitle().isEmpty()?workEInstanceOlemlForm.geteResourceTitle():null);
        editorForm.setDocId(null);
        editorForm.setShowEditorFooter(false);
        editorForm.setFromSearch(false);

        //editorForm.setMessage("Record copied successfully");
        //GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "info.einstance.copy.success");
        return editorForm;
    }

    @Override
    public EditorForm loadDocument(EditorForm editorForm) {
        LOG.info("loadDocument in E-Instance Editor");
        WorkEInstanceOlemlForm workEInstanceOlemlForm = new WorkEInstanceOlemlForm();
        String docId = editorForm.getDocId();
        String bibId = editorForm.getBibId();
        String tokenId = editorForm.getTokenId();
        workEInstanceOlemlForm.setTokenId(tokenId);
        workEInstanceOlemlForm.setBibId(bibId);
        String docType = editorForm.getDocType();
        editorForm.setHasLink(true);
        if ((StringUtils.isBlank(editorForm.getTitle()))) {
            getTitleInfo(editorForm);
        }
        if (editorForm.isFromDublin()) {
            editorForm.setHasLink(false);
        }
        if (editorForm.getTitle() != null && editorForm.getTitle().contains("<")) {
            String title = org.apache.commons.lang.StringEscapeUtils.escapeHtml((editorForm.getTitle()));
            editorForm.setTitle(title);
        }
        editorForm.setShowEditorFooter(false);
        editorForm.setFromSearch(false);
        String eResId = null;
        workEInstanceOlemlForm.seteResourceId(editorForm.geteResourceId());
        Bib bib = null;
        List<BibTree> bibTreeList = null;
        BibTree bibTree = null;
        if (StringUtils.isNotEmpty(bibId)) {
            try {
                bibTree = docstoreClient.retrieveBibTree(bibId);
                bibTreeList = new ArrayList<>();
                bibTreeList.add(bibTree);
                bib = bibTree.getBib();
                workEInstanceOlemlForm.setBibTreeList(bibTreeList);
                String titleField = bib.getTitle() + " / " + bib.getAuthor() + " / " + DocumentUniqueIDPrefix.getDocumentId(bib.getId());
                editorForm.setTitle(titleField);
            } catch (Exception e) {
                LOG.error("Exception :", e);
                e.printStackTrace();
            }
        }
        try {
            if (docId != null && docId.length() > 0) {


                Holdings holdings = docstoreClient.retrieveHoldings(docId);
                String docStoreData = holdings.getContent();
                OleHoldings eHoldings = holdingOlemlRecordProcessor.fromXML(docStoreData);
                if (eResId == null || eResId.length() == 0) {
                    eResId = eHoldings.getEResourceId();
                    workEInstanceOlemlForm.seteResourceId(eResId);
                }
                workEInstanceOlemlForm.setHoldingsId(DocumentUniqueIDPrefix.getDocumentId(holdings.getId()));
                editorForm.setStaffOnlyFlagForHoldings(holdings.isStaffOnly());
                getOleEResourceSearchService().getEResourcesFields(eResId, eHoldings, workEInstanceOlemlForm);
                getOleEResourceSearchService().getEResourcesLicenseFields(eResId, workEInstanceOlemlForm);
                if (eHoldings.getExtentOfOwnership() != null && eHoldings.getExtentOfOwnership().size() > 0
                        && eHoldings.getExtentOfOwnership().get(0).getCoverages() != null
                        && eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().size() == 0) {
                    eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().add(new Coverage());
                }
                if (eHoldings.getExtentOfOwnership() != null && eHoldings.getExtentOfOwnership().size() > 0
                        && eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses() != null
                        && eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().size() == 0) {
                    eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().add(new PerpetualAccess());
                }
                if (eHoldings.getNote() != null && eHoldings.getNote().size() == 0) {
                    eHoldings.getNote().add(new Note());
                }
                if (eHoldings.getDonorInfo() != null && eHoldings.getDonorInfo().size() == 0) {
                    eHoldings.getDonorInfo().add(new DonorInfo());
                }
                else {
                    String modifiedValue = null;
                    for (DonorInfo donorInformation : eHoldings.getDonorInfo()) {
                        if (null != donorInformation.getDonorNote()) {
                            modifiedValue = donorInformation.getDonorNote().replaceAll("&quot;","\"");
                          }
                            donorInformation.setDonorNote(modifiedValue);
                        if (null != donorInformation.getDonorPublicDisplay()) {
                                modifiedValue = donorInformation.getDonorPublicDisplay().replaceAll("&quot;","\"");
                            donorInformation.setDonorPublicDisplay(modifiedValue);
                        }
                    }
                }
                if(eHoldings.getLink().size() == 0){
                    List<Link> links = new ArrayList<>();
                    Link link = new Link();
                    link.setText("");
                    link.setUrl("");
                    links.add(link);
                    eHoldings.setLink(links);
                }
                workEInstanceOlemlForm.setSelectedEHoldings(eHoldings);

                if(workEInstanceOlemlForm.getSelectedEHoldings() != null &&  workEInstanceOlemlForm.getSelectedEHoldings().getHoldingsAccessInformation() != null
                        && workEInstanceOlemlForm.getSelectedEHoldings().getHoldingsAccessInformation().getProxiedResource() != null
                        && workEInstanceOlemlForm.getSelectedEHoldings().getHoldingsAccessInformation().getProxiedResource().equalsIgnoreCase(OLEConstants.OLEEResourceRecord.ON)) {
                    workEInstanceOlemlForm.setProxiedResource(true);
                }
                workEInstanceOlemlForm.getSelectedEHoldings().setStatusDate(new Date(System.currentTimeMillis()).toString());
                getEInstanceFormDataHandler().setLocationDetails(workEInstanceOlemlForm);
                if(editorForm.getMethodToCall().equalsIgnoreCase("copy")){
                    GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "record.copy.message");
                }
                else{
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "record.load.message");
                }
                editorForm.setHoldingCreatedDate(holdings.getCreatedOn());
                editorForm.setHoldingCreatedBy(holdings.getCreatedBy());
                editorForm.setHoldingUpdatedDate(holdings.getUpdatedOn());
                editorForm.setHoldingUpdatedBy(holdings.getUpdatedBy());
                getOleEResourceSearchService().getAcquisitionInfoFromPOAndInvoice(docId, workEInstanceOlemlForm);
            } else {
                OleHoldings eHoldings = new OleHoldings();
                setStaffOnly(editorForm);
                eHoldings.setStatusDate(new Date(System.currentTimeMillis()).toString());
                eHoldings.setEResourceId(editorForm.geteResourceId());
                //getOleEResourceSearchService().getAccessLocationFromEInstance(eHoldings, workEInstanceOlemlForm);
                getOleEResourceSearchService().getEResourcesFields(editorForm.geteResourceId(), eHoldings, workEInstanceOlemlForm);
                getOleEResourceSearchService().getEResourcesLicenseFields(editorForm.geteResourceId(), workEInstanceOlemlForm);
                if (eHoldings.getExtentOfOwnership() != null && eHoldings.getExtentOfOwnership().size() > 0
                        && eHoldings.getExtentOfOwnership().get(0).getCoverages() != null
                        && eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().size() == 0) {
                    eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().add(new Coverage());
                }
                if (eHoldings.getExtentOfOwnership() != null && eHoldings.getExtentOfOwnership().size() > 0
                        && eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses() != null
                        && eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().size() == 0) {
                    eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().add(new PerpetualAccess());
                }
                if (eHoldings.getNote() != null && eHoldings.getNote().size() == 0) {
                    eHoldings.getNote().add(new Note());
                }
                if (eHoldings.getDonorInfo() != null && eHoldings.getDonorInfo().size() == 0) {
                    eHoldings.getDonorInfo().add(new DonorInfo());
                }
                else {
                    for (DonorInfo donorInformation : eHoldings.getDonorInfo()) {
                        if (null != donorInformation.getDonorNote()) {
                            String modifiedValue = donorInformation.getDonorNote().replaceAll("\"","&quot;");
                            donorInformation.setDonorNote(modifiedValue);
                        }
                        if (null != donorInformation.getDonorPublicDisplay()) {
                            String modifiedValue = donorInformation.getDonorPublicDisplay().replaceAll("\"","&quot;");
                            donorInformation.setDonorPublicDisplay(modifiedValue);
                        }
                    }
                }
                List<Link> links = new ArrayList<>();
                Link link = new Link();
                link.setText("");
                link.setUrl("");
                links.add(link);
                eHoldings.setLink(links);
                workEInstanceOlemlForm.setSelectedEHoldings(eHoldings);
                workEInstanceOlemlForm.setViewId("WorkEInstanceViewPage");
                getEInstanceFormDataHandler().setLocationDetails(workEInstanceOlemlForm);
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "eholdings.record.new.load.message");
                editorForm.setHoldingCreatedDate("");
                editorForm.setHoldingCreatedBy("");
            }
        } catch (DocstoreException e) {
            LOG.error(e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
            return workEInstanceOlemlForm;
        }
        catch (Exception e) {
            LOG.error("loadDocument Instance Exception:" + e);
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "record.load.fail.message ");
            return workEInstanceOlemlForm;
        }
        if(workEInstanceOlemlForm.getSelectedEHoldings()!=null && workEInstanceOlemlForm.getSelectedEHoldings().getPlatform()==null){
            workEInstanceOlemlForm.getSelectedEHoldings().setPlatform(new Platform());
        }
        workEInstanceOlemlForm.setViewId("WorkEInstanceViewPage");
        workEInstanceOlemlForm.setIssn(bib.getIssn());
        editorForm.seteResourceTitle(workEInstanceOlemlForm.geteResourceTitle());
        String directory = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(org.kuali.ole.sys.OLEConstants.EXTERNALIZABLE_HELP_URL_KEY);
        editorForm.setExternalHelpUrl(directory+"/reference/webhelp/OLE/content/ch04s01.html#_EInstanceEditor");
        editorForm.setHeaderText("E-Holdings");
        if (workEInstanceOlemlForm.getTokenId() != null && !workEInstanceOlemlForm.getTokenId().isEmpty()) {
            editorForm.setTokenId(workEInstanceOlemlForm.getTokenId());
        }
        OleHoldings oleHoldings = workEInstanceOlemlForm.getSelectedEHoldings();
        if(oleHoldings!=null){
            if(oleHoldings.getCallNumber()  == null) {
                CallNumber callNumber = new CallNumber();
                String callNumberDefaultValue = getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DESC_NMSPC, OLEConstants
                        .DESCRIBE_COMPONENT, OLEConstants.E_HOLDINGS_CALL_NUMBER_TYPE);
                ShelvingScheme shelvingScheme = new ShelvingScheme();
                shelvingScheme.setCodeValue(callNumberDefaultValue);
                callNumber.setShelvingScheme(shelvingScheme);
                oleHoldings.setCallNumber(callNumber);
            }
        }
        return workEInstanceOlemlForm;
    }

    private void getTitleInfo(EditorForm editorForm) {
//        WorkBibDocument workBibDocument = new WorkBibDocument();
//        workBibDocument.setId(editorForm.getBibId());
//        QueryService queryService = QueryServiceImpl.getInstance();
//        WorkBibDocument document = null;
//        try {
//            document = queryService.buildBibDocumentInfo(workBibDocument);
//        } catch (Exception e) {
//            LOG.error("Failed to get Title / Author details : " + e.getMessage());
//        }
//        StringBuilder titleBuilder = new StringBuilder();
//        if (document != null) {
//            titleBuilder.append(document.getTitle());
//            titleBuilder.append("/");
//            if (document.getAuthor() != null) {
//                titleBuilder.append(document.getAuthor());
//            }
//            if (workBibDocument.getDocFormat().equalsIgnoreCase("dublinunq")) {
//                editorForm.setFromDublin(true);
//            }
//            editorForm.setTitle(org.apache.commons.lang.StringEscapeUtils.escapeHtml(titleBuilder.toString()));
//        }
    }

    @Override
    public EditorForm saveDocument(EditorForm editorForm) {
        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        String docId = editorForm.getDocId();
        String docType = editorForm.getDocType();
        String bibId = editorForm.getBibId();
        String editorStatusMessage = "";
        List<BibTree> bibTreeList = null;
        BibTree bibTree = null;
        Holdings eHoldingsDoc = null;
        String user = GlobalVariables.getUserSession().getPrincipalName();
        if (StringUtils.isNotEmpty(bibId)) {
            try {
                bibTree = docstoreClient.retrieveBibTree(bibId);
                bibTreeList = new ArrayList<>();
                bibTreeList.add(bibTree);
                workEInstanceOlemlForm.setBibTreeList(bibTreeList);
                String titleField = bibTree.getBib().getTitle() + " / " + bibTree.getBib().getAuthor() + " / " + DocumentUniqueIDPrefix.getDocumentId(bibTree.getBib().getId());
                editorForm.setTitle(titleField);
            } catch (Exception e) {
                LOG.error("Exception :", e);
                e.printStackTrace();
            }
        }
        long startTime = System.currentTimeMillis();
        try {
            if (docId != null && docId.length() > 0) {
                OleHoldings eHoldings = workEInstanceOlemlForm.getSelectedEHoldings();
                List<DonorInfo> donorInfos = eHoldings.getDonorInfo();
                if(donorInfos.size() > 0) {
                    for (DonorInfo donorInformation : donorInfos) {
                        if(donorInformation.getDonorPublicDisplay() != null || donorInformation.getDonorNote() != null) {
                            if(donorInformation.getDonorNote() != null) {
                                String modifiedDonorNoteValue = donorInformation.getDonorNote().replaceAll("\"","&quot;");
                                donorInformation.setDonorNote(modifiedDonorNoteValue);

                            }
                            if(donorInformation.getDonorPublicDisplay() != null) {
                                String modifiedDonorDisplayValue = donorInformation.getDonorPublicDisplay().replaceAll("\"", "&quot;");
                                donorInformation.setDonorPublicDisplay(modifiedDonorDisplayValue);
                            }
                        }
                        else {
                            Map donorMap = new HashMap();
                            donorMap.put("donorCode", donorInformation.getDonorCode());
                            OLEDonor oleDonor = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEDonor.class, donorMap);
                            if(oleDonor.getDonorNote() != null) {
                                String modifiedDonorNoteValue = oleDonor.getDonorNote().replaceAll("\"","&quot;");
                                donorInformation.setDonorNote(modifiedDonorNoteValue);
                            }
                            if(oleDonor.getDonorPublicDisplay() != null) {
                                String modifiedDonorDisplayValue = oleDonor.getDonorPublicDisplay().replaceAll("\"","&quot;");
                                donorInformation.setDonorPublicDisplay(modifiedDonorDisplayValue);
                            }
                        }
                    }
                    eHoldings.setDonorInfo(donorInfos);
                }
                boolean dateFlag = getOleEResourceSearchService().validateDates(eHoldings);
                if (dateFlag) {
                    getOleEResourceSearchService().getEResourcesFields(editorForm.geteResourceId(), eHoldings, workEInstanceOlemlForm);
                    String content = getInstanceEditorFormDataHandler().buildHoldingContent(eHoldings);
                    getOleEResourceSearchService().getEResourcesLicenseFields(editorForm.geteResourceId(), workEInstanceOlemlForm);
                    eHoldingsDoc = new EHoldings();
                    eHoldingsDoc.setId(editorForm.getDocId());
                    eHoldingsDoc.setType(editorForm.getDocType());
                    eHoldingsDoc.setFormat(editorForm.getDocFormat());
                    eHoldingsDoc.setCategory(editorForm.getDocCategory());
                    eHoldingsDoc.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
                    eHoldingsDoc.setUpdatedBy(user);
                    eHoldingsDoc.setBib(bibTree.getBib());
                    eHoldings = (OleHoldings) holdingOlemlRecordProcessor.fromXML(content);
                    eHoldings = getEHolding(editorForm, eHoldings);
                    String newContent = holdingOlemlRecordProcessor.toXML(eHoldings);
                    eHoldingsDoc.setContent(newContent);
                    docstoreClient.updateHoldings(eHoldingsDoc);
                    workEInstanceOlemlForm.getSelectedEHoldings().setHoldingsIdentifier(eHoldingsDoc.getId());
                    // To add updated EHoldings to bib tree
                    addEHoldingsToBibTree(workEInstanceOlemlForm.getBibTreeList(), eHoldingsDoc);
                    editorStatusMessage = "holdings.record.update.message";
                } else {
                    return workEInstanceOlemlForm;
                }
            } else {
                OleHoldings eHoldings = workEInstanceOlemlForm.getSelectedEHoldings();
                boolean dateFlag = getOleEResourceSearchService().validateDates(eHoldings);
                if (dateFlag) {
                    getOleEResourceSearchService().getEResourcesFields(editorForm.geteResourceId(), eHoldings, workEInstanceOlemlForm);
                    getOleEResourceSearchService().getEResourcesLicenseFields(editorForm.geteResourceId(), workEInstanceOlemlForm);
                    String content = getInstanceEditorFormDataHandler().buildHoldingContent(eHoldings);
                    eHoldingsDoc = new EHoldings();
                    eHoldingsDoc.setCategory(DocCategory.WORK.getCode());
                    eHoldingsDoc.setType(DocType.EHOLDINGS.getCode());
                    eHoldingsDoc.setFormat(DocFormat.OLEML.getCode());
                    eHoldingsDoc.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
                    eHoldingsDoc.setCreatedBy(user);
                    eHoldingsDoc.setBib(bibTree.getBib());
                    eHoldings = (OleHoldings) holdingOlemlRecordProcessor.fromXML(content);
                    eHoldings = getEHolding(editorForm, eHoldings);
                    String newContent = holdingOlemlRecordProcessor.toXML(eHoldings);
                    eHoldingsDoc.setContent(newContent);
                    docstoreClient.createHoldings(eHoldingsDoc);
                    editorForm.setDocId(eHoldingsDoc.getId());
                    workEInstanceOlemlForm.setHoldingsId(DocumentUniqueIDPrefix.getDocumentId(eHoldingsDoc.getId()));
                    processResponse(eHoldingsDoc.getId(), eHoldingsDoc.getBib().getId(), editorForm.getTokenId(), editorForm.getLinkToOrderOption());
                    workEInstanceOlemlForm.getSelectedEHoldings().setHoldingsIdentifier(eHoldingsDoc.getId());
                    HoldingsTree holdingsTree = new HoldingsTree();
                    holdingsTree.setHoldings(eHoldingsDoc);
                    List<HoldingsTree> holdingsTreeList = new ArrayList<>();
                    holdingsTreeList.add(holdingsTree);
                    bibTree.getHoldingsTrees().addAll(holdingsTreeList);
                    editorStatusMessage = "record.create.message";
                } else {
                    return workEInstanceOlemlForm;
                }
            }
            if(editorForm.getDocId()!=null){
            Holdings holdingsDoc = docstoreClient.retrieveHoldings(editorForm.getDocId());
            editorForm.setHoldingUpdatedDate(holdingsDoc.getUpdatedOn());
            editorForm.setHoldingUpdatedBy(holdingsDoc.getUpdatedBy());
            editorForm.setHoldingCreatedDate(holdingsDoc.getCreatedOn());
            editorForm.setHoldingCreatedBy(holdingsDoc.getCreatedBy());
            }

        }
        catch (DocstoreException e) {
            LOG.error(e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
            return workEInstanceOlemlForm;
        }
        catch (Exception e) {
            LOG.error("Exception :", e);
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, "docstore.response", e.getMessage());
            return workEInstanceOlemlForm;

        }
        long endTime = System.currentTimeMillis();
        editorForm.setSolrTime(String.valueOf((endTime-startTime)/1000));

        try {
            getEInstanceFormDataHandler().setLocationDetails(workEInstanceOlemlForm);
            String eResId = workEInstanceOlemlForm.geteResourceId();
            editorForm.seteResourceId(null);
            workEInstanceOlemlForm.setViewId("WorkEInstanceViewPage");

            GlobalVariables.getMessageMap().getInfoMessages().clear();
            if (eResId != null && !eResId.isEmpty()) {
                Map<String, String> tempId = new HashMap<String, String>();
                tempId.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, eResId);
                OLEEResourceRecordDocument tempDocument = (OLEEResourceRecordDocument) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, tempId);

                Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
                tempDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(tempDocument.getDocumentNumber()));
                tempDocument.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(tempDocument.getDocumentNumber(), principalPerson));
                if (tempDocument != null) {
                    try {
                        tempDocument.setSelectInstance(OLEConstants.OLEEResourceRecord.CREATE_NEW_INSTANCE);
                        tempDocument.seteInstanceFlag(true);
                        Map<String, String> eInsMap = new HashMap<>();
                        eInsMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, tempDocument.getOleERSIdentifier());
                        eInsMap.put(OLEConstants.INSTANCE_ID, editorForm.getDocId());
                        List<OLEEResourceInstance> oleERSInstances = (List<OLEEResourceInstance>)KRADServiceLocator.getBusinessObjectService().findMatching(OLEEResourceInstance.class, eInsMap);
                        tempDocument.setOleERSInstances(oleERSInstances);
                        for (OLEEResourceInstance oleeResourceInstance : tempDocument.getOleERSInstances()) {
                            if (oleeResourceInstance.getInstanceId().equals(editorForm.getDocId())) {
                                processResponse(editorForm.getDocId(), editorForm.getBibId(), tempDocument.getDocumentNumber(), editorForm.getLinkToOrderOption());
                            }
                        }
                        getOleEResourceSearchService().getNewInstance(tempDocument, tempDocument.getDocumentNumber(), eHoldingsDoc);
                        KRADServiceLocator.getBusinessObjectService().save(tempDocument.getCopyList());
                        KRADServiceLocator.getBusinessObjectService().save(tempDocument.getOleERSInstances());
                       // getDocumentService().updateDocument(tempDocument);
                    } catch (Exception e) {
                        LOG.error("Exception :", e);
                        throw new RiceRuntimeException(
                                "Exception trying to save document: " + tempDocument
                                        .getDocumentNumber(), e);
                    }
                }
            }
        }
        catch (DocstoreException e) {
            LOG.error(e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
            return workEInstanceOlemlForm;
        }
        catch (Exception e) {
            LOG.error("Exception :", e);
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, "docstore.response", e.getMessage());
            return workEInstanceOlemlForm;
        }
       /* WorkEInstanceOlemlForm eInstanceOlemlForm = (WorkEInstanceOlemlForm) loadDocument(editorForm);
        GlobalVariables.getMessageMap().getInfoMessages().clear();
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, editorStatusMessage);*/

        return workEInstanceOlemlForm;
    }

    /**
     * This methods adds the updated EHoldings to the bib tree to build left pane tree.
     * @param bibTreeList
     * @param eHoldings
     */
    private void addEHoldingsToBibTree(List<BibTree> bibTreeList, Holdings eHoldings) {
        if (CollectionUtils.isNotEmpty(bibTreeList)) {
            for (BibTree bibTree : bibTreeList) {
                if (CollectionUtils.isNotEmpty(bibTree.getHoldingsTrees())) {
                    for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                        if (null != holdingsTree.getHoldings() && null != eHoldings) {
                            String holdingsId = holdingsTree.getHoldings().getId();
                            String eHoldingsId = eHoldings.getId();
                            if (null != holdingsId && null != eHoldingsId) {
                                if (holdingsId.equals(eHoldingsId)) {
                                    holdingsTree.setHoldings(eHoldings);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void processResponse(String instanceId, String bibId, String tokenId, String linkToOrderOption) throws Exception {
        OLEEditorResponse oleEditorResponse = new OLEEditorResponse();
        oleEditorResponse.setTokenId(tokenId);
        oleEditorResponse.setLinkedInstanceId(instanceId);
        oleEditorResponse.setBib(docstoreClient.retrieveBib(bibId));
        oleEditorResponse.setLinkToOrderOption(linkToOrderOption);
        HashMap<String, OLEEditorResponse> oleEditorResponseMap = new HashMap<String, OLEEditorResponse>();
        oleEditorResponseMap.put(tokenId, oleEditorResponse);
        OleDocstoreResponse.getInstance().setEditorResponse(oleEditorResponseMap);
    }
    public OleHoldings getEHolding(EditorForm editorForm,OleHoldings eHoldings) {
        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        if (null != eHoldings.getHoldingsAccessInformation()) {
            if (workEInstanceOlemlForm.isProxiedResource()) {
                eHoldings.getHoldingsAccessInformation().setProxiedResource(OLEConstants.OLEEResourceRecord.ON);
            } else {
                eHoldings.getHoldingsAccessInformation().setProxiedResource(OLEConstants.OLEEResourceRecord.OFF);
            }
        }
        if(editorForm.getAccessLocationFlag().equalsIgnoreCase(OLEConstants.FALSE) && editorForm.geteResourceTitle() != null){
            eHoldings.getHoldingsAccessInformation().setAccessLocation(null);
        }
        if(editorForm.getStatisticalCodeFlag().equalsIgnoreCase(OLEConstants.FALSE) && editorForm.geteResourceTitle() != null){
            eHoldings.getStatisticalSearchingCode().setCodeValue(null);
        }
        if(editorForm.getNoOfUserFlag().equalsIgnoreCase(OLEConstants.FALSE) && editorForm.geteResourceTitle() != null){
            eHoldings.getHoldingsAccessInformation().setNumberOfSimultaneousUser(null);
        }
        if(editorForm.getAuthenticationTypeFlag().equalsIgnoreCase(OLEConstants.FALSE) && editorForm.geteResourceTitle() != null){
            eHoldings.getHoldingsAccessInformation().setAuthenticationType(null);
        }
        if(editorForm.getPerpetualDateEndFlag().equalsIgnoreCase(OLEConstants.FALSE) && editorForm.geteResourceTitle() != null){
            for(ExtentOfOwnership extentOfOwnershipList:eHoldings.getExtentOfOwnership()){
                for(PerpetualAccess perpetualAccess:extentOfOwnershipList.getPerpetualAccesses().getPerpetualAccess())  {
                    perpetualAccess.setPerpetualAccessEndDateString(StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDateString())?perpetualAccess.getPerpetualAccessEndDateString():"");
                    perpetualAccess.setPerpetualAccessEndDateFormat(StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDateFormat())?perpetualAccess.getPerpetualAccessEndDateFormat():"");
                    perpetualAccess.setPerpetualAccessEndIssue(StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndIssue())?perpetualAccess.getPerpetualAccessEndIssue():"");
                    perpetualAccess.setPerpetualAccessEndVolume(StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndVolume())?perpetualAccess.getPerpetualAccessEndVolume():"");
                    perpetualAccess.setPerpetualAccessEndDate(StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDate())?perpetualAccess.getPerpetualAccessEndDate():"");
                }
            }
        }
        if(editorForm.getPerpetualDateStartFlag().equalsIgnoreCase(OLEConstants.FALSE) && editorForm.geteResourceTitle() != null){
            for(ExtentOfOwnership extentOfOwnershipList:eHoldings.getExtentOfOwnership()){
                for(PerpetualAccess perpetualAccess:extentOfOwnershipList.getPerpetualAccesses().getPerpetualAccess())  {
                    perpetualAccess.setPerpetualAccessStartDateString(StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDateString())?perpetualAccess.getPerpetualAccessStartDateString():"");
                    perpetualAccess.setPerpetualAccessStartDateFormat(StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDateFormat())?perpetualAccess.getPerpetualAccessStartDateFormat():"");
                    perpetualAccess.setPerpetualAccessStartIssue(StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartIssue())?perpetualAccess.getPerpetualAccessStartIssue():"");
                    perpetualAccess.setPerpetualAccessStartVolume(StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartVolume())?perpetualAccess.getPerpetualAccessStartVolume():"");
                    perpetualAccess.setPerpetualAccessStartDate(StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDate())?perpetualAccess.getPerpetualAccessStartDate():"");
                }
            }
        }
        if(editorForm.getCoverageDateStartFlag().equalsIgnoreCase(OLEConstants.FALSE) && editorForm.geteResourceTitle() != null){
            for(ExtentOfOwnership extentOfOwnershipList:eHoldings.getExtentOfOwnership()){
                for(Coverage coverage:extentOfOwnershipList.getCoverages().getCoverage())  {
                    coverage.setCoverageStartIssue(StringUtils.isNotBlank(coverage.getCoverageStartIssue())?coverage.getCoverageStartIssue():"");
                    coverage.setCoverageStartDateString(StringUtils.isNotBlank(coverage.getCoverageStartDateString())?coverage.getCoverageStartDateString():"");
                    coverage.setCoverageStartDateFormat(StringUtils.isNotBlank(coverage.getCoverageStartDateFormat())?coverage.getCoverageStartDateFormat():"");
                    coverage.setCoverageStartVolume(StringUtils.isNotBlank(coverage.getCoverageStartVolume())?coverage.getCoverageStartVolume():"");
                    coverage.setCoverageStartDate(StringUtils.isNotBlank(coverage.getCoverageStartDate())?coverage.getCoverageStartDate():"");
                }
            }
        }
        if(editorForm.getCoverageDateEndFlag().equalsIgnoreCase(OLEConstants.FALSE) && editorForm.geteResourceTitle() != null){
            for(ExtentOfOwnership extentOfOwnershipList:eHoldings.getExtentOfOwnership()){
                for(Coverage coverage:extentOfOwnershipList.getCoverages().getCoverage())  {
                    coverage.setCoverageEndIssue(StringUtils.isNotBlank(coverage.getCoverageEndIssue())?coverage.getCoverageEndIssue():"");
                    coverage.setCoverageEndDateString(StringUtils.isNotBlank(coverage.getCoverageEndDateString())?coverage.getCoverageEndDateString():"");
                    coverage.setCoverageEndDateFormat(StringUtils.isNotBlank(coverage.getCoverageEndDateFormat())?coverage.getCoverageEndDateFormat():"");
                    coverage.setCoverageEndVolume(StringUtils.isNotBlank(coverage.getCoverageEndVolume())?coverage.getCoverageEndVolume():"");
                    coverage.setCoverageEndDate(StringUtils.isNotBlank(coverage.getCoverageEndDate())?coverage.getCoverageEndDate():"");
                }
            }
        }
        return eHoldings;
    }
    /**
     * This method creates a instance of InstanceEditorForm
     *
     * @param httpServletRequest
     * @return InstanceEditorForm
     */
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        return new WorkInstanceOlemlForm();
    }

    /**
     * This method takes the initial request when click on InstanceEditor Screen.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        WorkInstanceOlemlForm instanceEditorForm = (WorkInstanceOlemlForm) form;
        //ModelAndView modelAndView = super.start(instanceEditorForm, result, request, response);
        // return getUIFModelAndView(instanceEditorForm, "WorkHoldingsViewPage");
        return null;
    }


    /**
     * This method will fetch the holding/Item records based on the AdditionalAttributes.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    public ModelAndView load(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        WorkEInstanceOlemlForm workEInstanceOlemlForm = new WorkEInstanceOlemlForm();
        workEInstanceOlemlForm.setHideFooter(false);
        workEInstanceOlemlForm.setViewId("WorkEInstanceViewPage");
        workEInstanceOlemlForm.setMessage("Please enter details for new Item record.");
        //EditorForm workInstanceOlemlForm = (WorkInstanceOlemlForm) form;
        //WorkInstanceOlemlForm workInstanceOlemlForm = new WorkInstanceOlemlForm();
        String uuid = request.getParameter("uuid");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        String docCategory = request.getParameter("docCategory");
        if (null == uuid || StringUtils.isEmpty(uuid)) {
            uuid = workEInstanceOlemlForm.getUuid();
        }
        workEInstanceOlemlForm.setExisting("true");
        try {
              /*if (workEInstanceOlemlForm.getInstanceId() != null && workEInstanceOlemlForm.getInstanceId().length() > 0) {
                String docStoreData = getDocstoreHelperService().getDocstoreData(docCategory,docType,docFormat,workEInstanceOlemlForm.getInstanceId());
                InstanceCollection instanceCollection = new WorkEInstanceOlemlRecordProcessor().fromXML(docStoreData);
                workEInstanceOlemlForm.setSelectedEInstance(instanceCollection.getEInstance().get(0));
            }
            String docStoreData = getDocstoreHelperService().getDocstoreData(docCategory,docType,docFormat,uuid);
            if (docType.equalsIgnoreCase("eHoldings")) {
                EHoldings oleHoldings = new WorkEHoldingOlemlRecordProcessor().fromXML(docStoreData);
                workEInstanceOlemlForm.setSelectedEHoldings(oleHoldings);
                //return getUIFModelAndView(workInstanceOlemlForm,"WorkHoldingsViewPage");
            }
            else {
                InstanceCollection instanceCollection = new WorkEInstanceOlemlRecordProcessor().fromXML(docStoreData);
                workEInstanceOlemlForm.setSelectedEHoldings(instanceCollection.getEInstance().get(0).getEHoldings());
                //return getUIFModelAndView(workInstanceOlemlForm,"WorkHoldingsViewPage");
            }*/
        } catch (Exception e) {
            LOG.error("Exception :", e);
            StringBuffer instanceEditorErrorMessage = new StringBuffer(OLEConstants.INSTANCE_EDITOR_LOAD_FAILURE)
                    .append(" :: \n" + e.getMessage());
            //workInstanceOlemlForm.setMessage(instanceEditorErrorMessage.toString());
            LOG.error(instanceEditorErrorMessage);
            //return getUIFModelAndView(workInstanceOlemlForm);
        }
        return null;
    }

    /**
     * Used for Test-case
     *
     * @param result
     * @param request
     * @param response
     * @param instanceEditorForm
     * @return ModelAndView
     */
    protected ModelAndView callSuper(BindingResult result, HttpServletRequest request, HttpServletResponse response,
                                     InstanceEditorForm instanceEditorForm) {
        //return super.updateComponent(instanceEditorForm, result, request, response);
        return null;
    }

//    /**
//     * Gets the docstoreHelperService attribute
//     *
//     * @return Returns DocstoreHelperService
//     */
//    private DocstoreHelperService getDocstoreHelperService() {
//        if (null == docstoreHelperService) {
//            return new DocstoreHelperService();
//        }
//        return docstoreHelperService;
//    }

//    /**
//     * Sets the docstoreHelperService attribute values.
//     *
//     * @param docstoreHelperService
//     */
//    public void setDocstoreHelperService(DocstoreHelperService docstoreHelperService) {
//        this.docstoreHelperService = docstoreHelperService;
//    }

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

    private EInstanceFormDataHandler getEInstanceFormDataHandler() {
        if (null == eInstanceFormDataHandler) {
            eInstanceFormDataHandler = new EInstanceFormDataHandler();
        }
        return eInstanceFormDataHandler;
    }


//    /**
//     * This method returns AdditionalAttributes instance from the list of Extension.
//     * @param extensionList
//     * @return AdditionalAttributes
//     */
//    private AdditionalAttributes getFirstAdditionalAttributes(List<Extension> extensionList) {
//        for (Extension ext : extensionList) {
//            for (Object obj : ext.getContent()) {
//                if (obj instanceof AdditionalAttributes) {
//                    return (AdditionalAttributes) obj;
//                }
//            }
//        }
//        return null;
//    }

    /**
     * Sets the discoveryHelperService attribute value.
     *
     * @param discoveryHelperService
     */
    public void setDiscoveryHelperService(DiscoveryHelperService discoveryHelperService) {
        this.discoveryHelperService = discoveryHelperService;
    }

    /**
     * Gets the discoveryHelperService attribute.
     *
     * @return Returns DiscoveryHelperService.
     */
    public DiscoveryHelperService getDiscoveryHelperService() {
        if (null == discoveryHelperService) {
            return new DiscoveryHelperService();
        }
        return discoveryHelperService;
    }

    /*@RequestMapping(params = "methodToCall=addExtentOfOwnership")
    public ModelAndView addExtentOfOwnership(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {
        InstanceEditorForm instanceEditorForm = (InstanceEditorForm) form;
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        index++;
        List<ExtentOfOwnership> extentOfOwnershipForUI = instanceEditorForm.getSelectedHolding().getExtentOfOwnership();
        extentOfOwnershipForUI.add(index, new ExtentOfOwnership());
        return super.updateComponent(instanceEditorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=removeExtentOfOwnership")
    public ModelAndView removeExtentOfOwnership(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {
        InstanceEditorForm instanceEditorForm = (InstanceEditorForm) form;
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        List<ExtentOfOwnership> extentOfOwnershipForUI = instanceEditorForm.getSelectedHolding().getExtentOfOwnership();
        if (extentOfOwnershipForUI.size() > 1)
            extentOfOwnershipForUI.remove(index);
        return super.updateComponent(instanceEditorForm, result, request, response);
    }*/


  /*  */

    /**
     * Shows the linked Bibs corresponging to Holdings/Item
     *
     * @param editorForm
     * @return
     *//*
    @Override
    public EditorForm showBibs(EditorForm editorForm) {
        WorkEInstanceOlemlForm workEInstanceOlemlForm = new WorkEInstanceOlemlForm();
        QueryService queryService = QueryServiceImpl.getInstance();
        List<String> bibUUIDList = new ArrayList<String>();
        String instanceId = "";
        String docType = editorForm.getDocType();
        List<WorkBibDocument> workBibDocumentList = new ArrayList<WorkBibDocument>();
        try {

            if ((docType.equalsIgnoreCase(DocType.INSTANCE.getCode()) || docType
                    .equalsIgnoreCase(DocType.HOLDINGS.getCode())) || (docType
                    .equalsIgnoreCase(DocType.ITEM.getCode()))) {
                instanceId = editorForm.getInstanceId();
            }
            if (StringUtils.isNotEmpty(instanceId)) {
                bibUUIDList = queryService.queryForBibs(instanceId);
            }
            DocstoreHelperService docstoreHelperService = new DocstoreHelperService();
            if (bibUUIDList != null && bibUUIDList.size() > 0) {
                for (String bibUUID : bibUUIDList) {
                    WorkBibDocument workBibDocument = new WorkBibDocument();
                    workBibDocument.setId(bibUUID);
                    workBibDocument = docstoreHelperService.getInfoForBibTree(workBibDocument);
                    workBibDocumentList.add(workBibDocument);
                }
            }

            editorForm.setWorkBibDocumentList(workBibDocumentList);
        } catch (SolrServerException e) {
            LOG.error("Exception:" + e.getMessage(), e);
        }
        workEInstanceOlemlForm.setViewId("ShowBibViewPage");
        return workEInstanceOlemlForm;
    }*/

    /**
     * This method deletes the EHoldings record from docstore by the doc id.
     * @param editorForm
     * @return
     * @throws Exception
     */
    @Override
    public EditorForm delete(EditorForm editorForm) throws Exception {
        return deleteFromDocStore(editorForm);
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
        deleteMessage.append("WARNING : The following E-Holdings record will be deleted.\n");
        editorForm.setDeleteMessage(deleteMessage.toString());
        editorForm.getDocTree().setRootElement(docTree);
        editorForm.setViewId("DeleteViewPage");
        return editorForm;
    }

    /*@Override
    public EditorForm delete(EditorForm editorForm) throws Exception {
        WorkEInstanceOlemlForm workEInstanceOlemlForm = new WorkEInstanceOlemlForm();
        String operation = OLEConstants.DELETE;
        BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
        //Map<String, String> map = new HashMap<>();
        //Map<String, String> uuidMap = new HashMap<>();
        //map.put("bibId", editorForm.getBibId());
        //List<OleCopy> listOfValues = (List<OleCopy>) boService.findMatching(OleCopy.class, map);

        //if (listOfValues != null && listOfValues.size() > 0) {
        //    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DELETE_PURCHASE_ORDER_FAIL_MESSAGE, "Bib");
        //    return workEInstanceOlemlForm;
        //}

        getResponseFromDocStore(editorForm, editorForm.getDocId(), operation);
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, OLEConstants.RECORD_DELETE_MESSAGE);

       *//* if (responseXml.contains("Success")) {
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "record.delete.message");
            workEInstanceOlemlForm.setShowDeleteTree(false);
            workEInstanceOlemlForm.setHasLink(false);
        } else {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "record.delete.fail.message");
        }*//*
        removeDocumentFromTree(editorForm);
        editorForm.setShowDeleteTree(false);
        editorForm.setHasLink(false);
        workEInstanceOlemlForm.setViewId(OLEConstants.DELETE_VIEW_PAGE);
        return workEInstanceOlemlForm;
    }*/

    /*public boolean validateRequiredFields(WorkEInstanceOlemlForm workEInstanceOlemlForm) {
        List<Coverage> coverages = workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().getCoverages().getCoverage();
        if (coverages.size() > 0) {
            for (Coverage coverage : coverages) {
                String date = coverage.getCoverageStartDate();
                if (date != null && !date.isEmpty()) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }*/

    @Override
    public EditorForm bulkUpdate(EditorForm editorForm, List<String> ids) {
        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        String docId = editorForm.getDocId();
        String docType = editorForm.getDocType();
        String bibId = editorForm.getBibId();
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE+" HH:mm:ss");
        String dateStr = sdf.format(date);
        String user = GlobalVariables.getUserSession().getPrincipalName();
        String editorStatusMessage = "";
        //List<BibTree> bibTreeList = null;
        //BibTree bibTree = null;
        try {
            OleHoldings eHoldings = workEInstanceOlemlForm.getSelectedEHoldings();
            getOleEResourceSearchService().getEResourcesFields(editorForm.geteResourceId(), eHoldings, workEInstanceOlemlForm);
            getOleEResourceSearchService().getEResourcesLicenseFields(editorForm.geteResourceId(), workEInstanceOlemlForm);
            String content = getInstanceEditorFormDataHandler().buildHoldingContent(eHoldings);
            Holdings eHoldingsDoc = new EHoldings();
            eHoldingsDoc.setCategory(DocCategory.WORK.getCode());
            eHoldingsDoc.setType(DocType.EHOLDINGS.getCode());
            eHoldingsDoc.setFormat(DocFormat.OLEML.getCode());
            String canUpdateStaffOnlyFlag = "false";
            if (editorForm.isStaffOnlyFlagInGlobalEdit()) {
                canUpdateStaffOnlyFlag = "true";
                editorForm.setStaffOnlyFlagForHoldings(true);
                eHoldingsDoc.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
            }
            else if (!editorForm.isStaffOnlyFlagInGlobalEdit()) {
                canUpdateStaffOnlyFlag = "true";
                editorForm.setStaffOnlyFlagForHoldings(false);
                eHoldingsDoc.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
            }
            eHoldingsDoc.setStaffOnly(editorForm.isStaffOnlyFlagForHoldings());
            eHoldingsDoc.setCreatedBy(editorForm.getCreatedBy());
            eHoldingsDoc.setCreatedOn(editorForm.getCreatedDate());
            eHoldingsDoc.setUpdatedBy(user);
            eHoldingsDoc.setUpdatedOn(dateStr);
            Bib bib = new BibMarc();
            bib.setId(workEInstanceOlemlForm.getBibId());
            eHoldingsDoc.setBib(bib);
            eHoldingsDoc.setContent(content);
            getDocstoreClientLocator().getDocstoreClient().bulkUpdateHoldings(eHoldingsDoc,ids,canUpdateStaffOnlyFlag);
            editorForm.setDocId(eHoldingsDoc.getId());
           // processResponse(eHoldingsDoc.getId(), eHoldingsDoc.getBib().getId(), editorForm.getTokenId());
            workEInstanceOlemlForm.getSelectedEHoldings().setHoldingsIdentifier(eHoldingsDoc.getId());
            HoldingsTree holdingsTree = new HoldingsTree();
            holdingsTree.setHoldings(eHoldingsDoc);
            List<HoldingsTree> holdingsTreeList = new ArrayList<>();
            holdingsTreeList.add(holdingsTree);
            //bibTree.getHoldingsTrees().addAll(holdingsTreeList);
            editorStatusMessage = "record.create.message";
        }
        catch (DocstoreException e) {
            LOG.error(e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
            return workEInstanceOlemlForm;
        }
        catch (Exception e) {
            LOG.error("Exception :", e);
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, "docstore.response", e.getMessage());
            return workEInstanceOlemlForm;

        }


        try {
            getEInstanceFormDataHandler().setLocationDetails(workEInstanceOlemlForm);
            String eResId = workEInstanceOlemlForm.geteResourceId();
            editorForm.seteResourceId(null);
            workEInstanceOlemlForm.setViewId("WorkEInstanceViewPage");

            GlobalVariables.getMessageMap().getInfoMessages().clear();
        }
        catch (DocstoreException e) {
            LOG.error(e);
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
            return workEInstanceOlemlForm;
        }
        catch (Exception e) {
            LOG.error("Exception :", e);
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, "docstore.response", e.getMessage());
            return workEInstanceOlemlForm;
        }
       /* WorkEInstanceOlemlForm eInstanceOlemlForm = (WorkEInstanceOlemlForm) loadDocument(editorForm);
        GlobalVariables.getMessageMap().getInfoMessages().clear();
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, editorStatusMessage);*/

        return workEInstanceOlemlForm;
    }

}
