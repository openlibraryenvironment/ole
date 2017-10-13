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
package org.kuali.rice.krad.uif.view;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.control.TextControl;
import org.kuali.rice.krad.uif.field.InputField;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * ViewIndexTest has various tests for ViewIndex
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewIndexTest {
    @Before
    public void setUp() throws Exception {

    }

    /**
     * test that clear indexes after render does not clear fields with a value for
     * refreshWhenChanged and their nested controls
     * 
     * @throws Exception
     */
    @Test
    public void testClearIndexesAfterRender() throws Exception {
        ViewIndex viewIndex = new ViewIndex();
        // create an input field
        InputField field = new InputField();
        BindingInfo bindingInfo = new BindingInfo();
        bindingInfo.setBindingPath("property1");
        field.setBindingInfo(bindingInfo);
        String fieldId = "field1";
        field.setId(fieldId);
        if (field.getRefreshWhenChangedPropertyNames() == null) {
            field.setRefreshWhenChangedPropertyNames(new ArrayList<String>());
        }
        field.getRefreshWhenChangedPropertyNames().add("#lp.allDay eq true");
        // set a control
        TextControl textControl = new TextControl();
        String controlId = "text1";
        textControl.setId(controlId);
        field.setControl(textControl);

        //add to view index
        Component[] components = {field};
        for (Component component : components) {
            viewIndex.indexComponent(component);
            viewIndex.addInitialComponentStateIfNeeded(component);
        }

        // verify initial view index state
        for (Component component : components) {
            assertNotNull(viewIndex.getComponentById(component.getId()));
        }

        viewIndex.clearIndexesAfterRender();
        // confirm that the index still has the components
        for (Component component : components) {
            assertNotNull(viewIndex.getComponentById(component.getId()));
            assertTrue(viewIndex.getInitialComponentStates().containsKey(component.getId()));
        }
    }
}
