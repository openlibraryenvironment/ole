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
package org.kuali.rice.core.api.parameter

import org.junit.Test
import org.kuali.rice.core.test.CORETestCase
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator
import org.kuali.rice.coreservice.api.parameter.Parameter
import org.kuali.rice.krad.util.KRADConstants
import org.kuali.rice.kew.api.KewApiConstants

import static org.junit.Assert.assertNotNull
import org.kuali.rice.coreservice.framework.parameter.ParameterService
import org.apache.commons.lang.time.StopWatch


class ParameterUpdateTest extends CORETestCase {

    @Test
    void parameterCachingTest ( ){

        ParameterService parameterService = CoreFrameworkServiceLocator.getParameterService();
        Parameter parameter = parameterService.getParameter(KRADConstants.KUALI_RICE_WORKFLOW_NAMESPACE,
                KRADConstants.DetailTypes.BACKDOOR_DETAIL_TYPE, KewApiConstants.SHOW_BACK_DOOR_LOGIN_IND);


        String value = parameterService.getParameterValueAsString(KRADConstants.KUALI_RICE_WORKFLOW_NAMESPACE,
                KRADConstants.DetailTypes.BACKDOOR_DETAIL_TYPE, KewApiConstants.SHOW_BACK_DOOR_LOGIN_IND);
        assertNotNull("parameter should not be Null", parameter)

        //loop and get the same parameter to test caching
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        0.step(10000, 1) {
            parameter = parameterService.getParameter(KRADConstants.KUALI_RICE_WORKFLOW_NAMESPACE,
                KRADConstants.DetailTypes.BACKDOOR_DETAIL_TYPE, KewApiConstants.SHOW_BACK_DOOR_LOGIN_IND);
        }
        stopWatch.stop()
        LOG.info("loop time: " + stopWatch.getTime() + "ms");
    }
}
