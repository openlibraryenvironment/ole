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

package org.kuali.rice.coreservice.api.parameter;


import org.junit.Assert
import org.junit.Test
import org.kuali.rice.coreservice.test.JAXBAssert

public class ParameterTest {

    private static final String APPLICATION_ID = "BORG_HUNT";
	private static final String NAMESPACE_CODE = "TNG";
	private static final String COMPONENT_CODE = "C";
	private static final String NAME = "SHIELDS_ON";
    private static final String VALUE = "true";
	private static final String DESC = "turn the shields on";
    private static final org.kuali.rice.coreservice.api.parameter.EvaluationOperator EVALUATION_OP = org.kuali.rice.coreservice.api.parameter.EvaluationOperator.ALLOW;
	private static final String PARAMETER_TYPE_CODE = "PC"
    private static final String PARAMETER_TYPE_NAME = "Config"
    private static final String PARAMETER_TYPE_ACTIVE = "true"
	private static final Long PARAMETER_TYPE_VERSION_NUMBER = new Integer(1);
	private static final String PARAMETER_TYPE_OBJECT_ID = UUID.randomUUID();
    private static final Long VERSION_NUMBER = new Integer(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <parameter xmlns="http://rice.kuali.org/core/v2_0">
        <applicationId>${APPLICATION_ID}</applicationId>
        <namespaceCode>${NAMESPACE_CODE}</namespaceCode>
        <componentCode>${COMPONENT_CODE}</componentCode>
        <name>${NAME}</name>
        <value>${VALUE}</value>
        <description>${DESC}</description>
        <parameterType>
            <code>${PARAMETER_TYPE_CODE}</code>
            <name>${PARAMETER_TYPE_NAME}</name>
            <active>${PARAMETER_TYPE_ACTIVE}</active>
            <versionNumber>${PARAMETER_TYPE_VERSION_NUMBER}</versionNumber>
            <objectId>${PARAMETER_TYPE_OBJECT_ID}</objectId>
        </parameterType>
        <evaluationOperator>${EVALUATION_OP.code}</evaluationOperator>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </parameter>
    """

	@Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_all_null() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(null, null, null, null, null);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_null() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(null, NAMESPACE_CODE, COMPONENT_CODE, NAME, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_empty() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create("", NAMESPACE_CODE, COMPONENT_CODE, NAME, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_whitespace() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create("  ", NAMESPACE_CODE, COMPONENT_CODE, NAME, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_second_null() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, null, COMPONENT_CODE, NAME, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_second_empty() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, "", COMPONENT_CODE, NAME, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_second_whitespace() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, " ", COMPONENT_CODE, NAME, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_third_null() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, NAMESPACE_CODE, null, NAME, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_third_empty() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, NAMESPACE_CODE, "", NAME, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_third_whitespace() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, NAMESPACE_CODE, " ", NAME, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_fourth_null() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, NAMESPACE_CODE, COMPONENT_CODE, null, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_fourth_empty() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, NAMESPACE_CODE, COMPONENT_CODE, "", org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_fourth_whitespace() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, NAMESPACE_CODE, COMPONENT_CODE, " ", org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_fifth_whitespace() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, NAMESPACE_CODE, COMPONENT_CODE, NAME, null);
    }

    @Test
    void test_copy() {
        def o1 = org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, NAMESPACE_CODE, COMPONENT_CODE, NAME, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE)).build();
        def o2 = org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(APPLICATION_ID, NAMESPACE_CODE, COMPONENT_CODE, NAME, org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(PARAMETER_TYPE_CODE));
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, org.kuali.rice.coreservice.api.parameter.Parameter.class)
	}
    
    private create() {
		return org.kuali.rice.coreservice.api.parameter.Parameter.Builder.create(new org.kuali.rice.coreservice.api.parameter.ParameterContract() {
			def String name = ParameterTest.NAME
			def org.kuali.rice.coreservice.api.parameter.ParameterType getParameterType() { org.kuali.rice.coreservice.api.parameter.ParameterType.Builder.create(new org.kuali.rice.coreservice.api.parameter.ParameterTypeContract() {
				def String code = ParameterTest.PARAMETER_TYPE_CODE
				def String name = ParameterTest.PARAMETER_TYPE_NAME
				def boolean active = ParameterTest.PARAMETER_TYPE_ACTIVE.toBoolean()
                def Long versionNumber = ParameterTest.PARAMETER_TYPE_VERSION_NUMBER
				def String objectId = ParameterTest.PARAMETER_TYPE_OBJECT_ID
			}).build()
            }
            def String applicationId = ParameterTest.APPLICATION_ID
            def String namespaceCode = ParameterTest.NAMESPACE_CODE
            def String componentCode = ParameterTest.COMPONENT_CODE
            def String value = ParameterTest.VALUE
            def String description = ParameterTest.DESC
            def org.kuali.rice.coreservice.api.parameter.EvaluationOperator evaluationOperator = ParameterTest.EVALUATION_OP;
            def Long versionNumber = ParameterTest.VERSION_NUMBER;
			def String objectId = ParameterTest.OBJECT_ID
        }).build()
	}
}
