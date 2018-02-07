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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

import javax.servlet.ServletOutputStream;

/**
 * An OutputStream which decorates another OutputStream with a wrapper that digitally
 * signs the data when the OutputStream is closed.  Since this class does not know where
 * the resulting digital signature will reside, a DigitalSigner will be invoked to
 * execute the actual signing of the message (i.e. put it in a header).
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SignatureSigningOutputStream extends ServletOutputStream {

	private boolean delayWrite;
	private DigitalSigner signer;
	private BufferedOutputStream bufferedDataHoldingStream;
	private ByteArrayOutputStream dataHoldingStream;
	private OutputStream wrappedOutputStream;
	
	/**
	 * Constructs a SignatureSigningOutputStream with the given DigitalSigner and underlying OutputStream.
	 * If true, the delayWrite boolean indicates that the stream should store all data internally until the
	 * stream is closed, at which point it should forward all data to the wrapped OutputStream.  If delayWrite
	 * is false, then the data will be forwarded immediately.
	 */
	public SignatureSigningOutputStream(DigitalSigner signer, OutputStream wrappedOutputStream, boolean delayWrite) {
		super();
		this.delayWrite = delayWrite;
		if (delayWrite) {
		    this.dataHoldingStream = new ByteArrayOutputStream();
		    this.bufferedDataHoldingStream = new BufferedOutputStream(this.dataHoldingStream);
		}
		this.wrappedOutputStream = wrappedOutputStream;
		this.signer = signer;
	}

	public void write(int data) throws IOException {
		if (this.delayWrite) {
		    this.bufferedDataHoldingStream.write(data);
		} else {
		    this.wrappedOutputStream.write(data);
		}
		try {
		    this.signer.getSignature().update((byte)data);	
		} catch (GeneralSecurityException e) {
			IOException exception = new IOException("Error updating signature.");
			exception.initCause(e);
			throw exception;
		}
	}
	
	@Override
	public void close() throws IOException {
		// before we close, sign the message
		try {
		    this.signer.sign();
			if (this.delayWrite) {
			    this.bufferedDataHoldingStream.close();
				byte[] data = this.dataHoldingStream.toByteArray();
				for (int index = 0; index < data.length; index++) {
				    this.wrappedOutputStream.write(data[index]);
				}
			}
			this.wrappedOutputStream.close();
		} catch (Exception e) {
			IOException exception = new IOException("Error attaching digital signature to outbound response.");
			exception.initCause(e);
			throw exception;
		} finally {
			super.close();
		}
	}

}
