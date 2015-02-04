/*                                                                                invoiceNumber
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
package org.kuali.ole.select.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.service.OleLineItemReceivingService;
import org.kuali.ole.select.document.service.impl.OleLineItemReceivingServiceImpl;
import org.kuali.ole.select.service.OleDocStoreLookupService;
import org.kuali.ole.select.service.OleDocStoreSearchService;
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
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.service.PersistenceStructureService;

import java.lang.reflect.Method;
import java.util.*;

/**
 * This is the implementation class for OleDocStoreLookupService to integrate Docstore search.
 */
@NonTransactional
public class OleDocStoreLookupServiceImpl implements OleDocStoreLookupService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleDocStoreLookupServiceImpl.class);

    private OleDocStoreSearchService lookupDao;
    private LookupDao lookupDaoOjb;
    private ConfigurationService kualiConfigurationService;
    private DataDictionaryService dataDictionaryService;
    private PersistenceStructureService persistenceStructureService;

    /**
     * Returns a collection of objects without any bounded value, based on the given search parameters.
     *
     * @return Collection returned from the search
     */
    @Override
    public Collection findCollectionBySearchUnbounded(Class example, Map formProps) {
        LOG.debug("Inside findCollectionBySearchUnbounded of OleDocStoreLookupServiceImpl");
        return findCollectionBySearchHelper(example, formProps, true);
    }

    /**
     * Returns a collection of objects based on the given search parameters.
     *
     * @return Collection returned from the search
     */
    @Override
    public Collection findCollectionBySearch(Class example, Map formProps) {
        LOG.debug("Inside findCollectionBySearch of OleDocStoreLookupServiceImpl");
        try {
            return getResult(example, formProps, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a collection of objects without any bounded value, based on the given search parameters.
     *
     * @return Collection returned from the search
     */
    @Override
    public Collection findCollectionBySearchHelper(Class example, Map formProps, boolean unbounded) {
        LOG.debug("Inside findCollectionBySearchHelper of OleDocStoreLookupServiceImpl");
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
        LOG.debug("Inside findObjectBySearch of OleDocStoreLookupServiceImpl");
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
    public OleDocStoreSearchService getLookupDao() {
        return lookupDao;
    }

    /**
     * @param lookupDao The lookupDao to set.
     */
    public void setLookupDao(OleDocStoreSearchService lookupDao) {
        this.lookupDao = lookupDao;
    }


    /**
     * @return Returns the kualiConfigurationService.
     */
    public ConfigurationService getConfigurationService() {
        return kualiConfigurationService;
    }

    /**
     * @param kualiConfigurationService The kualiConfigurationService to set.
     */
    public void setConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

    /**
     * @param dataDictionaryService The dataDictionaryService to set.
     */
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

    /**
     * This method integrates database search with docstore search for the businessObject class passed as input
     *
     * @param businessObjectClass
     * @param criterValues
     * @param unbounded
     * @return
     * @throws Exception
     */
    private List getResult(Class businessObjectClass, Map criterValues, boolean unbounded) throws Exception {
        LOG.debug("Inside getResult of OleDocStoreLookupServiceImpl");
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

        if (businessObjectClass.equals(OleLineItemReceivingItem.class)) {
            List<OleLineItemReceivingItem> resultList = result;
            for (OleLineItemReceivingItem item : resultList) {
                Integer receivingItemIdentifier = item.getReceivingItemIdentifier();
                OleLineItemReceivingService oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingServiceImpl.class);
                OleLineItemReceivingDoc oleLineItemReceivingDoc = oleLineItemReceivingService.getOleLineItemReceivingDoc(receivingItemIdentifier);
                item.setItemTitleId(oleLineItemReceivingDoc.getItemTitleId());
            }
            result = resultList;
        }
        if (businessObjectClass.equals(OleCorrectionReceivingItem.class)) {
            List<OleCorrectionReceivingItem> resultList = result;
            for (OleCorrectionReceivingItem item : resultList) {
                Integer receivingItemIdentifier = item.getReceivingItemIdentifier();
                OleLineItemReceivingService oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingServiceImpl.class);
                OleLineItemCorrectionReceivingDoc oleLineItemCorrectionReceivingDoc = oleLineItemReceivingService.getOleLineItemCorrectionReceivingDoc(receivingItemIdentifier);
                item.setItemTitleId(oleLineItemCorrectionReceivingDoc.getItemTitleId());
            }
            result = resultList;
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
            boolean isDocCrit = docCrit == null || docCrit.size() < 2;
            //retrieves the list of source/database attribute values
            List dbSourceAttrib = getSourceData(result, sourAtt, businessObjectClass);
            //retrieves the list of document store data for the corresponding source/database attribute values
            List docStoreResult = this.getLookupDao().getResult(cla, targetAtt, dbSourceAttrib, docCrit);//docStoreResult.remove(2)
            //merges both docstore and database data
            result = mergeResult(result, sourAtt, businessObjectClass, docStoreResult, "uniqueId", cla, docData.keySet().iterator().next(), isDBCrit, isDocCrit);
        }
        if (org.kuali.ole.select.lookup.DocStoreData.class.isAssignableFrom(businessObjectClass)) {
            result = (List) this.getLookupDaoOjb().findCollectionBySearchHelper(businessObjectClass, criterValues, true, true);
        }

        LOG.debug("Leaving getResult of OleDocStoreLookupServiceImpl");
        if (businessObjectClass.equals(OleRequisitionItem.class)) {
            result = setBibUUID(result);
        }
        return result;
    }

    /**
     * This method invokes getter method for the attribute in the specified class
     *
     * @param c
     * @param attr
     * @param objectAttributes
     * @return
     * @throws Exception
     */
    private Method getMethod(Class c, String attr, Class[] objectAttributes) throws Exception {
        LOG.debug("Inside getMethod of OleDocStoreLookupServiceImpl");
        Method met = c.getMethod("get" + StringUtils.capitalize(attr), objectAttributes);
        LOG.debug("Leaving getMethod of OleDocStoreLookupServiceImpl");
        return met;
    }

    /**
     * This method invokes setter method for the attribute in the specified class
     *
     * @param c
     * @param attr
     * @param objectAttributes
     * @return
     * @throws Exception
     */
    private Method getSetMethod(Class c, String attr, Class[] objectAttributes) throws Exception {
        LOG.debug("Inside getSetMethod of OleDocStoreLookupServiceImpl");
        attr = "docData";
        Method met = c.getMethod("set" + StringUtils.capitalize(attr), objectAttributes);
        LOG.debug("Leaving getSetMethod of OleDocStoreLookupServiceImpl");
        return met;
    }

    /**
     * This method merges database search result with docstore search result
     *
     * @param result
     * @param sourAtt
     * @param sourClass
     * @param dres
     * @param targetAtt
     * @param targeClass
     * @param attt
     * @param isDBCrit
     * @param isDocCrit
     * @return
     * @throws Exception
     */
    private List mergeResult(List result, String sourAtt, Class sourClass, List dres, String targetAtt, Class targeClass,
                             String attt, boolean isDBCrit, boolean isDocCrit)
            throws Exception {
        LOG.debug("Inside mergeResult of OleDocStoreLookupServiceImpl");
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
                    //if(isDocCrit && dvall != null && dvall.equals(sval)){
                    Object[] arr = {dval};
                    sem.invoke(val, arr);
                    resul.add(val);
                    //}
                }
            }
        }
        LOG.debug("Leaving mergeResult of OleDocStoreLookupServiceImpl");
        return resul;

    }

    /**
     * This method returns the list of document uuids retrieved from database search
     *
     * @param result
     * @param objectAttribute
     * @param clas
     * @return
     * @throws Exception
     */
    private List<Object> getSourceData(List result, String objectAttribute, Class clas) throws Exception {
        LOG.debug("Inside getSourceData of OleDocStoreLookupServiceImpl");
        List<Object> resul = new ArrayList<Object>(0);
        Method met = clas.getMethod("get" + StringUtils.capitalize(objectAttribute), (Class[]) null);
        for (Object data : result) {
            Object res = met.invoke(data, (Object[]) null);
            if (res != null) {
                resul.add(res);
            }
        }
        LOG.debug("Leaving getSourceData of OleDocStoreLookupServiceImpl");
        return resul;
    }

    /**
     * This method returns the class which stores Docstore data
     *
     * @param clas
     * @param objectAttribute
     * @return
     */
    public Class getDocClass(Class clas, String objectAttribute) {
        LOG.debug("Inside getDocClass of OleDocStoreLookupServiceImpl");
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

    /**
     * This method returns relationships in datadictionary
     *
     * @param c
     * @return
     */
    public Map<String, List<String>> getDDRelationship(Class c) {
        LOG.debug("Inside getDDRelationship of OleDocStoreLookupServiceImpl");
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
        LOG.debug("Leaving getDDRelationship of OleDocStoreLookupServiceImpl");
        return result;
    }

    public List<OleRequisitionItem> setBibUUID(List<OleRequisitionItem> items) {
        List<OleRequisitionItem> itemList = new ArrayList<OleRequisitionItem>();
        OleRequisitionItem item;
        String uuid;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            uuid = lookupDao.getBibUUID(item.getItemTitleId());
            items.get(i).setBibUUID(uuid);
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    public <T> Collection<T> findCollectionBySearchHelper(Class<T> example, Map<String, String> formProperties, boolean unbounded, Integer searchResultsLimit) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

