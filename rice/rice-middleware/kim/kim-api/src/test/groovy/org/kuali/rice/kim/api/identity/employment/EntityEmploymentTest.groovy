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
package org.kuali.rice.kim.api.identity.employment

import org.junit.Assert
import org.junit.Test
import org.kuali.rice.core.api.util.type.KualiDecimal
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationContract
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationType
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationTypeContract
import org.kuali.rice.kim.api.test.JAXBAssert
import org.kuali.rice.kim.api.identity.CodedAttribute
import org.kuali.rice.kim.api.identity.CodedAttributeContract

class EntityEmploymentTest {
    private static final String ID = "1";
	private static final String ENTITY_ID = "190192";

    private static final String TYPE_CODE = "PROF"
    private static final String TYPE_NAME = "Profesional"
    private static final String TYPE_SORT_CODE = "0"
    private static final String TYPE_ACTIVE = "true"
    private static final Long TYPE_VERSION_NUMBER = new Integer(1)
    private static final String TYPE_OBJECT_ID = UUID.randomUUID()

    private static final String STATUS_CODE = "AL"
    private static final String STATUS_NAME = "Alive?"
    private static final String STATUS_SORT_CODE = "0"
    private static final String STATUS_ACTIVE = "true"
    private static final Long STATUS_VERSION_NUMBER = new Integer(1)
    private static final String STATUS_OBJECT_ID = UUID.randomUUID()

    private static final String AFFILIATION_ID = "1";
	private static final String AFFILIATION_ENTITY_ID = "190192";
    private static final String AFFILIATION_TYPE_CODE = "Home"
    private static final String AFFILIATION_TYPE_NAME = "Home-y"
    private static final String AFFILIATION_TYPE_EMPLOYEE = "false";
    private static final String AFFILIATION_TYPE_SORT_CODE = "0"
    private static final String AFFILIATION_TYPE_ACTIVE = "true"
    private static final Long AFFILIATION_TYPE_VERSION_NUMBER = new Integer(1)
	private static final String AFFILIATION_TYPE_OBJECT_ID = UUID.randomUUID()
	private static final String AFFILIATION_CAMPUS_CODE = "IU"
    private static final String AFFILIATION_DEFAULT = "true"
    private static final String AFFILIATION_ACTIVE = "true"
    private static final Long AFFILIATION_VERSION_NUMBER = new Integer(1);
	private static final String AFFILIATION_OBJECT_ID = UUID.randomUUID();


    private static final String PRIMARY_DEPARTMENT_CODE = "DEPT";
    private static final String EMPLOYEE_ID = "555555554";
    private static final String EMPLOYMENT_RECORD_ID = "REC 221";
    private static final KualiDecimal BASE_SALARY_AMOUNT = new KualiDecimal(1000000.00);
    private static final String PRIMARY = "false"
    private static final String ACTIVE = "true"
    private static final Long VERSION_NUMBER = new Integer(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <entityEmployment xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <entityId>${ENTITY_ID}</entityId>
        <employeeId>${EMPLOYEE_ID}</employeeId>
        <employmentRecordId>${EMPLOYMENT_RECORD_ID}</employmentRecordId>
        <entityAffiliation>
            <id>${AFFILIATION_ID}</id>
            <entityId>${AFFILIATION_ENTITY_ID}</entityId>
            <affiliationType>
                <code>${AFFILIATION_TYPE_CODE}</code>
                <name>${AFFILIATION_TYPE_NAME}</name>
                <active>${AFFILIATION_TYPE_ACTIVE}</active>
                <sortCode>${AFFILIATION_TYPE_SORT_CODE}</sortCode>
                <employmentAffiliationType>${AFFILIATION_TYPE_EMPLOYEE}</employmentAffiliationType>
                <versionNumber>${AFFILIATION_TYPE_VERSION_NUMBER}</versionNumber>
                <objectId>${AFFILIATION_TYPE_OBJECT_ID}</objectId>
            </affiliationType>
            <campusCode>${AFFILIATION_CAMPUS_CODE}</campusCode>
            <defaultValue>${AFFILIATION_DEFAULT}</defaultValue>
            <active>${AFFILIATION_ACTIVE}</active>
            <versionNumber>${AFFILIATION_VERSION_NUMBER}</versionNumber>
            <objectId>${AFFILIATION_OBJECT_ID}</objectId>
        </entityAffiliation>
        <employeeStatus>
            <code>${STATUS_CODE}</code>
            <name>${STATUS_NAME}</name>
            <active>${STATUS_ACTIVE}</active>
            <sortCode>${STATUS_SORT_CODE}</sortCode>
            <versionNumber>${STATUS_VERSION_NUMBER}</versionNumber>
            <objectId>${STATUS_OBJECT_ID}</objectId>
        </employeeStatus>
        <employeeType>
            <code>${TYPE_CODE}</code>
            <name>${TYPE_NAME}</name>
            <active>${TYPE_ACTIVE}</active>
            <sortCode>${TYPE_SORT_CODE}</sortCode>
            <versionNumber>${TYPE_VERSION_NUMBER}</versionNumber>
            <objectId>${TYPE_OBJECT_ID}</objectId>
        </employeeType>
        <primaryDepartmentCode>${PRIMARY_DEPARTMENT_CODE}</primaryDepartmentCode>
        <baseSalaryAmount>${BASE_SALARY_AMOUNT}</baseSalaryAmount>
        <primary>${PRIMARY}</primary>
        <active>${ACTIVE}</active>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </entityEmployment>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_id_whitespace() {
        EntityEmployment.Builder builder = EntityEmployment.Builder.create();
        builder.setId(" ")
    }

    @Test
    void test_copy() {
        def o1 = EntityEmployment.Builder.create().build();
        def o2 = EntityEmployment.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityEmployment.Builder.create();
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityEmployment.class)
	}

    public static create() {
		return EntityEmployment.Builder.create(new EntityEmploymentContract() {
			def String id = EntityEmploymentTest.ID
            def String entityId = EntityEmploymentTest.ENTITY_ID
            def String employeeId = EntityEmploymentTest.EMPLOYEE_ID
            def String employmentRecordId = EntityEmploymentTest.EMPLOYMENT_RECORD_ID
            def EntityAffiliation getEntityAffiliation() { EntityAffiliation.Builder.create(new EntityAffiliationContract() {
                def String id = EntityEmploymentTest.AFFILIATION_ID
                def String entityId = EntityEmploymentTest.AFFILIATION_ENTITY_ID
                def EntityAffiliationType getAffiliationType() { EntityAffiliationType.Builder.create(new EntityAffiliationTypeContract() {
                    def String code = EntityEmploymentTest.AFFILIATION_TYPE_CODE
                    def String name = EntityEmploymentTest.AFFILIATION_TYPE_NAME
                    def boolean employmentAffiliationType = EntityEmploymentTest.AFFILIATION_TYPE_EMPLOYEE.toBoolean()
                    def boolean active = EntityEmploymentTest.AFFILIATION_TYPE_ACTIVE.toBoolean()
                    def String sortCode = EntityEmploymentTest.AFFILIATION_TYPE_SORT_CODE
                    def Long versionNumber = EntityEmploymentTest.AFFILIATION_TYPE_VERSION_NUMBER
                    def String objectId = EntityEmploymentTest.AFFILIATION_TYPE_OBJECT_ID
                }).build()}
                def String campusCode = EntityEmploymentTest.AFFILIATION_CAMPUS_CODE
                def boolean defaultValue = EntityEmploymentTest.AFFILIATION_DEFAULT.toBoolean()
                def boolean active = EntityEmploymentTest.AFFILIATION_ACTIVE.toBoolean()
                def Long versionNumber = EntityEmploymentTest.AFFILIATION_VERSION_NUMBER;
                def String objectId = EntityEmploymentTest.AFFILIATION_OBJECT_ID
            }).build()}
            def CodedAttribute getEmployeeStatus() { CodedAttribute.Builder.create(new CodedAttributeContract() {
				def String code = EntityEmploymentTest.STATUS_CODE
				def String name = EntityEmploymentTest.STATUS_NAME
				def boolean active = EntityEmploymentTest.STATUS_ACTIVE.toBoolean()
                def String sortCode = EntityEmploymentTest.STATUS_SORT_CODE
                def Long versionNumber = EntityEmploymentTest.STATUS_VERSION_NUMBER
				def String objectId = EntityEmploymentTest.STATUS_OBJECT_ID
			}).build()}
			def CodedAttribute getEmployeeType() { CodedAttribute.Builder.create(new CodedAttributeContract() {
				def String code = EntityEmploymentTest.TYPE_CODE
				def String name = EntityEmploymentTest.TYPE_NAME
				def boolean active = EntityEmploymentTest.TYPE_ACTIVE.toBoolean()
                def String sortCode = EntityEmploymentTest.TYPE_SORT_CODE
                def Long versionNumber = EntityEmploymentTest.TYPE_VERSION_NUMBER
				def String objectId = EntityEmploymentTest.TYPE_OBJECT_ID
			}).build()}
            def String primaryDepartmentCode = EntityEmploymentTest.PRIMARY_DEPARTMENT_CODE
            def KualiDecimal baseSalaryAmount = EntityEmploymentTest.BASE_SALARY_AMOUNT
            def boolean primary = EntityEmploymentTest.PRIMARY.toBoolean()
            def boolean active = EntityEmploymentTest.ACTIVE.toBoolean()
            def Long versionNumber = EntityEmploymentTest.VERSION_NUMBER;
			def String objectId = EntityEmploymentTest.OBJECT_ID
        }).build()

	}
    
}
