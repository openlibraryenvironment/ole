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
package org.kuali.rice.kim.impl.permission;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.cache.CacheKeyUtils;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.LookupCustomizer;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.common.assignee.Assignee;
import org.kuali.rice.kim.api.common.delegate.DelegateType;
import org.kuali.rice.kim.api.common.template.Template;
import org.kuali.rice.kim.api.common.template.TemplateQueryResults;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.permission.PermissionQueryResults;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.role.RoleMembership;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeInfoService;
import org.kuali.rice.kim.framework.permission.PermissionTypeService;
import org.kuali.rice.kim.impl.common.attribute.AttributeTransform;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeDataBo;
import org.kuali.rice.kim.impl.role.RolePermissionBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;
import static org.kuali.rice.core.api.criteria.PredicateFactory.in;

public class PermissionServiceImpl implements PermissionService {
    private static final Logger LOG = Logger.getLogger( PermissionServiceImpl.class );

	private RoleService roleService;
    private PermissionTypeService defaultPermissionTypeService;
    private KimTypeInfoService kimTypeInfoService;
	private BusinessObjectService businessObjectService;
	private CriteriaLookupService criteriaLookupService;
    private CacheManager cacheManager;

 	private final CopyOnWriteArrayList<Template> allTemplates = new CopyOnWriteArrayList<Template>();

    // --------------------
    // Authorization Checks
    // --------------------

    public PermissionServiceImpl() {
        this.cacheManager = new NoOpCacheManager();
    }

    protected PermissionTypeService getPermissionTypeService(Template permissionTemplate) {
    	if ( permissionTemplate == null ) {
    		throw new IllegalArgumentException( "permissionTemplate may not be null" );
    	}
    	KimType kimType = kimTypeInfoService.getKimType( permissionTemplate.getKimTypeId() );
    	String serviceName = kimType.getServiceName();
    	// if no service specified, return a default implementation
    	if ( StringUtils.isBlank( serviceName ) ) {
    		return defaultPermissionTypeService;
    	}
    	try {
	    	Object service = GlobalResourceLoader.getService(QName.valueOf(serviceName));
	    	// if we have a service name, it must exist
	    	if ( service == null ) {
				throw new RuntimeException("null returned for permission type service for service name: " + serviceName);
	    	}
	    	// whatever we retrieved must be of the correct type
	    	if ( !(service instanceof PermissionTypeService)  ) {
	    		throw new RuntimeException( "Service " + serviceName + " was not a PermissionTypeService.  Was: " + service.getClass().getName() );
	    	}
	    	return (PermissionTypeService)service;
    	} catch( Exception ex ) {
    		// sometimes service locators throw exceptions rather than returning null, handle that
    		throw new RuntimeException( "Error retrieving service: " + serviceName + " from the KimImplServiceLocator.", ex );
    	}
    }

    @Override
    public boolean hasPermission(String principalId, String namespaceCode,
                                 String permissionName) throws RiceIllegalArgumentException  {
        incomingParamCheck(principalId, "principalId");
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionName, "permissionName");

        return isAuthorized( principalId, namespaceCode, permissionName, Collections.<String, String>emptyMap() );
    }

    @Override
    public boolean isAuthorized(String principalId, String namespaceCode,
                                String permissionName, Map<String, String> qualification ) throws RiceIllegalArgumentException {
        incomingParamCheck(principalId, "principalId");
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionName, "permissionName");
        incomingParamCheck(qualification, "qualification");

        if ( LOG.isDebugEnabled() ) {
            logAuthorizationCheck("Permission", principalId, namespaceCode, permissionName, qualification);
        }

        List<String> roleIds = getRoleIdsForPermission(namespaceCode, permissionName);
    	if ( roleIds.isEmpty() ) {
    		if ( LOG.isDebugEnabled() ) {
    			LOG.debug( "Result: false");
    		}
    		return false;
    	}

    	boolean isAuthorized = roleService.principalHasRole(principalId, roleIds, qualification);
        
        if ( LOG.isDebugEnabled() ) {
            LOG.debug( "Result: " + isAuthorized );
        }
        return isAuthorized;

    }
    @Override
    public boolean hasPermissionByTemplate(String principalId, String namespaceCode, String permissionTemplateName,
            Map<String, String> permissionDetails) throws RiceIllegalArgumentException {
        incomingParamCheck(principalId, "principalId");
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionTemplateName, "permissionTemplateName");

        return isAuthorizedByTemplate(principalId, namespaceCode, permissionTemplateName, permissionDetails,
                Collections.<String, String>emptyMap());
    }
    @Override
    public boolean isAuthorizedByTemplate(String principalId, String namespaceCode, String permissionTemplateName,
            Map<String, String> permissionDetails, Map<String, String> qualification) throws RiceIllegalArgumentException {
        incomingParamCheck(principalId, "principalId");
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionTemplateName, "permissionTemplateName");
        incomingParamCheck(qualification, "qualification");

        if ( LOG.isDebugEnabled() ) {
            logAuthorizationCheckByTemplate("Perm Templ", principalId, namespaceCode, permissionTemplateName, permissionDetails, qualification);
        }

        List<String> roleIds = getRoleIdsForPermissionTemplate(namespaceCode, permissionTemplateName, permissionDetails);
    	if (roleIds.isEmpty()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Result: false");
            }
    		return false;
    	}
        boolean isAuthorized = roleService.principalHasRole(principalId, roleIds, qualification);
        if (LOG.isDebugEnabled()) {
            LOG.debug( "Result: " + isAuthorized );
        }
		return isAuthorized;
    	
    }
    @Override
    public List<Permission> getAuthorizedPermissions( String principalId,
            String namespaceCode, String permissionName,
            Map<String, String> qualification ) throws RiceIllegalArgumentException {
        incomingParamCheck(principalId, "principalId");
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionName, "permissionName");
        incomingParamCheck(qualification, "qualification");

        // get all the permission objects whose name match that requested
    	List<Permission> permissions = getPermissionsByName(namespaceCode, permissionName);
    	// now, filter the full list by the detail passed
    	List<Permission> applicablePermissions = getMatchingPermissions( permissions, null );
        List<Permission> permissionsForUser = getPermissionsForUser(principalId, applicablePermissions, qualification);
    	return permissionsForUser;
    }
    @Override
    public List<Permission> getAuthorizedPermissionsByTemplate(String principalId, String namespaceCode,
            String permissionTemplateName, Map<String, String> permissionDetails, Map<String, String> qualification) throws RiceIllegalArgumentException {
        incomingParamCheck(principalId, "principalId");
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionTemplateName, "permissionTemplateName");
        incomingParamCheck(qualification, "qualification");

        // get all the permission objects whose name match that requested
    	List<Permission> permissions = getPermissionsByTemplateName(namespaceCode, permissionTemplateName);
    	// now, filter the full list by the detail passed
    	List<Permission> applicablePermissions = getMatchingPermissions( permissions, permissionDetails );

        return getPermissionsForUser(principalId, applicablePermissions, qualification);
    }
    
    /**
     * Checks the list of permissions against the principal's roles and returns a subset of the list which match.
     */
    protected List<Permission> getPermissionsForUser( String principalId, List<Permission> permissions,
            Map<String, String> qualification ) {
    	List<Permission> results = new ArrayList<Permission>();
    	for ( Permission perm : permissions ) {
    		List<String> roleIds = getRoleIdsForPermissions( Collections.singletonList(perm) );
    		if ( roleIds != null && !roleIds.isEmpty() ) {
    			if ( roleService.principalHasRole( principalId, roleIds, qualification) ) {
    				results.add( perm );
    			}
    		}
    	}
    	return Collections.unmodifiableList(results);
    }

    protected Map<String,PermissionTypeService> getPermissionTypeServicesByTemplateId( Collection<Permission> permissions ) {
    	Map<String,PermissionTypeService> permissionTypeServices = new HashMap<String, PermissionTypeService>( permissions.size() );
    	for (Permission perm : permissions) {
            if(!permissionTypeServices.containsKey(perm.getTemplate().getId())) {
                permissionTypeServices.put(perm.getTemplate().getId(), getPermissionTypeService(perm.getTemplate()));
            }
    	}
    	return permissionTypeServices;
    }
    
	protected Map<String,List<Permission>> groupPermissionsByTemplate(Collection<Permission> permissions) {
    	Map<String,List<Permission>> results = new HashMap<String,List<Permission>>();
    	for (Permission perm : permissions) {
    		List<Permission> perms = results.get(perm.getTemplate().getId());
    		if (perms == null) {
    			perms = new ArrayList<Permission>();
    			results.put(perm.getTemplate().getId(), perms);
    		}
    		perms.add(perm);
    	}
    	return results;
    }
    
	/**
     * Compare each of the passed in permissions with the given permissionDetails.  Those that
     * match are added to the result list.
     */
    protected List<Permission> getMatchingPermissions( List<Permission> permissions, Map<String, String> permissionDetails ) {
        List<String> permissionIds = new ArrayList<String>(permissions.size());
        for (Permission permission : permissions) {
            permissionIds.add(permission.getId());
        }
        String cacheKey =  new StringBuilder("{getMatchingPermissions}")
                .append("permissionIds=").append(CacheKeyUtils.key(permissionIds)).append("|")
                .append("permissionDetails=").append(CacheKeyUtils.mapKey(permissionDetails)).toString();
        Cache.ValueWrapper cachedValue = cacheManager.getCache(Permission.Cache.NAME).get(cacheKey);
        if (cachedValue != null && cachedValue.get() instanceof List) {
            return ((List<Permission>)cachedValue.get());
        }

    	List<Permission> applicablePermissions = new ArrayList<Permission>();    	
    	if ( permissionDetails == null || permissionDetails.isEmpty() ) {
    		// if no details passed, assume that all match
    		for ( Permission perm : permissions ) {
    			applicablePermissions.add(perm);
    		}
    	} else {
    		// otherwise, attempt to match the permission details
    		// build a map of the template IDs to the type services
    		Map<String,PermissionTypeService> permissionTypeServices = getPermissionTypeServicesByTemplateId(permissions);
    		// build a map of permissions by template ID
    		Map<String, List<Permission>> permissionMap = groupPermissionsByTemplate(permissions);
    		// loop over the different templates, matching all of the same template against the type
    		// service at once
    		for ( Map.Entry<String,List<Permission>> entry : permissionMap.entrySet() ) {
    			PermissionTypeService permissionTypeService = permissionTypeServices.get( entry.getKey() );
    			List<Permission> permissionList = entry.getValue();
				applicablePermissions.addAll( permissionTypeService.getMatchingPermissions( permissionDetails, permissionList ) );    				
    		}
    	}
        applicablePermissions = Collections.unmodifiableList(applicablePermissions);
        cacheManager.getCache(Permission.Cache.NAME).put(cacheKey, applicablePermissions);
        return applicablePermissions;
    }


    @Override
    public List<Assignee> getPermissionAssignees( String namespaceCode, String permissionName,
            Map<String, String> qualification ) throws RiceIllegalArgumentException {
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionName, "permissionName");
        incomingParamCheck(qualification, "qualification");

    	List<String> roleIds = getRoleIdsForPermission( namespaceCode, permissionName);
    	if ( roleIds.isEmpty() ) {
    		return Collections.emptyList();
    	}
    	Collection<RoleMembership> roleMembers = roleService.getRoleMembers( roleIds,qualification );
    	List<Assignee> results = new ArrayList<Assignee>();
        for ( RoleMembership rm : roleMembers ) {
			List<DelegateType.Builder> delegateBuilderList = new ArrayList<DelegateType.Builder>();
			if (!rm.getDelegates().isEmpty()) {
    			for (DelegateType delegate : rm.getDelegates()){
                    delegateBuilderList.add(DelegateType.Builder.create(delegate));
    			}
			}
    		if ( MemberType.PRINCIPAL.equals(rm.getType()) ) {
    			results.add (Assignee.Builder.create(rm.getMemberId(), null, delegateBuilderList).build());
    		} else if ( MemberType.GROUP.equals(rm.getType()) ) {
    			results.add (Assignee.Builder.create(null, rm.getMemberId(), delegateBuilderList).build());
    		}
    	}

    	return Collections.unmodifiableList(results);
    }

    @Override
    public List<Assignee> getPermissionAssigneesByTemplate(String namespaceCode, String permissionTemplateName,
            Map<String, String> permissionDetails, Map<String, String> qualification) throws RiceIllegalArgumentException {
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionTemplateName, "permissionTemplateName");
        incomingParamCheck(qualification, "qualification");

    	List<String> roleIds = getRoleIdsForPermissionTemplate( namespaceCode, permissionTemplateName, permissionDetails);
    	if ( roleIds.isEmpty() ) {
    		return Collections.emptyList();
    	}
    	Collection<RoleMembership> roleMembers = roleService.getRoleMembers( roleIds,qualification);
    	List<Assignee> results = new ArrayList<Assignee>();
        for ( RoleMembership rm : roleMembers ) {
			List<DelegateType.Builder> delegateBuilderList = new ArrayList<DelegateType.Builder>();
			if (!rm.getDelegates().isEmpty()) {
    			for (DelegateType delegate : rm.getDelegates()){
                    delegateBuilderList.add(DelegateType.Builder.create(delegate));
    			}
			}
    		if ( MemberType.PRINCIPAL.equals(rm.getType()) ) {
    			results.add (Assignee.Builder.create(rm.getMemberId(), null, delegateBuilderList).build());
    		} else { // a group membership
    			results.add (Assignee.Builder.create(null, rm.getMemberId(), delegateBuilderList).build());
    		}
    	}
    	return Collections.unmodifiableList(results);
    }

    @Override
    public boolean isPermissionDefined( String namespaceCode, String permissionName ) throws RiceIllegalArgumentException {
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionName, "permissionName");

    	// get all the permission objects whose name match that requested
    	List<Permission> permissions = getPermissionsByName(namespaceCode, permissionName);
    	// now, filter the full list by the detail passed
    	return !getMatchingPermissions(permissions, null).isEmpty();
    }

    @Override
    public boolean isPermissionDefinedByTemplate(String namespaceCode, String permissionTemplateName,
            Map<String, String> permissionDetails) throws RiceIllegalArgumentException {

        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionTemplateName, "permissionTemplateName");

    	// get all the permission objects whose name match that requested
    	List<Permission> permissions = getPermissionsByTemplateName(namespaceCode, permissionTemplateName);
    	// now, filter the full list by the detail passed
    	return !getMatchingPermissions(permissions, permissionDetails).isEmpty();
    }

    @Override
    public List<String> getRoleIdsForPermission(String namespaceCode, String permissionName) throws RiceIllegalArgumentException {
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionName, "permissionName");
        // note...this method is cached at the RoleService interface level using an annotation, but it's called quite
        // a bit internally, so we'll reproduce the caching here using the same key to help optimize
        String cacheKey =  new StringBuilder("{RoleIds}")
                .append("namespaceCode=").append(namespaceCode).append("|")
                .append("name=").append(permissionName).toString();
        Cache.ValueWrapper cachedValue = cacheManager.getCache(Permission.Cache.NAME).get(cacheKey);
        if (cachedValue != null && cachedValue.get() instanceof List) {
            return ((List<String>)cachedValue.get());
        }
        // get all the permission objects whose name match that requested
        List<Permission> permissions = getPermissionsByName(namespaceCode, permissionName);
        // now, filter the full list by the detail passed
        List<Permission> applicablePermissions = getMatchingPermissions(permissions, null);
        List<String> roleIds = getRoleIdsForPermissions(applicablePermissions);
        cacheManager.getCache(Permission.Cache.NAME).put(cacheKey, roleIds);
        return roleIds;
    }

    protected List<String> getRoleIdsForPermissionTemplate(String namespaceCode,
            String permissionTemplateName,
            Map<String, String> permissionDetails) {
        String cacheKey =  new StringBuilder("{getRoleIdsForPermissionTemplate}")
                .append("namespaceCode=").append(namespaceCode).append("|")
                .append("permissionTemplateName=").append(permissionTemplateName).append("|")
                .append("permissionDetails=").append(CacheKeyUtils.mapKey(permissionDetails)).toString();
        Cache.ValueWrapper cachedValue = cacheManager.getCache(Permission.Cache.NAME).get(cacheKey);
        if (cachedValue != null && cachedValue.get() instanceof List) {
            return ((List<String>)cachedValue.get());
        }
    	// get all the permission objects whose name match that requested
    	List<Permission> permissions = getPermissionsByTemplateName(namespaceCode, permissionTemplateName);
    	// now, filter the full list by the detail passed
    	List<Permission> applicablePermissions = getMatchingPermissions(permissions, permissionDetails);
    	List<String> roleIds = getRoleIdsForPermissions(applicablePermissions);
        cacheManager.getCache(Permission.Cache.NAME).put(cacheKey, roleIds);
        return roleIds;
    }

    // --------------------
    // Permission Data
    // --------------------
    
    @Override
    public Permission getPermission(String permissionId) throws RiceIllegalArgumentException {
        incomingParamCheck(permissionId, "permissionId");
        PermissionBo impl = getPermissionImpl(permissionId);
    	if (impl != null) {
    		return PermissionBo.to(impl);
    	}
    	return null;
    }
    
    @Override
    public List<Permission> findPermissionsByTemplate(String namespaceCode, String permissionTemplateName) throws RiceIllegalArgumentException {
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionTemplateName, "permissionTemplateName");

        List<Permission> perms = getPermissionsByTemplateName(namespaceCode, permissionTemplateName);
    	List<Permission> results = new ArrayList<Permission>(perms.size());
    	for (Permission perm : perms) {
    	    results.add(perm);
    	}
    	return Collections.unmodifiableList(results);
    }

	protected PermissionBo getPermissionImpl(String permissionId) throws RiceIllegalArgumentException {
    	incomingParamCheck(permissionId, "permissionId");

        HashMap<String,Object> pk = new HashMap<String,Object>( 1 );
        pk.put( KimConstants.PrimaryKeyConstants.PERMISSION_ID, permissionId );
        return businessObjectService.findByPrimaryKey( PermissionBo.class, pk );
    }
    
    protected List<Permission> getPermissionsByTemplateName( String namespaceCode, String permissionTemplateName ){
        String cacheKey =  new StringBuilder("{getPermissionsByTemplateName}")
                .append("namespaceCode=").append(namespaceCode).append("|")
                .append("permissionTemplateName=").append(permissionTemplateName).toString();
        Cache.ValueWrapper cachedValue = cacheManager.getCache(Permission.Cache.NAME).get(cacheKey);
        if (cachedValue != null && cachedValue.get() instanceof List) {
            return ((List<Permission>)cachedValue.get());
        }
        HashMap<String,Object> criteria = new HashMap<String,Object>(3);
        criteria.put("template.namespaceCode", namespaceCode);
        criteria.put("template.name", permissionTemplateName);
        criteria.put("template.active", "Y");
        criteria.put(KRADPropertyConstants.ACTIVE, "Y");
        List<Permission> permissions =
                toPermissions(businessObjectService.findMatching(PermissionBo.class, criteria));
        cacheManager.getCache(Permission.Cache.NAME).put(cacheKey, permissions);
        return permissions;
    }

    protected List<Permission> getPermissionsByName( String namespaceCode, String permissionName ) {
        String cacheKey =  new StringBuilder("{getPermissionsByName}")
                .append("namespaceCode=").append(namespaceCode).append("|")
                .append("permissionName=").append(permissionName).toString();
        Cache.ValueWrapper cachedValue = cacheManager.getCache(Permission.Cache.NAME).get(cacheKey);
        if (cachedValue != null && cachedValue.get() instanceof List) {
            return ((List<Permission>)cachedValue.get());
        }
        HashMap<String,Object> criteria = new HashMap<String,Object>(3);
        criteria.put(KimConstants.UniqueKeyConstants.NAMESPACE_CODE, namespaceCode);
        criteria.put(KimConstants.UniqueKeyConstants.PERMISSION_NAME, permissionName);
        criteria.put(KRADPropertyConstants.ACTIVE, "Y");
        List<Permission> permissions =
                toPermissions(businessObjectService.findMatching( PermissionBo.class, criteria ));
        cacheManager.getCache(Permission.Cache.NAME).put(cacheKey, permissions);
        return permissions;
    }
	
    @Override
	public Template getPermissionTemplate(String permissionTemplateId) throws RiceIllegalArgumentException {
        incomingParamCheck(permissionTemplateId, "permissionTemplateId");

        PermissionTemplateBo impl = businessObjectService.findBySinglePrimaryKey( PermissionTemplateBo.class, permissionTemplateId );
		if ( impl != null ) {
			return PermissionTemplateBo.to(impl);
		}
		return null;
	}

    @Override
	public Template findPermTemplateByNamespaceCodeAndName(String namespaceCode,
            String permissionTemplateName) throws RiceIllegalArgumentException {
		incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionTemplateName, "permissionTemplateName");

        Map<String,String> criteria = new HashMap<String,String>(2);
		criteria.put( KimConstants.UniqueKeyConstants.NAMESPACE_CODE, namespaceCode );
		criteria.put( KimConstants.UniqueKeyConstants.PERMISSION_TEMPLATE_NAME, permissionTemplateName );
		PermissionTemplateBo impl = businessObjectService.findByPrimaryKey( PermissionTemplateBo.class, criteria );
		if ( impl != null ) {
			return PermissionTemplateBo.to(impl);
		}
		return null;
	}

    @Override
	public List<Template> getAllTemplates() {
		if ( allTemplates.isEmpty() ) {
			Map<String,String> criteria = new HashMap<String,String>(1);
			criteria.put( KRADPropertyConstants.ACTIVE, "Y" );
			List<PermissionTemplateBo> impls = (List<PermissionTemplateBo>) businessObjectService.findMatching( PermissionTemplateBo.class, criteria );
			List<Template> infos = new ArrayList<Template>( impls.size() );
			for ( PermissionTemplateBo impl : impls ) {
				infos.add( PermissionTemplateBo.to(impl) );
			}
			Collections.sort(infos, new Comparator<Template>() {
				@Override public int compare(Template tmpl1,
						Template tmpl2) {

					int result = tmpl1.getNamespaceCode().compareTo(tmpl2.getNamespaceCode());
					if ( result != 0 ) {
						return result;
					}
					result = tmpl1.getName().compareTo(tmpl2.getName());
					return result;
				}
			});
			allTemplates.addAll(infos);
		}
		return Collections.unmodifiableList(allTemplates);
    }


	@Override
	public Permission createPermission(Permission permission)
			throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(permission, "permission");

        if (StringUtils.isNotBlank(permission.getId()) && getPermission(permission.getId()) != null) {
            throw new RiceIllegalStateException("the permission to create already exists: " + permission);
        }
        List<PermissionAttributeBo> attrBos = Collections.emptyList();
        if (permission.getTemplate() != null) {
            attrBos = KimAttributeDataBo.createFrom(PermissionAttributeBo.class, permission.getAttributes(), permission.getTemplate().getKimTypeId());
        }
        PermissionBo bo = PermissionBo.from(permission);
        if (bo.getTemplate() == null && bo.getTemplateId() != null) {
            bo.setTemplate(PermissionTemplateBo.from(getPermissionTemplate(bo.getTemplateId())));
        }
        bo.setAttributeDetails(attrBos);
        return PermissionBo.to(businessObjectService.save(bo));
	}

	@Override
	public Permission updatePermission(Permission permission)
			throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(permission, "permission");

        PermissionBo oldPermission = getPermissionImpl(permission.getId());
        if (StringUtils.isBlank(permission.getId()) || oldPermission == null) {
            throw new RiceIllegalStateException("the permission does not exist: " + permission);
        }

        //List<PermissionAttributeBo> attrBos = KimAttributeDataBo.createFrom(PermissionAttributeBo.class, permission.getAttributes(), permission.getTemplate().getKimTypeId());

        List<PermissionAttributeBo> oldAttrBos = oldPermission.getAttributeDetails();
        //put old attributes in map for easier updating
        Map<String, PermissionAttributeBo> oldAttrMap = new HashMap<String, PermissionAttributeBo>();
        for (PermissionAttributeBo oldAttr : oldAttrBos) {
            oldAttrMap.put(oldAttr.getKimAttribute().getAttributeName(), oldAttr);
        }
        List<PermissionAttributeBo> newAttrBos = new ArrayList<PermissionAttributeBo>();
        for (String key : permission.getAttributes().keySet()) {
            if (oldAttrMap.containsKey(key)) {
                PermissionAttributeBo updatedAttr = oldAttrMap.get(key);
                updatedAttr.setAttributeValue(permission.getAttributes().get(key));
                newAttrBos.add(updatedAttr);
            } else { //new attribute
                newAttrBos.addAll(KimAttributeDataBo.createFrom(PermissionAttributeBo.class, Collections.singletonMap(key, permission.getAttributes().get(key)), permission.getTemplate().getKimTypeId()));
            }
        }
        PermissionBo bo = PermissionBo.from(permission);
        if (CollectionUtils.isNotEmpty(newAttrBos)) {
            if(null!= bo.getAttributeDetails())  {
                bo.getAttributeDetails().clear();
            }
            bo.setAttributeDetails(newAttrBos);
        }
        if (bo.getTemplate() == null && bo.getTemplateId() != null) {
            bo.setTemplate(PermissionTemplateBo.from(getPermissionTemplate(bo.getTemplateId())));
        }

        return PermissionBo.to(businessObjectService.save(bo));		
	}
	
    @Override
    public Permission findPermByNamespaceCodeAndName(String namespaceCode, String permissionName)
            throws RiceIllegalArgumentException {
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(permissionName, "permissionName");

        PermissionBo permissionBo = getPermissionBoByName(namespaceCode, permissionName);
        if (permissionBo != null) {
            return PermissionBo.to(permissionBo);
        }
        return null;
    }
    
    protected PermissionBo getPermissionBoByName(String namespaceCode, String permissionName) {
        if (StringUtils.isBlank(namespaceCode)
                || StringUtils.isBlank(permissionName)) {
            return null;
        }
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put(KimConstants.UniqueKeyConstants.NAMESPACE_CODE, namespaceCode);
        criteria.put(KimConstants.UniqueKeyConstants.NAME, permissionName);
        criteria.put(KRADPropertyConstants.ACTIVE, "Y");
        // while this is not actually the primary key - there will be at most one row with these criteria
        return businessObjectService.findByPrimaryKey(PermissionBo.class, criteria);
    }

    @Override
    public PermissionQueryResults findPermissions(final QueryByCriteria queryByCriteria)
            throws RiceIllegalArgumentException {
        incomingParamCheck(queryByCriteria, "queryByCriteria");

        LookupCustomizer.Builder<PermissionBo> lc = LookupCustomizer.Builder.create();
        lc.setPredicateTransform(AttributeTransform.getInstance());

        GenericQueryResults<PermissionBo> results = criteriaLookupService.lookup(PermissionBo.class, queryByCriteria, lc.build());

        PermissionQueryResults.Builder builder = PermissionQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<Permission.Builder> ims = new ArrayList<Permission.Builder>();
        for (PermissionBo bo : results.getResults()) {
            ims.add(Permission.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
    }

    @Override
    public TemplateQueryResults findPermissionTemplates(final QueryByCriteria queryByCriteria)
            throws RiceIllegalArgumentException {
        incomingParamCheck(queryByCriteria, "queryByCriteria");

        GenericQueryResults<PermissionTemplateBo> results = criteriaLookupService.lookup(PermissionTemplateBo.class, queryByCriteria);

        TemplateQueryResults.Builder builder = TemplateQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<Template.Builder> ims = new ArrayList<Template.Builder>();
        for (PermissionTemplateBo bo : results.getResults()) {
            ims.add(Template.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
    }

    private List<String> getRoleIdsForPermissions( Collection<Permission> permissions ) {
        if (CollectionUtils.isEmpty(permissions)) {
            return Collections.emptyList();
        }
        List<String> ids = new ArrayList<String>();
        for (Permission p : permissions) {
            ids.add(p.getId());
        }

        return getRoleIdsForPermissionIds(ids);
    }

    private List<String> getRoleIdsForPermissionIds(Collection<String> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return Collections.emptyList();
        }
        String cacheKey =  new StringBuilder("{getRoleIdsForPermissionIds}")
                .append("permissionIds=").append(CacheKeyUtils.key(permissionIds)).toString();
        Cache.ValueWrapper cachedValue = cacheManager.getCache(Permission.Cache.NAME).get(cacheKey);
        if (cachedValue != null && cachedValue.get() instanceof List) {
            return ((List<String>)cachedValue.get());
        }
        QueryByCriteria query = QueryByCriteria.Builder.fromPredicates(equal("active", "true"), in("permissionId", permissionIds.toArray(new String[]{})));
        GenericQueryResults<RolePermissionBo> results = criteriaLookupService.lookup(RolePermissionBo.class, query);
        List<String> roleIds = new ArrayList<String>();
        for (RolePermissionBo bo : results.getResults()) {
            roleIds.add(bo.getRoleId());
        }
        roleIds = Collections.unmodifiableList(roleIds);
        cacheManager.getCache(Permission.Cache.NAME).put(cacheKey, roleIds);
        return roleIds;
    }
	
	/**
     * Sets the kimTypeInfoService attribute value.
     *
     * @param kimTypeInfoService The kimTypeInfoService to set.
     */
	public void setKimTypeInfoService(KimTypeInfoService kimTypeInfoService) {
		this.kimTypeInfoService = kimTypeInfoService;
	}
	
	/**
     * Sets the defaultPermissionTypeService attribute value.
     *
     * @param defaultPermissionTypeService The defaultPermissionTypeService to set.
     */
	public void setDefaultPermissionTypeService(PermissionTypeService defaultPermissionTypeService) {
    	this.defaultPermissionTypeService = defaultPermissionTypeService;
	}
	
	/**
     * Sets the roleService attribute value.
     *
     * @param roleService The roleService to set.
     */
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

    /**
     * Sets the businessObjectService attribute value.
     *
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Sets the criteriaLookupService attribute value.
     *
     * @param criteriaLookupService The criteriaLookupService to set.
     */
    public void setCriteriaLookupService(final CriteriaLookupService criteriaLookupService) {
        this.criteriaLookupService = criteriaLookupService;
    }

    /**
     * Sets the cache manager which this service implementation can for internal caching.
     * Calling this setter is optional, though the value passed to it must not be null.
     *
     * @param cacheManager the cache manager to use for internal caching, must not be null
     * @throws IllegalArgumentException if a null cache manager is passed
     */
    public void setCacheManager(final CacheManager cacheManager) {
        if (cacheManager == null) {
            throw new IllegalArgumentException("cacheManager must not be null");
        }
        this.cacheManager = cacheManager;
    }

    private List<Permission> toPermissions(Collection<PermissionBo> permissionBos) {
        if (CollectionUtils.isEmpty(permissionBos)) {
            return new ArrayList<Permission>();
        }
        List<Permission> permissions = new ArrayList<Permission>(permissionBos.size());
        for (PermissionBo permissionBo : permissionBos) {
            permissions.add(PermissionBo.to(permissionBo));
        }
        return permissions;
    }
    
    protected void logAuthorizationCheck(String checkType, String principalId, String namespaceCode, String permissionName, Map<String, String> qualification ) {
        StringBuilder sb = new StringBuilder();
        sb.append(  '\n' );
        sb.append( "Is AuthZ for " ).append( checkType ).append( ": " ).append( namespaceCode ).append( "/" ).append( permissionName ).append( '\n' );
        sb.append( "             Principal:  " ).append( principalId );
        if ( principalId != null ) {
            Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
            if ( principal != null ) {
                sb.append( " (" ).append( principal.getPrincipalName() ).append( ')' );
            }
        }
        sb.append( '\n' );
        sb.append( "             Qualifiers:\n" );
        if ( qualification != null && !qualification.isEmpty() ) {
            sb.append( qualification );
        } else {
            sb.append( "                         [null]\n" );
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace( sb.append(ExceptionUtils.getStackTrace(new Throwable())));
        } else {
            LOG.debug(sb.toString());
        }
    }
    
    protected void logAuthorizationCheckByTemplate(String checkType, String principalId, String namespaceCode, String permissionName, 
                                                   Map<String, String> permissionDetails, Map<String, String> qualification ) {
        StringBuilder sb = new StringBuilder();
        sb.append(  '\n' );
        sb.append( "Is AuthZ for " ).append( checkType ).append( ": " ).append( namespaceCode ).append( "/" ).append( permissionName ).append( '\n' );
        sb.append( "             Principal:  " ).append( principalId );
        if ( principalId != null ) {
            Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
            if ( principal != null ) {
                sb.append( " (" ).append( principal.getPrincipalName() ).append( ')' );
            }
        }
        sb.append( '\n' );
        sb.append( "             Details:\n" );
        if ( permissionDetails != null ) {
            sb.append( permissionDetails );
        } else {
            sb.append( "                         [null]\n" );
        }
        sb.append( "             Qualifiers:\n" );
        if ( qualification != null && !qualification.isEmpty() ) {
            sb.append( qualification );
        } else {
            sb.append( "                         [null]\n" );
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace( sb.append(ExceptionUtils.getStackTrace(new Throwable())));
        } else {
            LOG.debug(sb.toString());
        }
    }
    
    private void incomingParamCheck(Object object, String name) {
        if (object == null) {
            throw new RiceIllegalArgumentException(name + " was null");
        } else if (object instanceof String
                && StringUtils.isBlank((String) object)) {
            throw new RiceIllegalArgumentException(name + " was blank");
        }
    }


}
