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
package org.kuali.rice.krad.bo;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 */
@Entity
@Table(name="KRNS_NTE_TYP_T")
public class NoteType extends PersistableBusinessObjectBase {

	@Id
	@Column(name="NTE_TYP_CD")
	private String noteTypeCode;
	@Column(name="TYP_DESC_TXT")
	private String noteTypeDescription;
	@Type(type="yes_no")
	@Column(name="ACTV_IND")
	private boolean noteTypeActiveIndicator;

	/**
	 * Default constructor.
	 */
	public NoteType() {

	}

	/**
	 * Gets the noteTypeCode attribute.
	 * 
	 * @return Returns the noteTypeCode
	 * 
	 */
	public String getNoteTypeCode() { 
		return noteTypeCode;
	}

	/**
	 * Sets the noteTypeCode attribute.
	 * 
	 * @param noteTypeCode The noteTypeCode to set.
	 * 
	 */
	public void setNoteTypeCode(String noteTypeCode) {
		this.noteTypeCode = noteTypeCode;
	}


	/**
	 * Gets the noteTypeDescription attribute.
	 * 
	 * @return Returns the noteTypeDescription
	 * 
	 */
	public String getNoteTypeDescription() { 
		return noteTypeDescription;
	}

	/**
	 * Sets the noteTypeDescription attribute.
	 * 
	 * @param noteTypeDescription The noteTypeDescription to set.
	 * 
	 */
	public void setNoteTypeDescription(String noteTypeDescription) {
		this.noteTypeDescription = noteTypeDescription;
	}


	/**
	 * Gets the noteTypeActiveIndicator attribute.
	 * 
	 * @return Returns the noteTypeActiveIndicator
	 * 
	 */
	public boolean isNoteTypeActiveIndicator() { 
		return noteTypeActiveIndicator;
	}
	

	/**
	 * Sets the noteTypeActiveIndicator attribute.
	 * 
	 * @param noteTypeActiveIndicator The noteTypeActiveIndicator to set.
	 * 
	 */
	public void setNoteTypeActiveIndicator(boolean noteTypeActiveIndicator) {
		this.noteTypeActiveIndicator = noteTypeActiveIndicator;
	}
}

