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
package org.kuali.rice.kim.ldap;

import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.springframework.ldap.core.DirContextOperations;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EntityAddressMapper extends BaseMapper<EntityAddress> {
	
	@Override
	EntityAddress mapDtoFromContext(DirContextOperations context) {
		return mapDtoFromContext(context, false);
	}
	
    EntityAddress mapDtoFromContext(DirContextOperations context, boolean isdefault) {
    	EntityAddress.Builder builder = mapBuilderFromContext(context, isdefault);
        return builder != null ? builder.build() : null;
    }

    EntityAddress.Builder mapBuilderFromContext(DirContextOperations context) {
        return mapBuilderFromContext(context, false);
    }

    EntityAddress.Builder mapBuilderFromContext(DirContextOperations context, boolean isdefault) {        
        final EntityAddress.Builder builder = EntityAddress.Builder.create();
        final String line1              = context.getStringAttribute("employeePrimaryDeptName");
        final String line2              = context.getStringAttribute("employeePoBox");
        final String city               = context.getStringAttribute("employeeCity");
        final String stateProvinceCode  = context.getStringAttribute("employeeState");
        final String postalCode         = context.getStringAttribute("employeeZip");
        
        builder.setAddressType(CodedAttribute.Builder.create("WORK"));
        builder.setLine1(line1);
        builder.setLine2(line2);
        builder.setCity(city);
        builder.setStateProvinceCode(stateProvinceCode);
        builder.setPostalCode(postalCode);
        builder.setDefaultValue(isdefault);
        builder.setActive(true);
        return builder;
    }
    
}
