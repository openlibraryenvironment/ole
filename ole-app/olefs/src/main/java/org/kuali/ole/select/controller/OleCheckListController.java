package org.kuali.ole.select.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleCheckListBo;
import org.kuali.ole.service.OleCheckListMaintenanceDocumentService;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.bo.PersistableAttachment;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceUtils;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * OleCheckListController is the controller class for CheckList Maintenance Document.
 */
@Controller
@RequestMapping(value = "/oleCheckListMaintenance")
public class OleCheckListController extends MaintenanceDocumentController {
    protected static final Logger LOG = Logger.getLogger(OleCheckListController.class);

    /**
     * This method is used to download the attachment.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=downloadAttachment")
    public ModelAndView downloadAttachment(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {

        String checkListId = request.getParameter("oleCheckListId");
        Map criteriaMap = new HashMap();
        criteriaMap.put("oleCheckListId", checkListId);
        OleCheckListBo checkListBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleCheckListBo.class, criteriaMap);
        String fileName = "";
        String objectId = "";
        if (checkListBo != null) {
            fileName = checkListBo.getFileName();
            objectId = checkListBo.getObjectId();
        }
        String directory = getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + File.separator + "checkList" + File.separator + checkListBo.getRemoteObjectIdentifier();
        if (LOG.isInfoEnabled()) {
            LOG.info("file location : " + directory);
        }

        try {
            File file = new File(directory);
            response.setContentType(checkListBo.getMimeType());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentLength((int) file.length());
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(fis, response.getOutputStream());
        } catch (IOException e) {

        }

        return null;
    }

    /**
     * This method invokes setupMaintenanceForDelete method to populate the document for deleting.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=" + "maintenanceDelete")
    public ModelAndView maintenanceDelete(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        setupMaintenanceForDelete(form, request, "Delete");
        return getUIFModelAndView(form);
    }

    /**
     * This method invokes deleteAttachment method to delete attachment and set the error accordingly ..
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=" + "deleteDocument")
    public ModelAndView deleteDocument(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocument document = form.getDocument();
        OleCheckListBo oleCheckListBo = new OleCheckListBo();
        if (document.getDocumentDataObject() != null) {
            oleCheckListBo = (OleCheckListBo) document.getDocumentDataObject();
            if (oleCheckListBo != null && oleCheckListBo.getRemoteObjectIdentifier() != null) {
                boolean isDeleted = deleteAttachment(oleCheckListBo);
                if (isDeleted) {
                    KRADServiceLocator.getBusinessObjectService().delete(oleCheckListBo);
                    GlobalVariables.getMessageMap().putInfoWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                            OLEConstants.OleLicenseRequest.MSG_DELETE_DOC);
                } else {
                    GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_MESSAGES, OLEConstants.OleLicenseRequest.ERROR_FILE_NOT_FOUND);
                }
            } else {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_MESSAGES, OLEConstants.OleLicenseRequest.ERROR_CHECKLIST_NOT_FOUND);
            }
        }
        return back(form, result, request, response);
    }

    /**
     * This method populates confirmation to delete the document.
     *
     * @param form
     * @param request
     * @param maintenanceAction
     */
    protected void setupMaintenanceForDelete(MaintenanceDocumentForm form, HttpServletRequest request, String maintenanceAction) {
        MaintenanceDocument document = form.getDocument();
        if (document == null) {
            document = getMaintenanceDocumentService()
                    .setupNewMaintenanceDocument(form.getDataObjectClassName(), form.getDocTypeName(),
                            maintenanceAction);

            form.setDocument(document);
            form.setDocTypeName(document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
        }

        form.setMaintenanceAction(maintenanceAction);
        getMaintenanceDocumentService().setupMaintenanceObjectForDelete(document, maintenanceAction, request.getParameterMap());
        MaintenanceUtils.checkForLockingDocument(document, false);
    }

    /**
     * This method is used to delete the attachment(checklist template) file.
     *
     * @param oleCheckListBo
     * @return boolean
     */
    protected boolean deleteAttachment(OleCheckListBo oleCheckListBo) {
        if (oleCheckListBo != null) {
            String fullPathUniqueFileName = getKualiConfigurationService().getPropertyValueAsString(
                    KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + File.separator + "checkList" + File.separator + oleCheckListBo.getRemoteObjectIdentifier();
            File attachmentFile = new File(fullPathUniqueFileName);
            return attachmentFile.delete();
        }
        return false;
    }

    @Override
    protected OleCheckListMaintenanceDocumentService getMaintenanceDocumentService() {
        return GlobalResourceLoader.getService("oleCheckListMaintenanceDocumentService");
    }

    public ConfigurationService getKualiConfigurationService() {
        return GlobalResourceLoader.getService("kualiConfigurationService");
    }

    /**
     * This method store the uploaded checklist template in the specified path and also populates the document
     * for routing
     *
     * @param docForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase docForm, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {

        ModelAndView modelAndView;
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) docForm;
        MultipartFile attachmentFile = form.getAttachmentFile();
        MaintenanceDocument document = form.getDocument();
        OleCheckListBo oleCheckListBo = (OleCheckListBo) document.getNewMaintainableObject().getDataObject();
        OleCheckListBo oleCheckListBoOld = (OleCheckListBo) document.getOldMaintainableObject().getDataObject();
        Person kualiUser = GlobalVariables.getUserSession().getPerson();
        if (kualiUser!=null){
            oleCheckListBo.setAuthor(kualiUser.getPrincipalId());
        }
        oleCheckListBo.setLastModified((Timestamp) CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp());
        if (form.getMaintenanceAction().equals("New")) {
            try {
                if (oleCheckListBo.getFileName() == null) {
                    if ((attachmentFile.getSize() == 0 || (attachmentFile.getOriginalFilename().isEmpty() ||
                            attachmentFile.getOriginalFilename().equalsIgnoreCase("")))) {
                        GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                                RiceKeyConstants.ERROR_UPLOADFILE_EMPTY, "");
                        modelAndView = getUIFModelAndView(form);
                        return modelAndView;
                    } else {
                        oleCheckListBo.setFileName(attachmentFile.getOriginalFilename());
                        oleCheckListBo.setMimeType(attachmentFile.getContentType());
                        String remoteObjectIdentifier = UUID.randomUUID().toString();
                        if (storeAttachment(attachmentFile, remoteObjectIdentifier))
                            oleCheckListBo.setRemoteObjectIdentifier(remoteObjectIdentifier);
                    }
                }
            } catch (Exception e) {
                LOG.error("EXception while storing the attachment "+e);
            }
        }
        if (form.getMaintenanceAction().equals("Edit")) {
            try {
                if (!(attachmentFile.getOriginalFilename().isEmpty() ||
                        attachmentFile.getOriginalFilename().equalsIgnoreCase(""))) {
                    if (attachmentFile.getSize() == 0) {
                        GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                                RiceKeyConstants.ERROR_UPLOADFILE_EMPTY, attachmentFile.getOriginalFilename());
                    } else {
                        oleCheckListBo.setFileName(attachmentFile.getOriginalFilename());
                        oleCheckListBo.setMimeType(attachmentFile.getContentType());
                        String remoteObjectIdentifier = UUID.randomUUID().toString();
                        if (storeAttachment(attachmentFile, remoteObjectIdentifier))
                            deleteAttachment(oleCheckListBoOld);
                        oleCheckListBo.setRemoteObjectIdentifier(remoteObjectIdentifier);
                    }
                }
            } catch (Exception e) {
                LOG.error("EXception while storing the attachment "+e);
            }
        }


        modelAndView = super.route(form, result, request, response);

        if (document.getNewMaintainableObject().getDataObject() instanceof PersistableAttachment) {
            PersistableAttachment bo = (PersistableAttachment) getBusinessObjectService()
                    .retrieve((PersistableBusinessObject) document.getNewMaintainableObject().getDataObject());
            request.setAttribute("fileName", bo.getFileName());
        }

        modelAndView = getUIFModelAndView(form);

        return modelAndView;
    }


    /**
     * Performs the blanket approve workflow action on the form document instance
     *
     * @param docForm - document form base containing the document instance that will be blanket approved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=blanketApprove")
    public ModelAndView blanketApprove(@ModelAttribute("KualiForm") DocumentFormBase docForm, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView;
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) docForm;
        MultipartFile attachmentFile = form.getAttachmentFile();
        MaintenanceDocument document = form.getDocument();
        OleCheckListBo oleCheckListBo = (OleCheckListBo) document.getNewMaintainableObject().getDataObject();
        OleCheckListBo oleCheckListBoOld = (OleCheckListBo) document.getOldMaintainableObject().getDataObject();
        Person kualiUser = GlobalVariables.getUserSession().getPerson();
        if (kualiUser != null) {
            oleCheckListBo.setAuthor(kualiUser.getPrincipalId());
        }
        oleCheckListBo.setLastModified((Timestamp) CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp());
        if (form.getMaintenanceAction().equals("New")) {
            try {
                if (oleCheckListBo.getFileName() == null) {
                    if ((attachmentFile.getSize() == 0 || (attachmentFile.getOriginalFilename().isEmpty() ||
                            attachmentFile.getOriginalFilename().equalsIgnoreCase("")))) {
                        GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                                RiceKeyConstants.ERROR_UPLOADFILE_EMPTY, "");
                        modelAndView = getUIFModelAndView(form);
                        return modelAndView;
                    } else {
                        oleCheckListBo.setFileName(attachmentFile.getOriginalFilename());
                        oleCheckListBo.setMimeType(attachmentFile.getContentType());
                        String remoteObjectIdentifier = UUID.randomUUID().toString();
                        if (storeAttachment(attachmentFile, remoteObjectIdentifier))
                            oleCheckListBo.setRemoteObjectIdentifier(remoteObjectIdentifier);
                    }
                }

            } catch (Exception e) {
                LOG.error("EXception while storing the attachment "+e);
            }
        }
        if (form.getMaintenanceAction().equals("Edit")) {
            try {
                if (!(attachmentFile.getOriginalFilename().isEmpty() ||
                        attachmentFile.getOriginalFilename().equalsIgnoreCase(""))) {
                    if (attachmentFile.getSize() == 0) {
                        GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                                RiceKeyConstants.ERROR_UPLOADFILE_EMPTY, attachmentFile.getOriginalFilename());
                    } else {
                        oleCheckListBo.setFileName(attachmentFile.getOriginalFilename());
                        oleCheckListBo.setMimeType(attachmentFile.getContentType());
                        String remoteObjectIdentifier = UUID.randomUUID().toString();
                        if (storeAttachment(attachmentFile, remoteObjectIdentifier))
                            deleteAttachment(oleCheckListBoOld);
                        oleCheckListBo.setRemoteObjectIdentifier(remoteObjectIdentifier);
                    }
                }
            } catch (Exception e) {
                LOG.error("EXception while storing the attachment "+e);
            }
        }


        modelAndView = super.blanketApprove(form, result, request, response);

        if (document.getNewMaintainableObject().getDataObject() instanceof PersistableAttachment) {
            PersistableAttachment bo = (PersistableAttachment) getBusinessObjectService()
                    .retrieve((PersistableBusinessObject) document.getNewMaintainableObject().getDataObject());
            request.setAttribute("fileName", bo.getFileName());
        }

        modelAndView = getUIFModelAndView(form);

        return modelAndView;
    }

    /**
     * This method stores uploaded checklist template in the specified path.
     *
     * @param attachedFile
     * @param fileName
     * @return boolean
     * @throws java.io.IOException
     */
    public boolean storeAttachment(MultipartFile attachedFile, String fileName) throws IOException {
        String location = null;
        /*if(objectId != null) {
            location  = getKualiConfigurationService().getPropertyValueAsString(
                    KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY)+ File.separator+"checkList"+File.separator+objectId;
        }
        else {*/
        location = getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + File.separator + "checkList";
        //}
        if (LOG.isInfoEnabled()) {
            LOG.info("file location : " + location);
        }
        File dirLocation = new File(location);
        if (!dirLocation.exists()) {
            boolean success = dirLocation.mkdirs();
            if (!success) {
                LOG.error("Could not generate directory for File at: " + dirLocation.getAbsolutePath());
                return false;
            }
        }
        location = location + File.separator + fileName;
        File fileOut = new File(location);
        FileOutputStream streamOut = null;
        BufferedOutputStream bufferedStreamOut = null;
        try {
            InputStream fileContent = attachedFile.getInputStream();
            streamOut = new FileOutputStream(fileOut);
            bufferedStreamOut = new BufferedOutputStream(streamOut);
            int c;
            while ((c = fileContent.read()) != -1) {
                bufferedStreamOut.write(c);
            }
        } finally {
            bufferedStreamOut.close();
            streamOut.close();
        }
        return true;
    }

    /**
     * Saves the document instance contained on the form
     *
     * @param docForm - document form base containing the document instance that will be saved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase docForm, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView;
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) docForm;
        MultipartFile attachmentFile = form.getAttachmentFile();
        MaintenanceDocument document = form.getDocument();
        OleCheckListBo oleCheckListBo = (OleCheckListBo) document.getNewMaintainableObject().getDataObject();
        OleCheckListBo oleCheckListBoOld = (OleCheckListBo) document.getOldMaintainableObject().getDataObject();
        Person kualiUser = GlobalVariables.getUserSession().getPerson();
        if (kualiUser != null) {
            oleCheckListBo.setAuthor(kualiUser.getPrincipalId());
        }
        oleCheckListBo.setLastModified((Timestamp) CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp());
        if (form.getMaintenanceAction().equals("New")) {
            try {
                if ((attachmentFile.getSize() == 0 || (attachmentFile.getOriginalFilename().isEmpty() ||
                        attachmentFile.getOriginalFilename().equalsIgnoreCase("")))) {
                    GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                            RiceKeyConstants.ERROR_UPLOADFILE_EMPTY, "");
                    modelAndView = getUIFModelAndView(form);
                    return modelAndView;
                } else {
                    oleCheckListBo.setFileName(attachmentFile.getOriginalFilename());
                    oleCheckListBo.setMimeType(attachmentFile.getContentType());
                    String remoteObjectIdentifier = UUID.randomUUID().toString();
                    if (storeAttachment(attachmentFile, remoteObjectIdentifier))
                        oleCheckListBo.setRemoteObjectIdentifier(remoteObjectIdentifier);
                }
            } catch (Exception e) {
                LOG.error("EXception while storing the attachment "+e);
            }
        }
        if (form.getMaintenanceAction().equals("Edit")) {
            try {
                if (!(attachmentFile.getOriginalFilename().isEmpty() ||
                        attachmentFile.getOriginalFilename().equalsIgnoreCase(""))) {
                    if (attachmentFile.getSize() == 0) {
                        GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                                RiceKeyConstants.ERROR_UPLOADFILE_EMPTY, attachmentFile.getOriginalFilename());
                    } else {
                        oleCheckListBo.setFileName(attachmentFile.getOriginalFilename());
                        oleCheckListBo.setMimeType(attachmentFile.getContentType());
                        String remoteObjectIdentifier = UUID.randomUUID().toString();
                        if (storeAttachment(attachmentFile, remoteObjectIdentifier))
                            deleteAttachment(oleCheckListBoOld);
                        oleCheckListBo.setRemoteObjectIdentifier(remoteObjectIdentifier);
                    }
                }
            } catch (Exception e) {
                LOG.error("EXception while storing the attachment "+e);
            }
        }


        return super.save(form, result, request, response);
    }

}
