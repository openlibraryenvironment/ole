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

import java.util.ArrayList;

import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoDefault;
import org.springframework.ldap.core.DirContextOperations;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EntityDefaultMapper extends BaseMapper<EntityDefault> {

	private EntityAffiliationMapper affiliationMapper;
    private EntityTypeContactInfoDefaultMapper entityTypeContactInfoDefaultMapper;
    private EntityNameMapper defaultNameMapper;
    private EntityEmploymentMapper employmentMapper;
    
    @Override
    EntityDefault mapDtoFromContext(DirContextOperations context) {
    	EntityDefault.Builder builder = mapBuilderFromContext(context);
        return builder != null ? builder.build() : null;
    }
    
    EntityDefault.Builder mapBuilderFromContext(DirContextOperations context) {
        
        final String entityId = context.getStringAttribute(getConstants().getKimLdapIdProperty());
        final String principalName = context.getStringAttribute(getConstants().getKimLdapNameProperty());

        final EntityDefault.Builder person = EntityDefault.Builder.create(entityId);
        
        if (entityId == null) {
            throw new InvalidLdapEntityException("LDAP Search Results yielded an invalid result with attributes " 
                                                 + context.getAttributes());
        }
        
        person.setAffiliations(new ArrayList<EntityAffiliation.Builder>());
        person.setExternalIdentifiers(new ArrayList<EntityExternalIdentifier.Builder>());
        
        final EntityExternalIdentifier.Builder externalId = EntityExternalIdentifier.Builder.create();
        externalId.setExternalIdentifierTypeCode(getConstants().getTaxExternalIdTypeCode());
        externalId.setExternalId(entityId);
        person.getExternalIdentifiers().add(externalId);
        
        person.setAffiliations(getAffiliationMapper().mapBuilderFromContext(context));
        
        person.setEntityTypeContactInfos(new ArrayList<EntityTypeContactInfoDefault.Builder>());
        person.getEntityTypeContactInfos().add(getEntityTypeContactInfoDefaultMapper().mapBuilderFromContext(context));
        
        person.setName(getDefaultNameMapper().mapBuilderFromContext(context));
        person.setEntityId(entityId);
        
        person.setEmployment(getEmploymentMapper().mapBuilderFromContext(context));

        person.setEntityId(entityId);
        person.setPrincipals(new ArrayList<Principal.Builder>()); 
        //inactivate unless we find a matching affiliation
        person.setActive(true);
        
        final Principal.Builder defaultPrincipal = Principal.Builder.create(principalName);
        defaultPrincipal.setPrincipalId(entityId);
        defaultPrincipal.setEntityId(entityId);

        person.getPrincipals().add(defaultPrincipal);
        
        return person;
    }
    
    /**
     * Gets the value of affiliationMapper
     *
     * @return the value of affiliationMapper
     */
    public final EntityAffiliationMapper getAffiliationMapper() {
        return this.affiliationMapper;
    }

    /**
     * Sets the value of affiliationMapper
     *
     * @param argAffiliationMapper Value to assign to this.affiliationMapper
     */
    public final void setAffiliationMapper(final EntityAffiliationMapper argAffiliationMapper) {
        this.affiliationMapper = argAffiliationMapper;
    }

    /**
     * Gets the value of entityTypeMapper
     *
     * @return the value of entityTypeMapper
     */
    public final EntityTypeContactInfoDefaultMapper getEntityTypeContactInfoDefaultMapper() {
        return this.entityTypeContactInfoDefaultMapper;
    }

    /**
     * Sets the value of entityTypeMapper
     *
     * @param argEntityTypeMapper Value to assign to this.entityTypeMapper
     */
    public final void setEntityTypeContactInfoDefaultMapper(final EntityTypeContactInfoDefaultMapper argEntityTypeMapper) {
        this.entityTypeContactInfoDefaultMapper = argEntityTypeMapper;
    }

    /**
     * Gets the value of defaultNameMapper
     *
     * @return the value of defaultNameMapper
     */
    public final EntityNameMapper getDefaultNameMapper() {
        return this.defaultNameMapper;
    }

    /**
     * Sets the value of defaultNameMapper
     *
     * @param argDefaultNameMapper Value to assign to this.defaultNameMapper
     */
    public final void setDefaultNameMapper(final EntityNameMapper argDefaultNameMapper) {
        this.defaultNameMapper = argDefaultNameMapper;
    }

    /**
     * Gets the value of employmentMapper
     *
     * @return the value of employmentMapper
     */
    public final EntityEmploymentMapper getEmploymentMapper() {
        return this.employmentMapper;
    }

    /**
     * Sets the value of employmentMapper
     *
     * @param argEmploymentMapper Value to assign to this.employmentMapper
     */
    public final void setEmploymentMapper(final EntityEmploymentMapper argEmploymentMapper) {
        this.employmentMapper = argEmploymentMapper;
    }

}