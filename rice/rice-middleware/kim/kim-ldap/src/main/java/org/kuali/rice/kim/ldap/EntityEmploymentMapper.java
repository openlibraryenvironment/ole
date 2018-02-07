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

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.springframework.ldap.core.DirContextOperations;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EntityEmploymentMapper extends BaseMapper<EntityEmployment> {

    @Override
    EntityEmployment mapDtoFromContext(DirContextOperations context) {
    	EntityEmployment.Builder builder = mapBuilderFromContext(context);
        return builder != null ? builder.build() : null;
    }

    EntityEmployment.Builder mapBuilderFromContext(DirContextOperations context) {
        final String departmentCode = context.getStringAttribute(getConstants().getDepartmentLdapProperty());
        
        if (departmentCode == null) {
            return null;
        }

        final EntityEmployment.Builder employee = EntityEmployment.Builder.create();
        employee.setId(context.getStringAttribute(getConstants().getEmployeeIdProperty()));
        employee.setEmployeeStatus(
                CodedAttribute.Builder.create(context.getStringAttribute(getConstants().getEmployeeStatusProperty())));
        //employee.setEmployeeTypeCode(context.getStringAttribute(getConstants().getEmployeeTypeProperty()));
        employee.setEmployeeType(CodedAttribute.Builder.create("P"));
        employee.setBaseSalaryAmount(KualiDecimal.ZERO);
        
        employee.setActive(true);
        return employee;
    }
    
}
