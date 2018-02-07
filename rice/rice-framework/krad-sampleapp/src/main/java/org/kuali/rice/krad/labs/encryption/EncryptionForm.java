/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.labs.encryption;

import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * Form for the encrypt/decrypt utility
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EncryptionForm extends UifFormBase {

    private String encryptionServiceName;
    private String input;
    private String encryptedText;
    private String decryptedText;

    /**
     * Gets the name of the encryption service that is being used
     *
     * @return encryption service name
     */
    public String getEncryptionServiceName() {
        return encryptionServiceName;
    }

    public void setEncryptionServiceName(String encryptionServiceName) {
        this.encryptionServiceName = encryptionServiceName;
    }

    /**
     * Gets the input text to be encrypted/decrypted
     *
     * @return input text
     */
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    /**
     * Gets the resulting encrypted text
     *
     * @return encrypted text
     */
    public String getEncryptedText() {
        return encryptedText;
    }

    public void setEncryptedText(String encryptedText) {
        this.encryptedText = encryptedText;
    }

    /**
     * Gets the resulting decrypted text
     *
     * @return decrypted text
     */
    public String getDecryptedText() {
        return decryptedText;
    }

    public void setDecryptedText(String decryptedText) {
        this.decryptedText = decryptedText;
    }
}
