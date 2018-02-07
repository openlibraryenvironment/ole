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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.role.RoleResponsibilityBo;
import org.kuali.rice.kim.lookup.RoleMemberLookupableHelperServiceImpl;
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

public class ResponsibilityLookupableHelperServiceImpl extends RoleMemberLookupableHelperServiceImpl {

	private static final Logger LOG = Logger.getLogger( ResponsibilityLookupableHelperServiceImpl.class );
	
	private static final long serialVersionUID = -2882500971924192124L;
	
	private static LookupService lookupService;

	private static boolean reviewResponsibilityDocumentTypeNameLoaded = false;
	private static String reviewResponsibilityDocumentTypeName = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HtmlData> getCustomActionUrls(BusinessObject businessObject, List pkNames) {
    	List<HtmlData> htmlDataList = new ArrayList<HtmlData>();
    	// convert the UberResponsibilityBo class into a ReviewResponsibility object
        if ( (((UberResponsibilityBo)businessObject).getTemplate() != null)
                && ((UberResponsibilityBo)businessObject).getTemplate().getName().equals( KewApiConstants.DEFAULT_RESPONSIBILITY_TEMPLATE_NAME ) ) {
        	ReviewResponsibilityBo reviewResp = new ReviewResponsibilityBo( (UberResponsibilityBo)businessObject );
        	businessObject = reviewResp;
	        if (allowsMaintenanceEditAction(businessObject)) {
	        	htmlDataList.add(getUrlData(businessObject, KRADConstants.MAINTENANCE_EDIT_METHOD_TO_CALL, pkNames));
	        }
	        if (allowsMaintenanceNewOrCopyAction()) {
	        	htmlDataList.add(getUrlData(businessObject, KRADConstants.MAINTENANCE_COPY_METHOD_TO_CALL, pkNames));
	        }
        }
        return htmlDataList;
	}

    @SuppressWarnings("unchecked")
	protected String getActionUrlHref(BusinessObject businessObject, String methodToCall, List pkNames){
        Properties parameters = new Properties();
        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, methodToCall);
        // TODO: why is this not using the businessObject parmeter's class?
        parameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, businessObject.getClass().getName());
        parameters.put(KRADConstants.OVERRIDE_KEYS, KimConstants.PrimaryKeyConstants.RESPONSIBILITY_ID);
        parameters.put(KRADConstants.COPY_KEYS, KimConstants.PrimaryKeyConstants.RESPONSIBILITY_ID);
        if (StringUtils.isNotBlank(getReturnLocation())) {
        	parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, getReturnLocation());
		}
        parameters.putAll(getParametersFromPrimaryKey(businessObject, pkNames));
        return UrlFactory.parameterizeUrl(KRADConstants.MAINTENANCE_ACTION, parameters);
    }
	
	@Override
	protected String getMaintenanceDocumentTypeName() {
		if ( !reviewResponsibilityDocumentTypeNameLoaded ) {
			reviewResponsibilityDocumentTypeName = getMaintenanceDocumentDictionaryService().getDocumentTypeName(ReviewResponsibilityBo.class);
			reviewResponsibilityDocumentTypeNameLoaded = true;
		}
		return reviewResponsibilityDocumentTypeName;
	}
	
	@Override
	protected List<? extends BusinessObject> getMemberSearchResults(Map<String, String> searchCriteria, boolean unbounded) {
		Map<String, String> responsibilitySearchCriteria = buildSearchCriteria(searchCriteria);
		Map<String, String> roleSearchCriteria = buildRoleSearchCriteria(searchCriteria);
		boolean responsibilityCriteriaEmpty = responsibilitySearchCriteria==null || responsibilitySearchCriteria.isEmpty();
		boolean roleCriteriaEmpty = roleSearchCriteria==null || roleSearchCriteria.isEmpty();
		
		List<UberResponsibilityBo> responsibilitySearchResultsCopy = new CollectionIncomplete<UberResponsibilityBo>(new ArrayList<UberResponsibilityBo>(), new Long(0));
		if(!responsibilityCriteriaEmpty && !roleCriteriaEmpty){
			responsibilitySearchResultsCopy = getCombinedSearchResults(responsibilitySearchCriteria, roleSearchCriteria, unbounded);
		} else if(responsibilityCriteriaEmpty && !roleCriteriaEmpty){
			responsibilitySearchResultsCopy = getResponsibilitiesWithRoleSearchCriteria(roleSearchCriteria, unbounded);
		} else if(!responsibilityCriteriaEmpty && roleCriteriaEmpty){
			responsibilitySearchResultsCopy = getResponsibilitiesWithResponsibilitySearchCriteria(responsibilitySearchCriteria, unbounded);
		} else if(responsibilityCriteriaEmpty && roleCriteriaEmpty){
			return getAllResponsibilities(unbounded);
		}
		return responsibilitySearchResultsCopy;
	}
	
	private List<UberResponsibilityBo> getAllResponsibilities(boolean unbounded){
		List<UberResponsibilityBo> responsibilities = searchResponsibilities(new HashMap<String, String>(), unbounded);
		for(UberResponsibilityBo responsibility: responsibilities) {
			populateAssignedToRoles(responsibility);
        }
		return responsibilities;
	}
	
	private List<UberResponsibilityBo> getCombinedSearchResults(
			Map<String, String> responsibilitySearchCriteria, Map<String, String> roleSearchCriteria, boolean unbounded){
		List<UberResponsibilityBo> responsibilitySearchResults = searchResponsibilities(responsibilitySearchCriteria, unbounded);
		List<RoleBo> roleSearchResults = searchRoles(roleSearchCriteria, unbounded);
		List<UberResponsibilityBo> responsibilitiesForRoleSearchResults = getResponsibilitiesForRoleSearchResults(roleSearchResults, unbounded);
		List<UberResponsibilityBo> matchedResponsibilities = new CollectionIncomplete<UberResponsibilityBo>(
				new ArrayList<UberResponsibilityBo>(), getActualSizeIfTruncated(responsibilitiesForRoleSearchResults));
		if((responsibilitySearchResults!=null && !responsibilitySearchResults.isEmpty()) && 
				(responsibilitiesForRoleSearchResults!=null && !responsibilitiesForRoleSearchResults.isEmpty())){
			for(UberResponsibilityBo responsibility: responsibilitySearchResults){
				for(UberResponsibilityBo responsibilityFromRoleSearch: responsibilitiesForRoleSearchResults){
					if(responsibilityFromRoleSearch.getId().equals(responsibility.getId())) {
						matchedResponsibilities.add(responsibilityFromRoleSearch);
                    }
				}
			}
		}

		return matchedResponsibilities;
	}
	
	@SuppressWarnings("unchecked")
	private List<UberResponsibilityBo> searchResponsibilities(Map<String, String> responsibilitySearchCriteria, boolean unbounded){
		return getResponsibilitiesSearchResultsCopy((List<ResponsibilityBo>)
					getLookupService().findCollectionBySearchHelper(
							ResponsibilityBo.class, responsibilitySearchCriteria, unbounded));
	}
	
	private List<UberResponsibilityBo> getResponsibilitiesWithRoleSearchCriteria(Map<String, String> roleSearchCriteria, boolean unbounded){
		List<RoleBo> roleSearchResults = searchRoles(roleSearchCriteria, unbounded);
		return getResponsibilitiesForRoleSearchResults(roleSearchResults, unbounded);
	}

	private List<UberResponsibilityBo> getResponsibilitiesForRoleSearchResults(List<RoleBo> roleSearchResults, boolean unbounded){
		Long actualSizeIfTruncated = getActualSizeIfTruncated(roleSearchResults);
		List<UberResponsibilityBo> responsibilities = new ArrayList<UberResponsibilityBo>();
		List<UberResponsibilityBo> tempResponsibilities;
		List<String> collectedResponsibilityIds = new ArrayList<String>();
		Map<String, String> responsibilityCriteria;
		
		for(RoleBo roleImpl: roleSearchResults){
			responsibilityCriteria = new HashMap<String, String>();
			responsibilityCriteria.put("roleResponsibilities.roleId", roleImpl.getId());
			tempResponsibilities = searchResponsibilities(responsibilityCriteria, unbounded);
			actualSizeIfTruncated += getActualSizeIfTruncated(tempResponsibilities);
			for(UberResponsibilityBo responsibility: tempResponsibilities){
				if(!collectedResponsibilityIds.contains(responsibility.getId())){
					populateAssignedToRoles(responsibility);
					collectedResponsibilityIds.add(responsibility.getId());
					responsibilities.add(responsibility);
				}
				//need to find roles that current role is a member of and build search string
				List<String> parentRoleIds = KimApiServiceLocator.getRoleService().getMemberParentRoleIds(MemberType.ROLE.getCode(), roleImpl.getId());
				for (String parentRoleId : parentRoleIds) {
					Map<String, String> roleSearchCriteria = new HashMap<String, String>();
					roleSearchCriteria.put("id", parentRoleId);
					//get all parent role permissions and merge them with current permissions
					responsibilities = mergeResponsibilityLists(responsibilities, getResponsibilitiesWithRoleSearchCriteria(roleSearchCriteria, unbounded));
				}
			}
		}
		return new CollectionIncomplete<UberResponsibilityBo>(responsibilities, actualSizeIfTruncated);
	}

	private void populateAssignedToRoles(UberResponsibilityBo responsibility){
		Map<String, String> criteria = new HashMap<String, String>();
		if ( responsibility.getAssignedToRoles().isEmpty() ) {
			for(RoleResponsibilityBo roleResponsibility: responsibility.getRoleResponsibilities()){
				criteria.put(KimConstants.PrimaryKeyConstants.ID, roleResponsibility.getRoleId());
				responsibility.getAssignedToRoles().add(getBusinessObjectService().findByPrimaryKey(RoleBo.class, criteria));
			}
		}
	}

	private List<UberResponsibilityBo> getResponsibilitiesWithResponsibilitySearchCriteria(Map<String, String> responsibilitySearchCriteria, boolean unbounded){
		String detailCriteriaStr = responsibilitySearchCriteria.remove( DETAIL_CRITERIA );
		Map<String, String> detailCriteria = parseDetailCriteria(detailCriteriaStr);
		final List<UberResponsibilityBo> responsibilities = searchResponsibilities(responsibilitySearchCriteria, unbounded);
		List<UberResponsibilityBo> filteredResponsibilities = new CollectionIncomplete<UberResponsibilityBo>(
				new ArrayList<UberResponsibilityBo>(), getActualSizeIfTruncated(responsibilities)); 
		for(UberResponsibilityBo responsibility: responsibilities){
			if ( detailCriteria.isEmpty() ) {
				filteredResponsibilities.add(responsibility);
				populateAssignedToRoles(responsibility);
			} else {
				if ( isMapSubset( new HashMap<String, String>(responsibility.getAttributes()), detailCriteria ) ) {
					filteredResponsibilities.add(responsibility);
					populateAssignedToRoles(responsibility);
				}
			}
		}
		return filteredResponsibilities;
	}
	
	private List<UberResponsibilityBo> getResponsibilitiesSearchResultsCopy(List<ResponsibilityBo> responsibilitySearchResults){
		List<UberResponsibilityBo> responsibilitySearchResultsCopy = new CollectionIncomplete<UberResponsibilityBo>(
				new ArrayList<UberResponsibilityBo>(), getActualSizeIfTruncated(responsibilitySearchResults));
		for(ResponsibilityBo responsibilityImpl: responsibilitySearchResults){
			UberResponsibilityBo responsibilityCopy = new UberResponsibilityBo();

            try {
                PropertyUtils.copyProperties(responsibilityCopy, responsibilityImpl);
                //Hack for tomcat 7 KULRICE:5927
                responsibilityCopy.setTemplate(responsibilityImpl.getTemplate());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("unable to copy properties");
            } catch (InvocationTargetException e) {
                throw new RuntimeException("unable to copy properties");
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("unable to copy properties");
            }

            responsibilitySearchResultsCopy.add(responsibilityCopy);
		}
		return responsibilitySearchResultsCopy;
	}
	

	/**
	 * @return the lookupService
	 */
	public LookupService getLookupService() {
		if ( lookupService == null ) {
			lookupService = KRADServiceLocatorWeb.getLookupService();
		}
		return lookupService;
	}
 
	private List<UberResponsibilityBo> mergeResponsibilityLists(List<UberResponsibilityBo> perm1, List<UberResponsibilityBo> perm2) {
		List<UberResponsibilityBo> returnList = new ArrayList<UberResponsibilityBo>(perm1);
		List<String> responsibilityIds = new ArrayList<String>(perm1.size());
		for (UberResponsibilityBo perm : returnList) {
			responsibilityIds.add(perm.getId());
		}
		for (int i=0; i<perm2.size(); i++) {
		    if (!responsibilityIds.contains(perm2.get(i).getId())) {
		    	returnList.add(perm2.get(i));
		    }
		}
		return returnList;
	}
}
