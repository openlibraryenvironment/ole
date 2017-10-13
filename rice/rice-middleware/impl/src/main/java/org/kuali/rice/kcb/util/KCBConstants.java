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
package org.kuali.rice.kcb.util;

/**
 * This class houses various constants for KCB
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class KCBConstants {
	
	public static final String SERVICE_NAMESPACE = "KCB";
	
    /**
     * Different message delivers for KCB
     * DEFAULT_MESSAGE_DELIVERY_TYPE - the default message delivery type
     * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class MESSAGE_DELIVERY_TYPES {
        public static final String EMAIL_MESSAGE_DELIVERY_TYPE = "Email"; 
        
    	private MESSAGE_DELIVERY_TYPES() {
    		throw new UnsupportedOperationException("do not call");
    	}
    }
    
	private KCBConstants() {
		throw new UnsupportedOperationException("do not call");
	}
}
