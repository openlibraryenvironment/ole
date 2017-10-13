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
import org.kuali.rice.krms.api.repository.LogicalOperator;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinitionContract;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameter;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameterContract;
import org.kuali.rice.krms.api.repository.term.TermDefinition
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperator;

class PropositionBoServiceImplTest {
    private def MockFor mock
    private final shouldFail = new GroovyTestCase().&shouldFail
	private PropositionBoServiceImpl pservice;

	// Simple Proposition data structures	
	private static final List <PropositionParameter.Builder> parmList1 = createPropositionParametersSet1()
    private static final PropositionDefinition propositionWithId = createPropositionWithId()
    private static final PropositionDefinition propositionWithNullId = createPropositionWithNullId()
	private PropositionBo bo = PropositionBo.from(propositionWithId)

	// Compound Propositon data structures
	private static final List <PropositionParameter.Builder> parmListA = createPropositionParametersSet2()
	private static final List <PropositionParameter.Builder> parmListB = createPropositionParametersSet3()
	private static final PropositionDefinition.Builder propositionABuilder = createPropositionABuilder()
	private static final PropositionDefinition.Builder propositionBBuilder = createPropositionBBuilder()
	private static final PropositionDefinition compoundPropositionWithNullId = createCompoundPropositionWithNullId()

	// for PropositionParameter tests 	
	static List<PropositionParameter> parmList = new ArrayList<PropositionParameter>()
	static List<PropositionParameterBo> boList = new ArrayList<PropositionParameterBo>()

	@BeforeClass
	static void createSampleBOs() {
		PropositionParameterBo bo1 = new PropositionParameterBo(id: "1000",
			propId:"2001", value: "campusCode", parameterType: "T",
			sequenceNumber: new Integer("0"), versionNumber: new Long(1))
		PropositionParameterBo bo2 = new PropositionParameterBo(id: "1001",
			propId:"2001", value: "BL", parameterType: "C", 
			sequenceNumber: new Integer("1"), versionNumber: new Long(1))
		PropositionParameterBo bo3 = new PropositionParameterBo(id: "1003",
			propId:"2001", value: ComparisonOperator.EQUALS, parameterType: "F", 
			sequenceNumber: new Integer("2"), versionNumber: new Long(1))
		for (bo in [bo1, bo2, bo3]) {
		  boList.add(bo)
		}
	}
	
	@BeforeClass
	static void createSampleParameters() {
		PropositionParameter parm1 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "1000"
			def String propId = "2001"
			def String value = "campusCode"
                        def TermDefinition termValue
			def String parameterType = "T"
			def Integer sequenceNumber = new Integer("0")
			def Long versionNumber = new Long(1);
		}).build()
		PropositionParameter parm2 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "1001"
			def String propId = "2001"
			def String value = "BL"
                        def TermDefinition termValue
			def String parameterType = "C"
			def Integer sequenceNumber = new Integer("1")
			def Long versionNumber = new Long(1);
		}).build()
		PropositionParameter parm3 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "1003"
			def String propId = "2001"
			def String value = ComparisonOperator.EQUALS
                        def TermDefinition termValue
			def String parameterType = "F"
			def Integer sequenceNumber = new Integer("2")
			def Long versionNumber = new Long(1);
		}).build()
		for (p in [parm1, parm2, parm3]) {
		  parmList.add(p)
		}
	}


    @Before
    void setupBoServiceMockContext() {
        mock = new MockFor(BusinessObjectService.class)
		pservice = new PropositionBoServiceImpl()
    }

//
// Proposition tests
//			
	@Test
	public void test_create_proposition_null_proposition() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService)
		shouldFail(IllegalArgumentException.class) {
			pservice.createProposition(null)
		}
		mock.verify(boService)
	}

	@Test
	void test_create_proposition_exists() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService)
		shouldFail(IllegalStateException.class) {
			pservice.createProposition(propositionWithId)
		}
		mock.verify(boService)
	}

    @Test
    void test_create_proposition_success() {
        mock.demand.save { PersistableBusinessObject bo -> }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService)
        pservice.createProposition(propositionWithNullId)
        mock.verify(boService)
    }

	@Test
	public void test_update_proposition_null_proposition() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService)
		shouldFail(IllegalArgumentException.class) {
			pservice.updateProposition(null)
		}
		mock.verify(boService)
	}

	@Test
    void test_update_proposition_exists() {
        mock.demand.findBySinglePrimaryKey (1..1) { clazz, map -> bo }
        mock.demand.save { PersistableBusinessObject bo -> }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService)
        pservice.updateProposition(propositionWithId)
        mock.verify(boService)
    }

	@Test
    void test_update_proposition_does_not_exist() {
        mock.demand.findBySinglePrimaryKey (1..1) { clazz, map -> null }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService)
		shouldFail(IllegalStateException.class) {
			pservice.updateProposition(propositionWithId)
		}
        mock.verify(boService)
    }

	@Test
	void test_get_proposition_by_id_null_id() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);

		shouldFail(IllegalArgumentException.class) {
			pservice.getPropositionById(null)
		}
		mock.verify(boService)
	}

    @Test
    void test_get_proposition_by_id_exists() {
        mock.demand.findBySinglePrimaryKey (1..1) { clazz, map -> bo }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService)
        Assert.assertEquals (propositionWithId, pservice.getPropositionById("2002"))
        mock.verify(boService)
    }

    @Test
    void test_get_proposition_by_id_does_not_exist() {
        mock.demand.findBySinglePrimaryKey (1..1) { clazz, map -> null }
        def boService = mock.proxyDelegateInstance()
        pservice.setBusinessObjectService(boService)
        Assert.assertNull (pservice.getPropositionById("blah"))
        mock.verify(boService)
    }

	@Test
	void test_create_compound_proposition_success() {
		mock.demand.save { PersistableBusinessObject bo -> }
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService)
		pservice.createProposition(compoundPropositionWithNullId)
		mock.verify(boService)
	}

//
//	PropositionParameter tests
//
	
	@Test
	public void test_create_parameter_null_parameter() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService)
		shouldFail(IllegalArgumentException.class) {
			pservice.createParameter(null)
		}
		mock.verify(boService)
	}

	@Test
	void test_create_parameter_exists() {
		mock.demand.findByPrimaryKey (1..1) { clazz, map -> boList.get(0) }
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);
		shouldFail(IllegalStateException.class) {
			pservice.createParameter(parmList.get(0))
		}
		mock.verify(boService)
	}

	@Test
	void test_create_parameter_does_not_exist() {
		mock.demand.findByPrimaryKey (1..1) { clazz, map -> null }
		mock.demand.save { PersistableBusinessObject  myBo ->  }
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);
		pservice.createParameter(parmList.get(0))
		mock.verify(boService)
	}

	@Test
	void test_update_parameter_null_parameter() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);

		shouldFail(IllegalArgumentException.class) {
			pservice.updateParameter(null)
		}
		mock.verify(boService)
	}

	@Test
	void test_update_parameter_exists() {
		mock.demand.findByPrimaryKey (1..1) { clazz, map -> boList.get(0) }
		mock.demand.save { bo -> }
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);
		pservice.updateParameter(parmList.get(0))
		mock.verify(boService)
	}

	@Test
	void test_update_parameter_does_not_exist() {
		mock.demand.findByPrimaryKey (1..1) { clazz, map -> null }

		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);

		shouldFail(IllegalStateException.class) {
			pservice.updateParameter(parmList.get(0))
		}
		mock.verify(boService)
	}

	@Test
	void test_get_parameter_by_id_null_id() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);

		shouldFail(IllegalArgumentException.class) {
			pservice.getParameterById(null)
		}
		mock.verify(boService)
	}

	@Test
	void test_get_parameter_by_id_blank_id() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);

		shouldFail(IllegalArgumentException.class) {
			pservice.getParameterById("")
		}
		mock.verify(boService)
	}

	@Test
	void test_get_parameter_by_id_does_not_exist() {
		mock.demand.findBySinglePrimaryKey(1..1){ clazz, id -> null }
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);
		Assert.assertNull pservice.getParameterById("blah")
		mock.verify(boService)
	}

	@Test
	void test_get_parameter_by_id_success() {
		mock.demand.findBySinglePrimaryKey(1..1){ clazz, id -> boList.get(0) }
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);
		Assert.assertEquals(parmList.get(0), pservice.getParameterById("1000"))
		mock.verify(boService)
	}

	@Test
	void test_get_parameter_by_prop_id_and_seq_null_id() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);

		shouldFail(IllegalArgumentException.class) {
			pservice.getParameterByPropIdAndSequenceNumber(null, new Integer("1"))
		}
		mock.verify(boService)
	}

	@Test
	void test_get_parameter_by_prop_id_and_seq_blank_id() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);

		shouldFail(IllegalArgumentException.class) {
			pservice.getParameterByPropIdAndSequenceNumber("", new Integer("2"))
		}
		mock.verify(boService)
	}

	@Test
	void test_get_parameter_by_prop_id_and_seq_null_seq() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);

		shouldFail(IllegalArgumentException.class) {
			pservice.getParameterByPropIdAndSequenceNumber("2001", null)
		}
		mock.verify(boService)
	}

	@Test
	void test_get_parameter_by_prop_id_and_seq_does_not_exist() {
		mock.demand.findByPrimaryKey(1..1){ clazz, map -> null }
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);
		Assert.assertNull pservice.getParameterByPropIdAndSequenceNumber("blah", new Integer("0"))
		mock.verify(boService)
	}

	@Test
	void test_get_parameter_by_prop_id_and_seq_success() {
		mock.demand.findByPrimaryKey(1..1){ clazz, map -> boList.get(1) }
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);
		Assert.assertEquals(parmList.get(1), pservice.getParameterByPropIdAndSequenceNumber("2001", new Integer("1")))
		mock.verify(boService)
	}

	@Test
	void test_get_parameters_null_prop_id() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);

		shouldFail(IllegalArgumentException.class) {
			pservice.getParameters(null)
		}
		mock.verify(boService)
	}

	@Test
	void test_get_parameters_null_blank_id() {
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);

		shouldFail(IllegalArgumentException.class) {
			pservice.getParameters("")
		}
		mock.verify(boService)
	}

	@Test
	void test_get_parameters_does_not_exist() {
		mock.demand.findMatchingOrderBy (1..1) { clazz, fieldValues, sortField, sortAscending -> null }
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);
		Assert.assertNull (pservice.getParameters("blah"))
		mock.verify(boService)
	}

	@Test
	void test_get_parameters_success() {
		mock.demand.findMatchingOrderBy (1..1) { clazz, fieldValues, sortField, sortAscending -> boList }
		def boService = mock.proxyDelegateInstance()
		pservice.setBusinessObjectService(boService);
		Assert.assertEquals (parmList, pservice.getParameters("2001"))
		mock.verify(boService)
	}


//
// private static methods for creating test data
//		
	private static createPropositionWithId() {
		return PropositionDefinition.Builder.create(new PropositionDefinitionContract () {
			def String id = "2002"
			def String description = "Is campus type = Bloomington"
            def String ruleId = "1"
            def String typeId = "1"
			def String propositionTypeCode = "S"
			def List<? extends PropositionParameterContract> parameters = PropositionBoServiceImplTest.parmList1
			def String compoundOpCode = null
                        def Integer compoundSequenceNumber = null
			def List<? extends PropositionDefinition> compoundComponents = new ArrayList<PropositionDefinition>()
			def Long versionNumber = new Long(1)
		}).build()
	}
    
    private static createPropositionWithNullId() {
        return PropositionDefinition.Builder.create(new PropositionDefinitionContract () {
            def String id = null
            def String description = "Is campus type = Bloomington"
            def String ruleId = "1"
            def String typeId = "1"
            def String propositionTypeCode = "S"
            def List<? extends PropositionParameterContract> parameters = PropositionBoServiceImplTest.parmList1
            def String compoundOpCode = null
            def Integer compoundSequenceNumber = null
            def List<? extends PropositionDefinition> compoundComponents = new ArrayList<PropositionDefinition>()
			def Long versionNumber = new Long(1)
        }).build()
    }
		
	private static createPropositionABuilder() {
		return PropositionDefinition.Builder.create(new PropositionDefinitionContract () {
			def String id = "100"
			def String description = "Is campus type = Muir"
            def String ruleId = "1"
			def String typeId = "1"
			def String propositionTypeCode = "S"
			def List<? extends PropositionParameterContract> parameters = PropositionBoServiceImplTest.parmListA
			def String compoundOpCode = null
                        def Integer compoundSequenceNumber = null
			def List<? extends PropositionDefinition> compoundComponents = new ArrayList<PropositionDefinition>()
			def Long versionNumber = new Long(1)
		})
	}
		
	private static createPropositionBBuilder() {
		return PropositionDefinition.Builder.create(new PropositionDefinitionContract () {
			def String id = "101"
			def String description = "Is campus type = Thurgood Marshall"
            def String ruleId = "1"
			def String typeId = "1"
			def String propositionTypeCode = "S"
			def List<? extends PropositionParameterContract> parameters = PropositionBoServiceImplTest.parmListB
			def String compoundOpCode = null
                        def Integer compoundSequenceNumber = null
			def List<? extends PropositionDefinition> compoundComponents = new ArrayList<PropositionDefinition>()
			def Long versionNumber = new Long(1)
		})
	}
		
	private static createCompoundPropositionWithNullId() {
		return PropositionDefinition.Builder.create(new PropositionDefinitionContract () {
			def String id = null
			def String description = "Compound: Campus is Muir or Thurgood Marshall"
            def String ruleId = "1"
			def String typeId = "1"
			def String propositionTypeCode = "C"
			def List<? extends PropositionParameterContract> parameters = new ArrayList<PropositionParameter.Builder>()
			def String compoundOpCode = LogicalOperator.OR.getCode()
                        def Integer compoundSequenceNumber = null
			def List<? extends PropositionDefinition> compoundComponents = Arrays.asList(PropositionBoServiceImplTest.propositionABuilder, PropositionBoServiceImplTest.propositionBBuilder)
			def Long versionNumber = new Long(1)
		}).build()
	}
		
	private static createPropositionParametersSet1(){
		List <PropositionParameter.Builder> propParms = new ArrayList <PropositionParameter.Builder> ()
		PropositionParameter.Builder ppBuilder1 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "1000"
			def String propId = "2002"
			def String value = "campusCode"
			def String parameterType = "T"
			def Integer sequenceNumber = new Integer(0)
			def Long versionNumber = new Long(1)               
                        public TermDefinition getTermValue () {
                           return null;
                        }
		})
		PropositionParameter.Builder ppBuilder2 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "1001"
			def String propId = "2002"
			def String value = "BL"
			def String parameterType = "C"
			def Integer sequenceNumber = new Integer(1)
			def Long versionNumber = new Long(1)               
                        public TermDefinition getTermValue () {
                           return null;
                        }
		})
		PropositionParameter.Builder ppBuilder3 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "1003"
			def String propId = "2002"
			def String value = ComparisonOperator.EQUALS
			def String parameterType = "F"
			def Integer sequenceNumber = new Integer(2)
			def Long versionNumber = new Long(1)               
                        public TermDefinition getTermValue () {
                           return null;
                        }
		})
		for ( ppb in [ppBuilder1, ppBuilder2, ppBuilder3]){
			propParms.add (ppb)
		}
		return propParms;
	}
	private static createPropositionParametersSet2(){
		List <PropositionParameter.Builder> propParms = new ArrayList <PropositionParameter.Builder> ()
		PropositionParameter.Builder ppBuilder1 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "2000"
			def String propId = "100"
			def String value = "campusCode"
			def String parameterType = "T"
			def Integer sequenceNumber = new Integer(0)
			def Long versionNumber = new Long(1)               
                        public TermDefinition getTermValue () {
                           return null;
                        }
		})
		PropositionParameter.Builder ppBuilder2 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "2001"
			def String propId = "100"
			def String value = "Muir"
			def String parameterType = "C"
			def Integer sequenceNumber = new Integer(1)
			def Long versionNumber = new Long(1)               
                        public TermDefinition getTermValue () {
                           return null;
                        }
		})
		PropositionParameter.Builder ppBuilder3 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "2002"
			def String propId = "100"
			def String value = ComparisonOperator.EQUALS
			def String parameterType = "F"
			def Integer sequenceNumber = new Integer(2)
			def Long versionNumber = new Long(1)                
                        public TermDefinition getTermValue () {
                           return null;
                        }
		})
		for ( ppb in [ppBuilder1, ppBuilder2, ppBuilder3]){
			propParms.add (ppb)
		}
		return propParms;
	}
	private static createPropositionParametersSet3(){
		List <PropositionParameter.Builder> propParms = new ArrayList <PropositionParameter.Builder> ()
		PropositionParameter.Builder ppBuilder1 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "2010"
			def String propId = "101"
			def String value = "campusCode"
			def String parameterType = "T"
			def Integer sequenceNumber = new Integer("0")
			def Long versionNumber = new Long(1)               
                        public TermDefinition getTermValue () {
                           return null;
                        }
		})
		PropositionParameter.Builder ppBuilder2 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "2011"
			def String propId = "101"
			def String value = "Thurgood Marshall"
			def String parameterType = "C"
			def Integer sequenceNumber = new Integer("1")
			def Long versionNumber = new Long(1)               
                        public TermDefinition getTermValue () {
                           return null;
                        }
		})
		PropositionParameter.Builder ppBuilder3 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "2012"
			def String propId = "101"
			def String value = ComparisonOperator.EQUALS
			def String parameterType = "F"
			def Integer sequenceNumber = new Integer("2")
			def Long versionNumber = new Long(1)               
                        public TermDefinition getTermValue () {
                           return null;
                        }
		})
		for ( ppb in [ppBuilder1, ppBuilder2, ppBuilder3]){
			propParms.add (ppb)
		}
		return propParms;
	}
}
