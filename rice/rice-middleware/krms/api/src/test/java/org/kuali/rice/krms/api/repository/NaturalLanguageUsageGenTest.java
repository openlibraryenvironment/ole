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
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class NaturalLanguageUsageGenTest {

    private final static String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
            + "<naturalLanguageUsage xmlns=\"http://rice.kuali.org/krms/v2_0\">\n"
            + "    <name>NAME</name>\n"
            + "    <namespace>NAMESPACE</namespace>\n"
            + "    <id>ID</id>\n"
            + "    <active>false</active>\n"
            + "</naturalLanguageUsage>";
    private final static boolean ACTIVE = false;
    private final static String DESCRIPTION = "DESCRIPTION";
    private final static String ID = "ID";
    private final static String NAME = "NAME";
    private final static String NAMESPACE = "NAMESPACE";
    private final static Long VERSION_NUMBER = 0L;

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageUsage_Builder_create_fail_all_null() {
        NaturalLanguageUsage.Builder.create(null, null);
    }

    @Test
    public void test_NaturalLanguageUsage_set_validation_id_success_null() {
        NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
        builder.setId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageUsage_set_validation_id_fail_empty() {
        NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
        builder.setId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageUsage_set_validation_id_fail_whitespace() {
        NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
        builder.setId("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageUsage_set_validation_name_fail_null() {
        NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
        builder.setName(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageUsage_set_validation_name_fail_empty() {
        NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
        builder.setName("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageUsage_set_validation_name_fail_whitespace() {
        NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
        builder.setName("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageUsage_set_validation_namespace_fail_null() {
        NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
        builder.setNamespace(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageUsage_set_validation_namespace_fail_empty() {
        NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
        builder.setNamespace("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageUsage_set_validation_namespace_fail_whitespace() {
        NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
        builder.setNamespace("    ");
    }

    @Test
    public void test_NaturalLanguageUsage_Builder_create() {
        NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
    }

    @Test
    public void test_NaturalLanguageUsage_Builder_create_and_build() {
        NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
        builder.build();
    }

    @Test
    public void test_NaturalLanguageUsage_xml_marshaling()
        throws Exception
    {
        NaturalLanguageUsage naturalLanguageUsage = buildFullNaturalLanguageUsage();

        assertXmlMarshaling(naturalLanguageUsage, XML);
    }

    public void assertXmlMarshaling(Object naturalLanguageUsage, String expectedXml)
        throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(NaturalLanguageUsage.class);

        Marshaller marshaller = jc.createMarshaller();
        StringWriter stringWriter = new StringWriter();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new CustomNamespacePrefixMapper());
        marshaller.marshal(naturalLanguageUsage, stringWriter);
        String xml = stringWriter.toString();

//        System.out.println(xml); // run test, paste xml output into XML, comment out this line.

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object actual = unmarshaller.unmarshal(new StringReader(xml));
        Object expected = unmarshaller.unmarshal(new StringReader(expectedXml));
        Assert.assertEquals(expected, actual);
    }

    public static NaturalLanguageUsage buildFullNaturalLanguageUsage() {
        NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(NAME, NAMESPACE);
        builder.setId(ID);
        NaturalLanguageUsage naturalLanguageUsage = builder.build();
        return naturalLanguageUsage;
    }

}
