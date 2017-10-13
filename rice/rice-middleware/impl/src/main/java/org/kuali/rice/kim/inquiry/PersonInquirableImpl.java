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
package org.kuali.rice.kim.inquiry;

import java.util.Map;

import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.krad.bo.BusinessObject;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PersonInquirableImpl extends KualiInquirableImpl {

	PersonService personService;
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.krad.inquiry.KualiInquirableImpl#getBusinessObject(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BusinessObject getBusinessObject(Map fieldValues) {
		return getPersonService().getPerson( fieldValues.get( "principalId" ).toString() );
	}

	public PersonService getPersonService() {
		if ( personService == null ) {
			personService = KimApiServiceLocator.getPersonService();
		}
		return personService;
	}
}
