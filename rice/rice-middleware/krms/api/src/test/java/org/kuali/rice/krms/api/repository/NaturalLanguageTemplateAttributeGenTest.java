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
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplateAttribute;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class NaturalLanguageTemplateAttributeGenTest {

    private final static String XML = "paste xml output here";
    private final static String ATTRIBUTE_DEFINITION_ID = "ATTRIBUTE_DEFINITION_ID";
    private final static String ID = "ID";
    private final static String NATURAL_LANGUAGE_TEMPLATE_ID = "NATURAL_LANGUAGE_TEMPLATE_ID";
    private final static String VALUE = "VALUE";

    @Test
    public void test_NaturalLanguageTemplateAttribute_set_validation_id_success_null() {
        NaturalLanguageTemplateAttribute.Builder builder = NaturalLanguageTemplateAttribute.Builder.create();
        builder.setId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplateAttribute_set_validation_id_fail_empty() {
        NaturalLanguageTemplateAttribute.Builder builder = NaturalLanguageTemplateAttribute.Builder.create();
        builder.setId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_NaturalLanguageTemplateAttribute_set_validation_id_fail_whitespace() {
        NaturalLanguageTemplateAttribute.Builder builder = NaturalLanguageTemplateAttribute.Builder.create();
        builder.setId("    ");
    }

    @Test
    public void test_NaturalLanguageTemplateAttribute_Builder_create() {
        NaturalLanguageTemplateAttribute.Builder.create();
    }

    @Test
    public void test_NaturalLanguageTemplateAttribute_Builder_create_and_build() {
        NaturalLanguageTemplateAttribute.Builder builder = NaturalLanguageTemplateAttribute.Builder.create();
        builder.build();
    }

//    @Test
    public void test_NaturalLanguageTemplateAttribute_xml_marshaling()
        throws Exception
    {
        NaturalLanguageTemplateAttribute naturalLanguageTemplateAttribute = buildFullNaturalLanguageTemplateAttribute();

        assertXmlMarshaling(naturalLanguageTemplateAttribute, XML);
    }

    public void assertXmlMarshaling(Object naturalLanguageTemplateAttribute, String expectedXml)
        throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(NaturalLanguageTemplateAttribute.class);

        Marshaller marshaller = jc.createMarshaller();
        StringWriter stringWriter = new StringWriter();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new CustomNamespacePrefixMapper());
        marshaller.marshal(naturalLanguageTemplateAttribute, stringWriter);
        String xml = stringWriter.toString();

        System.out.println(xml); // TODO run test, paste xml output into XML, comment out this line.

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object actual = unmarshaller.unmarshal(new StringReader(xml));
        Object expected = unmarshaller.unmarshal(new StringReader(expectedXml));
        Assert.assertEquals(expected, actual);
    }

    public static NaturalLanguageTemplateAttribute buildFullNaturalLanguageTemplateAttribute() {
        NaturalLanguageTemplateAttribute.Builder builder = NaturalLanguageTemplateAttribute.Builder.create();
        builder.setId(ID);
        NaturalLanguageTemplateAttribute naturalLanguageTemplateAttribute = builder.build();
        return naturalLanguageTemplateAttribute;
    }

}
