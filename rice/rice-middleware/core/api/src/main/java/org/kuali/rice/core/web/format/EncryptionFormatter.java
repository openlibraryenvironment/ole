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
package org.kuali.rice.core.web.format;

import java.security.GeneralSecurityException;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.encryption.EncryptionService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

/**
 * This formatter calls the encryption service to encrypt/decrypt values.
 */
public class EncryptionFormatter extends Formatter {
    private static final long serialVersionUID = -4109390572922205211L;
    private transient EncryptionService encryptionService;

    protected Object convertToObject(String target) {
        if (Formatter.isEmptyValue(target))
            return null;

        String decryptedValue = null;
        try {
            if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                decryptedValue = getEncryptionFormatter().decrypt(target);
            }
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Unable to decrypt value.");
        }

        return decryptedValue;
    }

    public Object format(Object target) {
        String encryptedValue = null;
        try {
            if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                encryptedValue = getEncryptionFormatter().encrypt(target);
            }
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Unable to encrypt secure field.");
        }

        return encryptedValue;
    }
    
    protected EncryptionService getEncryptionFormatter() {
    	if (this.encryptionService == null) {
    		this.encryptionService = GlobalResourceLoader.getService(CoreConstants.Services.ENCRYPTION_SERVICE);
    	}
    	return this.encryptionService;
    }
}
