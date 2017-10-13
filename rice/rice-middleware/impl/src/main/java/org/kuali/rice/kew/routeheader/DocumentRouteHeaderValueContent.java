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
package org.kuali.rice.kew.routeheader;

import org.kuali.rice.core.api.util.xml.XmlJotter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="KREW_DOC_HDR_CNTNT_T")
@NamedQuery(name="DocumentRouteHeaderValueContent.FindByDocumentId", query="select d from DocumentRouteHeaderValueContent as d where d.documentId = :documentId")
public class DocumentRouteHeaderValueContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="DOC_HDR_ID")
	private String documentId;
	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="DOC_CNTNT_TXT")
	private String documentContent;
		
	public DocumentRouteHeaderValueContent() {}
	
	public DocumentRouteHeaderValueContent(String documentId) {
		this.documentId = documentId;
	}
	
	public String getDocumentContent() {
		return documentContent;
	}
	public void setDocumentContent(String documentContent) {
		this.documentContent = documentContent;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	
	public static org.kuali.rice.kew.api.document.DocumentContent to(DocumentRouteHeaderValueContent content) {
		if (content == null) {
			return null;
		}
		org.kuali.rice.kew.api.document.DocumentContent.Builder builder = org.kuali.rice.kew.api.document.DocumentContent.Builder.create(content.getDocumentId());
		// initialize the content fields
		builder.setApplicationContent("");
		builder.setAttributeContent("");
		builder.setSearchableContent("");
		DocumentContent documentContent = new StandardDocumentContent(content.getDocumentContent());
		if (documentContent.getApplicationContent() != null) {
			builder.setApplicationContent(XmlJotter.jotNode(documentContent.getApplicationContent()));
		}
		if (documentContent.getAttributeContent() != null) {
			builder.setAttributeContent(XmlJotter.jotNode(documentContent.getAttributeContent()));
		}
		if (documentContent.getSearchableContent() != null) {
			builder.setSearchableContent(XmlJotter.jotNode(documentContent.getSearchableContent()));
		}
		return builder.build();
	}

}

