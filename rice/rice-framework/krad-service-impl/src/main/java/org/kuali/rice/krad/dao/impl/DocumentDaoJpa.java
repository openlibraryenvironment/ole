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
package org.kuali.rice.krad.dao.impl;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.krad.dao.BusinessObjectDao;
import org.kuali.rice.krad.dao.DocumentDao;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentAdHocService;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.springframework.dao.DataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the OJB implementation of the DocumentDao interface.
 */
public class DocumentDaoJpa implements DocumentDao {
    
	private BusinessObjectDao businessObjectDao;
    private DocumentAdHocService documentAdHocService;

	@PersistenceContext
	private EntityManager entityManager;

    public DocumentDaoJpa(EntityManager entityManager, BusinessObjectDao businessObjectDao, DocumentAdHocService documentAdHocService) {
        super();
        this.entityManager = entityManager;
        this.businessObjectDao = businessObjectDao;
        this.documentAdHocService = documentAdHocService;
    }

    /**
     * @see org.kuali.dao.DocumentDao#save(null)
     */
    @Override
    public <T extends Document> T save(T document) throws DataAccessException {
		T attachedDoc = (T) findByDocumentHeaderId(document.getClass(),document.getDocumentNumber());
		if (attachedDoc == null) {
			entityManager.persist(document.getDocumentHeader());
			entityManager.persist(document);
			return document;
		}
		OrmUtils.reattach(document, attachedDoc);
		return entityManager.merge(attachedDoc);
    }

	/**
     * Retrieve a Document of a specific type with a given document header ID.
     *
     * @param clazz
     * @param id
     * @return Document with given id
     */
    @Override
    public <T extends Document> T findByDocumentHeaderId(Class<T> clazz, String id) {
        return entityManager.find(clazz, id);
    }

    /**
     * Retrieve a List of Document instances with the given ids
     *
     * @param clazz
     * @param idList
     * @return List
     */
    @Override
    public <T extends Document> List<T> findByDocumentHeaderIds(Class<T> clazz, List<String> idList) {
		Criteria criteria = new Criteria(clazz.getName());
		criteria.in(KRADPropertyConstants.DOCUMENT_NUMBER, idList);
		List<T> list = new ArrayList<T>();
		try {
			list = new QueryByCriteria(entityManager, criteria).toQuery().getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
        for (T doc : list) {
        	documentAdHocService.addAdHocs(doc);
			entityManager.refresh(doc);
        }
		return list;
    }

    @Override
    public BusinessObjectDao getBusinessObjectDao() {
        return businessObjectDao;
    }

    public void setBusinessObjectDao(BusinessObjectDao businessObjectDao) {
        this.businessObjectDao = businessObjectDao;
    }

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
	 * @return the documentAdHocService
	 */
    @Override
	public DocumentAdHocService getDocumentAdHocService() {
		return this.documentAdHocService;
	}

    /**
     * Setter for injecting the DocumentAdHocService
     * @param dahs
     */
    public void setDocumentAdHocService(DocumentAdHocService dahs) {
    	this.documentAdHocService = dahs;
    }

}
