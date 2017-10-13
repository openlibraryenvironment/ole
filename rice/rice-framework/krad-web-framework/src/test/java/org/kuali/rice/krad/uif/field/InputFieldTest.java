/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.uif.field;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krad.keyvalues.KeyValuesFinder;
import org.kuali.rice.krad.keyvalues.KeyValuesFinderFactory;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.service.ViewHelperService;
import org.kuali.rice.krad.uif.view.View;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import java.util.*;

/**
 * tests InputField object and methods
 *
**/
public class InputFieldTest {

    View view = null;
    TestModel model = null;
    KeyValuesFinder optionsFinder = null;
    BindingInfo bindingInfo = null;


    @Before
    public void setUp() {
        view = Mockito.mock(View.class);
        ViewHelperService mockViewHelperService = mock(ViewHelperService.class);
        when(view.getViewHelperService()).thenReturn(mockViewHelperService);

        optionsFinder = Mockito.mock(KeyValuesFinder.class);
        bindingInfo = Mockito.mock(BindingInfo.class);
        model = new TestModel();
    }

    @Test
    public void testPerformFinalizeWithNonStringFieldOptions() throws Exception {
        // setup options finder
        Map<String, String> map = new HashMap<String, String>();
        map.put("testInteger", "1");
        optionsFinder = KeyValuesFinderFactory.fromMap(map);

        // setup preconditions (view status is final; bindinginfo return testInteger)
        when(view.getViewStatus()).thenReturn(UifConstants.ViewStatus.FINAL);
        when(bindingInfo.getBindingPath()).thenReturn("testInteger");

        // setup input field with binding info and readonly
        InputField testObj = new InputField();        
        testObj.setBindingInfo(bindingInfo);
        testObj.setReadOnly(true);
        testObj.setOptionsFinder(optionsFinder);

        testObj.performFinalize(view, model, testObj);

    }

    // Simple model object to return testInteger integer
    private class TestModel {
        public int getTestInteger() {
            return 1;
        }
    }
}