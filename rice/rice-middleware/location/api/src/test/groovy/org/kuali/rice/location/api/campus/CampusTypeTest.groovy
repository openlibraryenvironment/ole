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
package org.kuali.rice.location.api.campus

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import junit.framework.Assert
import org.junit.Test

class CampusTypeTest {
    private static String CODE = "A";
    private static String NAME = "AWESOME";
    private static String ACTIVE = "true";
    private static Long VERSION_NUMBER = new Long(1);
	private static final String OBJECT_ID = UUID.randomUUID();
	def static final String XML = """
	  <campusType xmlns="http://rice.kuali.org/location/v2_0">
			<code>${CODE}</code>
			<name>${NAME}</name>
			<active>${ACTIVE}</active>
			<versionNumber>${VERSION_NUMBER}</versionNumber>
			<objectId>${OBJECT_ID}</objectId>
	  </campusType>
	  """

    @Test
    void test_create_only_required() {
        CampusType.Builder.create(CampusType.Builder.create(CODE)).build();
    }

	@Test
	public void testCampusTypeBuilderPassedInParams() {
	  //No assertions, just test whether the Builder gives us a Country object
	  CampusType campustype = CampusType.Builder.create(CODE).build()
	}
	
	@Test
	public void testCampusTypeBuilderPassedInCampusContract() {
	  this.createCampusTypeFromPassedInContract()
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCampusTypeBuilderEmptyCode() {
	  CampusType.Builder.create("")
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCampusTypeBuilderNullCode() {
	  CampusType.Builder.create(null)
	}
	
	@Test
	public void testXmlMarshaling() {
	  JAXBContext jc = JAXBContext.newInstance(CampusType.class)
	  Marshaller marshaller = jc.createMarshaller()
	  StringWriter sw = new StringWriter()
  
	  CampusType campusType = this.createCampusTypeFromPassedInContract()
	  marshaller.marshal(campusType,sw)
	  String marshalled_xml = sw.toString()

	  Unmarshaller unmarshaller = jc.createUnmarshaller();
	  Object actual = unmarshaller.unmarshal(new StringReader(marshalled_xml))
	  Object expected = unmarshaller.unmarshal(new StringReader(XML))
	  Assert.assertEquals(expected,actual)
	}
  
	@Test
	public void testXmlUnmarshal() {
	  String rawXml = XML
  
	  JAXBContext jc = JAXBContext.newInstance(Campus.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller();
	  CampusType campusType = (CampusType) unmarshaller.unmarshal(new StringReader(rawXml))
	  Assert.assertEquals(CODE,campusType.code)
	  Assert.assertEquals(NAME,campusType.name)
	  Assert.assertEquals(ACTIVE.toBoolean(),campusType.active)
      Assert.assertEquals(VERSION_NUMBER,campusType.versionNumber)
	  Assert.assertEquals(OBJECT_ID,campusType.objectId)
  
	}
	
	private CampusType createCampusTypeFromPassedInContract() {
		return CampusType.Builder.create(new CampusTypeContract() {
			String getCode() { CampusTypeTest.CODE }
			String getName() { CampusTypeTest.NAME }
			boolean isActive() { CampusTypeTest.ACTIVE.toBoolean() }
            Long getVersionNumber() { CampusTypeTest.VERSION_NUMBER }
			String getObjectId() { CampusTypeTest.OBJECT_ID }
		  }).build()
	}
}
