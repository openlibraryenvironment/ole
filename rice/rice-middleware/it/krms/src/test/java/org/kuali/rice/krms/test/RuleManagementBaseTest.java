/*
 * Copyright 2006-2013 The Kuali Foundation
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

package org.kuali.rice.krms.test;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.junit.Before;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krms.api.repository.LogicalOperator;
import org.kuali.rice.krms.api.repository.RuleManagementService;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameter;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameterType;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.impl.repository.ActionBoService;
import org.kuali.rice.krms.impl.repository.AgendaBoService;
import org.kuali.rice.krms.impl.repository.ContextBoService;
import org.kuali.rice.krms.impl.repository.FunctionBoServiceImpl;
import org.kuali.rice.krms.impl.repository.KrmsAttributeDefinitionService;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.repository.RuleBoService;
import org.kuali.rice.krms.impl.repository.TermBoService;
import org.kuali.rice.test.BaselineTestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Base test case and methods for testing RuleManagementServiceImpl
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public abstract class RuleManagementBaseTest extends KRMSTestCase {

    protected RuleManagementService ruleManagementService;
    protected TermBoService termBoService;
    protected ContextBoService contextRepository;
    protected KrmsTypeRepositoryService krmsTypeRepository;
    protected RuleBoService ruleBoService;
    protected AgendaBoService agendaBoService;
    protected ActionBoService actionBoService;
    protected FunctionBoServiceImpl functionBoService;
    protected KrmsAttributeDefinitionService krmsAttributeDefinitionService;
    protected BusinessObjectService businessObjectService;

    protected String CLASS_DISCRIMINATOR;

    private static String lastTestClass = null;

    @Before
    public void setup() {
        // Reset TestActionTypeService
        TestActionTypeService.resetActionsFired();

        ruleManagementService = KrmsRepositoryServiceLocator.getService("ruleManagementService");
        termBoService = KrmsRepositoryServiceLocator.getTermBoService();
        contextRepository = KrmsRepositoryServiceLocator.getContextBoService();
        krmsTypeRepository = KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();
        ruleBoService = KrmsRepositoryServiceLocator.getRuleBoService();
        agendaBoService = KrmsRepositoryServiceLocator.getAgendaBoService();
        actionBoService = KrmsRepositoryServiceLocator.getBean("actionBoService");
        functionBoService = KrmsRepositoryServiceLocator.getBean("functionRepositoryService");
        krmsAttributeDefinitionService = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService();
        businessObjectService = KRADServiceLocator.getBusinessObjectService();
    }

    /**
     * Extending test classes can override setClassDiscriminator method and set a unique value for the class
     *
     *   The override method should be called @before tests to ensure a unique discriminator for the class
     *
     *  Test object naming is comprised of class, test and object uniqueness discriminators.
     *     The Class Discriminator is set by this method
     *
     *     A Test Discriminator may be used to set unique test names at the start of each test.
     *        ex: RuleManagementBaseTestObjectNames.setTestObjectNames(testDiscriminator)
     *     The Object Discriminators are Sequential (object0, object1 ...)
     */
    @Before
    public void setClassDiscriminator() {
        // set a unique discriminator for test objects of this class should be uniquely set by each extending class
        CLASS_DISCRIMINATOR = "BaseTest";
    }

    /**
     *
     * Setting it up so that KRMS tables are not reset between test methods to make it run much faster
     *
     * @return
     */
    @Override
    protected List<String> getPerTestTablesNotToClear() {
        List<String> tablesNotToClear = super.getPerTestTablesNotToClear();

        // HACK: clear KRMS tables for first test method run, but not subsequent methods in the same class
        if (getClass().getName().equals(lastTestClass)) { //
            tablesNotToClear.add("KRMS_.*");
        }
        lastTestClass = getClass().getName();

        return tablesNotToClear;
    }

    /**
     *   buildTestRuleDefinition will create a RuleDefinition entry in the database
     *
     * @param namespace
     * @param objectDiscriminator
     *
     * @return {@link RuleDefinition}
     */
    protected RuleDefinition buildTestRuleDefinition(String namespace, String objectDiscriminator) {
        PropositionDefinition propositionDefinition = createTestPropositionForRule(objectDiscriminator);

        String ruleId = "RuleId" + objectDiscriminator;
        String name = "RuleName" + objectDiscriminator;
        String propId = propositionDefinition.getId();
        RuleDefinition.Builder ruleDefinitionBuilder = RuleDefinition.Builder.create(ruleId, name, namespace, null, propId);
        ruleDefinitionBuilder.setProposition(PropositionDefinition.Builder.create(propositionDefinition));

        String id = ruleManagementService.createRule(ruleDefinitionBuilder.build()).getId();
        RuleDefinition ruleDefinition = ruleManagementService.getRule(id);

        return ruleDefinition;
    }

    /**
     *  buildTestAgendaItemDefinition will build aAgendaItemDefinition for testing
     *
     * @param agendaItemId
     * @param agendaId
     * @param ruleId
     *
     * @return {@link AgendaItemDefinition}
     */
    protected AgendaItemDefinition buildTestAgendaItemDefinition(String agendaItemId, String agendaId, String ruleId) {
        AgendaItemDefinition.Builder agendaItemDefinitionBuilder = AgendaItemDefinition.Builder.create(agendaItemId,
                agendaId);
        agendaItemDefinitionBuilder.setRuleId(ruleId);

        String id = ruleManagementService.createAgendaItem(agendaItemDefinitionBuilder.build()).getId();
        AgendaItemDefinition agendaItemDefinition = ruleManagementService.getAgendaItem(id);

        AgendaDefinition agendaDefinition = ruleManagementService.getAgenda(agendaId);
        AgendaDefinition.Builder agendaDefinitionBuilder = AgendaDefinition.Builder.create(agendaDefinition);
        agendaDefinitionBuilder.setFirstItemId(agendaItemDefinition.getId());
        ruleManagementService.updateAgenda(agendaDefinitionBuilder.build());

        return agendaItemDefinition;
    }

    /**
     *   buildTestActionDefinition creates Actions in the database for testing
     *
     * @param actionId
     * @param actionName
     * @param actionDescr
     * @param actionSequence
     * @param ruleId
     * @param namespace
     *
     * @return {@link ActionDefinition}
     */
    protected ActionDefinition buildTestActionDefinition(String actionId, String actionName, String actionDescr, int actionSequence, String ruleId, String namespace) {
        return buildTestActionDefinition(actionId, actionName, actionDescr, actionSequence, ruleId, namespace, new HashMap<String, String>());
    }

    /**
     *   buildTestActionDefinition creates Actions in the database for testing
     *
     * @param actionId
     * @param actionName
     * @param actionDescr
     * @param actionSequence
     * @param ruleId
     * @param namespace
     * @param attributes
     *
     * @return {@link ActionDefinition}
     */
    protected ActionDefinition buildTestActionDefinition(String actionId, String actionName, String actionDescr, int actionSequence, String ruleId, String namespace, Map<String, String> attributes) {
        KrmsTypeDefinition krmsTypeDefinition = createKrmsActionTypeDefinition(namespace);

        ActionDefinition.Builder actionDefinitionBuilder = ActionDefinition.Builder.create(actionId, actionName,
                namespace, krmsTypeDefinition.getId(), ruleId, actionSequence);
        actionDefinitionBuilder.setDescription(actionDescr);
        actionDefinitionBuilder.setAttributes(attributes);
        String id =  ruleManagementService.createAction(actionDefinitionBuilder.build()).getId();
        ActionDefinition actionDefinition = ruleManagementService.getAction(id);

        return actionDefinition;
    }

    /**
     *  createTestPropositionForRule will create a PropositionDefinition entry in the database for test purposes
     *
     *    The form of the Proposition is   propId_simple_proposition   "SIMPLE"   "TSI_" + propId  "ABC"  "="
     * @param objectDiscriminator
     *
     * @return {@link PropositionDefinition}
     */
    protected PropositionDefinition createTestPropositionForRule(String objectDiscriminator) {
        createKrmsActionTypeDefinition("Namespace" + objectDiscriminator);

        String namespace = "Namespace" + objectDiscriminator;
        String propId = "P" + objectDiscriminator;
        String termSpecId = "TSI_" + propId;
        String ruleId = "RuleId" + objectDiscriminator;
        String termSpecDescr = "TSI_" + propId + "_Descr";

        return createTestSimpleProposition(namespace, propId, termSpecId, "ABC", "=", "java.lang.String", ruleId, termSpecDescr);
    }

    /**
     *  createTestPropositionForTranslation
     *
     * @param objectDiscriminator
     * @param namespace
     * @param typeId
     *
     * @return {@link PropositionDefinition}
     */
    protected PropositionDefinition createTestPropositionForTranslation(String objectDiscriminator, String namespace, String typeName) {
        createKrmsTypeDefinition("TypeId" + objectDiscriminator, namespace, typeName, null);

        String ruleId = "RuleId" + objectDiscriminator;
        String propId = "P" + objectDiscriminator;

        return createTestSimpleProposition(namespace, propId, typeName, "ABC", "=", "java.lang.String", ruleId,
                "TSI_" + propId + "_Descr");
    }

    /**
     *   createTestSimpleProposition creates a SIMPLE PropositionDefinition set of entries in the database
     *
     * @param namespace of the proposition type
     * @param propId
     * @param termSpecId
     * @param propConstant
     * @param propOperator
     * @param termSpecType
     * @param ruleId
     * @param termSpecDescr
     *
     * @return {@link PropositionDefinition}
     */
    protected PropositionDefinition createTestSimpleProposition(String namespace, String propId, String termSpecId, String propConstant, String propOperator, String termSpecType, String ruleId, String termSpecDescr){
        createTestTermSpecification(termSpecId, termSpecId, namespace, termSpecType, termSpecDescr);
        KrmsTypeDefinition krmsTypeDefinition = createKrmsTypeDefinition(null, namespace, termSpecId, "testTypeService");

        List<PropositionParameter.Builder> propParam =  new ArrayList<PropositionParameter.Builder>();
        propParam.add(PropositionParameter.Builder.create(propId + "_0", "unused_notnull", termSpecId,
                PropositionParameterType.TERM.getCode(), 0));
        propParam.add(PropositionParameter.Builder.create(propId + "_1", "unused_notnull", propConstant,
                PropositionParameterType.CONSTANT.getCode(), 1));
        propParam.add(PropositionParameter.Builder.create(propId + "_2", "unused_notnull", propOperator,
                PropositionParameterType.OPERATOR.getCode(), 2));
        PropositionDefinition.Builder propBuilder = PropositionDefinition.Builder.create(null,
                PropositionType.SIMPLE.getCode(), ruleId, krmsTypeDefinition.getId(), propParam);
        propBuilder.setDescription(propId + "_simple_proposition");

        String id = ruleManagementService.createProposition(propBuilder.build()).getId();
        PropositionDefinition propositionDefinition = ruleManagementService.getProposition(id);

        return propositionDefinition;
    }

    /**
     *   createTestTermSpecification
     *
     * @param termSpecId
     * @param termSpecName
     * @param namespace
     * @param type
     * @param termSpecDescr
     *
     * @return {@link TermSpecificationDefinition}
     */
    protected TermSpecificationDefinition createTestTermSpecification(String termSpecId, String termSpecName, String namespace, String type, String termSpecDescr){
        TermSpecificationDefinition termSpecificationDefinition = termBoService.getTermSpecificationByNameAndNamespace(
                termSpecName, namespace);

        if (termSpecificationDefinition == null) {
            TermSpecificationDefinition.Builder termSpecificationDefinitionBuilder =
                    TermSpecificationDefinition.Builder.create(null, termSpecName, namespace, type);
            termSpecificationDefinitionBuilder.setDescription(termSpecDescr);
            String id = termBoService.createTermSpecification(termSpecificationDefinitionBuilder.build()).getId();
            termSpecificationDefinition = termBoService.getTermSpecificationById(id);
        }

        return termSpecificationDefinition;
    }

    /**
     * createKrmsTypeDefinition
     *
     * @param typeId
     * @param nameSpace
     * @param typeName
     * @param serviceName
     * @param typeAttributes
     *
     * @return {@link KrmsTypeDefinition}
     */
    protected KrmsTypeDefinition createKrmsTypeDefinition(String typeId, String nameSpace, String typeName, String serviceName, List<KrmsTypeAttribute.Builder> typeAttributes) {
        KrmsTypeDefinition krmsTypeDefinition =  krmsTypeRepository.getTypeByName(nameSpace, typeName);

        if (krmsTypeDefinition == null) {
            KrmsTypeDefinition.Builder krmsTypeDefnBuilder = KrmsTypeDefinition.Builder.create(typeName, nameSpace);

            if (!CollectionUtils.isEmpty(typeAttributes)) {
                krmsTypeDefnBuilder.setAttributes(typeAttributes);
            }

            krmsTypeDefnBuilder.setId(typeId);
            krmsTypeDefnBuilder.setServiceName(serviceName);
            String id = krmsTypeRepository.createKrmsType(krmsTypeDefnBuilder.build()).getId();
            krmsTypeDefinition = krmsTypeRepository.getTypeById(id);
        }

        return krmsTypeDefinition;
    }

    /**
     * createKrmsTypeDefinition
     *
     * @param typeId
     * @param nameSpace
     * @param typeName
     * @param serviceName
     *
     * @return {@link KrmsTypeDefinition}
     */
    protected KrmsTypeDefinition createKrmsTypeDefinition(String typeId, String nameSpace, String typeName, String serviceName) {
        return createKrmsTypeDefinition(typeId, nameSpace, typeName, serviceName, null);
    }

    /**
     * Creates a test agenda setting the createAttributes flag to false
     * @see #createTestAgenda(String, boolean)
     */
    protected AgendaDefinition createTestAgenda(String objectDiscriminator) {
        return createTestAgenda(objectDiscriminator, false);
    }

    /**
     *   createTestAgenda creates a Agenda set of entries in the database for testing
     *
     *       Context ("ContextId0", "Namespace0", "ContextName0")
     *       Agenda  ("AgendaId0", "AgendaName0")
     *          AgendaItem ("AI0")
     *              Rule ("RuleId0")
     *      where 0 represents a discriminator value
     *
     * @param objectDiscriminator
     * @param createAttributes flag to have an attribute definition, a type attribute and an agenda attribute created
     *
     * @return {@link AgendaDefinition.Builder}
     */
    protected AgendaDefinition createTestAgenda(String objectDiscriminator, boolean createAttributes) {
        String namespace =  "Namespace" + objectDiscriminator;
        String typeId = "TypeId" + objectDiscriminator;
        String typeName = "TypeName" + objectDiscriminator;
        String agendaId = "AgendaId" + objectDiscriminator;
        String agendaName = "AgendaName" + objectDiscriminator;

        List<KrmsTypeAttribute.Builder> typeAttrs = Collections.emptyList();

        String attrDefName = "AttrName" + objectDiscriminator;
        String attrValue = "AttrVal" + objectDiscriminator;

        if (createAttributes) {
            // create an attribute definition
            String attrDefId = "KRTEST" + objectDiscriminator;
            String attrNamespace = "Namespace" + objectDiscriminator;

            KrmsAttributeDefinition attrDef = createTestKrmsAttribute(attrDefId, attrDefName, attrNamespace);

            typeAttrs = Collections.singletonList(KrmsTypeAttribute.Builder.create(null, attrDef.getId(), 1));
        }

        // create a type
        KrmsTypeDefinition krmsTypeDefinition = createKrmsTypeDefinition(typeId, namespace, typeName, null, typeAttrs);

        // create a context
        ContextDefinition contextDefinition = buildTestContext(objectDiscriminator);

        // create an agenda (an agendaItem cannot be created without an existing agenda).
        AgendaDefinition.Builder agendaBuilder = AgendaDefinition.Builder.create(agendaId, agendaName,
                krmsTypeDefinition.getId(), contextDefinition.getId());

        if (createAttributes) {
            agendaBuilder.setAttributes(Collections.singletonMap(attrDefName, attrValue));
        }

        String id = ruleManagementService.createAgenda(agendaBuilder.build()).getId();
        AgendaDefinition agendaDefinition = ruleManagementService.getAgenda(id);

        return agendaDefinition;
    }

    protected AgendaDefinition.Builder buildComplexAgenda(RuleManagementBaseTestObjectNames names) {
        String namespace = "Namespace" + names.discriminator;
        return createComplexAgenda(namespace, "AGENDA", names);
    }

    /**
     *   createComplexAgenda creates a "complex" agenda object in the database for testing
     *
     *   Structure of the created agenda is as shown here.
     *           // agenda
                 //   agendaItem0 ( rule0)
                 //       WhenTrue   agendaItem1( rule1 )
                 //       WhenFalse  agendaItem2( rule2 )
                 //       Always     agendaItem3( rule3 )
                 //   agendaItem1 ( rule1 )
                 //       Always     agendaItem5
                 //   agendaItem2 ( rule2 )
                 //       WhenFalse  agendaItem4
                 //       Always     agendaItem6
                 //   agendaItem3 ( rule3 )
                 //   agendaItem4 ( rule4 )
                 //   agendaItem5 ( rule5 )
                 //   agendaItem6 ( rule6 )
                 //   agendaItem7 ( rule7 )
                 //       action  ( key, value )
     *
     * @param namespace of the KRMS Agenda to be created
     * @param namespaceType of the namepace passed (Namespace will be created if it does not exist.)
     * @param {@link RuleManagementBaseTestObjectNames}  Unique discriminator names to base created Agenda upon
     *
     * @return {@link AgendaDefinition.Builder} populated from the built agenda
     */
    protected AgendaDefinition.Builder createComplexAgenda(String namespace, String namespaceType,
            RuleManagementBaseTestObjectNames names) {
        // create a context
        ContextDefinition.Builder contextBuilder = ContextDefinition.Builder.create(
                namespace, names.contextName);
        contextBuilder.setId(names.contextId);
        ContextDefinition context = contextBuilder.build();
        ruleManagementService.createContext(context );

        // create krms type AGENDA
        createKrmsTypeDefinition(null, namespace, namespaceType, null);

        // create a agenda
        AgendaDefinition.Builder agendaBuilder = AgendaDefinition.Builder.create(
                names.agenda_Id, names.agenda_Name, namespaceType, names.contextId);
        AgendaDefinition agenda = agendaBuilder.build();
        agenda = ruleManagementService.createAgenda(agenda);

        // create rules
        RuleDefinition rule0 = buildTestRuleDefinition(namespace, names.object0);
        RuleDefinition rule1 = buildTestRuleDefinition(namespace, names.object1);
        RuleDefinition rule2 = buildTestRuleDefinition(namespace, names.object2);
        RuleDefinition rule3 = buildTestRuleDefinition(namespace, names.object3);
        RuleDefinition rule4 = buildTestRuleDefinition(namespace, names.object4);
        RuleDefinition rule5 = buildTestRuleDefinition(namespace, names.object5);
        RuleDefinition rule6 = buildTestRuleDefinition(namespace, names.object6);
        RuleDefinition rule7 = buildTestRuleDefinition(namespace, names.object7);

        // register attribute types
        createTestKrmsAttribute(names.actionAttribute0, names.actionAttribute0_Key, names.namespaceName);
        createTestKrmsAttribute(names.actionAttribute1, names.actionAttribute1_Key, names.namespaceName);

        // create rule action attributes
        Map<String, String> actionAttributesOBJECT7 = new HashMap<String, String>();
        actionAttributesOBJECT7.put(names.actionAttribute_Key, names.actionAttribute_Value);

        // create rule actions
        buildTestActionDefinition(names.action_Id, names.action_Name, names.action_Descr, 1, rule7.getId(),
                names.namespaceName, actionAttributesOBJECT7);

        // create agendaItems
        buildTestAgendaItemDefinition(names.agendaItem_7_Id, agenda.getId(), rule7.getId());

        AgendaItemDefinition agendaItemOBJECT6 = buildTestAgendaItemDefinition(names.agendaItem_6_Id, agenda.getId(),
                rule6.getId());

        AgendaItemDefinition agendaItemOBJECT5 = buildTestAgendaItemDefinition(names.agendaItem_5_Id, agenda.getId(),
                rule5.getId());

        AgendaItemDefinition agendaItemOBJECT4 = buildTestAgendaItemDefinition(names.agendaItem_4_Id, agenda.getId(),
                rule4.getId());

        AgendaItemDefinition agendaItemOBJECT3 = buildTestAgendaItemDefinition(names.agendaItem_3_Id, agenda.getId(),
                rule3.getId());

        AgendaItemDefinition agendaItemOBJECT2 = buildTestAgendaItemDefinition(names.agendaItem_2_Id, agenda.getId(),
                rule2.getId());

        AgendaItemDefinition.Builder itemBuilderOBJECT2 = AgendaItemDefinition.Builder.create(agendaItemOBJECT2);
        AgendaItemDefinition.Builder itemBuilderOBJECT4 = AgendaItemDefinition.Builder.create(agendaItemOBJECT4);
        itemBuilderOBJECT2.setWhenFalse(itemBuilderOBJECT4);
        itemBuilderOBJECT2.setWhenFalseId(itemBuilderOBJECT4.getId());
        AgendaItemDefinition.Builder itemBuilderOBJECT6 = AgendaItemDefinition.Builder.create(agendaItemOBJECT6);
        itemBuilderOBJECT2.setAlways(itemBuilderOBJECT6);
        itemBuilderOBJECT2.setAlwaysId(itemBuilderOBJECT6.getId());
        itemBuilderOBJECT2 = AgendaItemDefinition.Builder.create(ruleManagementService.createAgendaItem(
                itemBuilderOBJECT2.build()));

        AgendaItemDefinition agendaItemOBJECT1 = buildTestAgendaItemDefinition(names.agendaItem_1_Id, agenda.getId(),
                rule1.getId());
        AgendaItemDefinition.Builder itemBuilderOBJECT1 = AgendaItemDefinition.Builder.create(agendaItemOBJECT1);
        AgendaItemDefinition.Builder itemBuilderOBJECT5 = AgendaItemDefinition.Builder.create(agendaItemOBJECT5);
        itemBuilderOBJECT1.setAlways(itemBuilderOBJECT5);
        itemBuilderOBJECT1.setAlwaysId(itemBuilderOBJECT5.getId());
        itemBuilderOBJECT1 = AgendaItemDefinition.Builder.create(ruleManagementService.createAgendaItem(
                itemBuilderOBJECT1.build()));

        AgendaItemDefinition agendaItemOBJECT0 = buildTestAgendaItemDefinition(names.agendaItem_0_Id, agenda.getId(),
                rule0.getId());
        AgendaItemDefinition.Builder itemBuilderOBJECT0 = AgendaItemDefinition.Builder.create(agendaItemOBJECT0);
        itemBuilderOBJECT1 = AgendaItemDefinition.Builder.create(ruleManagementService.getAgendaItem(itemBuilderOBJECT1.getId()));
        itemBuilderOBJECT0.setWhenTrue(itemBuilderOBJECT1);
        itemBuilderOBJECT0.setWhenTrueId(itemBuilderOBJECT1.getId());
        itemBuilderOBJECT2 = AgendaItemDefinition.Builder.create(ruleManagementService.getAgendaItem(itemBuilderOBJECT2.getId()));
        itemBuilderOBJECT0.setWhenFalse(itemBuilderOBJECT2);
        itemBuilderOBJECT0.setWhenFalseId(itemBuilderOBJECT2.getId());
        AgendaItemDefinition.Builder itemBuilderOBJECT3 = AgendaItemDefinition.Builder.create(agendaItemOBJECT3);
        itemBuilderOBJECT0.setAlways(itemBuilderOBJECT3);
        itemBuilderOBJECT0.setAlwaysId(itemBuilderOBJECT3.getId());
        itemBuilderOBJECT0 = AgendaItemDefinition.Builder.create(ruleManagementService.createAgendaItem(itemBuilderOBJECT0.build()));

        agendaBuilder = AgendaDefinition.Builder.create(ruleManagementService.getAgenda(agenda.getId()));
        agendaBuilder.setFirstItemId(itemBuilderOBJECT0.getId());
        ruleManagementService.updateAgenda(agendaBuilder.build());
        List<AgendaItemDefinition> agendaItems = ruleManagementService.getAgendaItemsByContext(names.contextId);

        assertEquals("Invalid number of agendaItems created", 8, agendaItems.size());

        return AgendaDefinition.Builder.create(ruleManagementService.getAgenda(agenda.getId()));
    }

    /**
     *  createTestReferenceObjectBinding creates a ReferenceObjectBinding entry in the database for testing
     *
     * @param objectDiscriminator
     *
     * @return {@link ReferenceObjectBinding.Builder}
     */
    protected ReferenceObjectBinding.Builder createTestReferenceObjectBinding(String objectDiscriminator) {
        String namespace = "Namespace" + objectDiscriminator;
        AgendaDefinition agendaDefinition = createTestAgenda(objectDiscriminator);
        // create krms type for AgendaOBJECT
        KrmsTypeDefinition krmsTypeDefinition = createKrmsTypeDefinition(null, namespace, "AgendaType" + objectDiscriminator, null);

        return createReferenceObjectBinding("ParkingPolicies", agendaDefinition.getId(), krmsTypeDefinition.getId(),
                namespace, "PA" + objectDiscriminator, "ParkingAffiliationType", true);
    }

    /**
     *   createReferenceObjectBinding create a ReferenceObjectBinding entry in the database for testing
     * @param collectionName
     * @param krmsObjectId
     * @param krmsDiscriminatorType
     * @param namespace
     * @param referenceObjectId
     * @param referenceDiscriminatorType
     * @param active
     *
     * @return {@link ReferenceObjectBinding.Builder}
     */
    protected ReferenceObjectBinding.Builder createReferenceObjectBinding(String collectionName, String krmsObjectId,
            String krmsDiscriminatorType, String namespace, String referenceObjectId, String referenceDiscriminatorType,
            boolean active) {

        ReferenceObjectBinding.Builder refObjBindingBuilder = ReferenceObjectBinding.Builder.
                create(krmsDiscriminatorType, krmsObjectId, namespace, referenceDiscriminatorType,   referenceObjectId);
        refObjBindingBuilder.setCollectionName(collectionName);
        refObjBindingBuilder.setActive(active);

        return  ReferenceObjectBinding.Builder.create(ruleManagementService.createReferenceObjectBinding(
                refObjBindingBuilder.build()));
    }

    /**
     * Used to create a NaturalLanguageTemplate in the database for testing.
     *
     * @param namespace of the KRMS Type which may be created
     * @param langCode  The two character code of applicable language
     * @param nlObjectName  Seed value to create unique Template and Usage records
     * @param template The text of the template for the Template record
     *
     * @return {@link NaturalLanguageTemplate} for the created Template
     */
    protected NaturalLanguageTemplate createTestNaturalLanguageTemplate(String namespace, String langCode,
            String nlObjectName, String template) {
        return createTestNaturalLanguageTemplate(namespace, langCode, nlObjectName, template, "krms.nl." + nlObjectName);
    }

    /**
     * Used to create a NaturalLanguageTemplate in the database for testing.
     *
     * @param namespace of the KRMS Type which may be created
     * @param langCode  The two character code of applicable language
     * @param nlObjectName  Seed value to create unique Template and Usage records
     * @param template The text of the template for the Template record
     * @param nlUsage NaturalLanguateUsageId for the Template record
     *
     * @return {@link NaturalLanguageTemplate} for the created Template
     */
    protected NaturalLanguageTemplate createTestNaturalLanguageTemplate(String namespace, String langCode,
            String nlObjectName, String template, String nlUsage) {
        KrmsTypeDefinition  krmsType = createKrmsTypeDefinition(null, namespace, nlObjectName, null);

        // create NaturalLanguageUsage if it does not exist
        if (ObjectUtils.isNull(ruleManagementService.getNaturalLanguageUsage(nlUsage))) {
            NaturalLanguageUsage.Builder naturalLanguageUsageBuilder =  NaturalLanguageUsage.Builder.create(nlObjectName,namespace );
            naturalLanguageUsageBuilder.setId(nlUsage);
            naturalLanguageUsageBuilder.setDescription("Description-" + nlObjectName);
            naturalLanguageUsageBuilder.setActive(true);
            NaturalLanguageUsage naturalLangumageUsage =  ruleManagementService.createNaturalLanguageUsage(naturalLanguageUsageBuilder.build());
        }

        // create  NaturalLanguageTemplate attributes if they don't exist
        createTestKrmsAttribute(langCode + "_" + nlObjectName + "Attribute1",  nlObjectName + "TemplateAttributeName1", namespace);
        createTestKrmsAttribute(langCode + "_" + nlObjectName + "Attribute2",  nlObjectName + "TemplateAttributeName2", namespace);
        Map<String, String> nlAttributes = new HashMap<String, String>();
        nlAttributes.put( nlObjectName + "TemplateAttributeName1","value1");
        nlAttributes.put( nlObjectName + "TemplateAttributeName2","value2");

        // create NaturalLanguageTemplate
        String naturalLanguageUsageId = nlUsage;
        String typeId = krmsType.getId();
        NaturalLanguageTemplate.Builder naturalLanguageTemplateBuilder = NaturalLanguageTemplate.Builder.create(
                langCode, naturalLanguageUsageId, template, typeId);
        naturalLanguageTemplateBuilder.setActive(true);
        naturalLanguageTemplateBuilder.setId(langCode + "-" + nlObjectName);
        naturalLanguageTemplateBuilder.setAttributes(nlAttributes);

        return ruleManagementService.createNaturalLanguageTemplate(naturalLanguageTemplateBuilder.build());
    }

    /**
     *  createTestKrmsAttribute create KrmsAttribute in the database
     *
     * @param id
     * @param name
     * @param namespace
     *
     * @return {@link KrmsAttributeDefinition}
     */
    protected KrmsAttributeDefinition createTestKrmsAttribute(String id, String name, String namespace) {
        // if the AttributeDefinition does not exist, create it

        if (ObjectUtils.isNull(krmsAttributeDefinitionService.getAttributeDefinitionById(id))) {
            KrmsAttributeDefinition.Builder krmsAttributeDefinitionBuilder = KrmsAttributeDefinition.Builder.create(id, name, namespace);
            KrmsAttributeDefinition  krmsAttributeDefinition = krmsAttributeDefinitionService.createAttributeDefinition(krmsAttributeDefinitionBuilder.build());
        }

        return krmsAttributeDefinitionService.getAttributeDefinitionById(id);
    }

    /**
     * Used to build a NaturalLanguageUsage entry in the database for testing.
     *
     *    The test NaturalLanguageUsage object wll have the form
     *        Namespace    as passed
     *        Name         nlUsageName as passed
     *        Id           "krms.nl." with nlUsageName appended
     *        Description  "Description" with nlUsageName appended
     *        Active       set to true
     *
     *    A krms type entry will be created for the namespace name passed (if it doesn't already exist)
     *
     * @param namespace will be used as namespace name
     * @param nlUsageName is the name of the NaturalLanguageUsage to be create
     *
     * @return {@link NaturalLanguageUsage}
     */
    protected NaturalLanguageUsage buildTestNaturalLanguageUsage(String namespace, String nlUsageName ) {
        KrmsTypeDefinition  krmsType = createKrmsTypeDefinition(null, namespace, nlUsageName, null);

        // create NaturalLanguageUsage
        NaturalLanguageUsage.Builder naturalLanguageUsageBuilder =  NaturalLanguageUsage.Builder.create(nlUsageName,namespace );
        naturalLanguageUsageBuilder.setId("krms.nl." + nlUsageName);
        naturalLanguageUsageBuilder.setDescription("Description-" + nlUsageName);
        naturalLanguageUsageBuilder.setActive(true);

        return  ruleManagementService.createNaturalLanguageUsage(naturalLanguageUsageBuilder.build());
    }

    /**
     * Used to build a ContextDefinition in the database for testing.
     *
     *    The test Context created will have the form ContextId0", "Namespace0", "ContextName0"
     *        where 0 represents the objectDiscriminator passed
     *
     * @param objectDiscriminator is a value use to create a unique test Context
     *
     * @return {@link ContextDefinition} for the created Template
     */
    protected ContextDefinition buildTestContext(String objectDiscriminator) {
        String namespace =  "Namespace" + objectDiscriminator;
        String contextId = "ContextId" + objectDiscriminator;
        String contextName = "ContextName" + objectDiscriminator;

        ContextDefinition.Builder contextDefinitionBuilder = ContextDefinition.Builder.create(namespace, contextName);
        contextDefinitionBuilder.setId(contextId);
        String id = ruleManagementService.createContext(contextDefinitionBuilder.build()).getId();
        ContextDefinition contextDefinition = ruleManagementService.getContext(id);

        return contextDefinition;
    }

    /**
     *  createTestCompoundProposition Create a complex Compound Proposition entry in the database
     // **************************
     // Compound Proposition (True if Account is 54321 and Occasion is either a Conference or Training)
     // ***************************
     //  C1_compound_proposition            "COMPOUND" "S1_simple_proposition"  "S2_simple_proposition"   "&"
     //      S1_simple_proposition          "SIMPLE"   "Account"                "54321"                   "="
     //      S2_simple_proposition          "SIMPLE"   "Occasion"               "Conference"              "="

     * @param t0
     * @return
     */
    protected PropositionDefinition createTestCompoundProposition(RuleManagementBaseTestObjectNames t0){
        // create a child proposition record
        PropositionDefinition propS2 = createTestSimpleProposition(
                t0.namespaceName, "S2", "Occasion", "Conference", "=", "java.lang.String", t0.rule_0_Id, "Special Event");
        PropositionDefinition.Builder propBuilderS2 = PropositionDefinition.Builder.create(propS2);

        // create a child proposition record
        PropositionDefinition propS1 = createTestSimpleProposition(
                t0.namespaceName, "S1", "Account", "54321", "=", "java.lang.String", t0.rule_0_Id, "Charged To Account");
        PropositionDefinition.Builder propBuilderS1 = PropositionDefinition.Builder.create(propS1);

        // create a compound proposition record (referencing the two child records
        KrmsTypeDefinition krmsTypeDefinition = createKrmsTypeDefinition(null, t0.namespaceName, "proposition", "testTypeService");
        PropositionDefinition.Builder propBuilderC1 = PropositionDefinition.Builder.create(
                null,PropositionType.COMPOUND.getCode(),t0.rule_0_Id,null,new ArrayList<PropositionParameter.Builder>());
        propBuilderC1.compoundOpCode(LogicalOperator.AND.getCode());
        List<PropositionDefinition.Builder> compoundComponentsC1 = new ArrayList<PropositionDefinition.Builder>();
        compoundComponentsC1.add(propBuilderS1);
        compoundComponentsC1.add(propBuilderS2);
        propBuilderC1.setCompoundComponents(compoundComponentsC1);
        propBuilderC1.setDescription("C1_compound_proposition");
        propBuilderC1.setTypeId(krmsTypeDefinition.getId());

        return ruleManagementService.createProposition(propBuilderC1.build());
    }

    protected KrmsTypeDefinition createKrmsActionTypeDefinition(String nameSpace) {
        String ACTION_TYPE_NAME = "KrmsActionResolverType";
        KrmsTypeDefinition krmsActionTypeDefinition =  krmsTypeRepository.getTypeByName(nameSpace, ACTION_TYPE_NAME);

        if (krmsActionTypeDefinition == null) {
            KrmsTypeDefinition.Builder krmsActionTypeDefnBuilder = KrmsTypeDefinition.Builder.create(ACTION_TYPE_NAME, nameSpace);
            krmsActionTypeDefnBuilder.setServiceName("testActionTypeService");
            krmsActionTypeDefinition = krmsTypeRepository.createKrmsType(krmsActionTypeDefnBuilder.build());
        }

        return krmsActionTypeDefinition;
    }
}
