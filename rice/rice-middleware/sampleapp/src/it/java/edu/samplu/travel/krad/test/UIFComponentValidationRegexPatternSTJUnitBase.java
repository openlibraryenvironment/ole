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
package edu.samplu.travel.krad.test;

import org.junit.Assert;

/**
 * Abstract base class for JUnit LoginLogout Smoke Tests.
 *
 * @see edu.samplu.travel.krad.test.UIFComponentValidationRegexPatternSTJUnitNavGen
 * @see edu.samplu.travel.krad.test.UIFComponentValidationRegexPatternSTJUnitBkMrkGen
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class UIFComponentValidationRegexPatternSTJUnitBase extends UIFComponentValidationRegexPatternAbstractSmokeTestBase {

    @Override
    public void fail(String string) {
        Assert.fail(string);
    }
}
