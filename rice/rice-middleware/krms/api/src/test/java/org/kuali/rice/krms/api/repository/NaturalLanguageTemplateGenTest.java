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
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class NaturalLanguageTemplateGenTest {

    private final static String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
            + "<ns2:naturalLanguageTemplate xmlns:ns2=\"http://rice.kuali.org/krms/v2_0\" xmlns=\"http://rice.kuali.org/core/v2_0\">\n"
            + "    <ns2:attributes/>\n"
            + "    <ns2:languageCode>NL</ns2:languageCode>\n"
            + "    <ns2:naturalLanguageUsageId>NATURAL_LANGUAGE_USAGE_ID</ns2:naturalLanguageUsageId>\n"
            + "    <ns2:typeId>TYPE_ID</ns2:typeId>\n"
            + "    <ns2:template>TEMPLATE</ns2:template>\n"
            + "    <ns2:id>ID</ns2:id>\n"
            + "    <ns2:active>false</ns2:active>\n"
            + "</ns2:naturalLanguageTemplate>";
    private final static boolean ACTIVE = false;
    private final static String ID = "ID";
    private final static String LANGUAGE_CODE = "NL";
    private final static String NATURAL_LANGUAGE_USAGE_ID = "NATURAL_LANGUAGE_USAGE_ID";
    private final static String TEMPLATE = "TEMPLATE";
    private final static String TYPE_ID = "TYPE_ID";
    private final static Long VERSION_NUMBER = 0L;

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_Builder_create_fail_all_null() {
        NaturalLanguageTemplate.Builder.create(null, null, null, null);
    }

    @Test
    public void test_NaturalLanguageTemplate_set_validation_id_success_null() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_id_fail_empty() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_id_fail_whitespace() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setId("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_language_fail_null() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setLanguageCode(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_language_fail_empty() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setLanguageCode("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_language_fail_whitespace() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setLanguageCode("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_naturalLanguageUsageId_fail_null() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setNaturalLanguageUsageId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_naturalLanguageUsageId_fail_empty() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setNaturalLanguageUsageId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_naturalLanguageUsageId_fail_whitespace() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setNaturalLanguageUsageId("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_template_fail_null() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setTemplate(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_template_fail_empty() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setTemplate("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_template_fail_whitespace() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setTemplate("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_typeId_fail_null() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setTypeId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_typeId_fail_empty() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setTypeId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplate_set_validation_typeId_fail_whitespace() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setTypeId("    ");
    }

    @Test
    public void test_NaturalLanguageTemplate_Builder_create() {
        NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
    }

    @Test
    public void test_NaturalLanguageTemplate_Builder_create_and_build() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE,
                NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.build();
    }

    @Test
    public void test_NaturalLanguageTemplate_xml_marshaling()
        throws Exception
    {
        NaturalLanguageTemplate naturalLanguageTemplate = buildFullNaturalLanguageTemplate();

        assertXmlMarshaling(naturalLanguageTemplate, XML);
    }

    public void assertXmlMarshaling(Object naturalLanguageTemplate, String expectedXml)
        throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(NaturalLanguageTemplate.class);

        Marshaller marshaller = jc.createMarshaller();
        StringWriter stringWriter = new StringWriter();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new CustomNamespacePrefixMapper());
        marshaller.marshal(naturalLanguageTemplate, stringWriter);
        String xml = stringWriter.toString();

//        System.out.println(xml); // run test, paste xml output into XML, comment out this line.

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object actual = unmarshaller.unmarshal(new StringReader(xml));
        Object expected = unmarshaller.unmarshal(new StringReader(expectedXml));
        Assert.assertEquals(expected, actual);
    }

    public static NaturalLanguageTemplate buildFullNaturalLanguageTemplate() {
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, NATURAL_LANGUAGE_USAGE_ID, TEMPLATE, TYPE_ID);
        builder.setId(ID);
        NaturalLanguageTemplate naturalLanguageTemplate = builder.build();
        return naturalLanguageTemplate;
    }

    public static NaturalLanguageTemplate buildFullNaturalLanguageTemplate(NaturalLanguageUsage depDef, KrmsTypeDefinition typeDef) { // TODO gen
        NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(LANGUAGE_CODE, depDef.getId(), TEMPLATE, typeDef.getId());
        builder.setId(ID);
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("TEMPLATE", "template");
        builder.setAttributes(attributes);
        NaturalLanguageTemplate naturalLanguageTemplate = builder.build();
        return naturalLanguageTemplate;
    }
}
