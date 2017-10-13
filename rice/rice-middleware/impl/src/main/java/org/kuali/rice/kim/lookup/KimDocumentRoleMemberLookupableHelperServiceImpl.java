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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimAttributeField;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeInfoService;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleQualifier;
import org.kuali.rice.kim.document.IdentityManagementRoleDocument;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.type.KimTypeAttributesHelper;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.service.UiDocumentService;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KimDocumentRoleMemberLookupableHelperServiceImpl extends KualiLookupableHelperServiceImpl {

    private static final long serialVersionUID = 1L;
    private transient UiDocumentService uiDocumentService;
    private transient RoleService roleService;
    private transient KimTypeInfoService kimTypeInfoService;

    @SuppressWarnings("unchecked")
    protected List<? extends BusinessObject> getSearchResultsHelper(Map<String, String> fieldValues, boolean unbounded) {
        List<KimDocumentRoleMember> searchResults = new ArrayList<KimDocumentRoleMember>();
        IdentityManagementRoleDocument roleDocument = (IdentityManagementRoleDocument) GlobalVariables.getUserSession().retrieveObject(KimConstants.KimUIConstants.KIM_ROLE_DOCUMENT_SHORT_KEY);
        if (roleDocument != null) {
            String memberId = fieldValues.get(KimConstants.PrimaryKeyConstants.MEMBER_ID);
            String memberTypeCode = fieldValues.get(KIMPropertyConstants.KimMember.MEMBER_TYPE_CODE);
            String memberName = fieldValues.get(KimConstants.KimUIConstants.MEMBER_NAME);
            String memberNamespaceCode = fieldValues.get(KimConstants.KimUIConstants.MEMBER_NAMESPACE_CODE);
            String activeFromDate = fieldValues.get(KIMPropertyConstants.KimMember.ACTIVE_FROM_DATE);
            String activeToDate = fieldValues.get(KIMPropertyConstants.KimMember.ACTIVE_TO_DATE);
            List<KimDocumentRoleMember> currentRoleMembers = roleDocument.getMembers();
            if (currentRoleMembers != null && !currentRoleMembers.isEmpty()) {
                for (KimDocumentRoleMember currentRoleMember : currentRoleMembers) {
                    if ((StringUtils.isEmpty(memberId) || (StringUtils.isNotEmpty(memberId) && memberId.equals(currentRoleMember.getMemberId())))
                            && (StringUtils.isEmpty(memberTypeCode) || (StringUtils.isNotEmpty(memberTypeCode) && memberTypeCode.equals(currentRoleMember.getMemberTypeCode())))
                            && (StringUtils.isEmpty(memberName) || (StringUtils.isNotEmpty(memberName) && memberName.equals(currentRoleMember.getMemberName())))
                            && (StringUtils.isEmpty(memberNamespaceCode) || (StringUtils.isNotEmpty(memberNamespaceCode) && memberNamespaceCode.equals(currentRoleMember.getMemberNamespaceCode())))
                            && (StringUtils.isEmpty(activeFromDate) || (StringUtils.isNotEmpty(activeFromDate) && activeFromDate.equals(currentRoleMember.getActiveFromDate())))
                            && (StringUtils.isEmpty(activeToDate) || (StringUtils.isNotEmpty(activeToDate) && activeToDate.equals(currentRoleMember.getActiveToDate())))) {
                        searchResults.add(currentRoleMember);
                    }
                }
            }
        } else {
            searchResults = getUiDocumentService().getRoleMembers(fieldValues);
        }
        if (searchResults != null) {
            for (KimDocumentRoleMember roleMember : searchResults) {
                roleMember.setQualifiersToDisplay(getQualifiersToDisplay(roleMember));
            }
        }
        return searchResults;
    }

    public String getQualifiersToDisplay(KimDocumentRoleMember roleMember) {
        if (roleMember != null && StringUtils.isNotEmpty(roleMember.getRoleId()) &&
                roleMember.getQualifiers() != null && !roleMember.getQualifiers().isEmpty()) {
            Role role = getRoleService().getRole(roleMember.getRoleId());
            KimType kimType = null;
            if (role != null) {
                kimType = getKimTypeInfoService().getKimType(role.getKimTypeId());
            }
            if (kimType != null) {
                KimTypeAttributesHelper attributesHelper = new KimTypeAttributesHelper(kimType);
                StringBuffer attributesToDisplay = new StringBuffer();
                KimAttributeField attribDefn;
                for (KimDocumentRoleQualifier attribute : roleMember.getQualifiers()) {
                    if (attribute.getKimAttribute() != null) {
                        attribDefn = attributesHelper.getAttributeDefinition(attribute.getKimAttribute().getAttributeName());
                        attributesToDisplay.append(attribDefn != null ? attribDefn.getAttributeField().getLongLabel() : "");
                        attributesToDisplay.append(KimConstants.KimUIConstants.NAME_VALUE_SEPARATOR);
                        attributesToDisplay.append(attribute.getAttrVal());
                        attributesToDisplay.append(KimConstants.KimUIConstants.COMMA_SEPARATOR);
                    }
                }
                return stripEnd(attributesToDisplay.toString(), KimConstants.KimUIConstants.COMMA_SEPARATOR);
            }
        }
        return "";
    }

    private static String stripEnd(String toStripFrom, String toStrip){
		String stripped;
		if(toStripFrom==null) return null;
		if(toStrip==null) return toStripFrom;
        if(toStrip.length() > toStripFrom.length()) return toStripFrom;
		if(toStripFrom.endsWith(toStrip)){
			StringBuffer buffer = new StringBuffer(toStripFrom);
			buffer.delete(buffer.length()-toStrip.length(), buffer.length());
			stripped = buffer.toString();
		} else stripped = toStripFrom;
		return stripped;
	}

    public RoleService getRoleService() {
        if (roleService == null) {
            roleService = KimApiServiceLocator.getRoleService();
        }
        return roleService;
    }

    public KimTypeInfoService getKimTypeInfoService() {
        if (kimTypeInfoService == null) {
            kimTypeInfoService = KimApiServiceLocator.getKimTypeInfoService();
        }
        return kimTypeInfoService;
    }

    public UiDocumentService getUiDocumentService() {
        if (uiDocumentService == null) {
            uiDocumentService = KIMServiceLocatorInternal.getUiDocumentService();
        }
        return uiDocumentService;
    }
}
