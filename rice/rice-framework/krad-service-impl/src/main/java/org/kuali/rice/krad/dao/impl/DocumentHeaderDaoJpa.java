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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.dao.DocumentHeaderDao;

/**
 * This class is the JPA implementation of the DocumentHeaderDao interface.
 */
public class DocumentHeaderDaoJpa implements DocumentHeaderDao {
	
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentHeaderDaoJpa.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
     * @see org.kuali.rice.krad.dao.DocumentHeaderDao#getDocumentHeaderBaseClass()
     */
    public Class getDocumentHeaderBaseClass() {
        LOG.debug("Method getDocumentHeaderBaseClass() returning class " + DocumentHeader.class.getName());
        return DocumentHeader.class;
    }
	
    /**
     * @see org.kuali.dao.DocumentHeaderDao#getByDocumentHeaderId(java.lang.Long)
     */
    public DocumentHeader getByDocumentHeaderId(String id) {
		Criteria criteria = new Criteria(DocumentHeader.class.getName());
		criteria.eq("FDOC_NBR", id);		
		return (DocumentHeader) new QueryByCriteria(entityManager, criteria).toQuery().getSingleResult();
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

}
