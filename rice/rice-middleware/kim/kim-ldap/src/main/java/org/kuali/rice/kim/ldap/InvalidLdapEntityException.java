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
package org.kuali.rice.kim.ldap;

/**
 * Exception that is used when we retrieve results from EDS that are not compatible with KIM
 * 
 * @author Leo Przybylski
 */
public class InvalidLdapEntityException extends RuntimeException { 

	private static final long serialVersionUID = 1831295133453011336L;

	public InvalidLdapEntityException(String message) {
        super(message);
    }
}
