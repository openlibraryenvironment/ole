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
import org.kuali.ole.OLEKeyConstants;
import org.kuali.ole.ingest.KrmsBuilder;
import org.kuali.ole.ingest.KrmsXMLSchemaValidator;
import org.kuali.ole.ingest.form.KrmsBuilderForm;
import org.kuali.ole.ingest.krms.builder.OleKrmsBuilder;
import org.kuali.rice.krad.util.GlobalVariables;
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
 * KrmsBuilderController is the controller class for Krms Builder
 */
@Controller
@RequestMapping(value = "/krmsbuildercontroller")
public class KrmsBuilderController
        extends UifControllerBase {
    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    private static final Logger LOG = Logger.getLogger(KrmsBuilderController.class);

    /**
     *  This method returns the instance of KrmsBuilderForm.
     * @param request
     * @return krmsBuilderForm
     */
    @Override
    protected KrmsBuilderForm createInitialForm(HttpServletRequest request) {
        return new KrmsBuilderForm();
    }

    /**
     * This method takes the initial request when click on KrmsBuilder Screen.
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
        KrmsBuilderForm krmsBuilderForm = (KrmsBuilderForm) form;
        return super.start(krmsBuilderForm, result, request, response);
    }

    /**
     * This method persist the data from krmsBuilderXml file into database after uploading the krmsBuilderXml.
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
        KrmsBuilderForm krmsBuilderForm = (KrmsBuilderForm) form;
        KrmsBuilder krmsBuilder = new KrmsBuilder();
        OleKrmsBuilder loanKrmsBuilder = new OleKrmsBuilder();
        KrmsXMLSchemaValidator krmsXMLSchemaValidator = new KrmsXMLSchemaValidator();
        MultipartFile multipartFile = krmsBuilderForm.getKrmsFile();
        if(multipartFile != null){
            if (validateFile(multipartFile.getOriginalFilename())) {
                String fileContent = new String(multipartFile.getBytes());
                try {
                    boolean validXML= true;
                    if(!validXML){
                        GlobalVariables.getMessageMap().putError(OLEKeyConstants.KRMS_BUILDER_INVALID_SCHEMA, OLEKeyConstants.KRMS_BUILDER_INVALID_SCHEMA);
                        return super.start(krmsBuilderForm, result, request, response);
                    }
                    boolean license  = krmsXMLSchemaValidator.validateContentsAgainstSchema(multipartFile.getInputStream());
                    if(license)
                        krmsBuilder.persistKrmsFromFileContent(fileContent);
                    else
                        loanKrmsBuilder.persistKrmsFromFileContent(fileContent);

                    GlobalVariables.getMessageMap().putInfo(OLEKeyConstants.KRMS_BUILDER_SUCCESS,OLEKeyConstants.KRMS_BUILDER_SUCCESS);
                } catch (Exception krmsBuilderException) {
                    GlobalVariables.getMessageMap().putError(OLEKeyConstants.KRMS_BUILDER_FAILURE, OLEKeyConstants.KRMS_BUILDER_FAILURE);
                    LOG.error("Failed to upload Krms builder.", krmsBuilderException);
                }
            } else {
                GlobalVariables.getMessageMap().putError(OLEKeyConstants.KRMS_BUILDER_SELECT_FILE, OLEKeyConstants.KRMS_BUILDER_SELECT_FILE);
            }
        }else {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.KRMS_BUILDER_SELECT_FILE, OLEKeyConstants.KRMS_BUILDER_SELECT_FILE);
        }
        return super.start(krmsBuilderForm, result, request, response);
    }

    /**
     * This method validate the xml file type.
     * @param inputFile
     * @return  boolean
     */
     public boolean validateFile(String inputFile) {
        return (inputFile.contains(".xml") ? true:false);
    }


}

