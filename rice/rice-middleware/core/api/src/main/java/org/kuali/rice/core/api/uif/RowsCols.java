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
package org.kuali.rice.core.api.uif;

/**
 * A Control with a vertical and horizontal size based on number of rows and columns.
 */
public interface RowsCols {

    /**
     * The rows value to make the control (The vertical size).  This field can be null. Cannot be less than 1.
     *
     * @return the rows value or null.
     */
    Integer getRows();

    /**
     * The cols value to make the control (The horizontal size).  This field can be null. Cannot be less than 1.
     *
     * @return the cols value or null.
     */
    Integer getCols();
}
