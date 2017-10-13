/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.ken.dao;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This abstract class puts forward a simple framework for testing the basic 
 * persistence of business objects in the system.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class BusinessObjectPersistenceTestCaseBase extends BusinessObjectDaoTestCaseBase {

    /**
     * This method is responsible for testing the basic persistence of a business object.
     */
    @Test
    public void testBasicPersistence() {
        setup();
        assertTrue(insert());
        assertTrue(retrieve());
        assertTrue(update());
        assertTrue(validateChanges());
        assertTrue(delete());
    }
    
    /**
     * This method should be overridden and implemented to perform a setup of any dependent objects that 
     * business object may need to reference.  Since we are using Spring's automated 
     * transaction rollback, we do not need to worry about tearing stuff down.
     * @return boolean
     */
    protected void setup() {
    }
    
    /**
     * This method must be implemented to return true if a record 
     * was properly inserted into the database.
     * @return boolean
     */
    protected abstract boolean insert();

    /**
     * This method must be implemented to return true if a record 
     * was properly retreived from the database.
     * @return boolean
     */
    protected abstract boolean retrieve();
    
    /**
     * This method must be implemented to return true if a record 
     * was properly updated in the database.
     * @return boolean
     */
    protected abstract boolean update();
    
    /**
     * This method should be implemented to retrieve the objects that were just updated, and validate 
     * that their changes took effect.
     * @return boolean
     */
    protected abstract boolean validateChanges();
    
    /**
     * This method must be implemented to return true if a record 
     * was properly delted from the database.
     * @return boolean
     */
    protected abstract boolean delete();
}
