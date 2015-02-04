package org.kuali.ole.ingest.controller;

/**
 * Created by IntelliJ IDEA.
 * User: balakumaranm
 * Date: 3/28/12
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.IngestProcessor;
import org.kuali.ole.ingest.form.StaffUploadForm;
import org.kuali.ole.ingest.pojo.IngestRecord;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * StaffUploadController is the controller class for Staff Upload Controller.
 */
@Controller
@RequestMapping(value = "/staffuploadcontroller")
public class StaffUploadController
        extends UifControllerBase {
    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    private static final Logger LOG = Logger.getLogger(StaffUploadController.class);

    /**
     * This method will return instance of StaffUploadForm.
     * @param request
     * @return  StaffUploadForm
     */

    @Override
    protected StaffUploadForm createInitialForm(HttpServletRequest request) {
        return new StaffUploadForm();
    }

    /**
     * This method will take the initial request when click on StaffUpload screen.
     * @param form
     * @param result
     * @param request
     * @param response
     * @return  ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        StaffUploadForm staffUploadForm = (StaffUploadForm) form;
        String user = request.getParameter(OLEConstants.LOGIN_USR);
        staffUploadForm.setUser(user);
        loadReportURL(staffUploadForm);
        return super.start(staffUploadForm, result, request, response);
    }

    /**
     * This method persist the data from StaffUploadXml file into database after uploading the StaffUploadXml..
     * @param form
     * @param result
     * @param request
     * @param response
     * @return  ModelAndView
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=upload")
    public ModelAndView upload(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response)throws Exception {
        StaffUploadForm staffUploadForm = (StaffUploadForm) form;

        MultipartFile marcFile = staffUploadForm.getMarcFile();
        MultipartFile ediFile = staffUploadForm.getEdiFile();
        String user = staffUploadForm.getUser();
        String agendaName=null;
        String agendaDescription = null;
        agendaName = staffUploadForm.getAgenda();
        agendaDescription = staffUploadForm.getAgendaDescription();
        if (null != agendaName && !agendaName.equals("")) {
            String marcFileContent = new String(marcFile.getBytes());
            String ediFileContent = new String(ediFile.getBytes());

            if(validateFile(marcFile.getOriginalFilename(),ediFile.getOriginalFilename())){
                IngestRecord ingestRecord = new IngestRecord();
                ingestRecord.setOriginalEdiFileName(ediFile.getOriginalFilename());
                ingestRecord.setOriginalMarcFileName(marcFile.getOriginalFilename());
                ingestRecord.setAgendaName(agendaName);
                ingestRecord.setAgendaDescription(agendaDescription);
                ingestRecord.setEdiFileContent(ediFileContent);
                ingestRecord.setMarcFileContent(marcFileContent);
                ingestRecord.setByPassPreProcessing(isPreProcessingRequired(marcFile, ediFile));
                ingestRecord.setUser(user);
                try {
                    boolean failure_flag=true;
                    int recordCount=getIngestProcessor().start(ingestRecord , failure_flag, null, null);
                    if(failure_flag)
                        staffUploadForm.setMessage(OLEConstants.STAFF_UPLOAD_SUCCESS);
                    else if(ingestRecord.isUpdate())
                        staffUploadForm.setMessage(OLEConstants.STAFF_UPLOAD_UPDATE_SUCCESS);
                    else
                        staffUploadForm.setMessage(OLEConstants.STAFF_UPLOAD_FAILURE);
                } catch (Exception staffUploadException) {
                    staffUploadForm.setMessage(OLEConstants.STAFF_UPLOAD_FAILURE);
                    LOG.error("Failed to perform Staff Upload:", staffUploadException);
                }

            } else {
                //GlobalVariables.getMessageMap().putError("items", OLEConstants.ERROR_MESSAGE_UPLOAD);
                staffUploadForm.setMessage(OLEConstants.ERROR_MESSAGE_UPLOAD);
            }
        } else {
                //GlobalVariables.getMessageMap().putError("items", OLEConstants.ERROR_AGENDA_NAME);
            staffUploadForm.setMessage(OLEConstants.ERROR_AGENDA_NAME);
        }
        loadReportURL(staffUploadForm);


        //return super.start(staffUploadForm, result, request, response);
        return getUIFModelAndView(staffUploadForm, "StaffUploadViewPage");
    }

    /**
     * Sets the LoadReportUrl attribute value.
     * @param staffUploadForm
     */
    private void loadReportURL(StaffUploadForm staffUploadForm) {
        String LOADREPORT_URL="loadreport.url";
        String loadreportURL = ConfigContext.getCurrentContextConfig().getProperty(LOADREPORT_URL);
        staffUploadForm.setLoadReportURL(loadreportURL);
    }

    /**
     * This method validate the xml fileType
     * @param marcFile
     * @param ediFile
     * @return boolean
     */
    public boolean validateFile(String marcFile, String ediFile) {
        return (marcFile.contains(".xml") && ediFile.contains(".edi") ||
                marcFile.contains(".mrc") && ediFile.contains(".xml") ||
                !(marcFile.contains(".mrc") && ediFile.contains(".edi")
                        || (marcFile.contains(".xml") && ediFile.contains(".xml")))? false:true);
    }

    /**
     *  This method checks whether preProcessing is required or not based on the originalFileName.
     * @param marcFile
     * @param ediFile
     * @return   boolean
     */
    private boolean isPreProcessingRequired(MultipartFile marcFile, MultipartFile ediFile) {
        return (marcFile.getOriginalFilename().contains(".mrc") && ediFile.getOriginalFilename().contains(".edi") ? true : false);
    }

    /**
     *  This method returns the instance of Ingest Processor.
     * @return IngestProcessor
     */
    private IngestProcessor getIngestProcessor() {
        return new IngestProcessor();
    }


}

