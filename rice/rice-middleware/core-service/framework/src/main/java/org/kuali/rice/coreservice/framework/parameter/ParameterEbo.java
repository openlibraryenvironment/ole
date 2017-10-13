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
package org.kuali.rice.coreservice.framework.parameter;

import org.kuali.rice.coreservice.api.parameter.EvaluationOperator;
import org.kuali.rice.coreservice.api.parameter.ParameterContract;
import org.kuali.rice.coreservice.api.parameter.ParameterTypeContract;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;

/**
 * TODO: Likely should remove all methods from this interface after KULRICE-7170 is fixed
 */
public interface ParameterEbo extends ParameterContract,
		ExternalizableBusinessObject {

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
	
	/**
	 * Returns the version number for this object.  In general, this value should only
	 * be null if the object has not yet been stored to a persistent data store.
	 * This version number is generally used for the purposes of optimistic locking.
	 * 
	 * @return the version number, or null if one has not been assigned yet
	 */
	Long getVersionNumber();
	
	/**
	 * Return the globally unique object id of this object.  In general, this value should only
	 * be null if the object has not yet been stored to a persistent data store.
	 * 
	 * @return the objectId of this object, or null if it has not been set yet
	 */
	String getObjectId();
}
