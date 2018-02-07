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
package org.kuali.rice.edl.framework.extract;

import java.io.Serializable;

public class FieldDTO implements Serializable {

	private static final long serialVersionUID = -6136544551121011531L;

	private Long fieldId;
	private String docId;
	private String fieldName;
	private String fieldValue;
	private Integer lockVerNbr;

	public Long getFieldId() {
		return fieldId;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(final String docId) {
		this.docId = docId;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(final String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public String getFiledName() {
		return fieldName;
	}
	public void setFieldName(final String filedName) {
		this.fieldName = filedName;
	}
	public Integer getLockVerNbr() {
		return lockVerNbr;
	}
	public void setLockVerNbr(final Integer lockVerNbr) {
		this.lockVerNbr = lockVerNbr;
	}
}

