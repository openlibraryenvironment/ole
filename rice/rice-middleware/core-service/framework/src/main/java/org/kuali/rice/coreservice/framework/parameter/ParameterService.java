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

import org.kuali.rice.coreservice.api.parameter.Parameter;

import java.util.Collection;

/**
 * This service is used by krad to interact with {@link Parameter Parameters}.
 *
 * <p>
 * Generally krad client applications will want to use this service since it contains many convenient methods.
 * </p>
 *
 * <p>
 * This service can be viewed a convenient wrapper around the {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService}.
 * Please see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService} for details on the behavior of this service.
 * </p>
 */
public interface ParameterService {

    /**
     * This will create a {@link Parameter} exactly like the parameter passed in.
     *
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#createParameter(org.kuali.rice.coreservice.api.parameter.Parameter)} for details
     */
    Parameter createParameter(Parameter parameter);

    /**
     * This will update a {@link Parameter}.
     *
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#updateParameter(org.kuali.rice.coreservice.api.parameter.Parameter)} for details
     */
    Parameter updateParameter(Parameter parameter);

    /**
     * This method checks if a parameter exists.  It will never return null.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from a {@link ParameterConstants.NAMESPACE}
     *          annotation on the componentClass</li>
     *       <li>component code: from a {@link ParameterConstants.COMPONENT}
     *         annotation on the componentClass</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     *  <p>
     *     If the parameter does not exist under the application
     *     code, then this method will check if the parameter
     *     exists under the default rice application id and
     *     will return that parameter.
     * </p>
     *
     * @param componentClass the class with the namespace & component annotations
     * @param parameterName the parameter name
     * @return true or false
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     */
    Boolean parameterExists(Class<?> componentClass, String parameterName);

    /**
     * This method checks if a parameter exists.  It will never return null.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from the passed in namespace code</li>
     *       <li>component code: from the passed in component code</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     *  <p>
     *     If the parameter does not exist under the application
     *     code, then this method will check if the parameter
     *     exists under the default rice application id and
     *     will return that parameter.
     * </p>
     *
     * @param namespaceCode the namespace code
     * @param componentCode the component code
     * @param parameterName the parameter name
     * @return true or false
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     */
    Boolean parameterExists(String namespaceCode, String componentCode, String parameterName);

    /**
     * Retrieves a parameter's string value.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from a {@link ParameterConstants.NAMESPACE}
     *          annotation on the componentClass</li>
     *       <li>component code: from a {@link ParameterConstants.COMPONENT}
     *         annotation on the componentClass</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param componentClass the class with the namespace & component annotations
     * @param parameterName the parameter name
     * @return string value or null
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameterValueAsString(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    String getParameterValueAsString(Class<?> componentClass, String parameterName);

    /**
     * Retrieves a parameter's string value.  If the parameter is not found the default value will be returned.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from a {@link ParameterConstants.NAMESPACE}
     *          annotation on the componentClass</li>
     *       <li>component code: from a {@link ParameterConstants.COMPONENT}
     *         annotation on the componentClass</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param componentClass the class with the namespace & component annotations
     * @param parameterName the parameter name
     * @param defaultValue the value to return is the parameter does not exist.  Can be any string value including null
     * @return string value or null
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameterValueAsString(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    String getParameterValueAsString(Class<?> componentClass, String parameterName, String defaultValue);


    /**
     * Retrieves a parameter's string value.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from the passed in namespace code</li>
     *       <li>component code: from the passed in component code</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param namespaceCode the namespace code
     * @param componentCode the component code
     * @param parameterName the parameter name
     * @return string value or null
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameterValueAsString(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    String getParameterValueAsString(String namespaceCode, String componentCode, String parameterName);

        /**
     * Retrieves a parameter's string value.  If the parameter is not found the default value will be returned.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from the passed in namespace code</li>
     *       <li>component code: from the passed in component code</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param namespaceCode the namespace code
     * @param componentCode the component code
     * @param parameterName the parameter name
     * @param defaultValue the value to return is the parameter does not exist.  Can be any string value including null
     * @return string value or null
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameterValueAsString(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    String getParameterValueAsString(String namespaceCode, String componentCode, String parameterName, String defaultValue);

    /**
     * Retrieves a parameter's boolean value.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from a {@link ParameterConstants.NAMESPACE}
     *          annotation on the componentClass</li>
     *       <li>component code: from a {@link ParameterConstants.COMPONENT}
     *         annotation on the componentClass</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param componentClass the class with the namespace & component annotations
     * @param parameterName the parameter name
     * @return true, false, null
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameterValueAsBoolean(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    Boolean getParameterValueAsBoolean(Class<?> componentClass, String parameterName);

    /**
     * Retrieves a parameter's boolean value.  If the parameter is not found the default value will be returned.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from a {@link ParameterConstants.NAMESPACE}
     *          annotation on the componentClass</li>
     *       <li>component code: from a {@link ParameterConstants.COMPONENT}
     *         annotation on the componentClass</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param componentClass the class with the namespace & component annotations
     * @param parameterName the parameter name
     * @param defaultValue the value to return is the parameter does not exist.  Can be any Boolean value including null
     * @return true, false, or the defaultValue
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameterValueAsBoolean(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    Boolean getParameterValueAsBoolean(Class<?> componentClass, String parameterName, Boolean defaultValue);

   /**
     * Retrieves a parameter's boolean value.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from the passed in namespace code</li>
     *       <li>component code: from the passed in component code</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param namespaceCode the namespace code
     * @param componentCode the component code
     * @param parameterName the parameter name
     * @return true, false, null
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameterValueAsBoolean(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    Boolean getParameterValueAsBoolean(String namespaceCode, String componentCode, String parameterName);

    /**
     * Retrieves a parameter's boolean value.  If the parameter is not found the default value will be returned.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from the passed in namespace code</li>
     *       <li>component code: from the passed in component code</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param namespaceCode the namespace code
     * @param componentCode the component code
     * @param parameterName the parameter name
     * @param defaultValue the value to return is the parameter does not exist.  Can be any Boolean value including null
     * @return true, false, or the defaultValue
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameterValueAsBoolean(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    Boolean getParameterValueAsBoolean(String namespaceCode, String componentCode, String parameterName, Boolean defaultValue);

    /**
     * Retrieves a parameter.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from a {@link ParameterConstants.NAMESPACE}
     *          annotation on the componentClass</li>
     *       <li>component code: from a {@link ParameterConstants.COMPONENT}
     *         annotation on the componentClass</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param componentClass the class with the namespace & component annotations
     * @param parameterName the parameter name
     * @return true or false
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameter(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    Parameter getParameter(Class<?> componentClass, String parameterName);

    /**
     * Retrieves a parameter.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from the passed in namespace code</li>
     *       <li>component code: from the passed in component code</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param namespaceCode the namespace code
     * @param componentCode the component code
     * @param parameterName the parameter name
     * @return true or false
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameter(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    Parameter getParameter(String namespaceCode, String componentCode, String parameterName);

    /**
     * Retrieves a parameter's string values where a parameter contains 0 or more values.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from a {@link ParameterConstants.NAMESPACE}
     *          annotation on the componentClass</li>
     *       <li>component code: from a {@link ParameterConstants.COMPONENT}
     *         annotation on the componentClass</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param componentClass the class with the namespace & component annotations
     * @param parameterName the parameter name
     * @return string values or empty Collection
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameterValuesAsString(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    Collection<String> getParameterValuesAsString(Class<?> componentClass, String parameterName);

    /**
     * Retrieves a parameter's string values where a parameter contains 0 or more values.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from a {@link ParameterConstants.NAMESPACE}
     *          annotation on the componentClass</li>
     *       <li>component code: from a {@link ParameterConstants.COMPONENT}
     *         annotation on the componentClass</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param namespaceCode the namespace code
     * @param componentCode the component code
     * @param parameterName the parameter name
     * @return string values or empty Collection
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getParameterValuesAsString(org.kuali.rice.coreservice.api.parameter.ParameterKey)} for details
     */
    Collection<String> getParameterValuesAsString(String namespaceCode, String componentCode, String parameterName);

    /**
     * Retrieves a subParameter's string value.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from a {@link ParameterConstants.NAMESPACE}
     *          annotation on the componentClass</li>
     *       <li>component code: from a {@link ParameterConstants.COMPONENT}
     *         annotation on the componentClass</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param componentClass the class with the namespace & component annotations
     * @param parameterName the parameter name
     * @return string value or null
     * @param subParameterName the subParameter name
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getSubParameterValueAsString(org.kuali.rice.coreservice.api.parameter.ParameterKey, String)} for details
     */
    String getSubParameterValueAsString(Class<?> componentClass, String parameterName, String subParameterName);

    /**
     * Retrieves a subParameter's string value.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from the passed in namespace code</li>
     *       <li>component code: from the passed in component code</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param namespaceCode the namespace code
     * @param componentCode the component code
     * @param parameterName the parameter name
     * @param subParameterName the subParameter name
     * @return string value or null
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getSubParameterValueAsString(org.kuali.rice.coreservice.api.parameter.ParameterKey, String)} for details
     */
    String getSubParameterValueAsString(String namespaceCode, String componentCode, String parameterName, String subParameterName);

    /**
     * Retrieves a subParameter's string values where a subParameter contains 0 or more values.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from a {@link ParameterConstants.NAMESPACE}
     *          annotation on the componentClass</li>
     *       <li>component code: from a {@link ParameterConstants.COMPONENT}
     *         annotation on the componentClass</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param componentClass the class with the namespace & component annotations
     * @param parameterName the parameter name
     * @param subParameterName the subParameter name
     * @return string values or empty Collection
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getSubParameterValuesAsString(org.kuali.rice.coreservice.api.parameter.ParameterKey, String)} for details
     */
    Collection<String> getSubParameterValuesAsString(Class<?> componentClass, String parameterName, String subParameterName);

    /**
     * Retrieves a subParameter's string values where a subParameter contains 0 or more values.
     *
     * <p>
     *     The parameter key is constructed from the following:
     *     <ul>
     *       <li>namespace code: from a {@link ParameterConstants.NAMESPACE}
     *          annotation on the componentClass</li>
     *       <li>component code: from a {@link ParameterConstants.COMPONENT}
     *         annotation on the componentClass</li>
     *       <li>parameter name: from the passed in parameter name</li>
     *       <li>application id: from the client configuration of the service implementation</li>
     *     </ul>
     * </p>
     *
     * @param namespaceCode the namespace code
     * @param componentCode the component code
     * @param parameterName the parameter name
     * @param subParameterName the subParameter name
     * @return string values or empty Collection
     * @throws IllegalArgumentException if any arguments are null
     * @throws IllegalStateException if the application id is not configured correctly
     * @see {@link org.kuali.rice.coreservice.api.parameter.ParameterRepositoryService#getSubParameterValuesAsString(org.kuali.rice.coreservice.api.parameter.ParameterKey, String)} for details
     */
    Collection<String> getSubParameterValuesAsString(String namespaceCode, String componentCode, String parameterName, String subParameterName);
}
