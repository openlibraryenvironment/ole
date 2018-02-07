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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.LookupCustomizer;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.common.delegate.DelegateType;
import org.kuali.rice.kim.api.common.template.Template;
import org.kuali.rice.kim.api.common.template.TemplateQueryResults;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.api.responsibility.ResponsibilityAction;
import org.kuali.rice.kim.api.responsibility.ResponsibilityQueryResults;
import org.kuali.rice.kim.api.responsibility.ResponsibilityService;
import org.kuali.rice.kim.api.role.RoleMembership;
import org.kuali.rice.kim.api.role.RoleResponsibilityAction;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeInfoService;
import org.kuali.rice.kim.framework.responsibility.ResponsibilityTypeService;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.common.attribute.AttributeTransform;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeDataBo;
import org.kuali.rice.kim.impl.role.RoleResponsibilityActionBo;
import org.kuali.rice.kim.impl.role.RoleResponsibilityBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.springframework.util.CollectionUtils;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.kuali.rice.core.api.criteria.PredicateFactory.*;

public class ResponsibilityServiceImpl implements ResponsibilityService {

    private static final Integer DEFAULT_PRIORITY_NUMBER = Integer.valueOf(1);
    private static final Log LOG = LogFactory.getLog(ResponsibilityServiceImpl.class);

    private BusinessObjectService businessObjectService;
    private CriteriaLookupService criteriaLookupService;
    private ResponsibilityTypeService defaultResponsibilityTypeService;
    private KimTypeInfoService kimTypeInfoService;
    private RoleService roleService;

    @Override
    public Responsibility createResponsibility(final Responsibility responsibility)
            throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(responsibility, "responsibility");

        if (StringUtils.isNotBlank(responsibility.getId()) && getResponsibility(responsibility.getId()) != null) {
            throw new RiceIllegalStateException("the responsibility to create already exists: " + responsibility);
        }
        List<ResponsibilityAttributeBo> attrBos = Collections.emptyList();
        if (responsibility.getTemplate() != null) {
            attrBos = KimAttributeDataBo.createFrom(ResponsibilityAttributeBo.class, responsibility.getAttributes(), responsibility.getTemplate().getKimTypeId());
        }
        ResponsibilityBo bo = ResponsibilityBo.from(responsibility);
        bo.setAttributeDetails(attrBos);
        return ResponsibilityBo.to(businessObjectService.save(bo));
    }

    @Override
    public Responsibility updateResponsibility(final Responsibility responsibility)
            throws RiceIllegalArgumentException, RiceIllegalStateException {
        incomingParamCheck(responsibility, "responsibility");

        if (StringUtils.isBlank(responsibility.getId()) || getResponsibility(responsibility.getId()) == null) {
            throw new RiceIllegalStateException("the responsibility does not exist: " + responsibility);
        }

       List<ResponsibilityAttributeBo> attrBos = Collections.emptyList();
        if (responsibility.getTemplate() != null) {
            attrBos = KimAttributeDataBo.createFrom(ResponsibilityAttributeBo.class, responsibility.getAttributes(), responsibility.getTemplate().getKimTypeId());
        }
        ResponsibilityBo bo = ResponsibilityBo.from(responsibility);

        if (bo.getAttributeDetails() != null) {
            bo.getAttributeDetails().clear();
            bo.setAttributeDetails(attrBos);
        }

        return ResponsibilityBo.to(businessObjectService.save(bo));
    }

    @Override
    public Responsibility getResponsibility(final String id) throws RiceIllegalArgumentException {
        incomingParamCheck(id, "id");

        return ResponsibilityBo.to(businessObjectService.findBySinglePrimaryKey(ResponsibilityBo.class, id));
    }

    @Override
    public Responsibility findRespByNamespaceCodeAndName(final String namespaceCode, final String name)
            throws RiceIllegalArgumentException {
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(name, "name");

        final Map<String, String> crit = new HashMap<String, String>();
        crit.put("namespaceCode", namespaceCode);
        crit.put("name", name);
        crit.put("active", "Y");

        final Collection<ResponsibilityBo> bos = businessObjectService.findMatching(ResponsibilityBo.class, Collections.unmodifiableMap(crit));

        if (bos != null) {
            if (bos.size() > 1) {
                throw new RiceIllegalStateException("more than one Responsibility found with namespace code: " + namespaceCode + " and name: " + name);
            }

            final Iterator<ResponsibilityBo> i = bos.iterator();
            return i.hasNext() ? ResponsibilityBo.to(i.next()) : null;
        }
        return null;
    }

    @Override
    public Template getResponsibilityTemplate(final String id) throws RiceIllegalArgumentException {
        incomingParamCheck(id, "id");

        return ResponsibilityTemplateBo.to(businessObjectService.findBySinglePrimaryKey(ResponsibilityTemplateBo.class, id));
    }

    @Override
    public Template findRespTemplateByNamespaceCodeAndName(final String namespaceCode, final String name) throws RiceIllegalArgumentException {
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(name, "name");

        final Map<String, String> crit = new HashMap<String, String>();
        crit.put("namespaceCode", namespaceCode);
        crit.put("name", name);
        crit.put("active", "Y");

        final Collection<ResponsibilityTemplateBo> bos = businessObjectService.findMatching(ResponsibilityTemplateBo.class, Collections.unmodifiableMap(crit));
        if (bos != null) {
            if (bos.size() > 1) {
                throw new RiceIllegalStateException("more than one Responsibility Template found with namespace code: " + namespaceCode + " and name: " + name);
            }

            final Iterator<ResponsibilityTemplateBo> i = bos.iterator();
            return i.hasNext() ? ResponsibilityTemplateBo.to(i.next()) : null;
        }
        return null;
    }

    @Override
    public boolean hasResponsibility(final String principalId, final String namespaceCode,
            final String respName, final Map<String, String> qualification) throws RiceIllegalArgumentException {
        incomingParamCheck(principalId, "principalId");
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(respName, "respName");
        incomingParamCheck(qualification, "qualification");

        // get all the responsibility objects whose name match that requested
        final List<Responsibility> responsibilities = Collections.singletonList(findRespByNamespaceCodeAndName(namespaceCode, respName));
        return hasResp(principalId, namespaceCode, responsibilities, qualification, null);
    }

    @Override
    public boolean hasResponsibilityByTemplate(final String principalId, final String namespaceCode,
            final String respTemplateName, final Map<String, String> qualification,
            final Map<String, String> responsibilityDetails) throws RiceIllegalArgumentException {
        incomingParamCheck(principalId, "principalId");
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(respTemplateName, "respTemplateName");
        incomingParamCheck(qualification, "qualification");
        incomingParamCheck(responsibilityDetails, "responsibilityDetails");


        // get all the responsibility objects whose name match that requested
        final List<Responsibility> responsibilities = findResponsibilitiesByTemplate(namespaceCode, respTemplateName);
        return hasResp(principalId, namespaceCode, responsibilities, qualification, responsibilityDetails);
    }

    private boolean hasResp(final String principalId, final String namespaceCode,
            final List<Responsibility> responsibilities,
            final Map<String, String> qualification,
            final Map<String, String> responsibilityDetails) throws RiceIllegalArgumentException {
        // now, filter the full list by the detail passed
        final List<String> ids = new ArrayList<String>();
        for (Responsibility r : getMatchingResponsibilities(responsibilities, responsibilityDetails)) {
            ids.add(r.getId());
        }
        final List<String> roleIds = getRoleIdsForResponsibilities(ids);
        return roleService.principalHasRole(principalId, roleIds, qualification);
    }

    @Override
    public List<ResponsibilityAction> getResponsibilityActions(final String namespaceCode,
            final String responsibilityName, final Map<String, String> qualification) throws RiceIllegalArgumentException {
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(responsibilityName, "responsibilityName");
        incomingParamCheck(qualification, "qualification");
        
        if ( LOG.isDebugEnabled() ) {
            logResponsibilityCheck( namespaceCode, responsibilityName, qualification, Collections.<String, String>emptyMap() );
        }

        // get all the responsibility objects whose name match that requested
        List<Responsibility> responsibilities = Collections.singletonList(findRespByNamespaceCodeAndName(namespaceCode, responsibilityName));
        return getRespActions(namespaceCode, responsibilities, qualification, null);
    }

    @Override
    public List<ResponsibilityAction> getResponsibilityActionsByTemplate(final String namespaceCode,
            final String respTemplateName, final Map<String, String> qualification,
            final Map<String, String> responsibilityDetails) throws RiceIllegalArgumentException {
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(respTemplateName, "respTemplateName");
        incomingParamCheck(qualification, "qualification");
        
        if ( LOG.isDebugEnabled() ) {
            logResponsibilityCheck( namespaceCode, respTemplateName, qualification, responsibilityDetails );
        }

        // get all the responsibility objects whose name match that requested
        List<Responsibility> responsibilities = findResponsibilitiesByTemplate(namespaceCode, respTemplateName);
        return getRespActions(namespaceCode, responsibilities, qualification, responsibilityDetails);
    }

    private List<ResponsibilityAction> getRespActions(final String namespaceCode,
            final List<Responsibility> responsibilities,
            final Map<String, String> qualification,
            final Map<String, String> responsibilityDetails) {
        // now, filter the full list by the detail passed
        List<Responsibility> applicableResponsibilities = getMatchingResponsibilities(responsibilities, responsibilityDetails);
        List<ResponsibilityAction> results = new ArrayList<ResponsibilityAction>();
        for (Responsibility r : applicableResponsibilities) {
            List<String> roleIds = getRoleIdsForResponsibility(r.getId());
            results.addAll(getActionsForResponsibilityRoles(r, roleIds, qualification));
        }
        if ( LOG.isDebugEnabled() ) {
            LOG.debug("Found " + results.size() + " matching ResponsibilityAction objects");
            if ( LOG.isTraceEnabled() ) {
                LOG.trace( results );
            }
        }
        return results;
    }

    private List<ResponsibilityAction> getActionsForResponsibilityRoles(Responsibility responsibility, List<String> roleIds, Map<String, String> qualification) {
        List<ResponsibilityAction> results = new ArrayList<ResponsibilityAction>();
        Collection<RoleMembership> roleMembers = roleService.getRoleMembers(roleIds,qualification);
        for (RoleMembership rm : roleMembers) {
            // only add them to the list if the member ID has been populated
            if (StringUtils.isNotBlank(rm.getMemberId())) {
                List<RoleResponsibilityAction> roleResponsibilityActions
                        = getResponsibilityActions(rm.getRoleId(), responsibility.getId(), rm.getId());
                for (RoleResponsibilityAction roleResponsibilityAction : roleResponsibilityActions) {
                    final ResponsibilityAction.Builder rai = ResponsibilityAction.Builder.create();
                    rai.setMemberRoleId((rm.getEmbeddedRoleId() == null) ? rm.getRoleId() : rm.getEmbeddedRoleId());
                    rai.setRoleId(rm.getRoleId());
                    rai.setQualifier(rm.getQualifier());
                    final List<DelegateType.Builder> bs = new ArrayList<DelegateType.Builder>();
                    for (DelegateType d : rm.getDelegates()) {
                        bs.add(DelegateType.Builder.create(d));
                    }
                    rai.setDelegates(bs);
                    rai.setResponsibilityId(responsibility.getId());
                    rai.setResponsibilityName(responsibility.getName());
                    rai.setResponsibilityNamespaceCode(responsibility.getNamespaceCode());

                    if (MemberType.PRINCIPAL.equals(rm.getType())) {
                        rai.setPrincipalId(rm.getMemberId());
                    } else {
                        rai.setGroupId(rm.getMemberId());
                    }
                    // add the data to the ResponsibilityActionInfo objects
                    rai.setActionTypeCode(roleResponsibilityAction.getActionTypeCode());
                    rai.setActionPolicyCode(roleResponsibilityAction.getActionPolicyCode());
                    rai.setPriorityNumber(roleResponsibilityAction.getPriorityNumber() == null ? DEFAULT_PRIORITY_NUMBER : roleResponsibilityAction.getPriorityNumber());
                    rai.setForceAction(roleResponsibilityAction.isForceAction());
                    rai.setParallelRoutingGroupingCode((rm.getRoleSortingCode() == null) ? "" : rm.getRoleSortingCode());
                    rai.setRoleResponsibilityActionId(roleResponsibilityAction.getId());
                    results.add(rai.build());
                }
            }
        }
        return Collections.unmodifiableList(results);
    }

    private List<RoleResponsibilityAction> getResponsibilityActions(String roleId, String responsibilityId, String roleMemberId) {
        List<RoleResponsibilityAction> responsibilityActions = new ArrayList<RoleResponsibilityAction>();

        // KULRICE-7459: Requisition, PO and its subtype documents are going to final status where they should not.
        //
        // need to do in 2 steps due to "*" wildcard convention in column data for role member id and role
        // responsibility id.  Well, we could do in 1 step w/ straight SQL, but not w/ Criteria API due to the
        // INNER JOIN automatically created between RoleResponsibility and RoleResponsibilityAction tables.

        final Predicate roleResponsibilityPredicate =
                and(
                        equal("responsibilityId", responsibilityId),
                        equal("roleId", roleId),
                        equal("active", "Y")
                );

        // First get RoleResponsibilityBos
        final QueryByCriteria.Builder roleResponsibilityQueryBuilder = QueryByCriteria.Builder.create();
        roleResponsibilityQueryBuilder.setPredicates(roleResponsibilityPredicate);
        final GenericQueryResults<RoleResponsibilityBo> roleResponsibilityResults =
                criteriaLookupService.lookup(RoleResponsibilityBo.class, roleResponsibilityQueryBuilder.build());
        final List<RoleResponsibilityBo> roleResponsibilityBos = roleResponsibilityResults.getResults();

        if (!CollectionUtils.isEmpty(roleResponsibilityBos)) { // if there are any...
            // Then query RoleResponsibilityActionBos based on them

            List<String> roleResponsibilityIds = new ArrayList<String>(roleResponsibilityBos.size());
            for (RoleResponsibilityBo roleResponsibilityBo : roleResponsibilityBos) {
                roleResponsibilityIds.add(roleResponsibilityBo.getRoleResponsibilityId());
            }

            final Predicate roleResponsibilityActionPredicate =
                    or(
                            and(
                                    in("roleResponsibilityId", roleResponsibilityIds.toArray()),
                                    or(
                                            equal(KIMPropertyConstants.RoleMember.ROLE_MEMBER_ID, roleMemberId),
                                            equal(KIMPropertyConstants.RoleMember.ROLE_MEMBER_ID, "*")
                                    )
                            ),
                            and(
                                    equal("roleResponsibilityId", "*"),
                                    equal(KIMPropertyConstants.RoleMember.ROLE_MEMBER_ID, roleMemberId)
                            )
                    );

            final QueryByCriteria.Builder roleResponsibilityActionQueryBuilder = QueryByCriteria.Builder.create();
            roleResponsibilityActionQueryBuilder.setPredicates(roleResponsibilityActionPredicate);

            final GenericQueryResults<RoleResponsibilityActionBo> roleResponsibilityActionResults =
                    criteriaLookupService.lookup(
                            RoleResponsibilityActionBo.class, roleResponsibilityActionQueryBuilder.build()
                    );

            final List<RoleResponsibilityActionBo> roleResponsibilityActionBos = roleResponsibilityActionResults.getResults();
            for (RoleResponsibilityActionBo roleResponsibilityActionBo : roleResponsibilityActionBos) {
                responsibilityActions.add(RoleResponsibilityActionBo.to(roleResponsibilityActionBo));
            };
        }

        return Collections.unmodifiableList(responsibilityActions);
    }

    @Override
    public List<String> getRoleIdsForResponsibility(String id) throws RiceIllegalArgumentException {
        incomingParamCheck(id, "id");

        final List<String> roleIds = getRoleIdsForPredicate(and(equal("responsibilityId", id), equal("active", "Y")));

        return Collections.unmodifiableList(roleIds);
    }

    @Override
    public ResponsibilityQueryResults findResponsibilities(final QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        incomingParamCheck(queryByCriteria, "queryByCriteria");

        LookupCustomizer.Builder<ResponsibilityBo> lc = LookupCustomizer.Builder.create();
        lc.setPredicateTransform(AttributeTransform.getInstance());

        GenericQueryResults<ResponsibilityBo> results = criteriaLookupService.lookup(ResponsibilityBo.class, queryByCriteria, lc.build());

        ResponsibilityQueryResults.Builder builder = ResponsibilityQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<Responsibility.Builder> ims = new ArrayList<Responsibility.Builder>();
        for (ResponsibilityBo bo : results.getResults()) {
            ims.add(Responsibility.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
    }

    @Override
    public TemplateQueryResults findResponsibilityTemplates(final QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        incomingParamCheck(queryByCriteria, "queryByCriteria");

        GenericQueryResults<ResponsibilityTemplateBo> results = criteriaLookupService.lookup(ResponsibilityTemplateBo.class, queryByCriteria);

        TemplateQueryResults.Builder builder = TemplateQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<Template.Builder> ims = new ArrayList<Template.Builder>();
        for (ResponsibilityTemplateBo bo : results.getResults()) {
            ims.add(Template.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
    }

    /**
     * Compare each of the passed in responsibilities with the given responsibilityDetails.  Those that
     * match are added to the result list.
     */
    private List<Responsibility> getMatchingResponsibilities(List<Responsibility> responsibilities, Map<String, String> responsibilityDetails) {
        // if no details passed, assume that all match
        if (responsibilityDetails == null || responsibilityDetails.isEmpty()) {
            return responsibilities;
        }

        final List<Responsibility> applicableResponsibilities = new ArrayList<Responsibility>();
        // otherwise, attempt to match the permission details
        // build a map of the template IDs to the type services
        Map<String, ResponsibilityTypeService> responsibilityTypeServices = getResponsibilityTypeServicesByTemplateId(responsibilities);
        // build a map of permissions by template ID
        Map<String, List<Responsibility>> responsibilityMap = groupResponsibilitiesByTemplate(responsibilities);
        // loop over the different templates, matching all of the same template against the type
        // service at once
        for (Map.Entry<String, List<Responsibility>> respEntry : responsibilityMap.entrySet()) {
            ResponsibilityTypeService responsibilityTypeService = responsibilityTypeServices.get(respEntry.getKey());
            List<Responsibility> responsibilityInfos = respEntry.getValue();
            if (responsibilityTypeService == null) {
                responsibilityTypeService = defaultResponsibilityTypeService;
            }
            applicableResponsibilities.addAll(responsibilityTypeService.getMatchingResponsibilities(responsibilityDetails, responsibilityInfos));
        }
        return Collections.unmodifiableList(applicableResponsibilities);
    }

    private Map<String, ResponsibilityTypeService> getResponsibilityTypeServicesByTemplateId(Collection<Responsibility> responsibilities) {
        Map<String, ResponsibilityTypeService> responsibilityTypeServices = new HashMap<String, ResponsibilityTypeService>(responsibilities.size());
        for (Responsibility responsibility : responsibilities) {
            final Template t = responsibility.getTemplate();
            final KimType type = kimTypeInfoService.getKimType(t.getKimTypeId());

            final String serviceName = type.getServiceName();
            if (serviceName != null) {
                ResponsibilityTypeService responsibiltyTypeService = GlobalResourceLoader.getService(QName.valueOf(serviceName));
                if (responsibiltyTypeService != null) {
                    responsibilityTypeServices.put(responsibility.getTemplate().getId(), responsibiltyTypeService);
                } else {
                    responsibilityTypeServices.put(responsibility.getTemplate().getId(), defaultResponsibilityTypeService);
                }
            }
        }
        return Collections.unmodifiableMap(responsibilityTypeServices);
    }

    private Map<String, List<Responsibility>> groupResponsibilitiesByTemplate(Collection<Responsibility> responsibilities) {
        final Map<String, List<Responsibility>> results = new HashMap<String, List<Responsibility>>();
        for (Responsibility responsibility : responsibilities) {
            List<Responsibility> responsibilityInfos = results.get(responsibility.getTemplate().getId());
            if (responsibilityInfos == null) {
                responsibilityInfos = new ArrayList<Responsibility>();
                results.put(responsibility.getTemplate().getId(), responsibilityInfos);
            }
            responsibilityInfos.add(responsibility);
        }
        return Collections.unmodifiableMap(results);
    }

    private List<String> getRoleIdsForResponsibilities(Collection<String> ids) {
        final List<String> roleIds = getRoleIdsForPredicate(and(in("responsibilityId", ids.toArray()), equal("active", "Y")));

        return Collections.unmodifiableList(roleIds);
    }

    private List<String> getRoleIdsForPredicate(Predicate p) {
        final QueryByCriteria.Builder builder = QueryByCriteria.Builder.create();
        builder.setPredicates(p);
        final GenericQueryResults<RoleResponsibilityBo> qr = criteriaLookupService.lookup(RoleResponsibilityBo.class, builder.build());

        final List<String> roleIds = new ArrayList<String>();
        for (RoleResponsibilityBo bo : qr.getResults()) {
            roleIds.add(bo.getRoleId());
        }
        return Collections.unmodifiableList(roleIds);
    }


    @Override
    public List<Responsibility> findResponsibilitiesByTemplate(String namespaceCode, String templateName) {

        final Map<String, String> crit = new HashMap<String, String>();
        crit.put("template.namespaceCode", namespaceCode); 
        crit.put("template.name", templateName); 
        crit.put("active", "Y"); 

        final Collection<ResponsibilityBo> bos = businessObjectService.findMatching(ResponsibilityBo.class, Collections.unmodifiableMap(crit));
        final List<Responsibility> ims = new ArrayList<Responsibility>();
        if (bos != null) {
            for (ResponsibilityBo bo : bos) {
                if (bo != null) {
                    ims.add(ResponsibilityBo.to(bo));
                }
            }
        }

        return Collections.unmodifiableList(ims);
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public void setCriteriaLookupService(final CriteriaLookupService criteriaLookupService) {
        this.criteriaLookupService = criteriaLookupService;
    }

    public void setDefaultResponsibilityTypeService(final ResponsibilityTypeService defaultResponsibilityTypeService) {
        this.defaultResponsibilityTypeService = defaultResponsibilityTypeService;
    }

    public void setKimTypeInfoService(final KimTypeInfoService kimTypeInfoService) {
        this.kimTypeInfoService = kimTypeInfoService;
    }

    public void setRoleService(final RoleService roleService) {
        this.roleService = roleService;
    }

    protected void logResponsibilityCheck(String namespaceCode, String responsibilityName, 
                         Map<String, String> responsibilityDetails, Map<String, String> qualification ) {
        StringBuilder sb = new StringBuilder();
        sb.append(  '\n' );
        sb.append( "Get Resp Actions: " ).append( namespaceCode ).append( "/" ).append( responsibilityName ).append( '\n' );
        sb.append( "             Details:\n" );
        if ( responsibilityDetails != null ) {
            sb.append( responsibilityDetails );
        } else {
            sb.append( "                         [null]\n" );
        }
        sb.append( "             Qualifiers:\n" );
        if ( qualification != null ) {
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
