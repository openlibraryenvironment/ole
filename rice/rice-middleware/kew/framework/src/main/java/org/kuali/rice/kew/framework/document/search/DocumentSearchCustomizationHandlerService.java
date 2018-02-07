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
package org.kuali.rice.kew.framework.document.search;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.framework.KewFrameworkServiceLocator;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;
import java.util.Set;

/**
 * A remotable service which handles processing of a client application's document search customizations.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = KewFrameworkServiceLocator.DOCUMENT_SEARCH_CUSTOMIZATION_HANDLER_SERVICE, targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface DocumentSearchCustomizationHandlerService {

    /**
     * Retrieves the custom {@code DocumentSearchCriteriaConfiguration} to use for the document type with the given name
     * and for the given list of searchable attributes.  This method is invoked by the document search implementation in
     * order to help assemble the final criteria attribute fields (which includes configuration for all searchable
     * attributes on the document type).
     *
     * <p>The given list of searchable attribute names may not necessary include all searchable attribute on the
     * document type, only those which need to be handled by the client application hosting this service.  This
     * determination is made based on the applicationId which is associated with the searchable attribute.
     * Implementations of this method will assemble this information by invoking the
     * {@link org.kuali.rice.kew.framework.document.attribute.SearchableAttribute#getSearchFields(org.kuali.rice.kew.api.extension.ExtensionDefinition, String)}
     * methods on each of the requested searchable attributes.</p>
     *
     * @param documentTypeName the document type name for which to retrieve the configuration
     * @param searchableAttributeNames the names of the searchable attributes from which to assemble criteria
     * configuration which are owned by the application hosting this service
     *
     * @return the custom document search criteria configuration for the given searchable attribute, or null if no
     * custom configuration is needed
     * 
     * @throws RiceIllegalArgumentException if documentTypeName is a null or blank value
     */
    @WebMethod(operationName = "getDocumentSearchConfiguration")
	@WebResult(name = "documentSearchConfiguration")
	@XmlElement(name = "documentSearchConfiguration", required = false)
    DocumentSearchCriteriaConfiguration getDocumentSearchConfiguration(
            @WebParam(name = "documentTypeName") String documentTypeName,
            @WebParam(name = "searchableAttributeNames") List<String> searchableAttributeNames) throws RiceIllegalArgumentException;

    /**
     * Executes validation of the given {@code DocumentSearchCriteria} against the searchable attributes with the given
     * names..  This method is invoked by the document search implementation in order to allow for validation to be
     * customized via custom searchable attribute implementations.
     *
     * <p>The given list of searchable attribute names may not necessary include all searchable attribute on the
     * document type, only those which need to be handled by the client application hosting this service.  This
     * determination is made based on the applicationId which is associated with the searchable attribute.
     * Implementations of this method execute this validationby invoking the
     * {@link org.kuali.rice.kew.framework.document.attribute.SearchableAttribute#validateDocumentAttributeCriteria(org.kuali.rice.kew.api.extension.ExtensionDefinition, org.kuali.rice.kew.api.document.search.DocumentSearchCriteria)}
     * methods on each of the requested searchable attributes.</p>
     *
     * @param documentSearchCriteria the criteria against which to perform the validation
     * @param searchableAttributeNames the names of the searchable attributes against which to execute validation which
     * are owned by the application hosting this service
     *
     * @return a list or remotable attribute errors in the case that any validation errors were raised by the
     * requested searchable attributes
     *
     * @throws RiceIllegalArgumentException if documentTypeName is a null or blank value
     */
    @WebMethod(operationName = "validateCriteria")
    @WebResult(name = "errors")
    @XmlElementWrapper(name = "errors", required = true)
    @XmlElement(name = "errors", required = false)
    List<RemotableAttributeError> validateCriteria(@WebParam(name = "documentSearchCriteria") DocumentSearchCriteria documentSearchCriteria,
            @WebParam(name = "searchableAttributeNames") List<String> searchableAttributeNames
    ) throws RiceIllegalArgumentException;

    /**
     * Executes criteria customization against the given criteria using the {@link DocumentSearchCustomizer} with the
     * given customizer name.  This name is the name of the {@code ExtensionDefinition} that defines the customizer
     * where the customizer extension's applicationId is the same as the application hosting this service.
     *
     * <p>This method effectively invokes the {@link DocumentSearchCustomizer#customizeCriteria(org.kuali.rice.kew.api.document.search.DocumentSearchCriteria)}
     * on the requested customizer which is owned by this application.
     *
     * @param documentSearchCriteria the criteria to customize
     * @param customizerName the name of the extension definition for the {@code DocumentSearchCustomizer} which should
     * be used in order to execute the customization
     *
     * @return the customized criteria, or null if no customization was performed
     *
     * @throws RiceIllegalArgumentException if documentSearchCriteria is null
     * @throws RiceIllegalArgumentException if customizerName is a null or blank value
     */
    @WebMethod(operationName = "customizeCriteria")
    @WebResult(name = "documentSearchCriteria")
    @XmlElement(name = "documentSearchCriteria", required = false)
    DocumentSearchCriteria customizeCriteria(
            @WebParam(name = "documentSearchCriteria") DocumentSearchCriteria documentSearchCriteria,
            @WebParam(name = "customizerName") String customizerName
    ) throws RiceIllegalArgumentException;

    /**
     * Executes custom criteria clearing against the given criteria using the {@link DocumentSearchCustomizer} with the
     * given customizer name.  This name is the name of the {@code ExtensionDefinition} that defines the customizer
     * where the customizer extension's applicationId is the same as the application hosting this service.
     *
     * <p>This method effectively invokes the {@link DocumentSearchCustomizer#customizeClearCriteria(org.kuali.rice.kew.api.document.search.DocumentSearchCriteria)}
     * on the requested customizer which is owned by this application.
     *
     * @param documentSearchCriteria the criteria on which to perform custom clearing
     * @param customizerName the name of the extension definition for the {@code DocumentSearchCustomizer} which should
     * be used in order to execute the customization
     *
     * @return the cleared criteria, or null if no custom clear was performed
     *
     * @throws RiceIllegalArgumentException if documentSearchCriteria is null
     * @throws RiceIllegalArgumentException if customizerName is a null or blank value
     */
    @WebMethod(operationName = "customizeClearCriteria")
    @WebResult(name = "documentSearchCriteria")
    @XmlElement(name = "documentSearchCriteria", required = false)
    DocumentSearchCriteria customizeClearCriteria(
            @WebParam(name = "documentSearchCriteria") DocumentSearchCriteria documentSearchCriteria,
            @WebParam(name = "customizerName") String customizerName
    ) throws RiceIllegalArgumentException;

    /**
     * Executes customization of document search results using the {@link DocumentSearchCustomizer} with the
     * given customizer name.  This name is the name of the {@code ExtensionDefinition} that defines the customizer
     * where the customizer extension's applicationId is the same as the application hosting this service.
     *
     * <p>This method effectively invokes the {@link DocumentSearchCustomizer#customizeResults(org.kuali.rice.kew.api.document.search.DocumentSearchCriteria, java.util.List)}
     * on the requested customizer which is owned by this application.
     *
     * @param documentSearchCriteria the criteria that was used to perform the lookup
     * @param results the results that were returned from the lookup
     * @param customizerName the name of the extension definition for the {@code DocumentSearchCustomizer} which should
     * be used in order to execute the customization
     *
     * @return the customized document search results values, or null if no customization was performed
     *
     * @throws RiceIllegalArgumentException if documentSearchCriteria is null
     * @throws RiceIllegalArgumentException if results is null
     * @throws RiceIllegalArgumentException if customizerName is a null or blank value
     */
    @WebMethod(operationName = "customizeResults")
    @WebResult(name = "resultValues")
    @XmlElement(name = "resultValues", required = false)
    DocumentSearchResultValues customizeResults(
            @WebParam(name = "documentSearchCriteria") DocumentSearchCriteria documentSearchCriteria,
            @WebParam(name = "results") List<DocumentSearchResult> results,
            @WebParam(name = "customizerName") String customizerName
    ) throws RiceIllegalArgumentException;

    /**
     * Executes customization of document search result set configuration using the {@link DocumentSearchCustomizer}
     * with the given customizer name.  This name is the name of the {@code ExtensionDefinition} that defines the
     * customizer where the customizer extension's applicationId is the same as the application hosting this service.
     *
     * <p>This method effectively invokes the {@link DocumentSearchCustomizer#customizeResultSetConfiguration(org.kuali.rice.kew.api.document.search.DocumentSearchCriteria)}
     * on the requested customizer which is owned by this application.
     *
     * @param documentSearchCriteria the criteria that was used to perform the lookup
     * @param customizerName the name of the extension definition for the {@code DocumentSearchCustomizer} which should
     * be used in order to execute the customization
     *
     * @return the customized document search result configuration, or null if no customization was performed
     *
     * @throws RiceIllegalArgumentException if documentSearchCriteria is null
     * @throws RiceIllegalArgumentException if customizerName is a null or blank value
     */
    @WebMethod(operationName = "customizeResultSetConfiguration")
    @WebResult(name = "resultSetConfiguration")
    @XmlElement(name = "resultSetConfiguration", required = false)
    DocumentSearchResultSetConfiguration customizeResultSetConfiguration(
            @WebParam(name = "documentSearchCriteria") DocumentSearchCriteria documentSearchCriteria,
            @WebParam(name = "customizerName") String customizerName) throws RiceIllegalArgumentException;

    /**
     * Returns the set of customizations that are enabled and should be executed for the {@link DocumentSearchCustomizer}
     * with the given customizer name.  This name is the name of the {@code ExtensionDefinition} that defines the
     * customizer where the customizer extension's applicationId is the same as the application hosting this service.
     *
     * <p>This method essentially invokes the various boolean methods on the {@code DocumentSearchCustomizer} which
     * indicate which customizations the implementation provides.  This primarily serves as a means of optimization to
     * reduce the number of remote callbacks that the document search implementation needs to make to the various
     * customizations provided by this service and the customizers it delegates too.</p>
     *
     * @param documentTypeName the name of the document type against which to check for enabled customizations on the
     * specified customizer
     * @param customizerName the name of the extension definition for the {@code DocumentSearchCustomizer} which should
     * be used in order to check for enabled customizations
     *
     * @return the set of customizations that are enabled
     * 
     * @throws RiceIllegalArgumentException if documentTypeName is a null or blank value
     * @throws RiceIllegalArgumentException if customizerName is a null or blank value
     */
    @WebMethod(operationName = "getEnabledCustomizations")
    @WebResult(name = "enabledCustomizations")
    @XmlElementWrapper(name = "enabledCustomizations", required = true)
    @XmlElement(name = "enabledCustomization", required = false)
    Set<DocumentSearchCustomization> getEnabledCustomizations(
            @WebParam(name = "documentTypeName") String documentTypeName,
            @WebParam(name = "customizerName") String customizerName
    ) throws RiceIllegalArgumentException;

}
