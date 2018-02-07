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
package org.kuali.rice.core.api.util;

import org.apache.commons.codec.binary.Base64;
import org.kuali.rice.core.api.exception.RiceRuntimeException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

/**
 * A utility class for generating checksum values from Serializable objects.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class ChecksumUtils {

    /**
	 * Creates a checksum for the given object which must be serializable.
	 *
	 * @param object the serializable object for which to calculate the checksum
     * 
	 * @return A checksum value for the object.
	 */
	public static String calculateChecksum(Object object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
        } catch (IOException e) {
            throw new RiceRuntimeException(e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {}
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return new String( Base64.encodeBase64(md.digest(bos.toByteArray())), "UTF-8");
        } catch( GeneralSecurityException ex ) {
        	throw new RiceRuntimeException(ex);
        } catch( UnsupportedEncodingException ex ) {
        	throw new RiceRuntimeException(ex);
        }
	}

    @SuppressWarnings("unused")
    private ChecksumUtils() {
        throw new UnsupportedOperationException("Should never be executed.");
    }

}
