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
package org.kuali.rice.krad.datadictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Encapsulates a set of statically generated (typically during startup)
 * DataDictionary indexes
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataDictionaryIndex implements Runnable {
    private static final Log LOG = LogFactory.getLog(DataDictionaryIndex.class);

    private DefaultListableBeanFactory ddBeans;

    // keyed by BusinessObject class
    private Map<String, BusinessObjectEntry> businessObjectEntries;
    private Map<String, DataObjectEntry> objectEntries;

    // keyed by documentTypeName
    private Map<String, DocumentEntry> documentEntries;
    // keyed by other things
    private Map<Class, DocumentEntry> documentEntriesByBusinessObjectClass;
    private Map<Class, DocumentEntry> documentEntriesByMaintainableClass;
    private Map<String, DataDictionaryEntry> entriesByJstlKey;

    // keyed by a class object, and the value is a set of classes that may block the class represented by the key from inactivation
    private Map<Class, Set<InactivationBlockingMetadata>> inactivationBlockersForClass;

    private Map<String, List<String>> dictionaryBeansByNamespace;

    public DataDictionaryIndex(DefaultListableBeanFactory ddBeans) {
        this.ddBeans = ddBeans;

        // primary indices
        businessObjectEntries = new HashMap<String, BusinessObjectEntry>();
        objectEntries = new HashMap<String, DataObjectEntry>();
        documentEntries = new HashMap<String, DocumentEntry>();

        // alternate indices
        documentEntriesByBusinessObjectClass = new HashMap<Class, DocumentEntry>();
        documentEntriesByMaintainableClass = new HashMap<Class, DocumentEntry>();
        entriesByJstlKey = new HashMap<String, DataDictionaryEntry>();

        dictionaryBeansByNamespace = new HashMap<String, List<String>>();
    }

    private void buildDDIndicies() {
        // primary indices
        businessObjectEntries = new HashMap<String, BusinessObjectEntry>();
        objectEntries = new HashMap<String, DataObjectEntry>();
        documentEntries = new HashMap<String, DocumentEntry>();

        // alternate indices
        documentEntriesByBusinessObjectClass = new HashMap<Class, DocumentEntry>();
        documentEntriesByMaintainableClass = new HashMap<Class, DocumentEntry>();
        entriesByJstlKey = new HashMap<String, DataDictionaryEntry>();

        // loop over all beans in the context
        Map<String, DataObjectEntry> boBeans = ddBeans.getBeansOfType(DataObjectEntry.class);
        for (DataObjectEntry entry : boBeans.values()) {

            DataObjectEntry indexedEntry = objectEntries.get(entry.getJstlKey());
            if (indexedEntry == null) {
                indexedEntry = businessObjectEntries.get(entry.getJstlKey());
            }

            if ((indexedEntry != null) && !(indexedEntry.getDataObjectClass().equals(entry.getDataObjectClass()))) {
                throw new DataDictionaryException(new StringBuffer(
                        "Two object classes may not share the same jstl key: this=").append(entry.getDataObjectClass())
                        .append(" / existing=").append(indexedEntry.getDataObjectClass()).toString());
            }

            // put all BO and DO entries in the objectEntries map
            objectEntries.put(entry.getDataObjectClass().getName(), entry);
            objectEntries.put(entry.getDataObjectClass().getSimpleName(), entry);

            // keep a separate map of BO entries for now
            if (entry instanceof BusinessObjectEntry) {
                BusinessObjectEntry boEntry = (BusinessObjectEntry) entry;

                businessObjectEntries.put(boEntry.getBusinessObjectClass().getName(), boEntry);
                businessObjectEntries.put(boEntry.getBusinessObjectClass().getSimpleName(), boEntry);

                // If a "base" class is defined for the entry, index the entry by that class as well.
                if (boEntry.getBaseBusinessObjectClass() != null) {
                    businessObjectEntries.put(boEntry.getBaseBusinessObjectClass().getName(), boEntry);
                    businessObjectEntries.put(boEntry.getBaseBusinessObjectClass().getSimpleName(), boEntry);
                }
            }

            entriesByJstlKey.put(entry.getJstlKey(), entry);
        }

        //Build Document Entry Index
        Map<String, DocumentEntry> docBeans = ddBeans.getBeansOfType(DocumentEntry.class);
        for (DocumentEntry entry : docBeans.values()) {
            String entryName = entry.getDocumentTypeName();

            if ((entry instanceof TransactionalDocumentEntry)
                    && (documentEntries.get(entry.getFullClassName()) != null)
                    && !StringUtils.equals(documentEntries.get(entry.getFullClassName()).getDocumentTypeName(),
                    entry.getDocumentTypeName())) {
                throw new DataDictionaryException(new StringBuffer(
                        "Two transactional document types may not share the same document class: this=").append(
                        entry.getDocumentTypeName()).append(" / existing=").append(((DocumentEntry) documentEntries.get(
                        entry.getDocumentClass().getName())).getDocumentTypeName()).toString());
            }

            if ((documentEntries.get(entry.getJstlKey()) != null) && !((DocumentEntry) documentEntries.get(
                    entry.getJstlKey())).getDocumentTypeName().equals(entry.getDocumentTypeName())) {
                throw new DataDictionaryException(new StringBuffer(
                        "Two document types may not share the same jstl key: this=").append(entry.getDocumentTypeName())
                        .append(" / existing=").append(((DocumentEntry) documentEntries.get(entry.getJstlKey()))
                                .getDocumentTypeName()).toString());
            }

            if (entryName != null) {
                documentEntries.put(entryName, entry);
            }

            //documentEntries.put(entry.getFullClassName(), entry);
            documentEntries.put(entry.getDocumentClass().getName(), entry);
            if (entry.getBaseDocumentClass() != null) {
                documentEntries.put(entry.getBaseDocumentClass().getName(), entry);
            }
            entriesByJstlKey.put(entry.getJstlKey(), entry);

            if (entry instanceof TransactionalDocumentEntry) {
                TransactionalDocumentEntry tde = (TransactionalDocumentEntry) entry;

                documentEntries.put(tde.getDocumentClass().getSimpleName(), entry);
                if (tde.getBaseDocumentClass() != null) {
                    documentEntries.put(tde.getBaseDocumentClass().getSimpleName(), entry);
                }
            }

            if (entry instanceof MaintenanceDocumentEntry) {
                MaintenanceDocumentEntry mde = (MaintenanceDocumentEntry) entry;

                documentEntriesByBusinessObjectClass.put(mde.getDataObjectClass(), entry);
                documentEntriesByMaintainableClass.put(mde.getMaintainableClass(), entry);
                documentEntries.put(mde.getDataObjectClass().getSimpleName() + "MaintenanceDocument", entry);
            }
        }
    }

    private void buildDDInactivationBlockingIndices() {
        inactivationBlockersForClass = new HashMap<Class, Set<InactivationBlockingMetadata>>();

        Map<String, DataObjectEntry> doBeans = ddBeans.getBeansOfType(DataObjectEntry.class);
        for (DataObjectEntry entry : doBeans.values()) {
            List<InactivationBlockingDefinition> inactivationBlockingDefinitions =
                    entry.getInactivationBlockingDefinitions();
            if (inactivationBlockingDefinitions != null && !inactivationBlockingDefinitions.isEmpty()) {
                for (InactivationBlockingDefinition inactivationBlockingDefinition : inactivationBlockingDefinitions) {
                    registerInactivationBlockingDefinition(inactivationBlockingDefinition);
                }
            }
        }
    }

    private void registerInactivationBlockingDefinition(InactivationBlockingDefinition inactivationBlockingDefinition) {
        Set<InactivationBlockingMetadata> inactivationBlockingDefinitions = inactivationBlockersForClass.get(
                inactivationBlockingDefinition.getBlockedBusinessObjectClass());
        if (inactivationBlockingDefinitions == null) {
            inactivationBlockingDefinitions = new HashSet<InactivationBlockingMetadata>();
            inactivationBlockersForClass.put(inactivationBlockingDefinition.getBlockedBusinessObjectClass(),
                    inactivationBlockingDefinitions);
        }
        boolean duplicateAdd = !inactivationBlockingDefinitions.add(inactivationBlockingDefinition);
        if (duplicateAdd) {
            throw new DataDictionaryException(
                    "Detected duplicate InactivationBlockingDefinition for class " + inactivationBlockingDefinition
                            .getBlockingReferenceBusinessObjectClass().getClass().getName());
        }
    }

    public void run() {
        LOG.info("Starting DD Index Building");
        buildDDIndicies();
        LOG.info("Completed DD Index Building");

        //        LOG.info( "Starting DD Validation" );
        //        validateDD();
        //        LOG.info( "Ending DD Validation" );

        LOG.info("Started DD Inactivation Blocking Index Building");
        buildDDInactivationBlockingIndices();
        LOG.info("Completed DD Inactivation Blocking Index Building");
    }

    public Map<String, BusinessObjectEntry> getBusinessObjectEntries() {
        return this.businessObjectEntries;
    }

    public Map<String, DataObjectEntry> getDataObjectEntries() {
        return this.objectEntries;
    }

    public Map<String, DocumentEntry> getDocumentEntries() {
        return this.documentEntries;
    }

    public Map<Class, DocumentEntry> getDocumentEntriesByBusinessObjectClass() {
        return this.documentEntriesByBusinessObjectClass;
    }

    public Map<Class, DocumentEntry> getDocumentEntriesByMaintainableClass() {
        return this.documentEntriesByMaintainableClass;
    }

    public Map<String, DataDictionaryEntry> getEntriesByJstlKey() {
        return this.entriesByJstlKey;
    }

    public Map<Class, Set<InactivationBlockingMetadata>> getInactivationBlockersForClass() {
        return this.inactivationBlockersForClass;
    }

    /**
     * Mapping of namespace codes to bean definition names that are associated with that namespace
     *
     * @return Map<String, List<String>> where map key is namespace code, and map value is list of bean names
     */
    public Map<String, List<String>> getDictionaryBeansByNamespace() {
        return dictionaryBeansByNamespace;
    }

    /**
     * Associates a list of bean names with the given namespace code
     *
     * @param namespaceCode - namespace code to associate beans with
     * @param beanNames - list of bean names that belong to the namespace
     */
    public void addBeanNamesToNamespace(String namespaceCode, List<String> beanNames) {
        List<String> namespaceBeans = new ArrayList<String>();
        if (dictionaryBeansByNamespace.containsKey(namespaceCode)) {
            namespaceBeans = dictionaryBeansByNamespace.get(namespaceCode);
        } else {
            dictionaryBeansByNamespace.put(namespaceCode, namespaceBeans);
        }
        namespaceBeans.addAll(beanNames);
    }
}
