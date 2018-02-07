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

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.ksb.util.KSBConstants;

/**
 * A DigitalSinger which places the alias and digital signature into the response headers of an HttpServletResponse.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ResponseHeaderDigitalSigner extends AbstractDigitalSigner {

	private String alias;
	private Certificate certificate;
	private HttpServletResponse response;
	
    public ResponseHeaderDigitalSigner(Signature signature, String alias, HttpServletResponse response) {
        super(signature);
        this.alias = alias;
        this.response = response;
    }
    
    public ResponseHeaderDigitalSigner(Signature signature, String alias, Certificate certificate, HttpServletResponse response) {
        this(signature, alias, response);
        this.certificate = certificate;
    }
    
    public ResponseHeaderDigitalSigner(Signature signature, Certificate certificate, HttpServletResponse response) {
        super(signature);
        this.certificate = certificate;
        this.response = response;
    }
    
	public void sign() throws Exception {
	    if (StringUtils.isNotBlank(this.alias) ) {
	        this.response.setHeader(KSBConstants.KEYSTORE_ALIAS_HEADER, this.alias);
	    }
	    if (this.certificate != null) {
	        this.response.setHeader(KSBConstants.KEYSTORE_CERTIFICATE_HEADER, getEncodedCertificate(this.certificate));
	    }
	    this.response.setHeader(KSBConstants.DIGITAL_SIGNATURE_HEADER, getEncodedSignature());
	}

}
