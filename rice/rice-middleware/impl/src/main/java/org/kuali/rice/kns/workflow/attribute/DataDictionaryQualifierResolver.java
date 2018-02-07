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
package org.kuali.rice.kns.workflow.attribute;

import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.datadictionary.RoutingTypeDefinition;
import org.kuali.rice.krad.datadictionary.WorkflowAttributes;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QualifierResolver which uses Data Dictionary defined workflow attributes to gather a collection
 * of qualifiers to use to determine the responsibility for a document at a given workflow route node.
 * 
 * WorkflowAttributes can be defined in the data dictionary like so (this has been abbreviated):
 * 
 * <!-- Exported Workflow Attributes -->
 *   <bean id="DisbursementVoucherDocument-workflowAttributes" parent="DisbursementVoucherDocument-workflowAttributes-parentBean"/>
 *
 *   <bean id="DisbursementVoucherDocument-workflowAttributes-parentBean" abstract="true" parent="WorkflowAttributes">
 *       <property name="routingTypeDefinitions">
 *           <map>
 *               <!-- no qualifiers for purchasing node -->
 *               <entry key="Account" value-ref="RoutingType-AccountingDocument-Account-sourceOnly"/>
 *               <entry key="AccountingOrganizationHierarchy" value-ref="RoutingType-AccountingDocument-OrganizationHierarchy-sourceOnly"/>
 *               <entry key="Campus" value-ref="DisbursementVoucherDocument-RoutingType-Campus"/>
 *               <!-- no qualifiers for tax review -->
 *               <!-- no qualifiers for travel review -->
 *               <entry key="PaymentMethod" value-ref="DisbursementVoucherDocument-RoutingType-PaymentMethod"/>
 *               <entry key="Award" value-ref="RoutingType-AccountingDocument-Award"/>
 *           </map>
 *       </property>
 *   </bean>
 * 
 *   <bean id="DisbursementVoucherDocument-RoutingType-PaymentMethod" class="org.kuali.rice.krad.datadictionary.RoutingTypeDefinition">
 *       <property name="routingAttributes">
 *           <list>
 *               <bean class="org.kuali.rice.krad.datadictionary.RoutingAttribute">
 *                   <property name="qualificationAttributeName" value="disbVchrPaymentMethodCode"/>
 *               </bean>
 *           </list>
 *       </property>
 *       <property name="documentValuePathGroups">
 *           <list>
 *               <bean class="org.kuali.rice.krad.datadictionary.DocumentValuePathGroup">
 *                   <property name="documentValues">
 *                       <list>
 *                           <value>disbVchrPaymentMethodCode</value>
 *                       </list>
 *                   </property>
 *               </bean>
 *           </list>
 *       </property>
 *   </bean> 
 * 
 * At the PaymentMethod node of the document, the DisbursementVoucherDocument-RoutingType-PaymentMethod RoutingTypeDefinition will be
 * consulted; it will pull values from the document (in this case, document.disbVchrPaymentMethodCode) and populate those
 * into the role qualifier Map<String, String>, with the key being the qualificationAttributeName and the value being the value of the property
 * listed in the documentValuePathGroups in the document.
 */
public class DataDictionaryQualifierResolver extends QualifierResolverBase {
//    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DataDictionaryQualifierResolver.class);
    

    /**
     * Given the RouteContext, determines the document type of the document being routed and the current
     * route nodes; generates a List of qualifier Map<String, String>s based on the the contents of the document.
     * @see org.kuali.rice.kew.role.QualifierResolver#resolve(org.kuali.rice.kew.engine.RouteContext)
     */
    public List<Map<String, String>> resolve(RouteContext context) {
        final String routeLevel = context.getNodeInstance().getName();
        final DocumentEntry documentEntry = getDocumentEntry(context);
        final RoutingTypeDefinition routingTypeDefinition = getWorkflowAttributeDefintion(documentEntry, routeLevel);
        final Document document = getDocument(context);
        List<Map<String, String>> qualifiers = null;
        
        if (document != null && routingTypeDefinition != null) {
            qualifiers = KNSServiceLocator.getWorkflowAttributePropertyResolutionService().resolveRoutingTypeQualifiers(document, routingTypeDefinition);
        } else {
            qualifiers = new ArrayList<Map<String, String>>();
            Map<String, String> basicQualifier = new HashMap<String, String>();
            qualifiers.add(basicQualifier);
        }
        decorateWithCommonQualifiers(qualifiers, document, documentEntry, routeLevel);
        return qualifiers;
    }

    /**
     * Retrieves the data dictionary entry for the document being operated on by the given route context
     * @param context the current route context
     * @return the data dictionary document entry
     */
    protected DocumentEntry getDocumentEntry(RouteContext context) {
        return KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry(context.getDocument().getDocumentType().getName());
    }

    /**
     * Retrieves the proper List of WorkflowAttributes for the given route level from the data dictionary
     * document entry
     * @param documentEntry the data dictionary document entry for the currently routed document
     * @param routeLevelName the name of the route level
     * @return a WorkflowAttributeDefinition if one could be found for the route level; otherwise, nothing
     */
    protected RoutingTypeDefinition getWorkflowAttributeDefintion(DocumentEntry documentEntry, String routeLevelName) {
       final WorkflowAttributes workflowAttributes = documentEntry.getWorkflowAttributes();
       if ( workflowAttributes == null ) {
           return null;
       }
       final Map<String, RoutingTypeDefinition> routingTypeMap = workflowAttributes.getRoutingTypeDefinitions();
       if (routingTypeMap.containsKey(routeLevelName)) return routingTypeMap.get(routeLevelName);
       return null;
    }
    
    /**
     * Add common qualifiers to every Map<String, String> in the given List of Map<String, String>
     * @param qualifiers a List of Map<String, String>s to add common qualifiers to
     * @param document the document currently being routed
     * @param documentEntry the data dictionary entry of the type of document currently being routed
     * @param routeLevel the document's current route level
     */
    protected void decorateWithCommonQualifiers(List<Map<String, String>> qualifiers, Document document, DocumentEntry documentEntry, String routeLevel) {
        for (Map<String, String> qualifier : qualifiers) {
            addCommonQualifiersToMap(qualifier, document, documentEntry, routeLevel);
        }
    }
    
    /**
     * Adds common qualifiers to a given Map<String, String>
     * @param qualifier an Map<String, String> to add common qualifiers to
     * @param document the document currently being routed
     * @param documentEntry the data dictionary entry of the type of document currently being routed
     * @param routeLevel the document's current route level
     */
    protected void addCommonQualifiersToMap(Map<String, String> qualifier, Document document, DocumentEntry documentEntry, String routeLevel) {
        if ( document != null ) {
            qualifier.put(KIM_ATTRIBUTE_DOCUMENT_NUMBER, document.getDocumentNumber());
        }
        if ( documentEntry != null ) {
            qualifier.put(KIM_ATTRIBUTE_DOCUMENT_TYPE_NAME, documentEntry.getDocumentTypeName());
        }
        qualifier.put(KIM_ATTRIBUTE_ROUTE_LEVEL_NAME, routeLevel);
    }
}
