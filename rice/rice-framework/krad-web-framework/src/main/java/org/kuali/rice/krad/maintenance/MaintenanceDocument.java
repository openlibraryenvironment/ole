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
package org.kuali.rice.krad.maintenance;

import org.kuali.rice.krad.document.Document;

/**
 * Common interface for all maintenance documents.
 */
public interface MaintenanceDocument extends Document {

    /**
     * Get the XML representation of the maintenance document
     *
     * @return String containing the xml representation of the maintenance document
     */
    String getXmlDocumentContents();

    /**
     * Get the new maintainable object
     *
     * @return Maintainable which holds the new maintenance record
     */
    Maintainable getNewMaintainableObject();

    /**
     * Get the old maintainable object
     *
     * @return Maintainable which holds the old maintenance record
     */
    Maintainable getOldMaintainableObject();

    /**
     * Sets the xml contents of the maintenance document
     *
     * @param documentContents String xml
     */
    void setXmlDocumentContents(String documentContents);

    /**
     * Set the new maintainable object
     *
     * @param newMaintainableObject maintainable with the new maintenance record
     */
    void setNewMaintainableObject(Maintainable newMaintainableObject);

    /**
     * Set the old maintainable object
     *
     * @param oldMaintainableObject maintainable with the old maintenance record
     */
    void setOldMaintainableObject(Maintainable oldMaintainableObject);

    /**
     * Return the data object that this MaintenanceDocument is maintaining
     *
     * @return document data object instance
     */
    Object getDocumentDataObject();

    /**
     * Build the xml document string from the contents of the old and new maintainables.
     */
    void populateXmlDocumentContentsFromMaintainables();

    /**
     * Populates the old and new maintainables from the xml document contents string.
     */
    void populateMaintainablesFromXmlDocumentContents();

    /**
     * Check if maintenance document has old maintenance data

     * @return true if this maintenance document has old data, false otherwise
     */
    boolean isOldDataObjectInDocument();

    /**
     * Check if maintenance document is creating a new Business Object
     *
     * @return true if this maintenance document is creating a new Business Object, false otherwise
     */
    boolean isNew();

    /**
     * Check if maintenance document is editing an existing Business Object
     *
     * @return true if this maintenance document is editing an existing Business Object, false otherwise
     */
    boolean isEdit();

    /**
     * Check if maintenance document is creating a new Business Object out of an existing Business Object
     *
     * <p>
     * For example, a new division vendor out of an existing parent vendor.
     * </p>
     *
     * @return true if maintenance document is creating a new Business Object out of an existing Business object, false otherwise
     */
    boolean isNewWithExisting();

    /**
     * Check if fields are cleared on copy
     *
     * <p>
     * For copy action the primary keys need to be cleared.  This flag indicates if the clearing has occurred.
     * </p>
     *
     * @return true if the primary keys have been cleared already, false otherwise
     */
    boolean isFieldsClearedOnCopy();

    /**
     * Set the keys cleared on copy flag
     *
     * @param keysClearedOnCopy
     *
     */
    void setFieldsClearedOnCopy(boolean keysClearedOnCopy);

    /**
     * Check if the topic field should be displayed in the notes section
     *
     * @return true if the topic field should be displayed in the notes section, false otherwise
     */
    boolean isDisplayTopicFieldInNotes();

    /**
     * Set the display topic field in notes flag
     *
     * @parm displayTopicFieldInNotes
     */
    void setDisplayTopicFieldInNotes(boolean displayTopicFieldInNotes);

}
