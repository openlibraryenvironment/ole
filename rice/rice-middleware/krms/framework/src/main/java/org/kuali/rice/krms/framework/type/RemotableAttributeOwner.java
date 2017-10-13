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
package org.kuali.rice.krms.framework.type;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Map;

/**
 * Interface to be extended by type services that have remotable attributes that will need to be rendered and
 * validated
 */

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL,
        parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)

public interface RemotableAttributeOwner {

    /**
     * <p>get the attributes supported by the type with the given krmsTypeId.</p>
     *
     * @param krmsTypeId the people flow type identifier.  Must not be null or blank.
     * @return the {@link RemotableAttributeField}s that the PeopleFlow type with the given id supports.
     * Will not return null.
     */
    @WebMethod(operationName="getAttributeFields")
    @XmlElementWrapper(name = "attributeFields", required = true)
    @XmlElement(name = "attributeField", required = false)
    @WebResult(name = "attributeFields")
    List<RemotableAttributeField> getAttributeFields( @WebParam(name = "krmsTypeId") String krmsTypeId )
            throws RiceIllegalArgumentException;

    /**
     * <p>This method validates the passed in attributes for a krmsTypeId generating a List of
     * {@link RemotableAttributeError}s.</p>
     *
     * @param krmsTypeId the people flow type identifier.  Must not be null or blank.
     * @param attributes the attributes to validate. Cannot be null.
     * @return any errors that are discovered during validation.  Will not return null.
     * @throws RiceIllegalArgumentException
     */
    @WebMethod(operationName="validateAttributes")
    @XmlElementWrapper(name = "attributeErrors", required = true)
    @XmlElement(name = "attributeError", required = false)
    @WebResult(name = "attributeErrors")
    List<RemotableAttributeError> validateAttributes(

            @WebParam(name = "krmsTypeId") String krmsTypeId,

            @WebParam(name = "attributes")
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
            Map<String, String> attributes

    )  throws RiceIllegalArgumentException;

    /**
     * <p>This method validates the passed in attributes for a krmsTypeId generating a List of
     * {@link RemotableAttributeError}s.  This method used the oldAttributes to aid in validation.  This is useful for
     * validating "new" or "updated" attributes.</p>
     *
     * @param krmsTypeId the people flow type identifier.  Must not be null or blank.
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

            @WebParam(name = "krmsTypeId") String krmsTypeId,

            @WebParam(name = "newAttributes")
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
            Map<String, String> newAttributes,

            @WebParam(name = "oldAttributes")
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
            Map<String, String> oldAttributes

    ) throws RiceIllegalArgumentException;

}
