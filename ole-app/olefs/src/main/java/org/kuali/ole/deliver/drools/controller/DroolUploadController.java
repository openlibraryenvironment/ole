package org.kuali.ole.deliver.drools.controller;

import org.apache.commons.io.FilenameUtils;
import org.directwebremoting.convert.ContextConverter;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.form.DroolUploadForm;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Created by sheiksalahudeenm on 6/30/15.
 */
@Controller
@RequestMapping(value = "/droolUploadController")
public class DroolUploadController extends UifControllerBase {

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        return new DroolUploadForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        DroolUploadForm droolUploadForm = (DroolUploadForm) form;
        return super.start(droolUploadForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=upload")
    public ModelAndView upload(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        DroolUploadForm droolUploadForm = (DroolUploadForm) form;
        MultipartFile multipartFile = droolUploadForm.getDroolFile();
        if(multipartFile != null){
            if (validateFile(multipartFile.getOriginalFilename())) {
                String droolBaseDirectoryPath = getDroolBaseDirectory();
                File droolDirectory = new File(droolBaseDirectoryPath + File.separator + droolUploadForm.getSelectedRuleDirectory());
                if(!(droolDirectory.exists() && droolDirectory.isDirectory())){
                    droolDirectory.mkdirs();
                }
                multipartFile.transferTo(new File(droolBaseDirectoryPath + File.separator
                        + droolUploadForm.getSelectedRuleDirectory() +
                        File.separator + multipartFile.getOriginalFilename()));
                GlobalVariables.getMessageMap().putError(DroolsConstants.DROOL_UPLOAD_SUCCESS, DroolsConstants.DROOL_UPLOAD_SUCCESS);
            } else {
                GlobalVariables.getMessageMap().putError(DroolsConstants.DROOL_UPLOAD_INVALID_FILE, DroolsConstants.DROOL_UPLOAD_INVALID_FILE);
            }
        }else {
            GlobalVariables.getMessageMap().putError(DroolsConstants.DROOL_UPLOAD_SELECT_FILE, DroolsConstants.DROOL_UPLOAD_SELECT_FILE);
        }
        return super.start(droolUploadForm, result, request, response);
    }

    public boolean validateFile(String inputFile) {
        return (FilenameUtils.getExtension(inputFile).equals(".drl") ? true:false);
    }

    private String getDroolBaseDirectory(){
        return ConfigContext.getCurrentContextConfig().getProperty("rules.directory");
    }
}
