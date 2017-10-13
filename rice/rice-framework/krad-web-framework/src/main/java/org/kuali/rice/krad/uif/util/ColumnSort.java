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
package org.kuali.rice.krad.uif.util;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Holds the information for sorting a table by a column.
 *
 * <ul>
 *     <li>column index</li>
 *     <li>direction</li>
 *     <li>sort type</li>
 * </ul>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ColumnSort {

    /**
     * Sort direction, either ASCending or DESCending.
     */
    public enum Direction { ASC, DESC };

    private final int columnIndex;
    private final Direction direction;
    private final String sortType;

    /**
     * Constructs a ColumnSort instance.
     *
     * @param columnIndex the index of the column to sort on
     * @param direction the direction of the sort
     * @param sortType the type of the sort -- see {@link org.kuali.rice.krad.uif.UifConstants.TableToolsValues}.
     */
    public ColumnSort(int columnIndex, Direction direction, String sortType) {
        this.columnIndex = columnIndex;
        this.direction = direction;
        this.sortType = sortType;
    }

    /**
     * Get the column index.
     *
     * @return the column index
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * Get the sort direction.
     *
     * @return the sort direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Get the sort type.
     *
     * @return the sort type
     */
    public String getSortType() {
        return sortType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}