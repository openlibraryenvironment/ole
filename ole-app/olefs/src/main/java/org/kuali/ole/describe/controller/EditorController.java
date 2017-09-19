package org.kuali.ole.describe.controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronLostBarcode;
import org.kuali.ole.describe.bo.EditorFormDataHandler;
import org.kuali.ole.describe.bo.GlobalEditEHoldingsFieldsFlagBO;
import org.kuali.ole.describe.bo.GlobalEditHoldingsFieldsFlagBO;
import org.kuali.ole.describe.bo.GlobalEditItemFieldsFlagBO;
import org.kuali.ole.describe.bo.marc.structuralfields.ControlFields;
import org.kuali.ole.describe.bo.marc.structuralfields.LeaderField;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield006.ControlField006;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield006.ControlField006Text;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield007.ControlField007;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield007.ControlField007Text;
import org.kuali.ole.describe.bo.marc.structuralfields.controlfield008.ControlField008;
import org.kuali.ole.describe.form.EditorForm;
import org.kuali.ole.describe.form.WorkBibMarcForm;
import org.kuali.ole.describe.form.WorkEInstanceOlemlForm;
import org.kuali.ole.describe.form.WorkInstanceOlemlForm;
import org.kuali.ole.describe.service.DiscoveryHelperService;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.config.DocFieldConfig;
import org.kuali.ole.docstore.common.document.config.DocFormatConfig;
import org.kuali.ole.docstore.common.document.config.DocTypeConfig;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.search.SearchField;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.docstore.common.util.DataSource;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PP7788
 * Date: 12/6/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/editorcontroller")

public class EditorController extends UifControllerBase {

    private DocumentEditor editor;
    private EditorFormDataHandler editorFormDataHandler;
    private DiscoveryHelperService discoveryHelperService;
    private boolean isFormInitialized = false;
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    private static final Logger LOG = Logger.getLogger(EditorController.class);

    private boolean canDeleteItem(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.INSTANCE_EDITOR_DELETE_ITEM);
    }

    private boolean canDeleteInstance(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.INSTANCE_EDITOR_DELETE_INSTANCE);
    }

    private boolean canAddItem(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.INSTANCE_EDITOR_ADD_ITEM);
    }

    private boolean canDeleteEInstance(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.INSTANCE_EDITOR_DELETE_EINSTANCE);
    }

    private boolean canCopyBib(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.MARC_EDITOR_COPY_BIB);
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        UifFormBase uifFormBase = null;
        uifFormBase = new EditorForm();
        return uifFormBase;
    }

    @RequestMapping(params = "methodToCall=copy")
    public ModelAndView copy(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        editorForm.setCopyFlag(true);
        if (null != editorForm.getDocumentForm()) {
            editorForm.getDocumentForm().setCopyFlag(true);
        }
        ModelAndView modelAndView = null;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");

        ((EditorForm) form).setDocCategory(docCategory);
        ((EditorForm) form).setDocType(docType);
        ((EditorForm) form).setDocFormat(docFormat);
        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);
        editorForm = documentEditor.copy((EditorForm) form);

        if(docType.equalsIgnoreCase(DocType.BIB.getCode())){
            // Build or update left pane data (tree structure of documents)
            getEditorFormDataHandler().buildLeftPaneData((EditorForm) form);
        }
        modelAndView = getUIFModelAndView(editorForm);
        return modelAndView;
    }

    @RequestMapping(params = "methodToCall=copyInstance")
    public ModelAndView copyInstance(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        ModelAndView modelAndView = null;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        ((EditorForm) form).setDocCategory(docCategory);
        ((EditorForm) form).setDocType(docType);
        ((EditorForm) form).setDocFormat(docFormat);
        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);
        editorForm = documentEditor.copy((EditorForm) form);
        modelAndView = getUIFModelAndView(editorForm, "WorkHoldingsViewPage");
        return modelAndView;
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {

        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        editorForm.setMainSerialReceivingHistoryList(null);
        editorForm.setSupplementSerialReceivingHistoryList(null);
        editorForm.setIndexSerialReceivingHistoryList(null);
        editorForm.setBibFlag(false);
        editorForm.setHoldingFlag(false);
        editorForm.setItemFlag(false);
        editorForm.seteHoldingsFlag(false);

        ModelAndView modelAndView = null;
        // get the document details from request and set them in the form.
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        String docId = request.getParameter("docId");
        String bibId = request.getParameter("bibId");
        String instanceId = request.getParameter("instanceId");
        String editable = request.getParameter("editable");
        String callNumberFlag = request.getParameter("isCallNumberFlag");


        if (null == editable || editable.length() > 0) {
            editable = "true";
        }

        if (docFormat == null || docFormat.length() == 0) {
            docFormat = ((EditorForm) form).getDocFormat();
        }
        if (docId == null || docId.length() == 0) {
            docId = ((EditorForm) form).getDocId();
        }

        if (DocType.ITEM.getCode().equalsIgnoreCase(docType)) {
            ((EditorForm) form).setItemLocalIdentifier(docId);
        }

        /* if (docType.equalsIgnoreCase("holdings")) {
            ((EditorForm) form).setHoldingLocalIdentifier(docId);
        }*/

        // modified code for Global Edit - start

        if (Boolean.parseBoolean(editorForm.getGlobalEditFlag())) {
            prepareGlobalEditFields(editorForm, docType);
        }

        // modified code for Global Edit - end

        ((EditorForm) form).setEditable(editable);
        ((EditorForm) form).setDocCategory(docCategory);
        ((EditorForm) form).setDocType(docType);
        ((EditorForm) form).setDocFormat(docFormat);

        if (DocFormat.MARC.getCode().equals(docFormat) && ((EditorForm) form).getViewId().equalsIgnoreCase(OLEConstants.EDITOR_WORKFORM_VIEW)) {
            editorForm.setDocumentForm(new WorkBibMarcForm());
            modelAndView = getUIFModelAndView(form, OLEConstants.EDITOR_WORKFORM_VIEW_PAGE);
        } else if (DocFormat.MARC.getCode().equals(docFormat)) {
            editorForm.setDocumentForm(new WorkBibMarcForm());
            modelAndView = getUIFModelAndView(form, OLEConstants.WORK_BIB_EDITOR_VIEW_PAGE);
        } else if (DocType.INSTANCE.getCode().equals(docType) || DocType.HOLDINGS.getCode().equals(docType)) {
            editorForm.setDocumentForm(new WorkInstanceOlemlForm());
            modelAndView = getUIFModelAndView(form, OLEConstants.WORK_HOLDINGS_VIEW_PAGE);
        } else if (DocType.EINSTANCE.getCode().equals(docType) || DocType.EHOLDINGS.getCode().equals(docType)) {
            editorForm.setDocumentForm(new WorkEInstanceOlemlForm());
            if (Boolean.parseBoolean(editorForm.getGlobalEditFlag())) {
                OleHoldings eHoldings = new OleHoldings();
                eHoldings.setStatusDate(new java.sql.Date(System.currentTimeMillis()).toString());
                eHoldings.setEResourceId(editorForm.geteResourceId());
                //getOleEResourceSearchService().getAccessLocationFromEInstance(eHoldings, workEInstanceOlemlForm);
                if (eHoldings.getExtentOfOwnership() != null && eHoldings.getExtentOfOwnership().size() > 0
                        && eHoldings.getExtentOfOwnership().get(0).getCoverages() != null
                        && eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().size() == 0) {
                    eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().add(new Coverage());
                }
                else if (eHoldings.getExtentOfOwnership() == null || eHoldings.getExtentOfOwnership().size() == 0) {
                    eHoldings.getExtentOfOwnership().add(new ExtentOfOwnership());
                    eHoldings.getExtentOfOwnership().get(0).setCoverages(new Coverages());
                    eHoldings.getExtentOfOwnership().get(0).setPerpetualAccesses(new PerpetualAccesses());
                }
                if (eHoldings.getExtentOfOwnership() != null && eHoldings.getExtentOfOwnership().size() > 0
                        && eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses() != null
                        && eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().size() == 0) {
                    eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().add(new Coverage());
                    eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().add(new PerpetualAccess());
                }
                if (eHoldings.getNote() != null && eHoldings.getNote().size() == 0) {
                    eHoldings.getNote().add(new Note());
                }
                if (eHoldings.getDonorInfo() != null && eHoldings.getDonorInfo().size() == 0) {
                    eHoldings.getDonorInfo().add(new DonorInfo());
                }
                if(eHoldings.getLink().size() == 0){
                    eHoldings.getLink().add(new Link());
                }
                ((WorkEInstanceOlemlForm)editorForm.getDocumentForm()).setSelectedEHoldings(eHoldings);
            }
            modelAndView = getUIFModelAndView(form, OLEConstants.WORK_EINSTANCE_VIEW_PAGE);
        } else if (DocType.ITEM.getCode().equals(docType)) {
            editorForm.setDocumentForm(new WorkInstanceOlemlForm());
            modelAndView = getUIFModelAndView(form, OLEConstants.WORK_ITEM_VIEW_PAGE);
        }
        return modelAndView;
    }


    /**
     * Load a (new or existing) document in the editor.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=load")
    public ModelAndView load(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {

        String tokenId = request.getParameter("tokenId");
        EditorForm editorForm = (EditorForm) form;
        if (tokenId != null) {
            editorForm.setTokenId(tokenId);
        }
        if (editorForm.getBibId() == null) {
            request.getSession().removeAttribute("treeDocumentList");
        }
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        editorForm.setRecordOpenedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        editorForm.setMainSerialReceivingHistoryList(null);
        editorForm.setSupplementSerialReceivingHistoryList(null);
        editorForm.setIndexSerialReceivingHistoryList(null);
        editorForm.setShowTime(true);
        // get the document details from request and set them in the form.
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        String docId = request.getParameter("docId");
        String bibId = request.getParameter("bibId");
        String instanceId = request.getParameter("instanceId");
        String editable = request.getParameter("editable");
        String callNumberFlag = request.getParameter("isCallNumberFlag");
        String eResourceId = request.getParameter("eResourceId");
        String holdingsId = request.getParameter("holdingsId");
        String fromSearch = request.getParameter("fromSearch");

        //Verifying editable at form object
        if ((null == editable) || (editable.length() == 0)) {
            editable = ((EditorForm) form).getEditable();
        }

        //Default value for editable field if it is null
        if (null == editable || editable.length() == 0) {
            editable = "true";
        }

        if (StringUtils.isBlank(docId)) {
            editorForm.setNewDocument(true);
        }

        /*if (docType.equalsIgnoreCase("item")) {
            ((EditorForm) form).setItemLocalIdentifier(docId);
        }*/

        if (docType.equalsIgnoreCase("holdings")) {
            if(StringUtils.isNotEmpty(docId) && !DocumentUniqueIDPrefix.hasPrefix(docId)){
                docId="who-" +docId;
            }
            ((EditorForm) form).setInstanceId(docId);
        }

        if (docType.equalsIgnoreCase("item")) {
            if(StringUtils.isNotEmpty(docId) && !DocumentUniqueIDPrefix.hasPrefix(docId)){
                docId="wio-" +docId;
            }
        }
        if (docType.equalsIgnoreCase("instance")) {
            ((EditorForm) form).setInstanceId(docId);
        }
        ((EditorForm) form).setEditable(editable);
        ((EditorForm) form).setDocCategory(docCategory);
        ((EditorForm) form).setDocType(docType);
        ((EditorForm) form).setDocFormat(docFormat);
        ((EditorForm) form).setDocId(docId);
        ((EditorForm) form).setBibId(bibId);
        ((EditorForm) form).setCallNumberFlag(callNumberFlag);
        ((EditorForm) form).setFromSearch(Boolean.valueOf(fromSearch));
        ((EditorForm) form).setHideFooter(true);
        boolean canDelete = canDeleteItem(GlobalVariables.getUserSession().getPrincipalId()) && canDeleteInstance(GlobalVariables.getUserSession().getPrincipalId());
        ((EditorForm) form).setCanDelete(canDelete);
        boolean canAdd = canAddItem(GlobalVariables.getUserSession().getPrincipalId());
        ((EditorForm) form).setCanAdd(canAdd);
        //if the user doesn't have permission to add or edit item, editable will be set as false
        if (!canAdd) {
            ((EditorForm) form).setEditable(String.valueOf(canAdd));
        }
        boolean canDeleteEInstance = canDeleteEInstance(GlobalVariables.getUserSession().getPrincipalId());
        ((EditorForm) form).setCanDeleteEInstance(canDeleteEInstance);


        boolean canCopyBib = canCopyBib(GlobalVariables.getUserSession().getPrincipalId());
        ((EditorForm) form).setCanCopyBib(canCopyBib);

        if (eResourceId != null) {
            ((EditorForm) form).seteResourceId(eResourceId);
        }
        if (holdingsId != null) {
            ((EditorForm) form).setHoldingsId(holdingsId);
        }
        // Identify the document editor to be used for the requested document.
        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        // TODO: Need to save the editorForm in the session.
        // Get documentList from session.
        /*List<WorkBibDocument> workBibDocumentList = (List) request.getSession().getAttribute("treeDocumentList");
        if (null != workBibDocumentList) {
            ((EditorForm) form).setWorkBibDocumentList(workBibDocumentList);
        }*/

        List<BibTree> bibTreeList = (List) request.getSession().getAttribute("treeDocumentList");
        if (null != bibTreeList) {
            ((EditorForm) form).setBibTreeList(bibTreeList);
        }
        if ("bibliographic".equals(editorForm.getDocType())) {
            editorForm.setBibFlag(false);
        }
        if ("holdings".equals(editorForm.getDocType())) {
            editorForm.setHoldingFlag(false);
        }
        if ("item".equals(editorForm.getDocType())) {
            editorForm.setItemFlag(false);
        }
        if ("eHoldings".equals(editorForm.getDocType())) {
            editorForm.seteHoldingsFlag(false);
        }

        EditorForm documentForm = null;
        //get request object fromm session and build new work bib marc record.
        if (request.getSession() != null && request.getSession().getAttribute("bibTree") != null &&
                "true".equalsIgnoreCase(request.getParameter("loadFromSession"))) {
            BibTree bibTree = (BibTree) request.getSession().getAttribute("bibTree");
            if (docId != null && docId.length() > 0 && !docId.equalsIgnoreCase("null")) {
                ((EditorForm) form).setShowLeftPane(false);
            } else {
                ((EditorForm) form).setShowLeftPane(false);
                ((EditorForm) form).setShowEditorFooter(false);
            }
            documentForm = documentEditor.createNewRecord((EditorForm) form, bibTree);
        } else {
            // Send the input through one (request)form and get the output through another (response) form.
            documentForm = documentEditor.loadDocument((EditorForm) form);
            if (documentForm instanceof WorkInstanceOlemlForm) {
                Item item = ((WorkInstanceOlemlForm) documentForm).getSelectedItem();
                if (item.getCurrentBorrower() != null && !item.getCurrentBorrower().isEmpty()) {
                    OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class, item.getCurrentBorrower());
                    ((WorkInstanceOlemlForm) documentForm).setCurrentBarcode(olePatronDocument.getBarcode());
                }
                if (item.getProxyBorrower() != null && !item.getProxyBorrower().isEmpty()) {
                    OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class, item.getProxyBorrower());
                    ((WorkInstanceOlemlForm) documentForm).setProxyBarcode(olePatronDocument.getBarcode());
                }
                if(StringUtils.isNotBlank(item.getLastBorrower())) {
                    OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class, item.getLastBorrower());
                    ((WorkInstanceOlemlForm) documentForm).setLastBarcode(olePatronDocument.getBarcode());
                }
                setClaimsAndDamagedPatronBarcode(item);
                setMissingPieceItemRecord(item);
            }
        }

        // Set the output (response) form containing document info into the current form.
        ((EditorForm) form).setDocumentForm(documentForm);
        // Set documentlist to session.
        request.getSession().setAttribute("treeDocumentList", ((EditorForm) form).getDocumentForm().getBibTreeList());

        // Build or update left pane data (tree structure of documents)
        getEditorFormDataHandler().buildLeftPaneData((EditorForm) form);

        // Return the next view to be shown to user.
        ModelAndView modelAndView = getUIFModelAndView(form, documentForm.getViewId());
        return modelAndView;
    }

    private void setClaimsAndDamagedPatronBarcode(Item item) {
        if(CollectionUtils.isNotEmpty(item.getItemClaimsReturnedRecords())){
            for(int index=0 ; index < item.getItemClaimsReturnedRecords().size() ; index++){
                if(StringUtils.isNotBlank(item.getItemClaimsReturnedRecords().get(index).getClaimsReturnedPatronId()) && StringUtils.isBlank(item.getItemClaimsReturnedRecords().get(index).getClaimsReturnedPatronBarcode())){
                    OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class,item.getItemClaimsReturnedRecords().get(index).getClaimsReturnedPatronId());
                    if(olePatronDocument != null){
                        item.getItemClaimsReturnedRecords().get(index).setClaimsReturnedPatronBarcode(olePatronDocument.getBarcode());
                    }
                } else if(StringUtils.isNotBlank(item.getItemClaimsReturnedRecords().get(index).getClaimsReturnedPatronBarcode()) && StringUtils.isBlank(item.getItemClaimsReturnedRecords().get(index).getClaimsReturnedPatronId())) {
                    Map criteria = new HashMap();
                    criteria.put("barcode",item.getItemClaimsReturnedRecords().get(index).getClaimsReturnedPatronBarcode());
                    List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>)KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,criteria);
                    if(olePatronDocumentList != null && olePatronDocumentList.size() > 0) {
                        for(OlePatronDocument olePatronDocument : olePatronDocumentList) {
                            item.getItemClaimsReturnedRecords().get(index).setClaimsReturnedPatronId(olePatronDocument.getOlePatronId());
                        }
                    } else {
                        criteria = new HashMap();
                        criteria.put("invalidOrLostBarcodeNumber", item.getItemClaimsReturnedRecords().get(index).getClaimsReturnedPatronBarcode());
                        List<OlePatronLostBarcode> olePatronLostBarcodeList = (List<OlePatronLostBarcode>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronLostBarcode.class, criteria);
                        if(CollectionUtils.isNotEmpty(olePatronLostBarcodeList)){
                            for(OlePatronLostBarcode olePatronLostBarcode : olePatronLostBarcodeList) {
                                item.getItemClaimsReturnedRecords().get(index).setClaimsReturnedPatronId(olePatronLostBarcode.getOlePatronId());
                            }
                        }
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(item.getItemDamagedRecords())){
            for(int index=0 ; index < item.getItemDamagedRecords().size() ; index++){
                if(StringUtils.isNotBlank(item.getItemDamagedRecords().get(index).getDamagedPatronId()) && StringUtils.isBlank(item.getItemDamagedRecords().get(index).getPatronBarcode())){
                    OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class,item.getItemDamagedRecords().get(index).getDamagedPatronId());
                    if(olePatronDocument != null){
                        item.getItemDamagedRecords().get(index).setPatronBarcode(olePatronDocument.getBarcode());
                    }
                } else if(StringUtils.isNotBlank(item.getItemDamagedRecords().get(index).getPatronBarcode()) && StringUtils.isBlank(item.getItemDamagedRecords().get(index).getDamagedPatronId())) {
                    Map criteria = new HashMap();
                    criteria.put("barcode",item.getItemDamagedRecords().get(index).getPatronBarcode());
                    List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>)KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,criteria);
                    if(olePatronDocumentList != null && olePatronDocumentList.size() > 0) {
                        for(OlePatronDocument olePatronDocument : olePatronDocumentList) {
                            item.getItemDamagedRecords().get(index).setDamagedPatronId(olePatronDocument.getOlePatronId());
                        }
                    } else {
                        criteria = new HashMap();
                        criteria.put("invalidOrLostBarcodeNumber", item.getItemDamagedRecords().get(index).getPatronBarcode());
                        List<OlePatronLostBarcode> olePatronLostBarcodeList = (List<OlePatronLostBarcode>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronLostBarcode.class, criteria);
                        if(CollectionUtils.isNotEmpty(olePatronLostBarcodeList)){
                            for(OlePatronLostBarcode olePatronLostBarcode : olePatronLostBarcodeList) {
                                item.getItemDamagedRecords().get(index).setDamagedPatronId(olePatronLostBarcode.getOlePatronId());
                            }
                        }
                    }
                }
            }
        }
    }

    public void setMissingPieceItemRecord (Item item){
        if(item.getMissingPieceItemRecordList() != null && !item.getMissingPieceItemRecordList().isEmpty()){
            for(int index=0 ; index < item.getMissingPieceItemRecordList().size() ; index++){
                if (StringUtils.isNotBlank(item.getMissingPieceItemRecordList().get(index).getPatronId()) && StringUtils.isBlank(item.getMissingPieceItemRecordList().get(index).getPatronBarcode())) {
                    OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OlePatronDocument.class, item.getMissingPieceItemRecordList().get(index).getPatronId());
                    if(olePatronDocument != null ){
                        item.getMissingPieceItemRecordList().get(index).setPatronBarcode(olePatronDocument.getBarcode());
                    }
                } else if (StringUtils.isNotBlank(item.getMissingPieceItemRecordList().get(index).getPatronBarcode()) && StringUtils.isBlank(item.getMissingPieceItemRecordList().get(index).getPatronId())) {
                    Map map = new HashMap();
                    map.put("barcode" ,item.getMissingPieceItemRecordList().get(index).getPatronBarcode());
                    List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>)KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class ,map );
                    if (CollectionUtils.isNotEmpty(olePatronDocumentList)) {
                        for(OlePatronDocument olePatronDocument : olePatronDocumentList){
                            item.getMissingPieceItemRecordList().get(index).setPatronId(olePatronDocument.getOlePatronId());
                        }
                    } else {
                        Map criteria1 = new HashMap();
                        criteria1.put("invalidOrLostBarcodeNumber" ,item.getMissingPieceItemRecordList().get(index).getPatronBarcode());
                        List<OlePatronLostBarcode> olePatronLostBarcodeList = (List<OlePatronLostBarcode>)KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronLostBarcode.class , criteria1);
                        if(olePatronLostBarcodeList != null && olePatronLostBarcodeList.size()>0){
                            for(OlePatronLostBarcode olePatronLostBarcode : olePatronLostBarcodeList){
                                item.getMissingPieceItemRecordList().get(index).setPatronId(olePatronLostBarcode.getOlePatronId());
                            }
                        }
                    }
                }
            }
        }
    }

    @RequestMapping(params = "methodToCall=updateLeftPane")
    public ModelAndView updateLeftPane(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        EditorForm editorForm = (EditorForm) form;
        String hiddenVal = editorForm.getHdnUuid();
        int hdnIndex = editorForm.getHdnIndex();
        DiscoveryHelperService discoveryHelperService = getDiscoveryHelperService();
        List responseFromSOLR = discoveryHelperService.getResponseFromSOLR("id", hiddenVal);
        String docType = null;
        for (Iterator iterator = responseFromSOLR.iterator(); iterator.hasNext(); ) {
            Map map = (Map) iterator.next();
            if (map.containsKey(OLEConstants.DOC_TYPE)) {
                String list = (String) map.get(OLEConstants.DOC_TYPE);
                docType = list;
                break;
            }
        }
        if ((docType.toString().equals(OLEConstants.ITEM_DOC_TYPE))) {
            WorkInstanceOlemlForm instanceEditorForm = new WorkInstanceOlemlForm();
            //instanceEditorForm.setInstance(editorForm.getInstance());
            //instanceEditorForm.setUuid(editorForm.getInstance().getInstanceIdentifier());
            //editor = new WorkInstanceOlemlEditor();
            //instanceEditorForm.setSelectedItem(editorForm.getInstance().getItems().getItem().get(hdnIndex));
            //return getUIFModelAndView(editorForm, "WorkItemViewPage");
            //ModelAndView modelAndView = editor.load(form, result, request, response);
            //return modelAndView;
            return null;
        } else {
            //editorForm.setSelectedHolding(editorForm.getInstance().getOleHoldings());
            return getUIFModelAndView(editorForm, "WorkHoldingsViewPage");
        }
    }

    public DiscoveryHelperService getDiscoveryHelperService() {
        if (null == discoveryHelperService) {
            return new DiscoveryHelperService();
        }
        return discoveryHelperService;
    }

    public EditorFormDataHandler getEditorFormDataHandler() {
        if (null == editorFormDataHandler) {
            editorFormDataHandler = new EditorFormDataHandler();
        }
        return editorFormDataHandler;
    }

    @RequestMapping(params = "methodToCall=EditNewInstance")
    public ModelAndView EditNewInstance(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {

        EditorForm editorForm = (EditorForm) form;
        // get request object from session
        BibTree bibTree = (BibTree) request.getSession().getAttribute("bibTree");
        // Get documentList from session.
        /*List<WorkBibDocument> workBibDocumentList = (List) request.getSession().getAttribute("treeDocumentList");
        if (null != workBibDocumentList) {
            ((EditorForm) form).setWorkBibDocumentList(workBibDocumentList);
        }*/
        if (editorForm.getDocumentForm().getViewId().equalsIgnoreCase("WorkHoldingsViewPage")) {
            // call instance editor
            DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                    .getDocumentEditor("work", "item", "oleml");
            // set item record to display on item tab
            editorForm = documentEditor.createNewRecord((EditorForm) form, bibTree);
            editorForm.setNeedToCreateInstance(false);

        } else {
            // call bib editor to store bib data
            DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                    .getDocumentEditor("work", "bibliographic", "marc");
            // update request object with modified marc data.
            EditorForm marcEditorForm = documentEditor.editNewRecord(editorForm, bibTree);

            // validate bib before going to instance
            if (!marcEditorForm.isValidInput()) {
                return getUIFModelAndView(form, marcEditorForm.getViewId());
            }

            // call instance editor and set holding record to display on holding tab
            documentEditor = DocumentEditorFactory.getInstance().getDocumentEditor("work", "holdings", "oleml");
            ((EditorForm) form).setDocCategory("work");
            ((EditorForm) form).setDocType("holdings");
            ((EditorForm) form).setDocFormat("oleml");
            editorForm = documentEditor.createNewRecord((EditorForm) form, bibTree);
        }

        // Set the output (response) form containing document info into the current form.
        ((EditorForm) form).setDocumentForm(editorForm);
        // Set documentlist to session.
        //request.getSession().setAttribute("treeDocumentList", ((EditorForm) form).getWorkBibDocumentList());

        // Build or update left pane data (tree structure of documents)
        getEditorFormDataHandler().buildLeftPaneData((EditorForm) form);

        // Return the next view to be shown to user.
        ModelAndView modelAndView = getUIFModelAndView(form, editorForm.getViewId());
        return modelAndView;

    }

    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        if ("bibliographic".equals(((EditorForm) form).getDocType())) {
            ((EditorForm) form).setBibFlag(false);
        }
        if ("holdings".equals(((EditorForm) form).getDocType())) {
            ((EditorForm) form).setHoldingFlag(false);
        }
        if ("item".equals(((EditorForm) form).getDocType())) {
            ((EditorForm) form).setItemFlag(false);
        }
        if ("eHoldings".equals(((EditorForm) form).getDocType())) {
            ((EditorForm) form).seteHoldingsFlag(false);
        }
        if (request.getSession() != null && request.getSession().getAttribute("bibTree") != null) {
            DocumentEditor documentEditor = null;
            if (((EditorForm) form).getDocId() == null || "null".equalsIgnoreCase(((EditorForm) form).getDocId())) {
                documentEditor = DocumentEditorFactory.getInstance()
                        .getDocumentEditor("work", "item", "oleml");
            } else {
                documentEditor = DocumentEditorFactory.getInstance()
                        .getDocumentEditor("work", "bibliographic", "marc");
            }

            BibTree bibTree = (BibTree) request.getSession().getAttribute("bibTree");
            EditorForm editorForm = documentEditor.editNewRecord((EditorForm) form, bibTree);

            // validate user entered item data before saving to docstore.
            if (!editorForm.isValidInput()) {
                return getUIFModelAndView(form, editorForm.getViewId());
            }

            String responseFromDocstore = documentEditor.saveDocument(bibTree, (EditorForm) form);
            if (StringUtils.isNotEmpty(responseFromDocstore) && responseFromDocstore.contains("success")) {
                request.getSession().setAttribute("responseBibTree", bibTree);

                String url = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
                url = url + "/portal.do?channelTitle=Import Bib&channelUrl=" + url +
                        "/ole-kr-krad/importBibController?viewId=ImportBibView&methodToCall=viewRecordNext";
                return performRedirect(editorForm, url);
            }
        } else {
            if (!isFormInitialized) {
                super.start(form, result, request, response);
                isFormInitialized = true;
            }

            // get the document details from request and set them in the form.
            String docCategory = request.getParameter("docCategory");
            String docType = request.getParameter("docType");
            String docFormat = request.getParameter("docFormat");
            String docId = request.getParameter("docId");
            String bibId = request.getParameter("bibId");
            String instanceId = request.getParameter("instanceId");

            String editable = request.getParameter("editable");

            if (docType != null && docType.equalsIgnoreCase("item")) {
                ((EditorForm) form).setItemLocalIdentifier(docId);
            }

            /* if (docType != null && docType.equalsIgnoreCase("holdings")) {
                ((EditorForm) form).setHoldingLocalIdentifier(docId);
            }*/

            if ((null == editable) || (editable.length() == 0)) {
                editable = "true";
            }
            if (docCategory == null || docCategory.length() == 0) {
                docCategory = ((EditorForm) form).getDocCategory();
            }
            if (docType == null || docType.length() == 0) {
                docType = ((EditorForm) form).getDocType();
            }

            if (docFormat == null || docFormat.length() == 0) {
                docFormat = ((EditorForm) form).getDocFormat();
            }
            if (docId == null || docId.length() == 0) {
                docId = ((EditorForm) form).getDocId();
            }

            ((EditorForm) form).setEditable(editable);
            ((EditorForm) form).setDocCategory(docCategory);
            ((EditorForm) form).setDocType(docType);
            ((EditorForm) form).setDocFormat(docFormat);
            ((EditorForm) form).setDocId(docId);
            ((EditorForm) form).setBibId(bibId);
            ((EditorForm) form).setInstanceId(instanceId);

            // Identify the document editor to be used for the requested document.
            DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                    .getDocumentEditor(docCategory, docType, docFormat);

            // TODO: Need to save the editorForm in the session.
            // Get documentList from session.
            /* List<WorkBibDocument> workBibDocumentList = (List) request.getSession().getAttribute("treeDocumentList");
            if (null != workBibDocumentList) {
                ((EditorForm) form).setWorkBibDocumentList(workBibDocumentList);
            }*/

            List<BibTree> bibTreeList = (List) ((EditorForm) form).getDocumentForm().getBibTreeList();
            Connection holdingsConnection = null;
            String dateUpdated = null;
            if(StringUtils.isNotBlank(instanceId)) {
                try {
                    holdingsConnection = getConnection();
                    ResultSet holdingsDateUpdated = holdingsConnection.prepareStatement("SELECT DATE_UPDATED FROM OLE_DS_HOLDINGS_T where HOLDINGS_ID =" + instanceId.substring(4)).executeQuery();
                    while (holdingsDateUpdated.next()) {
                        dateUpdated = holdingsDateUpdated.getString("DATE_UPDATED");
                        LOG.info(dateUpdated);
                    }
                    holdingsConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(StringUtils.isNotBlank(dateUpdated) && StringUtils.isNotBlank(((EditorForm) form).getRecordOpenedTime())) {
                try {
                    DateFormat recordOpendateFrmat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    DateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date recordOpenedDate = recordOpendateFrmat.parse(((EditorForm) form).getRecordOpenedTime());
                    Date holdingsUpdatedDate = dbDateFormat.parse(dateUpdated);
                    if ("holdings".equals(((EditorForm) form).getDocType())|| "item".equals(((EditorForm) form).getDocType())) {
                        if(recordOpenedDate.before(holdingsUpdatedDate)){
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_EDIT_INSTANCE_HOLDINGS_UPDATED);
                            ModelAndView modelAndView = getUIFModelAndView(form, ((EditorForm) form).getDocumentForm().getViewId());
                            return modelAndView;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (null != bibTreeList) {
                ((EditorForm) form).getDocumentForm().setBibTreeList(bibTreeList);
                ((EditorForm) form).setBibTreeList(bibTreeList);
            }
            if("overwrite".equals(((EditorForm) form).getCheckOverwriteFlag())) {
                EditorForm documentForm = documentEditor.saveDocument((EditorForm) form);
                if (documentForm instanceof WorkInstanceOlemlForm) {
                    Item item = ((WorkInstanceOlemlForm) documentForm).getSelectedItem();
                    setClaimsAndDamagedPatronBarcode(item);
                    setMissingPieceItemRecord(item);
                }
                ((EditorForm) form).setDocumentForm(documentForm);
            } else {
                if (documentEditor.isValidUpdate((EditorForm) form) && ((EditorForm) form).getAllowUpdate().equals
                        ("true") || documentEditor.isValidUpdate((EditorForm) form) && ((EditorForm) form).getAllowUpdate().equals
                        ("")) {
                    // Send the input through one (request)form and get the output through another (response) form.
                    EditorForm documentForm = documentEditor.saveDocument((EditorForm) form);
                    if (documentForm instanceof WorkInstanceOlemlForm) {
                        Item item = ((WorkInstanceOlemlForm) documentForm).getSelectedItem();
                        setClaimsAndDamagedPatronBarcode(item);
                        setMissingPieceItemRecord(item);
                    }
                    // Set the output (response) form containing docum ((EditorForm) form).isAllowUpdate()ent info into the current form.
                    ((EditorForm) form).setDocumentForm(documentForm);
                    ((EditorForm) form).setAllowUpdate("true");
                } else {
                    ((EditorForm) form).setAllowUpdate("false");
                    // EditorForm documentForm = documentEditor.loadDocument((EditorForm) form);
                    // ((EditorForm) form).setDocumentForm(documentForm);
                }

            }
            ((EditorForm) form).setCheckOverwriteFlag("");
            // Set documentlist to session.
            request.getSession().setAttribute("treeDocumentList", ((EditorForm) form).getDocumentForm().getBibTreeList());

            // Build or update left pane data (tree structure of documents)
            getEditorFormDataHandler().buildLeftPaneData((EditorForm) form);

        }
        ((EditorForm) form).setDisplayField006(Boolean.FALSE.toString());
        ((EditorForm) form).setDisplayField007(Boolean.FALSE.toString());
        ((EditorForm) form).setDisplayField008(Boolean.FALSE.toString());
        ((EditorForm) form).setBibFlag(false);
        ((EditorForm) form).setHoldingFlag(false);
        ((EditorForm) form).setItemFlag(false);
        ((EditorForm) form).seteHoldingsFlag(false);

        if (StringUtils.isBlank(((EditorForm) form).getDocId()) && GlobalVariables.getMessageMap().getErrorCount() > 0) {
            ((EditorForm) form).setNewDocument(true);
        } else {
            ((EditorForm) form).setRecordOpenedTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
            ((EditorForm) form).setNewDocument(false);
        }

        // Return the next view to be shown to user.
        long endTime = System.currentTimeMillis();
        ((EditorForm) form).setTotalTime(String.valueOf((endTime-startTime)/1000));
        ModelAndView modelAndView = getUIFModelAndView(form, ((EditorForm) form).getDocumentForm().getViewId());
        return modelAndView;
    }

    /**
     * This method will add the controlField 006 record based on the selected Line index and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addControlField006")
    public ModelAndView addControlField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORDeleteFields(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    /**
     * This method will remove the controlField  006 based on the index position and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=removeControlField006")
    public ModelAndView removeControlField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORDeleteFields(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }


    /**
     * This method will add the controlField 007 record based on the selected Line index and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addControlField007")
    public ModelAndView addControlField007(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORDeleteFields(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    /**
     * This method will remove the controlField  007 based on the index position and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=removeControlField007")
    public ModelAndView removeControlField007(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORDeleteFields(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }


    /**
     * This method will add the datField record based on the selected Line index and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addDataField")
    public ModelAndView addDataField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORDeleteFields(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    /**
     * This method will remove the dataField based on the index position and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=removeDataField")
    public ModelAndView removeDataField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORDeleteFields(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

/*
    private UifFormBase buildUifForm (HttpServletRequest httpServletRequest) {
        UifFormBase uifFormBase = null;

        String docType = httpServletRequest.getParameter("docType");
        String docFormat = httpServletRequest.getParameter("docFormat");
        if ("bibliographic".equalsIgnoreCase(docType)) {
            if ("marc".equalsIgnoreCase(docFormat)) {
                LOG.info("Inside EditorController createInitialForm if bibliographic marc");
                editor = new WorkBibMarcEditor();
                uifFormBase = editor.createInitialForm(httpServletRequest);
            } else if ("dublin".equalsIgnoreCase(docFormat)) {
                editor = new WorkBibDublinEditor();
                uifFormBase = editor.createInitialForm(httpServletRequest);
            }
        } else if ("instance".equalsIgnoreCase(docType)) {
            if ("oleml".equalsIgnoreCase(docFormat)) {
                editor = new WorkInstanceOlemlEditor();
                uifFormBase = editor.createInitialForm(httpServletRequest);
            }
        } else if ("holdings".equalsIgnoreCase(docType) || "item".equalsIgnoreCase(docType)) {
            if ("oleml".equalsIgnoreCase(docFormat)) {
                editor = new WorkInstanceOlemlEditor();
                uifFormBase = editor.createInitialForm(httpServletRequest);
            }
        }
        return uifFormBase;

    }
*/

    /**
     * DeleteVerify a  document in the editor.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=deleteVerify")
    public ModelAndView deleteVerify(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = null;
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }

        // get the document details from request and set them in the form.
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        String docId = request.getParameter("docId");
        ((EditorForm) form).setDocCategory(docCategory);
        ((EditorForm) form).setDocType(docType);
        ((EditorForm) form).setDocFormat(docFormat);
        ((EditorForm) form).setDocId(docId);
        ((EditorForm) form).setMessage("");
//        ((EditorForm) form).setEditable("false");
        ((EditorForm) form).setHideFooter(false);

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance().getDocumentEditor(docCategory, docType, docFormat);
        EditorForm documentForm = documentEditor.deleteVerify((EditorForm) form);
        LOG.info("view id-->" + documentForm.getViewId());
        //List<String> uuidList  = new ArrayList<String>();
        EditorForm editorForm = (EditorForm) form;
        //uuidList.add(docId);

/*        DocumentSelectionTree dst = new DocumentSelectionTree();
        LOG.info("get uud list from form-->"+editorForm.getUuidList());
        Node<DocumentTreeNode, String> rootNode = dst.add(editorForm.getUuidList());

         editorForm.getDocTree().setRootElement(rootNode);*/

        return getUIFModelAndView(editorForm, documentForm.getViewId());
/*
        System.out.println("delete verify method is executed-----1");

        ((EditorForm) form).setDocCategory(docCategory);
        ((EditorForm) form).setDocType(docType);
        ((EditorForm) form).setDocFormat(docFormat);
        ((EditorForm) form).setDocId(docId);

        // Identify the document editor to be used for the requested document.
        DocumentEditor documentEditor = DocumentEditorFactory.getInstance().getDocumentEditor(docCategory, docType, docFormat);
        System.out.println("delete verify method is executed-----2");
        //EditorForm documentForm = null;
        EditorForm documentForm = documentEditor.deleteVerify((EditorForm) form);
        ((EditorForm) form).setDocumentForm(documentForm);

        try {
            documentForm = documentEditor.deleteVerify((EditorForm) form);
            modelAndView =getUIFModelAndView(form,documentForm.getViewId());
            System.out.println("delete verify method is executed-----3");
        }catch (Exception e) {
            modelAndView =getUIFModelAndView(form,"WorkBibEditorViewPage");
            System.out.println("delete verify method is executed-----4");
        }
        System.out.println("delete verify method is executed-----5");
        return modelAndView;
*/
    }

    /**
     * Delete a  document in the editor.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=delete")
    public ModelAndView delete(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = null;
        EditorForm editorForm = (EditorForm) form;
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }

        // get the document details from request and set them in the form.
        String docCategory = ((EditorForm) form).getDocCategory();
        String docType = ((EditorForm) form).getDocType();
        String docFormat = ((EditorForm) form).getDocFormat();
        String docId = ((EditorForm) form).getDocId();


        LOG.info("docCategory-->" + docCategory);
        LOG.info("docType-->" + docType);
        LOG.info("docFormat-->" + docFormat);
        LOG.info("uuid-->" + docId);


/*        ((EditorForm) form).setDocCategory(docCategory);
        ((EditorForm) form).setDocType(docType);
        ((EditorForm) form).setDocFormat(docFormat);
        ((EditorForm) form).setUuid(docId);*/

        // Identify the document editor to be used for the requested document.
        DocumentEditor documentEditor = DocumentEditorFactory.getInstance().getDocumentEditor(docCategory, docType, docFormat);

        EditorForm documentForm = documentEditor.delete((EditorForm) form);

        /*       //boolean hasLinks = delete();
        try {

            modelAndView =getUIFModelAndView(form,"deleteConfirmation");
        } catch (Exception e) {
            modelAndView =getUIFModelAndView(form,"");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
        // Send the input through one (request)form and get the output through another (response) form.

        // Build or update left pane data (tree structure of documents)
        getEditorFormDataHandler().buildLeftPaneData((EditorForm) form);

        // Redirect to bib page after deleting eholdings.
        //Commented the code for the jira OLE-6862
        /*if (documentForm.isCanDeleteEHoldings() && docType.equalsIgnoreCase(DocType.EHOLDINGS.getCode())) {
            String url = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
            url = url + "/portal.do?channelTitle=Editor&channelUrl=" + url +
                    "/ole-kr-krad/editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=marc&bibId=&editable=true&docId=" + editorForm.getBibId();
            return performRedirect(editorForm, url);
        }*/

        return getUIFModelAndView(editorForm, documentForm.getViewId());
    }

    /**
     * This method will add the ExtentOfOwnership record based on the selected Line index and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addExtentOfOwnership")
    public ModelAndView addExtentOfOwnership(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORRemoveExtentOfOwnership(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    /**
     * This method will remove the ExtentOfOwnership based on the index position and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=removeExtentOfOwnership")
    public ModelAndView removeExtentOfOwnership(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORRemoveExtentOfOwnership(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    /**
     * This method will add the EOWHoldingNotes record based on the selected Line index and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addEOWHoldingNotes")
    public ModelAndView addEOWHoldingNotes(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORRemoveExtentOfOwnership(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    /**
     * This method will remove the EOWHoldingNotes based on the index position and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=removeEOWHoldingNotes")
    public ModelAndView removeEOWHoldingNotes(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORRemoveExtentOfOwnership(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    /**
     * This method will add the AccessInformation record based on the selected Line index and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addAccessInformation")
    public ModelAndView addAccessInformation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORRemoveAccessInformationAndHoldingsNotes(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    /**
     * This method will remove the AccessInformation based on the index position and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=removeAccessInformation")
    public ModelAndView removeAccessInformation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORRemoveAccessInformationAndHoldingsNotes(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    /**
     * This method will add the HoldingNotes record based on the selected Line index and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addHoldingNotes")
    public ModelAndView addHoldingNotes(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORRemoveAccessInformationAndHoldingsNotes(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    /**
     * This method will remove the HoldingNotes based on the index position and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=removeHoldingNotes")
    public ModelAndView removeHoldingNotes(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORRemoveAccessInformationAndHoldingsNotes(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=addField")
    public ModelAndView addField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                 HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORDeleteFields(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=removeField")
    public ModelAndView removeField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORDeleteFields(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }


    /**
     * Used for Test-case
     *
     * @param result
     * @param request
     * @param response
     * @param editorForm
     * @return ModelAndView
     */
    protected ModelAndView callSuper(BindingResult result, HttpServletRequest request, HttpServletResponse response,
                                     EditorForm editorForm) {
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=addItemNote")
    public ModelAndView addItemNote(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORRemoveItemNote(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=addDonorToItem")
    public ModelAndView addDonorToItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);
        documentEditor.addORRemoveItemDonor(editorForm, request);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=removeDonorFromItem")
    public ModelAndView removeDonorFromItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) {
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }
        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);
        documentEditor.addORRemoveItemDonor(editorForm, request);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=validateEHoldingsDonorCode")
    public ModelAndView validateEHoldingsDonorCode(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        EditorForm editorForm = (EditorForm) form;
        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        int index = Integer.parseInt(request.getParameter("index"));
        List<DonorInfo> donorInfoList = workEInstanceOlemlForm.getSelectedEHoldings().getDonorInfo();
        DonorInfo donorInfo = donorInfoList.get(index);
        if (donorInfo != null) {
            OLEDonor oleDonor = getOleDonorByDonorCode(donorInfo.getDonorCode());
            if (oleDonor != null) {
                donorInfo.setDonorNote(oleDonor.getDonorNote());
                donorInfo.setDonorPublicDisplay(oleDonor.getDonorPublicDisplay());
            }else {
                donorInfo.setDonorNote("");
                donorInfo.setDonorPublicDisplay("");
                GlobalVariables.getMessageMap().putErrorForSectionId("OleEInstanceDonorInformation-ListOfDonors", "error.donor.code.doesnt.exist", donorInfo.getDonorCode());
            }
            return getUIFModelAndView(form);
        }
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=validateItemDonorCode")
    public ModelAndView validateItemDonorCode(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        EditorForm editorForm = (EditorForm) form;
        WorkInstanceOlemlForm workInstanceOlemlForm = (WorkInstanceOlemlForm) editorForm.getDocumentForm();
        int index = Integer.parseInt(request.getParameter("index"));
        List<DonorInfo> donorInfoList = workInstanceOlemlForm.getSelectedItem().getDonorInfo();
        DonorInfo donorInfo = donorInfoList.get(index);
        if (donorInfo != null) {
            OLEDonor oleDonor = getOleDonorByDonorCode(donorInfo.getDonorCode());
            if (oleDonor != null) {
                donorInfo.setDonorNote(oleDonor.getDonorNote());
                donorInfo.setDonorPublicDisplay(oleDonor.getDonorPublicDisplay());
            }else {
                donorInfo.setDonorNote("");
                donorInfo.setDonorPublicDisplay("");
                GlobalVariables.getMessageMap().putErrorForSectionId("OleDonorInformation-ListOfDonors", "error.donor.code.doesnt.exist", donorInfo.getDonorCode());
            }
            return getUIFModelAndView(form);
        }
        return getUIFModelAndView(form);
    }

    private OLEDonor getOleDonorByDonorCode(String donorCode) {
        if (StringUtils.isNotEmpty(donorCode)) {
            Map map = new HashMap();
            map.put(OLEConstants.DONOR_CODE, donorCode);
            List<OLEDonor> oleDonorList = (List<OLEDonor>) KRADServiceLocatorWeb.getLookupService().findCollectionBySearch(OLEDonor.class, map);
            if (CollectionUtils.isNotEmpty(oleDonorList)) {
                return oleDonorList.get(0);
            }
        }
        return null;
    }

    @RequestMapping(params = "methodToCall=addDonorToEInstance")
    public ModelAndView addDonorToEInstance(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
      //  workEInstanceOlemlForm.getSelectedEHoldings().getDonorInfo().add(new DonorInfo());

        int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
             index++;
            List<DonorInfo> donorInfo = workEInstanceOlemlForm.getSelectedEHoldings().getDonorInfo();
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
            editorForm.setDocumentForm(workEInstanceOlemlForm);
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=removeDonorToEInstance")
    public ModelAndView removeDonorToEInstance(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        List<DonorInfo> donorInfo = workEInstanceOlemlForm.getSelectedEHoldings().getDonorInfo();
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
        editorForm.setDocumentForm(workEInstanceOlemlForm);

     /*   int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if (workEInstanceOlemlForm.getSelectedEHoldings().getDonorInfo().size() > 1) {
            workEInstanceOlemlForm.getSelectedEHoldings().getDonorInfo().remove(index);
        } else {
            if (workEInstanceOlemlForm.getSelectedEHoldings().getDonorInfo().size() == 1) {
                workEInstanceOlemlForm.getSelectedEHoldings().getDonorInfo().remove(index);
                workEInstanceOlemlForm.getSelectedEHoldings().getDonorInfo().add(new DonorInfo());
            }
        }*/
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=removeItemNote")
    public ModelAndView removeItemNote(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        editorForm = documentEditor.addORRemoveItemNote(editorForm, request);
        return super.navigate(editorForm, result, request, response);
    }

    /**
     * Display the linked Bib details for corresponding Holdings/Items.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=showBibs")
    public ModelAndView showBibs(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                 HttpServletRequest request, HttpServletResponse response) {
        EditorForm documentForm = null;
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        // get the document details from request and set them in the form.
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        String docId = request.getParameter("docId");
        String instanceId = request.getParameter("instanceId");
        String editable = request.getParameter("editable");

        //Verifying editable at form object
        if ((null == editable) || (editable.length() == 0)) {
            editable = ((EditorForm) form).getEditable();
        }

        //Default value for editable field if it is null
        if (null == editable || editable.length() == 0) {
            editable = "true";
        }

        ((EditorForm) form).setEditable(editable);
        ((EditorForm) form).setDocCategory(docCategory);
        ((EditorForm) form).setDocType(docType);
        ((EditorForm) form).setDocFormat(docFormat);
        ((EditorForm) form).setDocId(docId);
        ((EditorForm) form).setInstanceId(instanceId);

        // Identify the document editor to be used for the requested document.
        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        documentForm = documentEditor.showBibs((EditorForm) form);
        // Set the output (response) form containing document info into the current form.
        ((EditorForm) form).setDocumentForm(documentForm);

        // Return the next view to be shown to user.
        ModelAndView modelAndView = getUIFModelAndView(form, ((EditorForm) form).getDocumentForm().getViewId());
        return modelAndView;
    }


    @RequestMapping(params = "methodToCall=leaderFieldReset")
    public ModelAndView resetLeaderField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the clear leader method");
        EditorForm editForm = (EditorForm) form;
        WorkBibMarcForm workBibMarcForm = (WorkBibMarcForm) editForm.getDocumentForm();
        workBibMarcForm.setLeaderField(new LeaderField());
        ((EditorForm) form).setDocumentForm(workBibMarcForm);
        return navigate(form, result, request, response);
    }


    @RequestMapping(params = "methodToCall=createSerialReceiving")
    public ModelAndView createSerialReceiving(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the createSerialReceiving method");
        ModelAndView modelAndView = getUIFModelAndView(form, ((EditorForm) form).getDocumentForm().getViewId());
        return modelAndView;

    }


    @RequestMapping(params = "methodToCall=loadControlField006")
    public ModelAndView loadControlField006(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) {
        ((EditorForm) form).setDisplayField006("true");
        WorkBibMarcForm workBibMarcForm = (WorkBibMarcForm) ((EditorForm) form).getDocumentForm();
        String controlField006Format = workBibMarcForm.getMarcControlFields().getValue();
        if(workBibMarcForm.getMarcControlFields().getControlField006()==null){
            ControlField006 controlField006 = new ControlField006();
            workBibMarcForm.getMarcControlFields().setControlField006(controlField006);
        }
        String controlField006Format1 = workBibMarcForm.getMarcControlFields().getControlField006().getFormat();
        if(controlField006Format1!=null){
            controlField006Format=controlField006Format1;
        }
        ControlField006 controlFiled006 = new ControlField006();
        controlFiled006.setFormat(workBibMarcForm.getMarcControlFields().getValue());
        if(controlField006Format1==null){
            controlFiled006.setFormat(workBibMarcForm.getMarcControlFields().getValue());
            workBibMarcForm.getMarcControlFields().setControlField006(controlFiled006);
        }else {
            controlFiled006.setFormat(controlField006Format1);
            workBibMarcForm.getMarcControlFields().setControlField006(controlFiled006);

        }
        workBibMarcForm.getMarcControlFields().setMapVisible("false");
        workBibMarcForm.getMarcControlFields().setBooksVisible("false");
        workBibMarcForm.getMarcControlFields().setMusicVisible("false");
        workBibMarcForm.getMarcControlFields().setComputerFilesVisible("false");
        workBibMarcForm.getMarcControlFields().setContinuingResourcesVisible("false");
        workBibMarcForm.getMarcControlFields().setMixedMaterialVisible("false");
        workBibMarcForm.getMarcControlFields().setVisualMaterialsVisible("false");

        if (controlField006Format.equalsIgnoreCase("e") || controlField006Format.equalsIgnoreCase("f")) {
            workBibMarcForm.getMarcControlFields().setMapVisible("true");
        } else if (controlField006Format.equalsIgnoreCase("a") || controlField006Format.equalsIgnoreCase("t")) {
            workBibMarcForm.getMarcControlFields().setBooksVisible("true");
        } else if (controlField006Format.equalsIgnoreCase("c") || controlField006Format.equalsIgnoreCase("d") ||
                controlField006Format.equalsIgnoreCase("i") || controlField006Format.equalsIgnoreCase("j")) {
            workBibMarcForm.getMarcControlFields().setMusicVisible("true");
        } else if (controlField006Format.equalsIgnoreCase("m")) {
            workBibMarcForm.getMarcControlFields().setComputerFilesVisible("true");
        } else if (controlField006Format.equalsIgnoreCase("s")) {
            workBibMarcForm.getMarcControlFields().setContinuingResourcesVisible("true");
        } else if (controlField006Format.equalsIgnoreCase("p")) {
            workBibMarcForm.getMarcControlFields().setMixedMaterialVisible("true");
        } else if (controlField006Format.equalsIgnoreCase("g") || controlField006Format.equalsIgnoreCase("k") ||
                controlField006Format.equalsIgnoreCase("o") || controlField006Format.equalsIgnoreCase("r")) {
            workBibMarcForm.getMarcControlFields().setVisualMaterialsVisible("true");
        }
        ((EditorForm) form).setDocumentForm(workBibMarcForm);
        return navigate(form, result, request, response);
    }

    @RequestMapping(params = "methodToCall=loadControlField007")
    public ModelAndView loadControlField007(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) {
        ((EditorForm) form).setDisplayField007("true");
        WorkBibMarcForm workBibMarcForm = (WorkBibMarcForm) ((EditorForm) form).getDocumentForm();
        if(workBibMarcForm.getMarcControlFields().getControlField007()==null){
            ControlField007 controlField007 = new ControlField007();
            workBibMarcForm.getMarcControlFields().setControlField007(controlField007);
        }

        String controlField007Format = workBibMarcForm.getMarcControlFields().getValue007();
        String controlField007Format1 = workBibMarcForm.getMarcControlFields().getControlField007().getFormat();

        ControlField007 controlFiled007 = new ControlField007();

        if(controlField007Format1!=null){
            controlField007Format=controlField007Format1;
        }
        if(controlField007Format1!=null){
            controlFiled007.setFormat(controlField007Format1);
            workBibMarcForm.getMarcControlFields().setControlField007(controlFiled007);
        }   else{
            controlFiled007.setFormat(controlField007Format);
            workBibMarcForm.getMarcControlFields().setControlField007(controlFiled007);
        }

        workBibMarcForm.getMarcControlFields().setMapVisible007("false");
        workBibMarcForm.getMarcControlFields().setElectronicResourcesVisible007("false");
        workBibMarcForm.getMarcControlFields().setGlobeVisible007("false");
        workBibMarcForm.getMarcControlFields().setTactileMaterialVisible007("false");
        workBibMarcForm.getMarcControlFields().setProjectGraphicVisible007("false");
        workBibMarcForm.getMarcControlFields().setMicroFormVisible007("false");
        workBibMarcForm.getMarcControlFields().setNonProjectedGraphicVisible007("false");
        workBibMarcForm.getMarcControlFields().setMotionPictureVisible007("false");
        workBibMarcForm.getMarcControlFields().setKitVisible007("false");
        workBibMarcForm.getMarcControlFields().setNotatedMusicVisible007("false");
        workBibMarcForm.getMarcControlFields().setRemoteSensingImageVisible007("false");
        workBibMarcForm.getMarcControlFields().setSoundRecordingVisible007("false");
        workBibMarcForm.getMarcControlFields().setTextVisible007("false");
        workBibMarcForm.getMarcControlFields().setVideoRecordingVisible007("false");
        workBibMarcForm.getMarcControlFields().setUnspecifiedVisible007("false");

        if (controlField007Format.equalsIgnoreCase("a")) {
            workBibMarcForm.getMarcControlFields().setMapVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("c")) {
            workBibMarcForm.getMarcControlFields().setElectronicResourcesVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("d")) {
            workBibMarcForm.getMarcControlFields().setGlobeVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("f")) {
            workBibMarcForm.getMarcControlFields().setTactileMaterialVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("g")) {
            workBibMarcForm.getMarcControlFields().setProjectGraphicVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("h")) {
            workBibMarcForm.getMarcControlFields().setMicroFormVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("k")) {
            workBibMarcForm.getMarcControlFields().setNonProjectedGraphicVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("m")) {
            workBibMarcForm.getMarcControlFields().setMotionPictureVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("o")) {
            workBibMarcForm.getMarcControlFields().setKitVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("q")) {
            workBibMarcForm.getMarcControlFields().setNotatedMusicVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("r")) {
            workBibMarcForm.getMarcControlFields().setRemoteSensingImageVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("s")) {
            workBibMarcForm.getMarcControlFields().setSoundRecordingVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("t")) {
            workBibMarcForm.getMarcControlFields().setTextVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("v")) {
            workBibMarcForm.getMarcControlFields().setVideoRecordingVisible007("true");
        } else if (controlField007Format.equalsIgnoreCase("z")) {
            workBibMarcForm.getMarcControlFields().setUnspecifiedVisible007("true");
        }
        ((EditorForm) form).setDocumentForm(workBibMarcForm);

        return navigate(form, result, request, response);
    }

    @RequestMapping(params = "methodToCall=loadControlField008")
    public ModelAndView loadControlField008(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) {
        ((EditorForm) form).setDisplayField008("true");
        WorkBibMarcForm workBibMarcForm = (WorkBibMarcForm) ((EditorForm) form).getDocumentForm();

        if(workBibMarcForm.getMarcControlFields().getControlField008()==null){
            ControlField008 controlField008 = new ControlField008();
            workBibMarcForm.getMarcControlFields().setControlField008(controlField008);
        }
        String controlField008Format = workBibMarcForm.getMarcControlFields().getValue008();
        workBibMarcForm.getMarcControlFields().setControlField008(new ControlField008());


        workBibMarcForm.getMarcControlFields().setMapVisible008("false");
        workBibMarcForm.getMarcControlFields().setBooksVisible008("false");
        workBibMarcForm.getMarcControlFields().setMusicVisible008("false");
        workBibMarcForm.getMarcControlFields().setComputerFilesVisible008("false");
        workBibMarcForm.getMarcControlFields().setContinuingResourcesVisible008("false");
        workBibMarcForm.getMarcControlFields().setMixedMaterialVisible008("false");
        workBibMarcForm.getMarcControlFields().setVisualMaterialsVisible008("false");

        if (controlField008Format.equalsIgnoreCase("map")) {
            workBibMarcForm.getMarcControlFields().setMapVisible008("true");
        } else if (controlField008Format.equalsIgnoreCase("books")) {
            workBibMarcForm.getMarcControlFields().setBooksVisible008("true");
        } else if (controlField008Format.equalsIgnoreCase("music")) {
            workBibMarcForm.getMarcControlFields().setMusicVisible008("true");
        } else if (controlField008Format.equalsIgnoreCase("computer")) {
            workBibMarcForm.getMarcControlFields().setComputerFilesVisible008("true");
        } else if (controlField008Format.equalsIgnoreCase("countRes")) {
            workBibMarcForm.getMarcControlFields().setContinuingResourcesVisible008("true");
        } else if (controlField008Format.equalsIgnoreCase("mixMat")) {
            workBibMarcForm.getMarcControlFields().setMixedMaterialVisible008("true");
        } else if (controlField008Format.equalsIgnoreCase("visMat")) {
            workBibMarcForm.getMarcControlFields().setVisualMaterialsVisible008("true");
        }

        ((EditorForm) form).setDocumentForm(workBibMarcForm);
        return navigate(form, result, request, response);
    }

    @RequestMapping(params = "methodToCall=addCoverageSection")
    public ModelAndView addCoverageSection(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getCoverages().getCoverage().add(new Coverage());
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=removeCoverageSection")
    public ModelAndView removeCoverageSection(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if (workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getCoverages().getCoverage().size() > 1) {
            workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getCoverages().getCoverage().remove(index);
        } else {
            if (workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getCoverages().getCoverage().size() == 1) {
                workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getCoverages().getCoverage().remove(index);
                Coverage coverage = new Coverage();
                workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getCoverages().getCoverage().add(coverage);
            }
        }
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=addLink")
    public ModelAndView addLink(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        workEInstanceOlemlForm.getSelectedEHoldings().getLink().add(index+1,new Link());
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=deleteLink")
    public ModelAndView deleteLink(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if (workEInstanceOlemlForm.getSelectedEHoldings().getLink().size() > 1) {
            workEInstanceOlemlForm.getSelectedEHoldings().getLink().remove(index);
        } else {
            if (workEInstanceOlemlForm.getSelectedEHoldings().getLink().size() == 1) {
                workEInstanceOlemlForm.getSelectedEHoldings().getLink().remove(index);
                Link link = new Link();
                workEInstanceOlemlForm.getSelectedEHoldings().getLink().add(link);
            }
        }
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=addPerpetualAccessSection")
    public ModelAndView addPerpetualAccessSection(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                  HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        //int index = Integer.parseInt(workEInstanceOlemlForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
       /* String selectedPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        int selectedExtentIndex = Integer.parseInt(StringUtils.substring(selectedPath,
                (StringUtils.indexOf(selectedPath, "[") + 1),
                StringUtils.lastIndexOf(selectedPath, "]")));*/
        workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().add(new PerpetualAccess());
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=removePerpetualAccessSection")
    public ModelAndView removePerpetualAccessSection(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                     HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if (workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().size() > 1) {
            workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().remove(index);
        } else {
            if (workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().size() == 1) {
                workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().remove(index);
                PerpetualAccess perpetualAccess = new PerpetualAccess();
                workEInstanceOlemlForm.getSelectedEHoldings().getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().add(perpetualAccess);
            }
        }
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=addOleEInstanceHoldingNotes")
    public ModelAndView addOleEInstanceHoldingNotes(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                    HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        workEInstanceOlemlForm.getSelectedEHoldings().getNote().add(new Note());
        return super.navigate(editorForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=removeOleEInstanceHoldingNotes")
    public ModelAndView removeOleEInstanceHoldingNotes(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                       HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        WorkEInstanceOlemlForm workEInstanceOlemlForm = (WorkEInstanceOlemlForm) editorForm.getDocumentForm();
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if (workEInstanceOlemlForm.getSelectedEHoldings().getNote().size() > 1) {
            workEInstanceOlemlForm.getSelectedEHoldings().getNote().remove(index);
        } else {
            if (workEInstanceOlemlForm.getSelectedEHoldings().getNote().size() == 1) {
                workEInstanceOlemlForm.getSelectedEHoldings().getNote().remove(index);
                Note note = new Note();
                workEInstanceOlemlForm.getSelectedEHoldings().getNote().add(note);
            }
        }
        return super.navigate(editorForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=globalEditSave")
    public ModelAndView globalEditSave(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        if (!isFormInitialized) {
            super.start(form, result, request, response);
            isFormInitialized = true;
        }
        EditorForm editorForm = (EditorForm) form;
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        List<String> ids = (List<String>) request.getSession().getAttribute("Ids");
        if (docCategory == null) {
            docCategory = editorForm.getDocCategory();
        }
        if (docType == null) {
            docType = editorForm.getDocType();
        }
        if (docFormat == null) {
            docFormat = editorForm.getDocFormat();
        }

        ((EditorForm) form).setDocCategory(docCategory);
        ((EditorForm) form).setDocType(docType);
        ((EditorForm) form).setDocFormat(docFormat);
        DocumentEditor documentEditor = DocumentEditorFactory.getInstance()
                .getDocumentEditor(docCategory, docType, docFormat);

        if (documentEditor != null && documentEditor.isValidUpdate((EditorForm) form) || ((EditorForm) form).getAllowUpdate().equals
                ("true")) {
            // Send the input through one (request)form and get the output through another (response) form.
            EditorForm documentForm = documentEditor.bulkUpdate((EditorForm) form, ids);
            // Set the output (response) form containing docum ((EditorForm) form).isAllowUpdate()ent info into the current form.
            ((EditorForm) form).setDocumentForm(documentForm);
            ((EditorForm) form).setAllowUpdate(" ");
        } else {
            ((EditorForm) form).setAllowUpdate("false");
        }
        return super.navigate(editorForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=printCallSlip")
    public void printCallSlip(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        String formKey = request.getParameter("formKey");
        EditorForm editorForm = (EditorForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
        generateCallSlip(editorForm, response);
    }

    private void generateCallSlip(EditorForm editorForm, HttpServletResponse response) {
        LOG.debug("Creating pdf");
        String title = "", author = "", callNumber = "", location = "", copyNumber = "", enumeration = "", chronology = "", barcode = "";
        SearchResponse searchResponse = null;
        SearchParams searchParams = new SearchParams();
        SearchField searchField1 = searchParams.buildSearchField("item", "ItemIdentifier_search", editorForm.getDocId());
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("OR", searchField1, "AND"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Author"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CallNumber"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "LocationName"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CopyNumber"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "enumeration"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "chronology"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemBarcode"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "CallNumber"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", "LocationName"));
        try {
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        } catch (Exception e) {
            LOG.error(e, e);
        }
        if (CollectionUtils.isNotEmpty(searchResponse.getSearchResults())) {
            for (SearchResultField searchResultField : searchResponse.getSearchResults().get(0).getSearchResultFields()) {
                if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode()) && searchResultField.getFieldName().equalsIgnoreCase("Title")) {
                    if (StringUtils.isBlank(title)) {
                        title = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                    }
                } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode()) && searchResultField.getFieldName().equalsIgnoreCase("Author")) {
                    if (StringUtils.isBlank(author)) {
                        author = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                    }
                } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase("CallNumber")) {
                    callNumber = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase("LocationName")) {
                    location = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase("CopyNumber")) {
                    copyNumber = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase("enumeration")) {
                    enumeration = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase("chronology")) {
                    chronology = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase("ItemBarcode")) {
                    barcode = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase("CallNumber")) {
                    if (StringUtils.isBlank(callNumber)) {
                        callNumber = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                    }
                } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase("LocationName")) {
                    if (StringUtils.isBlank(location)) {
                        location = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                    }
                }
            }
        }
        String fileName = "Call/Paging Slip" + "_" + (editorForm.getTitle() != null ? editorForm.getTitle() : "") + "_" + new Date(System.currentTimeMillis()) + ".pdf";
        if (LOG.isInfoEnabled()) {
            LOG.info("File Created :" + title + "file name ::" + fileName + "::");
        }
        try {
            Document document = this.getDocument(0, 0, 5, 5);
            OutputStream outputStream = null;
            response.setContentType("application/pdf");
            //response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            outputStream = response.getOutputStream();
            PdfWriter.getInstance(document, outputStream);
            Font boldFont = new Font(Font.TIMES_ROMAN, 15, Font.BOLD);
            document.open();
            document.newPage();
            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{20, 2, 30});
            Paragraph paraGraph = new Paragraph();
            paraGraph.setAlignment(Element.ALIGN_CENTER);
            paraGraph.add(new Chunk("Call/Paging Slip", boldFont));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            document.add(paraGraph);

            pdfTable.addCell(getPdfPCellInJustified("Title"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(title));

            pdfTable.addCell(getPdfPCellInJustified("Author"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(author));

            pdfTable.addCell(getPdfPCellInJustified("Call Number"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(callNumber));

            pdfTable.addCell(getPdfPCellInJustified("Location"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(location));

            pdfTable.addCell(getPdfPCellInJustified("Copy Number"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(copyNumber));

            pdfTable.addCell(getPdfPCellInJustified("Enumeration"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(enumeration));

            pdfTable.addCell(getPdfPCellInJustified("Chronology"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(chronology));

            pdfTable.addCell(getPdfPCellInJustified("Barcode"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified(barcode));

            document.add(pdfTable);
            document.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

    private PdfPCell getPdfPCellInJustified(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_JUSTIFIED);
        return pdfPCell;
    }

    private PdfPCell getPdfPCellInLeft(String chunk) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(new Chunk(chunk)));
        pdfPCell.setBorder(pdfPCell.NO_BORDER);
        pdfPCell.setHorizontalAlignment(pdfPCell.ALIGN_LEFT);
        return pdfPCell;
    }

    public Document getDocument(float f1, float f2, float f3, float f4) {
        Document document = new Document(PageSize.A4);
        document.setMargins(f1, f2, f3, f4);
        return document;
    }


    private void prepareGlobalEditFields(EditorForm editorForm, String docType) {

        if (DocType.INSTANCE.getCode().equals(docType) || DocType.HOLDINGS.getCode().equals(docType)) {
            editorForm.setHeaderText(OLEConstants.GLOBAL_EDIT_HOLDINGS_HEADER_MESSAGE);
        } else if (DocType.EINSTANCE.getCode().equals(docType) || DocType.EHOLDINGS.getCode().equals(docType)) {
            editorForm.setHeaderText(OLEConstants.GLOBAL_EDIT_EHOLDINGS_HEADER_MESSAGE);
        } else if (DocType.ITEM.getCode().equals(docType)) {
            editorForm.setHeaderText(OLEConstants.GLOBAL_EDIT_ITEM_HEADER_MESSAGE);
        }

        DocumentSearchConfig documentSearchConfig = DocumentSearchConfig.getDocumentSearchConfig();
        List<DocTypeConfig> docTypeFields = documentSearchConfig.getDocTypeConfigs();

        for (DocTypeConfig docTypeConfig : docTypeFields) {

            List<DocFormatConfig> docFormatConfigList = docTypeConfig.getDocFormatConfigList();

            if (DocType.HOLDINGS.getCode().equals(docTypeConfig.getName()) && DocType.HOLDINGS.getCode().equals(docType)) {
                setGlobalEditFlagValues(editorForm, docFormatConfigList, docType);
            } else if (DocType.EHOLDINGS.getCode().equals(docTypeConfig.getName()) && DocType.EHOLDINGS.getCode().equals(docType)) {
                setGlobalEditFlagValues(editorForm, docFormatConfigList, docType);
            } else if (DocType.ITEM.getCode().equals(docType) && DocType.ITEM.getCode().equals(docTypeConfig.getName())) {
                setGlobalEditFlagValues(editorForm, docFormatConfigList, docType);
            }

        }

    }

    private void setGlobalEditFlagValues(EditorForm editorForm, List<DocFormatConfig> docFormatConfigList, String docType) {

        for (DocFormatConfig docFormatConfig : docFormatConfigList) {

            List<DocFieldConfig> docFieldConfigList = docFormatConfig.getDocFieldConfigList();

            if (OLEConstants.OLEML_FORMAT.equals(docFormatConfig.getName())) {
                if (DocType.INSTANCE.getCode().equals(docType) || DocType.HOLDINGS.getCode().equals(docType)) {
                    GlobalEditHoldingsFieldsFlagBO globalEditHoldingsFieldsFlagBO = editorForm.getGlobalEditHoldingsFieldsFlagBO();
                    for (DocFieldConfig docFieldConfig : docFieldConfigList) {

                        if (docFieldConfig.isGloballyEditable()) {

                            if (OLEConstants.CALL_NUMBER.equals(docFieldConfig.getName())) {
                                globalEditHoldingsFieldsFlagBO.setCallNumberEditFlag(true);
                            } else if (OLEConstants.CALL_NUMBER_PREFIX.equals(docFieldConfig.getName())) {
                                globalEditHoldingsFieldsFlagBO.setCallNumberPrefixEditFlag(true);
                            } else if (OLEConstants.CALL_NUMBER_TYPE_CODE.equals(docFieldConfig.getName())) {
                                globalEditHoldingsFieldsFlagBO.setCallNumberTypeEditFlag(true);
                            } else if (OLEConstants.SHELVING_ORDER.equals(docFieldConfig.getName())) {
                                globalEditHoldingsFieldsFlagBO.setShelvingOrderEditFlag(true);
                            } else if (OLEConstants.LOCATION_LEVEL.equals(docFieldConfig.getName())) {
                                globalEditHoldingsFieldsFlagBO.setLocationEditFlag(true);
                            } else if (OLEConstants.HOLDINGS_NOTE.equals(docFieldConfig.getName())) {
                                globalEditHoldingsFieldsFlagBO.setHoldingsNoteEditFlag(true);
                            } else if (OLEConstants.URI.equals(docFieldConfig.getName())) {
                                globalEditHoldingsFieldsFlagBO.setAccessInformationEditFlag(true);
                            } else if (OLEConstants.RECEIPT_STATUS_CODE.equals(docFieldConfig.getName())) {
                                globalEditHoldingsFieldsFlagBO.setReceiptStatusEditFlag(true);
                            } else if (OLEConstants.COPY_NUMBER_LABEL.equals(docFieldConfig.getName())) {
                                globalEditHoldingsFieldsFlagBO.setCopyNumberEditFlag(true);
                            } else if (OLEConstants.EXTENTOFOWNERSHIP_NOTE_VALUE_DISPLAY.equals(docFieldConfig.getName()) || OLEConstants.EXTENTOFOWNERSHIP_NOTE_TYPE_DISPLAY.equals(docFieldConfig.getName()) || OLEConstants.EXTENTOFOWNERSHIP_Type_display.equals(docFieldConfig.getName())) {
                                globalEditHoldingsFieldsFlagBO.setExtentOwnerShipEditFlag(true);
                            } else if (OLEConstants.URI_SEARCH.equals(docFieldConfig.getName()) || OLEConstants.URI_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditHoldingsFieldsFlagBO.setExtendedInfoEditFlag(true);
                            }
                            //TODO - Need to do for other fields in holdings
                        }
                    }
                } else if (DocType.EINSTANCE.getCode().equals(docType) || DocType.EHOLDINGS.getCode().equals(docType)) {
                    GlobalEditEHoldingsFieldsFlagBO globalEditEHoldingsFieldsFlagBO = editorForm.getGlobalEditEHoldingsFieldsFlagBO();
                    for (DocFieldConfig docFieldConfig : docFieldConfigList) {

                        if (docFieldConfig.isGloballyEditable()) {
                            if (OLEConstants.CALL_NUMBER.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setCallNumberEditFlag(true);
                            } else if (OLEConstants.CALL_NUMBER_PREFIX.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setCallNumberPrefixEditFlag(true);
                            } else if (OLEConstants.CALL_NUMBER_TYPE_CODE.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setCallNumberTypeEditFlag(true);
                            } else if (OLEConstants.SHELVING_ORDER.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setShelvingOrderEditFlag(true);
                            } else if (OLEConstants.LOCATION_LEVEL.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setLocationEditFlag(true);
                            } else if (OLEConstants.EHOLDING_ACCESS_STATUS.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setAccessStatusEditFlag(true);
                            } else if (OLEConstants.EHOLDING_PLATFORM_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setPlatformEditFlag(true);
                            } else if (OLEConstants.EHOLDING_IMPRINT.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setImprintEditFlag(true);
                            } else if (OLEConstants.EHOLDING_E_PUBLISHER.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setPublisherEditFlag(true);
                            } else if (OLEConstants.EHOLDING_STATISTICAL_CODE.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setStatisticalCodeEditFlag(true);
                            } /*else if (OLEConstants.EHOLDING_SUBSCRIPTION_STATUS.equals(docFieldConfig.getName())) {
                                // TODO add field to js staffOnlyEditFlag
                                globalEditEHoldingsFieldsFlagBO.setSubscriptionEditFlag(true);
                            }*/ else if (OLEConstants.EHOLDING_SUBSCRIPTION_STATUS.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setAcquisitionInformationEditFlag(true);
                                globalEditEHoldingsFieldsFlagBO.setSubscriptionEditFlag(true);
                            } else if (OLEConstants.EHOLDING_LINK.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setLinkEditFlag(true);
                                globalEditEHoldingsFieldsFlagBO.setAccessInformationEditFlag(true);
                            } else if (OLEConstants.EHOLDING_SIMULTANEOUS_USER.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setSimultaneousEditFlag(true);
                                globalEditEHoldingsFieldsFlagBO.setAccessInformationEditFlag(true);
                            } else if (OLEConstants.EHOLDING_PERSIST_LINK.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setPersistentLinkEditFlag(true);
                                globalEditEHoldingsFieldsFlagBO.setAccessInformationEditFlag(true);
                            } else if (OLEConstants.EHOLDING_ACCESS_LOCATION.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setAccessInformationEditFlag(true);
                                globalEditEHoldingsFieldsFlagBO.setAccessLocationEditFlag(true);
                            } else if (OLEConstants.EHOLDING_LINK_TEXT.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setLinkTextEditFlag(true);
                            } else if (OLEConstants.EHOLDING_ADMIN_USER.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setAdminUserNameEditFlag(true);
                            } else if (OLEConstants.EHOLDING_ADMIN_PWSD.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setAdminPasswordEditFlag(true);
                            } else if (OLEConstants.EHOLDING_ACCESS_USR_NAME.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setAccessUserNameEditFlag(true);
                            } else if (OLEConstants.EHOLDING_ACCESS_PSWD.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setAccessPasswordEditFlag(true);
                            } else if (OLEConstants.EHOLDING_ADMIN_URL.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setAdminUrlEditFlag(true);
                            } else if (OLEConstants.EHOLDING_AUTHENTICATION_TYPE.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setAuthenticationEditFlag(true);
                            } else if (OLEConstants.EHOLDING_PROXIED.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setProxiedEditFlag(true);
                            } else if (OLEConstants.EHOLDING_ILL_IND.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setLicenseDetailsEditFlag(true);
                                globalEditEHoldingsFieldsFlagBO.setIllEditFlag(true);
                            } else if (OLEConstants.EHOLDING_COVERAGE.equals(docFieldConfig.getName())) {
                                //globalEditEHoldingsFieldsFlagBO.setExtentOfOwnerShipEditFlag(true);
                                globalEditEHoldingsFieldsFlagBO.setCoverageExtentOfOwnerShipEditFlag(true);
                            } else if (OLEConstants.EHOLDING_PERPETUAL.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setExtentOfOwnerShipEditFlag(true);
                                globalEditEHoldingsFieldsFlagBO.setPerpetualAccessEditFlag(true);
                            } else if (OLEConstants.HOLDINGS_NOTE.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.seteHoldingsNoteEditFlag(true);
                            } else if (OLEConstants.DONOR_CODE_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setDonorCodeEditFlag(true);
                            } else if (OLEConstants.EHOLDING_DONOR_PUBLIC_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setDonorPublicDisplayEditFlag(true);
                            } else if (OLEConstants.EHOLDING_DONOR_NOTE.equals(docFieldConfig.getName())) {
                                globalEditEHoldingsFieldsFlagBO.setDonorNoteEditFlag(true);
                            }
                        }
                    }

                    // TODO -  need to do for E-holdings for global edit

                } else if (DocType.ITEM.getCode().equals(docType)) {

                    GlobalEditItemFieldsFlagBO globalEditItemFieldsFlagBO = editorForm.getGlobalEditItemFieldsFlagBO();
                    for (DocFieldConfig docFieldConfig : docFieldConfigList) {
                        if (docFieldConfig.isGloballyEditable()) {

                            if (OLEConstants.CALL_NUMBER.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setCallNumberEditFlag(true);
                            } else if (OLEConstants.CALL_NUMBER_PREFIX.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setCallNumberPrefixEditFlag(true);
                            } else if (OLEConstants.CALL_NUMBER_TYPE_CODE.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setCallNumberTypeEditFlag(true);
                            } else if (OLEConstants.SHELVING_ORDER.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setShelvingOrderEditFlag(true);
                            } else if (OLEConstants.LOCATION_LEVEL.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setLocationEditFlag(true);
                            } else if (OLEConstants.ITEM_STATUS_CODE_VALUE.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setItemStatusEditFlag(true);
                                globalEditItemFieldsFlagBO.setAccessInfoEditFlag(true);
                            } else if (OLEConstants.PO_LINE_ITEM_IDENTIFIER.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setCallNumberTypeEditFlag(true);
                            } else if (OLEConstants.VENDOR_LINE_ITEM_IDENTIFIER.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setVendorLineItemIDEditFlag(true);
                            } else if (OLEConstants.BAR_CODE_ARSL.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setBarcodeARSLEditFlag(true);
                            } else if (OLEConstants.STATISTICAL_SEARCH_CODE.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setStatisticalSearchingCodesEditFlag(true);
                            } else if (OLEConstants.ITEM_TYPE_CODE_VALUE.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setItemTypeEditFlag(true);
                            } else if (OLEConstants.COPY_NUMBER_LABEL.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setCopyNumberEditFlag(true);
                            } else if (OLEConstants.VOLUME_NUMBER.equals(docFieldConfig.getName()) || OLEConstants.VOLUME_NUMBER_LABEL.equals(docFieldConfig.getName())) {
                                //globalEditItemFieldsFlagBO.set(true);
                            } else if (OLEConstants.ENUMARATION.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setEnumerationEditFlag(true);
                            } else if (OLEConstants.CHRONOLOGY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setChronologyEditFlag(true);
                            } else if (OLEConstants.DONORPUBLIC_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setDonorPublicDisplayEditFlag(true);
                            } else if (OLEConstants.DONORNOTE_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setDonorNoteEditFlag(true);
                            } else if (OLEConstants.ITEMNOTE_TYPE_DISPLAY.equals(docFieldConfig.getName()) ) {
                                globalEditItemFieldsFlagBO.setExtndInfoEditFlag(true);
                            } else if (OLEConstants.ITEMBARCODE_DISPLAY.equals(docFieldConfig.getName()) || OLEConstants.ITEMBARCODE_SEARCH.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setBarcodeEditFlag(true);
                            } else if (OLEConstants.TEMPITEMTYPE_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setTempItemTypeEditFlag(true);
                            } else if (OLEConstants.DONORCODE_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setDonorCodeEditFlag(true);
                            } else if (OLEConstants.FORMERIDENTIFIER_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setFormerIdentifiersEditFlag(true);
                            } else if (OLEConstants.FASTADD_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setFastAddEditFlag(true);
                            } else if (OLEConstants.PIECES_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setNumberOfPiecesEditFlag(true);
                            } else if (OLEConstants.ITEMSTATUSDATE_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setItemStatusDateEditFlag(true);
                            } else if (OLEConstants.CHECKINNOTE_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setCheckinNoteEditFlag(true);
                            } else if (OLEConstants.DUEDATETIME_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setDueDatetimeEditFlag(true);
                            } else if (OLEConstants.CLAIMSRETURNFLAG_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setClaimsReturnEditFlag(true);
                            } else if (OLEConstants.MISSINGPIECEFLAG_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setMissingPieceEditFlag(true);
                            } else if (OLEConstants.ITEMDAMAGEDSTATUS_DISPLAY.equals(docFieldConfig.getName())) {
                                globalEditItemFieldsFlagBO.setItemDamagedStatusEditFlag(true);
                            }

                            // TODO - need to do for other fields in ITEM
                        }
                    }
                }
            }


        }


    }


    @RequestMapping(params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        String docCategory = request.getParameter("docCategory");
        String docType = request.getParameter("docType");
        String docFormat = request.getParameter("docFormat");
        String docId = request.getParameter("docId");
        String bibId = request.getParameter("bibId");
        String instanceId = request.getParameter("instanceId");
        String editable = request.getParameter("editable");

        String requestUrl="/ole-kr-krad/editorcontroller?viewId=EditorView&methodToCall=load&docCategory="+docCategory+"&docType="+docType+"&docFormat="+docFormat+"&docId="+docId+"&bibId="+bibId+"&instanceId="+instanceId+"&editable="+editable;
        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + requestUrl;
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, "load");
        if (StringUtils.isNotBlank(form.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, form.getReturnFormKey());
        }


        return performRedirect(form, url, props);
    }
    /**
     * This method prints the bib information in to a PDF.
     * @param form
     * @param result
     * @param request
     * @param response
     */
    @RequestMapping(params = "methodToCall=printBib")
    public void printBib(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        String formKey = request.getParameter("formKey");
        EditorForm editorForm = (EditorForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
        generateBibPdfView(editorForm, response);
    }

    /**
     * This method generates the pdf with bib information.
     * @param editorForm
     * @param response
     */
    private void generateBibPdfView(EditorForm editorForm, HttpServletResponse response) {
        LOG.debug("Creating Bib PDF");
        String fileName = "Bib" + "_" + editorForm.getDocId() + ".pdf";

        Bib bib = null;
        WorkBibMarcForm documentForm = null;
        if (null != editorForm.getDocumentForm() && editorForm.getDocumentForm() instanceof WorkBibMarcForm) {
            documentForm = (WorkBibMarcForm) editorForm.getDocumentForm();
            if (CollectionUtils.isNotEmpty(documentForm.getBibTreeList())) {
                bib = documentForm.getBibTreeList().get(0).getBib();
            }
        }

        try {
            Document document = this.getDocument(0, 0, 5, 5);
            OutputStream outputStream = null;
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline;filename=" + fileName);
            outputStream = response.getOutputStream();
            PdfWriter.getInstance(document, outputStream);
            Font boldFont = new Font(Font.TIMES_ROMAN, 15, Font.BOLD);
            document.open();
            document.newPage();

            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.setWidths(new int[]{15, 2, 30});

            Paragraph paraGraph = new Paragraph();
            paraGraph.setAlignment(Element.ALIGN_CENTER);
            paraGraph.add(new Chunk("Bibliographic Record", boldFont));
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);
            paraGraph.add(Chunk.NEWLINE);

            pdfTable.addCell(getPdfPCellInJustified("Bib Id"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((bib != null && bib.getId() != null) ? bib.getId() : ""));

            pdfTable.addCell(getPdfPCellInJustified("Bib Record Status"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((bib != null && bib.getStatus() != null) ? bib.getStatus() : ""));

            pdfTable.addCell(getPdfPCellInJustified("Leader"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            pdfTable.addCell(getPdfPCellInJustified((documentForm != null && documentForm.getLeader() != null) ? documentForm.getLeader() : ""));

            if (null != documentForm && null != documentForm.getMarcControlFields()) {
                List<ControlField006Text> controlFields006List = documentForm.getMarcControlFields().getControlFields006List();
                if (CollectionUtils.isNotEmpty(controlFields006List)) {
                    if (StringUtils.isNotBlank(controlFields006List.get(0).getRawText())) {
                        pdfTable.addCell(getPdfPCellInJustified("006"));
                        pdfTable.addCell(getPdfPCellInLeft(":"));
                        if (controlFields006List.size() == 1) {
                            pdfTable.addCell(getPdfPCellInJustified(controlFields006List.get(0).getRawText()));
                        } else if (controlFields006List.size() > 1) {
                            StringBuilder textBuilder = new StringBuilder();
                            for (ControlField006Text controlField006Text : controlFields006List) {
                                if (null != controlField006Text.getRawText()) {
                                    textBuilder.append(controlField006Text.getRawText());
                                    textBuilder.append(System.lineSeparator());
                                }
                            }
                            pdfTable.addCell(getPdfPCellInJustified(textBuilder.toString()));
                        }
                    }
                }

                List<ControlField007Text> controlFields007List = documentForm.getMarcControlFields().getControlFields007List();
                if (CollectionUtils.isNotEmpty(controlFields007List)) {
                    if (StringUtils.isNotBlank(controlFields007List.get(0).getRawText())) {
                        pdfTable.addCell(getPdfPCellInJustified("007"));
                        pdfTable.addCell(getPdfPCellInLeft(":"));
                        if (controlFields007List.size() == 1) {
                            pdfTable.addCell(getPdfPCellInJustified(controlFields007List.get(0).getRawText()));
                        } else if (controlFields007List.size() > 1) {
                            StringBuilder textBuilder = new StringBuilder();
                            for (ControlField007Text controlField007Text : controlFields007List) {
                                if (null != controlField007Text.getRawText()) {
                                    textBuilder.append(controlField007Text.getRawText());
                                    textBuilder.append(System.lineSeparator());
                                }
                            }
                            pdfTable.addCell(getPdfPCellInJustified(textBuilder.toString()));
                        }
                    }
                }
            }

            BibMarcRecord bibMarcRecord = null;
            String controlField008Value = null;
            if (null != bib) {
                BibMarcRecordProcessor recordProcessor = new BibMarcRecordProcessor();
                BibMarcRecords bibMarcRecords = recordProcessor.fromXML(bib.getContent());
                if (null != bibMarcRecords && CollectionUtils.isNotEmpty(bibMarcRecords.getRecords())) {
                    bibMarcRecord = bibMarcRecords.getRecords().get(0);
                    List<ControlField> controlFields = bibMarcRecord.getControlFields();
                    for (ControlField controlField : controlFields) {
                        if (controlField.getTag().equals(ControlFields.CONTROL_FIELD_008)) {
                            controlField008Value = controlField.getValue();
                        }
                    }
                }
            }

            pdfTable.addCell(getPdfPCellInJustified("008"));
            pdfTable.addCell(getPdfPCellInLeft(":"));
            if (StringUtils.isNotBlank(controlField008Value)) {
                pdfTable.addCell(getPdfPCellInJustified(controlField008Value.replaceAll(" ", "#")));
            } else {
                pdfTable.addCell(getPdfPCellInJustified(""));
            }

            Paragraph dataFieldsParaGraph = new Paragraph(new Paragraph("Data Fields : ", new Font(Font.TIMES_ROMAN, 13, Font.BOLD)));
            dataFieldsParaGraph.setIndentationLeft(60);
            dataFieldsParaGraph.add(Chunk.NEWLINE);
            dataFieldsParaGraph.add(Chunk.NEWLINE);

            PdfPTable dataFieldsTable = new PdfPTable(4);

            float[] columnWidths = new float[] {10f, 5f, 5f, 100f};
            dataFieldsTable.setWidths(columnWidths);

            if (null != bib && null != bibMarcRecord) {
                StringBuffer value = new StringBuffer();
                for (DataField dataField : bibMarcRecord.getDataFields()) {
                    dataFieldsTable.addCell(getPdfPCellInLeft(dataField.getTag()));
                    dataFieldsTable.addCell(getPdfPCellInLeft(StringUtils.isNotBlank(dataField.getInd1()) ? dataField.getInd1() : "#"));
                    dataFieldsTable.addCell(getPdfPCellInLeft(StringUtils.isNotBlank(dataField.getInd2()) ? dataField.getInd2() : "#"));
                    for (SubField subField : dataField.getSubFields()) {
                        value.append("|" + subField.getCode());
                        value.append(" ");
                        value.append(subField.getValue());
                    }
                    dataFieldsTable.addCell(getPdfPCellInLeft(value.toString()));
                    value.delete(0, value.length());
                }
            }

            document.add(paraGraph);
            document.add(pdfTable);
            document.add(Chunk.NEWLINE);
            document.add(dataFieldsParaGraph);
            document.add(dataFieldsTable);
            document.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

    private Connection getConnection() throws SQLException {
        DataSource dataSource = null;
        try {
            dataSource = DataSource.getInstance();
        } catch (IOException e) {
            LOG.error("IOException : " + e);
        } catch (SQLException e) {
            LOG.error("SQLException : " + e);
        } catch (PropertyVetoException e) {
            LOG.error("PropertyVetoException : " + e);
        }
        return dataSource.getConnection();
    }

}