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
package org.kuali.rice.core.api.encryption;

import java.security.GeneralSecurityException;

/**
 * This is a service interface to consolidate Kuali encryption operation 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface EncryptionService {
    /* string appended to an encrypted value by the frameworks for determine if a 
    value coming back from the ui is encrypted */
    public static final String ENCRYPTION_POST_PREFIX = "(&^#&)";
    public static final String HASH_POST_PREFIX = "(&^HSH#&)";

    /**
     * Encrypts a value
     *
     * @param valueToHide - original value
     * @return encrypted value
     * @throws GeneralSecurityException
     */
    public String encrypt(Object valueToHide) throws GeneralSecurityException;

    /**
     * Encrypts a value
     *
     * @param valueToHide - original value
     * @return encrypted value
     * @throws GeneralSecurityException
     */
    public byte[] encryptBytes(byte[] valueToHide) throws GeneralSecurityException;
    
    /**
     * Decrypts a value
     *
     * @param ciphertext - encrypted value
     * @return decrypted value
     * @throws GeneralSecurityException
     */
    public String decrypt(String ciphertext) throws GeneralSecurityException;

    /**
     * Decrypts a value
     *
     * @param ciphertext - encrypted value
     * @return decrypted value
     * @throws GeneralSecurityException
     */
    public byte[] decryptBytes(byte[] ciphertext) throws GeneralSecurityException;

    /**
     * Returns true if encryption is enabled within KEW, false otherwise.
     */
    public boolean isEnabled();

    /**
     * Hashes a value (for one-way transformations)
     * 
     * @param valueToHide - original value
     * @return encrypted value
     * @throws GeneralSecurityException
     */
    public String hash(Object valueToHide) throws GeneralSecurityException;
}
