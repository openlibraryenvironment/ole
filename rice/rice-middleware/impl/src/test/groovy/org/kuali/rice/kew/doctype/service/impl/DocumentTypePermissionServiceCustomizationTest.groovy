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
package org.kuali.rice.kew.doctype.service.impl

import org.joda.time.DateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.kuali.rice.core.api.CoreConstants
import org.kuali.rice.core.api.config.module.RunMode
import org.kuali.rice.core.api.config.property.ConfigContext
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader
import org.kuali.rice.core.api.resourceloader.ResourceLoader
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl
import org.kuali.rice.kew.api.KewApiConstants
import org.kuali.rice.kew.doctype.bo.DocumentType
import org.kuali.rice.kew.doctype.service.DocumentTypeService
import org.kuali.rice.kew.engine.node.RouteNodeInstance
import org.kuali.rice.kew.engine.node.service.RouteNodeService
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValueContent
import org.kuali.rice.kew.routeheader.service.RouteHeaderService
import org.kuali.rice.kew.service.KEWServiceLocator
import org.kuali.rice.kim.api.group.GroupService
import org.kuali.rice.kim.api.permission.PermissionService
import org.kuali.rice.krad.datadictionary.DataDictionary
import org.kuali.rice.krad.service.DataDictionaryService

import javax.xml.namespace.QName

/**
 * Tests per-DocumentType customization of permission details and role qualifiers
 */
@RunWith(Parameterized)
class DocumentTypePermissionServiceCustomizationTest {
    static def service = new DocumentTypePermissionServiceAuthorizerImpl() {
        @Override
        def boolean useKimPermission(String namespace, String permissionTemplateName, Map<String, String> permissionDetails, boolean checkKimPriorityInd) { true }
    }

    // have to override default impl to avoid kim check
    static def defaultDocumentTypeAuthorizer = new KimDocumentTypeAuthorizer() {
        @Override
        def boolean useKimPermission(String namespace, String permissionTemplateName, Map<String, String> permissionDetails, boolean checkKimPriorityInd) { true }
    }

    static def customDocumentTypeAuthorizer = new TestDocumentTypeAuthorizer()

    static def permissionService = [
        invocations: [],
        isAuthorizedByTemplate: { principalId, namespace, permission, Map<String, String> permissionDetails, Map<String, String> roleQualifiers ->
            permissionService.invocations << [ permissionDetails: permissionDetails, roleQualifiers: roleQualifiers ]
            return permissionDetails.containsKey("ADDITIONAL_ENTRY") && (roleQualifiers.isEmpty() || roleQualifiers.containsKey("ADDITIONAL_ENTRY"))
        },
        isPermissionDefinedByTemplate: { ns, perm, details -> true }
    ]

    def documentTypeAuthorizer = null
    def customized = false
    def additionalEntries = [:]

    def bo = new DocumentType() {
        def String authorizer = "testDocumentTypeAuthorizer"
        def String name = "TestDocumentType"
        def DocumentType parentDocType = null
    }
    def drhv = new DocumentRouteHeaderValue() {
        def DocumentType documentType = bo
        def String documentId = "0"
        def String documentTypeId = "0"
        def DateTime dateCreated = new DateTime();
        def String initiatorWorkflowId = "test initiatorid"
        def String initiatorPrincipalId = "test initiatorid"
        def String docRouteStatus = KewApiConstants.ROUTE_HEADER_INITIATED_CD
        def DocumentRouteHeaderValueContent documentContent = new DocumentRouteHeaderValueContent()
        def List<String> currentNodeNames = [ "routeNode1", "routeNode2" ]
    }
    def princ = "test principal id"
    def nodeNames = ["routeNode1", "routeNode2"]
    def nodeInstances = [
        new RouteNodeInstance() { def String getName() { "routeNode1" } },
        new RouteNodeInstance() { def String getName() { "routeNode2" } }
    ]

    @Parameterized.Parameters static data() {
        return [ [ false, defaultDocumentTypeAuthorizer, [:] ] as Object[],
                 [ true, customDocumentTypeAuthorizer, TestDocumentTypeAuthorizer.ADDITIONAL_ENTRIES ] as Object[]]
    }

    DocumentTypePermissionServiceCustomizationTest(callResult, documentTypeAuthorizer, additionalEntries) {
        this.customized = callResult
        this.documentTypeAuthorizer = documentTypeAuthorizer
        this.additionalEntries = additionalEntries
    }

    @Before
    void setupFakeEnv() {
        def config = new JAXBConfigImpl();
        config.putProperty(CoreConstants.Config.APPLICATION_ID, "APPID");
        config.putProperty(KEWServiceLocator.KEW_RUN_MODE_PROPERTY, RunMode.LOCAL.toString());
        ConfigContext.init(config);

        GlobalResourceLoader.stop()
        GlobalResourceLoader.addResourceLoader([
            getName: { -> new QName("KEW Unit Test", "DocumentTypePermissionServiceDelegatorTest") },
            getService: { QName name ->
                [
                    testDocumentTypeAuthorizer: documentTypeAuthorizer,
                    kimPermissionService: permissionService as PermissionService,
                    kimGroupService: [
                        getMemberPrincipalIds: { ["groupmemberid"] }
                    ] as GroupService,
                    dataDictionaryService: [
                        getDataDictionary: { [ getDocumentEntry: { null } ] as DataDictionary }
                    ] as DataDictionaryService,
                    enDocumentRouteHeaderService: [
                        getContent: { id -> new DocumentRouteHeaderValueContent() }
                    ] as RouteHeaderService,
                    enDocumentTypeService: [
                        findById: { bo },
                        findByName: { bo }
                    ] as DocumentTypeService,
                    enRouteNodeService: [
                        getCurrentRouteNodeNames: { drhv.currentNodeNames }
                    ] as RouteNodeService
                ][name.getLocalPart()]
            },
            getObject: { null },
            stop: {}
        ] as ResourceLoader)

        permissionService.invocations.clear()
        drhv.docRouteStatus = KewApiConstants.ROUTE_HEADER_INITIATED_CD
    }

    @Test
    def void testCanAddRouteLogMessage() {
        // KimDocumentTypeAuthorizer does not apply to this call
        Assert.assertEquals(false, service.canAddRouteLogMessage(princ, drhv))
        Assert.assertEquals(1, permissionService.invocations.size())
        Assert.assertEquals([routeNodeName: "PreRoute", documentTypeName: "TestDocumentType", routeStatusCode: "I"], permissionService.invocations[0].permissionDetails)
        Assert.assertEquals([routeNodeName: "PreRoute", documentNumber: "0", documentTypeName: "TestDocumentType", routeStatusCode: "I"], permissionService.invocations[0].roleQualifiers)
    }

    @Test
    def void testCanAdministerRouting() {
        // KimDocumentTypeAuthorizer does not apply to this call
        Assert.assertEquals(false, service.canAdministerRouting(princ, bo))
        Assert.assertEquals(1, permissionService.invocations.size())
        Assert.assertEquals([documentTypeName: "TestDocumentType"], permissionService.invocations[0].permissionDetails)
        Assert.assertEquals([:], permissionService.invocations[0].roleQualifiers)
    }

    @Test
    def void testCanBlanketApprove() {
        Assert.assertEquals(customized, service.canBlanketApprove(princ, drhv))
        Assert.assertEquals(1, permissionService.invocations.size())
        Assert.assertEquals([routeNodeName: "PreRoute", documentTypeName: "TestDocumentType", routeStatusCode: "I"] + additionalEntries, permissionService.invocations[0].permissionDetails)
        Assert.assertEquals([:], permissionService.invocations[0].roleQualifiers)
    }

    @Test
    def void testCanCancel() {
        drhv.docRouteStatus = KewApiConstants.ROUTE_HEADER_ENROUTE_CD
        Assert.assertEquals(customized, service.canCancel(princ, drhv))
        if (customized) {
            Assert.assertEquals(1, permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([routeNodeName: "routeNode1", documentNumber: "0", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].roleQualifiers)
        } else {
            Assert.assertEquals(nodeNames.size(), permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([routeNodeName: "routeNode1", documentNumber: "0", documentTypeName: "TestDocumentType", routeStatusCode: "R"], permissionService.invocations[0].roleQualifiers)
            Assert.assertEquals([routeNodeName: "routeNode2", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[1].permissionDetails)
            Assert.assertEquals([routeNodeName: "routeNode2", documentNumber: "0", documentTypeName: "TestDocumentType", routeStatusCode: "R"], permissionService.invocations[1].roleQualifiers)
        }

    }

    @Test
    def void testCanGroupReceiveAdHocRequest() {
        // KimDocumentTypeAuthorizer does not apply to this call
        Assert.assertEquals(false, service.canGroupReceiveAdHocRequest("group id", drhv, KewApiConstants.ACTION_REQUEST_APPROVE_REQ))
        Assert.assertEquals(1, permissionService.invocations.size())
        Assert.assertEquals([actionRequestCd: "A", documentTypeName: "TestDocumentType"], permissionService.invocations[0].permissionDetails)
        Assert.assertEquals([:], permissionService.invocations[0].roleQualifiers)
    }

    @Test
    def void testCanInitiate() {
        Assert.assertEquals(customized, service.canInitiate(princ, bo))
        Assert.assertEquals(1, permissionService.invocations.size())
        Assert.assertEquals([documentTypeName: "TestDocumentType"] + additionalEntries, permissionService.invocations[0].permissionDetails)
        Assert.assertEquals([:], permissionService.invocations[0].roleQualifiers)
    }

    @Test
    def void testCanRecall() {
        drhv.docRouteStatus = KewApiConstants.ROUTE_HEADER_ENROUTE_CD
        Assert.assertEquals(customized, service.canRecall(princ, drhv))
        if (customized) {
            Assert.assertEquals(1, permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([routeNodeName: "routeNode1", documentNumber: "0", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].roleQualifiers)
        } else {
            Assert.assertEquals(nodeNames.size(), permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([routeNodeName: "routeNode1", documentNumber: "0", documentTypeName: "TestDocumentType", routeStatusCode: "R"], permissionService.invocations[0].roleQualifiers)
            Assert.assertEquals([routeNodeName: "routeNode2", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[1].permissionDetails)
            Assert.assertEquals([routeNodeName: "routeNode2", documentNumber: "0", documentTypeName: "TestDocumentType", routeStatusCode: "R"], permissionService.invocations[1].roleQualifiers)
        }
    }

    @Test
    def void testCanReceiveAdHocRequest() {
        // KimDocumentTypeAuthorizer does not apply to this call
        Assert.assertEquals(false, service.canReceiveAdHocRequest("group id", drhv, KewApiConstants.ACTION_REQUEST_APPROVE_REQ))
        Assert.assertEquals(1, permissionService.invocations.size())
        Assert.assertEquals([actionRequestCd: "A", documentTypeName: "TestDocumentType"], permissionService.invocations[0].permissionDetails)
        Assert.assertEquals([:], permissionService.invocations[0].roleQualifiers)
    }

    @Test
    def void testCanRoute() {
        Assert.assertEquals(customized, service.canRoute(princ, drhv))
        Assert.assertEquals(1, permissionService.invocations.size())
        Assert.assertEquals([routeNodeName: "PreRoute", documentTypeName: "TestDocumentType", routeStatusCode: "I"] + additionalEntries, permissionService.invocations[0].permissionDetails)
        Assert.assertEquals([routeNodeName: "PreRoute", documentNumber: "0", documentTypeName: "TestDocumentType", routeStatusCode: "I"] + additionalEntries, permissionService.invocations[0].roleQualifiers)
    }

    @Test
    def void testCanSave() {
        drhv.docRouteStatus = KewApiConstants.ROUTE_HEADER_ENROUTE_CD
        Assert.assertEquals(customized, service.canSave(princ, drhv))
        if (customized) {
            Assert.assertEquals(1, permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([routeNodeName: "routeNode1", documentNumber: "0", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].roleQualifiers)
        } else {
            Assert.assertEquals(nodeNames.size(), permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([routeNodeName: "routeNode1", documentNumber: "0", documentTypeName: "TestDocumentType", routeStatusCode: "R"], permissionService.invocations[0].roleQualifiers)
            Assert.assertEquals([routeNodeName: "routeNode2", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[1].permissionDetails)
            Assert.assertEquals([routeNodeName: "routeNode2", documentNumber: "0", documentTypeName: "TestDocumentType", routeStatusCode: "R"], permissionService.invocations[1].roleQualifiers)
        }
    }

    @Test
    def void testCanSuperUserApproveDocument() {
        Assert.assertEquals(customized, service.canSuperUserApproveDocument(princ, bo, nodeInstances, KewApiConstants.ROUTE_HEADER_ENROUTE_CD))
        if (customized) {
            Assert.assertEquals(1, permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([:], permissionService.invocations[0].roleQualifiers)
        } else {
            Assert.assertEquals(nodeInstances.size(), permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([:], permissionService.invocations[0].roleQualifiers)
            Assert.assertEquals([routeNodeName: "routeNode2", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[1].permissionDetails)
            Assert.assertEquals([:], permissionService.invocations[1].roleQualifiers)
        }
    }

    @Test
    def void testCanSuperUserApproveSingleActionRequest() {
        Assert.assertEquals(customized, service.canSuperUserApproveSingleActionRequest(princ, bo, nodeInstances, KewApiConstants.ROUTE_HEADER_ENROUTE_CD))
        if (customized) {
            Assert.assertEquals(1, permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([:], permissionService.invocations[0].roleQualifiers)
        } else {
            Assert.assertEquals(nodeInstances.size(), permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([:], permissionService.invocations[0].roleQualifiers)
            Assert.assertEquals([routeNodeName: "routeNode2", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[1].permissionDetails)
            Assert.assertEquals([:], permissionService.invocations[1].roleQualifiers)
        }

    }

    @Test
    def void testCanSuperUserDisapproveDocument() {
        Assert.assertEquals(customized, service.canSuperUserDisapproveDocument(princ, bo, nodeInstances, KewApiConstants.ROUTE_HEADER_ENROUTE_CD))
        if (customized) {
            Assert.assertEquals(1, permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([:], permissionService.invocations[0].roleQualifiers)
        } else {
            Assert.assertEquals(nodeInstances.size(), permissionService.invocations.size())
            Assert.assertEquals([routeNodeName: "routeNode1", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[0].permissionDetails)
            Assert.assertEquals([:], permissionService.invocations[0].roleQualifiers)
            Assert.assertEquals([routeNodeName: "routeNode2", documentTypeName: "TestDocumentType", routeStatusCode: "R"] + additionalEntries, permissionService.invocations[1].permissionDetails)
            Assert.assertEquals([:], permissionService.invocations[1].roleQualifiers)
        }
    }
}
