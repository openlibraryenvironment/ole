/**
 * Copyright 2005-2012 The Kuali Foundation
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
package org.kuali.ole;

/**
 * Kuali Rice ArcheType Help
 * <p/>
 * This is an example of a service interface which contains several implementations.
 * <p/>
 * A service interface that calculates the product of two numbers.
 */
public interface ProductService {

    /**
     * Calculates the product of two {@link Integer Integers} yielding an {@link Integer}.  Will not check for overflow.
     *
     * @param left  an {@link Integer}. cannot be null.
     * @param right an {@link Integer}. cannot be null.
     * @return the product of the two passed in {@link Integer Integers}.  Will never return null.
     * @throws IllegalArgumentException if either passed in {@link Integer Integers} are null.
     */
    Integer product(Integer left, Integer right);
}