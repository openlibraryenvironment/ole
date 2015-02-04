/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.sys.web.dddumper;

public class DataDictionaryDumperDocumentRow implements Comparable<DataDictionaryDumperDocumentRow> {

	private String name;
	private String fieldName;
	private String column;
	private int columnNo;
	private boolean required;
	private String fieldType;
	private String defaultValue;
	private boolean readOnly;
	private String validationRules;
	private String maxLength;
	private boolean existenceCheck;
	private boolean lookupParam;
	private boolean lookupResult;
	private boolean onInquiry;
	private String controlDefinition;
	private String fieldSecurity;
	
	public int sortColumnValue(int colNo){
		if (colNo==0) return 10000;
		return colNo;
	}
//	public String toString(){
//		return this.getName()+this.getClass().getName()+this.getFieldName()+this.getFieldType();
//	}
	
	public int compareTo(DataDictionaryDumperDocumentRow other){
		int tmpVal = new Integer(sortColumnValue(this.getColumnNo())).compareTo(sortColumnValue(other.getColumnNo()));
		if (tmpVal !=0) {
			return tmpVal;
		}else{
			return (this.getName()+this.getClass().getName()+this.getFieldName()+this.getFieldType()).compareTo(other.getName()+other.getClass().getName()+other.getFieldName()+other.getFieldType());
		}
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getColumn() {
		return this.column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	
	public int getColumnNo() {
		return columnNo;
	}
	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
	}
	public boolean isRequired() {
		return this.required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getFieldType() {
		return this.fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getDefaultValue() {
		return this.defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public boolean isReadOnly() {
		return this.readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public String getValidationRules() {
		return this.validationRules;
	}
	public void setValidationRules(String validationRules) {
		this.validationRules = validationRules;
	}
	public String getMaxLength() {
		return this.maxLength;
	}
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	public boolean isExistenceCheck() {
		return this.existenceCheck;
	}
	public void setExistenceCheck(boolean existenceCheck) {
		this.existenceCheck = existenceCheck;
	}
	public boolean isLookupParam() {
		return this.lookupParam;
	}
	public void setLookupParam(boolean lookupParam) {
		this.lookupParam = lookupParam;
	}
	public boolean isLookupResult() {
		return this.lookupResult;
	}
	public void setLookupResult(boolean lookupResult) {
		this.lookupResult = lookupResult;
	}
	public boolean isOnInquiry() {
		return this.onInquiry;
	}
	public void setOnInquiry(boolean onInquiry) {
		this.onInquiry = onInquiry;
	}

	public String getControlDefinition() {
		return controlDefinition;
	}

	public void setControlDefinition(String controlDefinition) {
		this.controlDefinition = controlDefinition;
	}

	public String getFieldSecurity() {
		return fieldSecurity;
	}

	public void setFieldSecurity(String fieldSecurity) {
		this.fieldSecurity = fieldSecurity;
	}
	
	
}