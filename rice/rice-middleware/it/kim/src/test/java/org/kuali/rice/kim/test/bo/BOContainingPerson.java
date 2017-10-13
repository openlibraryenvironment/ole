/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kim.test.bo;


import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class BOContainingPerson extends PersistableBusinessObjectBase {

	@Id
	@Column(name="pk")
	protected String boPrimaryKey;
	
	@Column(name="prncpl_id")
	protected String principalId;
	
	@Transient
	protected Person person;
	
	public String getBoPrimaryKey() {
		return this.boPrimaryKey;
	}

	public void setBoPrimaryKey(String boPrimaryKey) {
		this.boPrimaryKey = boPrimaryKey;
	}

	public String getPrincipalId() {
		return this.principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public Person getPerson() {
		person = KimApiServiceLocator.getPersonService().updatePersonIfNecessary( principalId, person );
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
