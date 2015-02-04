package org.kuali.ole.ingest.controller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.OleLocationXMLSchemaValidator;
import org.kuali.ole.ingest.form.OleLocationForm;
import org.kuali.ole.service.OleLocationConverterService;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * OleLocationController is the controller class for Location Controller
 */
@Controller
@RequestMapping(value = "/locationcontroller")
public class OleLocationController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(OleLocationController.class);

    /**
     *  This method will return instance of OleLocationForm.
     * @param request
     * @return   OleLocationForm.
     */
     @Override
    protected OleLocationForm createInitialForm(HttpServletRequest request) {
        return new OleLocationForm();
    }

    /**
     *  This method takes the initial request when click on OleLocation Screen.
     * @param form
     * @param result
     * @param request
     * @param response
     * @return  ModelAndView.
     */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OleLocationForm oleLocationForm = (OleLocationForm) form;
        return super.start(oleLocationForm, result, request, response);
    }

    /**
     *   This method persist the data from locationXml file into database after uploading the locationXml.
     * @param form
     * @param result
     * @param request
     * @param response
     * @return  ModelAndView.
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=upload")
    public ModelAndView upload(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleLocationForm oleLocationForm = (OleLocationForm) form;
        OleLocationConverterService oleLocationRecordService = GlobalResourceLoader.getService("oleLocationConverterService");
        OleLocationXMLSchemaValidator oleLocationXMLSchemaValidator = new OleLocationXMLSchemaValidator();
        MultipartFile multipartFile = oleLocationForm.getLocationFile();
        String locationFileName = multipartFile.getOriginalFilename();
        if (validateFile(multipartFile.getOriginalFilename())) {
            String fileContent = new String(multipartFile.getBytes());
            try {
                boolean schemaFlag = oleLocationXMLSchemaValidator.validateContentsAgainstSchema(multipartFile.getInputStream());
                if(!schemaFlag){
                    oleLocationForm.setMessage(OLEConstants.LOCATION_RECORD_INVALID_SCHEMA);
                    return super.start(oleLocationForm, result, request, response);
                }
                String summary=oleLocationRecordService.persistLocationFromFileContent(fileContent,locationFileName);
                oleLocationForm.setMessage(OLEConstants.LOCATION_RECORD_SUCCESS+" "+summary);
            } catch (Exception locationIngestException) {
                oleLocationForm.setMessage(OLEConstants.LOCATION_RECORD_FAILURE);
                LOG.error("Failed to upload location.", locationIngestException);
            }
        } else {
            oleLocationForm.setMessage(OLEConstants.LOCATION_RECORD_SELECT_FILE);
        }
        return super.start(oleLocationForm, result, request, response);
    }

    /**
     *  This method validate the xml file type.
     * @param inputFile
     * @return  boolean.
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
     * @return  null.
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=downloadAttachment")
    public ModelAndView downloadAttachment(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.info("Start -- DownLoad Method of OleLocationRecordForm");
        String oleLocationSummaryId=request.getParameter(OLEConstants.LOCATION_SUMMARY_REPORT_ID);
        OleLocationForm oleLocationForm = (OleLocationForm) form;
        oleLocationForm.setOleLocationSummaryId(oleLocationSummaryId);
        String directory =ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.LOCATION_ERROR_FILE_PATH);
        String homeDirectory = System.getProperty(OLEConstants.USER_HOME_DIRECTORY);
        File file=new File(directory+oleLocationSummaryId+OLEConstants.FAILED_LOCATION_RECORD_NAME);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment; filename=" + oleLocationSummaryId+OLEConstants.FAILED_LOCATION_RECORD_NAME);
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setContentLength((int) file.length());

        InputStream fis = new FileInputStream(file);
        IOUtils.copy(fis, response.getOutputStream());
        response.getOutputStream().flush();

        // }
        LOG.info("End -- DownLoad Method of AcquisitionBatchInputFileAction");
        return null;
    }
}
