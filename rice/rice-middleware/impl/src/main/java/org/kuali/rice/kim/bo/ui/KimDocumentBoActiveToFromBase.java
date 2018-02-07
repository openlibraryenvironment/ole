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
package org.kuali.rice.kim.bo.ui;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@MappedSuperclass
public class KimDocumentBoActiveToFromBase  extends KimDocumentBoBase {
    private static final long serialVersionUID = 9042706897191231671L;

	@Column(name="ACTV_FRM_DT")
	protected Timestamp activeFromDate;
	@Column(name="ACTV_TO_DT")
	protected Timestamp activeToDate;
	@Transient
	protected boolean edit;


	/**
	 * @return the edit
	 */
	public boolean isEdit() {
		return this.edit;
	}

	/**
	 * @param edit the edit to set
	 */
	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	/**
	 * @return the activeFromDate
	 */
	public Timestamp getActiveFromDate() {
		return this.activeFromDate;
	}

	/**
	 * @param activeFromDate the activeFromDate to set
	 */
	public void setActiveFromDate(Timestamp activeFromDate) {
		this.activeFromDate = activeFromDate;
	}

	/**
	 * @return the activeToDate
	 */
	public Timestamp getActiveToDate() {
		return this.activeToDate;
	}

	/**
	 * @param activeToDate the activeToDate to set
	 */
	public void setActiveToDate(Timestamp activeToDate) {
		this.activeToDate = activeToDate;
	}

	public boolean isActive() {
		long now = System.currentTimeMillis();		
		return (activeFromDate == null || now > activeFromDate.getTime()) && (activeToDate == null || now < activeToDate.getTime());
	}

}
