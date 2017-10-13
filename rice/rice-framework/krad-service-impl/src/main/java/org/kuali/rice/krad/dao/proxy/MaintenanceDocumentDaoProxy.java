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
package org.kuali.rice.krad.dao.proxy;

import java.util.List;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.krad.dao.MaintenanceDocumentDao;
import org.kuali.rice.krad.maintenance.MaintenanceLock;

public class MaintenanceDocumentDaoProxy implements MaintenanceDocumentDao {

    private MaintenanceDocumentDao maintenanceDocumentDaoJpa;
    private MaintenanceDocumentDao maintenanceDocumentDaoOjb;
	
    @SuppressWarnings("unchecked")
	private MaintenanceDocumentDao getDao(Class clazz) {
    	final String TMP_NM = clazz.getName();
		final int START_INDEX = TMP_NM.indexOf('.', TMP_NM.indexOf('.') + 1) + 1;
    	return (OrmUtils.isJpaAnnotated(clazz) && (OrmUtils.isJpaEnabled() || OrmUtils.isJpaEnabled("rice.krad"))) ?
						maintenanceDocumentDaoJpa : maintenanceDocumentDaoOjb; 
    }
    
	public String getLockingDocumentNumber(String lockingRepresentation, String documentNumber) {
		return getDao(MaintenanceLock.class).getLockingDocumentNumber(lockingRepresentation, documentNumber);
	}

//	public Collection getPendingDocumentsForClass(Class dataObjectClass) {
//		return getDao(MaintenanceLock.class).getPendingDocumentsForClass(dataObjectClass);
//	}

	public void deleteLocks(String documentNumber) {
		getDao(MaintenanceLock.class).deleteLocks(documentNumber);
	}

	public void storeLocks(List<MaintenanceLock> maintenanceLocks) {
		getDao(MaintenanceLock.class).storeLocks(maintenanceLocks);
	}

	public void setMaintenanceDocumentDaoJpa(MaintenanceDocumentDao maintenanceDocumentDaoJpa) {
		this.maintenanceDocumentDaoJpa = maintenanceDocumentDaoJpa;
	}

	public void setMaintenanceDocumentDaoOjb(MaintenanceDocumentDao maintenanceDocumentDaoOjb) {
		this.maintenanceDocumentDaoOjb = maintenanceDocumentDaoOjb;
	}

}
