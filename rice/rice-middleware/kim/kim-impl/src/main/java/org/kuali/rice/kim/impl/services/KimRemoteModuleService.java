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
package org.kuali.rice.kim.impl.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateUtils;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupContract;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.CodedAttributeContract;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationType;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierType;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleContract;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimTypeInfoService;
import org.kuali.rice.kim.framework.group.GroupEbo;
import org.kuali.rice.kim.framework.identity.EntityTypeEbo;
import org.kuali.rice.kim.framework.identity.address.EntityAddressTypeEbo;
import org.kuali.rice.kim.framework.identity.affiliation.EntityAffiliationTypeEbo;
import org.kuali.rice.kim.framework.identity.citizenship.EntityCitizenshipStatusEbo;
import org.kuali.rice.kim.framework.identity.email.EntityEmailTypeEbo;
import org.kuali.rice.kim.framework.identity.employment.EntityEmploymentStatusEbo;
import org.kuali.rice.kim.framework.identity.employment.EntityEmploymentTypeEbo;
import org.kuali.rice.kim.framework.identity.external.EntityExternalIdentifierTypeEbo;
import org.kuali.rice.kim.framework.identity.name.EntityNameTypeEbo;
import org.kuali.rice.kim.framework.identity.phone.EntityPhoneTypeEbo;
import org.kuali.rice.kim.framework.role.RoleEbo;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.identity.EntityTypeBo;
import org.kuali.rice.kim.impl.identity.address.EntityAddressTypeBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationTypeBo;
import org.kuali.rice.kim.impl.identity.citizenship.EntityCitizenshipStatusBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailTypeBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentStatusBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentTypeBo;
import org.kuali.rice.kim.impl.identity.external.EntityExternalIdentifierTypeBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameTypeBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneTypeBo;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.service.impl.RemoteModuleServiceBase;

public class KimRemoteModuleService extends RemoteModuleServiceBase {

    private PersonService personService;
    private RoleService kimRoleService;
    private GroupService groupService;
    private IdentityService identityService;
    private KimTypeInfoService kimTypeInfoService;

    public <T extends ExternalizableBusinessObject> T getExternalizableBusinessObject(Class<T> businessObjectClass, Map<String, Object> fieldValues) {
        if ( Person.class.isAssignableFrom( businessObjectClass ) ) {
            if ( fieldValues.containsKey( KIMPropertyConstants.Person.PRINCIPAL_ID ) ) {
                return (T) getPersonService().getPerson( (String)fieldValues.get( KIMPropertyConstants.Person.PRINCIPAL_ID ) );
            } else if ( fieldValues.containsKey( KIMPropertyConstants.Person.PRINCIPAL_NAME ) ) {
                return (T) getPersonService().getPersonByPrincipalName( (String)fieldValues.get( KIMPropertyConstants.Person.PRINCIPAL_NAME ) );
            }
            // otherwise, fall through since critieria is not known
        } else if(RoleContract.class.isAssignableFrom(businessObjectClass)){
            if(fieldValues.containsKey(KimConstants.PrimaryKeyConstants.ROLE_ID)){
                Role role = getKimRoleService().getRole((String) fieldValues.get(
                        KimConstants.PrimaryKeyConstants.ROLE_ID));
                return (T) RoleBo.from(role);
            }
        } else if(GroupContract.class.isAssignableFrom(businessObjectClass)){
            if(fieldValues.containsKey(KimConstants.PrimaryKeyConstants.GROUP_ID)) {
                Group group = getGroupService().getGroup((String) fieldValues.get(
                        KimConstants.PrimaryKeyConstants.GROUP_ID));
                return (T) GroupBo.from(group);
            }
        } else if (EntityEmailTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            if (fieldValues.containsKey(KimConstants.PrimaryKeyConstants.CODE)) {
                CodedAttribute codedAttribute = getIdentityService()
                        .getEmailType((String) fieldValues.get(KimConstants.PrimaryKeyConstants.CODE));
                return (T)EntityEmailTypeBo.from(codedAttribute);
            }
        } else if (EntityAddressTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            if (fieldValues.containsKey(KimConstants.PrimaryKeyConstants.CODE)) {
                CodedAttribute codedAttribute = getIdentityService()
                        .getAddressType((String) fieldValues.get(KimConstants.PrimaryKeyConstants.CODE));
                return (T)EntityAddressTypeBo.from(codedAttribute);
            }
        } else if (EntityAffiliationTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            if (fieldValues.containsKey(KimConstants.PrimaryKeyConstants.CODE)) {
                EntityAffiliationType codedAttribute = getIdentityService()
                        .getAffiliationType((String) fieldValues.get(KimConstants.PrimaryKeyConstants.CODE));
                return (T)EntityAffiliationTypeBo.from(codedAttribute);
            }
        } else if (EntityCitizenshipStatusEbo.class.isAssignableFrom(businessObjectClass)) {
            if (fieldValues.containsKey(KimConstants.PrimaryKeyConstants.CODE)) {
                CodedAttribute codedAttribute = getIdentityService()
                        .getCitizenshipStatus((String) fieldValues.get(KimConstants.PrimaryKeyConstants.CODE));
                return (T)EntityCitizenshipStatusBo.from(codedAttribute);
            }
        } else if (EntityEmploymentStatusEbo.class.isAssignableFrom(businessObjectClass)) {
            if (fieldValues.containsKey(KimConstants.PrimaryKeyConstants.CODE)) {
                CodedAttribute codedAttribute = getIdentityService()
                        .getEmploymentStatus((String) fieldValues.get(KimConstants.PrimaryKeyConstants.CODE));
                return (T)EntityEmploymentStatusBo.from(codedAttribute);
            }
        }  else if (EntityEmploymentTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            if (fieldValues.containsKey(KimConstants.PrimaryKeyConstants.CODE)) {
                CodedAttribute codedAttribute = getIdentityService()
                        .getEmploymentType((String) fieldValues.get(KimConstants.PrimaryKeyConstants.CODE));
                return (T)EntityEmploymentTypeBo.from(codedAttribute);
            }
        } else if (EntityNameTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            if (fieldValues.containsKey(KimConstants.PrimaryKeyConstants.CODE)) {
                CodedAttribute codedAttribute = getIdentityService()
                        .getNameType((String) fieldValues.get(KimConstants.PrimaryKeyConstants.CODE));
                return (T)EntityNameTypeBo.from(codedAttribute);
            }
        } else if (EntityTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            if (fieldValues.containsKey(KimConstants.PrimaryKeyConstants.CODE)) {
                CodedAttribute codedAttribute = getIdentityService()
                        .getEntityType((String) fieldValues.get(KimConstants.PrimaryKeyConstants.CODE));
                return (T)EntityTypeBo.from(codedAttribute);
            }
        } else if (EntityExternalIdentifierTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            if (fieldValues.containsKey(KimConstants.PrimaryKeyConstants.CODE)) {
                EntityExternalIdentifierType codedAttribute = getIdentityService()
                        .getExternalIdentifierType((String) fieldValues.get(KimConstants.PrimaryKeyConstants.CODE));
                return (T)EntityExternalIdentifierTypeBo.from(codedAttribute);
            }
        } else if (EntityPhoneTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            if (fieldValues.containsKey(KimConstants.PrimaryKeyConstants.CODE)) {
                CodedAttribute codedAttribute = getIdentityService()
                        .getPhoneType((String) fieldValues.get(KimConstants.PrimaryKeyConstants.CODE));
                return (T)EntityPhoneTypeBo.from(codedAttribute);
            }
        }
        return null;
    }

    @Override
    public <T extends ExternalizableBusinessObject> List<T> getExternalizableBusinessObjectsList(
            Class<T> businessObjectClass, Map<String, Object> fieldValues) {
        //convert fieldValues to Query
        QueryByCriteria.Builder queryBuilder = QueryByCriteria.Builder.create();
        Predicate predicate = PredicateUtils.convertObjectMapToPredicate(fieldValues);
        queryBuilder.setPredicates(predicate);
        
        return this.queryForEbos(businessObjectClass, queryBuilder.build(), fieldValues);
    }

    @Override
    public boolean isExternalizableBusinessObjectLookupable(Class boClass) {
        return isExternalizable(boClass);
    }

    @Override
    public boolean isExternalizableBusinessObjectInquirable(Class boClass) {
        return isExternalizable(boClass);
    }

    @Override
    public boolean isExternalizable(Class boClazz) {
        if (boClazz == null) {
            return false;
        }
        if(RoleContract.class.isAssignableFrom(boClazz)) {
            return true;
        } else if(GroupContract.class.isAssignableFrom(boClazz)) {
            return true;
        } else if(Person.class.isAssignableFrom(boClazz)) {
            return true;
        }
        return ExternalizableBusinessObject.class.isAssignableFrom(boClazz);
    }

    @Override
    public List<String> listPrimaryKeyFieldNames(Class boClass) {

        //TODO:  I strongly dislike hard-coding these values, but have this here because the OJB stuff
        //TODO: isn't available when loaded in REMOTE mode...
        if(GroupContract.class.isAssignableFrom(boClass)
                || RoleContract.class.isAssignableFrom(boClass)){
            return Collections.singletonList(KimConstants.PrimaryKeyConstants.ID);
        } else if (Person.class.isAssignableFrom(boClass)) {
            return Collections.singletonList(KimConstants.PrimaryKeyConstants.PRINCIPAL_ID);
        } else if (CodedAttributeContract.class.isAssignableFrom(boClass)) {
            return Collections.singletonList(KimConstants.PrimaryKeyConstants.CODE);
        }
        return Collections.emptyList();
    }

    private <T extends ExternalizableBusinessObject> List<T> queryForEbos(
            Class<T> businessObjectClass, QueryByCriteria query, Map<String, Object> fieldValues) {
        if ( Person.class.isAssignableFrom( businessObjectClass ) ) {
            return (List)getPersonService().findPeople( (Map)fieldValues );
        }
        else if ( RoleContract.class.isAssignableFrom( businessObjectClass ) ) {
            List<Role> roles = getKimRoleService().findRoles(query).getResults();
            List<RoleEbo> roleEbos = new ArrayList<RoleEbo>(roles.size());
            for (Role role : roles) {
                roleEbos.add(RoleBo.from(role));
            }
            return (List<T>)roleEbos;
        } else if ( GroupContract.class.isAssignableFrom(businessObjectClass) ) {
            List<Group> groups = getGroupService().findGroups(query).getResults();
            List<GroupEbo> groupEbos = new ArrayList<GroupEbo>(groups.size());
            for (Group group : groups) {
                groupEbos.add(GroupBo.from(group));
            }
            return (List<T>)groupEbos;
        } else if (EntityEmailTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            List<CodedAttribute> codedAttributes = getIdentityService().findAllEmailTypes();
            List<EntityEmailTypeEbo> ebos = new ArrayList<EntityEmailTypeEbo>();
            for(CodedAttribute attr : codedAttributes) {
                ebos.add(EntityEmailTypeBo.from(attr));
            }
            return (List<T>)ebos;
        } else if (EntityAddressTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            List<CodedAttribute> codedAttributes = getIdentityService().findAllAddressTypes();
            List<EntityAddressTypeEbo> ebos = new ArrayList<EntityAddressTypeEbo>();
            for(CodedAttribute attr : codedAttributes) {
                ebos.add(EntityAddressTypeBo.from(attr));
            }
            return (List<T>)ebos;
        } else if (EntityAffiliationTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            List<EntityAffiliationType> codedAttributes = getIdentityService().findAllAffiliationTypes();
            List<EntityAffiliationTypeEbo> ebos = new ArrayList<EntityAffiliationTypeEbo>();
            for(EntityAffiliationType attr : codedAttributes) {
                ebos.add(EntityAffiliationTypeBo.from(attr));
            }
            return (List<T>)ebos;
        } else if (EntityCitizenshipStatusEbo.class.isAssignableFrom(businessObjectClass)) {
            List<CodedAttribute> codedAttributes = getIdentityService().findAllCitizenshipStatuses();
            List<EntityCitizenshipStatusEbo> ebos = new ArrayList<EntityCitizenshipStatusEbo>();
            for(CodedAttribute attr : codedAttributes) {
                ebos.add(EntityCitizenshipStatusBo.from(attr));
            }
            return (List<T>)ebos;
        } else if (EntityEmploymentStatusEbo.class.isAssignableFrom(businessObjectClass)) {
            List<CodedAttribute> codedAttributes = getIdentityService().findAllEmploymentStatuses();
            List<EntityEmploymentStatusEbo> ebos = new ArrayList<EntityEmploymentStatusEbo>();
            for(CodedAttribute attr : codedAttributes) {
                ebos.add(EntityEmploymentStatusBo.from(attr));
            }
            return (List<T>)ebos;
        }  else if (EntityEmploymentTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            List<CodedAttribute> codedAttributes = getIdentityService().findAllEmploymentTypes();
            List<EntityEmploymentTypeEbo> ebos = new ArrayList<EntityEmploymentTypeEbo>();
            for(CodedAttribute attr : codedAttributes) {
                ebos.add(EntityEmploymentTypeBo.from(attr));
            }
            return (List<T>)ebos;
        } else if (EntityNameTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            List<CodedAttribute> codedAttributes = getIdentityService().findAllNameTypes();
            List<EntityNameTypeEbo> ebos = new ArrayList<EntityNameTypeEbo>();
            for(CodedAttribute attr : codedAttributes) {
                ebos.add(EntityNameTypeBo.from(attr));
            }
            return (List<T>)ebos;
        } else if (EntityTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            List<CodedAttribute> codedAttributes = getIdentityService().findAllEntityTypes();
            List<EntityTypeEbo> ebos = new ArrayList<EntityTypeEbo>();
            for(CodedAttribute attr : codedAttributes) {
                ebos.add(EntityTypeBo.from(attr));
            }
            return (List<T>)ebos;
        } else if (EntityExternalIdentifierTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            List<EntityExternalIdentifierType> codedAttributes = getIdentityService().findAllExternalIdendtifierTypes();
            List<EntityExternalIdentifierTypeEbo> ebos = new ArrayList<EntityExternalIdentifierTypeEbo>();
            for(EntityExternalIdentifierType attr : codedAttributes) {
                ebos.add(EntityExternalIdentifierTypeBo.from(attr));
            }
            return (List<T>)ebos;
        } else if (EntityPhoneTypeEbo.class.isAssignableFrom(businessObjectClass)) {
            List<CodedAttribute> codedAttributes = getIdentityService().findAllPhoneTypes();
            List<EntityPhoneTypeEbo> ebos = new ArrayList<EntityPhoneTypeEbo>();
            for(CodedAttribute attr : codedAttributes) {
                ebos.add(EntityPhoneTypeBo.from(attr));
            }
            return (List<T>)ebos;
        }
        return Collections.emptyList();

    }

    
    protected PersonService getPersonService() {
        if ( personService == null ) {
            personService = KimApiServiceLocator.getPersonService();
        }
        return personService;
    }

    protected RoleService getKimRoleService() {
        if ( kimRoleService == null ) {
            kimRoleService = KimApiServiceLocator.getRoleService();
        }
        return kimRoleService;
    }

    protected GroupService getGroupService() {
        if ( groupService == null ) {
            groupService = KimApiServiceLocator.getGroupService();
        }
        return groupService;
    }

    protected IdentityService getIdentityService() {
        if ( identityService == null ) {
            identityService = KimApiServiceLocator.getIdentityService();
        }
        return identityService;
    }

    protected KimTypeInfoService getTypeInfoService() {
        if(kimTypeInfoService == null){
            kimTypeInfoService = KimApiServiceLocator.getKimTypeInfoService();
        }
        return kimTypeInfoService;
    }
}
