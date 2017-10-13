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

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;
import static org.kuali.rice.core.api.criteria.PredicateFactory.in;

/**
 *   RuleManagementReferenceObjectBindingTest is to test the methods of ruleManagementServiceImpl relating to ReferenceObjectBindings
 *
 *   Each test focuses on one of the methods.
 */
public class RuleManagementReferenceObjectBindingTest extends RuleManagementBaseTest {
    @Override
    @Before
    public void setClassDiscriminator() {
        // set a unique discriminator for test objects of this class
        CLASS_DISCRIMINATOR = "RMROBT";
    }

    /**
     *  Test testCreateReferenceObjectBinding()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .createTestReferenceObjectBinding(ReferenceObjectBinding) method
     */
    @Test
    public void testCreateReferenceObjectBinding() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t0 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t0");

        // create a ReferenceObjectBinding entry
        ReferenceObjectBinding.Builder refObjBindingBuilder =  createTestReferenceObjectBinding(t0.object0);
        ReferenceObjectBinding refObjBinding = ruleManagementService.getReferenceObjectBinding(
                refObjBindingBuilder.getId());
        refObjBindingBuilder =  ReferenceObjectBinding.Builder.create(refObjBinding);

        // verify the Created entry
        assertNotNull("Created ReferenceObjectBinding not found", refObjBindingBuilder);
        assertEquals("Invalid CollectionName of refObjBindingBuilder found", "ParkingPolicies",
                refObjBindingBuilder.getCollectionName());
        assertEquals("Invalid KrmsObjectId of refObjBindingBuilder found",t0.agenda_Id, refObjBindingBuilder.getKrmsObjectId());
        assertEquals("Invalid KrmsDiscriminatorType of refObjBindingBuilder found",
                krmsTypeRepository.getTypeByName(t0.namespaceName, "AgendaType" + t0.object0).getId(), refObjBindingBuilder.getKrmsDiscriminatorType() );
        assertEquals("Invalid Namespace of refObjBindingBuilder found",t0.namespaceName, refObjBindingBuilder.getNamespace());
        assertEquals("Invalid ReferenceObjectId of refObjBindingBuilder found",t0.referenceObject_0_Id, refObjBindingBuilder.getReferenceObjectId());
        assertEquals("Invalid ReferenceDiscriminatorType of refObjBindingBuilder found",
                t0.referenceObject_0_DiscriminatorType, refObjBindingBuilder.getReferenceDiscriminatorType());
        assertEquals("Invalid Active value of refObjBindingBuilder found",true, refObjBindingBuilder.isActive());

        // try to create a ReferenceObjectBinding which already exists
        try {
            ruleManagementService.createReferenceObjectBinding(refObjBindingBuilder.build());
            fail("Should have thrown IllegalStateException: the ReferenceObjectBinding to create already exists");
        } catch (Exception e) {
            // throws IllegalStateException: the ReferenceObjectBinding to create already exists
        }

        // try to create a ReferenceObjectBinding entry which exists using a different Id
        refObjBindingBuilder.setId("RefObjBindIdChanged");
        try {
            ruleManagementService.createReferenceObjectBinding(refObjBindingBuilder.build());
            fail("Should have thrown DataIntegrityViolationException: OJB operation; SQL []; Duplicate entry");
        } catch (Exception e) {
            // throws DataIntegrityViolationException: OJB operation; SQL []; Duplicate entry
        }
    }

    /**
     *  Test testGetReferenceObjectBinding()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getReferenceObjectBinding("ReferenceObjectId") method
     */
    @Test
    public void testGetReferenceObjectBinding() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t1 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t1");

        ReferenceObjectBinding.Builder refObjBindingBuilder =  createTestReferenceObjectBinding(t1.object0);

        ReferenceObjectBinding refObjBinding = ruleManagementService.getReferenceObjectBinding(
                refObjBindingBuilder.getId());
        assertNotNull("ReferenceObjectBinding should have been returned",refObjBinding);
        assertEquals("Incorrect value found on returned ReferenceObjectBinding",t1.referenceObject_0_Id,refObjBinding.getReferenceObjectId());

        // try to getReferenceObjectBinding using null Id
        try {
            refObjBinding = ruleManagementService.getReferenceObjectBinding(null);
            fail("Should have thrown IllegalArgumentException: referenceObjectBindingId was null");
        } catch (Exception e) {
            // throws IllegalArgumentException: referenceObjectBindingId was null
        }

        // try to find a ReferenceObjectBinding using a non-existent Id
        assertNull("ReferenceObjectBinding should not have been found", ruleManagementService.getReferenceObjectBinding("junk_value"));
    }

    /**
     *  Test testGetReferenceObjectBindings()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .getReferenceObjectBindings(List<ReferenceObjectBinding>) method
     */
    @Test
    public void testGetReferenceObjectBindings() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t2 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t2");

        List<String> referenceObjectBindingIds = new ArrayList<String>();
        referenceObjectBindingIds.add(createTestReferenceObjectBinding(t2.object0).getId());
        referenceObjectBindingIds.add(createTestReferenceObjectBinding(t2.object1).getId());
        referenceObjectBindingIds.add(createTestReferenceObjectBinding(t2.object2).getId());

        List<ReferenceObjectBinding> referenceObjectBindings = ruleManagementService.getReferenceObjectBindings(referenceObjectBindingIds);
        int objectsFound = 0;
        for ( ReferenceObjectBinding referenceObjectBinding : referenceObjectBindings) {
            if ( referenceObjectBindingIds.contains( referenceObjectBinding.getId())) {
                objectsFound++;
            } else {
                fail("Unexpected object returned");
            }
        }
        assertEquals("Expected number of objects not returned",3,objectsFound);

        // try to getReferenceObjectBindings using a null Id List
        try {
            ruleManagementService.getReferenceObjectBindings(null);
            fail("Should have thrown IllegalArgumentException: reference binding object ids must not be null");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: reference binding object ids must not be null
        }

        assertEquals("Incorrect number of objects returned", 0, ruleManagementService.getReferenceObjectBindings(new ArrayList<String>()).size());

        // try requesting a list of objects with a bad value in it
        referenceObjectBindingIds.add("junkValue");
        referenceObjectBindings = ruleManagementService.getReferenceObjectBindings(referenceObjectBindingIds);
        objectsFound = 0;
        for ( ReferenceObjectBinding referenceObjectBinding : referenceObjectBindings) {
            if ( referenceObjectBindingIds.contains( referenceObjectBinding.getId())) {
                objectsFound++;
            } else {
                fail("Unexpected object returned");
            }
        }
        assertEquals("Expected number of objects not returned",3,objectsFound);
    }

    /**
     *  Test testFindReferenceObjectBindingsByReferenceObject()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .findReferenceObjectBindingsByReferenceObject("ReferenceObjectId") method
     */
    @Test
    public void testFindReferenceObjectBindingsByReferenceObject() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t3 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t3");
        ReferenceObjectBinding.Builder refObjBindingBuilder =  createTestReferenceObjectBinding(t3.object0);

        //assertEquals("",refObjBindingBuilder.getId(),
        List<ReferenceObjectBinding> referenceObjectBindings = ruleManagementService.findReferenceObjectBindingsByReferenceObject(
                refObjBindingBuilder.getReferenceDiscriminatorType(), refObjBindingBuilder.getReferenceObjectId());
        assertEquals("Incorrect number of objects returned",1,referenceObjectBindings.size());

        // test findReferenceObjectBindingsByReferenceObject with null object discriminator
        try {
            ruleManagementService.findReferenceObjectBindingsByReferenceObject(null,refObjBindingBuilder.getReferenceObjectId());
            fail("should have thrown RiceIllegalArgumentException: reference binding object discriminator type must not be null");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: reference binding object discriminator type must not be null
        }

        // test findReferenceObjectBindingsByReferenceObject with null reference object id
        try {
            ruleManagementService.findReferenceObjectBindingsByReferenceObject(refObjBindingBuilder.getReferenceDiscriminatorType(),null);
            fail("should have thrown RiceIllegalArgumentException: reference object id must not be null");
        } catch (RiceIllegalArgumentException e) {
            // throws RiceIllegalArgumentException: reference object id must not be null
        }

        // test with bad values for object discriminator and object id
        referenceObjectBindings = ruleManagementService.findReferenceObjectBindingsByReferenceObject("junkvalue","junkvalue");
        assertEquals("Incorrect number of objects returned",0,referenceObjectBindings.size());
    }

    /**
     *  Test testFindReferenceObjectBindingsByReferenceDiscriminatorType()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .findReferenceObjectBindingsByReferenceDiscriminatorType("ReferenceDiscriminatorType") method
     */
    @Test
    public void testFindReferenceObjectBindingsByReferenceDiscriminatorType() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t4 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t4");

        // create two ReferenceObjectBindings with same ReferenceDiscriminatorType
        ReferenceObjectBinding.Builder refObjBindingBuilder =  createTestReferenceObjectBinding(t4.object0);
        refObjBindingBuilder.setReferenceDiscriminatorType("ParkingAffiliationType" + t4.discriminator);
        ruleManagementService.updateReferenceObjectBinding(refObjBindingBuilder.build());

        refObjBindingBuilder =  createTestReferenceObjectBinding(t4.object1);
        refObjBindingBuilder.setReferenceDiscriminatorType("ParkingAffiliationType" + t4.discriminator);
        ruleManagementService.updateReferenceObjectBinding(refObjBindingBuilder.build());

        List<ReferenceObjectBinding> referenceObjectBindings = ruleManagementService.findReferenceObjectBindingsByReferenceDiscriminatorType(
                refObjBindingBuilder.getReferenceDiscriminatorType());
        assertEquals("Incorrect number of objects returned",2,referenceObjectBindings.size());

        // check with blank ReferenceDiscriminatorType
        try {
            ruleManagementService.findReferenceObjectBindingsByReferenceDiscriminatorType("   ");
            fail("Should have thrown IllegalArgumentException: referenceDiscriminatorType is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: referenceDiscriminatorType is null or blank
        }

        // check with null ReferenceDiscriminatorType
        try {
            ruleManagementService.findReferenceObjectBindingsByReferenceDiscriminatorType(null);
            fail("Should have thrown IllegalArgumentException: referenceDiscriminatorType is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: referenceDiscriminatorType is null or blank
        }
    }

    /**
     *  Test testFindReferenceObjectBindingsByKrmsDiscriminatorType()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .findReferenceObjectBindingsByKrmsDiscriminatorType("KrmsDiscriminatorType") method
     */
    @Test
    public void testFindReferenceObjectBindingsByKrmsDiscriminatorType() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t5 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t5");

        // create two ReferenceObjectBindings with same KrmsDiscriminatorType
        ReferenceObjectBinding.Builder refObjBindingBuilder6008 =  createTestReferenceObjectBinding(t5.object0);

        ReferenceObjectBinding.Builder refObjBindingBuilder6009 =  createTestReferenceObjectBinding(t5.object1);
        refObjBindingBuilder6009.setKrmsDiscriminatorType(refObjBindingBuilder6008.getKrmsDiscriminatorType());
        ruleManagementService.updateReferenceObjectBinding(refObjBindingBuilder6009.build());

        List<ReferenceObjectBinding> referenceObjectBindings = ruleManagementService.findReferenceObjectBindingsByKrmsDiscriminatorType(refObjBindingBuilder6008.getKrmsDiscriminatorType());
        assertEquals("Incorrect number of objects returned",2,referenceObjectBindings.size());

        // check with blank KrmsDiscriminatorType
        try {
            ruleManagementService.findReferenceObjectBindingsByKrmsDiscriminatorType("   ");
            fail("Should have thrown IllegalArgumentException: krmsDiscriminatorType is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: krmsDiscriminatorType is null or blank
        }

        // check with null KrmsDiscriminatorType
        try {
            ruleManagementService.findReferenceObjectBindingsByKrmsDiscriminatorType(null);
            fail("Should have thrown IllegalArgumentException: krmsDiscriminatorType is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: krmsDiscriminatorType is null or blank
        }
    }

    /**
     *  Test testFindReferenceObjectBindingsByKrmsObject()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *      .findReferenceObjectBindingsByKrmsObject("KrmsObjectId") method
     */
    @Test
    public void testFindReferenceObjectBindingsByKrmsObject() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t6 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t6");

        // create two ReferenceObjectBindings with same KrmsObjectId
        ReferenceObjectBinding.Builder refObjBindingBuilder0 =  createTestReferenceObjectBinding(t6.object0);

        ReferenceObjectBinding.Builder refObjBindingBuilder1 =  createTestReferenceObjectBinding(t6.object1);
        refObjBindingBuilder1.setKrmsObjectId(refObjBindingBuilder0.getKrmsObjectId());
        ruleManagementService.updateReferenceObjectBinding(refObjBindingBuilder1.build());

        List<ReferenceObjectBinding> referenceObjectBindings = ruleManagementService.findReferenceObjectBindingsByKrmsObject(
                refObjBindingBuilder0.getKrmsObjectId());
        assertEquals("Incorrect number of objects returned",2,referenceObjectBindings.size());

        // check with blank KrmsObjectId
        try {
            ruleManagementService.findReferenceObjectBindingsByKrmsObject("   ");
            fail("Should have thrown IllegalArgumentException: krmsObjectId is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: krmsObjectId is null or blank
        }

        // check with null KrmsObjectId
        try {
            ruleManagementService.findReferenceObjectBindingsByKrmsObject(null);
            fail("Should have thrown IllegalArgumentException: krmsObjectId is null or blank");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: krmsObjectId is null or blank
        }
    }

    /**
     *  Test testUpdateReferenceObjectBinding()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl .updateReferenceObjectBinding(ReferenceObjectBinding) method
     */
    @Test
    public void testUpdateReferenceObjectBinding() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t7 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t7");

        ReferenceObjectBinding.Builder refObjBindingBuilder =  createTestReferenceObjectBinding(t7.object0);

        ReferenceObjectBinding refObjBinding = ruleManagementService.getReferenceObjectBinding(refObjBindingBuilder.getId());
        refObjBindingBuilder =  ReferenceObjectBinding.Builder.create(refObjBinding);

        // verify all current values
        assertNotNull("Created ReferenceObjectBinding not found", refObjBindingBuilder);
        assertEquals("Invalid CollectionName of refObjBindingBuilder found","ParkingPolicies", refObjBindingBuilder.getCollectionName());
        assertEquals("Invalid KrmsObjectId of refObjBindingBuilder found",t7.agenda_Id, refObjBindingBuilder.getKrmsObjectId());
        assertEquals("Invalid KrmsDiscriminatorType of refObjBindingBuilder found",
                krmsTypeRepository.getTypeByName(t7.namespaceName, "AgendaType"+t7.object0).getId(),
                refObjBindingBuilder.getKrmsDiscriminatorType() );
        assertEquals("Invalid Namespace of refObjBindingBuilder found",t7.namespaceName, refObjBindingBuilder.getNamespace());
        assertEquals("Invalid ReferenceObjectId of refObjBindingBuilder found",t7.referenceObject_0_Id, refObjBindingBuilder.getReferenceObjectId());
        assertEquals("Invalid ReferenceDiscriminatorType  of refObjBindingBuilder found","ParkingAffiliationType",
                refObjBindingBuilder.getReferenceDiscriminatorType());
        assertEquals("Invalid Active value of refObjBindingBuilder found",true, refObjBindingBuilder.isActive());

        // change everything but the id and submit update
        refObjBindingBuilder.setCollectionName("ParkingPolicies6Changed");
        refObjBindingBuilder.setKrmsObjectId("AgendaId6Changed");
        refObjBindingBuilder.setKrmsDiscriminatorType("KDTtype6Changed");
        refObjBindingBuilder.setNamespace("Namespace6Changed");
        refObjBindingBuilder.setReferenceObjectId("PA6Changed");
        refObjBindingBuilder.setReferenceDiscriminatorType("ParkingAffiliationTypeChanged");
        refObjBindingBuilder.setActive(false);
        ruleManagementService.updateReferenceObjectBinding(refObjBindingBuilder.build());

        // verify updated values
        refObjBinding = ruleManagementService.getReferenceObjectBinding(refObjBindingBuilder.getId());
        refObjBindingBuilder =  ReferenceObjectBinding.Builder.create(refObjBinding);
        assertNotNull("Created ReferenceObjectBinding not found", refObjBindingBuilder);
        assertEquals("Invalid CollectionName of refObjBindingBuilder found", "ParkingPolicies6Changed",
                refObjBindingBuilder.getCollectionName());
        assertEquals("Invalid KrmsObjectId of refObjBindingBuilder found","AgendaId6Changed", refObjBindingBuilder.getKrmsObjectId());
        assertEquals("Invalid KrmsDiscriminatorType of refObjBindingBuilder found","KDTtype6Changed", refObjBindingBuilder.getKrmsDiscriminatorType() );
        assertEquals("Invalid Namespace of refObjBindingBuilder found","Namespace6Changed", refObjBindingBuilder.getNamespace());
        assertEquals("Invalid ReferenceObjectId of refObjBindingBuilder found","PA6Changed", refObjBindingBuilder.getReferenceObjectId());
        assertEquals("Invalid ReferenceDiscriminatorType  of refObjBindingBuilder found","ParkingAffiliationTypeChanged", refObjBindingBuilder.getReferenceDiscriminatorType());
        assertEquals("Invalid Active value of refObjBindingBuilder found",false, refObjBindingBuilder.isActive());

        // update a object which does not exist
        refObjBindingBuilder.setId("junkValue");
        try {
            ruleManagementService.updateReferenceObjectBinding(refObjBindingBuilder.build());
            fail("Should have thrown IllegalStateException: the ReferenceObjectBinding to update does not exists");
        } catch (IllegalStateException e) {
            // throws IllegalStateException: the ReferenceObjectBinding to update does not exists
        }
    }

    /**
     *  Test testDeleteReferenceObjectBinding()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *       .deleteReferenceObjectBinding("referenceObjectBinding id") method
     */
    @Test
    public void testDeleteReferenceObjectBinding() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t8 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t8");

        ReferenceObjectBinding.Builder refObjBindingBuilder =  createTestReferenceObjectBinding(t8.object0);
        ReferenceObjectBinding refObjBinding = ruleManagementService.getReferenceObjectBinding(refObjBindingBuilder.getId());
        refObjBindingBuilder =  ReferenceObjectBinding.Builder.create(refObjBinding);
        assertNotNull("Created ReferenceObjectBinding not found", refObjBindingBuilder);

        // delete the ReferenceObjectBinding entry
        ruleManagementService.deleteReferenceObjectBinding(refObjBinding.getId());
        assertNull("Deleted ReferenceObjectBinding found", ruleManagementService.getReferenceObjectBinding(
                refObjBindingBuilder.getId()));

        // try to delete it a second time
        try {
            ruleManagementService.deleteReferenceObjectBinding(refObjBinding.getId());
            fail("should have thrown IllegalStateException: the ReferenceObjectBinding to delete does not exists");
        } catch (IllegalStateException e) {
            // throws IllegalStateException: the ReferenceObjectBinding to delete does not exists
        }

        // try to delete using null
        try {
            ruleManagementService.deleteReferenceObjectBinding(null);
            fail("should have thrown IllegalArgumentException: referenceObjectBindingId was null");
        } catch (IllegalArgumentException e) {
            // throws IllegalArgumentException: referenceObjectBindingId was null
        }
    }

    /**
     *  Test testFindReferenceObjectBindingIds()
     *
     *  This test focuses specifically on the RuleManagementServiceImpl
     *       .findReferenceObjectBindingIds(QueryByCriteria) method
     */
    @Test
    public void testFindReferenceObjectBindingIds() {
        // get a set of unique object names for use by this test (discriminator passed can be any unique value within this class)
        RuleManagementBaseTestObjectNames t9 =  new RuleManagementBaseTestObjectNames( CLASS_DISCRIMINATOR, "t9");

        // build three objects to search for.  Two active and one not active
        List<String> refObjBindingBuilderIds = new ArrayList<String>();
        ReferenceObjectBinding.Builder refObjBindingBuilder =  createTestReferenceObjectBinding(t9.object0);
        refObjBindingBuilderIds.add(refObjBindingBuilder.getId());
        refObjBindingBuilder =  createTestReferenceObjectBinding(t9.object1);
        refObjBindingBuilderIds.add(refObjBindingBuilder.getId());
        refObjBindingBuilder =  createTestReferenceObjectBinding(t9.object2);
        refObjBindingBuilderIds.add(refObjBindingBuilder.getId());
        refObjBindingBuilder.setActive(false);
        ruleManagementService.updateReferenceObjectBinding(refObjBindingBuilder.build());

        // Find the three ReferenceObjectBindings by id list
        QueryByCriteria.Builder query = QueryByCriteria.Builder.create();
        query.setPredicates(in("id", refObjBindingBuilderIds.toArray(new String[]{})));

        List<String> referenceObjectBindingIds = ruleManagementService.findReferenceObjectBindingIds(query.build());
        for (String referenceObjectBindingId : referenceObjectBindingIds ) {
            assertTrue(refObjBindingBuilderIds.contains(referenceObjectBindingId));
        }

        assertEquals("incorrect number of ReferenceObjectBindingIds found", 3, referenceObjectBindingIds.size());

        // find the two active ReferenceObjectBindings in the list
        query = QueryByCriteria.Builder.create();
        query.setPredicates( equal("active","Y"), in("id", refObjBindingBuilderIds.toArray(new String[]{})));

        referenceObjectBindingIds = ruleManagementService.findReferenceObjectBindingIds(query.build());
        for (String referenceObjectBindingId : referenceObjectBindingIds ) {
            assertTrue(refObjBindingBuilderIds.contains(referenceObjectBindingId));
        }

        assertEquals("incorrect number of ReferenceObjectBindingIds found", 2, referenceObjectBindingIds.size());
    }
}
