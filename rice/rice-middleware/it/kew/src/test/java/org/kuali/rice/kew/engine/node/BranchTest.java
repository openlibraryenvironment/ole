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
/**
 * 
 */
package org.kuali.rice.kew.engine.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.kuali.rice.kew.test.KEWTestCase;

/**
 * 
 * Tests on functionality within the Branch class.
 *
 */
public class BranchTest extends KEWTestCase {

    private Branch branch;
    
    public BranchTest() {
        super();
    }
    
    @Test public void testBranchStateIsLazyLoadable() {
        branch = new Branch();
        List<BranchState> branchStates = branch.getBranchState();
        assertNotNull("new branchStates should not be null", branchStates);
        assertEquals("new branchStates should be empty", 0, branchStates.size());
        
        //  it should fail with out of bounds error if you try to access an invalid 
        // index directly on the get() method of the List, as we're not using a lazy list
        boolean errorThrown = false;
        try {
            branchStates.get(0);
        }
        catch (IndexOutOfBoundsException e) {
            errorThrown = true;
        }
        assertTrue("An IndexOutOfBoundsException should have been thrown", errorThrown);
        
        //  it should not fail if using the getBranchStates(int index) method
        errorThrown = false;
        try {
            branch.getDocBranchState(0);
        }
        catch (Exception e) {
            errorThrown = true;
        }
        assertFalse("No exception should have been thrown", errorThrown);
        assertEquals("There should now be one element in the list (empty)", 1, branchStates.size());
    }
    
}
