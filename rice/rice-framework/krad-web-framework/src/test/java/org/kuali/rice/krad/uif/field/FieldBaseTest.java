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

import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Label;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.service.ViewHelperService;
import org.kuali.rice.krad.uif.view.View;

import org.junit.Test;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * test various FieldBase methods
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FieldBaseTest {

    @Test
    /**
     * Tests rendering on required messages
     *
     * @see KULRICE-7130
     */
    public void testRequiredMessageDisplay() {

        // create mock objects for view, view helper service, model, and component
        View mockView =  mock(View.class);
        ViewHelperService mockViewHelperService = mock(ViewHelperService.class);
        when(mockView.getViewHelperService()).thenReturn(mockViewHelperService);
        Object nullModel = null;
        Component mockComponent = mock(Component.class);

        // build asterisk required message and mock label to test rendering
        Label mockLabel = mock(Label.class);

        Message requiredMessage = new Message();
        requiredMessage.setMessageText("*");
        requiredMessage.setRender(true);
        when(mockLabel.getRequiredMessage()).thenReturn(requiredMessage);

        try {
            FieldBase fieldBase = new FieldBase();
            fieldBase.setFieldLabel(mockLabel);
            fieldBase.setRequired(true);

            // required and not readonly - render
            fieldBase.setReadOnly(false);
            fieldBase.performFinalize(mockView, nullModel, mockComponent);
            assertTrue(fieldBase.getFieldLabel().getRequiredMessage().isRender());

            // required and readonly -  do not render
            fieldBase.setReadOnly(true);
            fieldBase.performFinalize(mockView, nullModel, mockComponent);
            assertFalse(fieldBase.getFieldLabel().getRequiredMessage().isRender());
        } catch(Exception ex) {
            fail("Unit Test Exception - " + ex.getMessage());
        }
    }
}