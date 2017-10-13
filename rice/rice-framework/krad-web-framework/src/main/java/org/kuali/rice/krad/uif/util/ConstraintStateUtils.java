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
package org.kuali.rice.krad.uif.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.state.StateMapping;
import org.kuali.rice.krad.datadictionary.validation.constraint.BaseConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Various utility methods for determining when to use constraints during states
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
  *@since 2.2
 */
public class ConstraintStateUtils {

    /**
     * Determines if the constraint passed in applies for the applicableState, based on the stateMapping
     *
     * <p>Note: this method will automatically return TRUE if the stateMapping is null, the Constraint is not a BaseConstraint,
     * if there are no states defined on the Constraint, or if the state it applies to cannot be found in the stateMapping,
     * because in all these cases the Constraint is considered
     * stateless and will apply to any state</p>
     *
     * @param applicableState the state to check to see if the constraint applies
     * @param constraint the Constraint to check
     * @param stateMapping the StateMapping object containing state information
     * @return true if the Constraint applies to the applicableState, false otherwise
     */
    public static boolean constraintAppliesForState(String applicableState, Constraint constraint,
            StateMapping stateMapping) {
        List<String> stateOrder = new ArrayList<String>();
        if (stateMapping != null) {
            stateOrder = stateMapping.getStates();
        }

        if (stateMapping == null || !(constraint instanceof BaseConstraint) || StringUtils.isEmpty(applicableState)) {
            //process constraint because it is considered "stateless" if not a BaseConstraint
            //or no associated state mapping or no has no state to compare to
            return true;
        } else if (((BaseConstraint) constraint).getStates() == null || ((BaseConstraint) constraint).getStates()
                .isEmpty()) {
            //simple case - no states for this constraint, so always apply
            return true;
        } else if (((BaseConstraint) constraint).getStates().contains(applicableState) && stateOrder.contains(
                applicableState)) {
            //constraint applies for the applicableState and the state exists for the object
            return true;
        } else {
            for (String constraintState : ((BaseConstraint) constraint).getStates()) {
                //range case
                if (constraintState.contains(">")) {
                    String[] rangeArray = constraintState.split(">");
                    if (rangeArray[1].endsWith("+")) {
                        //make 2nd part of range current state being checked if nothing is
                        //matched below for the range case
                        constraintState = rangeArray[1];
                        rangeArray[1] = StringUtils.removeEnd(rangeArray[1], "+");
                    }
                    if (stateOrder.contains(rangeArray[0]) && stateOrder.contains(rangeArray[1])) {
                        for (int i = stateOrder.indexOf(rangeArray[0]); i <= stateOrder.indexOf(rangeArray[1]); i++) {
                            if (stateOrder.get(i).equals(applicableState)) {
                                return true;
                            }
                        }
                    } else {
                        throw new RuntimeException("Invalid state range: " + constraintState);
                    }
                }

                //+ case (everything after and including this state)
                if (constraintState.contains("+")) {
                    constraintState = StringUtils.removeEnd(constraintState, "+");
                    if (stateOrder.contains(constraintState)) {
                        for (int i = stateOrder.indexOf(constraintState); i < stateOrder.size(); i++) {
                            if (stateOrder.get(i).equals(applicableState)) {
                                return true;
                            }
                        }
                    } else {
                        throw new RuntimeException("Invalid constraint state: " + constraintState);
                    }
                }
            }
        }
        //if no case is matched, return false
        return false;
    }

    /**
     * Gets the constraint that applies for the validationState passed in with the appropriate StateMapping (which
     * should include validationState as one of its states).
     *
     * <p>This method will essentially return the constraint
     * passed in, in most cases, if the constraint applies.  In cases where there are constraintStateOverrides
     * set for the constraint, those will be evaluated to get the appropriate replacement.  If the constraint does
     * not apply for the validationState (as well as none of the replacements), this method will return null.
     * If stateMapping passed in is null, the constraint is not a BaseConstraint, or validationState is blank,
     * the original constraint will be returned
     * (assumed stateless).</p>
     *
     * @param constraint the original constraint
     * @param validationState the validation state
     * @param stateMapping the state information for the model being evaluated
     * @param <T> constraint type
     * @return the applicable constraint, null if this constraint does not apply to the validationState
     * @throws RuntimeException if the type of constraint passed in cannot be cast to the replacement constraint
     */
    public static <T extends Constraint> T getApplicableConstraint(T constraint, String validationState,
            StateMapping stateMapping) {

        //is state information setup?
        if (constraint != null && constraint instanceof BaseConstraint && stateMapping != null &&
                StringUtils.isNotBlank(validationState)) {

            //Does the constraint have overrides?
            if (((BaseConstraint) constraint).getConstraintStateOverrides() != null && !((BaseConstraint) constraint)
                    .getConstraintStateOverrides().isEmpty()) {
                T override = null;
                BaseConstraint theConstraint = ((BaseConstraint) constraint);
                for (BaseConstraint bc : theConstraint.getConstraintStateOverrides()) {
                    //does the override apply for this state?
                    if (!bc.getStates().isEmpty() && ConstraintStateUtils.constraintAppliesForState(validationState, bc,
                            stateMapping)) {
                        try {
                            //Last on the list takes precedence
                            override = (T) bc;
                        } catch (ClassCastException e) {
                            throw new RuntimeException("Replacement state constraint for this constraint is not an "
                                    + "appropriate type: "
                                    + constraint.getClass().toString()
                                    + " cannot be cast to "
                                    + bc.getClass().toString());
                        }
                    }
                }

                if(override != null){
                    return override;
                }
                else if(override == null && ConstraintStateUtils.constraintAppliesForState(validationState, constraint, stateMapping)){
                    //use base constraint if no overrides apply and it still applies for this state
                    return constraint;
                }
                else{
                    //the constaint AND its overrides do not apply
                    return null;
                }
            } else if(ConstraintStateUtils.constraintAppliesForState(validationState, constraint, stateMapping)) {
                //Constraint applies for this state
                return constraint;
            }
            else{
                //Constraint does not apply for this state
                return null;
            }
        }

        //state information either not setup or not setup correctly for this constraint/stateMapping,
        //so constraint will apply by default
        return constraint;
    }

    /**
     * Gets the client validation state.  If there are customClientSideValidationStates configured for the view's
     * stateMapping, these are used, otherwise client side validation state is assumed to be the next state (or
     * current state if there is no next state).  Returns null if there is no state for client side validation (ie
     * stateless).
     *
     * @param model
     * @param view
     * @return the state to validate against for client side view validation
     */
    public static String getClientViewValidationState(Object model, View view){
        String validationState = null;
        String path = view.getStateObjectBindingPath();
        Object stateObject;

        if (StringUtils.isNotBlank(path)) {
            stateObject = ObjectPropertyUtils.getPropertyValue(model, path);
        } else {
            stateObject = model;
        }
        StateMapping stateMapping = view.getStateMapping();
        if (stateMapping != null) {
            validationState = stateMapping.getNextState(stateObject);

            if (stateMapping.getCustomClientSideValidationStates() != null) {
                String currentState = stateMapping.getCurrentState(stateObject);
                validationState = stateMapping.getCustomClientSideValidationStates().get(currentState);
                if (StringUtils.isBlank(validationState)) {
                    validationState = stateMapping.getNextState(stateObject);
                }
            }
        }
        return validationState;
    }
}
