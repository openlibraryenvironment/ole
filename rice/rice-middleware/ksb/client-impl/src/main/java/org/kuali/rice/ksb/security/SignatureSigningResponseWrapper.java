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

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Signature;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.kuali.rice.ksb.service.KSBServiceLocator;

/**
 * An HttpServletResponseWrapper which wraps the underlying response's OutputStream in a 
 * SignatureSingingOutputStream which will generate a digital signature for the outgoing message.
 *  
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SignatureSigningResponseWrapper extends HttpServletResponseWrapper {

	private DigitalSigner signer;
	private ServletOutputStream outputStream;
	private PrintWriter writer;
	
	public SignatureSigningResponseWrapper(HttpServletResponse response) {
		super(response);
		try {		
			Signature signature = KSBServiceLocator.getDigitalSignatureService().getSignatureForSigning();
			String alias = KSBServiceLocator.getJavaSecurityManagementService().getModuleKeyStoreAlias();	
			this.signer = new ResponseHeaderDigitalSigner(signature, alias, response);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize digital signature verification.", e);
		}
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (this.outputStream == null) {
		    this.outputStream = new SignatureSigningOutputStream(this.signer, super.getOutputStream(), true);
		}
		return this.outputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (this.writer == null) {
		    this.writer =  new PrintWriter(getOutputStream());
		}
		return this.writer;
	}
	
}
