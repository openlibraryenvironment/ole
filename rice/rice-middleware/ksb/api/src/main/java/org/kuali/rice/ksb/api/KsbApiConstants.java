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
package org.kuali.rice.ksb.api;

import org.kuali.rice.core.api.CoreConstants;

/**
 * Defines various constants that are used by or in conjunction with the KSB api.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class KsbApiConstants {

	/**
	 * The name of the KSB module
	 */
	public static final String KSB_MODULE_NAME = "ksb";

    public final static String SERVICE_PATH_SOAP = "soap/" + Namespaces.MODULE_NAME + "/" + CoreConstants.Versions.VERSION_2_0;
	
	/**
	 * Defines the set of out-of-the-box service types that the KSB understands.
	 */
	public static final class ServiceTypes {
		
		/**
		 * The service type for services that use Spring's Http Invoker to perform Java Serialization over HTTP
		 */
		public static final String HTTP_INVOKER = "httpInvoker";
		
		/**
		 * The service type for services that use SOAP
		 */
		public static final String SOAP = "SOAP";
		
		/**
		 * The service type for services that use a REST-style approach 
		 */
		public static final String REST = "REST";
		
	}
	
	/**
	 * Defines various namespace-related constants for the KSB.
	 */
	public static final class Namespaces {
		 public static final String MODULE_NAME = "ksb";
		/**
		 * The KSB namespace for version 2.x of Kuali Rice
		 */
        public static final String KSB_NAMESPACE_2_0 = CoreConstants.Namespaces.ROOT_NAMESPACE_PREFIX + "/" + MODULE_NAME + "/" + CoreConstants.Versions.VERSION_2_0;
        
	}
	
	private KsbApiConstants() {
		throw new UnsupportedOperationException("Should never be called!");
	}
	
}
