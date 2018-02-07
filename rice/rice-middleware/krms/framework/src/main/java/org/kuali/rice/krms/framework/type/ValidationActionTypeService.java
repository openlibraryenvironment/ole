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
package org.kuali.rice.krms.framework.type;

import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.framework.engine.Action;

/**
 * Interface defining the loading of a {@link ValidationAction} from a {@link ActionDefinition}
 *
 * @see ValidationAction
 * @see ActionDefinition
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface ValidationActionTypeService extends RemotableAttributeOwner, ActionTypeService {

    /**
     * VALIDATIONS_ACTION_ATTRIBUTE
     */
    static public final String VALIDATIONS_ACTION_ATTRIBUTE = "validations";

    /**
     * VALIDATIONS_ACTION_TYPE_CODE_ATTRIBUTE Database krms_attr_defn_t NM value
     */
    static public final String VALIDATIONS_ACTION_TYPE_CODE_ATTRIBUTE = "actionTypeCode"; // Database krms_attr_defn_t NM value

    /**
     * VALIDATIONS_ACTION_MESSAGE_ATTRIBUTE Database krms_attr_defn_t NM value
     */
    static public final String VALIDATIONS_ACTION_MESSAGE_ATTRIBUTE = "actionMessage"; // Database krms_attr_defn_t NM value

    @Override
	public Action loadAction(ActionDefinition actionDefinition);

    /**
     * Set the {@link ValidationActionService}.
     *
     * @param mockValidationService the {@link ValidationActionService} to use.
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if the given Validation Service is null
     */
    void setValidationService(ValidationActionService mockValidationService);
}
