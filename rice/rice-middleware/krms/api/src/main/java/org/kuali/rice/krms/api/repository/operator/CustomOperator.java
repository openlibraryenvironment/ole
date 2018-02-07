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
package org.kuali.rice.krms.api.repository.operator;

import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.krms.api.repository.function.FunctionDefinition;

import java.util.List;

/**
 * A service that can export a custom function as an operator in the KRMS rule editor user interface.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface CustomOperator {

    /**
     * gets the {@link org.kuali.rice.krms.api.repository.function.FunctionDefinition} for the function that will be
     * evaluated at rule execution time.  Note that the backing FunctionTypeService must be locally available to the
     * application at execution time, as execution objects in KRMS can not be obtained over the service bus.
     *
     * The FunctionDefinition returned by this method must have a return type of java.lang.Boolean, and must have
     * either one or two FunctionParameterDefinitions.
     *
     * @return a FunctionDefinition with a return type of Boolean
     */
    FunctionDefinition getOperatorFunctionDefinition();

    /**
     * Validates that the given operand classes are acceptable for the custom function.
     *
     * <p>Note that the attribute name in returned errors will be ignored.</p>
     * <p>If only a single operand has been specified, then rhsClassName will be null.</p>
     *
     * @param lhsClassName the class name for the left hand side operand
     * @param rhsClassName the class name for the right hand side operand
     * @return A list of errors, or an empty list if no errors are found
     */
    List<RemotableAttributeError> validateOperandClasses(String lhsClassName, String rhsClassName);
}
