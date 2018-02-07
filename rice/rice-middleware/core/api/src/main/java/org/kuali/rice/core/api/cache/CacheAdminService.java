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
package org.kuali.rice.core.api.cache;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import java.util.Collection;

/**
 * A service which facilitates remote operations against a cache which is deployed using Kuali Rice's core caching
 * infrastructure.  The only operation currently supported by this service allows for flushing of cache entries based on
 * a specified collection of {@link CacheTarget} objects.  These cache targets specify information about which cache
 * entries should be flushed.
 *
 * <p>This service exists primarily to support client-side caching of data provided by remote services.  It allows the
 * host of the service to notify the client application about flush events, which typically result whenever changes
 * have been made to data such that it would be stale if cached within the client.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 2.0
 */
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface CacheAdminService {

    /**
     * Flushes an object or group of objects from the cache based on a cache target.  If the given collection of cache
     * targets is empty or null, this method will do nothing.
     *
     * @param cacheTargets a collection of targets to flush
     * @throws RiceIllegalArgumentException if {@code cacheTargets} contains any null items.
     */
    @WebMethod(operationName = "flush")
    void flush(@WebParam(name = "cacheTargets") Collection<CacheTarget> cacheTargets) throws RiceIllegalArgumentException;

}
