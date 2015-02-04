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
import org.kuali.ole.ingest.ProfileBuilder;
import org.kuali.ole.ingest.ProfileXMLSchemaValidator;
import org.kuali.ole.ingest.form.ProfileBuilderForm;
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
 * ProfileBuilderController is the controller class for Profile Builder Controller
 */
@Controller
@RequestMapping(value = "/profilebuildercontroller")
public class ProfileBuilderController
        extends UifControllerBase {
    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    private static final Logger LOG = Logger.getLogger(ProfileBuilderController.class);

    /**
     * This method will return instance of ProfileBuilderForm
     * @param request
     * @return  ProfileBuilderForm
     */

    @Override
    protected ProfileBuilderForm createInitialForm(HttpServletRequest request) {
        return new ProfileBuilderForm();
    }

    /**
     *  This method will take the initial request when click on ProfileBuilder controller.
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
        ProfileBuilderForm profileBuilderForm = (ProfileBuilderForm) form;
        return super.start(profileBuilderForm, result, request, response);
    }

    /**
     *  This method persist the data from ProfileBuilderXml file into database after uploading the ProfileBuilderXml.
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
        ProfileBuilderForm profileBuilderForm = (ProfileBuilderForm) form;
        ProfileBuilder profileBuilder = new ProfileBuilder();
        ProfileXMLSchemaValidator profileXMLSchemaValidator=new ProfileXMLSchemaValidator();
        MultipartFile multipartFile = profileBuilderForm.getProfileFile();
        if (validateFile(multipartFile.getOriginalFilename())) {
            String fileContent = new String(multipartFile.getBytes());
            try {
                boolean validXML=profileXMLSchemaValidator.validateContentsAgainstSchema(multipartFile.getInputStream());
                if(!validXML){
                    profileBuilderForm.setMessage(OLEConstants.PROFILE_BUILDER_INVALID_SCHEMA);
                    return super.start(profileBuilderForm, result, request, response);
                }
                profileBuilder.persistKRMSProfileFromFileContent(fileContent);
                profileBuilderForm.setMessage(OLEConstants.PROFILE_BUILDER_SUCCESS);
            } catch (Exception profileBuilderException) {
                profileBuilderForm.setMessage(OLEConstants.PROFILE_BUILDER_FAILURE);
                LOG.error("Failed to upload Profile builder.", profileBuilderException);
            }
        } else {
            profileBuilderForm.setMessage(OLEConstants.PROFILE_BUILDER_SELECT_FILE);
        }
        return super.start(profileBuilderForm, result, request, response);
    }

    /**
     *  This method validate the xml file type.
     * @param inputFile
     * @return  boolean
     */
     public boolean validateFile(String inputFile) {
        return (inputFile.contains(".xml") ? true:false);
    }


}

