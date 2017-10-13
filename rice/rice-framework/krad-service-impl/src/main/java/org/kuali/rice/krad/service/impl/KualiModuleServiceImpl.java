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

import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.namespace.Namespace;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.framework.parameter.ParameterConstants;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.document.TransactionalDocument;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.exception.ModuleServiceNotFoundException;
import org.kuali.rice.krad.util.KRADConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KualiModuleServiceImpl implements KualiModuleService, InitializingBean, ApplicationContextAware {

    private List<ModuleService> installedModuleServices = new ArrayList<ModuleService>();
    private boolean loadRiceInstalledModuleServices;
    private ApplicationContext applicationContext;
    
    /**
	 * @param applicationContext the applicationContext to set
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public List<ModuleService> getInstalledModuleServices() {
        return installedModuleServices;
    }

    @Override
	public ModuleService getModuleService(String moduleId) {
        for (ModuleService moduleService : installedModuleServices) {
            if ( moduleService.getModuleConfiguration().getNamespaceCode().equals( moduleId ) ) {
                return moduleService;
            }
        } 
        return null;
    }

    @Override
	public ModuleService getModuleServiceByNamespaceCode(String namespaceCode) {
        for (ModuleService moduleService : installedModuleServices) {
            if ( moduleService.getModuleConfiguration().getNamespaceCode().equals( namespaceCode ) ) {
                return moduleService;
            }
        } 
        return null;
    }

    @Override
	public boolean isModuleServiceInstalled(String namespaceCode) {
        for (ModuleService moduleService : installedModuleServices) {
            if ( moduleService.getModuleConfiguration().getNamespaceCode().equals( namespaceCode ) ) {
                return true;
            }
        } 
        return false;
    }

    @Override
	public ModuleService getResponsibleModuleService(Class boClass) {
    	if(boClass==null) {
			return null;
		}
    	for (ModuleService moduleService : installedModuleServices) {
    	    if ( moduleService.isResponsibleFor( boClass ) ) {
    	        return moduleService;
    	    }
    	}
    	//Throwing exception only for externalizable business objects
    	if(ExternalizableBusinessObject.class.isAssignableFrom(boClass)){
    	    String message;
    		if(!boClass.isInterface()) {
				message = "There is no responsible module for the externalized business object class: "+boClass;
			} else {
				message = "There is no responsible module for the externalized business object interface: "+boClass;
			}
    		throw new ModuleServiceNotFoundException(message);
    	} 
    	//Returning null for business objects other than externalizable to keep the framework backward compatible
    	return null;
    }

    @Override
	public ModuleService getResponsibleModuleServiceForJob(String jobName){
        for(ModuleService moduleService : installedModuleServices){
            if(moduleService.isResponsibleForJob(jobName)){
                return moduleService;
            }
        }
        return null;
    }
    
    @Override
	public void setInstalledModuleServices(List<ModuleService> installedModuleServices) {
        this.installedModuleServices = installedModuleServices;
    }

    @Override
	public List<String> getDataDictionaryPackages() {
        List<String> packages  = new ArrayList<String>();
        for ( ModuleService moduleService : installedModuleServices ) {
            if ( moduleService.getModuleConfiguration().getDataDictionaryPackages() != null ) {
                packages.addAll( moduleService.getModuleConfiguration().getDataDictionaryPackages() );
            }
        }
        return packages;
    }

    @Override
	public String getNamespaceName(final String namespaceCode){
    	Namespace parameterNamespace = CoreServiceApiServiceLocator.getNamespaceService().getNamespace(namespaceCode);
    	return parameterNamespace==null ? "" : parameterNamespace.getName();
    }
    
	/**
	 * @param loadRiceInstalledModuleServices the loadRiceInstalledModuleServices to set
	 */
	public void setLoadRiceInstalledModuleServices(
			boolean loadRiceInstalledModuleServices) {
		this.loadRiceInstalledModuleServices = loadRiceInstalledModuleServices;
	}

	/***
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if(loadRiceInstalledModuleServices){
			try {
				installedModuleServices.addAll(
						GlobalResourceLoader.<KualiModuleService>getService(KualiModuleService.class.getSimpleName().substring(0, 1).toLowerCase() + KualiModuleService.class.getSimpleName().substring(1)).getInstalledModuleServices());
			} catch ( NoSuchBeanDefinitionException ex ) {
				installedModuleServices.addAll( ((KualiModuleService)applicationContext.getBean( KRADServiceLocatorWeb.KUALI_MODULE_SERVICE )).getInstalledModuleServices() );
			}
		}
	}

    @Override
    public String getNamespaceCode(Class<?> documentClass) {
        if (documentClass == null) {
            throw new IllegalArgumentException("documentClass must not be null");
        }

        if (documentClass.isAnnotationPresent(ParameterConstants.NAMESPACE.class)) {
            return (documentClass.getAnnotation(ParameterConstants.NAMESPACE.class)).namespace();
        }
        ModuleService moduleService = getResponsibleModuleService(documentClass);
        if (moduleService != null) {
            return moduleService.getModuleConfiguration().getNamespaceCode();
        }
        if (documentClass.getName().startsWith("org.kuali.rice.krad")) {
            return KRADConstants.KNS_NAMESPACE;
        }
        if (documentClass.getName().startsWith("org.kuali.rice.edl")) {
            return "KR-EDL";
        }
        if (documentClass.getName().startsWith("org.kuali.rice.kew")) {
            return "KR-WKFLW";
        }
        if (documentClass.getName().startsWith("org.kuali.rice.edl")) {
        	return "KR-WKFLW";
    	}
        if (documentClass.getName().startsWith("org.kuali.rice.kim")) {
            return "KR-IDM";
        }
        if (documentClass.getName().startsWith("org.kuali.rice.core")) {
            return "KR-CORE";
        }
        throw new IllegalArgumentException("Unable to determine the namespace code for documentClass " + documentClass.getName());
    }

    @Override
    public String getComponentCode(Class<?> documentClass) {
        if (documentClass == null) {
            throw new IllegalArgumentException("documentClass must not be null");
        }

        if (documentClass.isAnnotationPresent(ParameterConstants.COMPONENT.class)) {
            return documentClass.getAnnotation(ParameterConstants.COMPONENT.class).component();
        } else if (TransactionalDocument.class.isAssignableFrom(documentClass)) {
            return documentClass.getSimpleName().replace("Document", "");
        } else if (BusinessObject.class.isAssignableFrom(documentClass)) {
            return documentClass.getSimpleName();
        }
        throw new IllegalArgumentException("Unable to determine the component code for documentClass " + documentClass.getName());
    }

}

