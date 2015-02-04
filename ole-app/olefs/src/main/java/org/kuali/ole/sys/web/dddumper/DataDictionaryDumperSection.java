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

import java.util.ArrayList;
import java.util.List;

public class DataDictionaryDumperSection{
	protected static final long serialVersionUID = 3632283509506923869L;

	protected String docName = "";
	protected String docId = "";
	protected String businessObject = "";
	protected String businessObjectRulesClass = "";
	protected String documentAuthorizerClass = "";
	protected String table = "";
	protected boolean collection = false;
	protected List<DataDictionaryDumperDocumentRow> documentRows = new ArrayList<DataDictionaryDumperDocumentRow>();

	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public boolean isCollection() {
		return collection;
	}
	public void setCollection(boolean collection) {
		this.collection = collection;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getBusinessObject() {
		return businessObject;
	}
	public void setBusinessObject(String businessObject) {
		this.businessObject = businessObject;
	}
	public String getBusinessObjectRulesClass() {
		return businessObjectRulesClass;
	}
	public void setBusinessObjectRulesClass(String businessObjectRulesClass) {
		this.businessObjectRulesClass = businessObjectRulesClass;
	}
	public String getDocumentAuthorizerClass() {
		return documentAuthorizerClass;
	}
	public void setDocumentAuthorizerClass(String documentAuthorizerClass) {
		this.documentAuthorizerClass = documentAuthorizerClass;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public List<DataDictionaryDumperDocumentRow> getDocumentRows() {
		return documentRows;
	}
	public void setDocumentRows(List<DataDictionaryDumperDocumentRow> documentRows) {
		this.documentRows = documentRows;
	}
}