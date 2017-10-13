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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.search.SearchOperator;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.dao.LookupDao;
import org.kuali.rice.krad.service.LookupService;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Service implementation for the Lookup structure. It Provides a generic search
 * mechanism against Business Objects. This is the default implementation, that
 * is delivered with Kuali.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LookupServiceImpl implements LookupService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LookupServiceImpl.class);

    private LookupDao lookupDao;
    private ConfigurationService kualiConfigurationService;

    public <T extends Object> Collection<T> findCollectionBySearchUnbounded(Class<T> example,
            Map<String, String> formProps) {
        return findCollectionBySearchHelper(example, formProps, true);
    }

    /**
     * Returns a collection of objects based on the given search parameters.
     * 
     * @return Collection returned from the search
     */
    public <T extends Object> Collection<T> findCollectionBySearch(Class<T> example, Map<String, String> formProps) {
        return findCollectionBySearchHelper(example, formProps, false);
    }

    /**
     * Since 2.3
     * This version of findCollectionBySearchHelper is needed for version compatibility.   It allows executeSearch
     * to behave the same way as it did prior to 2.3. In the LookupDao, the value for searchResultsLimit will be
     * retrieved from the KNS version of LookupUtils in the LookupDao.
     */
    public <T extends Object> Collection<T> findCollectionBySearchHelper(Class<T> example,
            Map<String, String> formProps, boolean unbounded) {
        return lookupDao.findCollectionBySearchHelper(example, formProps, unbounded,
                allPrimaryKeyValuesPresentAndNotWildcard(example, formProps));
    }

    public <T extends Object> Collection<T> findCollectionBySearchHelper(Class<T> example,
            Map<String, String> formProps, boolean unbounded, Integer searchResultsLimit) {
        return lookupDao.findCollectionBySearchHelper(example, formProps, unbounded,
                allPrimaryKeyValuesPresentAndNotWildcard(example, formProps), searchResultsLimit);
    }

    /**
     * Retrieves a Object based on the search criteria, which should uniquely
     * identify a record.
     * 
     * @return Object returned from the search
     */
    public <T extends Object> T findObjectBySearch(Class<T> example, Map<String, String> formProps) {
        if (example == null || formProps == null) {
            throw new IllegalArgumentException("Object and Map must not be null");
        }

        T obj = null;
        try {
            obj = example.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot get new instance of " + example.getName(), e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot instantiate " + example.getName(), e);
        }

        return lookupDao.findObjectByMap(obj, formProps);
    }

    public boolean allPrimaryKeyValuesPresentAndNotWildcard(Class<?> boClass, Map<String, String> formProps) {
        List<String> pkFields = KNSServiceLocator.getBusinessObjectMetaDataService().listPrimaryKeyFieldNames(
                boClass);
        Iterator<String> pkIter = pkFields.iterator();
        boolean returnVal = true;
        while (returnVal && pkIter.hasNext()) {
            String pkName = pkIter.next();
            String pkValue = formProps.get(pkName);

            if (StringUtils.isBlank(pkValue)) {
                returnVal = false;
            } else {
                for (SearchOperator op : SearchOperator.QUERY_CHARACTERS) {
                    if (pkValue.contains(op.op())) {
                        returnVal = false;
                        break;
                    }
                }
            }
        }
        return returnVal;
    }

    /**
     * @return Returns the lookupDao.
     */
    public LookupDao getLookupDao() {
        return lookupDao;
    }

    /**
     * @param lookupDao The lookupDao to set.
     */
    public void setLookupDao(LookupDao lookupDao) {
        this.lookupDao = lookupDao;
    }

    public ConfigurationService getKualiConfigurationService() {
        return kualiConfigurationService;
    }

    public void setKualiConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }
}
