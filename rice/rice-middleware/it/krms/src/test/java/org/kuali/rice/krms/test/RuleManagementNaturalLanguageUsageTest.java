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

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.NaturalLanguageTree;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 *   RuleManagementNaturalLanguageUsageTest is to test the methods of
 *       ruleManagementServiceImpl relating toNaturalLanguageUsage
 *
 *   Each test focuses on one of the methods.
 */
public class RuleManagementNaturalLanguageUsageTest extends RuleManagementBaseTest {
    @Override
    @Before
    public void setClassDiscriminator() {
        // set a unique discriminator for test objects of this class
        CLASS_DISCRIMINATOR = "RMNLUT";
    }

    /**
     *  Test testCreateNaturalLanguageUsage()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .createNaturalLanguageUsage(NaturalLanguageUsage) method
     */
    @Test
    public void testCreateNaturalLanguageUsage() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t0 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t0");

        // buildTestNaturalLanguageUsage calls createNaturalLanguageUsage
        NaturalLanguageUsage usage = buildTestNaturalLanguageUsage(t0.namespaceName, t0.object0 );

        // verify created NaturalLanguageUsage
        usage = ruleManagementService.getNaturalLanguageUsage(t0.nlUsage0_Id);
        assertEquals("Unexpected Name returned",t0.nlUsage0_Name,usage.getName());
        assertEquals("Unexpected Namespace returned ", t0.namespaceName, usage.getNamespace());
        assertEquals("Unexpected Description returned", t0.nlUsage0_Descr, usage.getDescription());
        assertTrue("Unexpected Active value returned", usage.isActive());

        // test createNaturalLanguageUsage with null
        try {
            ruleManagementService.createNaturalLanguageUsage(null);
            fail("Should have thrown IllegalArgumentException: naturalLanguageUsage was null");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: naturalLanguageUsage was null
        }
    }


    /**
     *  Test testGetNaturalLanguageUsage()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getNaturalLanguageUsage(String id) method
     */
    @Test
    public void testGetNaturalLanguageUsage() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t1 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t1");

        // create a NaturalLanguageUsage to test with
        NaturalLanguageUsage usage = buildTestNaturalLanguageUsage(t1.namespaceName, t1.object0 );

        // verify correct NaturalLanguageUsage returned
        assertEquals("Unexpected Description returned",
                t1.nlUsage0_Descr, ruleManagementService.getNaturalLanguageUsage(t1.nlUsage0_Id).getDescription());

        // test getNaturalLanguageUsage with null
        try {
            ruleManagementService.getNaturalLanguageUsage(null);
            fail("Should have thrown IllegalArgumentException: naturalLanguageUsageId was null");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: naturalLanguageUsageId was null
        }

        // test getNaturalLanguageUsage with blank
        try {
            ruleManagementService.getNaturalLanguageUsage("   ");
            fail("Should have thrown IllegalArgumentException: naturalLanguageUsageId was blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: naturalLanguageUsageId was blank
        }

        // test getNaturalLanguageUsage wih non-existent Id
        assertNull("Should not have return object", ruleManagementService.getNaturalLanguageUsage("badValue"));
    }


    /**
     *  Test testUpdateNaturalLanguageUsage()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .updateNaturalLanguageUsage(NaturalLanguageUsage) method
     */
    @Test
    public void testUpdateNaturalLanguageUsage() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t2 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t2");

        // create NaturalLanguageUsage to test with
        NaturalLanguageUsage usage = buildTestNaturalLanguageUsage(t2.namespaceName, t2.object0 );

        // verify created NaturalLanguageUsage
        usage = ruleManagementService.getNaturalLanguageUsage(t2.nlUsage0_Id);
        assertEquals("Unexpected Name returned",t2.nlUsage0_Name,usage.getName());
        assertEquals("Unexpected Namespace returned ", t2.namespaceName, usage.getNamespace());
        assertEquals("Unexpected Description returned", t2.nlUsage0_Descr, usage.getDescription());
        assertTrue("Unexpected Active value returned", usage.isActive());

        // Update Namespace, Name, Description and Active values
        NaturalLanguageUsage.Builder usageBuilder =  NaturalLanguageUsage.Builder.create(usage);
        usageBuilder.setNamespace(t2.namespaceName + "Changed");
        usageBuilder.setName(t2.nlUsage0_Name + "Changed");
        usageBuilder.setDescription(t2.nlUsage0_Descr + "Changed");
        usageBuilder.setActive(false);
        ruleManagementService.updateNaturalLanguageUsage(usageBuilder.build());

        // verify updated NaturalLanguageUsage values
        usage = ruleManagementService.getNaturalLanguageUsage(t2.nlUsage0_Id);
        assertEquals("Unexpected Name returned",t2.nlUsage0_Name + "Changed",usage.getName());
        assertEquals("Unexpected Namespace returned ", t2.namespaceName + "Changed", usage.getNamespace());
        assertEquals("Unexpected Description returned", t2.nlUsage0_Descr + "Changed", usage.getDescription());
        assertFalse("Unexpected Active value returned", usage.isActive());

        // test updateNaturalLanguageUsage with null
        try {
            ruleManagementService.updateNaturalLanguageUsage(null);
            fail("Should have thrown IllegalArgumentException: naturalLanguageUsage was null");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: naturalLanguageUsage was null
        }
    }


    /**
     *  Test testDeleteNaturalLanguageUsage()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .deleteNaturalLanguageUsage(String naturalLanguageUsageId) method
     */
    @Test
    public void testDeleteNaturalLanguageUsage() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t3 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t3");

        // create a NaturalLanguageUsage to test with
        NaturalLanguageUsage usage = buildTestNaturalLanguageUsage(t3.namespaceName, t3.object0 );

        // verify correct NaturalLanguageUsage exists
        assertEquals("Unexpected Description returned",
                t3.nlUsage0_Descr, ruleManagementService.getNaturalLanguageUsage(t3.nlUsage0_Id).getDescription());

        // test deleteNaturalLanguageUsage
        ruleManagementService.deleteNaturalLanguageUsage(t3.nlUsage0_Id);

        // verify object deleted
        assertNull("Should not have returned deleted entry", ruleManagementService.getNaturalLanguageUsage(t3.nlUsage0_Id));

        // test deleteNaturalLanguageUsage with null
        try {
            ruleManagementService.deleteNaturalLanguageUsage(null);
            fail("Should have thrown IllegalArgumentException: naturalLanguageUsageId was null");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: naturalLanguageUsageId was null
        }

        // test deleteNaturalLanguageUsage with blank
        try {
            ruleManagementService.deleteNaturalLanguageUsage("   ");
            fail("Should have thrown IllegalArgumentException: naturalLanguageUsageId was blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: naturalLanguageUsageId was blank
        }

        // test deleteNaturalLanguageUsage with badValue
        try {
            ruleManagementService.deleteNaturalLanguageUsage("badValue");
            fail("Should have thrown IllegalStateException: the NaturalLanguageUsage to delete does not exists: badValue");
        } catch (IllegalStateException e) {
            // throws IllegalStateException: the NaturalLanguageUsage to delete does not exists: badValue
        }
    }


    /**
     *  Test testGetNaturalLanguageUsagesByNamespace()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .getNaturalLanguageUsagesByNamespace(String namespace) method
     */
    @Test
    public void testGetNaturalLanguageUsagesByNamespace() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t4 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t4");

        // create three NaturalLanguageUsage entries with same Namespace to test with
        buildTestNaturalLanguageUsage(t4.namespaceName, t4.object0 );
        buildTestNaturalLanguageUsage(t4.namespaceName, t4.object1 );
        buildTestNaturalLanguageUsage(t4.namespaceName, t4.object2 );
        List<String> usageIds = Arrays.asList(t4.nlUsage0_Id, t4.nlUsage1_Id, t4.nlUsage2_Id);

        // test getNaturalLanguageUsagesByNamespace
        List<NaturalLanguageUsage> usages = ruleManagementService.getNaturalLanguageUsagesByNamespace(t4.namespaceName);

        assertEquals("Should have returned 3 entries",3,usages.size());
        for (NaturalLanguageUsage usage : usages) {
            assertTrue("Unexpected entry id returned", usageIds.contains(usage.getId()));
        }

        // test getNaturalLanguageUsagesByNamespace with null
        try {
            ruleManagementService.getNaturalLanguageUsagesByNamespace(null);
            fail("Should have thrown IllegalArgumentException: namespace is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: namespace is null or blank
        }

        // test getNaturalLanguageUsagesByNamespace with blank
        try {
            ruleManagementService.getNaturalLanguageUsagesByNamespace("  ");
            fail("Should have thrown IllegalArgumentException: namespace is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: namespace is null or blank
        }

        // test getNaturalLanguageUsagesByNamespace with non-existent namespace value
        assertEquals("Should have returned 0 entries", 0, ruleManagementService.getNaturalLanguageUsagesByNamespace(
                "badValue").size());
    }


    /**
     *  Test testGetNaturalLanguageUsageByNameAndNamespace()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .getNaturalLanguageUsageByNameAndNamespace(String name, String namespace) method
     */
    @Test
    public void testGetNaturalLanguageUsageByNameAndNamespace() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t5 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t5");

        // create two NaturalLanguageUsage entries to test with
        buildTestNaturalLanguageUsage(t5.namespaceName, t5.object0 );
        buildTestNaturalLanguageUsage(t5.namespaceName, t5.object1 );

        assertEquals("Unexpected Description on entry returned",
             t5.nlUsage0_Descr, ruleManagementService.getNaturalLanguageUsageByNameAndNamespace(
                   t5.nlUsage0_Name,t5.namespaceName).getDescription());

        // test getNaturalLanguageUsageByNameAndNamespace with null name
        try {
            ruleManagementService.getNaturalLanguageUsageByNameAndNamespace(null, t5.namespaceName);
            fail("Should have thrown RiceIllegalArgumentException: name was a null or blank value");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: name was a null or blank value
        }

        // test getNaturalLanguageUsageByNameAndNamespace with blank name
        try {
            ruleManagementService.getNaturalLanguageUsageByNameAndNamespace("  ",t5.namespaceName);
            fail("Should have thrown RiceIllegalArgumentException: name was a null or blank value");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: name was a null or blank value
        }

        // test getNaturalLanguageUsageByNameAndNamespace with non-existent name value
        assertNull("Should not have return object",
                ruleManagementService.getNaturalLanguageUsageByNameAndNamespace("badValue", t5.namespaceName));

        // test getNaturalLanguageUsageByNameAndNamespace with null namespace
        try {
            ruleManagementService.getNaturalLanguageUsageByNameAndNamespace(t5.nlUsage0_Name,null);
            fail("Should have thrown RiceIllegalArgumentException: namespace was a null or blank value");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: namespace was a null or blank value
        }

        // test getNaturalLanguageUsageByNameAndNamespace with blank namespace
        try {
            ruleManagementService.getNaturalLanguageUsageByNameAndNamespace(t5.nlUsage0_Name,"  ");
            fail("Should have thrown RiceIllegalArgumentException: namespace was a null or blank value");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: namespace was a null or blank value
        }

        // test getNaturalLanguageUsageByNameAndNamespace with non-existent namespace value
        assertNull("Should not have return object",
                ruleManagementService.getNaturalLanguageUsageByNameAndNamespace(t5.nlUsage0_Name,"badValue"));
    }


    ////
    //// natural language translations
    ////
    /**
     *  Test testTranslateNaturalLanguageForObject()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .translateNaturalLanguageForObject(String naturalLanguageUsageId,
     *                                         String typeId,
     *                                         String krmsObjectId,
     *                                         String languageCode)  method
     */
    @Test
    public void testTranslateNaturalLanguageForObject() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t6 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t6");

        // build proposition and NaturalLanguageTemplate for testing
        PropositionDefinition propositionDefinition = createTestPropositionForTranslation(t6.object0, t6.namespaceName,
                "proposition");
        NaturalLanguageTemplate template = createTestNaturalLanguageTemplate(t6.namespaceName, "sw", "proposition",
                "Detta ändamål får inte vara inaktiv");

        // test translateNaturalLanguageForObject call
        String translation = ruleManagementService.translateNaturalLanguageForObject(
                template.getNaturalLanguageUsageId(),"proposition",propositionDefinition.getId(),"sw");

        // check result of test
        assertEquals("Unexpected translation returned","Detta ändamål får inte vara inaktiv. ",translation);

        //test with null NaturalLanguageUsageId
        assertEquals("Should have returned '. ' String", ". ",
                ruleManagementService.translateNaturalLanguageForObject(null, "proposition",
                        propositionDefinition.getId(), "sw"));

        // test with null typeId
        try {
            ruleManagementService.translateNaturalLanguageForObject(
                    template.getNaturalLanguageUsageId(),null,propositionDefinition.getId(),"sw");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // throws NullPointerException
        }

        // test with null krmsObjectId
        try {
            ruleManagementService.translateNaturalLanguageForObject(
                    template.getNaturalLanguageUsageId(),"proposition",null,"sw");
            fail("Should have thrown RiceIllegalArgumentException: Proposition id must not be null or blank");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: Proposition id must not be null or blank
        }

        // test with null languageCode
        try {
            ruleManagementService.translateNaturalLanguageForObject(
                    template.getNaturalLanguageUsageId(),"proposition",propositionDefinition.getId(),null);
            fail("Should have thrown IllegalArgumentException: languageCode is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: languageCode is null or blank
        }

        // test with blank NaturalLanguageUsageId
        assertEquals("Should have returned '. ' String", ". ", ruleManagementService.translateNaturalLanguageForObject(
                    "   ","proposition",propositionDefinition.getId(),"sw"));


        // test with blank typeId
        assertEquals("Should have returned empty String", StringUtils.EMPTY,
                ruleManagementService.translateNaturalLanguageForObject(template.getNaturalLanguageUsageId(),
                        "    ", propositionDefinition.getId(), "sw"));


        // test with blank krmsObjectId
        try {
            ruleManagementService.translateNaturalLanguageForObject(
                    template.getNaturalLanguageUsageId(),"proposition","    ","sw");
            fail("Should have thrown RiceIllegalArgumentException: Proposition id must not be null or blank");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: Proposition id must not be null or blank
        }

        // test with blank languageCode
        try {
            ruleManagementService.translateNaturalLanguageForObject(
                    template.getNaturalLanguageUsageId(),"proposition",propositionDefinition.getId(),"  ");
            fail("Should have thrown IllegalArgumentException: languageCode is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: languageCode is null or blank
        }

        //test with non-existent NaturalLanguageUsageId
        assertEquals("Should have returned '. ' String", ". ",
                ruleManagementService.translateNaturalLanguageForObject("badValue", "proposition",
                        propositionDefinition.getId(), "sw"));

        // test with non-existent typeId
        assertEquals("Should have returned empty String", StringUtils.EMPTY,
                ruleManagementService.translateNaturalLanguageForObject(template.getNaturalLanguageUsageId(),
                        "badValue", propositionDefinition.getId(), "sw"));

        // test with non-existent krmsObjectId
        try {
            ruleManagementService.translateNaturalLanguageForObject(
                    template.getNaturalLanguageUsageId(),"proposition","badValue","sw");
            fail("Should have thrown RiceIllegalArgumentException: badValue is not an Id for a proposition");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: badValue is not an Id for a proposition
        }

        // test with non-existent languageCode
        assertEquals("Should have returned '. ' String", ". ", ruleManagementService.translateNaturalLanguageForObject(
                template.getNaturalLanguageUsageId(),"proposition",propositionDefinition.getId(),"xx"));
    }


    /**
     *  Test testTranslateNaturalLanguageForProposition()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .translateNaturalLanguageForProposition(String naturalLanguageUsageId,
     *                                              PropositionDefinition proposition,
     *                                              String languageCode) method
     */
    @Test
    public void testTranslateNaturalLanguageForProposition() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t7 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t7");

        PropositionDefinition propositionDefinition = createTestPropositionForTranslation(t7.object0, t7.namespaceName, "proposition" );
        NaturalLanguageTemplate template = createTestNaturalLanguageTemplate(t7.namespaceName, "tr", "proposition",
                "Bu nesne inaktif olmamalidir");

        String translation = ruleManagementService.translateNaturalLanguageForProposition(template.getNaturalLanguageUsageId(),propositionDefinition,"tr");

        assertEquals("Unexpected translation returned","Bu nesne inaktif olmamalidir. ",translation);

        // test with null naturalLanguageUsageId
        assertEquals("Should have returned '. ' String",". ", ruleManagementService.translateNaturalLanguageForProposition(null, propositionDefinition, "tr"));

        // test with null PropositionDefinition
        try {
            ruleManagementService.translateNaturalLanguageForProposition(
                    template.getNaturalLanguageUsageId(), null, "tr");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // throws NullPointerException
        }

            // test with null languageCode
        try {
            ruleManagementService.translateNaturalLanguageForProposition(template.getNaturalLanguageUsageId(),
                    propositionDefinition, null);
            fail("Should have thrown IllegalArgumentException: languageCode is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: languageCode is null or blank
        }

        // test with blank naturalLanguageUsageId
        assertEquals("Should have returned '. ' String", ". ",
                ruleManagementService.translateNaturalLanguageForProposition("    ", propositionDefinition, "tr"));

        // test with blank languageCode
        try {
            ruleManagementService.translateNaturalLanguageForProposition(template.getNaturalLanguageUsageId(),propositionDefinition,"    ");
            fail("Should have thrown IllegalArgumentException: languageCode is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: languageCode is null or blank
        }

        // test with non-existent naturalLanguageUsageId
        assertEquals("Should have returned '. ' String",". ",
                ruleManagementService.translateNaturalLanguageForProposition("badValue",propositionDefinition,"tr"));

        // test with non-existent languageCode
        assertEquals("Should have returned '. ' String",". ",
                ruleManagementService.translateNaturalLanguageForProposition(
                template.getNaturalLanguageUsageId(),propositionDefinition,"badValue"));
    }


    /**
     *  Test testTranslateNaturalLanguageTreeForProposition()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .translateNaturalLanguageTreeForProposition(String naturalLanguageUsageId,
     *                                                  PropositionDefinition propositionDefinintion,
     *                                                  String languageCode) method
     */
    @Test
    public void testTranslateNaturalLanguageTreeForProposition() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t8 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t8");

        // build SIMPLE proposition
        PropositionDefinition propositionDefinition = createTestPropositionForTranslation(t8.object0, t8.namespaceName, "proposition" );
        NaturalLanguageTemplate template = createTestNaturalLanguageTemplate(t8.namespaceName, "cy", "proposition",
                "Ni ddylai hyn fod yn segur, Gwrthrych");

        NaturalLanguageTree naturalLanguageTree = ruleManagementService.translateNaturalLanguageTreeForProposition(
                template.getNaturalLanguageUsageId(), propositionDefinition, "cy");

        String translation = naturalLanguageTree.getNaturalLanguage();
        assertEquals("Unexpected translation returned","Ni ddylai hyn fod yn segur, Gwrthrych",translation);

        // SIMPLE proposition should not have children
        assertNull("Should have returned null",naturalLanguageTree.getChildren());
    }

    /**
     *  Test testCompoundTranslateNaturalLanguageTreeForProposition()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .translateNaturalLanguageTreeForProposition(String naturalLanguageUsageId,
     *                                                  PropositionDefinition propositionDefinintion,
     *                                                  String languageCode) method
     *      where the PropositionDefinition is a Compound proposition
     */
    @Test
    public void testCompoundTranslateNaturalLanguageTreeForProposition() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t9 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t9");

        // Build COMPOUND proposition to test with
        PropositionDefinition propositionDefinition = createTestCompoundProposition(t9);

        // Build the templates for the tree
        NaturalLanguageTemplate template = createTestNaturalLanguageTemplate(t9.namespaceName, "ms",
                krmsTypeRepository.getTypeById(propositionDefinition.getTypeId()).getName(),
                "Objek ini tidak boleh aktif");
        PropositionDefinition child1 = propositionDefinition.getCompoundComponents().get(0);
        createTestNaturalLanguageTemplate(t9.namespaceName, "ms", "Account", "Objek ini tidak boleh aktif",
                "krms.nl.proposition");
        PropositionDefinition child2 = propositionDefinition.getCompoundComponents().get(1);
        createTestNaturalLanguageTemplate(t9.namespaceName, "ms", "Occasion", "Objek ini tidak boleh aktif",
                "krms.nl.proposition");

        // test the call to translateNaturalLanguageTreeForProposition
        NaturalLanguageTree naturalLanguageTree = ruleManagementService.translateNaturalLanguageTreeForProposition(
                template.getNaturalLanguageUsageId(), propositionDefinition, "ms");
        List<NaturalLanguageTree> naturalLanguageTrees = naturalLanguageTree.getChildren();
        assertEquals("Should have found 2 child entries",2,naturalLanguageTrees.size());

        // test with null PropositionDefinition
        try {
            ruleManagementService.translateNaturalLanguageTreeForProposition(
                    template.getNaturalLanguageUsageId(), null, "ms");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // throws NullPointerException
        }

        // test with a null LanguageCode;
        try {
            ruleManagementService.translateNaturalLanguageTreeForProposition(
                    template.getNaturalLanguageUsageId(), propositionDefinition, null);
            fail("Should have thrown IllegalArgumentException: languageCode is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: languageCode is null or blank
        }
    }


    /**
     *  Test testNullTranslateNaturalLanguageTreeForProposition()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .translateNaturalLanguageTreeForProposition(String naturalLanguageUsageId,
     *                                                  PropositionDefinition propositionDefinintion,
     *                                                  String languageCode) method
     *      where there is no NaturalLanguageTemplate
     */
    @Test
    public void testNullTranslateNaturalLanguageTreeForProposition() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t9 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t9");

        // build SIMPLE proposition
        PropositionDefinition propositionDefinition = createTestPropositionForTranslation(t9.object0, t9.namespaceName, "proposition" );

        NaturalLanguageTree naturalLanguageTree = ruleManagementService.translateNaturalLanguageTreeForProposition(
                null, propositionDefinition, "cy2");

        String translation = naturalLanguageTree.getNaturalLanguage();
        assertEquals("Non-empty translation returned",StringUtils.EMPTY,translation);

        // SIMPLE proposition should not have children
        assertNull("Should have returned null",naturalLanguageTree.getChildren());
    }

    /**
     *  Test testEmptyTranslateNaturalLanguageTreeForProposition()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .translateNaturalLanguageTreeForProposition(String naturalLanguageUsageId,
     *                                                  PropositionDefinition propositionDefinintion,
     *                                                  String languageCode) method
     *      where there is no NaturalLanguageTemplate associated with the PropositionDefinition and languageCode
     */
    @Test
    public void testEmptyTranslateNaturalLanguageTreeForProposition() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t10 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t10");

        // build SIMPLE proposition
        PropositionDefinition propositionDefinition = createTestPropositionForTranslation(t10.object0, t10.namespaceName, "proposition2" );
        NaturalLanguageTemplate template = createTestNaturalLanguageTemplate(t10.namespaceName, "cy", "proposition2",
                "Ddylai hyn fod yn segur, Gwrthrych");

        NaturalLanguageTree naturalLanguageTree = ruleManagementService.translateNaturalLanguageTreeForProposition(
                template.getNaturalLanguageUsageId(), propositionDefinition, "en");

        String translation = naturalLanguageTree.getNaturalLanguage();
        assertEquals("Non-empty translation returned",StringUtils.EMPTY,translation);

        // SIMPLE proposition should not have children
        assertNull("Should have returned null",naturalLanguageTree.getChildren());
    }
}
