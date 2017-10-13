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
package org.kuali.rice.kim.lookup;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.permission.GenericPermissionBo;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.kuali.rice.kim.impl.permission.UberPermissionBo;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.role.RolePermissionBo;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.lookup.CollectionIncomplete;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PermissionLookupableHelperServiceImpl extends RoleMemberLookupableHelperServiceImpl {

	private static final long serialVersionUID = -3578448525862270477L;

	private transient LookupService lookupService;
	private transient RoleService roleService;
	private volatile String genericPermissionDocumentTypeName;

	@Override
	public List<HtmlData> getCustomActionUrls(BusinessObject businessObject, List pkNames) {
    	List<HtmlData> htmlDataList = new ArrayList<HtmlData>();
    	// convert the PermissionBo class into an UberPermission object
    	businessObject = new GenericPermissionBo((UberPermissionBo)businessObject);
        if (allowsMaintenanceEditAction(businessObject)) {
        	htmlDataList.add(getUrlData(businessObject, KRADConstants.MAINTENANCE_EDIT_METHOD_TO_CALL, pkNames));
        }
        if (allowsMaintenanceNewOrCopyAction()) {
        	htmlDataList.add(getUrlData(businessObject, KRADConstants.MAINTENANCE_COPY_METHOD_TO_CALL, pkNames));
        }
        return htmlDataList;
	}

	protected String getActionUrlHref(BusinessObject businessObject, String methodToCall, List pkNames){
        Properties parameters = new Properties();
        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, methodToCall);
        parameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, businessObject.getClass().getName());
        parameters.put(KRADConstants.OVERRIDE_KEYS, KimConstants.PrimaryKeyConstants.PERMISSION_ID);
        parameters.put(KRADConstants.COPY_KEYS, KimConstants.PrimaryKeyConstants.PERMISSION_ID);
        if (StringUtils.isNotBlank(getReturnLocation())) {
        	parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, getReturnLocation());
		}
        parameters.putAll(getParametersFromPrimaryKey(businessObject, pkNames));
        return UrlFactory.parameterizeUrl(KRADConstants.MAINTENANCE_ACTION, parameters);
    }
	
	@Override
	protected String getMaintenanceDocumentTypeName() {
		//using DCL idiom to cache genericPermissionDocumentTypeName.
        //see effective java 2nd ed. pg. 71
        String g = genericPermissionDocumentTypeName;
        if (g == null) {
            synchronized (this) {
                g = genericPermissionDocumentTypeName;
                if (g == null) {
                    genericPermissionDocumentTypeName = g = getMaintenanceDocumentDictionaryService().getDocumentTypeName(
                            GenericPermissionBo.class);
                }
            }
        }

        return g;
	}
		
	@Override
	protected List<? extends BusinessObject> getMemberSearchResults(Map<String, String> searchCriteria, boolean unbounded) {
		Map<String, String> permissionSearchCriteria = buildSearchCriteria(searchCriteria);
		Map<String, String> roleSearchCriteria = buildRoleSearchCriteria(searchCriteria);
		boolean permissionCriteriaEmpty = permissionSearchCriteria==null || permissionSearchCriteria.isEmpty();
		boolean roleCriteriaEmpty = roleSearchCriteria==null || roleSearchCriteria.isEmpty();
		
		List<UberPermissionBo> permissionSearchResultsCopy = new CollectionIncomplete<UberPermissionBo>(new ArrayList<UberPermissionBo>(), new Long(0));
		if(!permissionCriteriaEmpty && !roleCriteriaEmpty){
			permissionSearchResultsCopy = getCombinedSearchResults(permissionSearchCriteria, roleSearchCriteria, unbounded);
		} else if(permissionCriteriaEmpty && !roleCriteriaEmpty){
			permissionSearchResultsCopy = getPermissionsWithRoleSearchCriteria(roleSearchCriteria, unbounded);
		} else if(!permissionCriteriaEmpty && roleCriteriaEmpty){
			permissionSearchResultsCopy = getPermissionsWithPermissionSearchCriteria(permissionSearchCriteria, unbounded);
		} else if(permissionCriteriaEmpty && roleCriteriaEmpty){
			return getAllPermissions(unbounded);
		}
		return permissionSearchResultsCopy;
	}
	
	private List<UberPermissionBo> getAllPermissions(boolean unbounded){
		List<UberPermissionBo> permissions = searchPermissions(new HashMap<String, String>(), unbounded);
		for(UberPermissionBo permission: permissions) {
			populateAssignedToRoles(permission);
        }
		return permissions;
	}
	
	private List<UberPermissionBo> getCombinedSearchResults(
			Map<String, String> permissionSearchCriteria, Map<String, String> roleSearchCriteria, boolean unbounded){
		List<UberPermissionBo> permissionSearchResults = searchPermissions(permissionSearchCriteria, unbounded);
		List<RoleBo> roleSearchResults = searchRoles(roleSearchCriteria, unbounded);
		List<UberPermissionBo> permissionsForRoleSearchResults = getPermissionsForRoleSearchResults(roleSearchResults, unbounded);
		List<UberPermissionBo> matchedPermissions = new CollectionIncomplete<UberPermissionBo>(
			new ArrayList<UberPermissionBo>(), getActualSizeIfTruncated(permissionsForRoleSearchResults));
		if((permissionSearchResults!=null && !permissionSearchResults.isEmpty()) && 
				(permissionsForRoleSearchResults!=null && !permissionsForRoleSearchResults.isEmpty())){
			for(UberPermissionBo permission: permissionSearchResults){
				for(UberPermissionBo permissionFromRoleSearch: permissionsForRoleSearchResults){
					if(permissionFromRoleSearch.getId().equals(permission.getId())) {
						matchedPermissions.add(permissionFromRoleSearch);
                    }
				}
			}
		}
		return matchedPermissions;
	}
	
	private List<UberPermissionBo> searchPermissions(Map<String, String> permissionSearchCriteria, boolean unbounded){
		return getPermissionsSearchResultsCopy(new ArrayList<PermissionBo>(getLookupService().findCollectionBySearchHelper(
				PermissionBo.class, permissionSearchCriteria, unbounded)));

	}
	
	private List<UberPermissionBo> getPermissionsWithRoleSearchCriteria(Map<String, String> roleSearchCriteria, boolean unbounded){
		return getPermissionsForRoleSearchResults(searchRoles(roleSearchCriteria, unbounded), unbounded);
	}

	private List<UberPermissionBo> getPermissionsForRoleSearchResults(List<RoleBo> roleSearchResults, boolean unbounded){
		Long actualSizeIfTruncated = getActualSizeIfTruncated(roleSearchResults);
		List<UberPermissionBo> permissions = new ArrayList<UberPermissionBo>();
		List<UberPermissionBo> tempPermissions;
		List<String> collectedPermissionIds = new ArrayList<String>();
		Map<String, String> permissionCriteria;
		
		for(RoleBo roleImpl: roleSearchResults){
			permissionCriteria = new HashMap<String, String>();
			permissionCriteria.put("rolePermissions.roleId", roleImpl.getId());
			tempPermissions = searchPermissions(permissionCriteria, unbounded);
			actualSizeIfTruncated += getActualSizeIfTruncated(tempPermissions);
			for(UberPermissionBo permission: tempPermissions){
				if(!collectedPermissionIds.contains(permission.getId())){
					populateAssignedToRoles(permission);
					collectedPermissionIds.add(permission.getId());
					permissions.add(permission);
				}
			}
			//need to find roles that current role is a member of and build search string
			List<String> parentRoleIds = KimApiServiceLocator.getRoleService().getMemberParentRoleIds(MemberType.ROLE.getCode(), roleImpl.getId());
			for (String parentRoleId : parentRoleIds) {
				Map<String, String> roleSearchCriteria = new HashMap<String, String>();
				roleSearchCriteria.put("roleId", parentRoleId);
				//get all parent role permissions and merge them with current permissions
				permissions = mergePermissionLists(permissions, getPermissionsWithRoleSearchCriteria(roleSearchCriteria, unbounded));
			}
		}
		
		return new CollectionIncomplete<UberPermissionBo>(permissions, actualSizeIfTruncated);
	}
	

	private void populateAssignedToRoles(UberPermissionBo permission){
		Map<String, String> criteria;
		for(RolePermissionBo rolePermission: permission.getRolePermissions()){
			if ( rolePermission.isActive() ) {
				criteria = new HashMap<String, String>();
				criteria.put("id", rolePermission.getRoleId());
	//			permission.getAssignedToRoles().add((RoleBo)getBusinessObjectService().findByPrimaryKey(RoleBo.class, criteria));
                RoleBo roleBo = getBusinessObjectService().findByPrimaryKey(RoleBo.class, criteria);
                permission.getAssignedToRoles().add(roleBo);

			}
		}
	}

	private List<UberPermissionBo> getPermissionsWithPermissionSearchCriteria(
			Map<String, String> permissionSearchCriteria, boolean unbounded){
		String detailCriteriaStr = permissionSearchCriteria.remove( DETAIL_CRITERIA );
		Map<String, String> detailCriteria = parseDetailCriteria(detailCriteriaStr);

		final List<UberPermissionBo> permissions = searchPermissions(permissionSearchCriteria, unbounded);
		List<UberPermissionBo> filteredPermissions = new CollectionIncomplete<UberPermissionBo>(
				new ArrayList<UberPermissionBo>(), getActualSizeIfTruncated(permissions));
		for(UberPermissionBo perm: permissions){
			if ( detailCriteria.isEmpty() ) {
				filteredPermissions.add(perm);
				populateAssignedToRoles(perm);
			} else {
				if ( isMapSubset( new HashMap<String, String>(perm.getDetails()), detailCriteria ) ) {
					filteredPermissions.add(perm);
					populateAssignedToRoles(perm);
				}
			}
		}
		return filteredPermissions;
	}
	
	private List<UberPermissionBo> getPermissionsSearchResultsCopy(List<PermissionBo> permissionSearchResults){
		List<UberPermissionBo> permissionSearchResultsCopy = new CollectionIncomplete<UberPermissionBo>(
				new ArrayList<UberPermissionBo>(), getActualSizeIfTruncated(permissionSearchResults));
		for(PermissionBo permissionBo: permissionSearchResults){
			UberPermissionBo permissionCopy = new UberPermissionBo();

            try {
                PropertyUtils.copyProperties(permissionCopy, permissionBo);
                //Hack for tomcat 7 KULRICE-5927
                permissionCopy.setTemplate(permissionBo.getTemplate());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("unable to copy properties");
            } catch (InvocationTargetException e) {
                throw new RuntimeException("unable to copy properties");
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("unable to copy properties");
            }

            permissionSearchResultsCopy.add(permissionCopy);
		}
		return permissionSearchResultsCopy;
	}

	/**
	 * @return the lookupService
	 */
	public synchronized LookupService getLookupService() {
		if ( lookupService == null ) {
			lookupService = KRADServiceLocatorWeb.getLookupService();
		}
		return lookupService;
	}

	public synchronized RoleService getRoleService() {
		if (roleService == null) {
			roleService = KimApiServiceLocator.getRoleService();
		}
		return roleService;
	}

	private List<UberPermissionBo> mergePermissionLists(List<UberPermissionBo> perm1, List<UberPermissionBo> perm2) {
		List<UberPermissionBo> returnList = new ArrayList<UberPermissionBo>(perm1);
		List<String> permissionIds = new ArrayList<String>(perm1.size());
		for (UberPermissionBo perm : returnList) {
			permissionIds.add(perm.getId());
		}
		for (int i=0; i<perm2.size(); i++) {
		    if (!permissionIds.contains(perm2.get(i).getId())) {
		    	returnList.add(perm2.get(i));
		    }
		}
		return returnList;
	}
}
