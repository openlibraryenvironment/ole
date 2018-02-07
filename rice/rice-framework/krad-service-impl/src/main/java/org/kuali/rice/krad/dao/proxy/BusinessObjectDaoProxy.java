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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.ModuleConfiguration;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.dao.BusinessObjectDao;
import org.kuali.rice.krad.dao.impl.BusinessObjectDaoJpa;
import org.kuali.rice.krad.dao.impl.BusinessObjectDaoOjb;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class BusinessObjectDaoProxy implements BusinessObjectDao {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BusinessObjectDaoProxy.class);

	private BusinessObjectDao businessObjectDaoJpa;
	private BusinessObjectDao businessObjectDaoOjb;
    private static KualiModuleService kualiModuleService;
    private static HashMap<String, BusinessObjectDao> boDaoValues = new HashMap<String, BusinessObjectDao>();

    private BusinessObjectDao getDao(Class clazz) {
        ModuleService moduleService = getKualiModuleService().getResponsibleModuleService(clazz);
        if (moduleService != null) {
            ModuleConfiguration moduleConfig = moduleService.getModuleConfiguration();
            String dataSourceName = "";
            EntityManager entityManager = null;
            if (moduleConfig != null) {
                dataSourceName = moduleConfig.getDataSourceName();
                entityManager = moduleConfig.getEntityManager();
            }

            if (StringUtils.isNotEmpty(dataSourceName)) {
                if (boDaoValues.get(dataSourceName) != null) {
                    return boDaoValues.get(dataSourceName);
                } else {
                	if (OrmUtils.isJpaAnnotated(clazz) && OrmUtils.isJpaEnabled()) {
                        //using JPA
                		if (entityManager != null) {
                            BusinessObjectDaoJpa boDaoJpa =
                            	new BusinessObjectDaoJpa(entityManager, KRADServiceLocator
                                        .getPersistenceStructureService());
                            // add to our cache of bo daos
                			boDaoValues.put(dataSourceName, boDaoJpa);
                			return boDaoJpa;
                		} else {
                			throw new ConfigurationException("EntityManager is null. EntityManager must be set in the Module Configuration bean in the appropriate spring beans xml. (see nested exception for details).");
                		}
                	} else {	
                	    //using OJB
                        BusinessObjectDaoOjb boDaoOjb = new BusinessObjectDaoOjb(
                                KRADServiceLocator.getPersistenceStructureService());
                        boDaoOjb.setJcdAlias(dataSourceName);
                        // add to our cache of bo daos
                        boDaoValues.put(dataSourceName, boDaoOjb);
                        return boDaoOjb;
                    }    
                }

            }
        }
        //return businessObjectDaoJpa;
        return (OrmUtils.isJpaAnnotated(clazz) && OrmUtils.isJpaEnabled()) ? businessObjectDaoJpa : businessObjectDaoOjb;
    }

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#countMatching(java.lang.Class, java.util.Map)
	 */
	public int countMatching(Class clazz, Map<String, ?> fieldValues) {
		return getDao(clazz).countMatching(clazz, fieldValues);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#countMatching(java.lang.Class, java.util.Map, java.util.Map)
	 */
	public int countMatching(Class clazz, Map<String, ?> positiveFieldValues, Map<String, ?> negativeFieldValues) {
		return getDao(clazz).countMatching(clazz, positiveFieldValues, negativeFieldValues);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#delete(org.kuali.rice.krad.bo.PersistableBusinessObject)
	 */
	public void delete(PersistableBusinessObject bo) {
		if (bo != null) {
			getDao(bo.getClass()).delete(bo);
		}
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#delete(java.util.List)
	 */
	public void delete(List<? extends PersistableBusinessObject> boList) {
		if (!boList.isEmpty()) {
			getDao(boList.get(0).getClass()).delete(boList);
		}
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#deleteMatching(java.lang.Class, java.util.Map)
	 */
	public void deleteMatching(Class clazz, Map<String, ?> fieldValues) {
		getDao(clazz).deleteMatching(clazz, fieldValues);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findAll(java.lang.Class)
	 */
	public <T extends BusinessObject> Collection<T> findAll(Class<T> clazz) {
		return getDao(clazz).findAll(clazz);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findAllActive(java.lang.Class)
	 */
	public <T extends BusinessObject> Collection<T> findAllActive(Class<T> clazz) {
		return getDao(clazz).findAllActive(clazz);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findAllInactive(java.lang.Class)
	 */
	public <T extends BusinessObject> Collection<T> findAllInactive(Class<T> clazz) {
		return getDao(clazz).findAllInactive(clazz);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findAllActiveOrderBy(java.lang.Class, java.lang.String, boolean)
	 */
	public <T extends BusinessObject> Collection<T> findAllActiveOrderBy(Class<T> clazz, String sortField, boolean sortAscending) {
		return getDao(clazz).findAllActiveOrderBy(clazz, sortField, sortAscending);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findAllOrderBy(java.lang.Class, java.lang.String, boolean)
	 */
	public <T extends BusinessObject> Collection<T> findAllOrderBy(Class<T> clazz, String sortField, boolean sortAscending) {
		return getDao(clazz).findAllOrderBy(clazz, sortField, sortAscending);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findBySinglePrimaryKey(java.lang.Class, java.lang.Object)
	 */
	public <T extends BusinessObject> T findBySinglePrimaryKey(Class<T> clazz, Object primaryKey) {
		return getDao(clazz).findBySinglePrimaryKey(clazz, primaryKey);
	}
	
	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findByPrimaryKey(java.lang.Class, java.util.Map)
	 */
	public <T extends BusinessObject> T findByPrimaryKey(Class<T> clazz, Map<String, ?> primaryKeys) {
		return getDao(clazz).findByPrimaryKey(clazz, primaryKeys);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findMatching(java.lang.Class, java.util.Map)
	 */
	public <T extends BusinessObject> Collection<T> findMatching(Class<T> clazz, Map<String, ?> fieldValues) {
		return getDao(clazz).findMatching(clazz, fieldValues);
	}

	/**
	 * Has the proxied DAO handle the criteria
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findMatching(org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria)
	 */
	//public <T extends BusinessObject> Collection<T> findMatching(Criteria criteria) {
	//	Class clazz = null;
	//	try {
	//		clazz = Class.forName(criteria.getEntityName());
	//	} catch (ClassNotFoundException cnfe) {
	//		throw new RuntimeException("Attempted to run JPA Criteria which uses a non-existent class to query against: "+criteria.getEntityName(), cnfe);
	//	}
	//	return getDao(clazz).findMatching(criteria);
	//}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findMatchingActive(java.lang.Class, java.util.Map)
	 */
	public <T extends BusinessObject> Collection<T> findMatchingActive(Class<T> clazz, Map<String, ?> fieldValues) {
		return getDao(clazz).findMatchingActive(clazz, fieldValues);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findMatchingOrderBy(java.lang.Class, java.util.Map, java.lang.String, boolean)
	 */
	public <T extends BusinessObject> Collection<T> findMatchingOrderBy(Class<T> clazz, Map<String, ?> fieldValues, String sortField, boolean sortAscending) {
		return getDao(clazz).findMatchingOrderBy(clazz, fieldValues, sortField, sortAscending);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#retrieve(org.kuali.rice.krad.bo.PersistableBusinessObject)
	 */
	public PersistableBusinessObject retrieve(PersistableBusinessObject object) {
		return getDao(object.getClass()).retrieve(object);
	}
	
	/**

	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#manageReadOnly(org.kuali.rice.krad.bo.PersistableBusinessObject)
	 */
	public PersistableBusinessObject manageReadOnly(PersistableBusinessObject bo) {
		return getDao(bo.getClass()).manageReadOnly(bo);
	}

	/**
	 * Defers to correct DAO for this class
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#findByPrimaryKeyUsingKeyObject(java.lang.Class, java.lang.Object)
	 */
	public <T extends BusinessObject> T findByPrimaryKeyUsingKeyObject(Class<T> clazz, Object pkObject) {
		return getDao(clazz).findByPrimaryKeyUsingKeyObject(clazz, pkObject);
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#save(org.kuali.rice.krad.bo.PersistableBusinessObject)
	 */
	public PersistableBusinessObject save(PersistableBusinessObject bo) {
		PersistableBusinessObject savedBo;
		savedBo = getDao(bo.getClass()).save(bo);
		return savedBo;
	}

	/**
	 * @see org.kuali.rice.krad.dao.BusinessObjectDao#save(java.util.List)
	 */
	public List<? extends PersistableBusinessObject> save(List businessObjects) {
		if (!businessObjects.isEmpty()) {
			return getDao(businessObjects.get(0).getClass()).save(businessObjects);
		}
		return new ArrayList<PersistableBusinessObject>();
	}

    private static KualiModuleService getKualiModuleService() {
        if (kualiModuleService == null) {
            kualiModuleService = KRADServiceLocatorWeb.getKualiModuleService();
        }
        return kualiModuleService;
    }

	public void setBusinessObjectDaoJpa(BusinessObjectDao businessObjectDaoJpa) {
		this.businessObjectDaoJpa = businessObjectDaoJpa;
	}

	public void setBusinessObjectDaoOjb(BusinessObjectDao businessObjectDaoOjb) {
		this.businessObjectDaoOjb = businessObjectDaoOjb;
	}
}
