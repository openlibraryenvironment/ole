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
package org.kuali.rice.core.api.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * This class contains a static list of search operators used in rice.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public enum SearchOperator {
    AND("&&"),
    OR("|"),
    NOT("!"),
    BETWEEN(".."),
    BETWEEN_EXCLUSIVE_LOWER(">.."),
    BETWEEN_EXCLUSIVE_UPPER("..."),
    BETWEEN_EXCLUSIVE_UPPER2("..<"),
    BETWEEN_EXCLUSIVE(">..<"),
    NULL("NULL"),
    NOT_NULL("!NULL"),
    LIKE_ONE("?"),
    LIKE_MANY("*"),
    LIKE_MANY_P("%"),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    EQUAL("="),
    GREATER_THAN_EQUAL(">="),
    LESS_THAN_EQUAL("<=");

    private final String op;
    private SearchOperator(String op) {
        this.op = op;
    }

    public static final Collection<SearchOperator> QUERY_CHARACTERS =
        Collections.unmodifiableCollection(Arrays.asList(LIKE_MANY, LIKE_ONE, LIKE_MANY_P, GREATER_THAN, LESS_THAN, BETWEEN, BETWEEN_EXCLUSIVE_UPPER, OR, NOT, EQUAL));

    public static final Collection<SearchOperator> RANGE_CHARACTERS =
        Collections.unmodifiableCollection(Arrays.asList(GREATER_THAN_EQUAL, LESS_THAN_EQUAL, GREATER_THAN, LESS_THAN, BETWEEN, BETWEEN_EXCLUSIVE_LOWER,
                                                         BETWEEN_EXCLUSIVE_UPPER, BETWEEN_EXCLUSIVE_UPPER2, BETWEEN_EXCLUSIVE));

    public String op() {
        return op;
    }

    @Override
    public String toString() {
        return op;
    }
}
