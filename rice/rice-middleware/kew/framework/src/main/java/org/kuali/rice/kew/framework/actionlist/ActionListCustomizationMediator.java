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
package org.kuali.rice.kew.framework.actionlist;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.action.ActionItemCustomization;
import org.kuali.rice.kew.framework.KewFrameworkServiceLocator;

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
 * A remotable service which handles processing of a client application's custom processing of
 * action list attributes.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = KewFrameworkServiceLocator.ACTION_LIST_CUSTOMIZATION_HANDLER_SERVICE, targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ActionListCustomizationMediator {

    /**
     * Retrieves a Map from ActionItem id to ActionItemCustomization for the ActionItems
     * provided.  Any ActionItems that don't have customizations will not have entries in
     * the resulting map.
     *
     * @param principalId the id of the principal on whose behalf these customizations are being processed
     * @param actionItems the list of Action Items to get customizations for.
     * @return a Map from ActionItem id to ActionItemCustomization. Any ActionItems that don't have customizations will
     * not have entries in the resulting map.
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if the given principalId is a null or blank value
     */
    @WebMethod(operationName = "getActionListCustomizations")
    @WebResult(name = "actionListCustomizations")
    @XmlElement(name = "actionListCustomization", required = false)
    @XmlJavaTypeAdapter(MapStringActionItemCustomizationAdapter.class)
    Map<String, ActionItemCustomization> getActionListCustomizations(@WebParam(name = "principalId") String principalId,
            @WebParam(name = "actionItems") List<ActionItem> actionItems)
            throws RiceIllegalArgumentException;
}
