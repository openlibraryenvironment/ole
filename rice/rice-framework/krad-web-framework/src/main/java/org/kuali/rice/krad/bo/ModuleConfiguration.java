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
package org.kuali.rice.krad.bo;

import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.PersistenceService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.persistence.EntityManager;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class contains various configuration properties for a Rice module.
 *
 * <p>
 * The Rice framework is  composed of several separate modules, each of which is
 * responsible for providing a set of functionality. These include:
 * <ul>
 *      <li>KEW - the Rice enterprise workflow module
 *      <li>KIM - the Rice identity management module
 *      <li>KSB - the Rice service bus
 *      <li>KRAD - the Rice rapid application development module
 *      <li>KRMS - the Rice business rules management syste
 *      <li>eDocLite - a Rice framework for creating simple documents quickly
 *      <li>...as well as several others. Refer to the Rice documentation for a complete list.
 * </ul>
 * <br>
 * Client Applications will also have their own module configurations. A client application could create a single
 * module or multiple modules, depending on how it is organized.
 * <br>
 * This ModuleConfiguration object is created during Spring initialization. The properties of this ModuleConfiguration
 * are specified in the module's SpringBean definition XML configuration file.
 *</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ModuleConfiguration implements InitializingBean, ApplicationContextAware {

    /**
     * the module's namespace.
     */
	protected String namespaceCode;
	protected ApplicationContext applicationContext;

    /**
     * the package name prefixes for classes used in this module
     */
	protected List<String> packagePrefixes;

    /**
     * a list of entity description files to be loaded during initialization of the persistence service.
     * <p>
     * Currently only used by OJB repository service implementation.
     * </p>
     */
	protected List<String> databaseRepositoryFilePaths;

    /**
     * the list of data dictionary packages to be loaded for this module by the data dictionary service during system
     * startup.
     */
	protected List<String> dataDictionaryPackages;

	protected List<String> scriptConfigurationFilePaths;

	protected List<String> jobNames;

	protected List<String> triggerNames;

    protected List<String> resourceBundleNames;

	//optional
	protected String dataSourceName;

	//optional
	protected EntityManager entityManager;

	protected Map<Class, Class> externalizableBusinessObjectImplementations;

	protected boolean initializeDataDictionary;

	protected PersistenceService persistenceService;

    /**
     * the implementation of the data dictionary service to use for this module.
     */
	protected DataDictionaryService dataDictionaryService;

    /**
     *  Constructor for a ModuleConfiguration.
     *
     *  <p>
     *  Initializes the arrays of this ModuleConfiguration to empty ArrayLists.
     *  </p>
     */
	public ModuleConfiguration() {
		databaseRepositoryFilePaths = new ArrayList<String>();
		dataDictionaryPackages = new ArrayList<String>();
		scriptConfigurationFilePaths = new ArrayList<String>();
		jobNames = new ArrayList<String>();
		triggerNames = new ArrayList<String>();
        resourceBundleNames = new ArrayList<String>();
	}

    /**
     * Performs additional custom initialization after the bean is created and it's properties are set by the
     * Spring framework.
     *
     * <p>
     * Loads the data dictionary packages configured for this module.
     * Also loads any OJB database repository files configured.
     * </p>
     *
     * @throws Exception
     */
	@Override
    public void afterPropertiesSet() throws Exception {
        if (isInitializeDataDictionary() && getDataDictionaryPackages() != null &&
                !getDataDictionaryPackages().isEmpty()) {
            if (getDataDictionaryService() == null) {
                setDataDictionaryService(KRADServiceLocatorWeb.getDataDictionaryService());
            }

            if (getDataDictionaryService() == null) {
                setDataDictionaryService((DataDictionaryService) applicationContext.getBean(
                        KRADServiceLocatorWeb.DATA_DICTIONARY_SERVICE));
            }

            if (dataDictionaryService != null) {
                dataDictionaryService.addDataDictionaryLocations(getNamespaceCode(), getDataDictionaryPackages());
            }
        }

        if (getDatabaseRepositoryFilePaths() != null) {
            for (String repositoryLocation : getDatabaseRepositoryFilePaths()) {
                // Need the OJB persistence service because it is the only one ever using the database repository files
                if (getPersistenceService() == null) {
                    setPersistenceService(KRADServiceLocatorWeb.getPersistenceServiceOjb());
                }
                if (persistenceService == null) {
                    setPersistenceService((PersistenceService) applicationContext.getBean(
                            KRADServiceLocatorWeb.PERSISTENCE_SERVICE_OJB));
                }
                getPersistenceService().loadRepositoryDescriptor(repositoryLocation);
            }
        }
    }

	/**
     * Retrieves the database repository file paths to be used by the persistence service configured for this module.
     *
     * <p>
     * Used by the OBJ persistence service to load entity descriptors.
     * The file paths are returned as a List of Strings. If no file paths are configured,
     * an empty list is returned.  This method should never return null.
     * </p>
     *
	 * @return a List containing the databaseRepositoryFilePaths
	 */
	public List<String> getDatabaseRepositoryFilePaths() {
		return this.databaseRepositoryFilePaths;
	}

	/**
     * Initializes the list of database repository files to load during persistence service initialization.
     *
     * <p>
     * The repository file names are listed in the module's Spring bean configuration file.
     * This property is set during Spring initialization.
     * </p>
     *
	 * @param databaseRepositoryFilePaths the List of entity descriptor files to load.
	 */
	public void setDatabaseRepositoryFilePaths(
			List<String> databaseRepositoryFilePaths) {
		this.trimList(databaseRepositoryFilePaths);
		this.databaseRepositoryFilePaths = databaseRepositoryFilePaths;
	}

	/**
     * Returns a list of data dictionary packages configured for this ModuleConfiguration.
     *
     * <p>
     * If no data dictionary packages are defined, will return an empty list.
     * Should never return null.
     * </p>
     *
	 * @return a List of Strings containing the names of the dataDictionaryPackages
	 */
	public List<String> getDataDictionaryPackages() {
		return this.dataDictionaryPackages;
	}

	/**
     * Initializes the list of data dictionary packages associated with this ModuleConfiguration.
     *
     * <p>
     * The data dictionary packages are listed in the module's Spring bean configuration file.
     * This property is set during Spring initialization.
     * </p>
     *
	 * @param dataDictionaryPackages a List of Strings containing the dataDictionaryPackages.
	 */
	public void setDataDictionaryPackages(List<String> dataDictionaryPackages) {
		this.trimList(dataDictionaryPackages);
		this.dataDictionaryPackages = dataDictionaryPackages;
	}

	/**
	 * @return the externalizableBusinessObjectImplementations
	 */
	public Map<Class, Class> getExternalizableBusinessObjectImplementations() {
		if (this.externalizableBusinessObjectImplementations == null)
			return null;
		return Collections.unmodifiableMap(this.externalizableBusinessObjectImplementations);
	}

	/**
	 * @param externalizableBusinessObjectImplementations the externalizableBusinessObjectImplementations to set
	 */
	public void setExternalizableBusinessObjectImplementations(
			Map<Class, Class> externalizableBusinessObjectImplementations) {
		if (externalizableBusinessObjectImplementations != null) {
			for (Class implClass : externalizableBusinessObjectImplementations.values()) {
				int implModifiers = implClass.getModifiers();
				if (Modifier.isInterface(implModifiers) || Modifier.isAbstract(implModifiers)) {
					throw new RuntimeException("Externalizable business object implementation class " +
							implClass.getName() + " must be a non-interface, non-abstract class");
				}
			}
		}
		this.externalizableBusinessObjectImplementations = externalizableBusinessObjectImplementations;
	}

	public List<String> getPackagePrefixes(){
		return this.packagePrefixes;
	}

	public void setPackagePrefixes(List<String> packagePrefixes){
		this.trimList(packagePrefixes);
		this.packagePrefixes = packagePrefixes;
	}

	public void setInitializeDataDictionary(boolean initializeDataDictionary){
		this.initializeDataDictionary = initializeDataDictionary;
	}

	public List<String> getScriptConfigurationFilePaths(){
		return this.scriptConfigurationFilePaths;
	}

	/**
	 * @return the jobNames
	 */
	public List<String> getJobNames() {
		return this.jobNames;
	}

	/**
	 * @param jobNames the jobNames to set
	 */
	public void setJobNames(List<String> jobNames) {
		this.jobNames = jobNames;
	}

	/**
	 * @return the triggerNames
	 */
	public List<String> getTriggerNames() {
		return this.triggerNames;
	}

	/**
	 * @param triggerNames the triggerNames to set
	 */
	public void setTriggerNames(List<String> triggerNames) {
		this.triggerNames = triggerNames;
	}

    /**
     * List of resource bundle names that will provides messages for this module
     *
     * <p>
     * Each bundle will point to a resource property file that contain key/value message pairs. The properties
     * file should be on the classpath and the name is given by specifying the fully qualified class name
     * (dot notation).
     * </p>
     *
     * @return List<String> resource bundle names
     * @see java.util.ResourceBundle
     */
    public List<String> getResourceBundleNames() {
        return resourceBundleNames;
    }

    /**
     * Setter for the list of resource bundle names that provides messages for the module
     *
     * @param resourceBundleNames
     */
    public void setResourceBundleNames(List<String> resourceBundleNames) {
        this.resourceBundleNames = resourceBundleNames;
    }

    /**
	 * @return the initializeDataDictionary
	 */
	public boolean isInitializeDataDictionary() {
		return this.initializeDataDictionary;
	}

	/**
	 * @param scriptConfigurationFilePaths the scriptConfigurationFilePaths to set
	 */
	public void setScriptConfigurationFilePaths(
			List<String> scriptConfigurationFilePaths) {
		this.scriptConfigurationFilePaths = scriptConfigurationFilePaths;
	}

	/**
	 * @return the namespaceCode
	 */
	public String getNamespaceCode() {
		return this.namespaceCode;
	}

	/**
	 * @param namespaceCode the namespaceCode to set
	 */
	public void setNamespaceCode(String namespaceCode) {
		this.namespaceCode = namespaceCode;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * @return the dataDictionaryService
	 */
	public DataDictionaryService getDataDictionaryService() {
		return this.dataDictionaryService;
	}

	/**
	 * @param dataDictionaryService the dataDictionaryService to set
	 */
	public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
		this.dataDictionaryService = dataDictionaryService;
	}

	/**
	 * @return the persistenceService
	 */
	public PersistenceService getPersistenceService() {
		return this.persistenceService;
	}

	/**
	 * @param persistenceService the persistenceService to set
	 */
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

    public String getDataSourceName() {
        return this.dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
	 *
	 * This method passes by reference. It will alter the list passed in.
	 *
	 * @param stringList
	 */
	protected void trimList(List<String> stringList){
		if(stringList != null){
			// we need to trim whitespace from the stringList. Because trim() creates a new string
			// we have to explicitly put the new string back into the list
			for(int i=0; i<stringList.size(); i++){
				String elmt = stringList.get(i);
				elmt = elmt.trim();
				stringList.set(i, elmt);
			}
		}
	}

}
