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

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.kuali.rice.core.util.BufferedLogger.debug;

import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.springframework.ldap.core.DirContextOperations;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EntityPhoneMapper extends BaseMapper<EntityPhone> {

	@Override
    EntityPhone mapDtoFromContext(DirContextOperations context) {
        return mapDtoFromContext(context, true);
    }
    
    EntityPhone mapDtoFromContext(DirContextOperations context, boolean isdefault) {
    	EntityPhone.Builder builder = mapBuilderFromContext(context, isdefault);
        return builder != null ? builder.build() : null;
    }

    EntityPhone.Builder mapBuilderFromContext(DirContextOperations context) {
        return mapBuilderFromContext(context, true);
    }

    EntityPhone.Builder mapBuilderFromContext(DirContextOperations context, boolean isdefault) {        
        final EntityPhone.Builder builder = EntityPhone.Builder.create();
        debug("Looking up attribute from context ", getConstants().getEmployeePhoneLdapProperty());
        final String pn = context.getStringAttribute(getConstants().getEmployeePhoneLdapProperty());
        
        if (isBlank(pn) || equalsIgnoreCase("NA", pn)) {
            debug("Got nothing. Giving nothing back.");
            return null;
        }
        
        String phoneNumber = pn;
        if (pn.length() >= 10) {
            phoneNumber = pn.substring(0, 3) + "-" + pn.substring(3, 6) + "-" + pn.substring(6);
        } else if (pn.length() >= 6) {
                    phoneNumber = pn.substring(0, 3) + "-" + pn.substring(3);
        }
        final String countryCode = getConstants().getDefaultCountryCode();
        
        builder.setCountryCode(countryCode);
        builder.setPhoneNumber(phoneNumber);
        builder.setPhoneType(CodedAttribute.Builder.create("WORK"));
        builder.setActive(true);
        builder.setDefaultValue(isdefault);

        return builder;
    }

}
