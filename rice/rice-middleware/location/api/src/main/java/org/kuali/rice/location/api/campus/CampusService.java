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
package org.kuali.rice.location.api.campus;

import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
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
 * <p>CampusService interface.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "campusService", targetNamespace = LocationConstants.Namespaces.LOCATION_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface CampusService {

    /**
     * This will return a {@link Campus}.
     *
     * @param code the code of the campus to return
     * @throws RiceIllegalArgumentException if the code is null or blank
     */
    @WebMethod(operationName="getCampus")
    @WebResult(name = "campus")
    @Cacheable(value=Campus.Cache.NAME, key="'code=' + #p0")
    Campus getCampus(@WebParam(name = "code") String code) throws RiceIllegalArgumentException;
    
    /**
     * This will return all {@link Campus}.
     */
    @WebMethod(operationName="findAllCampuses")
    @XmlElementWrapper(name = "campuses", required = false)
    @XmlElement(name = "campus", required = false)
    @WebResult(name = "campuses")
    @Cacheable(value=Campus.Cache.NAME, key="'all'")
    List<Campus> findAllCampuses();
    
    /**
     * This will return a {@link CampusType}.
     *
     * @param code the code of the campus type to return
     * @return CampusType object represented by the passed in code
     * @throws RiceIllegalArgumentException if the code is null
     *
     */
    @WebMethod(operationName="getCampusType")
    @WebResult(name = "campusType")
    @Cacheable(value=CampusType.Cache.NAME, key="'code=' + #p0")
    CampusType getCampusType(@WebParam(name = "code") String code) throws RiceIllegalArgumentException;
    
    /**
     * This will return all {@link CampusType}.
     */
    @WebMethod(operationName="findAllCampusTypes")
    @XmlElementWrapper(name = "campusTypes", required = false)
    @XmlElement(name = "campusType", required = false)
    @WebResult(name = "campusTypes")
    @Cacheable(value=CampusType.Cache.NAME, key="'all'")
    List<CampusType> findAllCampusTypes();

    /**
     * This method find Campuses based on a query criteria.  The criteria cannot be null.
     *
     * @since 2.0.1
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return query results.  will never return null.
     * @throws IllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findCampuses")
    @WebResult(name = "results")
    CampusQueryResults findCampuses(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;

    /**
     * This method find CampusTypes based on a query criteria.  The criteria cannot be null.
     *
     * @since 2.0.1
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return query results.  will never return null.
     * @throws IllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findCampusTypes")
    @WebResult(name = "results")
    CampusTypeQueryResults findCampusTypes(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;
}
