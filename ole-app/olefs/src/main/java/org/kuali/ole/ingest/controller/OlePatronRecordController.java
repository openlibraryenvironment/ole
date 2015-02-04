package org.kuali.ole.ingest.controller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord;
import org.kuali.ole.service.OlePatronConverterService;
import org.kuali.ole.ingest.OlePatronXMLSchemaValidator;
import org.kuali.ole.ingest.form.OlePatronRecordForm;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * OlePatronRecordController is the controller class for Patron Record Controller
 */
@Controller
@RequestMapping(value = "/patronrecordcontroller")
public class OlePatronRecordController extends UifControllerBase {
    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    private static final Logger LOG = Logger.getLogger(OlePatronRecordController.class);

    /**
     *   This method will return new Instance of OlePatronRecordForm.
     * @param request
     * @return   OlePatronRecordForm.
     */
    @Override
    protected OlePatronRecordForm createInitialForm(HttpServletRequest request) {
        return new OlePatronRecordForm();
    }

    /**
     *   This method takes the initial request when click on OlePatronRecord Screen.
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
        LOG.debug("Start -- Start Method of OlePatronRecordForm");
        OlePatronRecordForm olePatronRecordForm = (OlePatronRecordForm) form;
        return super.start(olePatronRecordForm, result, request, response);
    }

    /**
     * This method persist the data from patronXml file into database after uploading the patronXml.
     * @param form
     * @param result
     * @param request
     * @param response
     * @return  ModelAndView
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=upload")
    public ModelAndView upload(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Start -- Upload Method of OlePatronRecordForm");
        OlePatronRecordForm olePatronRecordForm = (OlePatronRecordForm) form;
        OlePatronConverterService olePatronRecordService = GlobalResourceLoader.getService(OLEConstants.PATRON_CONVERTER_SERVICE);
        OlePatronXMLSchemaValidator olePatronXMLSchemaValidator = new OlePatronXMLSchemaValidator();
        OlePatronIngestSummaryRecord olePatronIngestSummaryRecord =  new OlePatronIngestSummaryRecord();
        MultipartFile multipartFile = olePatronRecordForm.getPatronFile();
        String fileName = multipartFile.getOriginalFilename();
        if (validateFile(multipartFile.getOriginalFilename())) {
            String fileContent = new String(multipartFile.getBytes());
            try {
                boolean schemaFlag = olePatronXMLSchemaValidator.validateContentsAgainstSchema(multipartFile.getInputStream());
                if(!schemaFlag){
                    olePatronRecordForm.setMessage(OLEConstants.PATRON_RECORD_INVALID_SCHEMA);
                    return super.start(olePatronRecordForm, result, request, response);
                }
                boolean addUnMatchedPatronFlag = olePatronRecordForm.isAddUnmatchedPatron();
                String principalName = GlobalVariables.getUserSession().getPrincipalName();
                olePatronRecordService.persistPatronFromFileContent(fileContent,addUnMatchedPatronFlag,fileName,olePatronIngestSummaryRecord,olePatronRecordForm.getPatronAddressSource(),principalName);
                olePatronRecordForm.setMessage(olePatronRecordService.getUploadProcessMessage(olePatronIngestSummaryRecord));
            } catch (Exception patronRecordIngestException) {
                olePatronRecordForm.setMessage(OLEConstants.PATRON_RECORD_FAILURE);
                LOG.error("Failed to upload Patron record.", patronRecordIngestException);
            }
        } else {
            olePatronRecordForm.setMessage(OLEConstants.PATRON_RECORD_SELECT_FILE);
        }
        return super.start(olePatronRecordForm, result, request, response);
    }

    /**
     *  This method validate the xml file type.
     * @param inputFile
     * @return boolean
     */
    public boolean validateFile(String inputFile) {
        return (inputFile.contains(".xml") ? true:false);
    }

    /**
     *  This method will download the Failure records as an attachment based on summaryId.
     * @param form
     * @param result
     * @param request
     * @param response
     * @return null
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=downloadAttachment")
    public ModelAndView downloadAttachment(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.info("Start -- DownLoad Method of OlePatronRecordForm");
        String olePatronSummaryId=request.getParameter(OLEConstants.PATRON_SUMMARY_REPORT_ID);
        OlePatronRecordForm olePatronRecordForm = (OlePatronRecordForm) form;
        olePatronRecordForm.setOlePatronSummaryId(olePatronSummaryId);
        String directory = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.STAGING_DIRECTORY)+
                OLEConstants.PATRON_FILE_DIRECTORY;
        String homeDirectory = System.getProperty(OLEConstants.USER_HOME_DIRECTORY);
        File file=new File(homeDirectory+directory+olePatronSummaryId+OLEConstants.FAILED_PATRON_RECORD_NAME);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment; filename=" + olePatronSummaryId+OLEConstants.FAILED_PATRON_RECORD_NAME);
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setContentLength((int) file.length());
        InputStream fis = new FileInputStream(file);
        IOUtils.copy(fis, response.getOutputStream());
        response.getOutputStream().flush();

        LOG.info("End -- DownLoad Method of AcquisitionBatchInputFileAction");
        return null;
    }




}