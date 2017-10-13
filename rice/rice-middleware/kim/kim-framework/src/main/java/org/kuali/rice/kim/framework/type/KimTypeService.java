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
package org.kuali.rice.kim.framework.type;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.type.KimAttributeField;

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
 * This is the base service interface for handling type-specific behavior.  Types can be attached
 * to various objects (currently groups and roles) in KIM to add additional attributes and
 * modify their behavior.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "kimTypeService", targetNamespace = KimConstants.Namespaces.KIM_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface KimTypeService {

    /**
     * Gets the name of a workflow document type that should be passed to kew when resolving responsibilities for routing.
     *
     * This name will be passed as a qualifier with the "documentTypeName" key.
     * return null to indicate that there is no custom workflow document needed for this type.
     *
     * @return the doc type name or null.
     */
    @WebMethod(operationName="getWorkflowDocumentTypeName")
    @WebResult(name = "name")
    String getWorkflowDocumentTypeName();

    /**
     * Gets an unmodifiable list of attribute names identifying the attribute qualifiers that are provided to
     * the KIM responsibility service when resolving responsibility-based routing at the node with the given name.
     *
     * Returns an empty list, indicating that no attributes from this
     * type should be passed to workflow.
     *
     * @param nodeName the name of the node to retrieve attribute names for.  Cannot be null or blank.
     * @return an unmodifiable list should not return null.
     * @throws IllegalArgumentException if the nodeName is null or blank.
     */
    @WebMethod(operationName="getWorkflowRoutingAttributes")
    @XmlElementWrapper(name = "attributeNames", required = true)
    @XmlElement(name = "attributeName", required = false)
    @WebResult(name = "attributeNames")
    List<String> getWorkflowRoutingAttributes(@WebParam(name = "nodeName") String nodeName) throws RiceIllegalArgumentException;

    /**
     * Gets a List of {@link KimAttributeField} for a kim type id.  The order of the attribute fields in the list
     * can be used as a hint to a ui framework consuming these attributes as to how to organize these fields.
     *
     * @param kimTypeId the kimTypeId to retrieve fields for. Cannot be null or blank.
     * @return an immutable list of KimAttributeField. Will not return null.
     * @throws IllegalArgumentException if the kimTypeId is null or blank
     */
    @WebMethod(operationName="getAttributeDefinitions")
    @XmlElementWrapper(name = "kimAttributeFields", required = true)
    @XmlElement(name = "kimAttributeField", required = false)
    @WebResult(name = "kimAttributeFields")
    List<KimAttributeField> getAttributeDefinitions(@WebParam(name = "kimTypeId") String kimTypeId) throws RiceIllegalArgumentException;

    /**
     * This method validates the passed in attributes for a kimTypeId generating a List of {@link RemotableAttributeError}.
     *
     * The order of the attribute errors in the list
     * can be used as a hint to a ui framework consuming these errors as to how to organize these errors.
     *
     * @param kimTypeId the kimTypeId that is associated with the attributes. Cannot be null or blank.
     * @param attributes the kim type attributes to validate. Cannot be null.
     * @return an immutable list of RemotableAttributeError. Will not return null.
     * @throws IllegalArgumentException if the kimTypeId is null or blank or the attributes are null
     */
    @WebMethod(operationName="validateAttributes")
    @XmlElementWrapper(name = "attributeErrors", required = true)
    @XmlElement(name = "attributeError", required = false)
    @WebResult(name = "attributeErrors")
    List<RemotableAttributeError> validateAttributes(@WebParam(name = "kimTypeId")
                                                     String kimTypeId,
                                                     @WebParam(name = "attributes")
                                                     @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                     Map<String, String> attributes) throws RiceIllegalArgumentException;

    /**
     * This method validates the passed in attributes for a kimTypeId generating a List of {@link RemotableAttributeError}.
     * This method used the oldAttributes to aid in validation.  This is useful for validating "new" or "updated" attributes.
     *
     * The order of the attribute errors in the list
     * can be used as a hint to a ui framework consuming these errors as to how to organize these errors.
     *
     * @param kimTypeId the kimTypeId that is associated with the attributes. Cannot be null or blank.
     * @param newAttributes the kim type attributes to validate. Cannot be null.
     * @param oldAttributes the old kim type attributes to use for validation. Cannot be null.
     * @return an immutable list of RemotableAttributeError. Will not return null.
     * @throws IllegalArgumentException if the kimTypeId is null or blank or the newAttributes or oldAttributes are null
     */
    @WebMethod(operationName="validateAttributesAgainstExisting")
    @XmlElementWrapper(name = "attributeErrors", required = true)
    @XmlElement(name = "attributeError", required = false)
    @WebResult(name = "attributeErrors")
    List<RemotableAttributeError> validateAttributesAgainstExisting(@WebParam(name = "kimTypeId")
                                                                    String kimTypeId,
                                                                    @WebParam(name = "newAttributes")
                                                                    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                                    Map<String, String> newAttributes,
                                                                    @WebParam(name = "oldAttributes")
                                                                    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                                    Map<String, String> oldAttributes) throws RiceIllegalArgumentException;


    /**
     * This method validates the passed in attributes for a kimTypeId generating a List of {@link RemotableAttributeError}.
     * This method used the oldAttributes to aid in validation.  This method specifically determines if attributes should be
     * unique and verifying them against other attributes that have been set to determine uniqueness
     *
     * The order of the attribute errors in the list
     * can be used as a hint to a ui framework consuming these errors as to how to organize these errors.
     *
     * @param kimTypeId the kimTypeId that is associated with the attributes. Cannot be null or blank.
     * @param newAttributes the kim type attributes to validate. Cannot be null.
     * @param oldAttributes the old kim type attributes to use for validation. Cannot be null.
     * @return an immutable list of RemotableAttributeError. Will not return null.
     * @throws IllegalArgumentException if the kimTypeId is null or blank or the newAttributes or oldAttributes are null
     */
    @WebMethod(operationName="validateUniqueAttributes")
    @XmlElementWrapper(name = "attributeErrors", required = true)
    @XmlElement(name = "attributeError", required = false)
    @WebResult(name = "attributeErrors")
    List<RemotableAttributeError> validateUniqueAttributes(@WebParam(name = "kimTypeId")
                                                           String kimTypeId,
                                                           @WebParam(name = "newAttributes")
                                                           @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                           Map<String, String> newAttributes,
                                                           @WebParam(name = "oldAttributes")
                                                           @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                           Map<String, String> oldAttributes)
                                                    throws RiceIllegalArgumentException;

    /**
     * This method validates the passed in attributes for a kimTypeId generating a List of {@link RemotableAttributeError}.
     * This method used the oldAttributes to aid in validation.  This method specifically validates that the new attribute
     * values have not been changed if they are unmodifiable.
     *
     * The order of the attribute errors in the list
     * can be used as a hint to a ui framework consuming these errors as to how to organize these errors.
     *
     * @param kimTypeId the kimTypeId that is associated with the attributes. Cannot be null or blank.
     * @param newAttributes the kim type attributes to validate. Cannot be null.
     * @param originalAttributes the old kim type attributes to use for validation. Cannot be null.
     * @return an immutable list of RemotableAttributeError. Will not return null.
     * @throws IllegalArgumentException if the kimTypeId is null or blank or the newAttributes or oldAttributes are null
     */
    @WebMethod(operationName="validateUnmodifiableAttributes")
    @XmlElementWrapper(name = "attributeErrors", required = true)
    @XmlElement(name = "attributeError", required = false)
    @WebResult(name = "attributeErrors")
    List<RemotableAttributeError> validateUnmodifiableAttributes(@WebParam(name = "kimTypeId")
                                                                 String kimTypeId,
                                                                 @WebParam(name = "originalAttributes")
                                                                 @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                                 Map<String, String> originalAttributes,
                                                                 @WebParam(name = "newAttributes")
                                                                 @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                                 Map<String, String> newAttributes)
                                                     throws RiceIllegalArgumentException;
}

