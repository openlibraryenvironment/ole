/**
 * Copyright 2005-2012 The Kuali Foundation
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
package org.kuali.ole;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;

/**
 * Kuali Rice ArcheType Help
 *
 * This is an abstract unit test for several service implementations.  Unit tests should run quick and not
 * depend on external or unstable resources.  This test logic will run at every maven project build.
 *
 *
 * An abstract test class to test different {@link ProductService} implementations.
 * Extend this class and override the {@link #createService()} method for each implementation.
 */
public abstract class AbstractProductServiceImplTest {

    private ProductService service;

    /**
     * Abstract method to override by test implementations.
     * @return an instance of {@link ProductService}
     */
    public abstract ProductService createService();

    @Before
    public final void initService() {
        service = createService();
    }

    @After
    public final void destroyService() {
        service = null;
    }

    @Test
    public final void testPositiveNumbers() {
        assertEquals(4, service.product(2, 2).intValue());
    }

    @Test
    public final void testNegativeNumbers() {
        assertEquals(4, service.product(-2, -2).intValue());
    }

    @Test
    public final void testZero() {
        assertEquals(0, service.product(2, 0).intValue());
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testNulls() {
        assertEquals(0, service.product(null, null).intValue());
    }
}