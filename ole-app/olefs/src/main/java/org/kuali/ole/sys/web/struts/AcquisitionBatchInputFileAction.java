/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.sys.web.struts;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.kuali.ole.select.businessobject.OleLoadFailureRecords;
import org.kuali.ole.select.businessobject.OleLoadSumRecords;
import org.kuali.ole.select.document.AcquisitionBatchInputFileDocument;
import org.kuali.ole.select.service.BatchLoadService;
import org.kuali.ole.select.service.impl.BatchLoadServiceImpl;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.batch.BatchInputFileType;
import org.kuali.ole.sys.batch.BatchSpringContext;
import org.kuali.ole.sys.businessobject.AcquisitionBatchUpload;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.exception.FileStorageException;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.IdentityManagementService;
import org.kuali.rice.kns.question.ConfirmationQuestion;
import org.kuali.rice.kns.web.struts.action.KualiTransactionalDocumentActionBase;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;



/**
 * Handles actions from the batch upload screen.
 */
public class AcquisitionBatchInputFileAction extends KualiTransactionalDocumentActionBase {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AcquisitionBatchInputFileAction.class);
    private IdentityManagementService identityManagementService;

    public AcquisitionBatchInputFileAction()
    {
        super();
    }

    /**
     * @see org.kuali.rice.kns.web.struts.action.KualiAction#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = super.execute(mapping, form, request, response);
        setupForm((AcquisitionBatchInputFileForm) form);
        return forward;
    }

    @Override
    protected void checkAuthorization(ActionForm form, String methodToCall) throws AuthorizationException {
        AcquisitionBatchUpload acquisitionBatchUpload = ((AcquisitionBatchInputFileForm) form).getAcquisitionBatchUpload();
        acquisitionBatchUpload.setBatchInputTypeName(OLEConstants.ORD_FILE_TYPE_INDENTIFIER);
        BatchInputFileType batchInputFileType = retrieveBatchInputFileTypeImpl(acquisitionBatchUpload.getBatchInputTypeName());
        HashMap<String,String> permissionDetails = new HashMap<String,String>();
        permissionDetails.put(KimConstants.AttributeConstants.NAMESPACE_CODE, getNamespaceCode(batchInputFileType.getClass()));
        permissionDetails.put(KimConstants.AttributeConstants.BEAN_NAME, acquisitionBatchUpload.getBatchInputTypeName());
        if (!getIdentityManagementService().isAuthorizedByTemplateName(GlobalVariables.getUserSession().getPrincipalId(), KRADConstants.KNS_NAMESPACE, OLEConstants.PermissionTemplate.UPLOAD_BATCH_INPUT_FILES.name, permissionDetails, null)) {
            throw new AuthorizationException(GlobalVariables.getUserSession().getPrincipalName(), methodToCall, acquisitionBatchUpload.getBatchInputTypeName());
        }
        }

    /**
     * Forwards to the batch upload JSP. Initial request.
     */
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.docHandler(mapping, form, request, response);
    }

    /**
     * Retrieves a BatchInputFileType implementation from Spring based on the given name.
     */
    private BatchInputFileType retrieveBatchInputFileTypeImpl(String batchInputTypeName) {
        BatchInputFileType batchInputType = BatchSpringContext.getBatchInputFileType(batchInputTypeName);
        if (batchInputType == null) {
            LOG.error("Batch input type implementation not found for id " + batchInputTypeName);
            throw new RuntimeException(("Batch input type implementation not found for id " + batchInputTypeName));
        }

        return batchInputType;
    }

   /**
     * Builds list of filenames that the user has permission to manage, and populates the form member. Sets the title key from the
     * batch input type.
     */

    private void setupForm(AcquisitionBatchInputFileForm form) {
        BatchInputFileType batchInputFileType = retrieveBatchInputFileTypeImpl(form.getAcquisitionBatchUpload().getBatchInputTypeName());

        if (batchInputFileType == null) {
            LOG.error("Batch input type implementation not found for id " + form.getAcquisitionBatchUpload().getBatchInputTypeName());
            throw new RuntimeException(("Batch input type implementation not found for id " + form.getAcquisitionBatchUpload().getBatchInputTypeName()));
        }
       /*ParameterService parmeterService =  SpringContext.getBean(ParameterService.class);
        String url = parmeterService.getParameterValue(BatchUpload.class,OLEConstants.BATCH_UPLOAD_HELP_SYS_PARAM_NAME, batchInputFileType.getFileTypeIdentifer());
        form.setUrl(url);*/
          // set title key
        form.setTitleKey(batchInputFileType.getTitleKey());
    }

    /**
     *
     * This method... Processing the Upload file
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */

    public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

       LOG.debug("Start-- upload Method of AcquisitionBatchInputFileAction");

        try {
                AcquisitionBatchInputFileForm acquisitionBatchInputFileForm=(AcquisitionBatchInputFileForm)form;
                String docNumber=acquisitionBatchInputFileForm.getDocument().getDocumentNumber();
                AcquisitionBatchInputFileDocument acquisitionBatchInputFileDocument=(AcquisitionBatchInputFileDocument)acquisitionBatchInputFileForm.getDocument();
                AcquisitionBatchUpload acquisitionBatchUpload = acquisitionBatchInputFileForm.getAcquisitionBatchUpload();
                FormFile uploadedFile = acquisitionBatchInputFileForm.getUploadFile();
                BatchLoadService batchLoadService=(BatchLoadServiceImpl)SpringContext.getBean(BatchLoadServiceImpl.class);
                long filesize=batchLoadService.getFileSize(uploadedFile);
                if(acquisitionBatchUpload.getBatchLoadProfile()==null || "".equals(acquisitionBatchUpload.getBatchLoadProfile())){
                    GlobalVariables.getMessageMap().putError(OLEConstants.GLOBAL_ERRORS, OLEKeyConstants.ERROR_BATCH_LOAD_PROFILE, new String[] {});
                    return mapping.findForward(OLEConstants.MAPPING_BASIC);
                }
                if (uploadedFile == null || uploadedFile.getInputStream() == null || uploadedFile.getInputStream().available() == 0) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.GLOBAL_ERRORS, OLEKeyConstants.ERROR_BATCH_UPLOAD_NO_FILE_SELECTED_SAVE, new String[] {});
                    return mapping.findForward(OLEConstants.MAPPING_BASIC);
                }else if(batchLoadService.fileSizeValidation(filesize)){
                    return mapping.findForward(OLEConstants.MAPPING_BASIC);
                }
                acquisitionBatchInputFileForm.setFileName(uploadedFile.getFileName());
                OleLoadSumRecords oleLoadSumRecords = new OleLoadSumRecords();
                oleLoadSumRecords.setDocumentNumber(docNumber);
                KRADServiceLocatorWeb.getDocumentService().saveDocument(acquisitionBatchInputFileDocument);
                List<OleLoadFailureRecords> oleLoadFailureRecordsList=new ArrayList<OleLoadFailureRecords>(0);
                boolean schemaFlag=acquisitionBatchInputFileDocument.prepareForLoadSummary(uploadedFile.getInputStream(),oleLoadFailureRecordsList,oleLoadSumRecords,uploadedFile,acquisitionBatchUpload);
                if(!schemaFlag){
                    GlobalVariables.getMessageMap().putError(OLEConstants.GLOBAL_ERRORS, OLEKeyConstants.ERROR_BATCH_LOAD_SCHEMA, new String[] {});
                    return mapping.findForward(OLEConstants.MAPPING_BASIC);
                }
                acquisitionBatchInputFileForm.setOleLoadSumRecords(oleLoadSumRecords);
                acquisitionBatchInputFileForm.setOleLoadFailureRecordsList(oleLoadFailureRecordsList);
                if(oleLoadFailureRecordsList.size()>0){
                    FormFile formFile=getFormFileForAttachment(oleLoadSumRecords.getAcqLoadSumId());
                    acquisitionBatchInputFileForm.setAttachmentFile(formFile);
                    acquisitionBatchInputFileForm.getNewNote().setNoteText(OLEConstants.BATCH_FAILURE_ATCH_NAME);
                    super.insertBONote(mapping, form, request, response);
                }
               super.save(mapping, acquisitionBatchInputFileForm, request, response);
           }

         catch (FileStorageException e1) {
            LOG.error("errors saving xml " + e1.getMessage(), e1);
            GlobalVariables.getMessageMap().putError(OLEConstants.GLOBAL_ERRORS, OLEKeyConstants.ERROR_BATCH_UPLOAD_SAVE, new String[] { e1.getMessage() });
        }

         LOG.debug("End -- upload Method of AcquisitionBatchInputFileAction");

        return mapping.findForward(OLEConstants.MAPPING_VIEW);
    }
/**
     *
     * This method... for clearing the data
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward clearValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Start -- clearValues Method of AcquisitionBatchInputFileAction");
        AcquisitionBatchInputFileForm acquisitionBatchInputFileForm = (AcquisitionBatchInputFileForm) form;
        acquisitionBatchInputFileForm.getAcquisitionBatchUpload().setBatchLoadProfile(null);
        acquisitionBatchInputFileForm.setUploadFile(null);
        acquisitionBatchInputFileForm.getAcquisitionBatchUpload().setBatchDescription(null);
        LOG.debug("End -- clearValues Method of AcquisitionBatchInputFileAction");
        return mapping.findForward(OLEConstants.MAPPING_BASIC);
    }
    /**
     *
     * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#insertBONote(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward insertBONote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        super.insertBONote(mapping, form, request, response);
        return mapping.findForward(OLEConstants.MAPPING_VIEW);
    }
    /**
     *
     * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#deleteBONote(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward deleteBONote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.deleteBONote(mapping, form, request, response);
        return mapping.findForward(OLEConstants.MAPPING_VIEW);
    }
    /**
     *
     * This method uses for retrieve the load summary information uses the load summary id
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getLoadSummaryReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Start -- getLoadSummaryReports Method of AcquisitionBatchInputFileAction");
        // super.docHandler(mapping, form, request, response);
        AcquisitionBatchInputFileForm acquisitionBatchInputFileForm = (AcquisitionBatchInputFileForm) form;
        AcquisitionBatchInputFileDocument acquisitionBatchInputFileDocument = (AcquisitionBatchInputFileDocument) acquisitionBatchInputFileForm.getDocument();
        OleLoadSumRecords oleLoadSumRecords = new OleLoadSumRecords();
        List<OleLoadFailureRecords> oleLoadFailureRecordsList = new ArrayList<OleLoadFailureRecords>(0);
        oleLoadSumRecords = acquisitionBatchInputFileDocument.getLoadSummaryAndFailureRecordsByLoadSummaryId(acquisitionBatchInputFileForm.getOleLoadSumRecords().getAcqLoadSumId(), oleLoadFailureRecordsList);
        acquisitionBatchInputFileForm.setOleLoadSumRecords(oleLoadSumRecords);
        acquisitionBatchInputFileForm.setOleLoadFailureRecordsList(oleLoadFailureRecordsList);
        attachBONote(mapping, acquisitionBatchInputFileForm, request, response, oleLoadSumRecords.getAcqLoadSumId(),oleLoadFailureRecordsList.size());
        return mapping.findForward(OLEConstants.MAPPING_VIEW);
    }
   /**
    *
    * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#docHandler(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
    @Override
    public ActionForward docHandler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Start -- docHandler Method of AcquisitionBatchInputFileAction");
        super.docHandler(mapping, form, request, response);
        AcquisitionBatchInputFileForm acquisitionBatchInputFileForm = (AcquisitionBatchInputFileForm) form;
        AcquisitionBatchInputFileDocument acquisitionBatchInputFileDocument = (AcquisitionBatchInputFileDocument) acquisitionBatchInputFileForm.getDocument();
        OleLoadSumRecords oleLoadSumRecords = new OleLoadSumRecords();
        List<OleLoadFailureRecords> oleLoadFailureRecordsList = new ArrayList<OleLoadFailureRecords>(0);
        oleLoadSumRecords = acquisitionBatchInputFileDocument.getLoadSummaryAndFailureRecordsByDocId(acquisitionBatchInputFileForm.getDocId(), oleLoadFailureRecordsList);
        acquisitionBatchInputFileForm.setOleLoadSumRecords(oleLoadSumRecords);
        acquisitionBatchInputFileForm.setOleLoadFailureRecordsList(oleLoadFailureRecordsList);
        attachBONote(mapping, acquisitionBatchInputFileForm, request, response, oleLoadSumRecords.getAcqLoadSumId(),oleLoadFailureRecordsList.size());
        return mapping.findForward(OLEConstants.MAPPING_VIEW);
    }
   /**
    *
    * This method uses for attaching the add note information to batch upload screen
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @param acqLoadSumId
    * @throws Exception
    */
   public void attachBONote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response,int acqLoadSumId,int failureCount)throws Exception {
      try {
               AcquisitionBatchInputFileForm acquisitionBatchInputFileForm=(AcquisitionBatchInputFileForm)form;
               if(acquisitionBatchInputFileForm.getDocument().getNotes().size()<=0)
               {
                   FormFile formFile=getFormFileForAttachment(acqLoadSumId+OLEConstants.BATCH_FAILURE_BIB_FILE_ETN);
                   if(formFile!=null){
                               acquisitionBatchInputFileForm.setAttachmentFile(formFile);
                               if(failureCount==0)
                                   acquisitionBatchInputFileForm.getNewNote().setNoteText(OLEConstants.BATCH_FAILURE_EXTRA_ATCH_NAME_BIB);
                               else
                                   acquisitionBatchInputFileForm.getNewNote().setNoteText(OLEConstants.BATCH_FAILURE_ATCH_NAME_BIB);
                               super.insertBONote(mapping, form, request, response);
                   }
                   formFile=getFormFileForAttachment(acqLoadSumId+OLEConstants.BATCH_FAILURE_EDI_FILE_ETN);
                   if(formFile!=null){
                               acquisitionBatchInputFileForm.setAttachmentFile(formFile);
                               if(failureCount==0)
                                  acquisitionBatchInputFileForm.getNewNote().setNoteText(OLEConstants.BATCH_FAILURE_EXTRA_ATCH_NAME_EDI);
                               else
                                   acquisitionBatchInputFileForm.getNewNote().setNoteText(OLEConstants.BATCH_FAILURE_ATCH_NAME_EDI); 
                               super.insertBONote(mapping, form, request, response);
                   }
               }
          }
        catch (Exception ex) {
            LOG.error("error while attaching BONote " + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

   }
   /**
    *
    * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#route(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
public ActionForward route(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
   {
        super.route(mapping, form, request, response);
        return mapping.findForward(OLEConstants.MAPPING_VIEW);
    }
   /**
    *
    * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#save(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
   {
       super.save(mapping, form, request, response);
       return mapping.findForward(OLEConstants.MAPPING_VIEW);
    }
   /**
    *
    * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#cancel(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
   {
         Object question = request.getParameter(KRADConstants.QUESTION_INST_ATTRIBUTE_NAME);
         if (question == null) {
                return this.performQuestionWithoutInput(mapping, form, request, response, KRADConstants.DOCUMENT_CANCEL_QUESTION, getKualiConfigurationService().getPropertyValueAsString("document.question.cancel.text"), KRADConstants.CONFIRMATION_QUESTION, KRADConstants.MAPPING_CANCEL, "");
         }
         else {
            Object buttonClicked = request.getParameter(KRADConstants.QUESTION_CLICKED_BUTTON);
            if ((KRADConstants.DOCUMENT_CANCEL_QUESTION.equals(question)) && ConfirmationQuestion.NO.equals(buttonClicked)) {
                 return mapping.findForward(OLEConstants.MAPPING_VIEW);
             }
          }
         KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
         doProcessingAfterPost( kualiDocumentFormBase, request );
         if ( getDocumentService().documentExists(kualiDocumentFormBase.getDocId())){
            getDocumentService().cancelDocument(kualiDocumentFormBase.getDocument(), kualiDocumentFormBase.getAnnotation());
         }
         return returnToSender(request, mapping, kualiDocumentFormBase);

    }

   @Override
   public ActionForward close(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
       KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
       return returnToSender(request, mapping, kualiDocumentFormBase);
   }

   @Override
   public ActionForward showAllTabs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
      super.showAllTabs(mapping, form, request, response);
      return mapping.findForward(OLEConstants.MAPPING_VIEW);
   }
   @Override
   public ActionForward hideAllTabs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
      super.hideAllTabs(mapping, form, request, response);
      return mapping.findForward(OLEConstants.MAPPING_VIEW);
   }

   protected static String getNamespaceCode(Class<? extends Object> clazz) {
       ModuleService moduleService = getKualiModuleService().getResponsibleModuleService(clazz);
       if (moduleService == null) {
           return KimConstants.KIM_TYPE_DEFAULT_NAMESPACE;
       }
       return moduleService.getModuleConfiguration().getNamespaceCode();
   }

   protected IdentityManagementService getIdentityManagementService() {
       if (identityManagementService == null) {
           identityManagementService = SpringContext.getBean(IdentityManagementService.class);
       }
       return identityManagementService;
   }


   public FormFile getFormFileForAttachment(int acqLoadSumId) throws Exception {
       try {
           File f = new File(getBatchLoadService().getDestinationPath() + acqLoadSumId + OLEConstants.BATCH_FAILURE_FILE_MRK);
           if (f.exists()) {
               FormFile diskFile = new TempFormFile(f);
               diskFile.setContentType("application/octet-stream");
               diskFile.setFileName(acqLoadSumId + OLEConstants.BATCH_FAILURE_FILE_MRK);
               diskFile.setFileSize((int) f.length());
               return diskFile;
           }
       } catch (Exception ex) {
           throw new RuntimeException( "Error attempting to get " + acqLoadSumId + " attachment", ex);
       }
       return null;
   }

   public FormFile getFormFileForAttachment(String fileName) throws Exception {
       try {
           File f = new File(getBatchLoadService().getDestinationPath() + fileName);
           if (f.exists()) {
               FormFile diskFile = new TempFormFile(f);
               diskFile.setContentType("application/octet-stream");
               diskFile.setFileName(fileName);
               diskFile.setFileSize((int) f.length());
               return diskFile;
           }
       } catch (Exception ex) {
           throw new RuntimeException( "Error attempting to get " + fileName + " attachment", ex);
       }
       return null;
   }

    protected static class TempFormFile implements FormFile {

        File f;
        String contentType;
        int fileSize;
        String fileName;

        public TempFormFile( File f ) {
            this.f = f;
        }

        @Override
        public void destroy() {}

        @Override
        public byte[] getFileData() throws FileNotFoundException, IOException {
            byte[] fileData = new byte[getFileSize()];
            FileInputStream fis = null;

            try {
                fis = new FileInputStream(f);
                fis.read(fileData);
            } catch (IOException e) {
                fileData = null;
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }

            return fileData;
        }

        @Override
        public InputStream getInputStream() throws FileNotFoundException, IOException {
            return new FileInputStream(f);
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public int getFileSize() {
            return fileSize;
        }

        @Override
        public void setFileSize(int fileSize) {
            this.fileSize = fileSize;
        }

        @Override
        public String getFileName() {
            return fileName;
        }

        @Override
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

    }

   protected BatchLoadService batchLoadService;

   public BatchLoadService getBatchLoadService() {
       if(batchLoadService==null) {
           batchLoadService=SpringContext.getBean(BatchLoadServiceImpl.class);
       }
       return batchLoadService;
   }


}

