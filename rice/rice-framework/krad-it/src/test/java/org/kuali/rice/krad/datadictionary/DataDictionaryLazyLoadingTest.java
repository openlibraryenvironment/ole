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

import org.junit.Test;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.test.KRADTestCase;

import static org.junit.Assert.assertEquals;

/**
 * DataDictionaryLazyLoadingTest ensures that data dictionary lazy loading works as expected
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class DataDictionaryLazyLoadingTest extends KRADTestCase {

	@Test
	public void testBusinessObjectDataDictionaryEntriesAreSame() {
		BusinessObjectEntry entry1 = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getBusinessObjectEntry(AdHocRoutePerson.class.getName());
		BusinessObjectEntry entry2 = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getBusinessObjectEntry(AdHocRoutePerson.class.getName());
		BusinessObjectEntry entry3 = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getBusinessObjectEntry(AdHocRoutePerson.class.getName());
		
		assertEquals(entry1, entry2);
		assertEquals(entry1, entry3);
		assertEquals(entry2, entry3);
	}

	@Test
	public void testDocumentDataDictionaryEntriesAreSame() {
		DocumentEntry entry1 = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry("CampusMaintenanceDocument");
		DocumentEntry entry2 = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry("CampusMaintenanceDocument");
		DocumentEntry entry3 = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry("CampusMaintenanceDocument");
		
		assertEquals(entry1, entry2);
		assertEquals(entry1, entry3);
		assertEquals(entry2, entry3);
	}

}
