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
package org.kuali.rice.krad.devtools.maintainablexml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.criteria.PredicateUtils;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.doctype.DocumentTypeService;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.bo.DocumentTypeEBO;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.common.delegate.DelegateMemberContract;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMember;
import org.kuali.rice.kim.api.role.RoleMemberContract;
import org.kuali.rice.kim.api.role.RoleMemberQueryResults;
import org.kuali.rice.kim.api.role.RoleResponsibilityAction;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeAttribute;
import org.kuali.rice.kim.framework.group.GroupEbo;
import org.kuali.rice.kim.framework.role.RoleEbo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
public class TestReviewRole extends PersistableBusinessObjectBase implements MutableInactivatable {


    protected static final String ORR_INQUIRY_TITLE_PROPERTY = "message.inquiry.org.review.role.title";
    protected static String INQUIRY_TITLE_VALUE = null;

    //Dummy variable
    protected String organizationTypeCode = "99";
    private static final long serialVersionUID = 1L;

    public static final String REVIEW_ROLES_INDICATOR_FIELD_NAME = "reviewRolesIndicator";
    public static final String ROLE_NAME_FIELD_NAMESPACE_CODE = "roleMemberRoleNamespaceCode";
    public static final String ROLE_NAME_FIELD_NAME = "roleMemberRoleName";
    public static final String GROUP_NAME_FIELD_NAMESPACE_CODE = "groupMemberGroupNamespaceCode";
    public static final String GROUP_NAME_FIELD_NAME = "groupMemberGroupName";
    public static final String PRINCIPAL_NAME_FIELD_NAME = "principalMemberPrincipalName";
    public static final String DOC_TYPE_NAME_FIELD_NAME = "financialSystemDocumentTypeCode";
    public static final String DELEGATE_FIELD_NAME = "delegate";
    public static final String DELEGATION_TYPE_CODE = "delegationTypeCode";
    public static final String FROM_AMOUNT_FIELD_NAME = "fromAmount";
    public static final String TO_AMOUNT_FIELD_NAME = "toAmount";
    public static final String ACTION_TYPE_CODE_FIELD_NAME = "actionTypeCode";
    public static final String PRIORITY_CODE_FIELD_NAME = "priorityNumber";
    public static final String ACTION_POLICY_CODE_FIELD_NAME = "actionPolicyCode";
    public static final String FORCE_ACTION_FIELD_NAME = "forceAction";
    public static final String ACTIVE_FROM_DATE = "activeFromDate";
    public static final String ACTIVE_TO_DATE = "activeToDate";

    public static final String ORIGINAL_DELEGATION_MEMBER_ID_TO_MODIFY = "ODelMId";
    public static final String ORIGINAL_ROLE_MEMBER_ID_TO_MODIFY = "ORMId";

    public static final String NEW_DELEGATION_ID_KEY_VALUE = "New";

    protected String methodToCall;
    protected String kimTypeId;

    protected String orgReviewRoleMemberId;
    protected boolean edit;
    protected boolean copy;

    protected RoleEbo role;
    protected GroupEbo group;
    protected Person person;

    protected List<RoleResponsibilityAction> roleRspActions = new ArrayList<RoleResponsibilityAction>();

    //Identifying information for the 3 kinds of role members this document caters to
    protected String roleMemberRoleId;
    protected String roleMemberRoleNamespaceCode;
    protected String roleMemberRoleName;

    protected String groupMemberGroupId;
    protected String groupMemberGroupNamespaceCode;
    protected String groupMemberGroupName;

    protected String principalMemberPrincipalId;
    protected String principalMemberPrincipalName;
    protected String principalMemberName;

    //The role id this object corresponds to ( org review / acct review )
    protected String roleId;
    protected String namespaceCode;
    protected String roleName;

    //Identifying information for a single member (of any type)
    protected String memberTypeCode;

    //In case the document is dealing with delegations
    protected String delegationTypeCode;

    protected String delegationMemberId;
    protected String roleMemberId;

    protected String oDelMId;
    protected String oRMId;

    protected String financialSystemDocumentTypeCode;
    protected DocumentTypeEBO financialSystemDocumentType;
    protected List<String> roleNamesToConsider;
    protected String reviewRolesIndicator;

    protected String actionTypeCode;
    protected String priorityNumber;
    protected String actionPolicyCode;
    protected boolean forceAction;
    protected String chartOfAccountsCode;
    protected String organizationCode;
    protected KualiDecimal fromAmount;
    protected KualiDecimal toAmount;
    protected String overrideCode;
    protected boolean active = true;
    protected boolean delegate;

    protected Date activeFromDate;
    protected Date activeToDate;

    /**
     * Gets the active attribute.
     * @return Returns the active.
     */
    @Override
    public boolean isActive() {
        return active;
    }
    /**
     * Sets the active attribute value.
     * @param active The active to set.
     */
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
    /**
     * Gets the delegate attribute.
     * @return Returns the delegate.
     */
    public boolean isDelegate() {
        return delegate;
    }
    /**
     * Sets the delegate attribute value.
     * @param delegate The delegate to set.
     */
    public void setDelegate(boolean delegate) {
        this.delegate = delegate;
    }

    /**
     * Gets the groupMemberGroupId attribute.
     * @return Returns the groupMemberGroupId.
     */
    public String getGroupMemberGroupId() {
        if ( StringUtils.isBlank(groupMemberGroupId) ) {
            if ( StringUtils.isNotBlank(groupMemberGroupNamespaceCode) && StringUtils.isNotBlank(groupMemberGroupName) ) {
                getGroup();
            }
        }
        return groupMemberGroupId;
    }
    /**
     * Sets the groupMemberGroupId attribute value.
     * @param groupMemberGroupId The groupMemberGroupId to set.
     */
    public void setGroupMemberGroupId(String groupMemberGroupId) {
        this.groupMemberGroupId = groupMemberGroupId;
    }
    /**
     * Gets the groupMemberGroupName attribute.
     * @return Returns the groupMemberGroupName.
     */
    public String getGroupMemberGroupName() {
        return groupMemberGroupName;
    }
    /**
     * Sets the groupMemberGroupName attribute value.
     * @param groupMemberGroupName The groupMemberGroupName to set.
     */
    public void setGroupMemberGroupName(String groupMemberGroupName) {
        this.groupMemberGroupName = groupMemberGroupName;
    }
    /**
     * Gets the groupMemberGroupNamespaceCode attribute.
     * @return Returns the groupMemberGroupNamespaceCode.
     */
    public String getGroupMemberGroupNamespaceCode() {
        return groupMemberGroupNamespaceCode;
    }
    /**
     * Sets the groupMemberGroupNamespaceCode attribute value.
     * @param groupMemberGroupNamespaceCode The groupMemberGroupNamespaceCode to set.
     */
    public void setGroupMemberGroupNamespaceCode(String groupMemberGroupNamespaceCode) {
        this.groupMemberGroupNamespaceCode = groupMemberGroupNamespaceCode;
    }
    /**
     * Gets the principalMemberPrincipalId attribute.
     * @return Returns the principalMemberPrincipalId.
     */
    public String getPrincipalMemberPrincipalId() {
        if ( StringUtils.isBlank(principalMemberPrincipalId) ) {
            if ( StringUtils.isNotBlank(principalMemberPrincipalName) ) {
                getPerson();
            }
        }
        return principalMemberPrincipalId;
    }
    /**
     * Sets the principalMemberPrincipalId attribute value.
     * @param principalMemberPrincipalId The principalMemberPrincipalId to set.
     */
    public void setPrincipalMemberPrincipalId(String principalMemberPrincipalId) {
        this.principalMemberPrincipalId = principalMemberPrincipalId;
    }
    /**
     * Gets the principalMemberPrincipalName attribute.
     * @return Returns the principalMemberPrincipalName.
     */
    public String getPrincipalMemberPrincipalName() {
        if ( StringUtils.isBlank(principalMemberPrincipalName) ) {
            getPerson();
        }
        return principalMemberPrincipalName;
    }

    public String getPrincipalMemberName() {
        if ( StringUtils.isBlank(principalMemberName) ) {
            getPerson();
        }
        return principalMemberName;
    }

    /**
     * Sets the principalMemberPrincipalName attribute value.
     * @param principalMemberPrincipalName The principalMemberPrincipalName to set.
     */
    public void setPrincipalMemberPrincipalName(String principalMemberPrincipalName) {
        this.principalMemberPrincipalName = principalMemberPrincipalName;
    }
    /**
     * Gets the roleMemberRoleId attribute.
     * @return Returns the roleMemberRoleId.
     */
    public String getRoleMemberRoleId() {
        if ( StringUtils.isBlank(roleMemberRoleId) ) {
            if ( StringUtils.isNotBlank(roleMemberRoleName) && StringUtils.isNotBlank(roleMemberRoleName) ) {
                getRole();
            }
        }
        return roleMemberRoleId;
    }
    /**
     * Sets the roleMemberRoleId attribute value.
     * @param roleMemberRoleId The roleMemberRoleId to set.
     */
    public void setRoleMemberRoleId(String roleMemberRoleId) {
        this.roleMemberRoleId = roleMemberRoleId;
    }
    /**
     * Gets the roleMemberRoleName attribute.
     * @return Returns the roleMemberRoleName.
     */
    public String getRoleMemberRoleName() {
        return roleMemberRoleName;
    }
    /**
     * Sets the roleMemberRoleName attribute value.
     * @param roleMemberRoleName The roleMemberRoleName to set.
     */
    public void setRoleMemberRoleName(String roleMemberRoleName) {
        this.roleMemberRoleName = roleMemberRoleName;
    }
    /**
     * Gets the roleMemberRoleNamespaceCode attribute.
     * @return Returns the roleMemberRoleNamespaceCode.
     */
    public String getRoleMemberRoleNamespaceCode() {
        return roleMemberRoleNamespaceCode;
    }
    /**
     * Sets the roleMemberRoleNamespaceCode attribute value.
     * @param roleMemberRoleNamespaceCode The roleMemberRoleNamespaceCode to set.
     */
    public void setRoleMemberRoleNamespaceCode(String roleMemberRoleNamespaceCode) {
        this.roleMemberRoleNamespaceCode = roleMemberRoleNamespaceCode;
    }

    /**
     * Gets the overrideCode attribute.
     * @return Returns the overrideCode.
     */
    public String getOverrideCode() {
        return this.overrideCode;
    }
    /**
     * Sets the overrideCode attribute value.
     * @param overrideCode The overrideCode to set.
     */
    public void setOverrideCode(String overrideCode) {
        this.overrideCode = overrideCode;
    }

    /**
     * Gets the fromAmount attribute.
     * @return Returns the fromAmount.
     */
    public KualiDecimal getFromAmount() {
        return fromAmount;
    }

    public String getFromAmountStr() {
        return fromAmount==null?null:fromAmount.toString();
    }

    /**
     * Sets the fromAmount attribute value.
     * @param fromAmount The fromAmount to set.
     */
    public void setFromAmount(KualiDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public void setFromAmount(String fromAmount) {
        if(StringUtils.isNotEmpty(fromAmount) && NumberUtils.isNumber( fromAmount ) ) {
            this.fromAmount = new KualiDecimal(fromAmount);
        }
        else {
            this.fromAmount = null;
        }
    }

    /**
     * Gets the toAmount attribute.
     * @return Returns the toAmount.
     */
    public KualiDecimal getToAmount() {
        return toAmount;
    }

    public String getToAmountStr() {
        return toAmount==null?null:toAmount.toString();
    }

    /**
     * Sets the toAmount attribute value.
     * @param toAmount The toAmount to set.
     */
    public void setToAmount(KualiDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public void setToAmount(String toAmount) {
        if(StringUtils.isNotEmpty(toAmount) && NumberUtils.isNumber( toAmount ) ) {
            this.toAmount = new KualiDecimal(toAmount);
        }
        else {
            this.toAmount = null;
        }
    }

    /**
     * Gets the activeFromDate attribute.
     * @return Returns the activeFromDate.
     */
    public Date getActiveFromDate() {
        return activeFromDate;
    }
    /**
     * Sets the activeFromDate attribute value.
     * @param activeFromDate The activeFromDate to set.
     */
    public void setActiveFromDate(Date activeFromDate) {
        this.activeFromDate = activeFromDate;
    }

    /**
     * Gets the activeToDate attribute.
     * @return Returns the activeToDate.
     */
    public Date getActiveToDate() {
        return activeToDate;
    }
    /**
     * Sets the activeToDate attribute value.
     * @param activeToDate The activeToDate to set.
     */
    public void setActiveToDate(Date activeToDate) {
        this.activeToDate = activeToDate;
    }

    /**
     * Gets the orgReviewRoleMemberId attribute.
     * @return Returns the orgReviewRoleMemberId.
     */
    public String getOrgReviewRoleMemberId() {
        return orgReviewRoleMemberId;
    }
    /**
     * Sets the orgReviewRoleMemberId attribute value.
     * @param orgReviewRoleMemberId The orgReviewRoleMemberId to set.
     */
    public void setOrgReviewRoleMemberId(String orgReviewRoleMemberId) {
        this.orgReviewRoleMemberId = orgReviewRoleMemberId;
    }

    @Override
    public void refresh() {}


    /**
     * Gets the financialDocumentTypeCode attribute.
     * @return Returns the financialDocumentTypeCode.
     */
    public String getFinancialSystemDocumentTypeCode() {
        return financialSystemDocumentTypeCode;
    }

    public void setFinancialSystemDocumentType(DocumentTypeEBO financialSystemDocumentType) {
        this.financialSystemDocumentType = financialSystemDocumentType;
    }
    /**
     * Gets the delegationTypeCode attribute.
     * @return Returns the delegationTypeCode.
     */
    public String getDelegationTypeCode() {
        return delegationTypeCode;
    }

    public String getDelegationTypeCodeDescription() {
        if ( getDelegationType() != null ) {
            return getDelegationType().getLabel();
        }
        return "";
    }

    public DelegationType getDelegationType() {
        return DelegationType.parseCode(delegationTypeCode);
    }

    /**
     * Sets the delegationTypeCode attribute value.
     * @param delegationTypeCode The delegationTypeCode to set.
     */
    public void setDelegationTypeCode(String delegationTypeCode) {
        this.delegationTypeCode = delegationTypeCode;
    }

    /**
     * Gets the memberTypeCode attribute.
     * @return Returns the memberTypeCode.
     */
    public String getMemberTypeCodeDescription() {
        return KimConstants.KimUIConstants.KIM_MEMBER_TYPES_MAP.get(memberTypeCode);
    }
    /**
     * Sets the memberTypeCode attribute value.
     * @param memberTypeCode The memberTypeCode to set.
     */
    public void setMemberTypeCode(String memberTypeCode) {
        this.memberTypeCode = memberTypeCode;
    }
    /**
     * Sets the attributes attribute value.
     * @param attributes The attributes to set.
     */

    /**
     * Gets the chartCode attribute.
     * @return Returns the chartCode.
     */
    public String getChartOfAccountsCode() {
        return this.chartOfAccountsCode;
    }
    /**
     * Gets the organizationCode attribute.
     * @return Returns the organizationCode.
     */
    public String getOrganizationCode() {
        return this.organizationCode;
    }
    /**
     * Sets the organizationCode attribute value.
     * @param organizationCode The organizationCode to set.
     */
    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }
    /**
     * Sets the chartOfAccountsCode attribute value.
     * @param chartOfAccountsCode The chartOfAccountsCode to set.
     */
    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }
    /**
     * Gets the roleNamesToConsider attribute.
     * @return Returns the roleNamesToConsider.
     */
    public List<String> getRoleNamesToConsider() {
        if(roleNamesToConsider==null && getFinancialSystemDocumentTypeCode()!=null) {
        }
        return roleNamesToConsider;
    }
    public void setRoleNamesToConsider(List<String> narrowedDownRoleNames) {
        roleNamesToConsider = new ArrayList<String>( narrowedDownRoleNames );
    }

    /**
     * Gets the actionTypeCode attribute.
     * @return Returns the actionTypeCode.
     */
    public String getActionTypeCode() {
        return actionTypeCode;
    }

    public String getActionTypeCodeToDisplay() {
        if(roleRspActions==null || roleRspActions.isEmpty()) {
            return "";
        }
        return roleRspActions.get(0).getActionTypeCode();
    }

    /**
     *
     * This method fore readonlyalterdisplay
     *
     * @return
     */
    public String getActionTypeCodeDescription() {
        ActionType at = ActionType.fromCode(getActionTypeCodeToDisplay(), true);
        return (at==null)?"":at.getLabel();
    }

    /**
     * Sets the actionTypeCode attribute value.
     * @param actionTypeCode The actionTypeCode to set.
     */
    public void setActionTypeCode(String actionTypeCode) {
        this.actionTypeCode = actionTypeCode;
    }
    /**
     * Gets the priorityNumber attribute.
     * @return Returns the priorityNumber.
     */
    public String getPriorityNumber() {
        return priorityNumber;
    }

    public String getPriorityNumberToDisplay() {
        if(roleRspActions==null || roleRspActions.isEmpty() ) {
            return "";
        }
        return roleRspActions.get(0).getPriorityNumber()==null?"":roleRspActions.get(0).getPriorityNumber()+"";
    }

    /**
     * Sets the priorityNumber attribute value.
     * @param priorityNumber The priorityNumber to set.
     */
    public void setPriorityNumber(String priorityNumber) {
        this.priorityNumber = priorityNumber;
    }
    /**
     * Gets the actionPolicyCode attribute.
     * @return Returns the actionPolicyCode.
     */
    public String getActionPolicyCode() {
        return actionPolicyCode;
    }
    /**
     * Sets the actionPolicyCode attribute value.
     * @param actionPolicyCode The actionPolicyCode to set.
     */
    public void setActionPolicyCode(String actionPolicyCode) {
        this.actionPolicyCode = actionPolicyCode;
    }
    /**
     * Gets the ignorePrevious attribute.
     * @return Returns the ignorePrevious.
     */
    public boolean isForceAction() {
        return forceAction;
    }

    public void setForceAction(boolean forceAction) {
        this.forceAction = forceAction;
    }

    /**
     * Gets the roleId attribute.
     * @return Returns the roleId.
     */
    public String getRoleId() {
        return roleId;
    }
    /**
     * Sets the roleId attribute value.
     * @param roleId The roleId to set.
     */
    public void setRoleId(String roleId) {
        Role roleInfo = KimApiServiceLocator.getRoleService().getRole(roleId);
        if ( roleInfo != null ) {
            setNamespaceCode(roleInfo.getNamespaceCode());
            setRoleName(roleInfo.getName());
            setKimTypeId(roleInfo.getKimTypeId());
        }
        this.roleId = roleId;
    }
    /**
     * Gets the reviewRolesIndicator attribute.
     * @return Returns the reviewRolesIndicator.
     */
    public String getReviewRolesIndicator() {
        return reviewRolesIndicator;
    }
    /**
     * Sets the reviewRolesIndicator attribute value.
     * @param reviewRolesIndicator The reviewRolesIndicator to set.
     */
    public void setReviewRolesIndicator(String reviewRolesIndicator) {
        this.reviewRolesIndicator = reviewRolesIndicator;
    }
    /**
     * Sets the reviewRolesIndicator attribute value.
     * @param reviewRolesIndicator The reviewRolesIndicator to set.
     */
    private void setReviewRolesIndicatorOnDocTypeChange(String reviewRolesIndicator) {
        this.reviewRolesIndicator = reviewRolesIndicator;
    }


    public boolean hasRole(){
        getRole();
        return StringUtils.isNotBlank(roleMemberRoleName);
    }

    public boolean hasGroup(){
        getGroup();
        return StringUtils.isNotBlank(groupMemberGroupName);
    }

    public boolean hasPrincipal(){
        getPerson();
        return StringUtils.isNotBlank(principalMemberPrincipalName);
    }

    public boolean hasAnyMember(){
        return hasRole() || hasGroup() || hasPrincipal();
    }

    public void setRoleMember( RoleMemberContract roleMember ) {
        memberTypeCode = roleMember.getType().getCode();
        if(MemberType.ROLE.equals(roleMember.getType())){
            roleMemberRoleId = roleMember.getMemberId();
            roleMemberRoleNamespaceCode = roleMember.getMemberNamespaceCode();
            roleMemberRoleName = roleMember.getMemberName();
        } else if(MemberType.GROUP.equals(roleMember.getType())){
            groupMemberGroupId = roleMember.getMemberId();
            groupMemberGroupNamespaceCode = roleMember.getMemberNamespaceCode();
            groupMemberGroupName = roleMember.getMemberName();
        } else if(MemberType.PRINCIPAL.equals(roleMember.getType())){
            principalMemberPrincipalId = roleMember.getMemberId();
            principalMemberPrincipalName = roleMember.getMemberName();
        }

        if ( roleMember.getActiveFromDate() != null ) {
            setActiveFromDate(roleMember.getActiveFromDate().toDate());
        } else {
            setActiveFromDate( null );
        }
        if ( roleMember.getActiveToDate() != null ) {
            setActiveToDate(roleMember.getActiveToDate().toDate());
        } else {
            setActiveToDate( null );
        }
        setActive(roleMember.isActive());

        setRoleMemberId(roleMember.getId());
        setDelegate(false);
        setRoleId(roleMember.getRoleId());

        setRoleRspActions(KimApiServiceLocator.getRoleService().getRoleMemberResponsibilityActions(roleMember.getId()));
    }


    public void setDelegateMember( RoleMemberContract roleMember, DelegateMemberContract delegateMember ) {
        if ( roleMember == null ) {
            roleMember = getRoleMemberFromKimRoleService( delegateMember.getRoleMemberId() );
        }
        setRoleId( roleMember.getRoleId() );
        memberTypeCode = delegateMember.getType().getCode();
        if(MemberType.ROLE.equals(delegateMember.getType())){
            roleMemberRoleId = delegateMember.getMemberId();
            getRole();
        } else if(MemberType.GROUP.equals(delegateMember.getType())){
            groupMemberGroupId = delegateMember.getMemberId();
            getGroup();
        } else if(MemberType.PRINCIPAL.equals(delegateMember.getType())){
            principalMemberPrincipalId = delegateMember.getMemberId();
            getPerson();
        }

        if ( delegateMember.getActiveFromDate() != null ) {
            setActiveFromDate(delegateMember.getActiveFromDate().toDate());
        }
        if ( delegateMember.getActiveToDate() != null ) {
            setActiveToDate(delegateMember.getActiveToDate().toDate());
        }
        setActive(delegateMember.isActive());
        setDelegate(true);
        setDelegationMemberId(delegateMember.getDelegationMemberId());
        setRoleMemberId(roleMember.getId());

    }

    protected RoleMember getRoleMemberFromKimRoleService( String roleMemberId ) {
        RoleMemberQueryResults roleMembers = KimApiServiceLocator.getRoleService().findRoleMembers(QueryByCriteria.Builder.fromPredicates( PredicateUtils.convertMapToPredicate(Collections.singletonMap(KimConstants.PrimaryKeyConstants.ID, roleMemberId))));
        if ( roleMembers == null || roleMembers.getResults() == null || roleMembers.getResults().isEmpty() ) {
            throw new IllegalArgumentException( "Unknown role member ID passed in - nothing returned from KIM RoleService: " + roleMemberId );
        }
        return roleMembers.getResults().get(0);
    }

    public String getMemberId() {
        if(MemberType.ROLE.getCode().equals(memberTypeCode)){
            return getRoleMemberRoleId();
        } else if(MemberType.GROUP.getCode().equals(memberTypeCode)){
            return getGroupMemberGroupId();
        } else if(MemberType.PRINCIPAL.getCode().equals(memberTypeCode)){
            return getPrincipalMemberPrincipalId();
        }
        return "";
    }

    public String getMemberName() {
        if(MemberType.ROLE.getCode().equals(memberTypeCode)){
            return getRoleMemberRoleName();
        } else if(MemberType.GROUP.getCode().equals(memberTypeCode)){
            return getGroupMemberGroupName();
        } else if(MemberType.PRINCIPAL.getCode().equals(memberTypeCode)){
            return getPrincipalMemberName();
        }
        return "";
    }

    public String getMemberNamespaceCode() {
        if(MemberType.ROLE.getCode().equals(memberTypeCode)){
            return getRoleMemberRoleNamespaceCode();
        } else if(MemberType.GROUP.getCode().equals(memberTypeCode)){
            return getGroupMemberGroupNamespaceCode();
        } else if(MemberType.PRINCIPAL.getCode().equals(memberTypeCode)){
            return "";
        }
        return "";
    }

    public String getMemberFieldName(){
        if(MemberType.ROLE.equals(getMemberType())) {
            return ROLE_NAME_FIELD_NAME;
        } else if(MemberType.GROUP.equals(getMemberType())) {
            return GROUP_NAME_FIELD_NAME;
        } else if(MemberType.PRINCIPAL.equals(getMemberType())) {
            return PRINCIPAL_NAME_FIELD_NAME;
        }
        return null;
    }

    /**
     * Gets the memberTypeCode attribute.
     * @return Returns the memberTypeCode.
     */
    public String getMemberTypeCode() {
        return memberTypeCode;
    }

    public MemberType getMemberType() {
        if ( StringUtils.isBlank(memberTypeCode) ) {
            return null;
        }
        return MemberType.fromCode(memberTypeCode);
    }

    /**
     * Gets the group attribute.
     * @return Returns the group.
     */
    public GroupEbo getGroup() {
        return group;
    }
    /**
     * Sets the group attribute value.
     * @param group The group to set.
     */
    public void setGroup(GroupEbo group) {
        this.group = group;
        if ( group != null ) {
            groupMemberGroupNamespaceCode = group.getNamespaceCode();
            groupMemberGroupName = group.getName();
            groupMemberGroupId = group.getId();
        } else {
            groupMemberGroupNamespaceCode = "";
            groupMemberGroupName = "";
            groupMemberGroupId = "";
        }
    }
    /**
     * Gets the person attribute.
     * @return Returns the person.
     */
    public Person getPerson() {
        if( (StringUtils.isNotEmpty(principalMemberPrincipalId)
                || StringUtils.isNotEmpty(principalMemberPrincipalName))
                &&
                (person==null || !StringUtils.equals(person.getPrincipalId(), principalMemberPrincipalId) ) ) {
            if ( StringUtils.isNotEmpty(principalMemberPrincipalId) ) {
                person = KimApiServiceLocator.getPersonService().getPerson(principalMemberPrincipalId);
            } else if ( StringUtils.isNotEmpty(principalMemberPrincipalName) ) {
                person = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(principalMemberPrincipalName);
            } else {
                person = null;
            }
            if ( person != null ) {
                principalMemberPrincipalId = person.getPrincipalId();
                principalMemberPrincipalName = person.getPrincipalName();
                principalMemberName = person.getName();
            } else {
                principalMemberPrincipalId = "";
                principalMemberName = "";
            }
        }
        return person;
    }
    /**
     * Sets the person attribute value.
     * @param person The person to set.
     */
    public void setPerson(Person person) {
        this.person = person;
        if ( person != null ) {
            principalMemberPrincipalName = person.getPrincipalName();
            principalMemberPrincipalId = person.getPrincipalId();
            principalMemberName = person.getName();
        } else {
            principalMemberPrincipalId = "";
            principalMemberPrincipalName = "";
            principalMemberName = "";
        }
    }

    /**
     * Gets the role attribute.
     * @return Returns the role.
     */
    public RoleEbo getRole() {
        return role;
    }

    public void setRole( RoleEbo role ) {
        this.role = role;
        if ( role != null ) {
            roleMemberRoleNamespaceCode = role.getNamespaceCode();
            roleMemberRoleName = role.getName();
            roleMemberRoleId = role.getId();
        } else {
            roleMemberRoleNamespaceCode = "";
            roleMemberRoleName = "";
            roleMemberRoleId = "";
        }
    }

    /**
     * Gets the copy attribute.
     * @return Returns the copy.
     */
    public boolean isCopy() {
        return copy || KRADConstants.MAINTENANCE_COPY_METHOD_TO_CALL.equalsIgnoreCase(methodToCall);
    }
    /**
     * Sets the copy attribute value.
     * @param copy The copy to set.
     */
    public void setCopy(boolean copy) {
        this.copy = copy;
    }
    /**
     * Gets the edit attribute.
     * @return Returns the edit.
     */
    public boolean isEdit() {
        return edit || KRADConstants.MAINTENANCE_EDIT_METHOD_TO_CALL.equalsIgnoreCase(methodToCall);
    }
    /**
     * Sets the edit attribute value.
     * @param edit The edit to set.
     */
    public void setEdit(boolean edit) {
        this.edit = edit;
    }
    /**
     * Gets the oDelMId attribute.
     * @return Returns the oDelMId.
     */
    public String getODelMId() {
        return oDelMId;
    }
    /**
     * Sets the oDelMId attribute value.
     * @param delMId The oDelMId to set.
     */
    public void setODelMId(String delMId) {
        oDelMId = delMId;
    }
    /**
     * Gets the oRMId attribute.
     * @return Returns the oRMId.
     */
    public String getORMId() {
        return oRMId;
    }
    /**
     * Sets the oRMId attribute value.
     * @param id The oRMId to set.
     */
    public void setORMId(String id) {
        oRMId = id;
    }
    /**
     * Gets the delegationMemberId attribute.
     * @return Returns the delegationMemberId.
     */
    public String getDelegationMemberId() {
        return delegationMemberId;
    }
    /**
     * Sets the delegationMemberId attribute value.
     * @param delegationMemberId The delegationMemberId to set.
     */
    public void setDelegationMemberId(String delegationMemberId) {
        this.delegationMemberId = delegationMemberId;
    }
    /**
     * Gets the roleMemberId attribute.
     * @return Returns the roleMemberId.
     */
    public String getRoleMemberId() {
        return roleMemberId;
    }
    /**
     * Sets the roleMemberId attribute value.
     * @param roleMemberId The roleMemberId to set.
     */
    public void setRoleMemberId(String roleMemberId) {
        this.roleMemberId = roleMemberId;
    }
    /**
     * Gets the methodToCall attribute.
     * @return Returns the methodToCall.
     */
    public String getMethodToCall() {
        return methodToCall;
    }
    /**
     * Sets the methodToCall attribute value.
     * @param methodToCall The methodToCall to set.
     */
    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }

    public boolean isEditDelegation(){
        return isEdit() && isDelegate();
    }

    public boolean isEditRoleMember(){
        return isEdit() && !isDelegate();
    }

    public boolean isCopyDelegation(){
        return isCopy() && isDelegate();
    }

    public boolean isCopyRoleMember(){
        return isCopy() && !isDelegate();
    }

    public boolean isCreateDelegation(){
        return NEW_DELEGATION_ID_KEY_VALUE.equals(getODelMId()) || (isEditDelegation() && StringUtils.isBlank(getDelegationMemberId()));
    }

    public boolean isCreateRoleMember(){
        return StringUtils.isEmpty(methodToCall);
    }

    public String getOrganizationTypeCode() {
        return "99";
    }
    public void setOrganizationTypeCode(String organizationTypeCode) {
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
        setRoleNamesToConsider( Collections.singletonList(roleName) );
    }
    public String getNamespaceCode() {
        return namespaceCode;
    }
    public void setNamespaceCode(String namespaceCode) {
        this.namespaceCode = namespaceCode;
    }

    @Override
    public Long getVersionNumber(){
        return 1L;
    }

    public String getKimTypeId() {
        return kimTypeId;
    }
    public void setKimTypeId(String kimTypeId) {
        this.kimTypeId = kimTypeId;
    }

    /**
     * Gets the roleRspActions attribute.
     * @return Returns the roleRspActions.
     */
    public List<RoleResponsibilityAction> getRoleRspActions() {
        if ( roleRspActions == null ) {
            roleRspActions = new ArrayList<RoleResponsibilityAction>(1);
        }
        return roleRspActions;
    }
    /**
     * Sets the roleRspActions attribute value.
     * @param roleRspActions The roleRspActions to set.
     */
    public void setRoleRspActions(List<RoleResponsibilityAction> roleRspActions) {
        this.roleRspActions = roleRspActions;
    }

    public String getOrgReviewRoleInquiryTitle() {
        if ( INQUIRY_TITLE_VALUE == null ) {
        }
        return INQUIRY_TITLE_VALUE;
    }

    @Override
    public void refreshNonUpdateableReferences() {
        // do nothing
    }

    @Override
    public void refreshReferenceObject(String referenceObjectName) {
        // do nothing
    }


}
