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
package org.kuali.rice.location.api.state;

import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.location.api.LocationConstants;
import org.springframework.cache.annotation.Cacheable;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * Service for interacting with {@link State States}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "StateService", targetNamespace = LocationConstants.Namespaces.LOCATION_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface StateService {

    /**
     * Gets a {@link State} from a postal country code and postal state code.
     * <p/>
     * <p>
     * This method will return null if the state does not exist.
     * </p>
     * <p/>
     * <p>
     * This method will return active or inactive states.
     * </p>
     *
     * @param countryCode country code. cannot be blank.
     * @param code        state code. cannot be blank.
     * @return a {@link State} or null
     * @throws RiceIllegalArgumentException country code or state code is blank
     */
    @WebMethod(operationName = "getState")
    @WebResult(name = "state")
    @Cacheable(value=State.Cache.NAME, key="'countryCode=' + #p0 + '|' + 'code=' + #p1")
    State getState(@WebParam(name = "countryCode") String countryCode, @WebParam(name = "code") String code)
            throws RiceIllegalArgumentException;

    /**
     * Finds all the {@link State States} for postal country code.
     * <p/>
     * <p>
     * This method will always return an <b>immutable</b> Collection
     * even when no values exist.
     * </p>
     * <p/>
     * <p>
     * This method will only return active states.
     * </p>
     *
     * @param countryCode state code. cannot be blank.
     * @return an immutable collection of states
     * @throws RiceIllegalArgumentException country code is blank
     */
    @WebMethod(operationName = "findAllStatesInCountry")
    @XmlElementWrapper(name = "states", required = true)
    @XmlElement(name = "state", required = false)
    @WebResult(name = "states")
    @Cacheable(value=State.Cache.NAME, key="'countryCode=' + #p0")
    List<State> findAllStatesInCountry(@WebParam(name = "countryCode") String countryCode)
            throws RiceIllegalArgumentException;
    
    /**
     * Finds all the {@link State States} for alternate postal country code.
     * <p/>
     * <p>
     * This method will always return an <b>immutable</b> Collection
     * even when no values exist.
     * </p>
     * <p/>
     * <p>
     * This method will only return active states.
     * </p>
     *
     * @param alternateCode cannot be blank.
     * @return an immutable collection of states
     * @throws RiceIllegalArgumentException alternate country code is null
     * @throws RiceIllegalStateException when no countries are found for alternate country code
     */
    @WebMethod(operationName = "findAllStatesInCountryByAltCode")
    @XmlElementWrapper(name = "states", required = true)
    @XmlElement(name = "state", required = false)
    @WebResult(name = "states")
    @Cacheable(value=State.Cache.NAME, key="'alternateCode=' + #p0")
    List<State> findAllStatesInCountryByAltCode(@WebParam(name = "alternateCode") String alternateCode)
            throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * This method find States based on a query criteria.  The criteria cannot be null.
     *
     * @since 2.0.1
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return query results.  will never return null.
     * @throws IllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findStates")
    @WebResult(name = "results")
    StateQueryResults findStates(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;
}
