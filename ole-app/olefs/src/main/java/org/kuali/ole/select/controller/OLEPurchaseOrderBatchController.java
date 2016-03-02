package org.kuali.ole.select.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.document.OLEPurchaseOrderBatchDocument;
import org.kuali.ole.select.document.service.OLEPurchaseOrderBatchService;
import org.kuali.ole.select.form.OLEPurchaseOrderBatchForm;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.UserSession;

import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.TransactionalDocumentControllerBase;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.rice.ksb.messaging.threadpool.KSBThreadPool;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 5/15/15
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/olePurchaseOrderBatchController")
public class OLEPurchaseOrderBatchController extends TransactionalDocumentControllerBase {

    OLEPurchaseOrderBatchService olePurchaseOrderBatchService = (OLEPurchaseOrderBatchService)SpringContext.getService("olePurchaseOrderBatchService");
    private OLEPurchaseOrderBatchDocument purchaseOrderBatchDocument;
    private String principalName;

    @Override
    protected DocumentFormBase createInitialForm(HttpServletRequest request) {
        OLEPurchaseOrderBatchForm olePurchaseOrderBatchForm = new OLEPurchaseOrderBatchForm();
        return olePurchaseOrderBatchForm;
    }

    @RequestMapping(params = "methodToCall=docHandler")
    public ModelAndView docHandler(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEPurchaseOrderBatchForm olePurchaseOrderBatchForm = (OLEPurchaseOrderBatchForm)form;
        ModelAndView modelAndView = super.docHandler(olePurchaseOrderBatchForm,result,request,response);
        return modelAndView;
    }

    @RequestMapping(params = "methodToCall=upload")
    public ModelAndView createFileForPOBA(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response)   {
        OLEPurchaseOrderBatchForm olePurchaseOrderBatchForm = (OLEPurchaseOrderBatchForm)form;
        OLEPurchaseOrderBatchDocument olePurchaseOrderBatchDocument = (OLEPurchaseOrderBatchDocument)olePurchaseOrderBatchForm.getDocument();
        if(olePurchaseOrderBatchDocument.getDocIdIngestFile() == null) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.POBA_VIEW, OLEKeyConstants.INPUT_FILE_REQ);
            return getUIFModelAndView(olePurchaseOrderBatchForm);
        }
        try {
            olePurchaseOrderBatchService.downloadCSV(olePurchaseOrderBatchService.createFileForPOBA(olePurchaseOrderBatchDocument));
            olePurchaseOrderBatchDocument.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.SUCCESSFULLEY_UPLOADED));


        } catch(Exception e) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.POBA_VIEW, OLEKeyConstants.INPUT_FILE_REQ);
            return getUIFModelAndView(olePurchaseOrderBatchForm);
        }
        return getUIFModelAndView(olePurchaseOrderBatchForm);

    }


    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response)   {
        OLEPurchaseOrderBatchForm olePurchaseOrderBatchForm = (OLEPurchaseOrderBatchForm) form;
        OLEPurchaseOrderBatchDocument olePurchaseOrderBatchDocument = (OLEPurchaseOrderBatchDocument) olePurchaseOrderBatchForm.getDocument();
        olePurchaseOrderBatchDocument.setMessage("");
        principalName = GlobalVariables.getUserSession().getPrincipalName();
        final UserSession userSession = new UserSession(principalName);

        olePurchaseOrderBatchDocument.getDocumentHeader().setDocumentDescription(OLEConstants.OLEPurchaseOrderBulkAmendment.DOC_DESC);
        if(GlobalVariables.getUserSession().getPrincipalName() != null) {
            olePurchaseOrderBatchDocument.setPrincipalName(GlobalVariables.getUserSession().getPrincipalName());
        }
        if (olePurchaseOrderBatchDocument.getIngestedFile() == null) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.POBA_VIEW, OLEKeyConstants.INPUT_FILE_REQ);
            return getUIFModelAndView(olePurchaseOrderBatchForm);
        }
        if (olePurchaseOrderBatchDocument.getType() == null) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.POBA_VIEW, OLEKeyConstants.SELECT_PROCESS);
            return getUIFModelAndView(olePurchaseOrderBatchForm);
        }
        if (olePurchaseOrderBatchDocument.getType().equals(OLEConstants.OLEPurchaseOrderBulkAmendment.SPEC_TIME) && (olePurchaseOrderBatchDocument.getBatchStartDate() == null || olePurchaseOrderBatchDocument.getTime() == null)) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.POBA_VIEW, OLEKeyConstants.ERROR_DATE);
            return getUIFModelAndView(olePurchaseOrderBatchForm);
        }
        if(olePurchaseOrderBatchDocument.getType().equals(OLEConstants.OLEPurchaseOrderBulkAmendment.SPEC_TIME) && !(olePurchaseOrderBatchDocument.getTime().length() == 8)) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.POBA_VIEW, OLEKeyConstants.ERROR_TIME_LENGTH);
            return getUIFModelAndView(olePurchaseOrderBatchForm);
        }
        olePurchaseOrderBatchDocument.setUploadFileName(olePurchaseOrderBatchDocument.getDocumentNumber()+"_"+olePurchaseOrderBatchDocument.getIngestedFile().getOriginalFilename());
        if (olePurchaseOrderBatchDocument.getType().equals(OLEConstants.OLEPurchaseOrderBulkAmendment.IMMEDIATE)) {
            KSBThreadPool threadPool = KSBServiceLocator.getThreadPool();
            try {
                File file = new File(System.getProperty("java.io.tmpdir") + "/" + olePurchaseOrderBatchDocument.getIngestedFile().getOriginalFilename());
                olePurchaseOrderBatchDocument.getIngestedFile().transferTo(file);
                threadPool.submit(new POBACallable(olePurchaseOrderBatchDocument, userSession, file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String jobId = olePurchaseOrderBatchDocument.getDocumentNumber();
                String fileDirectory = ConfigContext.getCurrentContextConfig().getProperty(org.kuali.ole.OLEConstants.STAGING_DIRECTORY)+"/"+olePurchaseOrderBatchDocument.getUploadFileName();
                MultipartFile multipartFile = olePurchaseOrderBatchDocument.getIngestedFile();
                multipartFile.transferTo(new File(fileDirectory));
                String date = new SimpleDateFormat(OLEConstants.OLEPurchaseOrderBulkAmendment.FORMAT).format(olePurchaseOrderBatchDocument.getBatchStartDate());
                date += " " + olePurchaseOrderBatchDocument.getTime();
                purchaseOrderBatchDocument = olePurchaseOrderBatchDocument;
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        GlobalVariables.setUserSession(new UserSession(principalName));
                        getOlePurchaseOrderBatchService().readFile(purchaseOrderBatchDocument, userSession, null);
                    }
                }, new SimpleDateFormat(OLEConstants.OLEPurchaseOrderBulkAmendment.TIMESTAMP_FORMAT).parse(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.route(olePurchaseOrderBatchForm,result,request,response);
    }

    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        OLEPurchaseOrderBatchForm olePurchaseOrderBatchForm = (OLEPurchaseOrderBatchForm) form;
        OLEPurchaseOrderBatchDocument olePurchaseOrderBatchDocument = (OLEPurchaseOrderBatchDocument) olePurchaseOrderBatchForm.getDocument();
        olePurchaseOrderBatchDocument.setMessage("");
        principalName = GlobalVariables.getUserSession().getPrincipalName();
        final UserSession userSession = new UserSession(principalName);

        olePurchaseOrderBatchDocument.getDocumentHeader().setDocumentDescription(OLEConstants.OLEPurchaseOrderBulkAmendment.DOC_DESC);
        if(GlobalVariables.getUserSession().getPrincipalName() != null) {
            olePurchaseOrderBatchDocument.setPrincipalName(GlobalVariables.getUserSession().getPrincipalName());
        }
        if (olePurchaseOrderBatchDocument.getIngestedFile() == null) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.POBA_VIEW, OLEKeyConstants.INPUT_FILE_REQ);
            return getUIFModelAndView(olePurchaseOrderBatchForm);
        }
        if (olePurchaseOrderBatchDocument.getType() == null) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.POBA_VIEW, OLEKeyConstants.SELECT_PROCESS);
            return getUIFModelAndView(olePurchaseOrderBatchForm);
        }
        if (olePurchaseOrderBatchDocument.getType().equals(OLEConstants.OLEPurchaseOrderBulkAmendment.SPEC_TIME) && (olePurchaseOrderBatchDocument.getBatchStartDate() == null || olePurchaseOrderBatchDocument.getTime() == null)) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.POBA_VIEW, OLEKeyConstants.ERROR_DATE);
            return getUIFModelAndView(olePurchaseOrderBatchForm);
        }
        if(olePurchaseOrderBatchDocument.getType().equals(OLEConstants.OLEPurchaseOrderBulkAmendment.SPEC_TIME) && !(olePurchaseOrderBatchDocument.getTime().length() == 8)) {
            GlobalVariables.getMessageMap().putError(OleSelectConstant.POBA_VIEW, OLEKeyConstants.ERROR_TIME_LENGTH);
            return getUIFModelAndView(olePurchaseOrderBatchForm);
        }
        olePurchaseOrderBatchDocument.setUploadFileName(olePurchaseOrderBatchDocument.getDocumentNumber()+"_"+olePurchaseOrderBatchDocument.getIngestedFile().getOriginalFilename());
        if (olePurchaseOrderBatchDocument.getType().equals(OLEConstants.OLEPurchaseOrderBulkAmendment.IMMEDIATE)) {
            KSBThreadPool threadPool = KSBServiceLocator.getThreadPool();
            try {
                File file = new File(System.getProperty("java.io.tmpdir") + "/" + olePurchaseOrderBatchDocument.getIngestedFile().getOriginalFilename());
                olePurchaseOrderBatchDocument.getIngestedFile().transferTo(file);
                threadPool.submit(new POBACallable(olePurchaseOrderBatchDocument, userSession, file));
            }
             catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String jobId = olePurchaseOrderBatchDocument.getDocumentNumber();
                String fileDirectory = ConfigContext.getCurrentContextConfig().getProperty(org.kuali.ole.OLEConstants.STAGING_DIRECTORY)+"/"+olePurchaseOrderBatchDocument.getUploadFileName();
                MultipartFile multipartFile = olePurchaseOrderBatchDocument.getIngestedFile();
                multipartFile.transferTo(new File(fileDirectory));
                String date = new SimpleDateFormat(OLEConstants.OLEPurchaseOrderBulkAmendment.FORMAT).format(olePurchaseOrderBatchDocument.getBatchStartDate());
                date += " " + olePurchaseOrderBatchDocument.getTime();
                purchaseOrderBatchDocument = olePurchaseOrderBatchDocument;
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        GlobalVariables.setUserSession(new UserSession(principalName));
                        getOlePurchaseOrderBatchService().readFile(purchaseOrderBatchDocument, userSession, null);
                    }
                }, new SimpleDateFormat(OLEConstants.OLEPurchaseOrderBulkAmendment.TIMESTAMP_FORMAT).parse(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.blanketApprove(olePurchaseOrderBatchForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=approveDocument")
    public ModelAndView approveDocument(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        principalName = GlobalVariables.getUserSession().getPrincipalName();
        final UserSession userSession = new UserSession(principalName);

        OLEPurchaseOrderBatchForm olePurchaseOrderBatchForm = (OLEPurchaseOrderBatchForm)form;
        OLEPurchaseOrderBatchDocument olePurchaseOrderBatchDocument = (OLEPurchaseOrderBatchDocument)olePurchaseOrderBatchForm.getDocument();
        olePurchaseOrderBatchDocument.getDocumentHeader().setDocumentDescription(OLEConstants.OLEPurchaseOrderBulkAmendment.DOC_DESC);
        GlobalVariables.setUserSession(new UserSession(olePurchaseOrderBatchDocument.getPrincipalName()));
        if(olePurchaseOrderBatchDocument.getType().equals(OLEConstants.OLEPurchaseOrderBulkAmendment.IMMEDIATE)) {
            KSBThreadPool threadPool = KSBServiceLocator.getThreadPool();
            try {
                File file = new File(System.getProperty("java.io.tmpdir") + "/" + olePurchaseOrderBatchDocument.getIngestedFile().getOriginalFilename());
                olePurchaseOrderBatchDocument.getIngestedFile().transferTo(file);
                threadPool.submit(new POBACallable(olePurchaseOrderBatchDocument, userSession, file));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String date = new SimpleDateFormat(OLEConstants.OLEPurchaseOrderBulkAmendment.FORMAT).format(olePurchaseOrderBatchDocument.getBatchStartDate());
                date += " "+olePurchaseOrderBatchDocument.getTime();
                purchaseOrderBatchDocument =  olePurchaseOrderBatchDocument;
                Timer t=new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        GlobalVariables.setUserSession(new UserSession(purchaseOrderBatchDocument.getPrincipalName()));
                        getOlePurchaseOrderBatchService().readFile(purchaseOrderBatchDocument, userSession, null);
                    }
                }, new SimpleDateFormat(OLEConstants.OLEPurchaseOrderBulkAmendment.TIMESTAMP_FORMAT).parse(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.approve(olePurchaseOrderBatchForm, result, request, response);
        return closeDocument(olePurchaseOrderBatchForm, result, request, response);

    }

    @RequestMapping(params = "methodToCall=" + "closeDocument")
    public ModelAndView closeDocument(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {

        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(org.kuali.ole.OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + "/portal.do";
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        if (org.apache.commons.lang.StringUtils.isNotBlank(form.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, form.getReturnFormKey());
        }
        GlobalVariables.getUifFormManager().removeSessionForm(form);
        return performRedirect(form, url, props);
    }

    public OLEPurchaseOrderBatchService getOlePurchaseOrderBatchService() {
        if(olePurchaseOrderBatchService == null) {
            olePurchaseOrderBatchService = (OLEPurchaseOrderBatchService)SpringContext.getService("olePurchaseOrderBatchService");
        }
        return olePurchaseOrderBatchService;
    }

    @RequestMapping(params = "methodToCall=disapprove")
    public ModelAndView disapprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEPurchaseOrderBatchForm olePurchaseOrderBatchForm = (OLEPurchaseOrderBatchForm) form;
        OLEPurchaseOrderBatchDocument olePurchaseOrderBatchDocument = (OLEPurchaseOrderBatchDocument) olePurchaseOrderBatchForm.getDocument();

        Note noteObj = getDocumentService().createNoteFromDocument(olePurchaseOrderBatchDocument, "Dispproved at Exceptione By : " + GlobalVariables.getUserSession().getPerson().getName());
        PersistableBusinessObject noteParent = olePurchaseOrderBatchDocument.getNoteTarget();
        List<Note> noteList = getNoteService().getByRemoteObjectId(noteParent.getObjectId());
        noteList.add(noteObj);
        getNoteService().saveNoteList(noteList);
        getNoteService().save(noteObj);
        olePurchaseOrderBatchDocument.setNotes(noteList);
        getDocumentService().saveDocument(olePurchaseOrderBatchDocument);

        performWorkflowAction(olePurchaseOrderBatchForm, UifConstants.WorkflowAction.DISAPPROVE, true);
        return returnToPrevious(olePurchaseOrderBatchForm);

    }

}
