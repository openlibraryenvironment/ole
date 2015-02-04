package org.kuali.ole.ingest.form;

import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.multipart.MultipartFile;

/**
 * KrmsBuilderForm is the Form class for Krms Builder
 */
public class KrmsBuilderForm extends UifFormBase {

    private MultipartFile krmsFile;
    private String message;

    /**
     * Gets the krmsFile attribute.
     * @return Returns krmsFile.
     */
    public MultipartFile getKrmsFile() {
        return krmsFile;
    }

    /**
     * Sets the krmsFile attribute value.
     * @param krmsFile The krmsFile to set.
     */
    public void setKrmsFile(MultipartFile krmsFile) {
        this.krmsFile = krmsFile;
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
