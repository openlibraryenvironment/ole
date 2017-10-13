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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria.QueryByCriteriaType;
import org.kuali.rice.krad.dao.MaintenanceDocumentDao;
import org.kuali.rice.krad.maintenance.MaintenanceLock;
import org.kuali.rice.krad.util.KRADPropertyConstants;

/**
 * This class is the JPA implementation of the MaintenanceDocumentDao interface.
 */
public class MaintenanceDocumentDaoJpa implements MaintenanceDocumentDao {

//	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MaintenanceDocumentDaoJpa.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * @see org.kuali.rice.krad.dao.MaintenanceDocumentDao#getLockingDocumentNumber(java.lang.String, java.lang.String)
	 */
	public String getLockingDocumentNumber(String lockingRepresentation, String documentNumber) {
		String lockingDocNumber = "";

		// build the query criteria
		Criteria criteria = new Criteria(MaintenanceLock.class.getName());
		criteria.eq("lockingRepresentation", lockingRepresentation);

		// if a docHeaderId is specified, then it will be excluded from the
		// locking representation test.
		if (StringUtils.isNotBlank(documentNumber)) {
			criteria.ne(KRADPropertyConstants.DOCUMENT_NUMBER, documentNumber);
		}

		// attempt to retrieve a document based off this criteria
		MaintenanceLock maintenanceLock = (MaintenanceLock) new QueryByCriteria(entityManager, criteria).toQuery().getSingleResult();

		// if a document was found, then there's already one out there pending,
		// and we consider it 'locked' and we return the docnumber.
		if (maintenanceLock != null) {
			lockingDocNumber = maintenanceLock.getDocumentNumber();
		}
		return lockingDocNumber;
	}

//	/**
//	 * Returns all pending maintenance documents locked by the given business object class.
//	 */
//	public Collection getPendingDocumentsForClass(Class dataObjectClass) {
//		Criteria criteria = new Criteria(MaintenanceLock.class.getName());
//		criteria.like("lockingRepresentation", "%" + dataObjectClass.getName() + "%");
//
//		Collection maintenanceLocks = new QueryByCriteria(entityManager, criteria).toQuery().getResultList();
//		if (!maintenanceLocks.isEmpty()) {
//			criteria = new Criteria(MaintenanceDocumentBase.class.getName());
//			List<String> documentNumbers = new ArrayList<String>();
//
//			for (Object maintenanceLock : maintenanceLocks) {
//				documentNumbers.add(((MaintenanceLock) maintenanceLock).getDocumentNumber());
//			}
//			criteria.in("documentNumber", documentNumbers);
//
//			return new QueryByCriteria(entityManager, criteria).toQuery().getResultList();
//		} else {
//			return maintenanceLocks;
//		}
//	}

	/**
	 * @see org.kuali.rice.krad.dao.MaintenanceDocumentDao#deleteLocks(java.lang.String)
	 */
	public void deleteLocks(String documentNumber) {
		Criteria criteria = new Criteria(MaintenanceLock.class.getName());
		criteria.eq("documentNumber", documentNumber);
		new QueryByCriteria(entityManager, criteria, QueryByCriteriaType.DELETE).toQuery().executeUpdate();
	}

	/**
	 * @see org.kuali.rice.krad.dao.MaintenanceDocumentDao#storeLocks(java.util.List)
	 */
	public void storeLocks(List<MaintenanceLock> maintenanceLocks) {
		for (MaintenanceLock maintenanceLock : maintenanceLocks) {
			entityManager.merge(maintenanceLock);
		}
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
