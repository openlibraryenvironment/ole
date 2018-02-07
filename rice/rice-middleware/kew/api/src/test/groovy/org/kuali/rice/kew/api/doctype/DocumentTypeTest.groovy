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
package org.kuali.rice.kew.api.doctype

import org.junit.Test
import org.junit.Assert
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import org.junit.internal.ExactComparisonCriteria
import org.kuali.rice.core.test.JAXBAssert

/**
 * Tests DocumentType JAXB marshalling
 */
class DocumentTypeTest {
    private static final String EXPECTED_XML = """
        <documentType xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns="http://rice.kuali.org/kew/v2_0">
            <id>fakeid</id>
            <name>DocumentTypeTestName</name>
            <documentTypeVersion>0</documentTypeVersion>
            <label>documenttypetest label</label>
            <description>documenttypetest description</description>
            <parentId>fakeparentid</parentId>
            <active>true</active>
            <unresolvedDocHandlerUrl>http://fakedochandlerurl</unresolvedDocHandlerUrl>
            <resolvedDocumentHandlerUrl>http://fakeresolvedDocumentHandlerUrl</resolvedDocumentHandlerUrl>
            <helpDefinitionUrl>http://fakehelpdefinitionurl</helpDefinitionUrl>
            <docSearchHelpUrl>http://fakedocsearchhelpurl</docSearchHelpUrl>
            <postProcessorName>postprocessor name</postProcessorName>
            <applicationId>application id</applicationId>
            <current>true</current>
            <blanketApproveGroupId>fakeblanketapprovegroupid</blanketApproveGroupId>
            <superUserGroupId>fakesuperusergroupid</superUserGroupId>
            <policies>
                <ns2:entry key="DEFAULT_APPROVE">Y</ns2:entry>
            </policies>
            <versionNumber>0</versionNumber>
            <authorizer>fakeDocumentTypeAuthorizer</authorizer>
        </documentType>
        """

    @Test
    void happy_path(){
        DocumentType.Builder.create("fake name")
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_create_fail_null_contract(){
        DocumentTypeContract contract = null
        DocumentType.Builder.create(contract)
    }

    @Test
    void test_copy(){
        def o1b = DocumentType.Builder.create("fake name")
        def o1 = o1b.build()
        def o2 = DocumentType.Builder.create(o1).build()
        Assert.assertEquals(o1, o2) //.actionSetList, o2.actionSetList)
    }

    @Test
    public void test_Xml_Marshal_Unmarshal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), EXPECTED_XML, DocumentType.class)
    }

    public static DocumentType create() {
        return DocumentType.Builder.create(new DocumentTypeContract() {
            def String id = "fakeid"
            def Long versionNumber = 0
            def String name = "DocumentTypeTestName"
            def Integer documentTypeVersion = 0
            def String label = "documenttypetest label"
            def String description = "documenttypetest description"
            def String parentId = "fakeparentid"
            def boolean active = true
            def String unresolvedDocHandlerUrl = "http://fakedochandlerurl"
            def String resolvedDocumentHandlerUrl = "http://fakeresolvedDocumentHandlerUrl"
            def String helpDefinitionUrl = "http://fakehelpdefinitionurl"
            def String docSearchHelpUrl = "http://fakedocsearchhelpurl"
            def String postProcessorName = "postprocessor name"
            def String applicationId = "application id"
            def boolean current = true
            def String blanketApproveGroupId = "fakeblanketapprovegroupid"
            def String superUserGroupId = "fakesuperusergroupid"
            def Map<DocumentTypePolicy, String> getPolicies() {
               def policies = new HashMap<DocumentTypePolicy, String>();
               policies.put(DocumentTypePolicy.DEFAULT_APPROVE, "Y")
               policies
            }
            def List<DocumentTypeAttribute> documentTypeAttributes = Collections.EMPTY_LIST
            def String authorizer = "fakeDocumentTypeAuthorizer"
        }).build()
    }
}
