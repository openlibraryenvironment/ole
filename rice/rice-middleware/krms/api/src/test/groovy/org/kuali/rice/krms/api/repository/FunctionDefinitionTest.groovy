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

import static org.junit.Assert.*

import org.junit.Test
import org.kuali.rice.krms.api.repository.function.FunctionDefinition
import org.kuali.rice.krms.api.repository.function.FunctionDefinitionContract
import org.kuali.rice.krms.api.repository.function.FunctionParameterDefinition
import org.kuali.rice.krms.api.repository.function.FunctionParameterDefinitionContract
import org.kuali.rice.krms.api.test.JAXBAssert
import org.kuali.rice.krms.api.repository.category.CategoryDefinitionContract
import org.kuali.rice.krms.api.repository.category.CategoryDefinition

class FunctionDefinitionTest {

	private static final String ID = "1";
	private static final String NAMESPACE_CODE = "KRMS";
	private static final String NAME = "myFunction";
	private static final String DESCRIPTION = "This is my function, it's rad.";
	private static final String RETURN_TYPE = "boolean";
	private static final String TYPE_ID = "1";
	private static final boolean ACTIVE = true;
	private static final Long VERSION_NUMBER = 1;
	
	private static final String PARM1_ID = "2";
	private static final String PARM1_NAME = "parameter1";
	private static final String PARM1_DESCRIPTION = "parameter1";
	private static final String PARM1_TYPE = "string";
	private static final Long PARM1_VERSION_NUMBER = 1;
	private static final Integer PARM1_SEQUENCE_NUMBER = 1;
	
	private static final String PARM2_ID = "3";
	private static final String PARM2_NAME = "parameter2";
	private static final String PARM2_DESCRIPTION = "parameter2";
	private static final String PARM2_TYPE = "integer";
	private static final Long PARM2_VERSION_NUMBER = 1;
	private static final Integer PARM2_SEQUENCE_NUMBER = 2;
	
    private static final String CTGRY1_ID = "4";
    private static final String CTGRY1_NAME = "category1";
    private static final String CTGRY1_NAMESPACE = "namespace";
    private static final Long CTGRY1_VERSION_NUMBER = 1;

    private static final String CTGRY2_ID = "5";
    private static final String CTGRY2_NAME = "category2";
    private static final String CTGRY2_NAMESPACE = "namespace";
    private static final Long CTGRY2_VERSION_NUMBER = 1;

	private static final String XML = """
<function xmlns="http://rice.kuali.org/krms/v2_0">
	<id>1</id>
	<namespace>KRMS</namespace>
	<name>myFunction</name>
	<description>This is my function, it's rad.</description>
	<returnType>boolean</returnType>
	<typeId>1</typeId>
	<active>true</active>
	<versionNumber>1</versionNumber>
	<parameters>
		<parameter>
			<id>2</id>
			<name>parameter1</name>
			<description>parameter1</description>
			<type>string</type>
			<sequenceNumber>1</sequenceNumber>
			<versionNumber>1</versionNumber>
			<functionId>1</functionId>
		</parameter>
		<parameter>
			<id>3</id>
			<name>parameter2</name>
			<description>parameter2</description>
			<type>integer</type>
			<sequenceNumber>2</sequenceNumber>
			<versionNumber>1</versionNumber>
			<functionId>1</functionId>
		</parameter>
	</parameters>
	<categories>
	    <category>
	        <id>4</id>
	        <name>category1</name>
	        <namespace>namespace</namespace>
	        <versionNumber>1</versionNumber>
	    </category>
	    <category>
	        <id>5</id>
	        <name>category2</name>
	        <namespace>namespace</namespace>
	        <versionNumber>1</versionNumber>
	    </category>
	</categories>
</function>
""";

	def checkNamespaceCode = { namespace -> FunctionDefinition.Builder.create(namespace, NAME, RETURN_TYPE, TYPE_ID) } 
	
    @Test(expected=IllegalArgumentException.class)
    void testBuilderCreate_fail_null_namespaceCode() {
		checkNamespaceCode null;
    }
	
	@Test(expected=IllegalArgumentException.class)
	void testBuilderCreate_fail_empty_namespaceCode() {
		checkNamespaceCode "";
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testBuilderCreate_fail_whitespace_namespaceCode() {
		checkNamespaceCode " ";
	}
	
	def checkName = { name -> FunctionDefinition.Builder.create(NAMESPACE_CODE, name, RETURN_TYPE, TYPE_ID) }

	@Test(expected=IllegalArgumentException.class)
	void testBuilderCreate_fail_null_name() {
		checkName null;
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testBuilderCreate_fail_empty_name() {
		checkName "";
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testBuilderCreate_fail_whitespace_name() {
		checkName " ";
	}
	
	def checkReturnType = { returnType -> FunctionDefinition.Builder.create(NAMESPACE_CODE, NAME, returnType, TYPE_ID) }
	
	@Test(expected=IllegalArgumentException.class)
	void testBuilderCreate_fail_null_returnType() {
		checkReturnType null;
	}
		
	@Test(expected=IllegalArgumentException.class)
	void testBuilderCreate_fail_empty_returnType() {
		checkReturnType "";
	}
		
	@Test(expected=IllegalArgumentException.class)
	void testBuilderCreate_fail_whitespace_returnType() {
		checkReturnType " ";
	}
	
	def checkTypeId = { typeId -> FunctionDefinition.Builder.create(NAMESPACE_CODE, NAME, RETURN_TYPE, typeId) }
	
	@Test(expected=IllegalArgumentException.class)
	void testBuilderCreate_fail_null_typeId() {
		checkTypeId null;
	}
		
	@Test(expected=IllegalArgumentException.class)
	void testBuilderCreate_fail_empty_typeId() {
		checkTypeId "";
	}
		
	@Test(expected=IllegalArgumentException.class)
	void testBuilderCreate_fail_whitespace_typeId() {
		checkTypeId " ";
	}

	@Test
	void testBuilderCreate_minimal() {
		def builder = FunctionDefinition.Builder.create(NAMESPACE_CODE, NAME, RETURN_TYPE, TYPE_ID);
		def functionDef = builder.build();
		assert functionDef.namespace == NAMESPACE_CODE;
		assert functionDef.name == NAME;
		assert functionDef.returnType == RETURN_TYPE;
		assert functionDef.typeId == TYPE_ID;
		assertNull functionDef.id;
		assertNull functionDef.description;
		assertTrue functionDef.active;
		assertNull functionDef.versionNumber;
		assert functionDef.parameters.isEmpty();
	}
			
	@Test
	void testBuilder_fullCreate() {
		FunctionDefinition functionDef = create();
		assert functionDef.id == ID;
		assert functionDef.namespace == NAMESPACE_CODE;
		assert functionDef.name == NAME;
		assert functionDef.description == DESCRIPTION;
		assert functionDef.returnType == RETURN_TYPE;
		assert functionDef.typeId == TYPE_ID;
		assert functionDef.active == ACTIVE;
		assert functionDef.versionNumber == VERSION_NUMBER;
		assert functionDef.parameters.size() == 2;

		FunctionParameterDefinition parameter1 = functionDef.getParameters().get(0);
		assert parameter1.id == PARM1_ID;
		assert parameter1.name == PARM1_NAME;
		assert parameter1.description == PARM1_DESCRIPTION;
		assert parameter1.parameterType == PARM1_TYPE;
		assert parameter1.versionNumber == PARM1_VERSION_NUMBER;
		
		FunctionParameterDefinition parameter2 = functionDef.getParameters().get(1);
		assert parameter2.id == PARM2_ID;
		assert parameter2.name == PARM2_NAME;
		assert parameter2.description == PARM2_DESCRIPTION;
		assert parameter2.parameterType == PARM2_TYPE;
		assert parameter2.versionNumber == PARM2_VERSION_NUMBER;

        CategoryDefinition category1 = functionDef.getCategories().get(0);
        assert category1.id == CTGRY1_ID;
        assert category1.name == CTGRY1_NAME;
        assert category1.namespace == CTGRY1_NAMESPACE;
        assert category1.versionNumber == CTGRY1_VERSION_NUMBER;

        CategoryDefinition category2 = functionDef.getCategories().get(1);
        assert category2.id == CTGRY2_ID;
        assert category2.name == CTGRY2_NAME;
        assert category2.namespace == CTGRY2_NAMESPACE;
        assert category2.versionNumber == CTGRY2_VERSION_NUMBER;
	}

	@Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, FunctionDefinition.class)
	}
	
	/**
	 * Ensures that toString executes cleanly.
	 */
	@Test
	public void testToString() {
		def FunctionDefinition function = create();
		def toString = function.toString();
		assertNotNull toString;
		System.out.println(toString);
	}

    private FunctionDefinition.Builder createBuilder() {
		return FunctionDefinition.Builder.create(new FunctionDefinitionContract() {
				String id = FunctionDefinitionTest.ID;
				String namespace = FunctionDefinitionTest.NAMESPACE_CODE;
				String name = FunctionDefinitionTest.NAME;
                String description = FunctionDefinitionTest.DESCRIPTION;
				String returnType = FunctionDefinitionTest.RETURN_TYPE;
				String typeId = FunctionDefinitionTest.TYPE_ID;
                boolean active = FunctionDefinitionTest.ACTIVE;
                Long versionNumber = FunctionDefinitionTest.VERSION_NUMBER;
				List<FunctionParameterDefinition.Builder> parameters = [
					FunctionParameterDefinition.Builder.create(new FunctionParameterDefinitionContract() {
						String id = FunctionDefinitionTest.PARM1_ID;
						String name = FunctionDefinitionTest.PARM1_NAME;
						String description = FunctionDefinitionTest.PARM1_DESCRIPTION;
						String parameterType = FunctionDefinitionTest.PARM1_TYPE;
						Integer sequenceNumber = FunctionDefinitionTest.PARM1_SEQUENCE_NUMBER;
						Long versionNumber = FunctionDefinitionTest.PARM1_VERSION_NUMBER;
						String functionId = FunctionDefinitionTest.ID;
					}),
					FunctionParameterDefinition.Builder.create(new FunctionParameterDefinitionContract() {
						String id = FunctionDefinitionTest.PARM2_ID;
						String name = FunctionDefinitionTest.PARM2_NAME;
						String description = FunctionDefinitionTest.PARM2_DESCRIPTION;
						String parameterType = FunctionDefinitionTest.PARM2_TYPE;
						Integer sequenceNumber = FunctionDefinitionTest.PARM2_SEQUENCE_NUMBER;
						Long versionNumber = FunctionDefinitionTest.PARM2_VERSION_NUMBER;
						String functionId = FunctionDefinitionTest.ID;
					})
				]
                List<CategoryDefinition.Builder> categories = [
                    CategoryDefinition.Builder.create(new CategoryDefinitionContract() {
                        String id = FunctionDefinitionTest.CTGRY1_ID;
                        String name = FunctionDefinitionTest.CTGRY1_NAME;
                        String namespace = FunctionDefinitionTest.CTGRY1_NAMESPACE;
                        Long versionNumber = FunctionDefinitionTest.CTGRY1_VERSION_NUMBER;
                    }),
                    CategoryDefinition.Builder.create(new CategoryDefinitionContract() {
                        String id = FunctionDefinitionTest.CTGRY2_ID;
                        String name = FunctionDefinitionTest.CTGRY2_NAME;
                        String namespace = FunctionDefinitionTest.CTGRY2_NAMESPACE;
                        Long versionNumber = FunctionDefinitionTest.CTGRY2_VERSION_NUMBER;
                    })
                ]
			});
	}
	
	private FunctionDefinition create() {
		return createBuilder().build();
	}
}
