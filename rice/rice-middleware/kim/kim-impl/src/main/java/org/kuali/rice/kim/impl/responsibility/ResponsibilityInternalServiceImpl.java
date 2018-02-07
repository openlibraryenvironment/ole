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
package org.kuali.rice.kim.impl.responsibility;

import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.api.responsibility.ResponsibilityService;
import org.kuali.rice.kim.api.role.RoleResponsibility;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.common.delegate.DelegateMemberBo;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.kim.impl.role.RoleResponsibilityBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResponsibilityInternalServiceImpl implements ResponsibilityInternalService {

	private BusinessObjectService businessObjectService;
    private ResponsibilityService responsibilityService;

    @Override
	public RoleMemberBo saveRoleMember(RoleMemberBo roleMember){

		//need to find what responsibilities changed so we can notify interested clients.  Like workflow.
    	List<RoleResponsibility> oldRoleResp = getRoleResponsibilities(roleMember.getRoleId());

    	// add row to member table
    	RoleMemberBo member = getBusinessObjectService().save( roleMember );

    	//need to find what responsibilities changed so we can notify interested clients.  Like workflow.
    	// the new member has been added
    	List<RoleResponsibility> newRoleResp = getRoleResponsibilities(roleMember.getRoleId());

    	updateActionRequestsForResponsibilityChange(getChangedRoleResponsibilityIds(oldRoleResp, newRoleResp));

        return member;
	}

    @Override
    public DelegateMemberBo saveDelegateMember(DelegateMemberBo delegateMember) {

        // add row to member table
        DelegateMemberBo member = getBusinessObjectService().save(delegateMember);

        return member;
    }

    @Override
	public void removeRoleMember(RoleMemberBo roleMember){
		//need to find what responsibilities changed so we can notify interested clients.  Like workflow.
    	List<RoleResponsibility> oldRoleResp = getRoleResponsibilities(roleMember.getRoleId());

    	// need to set end date to inactivate, not delete
        roleMember.setActiveToDateValue(new java.sql.Timestamp(System.currentTimeMillis()));
    	getBusinessObjectService().save( roleMember );

    	//need to find what responsibilities changed so we can notify interested clients.  Like workflow.
    	// the new member has been added
    	List<RoleResponsibility> newRoleResp = getRoleResponsibilities(roleMember.getRoleId());

    	updateActionRequestsForResponsibilityChange(getChangedRoleResponsibilityIds(oldRoleResp, newRoleResp));
	}

    @Override
	@SuppressWarnings("unchecked")
	public void updateActionRequestsForRoleChange(String roleId) {
    	List<RoleResponsibility> newRoleResp = getRoleResponsibilities(roleId);
		
    	updateActionRequestsForResponsibilityChange(getChangedRoleResponsibilityIds(Collections.EMPTY_LIST, newRoleResp));
	}
	

	/**
	 * This overridden method ...
	 *
	 * @see ResponsibilityInternalService#updateActionRequestsForResponsibilityChange(java.util.Set)
	 */
    @Override
	public void updateActionRequestsForResponsibilityChange(Set<String> responsibilityIds) {
        KewApiServiceLocator.getResponsibilityChangeQueue().responsibilitiesChanged(responsibilityIds);
	}

	@SuppressWarnings("unchecked")
	public List<RoleResponsibility> getRoleResponsibilities(String roleId){
		List<RoleResponsibilityBo> rrBoList =
				(List<RoleResponsibilityBo>)getBusinessObjectService()
				.findMatching(RoleResponsibilityBo.class, Collections.singletonMap(KimConstants.PrimaryKeyConstants.SUB_ROLE_ID, roleId));
		List<RoleResponsibility> result = new ArrayList<RoleResponsibility>( rrBoList.size() );
		for ( RoleResponsibilityBo bo : rrBoList ) {
			result.add( RoleResponsibilityBo.to(bo) );
		}
		return result;
    }

	 /**
    *
    * This method compares the two lists of responsibilitiy IDs and does a union.  returns a unique list of responsibility ids.
    *
    * @param oldRespList
    * @param newRespList
    * @return
    */
   protected Set<String> getChangedRoleResponsibilityIds(
			List<RoleResponsibility> oldRespList,
			List<RoleResponsibility> newRespList) {
		Set<String> lRet = new HashSet<String>();

		for (RoleResponsibility resp : oldRespList) {
			lRet.add(resp.getResponsibilityId());
		}
		for (RoleResponsibility resp : newRespList) {
			lRet.add(resp.getResponsibilityId());
		}

		return lRet;
	}

	protected BusinessObjectService getBusinessObjectService() {
		if ( businessObjectService == null ) {
			businessObjectService = KRADServiceLocator.getBusinessObjectService();
		}
		return businessObjectService;
	}

    protected ResponsibilityService getResponsibilityService() {
		if ( responsibilityService == null ) {
			responsibilityService = KimApiServiceLocator.getResponsibilityService();
		}
		return responsibilityService;
	}

    @Override
    public boolean areActionsAtAssignmentLevel(Responsibility responsibility ) {
    	Map<String, String> details = responsibility.getAttributes();
    	if ( details == null ) {
    		return false;
    	}
    	String actionDetailsAtRoleMemberLevel = details.get( KimConstants.AttributeConstants.ACTION_DETAILS_AT_ROLE_MEMBER_LEVEL );
    	return Boolean.valueOf(actionDetailsAtRoleMemberLevel);
    }

    @Override
    public boolean areActionsAtAssignmentLevelById( String responsibilityId ) {
    	Responsibility responsibility = getResponsibilityService().getResponsibility(responsibilityId);
    	if ( responsibility == null ) {
    		return false;
    	}
    	return areActionsAtAssignmentLevel(responsibility);
    }

}
