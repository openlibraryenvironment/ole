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
package org.kuali.rice.kim.framework.responsibility;


import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.framework.type.KimTypeService;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Map;

/**
 * A {@link KimTypeService} with specific methods for Responsibilities.
 */
@WebService(name = "responsibilityTypeService", targetNamespace = KimConstants.Namespaces.KIM_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ResponsibilityTypeService extends KimTypeService {

    /** Gets whether a responsibility assignment with the given details is applicable for the given request details.
     *
     * For example, the details for a responsibilities (say approve) could be as follows:
     *   component = Account
     *   field = incomeStreamAccountNumber
     *
     * The Account component is known to belong to the KFS-COA namespace.  If this service is requested...
     * component = Account, field = All
     *
     * @param requestedDetails the requested details.  cannot be null.
     * @param responsibilities the list of responsibilities to check for matches. cannot be null.
     * @return an immutable list of matched responsibilities.  will not return null.
     * @throws IllegalArgumentException if the requestedDetails or responsibilities is null.
     */
    @WebMethod(operationName="getMatchingResponsibilities")
    @XmlElementWrapper(name = "responsibilities", required = true)
    @XmlElement(name = "responsibility", required = false)
    @WebResult(name = "responsibilities")
    List<Responsibility> getMatchingResponsibilities(@WebParam(name = "requestedDetails")
                                            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                            Map<String, String> requestedDetails,
                                            @WebParam(name = "responsibilities")
                                            List<Responsibility> responsibilities) throws RiceIllegalArgumentException;
}
