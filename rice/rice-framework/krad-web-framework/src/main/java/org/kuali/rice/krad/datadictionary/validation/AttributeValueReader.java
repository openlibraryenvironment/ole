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
package org.kuali.rice.krad.datadictionary.validation;

import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;

import java.util.List;

/**
 * AttributeValueReader defines classes that encapsulate access to both dictionary metadata and object field values
 *
 * <p>For example, by reflection
 * and introspection, for the purpose of performing validation against constraints defined in the
 * DictionaryValidationService implementation.</p>
 *
 * <p>Practically speaking, this interface should only need to be implemented by a small number of classes. The two
 * major use cases are for
 * <ol>
 * <li> a dictionary object with members</li>
 * <li> a specific member of a dictionary object</li>
 * </ol>
 * </p>
 * <p>
 * In the first case, implementing classes should provide access to all underlying members of the object via reflection
 * or some other mechanism.
 * In the second case, implementing classes only need to provide access to the value associated with that specific
 * member, and constraints
 * requiring access to additional members will be skipped.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 1.1
 */
public interface AttributeValueReader {

    /**
     * acts as an accessor for the attribute name that is currently being processed by the DictionaryValidationService
     * implementation
     *
     * @return the current attribute name being processed
     */
    public String getAttributeName();

    /**
     * provides access to the constrainable attribute definition of a specific attribute name
     *
     * <p>If the value of the metadata
     * associated with the object field does not implement constrainable, or if no metadata is associated with this
     * object
     * field,
     * then null should be returned.</p>
     *
     * @param attributeName - the name of the attribute/field whose metadata is being requested
     * @return dictionary metadata object implementing some constrainable capability
     */
    public Constrainable getDefinition(String attributeName);

    /**
     * gets a list of all constrainable dictionary metadata definitions for attributes or fields encapsulated by this
     * object
     *
     * @return a list of constrainable definitions
     */
    public List<Constrainable> getDefinitions();

    /**
     * gets the dictionary metadata associated with an object (its "entry" in the dictionary)
     *
     * <p>It can also be constrainable, in which case the object
     * value itself can be validated against one or more constraints. If the specific entry for the dictionary object
     * encapsulated by this
     * reader is not constrainable, or if no entry exists for this dictionary object, or no dictionary object is being
     * encapsulted, then
     * null should be returned.
     * </p>
     *
     * @return the constrainable dictionary entry metadata for this object, or null
     */
    public Constrainable getEntry();

    /**
     * gets the entry name for the purposes of correct error look up
     *
     * <p>Errors are generally found by entry name + attribute name + error key</p>
     *
     * @return the name that the data dictionary uses to store metadata about this object (not its attributes)
     */
    public String getEntryName();

    /**
     * looks up a label for a specific attribute name
     *
     * @param attributeName - the name of attribute
     * @return some descriptive label that can be exposed to the end user for error messages
     */
    public String getLabel(String attributeName);

    /**
     * gets the underlying object itself (not the field/attribute value, but the object)
     *
     * @return the object that is being encapsulated by this reader, or null if no object is being encapsulated
     */
    public Object getObject();

    /**
     * gets the path, which is a string representation of specifically which attribute (at some depth) is being
     * accessed
     *
     * <p>For example, on a person object there might be the following field path:
     * joe.home.mailingAddress.state</p>
     *
     * @return the string representation of the attribute identifier currently being processed
     */
    public String getPath();

    /**
     * gets the type of the attribute specified - A Java class
     *
     * @param attributeName - the name of attribute
     * @return the type of the attribute referenced by the passed name, or null if no attribute exists of that name
     */
    public Class<?> getType(String attributeName);

    /**
     * Indicates whether the configured attribute name is readable for the object
     *
     * @return boolean if attribute is readable, false if not
     */
    public boolean isReadable();

    /**
     * looks up the attribute value that is currently being processed
     *
     * @param <X> - the type of the attribute
     * @return the attribute's value if found, null if not
     * @throws AttributeValidationException
     */
    public <X> X getValue() throws AttributeValidationException;

    /**
     * looks up any attribute value by name for the object being processed
     *
     * @param <X> - the type of the attribute
     * @param attributeName - the name of attribute whose value is looked up
     * @return - the attribute's value if found, null if not
     * @throws AttributeValidationException
     */
    public <X> X getValue(String attributeName) throws AttributeValidationException;

    /**
     * enables legacy processing of string representations of attribute values like a date range in the format
     * 12/03/2001..1/29/2009
     *
     * @param attributeName - the attribute name
     * @return the list of token strings for the attribute value of the named attribute
     * @throws AttributeValidationException
     */
    public List<String> getCleanSearchableValues(String attributeName) throws AttributeValidationException;

    /**
     * Setter for the current attribute that is being processed
     *
     * @param attributeName
     */
    public void setAttributeName(String attributeName);

    /**
     * overrides {@link Object#clone()}
     *
     * @return a cloned {@code AttributeValueReader}
     */
    public AttributeValueReader clone();

}

