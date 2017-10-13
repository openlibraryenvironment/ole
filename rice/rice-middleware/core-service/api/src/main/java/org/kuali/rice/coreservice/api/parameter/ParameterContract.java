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
package org.kuali.rice.coreservice.api.parameter;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * This is the contract for a Parameter.  The concept of a parameter is a key=value pair that is associated
 * with a rice enabled application.
 *
 * <p>
 *     When thinking of a parameter in terms of a key=value pair, the following defines the key and value parts:
 *
 *     the key of a parameter consists of the following pieces:
 *
 *     <ol>
 *         <li>{@link #getApplicationId() applicationId}</li>
 *         <li>{@link #getNamespaceCode() namespaceCode}</li>
 *         <li>{@link #getComponentCode() componentCode}</li>
 *         <li>{@link #getName() name}</li>
 *     </ol>
 *
 *     the value consists of the {@link #getValue() value}
 * </p>
 */
public interface ParameterContract extends Versioned, GloballyUnique {

	/**
     * This is the application id for the Parameter.  This cannot be null or a blank string.
     *
     * <p>
     * It is a way of assigning the Parameter to a specific rice application or rice ecosystem.
     * </p>
     *
     * @return application id
     */
	String getApplicationId();

    /**
     * This is the namespace for the parameter.  This cannot be null or a blank string.
     *
     * <p>
     * It is a way of assigning the parameter to a logical grouping within a rice application or rice ecosystem.
     * </p>
     *
     * @return namespace code
     */
	String getNamespaceCode();
	
	/**
     * This is the component code for the parameter.  This cannot be null.
     *
     * <p>
     * It is a way of assigning a parameter to a functional component within a rice application or rice ecosystem.
     * </p>
     *
     * @return component
     */
	String getComponentCode();
	
    /**
     * The name of the parameter.  This cannot be null or a blank string.
     * @return name
     */
    String getName();

    /**
     * The value of the parameter.  This can be null or a blank string.
     * @return value
     */
	String getValue();

    /**
     * This is the description for what the parameter is used for.  This can be null or a blank string.
     * @return description
     */
	String getDescription();

    /**
     * This is the evaluation operator for the parameter.  This can be null.
     *
     * <p>
     * This allows parameters to be used as primitive business rules.
     * </p>
     *
     * @return evaluation operator
     */
	EvaluationOperator getEvaluationOperator();

    /**
     * This is the type for the parameter.  This cannot be null.
     *
     * <p>
     * Some parameters have special types in rice which may have special meaning
     * and is related to the {@link #getEvaluationOperator()}
     * </p>
     *
     * @return type
     */
	ParameterTypeContract getParameterType();

}
