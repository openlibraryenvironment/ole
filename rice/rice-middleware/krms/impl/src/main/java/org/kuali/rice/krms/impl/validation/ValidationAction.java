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
package org.kuali.rice.krms.impl.validation;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.framework.type.ValidationActionType;
import org.kuali.rice.krms.framework.type.ValidationActionTypeService;

/**
 * An {@link Action} that when executed appends its type and message to the results
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ValidationAction implements Action {


    private final ValidationActionType type;
    private final String message;

    /**
     * create a Validation action of the given type with the given message
     * @param type {@link ValidationActionType}
     * @param message for when action executes
     */
    public ValidationAction(ValidationActionType type, String message) {
        if (type == null) throw new IllegalArgumentException("type must not be null");

        this.type = type;
        this.message = message;
    }

    @Override
    public void execute(ExecutionEnvironment environment) {
        // create or extend an existing attribute on the EngineResults to communicate the selected Validation and
        // action

        Object value = environment.getEngineResults().getAttribute(ValidationActionTypeService.VALIDATIONS_ACTION_ATTRIBUTE);
        StringBuilder selectedAttributesStringBuilder = new StringBuilder();

        if (value != null) {
            // assume the value is what we think it is
            selectedAttributesStringBuilder.append(value.toString());
            // we need a comma after the initial value
            selectedAttributesStringBuilder.append(",");
        }

        // add our people flow action to the string using our convention
        selectedAttributesStringBuilder.append(type.getCode());
        selectedAttributesStringBuilder.append(":");
        selectedAttributesStringBuilder.append(message);

        // set our attribute on the engine results
        environment.getEngineResults().setAttribute(ValidationActionTypeService.VALIDATIONS_ACTION_ATTRIBUTE,
                selectedAttributesStringBuilder.toString()
        );
    }

    @Override
    public void executeSimulation(ExecutionEnvironment environment) {
        // our action doesn't need special handling during simulations
        execute(environment);
    }
}
