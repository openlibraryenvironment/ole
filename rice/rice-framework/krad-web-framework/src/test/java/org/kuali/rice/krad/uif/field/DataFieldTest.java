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

import org.junit.Test;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Label;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.view.View;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class DataFieldTest {
    @Test
    /**
     * Tests setting and retrieving default value
     */
    public void testSetDefaultValueSucceeds() {

        // create mock objects for view, model, and component
        String defaultValue = "default";

        DataField dataField = new DataField();
        dataField.setDefaultValue(defaultValue);
        assertEquals(defaultValue, dataField.getDefaultValue());
    }

    @Test
    /**
     * Tests setting and retrieving default values
     */
    public void testSetDefaultValuesSucceeds() {

        // create mock objects for view, model, and component
        Object[] defaultValues = new Object[2];
        defaultValues[0] = new String("A");
        defaultValues[1] = new String("B");

        DataField dataField = new DataField();
        dataField.setDefaultValues(defaultValues);
        assertEquals(defaultValues, dataField.getDefaultValues());
    }
}
