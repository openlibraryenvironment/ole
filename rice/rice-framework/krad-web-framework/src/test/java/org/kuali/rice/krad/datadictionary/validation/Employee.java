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
package org.kuali.rice.krad.datadictionary.validation;


/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class Employee {
	private String employeeId;
	private String positionTitle;
	private String contactPhone;
	private String contactEmail;
	private Person employeeDetails;
	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return this.employeeId;
	}
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * @return the positionTitle
	 */
	public String getPositionTitle() {
		return this.positionTitle;
	}
	/**
	 * @param positionTitle the positionTitle to set
	 */
	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}
	/**
	 * @return the contactPhone
	 */
	public String getContactPhone() {
		return this.contactPhone;
	}
	/**
	 * @param contactPhone the contactPhone to set
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	/**
	 * @return the contactEmail
	 */
	public String getContactEmail() {
		return this.contactEmail;
	}
	/**
	 * @param contactEmail the contactEmail to set
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	/**
	 * @return the employeeDetails
	 */
	public Person getEmployeeDetails() {
		return this.employeeDetails;
	}
	/**
	 * @param employeeDetails the employeeDetails to set
	 */
	public void setEmployeeDetails(Person employeeDetails) {
		this.employeeDetails = employeeDetails;
	}



}
