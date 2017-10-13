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
package org.kuali.rice.krad.datadictionary;

import org.kuali.rice.krad.datadictionary.state.StateMapping;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;

import java.util.List;

/**
 * Defines methods common to all DataDictionaryDefinition types.
 *
 * DD: The highest level objects in the data dictionary are of
 * the following types:
 * BusinessObjectEntry
 * MaintenanceDocumentEntry
 * TransactionalDocumentEntry
 *
 * JSTL: The data dictionary is exposed as a Map which is accessed
 * by referring to the "DataDictionary" global constant.  This Map contains
 * the following kinds of entries keyed as indicated:
 * Business Object Entries -
 * Key = dataObjectClass name
 * Value = Map created by BusinessObjectEntryMapper
 * Maintenance Document entries -
 * Key = DocumentType name
 * Value = Map created by MaintenanceObjectEntryMapper
 * Transactional Document entries -
 * Key = DocumentType name
 * Value = Map created by TransactionalDocumentEntryMapper
 *
 * All elements are exposed to JSTL as Maps (where the element has a
 * unique key by which they can be retrieved), or Strings.  For collections
 * of elements having no unique key, the entry's position in the list
 * (0, 1, etc.) is used as its index.
 *
 * All Maps (except the top-level DataDictionary one) are guaranteed to
 * present their entries with an iteration order identical to the order
 * in which the elements were defined in XML.
 */
public interface DataDictionaryEntry extends DictionaryBean {
    /**
     * @return String used as a globally-unique key for this entry's jstl-exported version
     */
    public String getJstlKey();

    /**
     * Kicks off complete entry-wide validation which couldn't be done earlier.
     *
     * @throws org.kuali.rice.krad.datadictionary.exception.CompletionException if a problem arises during
     * validation-completion
     */
    public void completeValidation();

    /**
     * Validates that the data objects created from the Spring Beans are correct
     *
     * @param tracer - Record of object's location
     */
    public void completeValidation(ValidationTrace tracer);

    /**
     * @param attributeName
     * @return AttributeDefinition with the given name, or null if none with that name exists
     */
    public AttributeDefinition getAttributeDefinition(String attributeName);

    /**
     * Returns the full class name of the underlying object.
     */
    public String getFullClassName();

    /**
     * @return a Map containing all RelationshipDefinitions associated with this BusinessObjectEntry, indexed by
     *         relationshipName
     */
    public List<RelationshipDefinition> getRelationships();

    /**
     * StateMapping for this DataDictionaryEntry, this represents the states of this entry, their names, and where
     * to find the state information on the model
     *
     * @return StateMapping object
     */
    public StateMapping getStateMapping();

    /**
     * Set the StateMapping object which represents state information for this entry
     *
     * @param stateMapping StateMapping object
     */
    public void setStateMapping(StateMapping stateMapping);
}
