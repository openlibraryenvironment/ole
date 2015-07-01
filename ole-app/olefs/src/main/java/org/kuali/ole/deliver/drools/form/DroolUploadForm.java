package org.kuali.ole.deliver.drools.form;

import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by sheiksalahudeenm on 6/30/15.
 */
public class DroolUploadForm extends UifFormBase {

    private MultipartFile droolFile;
    private String message;
    private String selectedRuleDirectory;

    public MultipartFile getDroolFile() {
        return droolFile;
    }

    public void setDroolFile(MultipartFile droolFile) {
        this.droolFile = droolFile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSelectedRuleDirectory() {
        return selectedRuleDirectory;
    }

    public void setSelectedRuleDirectory(String selectedRuleDirectory) {
        this.selectedRuleDirectory = selectedRuleDirectory;
    }
}
