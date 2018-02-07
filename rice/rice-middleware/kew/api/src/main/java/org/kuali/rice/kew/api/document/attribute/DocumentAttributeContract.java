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
package org.kuali.rice.kew.api.document.attribute;

/**
 * Defines the contract for an attribute of a document.  These attributes are generally extracted during an indexing
 * process and stored as key-value pairs associated with the document.  In addition to the key-value pair, the
 * document attribute also defines the data type of data that is held by the attribute.
 *
 * <p>This contract simply defines the interface that specific (and strongly typed) implementations of document
 * attributes will implement.  The number of possible implementations of this contract are generally constrainted by
 * the set of defined {@link DocumentAttributeDataType} enumeration values.</p>
 *
 * <p>Concrete instances of should be created using the {@link DocumentAttributeFactory}.  It is not generally
 * of value for a client of the API to create custom implementations of this contract interface.</p>
 *
 * @see DocumentAttributeFactory
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentAttributeContract {

    /**
     * Returns the name of this document attribute which serves as an identifier for this attribute on the document.  A
     * document may have more then one attribute with the same name, in which case it is treated as a multi-valued
     * attribute.  This method should never return a null or blank value.
     *
     * @return the name of the document attribute
     */
    String getName();

    /**
     * Returns the value of this document attribute.  It can be of any type as defined by the implementations of this
     * interface.  It is possible that this value may be null in cases where the document has a particular attribute
     * but no actual value associated with that attribute.
     *
     * @return the value of the document attribute
     */
    Object getValue();

    /**
     * Returns the data type of this document attribute.  This will generally inform the type of object returned from
     * the {@code #getValue} method.  This method should never return a null value.
     *
     * @return the data type of this document attribute
     */
    DocumentAttributeDataType getDataType();

}
