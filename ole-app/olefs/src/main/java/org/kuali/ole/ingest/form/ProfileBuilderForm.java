package org.kuali.ole.ingest.form;

import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.multipart.MultipartFile;

/**
 * ProfileBuilderForm is the Form class for Profile Builder Document
 */
public class ProfileBuilderForm extends UifFormBase {

    private MultipartFile profileFile;
    private String message;
    /**
     * Gets the profileFile attribute.
     * @return Returns the profileFile..
     */
    public MultipartFile getProfileFile() {
        return profileFile;
    }
    /**
     * Sets the profileFile attribute value.
     * @param profileFile The profileFile to set.
     */
    public void setProfileFile(MultipartFile profileFile) {
        this.profileFile = profileFile;
    }
    /**
     * Gets the message attribute.
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }
    /**
     * Sets the message attribute value.
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
