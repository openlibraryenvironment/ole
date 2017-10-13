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
package org.kuali.rice.krad.service;

import java.util.List;

public interface KualiModuleService {

    /**
     * get the list of all installed module services
     *
     * @return
     */
    List<ModuleService> getInstalledModuleServices();

    /**
     * Returns the module service with the given ID or null if the module ID is not found.
     *
     * @param moduleId
     * @return
     */
    ModuleService getModuleService(String moduleId);

    /**
     * Returns the module service with the given moduleCode or null if the moduleCode is not found.
     *
     * @param namespaceCode
     * @return
     */
    ModuleService getModuleServiceByNamespaceCode(String namespaceCode);

    boolean isModuleServiceInstalled(String namespaceCode);

    /**
     * Given a class, this method will return the module service which is responsible for authorizing access to it. It returns null if no
     * module is found.
     *
     * @param boClass
     * @return ModuleService representing the service responsible for the passed in Class
     * @throws ModuleServiceNotFoundException if boClass is an ExternalizableBusinessObject that no ModuleService is responsible for.
     */
    ModuleService getResponsibleModuleService(Class boClass);

    /**
     * Given a job name, this method will return the module service which is responsible for handling it. It returns null if no
     * module is found.
     *
     * @param jobName
     * @return
     */
    ModuleService getResponsibleModuleServiceForJob(String jobName);

    public void setInstalledModuleServices(List<ModuleService> moduleServices);

    public List<String> getDataDictionaryPackages();

    /**
     *
     * This method gets namespace name for the given namespace code
     *
     * @param namespaceCode
     * @return
     */
    public String getNamespaceName(String namespaceCode);

    String getNamespaceCode(Class<?> documentOrStepClass);
    String getComponentCode(Class<?> documentOrStepClass);

}

