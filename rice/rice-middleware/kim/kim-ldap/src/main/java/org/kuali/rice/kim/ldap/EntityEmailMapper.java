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
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.springframework.ldap.core.DirContextOperations;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EntityEmailMapper extends BaseMapper<EntityEmail> {

	@Override
    EntityEmail mapDtoFromContext(DirContextOperations context) {
    	EntityEmail.Builder builder = mapBuilderFromContext(context);
        return builder != null ? builder.build() : null;
    }

    EntityEmail.Builder mapBuilderFromContext(DirContextOperations context) {        
        return mapBuilderFromContext(context, true);
    }

    EntityEmail.Builder mapBuilderFromContext(DirContextOperations context, boolean isdefault) {        
        final EntityEmail.Builder retval = EntityEmail.Builder.create();
        final String emailAddress = context.getStringAttribute(getConstants().getEmployeeMailLdapProperty());
        retval.setEmailAddress(emailAddress);
        retval.setEmailType(CodedAttribute.Builder.create("WORK"));
        retval.setDefaultValue(isdefault);
        retval.setActive(true);
        return retval;
    }
    
}
