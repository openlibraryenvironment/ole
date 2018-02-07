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
package org.kuali.rice.ksb.messaging;

public class HttpException extends org.apache.commons.httpclient.HttpException {


	private static final long serialVersionUID = -2660638986164631692L;
	
	private int responseCode;
	
	public HttpException(int responseCode) {
		super();
		this.responseCode = responseCode;
	}

	public HttpException(int responseCode, String message, Throwable cause) {
		super(message, cause);
		this.responseCode = responseCode;
	}

	public HttpException(int responseCode, String arg0) {
		super(arg0);
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return this.responseCode;
	}
	
}
