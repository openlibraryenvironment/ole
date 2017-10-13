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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.kim.document.IdentityManagementGroupDocument;
import org.kuali.rice.kim.document.IdentityManagementKimDocument;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteRecipient;
import org.kuali.rice.krad.bo.AdHocRouteWorkgroup;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.exception.ClassValidationException;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.document.DocumentBase;
import org.kuali.rice.krad.impls.RiceTestTransactionalDocumentDivergent;
import org.kuali.rice.krad.impls.RiceTestTransactionalDocumentDivergentParent;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentBase;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.impls.RiceTestTransactionalDocument2;
import org.kuali.rice.krad.impls.RiceTestTransactionalDocument2Parent;
import org.kuali.rice.krad.test.document.AccountRequestDocument;
import org.kuali.rice.krad.test.document.bo.AccountType2;
import org.kuali.rice.krad.test.document.bo.AccountType2Parent;
import org.kuali.rice.krad.test.document.bo.AccountTypeDivergent;
import org.kuali.rice.krad.test.document.bo.AccountTypeDivergentParent;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.*;

/**
 * BaseBOClassAndBaseDocumentClassTest tests for the data dictionary's ability to index entries by a "base" superclass
 *
 * <p>When running this test, the working directory should be set to two levels down from the root of the project e.g.
 * it/krad</p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BaseBOClassAndBaseDocumentClassTest extends KRADTestCase {

	DataDictionary dd = null;

	/**
	 * Performs setup tasks similar to those in ExtensionAttributeTest.
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();

		dd = new DataDictionary();

        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifControlDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifFieldDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifGroupDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifHeaderFooterDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifLayoutManagerDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifViewPageDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifWidgetDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifConfigurationDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifRiceDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifLookupDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifInquiryDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifMaintenanceDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifDocumentDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifElementDefinitions.xml");
        dd.addConfigFileLocation("KR-NS", "classpath:org/kuali/rice/krad/uif/UifActionDefinitions.xml");
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
		dd.addConfigFileLocation("KR-NS","classpath:org/kuali/rice/krad/test/document/");
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
        dd.parseDataDictionaryConfigurationFiles( false );
	}

	/**
	 * Performs tearDown tasks similar to those in ExtensionAttributeTest.
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		dd = null;
	}
	
	/**
	 * This method tests to make sure that business object entries and document entries can only define regular and "base" classes that are compatible.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testValidAndInvalidDDEntries() throws Exception {
		// Ensure that we can specify a base class that is not the superclass of the document/businessObject class.
		assertExpectedOutcomeOfBOEntryConstruction(AdHocRoutePerson.class, DocumentBase.class, true);
		assertExpectedOutcomeOfDocEntryConstruction(TransactionalDocumentBase.class, MaintenanceDocumentBase.class, true);
		assertExpectedOutcomeOfBOEntryConstruction(TransactionalDocumentBase.class, AdHocRouteRecipient.class, true);
		assertExpectedOutcomeOfDocEntryConstruction(AccountRequestDocument.class, IdentityManagementKimDocument.class, true);
		
		// Ensure that we can specify a base class that is the superclass of the document/businessObject class.
		assertExpectedOutcomeOfBOEntryConstruction(AdHocRoutePerson.class, AdHocRouteRecipient.class, true);
		assertExpectedOutcomeOfDocEntryConstruction(TransactionalDocumentBase.class, DocumentBase.class, true);
		assertExpectedOutcomeOfBOEntryConstruction(AdHocRouteWorkgroup.class, AdHocRouteRecipient.class, true);
		assertExpectedOutcomeOfDocEntryConstruction(IdentityManagementPersonDocument.class, IdentityManagementKimDocument.class, true);
		
		// Ensure that we can specify a document/businessObject class that is a superclass of the base class.
		assertExpectedOutcomeOfBOEntryConstruction(AdHocRouteRecipient.class, AdHocRoutePerson.class, true);
		assertExpectedOutcomeOfDocEntryConstruction(IdentityManagementKimDocument.class, IdentityManagementGroupDocument.class, true);
		
		// Ensure that we can specify the same class for both the document/businessObject class and the base class
		// (as permitted by the use of Class.isAssignableFrom in the BO and doc entry validations).
		assertExpectedOutcomeOfBOEntryConstruction(AdHocRoutePerson.class, AdHocRoutePerson.class, true);
		assertExpectedOutcomeOfDocEntryConstruction(MaintenanceDocumentBase.class, MaintenanceDocumentBase.class, true);
	}
	
	/**
	 * This method tests the DataDictionary's ability to grab entries based on a "base" class.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRetrieveDDEntriesByBaseClass() throws Exception {
		// Test the ability to retrieve a BusinessObjectEntry by "base" class, using both the full name and the simple name.
        Object[][] tests = {
            { "AccountType2Parent", "AccountType2",
               AccountType2.class, AccountType2Parent.class, "accountTypeCode2", "Account Type 2" },
            { "org.kuali.rice.krad.test.document.bo.AccountType2Parent", "org.kuali.rice.krad.test.document.bo.AccountType2",
               AccountType2.class, AccountType2Parent.class, "accountTypeCode2", "Account Type 2" },
            { "AccountTypeDivergentParent", "AccountTypeDivergent",
               AccountTypeDivergent.class, AccountTypeDivergentParent.class, "accountTypeCode Divergent", "Account Type Divergent" },
            { "org.kuali.rice.krad.test.document.bo.AccountTypeDivergentParent", "org.kuali.rice.krad.test.document.bo.AccountTypeDivergent",
               AccountTypeDivergent.class, AccountTypeDivergentParent.class, "accountTypeCode Divergent", "Account Type Divergent" }
        };
		for (Object[] test: tests) {
			// Attempt to retrieve a BusinessObjectEntry that is indexed under the "base" class.
			assertBusinessObjectEntry(dd.getBusinessObjectEntry((String) test[0]), (Class) test[2], (Class) test[3], (String) test[4], (String) test[5]);
			// Now check to ensure that the same BusinessObjectEntry can still be retrieved by specifying the actual BO class name.
            assertBusinessObjectEntry(dd.getBusinessObjectEntry((String) test[1]), (Class) test[2], (Class) test[3], (String) test[4], (String) test[5]);
		}
		
		// Test the ability to retrieve a DocumentEntry by "base" class, using both the full name and the simple name.
        tests = new Object[][] {
            { "RiceTestTransactionalDocument2Parent", "RiceTestTransactionalDocument2",
               RiceTestTransactionalDocument2.class, RiceTestTransactionalDocument2Parent.class },
            { "org.kuali.rice.krad.impls.RiceTestTransactionalDocument2Parent", "org.kuali.rice.krad.impls.RiceTestTransactionalDocument2",
               RiceTestTransactionalDocument2.class, RiceTestTransactionalDocument2Parent.class },
            { "RiceTestTransactionalDocumentDivergentParent", "RiceTestTransactionalDocumentDivergent",
               RiceTestTransactionalDocumentDivergent.class, RiceTestTransactionalDocumentDivergentParent.class },
            { "org.kuali.rice.krad.impls.RiceTestTransactionalDocumentDivergentParent", "org.kuali.rice.krad.impls.RiceTestTransactionalDocumentDivergent",
               RiceTestTransactionalDocumentDivergent.class, RiceTestTransactionalDocumentDivergentParent.class }
        };
        for (Object[] test: tests) {
			// Attempt to retrieve a DocumentEntry indexed under the "base" class
            assertDocumentEntry(dd.getDocumentEntry((String) test[0]), (Class) test[2], (Class) test[3]);
			// Now check to ensure that the same DocumentEntry can still be retrieved by specifying the actual document class name.
            assertDocumentEntry(dd.getDocumentEntry((String) test[1]), (Class) test[2], (Class) test[3]);
		}
		
		// Test the ability to retrieve a BusinessObjectEntry by JSTL key, where the "base" class's simple name is the JSTL key if it is defined. However,
		// the getDictionaryObjectEntry could still find the object even if the JSTL key map doesn't have it, since it checks the other maps for the
		// entry if it cannot be found in the entriesByJstlKey map.
		DataDictionaryEntry ddEntry = dd.getDictionaryObjectEntry("AccountType2");
		assertNotNull("The AccountType2 DD entry from the entriesByJstlKey map should not be null", ddEntry);
		assertTrue("The DD entry should have been a BusinessObjectEntry", ddEntry instanceof BusinessObjectEntry);
		assertBusinessObjectEntryIsAccountType2((BusinessObjectEntry) ddEntry);
	}
	
	/**
	 * A convenience method for testing the construction of a BusinessObjectEntry that has the given BO class and "base" class.
	 * 
	 * @param boClass The businessObjectClass of the entry.
	 * @param boBaseClass The "base" class of the entry.
	 * @param shouldSucceed Indicates whether the construction task should succeed or fail.
	 * @throws Exception
	 */
	private void assertExpectedOutcomeOfBOEntryConstruction(Class<? extends BusinessObject> boClass,
			Class<? extends BusinessObject> boBaseClass, boolean shouldSucceed) throws Exception {
		// Construct the entry and set the necessary properties.
		BusinessObjectEntry boEntry = new BusinessObjectEntry();
		boEntry.setBusinessObjectClass(boClass);
		boEntry.setBaseBusinessObjectClass(boBaseClass);
		boEntry.setObjectLabel(boClass.getSimpleName());
		// Now attempt to validate these properties.
		try {
			boEntry.completeValidation();
			// If the above operation succeeds, check whether or not the operation was meant to succeed.
			if (!shouldSucceed) {
				fail("The BO entry should have thrown a ClassValidationException during the validation process");
			}
		}
		catch (ClassValidationException e) {
			// If the above operation fails, check whether or not the operation was meant to fail.
			if (shouldSucceed) {
				fail("The BO entry should not have thrown a ClassValidationException during the validation process");
			}
		}
	}
	
	/**
	 * A convenience method for testing the construction of a DocumentEntry that has the given doc class and "base" class.
	 * 
	 * @param docClass The documentClass of the entry.
	 * @param docBaseClass The "base" class of the entry.
	 * @param shouldSucceed Indicates whether the construction task should succeed or fail.
	 * @throws Exception
	 */
	private void assertExpectedOutcomeOfDocEntryConstruction(Class<? extends Document> docClass,
			Class<? extends Document> docBaseClass, boolean shouldSucceed) throws Exception {
		// Construct the entry and set the necessary properties.
		DocumentEntry docEntry = new TransactionalDocumentEntry();
		docEntry.setDocumentClass(docClass);
		docEntry.setBaseDocumentClass(docBaseClass);
		// Now attempt to validate these properties.
		try {
			docEntry.completeValidation();
			// If the above operation succeeds, check whether or not the operation was meant to succeed.
			if (!shouldSucceed) {
				fail("The doc entry should have thrown a ClassValidationException during the validation process");
			}
		}
		catch (ClassValidationException e) {
			// If the above operation fails, check whether or not the operation was meant to fail.
			if (shouldSucceed) {
				fail("The doc entry should not have thrown a ClassValidationException during the validation process");
			}
		}
	}
	
	/**
	 * A convenience method for checking if a BusinessObjectEntry represents the AccountType2 DD entry.
	 * 
	 * @param boEntry The BusinessObjectEntry to test.
	 * @throws Exception
	 */
	private void assertBusinessObjectEntryIsAccountType2(BusinessObjectEntry boEntry) throws Exception {
        assertBusinessObjectEntry(boEntry, AccountType2.class, AccountType2Parent.class, "accountTypeCode2", "Account Type 2");
	}

    /**
     * A convenience method for checking if a BusinessObjectEntry represents the correct DD entry.
     *
     * @param boEntry The BusinessObjectEntry to test.
     * @throws Exception
     */
    private void assertBusinessObjectEntry(BusinessObjectEntry boEntry, Class boClass, Class boBaseClass, String title, String label) throws Exception {
        assertNotNull("The DD entry should not be null", boEntry);
        assertEquals("The DD entry does not represent the " + boClass.getName() + " entry", boClass, boEntry.getBusinessObjectClass());
        assertEquals("The DD entry does not have the expected base class", boBaseClass, boEntry.getBaseBusinessObjectClass());
        assertEquals("The DD entry does not have the expected title attribute", title, boEntry.getTitleAttribute());
        assertEquals("The DD entry does not have the expected object label", label, boEntry.getObjectLabel());
    }

    /**
     * A convenience method for checking if a DocumentEntry represents the correct DD entry.
     *
     * @param docEntry The DocumentEntry to test.
     * @throws Exception
     */
    private void assertDocumentEntry(DocumentEntry docEntry, Class docClass, Class baseDocClass) throws Exception {
        assertNotNull("The RiceTestTransactionalDocument2 DD entry should not be null", docEntry);
        assertEquals("The DD entry does not represent the RiceTestTransactionalDocument2 entry",
                docClass, docEntry.getDocumentClass());
        assertEquals("The DD entry does not have the expected base class", baseDocClass, docEntry.getBaseDocumentClass());
    }
}
