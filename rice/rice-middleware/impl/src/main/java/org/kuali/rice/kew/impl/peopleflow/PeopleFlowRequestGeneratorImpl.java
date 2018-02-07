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
package org.kuali.rice.kew.impl.peopleflow;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.util.VersionHelper;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimGroupRecipient;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.RecipientType;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContent;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowDefinition;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowDelegate;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowMember;
import org.kuali.rice.kew.api.repository.type.KewTypeDefinition;
import org.kuali.rice.kew.api.repository.type.KewTypeRepositoryService;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.framework.peopleflow.PeopleFlowTypeService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValueContent;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMembership;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.Endpoint;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Reference implementation of the {@code PeopleFlowRequestGenerator} which is responsible for generating Action
 * Requests from a {@link PeopleFlowDefinition}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PeopleFlowRequestGeneratorImpl implements PeopleFlowRequestGenerator {

    private KewTypeRepositoryService typeRepositoryService;
    private RoleService roleService;

    @Override
    public List<ActionRequestValue> generateRequests(RouteContext routeContext, PeopleFlowDefinition peopleFlow, ActionRequestType actionRequested) {
        Context context = new Context(routeContext, peopleFlow, actionRequested);
        for (PeopleFlowMember member : peopleFlow.getMembers()) {
            generateRequestForMember(context, member);
        }

        return context.getActionRequestFactory().getRequestGraphs();
    }

    protected void generateRequestForMember(Context context, PeopleFlowMember member) {
        // used later for generating any delegate requests
        List<ActionRequestValue> memberRequests = new ArrayList<ActionRequestValue>();

        if (MemberType.ROLE == member.getMemberType()) {
            memberRequests.addAll(findNonRoleRequests(generateRequestsForRoleMember(context, member)));
        } else {
            ActionRequestValue actionRequest = context.getActionRequestFactory().addRootActionRequest(
                    context.getActionRequested().getCode(), member.getPriority(), toRecipient(member), "",
                    member.getResponsibilityId(), Boolean.TRUE, getActionRequestPolicyCode(member), null);

            if (actionRequest != null) {
                memberRequests.add(actionRequest);
            }
        }

        // KULRICE-5726: Add support for delegates on roles in PeopleFlows as well as using roles as delegates
        generateDelegationRequests(context, memberRequests, member);
    }

    /**
     * generates requests for a PeopleFlow member of type Role.
     *
     * <p>Will resolve role qualifiers through the appropriate PeopleFlowTypeService, and if multiple qualifier maps are
     * generated, it can generate multiple request trees.
     * parent requests</p>
     *
     * @param context the context for request generation
     * @param member the PeopleFlow member, which is assumed to be of MemberType ROLE
     * @return a list of root action requests that were generated, or an empty list if no role members were resolved
     */
    protected List<ActionRequestValue> generateRequestsForRoleMember(Context context, PeopleFlowMember member) {
        List<ActionRequestValue> roleMemberRequests = new ArrayList<ActionRequestValue>(); // results

        List<Map<String, String>> roleQualifierMaps = loadRoleQualifiers(context, member.getMemberId());
        Role role = getRoleService().getRole(member.getMemberId());

        boolean hasPeopleFlowDelegates = !CollectionUtils.isEmpty(member.getDelegates());

        if (role == null) {
            throw new IllegalStateException("Failed to locate a role with the given role id of '" +
                    member.getMemberId() + "'");
        }

        if (CollectionUtils.isEmpty(roleQualifierMaps)) {
            ActionRequestValue request = addKimRoleRequest(context, member, role, Collections.<String, String>emptyMap(),
                    hasPeopleFlowDelegates);

            if (request != null) {
                roleMemberRequests.add(request);
            }
        } else {
            // we may have multiple maps of role qualifiers, so we'll add a request for each map
            for (Map<String, String> roleQualifiers : roleQualifierMaps) {
                ActionRequestValue request = addKimRoleRequest(context, member, role, roleQualifiers,
                        hasPeopleFlowDelegates);

                if (request != null) {
                    roleMemberRequests.add(request);
                }
            }
        }

        return roleMemberRequests;
    }

    /**
     * Uses the ActionRequestFactory to build the request tree for the role members.
     *
     * <p>The role members themselves are derived here using the given qualifiers.</p>
     *
     * @param context the context for request generation
     * @param member the PeopleFlow member
     * @param role the role specified within the member
     * @param roleQualifiers the qualifiers to use for role member selection
     * @param ignoreKimDelegates should KIM delegates be ignored when generating requests?
     * @return the root request of the generated action request tree, or null if no members are found
     */
    private ActionRequestValue addKimRoleRequest(Context context, PeopleFlowMember member, Role role,
            Map<String, String> roleQualifiers, boolean ignoreKimDelegates) {

        ActionRequestValue roleMemberRequest = null;

        List<RoleMembership> memberships = getRoleService().getRoleMembers(Collections.singletonList(
                member.getMemberId()), roleQualifiers);

        String actionRequestPolicyCode = getActionRequestPolicyCode(member);

        if (!CollectionUtils.isEmpty(memberships)) {
            roleMemberRequest = context.getActionRequestFactory().addKimRoleRequest(
                    context.getActionRequested().getCode(), member.getPriority(), role, memberships, null,
                    member.getResponsibilityId(), true, actionRequestPolicyCode, null, ignoreKimDelegates);
        }

        return roleMemberRequest;
    }

    /**
     * Generates any needed requests for {@link PeopleFlowDelegate}s on the given member.
     *
     * <p>If there are no delegates, or if no requests were generated for the member, then this will be a no-op.</p>
     *
     * @param context the context for request generation
     * @param memberRequests any action requests that were generated for the given member
     * @param member the PeopleFlow member
     */
    private void generateDelegationRequests(Context context, List<ActionRequestValue> memberRequests,
            PeopleFlowMember member) {

        if (CollectionUtils.isEmpty(member.getDelegates()) || CollectionUtils.isEmpty(memberRequests)) {
            return;
        }

        for (PeopleFlowDelegate delegate : member.getDelegates()) {
            for (ActionRequestValue memberRequest : memberRequests) {
                if (MemberType.ROLE == delegate.getMemberType()) {
                    generateDelegationToRoleRequests(context, memberRequest, member, delegate);
                } else {
                    generateDelegationToNonRoleRequest(context, memberRequest, member, delegate);
                }
            }
        }
    }

    /**
     * Uses the ActionRequestFactory to add the delegate request to the given parent request.
     *
     * <p>Only handles non-role delegates.  If a delegate of type role is passed, a RiceIllegalStateException will be
     * thrown.</p>
     *
     * @param context the context for request generation
     * @param memberRequest an action request that was generated for the given member
     * @param member the PeopleFlow member
     * @param delegate the delegate to generate a request to
     */
    private void generateDelegationToNonRoleRequest(Context context, ActionRequestValue memberRequest,
            PeopleFlowMember member, PeopleFlowDelegate delegate) {

        Recipient recipient;

        if (MemberType.PRINCIPAL == delegate.getMemberType()) {
            recipient = new KimPrincipalRecipient(delegate.getMemberId());
        } else if (MemberType.GROUP == delegate.getMemberType()) {
            recipient = new KimGroupRecipient(delegate.getMemberId());
        } else {
            throw new RiceIllegalStateException("MemberType unknown: " + delegate.getMemberType());
        }

        String actionRequestPolicyCode = getDelegateActionRequestPolicyCode(member, delegate);

        String delegationAnnotation = generateDelegationAnnotation(memberRequest, member, delegate);

        context.getActionRequestFactory().addDelegationRequest(memberRequest, recipient,
                delegate.getResponsibilityId(), memberRequest.getForceAction(),
                delegate.getDelegationType(), actionRequestPolicyCode, delegationAnnotation, null);
    }

    /**
     * Builds the String that will be used for the annotation on the delegate requests
     *
     * @param parentRequest an action request that was generated for the given member
     * @param member the PeopleFlow member
     * @param delegate the delegate
     * @return the annotation string
     */
    private String generateDelegationAnnotation(ActionRequestValue parentRequest, PeopleFlowMember member,
            PeopleFlowDelegate delegate) {

        StringBuffer annotation = new StringBuffer( "Delegation of: " );
        annotation.append( parentRequest.getAnnotation() );
        annotation.append( " to " );

        if (delegate.getMemberType() == MemberType.PRINCIPAL) {
            annotation.append( "principal " );
            Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(delegate.getMemberId());

            if ( principal != null ) {
                annotation.append( principal.getPrincipalName() );
            } else {
                annotation.append( member.getMemberId() );
            }
        } else if (delegate.getMemberType() == MemberType.GROUP) {
            annotation.append( "group " );
            Group group = KimApiServiceLocator.getGroupService().getGroup(delegate.getMemberId());

            if ( group != null ) {
                annotation.append( group.getNamespaceCode() ).append( '/' ).append( group.getName() );
            } else {
                annotation.append( member.getMemberId() );
            }
        } else {
            annotation.append( "?????? '" );
            annotation.append( member.getMemberId() );
            annotation.append( "'" );
        }

        return annotation.toString();
    }


    /**
     * Generates any needed requests for the given {@link PeopleFlowDelegate}.
     *
     * <p>It is assumed that the given member has the given delegate configured.</p>
     *
     * @param context the context for request generation
     * @param parentRequest an action request that was generated for the given member
     * @param member the PeopleFlow member, which should contain the given delegate
     * @param delegate the delegate, assumed to be of MemberType ROLE, to generate a request to
     */
    protected void generateDelegationToRoleRequests(Context context,
            ActionRequestValue parentRequest, PeopleFlowMember member, PeopleFlowDelegate delegate) {

        List<Map<String, String>> roleQualifierList = loadRoleQualifiers(context, delegate.getMemberId());
        Role role = getRoleService().getRole(delegate.getMemberId());

        if (role == null) {
            throw new IllegalStateException("Failed to locate a role with the given role id of '" +
                    delegate.getMemberId() + "'");
        }

        if (CollectionUtils.isEmpty(roleQualifierList)) {
            addKimRoleDelegateRequest(context, parentRequest, member, delegate, role,
                    Collections.<String, String>emptyMap());
        } else {
            for (Map<String, String> roleQualifiers : roleQualifierList) {
                addKimRoleDelegateRequest(context, parentRequest, member, delegate, role, roleQualifiers);
            }
        }
    }

    /**
     * Helper method uses the ActionRequestFactory to add to the parent request the delegation request(s) to a role.
     *
     * <p>The role members themselves are derived here using the given qualifiers.</p>
     *
     * @param context the context for request generation
     * @param parentRequest an action request that was generated for the given member
     * @param member the PeopleFlow member
     * @param delegate the delegate to generate a request to
     * @param role the role specified within the delegate
     * @param roleQualifiers the qualifiers to use for role member selection
     */
    private void addKimRoleDelegateRequest(Context context, ActionRequestValue parentRequest,
            PeopleFlowMember member, PeopleFlowDelegate delegate, Role role, Map<String, String> roleQualifiers) {

        // sanity check
        if (delegate.getMemberType() != MemberType.ROLE) {
            throw new RiceIllegalArgumentException("delegate's member type must be ROLE");
        } else if (!delegate.getMemberId().equals(role.getId())) {
            throw new RiceIllegalArgumentException("delegate's member id must match the given role's id");
        }

        String actionRequestPolicyCode = getDelegateActionRequestPolicyCode(member, delegate);

        List<RoleMembership> memberships = getRoleService().getRoleMembers(Collections.singletonList(
                delegate.getMemberId()), roleQualifiers);

        if (!CollectionUtils.isEmpty(memberships)) {
            context.getActionRequestFactory().addDelegateKimRoleRequest(parentRequest,
                    delegate.getDelegationType(), context.getActionRequested().getCode(), member.getPriority(), role,
                    memberships, null, delegate.getResponsibilityId(), true, actionRequestPolicyCode, null);
        }
    }

    /**
     * Uses the appropriate {@link PeopleFlowTypeService} to get the role qualifier maps for the given document and
     * role.
     *
     * <p>Note that the PeopleFlowTypeService is selected based on the type id of the PeopleFlow.</p>
     *
     * @param context the context for request generation
     * @param roleId the ID of the role for whom qualifiers are being loaded
     * @return the qualifier maps, or an empty list if there are none
     */
    protected List<Map<String, String>> loadRoleQualifiers(Context context, String roleId) {
        PeopleFlowTypeService peopleFlowTypeService = context.getPeopleFlowTypeService();
        List<Map<String, String>> roleQualifierList = new ArrayList<Map<String, String>>();

        if (peopleFlowTypeService != null) {
            Document document = DocumentRouteHeaderValue.to(context.getRouteContext().getDocument());
            DocumentRouteHeaderValueContent content = new DocumentRouteHeaderValueContent(document.getDocumentId());
            content.setDocumentContent(context.getRouteContext().getDocumentContent().getDocContent());
            DocumentContent documentContent = DocumentRouteHeaderValueContent.to(content);

            Map<String, String> roleQualifiers = peopleFlowTypeService.resolveRoleQualifiers(
                    context.getPeopleFlow().getTypeId(), roleId, document, documentContent
            );

            if (roleQualifiers != null) {
                roleQualifierList.add(roleQualifiers);
            }

            boolean versionOk = VersionHelper.compareVersion(context.getPeopleFlowTypeServiceVersion(), CoreConstants.Versions.VERSION_2_3_0) != -1;
            if(versionOk) {
                List<Map<String, String>> multipleRoleQualifiers = peopleFlowTypeService.resolveMultipleRoleQualifiers(
                        context.getPeopleFlow().getTypeId(), roleId, document, documentContent);

                if (multipleRoleQualifiers != null) {
                    roleQualifierList.addAll(multipleRoleQualifiers);
                }
            }

        }

        return roleQualifierList;
    }

    /**
     * Gets the action request policy code for the given delegate.
     *
     * <p>the delegate is considered first, and the member is used as a fallback.  May return null.</p>
     *
     * @param member the PeopleFlow member
     * @param delegate the delegate
     * @return the action request policy code, or null if none is found
     */
    private String getDelegateActionRequestPolicyCode(PeopleFlowMember member, PeopleFlowDelegate delegate) {
        ActionRequestPolicy actionRequestPolicy = delegate.getActionRequestPolicy();

        return (actionRequestPolicy != null) ? actionRequestPolicy.getCode() : getActionRequestPolicyCode(member);
    }

    /**
     * Gets the action request policy code for the given member.
     *
     * @param member the PeopleFlow member
     * @return the action request policy code, or null if none is found
     */
    private String getActionRequestPolicyCode(PeopleFlowMember member) {
        ActionRequestPolicy actionRequestPolicy = member.getActionRequestPolicy();

        return (actionRequestPolicy != null) ? actionRequestPolicy.getCode() : null;
    }

    private Recipient toRecipient(PeopleFlowMember member) {
        Recipient recipient;
        if (MemberType.PRINCIPAL == member.getMemberType()) {
            recipient = new KimPrincipalRecipient(member.getMemberId());
        } else if (MemberType.GROUP == member.getMemberType()) {
            recipient = new KimGroupRecipient(member.getMemberId());
        } else {
            throw new IllegalStateException("encountered a member type which I did not understand: " +
                    member.getMemberType());
        }
        return recipient;
    }

    private Recipient toRecipient(PeopleFlowDelegate delegate) {
        Recipient recipient;
        if (MemberType.PRINCIPAL == delegate.getMemberType()) {
            recipient = new KimPrincipalRecipient(delegate.getMemberId());
        } else if (MemberType.GROUP == delegate.getMemberType()) {
            recipient = new KimGroupRecipient(delegate.getMemberId());
        } else {
            throw new IllegalStateException("encountered a delegate member type which I did not understand: " +
                    delegate.getMemberType());
        }
        return recipient;
    }

    public KewTypeRepositoryService getTypeRepositoryService() {
        return typeRepositoryService;
    }

    public void setTypeRepositoryService(KewTypeRepositoryService typeRepositoryService) {
        this.typeRepositoryService = typeRepositoryService;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Recursively find all non-delegate Group and Principal requests from all of the requests in the given list.
     *
     * @param actionRequestValues the list of {@link ActionRequestValue}s to search
     * @return a list of the non-delegate Group and Principal requests found
     */
    private List<ActionRequestValue> findNonRoleRequests(List<ActionRequestValue> actionRequestValues) {
        List<ActionRequestValue> nonRoleRequests = new ArrayList<ActionRequestValue>();

        return findNonRoleRequests(actionRequestValues, nonRoleRequests);
    }

    // Recursion helper method
    private List<ActionRequestValue> findNonRoleRequests(List<ActionRequestValue> actionRequestValues,
            List<ActionRequestValue> nonRoleRequests) {

        if (!CollectionUtils.isEmpty(actionRequestValues)) {
            for (ActionRequestValue request : actionRequestValues) if (request.getDelegationType() == null) {
                if (!CollectionUtils.isEmpty(request.getChildrenRequests())) {
                    findNonRoleRequests(request.getChildrenRequests(), nonRoleRequests);
                } else  {
                    // see if we have a principal request
                    if (RecipientType.ROLE.getCode() != request.getRecipientTypeCd()) {
                        nonRoleRequests.add(request);
                    }
                }
            }
        }

        return nonRoleRequests;
    }


    /**
     * A simple class used to hold context during the PeopleFlow action request generation process.  Construction of
     * the context will validate that the given values are valid, non-null values where appropriate.
     */
    final class Context {

        private final RouteContext routeContext;
        private final PeopleFlowDefinition peopleFlow;
        private final ActionRequestType actionRequested;
        private final ActionRequestFactory actionRequestFactory;

        // lazily loaded
        private PeopleFlowTypeService peopleFlowTypeService;
        private boolean peopleFlowTypeServiceLoaded = false;
        private String peopleFlowTypeServiceVersion;

        Context(RouteContext routeContext, PeopleFlowDefinition peopleFlow, ActionRequestType actionRequested) {
            if (routeContext == null) {
                throw new IllegalArgumentException("routeContext was null");
            }
            if (peopleFlow == null) {
                throw new IllegalArgumentException("peopleFlow was null");
            }
            if (!peopleFlow.isActive()) {
                throw new ConfigurationException("Attempted to route to a PeopleFlow that is not active! " + peopleFlow);
            }
            if (actionRequested == null) {
                actionRequested = ActionRequestType.APPROVE;
            }
            this.routeContext = routeContext;
            this.peopleFlow = peopleFlow;
            this.actionRequested = actionRequested;
            this.actionRequestFactory = new ActionRequestFactory(routeContext);
        }

        RouteContext getRouteContext() {
            return routeContext;
        }

        PeopleFlowDefinition getPeopleFlow() {
            return peopleFlow;
        }

        ActionRequestType getActionRequested() {
            return actionRequested;
        }

        ActionRequestFactory getActionRequestFactory() {
            return actionRequestFactory;
        }

        /**
         * Lazily loads and caches the {@code PeopleFlowTypeService} (if necessary) and returns it.
         */
        PeopleFlowTypeService getPeopleFlowTypeService() {
            if (peopleFlowTypeServiceLoaded) {
                return this.peopleFlowTypeService;
            }

            if (getPeopleFlow().getTypeId() != null) {
                KewTypeDefinition typeDefinition = getTypeRepositoryService().getTypeById(getPeopleFlow().getTypeId());

                if (typeDefinition == null) {
                    throw new IllegalStateException("Failed to locate a PeopleFlow type for the given type id of '" + getPeopleFlow().getTypeId() + "'");
                }

                if (StringUtils.isNotBlank(typeDefinition.getServiceName())) {
                    Endpoint endpoint = KsbApiServiceLocator.getServiceBus().getEndpoint(QName.valueOf(typeDefinition.getServiceName()));

                    if (endpoint == null) {
                        throw new IllegalStateException("Failed to load the PeopleFlowTypeService with the name '" + typeDefinition.getServiceName() + "'");
                    }

                    this.peopleFlowTypeService = (PeopleFlowTypeService)endpoint.getService();
                    this.peopleFlowTypeServiceVersion = endpoint.getServiceConfiguration().getServiceVersion();
                }
            }
            peopleFlowTypeServiceLoaded = true;
            return this.peopleFlowTypeService;
        }

        String getPeopleFlowTypeServiceVersion() {
            if (!this.peopleFlowTypeServiceLoaded) {
                // execute getPeopleFlowTypeService first to lazy load
                getPeopleFlowTypeService();
            }

            return this.peopleFlowTypeServiceVersion;
        }
    }
}
