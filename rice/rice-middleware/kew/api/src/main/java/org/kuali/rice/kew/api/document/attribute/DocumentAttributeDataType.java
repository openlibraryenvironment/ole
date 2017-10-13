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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Defines the valid data types for implementations of the {@link DocumentAttributeContract}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = "documentAttributeDataType")
@XmlType(name = "DocumentAttributeDataTypeType")
@XmlEnum
public enum DocumentAttributeDataType {

    /**
     * Indicates a document attribute which holds character data.
     */
    STRING,

    /**
     * Indicates a document attribute which holds date and (optional) time information.
     */
    DATE_TIME,

    /**
     * Indicates a document attribute which holds an integer value.
     */
    INTEGER,

    /**
     * Indicates a document attribute which holds a real number.
     */
    DECIMAL

}
