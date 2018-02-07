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

/**
 * Responsible for signing a message.  A reference is provided to the Signature to allow for population
 * of the singnature from message data.  When this population of data is complete, the sign() method
 * will sign the message according to the implementation.
 * <br>
 * Note that the interface itself does not provide any means of retrieving the message being signed.  It
 * is up to the implementing classes to determine what consititutes "signing" of a message.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DigitalSigner {

	/**
	 * Retrieve a reference to Signature which will be used for signing.
	 */
	public Signature getSignature();

	/**
	 * Sign the message using the Signature.  This method will not be called until all of the message data
	 * has been populated into the Signature.  After signing implementations may, for example, place the digital 
	 * signature in a header or perform whatever steps are required to successfully sign the message.
	 */
	public void sign() throws Exception;
	
}
