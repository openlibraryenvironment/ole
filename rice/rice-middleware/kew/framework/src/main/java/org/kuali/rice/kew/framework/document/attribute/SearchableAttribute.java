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
package org.kuali.rice.kew.framework.document.attribute;

import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.document.DocumentWithContent;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * Allows for definition of custom attributes on a document that should be indexed along with that document and made
 * searchable when performing document searches.  Applications who want to index and expose these custom attributes on
 * their documents can implement this interface and configure their document type to point to the searchable attributes
 * that it should use.
 *
 * <p>A searchable attribute provides the following basic functions:</p>
 *
 * <ul>
 *     <li>The ability to generate XML content that can be associated with the document and used for indexing purposes.</li>
 *     <li>The ability to extract attribute values from the document and supply them to the indexer for indexing.</li>
 *     <li>The ability to define how custom search attributes will be presented in the document search user interface.</li>
 *     <li>The ability to define validation for custom search attribute criteria which is executed from the document search user interface.</li>
 * </ul>
 *
 * <p>Searchable attributes are mapped to document types via the KEW extension framework (see
 * {@link org.kuali.rice.kew.api.extension.ExtensionRepositoryService}).
 *
 * <p>Through this extension mechanism, searchable attributes are designed to allow for re-use if desired.  To
 * facilitate this, the name of the document type for which the operation is being performed is included for all such
 * methods which might make use of it.  Additionally, all of the operations on a searchable attribute are passed the
 * {@link ExtensionDefinition} which was used to define the instance of the searchable attribute and link it to the
 * document type.  The extension definition can be defined to include additional configuration which can be used by the
 * various methods on the searchable attribute implementation.  This allows for creating a single
 * {@code SearchableAttribute} implementation which can then be parameterized externally by reusing the implementation
 * in the extension repository, but parameterizing it via one ore more extension definitions.</p>
 *
 * <p>This interface is annotated to allow for it to be exposed as a JAXWS web service, so client applications
 * wanting to publish their own searchable attribute implementations may do so by publishing their search attribute
 * implementations on the bus.  However, this is optional as it is possible to declare an extension definition for a searchable
 * attribute which uses the class name instead of a service name.  In these cases, searchable attribute implementations
 * will be located and invoked via an application's
 * {@link org.kuali.rice.kew.framework.document.search.DocumentSearchCustomizationHandlerService} endpoint assuming that
 * the proper application id is associated with the extension definition.</p>
 *
 * @see org.kuali.rice.kew.framework.document.search.DocumentSearchCustomizationHandlerService
 * @see org.kuali.rice.kew.api.extension.ExtensionRepositoryService
 * @see ExtensionDefinition
 * @see WorkflowAttributeDefinition
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "searchableAttributeService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface SearchableAttribute {

    /**
     * Allows for generation of custom XML for this searchable attribute.  The trigger for invocation of custom XML
     * generation happens via the workflow API whenever a document action is taken and a request is submitted to update
     * document XML based on searchable attribute definitions (see
     * {@link org.kuali.rice.kew.api.document.DocumentContentUpdate}).  This XML is ultimately included as part of the
     * document's content.
     *
     * <p>It is intended that this XML can be used by the {@code extractDocumentAttributes} method in order to pull
     * values out for indexing, though this method is free to use any source available to it for extracting data for
     * indexing alongside a document.</p>
     *
     * <p>A null or blank value may be returned from this method to indicate that no XML was generated.</p>
     *
     * @param extensionDefinition the extension definition which was used to locate and load this searchable attribute
     * implementation
     * @param documentTypeName the name of the document type for which this method is being invoked
     * @param attributeDefinition contains parameters and properties that can be used to inform generation of the XML,
     * these are supplied by the user of the workflow API when the document's searchable XML content is requested to be
     * updated
     * 
     * @return a String containing valid XML that should be included in the searchable attribute XML section of the
     * document's XML content
     */
    @WebMethod(operationName = "generateSearchContent")
    @WebResult(name = "searchContent")
    public String generateSearchContent(
            @WebParam(name = "extensionDefinition") ExtensionDefinition extensionDefinition,
            @WebParam(name = "documentTypeName") String documentTypeName,
            @WebParam(name = "attributeDefinition") WorkflowAttributeDefinition attributeDefinition
    );

    /**
     * Extracts and returns document attributes for the given document in order to allow indexing of those values for
     * association with the document and use in document searches.  The document and it's XML content is passed to this
     * method as that is a common source of data for indexing purposes, though implementations are free to pull data for
     * indexing from any readily accessible source.
     *
     * <p>There are a finite set of {@link DocumentAttribute} implementations which can be returned and interpreted
     * correctly.  Client application's should <strong>not</strong> create custom extensions of the
     * {@code DocumentAttribute} abstract class but should preferably use the
     * {@link org.kuali.rice.kew.api.document.attribute.DocumentAttributeFactory} to construct strongly-typed document
     * attribute instances for indexing.</p>
     *
     * @param extensionDefinition the extension definition which was used to locate and load this searchable attribute
     * implementation
     * @param documentWithContent the workflow document and it's XML content
     * 
     * @return a list of document attribute values that should be indexed for the given document, or a null or empty
     * list if no attributes should be indexed
     *
     * @see org.kuali.rice.kew.api.document.attribute.DocumentAttributeFactory
     */
    @WebMethod(operationName = "extractDocumentAttributes")
    @WebResult(name = "documentAttributes")
    @XmlElementWrapper(name = "documentAttributes", required = false)
    @XmlElement(name = "documentAttribute", required = false)
    public List<DocumentAttribute> extractDocumentAttributes(
            @WebParam(name = "extensionDefinition") ExtensionDefinition extensionDefinition,
            @WebParam(name = "documentWithContent") DocumentWithContent documentWithContent);

    /**
     * Returns a list of {@link RemotableAttributeField} objects which define which searchable attribute criteria fields
     * should be included in the criteria section of the document search user interface for this searchable attribute.
     *
     * @param extensionDefinition the extension definition which was used to locate and load this searchable attribute
     * implementation
     * @param documentTypeName the name of the document type for which this method is being invoked
     *
     * @return a list of remotable attribute fields which define the search fields that should be included in the
     * document search criteria, or a null or empty list if no criteria should be included for this searchable attribute
     */
    @WebMethod(operationName = "getSearchFields")
    @WebResult(name = "searchFields")
    @XmlElementWrapper(name = "searchFields", required = false)
    @XmlElement(name = "searchField", required = false)
    public List<RemotableAttributeField> getSearchFields(
            @WebParam(name = "extensionDefinition") ExtensionDefinition extensionDefinition,
            @WebParam(name = "documentTypeName") String documentTypeName
    );

    /**
     * Performs custom validation of document attribute values that come from this searchable attribute whenever a
     * document search is performed against a document type which uses this searchable attribute.  This hook allows for
     * any desired validation of this searchable attributes custom document attribute values to be performed prior to
     * the execution of the document search.
     *
     * <p>The entire {@link org.kuali.rice.kew.api.document.search.DocumentSearchCriteria} is passed to this method, though it's intended that implementing
     * code will pull out the document attribute values on the criteria which are managed by this searchable attribute
     * and perform any desired validation.  However, there are certainly no restrictions on this method that would
     * prevent it from performing validations outside of this scope and in relation to other portions of the criteria,
     * though this is certainly not the intent of this validation hook.</p>
     *
     * <p>Note that this method is invoked when performing a document search from the user interface as well as via
     * the {@link org.kuali.rice.kew.api.document.WorkflowDocumentService} api.</p>
     *
     * @param extensionDefinition the extension definition which was used to locate and load this searchable attribute
     * implementation
     * @param documentSearchCriteria the criteria that was submitted to the document search and against which validation
     * is requested
     *
     * @return a list of attribute errors containing and validation failure errors messages for the relevant document
     * attributes, if this returns a null or empty list it means that validation was successful
     */
    @WebMethod(operationName = "validateSearchParameters")
    @WebResult(name = "validationErrors")
    @XmlElementWrapper(name = "validationErrors", required = false)
    @XmlElement(name = "validationError", required = false)
    public List<RemotableAttributeError> validateDocumentAttributeCriteria(
            @WebParam(name = "extensionDefinition") ExtensionDefinition extensionDefinition,
            @WebParam(name = "documentSearchCriteria") DocumentSearchCriteria documentSearchCriteria
    );

}
