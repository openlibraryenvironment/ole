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
package org.kuali.rice.krms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.kuali.rice.krms.impl.ui.AgendaEditorMaintainableIntegrationTest;
import org.kuali.rice.krms.test.ComparisonOperatorIntegrationTest;
import org.kuali.rice.krms.test.PropositionBoServiceTest;
import org.kuali.rice.krms.test.RepositoryCreateAndExecuteIntegrationTest;
import org.kuali.rice.krms.test.TermBoServiceTest;
import org.kuali.rice.krms.test.TermRelatedBoTest;
import org.kuali.rice.krms.test.ValidationIntegrationTest;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ComparisonOperatorIntegrationTest.class,
        PropositionBoServiceTest.class,
        RepositoryCreateAndExecuteIntegrationTest.class,
        TermBoServiceTest.class,
        TermRelatedBoTest.class,
        ValidationIntegrationTest.class,
        AgendaEditorMaintainableIntegrationTest.class
})
public class KrmsSuite {
}