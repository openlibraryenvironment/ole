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
package org.kuali.rice.kew.api.preferences;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.kuali.rice.kew.api.KewApiConstants;

/**
 * A service which provides data access for {@link Preferences}.
 *
 * @see Preferences
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "preferencesService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PreferencesService {

    @WebMethod(operationName = "savePreferences")
    public void savePreferences(@WebParam(name="principalId") String principalId, @WebParam(name="preferences") Preferences actionListPreferences);

    @WebMethod(operationName = "getPreferences")
    @WebResult(name = "preferences")
    //@Cacheable(value= Preferences.Cache.NAME, key="'principalId=' + #principalId")
    public Preferences getPreferences(@WebParam(name="principalId") String principalId);
}
