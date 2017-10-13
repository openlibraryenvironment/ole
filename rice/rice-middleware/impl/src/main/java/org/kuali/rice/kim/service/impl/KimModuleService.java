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
package org.kuali.rice.kim.service.impl;

import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupContract;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationType;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierType;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleContract;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeContract;
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
import org.kuali.rice.kim.util.KimCommonUtilsInternal;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.service.impl.ModuleServiceBase;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KimModuleService extends ModuleServiceBase {

	private PersonService personService;
	private RoleService kimRoleService;
	private GroupService groupService;
    private IdentityService identityService;
	private KimTypeInfoService kimTypeInfoService;

	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.krad.service.impl.ModuleServiceBase#getExternalizableBusinessObject(java.lang.Class, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
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
				Role role = getKimRoleService().getRole((String)fieldValues.get(KimConstants.PrimaryKeyConstants.ROLE_ID));
				return (T) RoleBo.from(role);
			}
		} else if(GroupContract.class.isAssignableFrom(businessObjectClass)){
			if(fieldValues.containsKey(KimConstants.PrimaryKeyConstants.GROUP_ID)) {
                Group group = getGroupService().getGroup((String)fieldValues.get(KimConstants.PrimaryKeyConstants.GROUP_ID));
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
		// otherwise, use the default implementation
		return super.getExternalizableBusinessObject( businessObjectClass, fieldValues );
	}

	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.krad.service.impl.ModuleServiceBase#getExternalizableBusinessObjectsList(java.lang.Class, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ExternalizableBusinessObject> List<T> getExternalizableBusinessObjectsList(
			Class<T> externalizableBusinessObjectClass, Map<String, Object> fieldValues) {
		// for Person objects (which are not real PersistableBOs) pull them through the person service

		if ( Person.class.isAssignableFrom( externalizableBusinessObjectClass ) ) {
			return (List)getPersonService().findPeople( (Map)fieldValues );
		}
        else if ( RoleContract.class.isAssignableFrom( externalizableBusinessObjectClass ) ) {
            List<Role> roles = getKimRoleService().findRoles(toQuery(fieldValues)).getResults();
            List<RoleEbo> roleEbos = new ArrayList<RoleEbo>(roles.size());
            for (Role role : roles) {
                roleEbos.add(RoleBo.from(role));
            }
            return (List<T>)roleEbos;
		} else if ( GroupContract.class.isAssignableFrom(externalizableBusinessObjectClass) ) {
			List<Group> groups = getGroupService().findGroups(toQuery(fieldValues)).getResults();
            List<GroupEbo> groupEbos = new ArrayList<GroupEbo>(groups.size());
            for (Group group : groups) {
                groupEbos.add(GroupBo.from(group));
            }
            return (List<T>)groupEbos;
		}
		// otherwise, use the default implementation
		return super.getExternalizableBusinessObjectsList( externalizableBusinessObjectClass, fieldValues );
	}

    private QueryByCriteria toQuery(Map<String,?> fieldValues) {
        Set<Predicate> preds = new HashSet<Predicate>();
        for (String key : fieldValues.keySet()) {
            preds.add(equal(key, fieldValues.get(key)));
        }
        Predicate[] predicates = new Predicate[0];
        predicates = preds.toArray(predicates);
        return QueryByCriteria.Builder.fromPredicates(predicates);
    }

	/***
	 * @see org.kuali.rice.krad.service.ModuleService#getExternalizableBusinessObjectsListForLookup(java.lang.Class, java.util.Map, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ExternalizableBusinessObject> List<T> getExternalizableBusinessObjectsListForLookup(
			Class<T> externalizableBusinessObjectClass, Map<String, Object> fieldValues, boolean unbounded) {
		// for Person objects (which are not real PersistableBOs) pull them through the person service
		if ( Person.class.isAssignableFrom( externalizableBusinessObjectClass ) ) {
			return (List)getPersonService().findPeople( (Map)fieldValues, unbounded );
		} else if ( RoleContract.class.isAssignableFrom( externalizableBusinessObjectClass ) ) {
			List<Role> roles = getKimRoleService().findRoles(toQuery(fieldValues)).getResults();
            List<RoleEbo> roleEbos = new ArrayList<RoleEbo>(roles.size());
            for (Role role : roles) {
                roleEbos.add(RoleBo.from(role));
            }
            return (List<T>)roleEbos;
		} else if (GroupContract.class.isAssignableFrom( externalizableBusinessObjectClass)) {
            List<Group> groups = getGroupService().findGroups(toQuery(fieldValues)).getResults();
            List<GroupEbo> groupEbos = new ArrayList<GroupEbo>(groups.size());
            for (Group group : groups) {
                groupEbos.add(GroupBo.from(group));
            }
            return (List<T>)groupEbos;
        }
		// otherwise, use the default implementation
		return super.getExternalizableBusinessObjectsListForLookup(externalizableBusinessObjectClass, fieldValues, unbounded);
	}

	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.krad.service.impl.ModuleServiceBase#listPrimaryKeyFieldNames(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List listPrimaryKeyFieldNames(Class businessObjectInterfaceClass) {
		// for Person objects (which are not real PersistableBOs) pull them through the person service
		if ( Person.class.isAssignableFrom( businessObjectInterfaceClass ) ) {
			return Collections.singletonList( KimConstants.PrimaryKeyConstants.PRINCIPAL_ID );
		} else if ( RoleEbo.class.isAssignableFrom( businessObjectInterfaceClass ) ) {
			return Collections.singletonList( KimConstants.PrimaryKeyConstants.ROLE_ID );
		} else if ( GroupEbo.class.isAssignableFrom( businessObjectInterfaceClass ) ) {
			return Collections.singletonList( KimConstants.PrimaryKeyConstants.GROUP_ID );
		} else if ( KimType.class.isAssignableFrom( businessObjectInterfaceClass ) ) {
			return Collections.singletonList( KimConstants.PrimaryKeyConstants.KIM_TYPE_ID );
		} else if ( KimTypeContract.class.isAssignableFrom(businessObjectInterfaceClass)) {
			return Collections.singletonList( KimConstants.PrimaryKeyConstants.KIM_TYPE_CODE );
		}

		// otherwise, use the default implementation
		return super.listPrimaryKeyFieldNames( businessObjectInterfaceClass );
	}

	@SuppressWarnings("unchecked")
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

	protected Properties getUrlParameters(String businessObjectClassAttribute, Map<String, String[]> parameters){
		Properties urlParameters = new Properties();
		for (String paramName : parameters.keySet()) {
			String[] parameterValues = parameters.get(paramName);
			if (parameterValues.length > 0) {
				urlParameters.put(paramName, parameterValues[0]);
			}
		}
		urlParameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, businessObjectClassAttribute);
		try{
			Class inquiryBusinessObjectClass = Class.forName(businessObjectClassAttribute);
			if(RoleContract.class.isAssignableFrom(inquiryBusinessObjectClass) ||
					GroupContract.class.isAssignableFrom(inquiryBusinessObjectClass) ||
					Person.class.isAssignableFrom(inquiryBusinessObjectClass)) {
		        urlParameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.PARAM_MAINTENANCE_VIEW_MODE_INQUIRY);
			} else{
		        urlParameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.CONTINUE_WITH_INQUIRY_METHOD_TO_CALL);
			}
		} catch(Exception eix){
			urlParameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.CONTINUE_WITH_INQUIRY_METHOD_TO_CALL);
		}
        urlParameters.put(KRADConstants.PARAMETER_COMMAND, KewApiConstants.INITIATE_COMMAND);
		return urlParameters;
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
	protected String getInquiryUrl(Class inquiryBusinessObjectClass){
		String inquiryUrl = KimCommonUtilsInternal.getKimBasePath();
		if (!inquiryUrl.endsWith("/")) {
			inquiryUrl = inquiryUrl + "/";
		}
		if(RoleContract.class.isAssignableFrom(inquiryBusinessObjectClass)) {
			return inquiryUrl + KimConstants.KimUIConstants.KIM_ROLE_INQUIRY_ACTION;
		} else if(GroupContract.class.isAssignableFrom(inquiryBusinessObjectClass)) {
			return inquiryUrl + KimConstants.KimUIConstants.KIM_GROUP_INQUIRY_ACTION;
		} else if(Person.class.isAssignableFrom(inquiryBusinessObjectClass)) {
			return inquiryUrl + KimConstants.KimUIConstants.KIM_PERSON_INQUIRY_ACTION;
		}
		return super.getInquiryUrl(inquiryBusinessObjectClass);
	}

    @Override
    public List<List<String>> listAlternatePrimaryKeyFieldNames(
            Class businessObjectInterfaceClass) {
		if ( Person.class.isAssignableFrom( businessObjectInterfaceClass ) ) {
            ArrayList<List<String>> retList = new ArrayList<List<String>>();
            ArrayList<String> keyList = new ArrayList<String>();

            keyList.add("principalName");
            retList.add(keyList);
            return retList;
        }else{
            return null;
        }

    }
    
    @Override
    public boolean goToCentralRiceForInquiry() {
        RunMode runMode = getRunMode(KimApiConstants.Namespaces.MODULE_NAME);

        if (RunMode.EMBEDDED.equals(runMode)) {
            return true;
        } else {
            return false;
        }
    }
}
