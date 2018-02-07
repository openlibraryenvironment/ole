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
package org.kuali.rice.kew.documentlink;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.api.document.DocumentLinkContract;
import org.kuali.rice.kew.service.KEWServiceLocator;

/**
 * Server side bean for DocumentLinkDAO 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */

@Entity
@Table(name="KREW_DOC_LNK_T")
//@Sequence(name="KREW_DOC_LNK_S",property="docLinkId")
public class DocumentLink implements Serializable, DocumentLinkContract {

	private static final long serialVersionUID = 551926904795633010L;
	
	@Id
	@GeneratedValue(generator="KREW_DOC_LNK_S")
	@GenericGenerator(name="KREW_DOC_LNK_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_DOC_LNK_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="DOC_LNK_ID")
	private String docLinkId;
    @Column(name="ORGN_DOC_ID")
	private String orgnDocId;
    @Column(name="DEST_DOC_ID")
	private String destDocId;
    
	/**
	 * @return the docLinkId
	 */
	public String getDocLinkId() {
		return this.docLinkId;
	}

	/**
	 * @param docLinkId the docLinkId to set
	 */
	public void setDocLinkId(String docLinkId) {
		this.docLinkId = docLinkId;
	}

	/**
	 * @return the orgnDocId
	 */
	public String getOrgnDocId() {
		return this.orgnDocId;
	}

	/**
	 * @param orgnDocId the orgnDocId to set
	 */
	public void setOrgnDocId(String orgnDocId) {
		this.orgnDocId = orgnDocId;
	}

	/**
	 * @return the destDocId
	 */
	public String getDestDocId() {
		return this.destDocId;
	}

	/**
	 * @param destDocId the destDocId to set
	 */
	public void setDestDocId(String destDocId) {
		this.destDocId = destDocId;
	}
	
	//@PrePersist
	public void beforeInsert(){
		OrmUtils.populateAutoIncValue(this, KEWServiceLocator.getEntityManagerFactory().createEntityManager());		
	}
	
	// new contract methods for Rice 2.0
	
	@Override
	public String getId() {
		if (getDocLinkId() == null) {
			return null;
		}
		return getDocLinkId().toString();
	}

	@Override
	public String getOriginatingDocumentId() {
		return getOrgnDocId();
	}

	@Override
	public String getDestinationDocumentId() {
		return getDestDocId();
	}
	
	public static org.kuali.rice.kew.api.document.DocumentLink to(DocumentLink documentLinkBo) {
		if (documentLinkBo == null) {
			return null;
		}
		return org.kuali.rice.kew.api.document.DocumentLink.Builder.create(documentLinkBo).build();
	}

	public static DocumentLink from(org.kuali.rice.kew.api.document.DocumentLink documentLink) {
		if (documentLink == null) {
			return null;
		}
		DocumentLink documentLinkBo = new DocumentLink();
		if (documentLink.getId() != null) {
			documentLinkBo.setDocLinkId(documentLink.getId());
		}
		documentLinkBo.setOrgnDocId(documentLink.getOriginatingDocumentId());
		documentLinkBo.setDestDocId(documentLink.getDestinationDocumentId());
		return documentLinkBo;
	}
	
}
