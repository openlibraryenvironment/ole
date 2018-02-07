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
package org.kuali.rice.kew.doctype;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a description of what this class does - jjhanso don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SecurityPermissionInfo {

	private String permissionName;
	private String permissionNamespaceCode;
	private Map<String, String> permissionDetails = new HashMap<String, String>();
	private Map<String, String> qualifications = new HashMap<String, String>();
	
	
	/**
	 * @return the permissionName
	 */
	public String getPermissionName() {
		return this.permissionName;
	}
	/**
	 * @param permissionName the permissionName to set
	 */
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	/**
	 * @return the permissionNamespaceCode
	 */
	public String getPermissionNamespaceCode() {
		return this.permissionNamespaceCode;
	}
	/**
	 * @param permissionNamespaceCode the permissionNamespaceCode to set
	 */
	public void setPermissionNamespaceCode(String permissionNamespaceCode) {
		this.permissionNamespaceCode = permissionNamespaceCode;
	}
	/**
	 * @return the permissionDetails
	 */
	public Map<String, String> getPermissionDetails() {
		return this.permissionDetails;
	}
	/**
	 * @param permissionDetails the permissionDetails to set
	 */
	public void setPermissionDetails(Map<String, String> permissionDetails) {
		this.permissionDetails = permissionDetails;
	}
	/**
	 * @return the qualifications
	 */
	public Map<String, String> getQualifications() {
		return this.qualifications;
	}
	/**
	 * @param qualifications the qualifications to set
	 */
	public void setQualifications(Map<String, String> qualifications) {
		this.qualifications = qualifications;
	}
	
	
}
