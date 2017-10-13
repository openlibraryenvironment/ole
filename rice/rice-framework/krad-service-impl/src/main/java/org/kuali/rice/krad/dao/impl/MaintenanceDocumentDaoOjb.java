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

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;
import org.kuali.rice.krad.dao.MaintenanceDocumentDao;
import org.kuali.rice.krad.maintenance.MaintenanceLock;
import org.kuali.rice.krad.util.KRADPropertyConstants;

/**
 * This class is the OJB implementation of the MaintenanceDocumentDao interface.
 */
public class MaintenanceDocumentDaoOjb extends PlatformAwareDaoBaseOjb implements MaintenanceDocumentDao {

//    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MaintenanceDocumentDaoOjb.class);

    /**
     * @see org.kuali.rice.krad.dao.MaintenanceDocumentDao#getLockingDocumentNumber(java.lang.String, java.lang.String)
     */
    public String getLockingDocumentNumber(String lockingRepresentation, String documentNumber) {

        String lockingDocNumber = "";

        // build the query criteria
        Criteria criteria = new Criteria();
        criteria.addEqualTo("lockingRepresentation", lockingRepresentation);

        // if a docHeaderId is specified, then it will be excluded from the
        // locking representation test.
        if (StringUtils.isNotBlank(documentNumber)) {
            criteria.addNotEqualTo(KRADPropertyConstants.DOCUMENT_NUMBER, documentNumber);
        }

        // attempt to retrieve a document based off this criteria
        MaintenanceLock maintenanceLock = (MaintenanceLock) getPersistenceBrokerTemplate().getObjectByQuery(QueryFactory.newQuery(MaintenanceLock.class, criteria));

        // if a document was found, then there's already one out there pending, and
        // we consider it 'locked' and we return the docnumber.
        if (maintenanceLock != null) {
            lockingDocNumber = maintenanceLock.getDocumentNumber();
        }
        return lockingDocNumber;
    }

//    /**
//     * Returns all pending maintenance documents locked by the given business object class.
//     */
//    public Collection getPendingDocumentsForClass(Class dataObjectClass) {
//        Criteria criteria = new Criteria();
//        criteria.addLike("lockingRepresentation", "%" + dataObjectClass.getName() + "%");
//
//        Collection maintenanceLocks = getPersistenceBrokerTemplate().getCollectionByQuery(QueryFactory.newQuery(MaintenanceLock.class, criteria));
//
//        if (!maintenanceLocks.isEmpty()) {
//            criteria = new Criteria();
//            Collection<String> documentNumbers = new ArrayList();
//
//            for (Object maintenanceLock : maintenanceLocks) {
//                documentNumbers.add(((MaintenanceLock) maintenanceLock).getDocumentNumber());
//            }
//            criteria.addIn("documentNumber", documentNumbers);
//
//            MaintenanceDocumentEntry entry = KRADServiceLocatorInternal.getDataDictionaryService().getDataDictionary().getMaintenanceDocumentEntryForBusinessObjectClass(dataObjectClass);
//            return getPersistenceBrokerTemplate().getCollectionByQuery(QueryFactory.newQuery(entry.getStandardDocumentBaseClass(), criteria));
//        } else {
//            return maintenanceLocks;
//        }
//    }

    /**
     * @see org.kuali.rice.krad.dao.MaintenanceDocumentDao#deleteLocks(java.lang.String)
     */
    public void deleteLocks(String documentNumber) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("documentNumber", documentNumber);
        QueryByCriteria query = new QueryByCriteria(MaintenanceLock.class, criteria);
        getPersistenceBrokerTemplate().deleteByQuery(query);
    }

    /**
     * @see org.kuali.rice.krad.dao.MaintenanceDocumentDao#storeLocks(java.util.List)
     */
    public void storeLocks(List<MaintenanceLock> maintenanceLocks) {
        for (MaintenanceLock maintenanceLock : maintenanceLocks) {
            getPersistenceBrokerTemplate().store(maintenanceLock);
        }
    }

}
