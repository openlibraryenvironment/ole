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
package org.kuali.rice.krad.util;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.krad.datadictionary.state.StateMapping;
import org.kuali.rice.krad.datadictionary.state.StateMappingBase;
import org.kuali.rice.krad.datadictionary.validation.constraint.BaseConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint;
import org.kuali.rice.krad.uif.util.ConstraintStateUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for constraintStateUtils class to insure correct logic for applicable constraints in state based validation
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ConstraintStateUtilsTest {

    /**
     * Testing state based validation utility methods that the service uses to determine applicable constraints
     */
    @Test
    public void testStateValidationUtil() {
        BaseConstraint genericConstraint = new ValidCharactersConstraint() {};
        List<String> cStates = new ArrayList<String>();
        cStates.add("state1");
        cStates.add("state3>state5");
        cStates.add("state7+");
        genericConstraint.setStates(cStates);

        StateMapping genericStateMapping = new StateMappingBase();
        List<String> states = new ArrayList<String>();
        states.add("state1");
        states.add("state2");
        states.add("state3");
        states.add("state4");
        states.add("state5");
        states.add("state6");
        states.add("state7");
        states.add("state8");
        genericStateMapping.setStates(states);

        //constraint applies for state1
        boolean applies = ConstraintStateUtils.constraintAppliesForState("state1", genericConstraint,
                genericStateMapping);
        Assert.assertTrue(applies);
        Constraint constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state1",
                genericStateMapping);
        Assert.assertNotNull(constraintResult);

        //constraint does not apply for state2
        applies = ConstraintStateUtils.constraintAppliesForState("state2", genericConstraint, genericStateMapping);
        Assert.assertFalse(applies);
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state2",
                genericStateMapping);
        Assert.assertNull(constraintResult);

        //constraint applies for state3 through state5
        applies = ConstraintStateUtils.constraintAppliesForState("state3", genericConstraint, genericStateMapping);
        Assert.assertTrue(applies);
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state3",
                genericStateMapping);
        Assert.assertNotNull(constraintResult);

        applies = ConstraintStateUtils.constraintAppliesForState("state4", genericConstraint, genericStateMapping);
        Assert.assertTrue(applies);
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state4",
                genericStateMapping);
        Assert.assertNotNull(constraintResult);

        applies = ConstraintStateUtils.constraintAppliesForState("state5", genericConstraint, genericStateMapping);
        Assert.assertTrue(applies);
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state5",
                genericStateMapping);
        Assert.assertNotNull(constraintResult);

        //constraint applies for state7 and after
        applies = ConstraintStateUtils.constraintAppliesForState("state7", genericConstraint, genericStateMapping);
        Assert.assertTrue(applies);
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state7",
                genericStateMapping);
        Assert.assertNotNull(constraintResult);

        applies = ConstraintStateUtils.constraintAppliesForState("state8", genericConstraint, genericStateMapping);
        Assert.assertTrue(applies);
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state8",
                genericStateMapping);
        Assert.assertNotNull(constraintResult);

        //constraint does not apply for non-listed states or the empty state
        applies = ConstraintStateUtils.constraintAppliesForState("fake", genericConstraint, genericStateMapping);
        Assert.assertFalse(applies);
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "notListed",
                genericStateMapping);
        Assert.assertNull(constraintResult);

        //constraint applies if no state (state validation not setup or "stateless")
        applies = ConstraintStateUtils.constraintAppliesForState(null, genericConstraint, genericStateMapping);
        Assert.assertTrue(applies);
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, null, genericStateMapping);
        Assert.assertNotNull(constraintResult);

        applies = ConstraintStateUtils.constraintAppliesForState("", genericConstraint, genericStateMapping);
        Assert.assertTrue(applies);
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "", genericStateMapping);
        Assert.assertNotNull(constraintResult);

        //applies for stateless situations
        applies = ConstraintStateUtils.constraintAppliesForState(null, genericConstraint, null);
        Assert.assertTrue(applies);
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, null, null);
        Assert.assertNotNull(constraintResult);

        //constraint applies if null state mapping (state validation not setup or "stateless")
        applies = ConstraintStateUtils.constraintAppliesForState("state3", genericConstraint, null);
        Assert.assertTrue(applies);
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state3", null);
        Assert.assertNotNull(constraintResult);

        //throws error if stateMapping does not contain the current state (bad state mapping object)
        try {
            applies = ConstraintStateUtils.constraintAppliesForState("state3", genericConstraint,
                    new StateMappingBase());
        } catch (Exception e) {
            Assert.assertNotNull(e);
        }

        List<BaseConstraint> constraintStateOverrides = new ArrayList<BaseConstraint>();
        final List<String> overrideStates = Arrays.asList(new String[]{"state2", "state3"});
        BaseConstraint override = new ValidCharactersConstraint() {{
            setStates(overrideStates);
        }};
        constraintStateOverrides.add(override);

        genericConstraint.setConstraintStateOverrides(constraintStateOverrides);

        //should return back the same constraint
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state1",
                genericStateMapping);
        Assert.assertNotNull(constraintResult);
        Assert.assertEquals(genericConstraint, constraintResult);

        //should return back the override (testing common case)
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state3",
                genericStateMapping);
        Assert.assertNotNull(constraintResult);
        Assert.assertEquals(override, constraintResult);

        //should return back the override (testing where constraint does not apply but override does)
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state2",
                genericStateMapping);
        Assert.assertNotNull(constraintResult);
        Assert.assertEquals(override, constraintResult);

        //should return back the same constraint (testing where overrides are configured but none match
        //but the constraint is applicable for this state)
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state4",
                genericStateMapping);
        Assert.assertNotNull(constraintResult);
        Assert.assertEquals(genericConstraint, constraintResult);

        //should return back null (testing where the constrain overrides and the constraint do not match state)
        constraintResult = ConstraintStateUtils.getApplicableConstraint(genericConstraint, "state6",
                genericStateMapping);
        Assert.assertNull(constraintResult);

    }

    /**
     * Test for getClientViewValidationState method that returns the state the client should validate for
     */
    @Test
    public void testCustomClientsideUtilMethod() {
        View view = new View();
        StateMapping genericStateMapping = new StateMappingBase();
        List<String> states = new ArrayList<String>();
        states.add("state1");
        states.add("state2");
        states.add("state3");
        states.add("state4");
        states.add("state5");
        states.add("state6");
        states.add("state7");
        states.add("state8");
        genericStateMapping.setStates(states);
        genericStateMapping.setStatePropertyName("state");
        UifFormBase model = new UifFormBase();

        Map<String, String> customClientStateMap = new HashMap<String, String>();
        customClientStateMap.put("state1", "state3");
        customClientStateMap.put("state4", "state8");
        customClientStateMap.put("state7", "state7");
        genericStateMapping.setCustomClientSideValidationStates(customClientStateMap);
        view.setStateMapping(genericStateMapping);

        //custom
        model.setState("state1");
        String state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertEquals("state3", state);
        model.setState("state4");
        state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertEquals("state8", state);
        model.setState("state7");
        state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertEquals("state7", state);

        //should return next state
        model.setState("state2");
        state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertEquals("state3", state);
        model.setState("state3");
        state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertEquals("state4", state);
        model.setState("state5");
        state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertEquals("state6", state);

        //should return state8 (no next state)
        model.setState("state8");
        state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertEquals("state8", state);

        //should back same state sent in - bad or no configurations, if then used for get applicableConstraint
        //will get back the appropriate constraint
        model.setState("fake");
        state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertEquals("fake", state);

        model.setState(null);
        state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertNull(state);

        model.setState("");
        state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertEquals("", state);

        model.setState("state1");
        StateMapping emptyStateMapping = new StateMappingBase();
        emptyStateMapping.setStatePropertyName("state");
        view.setStateMapping(emptyStateMapping);
        state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertEquals("state1", state);

        //return back null state when StateMapping is not configured
        view.setStateMapping(null);
        state = ConstraintStateUtils.getClientViewValidationState(model, view);
        Assert.assertNull(state);
    }
}
