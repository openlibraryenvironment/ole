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
package org.kuali.rice.kew.impl.actionlist;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.actionlist.DefaultCustomActionListAttribute;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.action.ActionItemCustomization;
import org.kuali.rice.kew.api.action.ActionSet;
import org.kuali.rice.kew.api.actionlist.DisplayParameters;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.framework.actionlist.ActionListCustomizationHandlerService;
import org.kuali.rice.kew.rule.bo.RuleAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Integration test for {@link ActionListCustomizationMediatorImpl}
 */
public class ActionListCustomizationMediatorImplTest {

    private ActionListCustomizationMediatorImpl mediator = new ActionListCustomizationMediatorImpl();

    private final String FOO_ACTION_ITEM_ID = "123";

    private final ActionItemCustomization fooResult =
            ActionItemCustomization.Builder.create(
                    FOO_ACTION_ITEM_ID,
                    ActionSet.Builder.create().build(),
                    DisplayParameters.Builder.create(Integer.valueOf(10)).build()
            ).build();

    private final String DEFAULT_ACTION_ITEM_ID = "987";

    private final ActionItemCustomization defaultResult;
    {
        // set up this custom result
        DefaultCustomActionListAttribute defaultAttribute = new DefaultCustomActionListAttribute();
        ActionSet legalActions = null;
        DisplayParameters displayParameters = null;

        try {
            legalActions = defaultAttribute.getLegalActions(null, null);
            displayParameters = defaultAttribute.getDocHandlerDisplayParameters(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        defaultResult = ActionItemCustomization.Builder.create(DEFAULT_ACTION_ITEM_ID, legalActions, displayParameters).build();
    }

    @Before
    public void setup() throws Exception {

        /**
         * Mock up our ActionListCustomizationHandlerServices, one for the "foo" applicationId, and one for the null
         * applicationId (the default).  These pass back specific results for easy test verification.
         */

        ActionListCustomizationHandlerService fooActionListCustomizationHandlerService = mock(ActionListCustomizationHandlerService.class);
        when(fooActionListCustomizationHandlerService.customizeActionList(anyString(), anyList())).thenReturn(Collections.singletonList(fooResult));

        ActionListCustomizationHandlerService defaultActionListCustomizationHandlerService = mock(ActionListCustomizationHandlerService.class);
        when(defaultActionListCustomizationHandlerService.customizeActionList(anyString(), anyList())).thenReturn(Collections.singletonList(defaultResult));

        // Mock up this internal class in the ActionListCustomizationMediatorImpl so that we don't try to use the
        // GRL and end up with a configuration nightmare
        ActionListCustomizationMediatorImpl.ActionListCustomizationHandlerServiceChooser
                mockActionListCustomizationHandlerServiceChooser =
                mock(ActionListCustomizationMediatorImpl.ActionListCustomizationHandlerServiceChooser.class);
        // set it up to choose the correct mock implementation based on application ID
        when(mockActionListCustomizationHandlerServiceChooser.getByApplicationId("foo")).thenReturn(
                fooActionListCustomizationHandlerService);
        when(mockActionListCustomizationHandlerServiceChooser.getByApplicationId(null)).thenReturn(
                defaultActionListCustomizationHandlerService);

        /**
         * Mock up our DocumentTypeService.  Setting it up to handle two doctypes: FooDocType and BarDocType, where
         * FooDocType's custom action list rule attribute has an applicationId of "foo", and BarDocType's has an
         * applicationId of null.
         */

        DocumentTypeService mockDocumentTypeService = mock(DocumentTypeService.class);

        DocumentType fooDocType = mock(DocumentType.class);
        {
            RuleAttribute ruleAttr = new RuleAttribute();
            ruleAttr.setApplicationId("foo");

            when(fooDocType.getCustomActionListRuleAttribute()).thenReturn(ruleAttr);
        }

        when(mockDocumentTypeService.findByName("FooDocType")).thenReturn(fooDocType);

        DocumentType barDocType = mock(DocumentType.class);
        {
            RuleAttribute ruleAttr = new RuleAttribute();
            ruleAttr.setApplicationId(null);

            when(barDocType.getCustomActionListRuleAttribute()).thenReturn(ruleAttr);
        }

        when(mockDocumentTypeService.findByName("BarDocType")).thenReturn(barDocType);

        // set our mocks on the mediator
        mediator.setDocumentTypeService(mockDocumentTypeService);
        mediator.setActionListCustomizationHandlerServiceChooser(mockActionListCustomizationHandlerServiceChooser);
    }

    @Test public void testApplicationServiceMediation() {

        //
        // Create our test ActionItems to customize
        //

        ActionItem.Builder fooActionItemBuilder = ActionItem.Builder.create(
                "321", "A", "234", DateTime.now(), "Foo Doc Label", "http://asdf.com/", "FooDocType", "345", "gilesp"
        );
        fooActionItemBuilder.setId(FOO_ACTION_ITEM_ID);

        ActionItem.Builder barActionItemBuilder = ActionItem.Builder.create(
                "322", "A", "235", DateTime.now(), "Bar Doc Label", "http://asdf.com/", "BarDocType", "346", "gilesp"
        );
        fooActionItemBuilder.setId(DEFAULT_ACTION_ITEM_ID);

        ArrayList<ActionItem> actionItems = new ArrayList<ActionItem>();
        actionItems.add(fooActionItemBuilder.build());
        actionItems.add(barActionItemBuilder.build());

        Map<String, ActionItemCustomization> customizations = mediator.getActionListCustomizations("gilesp", actionItems);

        assertTrue(customizations.size() == 2);
        // this proves that the appropriate services were called by the mediator
        assertEquals(fooResult, customizations.get(fooResult.getActionItemId()));
        assertEquals(defaultResult, customizations.get(defaultResult.getActionItemId()));
    }

    @Test(expected = RiceIllegalArgumentException.class) public void testNullPrincipalId() {
        mediator.getActionListCustomizations(null, Collections.<ActionItem>emptyList());
    }

    @Test(expected = RiceIllegalArgumentException.class) public void testEmptyPrincipalId() {
        mediator.getActionListCustomizations("", Collections.<ActionItem>emptyList());
    }

    @Test public void testEmptyActionItemList() {
        Map<String, ActionItemCustomization> customizations = mediator.getActionListCustomizations("gilesp", Collections.<ActionItem>emptyList());
        assertTrue(customizations.size() == 0);
    }

}
