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

import groovy.mock.interceptor.MockFor
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass

import org.junit.Test
import org.kuali.rice.krad.bo.PersistableBusinessObject
import org.kuali.rice.krad.service.BusinessObjectService
import org.kuali.rice.krms.api.repository.action.ActionDefinition

class ActionBoServiceImplTest {

	private final shouldFail = new GroovyTestCase().&shouldFail

	static def NAMESPACE = "KRMS_TEST"
	static def TYPE_ID="1234ABCD"		
	
	static def ATTR_ID_1 = "ACTION_ATTR_001"
	static def ATTR_DEF_ID = "1002"
	static def ACTION_TYPE = "Notification"
	static def ATTR_VALUE = "Spam"
	static def SEQUENCE_1 = new Integer(1)
	
	static def RULE_ID_1 = "RULEID001"	
	static def ACTION_ID_1 = "ACTIONID01"
	static def ACTION_NAME_1 = "Say Hello"
	static def ACTION_DESCRIPTION_1 = "Send spam email"
	
	// test samples
	static def ActionDefinition TEST_ACTION_DEF
	static def ActionBo TEST_ACTION_BO
	private static KrmsAttributeDefinitionBo ADB1
	
	// test services
	def mockBusinessObjectService
	
	@BeforeClass
	static void createSamples() {
		//  create krmsAttributeDefinitionBo
		ADB1 = new KrmsAttributeDefinitionBo();
		ADB1.id = ATTR_DEF_ID
		ADB1.name = ACTION_TYPE
		ADB1.namespace = NAMESPACE
		
		// create ActionDefinition		
		Map<String,String> myAttrs = new HashMap<String,String>()
		myAttrs.put(ACTION_TYPE, ATTR_VALUE)
		ActionDefinition.Builder builder = ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
		builder.setDescription ACTION_DESCRIPTION_1
		builder.setAttributes myAttrs
		builder.build()
		TEST_ACTION_DEF = builder.build()

		// Create ActionAttributeBo
		ActionAttributeBo attributeBo1 = new ActionAttributeBo()
		attributeBo1.setId( ATTR_ID_1 )
		attributeBo1.setAttributeDefinitionId( ATTR_DEF_ID )
		attributeBo1.setValue( ATTR_VALUE )
		attributeBo1.setActionId( ACTION_ID_1 )
		attributeBo1.attributeDefinition = ADB1
		Set<ActionAttributeBo> attrBos = [attributeBo1]
		
		
		// Create ActionBo
		TEST_ACTION_BO = new ActionBo()
		TEST_ACTION_BO.setId ACTION_ID_1
		TEST_ACTION_BO.setNamespace NAMESPACE
		TEST_ACTION_BO.setName ACTION_NAME_1
		TEST_ACTION_BO.setTypeId TYPE_ID
		TEST_ACTION_BO.setRuleId RULE_ID_1
		TEST_ACTION_BO.setSequenceNumber SEQUENCE_1
		TEST_ACTION_BO.setAttributeBos attrBos
	}

	@Before
	void setupBoServiceMockContext() {
		mockBusinessObjectService = new MockFor(BusinessObjectService.class)
	}

	@Test
	public void test_getActionByActionId() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {clazz, id -> TEST_ACTION_BO}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)
		ActionDefinition myAction = service.getActionByActionId(ACTION_ID_1)

		Assert.assertEquals(ActionBo.to(TEST_ACTION_BO), myAction)
		mockBusinessObjectService.verify(bos)
	}

	@Test
	public void test_getActionByActionId_when_none_found() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {Class clazz, String id -> null}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)
		ActionDefinition myAction = service.getActionByActionId("I_DONT_EXIST")

		Assert.assertNull(myAction)
		mockBusinessObjectService.verify(bos)
	}

	@Test
	public void test_getActionByActionId_empty_id() {
		shouldFail(IllegalArgumentException.class) {
			new ActionBoServiceImpl().getActionByActionId("")
		}
	}

	@Test
	public void test_getActionByActionId_null_action_id() {
		shouldFail(IllegalArgumentException.class) {
			new ActionBoServiceImpl().getActionByActionId(null)
		}
	}
	
	@Test
	public void test_getActionByNameAndNamespace() {
		mockBusinessObjectService.demand.findByPrimaryKey(1..1) {clazz, map -> TEST_ACTION_BO}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)
		ActionDefinition myAction = service.getActionByNameAndNamespace(ACTION_ID_1, NAMESPACE)

		Assert.assertEquals(ActionBo.to(TEST_ACTION_BO), myAction)
		mockBusinessObjectService.verify(bos)
	}

	@Test
	public void test_getActionByNameAndNamespace_when_none_found() {
		mockBusinessObjectService.demand.findByPrimaryKey(1..1) {Class clazz, Map map -> null}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)
		ActionDefinition myAction = service.getActionByNameAndNamespace("I_DONT_EXIST", NAMESPACE)

		Assert.assertNull(myAction)
		mockBusinessObjectService.verify(bos)
	}

	@Test
	public void test_getActionByNameAndNamespace_empty_name() {
		shouldFail(IllegalArgumentException.class) {
			new ActionBoServiceImpl().getActionByNameAndNamespace("", NAMESPACE)
		}
	}

	@Test
	public void test_getActionByNameAndNamespace_null_name() {
		shouldFail(IllegalArgumentException.class) {
			new ActionBoServiceImpl().getActionByNameAndNamespace(null, NAMESPACE)
		}
	}

	@Test
	public void test_getActionByNameAndNamespace_empty_namespace() {
		shouldFail(IllegalArgumentException.class) {
			new ActionBoServiceImpl().getActionByNameAndNamespace(ACTION_ID_1, "")
		}
	}

	@Test
	public void test_getActionByNameAndNamespace_null_namespace() {
		shouldFail(IllegalArgumentException.class) {
			new ActionBoServiceImpl().getActionByNameAndNamespace(ACTION_ID_1, null)
		}
	}

	@Test
	public void test_getActionsByRuleId() {
		mockBusinessObjectService.demand.findMatchingOrderBy(1..1) {Class clazz, Map map, String columnName, boolean bool -> [TEST_ACTION_BO]}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)
		List<ActionDefinition> myActions = service.getActionsByRuleId(RULE_ID_1)

		Assert.assertEquals(ActionBo.to(TEST_ACTION_BO), myActions[0])
		mockBusinessObjectService.verify(bos)
	}

	@Test
	public void test_getActionsByRuleId_when_none_found() {
		mockBusinessObjectService.demand.findMatchingOrderBy(1..1) {Class clazz, Map map, String columnName, boolean bool -> null}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)
		List<ActionDefinition> myActions = service.getActionsByRuleId("I_DONT_EXIST")

		Assert.assertEquals(myActions.size(), 0)
		mockBusinessObjectService.verify(bos)
	}

	@Test
	public void test_getActionsByRuleId_empty_id() {
		shouldFail(IllegalArgumentException.class) {
			new ActionBoServiceImpl().getActionsByRuleId("")
		}
	}

	@Test
	public void test_getActionsByRuleId_null_rule_id() {
		shouldFail(IllegalArgumentException.class) {
			new ActionBoServiceImpl().getActionsByRuleId(null)
		}
	}

	@Test
	public void test_getActionByRuleIdAndSequenceNumber() {
		mockBusinessObjectService.demand.findByPrimaryKey(1..1) {clazz, map -> TEST_ACTION_BO}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)
		ActionDefinition myAction = service.getActionByRuleIdAndSequenceNumber(RULE_ID_1, SEQUENCE_1)

		Assert.assertEquals(ActionBo.to(TEST_ACTION_BO), myAction)
		mockBusinessObjectService.verify(bos)
	}

	@Test
	public void test_getActionByRuleIdAndSequenceNumber_when_none_found() {
		mockBusinessObjectService.demand.findByPrimaryKey(1..1) {Class clazz, Map id -> null}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()

		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)
		ActionDefinition myAction = service.getActionByRuleIdAndSequenceNumber("I_DONT_EXIST", SEQUENCE_1)

		Assert.assertNull(myAction)
		mockBusinessObjectService.verify(bos)
	}

	@Test
	public void test_getActionByRuleIdAndSequenceNumber_empty_id() {
		shouldFail(IllegalArgumentException.class) {
			new ActionBoServiceImpl().getActionByRuleIdAndSequenceNumber("", SEQUENCE_1)
		}
	}

	@Test
	public void test_getActionByRuleIdAndSequenceNumber_null_rule_id() {
		shouldFail(IllegalArgumentException.class) {
			new ActionBoServiceImpl().getActionByRuleIdAndSequenceNumber(null, SEQUENCE_1)
		}
	}
	
	@Test
	public void test_getActionByRuleIdAndSequenceNumber_null_rule_sequence() {
		shouldFail(IllegalArgumentException.class) {
			new ActionBoServiceImpl().getActionByRuleIdAndSequenceNumber(RULE_ID_1, null)
		}
	}

  @Test
  public void test_createAction_null_input() {
	  def boService = mockBusinessObjectService.proxyDelegateInstance()
	  ActionBoService service = new ActionBoServiceImpl()
	  service.setBusinessObjectService(boService)
	  shouldFail(IllegalArgumentException.class) {
		  service.createAction(null)
	  }
	  mockBusinessObjectService.verify(boService)
  }

  @Test
  void test_createAction_exists() {
		mockBusinessObjectService.demand.findByPrimaryKey(1..1) {
			Class clazz, Map map -> TEST_ACTION_BO
		}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)
		shouldFail(IllegalStateException.class) {
			service.createAction(TEST_ACTION_DEF)
		}
		mockBusinessObjectService.verify(bos)
  }

  @Test
  void test_createAction_success() {
		mockBusinessObjectService.demand.findByPrimaryKey(1..1) {Class clazz, Map map -> null}
		mockBusinessObjectService.demand.findMatching(1..1) { Class clazz, Map map -> [ADB1] }
		mockBusinessObjectService.demand.save { PersistableBusinessObject bo -> }
		
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)
		
		KrmsAttributeDefinitionService kads = new KrmsAttributeDefinitionServiceImpl();
		kads.setBusinessObjectService(bos)
		KrmsRepositoryServiceLocator.setKrmsAttributeDefinitionService(kads)
		
		service.createAction(TEST_ACTION_DEF)
		mockBusinessObjectService.verify(bos)
  }

  @Test
  public void test_updateAction_null_input() {
	  def boService = mockBusinessObjectService.proxyDelegateInstance()
	  ActionBoService service = new ActionBoServiceImpl()
	  service.setBusinessObjectService(boService)
	  shouldFail(IllegalArgumentException.class) {
		  service.updateAction(null)
	  }
	  mockBusinessObjectService.verify(boService)
  }

  @Test
  void test_updateAction_does_not_exist() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {
			Class clazz, String id -> null
		}
		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)
		shouldFail(IllegalStateException.class) {
			service.updateAction(TEST_ACTION_DEF)
		}
		mockBusinessObjectService.verify(bos)
  }

  @Test
  void test_updateAction_success() {
		mockBusinessObjectService.demand.findBySinglePrimaryKey(1..1) {Class clazz, String id -> TEST_ACTION_BO}
		mockBusinessObjectService.demand.findMatching(1..1) { Class clazz, Map map -> [ADB1] }
		mockBusinessObjectService.demand.deleteMatching(1) { Class clazz, Map map -> }
		mockBusinessObjectService.demand.save { PersistableBusinessObject bo -> }

		BusinessObjectService bos = mockBusinessObjectService.proxyDelegateInstance()
		ActionBoService service = new ActionBoServiceImpl()
		service.setBusinessObjectService(bos)

		KrmsAttributeDefinitionService kads = new KrmsAttributeDefinitionServiceImpl();
		kads.setBusinessObjectService(bos)
		KrmsRepositoryServiceLocator.setKrmsAttributeDefinitionService(kads)
		
		service.updateAction(TEST_ACTION_DEF)
		mockBusinessObjectService.verify(bos)
  }

}
