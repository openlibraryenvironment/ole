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
package org.kuali.rice.kew.framework.peopleflow;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContent;

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
 * <p>Interface for services that implement the type-specific behaviors of people flows.</p>
 */
@WebService(name = "PeopleFlowTypeService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PeopleFlowTypeService {

    /**
     * <p>Allows for the people flow to restrict the roles that can be selected when adding members to it.</p>
     *
     * @param kewTypeId the people flow type identifier.  Must not be null or blank.
     * @param roleIds the set of ids to filter down.  Must not be null.
     * @return the roleIds from the above list that are valid to select.  Will not return null.
     */
    @WebMethod(operationName="filterToSelectableRoleIds")
    @WebResult(name = "selectableRoleIds")
    @XmlElementWrapper(name = "selectableRoleIds", required = false)
    @XmlElement(name = "selectableRoleId", required = false)
    List<String> filterToSelectableRoleIds(
            @WebParam(name = "kewTypeId") String kewTypeId,
            @WebParam(name = "roleIds") List<String> roleIds
    );

    /**
     * <p>Resolve any role qualifiers for the given roleId, and document (along with documentContent).</p>
     *
     * @param kewTypeId the people flow type identifier.  Must not be null or blank.
     * @param roleId the role that the qualifiers are specific to.  Must not be null or blank.
     * @param document the document that the qualifiers are being resolved against.  Must not be null.
     * @param documentContent the contents for the document that the qualifiers are being resolved against.
     * Must not be null.
     * @return the resolved role qualifiers.  Will not return null.
     */
    @WebMethod(operationName="resolveRoleQualifiers")
    @WebResult(name = "roleQualifiers")
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    Map<String,String> resolveRoleQualifiers(
            @WebParam(name = "kewTypeId") String kewTypeId,
            @WebParam(name = "roleId") String roleId,
            @WebParam(name = "document") Document document,
            @WebParam(name = "documentContent") DocumentContent documentContent
    );

    /**
     * <p>Resolve any role qualifiers for the given roleId, and document (along with documentContent). Allows for
     * more than one set of qualifiers to be returned for the purpose of matching.</p>
     *
     * @param kewTypeId the people flow type identifier.  Must not be null or blank.
     * @param roleId the role that the qualifiers are specific to.  Must not be null or blank.
     * @param document the document that the qualifiers are being resolved against.  Must not be null.
     * @param documentContent the contents for the document that the qualifiers are being resolved against.
     * Must not be null.
     * @return a list of the resolved role qualifiers.  Will not return null.
     */
    @WebMethod(operationName="resolveMultipleRoleQualifiers")
    @WebResult(name = "roleQualifierList")
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    List<Map<String, String>> resolveMultipleRoleQualifiers(
            @WebParam(name = "kewTypeId") String kewTypeId,
            @WebParam(name = "roleId") String roleId,
            @WebParam(name = "document") Document document,
            @WebParam(name = "documentContent") DocumentContent documentContent
    );

    /**
     * <p>get the attributes supported by the people flow type with the given kewTypeId.</p>
     *
     * @param kewTypeId the people flow type identifier.  Must not be null or blank.
     * @return the {@link RemotableAttributeField}s that the PeopleFlow type with the given id supports.
     * Will not return null.
     */
    @WebMethod(operationName="getAttributeFields")
    @WebResult(name = "attributeFields")
    @XmlElementWrapper(name = "attributeFields", required = false)
    @XmlElement(name = "attributeField", required = false)
    List<RemotableAttributeField> getAttributeFields( @WebParam(name = "kewTypeId") String kewTypeId );

    /**
     * <p>This method validates the passed in attributes for a kewTypeId generating a List of
     * {@link RemotableAttributeError}s.</p>
     *
     * @param kewTypeId the people flow type identifier.  Must not be null or blank.
     * @param attributes the attributes to validate. Cannot be null.
     * @return any errors that are discovered during validation.  Will not return null.
     * @throws RiceIllegalArgumentException
     */
    @WebMethod(operationName="validateAttributes")
    @XmlElementWrapper(name = "attributeErrors", required = true)
    @XmlElement(name = "attributeError", required = false)
    @WebResult(name = "attributeErrors")
    List<RemotableAttributeError> validateAttributes(

            @WebParam(name = "kewTypeId") String kewTypeId,

            @WebParam(name = "attributes")
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
            Map<String, String> attributes

    )  throws RiceIllegalArgumentException;

    /**
     * <p>This method validates the passed in attributes for a kewTypeId generating a List of
     * {@link RemotableAttributeError}s.  This method used the oldAttributes to aid in validation.  This is useful for
     * validating "new" or "updated" attributes.</p>
     *
     * @param kewTypeId the people flow type identifier.  Must not be null or blank.
     * @param newAttributes the kim type attributes to validate. Cannot be null.
     * @param oldAttributes the old attributes to use for validation. Cannot be null.
     * @return any errors that are discovered during validation.  Will not return null.
     * @throws RiceIllegalArgumentException
     */
    @WebMethod(operationName="validateAttributesAgainstExisting")
    @XmlElementWrapper(name = "attributeErrors", required = true)
    @XmlElement(name = "attributeError", required = false)
    @WebResult(name = "attributeErrors")
    List<RemotableAttributeError> validateAttributesAgainstExisting(

            @WebParam(name = "kewTypeId") String kewTypeId,

            @WebParam(name = "newAttributes")
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
            Map<String, String> newAttributes,

            @WebParam(name = "oldAttributes")
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
            Map<String, String> oldAttributes

    ) throws RiceIllegalArgumentException;

}
