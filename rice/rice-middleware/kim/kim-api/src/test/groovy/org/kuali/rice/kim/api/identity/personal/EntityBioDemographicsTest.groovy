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
package org.kuali.rice.kim.api.identity.personal

import junit.framework.Assert
import org.junit.Test
import org.kuali.rice.kim.api.test.JAXBAssert
import java.text.SimpleDateFormat
import org.joda.time.DateTime
import org.joda.time.Years
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

class EntityBioDemographicsTest {
	private static final String ENTITY_ID = "190192";
    
    private static final String DECEASED_DATE_STRING = "2020-12-25";
    private static final String BIRTH_DATE_STRING = "1980-01-01";
    private static final Integer VALID_AGE = calculateAge(BIRTH_DATE_STRING, DECEASED_DATE_STRING);
    private static final String GENDER_CODE = "M";
    private static final String GENDER_CHANGE_CODE = "M";
    private static final String MARITAL_STATUS_CODE = "M";
    private static final String PRIMARY_LANGUAGE_CODE = "E";
    private static final String SECONDARY_LANGUAGE_CODE = "S";
    private static final String COUNTRY_OF_BIRTH_CODE = "USA";
    private static final String BIRTH_STATE_PROVINCE_CODE = "ST";
    private static final String BIRTH_CITY = "CITY";
    private static final String GEOGRAPHIC_ORIGIN = "Over there";
    private static final String NOTE_MESSAGE = "noteMessage";
    private static final String SUPPRESS_PERSONAL = "false";
    
    private static final Long VERSION_NUMBER = new Integer(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <entityBioDemographics xmlns="http://rice.kuali.org/kim/v2_0">
        <entityId>${ENTITY_ID}</entityId>
        <deceasedDate>${DECEASED_DATE_STRING}</deceasedDate>
        <birthDate>${BIRTH_DATE_STRING}</birthDate>
        <age>${VALID_AGE}</age>
        <genderCode>${GENDER_CODE}</genderCode>
        <genderChangeCode>${GENDER_CHANGE_CODE}</genderChangeCode>
        <maritalStatusCode>${MARITAL_STATUS_CODE}</maritalStatusCode>
        <primaryLanguageCode>${PRIMARY_LANGUAGE_CODE}</primaryLanguageCode>
        <secondaryLanguageCode>${SECONDARY_LANGUAGE_CODE}</secondaryLanguageCode>
        <birthCountry>${COUNTRY_OF_BIRTH_CODE}</birthCountry>
        <birthStateProvinceCode>${BIRTH_STATE_PROVINCE_CODE}</birthStateProvinceCode>
        <birthCity>${BIRTH_CITY}</birthCity>
        <geographicOrigin>${GEOGRAPHIC_ORIGIN}</geographicOrigin>
        <birthDateUnmasked>${BIRTH_DATE_STRING}</birthDateUnmasked>
        <genderCodeUnmasked>${GENDER_CODE}</genderCodeUnmasked>
        <genderChangeCodeUnmasked>${GENDER_CHANGE_CODE}</genderChangeCodeUnmasked>
        <maritalStatusCodeUnmasked>${MARITAL_STATUS_CODE}</maritalStatusCodeUnmasked>
        <primaryLanguageCodeUnmasked>${PRIMARY_LANGUAGE_CODE}</primaryLanguageCodeUnmasked>
        <secondaryLanguageCodeUnmasked>${SECONDARY_LANGUAGE_CODE}</secondaryLanguageCodeUnmasked>
        <birthCountryUnmasked>${COUNTRY_OF_BIRTH_CODE}</birthCountryUnmasked>
        <birthStateProvinceCodeUnmasked>${BIRTH_STATE_PROVINCE_CODE}</birthStateProvinceCodeUnmasked>
        <birthCityUnmasked>${BIRTH_CITY}</birthCityUnmasked>
        <geographicOriginUnmasked>${GEOGRAPHIC_ORIGIN}</geographicOriginUnmasked>
        <noteMessage>${NOTE_MESSAGE}</noteMessage>
        <suppressPersonal>${SUPPRESS_PERSONAL}</suppressPersonal>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </entityBioDemographics>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_entityId_whitespace() {
        EntityBioDemographics.Builder builder = EntityBioDemographics.Builder.create("", "M");
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_entityId_null() {
        EntityBioDemographics.Builder builder = EntityBioDemographics.Builder.create(null, "M");
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_gender_whitespace() {
        EntityBioDemographics.Builder builder = EntityBioDemographics.Builder.create("10101", "");
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_gender_null() {
        EntityBioDemographics.Builder builder = EntityBioDemographics.Builder.create("10101", null);
    }

    @Test
    void test_copy() {
        def o1 = EntityBioDemographics.Builder.create("10101", "M").build();
        def o2 = EntityBioDemographics.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityBioDemographics.Builder.create("10101", "M");
    }

    // for use with age calculation tests
    private static Integer calculateAge(birthDate, deceasedDate=null) {
        Date parsedBirthDate = new SimpleDateFormat(EntityBioDemographicsContract.BIRTH_DATE_FORMAT).parse(birthDate);
        def endDate
        if (deceasedDate == null) {
            endDate = new DateTime();
        } else {
            endDate = new DateTime(new SimpleDateFormat(EntityBioDemographicsContract.DECEASED_DATE_FORMAT).parse(deceasedDate));
        }
        return Years.yearsBetween(new DateTime(parsedBirthDate), endDate).getYears();
    }

    @Test
    void test_live_age_calculation() {
        EntityBioDemographics.Builder b = EntityBioDemographics.Builder.create("10101", "M");
        b.setBirthDate(BIRTH_DATE_STRING)
        Assert.assertEquals(calculateAge(BIRTH_DATE_STRING), b.build().age);
    }

    @Test
    void test_deceased_age_calculation() {
        EntityBioDemographics.Builder b = EntityBioDemographics.Builder.create("10101", "M");
        b.setBirthDate(BIRTH_DATE_STRING)
        b.setDeceasedDate(DECEASED_DATE_STRING);
        Assert.assertEquals(calculateAge(BIRTH_DATE_STRING, DECEASED_DATE_STRING), b.build().age);
    }

    /**
     * Tests that age in specific is getting marshalled as expected
     */
    @Test
	public void test_Xml_Marshal() {
        JAXBContext jaxbContext = JAXBContext.newInstance(EntityBioDemographics.class)
		Marshaller marshaller = jaxbContext.createMarshaller()
		StringWriter stringWriter = new StringWriter()
		marshaller.marshal(this.create(), stringWriter)
		def marshaledXml = stringWriter.toString()
        def expected = XML.replaceAll("[\\r\\n]*", "").replaceAll(">\\s*<", "><").trim()
        Assert.assertTrue(marshaledXml.contains(expected));
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
        // this does not compare XML directly, it only compares unmarshalled objects
        // since age is read-only, it does not survive unmarshalling
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityBioDemographics.class)
	}

    public static create() {
		return EntityBioDemographics.Builder.create(new EntityBioDemographicsContract() {
            def String entityId = EntityBioDemographicsTest.ENTITY_ID
            def String deceasedDate = EntityBioDemographicsTest.DECEASED_DATE_STRING
            def String birthDate = EntityBioDemographicsTest.BIRTH_DATE_STRING
            // age is a read-only property and is not preserved through builder construction
            // set a bogus value here so we are not confused as to whether this is actually under test
            def Integer age = -1 // satisfy interface
            def String genderCode = EntityBioDemographicsTest.GENDER_CODE
            def String genderChangeCode = EntityBioDemographicsTest.GENDER_CHANGE_CODE
            def String maritalStatusCode = EntityBioDemographicsTest.MARITAL_STATUS_CODE
            def String primaryLanguageCode = EntityBioDemographicsTest.PRIMARY_LANGUAGE_CODE
            def String secondaryLanguageCode = EntityBioDemographicsTest.SECONDARY_LANGUAGE_CODE
            def String birthCountry = EntityBioDemographicsTest.COUNTRY_OF_BIRTH_CODE
            def String birthCityCode = EntityBioDemographicsTest.BIRTH_CITY
            def String birthStateProvinceCode = EntityBioDemographicsTest.BIRTH_STATE_PROVINCE_CODE
            def String birthCity = EntityBioDemographicsTest.BIRTH_CITY
            def String geographicOrigin = EntityBioDemographicsTest.GEOGRAPHIC_ORIGIN
            def String birthDateUnmasked = EntityBioDemographicsTest.BIRTH_DATE_STRING
            def String genderCodeUnmasked = EntityBioDemographicsTest.GENDER_CODE
            def String maritalStatusCodeUnmasked = EntityBioDemographicsTest.MARITAL_STATUS_CODE
            def String primaryLanguageCodeUnmasked = EntityBioDemographicsTest.PRIMARY_LANGUAGE_CODE
            def String secondaryLanguageCodeUnmasked = EntityBioDemographicsTest.SECONDARY_LANGUAGE_CODE
            def String birthCountryUnmasked = EntityBioDemographicsTest.COUNTRY_OF_BIRTH_CODE
            def String birthCityCodeUnmasked = EntityBioDemographicsTest.BIRTH_CITY
            def String birthStateProvinceCodeUnmasked = EntityBioDemographicsTest.BIRTH_STATE_PROVINCE_CODE
            def String birthCityUnmasked = EntityBioDemographicsTest.BIRTH_CITY
            def String geographicOriginUnmasked = EntityBioDemographicsTest.GEOGRAPHIC_ORIGIN
            def String genderChangeCodeUnmasked = EntityBioDemographicsTest.GENDER_CHANGE_CODE
            def String noteMessage = EntityBioDemographicsTest.NOTE_MESSAGE
            def boolean suppressPersonal = EntityBioDemographicsTest.SUPPRESS_PERSONAL.toBoolean()
            def Long versionNumber = EntityBioDemographicsTest.VERSION_NUMBER;
			def String objectId = EntityBioDemographicsTest.OBJECT_ID
        }).build()

	}
    
}
