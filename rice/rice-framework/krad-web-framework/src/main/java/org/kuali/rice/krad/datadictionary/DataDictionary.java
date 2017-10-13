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

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.krad.bo.PersistableBusinessObjectExtension;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.exception.CompletionException;
import org.kuali.rice.krad.datadictionary.parse.StringListConverter;
import org.kuali.rice.krad.datadictionary.parse.StringMapConverter;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryIndex;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.PersistenceStructureService;
import org.kuali.rice.krad.uif.UifConstants.ViewType;
import org.kuali.rice.krad.uif.util.ComponentBeanPostProcessor;
import org.kuali.rice.krad.uif.util.UifBeanFactoryPostProcessor;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

/**
 * Encapsulates a bean factory and indexes to the beans within the factory for providing
 * framework metadata
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataDictionary {
    private static final Log LOG = LogFactory.getLog(DataDictionary.class);

    protected static boolean validateEBOs = true;

    protected DefaultListableBeanFactory ddBeans = new DefaultListableBeanFactory();
    protected XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ddBeans);

    protected DataDictionaryIndex ddIndex = new DataDictionaryIndex(ddBeans);
    protected UifDictionaryIndex uifIndex = new UifDictionaryIndex(ddBeans);

    protected DataDictionaryMapper ddMapper = new DataDictionaryIndexMapper();

    protected Map<String, List<String>> moduleDictionaryFiles = new HashMap<String, List<String>>();
    protected List<String> moduleLoadOrder = new ArrayList<String>();

    protected ArrayList<String> beanValidationFiles = new ArrayList<String>();

    /**
     * Populates and processes the dictionary bean factory based on the configured files and
     * performs indexing
     *
     * @param allowConcurrentValidation - indicates whether the indexing should occur on a different thread
     * or the same thread
     */
    public void parseDataDictionaryConfigurationFiles(boolean allowConcurrentValidation) {
        setupProcessor(ddBeans);

        loadDictionaryBeans(ddBeans, moduleDictionaryFiles, ddIndex, beanValidationFiles);

        performDictionaryPostProcessing(allowConcurrentValidation);
    }

    /**
     * Sets up the bean post processor and conversion service
     *
     * @param beans - The bean factory for the the dictionary beans
     */
    public static void setupProcessor(DefaultListableBeanFactory beans) {
        try {
            // UIF post processor that sets component ids
            BeanPostProcessor idPostProcessor = ComponentBeanPostProcessor.class.newInstance();
            beans.addBeanPostProcessor(idPostProcessor);
            beans.setBeanExpressionResolver(new StandardBeanExpressionResolver());

            // special converters for shorthand map and list property syntax
            GenericConversionService conversionService = new GenericConversionService();
            conversionService.addConverter(new StringMapConverter());
            conversionService.addConverter(new StringListConverter());

            beans.setConversionService(conversionService);
        } catch (Exception e1) {
            throw new DataDictionaryException("Cannot create component decorator post processor: " + e1.getMessage(),
                    e1);
        }
    }

    /**
     * Populates and processes the dictionary bean factory based on the configured files
     *
     * @param beans - The bean factory for the dictionary bean
     * @param moduleDictionaryFiles - List of bean xml files
     * @param index - Index of the data dictionary beans
     * @param validationFiles - The List of bean xml files loaded into the bean file
     */
    public void loadDictionaryBeans(DefaultListableBeanFactory beans,
            Map<String, List<String>> moduleDictionaryFiles, DataDictionaryIndex index,
            ArrayList<String> validationFiles) {
        // expand configuration locations into files
        LOG.info("Starting DD XML File Load");

        List<String> allBeanNames = new ArrayList<String>();
        for (String namespaceCode : moduleLoadOrder) {
            List<String> moduleDictionaryLocations = moduleDictionaryFiles.get(namespaceCode);

            if (moduleDictionaryLocations == null) {
               continue;
            }

            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(beans);

            String configFileLocationsArray[] = new String[moduleDictionaryLocations.size()];
            configFileLocationsArray = moduleDictionaryLocations.toArray(configFileLocationsArray);
            for (int i = 0; i < configFileLocationsArray.length; i++) {
                validationFiles.add(configFileLocationsArray[i]);
            }

            try {
                xmlReader.loadBeanDefinitions(configFileLocationsArray);

                // get updated bean names from factory and compare to our previous list to get those that
                // were added by the last namespace
                List<String> addedBeanNames = Arrays.asList(beans.getBeanDefinitionNames());
                addedBeanNames = ListUtils.removeAll(addedBeanNames, allBeanNames);
                index.addBeanNamesToNamespace(namespaceCode, addedBeanNames);

                allBeanNames.addAll(addedBeanNames);
            } catch (Exception e) {
                throw new DataDictionaryException("Error loading bean definitions: " + e.getLocalizedMessage());
            }
        }

        LOG.info("Completed DD XML File Load");
    }

    /**
     * Invokes post processors and builds indexes for the beans contained in the dictionary
     *
     * @param allowConcurrentValidation - indicates whether the indexing should occur on a different thread
     * or the same thread
     */
    public void performDictionaryPostProcessing(boolean allowConcurrentValidation) {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setProperties(ConfigContext.getCurrentContextConfig().getProperties());
        propertyPlaceholderConfigurer.postProcessBeanFactory(ddBeans);

        DictionaryBeanFactoryPostProcessor dictionaryBeanPostProcessor = new DictionaryBeanFactoryPostProcessor(this,
                ddBeans);
        dictionaryBeanPostProcessor.postProcessBeanFactory();

        // post processes UIF beans for pulling out expressions within property values
        UifBeanFactoryPostProcessor factoryPostProcessor = new UifBeanFactoryPostProcessor();
        factoryPostProcessor.postProcessBeanFactory(ddBeans);

        if (allowConcurrentValidation) {
            Thread t = new Thread(ddIndex);
            t.start();

            Thread t2 = new Thread(uifIndex);
            t2.start();
        } else {
            ddIndex.run();
            uifIndex.run();
        }
    }

    public void validateDD(boolean validateEbos) {
        DataDictionary.validateEBOs = validateEbos;

        /*  ValidationController validator = new ValidationController();
    String files[] = new String[beanValidationFiles.size()];
    files = beanValidationFiles.toArray(files);
    validator.validate(files, xmlReader.getResourceLoader(), ddBeans, LOG, false);*/

        Map<String, DataObjectEntry> doBeans = ddBeans.getBeansOfType(DataObjectEntry.class);
        for (DataObjectEntry entry : doBeans.values()) {
            entry.completeValidation();
        }

        Map<String, DocumentEntry> docBeans = ddBeans.getBeansOfType(DocumentEntry.class);
        for (DocumentEntry entry : docBeans.values()) {
            entry.completeValidation();
        }
    }

    public void validateDD() {
        validateDD(true);
    }

    /**
     * Adds a location of files or a individual resource to the data dictionary
     *
     * <p>
     * The location can either be an XML file on the classpath or a file or folder location within the
     * file system. If a folder location is given, the folder and all sub-folders will be traversed and any
     * XML files will be added to the dictionary
     * </p>
     *
     * @param namespaceCode - namespace the beans loaded from the location should be associated with
     * @param location - classpath resource or file system location
     * @throws IOException
     */
    public void addConfigFileLocation(String namespaceCode, String location) throws IOException {
        // add module to load order so we load in the order modules were configured
        if (!moduleLoadOrder.contains(namespaceCode)) {
            moduleLoadOrder.add(namespaceCode);
        }

        indexSource(namespaceCode, location);
    }

    /**
     * Processes a given source for XML files to populate the dictionary with
     *
     * @param namespaceCode - namespace the beans loaded from the location should be associated with
     * @param sourceName - a file system or classpath resource locator
     * @throws IOException
     */
    protected void indexSource(String namespaceCode, String sourceName) throws IOException {
        if (sourceName == null) {
            throw new DataDictionaryException("Source Name given is null");
        }

        if (!sourceName.endsWith(".xml")) {
            Resource resource = getFileResource(sourceName);
            if (resource.exists()) {
                try {
                    indexSource(namespaceCode, resource.getFile());
                } catch (IOException e) {
                    // ignore resources that exist and cause an error here
                    // they may be directories resident in jar files
                    LOG.debug("Skipped existing resource without absolute file path");
                }
            } else {
                LOG.warn("Could not find " + sourceName);
                throw new DataDictionaryException("DD Resource " + sourceName + " not found");
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("adding sourceName " + sourceName + " ");
            }

            Resource resource = getFileResource(sourceName);
            if (!resource.exists()) {
                throw new DataDictionaryException("DD Resource " + sourceName + " not found");
            }

            addModuleDictionaryFile(namespaceCode, sourceName);
        }
    }

    protected Resource getFileResource(String sourceName) {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader(ClassLoaderUtils.getDefaultClassLoader());

        return resourceLoader.getResource(sourceName);
    }

    protected void indexSource(String namespaceCode, File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                indexSource(namespaceCode, file);
            } else if (file.getName().endsWith(".xml")) {
                addModuleDictionaryFile(namespaceCode, "file:" + file.getAbsolutePath());
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Skipping non xml file " + file.getAbsolutePath() + " in DD load");
                }
            }
        }
    }

    /**
     * Adds a file location to the list of dictionary files for the given namespace code
     *
     * @param namespaceCode - namespace to add location for
     * @param location - file or resource location to add
     */
    protected void addModuleDictionaryFile(String namespaceCode, String location) {
        List<String> moduleFileLocations = new ArrayList<String>();
        if (moduleDictionaryFiles.containsKey(namespaceCode)) {
            moduleFileLocations = moduleDictionaryFiles.get(namespaceCode);
        }
        moduleFileLocations.add(location);

        moduleDictionaryFiles.put(namespaceCode, moduleFileLocations);
    }

    /**
     * Mapping of namespace codes to dictionary files that are associated with
     * that namespace
     *
     * @return Map<String, List<String>> where map key is namespace code, and value is list of dictionary
     *         file locations
     */
    public Map<String, List<String>> getModuleDictionaryFiles() {
        return moduleDictionaryFiles;
    }

    /**
     * Setter for the map of module dictionary files
     *
     * @param moduleDictionaryFiles
     */
    public void setModuleDictionaryFiles(Map<String, List<String>> moduleDictionaryFiles) {
        this.moduleDictionaryFiles = moduleDictionaryFiles;
    }

    /**
     * Order modules should be loaded into the dictionary
     *
     * <p>
     * Modules are loaded in the order they are found in this list. If not explicity set, they will be loaded in
     * the order their dictionary file locations are added
     * </p>
     *
     * @return List<String> list of namespace codes indicating the module load order
     */
    public List<String> getModuleLoadOrder() {
        return moduleLoadOrder;
    }

    /**
     * Setter for the list of namespace codes indicating the module load order
     *
     * @param moduleLoadOrder
     */
    public void setModuleLoadOrder(List<String> moduleLoadOrder) {
        this.moduleLoadOrder = moduleLoadOrder;
    }

    /**
     * Sets the DataDictionaryMapper
     *
     * @param mapper the datadictionary mapper
     */
    public void setDataDictionaryMapper(DataDictionaryMapper mapper) {
        this.ddMapper = mapper;
    }

    /**
     * @param className
     * @return BusinessObjectEntry for the named class, or null if none exists
     */
    @Deprecated
    public BusinessObjectEntry getBusinessObjectEntry(String className) {
        return ddMapper.getBusinessObjectEntry(ddIndex, className);
    }

    /**
     * @param className
     * @return BusinessObjectEntry for the named class, or null if none exists
     */
    public DataObjectEntry getDataObjectEntry(String className) {
        return ddMapper.getDataObjectEntry(ddIndex, className);
    }

    /**
     * This method gets the business object entry for a concrete class
     *
     * @param className
     * @return
     */
    public BusinessObjectEntry getBusinessObjectEntryForConcreteClass(String className) {
        return ddMapper.getBusinessObjectEntryForConcreteClass(ddIndex, className);
    }

    /**
     * @return List of businessObject classnames
     */
    public List<String> getBusinessObjectClassNames() {
        return ddMapper.getBusinessObjectClassNames(ddIndex);
    }

    /**
     * @return Map of (classname, BusinessObjectEntry) pairs
     */
    public Map<String, BusinessObjectEntry> getBusinessObjectEntries() {
        return ddMapper.getBusinessObjectEntries(ddIndex);
    }

    /**
     * @param className
     * @return DataDictionaryEntryBase for the named class, or null if none
     *         exists
     */
    public DataDictionaryEntry getDictionaryObjectEntry(String className) {
        return ddMapper.getDictionaryObjectEntry(ddIndex, className);
    }

    /**
     * Returns the KNS document entry for the given lookup key.  The documentTypeDDKey is interpreted
     * successively in the following ways until a mapping is found (or none if found):
     * <ol>
     * <li>KEW/workflow document type</li>
     * <li>business object class name</li>
     * <li>maintainable class name</li>
     * </ol>
     * This mapping is compiled when DataDictionary files are parsed on startup (or demand).  Currently this
     * means the mapping is static, and one-to-one (one KNS document maps directly to one and only
     * one key).
     *
     * @param documentTypeDDKey the KEW/workflow document type name
     * @return the KNS DocumentEntry if it exists
     */
    public DocumentEntry getDocumentEntry(String documentTypeDDKey) {
        return ddMapper.getDocumentEntry(ddIndex, documentTypeDDKey);
    }

    /**
     * Note: only MaintenanceDocuments are indexed by businessObject Class
     *
     * This is a special case that is referenced in one location. Do we need
     * another map for this stuff??
     *
     * @param businessObjectClass
     * @return DocumentEntry associated with the given Class, or null if there
     *         is none
     */
    public MaintenanceDocumentEntry getMaintenanceDocumentEntryForBusinessObjectClass(Class<?> businessObjectClass) {
        return ddMapper.getMaintenanceDocumentEntryForBusinessObjectClass(ddIndex, businessObjectClass);
    }

    public Map<String, DocumentEntry> getDocumentEntries() {
        return ddMapper.getDocumentEntries(ddIndex);
    }

    /**
     * Returns the View entry identified by the given id
     *
     * @param viewId unique id for view
     * @return View instance associated with the id
     */
    public View getViewById(String viewId) {
        return ddMapper.getViewById(uifIndex, viewId);
    }

    /**
     * Returns the View entry identified by the given id, meant for view readonly
     * access (not running the lifecycle but just checking configuration)
     *
     * @param viewId unique id for view
     * @return View instance associated with the id
     */
    public View getImmutableViewById(String viewId) {
        return ddMapper.getImmutableViewById(uifIndex, viewId);
    }

    /**
     * Returns View instance identified by the view type name and index
     *
     * @param viewTypeName - type name for the view
     * @param indexKey - Map of index key parameters, these are the parameters the
     * indexer used to index the view initially and needs to identify
     * an unique view instance
     * @return View instance that matches the given index
     */
    public View getViewByTypeIndex(ViewType viewTypeName, Map<String, String> indexKey) {
        return ddMapper.getViewByTypeIndex(uifIndex, viewTypeName, indexKey);
    }

    /**
     * Returns the view id for the view that matches the given view type and index
     *
     * @param viewTypeName type name for the view
     * @param indexKey Map of index key parameters, these are the parameters the
     * indexer used to index the view initially and needs to identify
     * an unique view instance
     * @return id for the view that matches the view type and index or null if a match is not found
     */
    public String getViewIdByTypeIndex(ViewType viewTypeName, Map<String, String> indexKey) {
        return ddMapper.getViewIdByTypeIndex(uifIndex, viewTypeName, indexKey);
    }

    /**
     * Indicates whether a <code>View</code> exists for the given view type and index information
     *
     * @param viewTypeName - type name for the view
     * @param indexKey - Map of index key parameters, these are the parameters the
     * indexer used to index the view initially and needs to identify
     * an unique view instance
     * @return boolean true if view exists, false if not
     */
    public boolean viewByTypeExist(ViewType viewTypeName, Map<String, String> indexKey) {
        return ddMapper.viewByTypeExist(uifIndex, viewTypeName, indexKey);
    }

    /**
     * Gets all <code>View</code> prototypes configured for the given view type
     * name
     *
     * @param viewTypeName - view type name to retrieve
     * @return List<View> view prototypes with the given type name, or empty
     *         list
     */
    public List<View> getViewsForType(ViewType viewTypeName) {
        return ddMapper.getViewsForType(uifIndex, viewTypeName);
    }

    /**
     * Returns an object from the dictionary by its spring bean name
     *
     * @param beanName - id or name for the bean definition
     * @return Object object instance created or the singleton being maintained
     */
    public Object getDictionaryObject(String beanName) {
        return ddBeans.getBean(beanName);
    }

    /**
     * Indicates whether the data dictionary contains a bean with the given id
     *
     * @param id - id of the bean to check for
     * @return boolean true if dictionary contains bean, false otherwise
     */
    public boolean containsDictionaryObject(String id) {
        return ddBeans.containsBean(id);
    }

    /**
     * Retrieves the configured property values for the view bean definition associated with the given id
     *
     * <p>
     * Since constructing the View object can be expensive, when metadata only is needed this method can be used
     * to retrieve the configured property values. Note this looks at the merged bean definition
     * </p>
     *
     * @param viewId - id for the view to retrieve
     * @return PropertyValues configured on the view bean definition, or null if view is not found
     */
    public PropertyValues getViewPropertiesById(String viewId) {
        return ddMapper.getViewPropertiesById(uifIndex, viewId);
    }

    /**
     * Retrieves the configured property values for the view bean definition associated with the given type and
     * index
     *
     * <p>
     * Since constructing the View object can be expensive, when metadata only is needed this method can be used
     * to retrieve the configured property values. Note this looks at the merged bean definition
     * </p>
     *
     * @param viewTypeName - type name for the view
     * @param indexKey - Map of index key parameters, these are the parameters the indexer used to index
     * the view initially and needs to identify an unique view instance
     * @return PropertyValues configured on the view bean definition, or null if view is not found
     */
    public PropertyValues getViewPropertiesByType(ViewType viewTypeName, Map<String, String> indexKey) {
        return ddMapper.getViewPropertiesByType(uifIndex, viewTypeName, indexKey);
    }

    /**
     * Retrieves the list of dictionary bean names that are associated with the given namespace code
     *
     * @param namespaceCode - namespace code to retrieve associated bean names for
     * @return List<String> bean names associated with the namespace
     */
    public List<String> getBeanNamesForNamespace(String namespaceCode) {
        List<String> namespaceBeans = new ArrayList<String>();

        Map<String, List<String>> dictionaryBeansByNamespace = ddIndex.getDictionaryBeansByNamespace();
        if (dictionaryBeansByNamespace.containsKey(namespaceCode)) {
            namespaceBeans = dictionaryBeansByNamespace.get(namespaceCode);
        }

        return namespaceBeans;
    }

    /**
     * Retrieves the namespace code the given bean name is associated with
     *
     * @param beanName - name of the dictionary bean to find namespace code for
     * @return String namespace code the bean is associated with, or null if a namespace was not found
     */
    public String getNamespaceForBeanDefinition(String beanName) {
        String beanNamespace = null;

        Map<String, List<String>> dictionaryBeansByNamespace = ddIndex.getDictionaryBeansByNamespace();
        for (Map.Entry<String, List<String>> moduleDefinitions : dictionaryBeansByNamespace.entrySet()) {
            List<String> namespaceBeans = moduleDefinitions.getValue();
            if (namespaceBeans.contains(beanName)) {
                beanNamespace = moduleDefinitions.getKey();
                break;
            }
        }

        return beanNamespace;
    }

    /**
     * @param targetClass
     * @param propertyName
     * @return true if the given propertyName names a property of the given class
     * @throws CompletionException if there is a problem accessing the named property on the given class
     */
    public static boolean isPropertyOf(Class targetClass, String propertyName) {
        if (targetClass == null) {
            throw new IllegalArgumentException("invalid (null) targetClass");
        }
        if (StringUtils.isBlank(propertyName)) {
            throw new IllegalArgumentException("invalid (blank) propertyName");
        }

        PropertyDescriptor propertyDescriptor = buildReadDescriptor(targetClass, propertyName);

        boolean isPropertyOf = (propertyDescriptor != null);
        return isPropertyOf;
    }

    /**
     * @param targetClass
     * @param propertyName
     * @return true if the given propertyName names a Collection property of the given class
     * @throws CompletionException if there is a problem accessing the named property on the given class
     */
    public static boolean isCollectionPropertyOf(Class targetClass, String propertyName) {
        boolean isCollectionPropertyOf = false;

        PropertyDescriptor propertyDescriptor = buildReadDescriptor(targetClass, propertyName);
        if (propertyDescriptor != null) {
            Class clazz = propertyDescriptor.getPropertyType();

            if ((clazz != null) && Collection.class.isAssignableFrom(clazz)) {
                isCollectionPropertyOf = true;
            }
        }

        return isCollectionPropertyOf;
    }

    public static PersistenceStructureService persistenceStructureService;

    /**
     * @return the persistenceStructureService
     */
    public static PersistenceStructureService getPersistenceStructureService() {
        if (persistenceStructureService == null) {
            persistenceStructureService = KRADServiceLocator.getPersistenceStructureService();
        }
        return persistenceStructureService;
    }

    /**
     * This method determines the Class of the attributeName passed in. Null will be returned if the member is not
     * available, or if
     * a reflection exception is thrown.
     *
     * @param boClass - Class that the attributeName property exists in.
     * @param attributeName - Name of the attribute you want a class for.
     * @return The Class of the attributeName, if the attribute exists on the rootClass. Null otherwise.
     */
    public static Class getAttributeClass(Class boClass, String attributeName) {

        // fail loudly if the attributeName isnt a member of rootClass
        if (!isPropertyOf(boClass, attributeName)) {
            throw new AttributeValidationException(
                    "unable to find attribute '" + attributeName + "' in rootClass '" + boClass.getName() + "'");
        }

        //Implementing Externalizable Business Object Services...
        //The boClass can be an interface, hence handling this separately, 
        //since the original method was throwing exception if the class could not be instantiated.
        if (boClass.isInterface()) {
            return getAttributeClassWhenBOIsInterface(boClass, attributeName);
        } else {
            return getAttributeClassWhenBOIsClass(boClass, attributeName);
        }

    }

    /**
     * This method gets the property type of the given attributeName when the bo class is a concrete class
     *
     * @param boClass
     * @param attributeName
     * @return
     */
    private static Class getAttributeClassWhenBOIsClass(Class boClass, String attributeName) {
        Object boInstance;
        try {
            boInstance = boClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantiate Data Object: " + boClass, e);
        }

        // attempt to retrieve the class of the property
        try {
            return ObjectUtils.getPropertyType(boInstance, attributeName, getPersistenceStructureService());
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to determine property type for: " + boClass.getName() + "." + attributeName, e);
        }
    }

    /**
     * This method gets the property type of the given attributeName when the bo class is an interface
     * This method will also work if the bo class is not an interface,
     * but that case requires special handling, hence a separate method getAttributeClassWhenBOIsClass
     *
     * @param boClass
     * @param attributeName
     * @return
     */
    private static Class getAttributeClassWhenBOIsInterface(Class boClass, String attributeName) {
        if (boClass == null) {
            throw new IllegalArgumentException("invalid (null) boClass");
        }
        if (StringUtils.isBlank(attributeName)) {
            throw new IllegalArgumentException("invalid (blank) attributeName");
        }

        PropertyDescriptor propertyDescriptor = null;

        String[] intermediateProperties = attributeName.split("\\.");
        int lastLevel = intermediateProperties.length - 1;
        Class currentClass = boClass;

        for (int i = 0; i <= lastLevel; ++i) {

            String currentPropertyName = intermediateProperties[i];
            propertyDescriptor = buildSimpleReadDescriptor(currentClass, currentPropertyName);

            if (propertyDescriptor != null) {

                Class propertyType = propertyDescriptor.getPropertyType();
                if (propertyType.equals(PersistableBusinessObjectExtension.class)) {
                    propertyType = getPersistenceStructureService().getBusinessObjectAttributeClass(currentClass,
                            currentPropertyName);
                }
                if (Collection.class.isAssignableFrom(propertyType)) {
                    // TODO: determine property type using generics type definition
                    throw new AttributeValidationException(
                            "Can't determine the Class of Collection elements because when the business object is an (possibly ExternalizableBusinessObject) interface.");
                } else {
                    currentClass = propertyType;
                }
            } else {
                throw new AttributeValidationException(
                        "Can't find getter method of " + boClass.getName() + " for property " + attributeName);
            }
        }
        return currentClass;
    }

    /**
     * This method determines the Class of the elements in the collectionName passed in.
     *
     * @param boClass Class that the collectionName collection exists in.
     * @param collectionName the name of the collection you want the element class for
     * @return
     */
    public static Class getCollectionElementClass(Class boClass, String collectionName) {
        if (boClass == null) {
            throw new IllegalArgumentException("invalid (null) boClass");
        }
        if (StringUtils.isBlank(collectionName)) {
            throw new IllegalArgumentException("invalid (blank) collectionName");
        }

        PropertyDescriptor propertyDescriptor = null;

        String[] intermediateProperties = collectionName.split("\\.");
        Class currentClass = boClass;

        for (int i = 0; i < intermediateProperties.length; ++i) {

            String currentPropertyName = intermediateProperties[i];
            propertyDescriptor = buildSimpleReadDescriptor(currentClass, currentPropertyName);

            if (propertyDescriptor != null) {

                Class type = propertyDescriptor.getPropertyType();
                if (Collection.class.isAssignableFrom(type)) {

                    if (getPersistenceStructureService().isPersistable(currentClass)) {

                        Map<String, Class> collectionClasses = new HashMap<String, Class>();
                        collectionClasses = getPersistenceStructureService().listCollectionObjectTypes(currentClass);
                        currentClass = collectionClasses.get(currentPropertyName);

                    } else {
                        throw new RuntimeException(
                                "Can't determine the Class of Collection elements because persistenceStructureService.isPersistable("
                                        +
                                        currentClass.getName()
                                        +
                                        ") returns false.");
                    }

                } else {

                    currentClass = propertyDescriptor.getPropertyType();

                }
            }
        }

        return currentClass;
    }

    static private Map<String, Map<String, PropertyDescriptor>> cache =
            new TreeMap<String, Map<String, PropertyDescriptor>>();

    /**
     * @param propertyClass
     * @param propertyName
     * @return PropertyDescriptor for the getter for the named property of the given class, if one exists.
     */
    public static PropertyDescriptor buildReadDescriptor(Class propertyClass, String propertyName) {
        if (propertyClass == null) {
            throw new IllegalArgumentException("invalid (null) propertyClass");
        }
        if (StringUtils.isBlank(propertyName)) {
            throw new IllegalArgumentException("invalid (blank) propertyName");
        }

        PropertyDescriptor propertyDescriptor = null;

        String[] intermediateProperties = propertyName.split("\\.");
        int lastLevel = intermediateProperties.length - 1;
        Class currentClass = propertyClass;

        for (int i = 0; i <= lastLevel; ++i) {

            String currentPropertyName = intermediateProperties[i];
            propertyDescriptor = buildSimpleReadDescriptor(currentClass, currentPropertyName);

            if (i < lastLevel) {

                if (propertyDescriptor != null) {

                    Class propertyType = propertyDescriptor.getPropertyType();
                    if (propertyType.equals(PersistableBusinessObjectExtension.class)) {
                        propertyType = getPersistenceStructureService().getBusinessObjectAttributeClass(currentClass,
                                currentPropertyName);
                    }
                    if (Collection.class.isAssignableFrom(propertyType)) {

                        if (getPersistenceStructureService().isPersistable(currentClass)) {

                            Map<String, Class> collectionClasses = new HashMap<String, Class>();
                            collectionClasses = getPersistenceStructureService().listCollectionObjectTypes(
                                    currentClass);
                            currentClass = collectionClasses.get(currentPropertyName);

                        } else {

                            throw new RuntimeException(
                                    "Can't determine the Class of Collection elements because persistenceStructureService.isPersistable("
                                            +
                                            currentClass.getName()
                                            +
                                            ") returns false.");

                        }

                    } else {

                        currentClass = propertyType;

                    }

                }

            }

        }

        return propertyDescriptor;
    }

    /**
     * @param propertyClass
     * @param propertyName
     * @return PropertyDescriptor for the getter for the named property of the given class, if one exists.
     */
    public static PropertyDescriptor buildSimpleReadDescriptor(Class propertyClass, String propertyName) {
        if (propertyClass == null) {
            throw new IllegalArgumentException("invalid (null) propertyClass");
        }
        if (StringUtils.isBlank(propertyName)) {
            throw new IllegalArgumentException("invalid (blank) propertyName");
        }

        PropertyDescriptor p = null;

        // check to see if we've cached this descriptor already. if yes, return true.
        String propertyClassName = propertyClass.getName();
        Map<String, PropertyDescriptor> m = cache.get(propertyClassName);
        if (null != m) {
            p = m.get(propertyName);
            if (null != p) {
                return p;
            }
        }

        // Use PropertyUtils.getPropertyDescriptors instead of manually constructing PropertyDescriptor because of
        // issues with introspection and generic/co-variant return types
        // See https://issues.apache.org/jira/browse/BEANUTILS-340 for more details

        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(propertyClass);
        if (ArrayUtils.isNotEmpty(descriptors)) {
            for (PropertyDescriptor descriptor : descriptors) {
                if (descriptor.getName().equals(propertyName)) {
                    p = descriptor;
                }
            }
        }

        // cache the property descriptor if we found it.
        if (p != null) {
            if (m == null) {
                m = new TreeMap<String, PropertyDescriptor>();
                cache.put(propertyClassName, m);
            }
            m.put(propertyName, p);
        }

        return p;
    }

    public Set<InactivationBlockingMetadata> getAllInactivationBlockingMetadatas(Class blockedClass) {
        return ddMapper.getAllInactivationBlockingMetadatas(ddIndex, blockedClass);
    }

    /**
     * This method gathers beans of type BeanOverride and invokes each one's performOverride() method.
     */
    // KULRICE-4513
    public void performBeanOverrides() {
        Collection<BeanOverride> beanOverrides = ddBeans.getBeansOfType(BeanOverride.class).values();

        if (beanOverrides.isEmpty()) {
            LOG.info("DataDictionary.performOverrides(): No beans to override");
        }
        for (BeanOverride beanOverride : beanOverrides) {

            Object bean = ddBeans.getBean(beanOverride.getBeanName());
            beanOverride.performOverride(bean);
            LOG.info("DataDictionary.performOverrides(): Performing override on bean: " + bean.toString());
        }
    }

}
