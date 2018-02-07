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
package org.kuali.rice.krad.dao.jdbc;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.PBKey;
import org.apache.ojb.broker.PersistenceBroker;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.ModuleConfiguration;
import org.kuali.rice.krad.dao.SequenceAccessorDao;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;
import org.springmodules.orm.ojb.OjbFactoryUtils;

import javax.persistence.EntityManager;

/**
 * This class uses the KualiDBPlatform to get the next number from a given sequence.
 */
public class SequenceAccessorDaoJdbc extends PlatformAwareDaoBaseJdbc implements SequenceAccessorDao {
	private KualiModuleService kualiModuleService;
	
	private Long nextAvailableSequenceNumber(String sequenceName, 
			Class<? extends BusinessObject> clazz) {
		
        ModuleService moduleService = getKualiModuleService().getResponsibleModuleService(clazz);
        if ( moduleService == null )
        	throw new ConfigurationException("moduleService is null");
        	        	
        ModuleConfiguration moduleConfig = moduleService.getModuleConfiguration();
        if ( moduleConfig == null )
        	throw new ConfigurationException("moduleConfiguration is null");
        
    	if ( OrmUtils.isJpaAnnotated(clazz) && ( OrmUtils.isJpaEnabled() ||	OrmUtils.isJpaEnabled("rice.krad") ) ) {
    		EntityManager entityManager = moduleConfig.getEntityManager();
    		
            if ( entityManager != null ) 
            	return getDbPlatform().getNextValSQL(sequenceName, entityManager);
            else
            	throw new ConfigurationException("EntityManager is null");
        } 
    	else {
    		String dataSourceName = moduleConfig.getDataSourceName();
    		if ( StringUtils.isEmpty(dataSourceName) ) 
            	throw new ConfigurationException("dataSourceName is not set");
    		
    		PBKey key = new PBKey(dataSourceName);
    		PersistenceBroker broker = OjbFactoryUtils.getPersistenceBroker(key, false);
    		if ( broker != null )
    			return getDbPlatform().getNextValSQL(sequenceName, broker);
    		else
    			throw new ConfigurationException("PersistenceBroker is null");            			
        }
	}
	
	public Long getNextAvailableSequenceNumber(String sequenceName, 
			Class<? extends BusinessObject> clazz) {
		
		// There are situations where a module hasn't been configured with
		// a dataSource.  In these cases, this method would have previously
		// thrown an error.  Instead, we've opted to factor out the code,
		// catch any configuration-related exceptions, and if one occurs,
		// attempt to use the dataSource associated with KNS. -- tbradford
		
		try {
			return nextAvailableSequenceNumber(sequenceName, clazz);
		}
		catch ( ConfigurationException e  ) {
	    	// Use DocumentHeader to get the dataSourceName associated with KNS			
			return nextAvailableSequenceNumber(sequenceName, DocumentHeader.class);			
		}
	}
	
    /**
     * @see org.kuali.rice.krad.dao.SequenceAccessorDao#getNextAvailableSequenceNumber(java.lang.String)
     */
    public Long getNextAvailableSequenceNumber(String sequenceName) {
    	// Use DocumentHeader to get the dataSourceName associated with KNS
    	return nextAvailableSequenceNumber(sequenceName, DocumentHeader.class);
    }
    
    private KualiModuleService getKualiModuleService() {
        if ( kualiModuleService == null ) 
            kualiModuleService = KRADServiceLocatorWeb.getKualiModuleService();
        return kualiModuleService;
    }
}
