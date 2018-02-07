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
package org.kuali.rice.kew.documentlink.dao.impl;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.rice.kew.documentlink.DocumentLink;
import org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

/**
 * This is a description of what this class does - g1zhang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public class DocumentLinkDAOOjbImpl extends PersistenceBrokerDaoSupport implements DocumentLinkDAO{

	public void saveDocumentLink(DocumentLink link) {
		DocumentLink linkedDocument = getLinkedDocument(link);
		if(linkedDocument == null)
			this.getPersistenceBrokerTemplate().store(link);
		else
			link.setDocLinkId(linkedDocument.getDocLinkId());
		//if we want a 2-way linked pair
		DocumentLink rLink = DocumentLinkDaoUtil.reverseLink(link);
		if(getLinkedDocument(rLink) == null)
			this.getPersistenceBrokerTemplate().store(rLink);
	}

	public void deleteDocumentLink(DocumentLink link) {
		deleteSingleLinkFromOrgnDoc(link);
		deleteSingleLinkFromOrgnDoc(DocumentLinkDaoUtil.reverseLink(link));
	}

	//double delete style
	public void deleteDocmentLinksByDocId(String docId){
		List<DocumentLink> links = getLinkedDocumentsByDocId(docId);
		for(DocumentLink link: links){
			deleteDocumentLink(link);
		}
		
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#getLinkedDocumentByDocId(java.lang.Long)
	 */
	public List<DocumentLink> getLinkedDocumentsByDocId(String docId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("orgnDocId", docId);
		QueryByCriteria query = new QueryByCriteria(DocumentLink.class, crit);
		query.addOrderByAscending("orgnDocId");
		return (List<DocumentLink>) this.getPersistenceBrokerTemplate().getCollectionByQuery(query);  
	}
	
	public List<DocumentLink> getOutgoingLinkedDocumentsByDocId(String docId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("destDocId", docId);
		QueryByCriteria query = new QueryByCriteria(DocumentLink.class, crit);
		query.addOrderByAscending("destDocId");
		return (List<DocumentLink>) this.getPersistenceBrokerTemplate().getCollectionByQuery(query);  
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#getLinkedDocument(org.kuali.rice.kew.documentlink.DocumentLink)
	 */
	public DocumentLink getLinkedDocument(DocumentLink link) {
		Criteria crit = new Criteria();
		crit.addEqualTo("orgnDocId", link.getOrgnDocId());
		crit.addEqualTo("destDocId", link.getDestDocId());
		QueryByCriteria query = new QueryByCriteria(DocumentLink.class, crit);
		query.addOrderByAscending("orgnDocId");
		return (DocumentLink) this.getPersistenceBrokerTemplate().getObjectByQuery(query);  
	}

	private void deleteSingleLinkFromOrgnDoc(DocumentLink link){
		Criteria crit = new Criteria();
		crit.addEqualTo("orgnDocId", link.getOrgnDocId());
		crit.addEqualTo("destDocId", link.getDestDocId());
		this.getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(DocumentLink.class, crit));
	}

	public void deleteSingleLinksByOrgnDocId(String docId){
		Criteria crit = new Criteria();
		crit.addEqualTo("orgnDocId", docId);
		this.getPersistenceBrokerTemplate().deleteByQuery(new QueryByCriteria(DocumentLink.class, crit));
	}
	
	public DocumentLink getDocumentLink(Long documentLinkId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("docLinkId", documentLinkId);
		return (DocumentLink) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(DocumentLink.class, crit));
	}
}
