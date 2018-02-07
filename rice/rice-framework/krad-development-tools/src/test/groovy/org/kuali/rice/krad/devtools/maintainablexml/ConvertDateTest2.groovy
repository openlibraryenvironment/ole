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
package org.kuali.rice.krad.devtools.maintainablexml

import org.apache.commons.lang.StringUtils
import org.kuali.rice.krad.service.impl.XmlObjectSerializerServiceImpl

oldXml = """<maintainableDocumentContents maintainableImplClass="org.kuali.rice.kew.document.RoutingRuleMaintainable"><oldMaintainableObject><org.kuali.rice.kew.rule.RuleBaseValues>
  <ruleBaseValuesId>1310610</ruleBaseValuesId>
  <name>86ABB42F-3CA3-E472-33EA-A4F9BE8F5FAD</name>
  <ruleTemplateId>892818</ruleTemplateId>
  <activeInd>true</activeInd>
  <description>adding rule for rice upgrade testing  ua-humm  sm</description>
  <docTypeName>HREEDocs v2</docTypeName>
  <routeHeaderId>6186600</routeHeaderId>
  <fromDate>2010-05-09</fromDate>
  <toDate>2100-05-09</toDate>
  <currentInd>true</currentInd>
  <versionNbr>0</versionNbr>
  <forceAction>false</forceAction>
  <ruleTemplate>
    <ruleTemplateId>892818</ruleTemplateId>
    <name>HRMS Review Hierarchy Acknowledgement v2</name>
    <description>HRMS Review Hierarchy Acknowledgement v2</description>
    <versionNumber>1</versionNumber>
    <objectId>86243097FAFC327CE0404F0A3A627217</objectId>
    <newCollectionRecord>false</newCollectionRecord>
    <autoIncrementSet>false</autoIncrementSet>
  </ruleTemplate>
  <activationDate>2010-05-09 10:16:30.0</activationDate>
  <delegateRule>false</delegateRule>
  <templateRuleInd>false</templateRuleInd>
  <personResponsibilities class="org.kuali.rice.kns.util.TypedArrayList" serialization="custom">
    <unserializable-parents/>
    <org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
      <default>
        <size>1</size>
      </default>
      <int>10</int>
      <org.kuali.rice.kew.rule.PersonRuleResponsibility>
        <principalName>ldcccram</principalName>
        <ruleResponsibilityKey>241226</ruleResponsibilityKey>
        <responsibilityId>241225</responsibilityId>
        <ruleBaseValuesId>1310610</ruleBaseValuesId>
        <actionRequestedCd>K</actionRequestedCd>
        <ruleResponsibilityName>005555838</ruleResponsibilityName>
        <ruleResponsibilityType>F</ruleResponsibilityType>
        <priority>1</priority>
        <approvePolicy>F</approvePolicy>
        <versionNumber>1</versionNumber>
        <objectId>470EF13D-380D-34EE-01C2-8C56F3B835BB</objectId>
        <newCollectionRecord>false</newCollectionRecord>
        <autoIncrementSet>false</autoIncrementSet>
      </org.kuali.rice.kew.rule.PersonRuleResponsibility>
    </org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
    <org.kuali.rice.kns.util.TypedArrayList>
      <default>
        <listObjectType>org.kuali.rice.kew.rule.PersonRuleResponsibility</listObjectType>
      </default>
    </org.kuali.rice.kns.util.TypedArrayList>
  </personResponsibilities>
  <groupResponsibilities class="org.kuali.rice.kns.util.TypedArrayList" serialization="custom">
    <unserializable-parents/>
    <org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
      <default>
        <size>0</size>
      </default>
      <int>10</int>
    </org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
    <org.kuali.rice.kns.util.TypedArrayList>
      <default>
        <listObjectType>org.kuali.rice.kew.rule.GroupRuleResponsibility</listObjectType>
      </default>
    </org.kuali.rice.kns.util.TypedArrayList>
  </groupResponsibilities>
  <roleResponsibilities class="org.kuali.rice.kns.util.TypedArrayList" serialization="custom">
    <unserializable-parents/>
    <org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
      <default>
        <size>0</size>
      </default>
      <int>10</int>
    </org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
    <org.kuali.rice.kns.util.TypedArrayList>
      <default>
        <listObjectType>org.kuali.rice.kew.rule.RoleRuleResponsibility</listObjectType>
      </default>
    </org.kuali.rice.kns.util.TypedArrayList>
  </roleResponsibilities>
  <fieldValues>
    <entry>
      <string>892819:pos_typ</string>
      <string>XX</string>
    </entry>
    <entry>
      <string>892819:fin_coa_cd</string>
      <string>AA</string>
    </entry>
    <entry>
      <string>892819:org_cd</string>
      <string>ZZZZ</string>
    </entry>
    <entry>
      <string>892819:sal_plan</string>
      <string>Any</string>
    </entry>
  </fieldValues>
  <versionNumber>1</versionNumber>
  <objectId>C8FB7680-6A0E-37A5-78B2-377DF880477E</objectId>
  <newCollectionRecord>false</newCollectionRecord>
  <autoIncrementSet>false</autoIncrementSet>
</org.kuali.rice.kew.rule.RuleBaseValues><maintenanceAction>Edit</maintenanceAction>
</oldMaintainableObject><newMaintainableObject><org.kuali.rice.kew.rule.RuleBaseValues>
  <ruleBaseValuesId>1310610</ruleBaseValuesId>
  <name>86ABB42F-3CA3-E472-33EA-A4F9BE8F5FAD</name>
  <ruleTemplateId>892818</ruleTemplateId>
  <previousVersionId>1310610</previousVersionId>
  <activeInd>false</activeInd>
  <description>adding rule for rice upgrade testing  ua-humm  sm</description>
  <docTypeName>HREEDocs v2</docTypeName>
  <routeHeaderId>6186616</routeHeaderId>
  <fromDate>2010-05-09</fromDate>
  <toDate>2010-05-09</toDate>
  <currentInd>true</currentInd>
  <versionNbr>0</versionNbr>
  <forceAction>false</forceAction>
  <ruleTemplate>
    <ruleTemplateId>892818</ruleTemplateId>
    <name>HRMS Review Hierarchy Acknowledgement v2</name>
    <description>HRMS Review Hierarchy Acknowledgement v2</description>
    <versionNumber>1</versionNumber>
    <objectId>86243097FAFC327CE0404F0A3A627217</objectId>
    <newCollectionRecord>false</newCollectionRecord>
    <autoIncrementSet>false</autoIncrementSet>
  </ruleTemplate>
  <activationDate>2010-05-09 10:16:30.0</activationDate>
  <delegateRule>false</delegateRule>
  <templateRuleInd>false</templateRuleInd>
  <personResponsibilities class="org.kuali.rice.kns.util.TypedArrayList" serialization="custom">
    <unserializable-parents/>
    <org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
      <default>
        <size>1</size>
      </default>
      <int>10</int>
      <org.kuali.rice.kew.rule.PersonRuleResponsibility>
        <principalName>ldixxxxm</principalName>
        <ruleResponsibilityKey>241226</ruleResponsibilityKey>
        <responsibilityId>241225</responsibilityId>
        <ruleBaseValuesId>1310610</ruleBaseValuesId>
        <actionRequestedCd>K</actionRequestedCd>
        <ruleResponsibilityName>000000008</ruleResponsibilityName>
        <ruleResponsibilityType>F</ruleResponsibilityType>
        <priority>1</priority>
        <approvePolicy>F</approvePolicy>
        <versionNumber>1</versionNumber>
        <objectId>470EF13D-380D-34EE-01C2-8C56F3B835BB</objectId>
        <newCollectionRecord>false</newCollectionRecord>
        <autoIncrementSet>false</autoIncrementSet>
      </org.kuali.rice.kew.rule.PersonRuleResponsibility>
    </org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
    <org.kuali.rice.kns.util.TypedArrayList>
      <default>
        <listObjectType>org.kuali.rice.kew.rule.PersonRuleResponsibility</listObjectType>
      </default>
    </org.kuali.rice.kns.util.TypedArrayList>
  </personResponsibilities>
  <groupResponsibilities class="org.kuali.rice.kns.util.TypedArrayList" serialization="custom">
    <unserializable-parents/>
    <org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
      <default>
        <size>0</size>
      </default>
      <int>10</int>
    </org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
    <org.kuali.rice.kns.util.TypedArrayList>
      <default>
        <listObjectType>org.kuali.rice.kew.rule.GroupRuleResponsibility</listObjectType>
      </default>
    </org.kuali.rice.kns.util.TypedArrayList>
  </groupResponsibilities>
  <roleResponsibilities class="org.kuali.rice.kns.util.TypedArrayList" serialization="custom">
    <unserializable-parents/>
    <org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
      <default>
        <size>0</size>
      </default>
      <int>10</int>
    </org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
    <org.kuali.rice.kns.util.TypedArrayList>
      <default>
        <listObjectType>org.kuali.rice.kew.rule.RoleRuleResponsibility</listObjectType>
      </default>
    </org.kuali.rice.kns.util.TypedArrayList>
  </roleResponsibilities>
  <fieldValues>
    <entry>
      <string>892819:pos_typ</string>
      <string>SM</string>
    </entry>
    <entry>
      <string>892819:fin_coa_cd</string>
      <string>UA</string>
    </entry>
    <entry>
      <string>892819:org_cd</string>
      <string>HUMM</string>
    </entry>
    <entry>
      <string>892819:sal_plan</string>
      <string>Any</string>
    </entry>
  </fieldValues>
  <versionNumber>1</versionNumber>
  <objectId>C8FB7680-6A0E-37A5-78B2-377DF880477E</objectId>
  <newCollectionRecord>false</newCollectionRecord>
  <autoIncrementSet>false</autoIncrementSet>
</org.kuali.rice.kew.rule.RuleBaseValues><maintenanceAction>Edit</maintenanceAction>
</newMaintainableObject></maintainableDocumentContents>"""

xstream = new XmlObjectSerializerServiceImpl()

System.out.println("******* Original DOC XML ********");
System.out.println(oldXml);
System.out.println("*********************************\n");
MaintainableXMLConversionServiceImpl maintainableXMLConversionServiceImpl = new MaintainableXMLConversionServiceImpl();
String newXML = maintainableXMLConversionServiceImpl.transformMaintainableXML(oldXml);
System.out.println("******* UPGRADED DOC XML ********");
System.out.println(newXML);
System.out.println("*********************************\n");

try {
    maintXml = StringUtils.substringBetween(newXML, "<oldMaintainableObject>", "</oldMaintainableObject>")
    xstream.fromXml(maintXml)

    maintXml = StringUtils.substringBetween(newXML, "<newMaintainableObject>", "</newMaintainableObject>")
    xstream.fromXml(maintXml)
} catch ( ex ) {
    println "FAIL : ${newXML.split("[\\r\\n]")[0]} : ${ex.class} : $ex.message"
}