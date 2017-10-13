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
package org.kuali.rice.location.api.state

import javax.xml.bind.JAXBContext
import org.junit.Assert
import org.junit.Test

class StateTest {

    private static final String CODE = "MI";
    private static final String NAME = "Michigan";
    private static final String COUNTRY_CODE = "US";
    private static final String ACTIVE = "true";
    private static final Long VERSION_NUMBER = new Long(1);

    private static final String XML = """
    <state xmlns="http://rice.kuali.org/location/v2_0">
        <code>${CODE}</code>
        <name>${NAME}</name>
        <countryCode>${COUNTRY_CODE}</countryCode>
        <active>${ACTIVE}</active>
        <versionNumber>1</versionNumber>
    </state>
    """

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_all_null() {
        State.Builder.create(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_first_null() {
        State.Builder.create(null, NAME, COUNTRY_CODE);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_first_empty() {
        State.Builder.create("", NAME, COUNTRY_CODE);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_first_whitespace() {
        State.Builder.create("  ", NAME, COUNTRY_CODE);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_second_null() {
        State.Builder.create(CODE, null, COUNTRY_CODE);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_second_empty() {
        State.Builder.create(CODE, "", COUNTRY_CODE);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_second_whitespace() {
        State.Builder.create(CODE, "  ", COUNTRY_CODE);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_third_null() {
        State.Builder.create(CODE, NAME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_third_empty() {
        State.Builder.create(CODE, NAME, "");
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_third_whitespace() {
        State.Builder.create(CODE, NAME, "  ");
    }

    @Test
    void test_create_only_required() {
        State.Builder.create(State.Builder.create(CODE, NAME, COUNTRY_CODE)).build();
    }

    @Test
    void happy_path() {
        State.Builder.create(CODE, NAME, COUNTRY_CODE).build();
    }

    @Test
    public void test_Xml_Marshal_Unmarshal() {
        def jc = JAXBContext.newInstance(State.class)
        def marshaller = jc.createMarshaller()
        def sw = new StringWriter()

        def param = this.create()
        marshaller.marshal(param, sw)

        def unmarshaller = jc.createUnmarshaller();
        def actual = unmarshaller.unmarshal(new StringReader(sw.toString()))
        def expected = unmarshaller.unmarshal(new StringReader(XML))

        Assert.assertEquals(expected, actual)
    }

    private create() {
        return State.Builder.create(new StateContract() {
            def String name = StateTest.NAME
            def String code = StateTest.CODE
            def String countryCode = StateTest.COUNTRY_CODE
            def boolean active = StateTest.ACTIVE.toBoolean()
            def Long versionNumber = StateTest.VERSION_NUMBER
        }).build()
    }
}
