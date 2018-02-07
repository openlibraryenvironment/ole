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
package org.kuali.rice.kew.user;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;

import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.UserSession;

/**
 * Provides some utility methods for translating user ID types.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class UserUtils {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UserUtils.class);

	private static PersonService personService;
	
	private UserUtils() {
		throw new UnsupportedOperationException("do not call");
	}
	
	public static String getIdValue(String idType, Person user) {
	    if ("workflowId".equalsIgnoreCase(idType) || "w".equalsIgnoreCase(idType) || "principalId".equalsIgnoreCase(idType)) {
	      return user.getPrincipalId();
	    } else if ("authenticationId".equalsIgnoreCase(idType) || "a".equalsIgnoreCase(idType) || "principalName".equalsIgnoreCase(idType)) {
	      return user.getPrincipalName();
	    } else if ("emplId".equalsIgnoreCase(idType) || "e".equalsIgnoreCase(idType)) {
	      return user.getEmployeeId();
	    } else {
	      LOG.error("Could not determine ID Value for given id type!" + idType);
	    }
	    return null;
	  }

    public static String getIdValue(String idType, String principalId) {
        if ("workflowId".equalsIgnoreCase(idType) || "w".equalsIgnoreCase(idType) || "principalId".equalsIgnoreCase(idType)) {
            return principalId;
        }
        Person person = KimApiServiceLocator.getPersonService().getPerson(principalId);
        return getIdValue(idType, person);
    }
	
	public static String getTransposedName(UserSession userSession, PrincipalContract principal) {
		Person person = getPersonService().getPerson(principal.getPrincipalId());
		return person.getName(); //contructTransposedName(person);
	}

	/**
	 * @return the personService
	 */
	public static PersonService getPersonService() {
		if ( personService == null ) {
			personService = KimApiServiceLocator.getPersonService();
		}
		return personService;
	}
}
