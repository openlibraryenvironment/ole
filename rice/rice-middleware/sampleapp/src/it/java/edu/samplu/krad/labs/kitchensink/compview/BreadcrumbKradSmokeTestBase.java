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
package edu.samplu.krad.labs.kitchensink.compview;

import edu.samplu.common.ITUtil;
import edu.samplu.krad.compview.BreadcrumbSmokeTestBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public abstract class BreadcrumbKradSmokeTestBase extends BreadcrumbSmokeTestBase {

    public static final String BOOKMARK_URL = "/kr-krad/uicomponents?viewId=UifCompView";

    @Override
    public String getTestUrl() {
        return ITUtil.LABS;
    }

    @Override
    protected void navigation() throws Exception {
        waitAndClickByLinkText("Kitchen Sink");
    }
}
