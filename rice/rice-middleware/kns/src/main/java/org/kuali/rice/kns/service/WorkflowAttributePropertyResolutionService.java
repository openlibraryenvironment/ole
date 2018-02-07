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
package org.kuali.rice.kns.service;

import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.RoutingTypeDefinition;
import org.kuali.rice.krad.datadictionary.WorkflowAttributes;
import org.kuali.rice.krad.document.Document;

import java.util.List;
import java.util.Map;

/**
 * A service which will resolve workflow attributes into the proper data for routing qualifier resolution
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface WorkflowAttributePropertyResolutionService {

    /** 
     * Generates a List of Map<String, String> data from the data on the document for the given WorkflowAttributeDefinitions
     * @param document the document to gather data from
     * @param routingTypeDefinition 
     * @return a List of populated Map<String, String> data
     */
    public abstract List<Map<String, String>> resolveRoutingTypeQualifiers(Document document, RoutingTypeDefinition routingTypeDefinition);
    
    /**
     * Given a document, returns the searchable attribute values to index for it
     * @param document the document to find indexable searchable attribute values for
     * @param workflowAttributes the WorkflowAttributes data dictionary metadata which contains the searchable attributes to index 
     * @return a List of SearchableAttributeValue objects for index
     */
    public abstract List<DocumentAttribute> resolveSearchableAttributeValues(Document document, WorkflowAttributes workflowAttributes);
    
    /**
     * Retrieves an object, the child of another given object passed in as a parameter, by the given path
     * @param object an object to find a child object of
     * @param path the path to that child object
     * @return the child object
     */
    public abstract Object getPropertyByPath(Object object, String path);
    
    /**
     * Determines the type of the field which is related to the given attribute name on instances of the given business object class
     * @param businessObjectClass the class of the business object which has an attribute
     * @param attributeName the name of the attribute
     * @return the String constrant representing what Field#fieldDataType this represents
     */
    public abstract String determineFieldDataType(Class<? extends BusinessObject> businessObjectClass, String attributeName);
    
    /**
     * Using the type of the sent in value, determines what kind of SearchableAttributeValue implementation should be passed back 
     * @param attributeKey
     * @param value
     * @return
     */
    public DocumentAttribute buildSearchableAttribute(Class<? extends BusinessObject> businessObjectClass, String attributeKey, Object value);
    
}
