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
package org.kuali.rice.kim.service;

import org.junit.Test;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.framework.services.KimFrameworkServiceLocator;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.test.KIMTestCase;
import org.kuali.rice.kns.kim.type.DataDictionaryTypeServiceBase;

import javax.xml.namespace.QName;

import static org.junit.Assert.*;

/**
 * Tests the {@link org.kuali.rice.kim.impl.services.KimImplServiceLocator} class.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KIMServiceLocatorInternalTest extends KIMTestCase {

	@Test
	public void testGetKimTypeService_KimType() {
		
		// test by passing null
		try {
		    KimTypeService typeService1 = KimFrameworkServiceLocator.getKimTypeService((KimType) null);
            assertTrue("should have thrown exception and never got here", false);
        } catch (IllegalArgumentException e) {
        }
		
		// test by passing a KimType with a null service name
		
		KimType.Builder nullKimType = KimType.Builder.create();
		nullKimType.setServiceName(null);
		KimTypeService typeService2 = KimFrameworkServiceLocator.getKimTypeService(nullKimType.build());
		assertNotNull("type service shoudl have been found", typeService2);
		assertEquals("should be the default kim type", DataDictionaryTypeServiceBase.class, typeService2.getClass());
		
		// test by passing a KimType with an empty service name
		
		KimType.Builder emptyKimType = KimType.Builder.create();
		nullKimType.setServiceName("");
		KimTypeService typeService3 = KimFrameworkServiceLocator.getKimTypeService(emptyKimType.build());
		assertNotNull("type service should have been found", typeService3);
		assertEquals("should be the default kim type", DataDictionaryTypeServiceBase.class, typeService3.getClass());
		
		// test by passing a KimType that refers to the Permission TypeService
		
		KimType permissionKimType = KimApiServiceLocator.getKimTypeInfoService().findKimTypeByNameAndNamespace(
                KimConstants.NAMESPACE_CODE, "Permission");
		assertNotNull("The KR-IDM:Permission KimType should exist.", permissionKimType);
		
		KimTypeService typeService4 = KimFrameworkServiceLocator.getKimTypeService(permissionKimType);
		assertNotNull("type service should have been found", typeService4);
		
	}
	
	@Test
	public void testGetKimTypeService_QName() {
		
		// test by passing null
		
		try {
			KimFrameworkServiceLocator.getKimTypeService((QName) null);
			fail("getKimTypeService with a null QName should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {}
		
		// test by passing an invalid QName
		
		KimTypeService typeService1 = KimFrameworkServiceLocator.getKimTypeService(new QName("badNamespace", "badServiceName"));
		assertNull("A null KimTypeService should have been returned.", typeService1);
		
		// test by passing a QName for a valid service, but not one which is a KimTypeService, null should be returned
		
		// fetch the group service instead
		KimTypeService typeService2 = KimFrameworkServiceLocator.getKimTypeService(new QName(KimApiServiceLocator.KIM_GROUP_SERVICE));
		assertNull("A null KimTypeService should have been returned.", typeService2);

		// test by passing the QName for the Permission TypeService
		
		QName permissionServiceName = new QName("permissionPermissionTypeService");
		KimTypeService typeService3 = KimFrameworkServiceLocator.getKimTypeService(permissionServiceName);
		assertNotNull("permission type service should have been found", typeService3);
		
	}
	
}
