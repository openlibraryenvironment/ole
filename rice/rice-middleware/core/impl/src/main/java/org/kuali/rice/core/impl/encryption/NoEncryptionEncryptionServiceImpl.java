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
package org.kuali.rice.core.impl.encryption;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.encryption.EncryptionService;

import java.security.GeneralSecurityException;

/**
 * Implementation of encryption service for demonstration. 
 * 
 * 
 */
public class NoEncryptionEncryptionServiceImpl implements EncryptionService {

    /**
     * @see org.kuali.rice.ksb.security.EncryptionService#isEnabled()
     */
    public boolean isEnabled() {
        return false;
    }
    
    public String encrypt(Object valueToHide) throws GeneralSecurityException {
        if (valueToHide == null) {
            return "";
        }

        return valueToHide.toString();
    }

    public String decrypt(String ciphertext) throws GeneralSecurityException {
        if (StringUtils.isBlank(ciphertext)) {
            return "";
        }

        return new String(ciphertext);
    }

    public String hash(Object valueToHide) throws GeneralSecurityException {
        if ( valueToHide == null || StringUtils.isEmpty( valueToHide.toString() ) ) {
            return "";
        }
        return valueToHide.toString();
    }
    
    /**
     * This overridden method ...
     * 
     * @see org.kuali.rice.core.api.encryption.EncryptionService#decryptBytes(byte[])
     */
    public byte[] decryptBytes(byte[] ciphertext)
    		throws GeneralSecurityException {
    	return ciphertext;
    }
    
    /**
     * This overridden method ...
     * 
     * @see org.kuali.rice.core.api.encryption.EncryptionService#encryptBytes(byte[])
     */
    public byte[] encryptBytes(byte[] valueToHide)
    		throws GeneralSecurityException {
    	return valueToHide;
    }
}
