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
package org.kuali.rice.kim.api.type

import javax.xml.namespace.QName
import org.junit.Test
import org.kuali.rice.kim.api.KimConstants
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * Tests the {@link KimTypeUtils} class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class KimTypeUtilsTest {

    @Test
	public void testResolveKimTypeServiceName() throws Exception {

		// first test by resolving the "null" or default kim type service

		QName defaultKimTypeServiceQName = QName.valueOf(KimConstants.DEFAULT_KIM_TYPE_SERVICE);
		assertEquals("default kim type service name should have no namespace", "", defaultKimTypeServiceQName.getNamespaceURI());

		assertEquals("When null is passed to resolveKimTypeService, should return default kim type service.", defaultKimTypeServiceQName, KimTypeUtils.resolveKimTypeServiceName(null));
		assertEquals("When empty string is passed to resolveKimTypeService, should return default kim type service.", defaultKimTypeServiceQName, KimTypeUtils.resolveKimTypeServiceName(""));
		assertEquals("When blank string is passed to resolveKimTypeService, should return default kim type service.", defaultKimTypeServiceQName, KimTypeUtils.resolveKimTypeServiceName(" "));

		// test resolving a custom kim type service name with no namespace

		String serviceName1 = "customKimTypeServiceName_noNamespace";

		QName serviceQName1 = KimTypeUtils.resolveKimTypeServiceName(serviceName1);
		assertNotNull("Service name was not successfully resolved", serviceQName1);
		assertEquals("Incorrect local part", serviceName1, serviceQName1.getLocalPart());
		assertEquals("Incorrect namespace uri, should have been empty string", "", serviceQName1.getNamespaceURI());

		// test resolving a custom kim type service name with a namespace

		String serviceNamespaceUri2 = "TestNamespace";
		String serviceLocalPart2 = "customKimTypeServiceName";
		String serviceName2 = "{" + serviceNamespaceUri2 + "}" + serviceLocalPart2;
		QName serviceQName2 = KimTypeUtils.resolveKimTypeServiceName(serviceName2);
		assertNotNull("Service name was not successfully resolved", serviceName2);
		assertEquals("Incorrect local part", serviceLocalPart2, serviceQName2.getLocalPart());
		assertEquals("Incorrect namespace uri", serviceNamespaceUri2, serviceQName2.getNamespaceURI());
		assertEquals("Incorrect full service name", serviceName2, serviceQName2.toString());
	}
}
