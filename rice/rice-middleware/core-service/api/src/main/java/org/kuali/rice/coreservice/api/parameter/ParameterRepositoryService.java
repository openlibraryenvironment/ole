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

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Collection;

/**
 * Service for interacting with {@link Parameter Parameters}.
 */
@WebService(name = "parameterService", targetNamespace = CoreConstants.Namespaces.CORE_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ParameterRepositoryService {

    /**
     * This will create a {@link Parameter} exactly like the parameter passed in.
     *
     * @param parameter the parameter to create
     * @return the parameter that was created
     * @throws RiceIllegalArgumentException if the parameter is null
     * @throws RiceIllegalStateException if the parameter is already existing in the system
     */
    @WebMethod(operationName="createParameter")
    @WebResult(name = "parameter")
    @CacheEvict(value={Parameter.Cache.NAME}, allEntries = true)
    Parameter createParameter(@WebParam(name = "parameter") Parameter parameter)
            throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * This will update a {@link Parameter}.
     *
     *  <p>
     *     If the parameter does not exist under the application
     *     code passed, then this method will check if the parameter
     *     exists under the default rice application id and
     *     will update that parameter.
     * </p>
     *
     * @param parameter the parameter to update
     * @return the parameter that was updated
     * @throws RiceIllegalArgumentException if the parameter is null
     * @throws RiceIllegalStateException if the parameter does not exist in the system under the
     * specific application id or default rice application id
     */
    @WebMethod(operationName="updateParameter")
    @WebResult(name = "parameter")
    @CacheEvict(value={Parameter.Cache.NAME}, allEntries = true)

    Parameter updateParameter(@WebParam(name = "parameter") Parameter parameter)
            throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * Gets a {@link Parameter} from a {@link ParameterKey}.
     *
     * <p>
     *     If the parameter does not exist under the application
     *     code passed, then this method will check if the parameter
     *     exists under the default rice application id and
     *     will return that parameter.
     * </p>
     *
     * <p>
     *   This method will return null if the parameter does not exist.
     * </p>
     *
     * @param key the key to retrieve the parameter by. cannot be null.
     * @return a {@link Parameter} or null
     * @throws RiceIllegalArgumentException if the key is null
     */
    @WebMethod(operationName="getParameter")
    @WebResult(name = "parameter")
    @Cacheable(value= Parameter.Cache.NAME, key="'key=' + #p0.getCacheKey()")
    Parameter getParameter(@WebParam(name = "key") ParameterKey key) throws RiceIllegalArgumentException;

    /**
     * Gets a {@link ParameterContract#getValue()} from a {@link ParameterKey}.
     *
     *  <p>
     *     If the parameter does not exist under the application
     *     code passed, then this method will check if the parameter
     *     exists under the default rice application id and
     *     will return that parameter.
     * </p>
     *
     * <p>
     *   This method will return null if the parameter does not exist.
     * </p>
     *
     * @param key the key to retrieve the parameter by. cannot be null.
     * @return a string value or null
     * @throws RiceIllegalArgumentException if the key is null
     */
    @WebMethod(operationName="getParameterValueAsString")
    @WebResult(name = "value")
    @Cacheable(value= Parameter.Cache.NAME, key="'{getParameterValueAsString}' + 'key=' + #p0.getCacheKey()")
    String getParameterValueAsString(@WebParam(name = "key") ParameterKey key) throws RiceIllegalArgumentException;

    /**
     * Gets a {@link ParameterContract#getValue()} as a Boolean from a {@link ParameterKey}.
     *
     * <p>
     *     If the parameter does not exist under the application
     *     code passed, then this method will check if the parameter
     *     exists under the default rice application id and
     *     will return that parameter.
     * </p>
     *
     * <p>
     *   This method will return null if the parameter does not exist or is not a valid truth value.
     * </p>
     *
     * valid true values (case insensitive):
     * <ul>
     *     <li>Y</li>
     *     <li>true</li>
     *     <li>on</li>
     *     <li>1</li>
     *     <li>t</li>
     *     <li>enabled</li>
     * </ul>
     *
     * valid false values (case insensitive):
     * <ul>
     *     <li>N</li>
     *     <li>false</li>
     *     <li>off</li>
     *     <li>0</li>
     *     <li>f</li>
     *     <li>disabled</li>
     * </ul>
     *
     * @param key the key to retrieve the parameter by. cannot be null.
     * @return a boolean value or null
     * @throws RiceIllegalArgumentException if the key is null
     */
    @WebMethod(operationName="getParameterValueAsBoolean")
    @WebResult(name = "value")
    @Cacheable(value= Parameter.Cache.NAME, key="'{getParameterValueAsBoolean}' + 'key=' + #p0.getCacheKey()")
    Boolean getParameterValueAsBoolean(@WebParam(name = "key") ParameterKey key) throws RiceIllegalArgumentException;

    /**
     * Gets a {@link ParameterContract#getValue()} from a {@link ParameterKey}
     * where the value is split on a semi-colon delimeter and each token is trimmed
     * of white space.
     *
     * for example:  param_name=foo; bar; baz
     *
     * will yield a collection containing foo, bar, baz
     *
     * <p>
     *     If the parameter does not exist under the application
     *     code passed, then this method will check if the parameter
     *     exists under the default rice application id and
     *     will return that parameter.
     * </p>
     *
     * <p>
     *   This method will always return an <b>immutable</b> Collection
     *   even when no values exist.
     * </p>
     *
     * @param key the key to retrieve the parameter by. cannot be null.
     * @return an immutable collection of strings
     * @throws RiceIllegalArgumentException if the key is null
     */
    @WebMethod(operationName="getParameterValuesAsString")
    @XmlElementWrapper(name = "values", required = true)
    @XmlElement(name = "value", required = false)
    @WebResult(name = "values")
    @Cacheable(value= Parameter.Cache.NAME, key="'{getParameterValuesAsString}' + 'key=' + #p0.getCacheKey()")
    Collection<String> getParameterValuesAsString(@WebParam(name = "key") ParameterKey key) throws RiceIllegalArgumentException;

    /**
     * Gets a {@link ParameterContract#getValue()} from a {@link ParameterKey}
     * where the value is split on a semi-colon delimeter and each token is trimmed
     * of white space.  Those values are themselves keyvalue pairs which are searched
     * for the sub parameter name.
     *
     * for example:
     *
     * param_name=foo=f1; bar=b1; baz=z1
     * subParameterName=bar
     *
     * will yield b1
     *
     * <p>if multiple subparameters are contained in the parameter value the first one is returned</p>
     *
     * <p>
     *     If the parameter does not exist under the application
     *     code passed, then this method will check if the parameter
     *     exists under the default rice application id and
     *     will return that parameter.
     * </p>
     *
     * <p>
     *   This method will always return null when the subparameter does not
     *   exist or if the parameter value does not conform to the following format(s):
     *   <ol>
     *      <li>subparameter_name=subparameter_value;</li>
     *   </ol>
     * </p>
     *
     * @param key the key to retrieve the parameter by. cannot be null.
     * @param subParameterName the sub parameter to search for
     * @return a string value or null
     * @throws RiceIllegalArgumentException if the key is null or if the subParameterName is blank
     */
    @WebMethod(operationName="getSubParameterValueAsString")
    @WebResult(name = "value")
    String getSubParameterValueAsString(@WebParam(name = "key") ParameterKey key,
                                        @WebParam(name = "subParameterName") String subParameterName)
            throws RiceIllegalArgumentException;

    /**
     * Gets a {@link ParameterContract#getValue()} from a {@link ParameterKey}
     * where the value is split on a semi-colon delimeter and each token is trimmed
     * of white space.  Those values are themselves keyvalue pairs which are searched
     * for the sub parameter name.  After the sub parameter is found it is split on a comma
     * and trimmed or whitespace before adding it to the final collection for return.
     *
     * for example:
     *
     * param_name=foo=f1,f2,f3; bar=b1,b2; baz=z1
     * subParameterName=bar
     *
     * will yield a collection containing b1, b2
     *
     * <p>if multiple subparameters are contained in the parameter value the first one is returned</p>
     *
     * <p>
     *     If the parameter does not exist under the application
     *     code passed, then this method will check if the parameter
     *     exists under the default rice application id and
     *     will return that parameter.
     * </p>
     *
     * <p>
     *   This method will always return an <b>immutable</b> Collection
     *   even when no values exist.
     * </p>
     *
     *  <p>
     *   This method will always return an empty <b>immutable</b> Collection when
     *   the subparameter does not exist or if the parameter value does not
     *   conform to the following format(s):
     *   <ol>
     *      <li>subparameter_name=subparameter_value;</li>
     *      <li>subparameter_name=subparameter_value1, subparameter_value2;</li>
     *      <li>subparameter_name=subparameter_value1, subparameter_value2,;</li>
     *   </ol>
     * </p>
     *
     * @param key the key to retrieve the parameter by. cannot be null.
     * @param subParameterName the sub parameter to search for
     * @return an immutable collection of strings
     * @throws RiceIllegalArgumentException if the key is null or if the subParameterName is blank
     */
    @WebMethod(operationName="getSubParameterValuesAsString")
    @XmlElementWrapper(name = "values", required = true)
    @XmlElement(name = "value", required = false)
    @WebResult(name = "values")
    Collection<String> getSubParameterValuesAsString(@WebParam(name = "key") ParameterKey key,
                                                     @WebParam(name = "subParameterName") String subParameterName)
            throws RiceIllegalArgumentException;
    
    @WebMethod(operationName="findParameters")
    @WebResult(name = "results")
    ParameterQueryResults findParameters(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;
}
