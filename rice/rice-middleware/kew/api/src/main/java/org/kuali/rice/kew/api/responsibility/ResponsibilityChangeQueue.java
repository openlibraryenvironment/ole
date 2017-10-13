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
package org.kuali.rice.kew.api.responsibility;

import org.kuali.rice.kew.api.KewApiConstants;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Set;

@WebService(name = "responsibilityChangeQueue", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ResponsibilityChangeQueue {

    /**
     * Notifies the workflow system that the given set of responsibility ids were updated in such a way that might
     * affect routing.  Implementation of this method should re-resolve enroute documents as neccessary in this case.
     *
     * @param responsibilityIds the set of ids of responsibilities which have been modified, if this is a null or
     * empty set then this method will do nothing
     */
    @WebMethod(operationName = "responsibilitiesChanged")
    void responsibilitiesChanged(@WebParam(name = "responsibilityIds") Set<String> responsibilityIds);
   
}
