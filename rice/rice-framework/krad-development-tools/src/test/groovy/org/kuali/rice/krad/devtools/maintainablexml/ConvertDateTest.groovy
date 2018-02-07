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

oldXml = """<maintainableDocumentContents maintainableImplClass="org.kuali.rice.krad.maintainablexml.OrgReviewRoleMaintainableImpl">
<oldMaintainableObject>
<org.kuali.rice.krad.devtools.maintainablexml.TestReviewRole>
  <organizationTypeCode>U</organizationTypeCode>
  <methodToCall>edit</methodToCall>
  <edit>true</edit>
  <copy>false</copy>
  <principalMemberPrincipalId>1377777792</principalMemberPrincipalId>
  <principalMemberPrincipalName>ekkhhhhg</principalMemberPrincipalName>
  <roleId>7</roleId>
  <namespaceCode>KFS-SYS</namespaceCode>
  <roleName>Organization Reviewer</roleName>
  <roleMemberId>KFS1378</roleMemberId>
  <oRMId/>
  <financialSystemDocumentTypeCode>KFS</financialSystemDocumentTypeCode>
  <reviewRolesIndicator>O</reviewRolesIndicator>
  <actionTypeCode>A</actionTypeCode>
  <priorityNumber>1</priorityNumber>
  <actionPolicyCode>F</actionPolicyCode>
  <forceAction>false</forceAction>
  <chartOfAccountsCode>S</chartOfAccountsCode>
  <organizationCode>MBIO</organizationCode>
  <overrideCode/>
  <active>true</active>
  <delegate>false</delegate>
  <activeFromDate reference="../memberPerson/activeFromDate"/>
  <newCollectionRecord>false</newCollectionRecord>
</org.kuali.rice.krad.devtools.maintainablexml.TestReviewRole>
<maintenanceAction>Edit</maintenanceAction>
</oldMaintainableObject>
<newMaintainableObject>
<org.kuali.rice.krad.devtools.maintainablexml.TestReviewRole>
  <organizationTypeCode>U</organizationTypeCode>
  <methodToCall>edit</methodToCall>
  <edit>false</edit>
  <copy>false</copy>
  <principalMemberPrincipalId>130777777792</principalMemberPrincipalId>
  <principalMemberPrincipalName>ekhhhhng</principalMemberPrincipalName>
  <roleId>7</roleId>
  <namespaceCode>KFS-SYS</namespaceCode>
  <roleName>Organization Reviewer</roleName>
  <roleMemberId>KFS1378</roleMemberId>
  <oRMId/>
  <financialSystemDocumentTypeCode>KFS</financialSystemDocumentTypeCode>
  <reviewRolesIndicator>O</reviewRolesIndicator>
  <actionTypeCode>A</actionTypeCode>
  <priorityNumber>1</priorityNumber>
  <actionPolicyCode>F</actionPolicyCode>
  <forceAction>false</forceAction>
  <chartOfAccountsCode>S</chartOfAccountsCode>
  <organizationCode>MBIO</organizationCode>
  <active>true</active>
  <delegate>false</delegate>
  <activeFromDate>2010-08-13</activeFromDate>
  <newCollectionRecord>false</newCollectionRecord>
</org.kuali.rice.krad.devtools.maintainablexml.TestReviewRole>
<maintenanceAction>Edit</maintenanceAction>
</newMaintainableObject>
</maintainableDocumentContents>"""

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