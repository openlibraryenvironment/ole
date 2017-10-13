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
package org.kuali.rice.krms.impl.repository

import java.util.ArrayList
import java.util.List

import junit.framework.Assert

import org.junit.Before
import org.junit.Test
import org.kuali.rice.krad.bo.PersistableBusinessObject
import org.kuali.rice.krad.service.BusinessObjectService
import org.kuali.rice.krms.api.repository.category.CategoryDefinition
import org.kuali.rice.krms.api.repository.category.CategoryDefinitionContract
import org.kuali.rice.krms.api.repository.function.FunctionDefinition
import org.kuali.rice.krms.api.repository.function.FunctionDefinitionContract
import org.kuali.rice.krms.api.repository.function.FunctionParameterDefinition
import org.kuali.rice.krms.api.repository.function.FunctionParameterDefinitionContract

import groovy.mock.interceptor.MockFor
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinitionContract

class FunctionBoServiceImplTest {
    private final shouldFail = new GroovyTestCase().&shouldFail
	
	// test services
	def mockBusinessObjectService
	BusinessObjectService boService;
	private FunctionBoServiceImpl functionService;
	
	// Simple Function data structures	
	private static final List <FunctionParameterDefinition.Builder> parmList1 = createFunctionParametersSet1()
	private static final List <FunctionParameterDefinition.Builder> parmList2 = createFunctionParametersSet2()	
	private static final List <CategoryDefinition.Builder> categoryList1 =createFunctionCategoriesSet1();
	
    private static final FunctionDefinition FUNCTION_DEF_001 = createFunctionDef001()
	private static final FunctionDefinition UPDATED_FUNCTION_DEF_001 = createUpdatedFunctionDef001()
	private static final FunctionDefinition FUNCTION_DEF_002 = createFunctionDef002()
	
	private static FunctionBo FUNCTION_BO_001 = FunctionBo.from(FUNCTION_DEF_001)
	private static FunctionBo UPDATED_FUNCTION_BO_001 = FunctionBo.from(UPDATED_FUNCTION_DEF_001)
	private static FunctionBo FUNCTION_BO_002 = FunctionBo.from(FUNCTION_DEF_002)
	
	static List<FunctionParameterBo> boList = new ArrayList<FunctionParameterBo>()
				
	@Before
	void setupBoServiceMockContext() {
		mockBusinessObjectService = new MockFor(BusinessObjectService.class)
		functionService = new FunctionBoServiceImpl()	
	}
	
	void injectBusinessObjectServiceIntoFunctionService() {
		boService = mockBusinessObjectService.proxyDelegateInstance()
		functionService.setBusinessObjectService(boService)
	}
//
// Function tests
//			
	@Test
	public void test_create_function_null_function() {
		injectBusinessObjectServiceIntoFunctionService()
		shouldFail(IllegalArgumentException.class) {
			functionService.createFunction(null)
		}
		mockBusinessObjectService.verify(boService)
	}

	@Test
	void test_create_function_exists() {
		  mockBusinessObjectService.demand.findByPrimaryKey(1..1) {
			  Class clazz, Map map -> FUNCTION_BO_001
		  }
		  injectBusinessObjectServiceIntoFunctionService()
		  shouldFail(IllegalStateException.class) {
			  functionService.createFunction(FUNCTION_DEF_001)
		  }
		  mockBusinessObjectService.verify(boService)
	} 
	
	@Test
	void test_create_function_successful() {
		  mockBusinessObjectService.demand.findByPrimaryKey(1..1) {Class clazz, Map map -> null}
		  mockBusinessObjectService.demand.save { PersistableBusinessObject bo -> }
		  
		  injectBusinessObjectServiceIntoFunctionService()
		  
		  FunctionDefinition fd = functionService.createFunction(FUNCTION_DEF_001)
		  Assert.assertEquals(FUNCTION_DEF_001, fd)
		  mockBusinessObjectService.verify(boService)
	}
	
	@Test
	public void test_update_function_null_function() {
		injectBusinessObjectServiceIntoFunctionService()
		shouldFail(IllegalArgumentException.class) {
			functionService.updateFunction(null)
		}
		mockBusinessObjectService.verify(boService)
	}
	
	@Test
	void test_update_function_does_not_exist() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) { clazz, map -> null }
		injectBusinessObjectServiceIntoFunctionService()

		shouldFail(IllegalStateException.class) {
			functionService.updateFunction(FUNCTION_DEF_001)
		}
		mockBusinessObjectService.verify(boService)
	}
	
	@Test
    void test_update_function_exists() {

		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..2) {	Class clazz, String id -> FUNCTION_BO_001	}
		mockBusinessObjectService.demand.save { UPDATED_FUNCTION_BO_001 -> UPDATED_FUNCTION_BO_001 }
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {	Class clazz, String id -> UPDATED_FUNCTION_BO_001	}
		injectBusinessObjectServiceIntoFunctionService()
		
		def initialFunction = functionService.getFunctionById ("001")
		Assert.assertEquals(initialFunction.getDescription(), FUNCTION_BO_001.getDescription())
		
		functionService.updateFunction(UPDATED_FUNCTION_DEF_001)
				
		def updatedFunction = functionService.getFunctionById ("001")
		Assert.assertEquals(updatedFunction.getDescription(), UPDATED_FUNCTION_BO_001.getDescription())
		mockBusinessObjectService.verify(boService)
    }
	
	@Test
	void test_get_function_by_id_null_id() {
		injectBusinessObjectServiceIntoFunctionService()

		shouldFail(IllegalArgumentException.class) {
			functionService.getFunctionById(null)
		}
		mockBusinessObjectService.verify(boService)
	}

    @Test
    void test_get_function_by_id_exists() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {	clazz, map -> FUNCTION_BO_001	}
		injectBusinessObjectServiceIntoFunctionService()
        Assert.assertEquals (FUNCTION_DEF_001, functionService.getFunctionById("002"))
        mockBusinessObjectService.verify(boService)
    }

    @Test
    void test_get_function_by_id_does_not_exist() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) { clazz, map -> null }
		injectBusinessObjectServiceIntoFunctionService()
        Assert.assertNull (functionService.getFunctionById("001"))
        mockBusinessObjectService.verify(boService)
    }

	@Test
	public void test_getActionByNameAndNamespace() {
		mockBusinessObjectService.demand.findByPrimaryKey(1..1) {	clazz, map -> FUNCTION_BO_001	}
        injectBusinessObjectServiceIntoFunctionService()

		FunctionDefinition fd = functionService.getFunctionByNameAndNamespace(FUNCTION_BO_001.getName(), FUNCTION_BO_001.getNamespace())

		Assert.assertEquals(FunctionBo.to(FUNCTION_BO_001), fd)
        mockBusinessObjectService.verify(boService)
	}

	@Test
	public void test_getActionByNameAndNamespace_when_none_found() {
		mockBusinessObjectService.demand.findByPrimaryKey(1..1) { clazz, map -> null }
        injectBusinessObjectServiceIntoFunctionService()

		FunctionDefinition fd = functionService.getFunctionByNameAndNamespace("I_DONT_EXIST", FUNCTION_BO_001.getNamespace())

		Assert.assertNull(fd)
        mockBusinessObjectService.verify(boService)
	}

	@Test
	public void test_getActionByNameAndNamespace_empty_name() {
		injectBusinessObjectServiceIntoFunctionService()
		shouldFail(IllegalArgumentException.class) {
			functionService.getFunctionByNameAndNamespace("", FUNCTION_BO_001.getNamespace())
		}
		mockBusinessObjectService.verify(boService)
	}

	@Test
	public void test_getActionByNameAndNamespace_null_name() {
		injectBusinessObjectServiceIntoFunctionService()
		shouldFail(IllegalArgumentException.class) {
			functionService.getFunctionByNameAndNamespace(null, FUNCTION_BO_001.getNamespace())
		}
		mockBusinessObjectService.verify(boService)
	}

	@Test
	public void test_getActionByNameAndNamespace_empty_namespace() {
		injectBusinessObjectServiceIntoFunctionService()
		shouldFail(IllegalArgumentException.class) {
			functionService.getFunctionByNameAndNamespace(FUNCTION_BO_001.getName(), "")
		}
		mockBusinessObjectService.verify(boService)
	}

	@Test
	public void test_getActionByNameAndNamespace_null_namespace() {
		injectBusinessObjectServiceIntoFunctionService()
		shouldFail(IllegalArgumentException.class) {
			functionService.getFunctionByNameAndNamespace(FUNCTION_BO_001.getName(), null)
		}
		mockBusinessObjectService.verify(boService)
	}

	@Test
	public void test_getFunction() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {	Class clazz, String id -> FUNCTION_BO_001	}
		injectBusinessObjectServiceIntoFunctionService()
		
		FunctionDefinition fd = functionService.getFunction(FUNCTION_BO_001.getId())
		
		Assert.assertEquals(fd, FUNCTION_DEF_001)
		mockBusinessObjectService.verify(boService)
	}
	
	@Test
	public void test_getFunctions() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {	Class clazz, String id -> FUNCTION_BO_001	}
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {	Class clazz, String id -> FUNCTION_BO_002	}	
		injectBusinessObjectServiceIntoFunctionService()
		
		List<String> functionIds = new ArrayList();
		functionIds.add(FUNCTION_BO_001.getId())
		functionIds.add(FUNCTION_BO_002.getId())
		
		List<FunctionDefinition> fds = functionService.getFunctions(functionIds)
		
		Assert.assertTrue(fds.size().equals(2));		
		Assert.assertEquals(fds.get(0).getId(), FUNCTION_BO_001.getId())
		Assert.assertEquals(fds.get(1).getId(), FUNCTION_BO_002.getId())
		
		mockBusinessObjectService.verify(boService)
	}
	
//
// private static methods for creating test data
//		
    
    private static createFunctionDef001() {
        return FunctionDefinition.Builder.create(new FunctionDefinitionContract () {
            def String id = "001"
			def String namespace = "namespace001"
			def String name = "Function001"
			def String description = "Function 001"
			def String returnType = "boolean"
			def String typeId = "S"
			def boolean active = true;
			def Long versionNumber = new Long(1)
			def List<? extends FunctionParameterDefinitionContract> parameters = FunctionBoServiceImplTest.parmList1
			def List<? extends CategoryDefinitionContract> categories = FunctionBoServiceImplTest.categoryList1
		}).build()
	}
	
	private static createUpdatedFunctionDef001() {
		return FunctionDefinition.Builder.create(new FunctionDefinitionContract () {
			def String id = "001"
			def String namespace = "namespace001"
			def String name = "Function001"
			def String description = "Updated Function 001"
			def String returnType = "boolean"
			def String typeId = "S"
			def boolean active = true;
			def Long versionNumber = new Long(1)
			def List<? extends FunctionParameterDefinitionContract> parameters = FunctionBoServiceImplTest.parmList1
			def List<? extends CategoryDefinitionContract> categories = FunctionBoServiceImplTest.categoryList1
		}).build()
	}
	
    private static createFunctionDef002() {
        return FunctionDefinition.Builder.create(new FunctionDefinitionContract () {
            def String id = "002"
			def String namespace = "namespace002"
			def String name = "Function002"
			def String description = "Function 002"
			def String returnType = "boolean"
			def String typeId = "S"
			def boolean active = true;
			def Long versionNumber = new Long(1)
			def List<? extends FunctionParameterDefinitionContract> parameters = FunctionBoServiceImplTest.parmList2
			def List<? extends CategoryDefinitionContract> categories = FunctionBoServiceImplTest.categoryList1
		}).build()
	}
			
	private static createFunctionParametersSet1(){
		List <FunctionParameterDefinition.Builder> functionParms = new ArrayList <FunctionParameterDefinition.Builder> ()
		FunctionParameterDefinition.Builder fpBuilder1 = FunctionParameterDefinition.Builder.create(new FunctionParameterDefinitionContract() {
			def String id = "1001"
			def String name = "functionParm1001"
			def String description = "function parameter 1001"
			def String functionId = "Function001"
			def String parameterType = "S"
			def Integer sequenceNumber = new Integer(0)
			def Long versionNumber = new Long(1)
		})
		FunctionParameterDefinition.Builder fpBuilder2 = FunctionParameterDefinition.Builder.create(new FunctionParameterDefinitionContract() {
			def String id = "1002"
			def String name = "functionParm1002"
			def String description = "function parameter 1002"
			def String functionId = "Function001"
			def String parameterType = "S"
			def Integer sequenceNumber = new Integer(1)
			def Long versionNumber = new Long(1)
		})
		FunctionParameterDefinition.Builder fpBuilder3 = FunctionParameterDefinition.Builder.create(new FunctionParameterDefinitionContract() {
			def String id = "1003"
			def String name = "functionParm1003"
			def String description = "function parameter 1003"
			def String functionId = "Function001"
			def String parameterType = "S"
			def Integer sequenceNumber = new Integer(2)
			def Long versionNumber = new Long(1)
		})
		for ( fpb in [fpBuilder1, fpBuilder2, fpBuilder3]){
			functionParms.add (fpb)
		}
		return functionParms;
	}
	
	private static createFunctionParametersSet2(){
		List <FunctionParameterDefinition.Builder> functionParms = new ArrayList <FunctionParameterDefinition.Builder> ()
		FunctionParameterDefinition.Builder fpBuilder1 = FunctionParameterDefinition.Builder.create(new FunctionParameterDefinitionContract() {
			def String id = "2001"
			def String name = "functionParm2001"
			def String description = "function parameter 2001"
			def String functionId = "Function002"
			def String parameterType = "S"
			def Integer sequenceNumber = new Integer(0)
			def Long versionNumber = new Long(1)
		})
		FunctionParameterDefinition.Builder fpBuilder2 = FunctionParameterDefinition.Builder.create(new FunctionParameterDefinitionContract() {
			def String id = "2002"
			def String name = "functionParm2002"
			def String description = "function parameter 2002"
			def String functionId = "Function002"
			def String parameterType = "S"
			def Integer sequenceNumber = new Integer(1)
			def Long versionNumber = new Long(1)
		})
		FunctionParameterDefinition.Builder fpBuilder3 = FunctionParameterDefinition.Builder.create(new FunctionParameterDefinitionContract() {
			def String id = "2003"
			def String name = "functionParm2003"
			def String description = "function parameter 2003"
			def String functionId = "Function002"
			def String parameterType = "S"
			def Integer sequenceNumber = new Integer(2)
			def Long versionNumber = new Long(1)
		})
		for ( fpb in [fpBuilder1, fpBuilder2, fpBuilder3]){
			functionParms.add (fpb)
		}
		return functionParms;
	}
		
	private static createFunctionCategoriesSet1(){
		List <CategoryDefinition.Builder> functionCategories = new ArrayList <CategoryDefinition.Builder> ()
		CategoryDefinition.Builder fcBuilder1 = CategoryDefinition.Builder.create(new CategoryDefinitionContract() {
			def String id = "category001"
			def String name = "category001"
			def String namespace = "namespace001"
			def Long versionNumber = new Long(1);
    		List<? extends TermSpecificationDefinitionContract> getTermSpecifications() {return new ArrayList<TermSpecificationDefinitionContract>()}
            List<? extends FunctionDefinitionContract> getFunctions() {return new ArrayList<FunctionDefinitionContract>()}
        })
		CategoryDefinition.Builder fcBuilder2 = CategoryDefinition.Builder.create(new CategoryDefinitionContract() {
			def String id = "category002"
			def String name = "category002"
			def String namespace = "namespace002"
			def Long versionNumber = new Long(1);
            List<? extends TermSpecificationDefinitionContract> getTermSpecifications() {return new ArrayList<TermSpecificationDefinitionContract>()}
            List<? extends FunctionDefinitionContract> getFunctions() {return new ArrayList<FunctionDefinitionContract>()}
		})
		CategoryDefinition.Builder fcBuilder3 = CategoryDefinition.Builder.create(new CategoryDefinitionContract() {
			def String id = "category003"
			def String name = "category003"
			def String namespace = "namespace003"
			def Long versionNumber = new Long(1);
            List<? extends TermSpecificationDefinitionContract> getTermSpecifications() {return new ArrayList<TermSpecificationDefinitionContract>()}
            List<? extends FunctionDefinitionContract> getFunctions() {return new ArrayList<FunctionDefinitionContract>()}
		})
		for ( fcb in [fcBuilder1, fcBuilder2, fcBuilder3]){
			functionCategories.add (fcb)
		}
		return functionCategories;
	}
}
