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
package org.kuali.rice.krad.service.impl;

import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.dao.BusinessObjectDao;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KeyValuesService;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.service.PersistenceStructureService;
import org.kuali.rice.krad.util.KRADPropertyConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * This class provides collection retrievals to populate key value pairs of business objects.
 */
public class KeyValuesServiceImpl implements KeyValuesService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(KeyValuesServiceImpl.class);

    private BusinessObjectDao businessObjectDao;
    private PersistenceStructureService persistenceStructureService;
    
    /**
     * @see org.kuali.rice.krad.service.KeyValuesService#findAll(java.lang.Class)
     */
    @Override
	public <T extends BusinessObject> Collection<T> findAll(Class<T> clazz) {
    	ModuleService responsibleModuleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(clazz);
		if(responsibleModuleService!=null && responsibleModuleService.isExternalizable(clazz)){
			return (Collection<T>) responsibleModuleService.getExternalizableBusinessObjectsList((Class<ExternalizableBusinessObject>) clazz, Collections.<String, Object>emptyMap());
		}
        if (containsActiveIndicator(clazz)) {
            return businessObjectDao.findAllActive(clazz);
        }
        if (LOG.isDebugEnabled()) {
			LOG.debug("Active indicator not found for class " + clazz.getName());
		}
        return businessObjectDao.findAll(clazz);
    }
    
	public static <E> Collection<E> createUnmodifiableUpcastList(Collection<? extends E> list, Class<E> type) {
		return new ArrayList<E>(list);
	}

    /**
     * @see org.kuali.rice.krad.service.KeyValuesService#findAllOrderBy(java.lang.Class, java.lang.String, boolean)
     */
    @Override
	public <T extends BusinessObject> Collection<T> findAllOrderBy(Class<T> clazz, String sortField, boolean sortAscending) {
        if (containsActiveIndicator(clazz)) {
            return businessObjectDao.findAllActiveOrderBy(clazz, sortField, sortAscending);
        }
        if (LOG.isDebugEnabled()) {
			LOG.debug("Active indicator not found for class " + clazz.getName());
		}
        return businessObjectDao.findAllOrderBy(clazz, sortField, sortAscending);
    }

    /**
     * @see org.kuali.rice.krad.service.BusinessObjectService#findMatching(java.lang.Class, java.util.Map)
     */
    @Override
	public <T extends BusinessObject> Collection<T> findMatching(Class<T> clazz, Map<String, Object> fieldValues) {
        if (containsActiveIndicator(clazz)) {
            return businessObjectDao.findMatchingActive(clazz, fieldValues);
        }
        if (LOG.isDebugEnabled()) {
			LOG.debug("Active indicator not found for class " + clazz.getName());
		}
        return businessObjectDao.findMatching(clazz, fieldValues);
    }



    /**
     * @return Returns the businessObjectDao.
     */
    public BusinessObjectDao getBusinessObjectDao() {
        return businessObjectDao;
    }

    /**
     * @param businessObjectDao The businessObjectDao to set.
     */
    public void setBusinessObjectDao(BusinessObjectDao businessObjectDao) {
        this.businessObjectDao = businessObjectDao;
    }

    /**
     * Gets the persistenceStructureService attribute.
     * 
     * @return Returns the persistenceStructureService.
     */
    public PersistenceStructureService getPersistenceStructureService() {
        return persistenceStructureService;
    }

    /**
     * Sets the persistenceStructureService attribute value.
     * 
     * @param persistenceStructureService The persistenceStructureService to set.
     */
    public void setPersistenceStructureService(PersistenceStructureService persistenceStructureService) {
        this.persistenceStructureService = persistenceStructureService;
    }

    /**
     * Uses persistence service to determine if the active column is mapped up in ojb.
     * 
     * @param clazz
     * @return boolean if active column is mapped for Class
     */
    private <T extends BusinessObject> boolean containsActiveIndicator(Class<T> clazz) {
        boolean containsActive = false;

        if (persistenceStructureService.listFieldNames(clazz).contains(KRADPropertyConstants.ACTIVE)) {
            containsActive = true;
        }

        return containsActive;
    }
    
    /**
     * @see org.kuali.rice.krad.service.KeyValuesService#findAll(java.lang.Class)
     */
    @Override
	public <T extends BusinessObject> Collection<T> findAllInactive(Class<T> clazz) {
    	if (LOG.isDebugEnabled()) {
			LOG.debug("Active indicator not found for class " + clazz.getName());
		}
        return businessObjectDao.findAllInactive(clazz);
    }

}
