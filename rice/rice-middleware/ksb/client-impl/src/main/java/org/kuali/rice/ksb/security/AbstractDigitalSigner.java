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
package org.kuali.rice.ksb.security;

import java.security.Signature;
import java.security.cert.Certificate;

import org.apache.commons.codec.binary.Base64;

/**
 * An abstract implementation of a DigitalSigner which provides convienance utilities for storing a reference
 * to the Signature and also generating and encoding the actual digital signature.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class AbstractDigitalSigner implements DigitalSigner {

	private Signature signature;
	
	public AbstractDigitalSigner(Signature signature) {
		this.signature = signature;
	}
	
	public Signature getSignature() {
		return this.signature;
	}
	
	protected byte[] getSignatureBytes() throws Exception {
		return getSignature().sign();
	}
	
    protected String getEncodedSignature() throws Exception {
        return new String(Base64.encodeBase64(getSignatureBytes()), "UTF-8");
    }

    protected String getEncodedCertificate(Certificate certificate) throws Exception {
        return new String(Base64.encodeBase64(certificate.getEncoded()), "UTF-8");
    }

}
