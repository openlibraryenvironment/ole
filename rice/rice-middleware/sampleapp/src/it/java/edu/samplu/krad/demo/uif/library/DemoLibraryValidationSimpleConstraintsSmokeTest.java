/*
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
package edu.samplu.krad.demo.uif.library;

import org.junit.Test;

public class DemoLibraryValidationSimpleConstraintsSmokeTest extends DemoLibraryBase {

    /**
     * /kr-krad/kradsampleapp?viewId=Demo-SimpleConstraint-View&methodToCall=start
     */
    public static final String BOOKMARK_URL = "/kr-krad/kradsampleapp?viewId=Demo-SimpleConstraint-View&methodToCall=start";

    /**
     * //div[@id='Demo-SimpleConstraint-Example1_tab']
     */
    public static final String SIMPLE_CONSTRAINT_REQUIRED_TAB = "//div[@id='Demo-SimpleConstraint-Example1_tab']";

    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    @Override
    protected void navigate() throws Exception {
        waitAndClickById("Demo-LibraryLink");
        waitAndClickByLinkText("Validation");
        waitAndClickByLinkText("Simple Constraints");
    }

    protected void testRequired() throws Exception {
        waitAndClickByLinkText("Required");
        assertIsVisibleByXpath(SIMPLE_CONSTRAINT_REQUIRED_TAB, "Simple Constraint Required Tab");
        passed();
    }

    @Test
    public void testRequiredBookmark() throws Exception {
        testRequired();
    }

    @Test
    public void testRequiredNav() throws Exception {
        testRequired();
    }
}