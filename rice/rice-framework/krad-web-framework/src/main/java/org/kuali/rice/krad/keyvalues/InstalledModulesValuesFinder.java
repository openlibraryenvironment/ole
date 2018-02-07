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
package org.kuali.rice.krad.keyvalues;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;

import java.util.ArrayList;
import java.util.List;

/**
 * This class returns list of approved document indicator value pairs.
 */
public class InstalledModulesValuesFinder extends KeyValuesBase {

    /*
     * @see org.kuali.keyvalues.KeyValuesFinder#getKeyValues()
     */
    @Override
	public List<KeyValue> getKeyValues() {
    	List<KeyValue> keyValues = new ArrayList<KeyValue>();
        KualiModuleService kms = KRADServiceLocatorWeb.getKualiModuleService();
        for ( ModuleService moduleService : kms.getInstalledModuleServices() ) {
            keyValues.add(new ConcreteKeyValue(moduleService.getModuleConfiguration().getNamespaceCode(),
            		moduleService.getModuleConfiguration().getNamespaceCode() + " - " +
            		kms.getNamespaceName(moduleService.getModuleConfiguration().getNamespaceCode())));
        }

        return keyValues;
    }

}
