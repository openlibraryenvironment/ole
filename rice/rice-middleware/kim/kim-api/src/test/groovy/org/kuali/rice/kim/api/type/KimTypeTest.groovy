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

import javax.xml.bind.JAXBContext
import org.junit.Assert
import org.junit.Test

class KimTypeTest {

    private static final String ID = "the_id";
    private static final String SERVICE_NAME = "fooService";
    private static final String NAMESPACE_CODE = "FOO_SUBSYS";
    private static final String NAME = "foo name";
    private static final String ACTIVE = "true";
    private static final Long VERSION_NUMBER = 1L;
    private static final String OBJECT_ID = "sdfkljasd";

    private static final String XML = """
    <kimType xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <serviceName>${SERVICE_NAME}</serviceName>
        <namespaceCode>${NAMESPACE_CODE}</namespaceCode>
        <name>${NAME}</name>
        <active>${ACTIVE}</active>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </kimType>
    """

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_ver_num_null() {
        KimType.Builder.create().setVersionNumber(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    void test_Builder_fail_ver_num_less_than_1() {
        KimType.Builder.create().setVersionNumber(-1);
    }

    @Test
    void test_copy() {
        def o1b = KimType.Builder.create()
        o1b.id = "the_id"
        o1b.namespaceCode = "a"
        o1b.serviceName = "kt_id"

        def o1 = o1b.build()

        def o2 = KimType.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        KimType.Builder.create();
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
	  def jc = JAXBContext.newInstance(KimType.class)
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
        return KimType.Builder.create(new KimTypeContract() {
            String id = KimTypeTest.ID
            String serviceName = KimTypeTest.SERVICE_NAME
            String namespaceCode = KimTypeTest.NAMESPACE_CODE
            String name = KimTypeTest.NAME
            List<KimTypeAttributeContract> attributeDefinitions = Collections.emptyList()
            boolean active = KimTypeTest.ACTIVE
            Long versionNumber = KimTypeTest.VERSION_NUMBER
            String objectId = KimTypeTest.OBJECT_ID
        }).build()
    }
}
