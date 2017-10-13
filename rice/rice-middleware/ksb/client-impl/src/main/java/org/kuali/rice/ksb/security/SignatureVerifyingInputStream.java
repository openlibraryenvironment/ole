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
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Signature;

import javax.servlet.ServletInputStream;

/**
 * An InputStream which decorates another InputStream with a wrapper that verifies the digital signature
 * of the data after the last piece of data is read.  The digital signature to verify against is
 * passed into the constructor of this stream.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SignatureVerifyingInputStream extends ServletInputStream {

	private byte[] digitalSignature;
	private Signature signature;
	private InputStream wrappedInputStream;
	
	public SignatureVerifyingInputStream(byte[] digitalSignature, Signature signature, InputStream wrappedInputStream) {
		this.digitalSignature = digitalSignature;
		this.signature = signature;
		this.wrappedInputStream = wrappedInputStream;
	}

	@Override
	public synchronized int read() throws IOException {
		int data = this.wrappedInputStream.read();
		try {
			if (data == -1) {
				verifySignature();
			} else {
			    this.signature.update((byte)data);
			}
		} catch (GeneralSecurityException e) {
			IOException exception = new IOException("Error processing digital signature.");
			exception.initCause(e);
			throw exception;
		}
		return data;
	}
	
	protected void verifySignature() throws IOException, GeneralSecurityException {
		boolean verifies = this.signature.verify(this.digitalSignature);
		if (!verifies) {
			throw new IOException("The digital signature could not be successfully verified!");
		}
	}

}
