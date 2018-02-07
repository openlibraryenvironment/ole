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
package org.kuali.rice.core.api.mo;

/**
 * Complete model objects in rice override {@link #equals(Object)}, {@link #hashCode()},
 * in addition to what is defined in the ModelObjectBasic interface.
 *
 * An example of a "Complete" Model object are the immutable transfer object
 * that rice uses in it's service APIs.
 */
public interface ModelObjectComplete extends ModelObjectBasic {

    /**
     * All "Complete" model object's should adhere to the {@link #equals(Object)} contract.
     *
     * @param o to object to compare for equality
     * @return if equal
     */
    @Override
    boolean equals(Object o);

    /**
     * All "Complete" model object's should adhere to the {@link #hashCode()} contract.
     *
     * @return the hashCode value
     */
    @Override
    int hashCode();
}
