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
package org.kuali.rice.kim.framework.permission;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.Permission;
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
 * A {@link KimTypeService} with specific methods for Permissions.
 */
@WebService(name = "permissionTypeService", targetNamespace = KimConstants.Namespaces.KIM_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PermissionTypeService extends KimTypeService {

    /**
     * Gets whether a permission assignment with the given details is applicable for the given request details.
     * 
     * For example, the details for a permission (say edit) could be as follows:
     *   component = Account
     *   field = incomeStreamAccountNumber
     *   
     * The Account component is known to belong to the KFS-COA namespace.  If this service is requested...
     * component = Account, field = All  
     *
     * @param requestedDetails the requested details.  cannot be null.
     * @param permissions the list of permission to check for matches. cannot be null.
     * @return an immutable list of matched permissions.  will not return null.
     * @throws IllegalArgumentException if the requestedDetails or permissions is null.
     */
    @WebMethod(operationName="getMatchingPermissions")
    @XmlElementWrapper(name = "permissions", required = true)
    @XmlElement(name = "permission", required = false)
    @WebResult(name = "permissions")
    List<Permission> getMatchingPermissions(@WebParam(name = "requestedDetails")
                                            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                            Map<String, String> requestedDetails,
                                            @WebParam(name = "permissions")
                                            List<Permission> permissions) throws RiceIllegalArgumentException;

}
