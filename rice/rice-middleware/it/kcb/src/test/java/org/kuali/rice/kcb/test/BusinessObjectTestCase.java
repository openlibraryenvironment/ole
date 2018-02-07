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
package org.kuali.rice.kcb.test;

import org.junit.Before;
import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

/**
 * This base class for testing CRUD operations on BOs
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineMode(Mode.ROLLBACK_CLEAR_DB)
public abstract class BusinessObjectTestCase extends KCBTestCase {
    protected GenericDao businessObjectDao;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        businessObjectDao = services.getKcbGenericDao();
    }

	/**
	 * This method should be overridden to test creation
	 */
    public abstract void testCreate() throws Exception;
    /**
     * This method should be overridden to test invalid creation
     */
    public void testInvalidCreate() throws Exception {}
    /**
     * This method should be overridden to test duplicate creation
     */
    public void testDuplicateCreate() throws Exception {}
    /**
     * This method should be overridden to test retrieval by id
     */
    public void testReadById() throws Exception {};
    /**
     * This method should be overridden to test retrieval
     */
    public void testReadByQuery() throws Exception {}
    /**
     * This method should be overridden to test an invalid retrieval
     */
    public void testInvalidRead() throws Exception {}
    /**
     * This method should be overridden to test updating
     */
    public abstract void testUpdate() throws Exception;
    /**
     * This method should be overridden to test an invalid update
     */
    public void testInvalidUpdate() throws Exception {}
    /**
     * This method should be overridden to test delete
     */
    public abstract void testDelete() throws Exception;
    /**
     * This method should be overridden to test an invalid delete
     */
    public void testInvalidDelete() throws Exception {}
}
