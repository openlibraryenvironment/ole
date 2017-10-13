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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupQueryResults;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityQueryResults;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.lookup.CollectionIncomplete;
import org.kuali.rice.krad.util.KRADPropertyConstants;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.kuali.rice.core.api.criteria.PredicateFactory.and;
import static org.kuali.rice.core.api.criteria.PredicateFactory.like;

public abstract class RoleMemberLookupableHelperServiceImpl extends KimLookupableHelperServiceImpl {

	protected static final String DETAIL_CRITERIA = "detailCriteria";
	protected static final String WILDCARD = "*";
    protected static final String TEMPLATE_NAMESPACE_CODE = "template." + KimConstants.UniqueKeyConstants.NAMESPACE_CODE;
    protected static final String TEMPLATE_NAME = "template.name";
    protected static final String TEMPLATE_ID = "template.id";
    protected static final String NAMESPACE_CODE = KimConstants.UniqueKeyConstants.NAMESPACE_CODE;
    protected static final String NAME = "name";
    protected static final String GROUP_NAME = KimConstants.UniqueKeyConstants.GROUP_NAME;
    protected static final String ASSIGNED_TO_PRINCIPAL_NAME = "assignedToPrincipal.principalName";
    protected static final String ASSIGNED_TO_GROUP_NAMESPACE_CODE = "assignedToGroupNamespaceForLookup";
    protected static final String ASSIGNED_TO_GROUP_NAME = "assignedToGroup." + KimConstants.UniqueKeyConstants.GROUP_NAME;
    protected static final String ASSIGNED_TO_NAMESPACE_FOR_LOOKUP = "assignedToRoleNamespaceForLookup";
    protected static final String ASSIGNED_TO_ROLE_NAME = "assignedToRole." + KimConstants.UniqueKeyConstants.ROLE_NAME;
    protected static final String ATTRIBUTE_NAME = "attributeName";
    protected static final String ATTRIBUTE_VALUE = "attributeValue";
    protected static final String ASSIGNED_TO_ROLE_NAMESPACE_CODE = KimConstants.UniqueKeyConstants.NAMESPACE_CODE;
    protected static final String ASSIGNED_TO_ROLE_ROLE_NAME = KimConstants.UniqueKeyConstants.ROLE_NAME;
    protected static final String ASSIGNED_TO_ROLE_MEMBER_ID = "members.memberId";
    protected static final String DETAIL_OBJECTS_ATTRIBUTE_VALUE = "attributeDetails.attributeValue";
    protected static final String DETAIL_OBJECTS_ATTRIBUTE_NAME = "attributeDetails.kimAttribute.attributeName";
    
    @Override
    protected List<? extends BusinessObject> getSearchResultsHelper(Map<String, String> fieldValues, boolean unbounded) {
    	Map<String, String> searchCriteria = buildRoleSearchCriteria(fieldValues);
    	if(searchCriteria == null) {
    		return new ArrayList<BusinessObject>();
        }
        return getMemberSearchResults(fieldValues, unbounded);
    }

    protected abstract List<? extends BusinessObject> getMemberSearchResults(Map<String, String> searchCriteria, boolean unbounded);
    
    protected Map<String, String> buildSearchCriteria(Map<String, String> fieldValues){
        String templateNamespaceCode = fieldValues.get(TEMPLATE_NAMESPACE_CODE);
        String templateName = fieldValues.get(TEMPLATE_NAME);
        String templateId = fieldValues.get(TEMPLATE_ID);
        String namespaceCode = fieldValues.get(NAMESPACE_CODE);
        String name = fieldValues.get(NAME);
        String attributeDetailValue = fieldValues.get(ATTRIBUTE_VALUE);
        String attributeDetailName = fieldValues.get(ATTRIBUTE_NAME);
        String detailCriteria = fieldValues.get( DETAIL_CRITERIA );
        String active = fieldValues.get( KRADPropertyConstants.ACTIVE );

    	Map<String,String> searchCriteria = new HashMap<String, String>();
    	if(StringUtils.isNotEmpty(templateNamespaceCode)) {
    		searchCriteria.put(TEMPLATE_NAMESPACE_CODE, WILDCARD+templateNamespaceCode+WILDCARD);
    	}
        if(StringUtils.isNotEmpty(templateName)) {
        	searchCriteria.put(TEMPLATE_NAME, WILDCARD+templateName+WILDCARD);
        }
        if(StringUtils.isNotEmpty(templateId)) {
            searchCriteria.put(TEMPLATE_ID, templateId);
        }
        if(StringUtils.isNotEmpty(namespaceCode)) {
        	searchCriteria.put(NAMESPACE_CODE, WILDCARD+namespaceCode+WILDCARD);
        }
        if(StringUtils.isNotEmpty(name)) {
        	searchCriteria.put(NAME, WILDCARD+name+WILDCARD);
        }
        if(StringUtils.isNotEmpty(attributeDetailValue)) {
        	searchCriteria.put(DETAIL_OBJECTS_ATTRIBUTE_VALUE, WILDCARD+attributeDetailValue+WILDCARD);
        }
        if(StringUtils.isNotEmpty(attributeDetailName)) {
        	searchCriteria.put(DETAIL_OBJECTS_ATTRIBUTE_NAME, WILDCARD+attributeDetailName+WILDCARD);
        }
        if ( StringUtils.isNotBlank( detailCriteria ) ) {
        	searchCriteria.put(DETAIL_CRITERIA, detailCriteria);
        }
        if ( StringUtils.isNotBlank( active ) ) {
        	searchCriteria.put(KRADPropertyConstants.ACTIVE, active);
        }

        return searchCriteria;
    }
    
    protected String getQueryString(String parameter){
    	if(StringUtils.isEmpty(parameter)) {
    		return WILDCARD;
        }
    	else {
    		return WILDCARD+parameter+WILDCARD;
        }
    }
    
    @SuppressWarnings({ "unchecked" })
	protected Map<String, String> buildRoleSearchCriteria(Map<String, String> fieldValues){
       	String assignedToPrincipalName = fieldValues.get(ASSIGNED_TO_PRINCIPAL_NAME);
    	Map<String, String> searchCriteria;
    	List<Principal> principals = new ArrayList<Principal>();

        if(StringUtils.isNotEmpty(assignedToPrincipalName)) { // if a principal name is specified in the search
            // KULRICE-9153: Analyze IU patch for preventing role member lookup from causing out of memory exceptions
            // Changing to exact match on principal name to prevent blowing up Rice by loading every user into memory
            if (assignedToPrincipalName.contains("*")) {
                return null; // bail out, wild cards are not allowed since
                             // IdentityServiceImpl.getPrincipalByPrincipalName has weird behavior around wildcards
            }

            Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(assignedToPrincipalName);

            if (principal == null) {
                return null; // bail out, if no principal matched and a principal name was supplied, then there will be
                             // no valid matching roles.
            }

            principals.add(principal);
        }

        String assignedToGroupNamespaceCode = fieldValues.get(ASSIGNED_TO_GROUP_NAMESPACE_CODE);
        String assignedToGroupName = fieldValues.get(ASSIGNED_TO_GROUP_NAME);
        List<Group> groups = null;
        if(StringUtils.isNotEmpty(assignedToGroupNamespaceCode) && StringUtils.isEmpty(assignedToGroupName) ||
        		StringUtils.isEmpty(assignedToGroupNamespaceCode) && StringUtils.isNotEmpty(assignedToGroupName) ||
        		StringUtils.isNotEmpty(assignedToGroupNamespaceCode) && StringUtils.isNotEmpty(assignedToGroupName)){
            QueryByCriteria.Builder builder = QueryByCriteria.Builder.create();
        	builder.setPredicates(and(
                            like(NAMESPACE_CODE, getQueryString(assignedToGroupNamespaceCode)),
                            like(GROUP_NAME, getQueryString(assignedToGroupName))));
        	GroupQueryResults qr = KimApiServiceLocator.getGroupService().findGroups(builder.build());
        	if (qr.getResults() == null || qr.getResults().isEmpty()) {
        		return null;
            }
            groups = qr.getResults();
        }

        String assignedToRoleNamespaceCode = fieldValues.get(ASSIGNED_TO_NAMESPACE_FOR_LOOKUP);
        String assignedToRoleName = fieldValues.get(ASSIGNED_TO_ROLE_NAME);

    	searchCriteria = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(assignedToRoleNamespaceCode)) {
        	searchCriteria.put(ASSIGNED_TO_ROLE_NAMESPACE_CODE, WILDCARD+assignedToRoleNamespaceCode+WILDCARD);
        }
        if(StringUtils.isNotEmpty(assignedToRoleName)) {
        	searchCriteria.put(ASSIGNED_TO_ROLE_ROLE_NAME, WILDCARD+assignedToRoleName+WILDCARD);
        }

    	StringBuffer memberQueryString = null;
        if(!principals.isEmpty()) {
        	memberQueryString = new StringBuffer();
        	for(Principal principal: principals){
        		memberQueryString.append(principal.getPrincipalId()+KimConstants.KimUIConstants.OR_OPERATOR);
        	}
            if(memberQueryString.toString().endsWith(KimConstants.KimUIConstants.OR_OPERATOR)) {
            	memberQueryString.delete(memberQueryString.length()-KimConstants.KimUIConstants.OR_OPERATOR.length(), memberQueryString.length());
            }
        }
        if(groups!=null){
        	if(memberQueryString==null) {
        		memberQueryString = new StringBuffer();
            }
        	else if(StringUtils.isNotEmpty(memberQueryString.toString())) {
        		memberQueryString.append(KimConstants.KimUIConstants.OR_OPERATOR);
            }
        	for(Group group: groups){
        		memberQueryString.append(group.getId()+KimConstants.KimUIConstants.OR_OPERATOR);
        	}
            if(memberQueryString.toString().endsWith(KimConstants.KimUIConstants.OR_OPERATOR)) {
            	memberQueryString.delete(memberQueryString.length()-KimConstants.KimUIConstants.OR_OPERATOR.length(), memberQueryString.length());
            }
        	searchCriteria.put(ASSIGNED_TO_ROLE_MEMBER_ID, memberQueryString.toString());
        }
        if (memberQueryString!=null && StringUtils.isNotEmpty(memberQueryString.toString())) {
        	searchCriteria.put(ASSIGNED_TO_ROLE_MEMBER_ID, memberQueryString.toString());
        }

        return searchCriteria;
    }

    
    /** Checks whether the 2nd map is a subset of the first. */
	protected boolean isMapSubset( Map<String, String> mainMap, Map<String, String> subsetMap ) {
		for ( Map.Entry<String, String> keyValue : subsetMap.entrySet() ) {
			if ( !mainMap.containsKey(keyValue.getKey()) 
					|| !StringUtils.equals( mainMap.get(keyValue.getKey()), keyValue.getValue() ) ) {
//				if ( LOG.isDebugEnabled() ) {
//					LOG.debug( "Maps did not match:\n" + mainMap + "\n" + subsetMap );
//				}
				return false;
			}
		}
//		if ( LOG.isDebugEnabled() ) {
//			LOG.debug( "Maps matched:\n" + mainMap + "\n" + subsetMap );
//		}
		return true;
	}

	/** Converts a special criteria string that is in the form key=value,key2=value2 into a map */
	protected Map<String, String> parseDetailCriteria( String detailCritiera ) {
	    if ( StringUtils.isBlank(detailCritiera) ) {
	        return new HashMap<String, String>(0);
	    }
		String[] keyValuePairs = StringUtils.split(detailCritiera, ',');
		if ( keyValuePairs == null || keyValuePairs.length == 0 ) {
		    return new HashMap<String, String>(0);
		}
		Map<String, String> parsedDetails = new HashMap<String, String>( keyValuePairs.length );
		for ( String keyValueStr : keyValuePairs ) {
			String[] keyValue = StringUtils.split(keyValueStr, '=');
			if ( keyValue.length >= 2 ) {
				parsedDetails.put(keyValue[0], keyValue[1]);
			}
		}
		return parsedDetails;
	}
	
	
	@Override
	public List<Row> getRows() {
		List<Row> rows = super.getRows();
		Iterator<Row> i = rows.iterator();
		while ( i.hasNext() ) {
			Row row = i.next();
			int numFieldsRemoved = 0;
			boolean rowIsBlank = true;
			for (Iterator<Field> fieldIter = row.getFields().iterator(); fieldIter.hasNext();) {
				Field field = fieldIter.next();
				String propertyName = field.getPropertyName();
				if ( propertyName.equals(DETAIL_CRITERIA) ) {
					Object val = getParameters().get( propertyName );
					String propVal = null;
					if ( val != null ) {
						propVal = (val instanceof String)?(String)val:((String[])val)[0];
					}
					if ( StringUtils.isBlank( propVal ) ) {
						fieldIter.remove();
						numFieldsRemoved++;
					} else {
						field.setReadOnly(true);
						rowIsBlank = false;
						// leaving this in would prevent the "clear" button from resetting this value
//						field.setDefaultValue( propVal );
					}
				} else if (!Field.BLANK_SPACE.equals(field.getFieldType())) {
					rowIsBlank = false;
				}
			}
			// Check if any fields were removed from the row.
			if (numFieldsRemoved > 0) {
				// If fields were removed, check whether or not the remainder of the row is empty or only has blank space fields.
				if (rowIsBlank) {
					// If so, then remove the row entirely.
					i.remove();
				} else {
					// Otherwise, add one blank space for each field that was removed, using a technique similar to FieldUtils.createBlankSpace.
					// It is safe to just add blank spaces as needed, since the removable field is the last of the visible ones in the list (or
					// at least for the Permission and Responsibility lookups).
					while (numFieldsRemoved > 0) {
						Field blankSpace = new Field();
						blankSpace.setFieldType(Field.BLANK_SPACE);
						blankSpace.setPropertyName(Field.BLANK_SPACE);
						row.getFields().add(blankSpace);
						numFieldsRemoved--;
					}
				}
			}
		}
		return rows;
	}
    
	protected Long getActualSizeIfTruncated(List result){
		Long actualSizeIfTruncated = new Long(0); 
		if(result instanceof CollectionIncomplete) {
			actualSizeIfTruncated = ((CollectionIncomplete)result).getActualSizeIfTruncated();
        }
		return actualSizeIfTruncated;
	}
	
	@SuppressWarnings("unchecked")
	protected List<RoleBo> searchRoles(Map<String, String> roleSearchCriteria, boolean unbounded){
		List<RoleBo> roles = (List<RoleBo>)getLookupService().findCollectionBySearchHelper(
				RoleBo.class, roleSearchCriteria, unbounded);
		String membersCrt = roleSearchCriteria.get("members.memberId");
		List<RoleBo> roles2Remove = new ArrayList<RoleBo>();
		if(StringUtils.isNotBlank(membersCrt)){
			List<String> memberSearchIds = new ArrayList<String>();
			List<String> memberIds = new ArrayList<String>(); 
			if(membersCrt.contains(KimConstants.KimUIConstants.OR_OPERATOR)) {
				memberSearchIds = new ArrayList<String>(Arrays.asList(membersCrt.split("\\|")));
            }
			else {
				memberSearchIds.add(membersCrt);
            }
			for(RoleBo roleBo : roles){
				List<RoleMemberBo> roleMembers = roleBo.getMembers();
				memberIds.clear(); 
		        CollectionUtils.filter(roleMembers, new Predicate() {
					public boolean evaluate(Object object) {
						RoleMemberBo member = (RoleMemberBo) object;
						// keep active member
						return member.isActive(new Timestamp(System.currentTimeMillis()));
					}
				});
		       
		        if(roleMembers != null && !roleMembers.isEmpty()){
		        	for(RoleMemberBo memberImpl : roleMembers) {
		        		memberIds.add(memberImpl.getMemberId());
                    }
		        	if(((List<String>)CollectionUtils.intersection(memberSearchIds, memberIds)).isEmpty()) {
		        		roles2Remove.add(roleBo);
                    }
		        }
		        else
		        {
		        	roles2Remove.add(roleBo);
		        }
			}
		}
		if(!roles2Remove.isEmpty()) {
			roles.removeAll(roles2Remove);
        }
		return roles;
	}


}
