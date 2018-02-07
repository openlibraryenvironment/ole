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
package org.kuali.rice.krms.api.repository;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class KrmsTypeGenTest {

    /**
     * NOTICE the create CONSTANTS below might not be in the right order, check the Contract.Builder.create.
     *
     */
    private final static String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
            + "<KRMSType xmlns=\"http://rice.kuali.org/krms/v2_0\">\n"
            + "    <id>ID</id>\n"
            + "    <name>NAME</name>\n"
            + "    <namespace>NAMESPACE</namespace>\n"
            + "    <serviceName></serviceName>\n"
            + "    <active>true</active>\n"
            + "</KRMSType>";
    private final static String NAME = "NAME";
    private final static String NAMESPACE = "NAMESPACE";
    private final static String SERVICE_NAME = "SERVICE_NAME";
    private final static String ID = "ID";
    private final static boolean ACTIVE = false;
    private final static Long VERSION_NUMBER = 0L;

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_KrmsTypeDefinition_Builder_create_fail_all_null() {
        KrmsTypeDefinition.Builder.create(null, null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_KrmsTypeDefinition_set_validation_name_fail_null() {
        KrmsTypeDefinition.Builder builder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
        builder.setName(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_KrmsTypeDefinition_set_validation_name_fail_empty() {
        KrmsTypeDefinition.Builder builder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
        builder.setName("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_KrmsTypeDefinition_set_validation_name_fail_whitespace() {
        KrmsTypeDefinition.Builder builder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
        builder.setName("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_KrmsTypeDefinition_set_validation_namespace_fail_null() {
        KrmsTypeDefinition.Builder builder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
        builder.setNamespace(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_KrmsTypeDefinition_set_validation_namespace_fail_empty() {
        KrmsTypeDefinition.Builder builder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
        builder.setNamespace("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_KrmsTypeDefinition_set_validation_namespace_fail_whitespace() {
        KrmsTypeDefinition.Builder builder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
        builder.setNamespace("    ");
    }

    @Test
    public void test_KrmsTypeDefinition_set_validation_id_success_null() {
        KrmsTypeDefinition.Builder builder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
        builder.setId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_KrmsTypeDefinition_set_validation_id_fail_empty() {
        KrmsTypeDefinition.Builder builder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
        builder.setId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_KrmsTypeDefinition_set_validation_id_fail_whitespace() {
        KrmsTypeDefinition.Builder builder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
        builder.setId("    ");
    }

    @Test
    public void test_KrmsTypeDefinition_Builder_create() {
        KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
    }

    @Test
    public void test_KrmsTypeDefinition_Builder_create_and_build() {
        KrmsTypeDefinition.Builder builder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
        builder.build();
    }

    @Test
    public void test_KrmsTypeDefinition_xml_marshaling()
            throws Exception
    {
        KrmsTypeDefinition krmsTypeDefinition = buildFullKrmsTypeDefinition();

        assertXmlMarshaling(krmsTypeDefinition, XML);
    }

    public void assertXmlMarshaling(Object krmsTypeDefinition, String expectedXml)
            throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(KrmsTypeDefinition.class);

        Marshaller marshaller = jc.createMarshaller();
        StringWriter stringWriter = new StringWriter();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new CustomNamespacePrefixMapper());
        marshaller.marshal(krmsTypeDefinition, stringWriter);
        String xml = stringWriter.toString();

//        System.out.println(xml); // run test, paste xml output into XML, comment out this line.

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object actual = unmarshaller.unmarshal(new StringReader(xml));
        Object expected = unmarshaller.unmarshal(new StringReader(expectedXml));
        Assert.assertEquals(expected, actual);
    }

    public static KrmsTypeDefinition buildFullKrmsType() {
        return buildFullKrmsTypeDefinition();
    }

    public static KrmsTypeDefinition buildFullKrmsTypeDefinition() {
        KrmsTypeDefinition.Builder builder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE);
        builder.setId(ID);
        KrmsTypeDefinition krmsTypeDefinition = builder.build();
        return krmsTypeDefinition;
    }

}
