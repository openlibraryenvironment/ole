/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.role;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleMembership;
import org.kuali.rice.kim.api.role.RoleResponsibilityAction;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeBo;
import org.kuali.rice.kim.impl.common.delegate.DelegateTypeBo;
import org.kuali.rice.kim.impl.common.delegate.DelegateMemberBo;
import org.kuali.rice.kim.impl.responsibility.ResponsibilityAttributeBo;
import org.kuali.rice.kim.impl.responsibility.ResponsibilityBo;
import org.kuali.rice.kim.impl.responsibility.ResponsibilityTemplateBo;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.role.RoleMemberAttributeDataBo;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.kim.impl.role.RoleResponsibilityActionBo;
import org.kuali.rice.kim.impl.role.RoleResponsibilityBo;
import org.kuali.rice.kim.impl.type.KimTypeAttributeBo;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.test.BaselineTestCase;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests Role-based routing integration between KEW and KIM.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
// FixMethodOrder will run tests in alphabetical order by test name
//                to ensure testing of forceAction by each user for
//                the case of an existing delegate being the initiator.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class RoleRouteModuleTest extends KEWTestCase {

    private static final String NAMESPACE = KewApiConstants.KEW_NAMESPACE;
    private static final String ROLE_NAME = "RoleRouteModuleTestRole";

    private static boolean suiteDataInitialized = false;
    private static boolean suiteCreateDelegateInitialized = false;

    protected void loadTestData() throws Exception {
        loadXmlFile("RoleRouteModuleTestConfig.xml");

        // only create this data once per suite!
        if (suiteDataInitialized) {
            return;
        }

        /**
         * First we need to set up:
         *
         * 1) KimAttributes for both chart and org
         * 2) The KimType for "chart/org"
         * 3) The KimTypeAttributes for chart and org to define relationship between KimType and it's KimAttributes
         */

        // create "chart" KimAttribute
        Long chartAttributeId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ATTR_DEFN_ID_S");
        KimAttributeBo chartAttribute = new KimAttributeBo();
        chartAttribute.setId("" + chartAttributeId);
        chartAttribute.setAttributeName("chart");
        chartAttribute.setComponentName("org.kuali.rice.kim.bo.impl.KimAttributes");
        chartAttribute.setNamespaceCode(NAMESPACE);
        chartAttribute.setAttributeLabel("chart");
        chartAttribute.setActive(true);
        chartAttribute = (KimAttributeBo) KRADServiceLocator.getBusinessObjectService().save(chartAttribute);

        // create "org" KimAttribute
        Long orgAttributeId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ATTR_DEFN_ID_S");
        KimAttributeBo orgAttribute = new KimAttributeBo();
        orgAttribute.setId("" + orgAttributeId);
        orgAttribute.setComponentName("org.kuali.rice.kim.bo.impl.KimAttributes");
        orgAttribute.setAttributeName("org");
        orgAttribute.setNamespaceCode(NAMESPACE);
        orgAttribute.setAttributeLabel("org");
        orgAttribute.setActive(true);
        orgAttribute = (KimAttributeBo) KRADServiceLocator.getBusinessObjectService().save(orgAttribute);

        // create KimType
        Long kimTypeId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_TYP_ID_S");
        KimTypeBo kimType = new KimTypeBo();
        kimType.setId("" + kimTypeId);
        kimType.setName("ChartOrg");
        kimType.setNamespaceCode(NAMESPACE);
        kimType.setServiceName("testBaseRoleTypeService"); // do we need to set the kim type service yet? we shall see...
        kimType.setActive(true);
        kimType = (KimTypeBo) KRADServiceLocator.getBusinessObjectService().save(kimType);

        // create chart KimTypeAttribute
        Long chartTypeAttributeId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_TYP_ATTR_ID_S");
        KimTypeAttributeBo chartTypeAttribute = new KimTypeAttributeBo();
        chartTypeAttribute.setId("" + chartTypeAttributeId);
        chartTypeAttribute.setActive(true);
        chartTypeAttribute.setKimAttributeId(chartAttribute.getId());
        chartTypeAttribute.setKimTypeId(kimType.getId());
        chartTypeAttribute = (KimTypeAttributeBo) KRADServiceLocator.getBusinessObjectService().save(chartTypeAttribute);

        // create org KimTypeAttribute
        Long orgTypeAttributeId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_TYP_ATTR_ID_S");
        KimTypeAttributeBo orgTypeAttribute = new KimTypeAttributeBo();
        orgTypeAttribute.setId("" + orgTypeAttributeId);
        orgTypeAttribute.setActive(true);
        orgTypeAttribute.setKimAttributeId(orgAttribute.getId());
        orgTypeAttribute.setKimTypeId(kimType.getId());
        orgTypeAttribute = (KimTypeAttributeBo) KRADServiceLocator.getBusinessObjectService().save(orgTypeAttribute);

        /**
         * New let's create the Role
         */

        String roleId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ROLE_ID_S");
        RoleBo role = new RoleBo();
        role.setId(roleId);
        role.setNamespaceCode(NAMESPACE);
        role.setDescription("");
        role.setName(ROLE_NAME);
        role.setActive(true);
        role.setKimTypeId(kimType.getId());

        String roleMemberId1 = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ROLE_ID_S");
        RoleMemberBo adminRolePrincipal = new RoleMemberBo();
        adminRolePrincipal.setId(roleMemberId1);
        adminRolePrincipal.setRoleId(roleId);
        Principal adminPrincipal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("admin");
        assertNotNull(adminPrincipal);
        adminRolePrincipal.setMemberId(adminPrincipal.getPrincipalId());
        adminRolePrincipal.setType( MemberType.PRINCIPAL );

        String roleMemberId2 = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ROLE_ID_S");
        RoleMemberBo user2RolePrincipal = new RoleMemberBo();
        user2RolePrincipal.setId(roleMemberId2);
        user2RolePrincipal.setRoleId(roleId);
        Principal user2Principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("user2");
        assertNotNull(user2Principal);
        user2RolePrincipal.setMemberId(user2Principal.getPrincipalId());
        user2RolePrincipal.setType( MemberType.PRINCIPAL );

        String roleMemberId3 = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ROLE_ID_S");
        RoleMemberBo user1RolePrincipal = new RoleMemberBo();
        user1RolePrincipal.setId(roleMemberId3);
        user1RolePrincipal.setRoleId(roleId);
        Principal user1Principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("user1");
        assertNotNull(user1Principal);
        user1RolePrincipal.setMemberId(user1Principal.getPrincipalId());
        user1RolePrincipal.setType( MemberType.PRINCIPAL );

        List<RoleMemberBo> memberPrincipals = new ArrayList<RoleMemberBo>();
        memberPrincipals.add(adminRolePrincipal);
        memberPrincipals.add(user2RolePrincipal);
        memberPrincipals.add(user1RolePrincipal);

        role.setMembers(memberPrincipals);

        /**
         * Let's create qualifiers for chart and org for our role members
         */

        String dataId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_GRP_ATTR_DATA_ID_S");
        RoleMemberAttributeDataBo chartDataBL = new RoleMemberAttributeDataBo();
        chartDataBL.setId(dataId);
        chartDataBL.setAttributeValue("BL");
        chartDataBL.setKimAttribute(chartAttribute);
        chartDataBL.setKimAttributeId(chartAttribute.getId());
        chartDataBL.setKimTypeId(kimType.getId());
        chartDataBL.setAssignedToId(adminRolePrincipal.getId());

        dataId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_GRP_ATTR_DATA_ID_S");
        RoleMemberAttributeDataBo chartDataBL2 = new RoleMemberAttributeDataBo();
        chartDataBL2.setId(dataId);
        chartDataBL2.setAttributeValue("BL");
        chartDataBL2.setKimAttribute(chartAttribute);
        chartDataBL2.setKimAttributeId(chartAttribute.getId());
        chartDataBL2.setKimTypeId(kimType.getId());
        chartDataBL2.setAssignedToId(user2RolePrincipal.getId());

        dataId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_GRP_ATTR_DATA_ID_S");
        RoleMemberAttributeDataBo orgDataBUS = new RoleMemberAttributeDataBo();
        orgDataBUS.setId(dataId);
        orgDataBUS.setAttributeValue("BUS");
        orgDataBUS.setKimAttribute(orgAttribute);
        orgDataBUS.setKimAttributeId(orgAttribute.getId());
        orgDataBUS.setKimTypeId(kimType.getId());
        orgDataBUS.setAssignedToId(adminRolePrincipal.getId());

        dataId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_GRP_ATTR_DATA_ID_S");
        RoleMemberAttributeDataBo orgDataBUS2 = new RoleMemberAttributeDataBo();
        orgDataBUS2.setId(dataId);
        orgDataBUS2.setAttributeValue("BUS");
        orgDataBUS2.setKimAttribute(orgAttribute);
        orgDataBUS2.setKimAttributeId(orgAttribute.getId());
        orgDataBUS2.setKimTypeId(kimType.getId());
        orgDataBUS2.setAssignedToId(user2RolePrincipal.getId());


        dataId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_GRP_ATTR_DATA_ID_S");
        RoleMemberAttributeDataBo chartDataIN = new RoleMemberAttributeDataBo();
        chartDataIN.setId(dataId);
        chartDataIN.setAttributeValue("IN");
        chartDataIN.setKimAttribute(chartAttribute);
        chartDataIN.setKimAttributeId(chartAttribute.getId());
        chartDataIN.setKimTypeId(kimType.getId());
        chartDataIN.setAssignedToId(user1RolePrincipal.getId());

        dataId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_GRP_ATTR_DATA_ID_S");
        RoleMemberAttributeDataBo orgDataMED = new RoleMemberAttributeDataBo();
        orgDataMED.setId(dataId);
        orgDataMED.setAttributeValue("MED");
        orgDataMED.setKimAttribute(orgAttribute);
        orgDataMED.setKimAttributeId(orgAttribute.getId());
        orgDataMED.setKimTypeId(kimType.getId());
        orgDataMED.setAssignedToId(user1RolePrincipal.getId());

        List<RoleMemberAttributeDataBo> user1Attributes = new ArrayList<RoleMemberAttributeDataBo>();
        user1Attributes.add(chartDataIN);
        user1Attributes.add(orgDataMED);
        user1RolePrincipal.setAttributeDetails(user1Attributes);

        List<RoleMemberAttributeDataBo> user2Attributes = new ArrayList<RoleMemberAttributeDataBo>();
        user2Attributes.add(chartDataBL2);
        user2Attributes.add(orgDataBUS2);
        user2RolePrincipal.setAttributeDetails(user2Attributes);

        List<RoleMemberAttributeDataBo> adminAttributes = new ArrayList<RoleMemberAttributeDataBo>();
        adminAttributes.add(chartDataBL);
        adminAttributes.add(orgDataBUS);
        adminRolePrincipal.setAttributeDetails(adminAttributes);


        /**
         * Now we can save the role!
         */

        role = KRADServiceLocator.getBusinessObjectService().save(role);


        /**
         * Let's set up attributes for responsibility details
         */

        // create "documentType" KimAttribute
        Long documentTypeAttributeId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ATTR_DEFN_ID_S");
        KimAttributeBo documentTypeAttribute = new KimAttributeBo();
        documentTypeAttribute.setId("" + documentTypeAttributeId);
        documentTypeAttribute.setAttributeName(KewApiConstants.DOCUMENT_TYPE_NAME_DETAIL);
        documentTypeAttribute.setNamespaceCode(NAMESPACE);
        documentTypeAttribute.setAttributeLabel("documentType");
        documentTypeAttribute.setActive(true);
        documentTypeAttribute = (KimAttributeBo) KRADServiceLocator.getBusinessObjectService().save(documentTypeAttribute);

        // create "node name" KimAttribute
        Long nodeNameAttributeId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ATTR_DEFN_ID_S");
        KimAttributeBo nodeNameAttribute = new KimAttributeBo();
        nodeNameAttribute.setId("" + nodeNameAttributeId);
        nodeNameAttribute.setAttributeName(KewApiConstants.ROUTE_NODE_NAME_DETAIL);
        nodeNameAttribute.setNamespaceCode(NAMESPACE);
        nodeNameAttribute.setAttributeLabel("nodeName");
        nodeNameAttribute.setActive(true);
        nodeNameAttribute = (KimAttributeBo) KRADServiceLocator.getBusinessObjectService().save(nodeNameAttribute);

        // create KimType for responsibility details
        Long kimRespTypeId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_TYP_ID_S");
        KimTypeBo kimRespType = new KimTypeBo();
        kimRespType.setId("" + kimRespTypeId);
        kimRespType.setName("RespDetails");
        kimRespType.setNamespaceCode(NAMESPACE);
        kimRespType.setServiceName("testBaseResponsibilityTypeService");
        kimRespType.setActive(true);
        kimRespType = (KimTypeBo) KRADServiceLocator.getBusinessObjectService().save(kimRespType);

        // create document type KimTypeAttribute
        Long documentTypeTypeAttributeId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_TYP_ATTR_ID_S");
        KimTypeAttributeBo documentTypeTypeAttribute = new KimTypeAttributeBo();
        documentTypeTypeAttribute.setId("" + documentTypeTypeAttributeId);
        documentTypeTypeAttribute.setActive(true);
        documentTypeTypeAttribute.setKimAttributeId(chartAttribute.getId());
        documentTypeTypeAttribute.setKimTypeId(kimType.getId());
        documentTypeTypeAttribute.setSortCode("a");
        documentTypeTypeAttribute = KRADServiceLocator.getBusinessObjectService().save(documentTypeTypeAttribute);

        // create nodeNameType KimTypeAttribute
        Long nodeNameTypeAttributeId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_TYP_ATTR_ID_S");
        KimTypeAttributeBo nodeNameTypeAttribute = new KimTypeAttributeBo();
        nodeNameTypeAttribute.setId("" + nodeNameTypeAttributeId);
        nodeNameTypeAttribute.setActive(true);
        nodeNameTypeAttribute.setKimAttributeId(orgAttribute.getId());
        nodeNameTypeAttribute.setKimTypeId(kimType.getId());
        nodeNameTypeAttribute.setSortCode("a");
        nodeNameTypeAttribute = KRADServiceLocator.getBusinessObjectService().save(nodeNameTypeAttribute);

        createResponsibilityForRoleRouteModuleTest(role, documentTypeAttribute, nodeNameAttribute, kimRespType, user1RolePrincipal, user2RolePrincipal, adminRolePrincipal,
                                                   "FirstApproveReview", "RoleRouteModuleTest1", "resp1", "VoluntaryReview1", ActionRequestPolicy.FIRST);
        //createResponsibilityForRoleRouteModuleTest1(role, documentTypeAttribute, nodeNameAttribute, kimRespType, user1RolePrincipal, user2RolePrincipal, adminRolePrincipal);
        createResponsibilityForRoleRouteModuleTest(role, documentTypeAttribute, nodeNameAttribute, kimRespType, user1RolePrincipal, user2RolePrincipal, adminRolePrincipal,
                                                   "AllApproveReview", "RoleRouteModuleTest2", "resp2", "VoluntaryReview2", ActionRequestPolicy.ALL);
        //createResponsibilityForRoleRouteModuleTest2(role, documentTypeAttribute, nodeNameAttribute, kimRespType, user1RolePrincipal, user2RolePrincipal, adminRolePrincipal);

        suiteDataInitialized = true;
    }

    private void createResponsibilityForRoleRouteModuleTest(RoleBo role, KimAttributeBo documentTypeAttribute, KimAttributeBo nodeNameAttribute, KimTypeBo kimRespType, RoleMemberBo user1RolePrincipal, RoleMemberBo user2RolePrincipal, RoleMemberBo adminRolePrincipal,
                                                            String templateName, String docTypeDetailValue, String responsibilityName, String responsibilityDesc, ActionRequestPolicy actionRequestPolicy) {

        /**
         * Create the responsibility template
         */

        String templateId = String.valueOf(KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_RSP_TMPL_ID_S"));
        ResponsibilityTemplateBo template = new ResponsibilityTemplateBo();
        template.setId(templateId);
        template.setNamespaceCode(NAMESPACE);
        template.setName(templateName);
        template.setKimTypeId(kimRespType.getId());
        template.setActive(true);
        template.setDescription("description");

        template = KRADServiceLocator.getBusinessObjectService().save(template);


        /**
         * Create the responsibility details for RoleRouteModuleTest1
         */

        String responsibilityId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ROLE_RSP_ID_S");

        String dataId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_GRP_ATTR_DATA_ID_S");
        ResponsibilityAttributeBo documentTypeDetail = new ResponsibilityAttributeBo();
        documentTypeDetail.setId(dataId);
        documentTypeDetail.setAttributeValue(docTypeDetailValue);
        documentTypeDetail.setKimAttribute(documentTypeAttribute);
        documentTypeDetail.setKimAttributeId(documentTypeAttribute.getId());
        documentTypeDetail.setKimTypeId(kimRespType.getId());
        documentTypeDetail.setAssignedToId(responsibilityId);

        dataId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_GRP_ATTR_DATA_ID_S");
        ResponsibilityAttributeBo nodeNameDetail = new ResponsibilityAttributeBo();
        nodeNameDetail.setId(dataId);
        nodeNameDetail.setAttributeValue("Role1");
        nodeNameDetail.setKimAttribute(nodeNameAttribute);
        nodeNameDetail.setKimAttributeId(nodeNameAttribute.getId());
        nodeNameDetail.setKimTypeId(kimRespType.getId());
        nodeNameDetail.setAssignedToId(responsibilityId);



        /**
         * Create the responsibility
         */

        List<ResponsibilityAttributeBo> detailObjects = new ArrayList<ResponsibilityAttributeBo>();
        detailObjects.add(documentTypeDetail);
        detailObjects.add(nodeNameDetail);

        ResponsibilityBo responsibility = new ResponsibilityBo();
        responsibility.setActive(true);
        responsibility.setDescription(responsibilityDesc);
        responsibility.setAttributeDetails(detailObjects);
        responsibility.setName(responsibilityName);
        responsibility.setNamespaceCode(NAMESPACE);
        responsibility.setId(responsibilityId);
        responsibility.setTemplate(template);
        responsibility.setTemplateId(template.getId());

        responsibility = (ResponsibilityBo) KRADServiceLocator.getBusinessObjectService().save(responsibility);

        /**
         * Create the RoleResponsibility
         */

        String roleResponsibilityId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ROLE_RSP_ID_S");
        RoleResponsibilityBo roleResponsibility = new RoleResponsibilityBo();
        roleResponsibility.setRoleResponsibilityId(roleResponsibilityId);
        roleResponsibility.setActive(true);
        roleResponsibility.setResponsibilityId(responsibilityId);
        roleResponsibility.setRoleId(role.getId());

        roleResponsibility = KRADServiceLocator.getBusinessObjectService().save(roleResponsibility);

        /**
         * Create the various responsibility actions
         */
        String roleResponsibilityActionId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ROLE_RSP_ACTN_ID_S");
        RoleResponsibilityActionBo roleResponsibilityAction1 = new RoleResponsibilityActionBo();
        roleResponsibilityAction1.setId(roleResponsibilityActionId);
        roleResponsibilityAction1.setRoleResponsibilityId(roleResponsibilityId);
        roleResponsibilityAction1.setRoleMemberId(user1RolePrincipal.getId());
        roleResponsibilityAction1.setActionTypeCode(KewApiConstants.ACTION_REQUEST_APPROVE_REQ);
        roleResponsibilityAction1.setActionPolicyCode(actionRequestPolicy.getCode());
        roleResponsibilityAction1.setPriorityNumber(1);
        roleResponsibilityAction1.setForceAction(true);
        roleResponsibilityAction1 = KRADServiceLocator.getBusinessObjectService().save(roleResponsibilityAction1);

        roleResponsibilityActionId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ROLE_RSP_ACTN_ID_S");
        RoleResponsibilityActionBo roleResponsibilityAction2 = new RoleResponsibilityActionBo();
        roleResponsibilityAction2.setId(roleResponsibilityActionId);
        roleResponsibilityAction2.setRoleResponsibilityId(roleResponsibilityId);
        roleResponsibilityAction2.setRoleMemberId(user2RolePrincipal.getId());
        roleResponsibilityAction2.setActionTypeCode(KewApiConstants.ACTION_REQUEST_APPROVE_REQ);
        roleResponsibilityAction2.setActionPolicyCode(actionRequestPolicy.getCode());
        roleResponsibilityAction2.setPriorityNumber(1);
        roleResponsibilityAction2.setForceAction(true);
        roleResponsibilityAction2 = KRADServiceLocator.getBusinessObjectService().save(roleResponsibilityAction2);

        roleResponsibilityActionId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_ROLE_RSP_ACTN_ID_S");
        RoleResponsibilityActionBo roleResponsibilityAction3 = new RoleResponsibilityActionBo();
        roleResponsibilityAction3.setId(roleResponsibilityActionId);
        roleResponsibilityAction3.setRoleResponsibilityId(roleResponsibilityId);
        roleResponsibilityAction3.setRoleMemberId(adminRolePrincipal.getId());
        roleResponsibilityAction3.setActionTypeCode(KewApiConstants.ACTION_REQUEST_APPROVE_REQ);
        roleResponsibilityAction3.setActionPolicyCode(actionRequestPolicy.getCode());
        roleResponsibilityAction3.setPriorityNumber(1);
        roleResponsibilityAction3.setForceAction(true);
        roleResponsibilityAction3 = KRADServiceLocator.getBusinessObjectService().save(roleResponsibilityAction3);

    }

    private void createDelegate(){

        if (suiteCreateDelegateInitialized) {
            return;
        }

        // create delegation KimType
        Long kimDlgnTypeId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_TYP_ID_S");
        KimTypeBo kimDlgnType = new KimTypeBo();
        kimDlgnType.setId("" + kimDlgnTypeId);
        kimDlgnType.setName("TestBaseDelegationType");
        kimDlgnType.setNamespaceCode(NAMESPACE);
        kimDlgnType.setServiceName("testBaseDelegationTypeService");
        kimDlgnType.setActive(true);
        kimDlgnType = KRADServiceLocator.getBusinessObjectService().save(kimDlgnType);

        /*
         * Manually create a delegate
         */
        String id = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_DLGN_MBR_ID_S");
        DelegateTypeBo delegate = new DelegateTypeBo();

        delegate.setDelegationId(id);
        delegate.setDelegationType(DelegationType.PRIMARY);
        delegate.setActive(true);
        delegate.setKimTypeId("" + kimDlgnTypeId);
        /*
         * Assign it a role that was created above.  This should mean that every
         * principle in the role can have the delegate added below as a delegate
         */
        Role role = KimApiServiceLocator.getRoleService().getRoleByNamespaceCodeAndName(NAMESPACE, ROLE_NAME);
        assertNotNull("Role should exist.", role);
        delegate.setRoleId(role.getId());
        delegate = KRADServiceLocator.getBusinessObjectService().save(delegate);

        // BC of the way the jpa is handled we have to create the delagate, then the members
        String delgMemberId = "" + KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRIM_DLGN_MBR_ID_S");
        DelegateMemberBo user1RoleDelegate = new DelegateMemberBo();
        user1RoleDelegate.setDelegationMemberId(delgMemberId);
        // This is the user the delegation requests should be sent to.
        // Note: If initiator is same as delegate, forceAction is utilized in responsibilities of approvers.
        Principal kPrincipal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("ewestfal");
        assertNotNull(kPrincipal);
        user1RoleDelegate.setMemberId(kPrincipal.getPrincipalId());
        /*
         * this is checked when adding delegates in both the ActionRequestFactory
         * and RoleServiceImpl
         */
        user1RoleDelegate.setType( MemberType.PRINCIPAL );

        // attach it to the delegate we created above
        user1RoleDelegate.setDelegationId(delegate.getDelegationId());

        user1RoleDelegate = KRADServiceLocator.getBusinessObjectService().save(user1RoleDelegate);

        suiteCreateDelegateInitialized = true;

    }


    @Test
    public void testRoleRouteModule_FirstApprove() throws Exception {

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "RoleRouteModuleTest1");
        document.route("");

        // in this case we should have a first approve role that contains admin and user2, we
        // should also have a first approve role that contains just user1

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());

        // examine the action requests
        List<ActionRequest> actionRequests = KewApiServiceLocator.getWorkflowDocumentService().getRootActionRequests(document.getDocumentId());
        // there should be 2 root action requests returned here, 1 containing the 2 requests for "BL", and one containing the request for "IN"
        assertEquals("Should have 3 action requests.", 3, actionRequests.size());
        int numRoots = 0;
        for (ActionRequest actionRequest : actionRequests) {
            // each of these should be "first approve"
            if (actionRequest.getRequestPolicy() != null) {
                assertEquals(ActionRequestPolicy.FIRST, actionRequest.getRequestPolicy());
            }
            if (actionRequest.getParentActionRequestId() == null) {
                numRoots++;
            }
        }
        assertEquals("There should have been 3 root requests.", 3, numRoots);

        // let's approve as "user1" and verify the document is still ENROUTE
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), document.getDocumentId());
        document.approve("");
        assertTrue("Document should be ENROUTE.", document.isEnroute());

        // verify that admin and user2 still have requests
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());

        // let's approve as "user2" and verify the document is still ENROUTE
        document.approve("");
        assertTrue("Document should be ENROUTE.", document.isEnroute());

        // let's approve as "admin" and verify the document has gone FINAL
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId());
        document.approve("");
        assertTrue("Document should be FINAL.", document.isFinal());
    }

    @Test
    public void testRoleRouteModule_AllApprove() throws Exception {

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "RoleRouteModuleTest2");
        document.route("");

        // in this case we should have all approve roles for admin, user1 and user2

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());

        // examine the action requests, because there was only 1 responsibility on each role, the KEW->KIM integration
        // transitions the requests from all approve to first approve
        List<ActionRequest> actionRequests = KewApiServiceLocator
                .getWorkflowDocumentService().getRootActionRequests(document.getDocumentId());
        assertEquals("Should have 3 action requests.", 3, actionRequests.size());
        int numRoots = 0;
        for (ActionRequest actionRequest : actionRequests) {
            assertNotNull(actionRequest.getRequestPolicy());
            assertEquals(ActionRequestPolicy.FIRST, actionRequest.getRequestPolicy());
            if (actionRequest.getParentActionRequestId() == null) {
                numRoots++;
            }
        }
        assertEquals("There should have been 3 root requests.", 3, numRoots);

        // let's approve as "user1" and verify the document does NOT go FINAL
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), document.getDocumentId());
        document.approve("");
        assertTrue("Document should still be enroute.", document.isEnroute());

        // verify that admin and user2 still have requests
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());

        // approve as "user2" and verify document is still ENROUTE
        document.approve("");
        assertTrue("Document should be ENROUTE.", document.isEnroute());

        // now approve as "admin", coument should be FINAL
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId());
        document.approve("");
        assertTrue("Document should be FINAL.", document.isFinal());
    }

    /**
     * Tests that ActionRequests are regenerated when RoleResponsibilityActions are programmatically changes via the RoleService
     * (Must be run before tests which alter delegate configuration)
     */
    @Test
    public void testRoleRouteModule_RoleResponsibilityActionUpdate() throws Exception {

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "RoleRouteModuleTest1");
        document.route("");

        // in this case we should have a first approve role that contains admin and user2, we
        // should also have a first approve role that contains just user1

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());

        // examine the action requests
        List<ActionRequest> actionRequests = KewApiServiceLocator.getWorkflowDocumentService().getRootActionRequests(document.getDocumentId());
        // there should be 2 root action requests returned here, 1 containing the 2 requests for "BL", and one containing the request for "IN"
        assertEquals("Should have 3 action requests.", 3, actionRequests.size());
        int numRoots = 0;
        for (ActionRequest actionRequest : actionRequests) {
            // each of these should be "first approve"
            if (actionRequest.getRequestPolicy() != null) {
                assertEquals(ActionRequestPolicy.FIRST, actionRequest.getRequestPolicy());
            }
            if (actionRequest.getParentActionRequestId() == null) {
                numRoots++;
            }
        }
        assertEquals("There should have been 3 root requests.", 3, numRoots);

        RoleService roleService = KimApiServiceLocator.getRoleService();
        Role role = roleService.getRoleByNamespaceCodeAndName(NAMESPACE, ROLE_NAME);
        assertNotNull(role);
        Principal user1Principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("user1");
        List<RoleMembership> members = roleService.getRoleMembers(Collections.singletonList(role.getId()), null);
        // find user1principal
        RoleMembership m = null;
        for (RoleMembership rm: members) {
            if (user1Principal.getPrincipalId().equals(rm.getMemberId())) {
                m = rm;
                break;
            }
        }
        assertNotNull("Failed to find user1Principal role membership", m);
        assertEquals(user1Principal.getPrincipalId(), m.getMemberId());
        List<RoleResponsibilityAction> rras = roleService.getRoleMemberResponsibilityActions(m.getId());
        assertEquals(2, rras.size());
        RoleResponsibilityAction rra = rras.get(0);
        RoleResponsibilityAction.Builder b = RoleResponsibilityAction.Builder.create(rra);
        b.setActionTypeCode(ActionType.ACKNOWLEDGE.getCode());

        roleService.updateRoleResponsibilityAction(b.build());

        // action request should have changed!
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), document.getDocumentId());
        assertFalse("Approval should NOT be requested.", document.isApprovalRequested());
        assertTrue("Acknowledge should be requested.", document.isAcknowledgeRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());

        roleService.deleteRoleResponsibilityAction(rras.get(0).getId());

        // no actions should be requested of user1 now
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user1"), document.getDocumentId());
        assertTrue(document.getRequestedActions().getRequestedActions().isEmpty());

        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("admin"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("user2"), document.getDocumentId());
        assertTrue("Approval should be requested.", document.isApprovalRequested());

        // examine the action requests
        actionRequests = KewApiServiceLocator.getWorkflowDocumentService().getRootActionRequests(document.getDocumentId());
        // now should be only 2 requests
        assertEquals("Should have 2 action requests.", 2, actionRequests.size());
    }

    @Test
    public void testRoleDelegate() throws Exception{
        this.createDelegate();

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "RoleRouteModuleTest2");
        document.route("");

        String ewestfalPrincipalId = getPrincipalIdForName("ewestfal");

        // now our fancy new delegate should have an action request
        document = WorkflowDocumentFactory.loadDocument(ewestfalPrincipalId, document.getDocumentId());
        assertTrue("ewestfal should be able to approve", document.isApprovalRequested());

        // let's look at the action requests, there should be 3 root requests, each one should have a delegation to ewestfal
        List<ActionRequest> actionRequests = document.getRootActionRequests();
        assertEquals(3, actionRequests.size());

        for (ActionRequest actionRequest : actionRequests) {
            // none of the root requests should be to ewestfal
            assertFalse(ewestfalPrincipalId.equals(actionRequest.getPrincipalId()));
            // but all of the delegate requests should!
            assertEquals(1, actionRequest.getChildRequests().size());
            ActionRequest delegateRequest = actionRequest.getChildRequests().get(0);
            assertEquals(ewestfalPrincipalId, delegateRequest.getPrincipalId());
            assertEquals("Delegation type should been PRIMARY", DelegationType.PRIMARY, delegateRequest.getDelegationType());
        }
    }

    @Test
    public void testRoleDelegateApproval() throws Exception{
        this.createDelegate();

        WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "RoleRouteModuleTest2");
        document.route("");

        // See if the delegate can approve the document
        document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
        assertTrue("ewestfal should have an approval request", document.isApprovalRequested());
        document.approve("");

        assertTrue("Document should have been approved by the delegate.", document.isFinal());
    }

    @Test
    public void testRoleWithNoMembers() throws Exception {
        getTransactionTemplate().execute(new TransactionCallback<Object>() {
            public Object doInTransaction(TransactionStatus status) {

                try {

                    // first let's clear all of the members out of our role

                    Role role = KimApiServiceLocator.getRoleService().getRoleByNamespaceCodeAndName(NAMESPACE,
                            ROLE_NAME);
                    Map<String, String> criteria = new HashMap<String, String>();
                    criteria.put("roleId", role.getId());
                    List<RoleMemberBo> roleMembers = (List<RoleMemberBo>) KRADServiceLocator
                            .getBusinessObjectService().findMatching(RoleMemberBo.class, criteria);
                    assertFalse("role member list should not currently be empty", roleMembers.isEmpty());
                    for (RoleMemberBo roleMember : roleMembers) {
                        //String roleMemberId = roleMember.getRoleMemberId();
                        //RoleMemberImpl roleMemberImpl = KRADServiceLocatorInternal.getBusinessObjectService().findBySinglePrimaryKey(RoleMemberImpl.class, roleMemberId);
                        assertNotNull("Role Member should exist.", roleMember);
                        KRADServiceLocator.getBusinessObjectService().delete(roleMember);
                    }

                    List<RoleMembership> roleMemberInfos = KimApiServiceLocator.getRoleService().getRoleMembers(Collections.singletonList(role.getId()), Collections.<String, String>emptyMap());
                    assertEquals("role member list should be empty now", 0, roleMemberInfos.size());

                    // now that we've removed all members from the Role, let's trying routing the doc!
                    WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "RoleRouteModuleTest1");
                    document.route("");

                    // the document should be final now, because the role has no members so all nodes should have been skipped for routing purposes

                    assertTrue("document should now be in final status", document.isFinal());

                    // verify that the document went through the appropriate route path

                    List<RouteNodeInstance> routeNodeInstances = document.getRouteNodeInstances();
                    assertEquals("Document should have 2 route node instances", 2, routeNodeInstances.size());

                    return null;
                } finally {
                    status.setRollbackOnly();
                }
            }
        });
    }

}
