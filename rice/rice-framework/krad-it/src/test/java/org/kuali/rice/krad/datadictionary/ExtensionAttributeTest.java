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
package org.kuali.rice.krad.datadictionary;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.util.FieldUtils;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.bo.PersistableBusinessObjectExtension;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.keyvalues.PersistableBusinessObjectValuesFinder;
import org.kuali.rice.kns.lookup.LookupUtils;
import org.kuali.rice.krad.rules.rule.event.RouteDocumentEvent;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.test.document.bo.Account;
import org.kuali.rice.krad.test.document.bo.AccountExtension;
import org.kuali.rice.krad.test.document.bo.AccountType;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.test.KRADTestCase;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * ExtensionAttributeTest tests that {@link org.kuali.rice.krad.bo.PersistableBusinessObject#getExtension()} works as expected
 *
 * <p>When running this test, the working directory should be set to two levels down from the root of the project e.g.
 * it/krad</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ExtensionAttributeTest extends KRADTestCase {

	DataDictionary dd = null;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		dd = new DataDictionary();
		dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifControlDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifFieldDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifGroupDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifHeaderFooterDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifLayoutManagerDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifViewPageDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifWidgetDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifConfigurationDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifRiceDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifLookupDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifInquiryDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifMaintenanceDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifDocumentDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifElementDefinitions.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/uif/UifActionDefinitions.xml");
        dd.addConfigFileLocation("KR-NS",
                "classpath:org/kuali/rice/krad/datadictionary/DataDictionaryBaseTypes.xml");
        dd.addConfigFileLocation("KR-NS",
                "classpath:org/kuali/rice/krad/datadictionary/AttributeReference.xml");
		dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/kns/bo/datadictionary/DataDictionaryBaseTypes.xml");
		dd.addConfigFileLocation("KR-KIM","classpath:org/kuali/rice/kim/bo/datadictionary/EmploymentStatus.xml");
		dd.addConfigFileLocation("KR-KIM","classpath:org/kuali/rice/kim/bo/datadictionary/EmploymentType.xml");
        dd.addConfigFileLocation("KR-KIM","classpath:org/kuali/rice/kim/impl/identity/PersonImpl.xml");
        dd.addConfigFileLocation("KR-KIM","classpath:org/kuali/rice/kim/bo/datadictionary/KimBaseBeans.xml");
        dd.addConfigFileLocation("KR-KIM","classpath:org/kuali/rice/kim/impl/group/Group.xml");
        dd.addConfigFileLocation("KR-KIM","classpath:org/kuali/rice/kim/impl/role/RoleBo.xml");
        dd.addConfigFileLocation("KR-KIM","classpath:org/kuali/rice/kim/impl/type/KimType.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/location/web/campus/Campus.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/location/web/campus/CampusType.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/location/web/country/Country.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/location/web/state/State.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/location/web/county/County.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/location/web/postalcode/PostalCode.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/coreservice/web/parameter/Parameter.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/coreservice/web/parameter/ParameterType.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/coreservice/web/namespace/Namespace.xml");
        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/coreservice/web/component/Component.xml");

        dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/test/document");

        dd.parseDataDictionaryConfigurationFiles( false );
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		dd = null;
	}

	@Test
    /**
     * tests that the extension attribute type is present and has all the configured values
     */
	public void testExtensionAttributeType() throws Exception {
		BusinessObjectEntry boe = dd.getBusinessObjectEntry( "Account" );
		assertNotNull( "BusinessObjectEntry for TravelAccount should not be null", boe );
		AttributeDefinition extAttrib = boe.getAttributeDefinition( "extension.accountTypeCode" );
		assertNotNull( "AttributeDefinition for 'extension.accountType' should not be null", extAttrib );
		assertEquals(PersistableBusinessObjectValuesFinder.class.getName(), extAttrib.getControl().getValuesFinderClass());
		assertEquals(AccountType.class.getName(), extAttrib.getControl().getBusinessObjectClass());
		assertEquals("accountTypeCode", extAttrib.getControl().getKeyAttribute());
		assertEquals("name", extAttrib.getControl().getLabelAttribute());
		assertEquals(true, extAttrib.getControl().getIncludeKeyInLabel());
		extAttrib = boe.getAttributeDefinition( "extension.accountType.codeAndDescription" );
		assertNotNull( "AttributeDefinition for 'extension.accountType.codeAndDescription' should not be null", extAttrib );
	}

	@Test
    /**
     * tests that various properties of the business object extension are of the expected Java Class
     */
	public void testObjectUtils_getPropertyType() throws Exception {
		Account ta = new Account();
	assertEquals("physical property type mismatch", PersistableBusinessObjectExtension.class, PropertyUtils
		.getPropertyType(ta, "extension"));
	assertEquals("DD property type mismatch", AccountExtension.class, ObjectUtils.getPropertyType(ta, "extension",
		KRADServiceLocator.getPersistenceStructureService()));
	assertEquals("extension.accountType attribute class mismatch", AccountType.class, ObjectUtils.getPropertyType(
		ta, "extension.accountType", KRADServiceLocator.getPersistenceStructureService()));
	assertEquals("extension.accountType.codeAndDescription attribute class mismatch", String.class, ObjectUtils
		.getPropertyType(ta, "extension.accountType.codeAndDescription", KRADServiceLocator
			.getPersistenceStructureService()));
	}

	@Test
    /**
     * test that a business object relationship definitions have the expected values
     */
	public void testBOMetaDataService() throws Exception {
		Account ta = new Account();
	DataObjectRelationship br = KNSServiceLocator.getBusinessObjectMetaDataService().getBusinessObjectRelationship(
		ta, "extension.accountType");
		assertEquals( "mismatch on parent class", Account.class, br.getParentClass() );
		assertEquals( "mismatch on related class", AccountType.class, br.getRelatedClass() );
		System.out.println( br.getParentToChildReferences() );
	assertEquals("parent/child key not correct - should be extension.accountTypeCode/accountTypeCode",
		"accountTypeCode", br.getParentToChildReferences().get("extension.accountTypeCode"));
		br = KNSServiceLocator.getBusinessObjectMetaDataService().getBusinessObjectRelationship( ta, "extension" );
		assertNull( "extension is not lookupable, should have returned null", br );
	}

	@Test
    /**
     * tests that a quick finder, when set on an extension attribute, has the expected values
     */
	public void testQuickFinder() throws Exception {
		Account ta = new Account();
		ArrayList<String> lookupFieldAttributeList = new ArrayList<String>();
		lookupFieldAttributeList.add( "extension.accountTypeCode");

        Field field = FieldUtils.getPropertyField(ta.getClass(), "extension.accountTypeCode", true);

	field = LookupUtils.setFieldQuickfinder((BusinessObject) ta, "extension.accountTypeCode", field,
		lookupFieldAttributeList);

		assertEquals( "lookup class not correct", AccountType.class.getName(), field.getQuickFinderClassNameImpl() );
	assertEquals("field lookup params not correct", "extension.accountTypeCode:accountTypeCode", field
		.getLookupParameters());
	assertEquals("lookup field conversions not correct", "accountTypeCode:extension.accountTypeCode", field
		.getFieldConversions());
	}

    /**
     * tests validation on the extension attribute
     *
     * // TODO: test is currently failing because default existence checks are not implemented in KRAD
     * // see https://jira.kuali.org/browse/KULRICE-7666
     *
     * <p>The values given for attributes that are foreign keys should represent existing objects when auto-update is set to false</p>
     */
	public void testExistenceChecks() throws Exception {
		Account account = new Account();
		((AccountExtension)account.getExtension()).setAccountTypeCode( "XYZ" ); // invalid account type
		account.setName("Test Name");
		account.setNumber("1234567");
        GlobalVariables.setUserSession(new UserSession("quickstart"));
	MaintenanceDocument document = (MaintenanceDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument(
		"AccountMaintenanceDocument");
        assertNotNull( "new document must not be null", document );
        document.getDocumentHeader().setDocumentDescription( getClass().getSimpleName() + "test" );
        document.getOldMaintainableObject().setDataObject(null);
        document.getOldMaintainableObject().setDataObjectClass(account.getClass());
        document.getNewMaintainableObject().setDataObject(account);
        document.getNewMaintainableObject().setDataObjectClass(account.getClass());

        boolean failedAsExpected = false;
        try {
        	document.validateBusinessRules( new RouteDocumentEvent(document) );
        } catch ( ValidationException expected ) {
        	failedAsExpected = true;
        }
        assertTrue( "validation should have failed", failedAsExpected );
        System.out.println( "document errors: " + GlobalVariables.getMessageMap() );
        assertTrue( "there should be errors", GlobalVariables.getMessageMap().getErrorCount() > 0 );
	assertTrue("should be an error on the account type code", GlobalVariables.getMessageMap().doesPropertyHaveError(
		"document.newMaintainableObject.dataObject.extension.accountTypeCode"));
	assertTrue("account type code should have an existence error", GlobalVariables.getMessageMap().fieldHasMessage(
		"document.newMaintainableObject.dataObject.extension.accountTypeCode", "error.existence"));
	}
}
