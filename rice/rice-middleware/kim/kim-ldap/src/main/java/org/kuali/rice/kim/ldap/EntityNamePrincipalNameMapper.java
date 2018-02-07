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

import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EntityNamePrincipalNameMapper extends BaseMapper<EntityNamePrincipalName> {

    private ContextMapper defaultNameMapper;
    
    @Override
    EntityNamePrincipalName mapDtoFromContext(DirContextOperations context) {
        EntityNamePrincipalName.Builder builder = mapBuilderFromContext(context);
        return builder != null ? builder.build() : null;
    }
    
    EntityNamePrincipalName.Builder mapBuilderFromContext(DirContextOperations context) {
        final EntityNamePrincipalName.Builder person = EntityNamePrincipalName.Builder.create();
        person.setDefaultName((EntityName.Builder) getDefaultNameMapper().mapFromContext(context));
        person.setPrincipalName(context.getStringAttribute(getConstants().getKimLdapNameProperty()));
        return person;
    }

    /**
     * Gets the value of defaultNameMapper
     *
     * @return the value of defaultNameMapper
     */
    public final ContextMapper getDefaultNameMapper() {
        return this.defaultNameMapper;
    }

    /**
     * Sets the value of defaultNameMapper
     *
     * @param argDefaultNameMapper Value to assign to this.defaultNameMapper
     */
    public final void setDefaultNameMapper(final ContextMapper argDefaultNameMapper) {
        this.defaultNameMapper = argDefaultNameMapper;
    }
}