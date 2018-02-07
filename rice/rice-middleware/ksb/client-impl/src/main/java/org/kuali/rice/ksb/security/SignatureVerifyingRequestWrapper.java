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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Signature;
import java.security.cert.CertificateFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.util.KSBConstants;

/**
 * An HttpServletRequestWrapper which will wraps the underlying request's InputStream in a 
 * SignatureVerifyingInputStream which will verify the digital signature of the request after 
 * all of the data has been read from the input stream.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SignatureVerifyingRequestWrapper extends HttpServletRequestWrapper {

	private byte[] digitalSignature;
	private Signature signature;
	
	public SignatureVerifyingRequestWrapper(HttpServletRequest request) {
		super(request);
		String encodedSignature = request.getHeader(KSBConstants.DIGITAL_SIGNATURE_HEADER);
		if (StringUtils.isEmpty(encodedSignature)) {
			throw new RuntimeException("A digital signature was required on the request but none was found.");
		}
		String verificationAlias = request.getHeader(KSBConstants.KEYSTORE_ALIAS_HEADER);
		String encodedCertificate = request.getHeader(KSBConstants.KEYSTORE_CERTIFICATE_HEADER);
		if ( (StringUtils.isEmpty(verificationAlias)) && (StringUtils.isEmpty(encodedCertificate)) ) {
            throw new RuntimeException("A verification alias or certificate was required on the request but neither was found.");
		}
		try {
            this.digitalSignature = Base64.decodeBase64(encodedSignature.getBytes("UTF-8"));
            if (StringUtils.isNotBlank(encodedCertificate)) {
                byte[] certificate = Base64.decodeBase64(encodedCertificate.getBytes("UTF-8"));
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                this.signature = KSBServiceLocator.getDigitalSignatureService().getSignatureForVerification(cf.generateCertificate(new ByteArrayInputStream(certificate)));
            } else if (StringUtils.isNotBlank(verificationAlias)) {
                this.signature = KSBServiceLocator.getDigitalSignatureService().getSignatureForVerification(verificationAlias);
            }
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize digital signature verification.", e);
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new SignatureVerifyingInputStream(this.digitalSignature, this.signature, super.getInputStream());
	}
	
}
