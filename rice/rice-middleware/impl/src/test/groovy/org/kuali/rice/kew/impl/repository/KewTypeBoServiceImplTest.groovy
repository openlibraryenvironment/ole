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
package org.kuali.rice.kew.impl.repository

import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.kuali.rice.kew.api.repository.type.KewAttributeDefinition
import org.kuali.rice.kew.api.repository.type.KewTypeAttribute
import org.kuali.rice.kew.api.repository.type.KewTypeDefinition
import org.kuali.rice.kew.api.repository.type.KewTypeRepositoryService
import org.kuali.rice.kew.impl.type.KewTypeAttributeBo
import org.kuali.rice.kew.impl.type.KewTypeBo
import org.kuali.rice.krad.bo.PersistableBusinessObject
import org.kuali.rice.krad.service.BusinessObjectService

import groovy.mock.interceptor.MockFor

class KewTypeBoServiceImplTest {

	private final shouldFail = new GroovyTestCase().&shouldFail

	static Map<String, KewTypeBo> sampleTypes = new HashMap<String, KewTypeBo>()
	static Map<String, KewTypeBo> sampleTypesKeyedByName = new HashMap<String, KewTypeBo>()
	def mockBusinessObjectService

	// create chart attribute Builder
	private static final String NAMESPACE = "KEW_TEST"
	private static final String TYPE_ID="KC_MAP123"
	private static final String NAME="KC_UNIT"
	private static final String SERVICE_NAME="kcUnitService"
		
	private static final String ATTR_ID_1="UNIT_NUM"
	private static final String UNIT_NUM_ATTR_DEF_ID = "1000"
	private static final Integer SEQUENCE_NUMBER_1 = new Integer(1)
	
	private static final String ATTR_ID_2="CAMPUS"
	private static final String CAMPUS_ATTR_DEF_ID = "1002"
	private static final Integer SEQUENCE_NUMBER_2 = new Integer(2)
	
    private static final String ATTR_ID_3="NewAttr"
    private static final String NEW_ATTR_DEF_ID = "1004"
    private static final Integer SEQUENCE_NUMBER_3 = new Integer(3)
    
    private static final String ORG_NAME = "ORG"
    private static final String ORG_LABEL = "Organization"
    private static final String COMPONENT = "someOrgComponent"
    
	// create sample KewType builder and build
	private static KewTypeAttribute.Builder unitNumAttrBuilder = KewTypeAttribute.Builder.create(ATTR_ID_1, TYPE_ID, UNIT_NUM_ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	private static KewTypeAttribute.Builder campusAttrBuilder = KewTypeAttribute.Builder.create(ATTR_ID_2, TYPE_ID, CAMPUS_ATTR_DEF_ID, SEQUENCE_NUMBER_2)  
    private static KewTypeAttribute newAttr = KewTypeAttribute.Builder.create(ATTR_ID_3, TYPE_ID, NEW_ATTR_DEF_ID, SEQUENCE_NUMBER_3).build()
                        
	private static List<KewTypeAttribute.Builder> attrs = Arrays.asList(unitNumAttrBuilder, campusAttrBuilder)
	private static KewTypeDefinition TEST_KEW_TYPE_DEF = KewTypeDefinition.Builder.create(TYPE_ID, NAME, NAMESPACE)
		.serviceName(SERVICE_NAME)
		.attributes(attrs)
		.build()
	private static KewTypeBo TEST_KEW_TYPE_BO = KewTypeBo.from(TEST_KEW_TYPE_DEF)
    private static KewTypeAttributeBo TEST_KEW_TYPE_ATTRIBUTE_BO = KewTypeAttributeBo.from(newAttr)
		
	@BeforeClass
	static void createSampleTypeBOs() {
		KewTypeBo defaultBo = new KewTypeBo(active: true, id: "1", name: "DEFAULT", namespace: "KEW_TEST", serviceName: "KewTypeBoServiceImpl")
		KewTypeBo studentBo = new KewTypeBo(active: true, id: "2", name: "Student", namespace: "KEW_TEST", serviceName: "KewTypeBoServiceImpl")
		KewTypeBo ifopalBo = new KewTypeBo(active: true, id: "3", name: "IFOPAL", namespace: "KC_TEST", serviceName: null)
		for (bo in [defaultBo, studentBo, ifopalBo]) {
			sampleTypes.put(bo.id, bo)
			sampleTypesKeyedByName.put(bo.name, bo)
		}
	}

	@Before
	void setupBoServiceMockContext() {
		mockBusinessObjectService = new MockFor(BusinessObjectService.class)
	}


	@Test
	public void test_getType() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {
			clazz, id -> sampleTypes.get("1")
		}

		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

		KewTypeRepositoryService service = new KewTypeBoServiceImpl()
		service.setBusinessObjectService(bos)
		KewTypeDefinition myType = service.getTypeById("1")

		Assert.assertEquals(KewTypeBo.to(sampleTypes.get("1")), myType)
		mockBusinessObjectService.verify(bos)
	}

	@Test
	public void testGetByIdWhenNoneFound() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {Class clazz, String id -> null}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

		KewTypeRepositoryService service = new KewTypeBoServiceImpl()
		service.setBusinessObjectService(bos)
		KewTypeDefinition myType = service.getTypeById("I_DONT_EXIST")

		Assert.assertNull(myType)
		mockBusinessObjectService.verify(bos)
	}

	@Test
	public void testGetByPrimaryIdEmptyTypeId() {
		shouldFail(IllegalArgumentException.class) {
			new KewTypeBoServiceImpl().getTypeById("")
		}
	}

	@Test
	public void testGetByPrimaryIdNullTypeId() {
		shouldFail(IllegalArgumentException.class) {
			new KewTypeBoServiceImpl().getTypeById(null)
		}
	}

	@Test
	public void testGetByNameAndNamespace_null_type_id() {
		KewTypeRepositoryService service = new KewTypeBoServiceImpl()
		shouldFail(IllegalArgumentException.class) {
			KewTypeDefinition myType = service.getTypeByNameAndNamespace(null,"KEW_TEST")
		}
	}

	@Test
	public void testGetByNameAndNamespace_null_namespace() {
		KewTypeRepositoryService service = new KewTypeBoServiceImpl()
		shouldFail(IllegalArgumentException.class) {
			KewTypeDefinition myType = service.getTypeByNameAndNamespace("Student",null)
		}
	}

	@Test
	public void testGetByNameAndNamespace() {
		mockBusinessObjectService.demand.findByPrimaryKey(1..1) {
			Class clazz, Map map -> sampleTypesKeyedByName.get("Student")
		}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

		KewTypeRepositoryService service = new KewTypeBoServiceImpl()
		service.setBusinessObjectService(bos)
		KewTypeDefinition myType = service.getTypeByNameAndNamespace("Student","KEW_TEST")

		Assert.assertEquals(KewTypeBo.to(sampleTypesKeyedByName.get("Student")), myType)
		mockBusinessObjectService.verify(bos)
	}

	@Test
	public void test_findAllTypesByNamespace_null_namespace() {
		KewTypeRepositoryService service = new KewTypeBoServiceImpl()
		shouldFail(IllegalArgumentException.class) {
			KewTypeDefinition myType = service.findAllTypesByNamespace(null)
		}
	}

  @Test
  public void test_findAllTypesByNamespace() {
     mockBusinessObjectService.demand.findMatching(1..1) {
      Class clazz, Map map -> [sampleTypes.get("1"), sampleTypes.get("2")]
    }
    BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
    KewTypeRepositoryService service = new KewTypeBoServiceImpl()
    service.setBusinessObjectService(bos)
    Collection<KewTypeDefinition> myTypes = service.findAllTypesByNamespace("KEW_TEST")

	Assert.assertEquals( myTypes.size(), new Integer(2))
	Assert.assertEquals(KewTypeBo.to(sampleTypes.get("1")), myTypes[0])
	Assert.assertEquals(KewTypeBo.to(sampleTypes.get("2")), myTypes[1])
    mockBusinessObjectService.verify(bos)
  }

  @Test
  public void test_findAllTypes() {
    mockBusinessObjectService.demand.findMatching(1..1) {
      Class clazz, Map map -> [sampleTypes.get("1"), sampleTypes.get("2"), sampleTypes.get("3")]
    }
    BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

    KewTypeRepositoryService service = new KewTypeBoServiceImpl()
    service.setBusinessObjectService(bos)
    Collection<KewTypeDefinition> myTypes = service.findAllTypes()
	Assert.assertEquals( myTypes.size(), new Integer(3))
    mockBusinessObjectService.verify(bos)
  }
  
  @Test
  public void test_createKewType_null_input() {
	  def boService = mockBusinessObjectService.proxyDelegateInstance()
	  KewTypeRepositoryService service = new KewTypeBoServiceImpl()
	  service.setBusinessObjectService(boService)
	  shouldFail(IllegalArgumentException.class) {
		  service.createKewType(null)
	  }
	  mockBusinessObjectService.verify(boService)
  }

  @Test
  void test_createKewType_exists() {
		mockBusinessObjectService.demand.findByPrimaryKey(1..1) {
			Class clazz, Map map -> TEST_KEW_TYPE_BO
		}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
		KewTypeRepositoryService service = new KewTypeBoServiceImpl()
		service.setBusinessObjectService(bos)
		shouldFail(IllegalStateException.class) {
			service.createKewType(TEST_KEW_TYPE_DEF)
		}
		mockBusinessObjectService.verify(bos)
  }

  @Test
  void test_createKewType_success() {
		mockBusinessObjectService.demand.findByPrimaryKey(1..1) {Class clazz, Map map -> null}
		mockBusinessObjectService.demand.save { PersistableBusinessObject bo -> }
		
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
		KewTypeRepositoryService service = new KewTypeBoServiceImpl()
		service.setBusinessObjectService(bos)
		
		service.createKewType(TEST_KEW_TYPE_DEF)
		mockBusinessObjectService.verify(bos)
  }

  @Test
  public void test_updateKewType_null_input() {
	  def boService = mockBusinessObjectService.proxyDelegateInstance()
	  KewTypeRepositoryService service = new KewTypeBoServiceImpl()
	  service.setBusinessObjectService(boService)
	  shouldFail(IllegalArgumentException.class) {
		  service.updateKewType(null)
	  }
	  mockBusinessObjectService.verify(boService)
  }

  @Test
  void test_updateKewType_does_not_exist() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {
			Class clazz, String id -> null
		}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
		KewTypeRepositoryService service = new KewTypeBoServiceImpl()
		service.setBusinessObjectService(bos)
		shouldFail(IllegalStateException.class) {
			service.updateKewType(TEST_KEW_TYPE_DEF)
		}
		mockBusinessObjectService.verify(bos)
  }

  @Test
  void test_updateKewType_success() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {Class clazz, String id -> TEST_KEW_TYPE_BO}
		mockBusinessObjectService.demand.save { PersistableBusinessObject bo -> }
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
		KewTypeRepositoryService service = new KewTypeBoServiceImpl()
		service.setBusinessObjectService(bos)
		service.updateKewType(TEST_KEW_TYPE_DEF)
		mockBusinessObjectService.verify(bos)
  }

  @Test
  public void test_createKewTypeAttribute_null_input() {
      def boService = mockBusinessObjectService.proxyDelegateInstance()
      KewTypeRepositoryService service = new KewTypeBoServiceImpl()
      service.setBusinessObjectService(boService)
      shouldFail(IllegalArgumentException.class) {
          service.createKewTypeAttribute(null)
      }
      mockBusinessObjectService.verify(boService)
  } 
  
  @Test
  void test_createKewTypeAttribute_exists() {
        mockBusinessObjectService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> TEST_KEW_TYPE_ATTRIBUTE_BO
        }
        BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
        KewTypeRepositoryService service = new KewTypeBoServiceImpl()
        service.setBusinessObjectService(bos)
        shouldFail(IllegalStateException.class) {
            service.createKewTypeAttribute(newAttr)
        }
        mockBusinessObjectService.verify(bos)
  }

  @Test
  void test_createKewTypeAttribute_success() {
        mockBusinessObjectService.demand.findByPrimaryKey(1..1) {Class clazz, Map map -> null}
        mockBusinessObjectService.demand.save { PersistableBusinessObject bo -> }
        
        BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
        KewTypeRepositoryService service = new KewTypeBoServiceImpl()
        service.setBusinessObjectService(bos)
        
        service.createKewTypeAttribute(newAttr)
        mockBusinessObjectService.verify(bos)
  }
  
  @Test
  public void test_updateKewTypeAttribute_null_input() {
      def boService = mockBusinessObjectService.proxyDelegateInstance()
      KewTypeRepositoryService service = new KewTypeBoServiceImpl()
      service.setBusinessObjectService(boService)
      shouldFail(IllegalArgumentException.class) {
          service.updateKewTypeAttribute(null)
      }
      mockBusinessObjectService.verify(boService)
  }
  
  @Test
  void test_updateKewTypeAttribute_does_not_exist() {
        mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {
            Class clazz, String id -> null
        }
        BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
        KewTypeRepositoryService service = new KewTypeBoServiceImpl()
        service.setBusinessObjectService(bos)
        shouldFail(IllegalStateException.class) {
            service.updateKewTypeAttribute(newAttr)
        }
        mockBusinessObjectService.verify(bos)
  }

  @Test
  void test_updateKewTypeAttribute_success() {
        mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {Class clazz, String id -> TEST_KEW_TYPE_ATTRIBUTE_BO}
        mockBusinessObjectService.demand.save { PersistableBusinessObject bo -> }
        BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
        KewTypeRepositoryService service = new KewTypeBoServiceImpl()
        service.setBusinessObjectService(bos)
        service.updateKewTypeAttribute(newAttr)
        mockBusinessObjectService.verify(bos)
  }
}

