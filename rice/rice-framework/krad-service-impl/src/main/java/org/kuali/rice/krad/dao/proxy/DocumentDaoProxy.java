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
import org.kuali.rice.krad.bo.ModuleConfiguration;
import org.kuali.rice.krad.dao.BusinessObjectDao;
import org.kuali.rice.krad.dao.DocumentDao;
import org.kuali.rice.krad.dao.impl.DocumentDaoJpa;
import org.kuali.rice.krad.dao.impl.DocumentDaoOjb;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentAdHocService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Transactional
public class DocumentDaoProxy implements DocumentDao {

    private DocumentDao documentDaoJpa;
    private DocumentDao documentDaoOjb;

    private static KualiModuleService kualiModuleService;
    private static Map<String, DocumentDao> documentDaoValues = new ConcurrentHashMap<String, DocumentDao>();

    private DocumentDao getDao(Class<? extends Document> clazz) {
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
                if (documentDaoValues.get(dataSourceName) != null) {
                    return documentDaoValues.get(dataSourceName);
                }
                if (OrmUtils.isJpaAnnotated(clazz) && (OrmUtils.isJpaEnabled() || OrmUtils.isJpaEnabled("rice.krad"))) {
                	//using JPA
                	if (entityManager != null) {
                		// we set the entity manager directly in the constructor
                		DocumentDaoJpa documentDaoJpaInstance =
                			new DocumentDaoJpa(entityManager, this.documentDaoJpa.getBusinessObjectDao(),
                					this.documentDaoJpa.getDocumentAdHocService());

                		documentDaoValues.put(dataSourceName, documentDaoJpaInstance);
                		return documentDaoJpaInstance;
                	}
                	throw new ConfigurationException("EntityManager is null. EntityManager must be set in the Module Configuration bean in the appropriate spring beans xml. (see nested exception for details).");
                }
                //using OJB
                DocumentDaoOjb documentDaoOjbInstance =
                	new DocumentDaoOjb(
                			this.documentDaoOjb.getBusinessObjectDao(),
                			this.documentDaoOjb.getDocumentAdHocService());

                // set the data source alias
                documentDaoOjbInstance.setJcdAlias(dataSourceName);

                documentDaoValues.put(dataSourceName, documentDaoOjbInstance);
                return documentDaoOjbInstance;

                }

        }
        return (OrmUtils.isJpaAnnotated(clazz) && OrmUtils.isJpaEnabled()) ? documentDaoJpa : documentDaoOjb;
    }
    
    /**
	 * @see org.kuali.rice.krad.dao.DocumentDao#save(org.kuali.rice.krad.document.Document)
	 */
    @Override
	public <T extends Document> T save(T document) {
		return getDao(document.getClass()).save(document);
	}
    
	/**
	 * @see org.kuali.rice.krad.dao.DocumentDao#findByDocumentHeaderId(java.lang.Class, java.lang.String)
	 */
    @Override
	public <T extends Document> T findByDocumentHeaderId(Class<T> clazz, String id) {
		return getDao(clazz).findByDocumentHeaderId(clazz, id);
	}

	/**
	 * @see org.kuali.rice.krad.dao.DocumentDao#findByDocumentHeaderIds(java.lang.Class, java.util.List)
	 */
    @Override
	public <T extends Document> List<T> findByDocumentHeaderIds(Class<T> clazz, List<String> idList) {
		return getDao(clazz).findByDocumentHeaderIds(clazz, idList);
	}

	/**
	 * @see org.kuali.rice.krad.dao.DocumentDao#getBusinessObjectDao()
	 */
    @Override
	public BusinessObjectDao getBusinessObjectDao() {
		if (OrmUtils.isJpaEnabled()) {
			return documentDaoJpa.getBusinessObjectDao();
		}
		return documentDaoOjb.getBusinessObjectDao();
	}

	/**
	 * @see org.kuali.rice.krad.dao.DocumentDao#getDocumentAdHocService()
	 */
    @Override
	public DocumentAdHocService getDocumentAdHocService() {
		if (OrmUtils.isJpaEnabled()) {
			return documentDaoJpa.getDocumentAdHocService();
		}
		return documentDaoOjb.getDocumentAdHocService();
    }
	
	public void setDocumentDaoJpa(DocumentDao documentDaoJpa) {
		this.documentDaoJpa = documentDaoJpa;
	}

	public void setDocumentDaoOjb(DocumentDao documentDaoOjb) {
		this.documentDaoOjb = documentDaoOjb;
	}

    private synchronized static KualiModuleService getKualiModuleService() {
        if (kualiModuleService == null) {
            kualiModuleService = KRADServiceLocatorWeb.getKualiModuleService();
        }
        return kualiModuleService;
    }

}
