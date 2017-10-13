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

class CampusTest {
    private static final String CODE = "AMES";
    private static final String NAME = "Iowa State University - Ames"
    private static final String SHORT_NAME = "ISU - Ames";
    private static final String CAMPUS_TYPE_CODE = "B";
	private static final String CAMPUS_TYPE_NAME = "BOTH"
    private static final String CAMPUS_TYPE_ACTIVE = "true";
    private static final Long CAMPUS_TYPE_VERSION_NUMBER = new Long(1);
	private static final String CAMPUS_TYPE_OBJECT_ID = UUID.randomUUID();
	private static final String ACTIVE = "true";
    private static final Long VERSION_NUMBER = new Long(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <campus xmlns="http://rice.kuali.org/location/v2_0">
		<code>${CODE}</code>
		<name>${NAME}</name>
		<shortName>${SHORT_NAME}</shortName>
		<campusType>
			<code>${CAMPUS_TYPE_CODE}</code>
			<name>${CAMPUS_TYPE_NAME}</name>
			<active>${CAMPUS_TYPE_ACTIVE}</active>
			<versionNumber>${CAMPUS_TYPE_VERSION_NUMBER}</versionNumber>
			<objectId>${CAMPUS_TYPE_OBJECT_ID}</objectId>
		</campusType>
		<active>${ACTIVE}</active>
		<versionNumber>${VERSION_NUMBER}</versionNumber>
		<objectId>${OBJECT_ID}</objectId>
	  </campus>
    """


    @Test
    void test_create_only_required() {
        Campus.Builder.create(Campus.Builder.create(CODE)).build()
    }

	@Test
	public void testCampusBuilderPassedInParams() {
	  //No assertions, just test whether the Builder gives us a Country object
	  Campus campus = Campus.Builder.create(CODE).build()
	}
	
	@Test
	public void testCampusBuilderPassedInCampusContract() {
	  this.createCampusFromPassedInContract()
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCampusBuilderEmptyCode() {
	  Campus.Builder.create("")
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCampusBuilderNullCode() {
	  Campus.Builder.create(null)
	}
	
	@Test
	public void testXmlMarshaling() {
	  JAXBContext jc = JAXBContext.newInstance(Campus.class)
	  Marshaller marshaller = jc.createMarshaller()
	  StringWriter sw = new StringWriter()
	  //marshaller.setProperty (Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE)
	  //marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8")
	  //m.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", mapper)
  
	  Campus campus = this.createCampusFromPassedInContract()
	  marshaller.marshal(campus,sw)
	  String xml = sw.toString()
  
	  Unmarshaller unmarshaller = jc.createUnmarshaller();
	  Object actual = unmarshaller.unmarshal(new StringReader(xml))
	  Object expected = unmarshaller.unmarshal(new StringReader(XML))
	  Assert.assertEquals(expected,actual)
	}
  
	@Test
	public void testXmlUnmarshal() {
	  JAXBContext jc = JAXBContext.newInstance(Campus.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller();
	  Campus campus = (Campus) unmarshaller.unmarshal(new StringReader(XML))
	  Assert.assertEquals(CODE,campus.code)
	  Assert.assertEquals(NAME,campus.name)
	  Assert.assertEquals(SHORT_NAME,campus.shortName)
	  Assert.assertEquals(new Boolean(ACTIVE).booleanValue(),campus.active)
	  Assert.assertEquals(CAMPUS_TYPE_CODE,campus.campusType.code)
	  Assert.assertEquals(CAMPUS_TYPE_NAME, campus.campusType.name)
	  Assert.assertEquals(CAMPUS_TYPE_ACTIVE.toBoolean(), campus.campusType.active)
      Assert.assertEquals(VERSION_NUMBER, campus.versionNumber)
	  Assert.assertEquals(OBJECT_ID, campus.objectId)
      Assert.assertEquals(CAMPUS_TYPE_VERSION_NUMBER, campus.campusType.versionNumber)
	  Assert.assertEquals(CAMPUS_TYPE_OBJECT_ID, campus.campusType.objectId)
	}
	
	private Campus createCampusFromPassedInContract() {
		Campus campus =  Campus.Builder.create(new CampusContract() {
			String getCode() {CampusTest.CODE}
			String getName() {CampusTest.NAME}
			String getShortName() {CampusTest.SHORT_NAME}
			CampusType getCampusType() { CampusType.Builder.create(new CampusTypeContract() {
				String getCode() {CampusTest.CAMPUS_TYPE_CODE}
				String getName() {CampusTest.CAMPUS_TYPE_NAME}
				boolean isActive() {CampusTest.CAMPUS_TYPE_ACTIVE.toBoolean()}
                Long getVersionNumber() { CampusTest.CAMPUS_TYPE_VERSION_NUMBER }
				String getObjectId() { CampusTest.CAMPUS_TYPE_OBJECT_ID }
			}).build()}
			boolean isActive() { CampusTest.ACTIVE.toBoolean() }
            Long getVersionNumber() { CampusTest.VERSION_NUMBER }
			String getObjectId() { CampusTest.OBJECT_ID }
		  }).build()

        return campus
	}
}
