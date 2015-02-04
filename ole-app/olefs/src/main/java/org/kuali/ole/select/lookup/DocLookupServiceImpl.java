/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.NonTransactional;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.search.SearchOperator;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.dao.LookupDao;
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.DataDictionaryEntry;
import org.kuali.rice.krad.datadictionary.PrimitiveAttributeDefinition;
import org.kuali.rice.krad.datadictionary.RelationshipDefinition;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.service.PersistenceStructureService;

import java.lang.reflect.Method;
import java.util.*;

@NonTransactional
public class DocLookupServiceImpl implements LookupService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocLookupServiceImpl.class);

    //TODO:This is just a draft version .The whole class needs to be rewritten once we have Document store
    private IDocLookupSearch lookupDao;
    private LookupDao lookupDaoOjb;
    private ConfigurationService kualiConfigurationService;
    private DataDictionaryService dataDictionaryService;
    private PersistenceStructureService persistenceStructureService;


    @Override
    public Collection findCollectionBySearchUnbounded(Class example, Map formProps) {
        return findCollectionBySearchHelper(example, formProps, true);
    }

    /**
     * Returns a collection of objects based on the given search parameters.
     *
     * @return Collection returned from the search
     */

    @Override
    public Collection findCollectionBySearch(Class example, Map formProps) {
        try {
            return getResult(example, formProps, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Collection findCollectionBySearchHelper(Class example, Map formProps, boolean unbounded) {
        try {
            return getResult(example, formProps, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a Object based on the search criteria, which should uniquely identify a record.
     *
     * @return Object returned from the search
     */

    @Override
    public Object findObjectBySearch(Class example, Map formProps) {
        if (example == null || formProps == null) {
            throw new IllegalArgumentException("Object and Map must not be null");
        }

        PersistableBusinessObject obj = null;
        try {
            obj = (PersistableBusinessObject) example.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot get new instance of " + example.getName(), e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot instantiate " + example.getName(), e);
        }

        return lookupDaoOjb.findObjectByMap(obj, formProps);
    }


    @Override
    public boolean allPrimaryKeyValuesPresentAndNotWildcard(Class boClass, Map formProps) {
        List pkFields = KNSServiceLocator.getBusinessObjectMetaDataService().listPrimaryKeyFieldNames(boClass);
        Iterator pkIter = pkFields.iterator();
        boolean returnVal = true;
        while (returnVal && pkIter.hasNext()) {
            String pkName = (String) pkIter.next();
            String pkValue = (String) formProps.get(pkName);

            if (StringUtils.isBlank(pkValue)) {
                returnVal = false;
            } else if (StringUtils.indexOfAny(pkValue, SearchOperator.QUERY_CHARACTERS.toArray().toString()) != -1) {
                returnVal = false;
            }
        }
        return returnVal;
    }

    /**
     * @return Returns the lookupDao.
     */

    public IDocLookupSearch getLookupDao() {
        return lookupDao;
    }

    /**
     * @param lookupDao The lookupDao to set.
     */

    public void setLookupDao(IDocLookupSearch lookupDao) {
        this.lookupDao = lookupDao;
    }


    public ConfigurationService getConfigurationService() {
        return kualiConfigurationService;
    }


    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

    /**
     * @return Returns the lookupDao.
     */

    public LookupDao getLookupDaoOjb() {
        return lookupDaoOjb;
    }


    /**
     * @param lookupDao The lookupDao to set.
     */

    public void setLookupDaoOjb(LookupDao lookupDaoOjb) {
        this.lookupDaoOjb = lookupDaoOjb;
    }


    public void setPersistenceStructureService(PersistenceStructureService persistenceStructureService) {
        this.persistenceStructureService = persistenceStructureService;
    }


    private List getResult(Class businessObjectClass, Map criterValues, boolean unbounded) throws Exception {
        List result = new ArrayList();
        Map dbCrit = new HashMap();
        Map docCrit = new HashMap();
        Map<String, List<String>> docData = getDDRelationship(businessObjectClass);
        if (docData != null && docData.size() > 0) {
            for (String key : (java.util.Set<String>) criterValues.keySet()) {
                boolean found = false;
                for (String key1 : docData.keySet()) {
                    {
                        if (key.contains(key1) && key.contains(".")) {
                            found = true;
                            String val = key.split("\\.")[key.split("\\.").length - 1];
                            docCrit.put(val, criterValues.get(key));//dbCrit = null;
                            break;
                        }
                    }
                    if (!found) {
                        dbCrit.put(key, criterValues.get(key));//dbCrit = null;
                    }
                }
            }
        }
        if (ExternalizableBusinessObject.class.isAssignableFrom(businessObjectClass)) {

            ModuleService eboModuleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(businessObjectClass);
            BusinessObjectEntry ddEntry = eboModuleService.getExternalizableBusinessObjectDictionaryEntry(businessObjectClass);
            Map<String, String> filteredFieldValues = new HashMap<String, String>();
            for (String fieldName : (java.util.Set<String>) dbCrit.keySet()) {
                if (ddEntry.getAttributeNames().contains(fieldName)) {
                    filteredFieldValues.put(fieldName, (String) dbCrit.get(fieldName));
                }
            }

            result = eboModuleService.getExternalizableBusinessObjectsListForLookup(businessObjectClass,
                    (Map) filteredFieldValues, unbounded);

        } else if (!org.kuali.ole.select.lookup.DocStoreData.class.isAssignableFrom(businessObjectClass)) {
            result = (List) this.getLookupDaoOjb().findCollectionBySearchHelper(businessObjectClass, dbCrit, unbounded,
                    allPrimaryKeyValuesPresentAndNotWildcard(businessObjectClass, dbCrit));
        }
        Class cla = null;
        for (String key : docData.keySet()) {
            List<String> data = docData.get(key);

            try {
                cla = Class.forName(data.get(0));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String attrs = data.get(1);
            String[] ff = attrs.split(":")[0].split(",");
            String sourAtt = null, targetAtt = null;
            if (ff.length == 2) {
                sourAtt = ff[0];
                targetAtt = ff[1];
            }
            boolean isDBCrit = dbCrit == null || dbCrit.size() < 3;
            boolean isDocCrit = docCrit == null || docCrit.size() < 1;
            //retrieves the list of source/database attribute values
            List dbSourceAttrib = getSourceData(result, sourAtt, businessObjectClass);
            //retrieves the list of document store data for the corresponding source/database attribute values
            List docStoreResult = this.getLookupDao().getResult(cla, targetAtt, dbSourceAttrib, docCrit);//docStoreResult.remove(2)
            //merges both docstore and database data
            result = mergeResult(result, sourAtt, businessObjectClass, docStoreResult, targetAtt, cla, docData.keySet().iterator().next(), isDBCrit, isDocCrit);
        }
        if (org.kuali.ole.select.lookup.DocStoreData.class.isAssignableFrom(businessObjectClass)) {
            result = (List) this.getLookupDaoOjb().findCollectionBySearchHelper(businessObjectClass, criterValues, true, true);
        }
        return result;
    }

    private Method getMethod(Class c, String attr, Class[] objectAttributes) throws Exception {
        Method met = c.getMethod("get" + StringUtils.capitalize(attr), objectAttributes);
        return met;
    }

    private Method getSetMethod(Class c, String attr, Class[] objectAttributes) throws Exception {
        attr = "docData";
        Method met = c.getMethod("set" + StringUtils.capitalize(attr), objectAttributes);
        return met;
    }

    private List mergeResult(List result, String sourAtt, Class sourClass, List dres, String targetAtt, Class targeClass,
                             String attt, boolean isDBCrit, boolean isDocCrit)
            throws Exception {
        List resul = new ArrayList();
        List resut = new ArrayList();
        Class[] ptyeps = {targeClass};
        Method srcm = this.getMethod(sourClass, sourAtt, null);
        Method tcm = this.getMethod(targeClass, targetAtt, null);
        Method sem = this.getSetMethod(sourClass, attt, ptyeps);
        for (Object val : result) {
            for (Object dval : dres) {
                Object sval = srcm.invoke(val, (Object[]) null);
                Object dvall = tcm.invoke(dval, (Object[]) null);
                if ((!isDocCrit && sval != null && sval.equals(dvall)) || (isDocCrit && dvall != null && dvall.equals(sval))) {
                    Object[] arr = {dval};
                    sem.invoke(val, arr);
                    resul.add(val);
                }
            }
        }
        return resul;

    }

    private List<Object> getSourceData(List result, String objectAttribute, Class clas) throws Exception {
        List<Object> resul = new ArrayList<Object>(0);
        Method met = clas.getMethod("get" + StringUtils.capitalize(objectAttribute), (Class[]) null);
        for (Object data : result) {
            Object res = met.invoke(data, (Object[]) null);
            if (res != null) {
                resul.add(res);
            }
        }
        return resul;
    }

    public Class getDocClass(Class clas, String objectAttribute) {
        boolean result = false;
        Method met = null;
        try {
            met = clas.getMethod("get" + StringUtils.capitalize(objectAttribute), (Class[]) null);
        } catch (Exception e) {
            return null;
        }
        return org.kuali.ole.select.lookup.DocStoreData.class.isAssignableFrom(met.getReturnType()) ?
                met.getReturnType() : null;
    }


    public Map<String, List<String>> getDDRelationship(Class c) {
        Map<String, List<String>> result = new HashMap<String, List<String>>(0);
        DataDictionaryEntry entryBase = SpringContext.getBean(DataDictionaryService.class)
                .getDataDictionary().getDictionaryObjectEntry(c.getName());
        if (entryBase == null) {
            return null;
        }

        List<RelationshipDefinition> ddRelationships = entryBase
                .getRelationships();
        RelationshipDefinition relationship = null;
        int minKeys = Integer.MAX_VALUE;
        for (RelationshipDefinition def : ddRelationships) {
            // favor key sizes of 1 first
            if (def.getPrimitiveAttributes().size() == 1) {
                for (PrimitiveAttributeDefinition primitive : def
                        .getPrimitiveAttributes()) {
                    if (def.getObjectAttributeName() != null) {
                        List<String> data = new ArrayList<String>(0);
                        Class cc = getDocClass(c, def.getObjectAttributeName());//cc= null;data.remove("java.lang.String");
                        if (cc != null) {
                            data.add(cc.getName());
                            StringBuffer sb = new StringBuffer();
                            List<PrimitiveAttributeDefinition> res = def.getPrimitiveAttributes();
                            for (PrimitiveAttributeDefinition pdef : res) {
                                sb.append(pdef.getSourceName() + "," + pdef.getTargetName() + ":");
                            }
                            sb.deleteCharAt(sb.length() - 1);
                            data.add(sb.toString());
                            result.put(def.getObjectAttributeName(), data);
                        }
                    }
                }
            }
        }
        return result;//result.remove("oleRequestor");
    }

    @Override
    public <T> Collection<T> findCollectionBySearchHelper(Class<T> example, Map<String, String> formProperties, boolean unbounded, Integer searchResultsLimit) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

