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
import org.kuali.rice.core.test.JAXBAssert
import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.lang.math.RandomUtils

/**
 * Tests RouteNode JAXB marshalling
 */
class RouteNodeTest {
    private static final String EXPECTED_XML = """
        <routeNode xmlns="http://rice.kuali.org/kew/v2_0">
          <id>id</id>
          <documentTypeId>documentTypeId</documentTypeId>
          <name>name</name>
          <routeMethodName>routeMethodName</routeMethodName>
          <routeMethodCode>routeMethodCode</routeMethodCode>
          <finalApproval>false</finalApproval>
          <mandatory>false</mandatory>
          <activationType>activationType</activationType>
          <exceptionGroupId>exceptionGroupId</exceptionGroupId>
          <type>type</type>
          <branchName>branchName</branchName>
          <nextDocumentStatus>nextDocumentStatus</nextDocumentStatus>
          <configurationParameters>
            <configurationParameter><id>id0</id><routeNodeId>routeNodeId</routeNodeId><key>key0</key><value>value0</value></configurationParameter>
            <configurationParameter><id>id1</id><routeNodeId>routeNodeId</routeNodeId><key>key1</key><value>value1</value></configurationParameter>
            <configurationParameter><id>id2</id><routeNodeId>routeNodeId</routeNodeId><key>key2</key><value>value2</value></configurationParameter>
            <configurationParameter><id>id3</id><routeNodeId>routeNodeId</routeNodeId><key>key3</key><value>value3</value></configurationParameter>
            <configurationParameter><id>id4</id><routeNodeId>routeNodeId</routeNodeId><key>key4</key><value>value4</value></configurationParameter>
          </configurationParameters>
          <previousNodeIds>
            <previousNodeId>previousNodeId0</previousNodeId>
            <previousNodeId>previousNodeId1</previousNodeId>
            <previousNodeId>previousNodeId2</previousNodeId>
            <previousNodeId>previousNodeId3</previousNodeId>
            <previousNodeId>previousNodeId4</previousNodeId>
          </previousNodeIds>
          <nextNodeIds>
            <nextNodeId>nextNodeId0</nextNodeId>
            <nextNodeId>nextNodeId1</nextNodeId>
            <nextNodeId>nextNodeId2</nextNodeId>
            <nextNodeId>nextNodeId3</nextNodeId>
            <nextNodeId>nextNodeId4</nextNodeId>
          </nextNodeIds>
          <versionNumber>1</versionNumber>
        </routeNode>
        """

    @Test
    void happy_path(){
        RouteNode.Builder.create("name", "type")
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_create_fail_null_contract(){
        RouteNodeContract contract = null
        RouteNode.Builder.create(contract)
    }

    @Test
    void test_copy(){
        def o1b = RouteNode.Builder.create("name", "type")
        def o1 = o1b.build()
        def o2 = RouteNode.Builder.create(o1).build()
        Assert.assertEquals(o1, o2)
    }

    @Test
    public void test_Xml_Marshal_Unmarshal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), EXPECTED_XML, RouteNode.class)
    }

    public static RouteNode create() {
        return RouteNode.Builder.create(new RouteNodeContract() {
            String documentTypeId = "documentTypeId"
            String name = "name"
            String routeMethodName = "routeMethodName"
            String routeMethodCode = "routeMethodCode"
            boolean finalApproval = false
            boolean mandatory = false
            String activationType = "activationType"
            String exceptionGroupId = "exceptionGroupId"
            String type = "type"
            String branchName = "branchName"
            String nextDocumentStatus = "nextDocumentStatus"
            List<? extends RouteNodeConfigurationParameterContract> getConfigurationParameters() {
                def list = new ArrayList<RouteNodeConfigurationParameterContract>()
                for (i in 0..4) {
                    list << new RouteNodeConfigurationParameterContract() {
                        String routeNodeId = "routeNodeId"
                        String id = "id" + i
                        String key = "key" + i
                        String value = "value" + i
                    }
                }
                list
            }
            List<String> getPreviousNodeIds() {
                def list = new ArrayList<String>()
                for (i in 0..4) {
                    list << "previousNodeId" + i
                }
                list
            }
            List<String> getNextNodeIds() {
                def list = new ArrayList<String>()
                for (i in 0..4) {
                    list << "nextNodeId" + i
                }
                list
            }
            String id = "id"
            Long versionNumber = 1
        }).build()
    }
}