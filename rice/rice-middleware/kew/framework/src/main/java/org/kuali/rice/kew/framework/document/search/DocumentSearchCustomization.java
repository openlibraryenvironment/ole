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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * An enumeration which defines the set of available document search customizations.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = "documentSearchCustomization")
@XmlType(name = "DocumentSearchCustomizationType")
@XmlEnum
public enum DocumentSearchCustomization {

    /**
     * Customization of the document search criteria.
     */
    CRITERIA,

    /**
     * Customization of clearing of the document search criteria.
     */
    CLEAR_CRITERIA,

    /**
     * Customization of document search results.
     */
    RESULTS,

    /**
     * Customization of the document search result attribute fields.
     */
    RESULT_SET_FIELDS;

}
