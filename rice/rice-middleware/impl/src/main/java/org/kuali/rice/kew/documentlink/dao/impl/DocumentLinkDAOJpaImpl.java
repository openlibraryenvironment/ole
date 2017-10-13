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

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.kew.documentlink.DocumentLink;
import org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * This is a description of what this class does - g1zhang don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentLinkDAOJpaImpl implements DocumentLinkDAO {

	
    @PersistenceContext(unitName = "kew-unit")
    private EntityManager entityManager;
    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
	/**
	 * double delete all links from orgn doc
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#deleteDocmentLinksByDocId(java.lang.Long)
	 */
	public void deleteDocmentLinksByDocId(String docId) {
		List<DocumentLink> links = getLinkedDocumentsByDocId(docId);
		for(DocumentLink link: links){
			deleteDocumentLink(link);
		}
	}

	/**
	 * double delete a link
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#deleteDocumentLink(org.kuali.rice.kew.documentlink.DocumentLink)
	 */
	public void deleteDocumentLink(DocumentLink link) {
		deleteSingleLinkFromOrgnDoc(link);
		deleteSingleLinkFromOrgnDoc(DocumentLinkDaoUtil.reverseLink((DocumentLink)ObjectUtils.deepCopy(link)));
	}

	/**
	 * get a link from orgn doc
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#getLinkedDocument(org.kuali.rice.kew.documentlink.DocumentLink)
	 */
	public DocumentLink getLinkedDocument(DocumentLink link) {
		Criteria crit = new Criteria(DocumentLink.class.getName());
		crit.eq("orgnDocId", link.getOrgnDocId());
		crit.eq("destDocId", link.getDestDocId());
		try {
			return (DocumentLink) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * get all links from orgn doc
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#getLinkedDocumentsByDocId(java.lang.Long)
	 */
	public List<DocumentLink> getLinkedDocumentsByDocId(String docId) {
		Criteria crit = new Criteria(DocumentLink.class.getName());
		crit.eq("orgnDocId", docId);
		return (List<DocumentLink>) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
	
	}
	
	public List<DocumentLink> getOutgoingLinkedDocumentsByDocId(String docId) {
		Criteria crit = new Criteria(DocumentLink.class.getName());
		crit.eq("destDocId", docId);
		return (List<DocumentLink>) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
	}

	/**
	 * add double link
	 * 
	 * @see org.kuali.rice.kew.documentlink.dao.DocumentLinkDAO#saveDocumentLink(org.kuali.rice.kew.documentlink.DocumentLink)
	 */
	public void saveDocumentLink(DocumentLink link) {
		DocumentLink linkedDocument = getLinkedDocument(link);
		if(linkedDocument == null) {
			if (link.getDocLinkId() == null) {
				entityManager.persist(link);
			} else {
				OrmUtils.merge(entityManager, link);
			}
		} else {
			link.setDocLinkId(linkedDocument.getDocLinkId());
		}
//		//if we want a 2-way linked pair
		DocumentLink rLink = DocumentLinkDaoUtil.reverseLink((DocumentLink)ObjectUtils.deepCopy(link));
		if(getLinkedDocument(rLink) == null) {
			if (link.getDocLinkId() == null) {
				entityManager.persist(rLink);
			} else {
				OrmUtils.merge(entityManager, rLink);
			}
		}

	}
	
	private void deleteSingleLinkFromOrgnDoc(DocumentLink link){
		DocumentLink cur = getLinkedDocument(link);
		entityManager.remove(cur) ;
	}

	@Override
	public DocumentLink getDocumentLink(Long documentLinkId) {
		Criteria crit = new Criteria(DocumentLink.class.getName());
		crit.eq("docLinkId", documentLinkId);
		return (DocumentLink) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
	}
	
}
