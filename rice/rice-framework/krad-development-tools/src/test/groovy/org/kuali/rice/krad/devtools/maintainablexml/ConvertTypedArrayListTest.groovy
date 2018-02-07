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


import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import org.apache.commons.lang.StringUtils
import org.kuali.rice.kim.impl.identity.PersonImpl
import org.kuali.rice.krad.service.impl.XmlObjectSerializerServiceImpl

import com.thoughtworks.xstream.converters.SingleValueConverter
import com.thoughtworks.xstream.converters.basic.DateConverter

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory

oldXml = """<maintainableDocumentContents maintainableImplClass="org.kuali.kfs.coa.document.AccountDelegateGlobalMaintainableImpl">
<oldMaintainableObject>
<org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobal>
  <accountGlobalDetails class="org.kuali.rice.kns.util.TypedArrayList" serialization="custom">
    <unserializable-parents/>
    <org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
      <default>
        <size>12</size>
      </default>
      <int>58</int>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
    </org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
    <org.kuali.rice.kns.util.TypedArrayList>
      <default>
        <listObjectType>org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail</listObjectType>
      </default>
    </org.kuali.rice.kns.util.TypedArrayList>
  </accountGlobalDetails>
  <delegateGlobals class="org.kuali.rice.kns.util.TypedArrayList" serialization="custom">
    <unserializable-parents/>
    <org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
      <default>
        <size>2</size>
      </default>
      <int>10</int>
      <org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobalDetail>
        <accountDelegatePrimaryRoutingIndicator>false</accountDelegatePrimaryRoutingIndicator>
        <accountDelegate class="org.kuali.rice.kim.impl.identity.PersonImpl">
          <firstName/>
          <middleName/>
          <lastName/>
          <name/>
          <addressLine1/>
          <addressLine2/>
          <addressLine3/>
          <addressCityName/>
          <addressStateCode/>
          <addressPostalCode/>
          <addressCountryCode/>
          <emailAddress/>
          <phoneNumber/>
          <suppressName>false</suppressName>
          <suppressAddress>false</suppressAddress>
          <suppressPhone>false</suppressPhone>
          <suppressPersonal>false</suppressPersonal>
          <suppressEmail>false</suppressEmail>
          <campusCode/>
          <employeeStatusCode/>
          <employeeTypeCode/>
          <primaryDepartmentCode/>
          <employeeId/>
          <baseSalaryAmount>
            <value>0.00</value>
          </baseSalaryAmount>
          <active>true</active>
        </accountDelegate>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobalDetail>
        <accountDelegatePrimaryRoutingIndicator>false</accountDelegatePrimaryRoutingIndicator>
        <newCollectionRecord>false</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobalDetail>
    </org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
    <org.kuali.rice.kns.util.TypedArrayList>
      <default>
        <listObjectType>org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobalDetail</listObjectType>
      </default>
    </org.kuali.rice.kns.util.TypedArrayList>
  </delegateGlobals>
  <newCollectionRecord>false</newCollectionRecord>
</org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobal>
<maintenanceAction>New</maintenanceAction>
</oldMaintainableObject>
<newMaintainableObject>
<org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobal>
  <documentNumber>30059160</documentNumber>
  <accountGlobalDetails class="org.kuali.rice.kns.util.TypedArrayList" serialization="custom">
    <unserializable-parents/>
    <org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
      <default>
        <size>39</size>
      </default>
      <int>58</int>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ55507427</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ55542552</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ55542588</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ55542591</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ55558806</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ55568545</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ55568NUD</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ55569240</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ55569640</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ55575030</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555ADMIN</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BH02S</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BH02T</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BH03S</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BH03T</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BH04S</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BH04T</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BH05S</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BH05T</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BH06S</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BH06T</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BH07T</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555BHFEE</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555CHLNG</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555DBS02</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555DBS03</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555DBS04</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555DBS05</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555FELLO</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555INDIR</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555MB294</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555MNSTO</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555NSFHS</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555OVCRS</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555PCARD</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555RETRT</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555RTRET</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555SYSTM</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
        <chartOfAccountsCode>3</chartOfAccountsCode>
        <accountNumber>ZZ555TBC06</accountNumber>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail>
    </org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
    <org.kuali.rice.kns.util.TypedArrayList>
      <default>
        <listObjectType>org.kuali.rice.krad.devtools.maintainablexml.TestGlobalDetail</listObjectType>
      </default>
    </org.kuali.rice.kns.util.TypedArrayList>
  </accountGlobalDetails>
  <delegateGlobals class="org.kuali.rice.kns.util.TypedArrayList" serialization="custom">
    <unserializable-parents/>
    <org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
      <default>
        <size>2</size>
      </default>
      <int>10</int>
      <org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobalDetail>
        <accountDelegateUniversalId>47863</accountDelegateUniversalId>
        <financialDocumentTypeCode>KFS</financialDocumentTypeCode>
        <accountDelegatePrimaryRoutingIndicator>true</accountDelegatePrimaryRoutingIndicator>
        <accountDelegateStartDate>2010-12-14</accountDelegateStartDate>
        <accountDelegate class="org.kuali.rice.kim.impl.identity.PersonImpl">
          <principalId>4733333333</principalId>
          <principalName>mayyy</principalName>
          <entityId>00168700000</entityId>
          <entityTypeCode>PERSON</entityTypeCode>
          <firstName>Marianne</firstName>
          <middleName/>
          <lastName>Hunter</lastName>
          <name>Hunter, Marianne</name>
          <addressLine1>302 Life Sciences</addressLine1>
          <addressLine2/>
          <addressLine3/>
          <addressCityName>Davis</addressCityName>
          <addressStateCode>CA</addressStateCode>
          <addressPostalCode>95616</addressPostalCode>
          <addressCountryCode/>
          <emailAddress>mayyy@yyy.edu</emailAddress>
          <phoneNumber>+1 530 555 5555</phoneNumber>
          <suppressName>false</suppressName>
          <suppressAddress>false</suppressAddress>
          <suppressPhone>false</suppressPhone>
          <suppressPersonal>false</suppressPersonal>
          <suppressEmail>false</suppressEmail>
          <campusCode>DV</campusCode>
          <externalIdentifiers/>
          <employeeStatusCode>A</employeeStatusCode>
          <employeeTypeCode>P</employeeTypeCode>
          <primaryDepartmentCode>061827</primaryDepartmentCode>
          <employeeId>7777777</employeeId>
          <baseSalaryAmount>
            <value>0.00</value>
          </baseSalaryAmount>
          <active>true</active>
        </accountDelegate>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobalDetail>
      <org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobalDetail>
        <accountDelegateUniversalId>41788</accountDelegateUniversalId>
        <financialDocumentTypeCode>KFS</financialDocumentTypeCode>
        <accountDelegatePrimaryRoutingIndicator>false</accountDelegatePrimaryRoutingIndicator>
        <accountDelegateStartDate>2010-12-14</accountDelegateStartDate>
        <accountDelegate class="org.kuali.rice.kim.impl.identity.PersonImpl">
          <principalId>41788</principalId>
          <principalName>sztsssls</principalName>
          <entityId>00020456</entityId>
          <entityTypeCode>PERSON</entityTypeCode>
          <firstName>Lori</firstName>
          <middleName/>
          <lastName>Dana</lastName>
          <name>Dana, Lori</name>
          <addressLine1>xxxe, Suite 300</addressLine1>
          <addressLine2/>
          <addressLine3/>
          <addressCityName>xx</addressCityName>
          <addressStateCode>CA</addressStateCode>
          <addressPostalCode>95618</addressPostalCode>
          <addressCountryCode/>
          <emailAddress>lxsssa@sss.edu</emailAddress>
          <phoneNumber>+1 530 555 5555</phoneNumber>
          <suppressName>false</suppressName>
          <suppressAddress>false</suppressAddress>
          <suppressPhone>false</suppressPhone>
          <suppressPersonal>false</suppressPersonal>
          <suppressEmail>false</suppressEmail>
          <campusCode>DV</campusCode>
          <externalIdentifiers/>
          <employeeStatusCode>A</employeeStatusCode>
          <employeeTypeCode>P</employeeTypeCode>
          <primaryDepartmentCode>061700</primaryDepartmentCode>
          <employeeId>537777619</employeeId>
          <baseSalaryAmount reference="../../../org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobalDetail/accountDelegate/baseSalaryAmount"/>
          <active>true</active>
        </accountDelegate>
        <documentNumber>30059160</documentNumber>
        <newCollectionRecord>true</newCollectionRecord>
      </org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobalDetail>
    </org.apache.ojb.broker.core.proxy.ListProxyDefaultImpl>
    <org.kuali.rice.kns.util.TypedArrayList>
      <default>
        <listObjectType>org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobalDetail</listObjectType>
      </default>
    </org.kuali.rice.kns.util.TypedArrayList>
  </delegateGlobals>
  <newCollectionRecord>false</newCollectionRecord>
</org.kuali.rice.krad.devtools.maintainablexml.TestDelegateGlobal>
<maintenanceAction>New</maintenanceAction>
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
    println "$docNum failed : ${docXml.split("[\\r\\n]")[0]} : ${ex.class} : $ex.message"
}