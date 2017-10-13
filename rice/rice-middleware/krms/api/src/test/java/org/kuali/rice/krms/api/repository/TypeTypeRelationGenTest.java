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
import org.kuali.rice.krms.api.repository.typerelation.RelationshipType;
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelation;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class TypeTypeRelationGenTest {

    private final static String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
            + "<typeTypeRelation>\n"
            + "    <fromTypeId>ID</fromTypeId>\n"
            + "    <toTypeId>ID</toTypeId>\n"
            + "    <relationshipType>UNKNOWN</relationshipType>\n"
            + "    <sequenceNumber>-1</sequenceNumber>\n"
            + "    <id>ID</id>\n"
            + "    <active>false</active>\n"
            + "</typeTypeRelation>";
    private final static boolean ACTIVE = false;
    private final static String FROM_TYPE_ID = "FROM_TYPE_ID";
    private final static String ID = "ID";
    private final static RelationshipType RELATIONSHIP_TYPE = RelationshipType.UNKNOWN;
    private final static Integer SEQUENCE_NUMBER = -1;
    private final static String TO_TYPE_ID = "TO_TYPE_ID";
    private final static Long VERSION_NUMBER = 0L;

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_TypeTypeRelation_Builder_create_fail_all_null() {
        TypeTypeRelation.Builder.create(null, null, null, null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_TypeTypeRelation_set_validation_fromTypeId_fail_null() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.setFromTypeId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_TypeTypeRelation_set_validation_fromTypeId_fail_empty() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.setFromTypeId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_TypeTypeRelation_set_validation_fromTypeId_fail_whitespace() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.setFromTypeId("    ");
    }

    @Test
    public void test_TypeTypeRelation_set_validation_id_success_null() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.setId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_TypeTypeRelation_set_validation_id_fail_empty() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.setId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_TypeTypeRelation_set_validation_id_fail_whitespace() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.setId("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_TypeTypeRelation_set_validation_relationshipType_fail_null() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.setRelationshipType(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_TypeTypeRelation_set_validation_sequenceNumber_fail_null() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.setSequenceNumber(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_TypeTypeRelation_set_validation_toTypeId_fail_null() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.setToTypeId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_TypeTypeRelation_set_validation_toTypeId_fail_empty() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.setToTypeId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_TypeTypeRelation_set_validation_toTypeId_fail_whitespace() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.setToTypeId("    ");
    }

    @Test
    public void test_TypeTypeRelation_Builder_create() {
        TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
    }

    @Test
    public void test_TypeTypeRelation_Builder_create_and_build() {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(FROM_TYPE_ID, RELATIONSHIP_TYPE, SEQUENCE_NUMBER, TO_TYPE_ID);
        builder.build();
    }

    @Test
    public void test_TypeTypeRelation_xml_marshaling()
        throws Exception
    {
        TypeTypeRelation typeTypeRelation = buildFullTypeTypeRelation();

        assertXmlMarshaling(typeTypeRelation, XML);
    }

    public void assertXmlMarshaling(Object typeTypeRelation, String expectedXml)
        throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(TypeTypeRelation.class);

        Marshaller marshaller = jc.createMarshaller();
        StringWriter stringWriter = new StringWriter();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new CustomNamespacePrefixMapper());
        marshaller.marshal(typeTypeRelation, stringWriter);
        String xml = stringWriter.toString();

//        System.out.println(xml); // run test, paste xml output into XML, comment out this line.

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object actual = unmarshaller.unmarshal(new StringReader(xml));
        Object expected = unmarshaller.unmarshal(new StringReader(expectedXml));
        Assert.assertEquals(expected, actual);
    }

    public static TypeTypeRelation buildFullTypeTypeRelation() {
        org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition fromType = KrmsTypeGenTest.buildFullKrmsType();
        org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition toType = KrmsTypeGenTest.buildFullKrmsType();
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(fromType.getId(), RELATIONSHIP_TYPE, SEQUENCE_NUMBER, toType.getId());
        builder.setId(ID);
        TypeTypeRelation typeTypeRelation = builder.build();
        return typeTypeRelation;
    }

    public static TypeTypeRelation buildFullFKTypeTypeRelation(KrmsTypeDefinition fromType, KrmsTypeDefinition toType) {
        TypeTypeRelation.Builder builder = TypeTypeRelation.Builder.create(fromType.getId(), RELATIONSHIP_TYPE, SEQUENCE_NUMBER, toType.getId());
        builder.setId(ID);
        TypeTypeRelation typeTypeRelation = builder.build();
        return typeTypeRelation;
    }

}
