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
package org.kuali.rice.kim.api.permission;


import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.kim.api.common.template.TemplateContract;

import java.util.Map;

/**
 * This is the contract for a Permission. Represents a single permission within the system.
 * 
 * Permissions are attached to roles. All authorization checks should be done against permissions,
 * never against roles or groups.
 *  
 */
public interface PermissionContract extends Versioned, GloballyUnique, Inactivatable, Identifiable {
    
    /**
     * The namespace code that this Permission belongs too.
     *
     * @return namespaceCode
     */
    String getNamespaceCode();
    
    /**
     * The name of the Permission.
     *
     * @return name
     */
    String getName();
    
    /**
     * The description of the Permission.
     *
     * @return description
     */
	String getDescription();

    /**
     * The Template referenced by the Permission.
     *
     * @return templateId
     */
	TemplateContract getTemplate();
	
   /** 
	 * Attributes for a Permission.
	 * 
	 * @return attributes
	 */
    Map<String, String> getAttributes();
}
