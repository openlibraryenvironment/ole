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
package org.kuali.rice.krad.datadictionary.state;

import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;

import java.util.List;
import java.util.Map;

/**
 * StateMapping defines the methods necessary to allow for state validation to apply to a state object
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 2.2
 */
public interface StateMapping {

    /**
     * Gets the currentState by looking at the statePropertyName on the stateObject passed in
     *
     * @param stateObject the object containing the state information
     * @return the currentState of the object
     */
    public String getCurrentState(Object stateObject);

    /**
     * Gets the nextState of the object passed in by looking at the statePropertyName and using the states list
     * to determine the next state in order.  Returns the currentState if there is no next state.
     *
     * @param stateObject the object containing the state information
     * @return the next state of the object, or current state if no next state exists
     */
    public String getNextState(Object stateObject);

    /**
     * Returns the human readable message determined by the key set for the state passed in
     * against the stateNameMessageKeyMap.  If the state cannot be found in the states list or the state passed in is
     * blank, this method will return null.  If the key found does not match a message, this method will return the
     * state
     * passed in.
     *
     * @param state state to get the message for
     * @return state message, or the state passed in (if a valid state in states). Otherwise, returns null.
     */
    public String getStateNameMessage(String state);

    /**
     * Map used by getStateNameMessage to determine the key to use when retrieving the message for the state specified.
     * The map should be defined as state:messageKeyForState.
     *
     * @return map of states and their messageKeys
     */
    public Map<String, String> getStateNameMessageKeyMap();

    /**
     * Set the stateNameMessageKeyMap
     *
     * @param stateNameMessageKeyMap map of states and their messageKeys
     */
    public void setStateNameMessageKeyMap(Map<String, String> stateNameMessageKeyMap);

    /**
     * The states of this stateMapping.  IMPORTANT: This must be ALL states for this mapping IN ORDER.
     *
     * @return list of states, in the order in which they are applied.  If states were never set, returns an empty
     *         list.
     */
    public List<String> getStates();

    /**
     * Set the states of this stateMapping, in order
     *
     * @param states list of states, in the order in which they are applied.
     */
    public void setStates(List<String> states);

    /**
     * The property name/path to be used when trying to find state String information on the object.  This is used by
     * getCurrentState(Object object)
     * and getNextState(Object object);
     *
     * @return the property name/path representing where state information can be found on the object
     */
    public String getStatePropertyName();

    /**
     * Set the property name/path
     *
     * @param name the property name/path that points to the string representation of the object's tate
     */
    public void setStatePropertyName(String name);

    /**
     * This ONLY applies to client side validations as the controller has full control over what validations to apply
     * on the server.  This setting determines for what states, if any, the client side validation needs to use for
     * its validation instead of the default (by default if state validation is setup, the client side validation
     * will use n+1 as its validation, ie whatever the NEXT state is).
     *
     * <p>The map must be defined as such: (state of the object, state to use for validation on the
     * client when in that state).<br>
     * Example:<br>
     * key="INITIAL" value="SUBMIT"<br>
     * key="SAVE" value="SUBMIT"<br>
     * In this example, when the object is in the "INITIAL" or the "SAVE" states, validation on the client will
     * be against the "SUBMIT" state</p>
     *
     * @return map representing the state of the object and state to use for validation on the
     *         client when in that state
     */
    public Map<String, String> getCustomClientSideValidationStates();

    /**
     * Set the custom client side validation behavior map.  This only applies for client-side validation.
     *
     * @param customClientSideValidationStates map representing the state of the object and state to use for validation
     * on the
     * client when in that state
     */
    public void setCustomClientSideValidationStates(Map<String, String> customClientSideValidationStates);

    /**
     * Validates different requirements of component compiling a series of reports detailing information on errors
     * found in the component.  Used by the RiceDictionaryValidator.
     *
     * @param tracer Record of component's location
     */
    public void completeValidation(ValidationTrace tracer);

}
