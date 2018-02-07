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
package org.kuali.rice.kim.api.common.attribute

import javax.xml.bind.JAXBContext
import org.junit.Assert
import org.junit.Test

class KimAttributeTest {

    private static final String ID = "the_id";
    private static final String COMPONENT_NAME = "org.kuali.Foo";
    private static final String ATTRIBUTE_NAME = "bar";
    private static final String NAMESPACE_CODE = "WENNIE_SUBSYS";
    private static final String ATTRIBUTE_LABEL = "the_label";
    private static final String ACTIVE = "true";
    private static final Long VERSION_NUMBER = 1L;
    private static final String OBJECT_ID = "sdfkljasd";

    private static final String XML = """
    <kimAttribute xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <componentName>${COMPONENT_NAME}</componentName>
        <attributeName>${ATTRIBUTE_NAME}</attributeName>
        <namespaceCode>${NAMESPACE_CODE}</namespaceCode>
        <attributeLabel>${ATTRIBUTE_LABEL}</attributeLabel>
        <active>${ACTIVE}</active>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </kimAttribute>
    """


    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_all_null() {
        KimAttribute.Builder.create(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_second_null() {
        KimAttribute.Builder.create(COMPONENT_NAME, null, NAMESPACE_CODE);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_second_empty() {
        KimAttribute.Builder.create(COMPONENT_NAME, "", NAMESPACE_CODE);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_second_whitespace() {
        KimAttribute.Builder.create(COMPONENT_NAME, " ", NAMESPACE_CODE);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_third_null() {
        KimAttribute.Builder.create(COMPONENT_NAME, ATTRIBUTE_NAME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_third_empty() {
        KimAttribute.Builder.create(COMPONENT_NAME, ATTRIBUTE_NAME, "");
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_third_whitespace() {
        KimAttribute.Builder.create(COMPONENT_NAME, ATTRIBUTE_NAME, " ");
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_ver_num_less_than_1() {
        KimAttribute.Builder.create(COMPONENT_NAME, ATTRIBUTE_NAME, NAMESPACE_CODE).setVersionNumber(-1);
    }

    @Test
    void test_copy() {
        def o1 = KimAttribute.Builder.create(COMPONENT_NAME, ATTRIBUTE_NAME, NAMESPACE_CODE).build();
        def o2 = KimAttribute.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        KimAttribute.Builder.create(COMPONENT_NAME, ATTRIBUTE_NAME, NAMESPACE_CODE);
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
	  def jc = JAXBContext.newInstance(KimAttribute.class)
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
        return KimAttribute.Builder.create(new KimAttributeContract() {
            String id = KimAttributeTest.ID
            String componentName = KimAttributeTest.COMPONENT_NAME
            String attributeName = KimAttributeTest.ATTRIBUTE_NAME
            String namespaceCode = KimAttributeTest.NAMESPACE_CODE
            String attributeLabel = KimAttributeTest.ATTRIBUTE_LABEL
            boolean active = KimAttributeTest.ACTIVE
            Long versionNumber = KimAttributeTest.VERSION_NUMBER
            String objectId = KimAttributeTest.OBJECT_ID
        }).build()
    }
}
