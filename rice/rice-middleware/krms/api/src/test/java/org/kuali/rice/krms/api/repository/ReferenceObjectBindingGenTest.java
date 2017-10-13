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
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class ReferenceObjectBindingGenTest {

    private final static String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
            + "<referenceObjectBinding xmlns=\"http://rice.kuali.org/krms/v2_0\">\n"
            + "    <krmsDiscriminatorType>KRMS_DISCRIMINATOR_TYPE</krmsDiscriminatorType>\n"
            + "    <krmsObjectId>KRMS_OBJECT_ID</krmsObjectId>\n"
            + "    <namespace>NAMESPACE</namespace>\n"
            + "    <referenceDiscriminatorType>REFERENCE_DISCRIMINATOR_TYPE</referenceDiscriminatorType>\n"
            + "    <referenceObjectId>REFERENCE_OBJECT_ID</referenceObjectId>\n"
            + "    <id>ID</id>\n"
            + "    <active>false</active>\n"
            + "</referenceObjectBinding>";
    private final static boolean ACTIVE = false;
    private final static String COLLECTION_NAME = "COLLECTION_NAME";
    private final static String ID = "ID";
    private final static String KRMS_DISCRIMINATOR_TYPE = "KRMS_DISCRIMINATOR_TYPE";
    private final static String KRMS_OBJECT_ID = "KRMS_OBJECT_ID";
    private final static String NAMESPACE = "NAMESPACE";
    private final static String REFERENCE_DISCRIMINATOR_TYPE = "REFERENCE_DISCRIMINATOR_TYPE";
    private final static String REFERENCE_OBJECT_ID = "REFERENCE_OBJECT_ID";
    private final static Long VERSION_NUMBER = 0L;

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_Builder_create_fail_all_null() {
        ReferenceObjectBinding.Builder.create(null, null, null, null, null);
    }

    @Test
    public void test_ReferenceObjectBinding_set_validation_id_success_null() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_id_fail_empty() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_id_fail_whitespace() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setId("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_krmsDiscriminatorType_fail_null() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setKrmsDiscriminatorType(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_krmsDiscriminatorType_fail_empty() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setKrmsDiscriminatorType("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_krmsDiscriminatorType_fail_whitespace() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setKrmsDiscriminatorType("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_krmsObjectId_fail_null() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setKrmsObjectId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_krmsObjectId_fail_empty() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setKrmsObjectId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_krmsObjectId_fail_whitespace() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setKrmsObjectId("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_namespace_fail_null() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setNamespace(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_namespace_fail_empty() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setNamespace("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_namespace_fail_whitespace() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setNamespace("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_referenceDiscriminatorType_fail_null() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setReferenceDiscriminatorType(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_referenceDiscriminatorType_fail_empty() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setReferenceDiscriminatorType("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_referenceDiscriminatorType_fail_whitespace() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setReferenceDiscriminatorType("    ");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_referenceObjectId_fail_null() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setReferenceObjectId(null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_referenceObjectId_fail_empty() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setReferenceObjectId("");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_ReferenceObjectBinding_set_validation_referenceObjectId_fail_whitespace() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setReferenceObjectId("    ");
    }

    @Test
    public void test_ReferenceObjectBinding_Builder_create() {
        ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
    }

    @Test
    public void test_ReferenceObjectBinding_Builder_create_and_build() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.build();
    }

    @Test
    public void test_ReferenceObjectBinding_xml_marshaling()
        throws Exception
    {
        ReferenceObjectBinding referenceObjectBinding = buildFullReferenceObjectBinding();

        assertXmlMarshaling(referenceObjectBinding, XML);
    }

    public void assertXmlMarshaling(Object referenceObjectBinding, String expectedXml)
        throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(ReferenceObjectBinding.class);

        Marshaller marshaller = jc.createMarshaller();
        StringWriter stringWriter = new StringWriter();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new CustomNamespacePrefixMapper());
        marshaller.marshal(referenceObjectBinding, stringWriter);
        String xml = stringWriter.toString();

//        System.out.println(xml); // run test, paste xml output into XML, comment out this line.

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object actual = unmarshaller.unmarshal(new StringReader(xml));
        Object expected = unmarshaller.unmarshal(new StringReader(expectedXml));
        Assert.assertEquals(expected, actual);
    }

    public static ReferenceObjectBinding buildFullReferenceObjectBinding() {
        ReferenceObjectBinding.Builder builder = ReferenceObjectBinding.Builder.create(KRMS_DISCRIMINATOR_TYPE, KRMS_OBJECT_ID, NAMESPACE, REFERENCE_DISCRIMINATOR_TYPE, REFERENCE_OBJECT_ID);
        builder.setId(ID);
        ReferenceObjectBinding referenceObjectBinding = builder.build();
        return referenceObjectBinding;
    }

}
