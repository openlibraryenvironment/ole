package org.kuali.ole.describe.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.QualifiedDublinRecordHandler;
import org.kuali.ole.UnQualifiedDublinRecordHandler;
import org.kuali.ole.describe.bo.*;
import org.kuali.ole.describe.form.EditorForm;
import org.kuali.ole.describe.form.WorkBibDublinForm;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.WorkBibDublinRecord;
import org.kuali.ole.describe.service.DocstoreHelperService;
import org.kuali.ole.pojo.dublin.unqualified.UnQualifiedDublinRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pp7788
 * Date: 12/11/12
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * DublinEditorController is the controller class for Dublin Editor
 */

//@Controller
//@RequestMapping(value = "/dublincomponents")
public class WorkBibDublinEditor
        extends AbstractEditor
        implements DocumentEditor {
    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */

    private static final Logger LOG = Logger.getLogger(WorkBibDublinEditor.class);

    private WorkDublinEditorFormDataHandler dublinEditorFormDataHandler;
    private DocstoreHelperService docstoreHelperService;

    private static WorkBibDublinEditor workBibDublinEditor = new WorkBibDublinEditor();

    public static WorkBibDublinEditor getInstance() {
        return workBibDublinEditor;
    }

    private WorkBibDublinEditor() {
    }

    /*private boolean  canDisplayBib(String principalId){
        LOG.debug("Inside the canLoan method");
        PermissionService service= KimApiServiceLocator.getPermissionService();
        return  service.hasPermission(principalId,OLEConstants.OlePatron.PATRON_NAMESPACE,OLEConstants.DISPLAY_BIB);
    }
*/
    @Override
    public EditorForm loadDocument(EditorForm editorForm) {
        WorkBibDublinForm dublinEditorForm = new WorkBibDublinForm();
        String docId = editorForm.getDocId();
        String bibId = editorForm.getBibId();
        String docFormat = editorForm.getDocFormat();
        editorForm.setFromDublin(true);
        List<BibTree> bibTreeList = new ArrayList();
        BibTree bibTree = null;
        Bib bib = null;
//        editorForm.setHeaderText("Bibliographic Editor - Dublin Core (Unqualified) Format");
        String directory = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(org.kuali.ole.sys.OLEConstants.EXTERNALIZABLE_HELP_URL_KEY);
        editorForm.setExternalHelpUrl(directory+"/reference/webhelp/CG/content/ch01s01.html");
        editorForm.setHeaderText("Dublin");
        if (null != docId) {
            try {

                bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(docId);
                bibTreeList.add(bibTree);
                bib = bibTree.getBib();
                dublinEditorForm.setBibTreeList(bibTreeList);
                if (docFormat.equals(DocFormat.DUBLIN_UNQUALIFIED.getCode())) {
                    UnQualifiedDublinRecord unQualifiedDublinRecord = new UnQualifiedDublinRecordHandler().fromXML(bib.getContent());
                    editorForm.setCreatedBy(bib.getStatus());
                    editorForm.setCreatedDate(bib.getCreatedOn());
                    editorForm.setUpdatedBy(bib.getUpdatedBy());
                    editorForm.setUpdatedDate(bib.getUpdatedOn());
                    editorForm.setStatusUpdatedBy(bib.getStatusUpdatedBy());
                    editorForm.setStatusUpdatedOn(bib.getStatusUpdatedOn());
                    editorForm.setStaffOnlyFlagForBib(bib.isStaffOnly());
                    editorForm.setTitle(getDublinEditorFormDataHandler().buildDublinUnqTitleField(editorForm, unQualifiedDublinRecord));
                    editorForm.setDublinFieldList(getDublinEditorFormDataHandler().buildDublinUnqEditorFields(editorForm, unQualifiedDublinRecord));
                    BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
                    Map parentCriteria = new HashMap();
                    String bibStatusName = bib.getStatus();
                    parentCriteria.put("bibliographicRecordStatusName", bibStatusName);
                    OleBibliographicRecordStatus bibliographicRecordStatus = boService
                            .findByPrimaryKey(OleBibliographicRecordStatus.class, parentCriteria);
                    editorForm.setOleBibliographicRecordStatus(bibliographicRecordStatus);
                } else if (docFormat.equals(DocFormat.DUBLIN_CORE.getCode())) {
                    WorkBibDublinRecord workBibDublinRecord = new QualifiedDublinRecordHandler().fromXML(bib.getContent());
                    dublinEditorForm.setDublinFieldList(getDublinEditorFormDataHandler().buildDublinEditorFields(editorForm, workBibDublinRecord));
                }
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "record.load.message");
            } catch (Exception e) {
                LOG.error("Error: Could not load Bib record for id" + docId + ",Cause:" + e.getMessage(), e);
                dublinEditorForm.setMessage("Error: Could not load Bib record for id:" + docId);
            }
        } else {
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "dublin.editor.new.message");
        }
        // addDocumentToTree(editorForm);
        dublinEditorForm.setViewId("WorkBibDublinEditorViewPage");
        //dublinEditorForm.setMessage("Please enter details for new Dublin record.");
        editorForm.setHasLink(false);
        return dublinEditorForm;
    }


    /*private void addDocumentToTree(EditorForm editorForm) {
        // TODO: Make sure the same document is not added more than once.
        List<WorkBibDocument> workBibDocumentList = editorForm.getWorkBibDocumentList();
        int indexOfDocument = 0;
        int i = 0;
        String title = "New Bib1";
        boolean updateTitle = false;
        if (null == workBibDocumentList) {
            workBibDocumentList = new ArrayList<WorkBibDocument>();
        }
        workBibDocumentList = new ArrayList<WorkBibDocument>();
        WorkBibDocument tempDocument = new WorkBibDocument();
        if (null == editorForm.getDocId()) {
            // LOG.info("workBibDocumentList size before remove-->" + workBibDocumentList.size());
            for (WorkBibDocument workBibDocument : workBibDocumentList) {
                if (workBibDocument.getTitle().equalsIgnoreCase("New Bib1")) {
                    tempDocument = workBibDocument;
                    // workBibDocumentList.remove(workBibDocument);
                }
            }
            workBibDocumentList.remove(tempDocument);
            // LOG.info("workBibDocumentList size after remove-->" + workBibDocumentList.size());

            WorkBibDocument workBibDocument = new WorkBibDocument();
            workBibDocument.setTitle(title);
            workBibDocumentList.add(workBibDocument);
        } else {
            boolean isUpadated = false;
            for (WorkBibDocument bibDocument : workBibDocumentList) {
                if (bibDocument.getId() != null && bibDocument.getId().equals(editorForm.getDocId())) {
                    isUpadated = true;
                    break;
                } else if (bibDocument.getTitle().equals(title)) {
                    indexOfDocument = i;
                    updateTitle = true;
                    break;
                }
                i++;
            }
            if (!isUpadated) {
                WorkBibDocument workBibDocument = new WorkBibDocument();
                workBibDocument.setId(editorForm.getDocId());
                DocstoreHelperService docstoreHelperService = new DocstoreHelperService();
                workBibDocument = docstoreHelperService.getInfoForBibTree(workBibDocument);
                if (updateTitle) {
                    workBibDocumentList.set(indexOfDocument, workBibDocument);
                } else {
                    workBibDocumentList.add(workBibDocument);
                }
            }

        }
        editorForm.setWorkBibDocumentList(workBibDocumentList);
    }*/

    /*private void updateTreeData(EditorForm editorForm) {
        int indexOfDocument = 0;
        List<WorkBibDocument> workBibDocumentList = editorForm.getWorkBibDocumentList();
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
        workBibDocument.setDocFormat(editorForm.getDocFormat());
        workBibDocument = docstoreHelperService.getInfoForBibTree(workBibDocument);

        workBibDocumentList.set(indexOfDocument, workBibDocument);
        editorForm.setWorkBibDocumentList(workBibDocumentList);
    }*/

    @Override
    public EditorForm deleteDocument(EditorForm editorForm) {
        return new WorkBibDublinForm();
    }

    @Override
    public EditorForm saveDocument(EditorForm editorForm) {
        WorkBibDublinForm dublinEditorForm = (WorkBibDublinForm) editorForm.getDocumentForm();
        String bibliographicRecordStatusCode = editorForm.getOleBibliographicRecordStatus().getBibliographicRecordStatusCode();
        boolean staffOnlyFlag = editorForm.isStaffOnlyFlagForBib();
        String uuid = editorForm.getDocId();
        String bibId =  editorForm.getBibId();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        ResponseDocument responseDocument = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateStr = sdf.format(date);
        String user = GlobalVariables.getUserSession().getLoggedInUserPrincipalName();
        String content = "";
        String editorStatusMessage = "";
        BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
        OleBibliographicRecordStatus bibliographicRecordStatus = null;
        if (bibliographicRecordStatusCode != null) {
            content = getDublinEditorFormDataHandler().buildDublinRecordForDocStore(editorForm, uuid);
            Map parentCriteria = new HashMap();
            parentCriteria.put("bibliographicRecordStatusCode", bibliographicRecordStatusCode);
            bibliographicRecordStatus = boService.findByPrimaryKey(OleBibliographicRecordStatus.class, parentCriteria);
        }
        Bib bib = null;
        try {
            if (uuid != null && !uuid.trim().equals("")) {

                if (boService != null) {

                    bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibId);
                    bib.setStaffOnly(staffOnlyFlag);
                    bib.setContent(content);
                    if (bibliographicRecordStatus != null) {
                        bib.setStatus(bibliographicRecordStatus.getBibliographicRecordStatusName());
                    } else {
                        bib.setStatus("");
                    }
                    getDocstoreClientLocator().getDocstoreClient().updateBib(bib);
                    String bibStatusName = bib.getStatus();
                    editorForm.setCreatedBy(bib.getCreatedBy());
                    editorForm.setCreatedDate(bib.getCreatedOn());
                    editorForm.setUpdatedBy(bib.getUpdatedBy());
                    editorForm.setUpdatedDate(bib.getUpdatedOn());
                    editorForm.setStatusUpdatedOn(bib.getStatusUpdatedOn());
                    editorForm.setStatusUpdatedBy(bib.getStatusUpdatedBy());
                    Map parentCriteria = new HashMap();
                    parentCriteria.put("bibliographicRecordStatusName", bibStatusName);
                    bibliographicRecordStatus = boService.findByPrimaryKey(OleBibliographicRecordStatus.class, parentCriteria);
                    if (bibliographicRecordStatus != null) {
                        editorForm.setOleBibliographicRecordStatus(bibliographicRecordStatus);
                    }
                    editorStatusMessage = "record.update.message";
                }
            } else {
                BibTree bibTree = new BibTree();
                bib = new BibDcUnqualified();
                bib.setCategory(DocCategory.WORK.getCode());
                bib.setType(DocType.BIB.getCode());
                bib.setFormat(org.kuali.ole.docstore.common.document.content.enums.DocFormat.DUBLIN_UNQUALIFIED.getCode());
                bib.setStaffOnly(staffOnlyFlag);
                bib.setContent(content);
                if (bibliographicRecordStatus != null) {
                    bib.setStatus(bibliographicRecordStatus.getBibliographicRecordStatusName());
                } else {
                    bib.setStatus("");
                }
                bibTree.setBib(bib);
                HoldingsTree holdingsTree = getHoldingsTree(editorForm.geteResourceId());
                bibTree.getHoldingsTrees().add(holdingsTree);

                getDocstoreClientLocator().getDocstoreClient().createBib(bib);
                holdingsTree.getHoldings().setBib(bib);
                getDocstoreClientLocator().getDocstoreClient().createHoldingsTree(holdingsTree);
                editorForm.setDocId(bib.getId());
                editorForm.setBibId(bib.getId());
                List<BibTree> bibTreeList = new ArrayList<>();
                bibTreeList.add(bibTree);
                editorForm.setBibTreeList(bibTreeList);
                editorStatusMessage = "record.create.message";
                dublinEditorForm.setUuid("");


            }
        } catch (Exception e) {
            LOG.error("Failed to save record. Cause:" + e.getMessage(), e);
            dublinEditorForm.setDublinFieldList(Arrays.asList(new WorkDublinEditorField()));
            dublinEditorForm.setExistingDublinFieldList(new ArrayList<WorkDublinEditorField>());
            editorStatusMessage = "marc.editor.failure.message";
        }
        dublinEditorForm.setViewId("WorkBibDublinEditorViewPage");
        dublinEditorForm = (WorkBibDublinForm) loadDocument(editorForm);
        GlobalVariables.getMessageMap().getInfoMessages().clear();
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, editorStatusMessage);
        return dublinEditorForm;
    }

    /**
     * This method   will add the dublin record based on the selected Line index and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addField")
    public ModelAndView addDublinField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        WorkBibDublinForm dublinEditorForm = (WorkBibDublinForm) form;
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        index++;
        List<WorkDublinEditorField> dublinEditorFieldList = dublinEditorForm.getDublinFieldList();
        dublinEditorFieldList.add(index, new WorkDublinEditorField());
        //return super.updateComponent(dublinEditorForm, result, request, response);
        return null;
    }

    /**
     * This method will remove the DublinField based on the index position and updates the component.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=removeField")
    public ModelAndView removeDublinField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        WorkBibDublinForm dublinEditorForm = (WorkBibDublinForm) form;
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        String selectedCollection = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        List<WorkDublinEditorField> dublinEditorFieldList = dublinEditorForm.getDublinFieldList();
        List<WorkDublinEditorField> existingEditorFieldList;
        if (dublinEditorFieldList.size() > 1) {
            dublinEditorFieldList.remove(index);
        }
        if (selectedCollection.equalsIgnoreCase("existingDublinFieldList")) {
            existingEditorFieldList = dublinEditorForm.getExistingDublinFieldList();
            if (existingEditorFieldList.size() > 0) {
                existingEditorFieldList.remove(index);
            }
        }
        //        return super.updateComponent(dublinEditorForm, result, request, response);
        return null;
    }

    /**
     * This method will ingest new record to docStore if uuid is null else it will update exisiting record.
     * Once the responce has been received from docStore,then form has been nullified.
     * if response has not received from docStore then form nullified in catch block.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=submit")
    public ModelAndView submit(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        String responseFromDocstore = null;
        WorkBibDublinForm dublinEditorForm = (WorkBibDublinForm) form;
        String uuid = dublinEditorForm.getUuid();
        String content = getDublinEditorFormDataHandler().buildDublinRecordForDocStore(form, uuid);
        try {
            if (uuid != null && !uuid.trim().equals("")) {
                responseFromDocstore = getDocstoreHelperService()
                        .persistToDocstoreForEditor(content, uuid, OLEConstants.UNQUALIFIED_DUBLIN_FORMAT);
            } else {
                responseFromDocstore = getDocstoreHelperService()
                        .persistToDocstoreForEditor(content, null, OLEConstants.UNQUALIFIED_DUBLIN_FORMAT);
            }
            LOG.debug("responseFromDocstore---->" + responseFromDocstore);
            dublinEditorForm.setDublinFieldList(Arrays.asList(new WorkDublinEditorField()));
            dublinEditorForm.setExistingDublinFieldList(new ArrayList<WorkDublinEditorField>());
            dublinEditorForm.setUuid("");
            //dublinEditorForm.setMessage("Record saved successfully.");
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "record.save.message");
        } catch (Exception e) {
            LOG.error("Failed to save record:" + e.getMessage(), e);
            dublinEditorForm.setDublinFieldList(Arrays.asList(new WorkDublinEditorField()));
            dublinEditorForm.setExistingDublinFieldList(new ArrayList<WorkDublinEditorField>());
            //dublinEditorForm.setMessage("Failed to save record.");
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "record.save.fail.message");
        }
        //return getUIFModelAndView(dublinEditorForm, "DublinEditorViewPage");
        return null;
    }

    /**
     * This method will fetch the DublinEditorForm records.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=loadDublinRecord")
    public ModelAndView loadDublinRecord(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) {
        WorkBibDublinForm dublinEditorForm = (WorkBibDublinForm) form;
        String uuid = dublinEditorForm.getUuid();
        try {
            String docStoreData = getDocstoreHelperService().getDocstoreData(uuid);
            UnQualifiedDublinRecord unQualifiedDublinRecord = new UnQualifiedDublinRecordHandler()
                    .fromXML(docStoreData);
            dublinEditorForm.setExistingDublinFieldList(getDublinEditorFormDataHandler()
                    .buildDublinUnqEditorFields(dublinEditorForm,
                            unQualifiedDublinRecord));
            dublinEditorForm.setDublinFieldList(
                    new ArrayList<WorkDublinEditorField>(Arrays.asList(new WorkDublinEditorField())));
            //dublinEditorForm.setUuid("");
        } catch (Exception e) {
            LOG.error("Exception:" + e.getMessage(), e);
        }
        //return getUIFModelAndView(dublinEditorForm, "DublinEditorViewPage");
        return null;
    }

    /**
     * Gets the DublinEditorFormDataHandler attribute.
     *
     * @return Returns DublinEditorFormDataHandler.
     */
    private WorkDublinEditorFormDataHandler getDublinEditorFormDataHandler() {
        if (null == dublinEditorFormDataHandler) {
            dublinEditorFormDataHandler = new WorkDublinEditorFormDataHandler();
        }
        return dublinEditorFormDataHandler;
    }

    /**
     * Gets the docstoreHelperService attribute.
     *
     * @return Returns docstoreHelperService.
     */
    private DocstoreHelperService getDocstoreHelperService() {
        if (null == docstoreHelperService) {
            return new DocstoreHelperService();
        }
        return docstoreHelperService;
    }

    /**
     * Sets the docstoreHelperService attribute value.
     *
     * @param docstoreHelperService
     */
    public void setDocstoreHelperService(DocstoreHelperService docstoreHelperService) {
        this.docstoreHelperService = docstoreHelperService;
    }

    /**
     * used for test case.
     *
     * @param result
     * @param request
     * @param response
     * @param dublinEditorForm
     * @return ModelAndView
     */
    protected ModelAndView callSuper(BindingResult result, HttpServletRequest request, HttpServletResponse response,
                                     WorkBibDublinForm dublinEditorForm) {
        //return super.updateComponent(dublinEditorForm, result, request, response);
        return null;
    }

    @Override
    public EditorForm addORDeleteFields(EditorForm editorForm, HttpServletRequest request) {
        String methodName = request.getParameter("methodToCall");
        if (methodName.equalsIgnoreCase("addField")) {
            WorkBibDublinForm dublinEditorForm = (WorkBibDublinForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            index++;
            List<WorkDublinEditorField> dublinEditorFieldList = editorForm.getDublinFieldList();
            dublinEditorFieldList.add(index, new WorkDublinEditorField());
            dublinEditorForm.setDublinFieldList(dublinEditorFieldList);
            editorForm.setDocumentForm(dublinEditorForm);
        } else if (methodName.equalsIgnoreCase("removeField")) {
            WorkBibDublinForm dublinEditorForm = (WorkBibDublinForm) editorForm.getDocumentForm();
            int index = Integer.parseInt(editorForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
            List<WorkDublinEditorField> editorControlFieldList = editorForm.getDublinFieldList();
            if (editorControlFieldList.size() > 1) {
                editorControlFieldList.remove(index);
            }
            editorForm.setDocumentForm(dublinEditorForm);
        }

        return editorForm;
    }

    @Override
    public EditorForm deleteVerify(EditorForm editorForm) throws Exception {
        return null;
    }

    @Override
    public EditorForm delete(EditorForm editorForm) throws Exception {
        WorkBibDublinForm workBibDublinForm = new WorkBibDublinForm();
        String docId = editorForm.getDocId();
        String operation = "delete";
        getResponseFromDocStore(editorForm, docId, operation);
        return workBibDublinForm;
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

    @Override
    public EditorForm showBibs(EditorForm editorForm) {
        return null;
    }

    public EditorForm copy(EditorForm editorForm) {
        return null;
    }

}
