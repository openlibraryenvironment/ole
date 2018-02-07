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
package org.kuali.rice.krad.uif.component;

/**
 * various tests for {@link org.kuali.rice.krad.uif.component.ComponentBase}
 *
 @author Kuali Rice Team (rice.collab@kuali.org)
 */

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krad.uif.control.FileControl;
import org.kuali.rice.krad.uif.control.TextAreaControl;
import org.kuali.rice.krad.uif.control.TextControl;
import org.kuali.rice.krad.uif.control.UserControl;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.element.Image;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.field.LinkField;

import java.util.TreeMap;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ComponentBaseTest {
    private Component component;
    private TreeMap<String, String> dataAttributes;

    @Before
    public void setUp() throws Exception {
        // use an action field, since ComponentBase is abstract
        component = new Action();
        component.setId("action1");
        // used a TreeMap since it makes specific guarantees as to the order of entries
        dataAttributes = new TreeMap<String, String>();
        // set data attributes - for testing purposes only - they do not have any functional significance
        dataAttributes.put("iconTemplateName", "cool-icon-%s.png");
        dataAttributes.put("transitions", "3");
        component.setDataAttributes(dataAttributes);
    }

    @Test
    /**
     * test that simple data attributes are converted into inline attributes ok
     */
    public void testGetSimpleDataAttributes() throws Exception {
        assertNotNull(component.getSimpleDataAttributes());
        String expected = " data-iconTemplateName=\"cool-icon-%s.png\" data-transitions=\"3\"";
        assertEquals("simple attributes did not match", expected, component.getSimpleDataAttributes());
    }

    @Test
    /**
     * test that simple data attributes are converted into inline attributes ok  when data attributes are null
     */
    public void testGetSimpleDataAttributesWhenNull() throws Exception {
        component.setDataAttributes(null);
        assertEquals("simple attributes did not match", "", component.getSimpleDataAttributes());
    }
}
