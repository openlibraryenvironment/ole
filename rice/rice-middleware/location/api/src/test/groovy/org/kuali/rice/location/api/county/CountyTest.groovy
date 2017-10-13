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



package org.kuali.rice.location.api.county

import javax.xml.bind.JAXBContext
import org.junit.Assert
import org.junit.Test

class CountyTest {
    private static final String CODE = "SHA";
    private static final String NAME = "SHA county"
    private static final String CITY_NAME = "Laingsburg"
    private static final String COUNTRY_CODE = "US";
    private static final String STATE_CODE = "MI";
	private static final String ACTIVE = "true";
    private static final Long VERSION_NUMBER = new Long(1);

    private static final String XML = """
    <county xmlns="http://rice.kuali.org/location/v2_0">
        <code>${CODE}</code>
        <name>${NAME}</name>
        <countryCode>${COUNTRY_CODE}</countryCode>
        <stateCode>${STATE_CODE}</stateCode>
        <active>${ACTIVE}</active>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
    </county>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_all_null() {
        County.Builder.create(null, null, null, null);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_null() {
        County.Builder.create(null, NAME, COUNTRY_CODE, STATE_CODE);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_empty() {
        County.Builder.create("", NAME, COUNTRY_CODE, STATE_CODE);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_whitespace() {
        County.Builder.create(" ", NAME, COUNTRY_CODE, STATE_CODE);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_second_null() {
        County.Builder.create(CODE, null, COUNTRY_CODE, STATE_CODE);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_second_empty() {
        County.Builder.create(CODE, "", COUNTRY_CODE, STATE_CODE);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_second_whitespace() {
        County.Builder.create(CODE, "  ", COUNTRY_CODE, STATE_CODE);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_third_null() {
        County.Builder.create(CODE, NAME, null, STATE_CODE);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_third_empty() {
        County.Builder.create(CODE, NAME, "", STATE_CODE);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_third_whitespace() {
        County.Builder.create(CODE, NAME, "  ", STATE_CODE);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_fourth_null() {
        County.Builder.create(CODE, NAME, COUNTRY_CODE, null);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_fourth_empty() {
        County.Builder.create(CODE, NAME, COUNTRY_CODE, "");
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_fourth_whitespace() {
        County.Builder.create(CODE, NAME, COUNTRY_CODE, "  ");
    }

    @Test
    void test_create_only_required() {
        County.Builder.create(County.Builder.create(CODE, NAME, COUNTRY_CODE, STATE_CODE)).build();
    }

    @Test
    void happy_path() {
        County.Builder.create(CODE, NAME, COUNTRY_CODE, STATE_CODE).build();
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
	  def jc = JAXBContext.newInstance(County.class)
	  def marshaller = jc.createMarshaller()
	  def sw = new StringWriter()

	  def param = this.create()
	  marshaller.marshal(param,sw)

	  def unmarshaller = jc.createUnmarshaller();
	  def actual = unmarshaller.unmarshal(new StringReader(sw.toString()))
	  def expected = unmarshaller.unmarshal(new StringReader(XML))

	  Assert.assertEquals(expected,actual)
	}

    private create() {
		return County.Builder.create(new CountyContract() {
			def String code = CountyTest.CODE
            def String name = CountyTest.NAME
            def String countryCode = CountyTest.COUNTRY_CODE
            def String stateCode = CountyTest.STATE_CODE
            def boolean active = CountyTest.ACTIVE.toBoolean()
            def Long versionNumber = CountyTest.VERSION_NUMBER
        }).build()
	}
}
