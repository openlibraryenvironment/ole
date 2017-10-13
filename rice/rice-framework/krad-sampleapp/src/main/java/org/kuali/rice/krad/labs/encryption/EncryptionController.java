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

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.encryption.EncryptionService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.GeneralSecurityException;

/**
 * Controller for the encrypt/decrypt utility
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
@RequestMapping(value = "/encryption")
public class EncryptionController extends UifControllerBase {

    public static final String INPUT_FIELD = "input";
    public static final String ENCRYPTION_ERROR = "labs.encryption.error";
    private  EncryptionService encryptionService;

    /**
     * Polulate the encryptionServiceName on the form.
     */
    @Override
    protected EncryptionForm createInitialForm(HttpServletRequest request) {
        EncryptionForm encryptionForm = new EncryptionForm();
        encryptionForm.setEncryptionServiceName(getEncryptionService().getClass().getSimpleName());
        return encryptionForm;
    }

    /**
     * Encrypt the text of the input field.
     *
     * <p>
     * The encryptedText and decryptedText filds are populated when encryption is successful.  An error message
     * is displayed otherwise.
     * </p>
     */
    @RequestMapping(params = "methodToCall=encrypt")
    public ModelAndView encrypt(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        EncryptionForm encryptionForm = (EncryptionForm) form;

        try {
            encryptionForm.setEncryptedText(getEncryptionService().encrypt(
                    encryptionForm.getInput()));
            encryptionForm.setDecryptedText(encryptionForm.getInput());
        }
        catch (GeneralSecurityException gse) {
            GlobalVariables.getMessageMap().putError(INPUT_FIELD, ENCRYPTION_ERROR, gse.toString());
        }

        return getUIFModelAndView(form);
    }

    /**
     * Decrypt the text of the input field.
     *
     * <p>
     * The encryptedText and decryptedText filds are populated when decryption is successful.  An error message
     * is displayed otherwise.
     * </p>
     */
    @RequestMapping(params = "methodToCall=decrypt")
    public ModelAndView decrypt(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        EncryptionForm encryptionForm = (EncryptionForm) form;

        try {
            encryptionForm.setEncryptedText(encryptionForm.getInput());
            encryptionForm.setDecryptedText(getEncryptionService().decrypt(
                    encryptionForm.getInput()));
        }
        catch (GeneralSecurityException gse) {
            GlobalVariables.getMessageMap().putError(INPUT_FIELD, ENCRYPTION_ERROR, gse.toString());
        }

        return getUIFModelAndView(form);
    }

    /**
     * Get the encryption service
     *
     * @return EncryptionService
     */
    private EncryptionService getEncryptionService() {
        if (encryptionService == null) {
            encryptionService = CoreApiServiceLocator.getEncryptionService();
        }

        return encryptionService;
    }
}
