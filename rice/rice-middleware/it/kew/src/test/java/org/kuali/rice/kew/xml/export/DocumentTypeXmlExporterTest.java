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
package org.kuali.rice.kew.xml.export;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kew.doctype.DocumentTypePolicy;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.node.BranchPrototype;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.export.KewExportDataSet;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.test.BaselineTestCase;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class DocumentTypeXmlExporterTest extends XmlExporterTestCase {
    
    private static final Logger LOG = Logger.getLogger(DocumentTypeXmlExporterTest.class);

	@Test public void testExportDynamicProcessConfig() throws Exception {
    	loadXmlFile("DocTypeExportRuleTemplateConfig.xml");
        loadXmlFile("DocTypeExportConfig.xml");
        assertExport();
    }

    protected void assertExport() throws Exception {
        List documentTypes = KEWServiceLocator.getDocumentTypeService().findAllCurrent();
        for (Iterator iterator = documentTypes.iterator(); iterator.hasNext();) {
            DocumentType existingDocType = (DocumentType) iterator.next();
            KewExportDataSet dataSet = new KewExportDataSet();
            dataSet.getDocumentTypes().add(existingDocType);
            byte[] xmlBytes = CoreApiServiceLocator.getXmlExporterService().export(dataSet.createExportDataSet());
            assertTrue("XML should be non empty.", xmlBytes != null && xmlBytes.length > 0);
            loadXmlStream(new BufferedInputStream(new ByteArrayInputStream(xmlBytes)));
            DocumentType newDocType = KEWServiceLocator.getDocumentTypeService().findByName(existingDocType.getName());
            LOG.info("checking export for " + existingDocType.getName());
            assertDocTypeExport(existingDocType, newDocType);
        }
    }

    private void assertDocTypeExport(DocumentType oldDocType, DocumentType newDocType) {
        // assert fields which should be different
        assertFalse("Document type ids should be different.", oldDocType.getDocumentTypeId().equals(newDocType.getDocumentTypeId()));
        assertTrue("Version should be one greater.", newDocType.getVersion().intValue() == oldDocType.getVersion().intValue()+1);
        assertEquals("Previous version should be old doc type.", oldDocType.getDocumentTypeId(), newDocType.getPreviousVersionId());

        // assert fields which should be the same
        assertEquals("Should have same name", oldDocType.getName(), newDocType.getName());
        if (oldDocType.getParentDocType() == null) {
            assertNull("Should have same parent.", newDocType.getParentDocType());
        } else {
            assertEquals("Should have same parent.", oldDocType.getParentDocType().getName(), newDocType.getParentDocType().getName());
        }
        assertEquals(oldDocType.getActive(), newDocType.getActive());
        assertEquals(oldDocType.getDescription(), newDocType.getDescription());
        assertEquals(oldDocType.getUnresolvedDocHandlerUrl(), newDocType.getUnresolvedDocHandlerUrl());
        assertEquals(oldDocType.getUnresolvedHelpDefinitionUrl(), newDocType.getUnresolvedHelpDefinitionUrl());
        assertEquals(oldDocType.getUnresolvedDocSearchHelpUrl(), newDocType.getUnresolvedDocSearchHelpUrl());
        assertEquals(oldDocType.getLabel(), newDocType.getLabel());
        assertEquals(oldDocType.getPostProcessorName(), newDocType.getPostProcessorName());
        assertEquals(oldDocType.getAuthorizer(), newDocType.getAuthorizer());
        assertEquals(oldDocType.getRoutingVersion(), newDocType.getRoutingVersion());
        assertWorkgroupsEqual(oldDocType.getBlanketApproveWorkgroup(), newDocType.getBlanketApproveWorkgroup());
        assertEquals(oldDocType.getBlanketApprovePolicy(),newDocType.getBlanketApprovePolicy());
        assertEquals(oldDocType.getCurrentInd(), newDocType.getCurrentInd());
        
        assertWorkgroupsEqual(oldDocType.getSuperUserWorkgroup(), newDocType.getSuperUserWorkgroup());
        assertWorkgroupsEqual(oldDocType.getSuperUserWorkgroupNoInheritence(), newDocType.getSuperUserWorkgroupNoInheritence());
        assertEquals(oldDocType.getActualNotificationFromAddress(), newDocType.getActualNotificationFromAddress());
        assertEquals(oldDocType.getActualApplicationId(), newDocType.getActualApplicationId());
        assertRoutePath(oldDocType, newDocType);
        assertPolicies(oldDocType, newDocType);
    }

    /**
     * Asserts that two workgroup objects are either both null, or both have the same id
     * @param a a workgroup
     * @param b another workgroup
     */
    private void assertWorkgroupsEqual(Group a, Group b) {
        if (a == null) {
            assertNull(b);
        } else {
            assertNotNull(b);
            assertEquals(a.getId(), b.getId());
        }
        
    }

    private void assertRoutePath(DocumentType oldDocType, DocumentType newDocType) {
        for (Iterator iterator = oldDocType.getProcesses().iterator(); iterator.hasNext();) {
            ProcessDefinitionBo oldProcess = (ProcessDefinitionBo) iterator.next();
            ProcessDefinitionBo newProcess = newDocType.getNamedProcess(oldProcess.getName());
            assertRouteNodes(oldProcess.getInitialRouteNode(), newProcess.getInitialRouteNode(), new HashSet());
        }
    }

    private void assertRouteNodes(RouteNode oldNode, RouteNode newNode, Set processedNodeIds) {
        // it's possible that the doc type will have no route nodes
        if (oldNode == null && newNode == null) return;
        
        if (processedNodeIds.contains(oldNode.getRouteNodeId())) {
            if (!processedNodeIds.contains(newNode.getRouteNodeId())) {
                fail("If old node is processed, new node should also be processed.");
            }
            return;
        }
        assertEquals(oldNode.getRouteNodeName(), newNode.getRouteNodeName());
        assertEquals(oldNode.getActivationType(), newNode.getActivationType());
        assertEquals(oldNode.getExceptionWorkgroupId(), newNode.getExceptionWorkgroupId());
        assertEquals(oldNode.getNodeType(), newNode.getNodeType());
        assertEquals(oldNode.getRouteMethodCode(), newNode.getRouteMethodCode());
        assertEquals(oldNode.getRouteMethodName(), newNode.getRouteMethodName());
        assertEquals(oldNode.getDocumentType().getName(), newNode.getDocumentType().getName());
        assertEquals(oldNode.getFinalApprovalInd(), newNode.getFinalApprovalInd());
        assertEquals(oldNode.getMandatoryRouteInd(), newNode.getMandatoryRouteInd());
        assertBranches(oldNode.getBranch(), newNode.getBranch());
        assertEquals(oldNode.getNextNodes().size(), newNode.getNextNodes().size());
        processedNodeIds.add(oldNode.getRouteNodeId());
        processedNodeIds.add(newNode.getRouteNodeId());
        for (Iterator iterator = oldNode.getNextNodes().iterator(); iterator.hasNext();) {
            RouteNode nextOldNode = (RouteNode) iterator.next();
            boolean foundNode = false;
            for (Iterator iterator2 = newNode.getNextNodes().iterator(); iterator2.hasNext();) {
                RouteNode nextNewNode = (RouteNode) iterator2.next();
                if (nextNewNode.getRouteNodeName().equals(nextOldNode.getRouteNodeName())) {
                    foundNode = true;
                    assertRouteNodes(nextOldNode, nextNewNode, processedNodeIds);
                    break;
                }
            }
            assertTrue("Could not locate new node by name: " + nextOldNode.getRouteNodeName(), foundNode);
        }
    }

    private void assertBranches(BranchPrototype oldBranch, BranchPrototype newBranch) {
        if (oldBranch == null) {
            assertNull("New Branch should also be null.", newBranch);
        } else {
            assertEquals(oldBranch.getName(), newBranch.getName());
        }
    }

    private void assertPolicies(DocumentType oldDocType, DocumentType newDocType) {
        assertEquals(oldDocType.getDocumentTypePolicies().size(), newDocType.getDocumentTypePolicies().size());
        for (Iterator iterator = oldDocType.getDocumentTypePolicies().iterator(); iterator.hasNext();) {
            DocumentTypePolicy oldPolicy = (DocumentTypePolicy) iterator.next();
            boolean foundPolicy = false;
            for (Iterator iterator2 = newDocType.getDocumentTypePolicies().iterator(); iterator2.hasNext();) {
                DocumentTypePolicy newPolicy = (DocumentTypePolicy) iterator2.next();
                if (oldPolicy.getPolicyName().equals(newPolicy.getPolicyName())) {
                    foundPolicy = true;
                    assertEquals(oldPolicy.getInheritedFlag(), newPolicy.getInheritedFlag());
                    assertEquals(oldPolicy.getPolicyValue(), newPolicy.getPolicyValue());
                    break;
                }
            }
            assertTrue("Could not locate policy by name " + oldPolicy.getPolicyName(), foundPolicy);
        }
    }

}
