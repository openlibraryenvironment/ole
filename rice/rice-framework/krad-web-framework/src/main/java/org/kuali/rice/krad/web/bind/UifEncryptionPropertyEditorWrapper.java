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
package org.kuali.rice.krad.web.bind;

import org.kuali.rice.core.api.CoreApiServiceLocator;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.security.GeneralSecurityException;

/**
 * Property editor which encrypts values for display and decrypts on binding, uses the
 * {@link org.kuali.rice.core.api.encryption.EncryptionService} to perform the encryption
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifEncryptionPropertyEditorWrapper extends PropertyEditorSupport{

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UifEncryptionPropertyEditorWrapper.class);

    PropertyEditor propertyEditor;

    /**
     * @param propertyEditor
     */
    public UifEncryptionPropertyEditorWrapper(PropertyEditor propertyEditor) {
        super();
        this.propertyEditor = propertyEditor;
    }

    @Override
    public String getAsText() {
        try {
            if (propertyEditor != null) {
                if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                    return CoreApiServiceLocator.getEncryptionService().encrypt(propertyEditor.getAsText());
                }
            }
            if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                return CoreApiServiceLocator.getEncryptionService().encrypt(getValue());
            }
            return null;
        } catch (GeneralSecurityException e) {
            LOG.error("Unable to encrypt value");
            throw new RuntimeException("Unable to encrypt value.");
        }
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            String value = "";
            if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                value = CoreApiServiceLocator.getEncryptionService().decrypt(text);
            }
            if (propertyEditor != null) {
                propertyEditor.setAsText(value);
            } else {
                setValue(value);
            }
        } catch (GeneralSecurityException e) {
            LOG.error("Unable to decrypt value: " + text);
            throw new RuntimeException("Unable to decrypt value.");
        }
    }

}
