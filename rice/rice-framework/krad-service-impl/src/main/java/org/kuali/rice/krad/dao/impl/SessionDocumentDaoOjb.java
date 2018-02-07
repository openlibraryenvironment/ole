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

import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;
import org.kuali.rice.krad.bo.SessionDocument;
import org.kuali.rice.krad.dao.BusinessObjectDao;
import org.kuali.rice.krad.dao.SessionDocumentDao;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.springframework.dao.DataAccessException;

/**
 * This class is the OJB implementation of the DocumentDao interface.
 */
public class SessionDocumentDaoOjb extends PlatformAwareDaoBaseOjb implements SessionDocumentDao{
    private static Logger LOG = Logger.getLogger(SessionDocumentDaoOjb.class);
    private BusinessObjectDao businessObjectDao;
    

    public SessionDocumentDaoOjb() {
        super();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see SessionDocumentDao#purgeAllSessionDocuments(java.sql.Timestamp)
     */
    public void purgeAllSessionDocuments(Timestamp expirationDate)throws DataAccessException {
    	Criteria criteria = new Criteria();
		criteria.addLessThan(KRADPropertyConstants.LAST_UPDATED_DATE, expirationDate);
		 getPersistenceBrokerTemplate().deleteByQuery(QueryFactory.newQuery(SessionDocument.class, criteria));
		 //getPersistenceBrokerTemplate().clearCache();
	        
    }

 
}
